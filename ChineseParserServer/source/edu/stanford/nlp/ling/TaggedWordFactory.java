/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaggedWordFactory
/*     */   implements LabelFactory
/*     */ {
/*     */   public static final int TAG_LABEL = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final char divider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaggedWordFactory()
/*     */   {
/*  22 */     this('/');
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
/*     */   public TaggedWordFactory(char divider)
/*     */   {
/*  36 */     this.divider = divider;
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
/*  48 */     return new TaggedWord(labelStr);
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
/*  61 */     if (options == 2) {
/*  62 */       return new TaggedWord(null, labelStr);
/*     */     }
/*  64 */     return new TaggedWord(labelStr);
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
/*     */   public Label newLabelFromString(String word)
/*     */   {
/*  81 */     int where = word.lastIndexOf(this.divider);
/*  82 */     if (where >= 0) {
/*  83 */       return new TaggedWord(word.substring(0, where), word.substring(where + 1));
/*     */     }
/*  85 */     return new TaggedWord(word);
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
/* 100 */     return new TaggedWord(oldLabel);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\TaggedWordFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */