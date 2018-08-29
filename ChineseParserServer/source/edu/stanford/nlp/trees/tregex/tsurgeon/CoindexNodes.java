/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ 
/*    */ 
/*    */ public class CoindexNodes
/*    */   extends TsurgeonPattern
/*    */ {
/* 11 */   private static String coindexationIntroductionString = "-";
/*    */   
/*    */   public CoindexNodes(TsurgeonPattern[] children) {
/* 14 */     super("coindex", children);
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 18 */     int newIndex = this.root.coindexer.generateIndex();
/* 19 */     for (TsurgeonPattern child : this.children) {
/* 20 */       Tree node = child.evaluate(t, m);
/* 21 */       node.label().setValue(node.label().value() + coindexationIntroductionString + newIndex);
/*    */     }
/* 23 */     return t;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\CoindexNodes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */