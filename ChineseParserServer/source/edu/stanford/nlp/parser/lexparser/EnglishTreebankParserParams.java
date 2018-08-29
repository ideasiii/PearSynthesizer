/*      */ package edu.stanford.nlp.parser.lexparser;
/*      */ 
/*      */ import edu.stanford.nlp.ling.CategoryWordTag;
/*      */ import edu.stanford.nlp.ling.Label;
/*      */ import edu.stanford.nlp.trees.HeadFinder;
/*      */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*      */ import edu.stanford.nlp.trees.Tree;
/*      */ import edu.stanford.nlp.trees.TreeFactory;
/*      */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ 
/*      */ public class EnglishTreebankParserParams extends AbstractTreebankParserParams
/*      */ {
/*      */   private HeadFinder headFinder;
/*      */   
/*      */   protected class EnglishSubcategoryStripper implements edu.stanford.nlp.trees.TreeTransformer
/*      */   {
/*      */     protected EnglishSubcategoryStripper() {}
/*      */     
/*   23 */     protected TreeFactory tf = new LabeledScoredTreeFactory();
/*      */     
/*      */     public Tree transformTree(Tree tree) {
/*   26 */       Label lab = tree.label();
/*   27 */       String s = lab.value();
/*   28 */       String tag = null;
/*   29 */       if ((lab instanceof edu.stanford.nlp.ling.HasTag)) {
/*   30 */         tag = ((edu.stanford.nlp.ling.HasTag)lab).tag();
/*      */       }
/*   32 */       if (tree.isLeaf()) {
/*   33 */         Tree leaf = this.tf.newLeaf(s);
/*   34 */         leaf.setScore(tree.score());
/*   35 */         return leaf; }
/*   36 */       if (tree.isPhrasal()) {
/*   37 */         if ((EnglishTreebankParserParams.EnglishTest.retainADVSubcategories) && (s.indexOf("-ADV") >= 0)) {
/*   38 */           s = EnglishTreebankParserParams.this.tlp.basicCategory(s);
/*   39 */           s = s + "-ADV";
/*   40 */         } else if ((EnglishTreebankParserParams.EnglishTest.retainTMPSubcategories) && (s.indexOf("-TMP") >= 0)) {
/*   41 */           s = EnglishTreebankParserParams.this.tlp.basicCategory(s);
/*   42 */           s = s + "-TMP";
/*   43 */         } else if ((EnglishTreebankParserParams.EnglishTest.retainNPTMPSubcategories) && (s.startsWith("NP-TMP"))) {
/*   44 */           s = "NP-TMP";
/*      */         } else {
/*   46 */           s = EnglishTreebankParserParams.this.tlp.basicCategory(s);
/*      */         }
/*      */         
/*   49 */         if ((EnglishTreebankParserParams.EnglishTrain.splitBaseNP == 2) && (s.equals("NP")))
/*      */         {
/*   51 */           Tree[] kids = tree.children();
/*   52 */           if ((kids.length == 1) && (EnglishTreebankParserParams.this.tlp.basicCategory(kids[0].value()).equals("NP")))
/*      */           {
/*      */ 
/*   55 */             List<Tree> kidkids = new ArrayList();
/*   56 */             for (int cNum = 0; cNum < kids[0].children().length; cNum++) {
/*   57 */               Tree child = kids[0].children()[cNum];
/*   58 */               Tree newChild = transformTree(child);
/*   59 */               if (newChild != null) {
/*   60 */                 kidkids.add(newChild);
/*      */               }
/*      */             }
/*   63 */             CategoryWordTag myLabel = new CategoryWordTag(lab);
/*   64 */             myLabel.setCategory(s);
/*   65 */             return this.tf.newTreeNode(myLabel, kidkids);
/*      */           }
/*      */         }
/*      */         
/*   69 */         if ((EnglishTreebankParserParams.EnglishTrain.splitPoss == 2) && (s.equals("POSSP")))
/*      */         {
/*   71 */           Tree[] kids = tree.children();
/*   72 */           List<Tree> newkids = new ArrayList();
/*   73 */           for (int j = 0; j < kids.length - 1; j++) {
/*   74 */             for (int cNum = 0; cNum < kids[j].children().length; cNum++) {
/*   75 */               Tree child = kids[0].children()[cNum];
/*   76 */               Tree newChild = transformTree(child);
/*   77 */               if (newChild != null) {
/*   78 */                 newkids.add(newChild);
/*      */               }
/*      */             }
/*      */           }
/*   82 */           Tree finalChild = transformTree(kids[(kids.length - 1)]);
/*   83 */           newkids.add(finalChild);
/*   84 */           CategoryWordTag myLabel = new CategoryWordTag(lab);
/*   85 */           myLabel.setCategory("NP");
/*   86 */           return this.tf.newTreeNode(myLabel, newkids);
/*      */         }
/*      */       } else {
/*   89 */         s = EnglishTreebankParserParams.this.tlp.basicCategory(s);
/*   90 */         if (tag != null) {
/*   91 */           tag = EnglishTreebankParserParams.this.tlp.basicCategory(tag);
/*      */         }
/*      */       }
/*   94 */       List<Tree> children = new ArrayList();
/*   95 */       for (int cNum = 0; cNum < tree.numChildren(); cNum++) {
/*   96 */         Tree child = tree.getChild(cNum);
/*   97 */         Tree newChild = transformTree(child);
/*   98 */         if (newChild != null) {
/*   99 */           children.add(newChild);
/*      */         }
/*      */       }
/*  102 */       if (children.isEmpty()) {
/*  103 */         return null;
/*      */       }
/*  105 */       CategoryWordTag newLabel = new CategoryWordTag(lab);
/*  106 */       newLabel.setCategory(s);
/*  107 */       if (tag != null) {
/*  108 */         newLabel.setTag(tag);
/*      */       }
/*  110 */       Tree node = this.tf.newTreeNode(newLabel, children);
/*  111 */       node.setScore(tree.score());
/*  112 */       return node;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public EnglishTreebankParserParams()
/*      */   {
/*  119 */     super(new edu.stanford.nlp.trees.PennTreebankLanguagePack());
/*  120 */     this.headFinder = new edu.stanford.nlp.trees.ModCollinsHeadFinder(this.tlp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public HeadFinder headFinder()
/*      */   {
/*  128 */     return this.headFinder;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public edu.stanford.nlp.trees.DiskTreebank diskTreebank()
/*      */   {
/*  139 */     return new edu.stanford.nlp.trees.DiskTreebank(treeReaderFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public edu.stanford.nlp.trees.MemoryTreebank memoryTreebank()
/*      */   {
/*  150 */     return new edu.stanford.nlp.trees.MemoryTreebank(treeReaderFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public edu.stanford.nlp.trees.TreeReaderFactory treeReaderFactory()
/*      */   {
/*  158 */     new edu.stanford.nlp.trees.TreeReaderFactory() {
/*      */       public edu.stanford.nlp.trees.TreeReader newTreeReader(Reader in) {
/*  160 */         return new edu.stanford.nlp.trees.PennTreeReader(in, new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.StringLabelFactory()), new edu.stanford.nlp.trees.NPTmpRetainingTreeNormalizer(EnglishTreebankParserParams.EnglishTrain.splitTMP, EnglishTreebankParserParams.EnglishTrain.splitSGapped == 5, Train.leaveItAll, EnglishTreebankParserParams.EnglishTrain.splitNPADV >= 1, EnglishTreebankParserParams.this.headFinder()));
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public edu.stanford.nlp.trees.MemoryTreebank testMemoryTreebank()
/*      */   {
/*  170 */     new edu.stanford.nlp.trees.MemoryTreebank(new edu.stanford.nlp.trees.TreeReaderFactory() {
/*      */       public edu.stanford.nlp.trees.TreeReader newTreeReader(Reader in) {
/*  172 */         return new edu.stanford.nlp.trees.PennTreeReader(in, new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.StringLabelFactory()), new edu.stanford.nlp.trees.BobChrisTreeNormalizer(EnglishTreebankParserParams.this.tlp));
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public edu.stanford.nlp.trees.TreeTransformer collinizer()
/*      */   {
/*  182 */     return new TreeCollinizer(this.tlp, true, EnglishTrain.splitBaseNP == 2);
/*      */   }
/*      */   
/*      */   public edu.stanford.nlp.trees.TreeTransformer collinizerEvalb() {
/*  186 */     return new TreeCollinizer(this.tlp, true, EnglishTrain.splitBaseNP == 2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreebankLanguagePack treebankLanguagePack()
/*      */   {
/*  195 */     return this.tlp;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.io.PrintWriter pw(java.io.OutputStream o)
/*      */   {
/*  204 */     return new java.io.PrintWriter(o, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  209 */   private static String[] sisterSplit1 = { "ADJP=l=VBD", "ADJP=l=VBP", "NP=r=RBR", "PRN=r=.", "ADVP=l=PP", "PP=l=JJ", "PP=r=NP", "SBAR=l=VB", "PP=l=VBG", "ADJP=r=,", "ADVP=r=.", "ADJP=l=VB", "FRAG=l=FRAG", "FRAG=r=:", "PP=r=,", "ADJP=l=,", "FRAG=r=FRAG", "FRAG=l=:", "PRN=r=VP", "PP=l=RB", "S=l=ADJP", "SBAR=l=VBN", "NP=r=NX", "SBAR=l=VBZ", "SBAR=l=ADVP", "QP=r=JJ", "SBAR=l=PP", "SBAR=l=ADJP", "NP=r=VBG", "VP=r=:", "VP=l=ADJP", "SBAR=l=VBP", "ADVP=r=NP", "PP=l=VB", "VP=r=PP", "ADJP=r=SBAR", "NP=r=JJR", "SBAR=l=NN", "S=l=RB", "S=l=NNS", "S=r=SBAR", "S=l=WHPP", "VP=l=:", "ADVP=l=NP", "ADVP=r=PP", "ADJP=l=JJ", "NP=r=VBN", "NP=l=PRN", "VP=r=S", "NP=r=NNPS", "NX=r=NX", "ADJP=l=PRP$", "SBAR=l=CC", "SBAR=l=S", "S=l=PRT", "ADVP=l=VB", "ADVP=r=JJ", "NP=l=DT" };
/*  210 */   private static String[] sisterSplit2 = { "S=r=PP", "NP=r=JJS", "ADJP=r=NNP", "NP=l=PRT", "ADJP=r=PP", "ADJP=l=VBZ", "PP=r=VP", "NP=r=CD", "ADVP=l=IN", "ADVP=l=,", "ADJP=r=JJ", "ADVP=l=VBD", "PP=r=.", "S=l=ADVP", "S=l=DT", "PP=l=NP", "VP=l=PRN", "NP=r=IN", "NP=r=``" };
/*  211 */   private static String[] sisterSplit3 = { "PP=l=VBD", "ADJP=r=NNS", "S=l=:", "NP=l=ADVP", "NP=r=PRN", "NP=r=-RRB-", "NP=l=-LRB-", "NP=l=JJ", "SBAR=r=.", "S=r=:", "ADVP=r=VP", "NP=l=RB", "NP=r=RB", "S=l=VBP", "SBAR=r=,", "VP=r=,", "PP=r=PP", "NP=r=S", "ADJP=l=NP", "VP=l=VBG", "PP=l=PP" };
/*  212 */   private static String[] sisterSplit4 = { "VP=l=NP", "NP=r=NN", "NP=r=VP", "VP=r=.", "NP=r=PP", "VP=l=TO", "VP=l=MD", "NP=r=,", "NP=r=NP", "NP=r=.", "NP=l=IN", "NP=l=NP", "VP=l=,", "VP=l=S", "NP=l=,", "VP=l=VBZ", "S=r=.", "NP=r=NNS", "S=l=IN", "NP=r=JJ", "NP=r=NNP", "VP=l=VBD", "S=l=WHNP", "VP=r=NP", "VP=l=''", "VP=l=VBP", "NP=l=:", "S=r=,", "VP=l=``", "VP=l=VB", "NP=l=S", "NP=l=VP", "NP=l=VB", "NP=l=VBD", "NP=r=SBAR", "NP=r=:", "VP=l=PP", "NP=l=VBZ", "NP=l=CC", "NP=l=''", "S=r=NP", "S=r=S", "S=l=VBN", "NP=l=``", "ADJP=r=NN", "S=r=VP", "NP=r=CC", "VP=l=RB", "S=l=S", "S=l=NP", "NP=l=TO", "S=l=,", "S=l=VBD", "S=r=''", "S=l=``", "S=r=CC", "PP=l=,", "S=l=CC", "VP=l=CC", "ADJP=l=DT", "NP=l=VBG", "VP=r=''", "SBAR=l=NP", "VP=l=VP", "NP=l=PP", "S=l=VB", "SBAR=l=VBD", "VP=l=ADVP", "VP=l=VBN", "NP=r=''", "VP=l=SBAR", "SBAR=l=,", "S=l=WHADVP", "VP=r=VP", "NP=r=ADVP", "QP=r=NNS", "NP=l=VBP", "S=l=VBZ", "NP=l=VBN", "S=l=PP", "VP=r=CC", "NP=l=SBAR", "SBAR=r=NP", "S=l=VBG", "SBAR=r=VP", "NP=r=ADJP", "S=l=JJ", "S=l=NN", "QP=r=NN" };
/*      */   
/*      */   public String[] sisterSplitters() {
/*  215 */     switch (EnglishTrain.sisterSplitLevel) {
/*      */     case 1: 
/*  217 */       return sisterSplit1;
/*      */     case 2: 
/*  219 */       return sisterSplit2;
/*      */     case 3: 
/*  221 */       return sisterSplit3;
/*      */     case 4: 
/*  223 */       return sisterSplit4;
/*      */     }
/*  225 */     return new String[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public edu.stanford.nlp.trees.TreeTransformer subcategoryStripper()
/*      */   {
/*  235 */     return new EnglishSubcategoryStripper();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static class EnglishTest
/*      */   {
/*  242 */     static boolean retainNPTMPSubcategories = false;
/*  243 */     static boolean retainTMPSubcategories = false;
/*  244 */     static boolean retainADVSubcategories = false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class EnglishTrain
/*      */   {
/*  267 */     public static int splitIN = 0;
/*      */     
/*      */ 
/*      */ 
/*  271 */     public static boolean splitQuotes = false;
/*      */     
/*      */ 
/*      */ 
/*  275 */     public static boolean splitSFP = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  280 */     public static boolean splitPercent = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  287 */     public static int splitNPpercent = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  293 */     public static boolean tagRBGPA = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  298 */     public static int splitNNP = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  303 */     public static boolean joinPound = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  308 */     public static boolean joinJJ = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  315 */     public static boolean joinNounTags = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  320 */     public static boolean splitPPJJ = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  327 */     public static boolean splitTRJJ = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  333 */     public static boolean splitJJCOMP = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  339 */     public static boolean splitMoreLess = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  344 */     public static boolean unaryDT = false;
/*      */     
/*      */ 
/*      */ 
/*  348 */     public static boolean unaryRB = false;
/*      */     
/*      */ 
/*      */ 
/*  352 */     public static boolean unaryPRP = false;
/*      */     
/*      */ 
/*      */ 
/*  356 */     public static boolean markReflexivePRP = false;
/*      */     
/*      */ 
/*      */ 
/*  360 */     public static boolean unaryIN = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  369 */     public static int splitCC = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  374 */     public static boolean splitNOT = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  379 */     public static boolean splitRB = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  390 */     public static int splitAux = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  396 */     public static boolean vpSubCat = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  403 */     public static int markDitransV = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  420 */     public static int splitVP = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  425 */     public static boolean splitVPNPAgr = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  437 */     public static int splitSTag = 0;
/*      */     
/*  439 */     public static boolean markContainedVP = false;
/*      */     
/*  441 */     public static boolean splitNPPRP = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  447 */     public static int dominatesV = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  452 */     public static boolean dominatesI = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  457 */     public static boolean dominatesC = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  467 */     public static int markCC = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  476 */     public static int splitSGapped = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  481 */     public static boolean splitNumNP = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  494 */     public static int splitPoss = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  506 */     public static int splitBaseNP = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  513 */     public static int splitTMP = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  525 */     public static int splitSbar = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  532 */     public static int splitNPADV = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  538 */     public static int splitNPNNP = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  543 */     public static boolean correctTags = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  548 */     public static boolean rightPhrasal = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  554 */     public static int sisterSplitLevel = 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  559 */     public static boolean gpaRootVP = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  564 */     public static boolean makePPTOintoIN = false;
/*      */     
/*      */     public static void display()
/*      */     {
/*  568 */       String englishParams = "Using EnglishTreebankParserParams splitIN=" + splitIN + " sPercent=" + splitPercent + " sNNP=" + splitNNP + " sQuotes=" + splitQuotes + " sSFP=" + splitSFP + " rbGPA=" + tagRBGPA + " j#=" + joinPound + " jJJ=" + joinJJ + " jNounTags=" + joinNounTags + " sPPJJ=" + splitPPJJ + " sTRJJ=" + splitTRJJ + " sJJCOMP=" + splitJJCOMP + " sMoreLess=" + splitMoreLess + " unaryDT=" + unaryDT + " unaryRB=" + unaryRB + " unaryPRP=" + unaryPRP + " reflPRP=" + markReflexivePRP + " unaryIN=" + unaryIN + " sCC=" + splitCC + " sNT=" + splitNOT + " sRB=" + splitRB + " sAux=" + splitAux + " vpSubCat=" + vpSubCat + " mDTV=" + markDitransV + " sVP=" + splitVP + " sVPNPAgr=" + splitVPNPAgr + " sSTag=" + splitSTag + " mVP=" + markContainedVP + " sNP%=" + splitNPpercent + " sNPPRP=" + splitNPPRP + " dominatesV=" + dominatesV + " dominatesI=" + dominatesI + " dominatesC=" + dominatesC + " mCC=" + markCC + " sSGapped=" + splitSGapped + " numNP=" + splitNumNP + " sPoss=" + splitPoss + " baseNP=" + splitBaseNP + " sNPNNP=" + splitNPNNP + " sTMP=" + splitTMP + " sNPADV=" + splitNPADV + " cTags=" + correctTags + " rightPhrasal=" + rightPhrasal + " gpaRootVP=" + gpaRootVP + " splitSbar=" + splitSbar + " mPPTOiIN=" + makePPTOintoIN;
/*  569 */       System.err.println(englishParams);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  575 */   private static TreeFactory categoryWordTagTreeFactory = new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.CategoryWordTagFactory());
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final long serialVersionUID = 4153878351331522581L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree transformTree(Tree t, Tree root)
/*      */   {
/*  596 */     if ((t == null) || (t.isLeaf())) {
/*  597 */       return t;
/*      */     }
/*      */     
/*      */     String parentStr;
/*      */     
/*      */     Tree parent;
/*      */     String parentStr;
/*  604 */     if ((root == null) || (t.equals(root))) {
/*  605 */       Tree parent = null;
/*  606 */       parentStr = "";
/*      */     } else {
/*  608 */       parent = t.parent(root);
/*  609 */       parentStr = parent.label().value(); }
/*      */     String grandParentStr;
/*  611 */     String grandParentStr; if ((parent == null) || (parent.equals(root))) {
/*  612 */       Tree grandParent = null;
/*  613 */       grandParentStr = "";
/*      */     } else {
/*  615 */       Tree grandParent = parent.parent(root);
/*  616 */       grandParentStr = grandParent.label().value();
/*      */     }
/*  618 */     String baseParentStr = this.tlp.basicCategory(parentStr);
/*  619 */     String baseGrandParentStr = this.tlp.basicCategory(grandParentStr);
/*      */     
/*  621 */     CategoryWordTag lab = (CategoryWordTag)t.label();
/*  622 */     String word = lab.word();
/*  623 */     String tag = lab.tag();
/*  624 */     String baseTag = this.tlp.basicCategory(tag);
/*  625 */     String cat = lab.value();
/*  626 */     String baseCat = this.tlp.basicCategory(cat);
/*      */     
/*  628 */     if (t.isPreTerminal()) {
/*  629 */       if (EnglishTrain.correctTags) {
/*  630 */         if (baseParentStr.equals("NP")) {
/*  631 */           if (baseCat.equals("IN")) {
/*  632 */             if ((word.equalsIgnoreCase("a")) || (word.equalsIgnoreCase("that"))) {
/*  633 */               cat = changeBaseCat(cat, "DT");
/*  634 */             } else if ((word.equalsIgnoreCase("so")) || (word.equalsIgnoreCase("about")))
/*      */             {
/*  636 */               cat = changeBaseCat(cat, "RB");
/*  637 */             } else if ((word.equals("fiscal")) || (word.equalsIgnoreCase("next"))) {
/*  638 */               cat = changeBaseCat(cat, "JJ");
/*      */             }
/*  640 */           } else if (baseCat.equals("RB")) {
/*  641 */             if (word.equals("McNally")) {
/*  642 */               cat = changeBaseCat(cat, "NNP");
/*  643 */             } else if (word.equals("multifamily")) {
/*  644 */               cat = changeBaseCat(cat, "NN");
/*  645 */             } else if (word.equals("MORE")) {
/*  646 */               cat = changeBaseCat(cat, "JJR");
/*  647 */             } else if (word.equals("hand")) {
/*  648 */               cat = changeBaseCat(cat, "NN");
/*  649 */             } else if (word.equals("fist")) {
/*  650 */               cat = changeBaseCat(cat, "NN");
/*      */             }
/*  652 */           } else if (baseCat.equals("RP")) {
/*  653 */             if (word.equals("Howard")) {
/*  654 */               cat = changeBaseCat(cat, "NNP");
/*  655 */             } else if (word.equals("whole")) {
/*  656 */               cat = changeBaseCat(cat, "JJ");
/*      */             }
/*  658 */           } else if (baseCat.equals("JJ")) {
/*  659 */             if (word.equals("U.S.")) {
/*  660 */               cat = changeBaseCat(cat, "NNP");
/*  661 */             } else if (word.equals("ours")) {
/*  662 */               cat = changeBaseCat(cat, "PRP");
/*  663 */             } else if (word.equals("mine")) {
/*  664 */               cat = changeBaseCat(cat, "NN");
/*  665 */             } else if (word.equals("Sept.")) {
/*  666 */               cat = changeBaseCat(cat, "NNP");
/*      */             }
/*  668 */           } else if (baseCat.equals("NN")) {
/*  669 */             if ((word.equals("Chapman")) || (word.equals("Jan.")) || (word.equals("Sept.")) || (word.equals("Oct.")) || (word.equals("Nov.")) || (word.equals("Dec."))) {
/*  670 */               cat = changeBaseCat(cat, "NNP");
/*  671 */             } else if ((word.equals("members")) || (word.equals("bureaus")) || (word.equals("days")) || ((word.equals("outfits") | word.equals("institutes"))) || (word.equals("innings")) || (word.equals("write-offs")) || (word.equals("wines")) || (word.equals("trade-offs")) || (word.equals("tie-ins")) || (word.equals("thrips")) || (word.equals("1980s")) || (word.equals("1920s"))) {
/*  672 */               cat = changeBaseCat(cat, "NNS");
/*  673 */             } else if (word.equals("this")) {
/*  674 */               cat = changeBaseCat(cat, "DT");
/*      */             }
/*  676 */           } else if (baseCat.equals(":")) {
/*  677 */             if (word.equals("'")) {
/*  678 */               cat = changeBaseCat(cat, "''");
/*      */             }
/*  680 */           } else if (baseCat.equals("NNS")) {
/*  681 */             if ((word.equals("start-up")) || (word.equals("ground-handling")) || (word.equals("word-processing")) || (word.equals("T-shirt")) || (word.equals("co-pilot")))
/*      */             {
/*      */ 
/*  684 */               cat = changeBaseCat(cat, "NN");
/*  685 */             } else if ((word.equals("Sens.")) || (word.equals("Aichi"))) {
/*  686 */               cat = changeBaseCat(cat, "NNP");
/*      */             }
/*  688 */           } else if (baseCat.equals("VBZ")) {
/*  689 */             if (word.equals("'s")) {
/*  690 */               cat = changeBaseCat(cat, "POS");
/*  691 */             } else if (!word.equals("kills")) {
/*  692 */               cat = changeBaseCat(cat, "NNS");
/*      */             }
/*  694 */           } else if (baseCat.equals("VBG")) {
/*  695 */             if (word.equals("preferred")) {
/*  696 */               cat = changeBaseCat(cat, "VBN");
/*      */             }
/*  698 */           } else if (baseCat.equals("VB")) {
/*  699 */             if (word.equals("The")) {
/*  700 */               cat = changeBaseCat(cat, "DT");
/*  701 */             } else if (word.equals("allowed")) {
/*  702 */               cat = changeBaseCat(cat, "VBD");
/*  703 */             } else if ((word.equals("short")) || (word.equals("key")) || (word.equals("many")) || (word.equals("last")) || (word.equals("further"))) {
/*  704 */               cat = changeBaseCat(cat, "JJ");
/*  705 */             } else if (word.equals("lower")) {
/*  706 */               cat = changeBaseCat(cat, "JJR");
/*  707 */             } else if ((word.equals("Nov.")) || (word.equals("Jan.")) || (word.equals("Dec.")) || (word.equals("Tandy")) || (word.equals("Release")) || (word.equals("Orkem"))) {
/*  708 */               cat = changeBaseCat(cat, "NNP");
/*  709 */             } else if ((word.equals("watch")) || (word.equals("review")) || (word.equals("risk")) || (word.equals("realestate")) || (word.equals("love")) || (word.equals("experience")) || (word.equals("control")) || (word.equals("Transport")) || (word.equals("mind")) || (word.equals("term")) || (word.equals("program")) || (word.equals("gender")) || (word.equals("audit")) || (word.equals("blame")) || (word.equals("stock")) || (word.equals("run")) || (word.equals("group")) || (word.equals("affect")) || (word.equals("rent")) || (word.equals("show")) || (word.equals("accord")) || (word.equals("change")) || (word.equals("finish")) || (word.equals("work")) || (word.equals("schedule")) || (word.equals("influence")) || (word.equals("school")) || (word.equals("freight")) || (word.equals("growth")) || (word.equals("travel")) || (word.equals("call")) || (word.equals("autograph")) || (word.equals("demand")) || (word.equals("abuse")) || (word.equals("return")) || (word.equals("defeat")) || (word.equals("pressure")) || (word.equals("bank")) || (word.equals("notice")) || (word.equals("tax")) || (word.equals("ooze")) || (word.equals("network")) || (word.equals("concern")) || (word.equals("pit")) || (word.equals("contract")) || (word.equals("cash"))) {
/*  710 */               cat = changeBaseCat(cat, "NN");
/*      */             }
/*  712 */           } else if (baseCat.equals("NNP")) {
/*  713 */             if (word.equals("Officials")) {
/*  714 */               cat = changeBaseCat(cat, "NNS");
/*  715 */             } else if (word.equals("Currently")) {
/*  716 */               cat = changeBaseCat(cat, "RB");
/*      */             }
/*      */           }
/*  719 */           else if (baseCat.equals("PRP")) {
/*  720 */             if ((word.equals("her")) && (parent.numChildren() > 1)) {
/*  721 */               cat = changeBaseCat(cat, "PRP$");
/*  722 */             } else if (word.equals("US")) {
/*  723 */               cat = changeBaseCat(cat, "NNP");
/*      */             }
/*      */           }
/*  726 */         } else if (baseParentStr.equals("WHNP")) {
/*  727 */           if ((baseCat.equals("VBP")) && (word.equalsIgnoreCase("that"))) {
/*  728 */             cat = changeBaseCat(cat, "WDT");
/*      */           }
/*  730 */         } else if (baseParentStr.equals("UCP")) {
/*  731 */           if (word.equals("multifamily")) {
/*  732 */             cat = changeBaseCat(cat, "NN");
/*      */           }
/*  734 */         } else if (baseParentStr.equals("PRT")) {
/*  735 */           if ((baseCat.equals("RBR")) && (word.equals("in"))) {
/*  736 */             cat = changeBaseCat(cat, "RP");
/*  737 */           } else if ((baseCat.equals("NNP")) && (word.equals("up"))) {
/*  738 */             cat = changeBaseCat(cat, "RP");
/*      */           }
/*  740 */         } else if (baseParentStr.equals("PP")) {
/*  741 */           if ((parentStr.equals("PP-TMP")) && 
/*  742 */             (baseCat.equals("RP"))) {
/*  743 */             cat = changeBaseCat(cat, "IN");
/*      */           }
/*      */           
/*  746 */           if ((word.equals("in")) && ((baseCat.equals("RP")) || (baseCat.equals("NN")))) {
/*  747 */             cat = changeBaseCat(cat, "IN");
/*  748 */           } else if (baseCat.equals("RB")) {
/*  749 */             if ((word.equals("for")) || (word.equals("After"))) {
/*  750 */               cat = changeBaseCat(cat, "IN");
/*      */             }
/*  752 */           } else if ((word.equals("if")) && (baseCat.equals("JJ"))) {
/*  753 */             cat = changeBaseCat(cat, "IN");
/*      */           }
/*  755 */         } else if (baseParentStr.equals("VP")) {
/*  756 */           if (baseCat.equals("NNS")) {
/*  757 */             cat = changeBaseCat(cat, "VBZ");
/*  758 */           } else if (baseCat.equals("IN")) {
/*  759 */             if (word.equals("complicated")) {
/*  760 */               cat = changeBaseCat(cat, "VBD");
/*  761 */             } else if (word.equals("post")) {
/*  762 */               cat = changeBaseCat(cat, "VB");
/*  763 */             } else if (word.equals("like")) {
/*  764 */               cat = changeBaseCat(cat, "VB");
/*  765 */             } else if (word.equals("off")) {
/*  766 */               cat = changeBaseCat(cat, "RP");
/*      */             }
/*  768 */           } else if (baseCat.equals("NN")) {
/*  769 */             if (word.endsWith("ing")) {
/*  770 */               cat = changeBaseCat(cat, "VBG");
/*  771 */             } else if (word.equals("bid")) {
/*  772 */               cat = changeBaseCat(cat, "VBN");
/*  773 */             } else if (word.equals("are")) {
/*  774 */               cat = changeBaseCat(cat, "VBP");
/*  775 */             } else if (word.equals("lure")) {
/*  776 */               cat = changeBaseCat(cat, "VB");
/*  777 */             } else if (word.equals("cost")) {
/*  778 */               cat = changeBaseCat(cat, "VBP");
/*  779 */             } else if (word.equals("agreed")) {
/*  780 */               cat = changeBaseCat(cat, "VBN");
/*  781 */             } else if (word.equals("restructure")) {
/*  782 */               cat = changeBaseCat(cat, "VB");
/*  783 */             } else if (word.equals("rule")) {
/*  784 */               cat = changeBaseCat(cat, "VB");
/*  785 */             } else if (word.equals("fret")) {
/*  786 */               cat = changeBaseCat(cat, "VBP");
/*  787 */             } else if (word.equals("retort")) {
/*  788 */               cat = changeBaseCat(cat, "VBP");
/*  789 */             } else if (word.equals("draft")) {
/*  790 */               cat = changeBaseCat(cat, "VB");
/*  791 */             } else if (word.equals("will")) {
/*  792 */               cat = changeBaseCat(cat, "MD");
/*  793 */             } else if (word.equals("yield")) {
/*  794 */               cat = changeBaseCat(cat, "VBP");
/*  795 */             } else if (word.equals("lure")) {
/*  796 */               cat = changeBaseCat(cat, "VBP");
/*  797 */             } else if (word.equals("feel")) {
/*  798 */               cat = changeBaseCat(cat, "VB");
/*  799 */             } else if (word.equals("institutes")) {
/*  800 */               cat = changeBaseCat(cat, "VBZ");
/*  801 */             } else if (word.equals("share")) {
/*  802 */               cat = changeBaseCat(cat, "VBP");
/*  803 */             } else if (word.equals("trade")) {
/*  804 */               cat = changeBaseCat(cat, "VB");
/*  805 */             } else if (word.equals("beat")) {
/*  806 */               cat = changeBaseCat(cat, "VBN");
/*  807 */             } else if (word.equals("effect")) {
/*  808 */               cat = changeBaseCat(cat, "VB");
/*  809 */             } else if (word.equals("speed")) {
/*  810 */               cat = changeBaseCat(cat, "VB");
/*  811 */             } else if (word.equals("work")) {
/*  812 */               cat = changeBaseCat(cat, "VB");
/*  813 */             } else if (word.equals("act")) {
/*  814 */               cat = changeBaseCat(cat, "VBP");
/*  815 */             } else if (word.equals("drop")) {
/*  816 */               cat = changeBaseCat(cat, "VB");
/*  817 */             } else if (word.equals("stand")) {
/*  818 */               cat = changeBaseCat(cat, "VBP");
/*  819 */             } else if (word.equals("push")) {
/*  820 */               cat = changeBaseCat(cat, "VB");
/*  821 */             } else if (word.equals("service")) {
/*  822 */               cat = changeBaseCat(cat, "VB");
/*  823 */             } else if (word.equals("set")) {
/*  824 */               cat = changeBaseCat(cat, "VBN");
/*  825 */             } else if (word.equals("appeal")) {
/*  826 */               cat = changeBaseCat(cat, "VBP");
/*  827 */             } else if (word.equals("mold")) {
/*  828 */               cat = changeBaseCat(cat, "VB");
/*  829 */             } else if (word.equals("mean")) {
/*  830 */               cat = changeBaseCat(cat, "VB");
/*  831 */             } else if (word.equals("reconfirm")) {
/*  832 */               cat = changeBaseCat(cat, "VB");
/*  833 */             } else if (word.equals("land")) {
/*  834 */               cat = changeBaseCat(cat, "VB");
/*  835 */             } else if (word.equals("point")) {
/*  836 */               cat = changeBaseCat(cat, "VBP");
/*  837 */             } else if (word.equals("rise")) {
/*  838 */               cat = changeBaseCat(cat, "VB");
/*  839 */             } else if (word.equals("pressured")) {
/*  840 */               cat = changeBaseCat(cat, "VBN");
/*  841 */             } else if (word.equals("smell")) {
/*  842 */               cat = changeBaseCat(cat, "VBP");
/*  843 */             } else if (word.equals("pay")) {
/*  844 */               cat = changeBaseCat(cat, "VBP");
/*  845 */             } else if (word.equals("hum")) {
/*  846 */               cat = changeBaseCat(cat, "VB");
/*  847 */             } else if (word.equals("shape")) {
/*  848 */               cat = changeBaseCat(cat, "VBP");
/*  849 */             } else if (word.equals("benefit")) {
/*  850 */               cat = changeBaseCat(cat, "VB");
/*  851 */             } else if (word.equals("abducted")) {
/*  852 */               cat = changeBaseCat(cat, "VBN");
/*  853 */             } else if (word.equals("look")) {
/*  854 */               cat = changeBaseCat(cat, "VB");
/*  855 */             } else if (word.equals("fare")) {
/*  856 */               cat = changeBaseCat(cat, "VB");
/*  857 */             } else if (word.equals("change")) {
/*  858 */               cat = changeBaseCat(cat, "VB");
/*  859 */             } else if (word.equals("farm")) {
/*  860 */               cat = changeBaseCat(cat, "VB");
/*  861 */             } else if (word.equals("increase")) {
/*  862 */               cat = changeBaseCat(cat, "VB");
/*  863 */             } else if (word.equals("stem")) {
/*  864 */               cat = changeBaseCat(cat, "VB");
/*      */             }
/*  866 */             else if (word.equals("rebounded")) {
/*  867 */               cat = changeBaseCat(cat, "VBD");
/*  868 */             } else if (word.equals("face")) {
/*  869 */               cat = changeBaseCat(cat, "VB");
/*      */             }
/*  871 */           } else if (baseCat.equals("NNP")) {
/*  872 */             if (word.equals("GRAB")) {
/*  873 */               cat = changeBaseCat(cat, "VBP");
/*  874 */             } else if (word.equals("mature")) {
/*  875 */               cat = changeBaseCat(cat, "VB");
/*  876 */             } else if (word.equals("Face")) {
/*  877 */               cat = changeBaseCat(cat, "VBP");
/*  878 */             } else if (word.equals("are")) {
/*  879 */               cat = changeBaseCat(cat, "VBP");
/*  880 */             } else if (word.equals("Urging")) {
/*  881 */               cat = changeBaseCat(cat, "VBG");
/*  882 */             } else if (word.equals("Finding")) {
/*  883 */               cat = changeBaseCat(cat, "VBG");
/*  884 */             } else if (word.equals("say")) {
/*  885 */               cat = changeBaseCat(cat, "VBP");
/*  886 */             } else if (word.equals("Added")) {
/*  887 */               cat = changeBaseCat(cat, "VBD");
/*  888 */             } else if (word.equals("Adds")) {
/*  889 */               cat = changeBaseCat(cat, "VBZ");
/*  890 */             } else if (word.equals("BRACED")) {
/*  891 */               cat = changeBaseCat(cat, "VBD");
/*  892 */             } else if (word.equals("REQUIRED")) {
/*  893 */               cat = changeBaseCat(cat, "VBN");
/*  894 */             } else if (word.equals("SIZING")) {
/*  895 */               cat = changeBaseCat(cat, "VBG");
/*  896 */             } else if (word.equals("REVIEW")) {
/*  897 */               cat = changeBaseCat(cat, "VB");
/*  898 */             } else if (word.equals("code-named")) {
/*  899 */               cat = changeBaseCat(cat, "VBN");
/*  900 */             } else if (word.equals("Printed")) {
/*  901 */               cat = changeBaseCat(cat, "VBN");
/*  902 */             } else if (word.equals("Rated")) {
/*  903 */               cat = changeBaseCat(cat, "VBN");
/*  904 */             } else if (word.equals("FALTERS")) {
/*  905 */               cat = changeBaseCat(cat, "VBZ");
/*  906 */             } else if (word.equals("Got")) {
/*  907 */               cat = changeBaseCat(cat, "VBN");
/*  908 */             } else if (word.equals("JUMPING")) {
/*  909 */               cat = changeBaseCat(cat, "VBG");
/*  910 */             } else if (word.equals("Branching")) {
/*  911 */               cat = changeBaseCat(cat, "VBG");
/*  912 */             } else if (word.equals("Excluding")) {
/*  913 */               cat = changeBaseCat(cat, "VBG");
/*  914 */             } else if (word.equals("Adds")) {
/*  915 */               cat = changeBaseCat(cat, "VBZ");
/*  916 */             } else if (word.equals("OKing")) {
/*  917 */               cat = changeBaseCat(cat, "VBG");
/*      */             }
/*  919 */           } else if (baseCat.equals("POS")) {
/*  920 */             cat = changeBaseCat(cat, "VBZ");
/*  921 */           } else if (baseCat.equals("VBD")) {
/*  922 */             if (word.equals("heaves")) {
/*  923 */               cat = changeBaseCat(cat, "VBZ");
/*      */             }
/*  925 */           } else if (baseCat.equals("VB")) {
/*  926 */             if ((word.equals("allowed")) || (word.equals("increased"))) {
/*  927 */               cat = changeBaseCat(cat, "VBD");
/*      */             }
/*  929 */           } else if (baseCat.equals("VBN")) {
/*  930 */             if (word.equals("has")) {
/*  931 */               cat = changeBaseCat(cat, "VBZ");
/*  932 */             } else if ((word.equals("grew")) || (word.equals("fell"))) {
/*  933 */               cat = changeBaseCat(cat, "VBD");
/*      */             }
/*  935 */           } else if (baseCat.equals("JJ")) {
/*  936 */             if (word.equals("own")) {
/*  937 */               cat = changeBaseCat(cat, "VB");
/*      */             }
/*      */           }
/*  940 */           else if (word.equalsIgnoreCase("being")) {
/*  941 */             if (!cat.equals("VBG")) {
/*  942 */               cat = changeBaseCat(cat, "VBG");
/*      */             }
/*  944 */           } else if (word.equalsIgnoreCase("all")) {
/*  945 */             cat = changeBaseCat(cat, "RB");
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*  951 */         else if (baseParentStr.equals("S")) {
/*  952 */           if (word.equalsIgnoreCase("all")) {
/*  953 */             cat = changeBaseCat(cat, "RB");
/*      */           }
/*  955 */         } else if (baseParentStr.equals("ADJP")) {
/*  956 */           if (baseCat.equals("UH")) {
/*  957 */             cat = changeBaseCat(cat, "JJ");
/*  958 */           } else if (baseCat.equals("JJ")) {
/*  959 */             if (word.equalsIgnoreCase("more")) {
/*  960 */               cat = changeBaseCat(cat, "JJR");
/*      */             }
/*  962 */           } else if (baseCat.equals("RB")) {
/*  963 */             if (word.equalsIgnoreCase("free")) {
/*  964 */               cat = changeBaseCat(cat, "JJ");
/*  965 */             } else if (word.equalsIgnoreCase("clear")) {
/*  966 */               cat = changeBaseCat(cat, "JJ");
/*  967 */             } else if (word.equalsIgnoreCase("tight")) {
/*  968 */               cat = changeBaseCat(cat, "JJ");
/*  969 */             } else if (word.equalsIgnoreCase("sure")) {
/*  970 */               cat = changeBaseCat(cat, "JJ");
/*  971 */             } else if (word.equalsIgnoreCase("particular")) {
/*  972 */               cat = changeBaseCat(cat, "JJ");
/*      */             }
/*      */           }
/*  975 */           else if (baseCat.equals("VB")) {
/*  976 */             if (word.equalsIgnoreCase("stock")) {
/*  977 */               cat = changeBaseCat(cat, "NN");
/*  978 */             } else if (word.equalsIgnoreCase("secure")) {
/*  979 */               cat = changeBaseCat(cat, "JJ");
/*      */             }
/*      */           }
/*  982 */         } else if (baseParentStr.equals("QP")) {
/*  983 */           if (word.equalsIgnoreCase("about")) {
/*  984 */             cat = changeBaseCat(cat, "RB");
/*  985 */           } else if ((baseCat.equals("JJ")) && 
/*  986 */             (word.equalsIgnoreCase("more"))) {
/*  987 */             cat = changeBaseCat(cat, "JJR");
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*  993 */         else if (baseParentStr.equals("ADVP")) {
/*  994 */           if (baseCat.equals("EX")) {
/*  995 */             cat = changeBaseCat(cat, "RB");
/*  996 */           } else if ((baseCat.equals("NN")) && (word.equalsIgnoreCase("that"))) {
/*  997 */             cat = changeBaseCat(cat, "DT");
/*  998 */           } else if ((baseCat.equals("NNP")) && ((word.endsWith("ly")) || (word.equals("Overall"))))
/*      */           {
/* 1000 */             cat = changeBaseCat(cat, "RB");
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/* 1006 */         else if (baseParentStr.equals("SBAR")) {
/* 1007 */           if (((word.equalsIgnoreCase("that")) || (word.equalsIgnoreCase("because")) || (word.equalsIgnoreCase("while"))) && (!baseCat.equals("IN"))) {
/* 1008 */             cat = changeBaseCat(cat, "IN");
/* 1009 */           } else if (((word.equals("Though")) || (word.equals("Whether"))) && (baseCat.equals("NNP"))) {
/* 1010 */             cat = changeBaseCat(cat, "IN");
/*      */           }
/* 1012 */         } else if (baseParentStr.equals("SBARQ")) {
/* 1013 */           if ((baseCat.equals("S")) && 
/* 1014 */             (word.equalsIgnoreCase("had"))) {
/* 1015 */             cat = changeBaseCat(cat, "SQ");
/*      */           }
/*      */         }
/* 1018 */         else if (baseCat.equals("JJS")) {
/* 1019 */           if (word.equalsIgnoreCase("less")) {
/* 1020 */             cat = changeBaseCat(cat, "JJR");
/*      */           }
/* 1022 */         } else if (baseCat.equals("JJ")) {
/* 1023 */           if (word.equalsIgnoreCase("%"))
/*      */           {
/* 1025 */             cat = changeBaseCat(cat, "NN");
/* 1026 */           } else if (word.equalsIgnoreCase("to")) {
/* 1027 */             cat = changeBaseCat(cat, "TO");
/*      */           }
/* 1029 */         } else if (baseCat.equals("VB")) {
/* 1030 */           if (word.equalsIgnoreCase("even")) {
/* 1031 */             cat = changeBaseCat(cat, "RB");
/*      */           }
/* 1033 */         } else if (baseCat.equals(",")) {
/* 1034 */           if (word.equals("2")) {
/* 1035 */             cat = changeBaseCat(cat, "CD");
/* 1036 */           } else if (word.equals("an")) {
/* 1037 */             cat = changeBaseCat(cat, "DT");
/* 1038 */           } else if (word.equals("Wa")) {
/* 1039 */             cat = changeBaseCat(cat, "NNP");
/* 1040 */           } else if (word.equals("section")) {
/* 1041 */             cat = changeBaseCat(cat, "NN");
/* 1042 */           } else if (word.equals("underwriters")) {
/* 1043 */             cat = changeBaseCat(cat, "NNS");
/*      */           }
/* 1045 */         } else if (baseCat.equals("CD")) {
/* 1046 */           if (word.equals("high-risk")) {
/* 1047 */             cat = changeBaseCat(cat, "JJ");
/*      */           }
/* 1049 */         } else if (baseCat.equals("RB")) {
/* 1050 */           if (word.equals("for")) {
/* 1051 */             cat = changeBaseCat(cat, "IN");
/*      */           }
/* 1053 */         } else if (baseCat.equals("RP")) {
/* 1054 */           if (word.equals("for")) {
/* 1055 */             cat = changeBaseCat(cat, "IN");
/*      */           }
/* 1057 */         } else if (baseCat.equals("NN")) {
/* 1058 */           if ((word.length() == 2) && (word.charAt(1) == '.') && (Character.isUpperCase(word.charAt(0)))) {
/* 1059 */             cat = changeBaseCat(cat, "NNP");
/* 1060 */           } else if (word.equals("Lorillard")) {
/* 1061 */             cat = changeBaseCat(cat, "NNP");
/*      */           }
/* 1063 */         } else if ((word.equals("for")) || (word.equals("at"))) {
/* 1064 */           if (!baseCat.equals("IN"))
/*      */           {
/* 1066 */             cat = changeBaseCat(cat, "IN");
/*      */           }
/* 1068 */         } else if ((word.equalsIgnoreCase("and")) && (!baseCat.equals("CC"))) {
/* 1069 */           cat = changeBaseCat(cat, "CC");
/* 1070 */         } else if ((word.equals("ago")) && 
/* 1071 */           (!baseCat.equals("RB"))) {
/* 1072 */           cat = changeBaseCat(cat, "RB");
/*      */         }
/*      */         
/*      */ 
/* 1076 */         baseCat = this.tlp.basicCategory(cat);
/*      */       }
/* 1078 */       if ((EnglishTrain.makePPTOintoIN) && (baseCat.equals("TO")) && (baseParentStr.equals("PP")))
/*      */       {
/* 1080 */         cat = changeBaseCat(cat, "IN");
/*      */       }
/* 1082 */       if ((EnglishTrain.splitIN == 5) && (baseCat.equals("TO")) && 
/* 1083 */         (grandParentStr.charAt(0) == 'N') && ((parentStr.charAt(0) == 'P') || (parentStr.charAt(0) == 'A')))
/*      */       {
/* 1085 */         cat = changeBaseCat(cat, "IN") + "-N";
/*      */       }
/*      */       
/* 1088 */       if ((EnglishTrain.splitIN == 1) && (baseCat.equals("IN")) && (parentStr.charAt(0) == 'S')) {
/* 1089 */         cat = cat + "^S";
/* 1090 */       } else if ((EnglishTrain.splitIN == 2) && (baseCat.equals("IN"))) {
/* 1091 */         if (parentStr.charAt(0) == 'S') {
/* 1092 */           cat = cat + "^S";
/* 1093 */         } else if (grandParentStr.charAt(0) == 'N') {
/* 1094 */           cat = cat + "^N";
/*      */         }
/* 1096 */       } else if ((EnglishTrain.splitIN == 3) && (baseCat.equals("IN")))
/*      */       {
/*      */ 
/*      */ 
/* 1100 */         if ((grandParentStr.charAt(0) == 'N') && ((parentStr.charAt(0) == 'P') || (parentStr.charAt(0) == 'A')))
/*      */         {
/* 1102 */           cat = cat + "-N";
/* 1103 */         } else if ((parentStr.charAt(0) == 'Q') && ((grandParentStr.charAt(0) == 'N') || (grandParentStr.startsWith("ADJP"))))
/*      */         {
/* 1105 */           cat = cat + "-Q";
/* 1106 */         } else if (grandParentStr.equals("S"))
/*      */         {
/* 1108 */           if (baseParentStr.equals("SBAR"))
/*      */           {
/* 1110 */             cat = cat + "-SCC";
/*      */           }
/*      */           else {
/* 1113 */             cat = cat + "-SC";
/*      */           }
/* 1115 */         } else if ((baseParentStr.equals("SBAR")) || (baseParentStr.equals("WHNP")))
/*      */         {
/*      */ 
/* 1118 */           cat = cat + "-T";
/*      */         }
/*      */       }
/* 1121 */       else if ((EnglishTrain.splitIN >= 4) && (EnglishTrain.splitIN <= 5) && (baseCat.equals("IN"))) {
/* 1122 */         if ((grandParentStr.charAt(0) == 'N') && ((parentStr.charAt(0) == 'P') || (parentStr.charAt(0) == 'A')))
/*      */         {
/* 1124 */           cat = cat + "-N";
/* 1125 */         } else if ((parentStr.charAt(0) == 'Q') && ((grandParentStr.charAt(0) == 'N') || (grandParentStr.startsWith("ADJP"))))
/*      */         {
/* 1127 */           cat = cat + "-Q";
/* 1128 */         } else if ((baseGrandParentStr.charAt(0) == 'S') && (!baseGrandParentStr.equals("SBAR")))
/*      */         {
/*      */ 
/* 1131 */           if (baseParentStr.equals("SBAR"))
/*      */           {
/* 1133 */             cat = cat + "-SCC";
/* 1134 */           } else if ((!baseParentStr.equals("NP")) && (!baseParentStr.equals("ADJP")))
/*      */           {
/* 1136 */             cat = cat + "-SC";
/*      */           }
/* 1138 */         } else if ((baseParentStr.equals("SBAR")) || (baseParentStr.equals("WHNP")) || (baseParentStr.equals("WHADVP")))
/*      */         {
/*      */ 
/* 1141 */           cat = cat + "-T";
/*      */         }
/*      */       }
/* 1144 */       else if ((EnglishTrain.splitIN == 6) && (baseCat.equals("IN"))) {
/* 1145 */         if ((grandParentStr.charAt(0) == 'V') || (grandParentStr.charAt(0) == 'A')) {
/* 1146 */           cat = cat + "-V";
/* 1147 */         } else if ((grandParentStr.charAt(0) != 'N') || ((parentStr.charAt(0) != 'P') && (parentStr.charAt(0) != 'A')))
/*      */         {
/*      */ 
/* 1150 */           if ((parentStr.charAt(0) == 'Q') && ((grandParentStr.charAt(0) == 'N') || (grandParentStr.startsWith("ADJP"))))
/*      */           {
/* 1152 */             cat = cat + "-Q";
/* 1153 */           } else if ((baseGrandParentStr.charAt(0) == 'S') && (!baseGrandParentStr.equals("SBAR")))
/*      */           {
/*      */ 
/* 1156 */             if (baseParentStr.equals("SBAR"))
/*      */             {
/* 1158 */               cat = cat + "-SCC";
/* 1159 */             } else if ((!baseParentStr.equals("NP")) && (!baseParentStr.equals("ADJP")))
/*      */             {
/* 1161 */               cat = cat + "-SC";
/*      */             }
/* 1163 */           } else if ((baseParentStr.equals("SBAR")) || (baseParentStr.equals("WHNP")) || (baseParentStr.equals("WHADVP")))
/*      */           {
/*      */ 
/* 1166 */             cat = cat + "-T";
/*      */           }
/*      */         }
/*      */       }
/* 1170 */       if ((EnglishTrain.splitPercent) && (word.equals("%"))) {
/* 1171 */         cat = cat + "-%";
/*      */       }
/* 1173 */       if ((EnglishTrain.splitNNP > 0) && (baseCat.startsWith("NNP"))) {
/* 1174 */         if (EnglishTrain.splitNNP == 1) {
/* 1175 */           if (baseCat.equals("NNP")) {
/* 1176 */             if (parent.numChildren() == 1) {
/* 1177 */               cat = cat + "-S";
/* 1178 */             } else if (parent.firstChild().equals(t)) {
/* 1179 */               cat = cat + "-L";
/* 1180 */             } else if (parent.lastChild().equals(t)) {
/* 1181 */               cat = cat + "-R";
/*      */             } else {
/* 1183 */               cat = cat + "-I";
/*      */             }
/*      */           }
/* 1186 */         } else if (EnglishTrain.splitNNP == 2) {
/* 1187 */           if (word.matches("[A-Z]\\.?")) {
/* 1188 */             cat = cat + "-I";
/* 1189 */           } else if (firstOfSeveralNNP(parent, t)) {
/* 1190 */             cat = cat + "-B";
/* 1191 */           } else if (lastOfSeveralNNP(parent, t)) {
/* 1192 */             cat = cat + "-E";
/*      */           }
/*      */         }
/*      */       }
/* 1196 */       if ((EnglishTrain.splitQuotes) && ((word.equals("'")) || (word.equals("`"))))
/*      */       {
/* 1198 */         cat = cat + "-SG";
/*      */       }
/* 1200 */       if ((EnglishTrain.splitSFP) && (baseTag.equals("."))) {
/* 1201 */         if (word.equals("?")) {
/* 1202 */           cat = cat + "-QUES";
/* 1203 */         } else if (word.equals("!")) {
/* 1204 */           cat = cat + "-EXCL";
/*      */         }
/*      */       }
/* 1207 */       if ((EnglishTrain.tagRBGPA) && 
/* 1208 */         (baseCat.equals("RB"))) {
/* 1209 */         cat = cat + "^" + baseGrandParentStr;
/*      */       }
/*      */       
/* 1212 */       if ((EnglishTrain.joinPound) && (baseCat.equals("#"))) {
/* 1213 */         cat = changeBaseCat(cat, "$");
/*      */       }
/* 1215 */       if (EnglishTrain.joinNounTags) {
/* 1216 */         if (baseCat.equals("NNP")) {
/* 1217 */           cat = changeBaseCat(cat, "NN");
/* 1218 */         } else if (baseCat.equals("NNPS")) {
/* 1219 */           cat = changeBaseCat(cat, "NNS");
/*      */         }
/*      */       }
/* 1222 */       if ((EnglishTrain.joinJJ) && (cat.startsWith("JJ"))) {
/* 1223 */         cat = changeBaseCat(cat, "JJ");
/*      */       }
/* 1225 */       if ((EnglishTrain.splitPPJJ) && (cat.startsWith("JJ")) && (parentStr.startsWith("PP"))) {
/* 1226 */         cat = cat + "^S";
/*      */       }
/* 1228 */       if ((EnglishTrain.splitTRJJ) && (cat.startsWith("JJ")) && ((parentStr.startsWith("PP")) || (parentStr.startsWith("ADJP"))) && (headFinder().determineHead(parent) == t))
/*      */       {
/* 1230 */         Tree[] kids = parent.children();
/* 1231 */         boolean foundJJ = false;
/* 1232 */         for (int i = 0; 
/* 1233 */             (i < kids.length) && (!foundJJ); i++) {
/* 1234 */           if (kids[i].label().value().startsWith("JJ")) {
/* 1235 */             foundJJ = true;
/*      */           }
/*      */         }
/* 1238 */         for (int j = i; j < kids.length; j++) {
/* 1239 */           if (kids[j].label().value().startsWith("NP")) {
/* 1240 */             cat = cat + "^T";
/* 1241 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1245 */       if ((EnglishTrain.splitJJCOMP) && (cat.startsWith("JJ")) && ((parentStr.startsWith("PP")) || (parentStr.startsWith("ADJP"))) && (headFinder().determineHead(parent) == t))
/*      */       {
/* 1247 */         Tree[] kids = parent.children();
/* 1248 */         boolean foundJJ = false;
/* 1249 */         for (int i = 0; 
/* 1250 */             (i < kids.length) && (!foundJJ); i++) {
/* 1251 */           if (kids[i].label().value().startsWith("JJ")) {
/*      */             break;
/*      */           }
/*      */         }
/* 1255 */         if (i < kids.length - 1)
/*      */         {
/* 1257 */           cat = cat + "^CMPL";
/*      */         }
/*      */       }
/* 1260 */       if (EnglishTrain.splitMoreLess) {
/* 1261 */         char ch = cat.charAt(0);
/* 1262 */         if ((ch == 'R') || (ch == 'J') || (ch == 'C'))
/*      */         {
/* 1264 */           if ((word.equalsIgnoreCase("more")) || (word.equalsIgnoreCase("most")) || (word.equalsIgnoreCase("less")) || (word.equalsIgnoreCase("least"))) {
/* 1265 */             cat = cat + "-ML";
/*      */           }
/*      */         }
/*      */       }
/* 1269 */       if ((EnglishTrain.unaryDT) && (cat.startsWith("DT")) && 
/* 1270 */         (parent.children().length == 1)) {
/* 1271 */         cat = cat + "^U";
/*      */       }
/*      */       
/* 1274 */       if ((EnglishTrain.unaryRB) && (cat.startsWith("RB")) && 
/* 1275 */         (parent.children().length == 1)) {
/* 1276 */         cat = cat + "^U";
/*      */       }
/*      */       
/* 1279 */       if ((EnglishTrain.markReflexivePRP) && (cat.startsWith("PRP")) && (
/* 1280 */         (word.equalsIgnoreCase("itself")) || (word.equalsIgnoreCase("themselves")) || (word.equalsIgnoreCase("himself")) || (word.equalsIgnoreCase("herself")) || (word.equalsIgnoreCase("ourselves")) || (word.equalsIgnoreCase("yourself")) || (word.equalsIgnoreCase("yourselves")) || (word.equalsIgnoreCase("myself")) || (word.equalsIgnoreCase("thyself")))) {
/* 1281 */         cat = cat + "-SE";
/*      */       }
/*      */       
/* 1284 */       if ((EnglishTrain.unaryPRP) && (cat.startsWith("PRP")) && 
/* 1285 */         (parent.children().length == 1)) {
/* 1286 */         cat = cat + "^U";
/*      */       }
/*      */       
/* 1289 */       if ((EnglishTrain.unaryIN) && (cat.startsWith("IN")) && 
/* 1290 */         (parent.children().length == 1)) {
/* 1291 */         cat = cat + "^U";
/*      */       }
/*      */       
/* 1294 */       if ((EnglishTrain.splitCC > 0) && (baseCat.equals("CC"))) {
/* 1295 */         if ((EnglishTrain.splitCC == 1) && ((word.equals("and")) || (word.equals("or")))) {
/* 1296 */           cat = cat + "-C";
/* 1297 */         } else if (EnglishTrain.splitCC == 2) {
/* 1298 */           if (word.equalsIgnoreCase("but")) {
/* 1299 */             cat = cat + "-B";
/* 1300 */           } else if (word.equals("&")) {
/* 1301 */             cat = cat + "-A";
/*      */           }
/* 1303 */         } else if ((EnglishTrain.splitCC == 3) && (word.equalsIgnoreCase("and"))) {
/* 1304 */           cat = cat + "-A";
/*      */         }
/*      */       }
/* 1307 */       if ((EnglishTrain.splitNOT) && (baseCat.equals("RB")) && ((word.equalsIgnoreCase("n't")) || (word.equalsIgnoreCase("not")) || (word.equalsIgnoreCase("nt")))) {
/* 1308 */         cat = cat + "-N";
/* 1309 */       } else if ((EnglishTrain.splitRB) && (baseCat.equals("RB")) && ((baseParentStr.equals("NP")) || (baseParentStr.equals("QP")) || (baseParentStr.equals("ADJP")))) {
/* 1310 */         cat = cat + "^M";
/*      */       }
/* 1312 */       if ((EnglishTrain.splitAux > 1) && ((baseCat.equals("VBZ")) || (baseCat.equals("VBP")) || (baseCat.equals("VBD")) || (baseCat.equals("VBN")) || (baseCat.equals("VBG")) || (baseCat.equals("VB")))) {
/* 1313 */         if (word.equalsIgnoreCase("'s")) {
/* 1314 */           Tree[] sisters = parent.children();
/* 1315 */           boolean foundMe = false;
/* 1316 */           for (int i = 0; 
/* 1317 */               (i < sisters.length) && (!foundMe); i++) {
/* 1318 */             if (sisters[i].label().value().startsWith("VBZ")) {
/* 1319 */               foundMe = true;
/*      */             }
/*      */           }
/* 1322 */           boolean annotateHave = false;
/* 1323 */           for (int j = i; j < sisters.length; j++) {
/* 1324 */             if (sisters[j].label().value().startsWith("VP")) {
/* 1325 */               Tree[] kids = sisters[j].children();
/* 1326 */               for (int k = 0; k < kids.length; k++) {
/* 1327 */                 if ((kids[k].label().value().startsWith("VBN")) || (kids[k].label().value().startsWith("VBD"))) {
/* 1328 */                   annotateHave = true;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/* 1333 */           if (annotateHave) {
/* 1334 */             cat = cat + "-HV";
/*      */           }
/*      */           else {
/* 1337 */             cat = cat + "-BE";
/*      */           }
/*      */         }
/* 1340 */         else if ((word.equalsIgnoreCase("is")) || (word.equalsIgnoreCase("am")) || (word.equalsIgnoreCase("are")) || (word.equalsIgnoreCase("was")) || (word.equalsIgnoreCase("were")) || (word.equalsIgnoreCase("'m")) || (word.equalsIgnoreCase("'re")) || (word.equalsIgnoreCase("s")) || (word.equalsIgnoreCase("being")) || (word.equalsIgnoreCase("be")) || (word.equalsIgnoreCase("been")))
/*      */         {
/* 1342 */           cat = cat + "-BE";
/* 1343 */         } else if ((word.equalsIgnoreCase("have")) || (word.equalsIgnoreCase("'ve")) || (word.equalsIgnoreCase("having")) || (word.equalsIgnoreCase("has")) || (word.equalsIgnoreCase("had")) || (word.equalsIgnoreCase("'d"))) {
/* 1344 */           cat = cat + "-HV";
/* 1345 */         } else if ((EnglishTrain.splitAux >= 3) && ((word.equalsIgnoreCase("do")) || (word.equalsIgnoreCase("did")) || (word.equalsIgnoreCase("does")) || (word.equalsIgnoreCase("done")) || (word.equalsIgnoreCase("help")) || (word.equalsIgnoreCase("helps")) || (word.equalsIgnoreCase("helped")) || (word.equalsIgnoreCase("helping"))))
/*      */         {
/*      */ 
/*      */ 
/* 1349 */           cat = cat + "-DO";
/*      */         }
/*      */       }
/* 1352 */       else if ((EnglishTrain.splitAux > 0) && ((baseCat.equals("VBZ")) || (baseCat.equals("VBP")) || (baseCat.equals("VBD")) || (baseCat.equals("VBN")) || (baseCat.equals("VBG")) || (baseCat.equals("VB")))) {
/* 1353 */         if ((word.equalsIgnoreCase("is")) || (word.equalsIgnoreCase("am")) || (word.equalsIgnoreCase("are")) || (word.equalsIgnoreCase("was")) || (word.equalsIgnoreCase("were")) || (word.equalsIgnoreCase("'m")) || (word.equalsIgnoreCase("'re")) || (word.equalsIgnoreCase("'s")) || (word.equalsIgnoreCase("being")) || (word.equalsIgnoreCase("be")) || (word.equalsIgnoreCase("been")))
/*      */         {
/* 1355 */           cat = cat + "-BE";
/*      */         }
/* 1357 */         if ((word.equalsIgnoreCase("have")) || (word.equalsIgnoreCase("'ve")) || (word.equalsIgnoreCase("having")) || (word.equalsIgnoreCase("has")) || (word.equalsIgnoreCase("had")) || (word.equalsIgnoreCase("'d"))) {
/* 1358 */           cat = cat + "-HV";
/*      */         }
/*      */       }
/* 1361 */       if ((EnglishTrain.markDitransV > 0) && (cat.startsWith("VB"))) {
/* 1362 */         cat = cat + ditrans(parent);
/* 1363 */       } else if ((EnglishTrain.vpSubCat) && (cat.startsWith("VB"))) {
/* 1364 */         cat = cat + subCatify(parent);
/*      */       }
/*      */       
/* 1367 */       tag = cat;
/*      */     } else {
/* 1369 */       Tree[] kids = t.children();
/*      */       
/* 1371 */       if (baseCat.equals("VP")) {
/* 1372 */         if ((EnglishTrain.gpaRootVP) && 
/* 1373 */           (this.tlp.isStartSymbol(baseGrandParentStr))) {
/* 1374 */           cat = cat + "~ROOT";
/*      */         }
/*      */         
/* 1377 */         if (EnglishTrain.splitVPNPAgr)
/*      */         {
/*      */ 
/*      */ 
/* 1381 */           if ((baseTag.equals("VBD")) || (baseTag.equals("MD"))) {
/* 1382 */             cat = cat + "-VBF";
/* 1383 */           } else if ((baseTag.equals("VBZ")) || (baseTag.equals("TO")) || (baseTag.equals("VBG")) || (baseTag.equals("VBP")) || (baseTag.equals("VBN")) || (baseTag.equals("VB"))) {
/* 1384 */             cat = cat + "-" + baseTag;
/*      */           } else {
/* 1386 */             System.err.println("XXXX Head of " + t + " is " + word + "/" + baseTag);
/*      */           }
/* 1388 */         } else if ((EnglishTrain.splitVP == 3) || (EnglishTrain.splitVP == 4))
/*      */         {
/* 1390 */           if ((baseTag.equals("VBZ")) || (baseTag.equals("VBD")) || (baseTag.equals("VBP")) || (baseTag.equals("MD"))) {
/* 1391 */             cat = cat + "-VBF";
/* 1392 */           } else if ((baseTag.equals("TO")) || (baseTag.equals("VBG")) || (baseTag.equals("VBN")) || (baseTag.equals("VB"))) {
/* 1393 */             cat = cat + "-" + baseTag;
/* 1394 */           } else if (EnglishTrain.splitVP == 4) {
/* 1395 */             String dTag = deduceTag(word);
/* 1396 */             cat = cat + "-" + dTag;
/*      */           }
/* 1398 */         } else if (EnglishTrain.splitVP == 2) {
/* 1399 */           if ((baseTag.equals("VBZ")) || (baseTag.equals("VBD")) || (baseTag.equals("VBP")) || (baseTag.equals("MD"))) {
/* 1400 */             cat = cat + "-VBF";
/*      */           } else {
/* 1402 */             cat = cat + "-" + baseTag;
/*      */           }
/* 1404 */         } else if (EnglishTrain.splitVP == 1) {
/* 1405 */           cat = cat + "-" + baseTag;
/*      */         }
/*      */       }
/* 1408 */       if (EnglishTrain.dominatesV > 0) {
/* 1409 */         if (EnglishTrain.dominatesV == 2) {
/* 1410 */           if (hasClausalV(t)) {
/* 1411 */             cat = cat + "-v";
/*      */           }
/* 1413 */         } else if (EnglishTrain.dominatesV == 3) {
/* 1414 */           if ((hasV(t.preTerminalYield())) && (!baseCat.equals("WHPP")) && (!baseCat.equals("RRC")) && (!baseCat.equals("QP")) && (!baseCat.equals("PRT")))
/*      */           {
/*      */ 
/* 1417 */             cat = cat + "-v";
/*      */           }
/*      */         }
/* 1420 */         else if (hasV(t.preTerminalYield())) {
/* 1421 */           cat = cat + "-v";
/*      */         }
/*      */       }
/*      */       
/* 1425 */       if ((EnglishTrain.dominatesI) && (hasI(t.preTerminalYield()))) {
/* 1426 */         cat = cat + "-i";
/*      */       }
/* 1428 */       if ((EnglishTrain.dominatesC) && (hasC(t.preTerminalYield()))) {
/* 1429 */         cat = cat + "-c";
/*      */       }
/* 1431 */       if ((EnglishTrain.splitNPpercent > 0) && (word.equals("%")) && (
/* 1432 */         (baseCat.equals("NP")) || ((EnglishTrain.splitNPpercent > 1) && (baseCat.equals("ADJP"))) || ((EnglishTrain.splitNPpercent > 2) && (baseCat.equals("QP"))) || (EnglishTrain.splitNPpercent > 3)))
/*      */       {
/*      */ 
/*      */ 
/* 1436 */         cat = cat + "-%";
/*      */       }
/*      */       
/* 1439 */       if ((EnglishTrain.splitNPPRP) && (baseTag.equals("PRP"))) {
/* 1440 */         cat = cat + "-PRON";
/*      */       }
/* 1442 */       if ((EnglishTrain.splitSbar > 0) && (baseCat.equals("SBAR"))) {
/* 1443 */         boolean foundIn = false;
/* 1444 */         boolean foundOrder = false;
/* 1445 */         boolean infinitive = baseTag.equals("TO");
/* 1446 */         for (int i = 0; i < kids.length; i++) {
/* 1447 */           if ((kids[i].isPreTerminal()) && (kids[i].children()[0].value().equalsIgnoreCase("in"))) {
/* 1448 */             foundIn = true;
/*      */           }
/* 1450 */           if ((kids[i].isPreTerminal()) && (kids[i].children()[0].value().equalsIgnoreCase("order"))) {
/* 1451 */             foundOrder = true;
/*      */           }
/*      */         }
/* 1454 */         if ((EnglishTrain.splitSbar > 1) && (infinitive)) {
/* 1455 */           cat = cat + "-INF";
/*      */         }
/* 1457 */         if (((EnglishTrain.splitSbar == 1) || (EnglishTrain.splitSbar == 3)) && (foundIn) && (foundOrder))
/*      */         {
/* 1459 */           cat = cat + "-PURP";
/*      */         }
/*      */       }
/* 1462 */       if (EnglishTrain.splitNPNNP > 0) {
/* 1463 */         if ((EnglishTrain.splitNPNNP == 1) && (baseCat.equals("NP")) && (baseTag.equals("NNP"))) {
/* 1464 */           cat = cat + "-NNP";
/* 1465 */         } else if ((EnglishTrain.splitNPNNP == 2) && (baseCat.equals("NP")) && (baseTag.startsWith("NNP"))) {
/* 1466 */           cat = cat + "-NNP";
/* 1467 */         } else if ((EnglishTrain.splitNPNNP == 3) && (baseCat.equals("NP"))) {
/* 1468 */           boolean split = false;
/* 1469 */           for (int i = 0; i < kids.length; i++) {
/* 1470 */             if (kids[i].value().startsWith("NNP")) {
/* 1471 */               split = true;
/* 1472 */               break;
/*      */             }
/*      */           }
/* 1475 */           if (split) {
/* 1476 */             cat = cat + "-NNP";
/*      */           }
/*      */         }
/*      */       }
/* 1480 */       if ((EnglishTrain.splitVPNPAgr) && (baseCat.equals("NP")) && (baseParentStr.startsWith("S")))
/*      */       {
/* 1482 */         if ((baseTag.equals("NNPS")) || (baseTag.equals("NNS"))) {
/* 1483 */           cat = cat + "-PL";
/* 1484 */         } else if ((word.equalsIgnoreCase("many")) || (word.equalsIgnoreCase("more")) || (word.equalsIgnoreCase("most")) || (word.equalsIgnoreCase("plenty"))) {
/* 1485 */           cat = cat + "-PL";
/* 1486 */         } else if ((!baseTag.equals("NN")) && (!baseTag.equals("NNP")) && (!baseTag.equals("POS")) && (!baseTag.equals("CD")) && (!baseTag.equals("PRP$")) && (!baseTag.equals("JJ")) && (!baseTag.equals("EX")) && (!baseTag.equals("$")) && (!baseTag.equals("RB")) && (!baseTag.equals("FW")) && (!baseTag.equals("VBG")) && (!baseTag.equals("JJS")) && (!baseTag.equals("JJR"))) {
/* 1487 */           if (baseTag.equals("PRP")) {
/* 1488 */             if ((word.equalsIgnoreCase("they")) || (word.equalsIgnoreCase("them")) || (word.equalsIgnoreCase("we")) || (word.equalsIgnoreCase("us"))) {
/* 1489 */               cat = cat + "-PL";
/*      */             }
/* 1491 */           } else if ((baseTag.equals("DT")) || (baseTag.equals("WDT"))) {
/* 1492 */             if ((word.equalsIgnoreCase("these")) || (word.equalsIgnoreCase("those")) || (word.equalsIgnoreCase("several"))) {
/* 1493 */               cat = cat + "-PL";
/*      */             }
/*      */           } else
/* 1496 */             System.err.println("XXXX Head of " + t + " is " + word + "/" + baseTag);
/*      */         }
/*      */       }
/* 1499 */       if ((EnglishTrain.splitSTag > 0) && ((baseCat.equals("S")) || ((EnglishTrain.splitSTag <= 3) && ((baseCat.equals("SINV")) || (baseCat.equals("SQ"))))))
/*      */       {
/* 1501 */         if (EnglishTrain.splitSTag == 1) {
/* 1502 */           cat = cat + "-" + baseTag;
/* 1503 */         } else if ((baseTag.equals("VBZ")) || (baseTag.equals("VBD")) || (baseTag.equals("VBP")) || (baseTag.equals("MD"))) {
/* 1504 */           cat = cat + "-VBF";
/* 1505 */         } else if (((EnglishTrain.splitSTag == 3) || (EnglishTrain.splitSTag == 5)) && ((baseTag.equals("TO")) || (baseTag.equals("VBG")) || (baseTag.equals("VBN")) || (baseTag.equals("VB"))))
/*      */         {
/* 1507 */           cat = cat + "-VBNF";
/*      */         }
/*      */       }
/* 1510 */       if ((EnglishTrain.markContainedVP) && (containsVP(t))) {
/* 1511 */         cat = cat + "-vp";
/*      */       }
/* 1513 */       if (EnglishTrain.markCC > 0)
/*      */       {
/*      */ 
/*      */ 
/* 1517 */         for (int i = 1; i < kids.length - 1; i++) {
/* 1518 */           String cat2 = kids[i].label().value();
/* 1519 */           if (cat2.startsWith("CC")) {
/* 1520 */             String word2 = kids[i].children()[0].value();
/*      */             
/* 1522 */             if ((!word2.equals("either")) && (!word2.equals("both")) && (!word2.equals("neither"))) {
/* 1523 */               cat = cat + "-CC";
/* 1524 */               break;
/*      */             }
/*      */             
/*      */           }
/* 1528 */           else if ((EnglishTrain.markCC > 1) && (cat2.startsWith("CONJP"))) {
/* 1529 */             cat = cat + "-CC";
/* 1530 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1534 */       if ((EnglishTrain.splitSGapped == 1) && (baseCat.equals("S")) && (!kids[0].label().value().startsWith("NP")))
/*      */       {
/*      */ 
/* 1537 */         cat = cat + "-G";
/* 1538 */       } else if ((EnglishTrain.splitSGapped == 2) && (baseCat.equals("S")))
/*      */       {
/*      */ 
/* 1541 */         boolean seenPredCat = false;
/* 1542 */         int seenNP = 0;
/* 1543 */         for (int i = 0; i < kids.length; i++) {
/* 1544 */           String cat2 = kids[i].label().value();
/* 1545 */           if (cat2.startsWith("NP")) {
/* 1546 */             seenNP++;
/* 1547 */           } else if ((cat2.startsWith("VP")) || (cat2.startsWith("ADJP")) || (cat2.startsWith("PP")) || (cat2.startsWith("UCP"))) {
/* 1548 */             seenPredCat = true;
/*      */           }
/*      */         }
/* 1551 */         if ((seenNP == 0) || ((seenNP == 1) && (!seenPredCat))) {
/* 1552 */           cat = cat + "-G";
/*      */         }
/* 1554 */       } else if ((EnglishTrain.splitSGapped == 3) && (baseCat.equals("S")))
/*      */       {
/*      */ 
/*      */ 
/* 1558 */         boolean seenPredCat = false;
/* 1559 */         boolean seenCC = false;
/* 1560 */         boolean seenS = false;
/* 1561 */         int seenNP = 0;
/* 1562 */         for (int i = 0; i < kids.length; i++) {
/* 1563 */           String cat2 = kids[i].label().value();
/* 1564 */           if (cat2.startsWith("NP")) {
/* 1565 */             seenNP++;
/* 1566 */           } else if ((cat2.startsWith("VP")) || (cat2.startsWith("ADJP")) || (cat2.startsWith("PP")) || (cat2.startsWith("UCP"))) {
/* 1567 */             seenPredCat = true;
/* 1568 */           } else if (cat2.startsWith("CC")) {
/* 1569 */             seenCC = true;
/* 1570 */           } else if (cat2.startsWith("S")) {
/* 1571 */             seenS = true;
/*      */           }
/*      */         }
/* 1574 */         if (((!seenCC) || (!seenS)) && ((seenNP == 0) || ((seenNP == 1) && (!seenPredCat)))) {
/* 1575 */           cat = cat + "-G";
/*      */         }
/* 1577 */       } else if ((EnglishTrain.splitSGapped == 4) && (baseCat.equals("S")))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1582 */         boolean seenPredCat = false;
/* 1583 */         boolean sawSBeforePredCat = false;
/* 1584 */         int seenS = 0;
/* 1585 */         int seenNP = 0;
/* 1586 */         for (int i = 0; i < kids.length; i++) {
/* 1587 */           String cat2 = kids[i].label().value();
/* 1588 */           if (cat2.startsWith("NP")) {
/* 1589 */             seenNP++;
/* 1590 */           } else if ((cat2.startsWith("VP")) || (cat2.startsWith("ADJP")) || (cat2.startsWith("PP")) || (cat2.startsWith("UCP"))) {
/* 1591 */             seenPredCat = true;
/* 1592 */           } else if (cat2.startsWith("S")) {
/* 1593 */             seenS++;
/* 1594 */             if (!seenPredCat) {
/* 1595 */               sawSBeforePredCat = true;
/*      */             }
/*      */           }
/*      */         }
/* 1599 */         if ((seenS < 2) && ((!sawSBeforePredCat) || (!seenPredCat)) && ((seenNP == 0) || ((seenNP == 1) && (!seenPredCat)))) {
/* 1600 */           cat = cat + "-G";
/*      */         }
/*      */       }
/* 1603 */       if ((EnglishTrain.splitNumNP) && (baseCat.equals("NP"))) {
/* 1604 */         boolean seenNum = false;
/* 1605 */         for (int i = 0; i < kids.length; i++) {
/* 1606 */           String cat2 = kids[i].label().value();
/* 1607 */           if ((cat2.startsWith("QP")) || (cat2.startsWith("CD")) || (cat2.startsWith("$")) || (cat2.startsWith("#")) || ((cat2.startsWith("NN")) && (cat2.indexOf("-%") >= 0))) {
/* 1608 */             seenNum = true;
/* 1609 */             break;
/*      */           }
/*      */         }
/* 1612 */         if (seenNum) {
/* 1613 */           cat = cat + "-NUM";
/*      */         }
/*      */       }
/* 1616 */       if ((EnglishTrain.splitPoss > 0) && (baseCat.equals("NP")) && (kids[(kids.length - 1)].label().value().startsWith("POS")))
/*      */       {
/* 1618 */         if (EnglishTrain.splitPoss == 2) {
/*      */           Label labelBot;
/*      */           Label labelBot;
/* 1621 */           if (t.isPrePreTerminal()) {
/* 1622 */             labelBot = new CategoryWordTag("NP^POSSP-B", word, tag);
/*      */           } else {
/* 1624 */             labelBot = new CategoryWordTag("NP^POSSP", word, tag);
/*      */           }
/* 1626 */           t.setLabel(labelBot);
/* 1627 */           List<Tree> oldKids = t.getChildrenAsList();
/*      */           
/*      */ 
/* 1630 */           List<Tree> newKids = new ArrayList();
/* 1631 */           for (int i = 0; i < oldKids.size() - 1; i++) {
/* 1632 */             newKids.add(oldKids.get(i));
/*      */           }
/* 1634 */           t.setChildren(newKids);
/* 1635 */           cat = changeBaseCat(cat, "POSSP");
/* 1636 */           Label labelTop = new CategoryWordTag(cat, word, tag);
/* 1637 */           List<Tree> newerChildren = new ArrayList(2);
/* 1638 */           newerChildren.add(t);
/*      */           
/* 1640 */           Tree last = (Tree)oldKids.get(oldKids.size() - 1);
/* 1641 */           if (!last.value().equals("POS^NP")) {
/* 1642 */             System.err.println("Unexpected POS value (!): " + last);
/*      */           }
/* 1644 */           last.setValue("POS^POSSP");
/* 1645 */           newerChildren.add(last);
/* 1646 */           return categoryWordTagTreeFactory.newTreeNode(labelTop, newerChildren);
/*      */         }
/* 1648 */         cat = cat + "-P";
/*      */       }
/*      */       
/* 1651 */       if ((EnglishTrain.splitBaseNP > 0) && (baseCat.equals("NP")) && (t.isPrePreTerminal()))
/*      */       {
/* 1653 */         if (EnglishTrain.splitBaseNP == 2) {
/* 1654 */           if (parentStr.startsWith("NP")) {
/* 1655 */             cat = cat + "-B";
/*      */           }
/*      */           else {
/* 1658 */             Label labelBot = new CategoryWordTag("NP^NP-B", word, tag);
/* 1659 */             t.setLabel(labelBot);
/* 1660 */             Label labelTop = new CategoryWordTag(cat, word, tag);
/* 1661 */             List<Tree> newerChildren = new ArrayList(1);
/* 1662 */             newerChildren.add(t);
/* 1663 */             return categoryWordTagTreeFactory.newTreeNode(labelTop, newerChildren);
/*      */           }
/*      */         } else {
/* 1666 */           cat = cat + "-B";
/*      */         }
/*      */       }
/* 1669 */       if ((EnglishTrain.rightPhrasal) && (rightPhrasal(t))) {
/* 1670 */         cat = cat + "-RX";
/*      */       }
/*      */     }
/*      */     
/* 1674 */     t.setLabel(new CategoryWordTag(cat, word, tag));
/* 1675 */     return t;
/*      */   }
/*      */   
/*      */   private boolean containsVP(Tree t)
/*      */   {
/* 1680 */     String cat = this.tlp.basicCategory(t.label().value());
/* 1681 */     if (cat.equals("VP")) {
/* 1682 */       return true;
/*      */     }
/* 1684 */     Tree[] kids = t.children();
/* 1685 */     for (int i = 0; i < kids.length; i++) {
/* 1686 */       if (containsVP(kids[i])) {
/* 1687 */         return true;
/*      */       }
/*      */     }
/* 1690 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean firstOfSeveralNNP(Tree parent, Tree t)
/*      */   {
/* 1695 */     boolean firstIsT = false;
/* 1696 */     int numNNP = 0;
/* 1697 */     Tree[] kids = parent.children();
/* 1698 */     for (int i = 0; i < kids.length; i++) {
/* 1699 */       if (kids[i].value().startsWith("NNP")) {
/* 1700 */         if ((t.equals(kids[i])) && (numNNP == 0)) {
/* 1701 */           firstIsT = true;
/*      */         }
/* 1703 */         numNNP++;
/*      */       }
/*      */     }
/* 1706 */     return (numNNP > 1) && (firstIsT);
/*      */   }
/*      */   
/*      */   private static boolean lastOfSeveralNNP(Tree parent, Tree t) {
/* 1710 */     Tree last = null;
/* 1711 */     int numNNP = 0;
/* 1712 */     Tree[] kids = parent.children();
/* 1713 */     for (int i = 0; i < kids.length; i++) {
/* 1714 */       if (kids[i].value().startsWith("NNP")) {
/* 1715 */         numNNP++;
/* 1716 */         last = kids[i];
/*      */       }
/*      */     }
/* 1719 */     return (numNNP > 1) && (t.equals(last));
/*      */   }
/*      */   
/*      */ 
/*      */   private static String deduceTag(String w)
/*      */   {
/* 1725 */     String word = w.toLowerCase();
/* 1726 */     if (word.endsWith("ing"))
/* 1727 */       return "VBG";
/* 1728 */     if ((word.endsWith("d")) || (word.endsWith("t")))
/* 1729 */       return "VBN";
/* 1730 */     if (word.endsWith("s"))
/* 1731 */       return "VBZ";
/* 1732 */     if (word.equals("to")) {
/* 1733 */       return "TO";
/*      */     }
/* 1735 */     return "VB";
/*      */   }
/*      */   
/*      */   private static boolean rightPhrasal(Tree t)
/*      */   {
/* 1740 */     while (!t.isLeaf()) {
/* 1741 */       t = t.lastChild();
/* 1742 */       String str = t.label().value();
/* 1743 */       if ((str.startsWith("NP")) || (str.startsWith("PP")) || (str.startsWith("VP")) || (str.startsWith("S")) || (str.startsWith("Q")) || (str.startsWith("A"))) {
/* 1744 */         return true;
/*      */       }
/*      */     }
/* 1747 */     return false;
/*      */   }
/*      */   
/*      */   private static String subCatify(Tree t)
/*      */   {
/* 1752 */     StringBuilder sb = new StringBuilder("^a");
/* 1753 */     boolean n = false;
/* 1754 */     boolean s = false;
/* 1755 */     boolean p = false;
/* 1756 */     for (int i = 0; i < t.children().length; i++) {
/* 1757 */       String childStr = t.children()[i].label().value();
/* 1758 */       n = (n) || (childStr.startsWith("NP"));
/* 1759 */       s = (s) || (childStr.startsWith("S"));
/* 1760 */       p = (p) || (childStr.startsWith("PP"));
/*      */     }
/* 1762 */     n = false;
/* 1763 */     if (n) {
/* 1764 */       sb.append("N");
/*      */     }
/* 1766 */     if (p) {
/* 1767 */       sb.append("P");
/*      */     }
/* 1769 */     if (s) {
/* 1770 */       sb.append("S");
/*      */     }
/* 1772 */     return sb.toString();
/*      */   }
/*      */   
/*      */   private static String ditrans(Tree t)
/*      */   {
/* 1777 */     int n = 0;
/* 1778 */     Tree[] kids = t.children();
/* 1779 */     int i = 0; for (int len = kids.length; i < len; i++) {
/* 1780 */       String childStr = kids[i].label().value();
/* 1781 */       if ((childStr.startsWith("NP")) && (childStr.indexOf("-TMP") < 0)) {
/* 1782 */         n++;
/* 1783 */       } else if ((EnglishTrain.markDitransV == 1) && (childStr.startsWith("S"))) {
/* 1784 */         n++;
/*      */       }
/*      */     }
/* 1787 */     if (n >= 2) {
/* 1788 */       return "^2Arg";
/*      */     }
/* 1790 */     return "";
/*      */   }
/*      */   
/*      */ 
/*      */   private String changeBaseCat(String cat, String newBaseCat)
/*      */   {
/* 1796 */     int i = 1;
/* 1797 */     int leng = cat.length();
/* 1798 */     for (; i < leng; i++) {
/* 1799 */       if (this.tlp.isLabelAnnotationIntroducingCharacter(cat.charAt(i))) {
/*      */         break;
/*      */       }
/*      */     }
/* 1803 */     if (i < leng) {
/* 1804 */       return newBaseCat + cat.substring(i);
/*      */     }
/* 1806 */     return newBaseCat;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean hasClausalV(Tree tree)
/*      */   {
/* 1819 */     if (tree.isPhrasal()) {
/* 1820 */       if ((tree.isPrePreTerminal()) && (tree.value().startsWith("NP")))
/*      */       {
/* 1822 */         return false;
/*      */       }
/* 1824 */       Tree[] kids = tree.children();
/* 1825 */       for (Tree t : kids) {
/* 1826 */         if (hasClausalV(t)) {
/* 1827 */           return true;
/*      */         }
/*      */       }
/* 1830 */       return false;
/*      */     }
/* 1832 */     String str = tree.value();
/* 1833 */     return (str.startsWith("VB")) || (str.startsWith("MD"));
/*      */   }
/*      */   
/*      */   private static boolean hasV(List tags)
/*      */   {
/* 1838 */     int i = 0; for (int tsize = tags.size(); i < tsize; i++) {
/* 1839 */       String str = tags.get(i).toString();
/* 1840 */       if ((str.startsWith("V")) || (str.startsWith("MD"))) {
/* 1841 */         return true;
/*      */       }
/*      */     }
/* 1844 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean hasI(List tags) {
/* 1848 */     int i = 0; for (int tsize = tags.size(); i < tsize; i++) {
/* 1849 */       if (tags.get(i).toString().startsWith("I")) {
/* 1850 */         return true;
/*      */       }
/*      */     }
/* 1853 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean hasC(List tags) {
/* 1857 */     int i = 0; for (int tsize = tags.size(); i < tsize; i++) {
/* 1858 */       if (tags.get(i).toString().startsWith("CC")) {
/* 1859 */         return true;
/*      */       }
/*      */     }
/* 1862 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void display() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int setOptionFlag(String[] args, int i)
/*      */   {
/* 1880 */     if (args[i].equalsIgnoreCase("-splitIN")) {
/* 1881 */       EnglishTrain.splitIN = Integer.parseInt(args[(i + 1)]);
/* 1882 */       i += 2;
/* 1883 */     } else if (args[i].equalsIgnoreCase("-splitPercent")) {
/* 1884 */       EnglishTrain.splitPercent = true;
/* 1885 */       i++;
/* 1886 */     } else if (args[i].equalsIgnoreCase("-splitQuotes")) {
/* 1887 */       EnglishTrain.splitQuotes = true;
/* 1888 */       i++;
/* 1889 */     } else if (args[i].equalsIgnoreCase("-splitSFP")) {
/* 1890 */       EnglishTrain.splitSFP = true;
/* 1891 */       i++;
/* 1892 */     } else if (args[i].equalsIgnoreCase("-splitNNP")) {
/* 1893 */       EnglishTrain.splitNNP = Integer.parseInt(args[(i + 1)]);
/* 1894 */       i += 2;
/* 1895 */     } else if (args[i].equalsIgnoreCase("-rbGPA")) {
/* 1896 */       EnglishTrain.tagRBGPA = true;
/* 1897 */       i++;
/* 1898 */     } else if (args[i].equalsIgnoreCase("-splitTRJJ")) {
/* 1899 */       EnglishTrain.splitTRJJ = true;
/* 1900 */       i++;
/* 1901 */     } else if (args[i].equalsIgnoreCase("-splitJJCOMP")) {
/* 1902 */       EnglishTrain.splitJJCOMP = true;
/* 1903 */       i++;
/* 1904 */     } else if (args[i].equalsIgnoreCase("-splitMoreLess")) {
/* 1905 */       EnglishTrain.splitMoreLess = true;
/* 1906 */       i++;
/* 1907 */     } else if (args[i].equalsIgnoreCase("-unaryDT")) {
/* 1908 */       EnglishTrain.unaryDT = true;
/* 1909 */       i++;
/* 1910 */     } else if (args[i].equalsIgnoreCase("-unaryRB")) {
/* 1911 */       EnglishTrain.unaryRB = true;
/* 1912 */       i++;
/* 1913 */     } else if (args[i].equalsIgnoreCase("-unaryIN")) {
/* 1914 */       EnglishTrain.unaryIN = true;
/* 1915 */       i++;
/* 1916 */     } else if (args[i].equalsIgnoreCase("-markReflexivePRP")) {
/* 1917 */       EnglishTrain.markReflexivePRP = true;
/* 1918 */       i++;
/* 1919 */     } else if ((args[i].equalsIgnoreCase("-splitCC")) && (i + 1 < args.length)) {
/* 1920 */       EnglishTrain.splitCC = Integer.parseInt(args[(i + 1)]);
/* 1921 */       i += 2;
/* 1922 */     } else if (args[i].equalsIgnoreCase("-splitRB")) {
/* 1923 */       EnglishTrain.splitRB = true;
/* 1924 */       i++;
/* 1925 */     } else if ((args[i].equalsIgnoreCase("-splitAux")) && (i + 1 < args.length)) {
/* 1926 */       EnglishTrain.splitAux = Integer.parseInt(args[(i + 1)]);
/* 1927 */       i += 2;
/* 1928 */     } else if ((args[i].equalsIgnoreCase("-splitSbar")) && (i + 1 < args.length)) {
/* 1929 */       EnglishTrain.splitSbar = Integer.parseInt(args[(i + 1)]);
/* 1930 */       i += 2;
/* 1931 */     } else if ((args[i].equalsIgnoreCase("-splitVP")) && (i + 1 < args.length)) {
/* 1932 */       EnglishTrain.splitVP = Integer.parseInt(args[(i + 1)]);
/* 1933 */       i += 2;
/* 1934 */     } else if (args[i].equalsIgnoreCase("-splitVPNPAgr")) {
/* 1935 */       EnglishTrain.splitVPNPAgr = true;
/* 1936 */       i++;
/* 1937 */     } else if (args[i].equalsIgnoreCase("-gpaRootVP")) {
/* 1938 */       EnglishTrain.gpaRootVP = true;
/* 1939 */       i++;
/* 1940 */     } else if (args[i].equalsIgnoreCase("-makePPTOintoIN")) {
/* 1941 */       EnglishTrain.makePPTOintoIN = true;
/* 1942 */       i++;
/* 1943 */     } else if (args[i].equalsIgnoreCase("-splitSTag")) {
/* 1944 */       EnglishTrain.splitSTag = Integer.parseInt(args[(i + 1)]);
/* 1945 */       i += 2;
/* 1946 */     } else if ((args[i].equalsIgnoreCase("-splitSGapped")) && (i + 1 < args.length)) {
/* 1947 */       EnglishTrain.splitSGapped = Integer.parseInt(args[(i + 1)]);
/* 1948 */       i += 2;
/* 1949 */     } else if ((args[i].equalsIgnoreCase("-splitNPpercent")) && (i + 1 < args.length)) {
/* 1950 */       EnglishTrain.splitNPpercent = Integer.parseInt(args[(i + 1)]);
/* 1951 */       i += 2;
/* 1952 */     } else if (args[i].equalsIgnoreCase("-splitNPPRP")) {
/* 1953 */       EnglishTrain.splitNPPRP = true;
/* 1954 */       i++;
/* 1955 */     } else if ((args[i].equalsIgnoreCase("-dominatesV")) && (i + 1 < args.length)) {
/* 1956 */       EnglishTrain.dominatesV = Integer.parseInt(args[(i + 1)]);
/* 1957 */       i += 2;
/* 1958 */     } else if (args[i].equalsIgnoreCase("-dominatesI")) {
/* 1959 */       EnglishTrain.dominatesI = true;
/* 1960 */       i++;
/* 1961 */     } else if (args[i].equalsIgnoreCase("-dominatesC")) {
/* 1962 */       EnglishTrain.dominatesC = true;
/* 1963 */       i++;
/* 1964 */     } else if ((args[i].equalsIgnoreCase("-splitNPNNP")) && (i + 1 < args.length)) {
/* 1965 */       EnglishTrain.splitNPNNP = Integer.parseInt(args[(i + 1)]);
/* 1966 */       i += 2;
/* 1967 */     } else if ((args[i].equalsIgnoreCase("-splitTMP")) && (i + 1 < args.length)) {
/* 1968 */       EnglishTrain.splitTMP = Integer.parseInt(args[(i + 1)]);
/* 1969 */       i += 2;
/* 1970 */     } else if ((args[i].equalsIgnoreCase("-splitNPADV")) && (i + 1 < args.length)) {
/* 1971 */       EnglishTrain.splitNPADV = Integer.parseInt(args[(i + 1)]);
/* 1972 */       i += 2;
/* 1973 */     } else if (args[i].equalsIgnoreCase("-markContainedVP")) {
/* 1974 */       EnglishTrain.markContainedVP = true;
/* 1975 */       i++;
/* 1976 */     } else if ((args[i].equalsIgnoreCase("-markDitransV")) && (i + 1 < args.length)) {
/* 1977 */       EnglishTrain.markDitransV = Integer.parseInt(args[(i + 1)]);
/* 1978 */       i += 2;
/* 1979 */     } else if ((args[i].equalsIgnoreCase("-splitPoss")) && (i + 1 < args.length)) {
/* 1980 */       EnglishTrain.splitPoss = Integer.parseInt(args[(i + 1)]);
/* 1981 */       i += 2;
/* 1982 */     } else if ((args[i].equalsIgnoreCase("-baseNP")) && (i + 1 < args.length)) {
/* 1983 */       EnglishTrain.splitBaseNP = Integer.parseInt(args[(i + 1)]);
/* 1984 */       i += 2;
/* 1985 */     } else if (args[i].equalsIgnoreCase("-joinNounTags")) {
/* 1986 */       EnglishTrain.joinNounTags = true;
/* 1987 */       i++;
/* 1988 */     } else if (args[i].equalsIgnoreCase("-correctTags")) {
/* 1989 */       EnglishTrain.correctTags = true;
/* 1990 */       i++;
/* 1991 */     } else if (args[i].equalsIgnoreCase("-noCorrectTags")) {
/* 1992 */       EnglishTrain.correctTags = false;
/* 1993 */       i++;
/* 1994 */     } else if ((args[i].equalsIgnoreCase("-markCC")) && (i + 1 < args.length)) {
/* 1995 */       EnglishTrain.markCC = Integer.parseInt(args[(i + 1)]);
/* 1996 */       i += 2;
/* 1997 */     } else if (args[i].equalsIgnoreCase("-noAnnotations")) {
/* 1998 */       EnglishTrain.splitVP = 0;
/* 1999 */       EnglishTrain.splitTMP = 0;
/* 2000 */       EnglishTrain.splitSGapped = 0;
/* 2001 */       i++;
/* 2002 */     } else if (args[i].equalsIgnoreCase("-retainNPTMPSubcategories")) {
/* 2003 */       EnglishTest.retainNPTMPSubcategories = true;
/* 2004 */       i++;
/* 2005 */     } else if (args[i].equalsIgnoreCase("-retainTMPSubcategories")) {
/* 2006 */       EnglishTest.retainTMPSubcategories = true;
/* 2007 */       i++;
/* 2008 */     } else if (args[i].equalsIgnoreCase("-retainADVSubcategories")) {
/* 2009 */       EnglishTest.retainADVSubcategories = true;
/* 2010 */       i++;
/* 2011 */     } else if ((args[i].equalsIgnoreCase("-headFinder")) && (i + 1 < args.length))
/*      */     {
/*      */       try
/*      */       {
/* 2015 */         this.headFinder = ((HeadFinder)Class.forName(args[(i + 1)]).newInstance());
/*      */       } catch (Exception e) {
/* 2017 */         System.err.println(e);
/* 2018 */         System.err.println("Warning: Default HeadFinder will be used.");
/*      */       }
/* 2020 */       i += 2;
/* 2021 */     } else if (args[i].equalsIgnoreCase("-acl03pcfg")) {
/* 2022 */       EnglishTrain.splitIN = 3;
/* 2023 */       EnglishTrain.splitPercent = true;
/* 2024 */       EnglishTrain.splitPoss = 1;
/* 2025 */       EnglishTrain.splitCC = 2;
/* 2026 */       EnglishTrain.unaryDT = true;
/* 2027 */       EnglishTrain.unaryRB = true;
/* 2028 */       EnglishTrain.splitAux = 1;
/* 2029 */       EnglishTrain.splitVP = 2;
/* 2030 */       EnglishTrain.splitSGapped = 3;
/* 2031 */       EnglishTrain.dominatesV = 1;
/* 2032 */       EnglishTrain.splitTMP = 1;
/* 2033 */       EnglishTrain.splitBaseNP = 1;
/* 2034 */       i++;
/* 2035 */     } else if (args[i].equalsIgnoreCase("-jenny")) {
/* 2036 */       EnglishTrain.splitIN = 3;
/* 2037 */       EnglishTrain.splitPercent = true;
/* 2038 */       EnglishTrain.splitPoss = 1;
/* 2039 */       EnglishTrain.splitCC = 2;
/* 2040 */       EnglishTrain.unaryDT = true;
/* 2041 */       EnglishTrain.unaryRB = true;
/* 2042 */       EnglishTrain.splitAux = 1;
/* 2043 */       EnglishTrain.splitVP = 2;
/* 2044 */       EnglishTrain.splitSGapped = 3;
/* 2045 */       EnglishTrain.dominatesV = 1;
/* 2046 */       EnglishTrain.splitTMP = 1;
/* 2047 */       EnglishTrain.splitBaseNP = 1;
/* 2048 */       i++;
/* 2049 */     } else if (args[i].equalsIgnoreCase("-linguisticPCFG")) {
/* 2050 */       EnglishTrain.splitIN = 3;
/* 2051 */       EnglishTrain.splitPercent = true;
/* 2052 */       EnglishTrain.splitPoss = 1;
/* 2053 */       EnglishTrain.splitCC = 2;
/* 2054 */       EnglishTrain.unaryDT = true;
/* 2055 */       EnglishTrain.unaryRB = true;
/* 2056 */       EnglishTrain.splitAux = 2;
/* 2057 */       EnglishTrain.splitVP = 3;
/* 2058 */       EnglishTrain.splitSGapped = 4;
/* 2059 */       EnglishTrain.dominatesV = 0;
/* 2060 */       EnglishTrain.splitTMP = 1;
/* 2061 */       EnglishTrain.splitBaseNP = 1;
/* 2062 */       EnglishTrain.splitMoreLess = true;
/* 2063 */       EnglishTrain.correctTags = true;
/* 2064 */       i++;
/* 2065 */     } else if (args[i].equalsIgnoreCase("-goodPCFG")) {
/* 2066 */       EnglishTrain.splitIN = 4;
/* 2067 */       EnglishTrain.splitPercent = true;
/* 2068 */       EnglishTrain.splitNPpercent = 0;
/* 2069 */       EnglishTrain.splitPoss = 1;
/* 2070 */       EnglishTrain.splitCC = 1;
/* 2071 */       EnglishTrain.unaryDT = true;
/* 2072 */       EnglishTrain.unaryRB = true;
/* 2073 */       EnglishTrain.splitAux = 2;
/* 2074 */       EnglishTrain.splitVP = 3;
/* 2075 */       EnglishTrain.splitSGapped = 4;
/* 2076 */       EnglishTrain.dominatesV = 1;
/* 2077 */       EnglishTrain.splitTMP = 1;
/* 2078 */       EnglishTrain.splitNPADV = 1;
/* 2079 */       EnglishTrain.splitBaseNP = 1;
/*      */       
/* 2081 */       EnglishTrain.correctTags = true;
/* 2082 */       EnglishTrain.markDitransV = 2;
/* 2083 */       i++;
/* 2084 */     } else if (args[i].equalsIgnoreCase("-ijcai03")) {
/* 2085 */       EnglishTrain.splitIN = 3;
/* 2086 */       EnglishTrain.splitPercent = true;
/* 2087 */       EnglishTrain.splitPoss = 1;
/* 2088 */       EnglishTrain.splitCC = 2;
/* 2089 */       EnglishTrain.unaryDT = false;
/* 2090 */       EnglishTrain.unaryRB = false;
/* 2091 */       EnglishTrain.splitAux = 0;
/* 2092 */       EnglishTrain.splitVP = 2;
/* 2093 */       EnglishTrain.splitSGapped = 4;
/* 2094 */       EnglishTrain.dominatesV = 0;
/* 2095 */       EnglishTrain.splitTMP = 1;
/* 2096 */       EnglishTrain.splitBaseNP = 1;
/* 2097 */       i++;
/* 2098 */     } else if (args[i].equalsIgnoreCase("-goodFactored")) {
/* 2099 */       EnglishTrain.splitIN = 3;
/* 2100 */       EnglishTrain.splitPercent = true;
/* 2101 */       EnglishTrain.splitPoss = 1;
/* 2102 */       EnglishTrain.splitCC = 2;
/* 2103 */       EnglishTrain.unaryDT = false;
/* 2104 */       EnglishTrain.unaryRB = false;
/* 2105 */       EnglishTrain.splitAux = 0;
/* 2106 */       EnglishTrain.splitVP = 3;
/* 2107 */       EnglishTrain.splitSGapped = 4;
/* 2108 */       EnglishTrain.dominatesV = 0;
/* 2109 */       EnglishTrain.splitTMP = 1;
/* 2110 */       EnglishTrain.splitBaseNP = 1;
/*      */       
/* 2112 */       EnglishTrain.correctTags = true;
/* 2113 */       i++;
/*      */     }
/* 2115 */     return i;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List defaultTestSentence()
/*      */   {
/* 2123 */     return java.util.Arrays.asList(new String[] { "This", "is", "just", "a", "test", "." });
/*      */   }
/*      */   
/*      */   public static void main(String[] args) {
/* 2127 */     TreebankLangParserParams tlpp = new EnglishTreebankParserParams();
/* 2128 */     edu.stanford.nlp.trees.Treebank tb = tlpp.memoryTreebank();
/* 2129 */     tb.loadPath(args[0]);
/* 2130 */     for (Tree t : tb) {
/* 2131 */       t.pennPrint();
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\EnglishTreebankParserParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */