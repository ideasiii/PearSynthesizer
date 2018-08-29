/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WordTagFactory
/*     */   implements LabelFactory
/*     */ {
/*     */   private final char divider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordTagFactory()
/*     */   {
/*  23 */     this('/');
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
/*     */   public WordTagFactory(char divider)
/*     */   {
/*  37 */     this.divider = divider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Label newLabel(String labelStr)
/*     */   {
/*  49 */     return new WordTag(labelStr);
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
/*     */   public Label newLabel(String labelStr, int options)
/*     */   {
/*  62 */     if (options == 2) {
/*  63 */       return new WordTag(null, labelStr);
/*     */     }
/*  65 */     return new WordTag(labelStr);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public Label newLabelFromString(String word)
/*     */   {
/*  83 */     int where = word.lastIndexOf(this.divider);
/*  84 */     if (where >= 0) {
/*  85 */       return new WordTag(word.substring(0, where), word.substring(where + 1));
/*     */     }
/*  87 */     return new WordTag(word);
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
/*     */   public Label newLabel(Label oldLabel)
/*     */   {
/* 102 */     return new WordTag(oldLabel);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\WordTagFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */