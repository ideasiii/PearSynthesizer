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
/*     */ public class LabeledScoredConstituent
/*     */   extends LabeledConstituent
/*     */ {
/*     */   private double score;
/*     */   
/*     */   public LabeledScoredConstituent() {}
/*     */   
/*     */   public LabeledScoredConstituent(int start, int end)
/*     */   {
/*  33 */     super(start, end);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabeledScoredConstituent(int start, int end, Label label, double score)
/*     */   {
/*  45 */     super(start, end, label);
/*  46 */     this.score = score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double score()
/*     */   {
/*  57 */     return this.score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScore(double score)
/*     */   {
/*  65 */     this.score = score;
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
/*     */   private static class LabeledScoredConstituentLabelFactory
/*     */     implements LabelFactory
/*     */   {
/*     */     public Label newLabel(String labelStr)
/*     */     {
/*  83 */       return new LabeledScoredConstituent(0, 0, new StringLabel(labelStr), 0.0D);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Label newLabel(String labelStr, int options)
/*     */     {
/*  95 */       return newLabel(labelStr);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Label newLabelFromString(String labelStr)
/*     */     {
/* 106 */       return newLabel(labelStr);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Label newLabel(Label oldLabel)
/*     */     {
/* 117 */       return new LabeledScoredConstituent(0, 0, oldLabel, 0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class LabelFactoryHolder
/*     */   {
/* 125 */     static final LabelFactory lf = new LabeledScoredConstituent.LabeledScoredConstituentLabelFactory(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabelFactory labelFactory()
/*     */   {
/* 135 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ConstituentFactoryHolder
/*     */   {
/* 142 */     private static final ConstituentFactory cf = new LabeledScoredConstituentFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstituentFactory constituentFactory()
/*     */   {
/* 154 */     return ConstituentFactoryHolder.cf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConstituentFactory factory()
/*     */   {
/* 165 */     return ConstituentFactoryHolder.cf;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\LabeledScoredConstituent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */