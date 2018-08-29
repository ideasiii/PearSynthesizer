/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ class TsurgeonPatternRoot
/*    */   extends TsurgeonPattern
/*    */ {
/* 13 */   CoindexationGenerator coindexer = new CoindexationGenerator();
/*    */   Map<String, Tree> newNodeNames;
/*    */   
/*    */   public TsurgeonPatternRoot(TsurgeonPattern[] children) {
/* 17 */     super("operations:", children);
/* 18 */     setRoot(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Tree evaluate(Tree t, TregexMatcher m)
/*    */   {
/* 27 */     this.newNodeNames = new HashMap();
/* 28 */     this.coindexer.setLastIndex(t);
/* 29 */     for (TsurgeonPattern child : this.children) {
/* 30 */       t = child.evaluate(t, m);
/* 31 */       if (t == null) {
/* 32 */         return null;
/*    */       }
/*    */     }
/* 35 */     return t;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\TsurgeonPatternRoot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */