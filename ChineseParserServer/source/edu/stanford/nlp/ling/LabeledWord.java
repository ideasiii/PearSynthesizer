/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LabeledWord
/*     */   extends Word
/*     */ {
/*     */   private Label tag;
/*     */   
/*     */ 
/*  12 */   private static String divider = "/";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = -7252006452127051085L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabeledWord() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabeledWord(String word)
/*     */   {
/*  28 */     super(word);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabeledWord(String word, Label tag)
/*     */   {
/*  38 */     super(word);
/*  39 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public LabeledWord(Label word, Label tag) {
/*  43 */     super(word);
/*  44 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public Label tag() {
/*  48 */     return this.tag;
/*     */   }
/*     */   
/*     */   public void setTag(Label tag) {
/*  52 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  56 */     return word() + divider + this.tag;
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
/*     */   public static void setDivider(String divider)
/*     */   {
/*  70 */     divider = divider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class LabelFactoryHolder
/*     */   {
/*  78 */     private static final LabelFactory lf = new TaggedWordFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabelFactory labelFactory()
/*     */   {
/*  90 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LabelFactory factory()
/*     */   {
/* 100 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\LabeledWord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */