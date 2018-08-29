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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LabeledConstituent
/*     */   extends SimpleConstituent
/*     */ {
/*     */   private Label label;
/*     */   
/*     */   public LabeledConstituent() {}
/*     */   
/*     */   public LabeledConstituent(int start, int end)
/*     */   {
/*  41 */     super(start, end);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabeledConstituent(int start, int end, Label label)
/*     */   {
/*  53 */     super(start, end);
/*  54 */     this.label = label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabeledConstituent(int start, int end, String stringValue)
/*     */   {
/*  66 */     super(start, end);
/*  67 */     this.label = new StringLabel(stringValue);
/*     */   }
/*     */   
/*     */   public Label label()
/*     */   {
/*  72 */     return this.label;
/*     */   }
/*     */   
/*     */   public void setLabel(Label label) {
/*  76 */     this.label = label;
/*     */   }
/*     */   
/*     */   public void setFromString(String labelStr) {
/*  80 */     this.label = new StringLabel(labelStr);
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
/*     */   private static class LabeledConstituentLabelFactory
/*     */     implements LabelFactory
/*     */   {
/*     */     public Label newLabel(String labelStr)
/*     */     {
/*  96 */       return new LabeledConstituent(0, 0, new StringLabel(labelStr));
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
/* 108 */       return newLabel(labelStr);
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
/* 119 */       return newLabel(labelStr);
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
/* 130 */       return new LabeledConstituent(0, 0, oldLabel);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class LabelFactoryHolder
/*     */   {
/* 138 */     static final LabelFactory lf = new LabeledConstituent.LabeledConstituentLabelFactory(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabelFactory labelFactory()
/*     */   {
/* 148 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ConstituentFactoryHolder
/*     */   {
/*     */     private static class LabeledConstituentFactory
/*     */       implements ConstituentFactory
/*     */     {
/*     */       public Constituent newConstituent(int start, int end)
/*     */       {
/* 162 */         return new LabeledConstituent(start, end);
/*     */       }
/*     */       
/*     */       public Constituent newConstituent(int start, int end, Label label, double score) {
/* 166 */         return new LabeledConstituent(start, end, label);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 171 */     static final ConstituentFactory cf = new LabeledConstituentFactory(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstituentFactory constituentFactory()
/*     */   {
/* 182 */     return ConstituentFactoryHolder.cf;
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
/* 193 */     return ConstituentFactoryHolder.cf;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\LabeledConstituent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */