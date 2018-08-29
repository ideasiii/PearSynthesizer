/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ 
/*    */ public abstract interface TsurgeonParserConstants
/*    */ {
/*    */   public static final int EOF = 0;
/*    */   
/*    */   public static final int DELETE = 4;
/*    */   
/*    */   public static final int PRUNE = 5;
/*    */   public static final int RELABEL = 6;
/*    */   public static final int EXCISE = 7;
/*    */   public static final int INSERT = 8;
/*    */   public static final int MOVE = 9;
/*    */   public static final int REPLACE = 10;
/*    */   public static final int ADJOIN = 11;
/*    */   public static final int ADJOIN_TO_HEAD = 12;
/*    */   public static final int ADJOIN_TO_FOOT = 13;
/*    */   public static final int COINDEX = 14;
/*    */   public static final int SELECTION = 15;
/*    */   public static final int IDENTIFIER = 16;
/*    */   public static final int LABEL = 17;
/*    */   public static final int LOCATION_RELATION = 18;
/*    */   public static final int REGEX = 19;
/*    */   public static final int QUOTEX = 20;
/*    */   public static final int HASH_INTEGER = 21;
/*    */   public static final int TREE_NODE_TERMINAL_LABEL = 22;
/*    */   public static final int TREE_NODE_NONTERMINAL_LABEL = 23;
/*    */   public static final int DEFAULT = 0;
/* 30 */   public static final String[] tokenImage = { "<EOF>", "\" \"", "\"\\r\"", "\"\\t\"", "\"delete\"", "\"prune\"", "\"relabel\"", "\"excise\"", "\"insert\"", "\"move\"", "\"replace\"", "\"adjoin\"", "\"adjoinH\"", "\"adjoinF\"", "\"coindex\"", "<SELECTION>", "<IDENTIFIER>", "<LABEL>", "<LOCATION_RELATION>", "<REGEX>", "<QUOTEX>", "<HASH_INTEGER>", "<TREE_NODE_TERMINAL_LABEL>", "<TREE_NODE_NONTERMINAL_LABEL>", "\"\\n\"", "\")\"" };
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\TsurgeonParserConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */