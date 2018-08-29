/*    */ package edu.stanford.nlp.trees.international.pennchinese;
/*    */ 
/*    */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChineseSemanticHeadFinder
/*    */   extends ChineseHeadFinder
/*    */ {
/*    */   private static final long serialVersionUID = 2L;
/*    */   
/*    */   public ChineseSemanticHeadFinder()
/*    */   {
/* 16 */     this(new ChineseTreebankLanguagePack());
/*    */   }
/*    */   
/*    */   public ChineseSemanticHeadFinder(TreebankLanguagePack tlp) {
/* 20 */     super(tlp);
/* 21 */     ruleChanges();
/*    */   }
/*    */   
/*    */   private void ruleChanges()
/*    */   {
/* 26 */     this.nonTerminalInfo.remove("VP");
/* 27 */     this.nonTerminalInfo.put("VP", new String[][] { { "left", "VP", "VCD", "VPT", "VV", "VA", "VE", "VC", "IP" } });
/* 28 */     this.nonTerminalInfo.remove("CP");
/* 29 */     this.nonTerminalInfo.put("CP", new String[][] { { "right", "CP", "IP", "VP" } });
/* 30 */     this.nonTerminalInfo.remove("DNP");
/* 31 */     this.nonTerminalInfo.put("DNP", new String[][] { { "leftdis", "NP" } });
/* 32 */     this.nonTerminalInfo.remove("DVP");
/* 33 */     this.nonTerminalInfo.put("DVP", new String[][] { { "leftdis", "VP", "ADVP" } });
/* 34 */     this.nonTerminalInfo.remove("LST");
/* 35 */     this.nonTerminalInfo.put("LST", new String[][] { { "right", "CD", "NP", "QP", "PU" } });
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\ChineseSemanticHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */