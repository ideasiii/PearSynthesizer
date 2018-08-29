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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChineseHeadFinder
/*    */   extends AbstractCollinsHeadFinder
/*    */ {
/*    */   private static final boolean coordSwitch = false;
/*    */   
/*    */   public ChineseHeadFinder()
/*    */   {
/* 24 */     this(new ChineseTreebankLanguagePack());
/*    */   }
/*    */   
/*    */   public ChineseHeadFinder(TreebankLanguagePack tlp) {
/* 28 */     super(tlp);
/*    */     
/* 30 */     this.nonTerminalInfo = new HashMap();
/*    */     
/*    */ 
/* 33 */     String left = "left";
/* 34 */     String right = "right";
/* 35 */     String rightdis = "rightdis";
/*    */     
/* 37 */     this.defaultRule = new String[] { right };
/*    */     
/*    */ 
/*    */ 
/* 41 */     this.nonTerminalInfo.put("ROOT", new String[][] { { left, "IP" } });
/* 42 */     this.nonTerminalInfo.put("PAIR", new String[][] { { left, "IP" } });
/*    */     
/*    */ 
/* 45 */     this.nonTerminalInfo.put("ADJP", new String[][] { { left, "JJ", "ADJP" } });
/* 46 */     this.nonTerminalInfo.put("ADVP", new String[][] { { left, "AD", "CS", "ADVP", "JJ" } });
/* 47 */     this.nonTerminalInfo.put("CLP", new String[][] { { right, "M", "CLP" } });
/*    */     
/* 49 */     this.nonTerminalInfo.put("CP", new String[][] { { right, "DEC", "WHNP", "WHPP" } });
/*    */     
/* 51 */     this.nonTerminalInfo.put("DNP", new String[][] { { right, "DEG" } });
/* 52 */     this.nonTerminalInfo.put("DP", new String[][] { { left, "DT", "DP" } });
/* 53 */     this.nonTerminalInfo.put("DVP", new String[][] { { right, "DEV" } });
/* 54 */     this.nonTerminalInfo.put("FRAG", new String[][] { { right, "VV", "NN" } });
/* 55 */     this.nonTerminalInfo.put("INTJ", new String[][] { { right, "INTJ", "IJ", "SP" } });
/* 56 */     this.nonTerminalInfo.put("IP", new String[][] { { left, "IP", "VP" } });
/* 57 */     this.nonTerminalInfo.put("LCP", new String[][] { { right, "LC", "LCP" } });
/* 58 */     this.nonTerminalInfo.put("LST", new String[][] { { right, "CD", "PU" } });
/* 59 */     this.nonTerminalInfo.put("NP", new String[][] { { right, "NN", "NR", "NT", "NP", "PN", "CP" } });
/* 60 */     this.nonTerminalInfo.put("PP", new String[][] { { left, "P", "PP" } });
/*    */     
/*    */ 
/* 63 */     this.nonTerminalInfo.put("PRN", new String[][] { { left, "NP", "VP", "IP", "QP", "PP", "ADJP", "CLP", "LCP" }, { rightdis, "NN", "NR", "NT", "FW" } });
/*    */     
/* 65 */     this.nonTerminalInfo.put("QP", new String[][] { { right, "QP", "CLP", "CD", "OD", "NP", "NT", "M" } });
/*    */     
/* 67 */     this.nonTerminalInfo.put("UCP", new String[][] { { left } });
/* 68 */     this.nonTerminalInfo.put("VP", new String[][] { { left, "VP", "VPT", "VV", "VA", "VC", "VE", "IP" } });
/*    */     
/*    */ 
/*    */ 
/* 72 */     this.nonTerminalInfo.put("VCD", new String[][] { { left, "VCD", "VV", "VA", "VC", "VE" } });
/* 73 */     this.nonTerminalInfo.put("VCP", new String[][] { { left, "VCD", "VV", "VA", "VC", "VE" } });
/* 74 */     this.nonTerminalInfo.put("VRD", new String[][] { { left, "VCD", "VRD", "VV", "VA", "VC", "VE" } });
/* 75 */     this.nonTerminalInfo.put("VSB", new String[][] { { right, "VCD", "VSB", "VV", "VA", "VC", "VE" } });
/* 76 */     this.nonTerminalInfo.put("VNV", new String[][] { { left, "VV", "VA", "VC", "VE" } });
/* 77 */     this.nonTerminalInfo.put("VPT", new String[][] { { left, "VV", "VA", "VC", "VE" } });
/*    */     
/*    */ 
/* 80 */     this.nonTerminalInfo.put("CD", new String[][] { { right, "CD" } });
/* 81 */     this.nonTerminalInfo.put("NN", new String[][] { { right, "NN" } });
/* 82 */     this.nonTerminalInfo.put("NR", new String[][] { { right, "NR" } });
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 87 */     this.nonTerminalInfo.put("VV", new String[][] { { left } });
/* 88 */     this.nonTerminalInfo.put("VA", new String[][] { { left } });
/* 89 */     this.nonTerminalInfo.put("VC", new String[][] { { left } });
/* 90 */     this.nonTerminalInfo.put("VE", new String[][] { { left } });
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\ChineseHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */