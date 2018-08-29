/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.LabelFactory;
/*     */ import edu.stanford.nlp.ling.StringLabel;
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
/*     */ public class LabeledScoredTreeLeaf
/*     */   extends Tree
/*     */ {
/*     */   private Label label;
/*     */   private double score;
/*     */   
/*     */   public LabeledScoredTreeLeaf() {}
/*     */   
/*     */   public LabeledScoredTreeLeaf(Label label)
/*     */   {
/*  39 */     this.label = label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabeledScoredTreeLeaf(Label label, double score)
/*     */   {
/*  50 */     this.label = label;
/*  51 */     this.score = score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLeaf()
/*     */   {
/*  61 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree[] children()
/*     */   {
/*  70 */     return ZEROCHILDREN;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChildren(Tree[] children)
/*     */   {
/*  79 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  88 */     return this.label.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuffer toStringBuffer(StringBuffer sb)
/*     */   {
/*  99 */     return sb.append(toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Label label()
/*     */   {
/* 109 */     return this.label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLabel(Label label)
/*     */   {
/* 118 */     this.label = label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double score()
/*     */   {
/* 128 */     return this.score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScore(double score)
/*     */   {
/* 137 */     this.score = score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void insertDtr(Tree dtr, int position)
/*     */   {
/* 144 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addChild(int i, Tree t)
/*     */   {
/* 151 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addChild(Tree t)
/*     */   {
/* 158 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Tree setChild(int i, Tree t)
/*     */   {
/* 165 */     throw new UnsupportedOperationException();
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
/* 186 */     if (label() != null) {
/* 187 */       lf = label().labelFactory();
/*     */     } else {
/* 189 */       lf = StringLabel.factory();
/*     */     }
/* 191 */     return new LabeledScoredTreeFactory(lf);
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
/* 202 */     return LabeledScoredTreeNode.factory();
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
/* 216 */     return LabeledScoredTreeNode.factory(lf);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\LabeledScoredTreeLeaf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */