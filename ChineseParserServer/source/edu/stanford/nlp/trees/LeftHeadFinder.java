/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LeftHeadFinder
/*    */   implements HeadFinder
/*    */ {
/*    */   public Tree determineHead(Tree t)
/*    */   {
/* 13 */     if (t.isLeaf()) {
/* 14 */       return null;
/*    */     }
/* 16 */     return t.children()[0];
/*    */   }
/*    */   
/*    */   public Tree determineHead(Tree t, Tree parent)
/*    */   {
/* 21 */     return determineHead(t);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\LeftHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */