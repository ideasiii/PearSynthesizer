/*     */ package edu.stanford.nlp.trees.tregex;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.StringLabelFactory;
/*     */ import edu.stanford.nlp.trees.DiskTreebank;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.MemoryTreebank;
/*     */ import edu.stanford.nlp.trees.PennTreeReader;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeNormalizer;
/*     */ import edu.stanford.nlp.trees.TreeReader;
/*     */ import edu.stanford.nlp.trees.TreeReaderFactory;
/*     */ import edu.stanford.nlp.trees.TreeVisitor;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.util.Timing;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeMatcher
/*     */ {
/*     */   Tree root;
/*     */   TreePattern rootPattern;
/*     */   TreePatternIterator i;
/*     */   TreePattern currentPatternNode;
/*     */   List stack;
/*     */   Iterator currentTreeIterator;
/*     */   static Treebank treebank;
/*     */   
/*     */   TreeMatcher(Tree t, TreePattern p)
/*     */   {
/*  62 */     this.root = t;
/*  63 */     this.rootPattern = p;
/*  64 */     reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matches(Tree t)
/*     */   {
/*  83 */     reset();
/*  84 */     if (!this.rootPattern.descriptionPattern.matcher(t.label().value()).matches()) {
/*  85 */       return false;
/*     */     }
/*  87 */     while (find()) {
/*  88 */       if (t == getMatch()) {
/*  89 */         return true;
/*     */       }
/*     */     }
/*  92 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeMatcher reset()
/*     */   {
/* 100 */     this.i = this.rootPattern.iterator();
/* 101 */     this.currentPatternNode = this.i.next();
/* 102 */     this.stack = new ArrayList(this.rootPattern.size());
/* 103 */     this.currentTreeIterator = this.root.iterator();
/* 104 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean find()
/*     */   {
/*     */     for (;;)
/*     */     {
/* 114 */       if (!this.currentTreeIterator.hasNext()) {
/* 115 */         this.i.previous();
/* 116 */         while (!this.currentTreeIterator.hasNext()) {
/* 117 */           if (!this.i.hasPrevious()) {
/* 118 */             return false;
/*     */           }
/* 120 */           this.currentPatternNode = this.i.previous();
/* 121 */           this.currentTreeIterator = ((Iterator)this.stack.remove(this.stack.size() - 1));
/* 122 */           if (Verbose.verbose) {
/* 123 */             System.out.println("###Stack: " + this.stack);
/*     */           }
/*     */         }
/*     */         
/* 127 */         this.i.next();
/*     */       }
/* 129 */       Tree t = (Tree)this.currentTreeIterator.next();
/*     */       
/*     */ 
/*     */ 
/* 133 */       if (Verbose.verbose) {
/* 134 */         System.out.println("###" + t + "\t" + this.currentPatternNode.relation + "(" + (this.currentPatternNode.parent == null ? "--" : this.currentPatternNode.parent.description) + "," + this.currentPatternNode.description + ")");
/*     */       }
/*     */       
/* 137 */       boolean descriptionMatchesNode = this.currentPatternNode.descriptionPattern.matcher(t.label().value()).matches();
/* 138 */       if (((descriptionMatchesNode) && (!this.currentPatternNode.negatedDescription)) || ((this.currentPatternNode.negatedDescription) && (!descriptionMatchesNode) && ((this.currentPatternNode.parent == null) || (this.currentPatternNode.relation.satisfies(this.currentPatternNode.parent.node(), t, this.root))))) {
/* 139 */         if (Verbose.verbose) {
/* 140 */           System.out.println("###matched node description " + this.currentPatternNode.descriptionPattern.pattern() + " at node " + t);
/*     */         }
/* 142 */         this.currentPatternNode.namesToNodes.put(this.currentPatternNode.name, t);
/* 143 */         if (!this.i.hasNext()) {
/* 144 */           return true;
/*     */         }
/* 146 */         this.currentPatternNode = this.i.next();
/* 147 */         this.stack.add(this.currentTreeIterator);
/* 148 */         if (Verbose.verbose) {
/* 149 */           System.out.println("###Stack: " + this.stack);
/* 150 */           System.out.println("Last iterator at: " + t);
/*     */         }
/* 152 */         this.currentTreeIterator = this.currentPatternNode.relation.searchNodeIterator(this.currentPatternNode.parent.node(), this.root);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree getMatch()
/*     */   {
/* 162 */     return this.rootPattern.node();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree getNode(Object name)
/*     */   {
/* 170 */     if (Verbose.verbose) {
/* 171 */       System.out.println("###Here's the names to nodes map:\n" + this.rootPattern.namesToNodes);
/*     */     }
/* 173 */     return (Tree)this.rootPattern.namesToNodes.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 202 */     if (args.length < 2) {
/* 203 */       System.err.println("Usage: java edu.stanford.nlp.trees.tregex.TreeMatcher [-T] [-C] [-w] [-f] pattern [handle] filepath");
/* 204 */       System.exit(0);
/*     */     }
/*     */     
/* 207 */     String tnClass = null;
/* 208 */     int i = 0;
/* 209 */     while ((i < args.length) && (args[i].charAt(0) == '-')) {
/* 210 */       if (args[i].equals("-T")) {
/* 211 */         Verbose.printTree = true;
/* 212 */       } else if (args[i].equals("-C")) {
/* 213 */         Verbose.printMatches = false;
/* 214 */       } else if (args[i].equals("-w")) {
/* 215 */         Verbose.printWholeTree = true;
/* 216 */       } else if (args[i].equals("-f")) {
/* 217 */         Verbose.printFilename = true;
/* 218 */       } else if ((args[i].equals("-tn")) && (i + 1 < args.length)) {
/* 219 */         tnClass = args[(++i)];
/*     */       } else {
/* 221 */         System.err.println("Unknown flag: " + args[i]);
/*     */       }
/* 223 */       i++;
/*     */     }
/*     */     try
/*     */     {
/* 227 */       TreePattern p = TreePattern.compile(args[(i++)]);
/* 228 */       System.err.println("Parsed pattern: " + p.pattern());
/*     */       
/* 230 */       int j = i + 1;
/* 231 */       if (j >= args.length) {
/* 232 */         j--;
/*     */       }
/* 234 */       if (j < args.length) {
/* 235 */         System.err.println("Reading trees from file(s) " + args[j]);
/*     */         
/* 237 */         TreeNormalizer tn = null;
/* 238 */         if (tnClass != null)
/*     */           try {
/* 240 */             tn = (TreeNormalizer)Class.forName(tnClass).newInstance();
/*     */           } catch (Exception e) {}
/*     */         TreeReaderFactory trf;
/*     */         TreeReaderFactory trf;
/* 244 */         if (tn == null) {
/* 245 */           trf = new TRegexTreeReaderFactory();
/*     */         } else {
/* 247 */           trf = new TRegexTreeReaderFactory(tn);
/*     */         }
/* 249 */         treebank = new DiskTreebank(trf);
/* 250 */         treebank.loadPath(args[j], null, true);
/*     */       } else {
/* 252 */         System.err.println("Using default tree");
/* 253 */         Tree t = Tree.valueOf("(VP (VP (VBZ Try) (NP (NP (DT this) (NN wine)) (CC and) (NP (DT these) (NNS snails)))) (PUNCT .))");
/* 254 */         treebank = new MemoryTreebank();
/* 255 */         treebank.add(t);
/*     */       }
/* 257 */       String label = null;
/* 258 */       if (j != i) {
/* 259 */         label = args[i];
/*     */       }
/* 261 */       TRegexTreeVisitor vis = new TRegexTreeVisitor(p, label);
/*     */       
/* 263 */       treebank.apply(vis);
/* 264 */       Timing.endTime();
/* 265 */       System.err.println("There were " + vis.numMatches() + " matches in total.");
/*     */     } catch (IOException e) {
/* 267 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   static class TRegexTreeVisitor implements TreeVisitor
/*     */   {
/*     */     TreePattern p;
/*     */     String node;
/*     */     int numMatches;
/*     */     
/*     */     TRegexTreeVisitor(TreePattern p, String node)
/*     */     {
/* 279 */       this.p = p;
/* 280 */       this.node = node;
/*     */     }
/*     */     
/*     */     public void visitTree(Tree t) {
/* 284 */       if (TreeMatcher.Verbose.printTree) {
/* 285 */         System.out.println("Next tree read:");
/* 286 */         t.pennPrint();
/*     */       }
/* 288 */       TreeMatcher match = this.p.matcher(t);
/* 289 */       while (match.find()) {
/* 290 */         this.numMatches += 1;
/* 291 */         if ((TreeMatcher.Verbose.printFilename) && ((TreeMatcher.treebank instanceof DiskTreebank))) {
/* 292 */           DiskTreebank dtb = (DiskTreebank)TreeMatcher.treebank;
/* 293 */           System.out.print("# ");
/* 294 */           System.out.println(dtb.getCurrentFile());
/*     */         }
/* 296 */         if (TreeMatcher.Verbose.printMatches) {
/* 297 */           if (TreeMatcher.Verbose.printTree) {
/* 298 */             System.out.println("Found a full match:");
/*     */           }
/* 300 */           if (TreeMatcher.Verbose.printWholeTree) {
/* 301 */             t.pennPrint();
/* 302 */           } else if (this.node != null) {
/* 303 */             if (TreeMatcher.Verbose.printTree) {
/* 304 */               System.out.println("Here's the node you were interested in:");
/*     */             }
/* 306 */             match.getNode(this.node).pennPrint();
/*     */           } else {
/* 308 */             match.getMatch().pennPrint();
/*     */           }
/* 310 */           System.out.println();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public int numMatches() {
/* 316 */       return this.numMatches;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TRegexTreeReaderFactory
/*     */     implements TreeReaderFactory
/*     */   {
/*     */     private TreeNormalizer tn;
/*     */     
/*     */     public TRegexTreeReaderFactory()
/*     */     {
/* 327 */       this(new TreeNormalizer() {
/*     */         public String normalizeNonterminal(String str) {
/* 329 */           if (str == null) {
/* 330 */             return "";
/*     */           }
/* 332 */           return str;
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     public TRegexTreeReaderFactory(TreeNormalizer tn)
/*     */     {
/* 339 */       this.tn = tn;
/*     */     }
/*     */     
/*     */     public TreeReader newTreeReader(Reader in) {
/* 343 */       return new PennTreeReader(new BufferedReader(in), new LabeledScoredTreeFactory(new StringLabelFactory()), this.tn);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static class Verbose
/*     */   {
/* 350 */     static boolean verbose = false;
/* 351 */     static boolean printTree = false;
/* 352 */     static boolean printWholeTree = false;
/* 353 */     static boolean printMatches = true;
/* 354 */     static boolean printFilename = false;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\TreeMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */