/*    */ package edu.stanford.nlp.trees.international.pennchinese;
/*    */ 
/*    */ import edu.stanford.nlp.trees.AbstractCollinsHeadFinder;
/*    */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SunJurafskyChineseHeadFinder
/*    */   extends AbstractCollinsHeadFinder
/*    */ {
/*    */   public SunJurafskyChineseHeadFinder()
/*    */   {
/* 17 */     this(new ChineseTreebankLanguagePack());
/*    */   }
/*    */   
/*    */   public SunJurafskyChineseHeadFinder(TreebankLanguagePack tlp) {
/* 21 */     super(tlp);
/*    */     
/* 23 */     this.defaultRule = new String[] { "right" };
/*    */     
/* 25 */     this.nonTerminalInfo = new HashMap();
/*    */     
/* 27 */     this.nonTerminalInfo.put("ROOT", new String[][] { { "left", "IP" } });
/* 28 */     this.nonTerminalInfo.put("PAIR", new String[][] { { "left", "IP" } });
/*    */     
/* 30 */     this.nonTerminalInfo.put("ADJP", new String[][] { { "right", "ADJP", "JJ", "AD" } });
/* 31 */     this.nonTerminalInfo.put("ADVP", new String[][] { { "right", "ADVP", "AD", "CS", "JJ", "NP", "PP", "P", "VA", "VV" } });
/* 32 */     this.nonTerminalInfo.put("CLP", new String[][] { { "right", "CLP", "M", "NN", "NP" } });
/* 33 */     this.nonTerminalInfo.put("CP", new String[][] { { "right", "CP", "IP", "VP" } });
/* 34 */     this.nonTerminalInfo.put("DNP", new String[][] { { "right", "DEG", "DNP", "DEC", "QP" } });
/* 35 */     this.nonTerminalInfo.put("DP", new String[][] { { "left", "M", "DP", "DT", "OD" } });
/* 36 */     this.nonTerminalInfo.put("DVP", new String[][] { { "right", "DEV", "AD", "VP" } });
/* 37 */     this.nonTerminalInfo.put("IP", new String[][] { { "right", "VP", "IP", "NP" } });
/* 38 */     this.nonTerminalInfo.put("LCP", new String[][] { { "right", "LCP", "LC" } });
/* 39 */     this.nonTerminalInfo.put("LST", new String[][] { { "right", "CD", "NP", "QP" } });
/* 40 */     this.nonTerminalInfo.put("NP", new String[][] { { "right", "NP", "NN", "IP", "NR", "NT" } });
/* 41 */     this.nonTerminalInfo.put("PP", new String[][] { { "left", "P", "PP" } });
/* 42 */     this.nonTerminalInfo.put("PRN", new String[][] { { "left", "PU" } });
/* 43 */     this.nonTerminalInfo.put("QP", new String[][] { { "right", "QP", "CLP", "CD" } });
/* 44 */     this.nonTerminalInfo.put("UCP", new String[][] { { "left", "IP", "NP", "VP" } });
/* 45 */     this.nonTerminalInfo.put("VCD", new String[][] { { "left", "VV", "VA", "VE" } });
/* 46 */     this.nonTerminalInfo.put("VP", new String[][] { { "left", "VE", "VC", "VV", "VNV", "VPT", "VRD", "VSB", "VCD", "VP" } });
/* 47 */     this.nonTerminalInfo.put("VPT", new String[][] { { "left", "VA", "VV" } });
/* 48 */     this.nonTerminalInfo.put("VCP", new String[][] { { "left" } });
/* 49 */     this.nonTerminalInfo.put("VNV", new String[][] { { "left" } });
/* 50 */     this.nonTerminalInfo.put("VRD", new String[][] { { "left", "VV", "VA" } });
/* 51 */     this.nonTerminalInfo.put("VSB", new String[][] { { "right", "VV", "VE" } });
/* 52 */     this.nonTerminalInfo.put("FRAG", new String[][] { { "right", "VV", "NN" } });
/*    */     
/*    */ 
/* 55 */     this.nonTerminalInfo.put("CD", new String[][] { { "right", "CD" } });
/* 56 */     this.nonTerminalInfo.put("NN", new String[][] { { "right", "NN" } });
/* 57 */     this.nonTerminalInfo.put("NR", new String[][] { { "right", "NR" } });
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 62 */     this.nonTerminalInfo.put("VV", new String[][] { { "left" } });
/* 63 */     this.nonTerminalInfo.put("VA", new String[][] { { "left" } });
/* 64 */     this.nonTerminalInfo.put("VC", new String[][] { { "left" } });
/* 65 */     this.nonTerminalInfo.put("VE", new String[][] { { "left" } });
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\SunJurafskyChineseHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */