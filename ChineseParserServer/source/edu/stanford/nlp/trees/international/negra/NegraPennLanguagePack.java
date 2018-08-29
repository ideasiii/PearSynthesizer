/*     */ package edu.stanford.nlp.trees.international.negra;
/*     */ 
/*     */ import edu.stanford.nlp.trees.AbstractTreebankLanguagePack;
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
/*     */ public class NegraPennLanguagePack
/*     */   extends AbstractTreebankLanguagePack
/*     */   implements Serializable
/*     */ {
/*     */   private static final String NEGRA_ENCODING = "ISO-8859-1";
/*  23 */   private static final String[] evalBignoredTags = { "$.", "$," };
/*     */   
/*  25 */   private static final String[] negraSFPunctTags = { "$." };
/*     */   
/*  27 */   private static final String[] negraSFPunctWords = { ".", "!", "?" };
/*     */   
/*  29 */   private static final String[] negraPunctTags = { "$.", "$,", "$*LRB*" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  34 */   private static final String[] negraPunctWords = { "-", ",", ";", ":", "!", "?", "/", ".", "...", "·", "'", "\"", "(", ")", "*LRB*", "*RRB*" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  40 */   private static char[] annotationIntroducingChars = { '-', '=', '|', '#', '^', '~' };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  45 */   private static String[] pennStartSymbols = { "ROOT" };
/*     */   
/*     */ 
/*     */   private static final long serialVersionUID = 9081305982861675328L;
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] punctuationTags()
/*     */   {
/*  54 */     return negraPunctTags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] punctuationWords()
/*     */   {
/*  64 */     return negraPunctWords;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] sentenceFinalPunctuationTags()
/*     */   {
/*  75 */     return negraSFPunctTags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] sentenceFinalPunctuationWords()
/*     */   {
/*  85 */     return negraSFPunctWords;
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
/*     */   public String[] evalBIgnoredPunctuationTags()
/*     */   {
/*  99 */     return evalBignoredTags;
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
/* 114 */     return annotationIntroducingChars;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] startSymbols()
/*     */   {
/* 124 */     return pennStartSymbols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 134 */     return "ISO-8859-1";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String treebankFileExtension()
/*     */   {
/* 142 */     return "mrg";
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {}
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\negra\NegraPennLanguagePack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */