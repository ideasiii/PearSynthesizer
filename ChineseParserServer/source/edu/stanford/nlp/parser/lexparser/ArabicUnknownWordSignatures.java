/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ArabicUnknownWordSignatures
/*    */ {
/*    */   static boolean allDigitPlus(String word)
/*    */   {
/* 15 */     boolean allDigitPlus = true;
/* 16 */     boolean seenDigit = false;
/* 17 */     int i = 0; for (int wlen = word.length(); i < wlen; i++) {
/* 18 */       char ch = word.charAt(i);
/* 19 */       if (Character.isDigit(ch)) {
/* 20 */         seenDigit = true;
/* 21 */       } else if ((ch != '-') && (ch != '.') && (ch != ',') && (ch != '٫') && (ch != '٬') && (ch != '−'))
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 27 */         allDigitPlus = false;
/*    */       }
/*    */     }
/* 30 */     return (allDigitPlus) && (seenDigit);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */   private static final Pattern adjectivalSuffixPattern = Pattern.compile("[yي][yي](?:[tت]?[nن])?$");
/*    */   
/*    */   static String likelyAdjectivalSuffix(String word)
/*    */   {
/* 41 */     if (adjectivalSuffixPattern.matcher(word).find()) {
/* 42 */       return "-AdjSuffix";
/*    */     }
/* 44 */     return "";
/*    */   }
/*    */   
/*    */ 
/* 48 */   private static Pattern singularPastTenseSuffixPattern = Pattern.compile("[tت]$");
/*    */   
/* 50 */   private static Pattern pluralFirstPersonPastTenseSuffixPattern = Pattern.compile("[nن][Aا]$");
/* 51 */   private static Pattern pluralThirdPersonMasculinePastTenseSuffixPattern = Pattern.compile("[wو]$");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static String pastTenseVerbNumberSuffix(String word)
/*    */   {
/* 59 */     if (singularPastTenseSuffixPattern.matcher(word).find())
/* 60 */       return "-PV.sg";
/* 61 */     if (pluralFirstPersonPastTenseSuffixPattern.matcher(word).find())
/* 62 */       return "-PV.pl1";
/* 63 */     if (pluralThirdPersonMasculinePastTenseSuffixPattern.matcher(word).find())
/* 64 */       return "-PV.pl3m";
/* 65 */     return "";
/*    */   }
/*    */   
/* 68 */   private static Pattern pluralThirdPersonMasculinePresentTenseSuffixPattern = Pattern.compile("[wو][نn]$");
/*    */   
/*    */   static String presentTenseVerbNumberSuffix(String word) {
/* 71 */     return pluralThirdPersonMasculinePresentTenseSuffixPattern.matcher(word).find() ? "-IV.pl3m" : "";
/*    */   }
/*    */   
/* 74 */   private static Pattern taaMarbuuTaSuffixPattern = Pattern.compile("[ةp]$");
/*    */   
/*    */   static String taaMarbuuTaSuffix(String word) {
/* 77 */     return taaMarbuuTaSuffixPattern.matcher(word).find() ? "-taaMarbuuTa" : "";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 82 */   private static Pattern abstractionNounSuffixPattern = Pattern.compile("[yي][pة]$");
/*    */   
/*    */   static String abstractionNounSuffix(String word) {
/* 85 */     return abstractionNounSuffixPattern.matcher(word).find() ? "-AbstractionSuffix" : "";
/*    */   }
/*    */   
/* 88 */   private static Pattern masdarPrefixPattern = Pattern.compile("^[tت]");
/*    */   
/*    */   static String masdarPrefix(String word) {
/* 91 */     return masdarPrefixPattern.matcher(word).find() ? "-maSdr" : "";
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ArabicUnknownWordSignatures.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */