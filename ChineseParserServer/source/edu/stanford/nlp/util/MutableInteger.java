/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ 
/*     */ public final class MutableInteger
/*     */   extends Number
/*     */   implements Comparable
/*     */ {
/*     */   private int i;
/*     */   
/*     */   private static final long serialVersionUID = 624465615824626762L;
/*     */   
/*     */   public void set(int i)
/*     */   {
/*  14 */     this.i = i;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  18 */     return this.i;
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
/*  33 */     if ((obj instanceof MutableInteger)) {
/*  34 */       return this.i == ((MutableInteger)obj).i;
/*     */     }
/*  36 */     return false;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  40 */     return Integer.toString(this.i);
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
/*     */   public int compareTo(MutableInteger anotherMutableInteger)
/*     */   {
/*  59 */     int thisVal = this.i;
/*  60 */     int anotherVal = anotherMutableInteger.i;
/*  61 */     return thisVal == anotherVal ? 0 : thisVal < anotherVal ? -1 : 1;
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
/*  80 */     return compareTo((MutableInteger)o);
/*     */   }
/*     */   
/*     */   public int intValue()
/*     */   {
/*  85 */     return this.i;
/*     */   }
/*     */   
/*     */   public long longValue() {
/*  89 */     return this.i;
/*     */   }
/*     */   
/*     */   public short shortValue() {
/*  93 */     return (short)this.i;
/*     */   }
/*     */   
/*     */   public byte byteValue() {
/*  97 */     return (byte)this.i;
/*     */   }
/*     */   
/*     */   public float floatValue() {
/* 101 */     return this.i;
/*     */   }
/*     */   
/*     */   public double doubleValue() {
/* 105 */     return this.i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void incValue(int val)
/*     */   {
/* 113 */     this.i += val;
/*     */   }
/*     */   
/*     */   public MutableInteger() {
/* 117 */     this(0);
/*     */   }
/*     */   
/*     */   public MutableInteger(int i) {
/* 121 */     this.i = i;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\MutableInteger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */