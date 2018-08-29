/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ import edu.stanford.nlp.process.Morphology;
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
/*     */ public class WordLemmaTagFactory
/*     */   implements LabelFactory
/*     */ {
/*     */   public static final int LEMMA_LABEL = 1;
/*     */   public static final int TAG_LABEL = 2;
/*     */   private final char divider;
/*     */   
/*     */   public WordLemmaTagFactory()
/*     */   {
/*  25 */     this('/');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordLemmaTagFactory(char divider)
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
/*  49 */     return new WordLemmaTag(labelStr);
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
/*  62 */     if (options == 2)
/*  63 */       return new WordLemmaTag(null, null, labelStr);
/*  64 */     if (options == 1) {
/*  65 */       return new WordLemmaTag(null, labelStr, null);
/*     */     }
/*  67 */     return new WordLemmaTag(labelStr);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public Label newLabelFromString(String labelStr)
/*     */   {
/*  88 */     int first = labelStr.indexOf(this.divider);
/*  89 */     int second = labelStr.lastIndexOf(this.divider);
/*  90 */     if (first == second)
/*  91 */       return new WordLemmaTag(labelStr.substring(0, first), Morphology.stemStatic(labelStr.substring(0, first), labelStr.substring(first + 1)).word(), labelStr.substring(first + 1));
/*  92 */     if (first >= 0) {
/*  93 */       return new WordLemmaTag(labelStr.substring(0, first), labelStr.substring(first + 1, second), labelStr.substring(second + 1));
/*     */     }
/*  95 */     return new WordLemmaTag(labelStr);
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
/*     */   public Label newLabel(Label oldLabel)
/*     */   {
/* 109 */     return new WordLemmaTag(oldLabel);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\WordLemmaTagFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */