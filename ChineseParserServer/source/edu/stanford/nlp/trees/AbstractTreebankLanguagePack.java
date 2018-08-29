/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.process.Function;
/*     */ import edu.stanford.nlp.process.WhitespaceTokenizer;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import edu.stanford.nlp.util.Filters;
/*     */ import java.io.Serializable;
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
/*     */ public abstract class AbstractTreebankLanguagePack
/*     */   implements TreebankLanguagePack, Serializable
/*     */ {
/*     */   public static final String DEFAULT_ENCODING = "UTF-8";
/*     */   
/*     */   public abstract String[] punctuationTags();
/*     */   
/*     */   public abstract String[] punctuationWords();
/*     */   
/*     */   public abstract String[] sentenceFinalPunctuationTags();
/*     */   
/*     */   public String[] evalBIgnoredPunctuationTags()
/*     */   {
/*  70 */     return punctuationTags();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPunctuationTag(String str)
/*     */   {
/*  81 */     return this.punctTagStringAcceptFilter.accept(str);
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
/*     */   public boolean isPunctuationWord(String str)
/*     */   {
/*  94 */     return this.punctWordStringAcceptFilter.accept(str);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSentenceFinalPunctuationTag(String str)
/*     */   {
/* 105 */     return this.sFPunctTagStringAcceptFilter.accept(str);
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
/*     */   public boolean isEvalBIgnoredPunctuationTag(String str)
/*     */   {
/* 120 */     return this.eIPunctTagStringAcceptFilter.accept(str);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Filter<String> punctuationTagAcceptFilter()
/*     */   {
/* 131 */     return this.punctTagStringAcceptFilter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Filter<String> punctuationTagRejectFilter()
/*     */   {
/* 142 */     return Filters.notFilter(this.punctTagStringAcceptFilter);
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
/*     */   public Filter<String> punctuationWordAcceptFilter()
/*     */   {
/* 155 */     return this.punctWordStringAcceptFilter;
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
/*     */   public Filter<String> punctuationWordRejectFilter()
/*     */   {
/* 168 */     return Filters.notFilter(this.punctWordStringAcceptFilter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Filter<String> sentenceFinalPunctuationTagAcceptFilter()
/*     */   {
/* 179 */     return this.sFPunctTagStringAcceptFilter;
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
/*     */   public Filter<String> evalBIgnoredPunctuationTagAcceptFilter()
/*     */   {
/* 194 */     return this.eIPunctTagStringAcceptFilter;
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
/*     */   public Filter<String> evalBIgnoredPunctuationTagRejectFilter()
/*     */   {
/* 208 */     return Filters.notFilter(this.eIPunctTagStringAcceptFilter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 219 */     return "UTF-8";
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
/*     */   public char[] labelAnnotationIntroducingCharacters()
/*     */   {
/* 234 */     return new char[0];
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
/*     */   private int postBasicCategoryIndex(String category)
/*     */   {
/* 251 */     boolean sawAtZero = false;
/* 252 */     char seenAtZero = '\000';
/* 253 */     int i = 0;
/* 254 */     for (int leng = category.length(); i < leng; i++) {
/* 255 */       char ch = category.charAt(i);
/* 256 */       if (isLabelAnnotationIntroducingCharacter(ch)) {
/* 257 */         if (i == 0) {
/* 258 */           sawAtZero = true;
/* 259 */           seenAtZero = ch;
/* 260 */         } else { if ((!sawAtZero) || (ch != seenAtZero)) break;
/* 261 */           sawAtZero = false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 267 */     return i;
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
/*     */   public String basicCategory(String category)
/*     */   {
/* 286 */     if (category == null) {
/* 287 */       return null;
/*     */     }
/* 289 */     return category.substring(0, postBasicCategoryIndex(category));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Function<String, String> getBasicCategoryFunction()
/*     */   {
/* 299 */     new Function() {
/*     */       public String apply(String in) {
/* 301 */         return AbstractTreebankLanguagePack.this.basicCategory(in);
/*     */       }
/*     */     };
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
/*     */   public String categoryAndFunction(String category)
/*     */   {
/* 320 */     if (category == null) {
/* 321 */       return null;
/*     */     }
/* 323 */     String catFunc = category.substring(0);
/* 324 */     int i = lastIndexOfNumericTag(catFunc);
/* 325 */     while (i >= 0) {
/* 326 */       catFunc = catFunc.substring(0, i);
/* 327 */       i = lastIndexOfNumericTag(catFunc);
/*     */     }
/* 329 */     return catFunc;
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
/*     */   private int lastIndexOfNumericTag(String category)
/*     */   {
/* 345 */     if (category == null) {
/* 346 */       return -1;
/*     */     }
/* 348 */     int last = -1;
/* 349 */     for (int i = category.length() - 1; i >= 0; i--) {
/* 350 */       if (isLabelAnnotationIntroducingCharacter(category.charAt(i))) {
/* 351 */         boolean onlyDigitsFollow = false;
/* 352 */         for (int j = i + 1; j < category.length(); j++) {
/* 353 */           onlyDigitsFollow = true;
/* 354 */           if (!Character.isDigit(category.charAt(j))) {
/* 355 */             onlyDigitsFollow = false;
/* 356 */             break;
/*     */           }
/*     */         }
/* 359 */         if (onlyDigitsFollow) {
/* 360 */           last = i;
/*     */         }
/*     */       }
/*     */     }
/* 364 */     return last;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Function<String, String> getCategoryAndFunctionFunction()
/*     */   {
/* 374 */     new Function() {
/*     */       public String apply(String in) {
/* 376 */         return AbstractTreebankLanguagePack.this.categoryAndFunction(in);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLabelAnnotationIntroducingCharacter(char ch)
/*     */   {
/* 390 */     char[] cutChars = labelAnnotationIntroducingCharacters();
/* 391 */     for (char cutChar : cutChars) {
/* 392 */       if (ch == cutChar) {
/* 393 */         return true;
/*     */       }
/*     */     }
/* 396 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isStartSymbol(String str)
/*     */   {
/* 406 */     return this.startSymbolAcceptFilter.accept(str);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Filter<String> startSymbolAcceptFilter()
/*     */   {
/* 417 */     return this.startSymbolAcceptFilter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String[] startSymbols();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String startSymbol()
/*     */   {
/* 436 */     String[] ssyms = startSymbols();
/* 437 */     if ((ssyms == null) || (ssyms.length == 0)) {
/* 438 */       return null;
/*     */     }
/* 440 */     return ssyms[0];
/*     */   }
/*     */   
/*     */ 
/* 444 */   private final Filter<String> punctTagStringAcceptFilter = Filters.collectionAcceptFilter(punctuationTags());
/*     */   
/* 446 */   private final Filter<String> punctWordStringAcceptFilter = Filters.collectionAcceptFilter(punctuationWords());
/*     */   
/* 448 */   private final Filter<String> sFPunctTagStringAcceptFilter = Filters.collectionAcceptFilter(sentenceFinalPunctuationTags());
/*     */   
/* 450 */   private final Filter<String> eIPunctTagStringAcceptFilter = Filters.collectionAcceptFilter(evalBIgnoredPunctuationTags());
/*     */   
/* 452 */   private final Filter<String> startSymbolAcceptFilter = Filters.collectionAcceptFilter(startSymbols());
/*     */   
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = -6506749780512708352L;
/*     */   
/*     */ 
/*     */ 
/*     */   public TokenizerFactory<? extends HasWord> getTokenizerFactory()
/*     */   {
/* 462 */     return WhitespaceTokenizer.factory(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GrammaticalStructureFactory grammaticalStructureFactory()
/*     */   {
/* 472 */     throw new UnsupportedOperationException("No GrammaticalStructureFactory defined for " + getClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GrammaticalStructureFactory grammaticalStructureFactory(Filter<String> puncFilt)
/*     */   {
/* 482 */     return grammaticalStructureFactory();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\AbstractTreebankLanguagePack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */