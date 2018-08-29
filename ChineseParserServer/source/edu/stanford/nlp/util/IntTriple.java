/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ public class IntTriple extends IntTuple
/*    */ {
/*    */   public IntTriple()
/*    */   {
/*  7 */     this.elements = new int[3];
/*    */   }
/*    */   
/*    */   public IntTriple(int src, int mid, int trgt)
/*    */   {
/* 12 */     this.elements = new int[3];
/* 13 */     this.elements[0] = src;
/* 14 */     this.elements[1] = mid;
/* 15 */     this.elements[2] = trgt;
/*    */   }
/*    */   
/*    */   public IntTuple getCopy()
/*    */   {
/* 20 */     IntTriple nT = new IntTriple(this.elements[0], this.elements[1], this.elements[2]);
/* 21 */     return nT;
/*    */   }
/*    */   
/*    */   public int getSource()
/*    */   {
/* 26 */     return this.elements[0];
/*    */   }
/*    */   
/*    */   public int getTarget() {
/* 30 */     return this.elements[2];
/*    */   }
/*    */   
/*    */   public int getMiddle() {
/* 34 */     return this.elements[1];
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 38 */     return this.elements[0] << 20 ^ this.elements[1] << 10 ^ this.elements[2];
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\IntTriple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */