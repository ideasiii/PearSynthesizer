/*    */ package edu.stanford.nlp.trees.tregex;
/*    */ 
/*    */ 
/*    */ public abstract interface TregexParserConstants
/*    */ {
/*    */   public static final int EOF = 0;
/*    */   
/*    */   public static final int RELATION = 4;
/*    */   
/*    */   public static final int REL_W_STR_ARG = 5;
/*    */   public static final int NUMBER = 6;
/*    */   public static final int IDENTIFIER = 7;
/*    */   public static final int BLANK = 8;
/*    */   public static final int REGEX = 9;
/*    */   public static final int VARNAME = 10;
/*    */   public static final int DEFAULT = 0;
/* 17 */   public static final String[] tokenImage = { "<EOF>", "\" \"", "\"\\r\"", "\"\\t\"", "<RELATION>", "<REL_W_STR_ARG>", "<NUMBER>", "<IDENTIFIER>", "\"__\"", "<REGEX>", "<VARNAME>", "\"\\n\"", "\"(\"", "\")\"", "\"!\"", "\"@\"", "\"#\"", "\"%\"", "\"=\"", "\"~\"", "\"|\"", "\"&\"", "\"?\"", "\"[\"", "\"]\"" };
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\TregexParserConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */