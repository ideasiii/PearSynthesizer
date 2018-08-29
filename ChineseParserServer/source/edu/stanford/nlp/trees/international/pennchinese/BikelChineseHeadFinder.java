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
/*    */ 
/*    */ 
/*    */ public class BikelChineseHeadFinder
/*    */   extends AbstractCollinsHeadFinder
/*    */ {
/*    */   public BikelChineseHeadFinder()
/*    */   {
/* 19 */     this(new ChineseTreebankLanguagePack());
/*    */   }
/*    */   
/*    */   public BikelChineseHeadFinder(TreebankLanguagePack tlp) {
/* 23 */     super(tlp);
/*    */     
/* 25 */     this.nonTerminalInfo = new HashMap();
/*    */     
/*    */ 
/* 28 */     this.defaultRule = new String[] { "right" };
/*    */     
/*    */ 
/*    */ 
/* 32 */     this.nonTerminalInfo.put("ROOT", new String[][] { { "left", "IP" } });
/* 33 */     this.nonTerminalInfo.put("PAIR", new String[][] { { "left", "IP" } });
/*    */     
/*    */ 
/* 36 */     this.nonTerminalInfo.put("ADJP", new String[][] { { "right", "ADJP", "JJ" }, { "right", "AD", "NN", "CS" } });
/* 37 */     this.nonTerminalInfo.put("ADVP", new String[][] { { "right", "ADVP", "AD" } });
/* 38 */     this.nonTerminalInfo.put("CLP", new String[][] { { "right", "CLP", "M" } });
/* 39 */     this.nonTerminalInfo.put("CP", new String[][] { { "right", "DEC", "SP" }, { "left", "ADVP", "CS" }, { "right", "CP", "IP" } });
/* 40 */     this.nonTerminalInfo.put("DNP", new String[][] { { "right", "DNP", "DEG" }, { "right", "DEC" } });
/* 41 */     this.nonTerminalInfo.put("DP", new String[][] { { "left", "DP", "DT" } });
/* 42 */     this.nonTerminalInfo.put("DVP", new String[][] { { "right", "DVP", "DEV" } });
/* 43 */     this.nonTerminalInfo.put("FRAG", new String[][] { { "right", "VV", "NR", "NN" } });
/* 44 */     this.nonTerminalInfo.put("INTJ", new String[][] { { "right", "INTJ", "IJ" } });
/* 45 */     this.nonTerminalInfo.put("IP", new String[][] { { "right", "IP", "VP" }, { "right", "VV" } });
/* 46 */     this.nonTerminalInfo.put("LCP", new String[][] { { "right", "LCP", "LC" } });
/* 47 */     this.nonTerminalInfo.put("LST", new String[][] { { "left", "LST", "CD", "OD" } });
/* 48 */     this.nonTerminalInfo.put("NP", new String[][] { { "right", "NP", "NN", "NT", "NR", "QP" } });
/* 49 */     this.nonTerminalInfo.put("PP", new String[][] { { "left", "PP", "P" } });
/* 50 */     this.nonTerminalInfo.put("PRN", new String[][] { { "right", "NP", "IP", "VP", "NT", "NR", "NN" } });
/* 51 */     this.nonTerminalInfo.put("QP", new String[][] { { "right", "QP", "CLP", "CD", "OD" } });
/* 52 */     this.nonTerminalInfo.put("UCP", new String[][] { { "right" } });
/* 53 */     this.nonTerminalInfo.put("VP", new String[][] { { "left", "VP", "VA", "VC", "VE", "VV", "BA", "LB", "VCD", "VSB", "VRD", "VNV", "VCP" } });
/* 54 */     this.nonTerminalInfo.put("VCD", new String[][] { { "right", "VCD", "VV", "VA", "VC", "VE" } });
/* 55 */     this.nonTerminalInfo.put("VCP", new String[][] { { "right", "VCP", "VV", "VA", "VC", "VE" } });
/* 56 */     this.nonTerminalInfo.put("VRD", new String[][] { { "right", "VRD", "VV", "VA", "VC", "VE" } });
/* 57 */     this.nonTerminalInfo.put("VSB", new String[][] { { "right", "VSB", "VV", "VA", "VC", "VE" } });
/* 58 */     this.nonTerminalInfo.put("VNV", new String[][] { { "right", "VNV", "VV", "VA", "VC", "VE" } });
/* 59 */     this.nonTerminalInfo.put("VPT", new String[][] { { "right", "VNV", "VV", "VA", "VC", "VE" } });
/* 60 */     this.nonTerminalInfo.put("WHNP", new String[][] { { "right", "WHNP", "NP", "NN", "NT", "NR", "QP" } });
/* 61 */     this.nonTerminalInfo.put("WHPP", new String[][] { { "left", "WHPP", "PP", "P" } });
/*    */     
/*    */ 
/* 64 */     this.nonTerminalInfo.put("CD", new String[][] { { "right", "CD" } });
/* 65 */     this.nonTerminalInfo.put("NN", new String[][] { { "right", "NN" } });
/* 66 */     this.nonTerminalInfo.put("NR", new String[][] { { "right", "NR" } });
/*    */     
/*    */ 
/* 69 */     this.nonTerminalInfo.put("VV", new String[][] { { "left" } });
/* 70 */     this.nonTerminalInfo.put("VA", new String[][] { { "left" } });
/* 71 */     this.nonTerminalInfo.put("VC", new String[][] { { "left" } });
/* 72 */     this.nonTerminalInfo.put("VE", new String[][] { { "left" } });
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\BikelChineseHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */