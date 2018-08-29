/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScoredObject<T>
/*    */   implements Scored
/*    */ {
/*    */   private double score;
/*    */   
/*    */   private T object;
/*    */   
/*    */ 
/*    */   public double score()
/*    */   {
/* 15 */     return this.score;
/*    */   }
/*    */   
/*    */   public void setScore(double score) {
/* 19 */     this.score = score;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public T object()
/*    */   {
/* 26 */     return (T)this.object;
/*    */   }
/*    */   
/*    */   public void setObject(T object) {
/* 30 */     this.object = object;
/*    */   }
/*    */   
/*    */   public ScoredObject() {}
/*    */   
/*    */   public ScoredObject(T object, double score)
/*    */   {
/* 37 */     this.object = object;
/* 38 */     this.score = score;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 42 */     return this.object + " @ " + this.score;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\ScoredObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */