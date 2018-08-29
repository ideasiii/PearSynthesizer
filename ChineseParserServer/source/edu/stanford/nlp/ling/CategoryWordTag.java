/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CategoryWordTag
/*     */   extends StringLabel
/*     */   implements HasCategory, HasWord, HasTag
/*     */ {
/*     */   private static final long serialVersionUID = -745085381666943254L;
/*     */   
/*     */ 
/*     */ 
/*     */   protected String word;
/*     */   
/*     */ 
/*     */ 
/*     */   protected String tag;
/*     */   
/*     */ 
/*     */ 
/*  22 */   public static boolean printWordTag = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  29 */   public static boolean suppressTerminalDetails = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CategoryWordTag() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CategoryWordTag(String label)
/*     */   {
/*  42 */     super(label);
/*     */   }
/*     */   
/*     */   public CategoryWordTag(String category, String word, String tag) {
/*  46 */     super(category);
/*  47 */     this.word = word;
/*  48 */     this.tag = tag;
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
/*     */   public CategoryWordTag(Label oldLabel)
/*     */   {
/*  61 */     super(oldLabel.value());
/*  62 */     if ((oldLabel instanceof HasTag)) {
/*  63 */       this.tag = ((HasTag)oldLabel).tag();
/*     */     }
/*  65 */     if ((oldLabel instanceof HasWord)) {
/*  66 */       this.word = ((HasWord)oldLabel).word();
/*     */     }
/*     */   }
/*     */   
/*     */   public String category() {
/*  71 */     return value();
/*     */   }
/*     */   
/*     */   public void setCategory(String category) {
/*  75 */     setValue(category);
/*     */   }
/*     */   
/*     */   public String word() {
/*  79 */     return this.word;
/*     */   }
/*     */   
/*     */   public void setWord(String word) {
/*  83 */     this.word = word;
/*     */   }
/*     */   
/*     */   public String tag() {
/*  87 */     return this.tag;
/*     */   }
/*     */   
/*     */   public void setTag(String tag) {
/*  91 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public void setCategoryWordTag(String category, String word, String tag) {
/*  95 */     setCategory(category);
/*  96 */     setWord(word);
/*  97 */     setTag(tag);
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
/*     */   public String toString()
/*     */   {
/* 111 */     if (category() != null) {
/* 112 */       if ((word() == null) || (tag() == null) || (!printWordTag) || ((suppressTerminalDetails) && ((word().equals(category())) || (tag().equals(category()))))) {
/* 113 */         return category();
/*     */       }
/* 115 */       return category() + "[" + word() + "/" + tag() + "]";
/*     */     }
/*     */     
/* 118 */     if (tag() == null) {
/* 119 */       return word();
/*     */     }
/* 121 */     return word() + "/" + tag();
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
/*     */   public String toString(String mode)
/*     */   {
/* 135 */     if ("full".equals(mode)) {
/* 136 */       return category() + "[" + word() + "/" + tag() + "]";
/*     */     }
/* 138 */     return toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFromString(String labelStr)
/*     */   {
/* 147 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   private static class LabelFactoryHolder
/*     */   {
/* 153 */     private static final LabelFactory lf = new CategoryWordTagFactory();
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
/* 164 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LabelFactory factory()
/*     */   {
/* 174 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\CategoryWordTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */