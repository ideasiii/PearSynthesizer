/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringLabel
/*     */   extends ValueLabel
/*     */ {
/*     */   private String str;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = -4153619273767524247L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringLabel() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringLabel(String str)
/*     */   {
/*  30 */     this.str = str;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringLabel(Label label)
/*     */   {
/*  41 */     this.str = label.value();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String value()
/*     */   {
/*  51 */     return this.str;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(String value)
/*     */   {
/*  61 */     this.str = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFromString(String str)
/*     */   {
/*  71 */     this.str = str;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  75 */     return this.str;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class StringLabelFactoryHolder
/*     */   {
/*  83 */     static final LabelFactory lf = new StringLabelFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabelFactory labelFactory()
/*     */   {
/*  94 */     return StringLabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LabelFactory factory()
/*     */   {
/* 104 */     return StringLabelFactoryHolder.lf;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\StringLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */