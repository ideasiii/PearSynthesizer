/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTag;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import edu.stanford.nlp.ling.StringLabelFactory;
/*     */ import edu.stanford.nlp.process.Function;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class TreeFunctions
/*     */ {
/*     */   private static class LabeledTreeToStringLabeledTreeFunction
/*     */     implements Function<Tree, Tree>
/*     */   {
/*  30 */     protected TreeFactory tf = new LabeledScoredTreeFactory();
/*     */     
/*     */     public Tree helper(Tree t) {
/*  33 */       if (t == null) {
/*  34 */         return null;
/*     */       }
/*  36 */       if (t.isLeaf()) {
/*  37 */         return this.tf.newLeaf(new StringLabel(t.label().value()));
/*     */       }
/*  39 */       if (t.isPreTerminal()) {
/*  40 */         return this.tf.newTreeNode(new StringLabel(t.label().value()), Collections.singletonList(helper(t.children()[0])));
/*     */       }
/*  42 */       int numKids = t.numChildren();
/*  43 */       List<Tree> children = new ArrayList(numKids);
/*  44 */       for (int k = 0; k < numKids; k++) {
/*  45 */         children.add(helper(t.children()[k]));
/*     */       }
/*  47 */       return this.tf.newTreeNode(new StringLabel(t.label().value()), children);
/*     */     }
/*     */     
/*     */     public Tree apply(Tree t) {
/*  51 */       return helper(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Function<Tree, Tree> getLabeledTreeToStringLabeledTreeFunction()
/*     */   {
/*  63 */     return new LabeledTreeToStringLabeledTreeFunction(null);
/*     */   }
/*     */   
/*     */   private static class LabeledTreeToCategoryWordTagTreeFunction
/*     */     implements Function<Tree, Tree>
/*     */   {
/*  69 */     protected TreeFactory tf = new LabeledScoredTreeFactory();
/*     */     
/*     */     public Tree helper(Tree t) {
/*  72 */       if (t == null)
/*  73 */         return null;
/*  74 */       if (t.isLeaf())
/*  75 */         return this.tf.newLeaf(new CategoryWordTag(t.label().value()));
/*  76 */       if (t.isPreTerminal()) {
/*  77 */         return this.tf.newTreeNode(new CategoryWordTag(t.label().value()), Collections.singletonList(helper(t.children()[0])));
/*     */       }
/*  79 */       int numKids = t.numChildren();
/*  80 */       List<Tree> children = new ArrayList(numKids);
/*  81 */       for (int k = 0; k < numKids; k++) {
/*  82 */         children.add(helper(t.children()[k]));
/*     */       }
/*  84 */       return this.tf.newTreeNode(new CategoryWordTag(t.label().value()), children);
/*     */     }
/*     */     
/*     */     public Tree apply(Tree o)
/*     */     {
/*  89 */       return helper(o);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Function<Tree, Tree> getLabeledTreeToCategoryWordTagTreeFunction()
/*     */   {
/* 101 */     return new LabeledTreeToCategoryWordTagTreeFunction(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 108 */     TreeFactory tf = new LabeledScoredTreeFactory();
/* 109 */     Tree stringyTree = null;
/*     */     try {
/* 111 */       stringyTree = new PennTreeReader(new StringReader("(S (VP (VBZ Try) (NP (DT this))) (. .))"), new LabeledScoredTreeFactory(new StringLabelFactory())).readTree();
/*     */     }
/*     */     catch (IOException e) {}
/* 114 */     System.out.println(stringyTree);
/* 115 */     Function<Tree, Tree> a = getLabeledTreeToCategoryWordTagTreeFunction();
/* 116 */     Tree adaptyTree = (Tree)a.apply(stringyTree);
/* 117 */     System.out.println(adaptyTree);
/* 118 */     adaptyTree.percolateHeads(new CollinsHeadFinder());
/* 119 */     System.out.println(adaptyTree);
/*     */     
/* 121 */     Function<Tree, Tree> b = getLabeledTreeToStringLabeledTreeFunction();
/* 122 */     Tree stringLabelTree = (Tree)b.apply(adaptyTree);
/* 123 */     System.out.println(stringLabelTree);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreeFunctions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */