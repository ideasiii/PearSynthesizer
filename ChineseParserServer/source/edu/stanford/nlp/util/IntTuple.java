/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
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
/*     */ public class IntTuple
/*     */   implements Serializable
/*     */ {
/*     */   int[] elements;
/*     */   private static final long serialVersionUID = 7266305463893511982L;
/*     */   
/*     */   public IntTuple(int[] arr)
/*     */   {
/*  24 */     this.elements = arr;
/*     */   }
/*     */   
/*     */   public IntTuple(int num) {
/*  28 */     this.elements = new int[num];
/*     */   }
/*     */   
/*     */   public int get(int num)
/*     */   {
/*  33 */     return this.elements[num];
/*     */   }
/*     */   
/*     */   public void set(int num, int val)
/*     */   {
/*  38 */     this.elements[num] = val;
/*     */   }
/*     */   
/*     */   public void shiftLeft() {
/*  42 */     for (int j = 0; j < this.elements.length - 1; j++) {
/*  43 */       this.elements[j] = this.elements[(j + 1)];
/*     */     }
/*  45 */     this.elements[(this.elements.length - 1)] = 0;
/*     */   }
/*     */   
/*     */   public IntTuple getCopy()
/*     */   {
/*  50 */     IntTuple copy = getIntTuple(this.elements.length);
/*  51 */     for (int i = 0; i < this.elements.length; i++) {
/*  52 */       copy.set(i, this.elements[i]);
/*     */     }
/*  54 */     return copy;
/*     */   }
/*     */   
/*     */   public int[] elems()
/*     */   {
/*  59 */     return this.elements;
/*     */   }
/*     */   
/*     */   public boolean equals(Object iO) {
/*  63 */     if (!(iO instanceof IntTuple)) {
/*  64 */       return false;
/*     */     }
/*  66 */     IntTuple i = (IntTuple)iO;
/*  67 */     if (i.elements.length != this.elements.length) {
/*  68 */       return false;
/*     */     }
/*  70 */     for (int j = 0; j < this.elements.length; j++) {
/*  71 */       if (this.elements[j] != i.get(j)) {
/*  72 */         return false;
/*     */       }
/*     */     }
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  80 */     int sum = 0;
/*  81 */     for (int element : this.elements) {
/*  82 */       sum = sum * 17 + element;
/*     */     }
/*  84 */     return sum;
/*     */   }
/*     */   
/*     */   public int length()
/*     */   {
/*  89 */     return this.elements.length;
/*     */   }
/*     */   
/*     */ 
/*     */   public IntTuple() {}
/*     */   
/*     */ 
/*     */   public static IntTuple getIntTuple(int num)
/*     */   {
/*  98 */     if (num == 1) {
/*  99 */       return new IntUni();
/*     */     }
/* 101 */     if (num == 2) {
/* 102 */       return new IntPair();
/*     */     }
/* 104 */     if (num == 3) {
/* 105 */       return new IntTriple();
/*     */     }
/* 107 */     if (num == 4) {
/* 108 */       return new IntQuadruple();
/*     */     }
/* 110 */     return new IntTuple(num);
/*     */   }
/*     */   
/*     */ 
/*     */   public static IntTuple getIntTuple(ArrayList integers)
/*     */   {
/* 116 */     IntTuple t = getIntTuple(integers.size());
/* 117 */     for (int i = 0; i < t.length(); i++) {
/* 118 */       t.set(i, ((Integer)integers.get(i)).intValue());
/*     */     }
/* 120 */     return t;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 124 */     String name = "";
/* 125 */     for (int i = 0; i < this.elements.length; i++) {
/* 126 */       name = name + get(i);
/* 127 */       if (i < this.elements.length - 1) {
/* 128 */         name = name + " ";
/*     */       }
/*     */     }
/*     */     
/* 132 */     return name;
/*     */   }
/*     */   
/*     */   public static IntTuple concat(IntTuple t1, IntTuple t2) {
/* 136 */     int n1 = t1.length();
/* 137 */     int n2 = t2.length();
/* 138 */     IntTuple res = getIntTuple(n1 + n2);
/*     */     
/* 140 */     for (int j = 0; j < n1; j++) {
/* 141 */       res.set(j, t1.get(j));
/*     */     }
/* 143 */     for (int i = 0; i < n2; i++) {
/* 144 */       res.set(n1 + i, t2.get(i));
/*     */     }
/* 146 */     return res;
/*     */   }
/*     */   
/*     */   public void print()
/*     */   {
/* 151 */     String s = toString();
/* 152 */     System.out.print(s);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\IntTuple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */