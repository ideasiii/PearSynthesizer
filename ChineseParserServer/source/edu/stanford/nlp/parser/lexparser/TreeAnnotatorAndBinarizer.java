/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTag;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TreeAnnotatorAndBinarizer implements TreeTransformer
/*     */ {
/*     */   private TreeFactory tf;
/*     */   private TreebankLanguagePack tlp;
/*     */   private TreeTransformer annotator;
/*     */   private TreeTransformer binarizer;
/*     */   private TreeTransformer postSplitter;
/*     */   private boolean forceCNF;
/*     */   private Counter<Tree> annotatedRuleCounts;
/*     */   private Counter<String> annotatedStateCounts;
/*     */   
/*     */   public TreeAnnotatorAndBinarizer(TreebankLangParserParams tlpParams, boolean forceCNF, boolean insideFactor, boolean doSubcategorization)
/*     */   {
/*  30 */     this(tlpParams.headFinder(), tlpParams.headFinder(), tlpParams, forceCNF, insideFactor, doSubcategorization);
/*     */   }
/*     */   
/*     */   public TreeAnnotatorAndBinarizer(HeadFinder annotationHF, HeadFinder binarizationHF, TreebankLangParserParams tlpParams, boolean forceCNF, boolean insideFactor, boolean doSubcategorization) {
/*  34 */     if (doSubcategorization) {
/*  35 */       this.annotator = new TreeAnnotator(annotationHF, tlpParams);
/*     */     } else {
/*  37 */       this.annotator = new TreeNullAnnotator(annotationHF);
/*     */     }
/*  39 */     this.binarizer = new TreeBinarizer(binarizationHF, tlpParams.treebankLanguagePack(), insideFactor, Train.markovFactor, Train.markovOrder, Train.compactGrammar() > 0, Train.compactGrammar() > 1, Train.HSEL_CUT, Train.markFinalStates);
/*  40 */     if (Train.selectivePostSplit) {
/*  41 */       this.postSplitter = new PostSplitter(tlpParams);
/*     */     }
/*  43 */     this.tf = new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.CategoryWordTagFactory());
/*  44 */     this.tlp = tlpParams.treebankLanguagePack();
/*  45 */     this.forceCNF = forceCNF;
/*  46 */     if (Train.printAnnotatedRuleCounts) {
/*  47 */       this.annotatedRuleCounts = new Counter();
/*     */     }
/*  49 */     if (Train.printAnnotatedStateCounts) {
/*  50 */       this.annotatedStateCounts = new Counter();
/*     */     }
/*     */   }
/*     */   
/*     */   public void dumpStats() {
/*  55 */     if (Train.selectivePostSplit) {
/*  56 */       ((PostSplitter)this.postSplitter).dumpStats();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDoSelectiveSplit(boolean doSelectiveSplit) {
/*  61 */     ((TreeBinarizer)this.binarizer).setDoSelectiveSplit(doSelectiveSplit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addRoot(Tree t)
/*     */   {
/*  72 */     if (t.isLeaf()) {
/*  73 */       System.err.println("Warning: tree is leaf: " + t);
/*  74 */       t = this.tf.newTreeNode(this.tlp.startSymbol(), java.util.Collections.singletonList(t));
/*     */     }
/*  76 */     t.setLabel(new CategoryWordTag(this.tlp.startSymbol(), ".$.", ".$$."));
/*  77 */     List<Tree> preTermChildList = new ArrayList();
/*  78 */     Tree boundaryTerm = this.tf.newLeaf(new edu.stanford.nlp.ling.StringLabel(".$."));
/*  79 */     preTermChildList.add(boundaryTerm);
/*  80 */     Tree boundaryPreTerm = this.tf.newTreeNode(new CategoryWordTag(".$$.", ".$.", ".$$."), preTermChildList);
/*  81 */     List<Tree> childList = t.getChildrenAsList();
/*  82 */     childList.add(boundaryPreTerm);
/*  83 */     t.setChildren(childList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree transformTree(Tree t)
/*     */   {
/*  93 */     if (Train.printTreeTransformations > 0) {
/*  94 */       Train.printTrainTree(null, "ORIGINAL TREE:", t);
/*     */     }
/*  96 */     Tree trTree = this.annotator.transformTree(t);
/*  97 */     if (Train.selectivePostSplit) {
/*  98 */       trTree = this.postSplitter.transformTree(trTree);
/*     */     }
/* 100 */     if (Train.printTreeTransformations > 0) {
/* 101 */       Train.printTrainTree(Train.printAnnotatedPW, "ANNOTATED TREE:", trTree);
/*     */     }
/* 103 */     if (Train.printAnnotatedRuleCounts) {
/* 104 */       Tree tr2 = trTree.deeperCopy(new LabeledScoredTreeFactory(), new edu.stanford.nlp.ling.StringLabelFactory());
/* 105 */       java.util.Set<Tree> localTrees = tr2.localTrees();
/* 106 */       for (Tree tr : localTrees) {
/* 107 */         this.annotatedRuleCounts.incrementCount(tr);
/*     */       }
/*     */     }
/* 110 */     if (Train.printAnnotatedStateCounts) {
/* 111 */       for (Tree subt : trTree) {
/* 112 */         if (!subt.isLeaf()) {
/* 113 */           this.annotatedStateCounts.incrementCount(subt.label().value());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 119 */     addRoot(trTree);
/*     */     
/* 121 */     Tree binarizedTree = this.binarizer.transformTree(trTree);
/* 122 */     if (Train.printTreeTransformations > 0) {
/* 123 */       Train.printTrainTree(Train.printBinarizedPW, "BINARIZED TREE:", binarizedTree);
/* 124 */       Train.printTreeTransformations -= 1;
/*     */     }
/* 126 */     if (this.forceCNF) {
/* 127 */       binarizedTree = new CNFTransformers.ToCNFTransformer().transformTree(binarizedTree);
/*     */     }
/*     */     
/*     */ 
/* 131 */     return binarizedTree;
/*     */   }
/*     */   
/*     */   public void printRuleCounts() {
/* 135 */     System.err.println();
/* 136 */     for (Tree t : this.annotatedRuleCounts.keySet()) {
/* 137 */       System.err.print(this.annotatedRuleCounts.getCount(t) + "\t" + t.label().value() + " -->");
/*     */       
/* 139 */       for (Tree dtr : t.getChildrenAsList()) {
/* 140 */         System.err.print(" ");
/* 141 */         System.err.print(dtr.label().value());
/*     */       }
/* 143 */       System.err.println();
/*     */     }
/*     */   }
/*     */   
/*     */   public void printStateCounts() {
/* 148 */     System.err.println();
/* 149 */     System.err.println("Annotated state counts");
/* 150 */     java.util.Set<String> keys = this.annotatedStateCounts.keySet();
/* 151 */     List<String> keyList = new ArrayList(keys);
/* 152 */     java.util.Collections.sort(keyList);
/* 153 */     for (String s : keyList) {
/* 154 */       System.err.println(s + "\t" + this.annotatedStateCounts.getCount(s));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int numSubArgs(String[] args, int index)
/*     */   {
/* 164 */     int i = index;
/* 165 */     while ((i + 1 < args.length) && (args[(i + 1)].charAt(0) != '-')) {
/* 166 */       i++;
/*     */     }
/* 168 */     return i - index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 177 */     Options op = new Options();
/* 178 */     String treebankPath = null;
/* 179 */     java.io.FileFilter trainFilter = null;
/*     */     
/* 181 */     int i = 0;
/* 182 */     while ((i < args.length) && (args[i].startsWith("-"))) {
/* 183 */       if (args[i].equalsIgnoreCase("-train")) {
/* 184 */         int numSubArgs = numSubArgs(args, i);
/* 185 */         i++;
/* 186 */         if (numSubArgs >= 1) {
/* 187 */           treebankPath = args[i];
/* 188 */           i++;
/*     */         } else {
/* 190 */           throw new RuntimeException("Error: -train option must have treebankPath as first argument.");
/*     */         }
/* 192 */         if (numSubArgs == 2) {
/* 193 */           trainFilter = new edu.stanford.nlp.io.NumberRangesFileFilter(args[(i++)], true);
/* 194 */         } else if (numSubArgs >= 3) {
/* 195 */           int low = Integer.parseInt(args[i]);
/* 196 */           int high = Integer.parseInt(args[(i + 1)]);
/* 197 */           trainFilter = new edu.stanford.nlp.io.NumberRangeFileFilter(low, high, true);
/* 198 */           i += 2;
/*     */         }
/*     */       } else {
/* 201 */         i = op.setOption(args, i);
/*     */       }
/*     */     }
/* 204 */     if (i < args.length) {
/* 205 */       System.err.println("usage: java TreeAnnotatorAndBinarizer options*");
/* 206 */       System.err.println("  Options are like for lexicalized parser including -train treebankPath fileRange]");
/* 207 */       System.exit(0);
/*     */     }
/*     */     
/* 210 */     System.err.println("Annotating from treebank dir: " + treebankPath);
/* 211 */     Treebank trainTreebank = op.tlpParams.diskTreebank();
/* 212 */     if (trainFilter == null) {
/* 213 */       trainTreebank.loadPath(treebankPath);
/*     */     } else {
/* 215 */       trainTreebank.loadPath(treebankPath, trainFilter);
/*     */     }
/*     */     
/* 218 */     edu.stanford.nlp.util.Pair<List<Tree>, List<Tree>> pair = LexicalizedParser.getAnnotatedBinaryTreebankFromTreebank(trainTreebank, null, op);
/*     */     
/* 220 */     List<Tree> binaryTrainTreebank = (List)pair.first();
/*     */     
/* 222 */     java.util.Iterator<Tree> it = trainTreebank.iterator();
/* 223 */     for (Tree t : binaryTrainTreebank) {
/* 224 */       System.out.println("Original tree:");
/* 225 */       ((Tree)it.next()).pennPrint();
/* 226 */       System.out.println("Binarized tree:");
/* 227 */       t.pennPrint();
/* 228 */       System.out.println();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class TreeNullAnnotator
/*     */     implements TreeTransformer
/*     */   {
/* 241 */     private TreeFactory tf = new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.CategoryWordTagFactory());
/*     */     
/*     */     private HeadFinder hf;
/*     */     
/*     */     public Tree transformTree(Tree t)
/*     */     {
/* 247 */       Tree copy = t.deepCopy(this.tf);
/* 248 */       return transformTreeHelper(copy);
/*     */     }
/*     */     
/*     */     private Tree transformTreeHelper(Tree t) {
/* 252 */       if (t != null)
/*     */       {
/* 254 */         String cat = t.label().value();
/* 255 */         if (t.isLeaf()) {
/* 256 */           Label label = new edu.stanford.nlp.ling.StringLabel(cat);
/* 257 */           t.setLabel(label);
/*     */         } else {
/* 259 */           Tree[] kids = t.children();
/* 260 */           for (Tree child : kids) {
/* 261 */             transformTreeHelper(child);
/*     */           }
/* 263 */           Tree headChild = this.hf.determineHead(t);
/*     */           String tag;
/*     */           String word;
/* 266 */           String tag; if (headChild == null) {
/* 267 */             System.err.println("ERROR: null head for tree\n" + t.toString());
/* 268 */             String word = null;
/* 269 */             tag = null; } else { String word;
/* 270 */             if (headChild.isLeaf()) {
/* 271 */               String tag = cat;
/* 272 */               word = headChild.label().value();
/*     */             } else {
/* 274 */               CategoryWordTag headLabel = (CategoryWordTag)headChild.label();
/* 275 */               word = headLabel.word();
/* 276 */               tag = headLabel.tag();
/*     */             } }
/* 278 */           Label label = new CategoryWordTag(cat, word, tag);
/* 279 */           t.setLabel(label);
/*     */         }
/*     */       }
/* 282 */       return t;
/*     */     }
/*     */     
/*     */     public TreeNullAnnotator(HeadFinder hf) {
/* 286 */       this.hf = hf;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\TreeAnnotatorAndBinarizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */