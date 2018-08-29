/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CollinsPuncTransformer
/*     */   implements TreeTransformer
/*     */ {
/*     */   private TreebankLanguagePack tlp;
/*     */   
/*     */   boolean isPunc(Tree t)
/*     */   {
/*  25 */     if (t.isPreTerminal()) {
/*  26 */       String s = t.label().value();
/*  27 */       if (this.tlp.isEvalBIgnoredPunctuationTag(s)) {
/*  28 */         return true;
/*     */       }
/*     */     }
/*  31 */     return false;
/*     */   }
/*     */   
/*     */   static LinkedList<Tree> preTerms(Tree t) {
/*  35 */     LinkedList<Tree> l = new LinkedList();
/*  36 */     preTermHelper(t, l);
/*  37 */     return l;
/*     */   }
/*     */   
/*     */   static void preTermHelper(Tree t, List<Tree> l) {
/*  41 */     if (t.isLeaf()) {
/*  42 */       return;
/*     */     }
/*  44 */     if (t.isPreTerminal()) {
/*  45 */       l.add(t);
/*  46 */       return;
/*     */     }
/*  48 */     Tree[] children = t.children();
/*  49 */     for (Tree child : children) {
/*  50 */       preTermHelper(child, l);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   Tree transformRoot(Tree tree, TreeFactory tf)
/*     */   {
/*  58 */     if (tree.label().toString().startsWith("ROOT")) {
/*  59 */       return tf.newTreeNode(tree.label(), Collections.singletonList(transformNode(tree.children()[0], tf)));
/*     */     }
/*  61 */     return transformNode(tree, tf);
/*     */   }
/*     */   
/*     */   Tree transformNode(Tree tree, TreeFactory tf) {
/*  65 */     if (tree.isLeaf()) {
/*  66 */       return tf.newLeaf(tree.label());
/*     */     }
/*  68 */     if (tree.isPreTerminal()) {
/*  69 */       return tf.newTreeNode(tree.label(), Collections.singletonList(tf.newLeaf(tree.children()[0].label())));
/*     */     }
/*  71 */     List<Tree> children = tree.getChildrenAsList();
/*  72 */     LinkedList<Tree> newChildren = new LinkedList();
/*     */     
/*  74 */     for (Tree child : children) {
/*  75 */       LinkedList<Tree> preTerms = preTerms(child);
/*  76 */       while ((!preTerms.isEmpty()) && (isPunc((Tree)preTerms.getFirst()))) {
/*  77 */         newChildren.add(preTerms.getFirst());
/*  78 */         preTerms.removeFirst();
/*     */       }
/*  80 */       Tree newChild = transformNode(child, tf);
/*  81 */       LinkedList<Tree> temp = new LinkedList();
/*  82 */       if (newChild.children().length > 0) {
/*  83 */         newChildren.add(newChild);
/*     */       }
/*  85 */       while ((!preTerms.isEmpty()) && (isPunc((Tree)preTerms.getLast()))) {
/*  86 */         temp.addFirst(preTerms.getLast());
/*  87 */         preTerms.removeLast();
/*     */       }
/*  89 */       newChildren.addAll(temp);
/*     */     }
/*     */     
/*  92 */     while ((!newChildren.isEmpty()) && (isPunc((Tree)newChildren.getFirst()))) {
/*  93 */       newChildren.removeFirst();
/*     */     }
/*  95 */     while ((!newChildren.isEmpty()) && (isPunc((Tree)newChildren.getLast()))) {
/*  96 */       newChildren.removeLast();
/*     */     }
/*  98 */     return tf.newTreeNode(tree.label(), newChildren);
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
/*     */   public Tree transformTree(Tree tree)
/*     */   {
/* 111 */     return transformRoot(tree, tree.treeFactory());
/*     */   }
/*     */   
/*     */   public CollinsPuncTransformer(TreebankLanguagePack tlp) {
/* 115 */     this.tlp = tlp;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\CollinsPuncTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */