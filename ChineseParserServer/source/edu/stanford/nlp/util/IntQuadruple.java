/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ public class IntQuadruple extends IntTuple
/*    */ {
/*    */   private static final long serialVersionUID = 7154973101012473479L;
/*    */   
/*    */   public IntQuadruple()
/*    */   {
/*  9 */     this.elements = new int[4];
/*    */   }
/*    */   
/*    */   public IntQuadruple(int src, int mid, int trgt, int trgt2) {
/* 13 */     this.elements = new int[4];
/* 14 */     this.elements[0] = src;
/* 15 */     this.elements[1] = mid;
/* 16 */     this.elements[2] = trgt;
/* 17 */     this.elements[3] = trgt2;
/*    */   }
/*    */   
/*    */   public IntTuple getCopy()
/*    */   {
/* 22 */     IntQuadruple nT = new IntQuadruple(this.elements[0], this.elements[1], this.elements[2], this.elements[3]);
/* 23 */     return nT;
/*    */   }
/*    */   
/*    */   public int getSource()
/*    */   {
/* 28 */     return get(0);
/*    */   }
/*    */   
/*    */   public int getTarget() {
/* 32 */     return get(2);
/*    */   }
/*    */   
/*    */   public int getTarget2() {
/* 36 */     return get(3);
/*    */   }
/*    */   
/*    */   public int getMiddle() {
/* 40 */     return get(1);
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 45 */     return get(0) << 20 ^ get(1) << 10 ^ get(2) << 5 ^ get(3);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\IntQuadruple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */