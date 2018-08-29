/*    */ package edu.stanford.nlp.trees.international.tuebadz;
/*    */ 
/*    */ import edu.stanford.nlp.trees.AbstractTreebankLanguagePack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TueBaDZLanguagePack
/*    */   extends AbstractTreebankLanguagePack
/*    */ {
/* 13 */   private static String[] tuebadzPunctTags = { "$.", "$,", "$-LRB" };
/*    */   
/* 15 */   private static String[] tuebadzSFPunctTags = { "$." };
/*    */   
/*    */ 
/* 18 */   private static String[] tuebadzPunctWords = { "`", "-", ",", ";", ":", "!", "?", "/", ".", "...", "'", "\"", "[", "]", "*" };
/*    */   
/* 20 */   private static String[] tuebadzSFPunctWords = { ".", "!", "?" };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 25 */   private static char[] annotationIntroducingChars = { ':', '^', '~', '-', '#', '=' };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public char[] labelAnnotationIntroducingCharacters()
/*    */   {
/* 38 */     return annotationIntroducingChars;
/*    */   }
/*    */   
/*    */   public String[] punctuationTags() {
/* 42 */     return tuebadzPunctTags;
/*    */   }
/*    */   
/*    */   public String[] punctuationWords() {
/* 46 */     return tuebadzPunctWords;
/*    */   }
/*    */   
/*    */   public String[] sentenceFinalPunctuationTags() {
/* 50 */     return tuebadzSFPunctTags;
/*    */   }
/*    */   
/*    */   public String[] startSymbols() {
/* 54 */     return new String[] { "TOP" };
/*    */   }
/*    */   
/*    */   public String[] sentenceFinalPunctuationWords() {
/* 58 */     return tuebadzSFPunctWords;
/*    */   }
/*    */   
/*    */   public String treebankFileExtension() {
/* 62 */     return ".penn";
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\tuebadz\TueBaDZLanguagePack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */