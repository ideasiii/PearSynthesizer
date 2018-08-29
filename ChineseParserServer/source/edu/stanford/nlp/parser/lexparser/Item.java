/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.util.Scored;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class Item
/*    */   implements Scored
/*    */ {
/*    */   public int start;
/*    */   public int end;
/*    */   public int state;
/*    */   public int head;
/*    */   public int tag;
/*    */   public Edge backEdge;
/* 18 */   public double iScore = Double.NEGATIVE_INFINITY;
/* 19 */   public double oScore = Double.NEGATIVE_INFINITY;
/*    */   
/*    */ 
/*    */ 
/*    */   public double score()
/*    */   {
/* 25 */     return this.iScore + this.oScore;
/*    */   }
/*    */   
/*    */   public boolean isEdge()
/*    */   {
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isPreHook() {
/* 34 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isPostHook() {
/* 38 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Item.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */