/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import java.io.PrintStream;
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
/*     */ 
/*     */ public class SimpleTree
/*     */   extends Tree
/*     */ {
/*     */   private Tree[] daughterTrees;
/*     */   
/*     */   public SimpleTree()
/*     */   {
/*  25 */     this.daughterTrees = ZEROCHILDREN;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleTree(Label label)
/*     */   {
/*  35 */     this();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleTree(Label label, List daughterTreesList)
/*     */   {
/*  46 */     setChildren(daughterTreesList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree[] children()
/*     */   {
/*  55 */     return this.daughterTrees;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChildren(Tree[] children)
/*     */   {
/*  66 */     if (children == null) {
/*  67 */       System.err.println("Warning -- you tried to set the children of a SimpleTree to null.\nYou should be really using a zero-length array instead.");
/*  68 */       this.daughterTrees = ZEROCHILDREN;
/*     */     } else {
/*  70 */       this.daughterTrees = children;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class TreeFactoryHolder
/*     */   {
/*  77 */     static final TreeFactory tf = new SimpleTreeFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeFactory treeFactory()
/*     */   {
/*  89 */     return TreeFactoryHolder.tf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TreeFactory factory()
/*     */   {
/* 101 */     return TreeFactoryHolder.tf;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\SimpleTree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */