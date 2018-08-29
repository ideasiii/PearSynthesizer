/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ public class IntPair extends IntTuple
/*    */ {
/*    */   public IntPair()
/*    */   {
/*  7 */     this.elements = new int[2];
/*    */   }
/*    */   
/*    */   public IntPair(int src, int trgt)
/*    */   {
/* 12 */     this.elements = new int[2];
/* 13 */     this.elements[0] = src;
/* 14 */     this.elements[1] = trgt;
/*    */   }
/*    */   
/*    */ 
/*    */   public int getSource()
/*    */   {
/* 20 */     return get(0);
/*    */   }
/*    */   
/*    */   public int getTarget() {
/* 24 */     return get(1);
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 29 */     return 65536 * this.elements[0] ^ this.elements[1];
/*    */   }
/*    */   
/*    */   public IntTuple getCopy()
/*    */   {
/* 34 */     IntPair nT = new IntPair(this.elements[0], this.elements[1]);
/* 35 */     return nT;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\IntPair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */