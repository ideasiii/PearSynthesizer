/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayUtils
/*     */ {
/*     */   public static double[] flatten(double[][] array)
/*     */   {
/*  23 */     int size = 0;
/*  24 */     for (double[] a : array) {
/*  25 */       size += a.length;
/*     */     }
/*  27 */     double[] newArray = new double[size];
/*  28 */     int i = 0;
/*  29 */     for (double[] a : array) {
/*  30 */       for (double d : a) {
/*  31 */         newArray[(i++)] = d;
/*     */       }
/*     */     }
/*  34 */     return newArray;
/*     */   }
/*     */   
/*     */   public static double[][] to2D(double[] array, int dim1Size) {
/*  38 */     int dim2Size = array.length / dim1Size;
/*  39 */     return to2D(array, dim1Size, dim2Size);
/*     */   }
/*     */   
/*     */   public static double[][] to2D(double[] array, int dim1Size, int dim2Size) {
/*  43 */     double[][] newArray = new double[dim1Size][dim2Size];
/*  44 */     int k = 0;
/*  45 */     for (int i = 0; i < newArray.length; i++) {
/*  46 */       for (int j = 0; j < newArray[i].length; j++) {
/*  47 */         newArray[i][j] = array[(k++)];
/*     */       }
/*     */     }
/*  50 */     return newArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double[] removeAt(double[] array, int index)
/*     */   {
/*  59 */     if (array == null) {
/*  60 */       return null;
/*     */     }
/*  62 */     if ((index < 0) || (index >= array.length)) {
/*  63 */       return array;
/*     */     }
/*     */     
/*  66 */     double[] retVal = new double[array.length - 1];
/*  67 */     for (int i = 0; i < array.length; i++) {
/*  68 */       if (i < index) {
/*  69 */         retVal[i] = array[i];
/*  70 */       } else if (i > index) {
/*  71 */         retVal[(i - 1)] = array[i];
/*     */       }
/*     */     }
/*  74 */     return retVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object[] removeAt(Object[] array, int index)
/*     */   {
/*  84 */     if (array == null) {
/*  85 */       return null;
/*     */     }
/*  87 */     if ((index < 0) || (index >= array.length)) {
/*  88 */       return array;
/*     */     }
/*     */     
/*  91 */     Object[] retVal = (Object[])Array.newInstance(array[0].getClass(), array.length - 1);
/*  92 */     for (int i = 0; i < array.length; i++) {
/*  93 */       if (i < index) {
/*  94 */         retVal[i] = array[i];
/*  95 */       } else if (i > index) {
/*  96 */         retVal[(i - 1)] = array[i];
/*     */       }
/*     */     }
/*  99 */     return retVal;
/*     */   }
/*     */   
/*     */   public static String toString(int[][] a) {
/* 103 */     StringBuilder result = new StringBuilder("[");
/* 104 */     for (int i = 0; i < a.length; i++) {
/* 105 */       result.append(Arrays.toString(a[i]));
/* 106 */       if (i < a.length - 1)
/* 107 */         result.append(",");
/*     */     }
/* 109 */     result.append("]");
/* 110 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean equalContents(int[][] xs, int[][] ys)
/*     */   {
/* 120 */     if ((xs == null) && (ys != null))
/* 121 */       return false;
/* 122 */     if (ys == null)
/* 123 */       return false;
/* 124 */     if (xs.length != ys.length)
/* 125 */       return false;
/* 126 */     for (int i = xs.length - 1; i >= 0; i--) {
/* 127 */       if (!equalContents(xs[i], ys[i]))
/* 128 */         return false;
/*     */     }
/* 130 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean equals(double[][] xs, double[][] ys)
/*     */   {
/* 140 */     if ((xs == null) && (ys != null))
/* 141 */       return false;
/* 142 */     if (ys == null)
/* 143 */       return false;
/* 144 */     if (xs.length != ys.length)
/* 145 */       return false;
/* 146 */     for (int i = xs.length - 1; i >= 0; i--) {
/* 147 */       if (!Arrays.equals(xs[i], ys[i]))
/* 148 */         return false;
/*     */     }
/* 150 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean equalContents(int[] xs, int[] ys)
/*     */   {
/* 161 */     if (xs.length != ys.length)
/* 162 */       return false;
/* 163 */     for (int i = xs.length - 1; i >= 0; i--) {
/* 164 */       if (xs[i] != ys[i])
/* 165 */         return false;
/*     */     }
/* 167 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean equals(boolean[][] xs, boolean[][] ys)
/*     */   {
/* 177 */     if ((xs == null) && (ys != null))
/* 178 */       return false;
/* 179 */     if (ys == null)
/* 180 */       return false;
/* 181 */     if (xs.length != ys.length)
/* 182 */       return false;
/* 183 */     for (int i = xs.length - 1; i >= 0; i--) {
/* 184 */       if (!Arrays.equals(xs[i], ys[i]))
/* 185 */         return false;
/*     */     }
/* 187 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public static <T> boolean contains(T[] a, T o)
/*     */   {
/* 193 */     for (T item : a) {
/* 194 */       if (item.equals(o)) return true;
/*     */     }
/* 196 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static <T> Set<T> asSet(T[] a)
/*     */   {
/* 202 */     return new HashSet(Arrays.asList(a));
/*     */   }
/*     */   
/*     */   public static void fill(double[][] d, double val) {
/* 206 */     for (int i = 0; i < d.length; i++) {
/* 207 */       Arrays.fill(d[i], val);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void fill(double[][][] d, double val) {
/* 212 */     for (int i = 0; i < d.length; i++) {
/* 213 */       fill(d[i], val);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void fill(double[][][][] d, double val) {
/* 218 */     for (int i = 0; i < d.length; i++) {
/* 219 */       fill(d[i], val);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void fill(boolean[][] d, boolean val) {
/* 224 */     for (int i = 0; i < d.length; i++) {
/* 225 */       Arrays.fill(d[i], val);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void fill(boolean[][][] d, boolean val) {
/* 230 */     for (int i = 0; i < d.length; i++) {
/* 231 */       fill(d[i], val);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void fill(boolean[][][][] d, boolean val) {
/* 236 */     for (int i = 0; i < d.length; i++) {
/* 237 */       fill(d[i], val);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static double[] toDouble(int[] array)
/*     */   {
/* 245 */     double[] rv = new double[array.length];
/* 246 */     for (int i = 0; i < array.length; i++) {
/* 247 */       rv[i] = array[i];
/*     */     }
/* 249 */     return rv;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<Integer> asList(int[] array)
/*     */   {
/* 257 */     List<Integer> l = new ArrayList();
/* 258 */     for (int i : array) {
/* 259 */       l.add(Integer.valueOf(i));
/*     */     }
/* 261 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 268 */     String[] strings = { "a", "b", "c" };
/* 269 */     strings = (String[])removeAt(strings, 2);
/* 270 */     for (String string : strings) {
/* 271 */       System.err.println(string);
/*     */     }
/*     */     
/* 274 */     System.err.println(asSet(new String[] { "larry", "moe", "curly" }));
/*     */   }
/*     */   
/*     */   public static int[] copy(int[] i) {
/* 278 */     if (i == null) return null;
/* 279 */     int[] newI = new int[i.length];
/* 280 */     System.arraycopy(i, 0, newI, 0, i.length);
/* 281 */     return newI;
/*     */   }
/*     */   
/*     */   public static int[][] copy(int[][] i) {
/* 285 */     if (i == null) return (int[][])null;
/* 286 */     int[][] newI = new int[i.length][];
/* 287 */     for (int j = 0; j < newI.length; j++) {
/* 288 */       newI[j] = copy(i[j]);
/*     */     }
/* 290 */     return newI;
/*     */   }
/*     */   
/*     */   public static double[] copy(double[] d)
/*     */   {
/* 295 */     if (d == null) return null;
/* 296 */     double[] newD = new double[d.length];
/* 297 */     System.arraycopy(d, 0, newD, 0, d.length);
/* 298 */     return newD;
/*     */   }
/*     */   
/*     */   public static double[][] copy(double[][] d) {
/* 302 */     if (d == null) return (double[][])null;
/* 303 */     double[][] newD = new double[d.length][];
/* 304 */     for (int i = 0; i < newD.length; i++) {
/* 305 */       newD[i] = copy(d[i]);
/*     */     }
/* 307 */     return newD;
/*     */   }
/*     */   
/*     */   public static double[][][] copy(double[][][] d) {
/* 311 */     if (d == null) return (double[][][])null;
/* 312 */     double[][][] newD = new double[d.length][][];
/* 313 */     for (int i = 0; i < newD.length; i++) {
/* 314 */       newD[i] = copy(d[i]);
/*     */     }
/* 316 */     return newD;
/*     */   }
/*     */   
/*     */   public static float[] copy(float[] d) {
/* 320 */     if (d == null) return null;
/* 321 */     float[] newD = new float[d.length];
/* 322 */     System.arraycopy(d, 0, newD, 0, d.length);
/* 323 */     return newD;
/*     */   }
/*     */   
/*     */   public static float[][] copy(float[][] d) {
/* 327 */     if (d == null) return (float[][])null;
/* 328 */     float[][] newD = new float[d.length][];
/* 329 */     for (int i = 0; i < newD.length; i++) {
/* 330 */       newD[i] = copy(d[i]);
/*     */     }
/* 332 */     return newD;
/*     */   }
/*     */   
/*     */   public static float[][][] copy(float[][][] d) {
/* 336 */     if (d == null) return (float[][][])null;
/* 337 */     float[][][] newD = new float[d.length][][];
/* 338 */     for (int i = 0; i < newD.length; i++) {
/* 339 */       newD[i] = copy(d[i]);
/*     */     }
/* 341 */     return newD;
/*     */   }
/*     */   
/*     */   public static String toString(boolean[][] b)
/*     */   {
/* 346 */     String result = "[";
/* 347 */     for (int i = 0; i < b.length; i++) {
/* 348 */       result = result + Arrays.toString(b[i]);
/* 349 */       if (i < b.length - 1)
/* 350 */         result = result + ",";
/*     */     }
/* 352 */     result = result + "]";
/* 353 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\ArrayUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */