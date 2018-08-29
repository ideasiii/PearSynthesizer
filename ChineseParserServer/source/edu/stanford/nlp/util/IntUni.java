/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntUni
/*    */   extends IntTuple
/*    */ {
/*    */   public IntUni()
/*    */   {
/* 12 */     this.elements = new int[1];
/*    */   }
/*    */   
/*    */   public IntUni(int src)
/*    */   {
/* 17 */     this.elements = new int[1];
/* 18 */     this.elements[0] = src;
/*    */   }
/*    */   
/*    */   public int getSource()
/*    */   {
/* 23 */     return this.elements[0];
/*    */   }
/*    */   
/*    */   public void setSource(int src) {
/* 27 */     this.elements[0] = src;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 32 */     return this.elements[0];
/*    */   }
/*    */   
/*    */   public IntTuple getCopy()
/*    */   {
/* 37 */     IntUni nT = new IntUni(this.elements[0]);
/* 38 */     return nT;
/*    */   }
/*    */   
/*    */   public void add(int val) {
/* 42 */     this.elements[0] += val;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\IntUni.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */