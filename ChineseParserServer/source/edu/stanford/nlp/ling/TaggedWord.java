/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TaggedWord
/*     */   extends Word
/*     */   implements HasTag
/*     */ {
/*     */   private String tag;
/*     */   
/*     */ 
/*     */ 
/*  14 */   private static String divider = "/";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = -7252006452127051085L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaggedWord() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaggedWord(String word)
/*     */   {
/*  30 */     super(word);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaggedWord(String word, String tag)
/*     */   {
/*  40 */     super(word);
/*  41 */     this.tag = tag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaggedWord(Label oldLabel)
/*     */   {
/*  51 */     super(oldLabel.value());
/*  52 */     if ((oldLabel instanceof HasTag)) {
/*  53 */       this.tag = ((HasTag)oldLabel).tag();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaggedWord(Label word, Label tag)
/*     */   {
/*  65 */     super(word);
/*  66 */     this.tag = tag.value();
/*     */   }
/*     */   
/*     */   public String tag() {
/*  70 */     return this.tag;
/*     */   }
/*     */   
/*     */   public void setTag(String tag) {
/*  74 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  78 */     return word() + divider + this.tag;
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
/*     */   public static void setDivider(String divider)
/*     */   {
/*  93 */     divider = divider;
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
/*     */   public void setFromString(String taggedWord)
/*     */   {
/* 110 */     int where = taggedWord.lastIndexOf(divider);
/* 111 */     if (where >= 0) {
/* 112 */       setWord(taggedWord.substring(0, where));
/* 113 */       setTag(taggedWord.substring(where + 1));
/*     */     } else {
/* 115 */       setWord(taggedWord);
/* 116 */       setTag(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class LabelFactoryHolder
/*     */   {
/* 126 */     private static final LabelFactory lf = new TaggedWordFactory();
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
/* 138 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LabelFactory factory()
/*     */   {
/* 148 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\TaggedWord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */