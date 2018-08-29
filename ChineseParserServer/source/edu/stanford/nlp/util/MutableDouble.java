/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ 
/*     */ public final class MutableDouble
/*     */   extends Number
/*     */   implements Comparable
/*     */ {
/*     */   private double d;
/*     */   
/*     */   private static final long serialVersionUID = 624465615824626762L;
/*     */   
/*     */   public void set(double d)
/*     */   {
/*  14 */     this.d = d;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  18 */     long bits = Double.doubleToLongBits(this.d);
/*  19 */     return (int)(bits ^ bits >>> 32);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  34 */     if ((obj instanceof MutableDouble)) {
/*  35 */       return this.d == ((MutableDouble)obj).d;
/*     */     }
/*  37 */     return false;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  41 */     return Double.toString(this.d);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(MutableDouble anotherMutableDouble)
/*     */   {
/*  60 */     double thisVal = this.d;
/*  61 */     double anotherVal = anotherMutableDouble.d;
/*  62 */     return thisVal == anotherVal ? 0 : thisVal < anotherVal ? -1 : 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(Object o)
/*     */   {
/*  81 */     return compareTo((MutableDouble)o);
/*     */   }
/*     */   
/*     */   public int intValue()
/*     */   {
/*  86 */     return (int)this.d;
/*     */   }
/*     */   
/*     */   public long longValue() {
/*  90 */     return this.d;
/*     */   }
/*     */   
/*     */   public short shortValue() {
/*  94 */     return (short)(int)this.d;
/*     */   }
/*     */   
/*     */   public byte byteValue() {
/*  98 */     return (byte)(int)this.d;
/*     */   }
/*     */   
/*     */   public float floatValue() {
/* 102 */     return (float)this.d;
/*     */   }
/*     */   
/*     */   public double doubleValue() {
/* 106 */     return this.d;
/*     */   }
/*     */   
/*     */   public MutableDouble() {
/* 110 */     this(0.0D);
/*     */   }
/*     */   
/*     */   public MutableDouble(double d) {
/* 114 */     this.d = d;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\MutableDouble.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */