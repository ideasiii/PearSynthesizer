/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.LabelFactory;
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import java.io.PrintStream;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
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
/*     */ public class LabeledScoredTreeNode
/*     */   extends Tree
/*     */ {
/*     */   private Label label;
/*  26 */   private double score = NaN.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Tree[] daughterTrees;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabeledScoredTreeNode() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabeledScoredTreeNode(Label label, List<Tree> daughterTreesList)
/*     */   {
/*  45 */     this.label = label;
/*  46 */     setChildren(daughterTreesList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree[] children()
/*     */   {
/*  54 */     return this.daughterTrees;
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
/*     */   public void setChildren(Tree[] children)
/*     */   {
/*  67 */     if (children == null) {
/*  68 */       System.err.println("Warning -- you tried to set the children of a LabeledScoredTreeNode to null.\nYou really should be using a zero-length array instead.\nConsider building a LabeledScoredTreeLeaf instead.");
/*  69 */       this.daughterTrees = ZEROCHILDREN;
/*     */     } else {
/*  71 */       this.daughterTrees = children;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Label label()
/*     */   {
/*  80 */     return this.label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setLabel(Label label)
/*     */   {
/*  87 */     this.label = label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double score()
/*     */   {
/*  95 */     return this.score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setScore(double score)
/*     */   {
/* 102 */     this.score = score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuffer toStringBuffer(StringBuffer sb)
/*     */   {
/* 112 */     sb.append("(");
/* 113 */     sb.append(nodeString());
/* 114 */     for (Tree daughterTree : this.daughterTrees) {
/* 115 */       sb.append(" ");
/* 116 */       daughterTree.toStringBuffer(sb);
/*     */     }
/* 118 */     return sb.append(")");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeFactory treeFactory()
/*     */   {
/*     */     LabelFactory lf;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     LabelFactory lf;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 139 */     if (label() != null) {
/* 140 */       lf = label().labelFactory();
/*     */     } else {
/* 142 */       lf = StringLabel.factory();
/*     */     }
/* 144 */     return new LabeledScoredTreeFactory(lf);
/*     */   }
/*     */   
/*     */   private static class TreeFactoryHolder
/*     */   {
/* 149 */     static final TreeFactory tf = new LabeledScoredTreeFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TreeFactory factory()
/*     */   {
/* 160 */     return TreeFactoryHolder.tf;
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
/*     */   public static TreeFactory factory(LabelFactory lf)
/*     */   {
/* 174 */     return new LabeledScoredTreeFactory(lf);
/*     */   }
/*     */   
/* 177 */   private static NumberFormat nf = new DecimalFormat("0.000");
/*     */   
/*     */   public String nodeString() {
/* 180 */     StringBuilder buff = new StringBuilder();
/* 181 */     buff.append(super.nodeString());
/* 182 */     if (!Double.isNaN(this.score)) {
/* 183 */       buff.append(" [").append(nf.format(-this.score)).append("]");
/*     */     }
/* 185 */     return buff.toString();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\LabeledScoredTreeNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */