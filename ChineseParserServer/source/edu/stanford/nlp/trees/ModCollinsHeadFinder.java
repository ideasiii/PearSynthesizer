/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModCollinsHeadFinder
/*    */   extends CollinsHeadFinder
/*    */ {
/*    */   private static final long serialVersionUID = -5870387458902637256L;
/*    */   
/*    */   public ModCollinsHeadFinder()
/*    */   {
/* 39 */     this(new PennTreebankLanguagePack());
/*    */   }
/*    */   
/*    */   public ModCollinsHeadFinder(TreebankLanguagePack tlp) {
/* 43 */     super(tlp);
/*    */     
/*    */ 
/*    */ 
/* 47 */     this.nonTerminalInfo.clear();
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 55 */     this.nonTerminalInfo.put("ADJP", new String[][] { { "left", "NNS", "NN", "$", "QP", "JJ", "VBN", "VBG", "ADJP", "JJR", "NP", "JJS", "DT", "FW", "RBR", "RBS", "SBAR", "RB" } });
/* 56 */     this.nonTerminalInfo.put("ADVP", new String[][] { { "right", "RB", "RBR", "RBS", "FW", "ADVP", "TO", "CD", "JJR", "JJ", "IN", "NP", "JJS", "NN" } });
/* 57 */     this.nonTerminalInfo.put("CONJP", new String[][] { { "right", "CC", "RB", "IN" } });
/* 58 */     this.nonTerminalInfo.put("FRAG", new String[][] { { "right" } });
/* 59 */     this.nonTerminalInfo.put("INTJ", new String[][] { { "left" } });
/* 60 */     this.nonTerminalInfo.put("LST", new String[][] { { "right", "LS", ":" } });
/* 61 */     this.nonTerminalInfo.put("NAC", new String[][] { { "left", "NN", "NNS", "NNP", "NNPS", "NP", "NAC", "EX", "$", "CD", "QP", "PRP", "VBG", "JJ", "JJS", "JJR", "ADJP", "FW" } });
/* 62 */     this.nonTerminalInfo.put("NX", new String[][] { { "right", "NP", "NX" } });
/* 63 */     this.nonTerminalInfo.put("PP", new String[][] { { "right", "IN", "TO", "VBG", "VBN", "RP", "FW" }, { "right", "PP" } });
/*    */     
/*    */ 
/*    */ 
/* 67 */     this.nonTerminalInfo.put("PRN", new String[][] { { "left", "VP", "NP", "PP", "S", "SINV", "SBAR", "ADJP", "ADVP", "INTJ", "WHNP", "NAC", "VBP", "JJ", "NN", "NNP" } });
/* 68 */     this.nonTerminalInfo.put("PRT", new String[][] { { "right", "RP" } });
/*    */     
/* 70 */     this.nonTerminalInfo.put("QP", new String[][] { { "left", "$", "IN", "NNS", "NN", "JJ", "CD", "PDT", "DT", "RB", "NCD", "QP", "JJR", "JJS" } });
/* 71 */     this.nonTerminalInfo.put("RRC", new String[][] { { "right", "VP", "NP", "ADVP", "ADJP", "PP" } });
/*    */     
/*    */ 
/*    */ 
/* 75 */     this.nonTerminalInfo.put("S", new String[][] { { "left", "TO", "VP", "S", "FRAG", "SBAR", "ADJP", "UCP", "NP" } });
/* 76 */     this.nonTerminalInfo.put("SBAR", new String[][] { { "left", "WHNP", "WHPP", "WHADVP", "WHADJP", "IN", "DT", "S", "SQ", "SINV", "SBAR", "FRAG" } });
/* 77 */     this.nonTerminalInfo.put("SBARQ", new String[][] { { "left", "SQ", "S", "SINV", "SBARQ", "FRAG" } });
/* 78 */     this.nonTerminalInfo.put("SINV", new String[][] { { "left", "VBZ", "VBD", "VBP", "VB", "MD", "VP", "S", "SINV", "ADJP", "NP" } });
/* 79 */     this.nonTerminalInfo.put("SQ", new String[][] { { "left", "VBZ", "VBD", "VBP", "VB", "MD", "VP", "SQ" } });
/* 80 */     this.nonTerminalInfo.put("UCP", new String[][] { { "right" } });
/*    */     
/* 82 */     this.nonTerminalInfo.put("VP", new String[][] { { "left", "TO", "VBD", "VBN", "MD", "VBZ", "VB", "VBG", "VBP", "VP", "ADJP", "NN", "NNS", "JJ", "NP", "NNP" } });
/* 83 */     this.nonTerminalInfo.put("WHADJP", new String[][] { { "left", "CC", "WRB", "JJ", "ADJP" } });
/* 84 */     this.nonTerminalInfo.put("WHADVP", new String[][] { { "right", "CC", "WRB" } });
/* 85 */     this.nonTerminalInfo.put("WHNP", new String[][] { { "left", "WDT", "WP", "WP$", "WHADJP", "WHPP", "WHNP" } });
/* 86 */     this.nonTerminalInfo.put("WHPP", new String[][] { { "right", "IN", "TO", "FW" } });
/* 87 */     this.nonTerminalInfo.put("X", new String[][] { { "right", "S", "VP", "ADJP", "NP", "SBAR", "PP", "X" } });
/* 88 */     this.nonTerminalInfo.put("NP", new String[][] { { "rightdis", "NN", "NNP", "NNPS", "NNS", "NX", "POS", "JJR" }, { "left", "NP", "PRP" }, { "rightdis", "$", "ADJP", "PRN" }, { "right", "CD" }, { "rightdis", "JJ", "JJS", "RB", "QP", "DT", "WDT", "RBR", "ADVP" } });
/* 89 */     this.nonTerminalInfo.put("POSSP", new String[][] { { "right", "POS" } });
/*    */     
/*    */ 
/*    */ 
/* 93 */     this.nonTerminalInfo.put("ROOT", new String[][] { { "left", "S", "SQ", "SINV", "SBAR", "FRAG" } });
/* 94 */     this.nonTerminalInfo.put("TYPO", new String[][] { { "left", "NN", "NP", "NNP", "NNPS", "TO", "VBD", "VBN", "MD", "VBZ", "VB", "VBG", "VBP", "VP", "ADJP", "FRAG" } });
/*    */     
/* 96 */     this.nonTerminalInfo.put("ADV", new String[][] { { "right", "RB", "RBR", "RBS", "FW", "ADVP", "TO", "CD", "JJR", "JJ", "IN", "NP", "JJS", "NN" } });
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\ModCollinsHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */