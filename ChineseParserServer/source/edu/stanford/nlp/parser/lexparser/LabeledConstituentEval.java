/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.trees.Constituent;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class LabeledConstituentEval extends AbstractEval
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  21 */   private edu.stanford.nlp.trees.ConstituentFactory cf = new edu.stanford.nlp.trees.LabeledScoredConstituentFactory();
/*  22 */   private TreeFactory tf = new LabeledScoredTreeFactory();
/*     */   private TreebankLanguagePack tlp;
/*     */   
/*     */   protected Tree stripLeaves(Tree tree) {
/*  26 */     if (tree.isLeaf()) {
/*  27 */       return null;
/*     */     }
/*  29 */     if (tree.isPreTerminal()) {
/*  30 */       return this.tf.newLeaf(tree.label());
/*     */     }
/*  32 */     int numKids = tree.numChildren();
/*  33 */     List<Tree> children = new ArrayList(numKids);
/*  34 */     for (int cNum = 0; cNum < numKids; cNum++) {
/*  35 */       children.add(stripLeaves(tree.getChild(cNum)));
/*     */     }
/*  37 */     return this.tf.newTreeNode(tree.label(), children);
/*     */   }
/*     */   
/*     */ 
/*     */   Set makeObjects(Tree tree)
/*     */   {
/*  43 */     Tree noLeafTree = stripLeaves(tree);
/*  44 */     Set set = new java.util.HashSet();
/*  45 */     if (noLeafTree == null) {
/*  46 */       return set;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  51 */     if (this.tlp.isStartSymbol(noLeafTree.label().value())) {
/*  52 */       for (int i = 0; i < noLeafTree.children().length - 1; i++) {
/*  53 */         set.addAll(noLeafTree.children()[i].constituents(this.cf));
/*     */       }
/*     */     } else {
/*  56 */       set.addAll(noLeafTree.constituents(this.cf));
/*     */     }
/*  58 */     return set;
/*     */   }
/*     */   
/*     */   public void evaluate(Tree guess, Tree gold, PrintWriter pw) {
/*  62 */     if (guess.yield().size() != gold.yield().size()) {
/*  63 */       pw.println("Warning: yield differs:");
/*  64 */       pw.println("Guess: " + guess.yield());
/*  65 */       pw.println("Gold: " + gold.yield());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */     super.evaluate(guess, gold, pw);
/*     */   }
/*     */   
/*     */   public LabeledConstituentEval(String str, boolean runningAverages, TreebankLanguagePack tlp) {
/*  79 */     super(str, runningAverages);
/*  80 */     this.tlp = tlp;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  84 */     TreebankLangParserParams tlpp = new EnglishTreebankParserParams();
/*     */     
/*  86 */     if ((args.length < 2) || (args.length > 3)) {
/*  87 */       System.err.println("usage: LabeledConstituentEval gold gues [TLPP]");
/*  88 */       return;
/*     */     }
/*  90 */     if (args.length > 2) {
/*     */       try {
/*  92 */         Object o = Class.forName(args[2]).newInstance();
/*  93 */         tlpp = (TreebankLangParserParams)o;
/*     */       } catch (Exception e) {
/*  95 */         System.err.println("Couldn't instantiate: " + args[2]);
/*     */       }
/*     */     }
/*  98 */     Treebank tb1 = tlpp.diskTreebank();
/*  99 */     Treebank tb2 = tlpp.diskTreebank();
/* 100 */     PrintWriter pw = tlpp.pw();
/*     */     
/* 102 */     LabeledConstituentEval lce = new LabeledConstituentEval("LP/LR", true, tlpp.treebankLanguagePack());
/*     */     
/* 104 */     TreeTransformer tc = tlpp.collinizer();
/*     */     
/* 106 */     tb1.loadPath(args[0]);
/* 107 */     tb2.loadPath(args[1]);
/* 108 */     System.err.println(tb1.textualSummary());
/* 109 */     System.err.println(tb2.textualSummary());
/*     */     
/* 111 */     Iterator<Tree> tb2it = tb2.iterator();
/* 112 */     for (Tree t : tb1) {
/* 113 */       if (tb2it.hasNext()) {
/* 114 */         Tree tGuess = (Tree)tb2it.next();
/* 115 */         lce.evaluate(tc.transformTree(t), tc.transformTree(tGuess), pw);
/*     */       }
/*     */     }
/* 118 */     pw.println();
/* 119 */     lce.display(true, pw);
/*     */   }
/*     */   
/*     */   public static class CBEval
/*     */     extends LabeledConstituentEval
/*     */   {
/* 125 */     private double cb = 0.0D;
/* 126 */     private double num = 0.0D;
/* 127 */     private double zeroCB = 0.0D;
/*     */     
/*     */     protected void checkCrossing(Set<Constituent> s1, Set<Constituent> s2) {
/* 130 */       double c = 0.0D;
/* 131 */       for (Constituent constit : s1) {
/* 132 */         if (constit.crosses(s2)) {
/* 133 */           c += 1.0D;
/*     */         }
/*     */       }
/* 136 */       if (c == 0.0D) {
/* 137 */         this.zeroCB += 1.0D;
/*     */       }
/* 139 */       this.cb += c;
/* 140 */       this.num += 1.0D;
/*     */     }
/*     */     
/*     */     public void evaluate(Tree t1, Tree t2, PrintWriter pw) {
/* 144 */       Set b1 = makeObjects(t1);
/* 145 */       Set b2 = makeObjects(t2);
/* 146 */       checkCrossing(b1, b2);
/* 147 */       if ((pw != null) && (this.runningAverages)) {
/* 148 */         pw.println("AvgCB: " + (int)(10000.0D * this.cb / this.num) / 100.0D + " ZeroCB: " + (int)(10000.0D * this.zeroCB / this.num) / 100.0D + " N: " + getNum());
/*     */       }
/*     */     }
/*     */     
/*     */     public void display(boolean verbose, PrintWriter pw)
/*     */     {
/* 154 */       pw.println(this.str + " AvgCB: " + (int)(10000.0D * this.cb / this.num) / 100.0D + " ZeroCB: " + (int)(10000.0D * this.zeroCB / this.num) / 100.0D);
/*     */     }
/*     */     
/*     */     public CBEval(String str, boolean runningAverages, TreebankLanguagePack tlp)
/*     */     {
/* 159 */       super(runningAverages, tlp);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\LabeledConstituentEval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */