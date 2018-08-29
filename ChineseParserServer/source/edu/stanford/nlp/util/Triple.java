/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Triple<T1, T2, T3>
/*    */   implements Serializable
/*    */ {
/*    */   public T1 first;
/*    */   public T2 second;
/*    */   public T3 third;
/*    */   
/*    */   public Triple(T1 first, T2 second, T3 third)
/*    */   {
/* 20 */     this.first = first;
/* 21 */     this.second = second;
/* 22 */     this.third = third;
/*    */   }
/*    */   
/*    */   public T1 first() {
/* 26 */     return (T1)this.first;
/*    */   }
/*    */   
/*    */   public T2 second() {
/* 30 */     return (T2)this.second;
/*    */   }
/*    */   
/*    */   public T3 third() {
/* 34 */     return (T3)this.third;
/*    */   }
/*    */   
/*    */   public void setFirst(T1 o) {
/* 38 */     this.first = o;
/*    */   }
/*    */   
/*    */   public void setSecond(T2 o) {
/* 42 */     this.second = o;
/*    */   }
/*    */   
/*    */   public void setThird(T3 o) {
/* 46 */     this.third = o;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 51 */     if (this == o) {
/* 52 */       return true;
/*    */     }
/*    */     
/* 55 */     if (!(o instanceof Triple)) {
/* 56 */       return false;
/*    */     }
/*    */     
/* 59 */     Triple triple = (Triple)o;
/*    */     
/* 61 */     if (this.first != null ? !this.first.equals(triple.first) : triple.first != null) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (this.second != null ? !this.second.equals(triple.second) : triple.second != null) {
/* 65 */       return false;
/*    */     }
/* 67 */     if (this.third != null ? !this.third.equals(triple.third) : triple.third != null) {
/* 68 */       return false;
/*    */     }
/*    */     
/* 71 */     return true;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 76 */     int result = this.first != null ? this.first.hashCode() : 0;
/* 77 */     result = 29 * result + (this.second != null ? this.second.hashCode() : 0);
/* 78 */     result = 29 * result + (this.third != null ? this.third.hashCode() : 0);
/* 79 */     return result;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 83 */     return "(" + this.first + "," + this.second + "," + this.third + ")";
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Triple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */