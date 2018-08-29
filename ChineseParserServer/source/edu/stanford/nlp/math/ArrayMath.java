/*      */ package edu.stanford.nlp.math;
/*      */ 
/*      */ import edu.stanford.nlp.util.StringUtils;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ArrayMath
/*      */ {
/*   18 */   private static Random rand = new Random();
/*      */   
/*      */ 
/*      */ 
/*      */   public static int numRows(double[] v)
/*      */   {
/*   24 */     return v.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static float[] doubleArrayToFloatArray(double[] a)
/*      */   {
/*   31 */     float[] result = new float[a.length];
/*   32 */     for (int i = 0; i < a.length; i++) {
/*   33 */       result[i] = ((float)a[i]);
/*      */     }
/*   35 */     return result;
/*      */   }
/*      */   
/*      */   public static double[] floatArrayToDoubleArray(float[] a) {
/*   39 */     double[] result = new double[a.length];
/*   40 */     for (int i = 0; i < a.length; i++) {
/*   41 */       result[i] = a[i];
/*      */     }
/*   43 */     return result;
/*      */   }
/*      */   
/*      */   public static double[][] floatArrayToDoubleArray(float[][] a) {
/*   47 */     double[][] result = new double[a.length][];
/*   48 */     for (int i = 0; i < a.length; i++) {
/*   49 */       result[i] = new double[a[i].length];
/*   50 */       for (int j = 0; j < a[i].length; j++) {
/*   51 */         result[i][j] = a[i][j];
/*      */       }
/*      */     }
/*   54 */     return result;
/*      */   }
/*      */   
/*      */   public static float[][] doubleArrayToFloatArray(double[][] a) {
/*   58 */     float[][] result = new float[a.length][];
/*   59 */     for (int i = 0; i < a.length; i++) {
/*   60 */       result[i] = new float[a[i].length];
/*   61 */       for (int j = 0; j < a[i].length; j++) {
/*   62 */         result[i][j] = ((float)a[i][j]);
/*      */       }
/*      */     }
/*   65 */     return result;
/*      */   }
/*      */   
/*      */   public static int makeIntFromByte4(byte[] b, int offset) {
/*   69 */     return (b[(offset + 3)] & 0xFF) << 24 | (b[(offset + 2)] & 0xFF) << 16 | (b[(offset + 1)] & 0xFF) << 8 | b[offset] & 0xFF;
/*      */   }
/*      */   
/*      */   public static int makeIntFromByte2(byte[] b, int offset) {
/*   73 */     return (b[(offset + 1)] & 0xFF) << 8 | b[offset] & 0xFF;
/*      */   }
/*      */   
/*      */ 
/*      */   public static double[] exp(double[] a)
/*      */   {
/*   79 */     double[] result = new double[a.length];
/*   80 */     for (int i = 0; i < a.length; i++) {
/*   81 */       result[i] = Math.exp(a[i]);
/*      */     }
/*   83 */     return result;
/*      */   }
/*      */   
/*      */   public static double[] log(double[] a) {
/*   87 */     double[] result = new double[a.length];
/*   88 */     for (int i = 0; i < a.length; i++) {
/*   89 */       result[i] = Math.log(a[i]);
/*      */     }
/*   91 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public static void expInPlace(double[] a)
/*      */   {
/*   97 */     for (int i = 0; i < a.length; i++) {
/*   98 */       a[i] = Math.exp(a[i]);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void logInPlace(double[] a) {
/*  103 */     for (int i = 0; i < a.length; i++) {
/*  104 */       a[i] = Math.log(a[i]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void addInPlace(double[] a, double b)
/*      */   {
/*  114 */     for (int i = 0; i < a.length; i++) {
/*  115 */       a[i] += b;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void addInPlace(float[] a, double b)
/*      */   {
/*  123 */     for (int i = 0; i < a.length; i++) {
/*  124 */       a[i] = ((float)(a[i] + b));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void multiplyInPlace(double[] a, double b)
/*      */   {
/*  132 */     for (int i = 0; i < a.length; i++) {
/*  133 */       a[i] *= b;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void multiplyInPlace(float[] a, double b)
/*      */   {
/*  141 */     for (int i = 0; i < a.length; i++) {
/*  142 */       a[i] = ((float)(a[i] * b));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void powInPlace(double[] a, double c)
/*      */   {
/*  150 */     for (int i = 0; i < a.length; i++) {
/*  151 */       a[i] = Math.pow(a[i], c);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void powInPlace(float[] a, float c)
/*      */   {
/*  159 */     for (int i = 0; i < a.length; i++) {
/*  160 */       a[i] = ((float)Math.pow(a[i], c));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static double[] add(double[] a, double c)
/*      */   {
/*  167 */     double[] result = new double[a.length];
/*  168 */     for (int i = 0; i < a.length; i++) {
/*  169 */       a[i] += c;
/*      */     }
/*  171 */     return result;
/*      */   }
/*      */   
/*      */   public static float[] add(float[] a, double c) {
/*  175 */     float[] result = new float[a.length];
/*  176 */     for (int i = 0; i < a.length; i++) {
/*  177 */       result[i] = ((float)(a[i] + c));
/*      */     }
/*  179 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static double[] multiply(double[] a, double c)
/*      */   {
/*  186 */     double[] result = new double[a.length];
/*  187 */     for (int i = 0; i < a.length; i++) {
/*  188 */       a[i] *= c;
/*      */     }
/*  190 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static float[] multiply(float[] a, float c)
/*      */   {
/*  197 */     float[] result = new float[a.length];
/*  198 */     for (int i = 0; i < a.length; i++) {
/*  199 */       a[i] *= c;
/*      */     }
/*  201 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static double[] pow(double[] a, double c)
/*      */   {
/*  208 */     double[] result = new double[a.length];
/*  209 */     for (int i = 0; i < a.length; i++) {
/*  210 */       result[i] = Math.pow(a[i], c);
/*      */     }
/*  212 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static float[] pow(float[] a, float c)
/*      */   {
/*  219 */     float[] result = new float[a.length];
/*  220 */     for (int i = 0; i < a.length; i++) {
/*  221 */       result[i] = ((float)Math.pow(a[i], c));
/*      */     }
/*  223 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public static void pairwiseAddInPlace(double[] to, double[] from)
/*      */   {
/*  229 */     if (to.length != from.length) {
/*  230 */       throw new RuntimeException();
/*      */     }
/*  232 */     for (int i = 0; i < to.length; i++) {
/*  233 */       to[i] += from[i];
/*      */     }
/*      */   }
/*      */   
/*      */   public static void pairwiseSubtractInPlace(double[] to, double[] from) {
/*  238 */     if (to.length != from.length) {
/*  239 */       throw new RuntimeException();
/*      */     }
/*  241 */     for (int i = 0; i < to.length; i++) {
/*  242 */       to[i] -= from[i];
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static int[] pairwiseAdd(int[] a, int[] b)
/*      */   {
/*  249 */     int[] result = new int[a.length];
/*  250 */     for (int i = 0; i < a.length; i++) {
/*  251 */       a[i] += b[i];
/*      */     }
/*  253 */     return result;
/*      */   }
/*      */   
/*      */   public static double[] pairwiseAdd(double[] a, double[] b) {
/*  257 */     double[] result = new double[a.length];
/*  258 */     for (int i = 0; i < a.length; i++) {
/*  259 */       a[i] += b[i];
/*      */     }
/*  261 */     return result;
/*      */   }
/*      */   
/*      */   public static float[] pairwiseAdd(float[] a, float[] b) {
/*  265 */     float[] result = new float[a.length];
/*  266 */     for (int i = 0; i < a.length; i++) {
/*  267 */       a[i] += b[i];
/*      */     }
/*  269 */     return result;
/*      */   }
/*      */   
/*      */   public static double[] pairwiseSubtract(double[] a, double[] b) {
/*  273 */     double[] c = new double[a.length];
/*      */     
/*  275 */     for (int i = 0; i < a.length; i++) {
/*  276 */       a[i] -= b[i];
/*      */     }
/*  278 */     return c;
/*      */   }
/*      */   
/*      */   public static float[] pairwiseSubtract(float[] a, float[] b) {
/*  282 */     float[] c = new float[a.length];
/*      */     
/*  284 */     for (int i = 0; i < a.length; i++) {
/*  285 */       a[i] -= b[i];
/*      */     }
/*  287 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static double[] pairwiseMultiply(double[] a, double[] b)
/*      */   {
/*  294 */     if (a.length != b.length) {
/*  295 */       throw new RuntimeException("Can't pairwise multiple different lengths: a.length=" + a.length + " b.length=" + b.length);
/*      */     }
/*  297 */     double[] result = new double[a.length];
/*  298 */     for (int i = 0; i < result.length; i++) {
/*  299 */       a[i] *= b[i];
/*      */     }
/*  301 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static float[] pairwiseMultiply(float[] a, float[] b)
/*      */   {
/*  308 */     if (a.length != b.length) {
/*  309 */       throw new RuntimeException();
/*      */     }
/*  311 */     float[] result = new float[a.length];
/*  312 */     for (int i = 0; i < result.length; i++) {
/*  313 */       a[i] *= b[i];
/*      */     }
/*  315 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void pairwiseMultiply(double[] a, double[] b, double[] result)
/*      */   {
/*  323 */     if (a.length != b.length) {
/*  324 */       throw new RuntimeException();
/*      */     }
/*  326 */     for (int i = 0; i < result.length; i++) {
/*  327 */       a[i] *= b[i];
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void pairwiseMultiply(float[] a, float[] b, float[] result)
/*      */   {
/*  336 */     if (a.length != b.length) {
/*  337 */       throw new RuntimeException();
/*      */     }
/*  339 */     for (int i = 0; i < result.length; i++) {
/*  340 */       a[i] *= b[i];
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static boolean hasNaN(double[] a)
/*      */   {
/*  347 */     for (int i = 0; i < a.length; i++) {
/*  348 */       if (Double.isNaN(a[i])) return true;
/*      */     }
/*  350 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean hasInfinite(double[] a) {
/*  354 */     for (int i = 0; i < a.length; i++) {
/*  355 */       if (Double.isInfinite(a[i])) return true;
/*      */     }
/*  357 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public static int countNaN(double[] v)
/*      */   {
/*  363 */     int c = 0;
/*  364 */     for (int i = 0; i < v.length; i++)
/*  365 */       if (Double.isNaN(v[i]))
/*  366 */         c++;
/*  367 */     return c;
/*      */   }
/*      */   
/*      */   public static double[] filterNaN(double[] v) {
/*  371 */     double[] u = new double[numRows(v) - countNaN(v)];
/*  372 */     int j = 0;
/*  373 */     for (int i = 0; i < v.length; i++) {
/*  374 */       if (!Double.isNaN(v[i])) {
/*  375 */         u[(j++)] = v[i];
/*      */       }
/*      */     }
/*  378 */     return u;
/*      */   }
/*      */   
/*      */   public static int countInfinite(double[] v) {
/*  382 */     int c = 0;
/*  383 */     for (int i = 0; i < v.length; i++)
/*  384 */       if (Double.isInfinite(v[i]))
/*  385 */         c++;
/*  386 */     return c;
/*      */   }
/*      */   
/*      */   public static double[] filterInfinite(double[] v) {
/*  390 */     double[] u = new double[numRows(v) - countInfinite(v)];
/*  391 */     int j = 0;
/*  392 */     for (int i = 0; i < v.length; i++) {
/*  393 */       if (!Double.isInfinite(v[i])) {
/*  394 */         u[(j++)] = v[i];
/*      */       }
/*      */     }
/*  397 */     return u;
/*      */   }
/*      */   
/*      */   public static double[] filterNaNAndInfinite(double[] v) {
/*  401 */     return filterInfinite(filterNaN(v));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double sum(double[] a)
/*      */   {
/*  411 */     return sum(a, 0, a.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double sum(double[] a, int fromIndex, int toIndex)
/*      */   {
/*  420 */     double result = 0.0D;
/*  421 */     for (int i = fromIndex; i < toIndex; i++) {
/*  422 */       result += a[i];
/*      */     }
/*  424 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public static int sum(int[] a)
/*      */   {
/*  430 */     int result = 0;
/*  431 */     for (int i = 0; i < a.length; i++) {
/*  432 */       result += a[i];
/*      */     }
/*  434 */     return result;
/*      */   }
/*      */   
/*      */   public static float sum(float[] a) {
/*  438 */     float result = 0.0F;
/*  439 */     for (int i = 0; i < a.length; i++) {
/*  440 */       result += a[i];
/*      */     }
/*  442 */     return result;
/*      */   }
/*      */   
/*      */   public static int sum(int[][] a) {
/*  446 */     int result = 0;
/*  447 */     for (int i = 0; i < a.length; i++) {
/*  448 */       for (int j = 0; j < a[i].length; j++) {
/*  449 */         result += a[i][j];
/*      */       }
/*      */     }
/*  452 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int[] diag(int[][] a)
/*      */   {
/*  459 */     int[] rv = new int[a.length];
/*  460 */     for (int i = 0; i < a.length; i++) {
/*  461 */       rv[i] = a[i][i];
/*      */     }
/*  463 */     return rv;
/*      */   }
/*      */   
/*      */   public static double average(double[] a) {
/*  467 */     double total = sum(a);
/*  468 */     return total / a.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double norm_inf(double[] a)
/*      */   {
/*  478 */     double max = Double.NEGATIVE_INFINITY;
/*  479 */     for (int i = 0; i < a.length; i++) {
/*  480 */       if (Math.abs(a[i]) > max) {
/*  481 */         max = Math.abs(a[i]);
/*      */       }
/*      */     }
/*  484 */     return max;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double norm_inf(float[] a)
/*      */   {
/*  495 */     double max = Double.NEGATIVE_INFINITY;
/*  496 */     for (int i = 0; i < a.length; i++) {
/*  497 */       if (Math.abs(a[i]) > max) {
/*  498 */         max = Math.abs(a[i]);
/*      */       }
/*      */     }
/*  501 */     return max;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double norm_1(double[] a)
/*      */   {
/*  511 */     double sum = 0.0D;
/*  512 */     for (int i = 0; i < a.length; i++) {
/*  513 */       sum += (a[i] < 0.0D ? -a[i] : a[i]);
/*      */     }
/*  515 */     return sum;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double norm_1(float[] a)
/*      */   {
/*  525 */     double sum = 0.0D;
/*  526 */     for (int i = 0; i < a.length; i++) {
/*  527 */       sum += (a[i] < 0.0F ? -a[i] : a[i]);
/*      */     }
/*  529 */     return sum;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double norm(double[] a)
/*      */   {
/*  540 */     double squaredSum = 0.0D;
/*  541 */     for (int i = 0; i < a.length; i++) {
/*  542 */       squaredSum += a[i] * a[i];
/*      */     }
/*  544 */     return Math.sqrt(squaredSum);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double norm(float[] a)
/*      */   {
/*  554 */     double squaredSum = 0.0D;
/*  555 */     for (int i = 0; i < a.length; i++) {
/*  556 */       squaredSum += a[i] * a[i];
/*      */     }
/*  558 */     return Math.sqrt(squaredSum);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int argmax(double[] a)
/*      */   {
/*  565 */     double max = Double.NEGATIVE_INFINITY;
/*  566 */     int argmax = 0;
/*  567 */     for (int i = 0; i < a.length; i++) {
/*  568 */       if (a[i] > max) {
/*  569 */         max = a[i];
/*  570 */         argmax = i;
/*      */       }
/*      */     }
/*  573 */     return argmax;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int argmax_tieLast(double[] a)
/*      */   {
/*  580 */     double max = Double.NEGATIVE_INFINITY;
/*  581 */     int argmax = 0;
/*  582 */     for (int i = 0; i < a.length; i++) {
/*  583 */       if (a[i] >= max) {
/*  584 */         max = a[i];
/*  585 */         argmax = i;
/*      */       }
/*      */     }
/*  588 */     return argmax;
/*      */   }
/*      */   
/*      */   public static double max(double[] a) {
/*  592 */     return a[argmax(a)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int argmax(float[] a)
/*      */   {
/*  599 */     float max = Float.NEGATIVE_INFINITY;
/*  600 */     int argmax = 0;
/*  601 */     for (int i = 0; i < a.length; i++) {
/*  602 */       if (a[i] > max) {
/*  603 */         max = a[i];
/*  604 */         argmax = i;
/*      */       }
/*      */     }
/*  607 */     return argmax;
/*      */   }
/*      */   
/*      */   public static float max(float[] a) {
/*  611 */     return a[argmax(a)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int argmin(double[] a)
/*      */   {
/*  618 */     double min = Double.POSITIVE_INFINITY;
/*  619 */     int argmin = 0;
/*  620 */     for (int i = 0; i < a.length; i++) {
/*  621 */       if (a[i] < min) {
/*  622 */         min = a[i];
/*  623 */         argmin = i;
/*      */       }
/*      */     }
/*  626 */     return argmin;
/*      */   }
/*      */   
/*      */   public static double min(double[] a) {
/*  630 */     return a[argmin(a)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double safeMin(double[] v)
/*      */   {
/*  639 */     double[] u = filterNaNAndInfinite(v);
/*  640 */     if (numRows(u) == 0) return 0.0D;
/*  641 */     return min(u);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int argmin(float[] a)
/*      */   {
/*  648 */     float min = Float.POSITIVE_INFINITY;
/*  649 */     int argmin = 0;
/*  650 */     for (int i = 0; i < a.length; i++) {
/*  651 */       if (a[i] < min) {
/*  652 */         min = a[i];
/*  653 */         argmin = i;
/*      */       }
/*      */     }
/*  656 */     return argmin;
/*      */   }
/*      */   
/*      */   public static float min(float[] a) {
/*  660 */     return a[argmin(a)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int argmin(int[] a)
/*      */   {
/*  667 */     int min = Integer.MAX_VALUE;
/*  668 */     int argmin = 0;
/*  669 */     for (int i = 0; i < a.length; i++) {
/*  670 */       if (a[i] < min) {
/*  671 */         min = a[i];
/*  672 */         argmin = i;
/*      */       }
/*      */     }
/*  675 */     return argmin;
/*      */   }
/*      */   
/*      */   public static int min(int[] a) {
/*  679 */     return a[argmin(a)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int argmax(int[] a)
/*      */   {
/*  686 */     int max = Integer.MIN_VALUE;
/*  687 */     int argmax = 0;
/*  688 */     for (int i = 0; i < a.length; i++) {
/*  689 */       if (a[i] > max) {
/*  690 */         max = a[i];
/*  691 */         argmax = i;
/*      */       }
/*      */     }
/*  694 */     return argmax;
/*      */   }
/*      */   
/*      */   public static int max(int[] a) {
/*  698 */     return a[argmax(a)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double safeMax(double[] v)
/*      */   {
/*  707 */     double[] u = filterNaNAndInfinite(v);
/*  708 */     if (numRows(u) == 0) return 0.0D;
/*  709 */     return max(u);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double logSum(double[] logInputs)
/*      */   {
/*  723 */     return logSum(logInputs, 0, logInputs.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double logSum(double[] logInputs, int fromIndex, int toIndex)
/*      */   {
/*  742 */     if (logInputs.length == 0)
/*  743 */       throw new IllegalArgumentException();
/*  744 */     if ((fromIndex >= 0) && (toIndex < logInputs.length) && (fromIndex >= toIndex))
/*  745 */       return Double.NEGATIVE_INFINITY;
/*  746 */     int maxIdx = fromIndex;
/*  747 */     double max = logInputs[fromIndex];
/*  748 */     for (int i = fromIndex + 1; i < toIndex; i++) {
/*  749 */       if (logInputs[i] > max) {
/*  750 */         maxIdx = i;
/*  751 */         max = logInputs[i];
/*      */       }
/*      */     }
/*  754 */     boolean haveTerms = false;
/*  755 */     double intermediate = 0.0D;
/*  756 */     double cutoff = max - 30.0D;
/*      */     
/*  758 */     for (int i = fromIndex; i < toIndex; i++) {
/*  759 */       if ((i != maxIdx) && (logInputs[i] > cutoff)) {
/*  760 */         haveTerms = true;
/*  761 */         intermediate += Math.exp(logInputs[i] - max);
/*      */       }
/*      */     }
/*  764 */     if (haveTerms) {
/*  765 */       return max + Math.log(1.0D + intermediate);
/*      */     }
/*  767 */     return max;
/*      */   }
/*      */   
/*      */   public static double logSum(List<Double> logInputs)
/*      */   {
/*  772 */     return logSum(logInputs, 0, logInputs.size());
/*      */   }
/*      */   
/*      */   public static double logSum(List<Double> logInputs, int fromIndex, int toIndex) {
/*  776 */     int length = logInputs.size();
/*  777 */     if (length == 0)
/*  778 */       throw new IllegalArgumentException();
/*  779 */     if ((fromIndex >= 0) && (toIndex < length) && (fromIndex >= toIndex))
/*  780 */       return Double.NEGATIVE_INFINITY;
/*  781 */     int maxIdx = fromIndex;
/*  782 */     double max = ((Double)logInputs.get(fromIndex)).doubleValue();
/*  783 */     for (int i = fromIndex + 1; i < toIndex; i++) {
/*  784 */       double d = ((Double)logInputs.get(i)).doubleValue();
/*  785 */       if (d > max) {
/*  786 */         maxIdx = i;
/*  787 */         max = d;
/*      */       }
/*      */     }
/*  790 */     boolean haveTerms = false;
/*  791 */     double intermediate = 0.0D;
/*  792 */     double cutoff = max - 30.0D;
/*      */     
/*  794 */     for (int i = fromIndex; i < toIndex; i++) {
/*  795 */       double d = ((Double)logInputs.get(i)).doubleValue();
/*  796 */       if ((i != maxIdx) && (d > cutoff)) {
/*  797 */         haveTerms = true;
/*  798 */         intermediate += Math.exp(d - max);
/*      */       }
/*      */     }
/*  801 */     if (haveTerms) {
/*  802 */       return max + Math.log(1.0D + intermediate);
/*      */     }
/*  804 */     return max;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static float logSum(float[] logInputs)
/*      */   {
/*  820 */     int leng = logInputs.length;
/*  821 */     if (leng == 0) {
/*  822 */       throw new IllegalArgumentException();
/*      */     }
/*  824 */     int maxIdx = 0;
/*  825 */     float max = logInputs[0];
/*  826 */     for (int i = 1; i < leng; i++) {
/*  827 */       if (logInputs[i] > max) {
/*  828 */         maxIdx = i;
/*  829 */         max = logInputs[i];
/*      */       }
/*      */     }
/*  832 */     boolean haveTerms = false;
/*  833 */     double intermediate = 0.0D;
/*  834 */     float cutoff = max - 20.0F;
/*      */     
/*  836 */     for (int i = 0; i < leng; i++) {
/*  837 */       if ((i != maxIdx) && (logInputs[i] > cutoff)) {
/*  838 */         haveTerms = true;
/*  839 */         intermediate += Math.exp(logInputs[i] - max);
/*      */       }
/*      */     }
/*  842 */     if (haveTerms) {
/*  843 */       return max + (float)Math.log(1.0D + intermediate);
/*      */     }
/*  845 */     return max;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static double innerProduct(double[] a, double[] b)
/*      */   {
/*  852 */     double result = 0.0D;
/*  853 */     for (int i = 0; i < a.length; i++) {
/*  854 */       result += a[i] * b[i];
/*      */     }
/*  856 */     return result;
/*      */   }
/*      */   
/*      */   public static double innerProduct(float[] a, float[] b) {
/*  860 */     double result = 0.0D;
/*  861 */     for (int i = 0; i < a.length; i++) {
/*  862 */       result += a[i] * b[i];
/*      */     }
/*  864 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public static int[] subArray(int[] a, int from, int to)
/*      */   {
/*  870 */     int[] result = new int[to - from];
/*  871 */     System.arraycopy(a, from, result, 0, to - from);
/*  872 */     return result;
/*      */   }
/*      */   
/*      */   public static double[][] load2DMatrixFromFile(String filename) throws IOException {
/*  876 */     String s = StringUtils.slurpFile(filename);
/*  877 */     String[] rows = s.split("[\r\n]+");
/*  878 */     double[][] result = new double[rows.length][];
/*  879 */     for (int i = 0; i < result.length; i++) {
/*  880 */       String[] columns = rows[i].split("\\s+");
/*  881 */       result[i] = new double[columns.length];
/*  882 */       for (int j = 0; j < result[i].length; j++) {
/*  883 */         result[i][j] = Double.parseDouble(columns[j]);
/*      */       }
/*      */     }
/*  886 */     return result;
/*      */   }
/*      */   
/*      */   public static Integer[] box(int[] assignment) {
/*  890 */     Integer[] result = new Integer[assignment.length];
/*  891 */     for (int i = 0; i < assignment.length; i++) {
/*  892 */       result[i] = Integer.valueOf(assignment[i]);
/*      */     }
/*  894 */     return result;
/*      */   }
/*      */   
/*      */   public static int[] unbox(List<Integer> integerList) {
/*  898 */     int[] result = new int[integerList.size()];
/*  899 */     for (int i = 0; i < integerList.size(); i++) {
/*  900 */       result[i] = ((Integer)integerList.get(i)).intValue();
/*      */     }
/*  902 */     return result;
/*      */   }
/*      */   
/*      */   public static Double[] box(double[] assignment) {
/*  906 */     Double[] result = new Double[assignment.length];
/*  907 */     for (int i = 0; i < assignment.length; i++) {
/*  908 */       result[i] = Double.valueOf(assignment[i]);
/*      */     }
/*  910 */     return result;
/*      */   }
/*      */   
/*      */   public static double[] unbox(List<Double> integerList) {
/*  914 */     double[] result = new double[integerList.size()];
/*  915 */     for (int i = 0; i < integerList.size(); i++) {
/*  916 */       result[i] = ((Double)integerList.get(i)).doubleValue();
/*      */     }
/*  918 */     return result;
/*      */   }
/*      */   
/*      */   public static int indexOf(int n, int[] a) {
/*  922 */     for (int i = 0; i < a.length; i++) {
/*  923 */       if (a[i] == n) return i;
/*      */     }
/*  925 */     return -1;
/*      */   }
/*      */   
/*      */   public static int[][] castToInt(double[][] doubleCounts) {
/*  929 */     int[][] result = new int[doubleCounts.length][];
/*  930 */     for (int i = 0; i < doubleCounts.length; i++) {
/*  931 */       result[i] = new int[doubleCounts[i].length];
/*  932 */       for (int j = 0; j < doubleCounts[i].length; j++) {
/*  933 */         result[i][j] = ((int)doubleCounts[i][j]);
/*      */       }
/*      */     }
/*  936 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void normalize(double[] a)
/*      */   {
/*  946 */     double total = sum(a);
/*  947 */     if ((total == 0.0D) || (Double.isNaN(total))) {
/*  948 */       throw new RuntimeException("Can't normalize an array with sum 0.0 or NaN: " + Arrays.toString(a));
/*      */     }
/*  950 */     multiplyInPlace(a, 1.0D / total);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void normalize(float[] a)
/*      */   {
/*  958 */     float total = sum(a);
/*  959 */     if ((total == 0.0D) || (Double.isNaN(total))) {
/*  960 */       throw new RuntimeException("Can't normalize an array with sum 0.0 or NaN");
/*      */     }
/*  962 */     multiplyInPlace(a, 1.0D / total);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void logNormalize(double[] a)
/*      */   {
/*  970 */     double logTotal = logSum(a);
/*  971 */     if (logTotal == Double.NEGATIVE_INFINITY)
/*      */     {
/*  973 */       double v = -Math.log(a.length);
/*  974 */       for (int i = 0; i < a.length; i++) {
/*  975 */         a[i] = v;
/*      */       }
/*  977 */       return;
/*      */     }
/*  979 */     addInPlace(a, -logTotal);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int sampleFromDistribution(double[] d)
/*      */   {
/*  990 */     return sampleFromDistribution(d, rand);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int sampleFromDistribution(double[] d, Random random)
/*      */   {
/* 1002 */     double r = random.nextDouble();
/*      */     
/* 1004 */     double total = 0.0D;
/* 1005 */     for (int i = 0; i < d.length - 1; i++) {
/* 1006 */       if (Double.isNaN(d[i])) {
/* 1007 */         throw new RuntimeException("Can't sample from NaN");
/*      */       }
/* 1009 */       total += d[i];
/* 1010 */       if (r < total) {
/* 1011 */         return i;
/*      */       }
/*      */     }
/* 1014 */     return d.length - 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int sampleFromDistribution(float[] d, Random random)
/*      */   {
/* 1026 */     double r = random.nextDouble();
/*      */     
/* 1028 */     double total = 0.0D;
/* 1029 */     for (int i = 0; i < d.length - 1; i++) {
/* 1030 */       if (Float.isNaN(d[i])) {
/* 1031 */         throw new RuntimeException("Can't sample from NaN");
/*      */       }
/* 1033 */       total += d[i];
/* 1034 */       if (r < total) {
/* 1035 */         return i;
/*      */       }
/*      */     }
/* 1038 */     return d.length - 1;
/*      */   }
/*      */   
/*      */   public static double klDivergence(double[] from, double[] to) {
/* 1042 */     double kl = 0.0D;
/* 1043 */     double tot = sum(from);
/* 1044 */     double tot2 = sum(to);
/*      */     
/* 1046 */     for (int i = 0; i < from.length; i++)
/* 1047 */       if (from[i] != 0.0D)
/*      */       {
/*      */ 
/* 1050 */         double num = from[i] / tot;
/* 1051 */         double num2 = to[i] / tot2;
/*      */         
/* 1053 */         kl += num * (Math.log(num / num2) / Math.log(2.0D));
/*      */       }
/* 1055 */     return kl;
/*      */   }
/*      */   
/*      */   public static void setToLogDeterministic(float[] a, int i)
/*      */   {
/* 1060 */     for (int j = 0; j < a.length; j++) {
/* 1061 */       if (j == i) {
/* 1062 */         a[j] = 0.0F;
/*      */       } else {
/* 1064 */         a[j] = Float.NEGATIVE_INFINITY;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void setToLogDeterministic(double[] a, int i) {
/* 1070 */     for (int j = 0; j < a.length; j++) {
/* 1071 */       if (j == i) {
/* 1072 */         a[j] = 0.0D;
/*      */       } else {
/* 1074 */         a[j] = Double.NEGATIVE_INFINITY;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static double mean(double[] a)
/*      */   {
/* 1082 */     return sum(a) / a.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double safeMean(double[] v)
/*      */   {
/* 1090 */     double[] u = filterNaNAndInfinite(v);
/* 1091 */     if (numRows(u) == 0) return 0.0D;
/* 1092 */     return mean(u);
/*      */   }
/*      */   
/*      */   public static double sumSquaredError(double[] a) {
/* 1096 */     double mean = mean(a);
/* 1097 */     double result = 0.0D;
/* 1098 */     for (int i = 0; i < a.length; i++) {
/* 1099 */       double diff = a[i] - mean;
/* 1100 */       result += diff * diff;
/*      */     }
/* 1102 */     return result;
/*      */   }
/*      */   
/*      */   public static double variance(double[] a) {
/* 1106 */     return sumSquaredError(a) / (a.length - 1);
/*      */   }
/*      */   
/*      */   public static double stdev(double[] a) {
/* 1110 */     return Math.sqrt(variance(a));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double safeStdev(double[] v)
/*      */   {
/* 1119 */     double[] u = filterNaNAndInfinite(v);
/* 1120 */     if (numRows(u) < 2) return 1.0D;
/* 1121 */     return stdev(u);
/*      */   }
/*      */   
/*      */   public static double standardErrorOfMean(double[] a) {
/* 1125 */     return stdev(a) / Math.sqrt(a.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void sampleWithoutReplacement(int[] array, int numArgClasses)
/*      */   {
/* 1135 */     sampleWithoutReplacement(array, numArgClasses, rand);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void sampleWithoutReplacement(int[] array, int numArgClasses, Random rand)
/*      */   {
/* 1143 */     int[] temp = new int[numArgClasses];
/* 1144 */     for (int i = 0; i < temp.length; i++) {
/* 1145 */       temp[i] = i;
/*      */     }
/* 1147 */     shuffle(temp, rand);
/* 1148 */     System.arraycopy(temp, 0, array, 0, array.length);
/*      */   }
/*      */   
/*      */   public static void shuffle(int[] a) {
/* 1152 */     shuffle(a, rand);
/*      */   }
/*      */   
/*      */   public static void shuffle(int[] a, Random rand) {
/* 1156 */     for (int i = a.length - 1; i >= 1; i--) {
/* 1157 */       int j = rand.nextInt(i + 1);
/* 1158 */       int tmp = a[i];
/* 1159 */       a[i] = a[j];
/* 1160 */       a[j] = tmp;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reverse(int[] a) {
/* 1165 */     for (int i = 0; i < a.length / 2; i++) {
/* 1166 */       int j = a.length - i - 1;
/* 1167 */       int tmp = a[i];
/* 1168 */       a[i] = a[j];
/* 1169 */       a[j] = tmp;
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean contains(int[] a, int i) {
/* 1174 */     for (int j = 0; j < a.length; j++) {
/* 1175 */       if (a[j] == i) return true;
/*      */     }
/* 1177 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean containsInSubarray(int[] a, int begin, int end, int i) {
/* 1181 */     for (int j = begin; j < end; j++) {
/* 1182 */       if (a[j] == i) return true;
/*      */     }
/* 1184 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double sigLevelByApproxRand(double[] A, double[] B)
/*      */   {
/* 1193 */     return sigLevelByApproxRand(A, B, 1000);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static double sigLevelByApproxRand(double[] A, double[] B, int iterations)
/*      */   {
/* 1212 */     if (A.length == 0)
/* 1213 */       throw new IllegalArgumentException("Input arrays must not be empty!");
/* 1214 */     if (A.length != B.length)
/* 1215 */       throw new IllegalArgumentException("Input arrays must have equal length!");
/* 1216 */     if (iterations <= 0)
/* 1217 */       throw new IllegalArgumentException("Number of iterations must be positive!");
/* 1218 */     double testStatistic = absDiffOfMeans(A, B, false);
/* 1219 */     int successes = 0;
/* 1220 */     for (int i = 0; i < iterations; i++) {
/* 1221 */       double t = absDiffOfMeans(A, B, true);
/* 1222 */       if (t >= testStatistic) successes++;
/*      */     }
/* 1224 */     return (successes + 1) / (iterations + 1);
/*      */   }
/*      */   
/*      */   public static double sigLevelByApproxRand(int[] A, int[] B) {
/* 1228 */     return sigLevelByApproxRand(A, B, 1000);
/*      */   }
/*      */   
/*      */   public static double sigLevelByApproxRand(int[] A, int[] B, int iterations) {
/* 1232 */     if (A.length == 0)
/* 1233 */       throw new IllegalArgumentException("Input arrays must not be empty!");
/* 1234 */     if (A.length != B.length)
/* 1235 */       throw new IllegalArgumentException("Input arrays must have equal length!");
/* 1236 */     if (iterations <= 0)
/* 1237 */       throw new IllegalArgumentException("Number of iterations must be positive!");
/* 1238 */     double[] X = new double[A.length];
/* 1239 */     double[] Y = new double[B.length];
/* 1240 */     for (int i = 0; i < A.length; i++) {
/* 1241 */       X[i] = A[i];
/* 1242 */       Y[i] = B[i];
/*      */     }
/* 1244 */     return sigLevelByApproxRand(X, Y, iterations);
/*      */   }
/*      */   
/*      */   public static double sigLevelByApproxRand(boolean[] A, boolean[] B) {
/* 1248 */     return sigLevelByApproxRand(A, B, 1000);
/*      */   }
/*      */   
/*      */   public static double sigLevelByApproxRand(boolean[] A, boolean[] B, int iterations) {
/* 1252 */     if (A.length == 0)
/* 1253 */       throw new IllegalArgumentException("Input arrays must not be empty!");
/* 1254 */     if (A.length != B.length)
/* 1255 */       throw new IllegalArgumentException("Input arrays must have equal length!");
/* 1256 */     if (iterations <= 0)
/* 1257 */       throw new IllegalArgumentException("Number of iterations must be positive!");
/* 1258 */     double[] X = new double[A.length];
/* 1259 */     double[] Y = new double[B.length];
/* 1260 */     for (int i = 0; i < A.length; i++) {
/* 1261 */       X[i] = (A[i] != 0 ? 1.0D : 0.0D);
/* 1262 */       Y[i] = (B[i] != 0 ? 1.0D : 0.0D);
/*      */     }
/* 1264 */     return sigLevelByApproxRand(X, Y, iterations);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static double absDiffOfMeans(double[] A, double[] B, boolean randomize)
/*      */   {
/* 1272 */     Random random = new Random();
/* 1273 */     double aTotal = 0.0D;
/* 1274 */     double bTotal = 0.0D;
/* 1275 */     for (int i = 0; i < A.length; i++) {
/* 1276 */       if ((randomize) && (random.nextBoolean())) {
/* 1277 */         aTotal += B[i];
/* 1278 */         bTotal += A[i];
/*      */       } else {
/* 1280 */         aTotal += A[i];
/* 1281 */         bTotal += B[i];
/*      */       }
/*      */     }
/* 1284 */     double aMean = aTotal / A.length;
/* 1285 */     double bMean = bTotal / B.length;
/* 1286 */     return Math.abs(aMean - bMean);
/*      */   }
/*      */   
/*      */ 
/*      */   public static String toBinaryString(byte[] b)
/*      */   {
/* 1292 */     StringBuilder s = new StringBuilder();
/* 1293 */     for (int i = 0; i < b.length; i++) {
/* 1294 */       for (int j = 7; j >= 0; j--) {
/* 1295 */         if ((b[i] & 1 << j) > 0) {
/* 1296 */           s.append("1");
/*      */         } else {
/* 1298 */           s.append("0");
/*      */         }
/*      */       }
/* 1301 */       s.append(" ");
/*      */     }
/* 1303 */     return s.toString();
/*      */   }
/*      */   
/*      */   public static String toString(double[] a) {
/* 1307 */     return toString(a, null);
/*      */   }
/*      */   
/*      */   public static String toString(double[] a, NumberFormat nf) {
/* 1311 */     if (a == null) return null;
/* 1312 */     if (a.length == 0) return "[]";
/* 1313 */     StringBuffer b = new StringBuffer();
/* 1314 */     b.append("[");
/* 1315 */     for (int i = 0; i < a.length - 1; i++) { String s;
/*      */       String s;
/* 1317 */       if (nf == null) {
/* 1318 */         s = String.valueOf(a[i]);
/*      */       } else {
/* 1320 */         s = nf.format(a[i]);
/*      */       }
/* 1322 */       b.append(s);
/* 1323 */       b.append(", "); }
/*      */     String s;
/*      */     String s;
/* 1326 */     if (nf == null) {
/* 1327 */       s = String.valueOf(a[(a.length - 1)]);
/*      */     } else {
/* 1329 */       s = nf.format(a[(a.length - 1)]);
/*      */     }
/* 1331 */     b.append(s);
/* 1332 */     b.append(']');
/* 1333 */     return b.toString();
/*      */   }
/*      */   
/*      */   public static String toString(float[] a) {
/* 1337 */     return toString(a, null);
/*      */   }
/*      */   
/*      */   public static String toString(float[] a, NumberFormat nf) {
/* 1341 */     if (a == null) return null;
/* 1342 */     if (a.length == 0) return "[]";
/* 1343 */     StringBuffer b = new StringBuffer();
/* 1344 */     b.append("[");
/* 1345 */     for (int i = 0; i < a.length - 1; i++) { String s;
/*      */       String s;
/* 1347 */       if (nf == null) {
/* 1348 */         s = String.valueOf(a[i]);
/*      */       } else {
/* 1350 */         s = nf.format(a[i]);
/*      */       }
/* 1352 */       b.append(s);
/* 1353 */       b.append(", "); }
/*      */     String s;
/*      */     String s;
/* 1356 */     if (nf == null) {
/* 1357 */       s = String.valueOf(a[(a.length - 1)]);
/*      */     } else {
/* 1359 */       s = nf.format(a[(a.length - 1)]);
/*      */     }
/* 1361 */     b.append(s);
/* 1362 */     b.append(']');
/* 1363 */     return b.toString();
/*      */   }
/*      */   
/*      */   public static String toString(int[] a) {
/* 1367 */     return toString(a, null);
/*      */   }
/*      */   
/*      */   public static String toString(int[] a, NumberFormat nf) {
/* 1371 */     if (a == null) return null;
/* 1372 */     if (a.length == 0) return "[]";
/* 1373 */     StringBuffer b = new StringBuffer();
/* 1374 */     b.append("[");
/* 1375 */     for (int i = 0; i < a.length - 1; i++) { String s;
/*      */       String s;
/* 1377 */       if (nf == null) {
/* 1378 */         s = String.valueOf(a[i]);
/*      */       } else {
/* 1380 */         s = nf.format(a[i]);
/*      */       }
/* 1382 */       b.append(s);
/* 1383 */       b.append(", "); }
/*      */     String s;
/*      */     String s;
/* 1386 */     if (nf == null) {
/* 1387 */       s = String.valueOf(a[(a.length - 1)]);
/*      */     } else {
/* 1389 */       s = nf.format(a[(a.length - 1)]);
/*      */     }
/* 1391 */     b.append(s);
/* 1392 */     b.append(']');
/* 1393 */     return b.toString();
/*      */   }
/*      */   
/*      */   public static String toString(byte[] a) {
/* 1397 */     return toString(a, null);
/*      */   }
/*      */   
/*      */   public static String toString(byte[] a, NumberFormat nf) {
/* 1401 */     if (a == null) return null;
/* 1402 */     if (a.length == 0) return "[]";
/* 1403 */     StringBuffer b = new StringBuffer();
/* 1404 */     b.append("[");
/* 1405 */     for (int i = 0; i < a.length - 1; i++) { String s;
/*      */       String s;
/* 1407 */       if (nf == null) {
/* 1408 */         s = String.valueOf(a[i]);
/*      */       } else {
/* 1410 */         s = nf.format(a[i]);
/*      */       }
/* 1412 */       b.append(s);
/* 1413 */       b.append(", "); }
/*      */     String s;
/*      */     String s;
/* 1416 */     if (nf == null) {
/* 1417 */       s = String.valueOf(a[(a.length - 1)]);
/*      */     } else {
/* 1419 */       s = nf.format(a[(a.length - 1)]);
/*      */     }
/* 1421 */     b.append(s);
/* 1422 */     b.append(']');
/* 1423 */     return b.toString();
/*      */   }
/*      */   
/*      */   public static String toString(int[][] counts) {
/* 1427 */     return toString(counts, null, null, 10, 10, NumberFormat.getInstance(), false);
/*      */   }
/*      */   
/*      */   public static String toString(int[][] counts, Object[] rowLabels, Object[] colLabels, int labelSize, int cellSize, NumberFormat nf, boolean printTotals)
/*      */   {
/* 1432 */     if ((counts.length == 0) || (counts[0].length == 0)) return "";
/* 1433 */     int[] rowTotals = new int[counts.length];
/* 1434 */     int[] colTotals = new int[counts[0].length];
/* 1435 */     int total = 0;
/* 1436 */     for (int i = 0; i < counts.length; i++) {
/* 1437 */       for (int j = 0; j < counts[i].length; j++) {
/* 1438 */         rowTotals[i] += counts[i][j];
/* 1439 */         colTotals[j] += counts[i][j];
/* 1440 */         total += counts[i][j];
/*      */       }
/*      */     }
/* 1443 */     StringBuffer result = new StringBuffer();
/*      */     
/* 1445 */     if (colLabels != null) {
/* 1446 */       result.append(StringUtils.padLeft("", labelSize));
/* 1447 */       for (int j = 0; j < counts[0].length; j++) {
/* 1448 */         String s = colLabels[j] == null ? "null" : colLabels[j].toString();
/* 1449 */         if (s.length() > cellSize - 1) {
/* 1450 */           s = s.substring(0, cellSize - 1);
/*      */         }
/* 1452 */         s = StringUtils.padLeft(s, cellSize);
/* 1453 */         result.append(s);
/*      */       }
/* 1455 */       if (printTotals) {
/* 1456 */         result.append(StringUtils.padLeftOrTrim("Total", cellSize));
/*      */       }
/* 1458 */       result.append("\n");
/*      */     }
/* 1460 */     for (int i = 0; i < counts.length; i++)
/*      */     {
/* 1462 */       if (rowLabels != null) {
/* 1463 */         String s = rowLabels[i] == null ? "null" : rowLabels[i].toString();
/* 1464 */         s = StringUtils.padOrTrim(s, labelSize);
/* 1465 */         result.append(s);
/*      */       }
/*      */       
/* 1468 */       for (int j = 0; j < counts[i].length; j++) {
/* 1469 */         result.append(StringUtils.padLeft(nf.format(counts[i][j]), cellSize));
/*      */       }
/*      */       
/* 1472 */       if (printTotals) {
/* 1473 */         result.append(StringUtils.padLeft(nf.format(rowTotals[i]), cellSize));
/*      */       }
/* 1475 */       result.append("\n");
/*      */     }
/*      */     
/* 1478 */     if (printTotals) {
/* 1479 */       result.append(StringUtils.pad("Total", labelSize));
/* 1480 */       for (int j = 0; j < colTotals.length; j++) {
/* 1481 */         result.append(StringUtils.padLeft(nf.format(colTotals[j]), cellSize));
/*      */       }
/* 1483 */       result.append(StringUtils.padLeft(nf.format(total), cellSize));
/*      */     }
/* 1485 */     return result.toString();
/*      */   }
/*      */   
/*      */   public static String toString(double[][] counts)
/*      */   {
/* 1490 */     return toString(counts, 10, null, null, NumberFormat.getInstance(), false);
/*      */   }
/*      */   
/*      */   public static String toString(double[][] counts, int cellSize, Object[] rowLabels, Object[] colLabels, NumberFormat nf, boolean printTotals) {
/* 1494 */     if (counts == null) { return null;
/*      */     }
/* 1496 */     double[] rowTotals = new double[counts.length];
/* 1497 */     double[] colTotals = new double[counts[0].length];
/* 1498 */     double total = 0.0D;
/* 1499 */     for (int i = 0; i < counts.length; i++) {
/* 1500 */       for (int j = 0; j < counts[i].length; j++) {
/* 1501 */         rowTotals[i] += counts[i][j];
/* 1502 */         colTotals[j] += counts[i][j];
/* 1503 */         total += counts[i][j];
/*      */       }
/*      */     }
/* 1506 */     StringBuffer result = new StringBuffer();
/*      */     
/* 1508 */     if (colLabels != null) {
/* 1509 */       result.append(StringUtils.padLeft("", cellSize));
/* 1510 */       for (int j = 0; j < counts[0].length; j++) {
/* 1511 */         String s = colLabels[j].toString();
/* 1512 */         if (s.length() > cellSize - 1) {
/* 1513 */           s = s.substring(0, cellSize - 1);
/*      */         }
/* 1515 */         s = StringUtils.padLeft(s, cellSize);
/* 1516 */         result.append(s);
/*      */       }
/* 1518 */       if (printTotals) {
/* 1519 */         result.append(StringUtils.padLeftOrTrim("Total", cellSize));
/*      */       }
/* 1521 */       result.append("\n");
/*      */     }
/* 1523 */     for (int i = 0; i < counts.length; i++)
/*      */     {
/* 1525 */       if (rowLabels != null) {
/* 1526 */         String s = rowLabels[i].toString();
/* 1527 */         s = StringUtils.padOrTrim(s, cellSize);
/* 1528 */         result.append(s);
/*      */       }
/*      */       
/* 1531 */       for (int j = 0; j < counts[i].length; j++) {
/* 1532 */         result.append(StringUtils.padLeft(nf.format(counts[i][j]), cellSize));
/*      */       }
/*      */       
/* 1535 */       if (printTotals) {
/* 1536 */         result.append(StringUtils.padLeft(nf.format(rowTotals[i]), cellSize));
/*      */       }
/* 1538 */       result.append("\n");
/*      */     }
/*      */     
/* 1541 */     if (printTotals) {
/* 1542 */       result.append(StringUtils.pad("Total", cellSize));
/* 1543 */       for (int j = 0; j < colTotals.length; j++) {
/* 1544 */         result.append(StringUtils.padLeft(nf.format(colTotals[j]), cellSize));
/*      */       }
/* 1546 */       result.append(StringUtils.padLeft(nf.format(total), cellSize));
/*      */     }
/* 1548 */     return result.toString();
/*      */   }
/*      */   
/*      */   public static String toString(float[][] counts) {
/* 1552 */     return toString(counts, 10, null, null, NumberFormat.getIntegerInstance(), false);
/*      */   }
/*      */   
/*      */   public static String toString(float[][] counts, int cellSize, Object[] rowLabels, Object[] colLabels, NumberFormat nf, boolean printTotals)
/*      */   {
/* 1557 */     double[] rowTotals = new double[counts.length];
/* 1558 */     double[] colTotals = new double[counts[0].length];
/* 1559 */     double total = 0.0D;
/* 1560 */     for (int i = 0; i < counts.length; i++) {
/* 1561 */       for (int j = 0; j < counts[i].length; j++) {
/* 1562 */         rowTotals[i] += counts[i][j];
/* 1563 */         colTotals[j] += counts[i][j];
/* 1564 */         total += counts[i][j];
/*      */       }
/*      */     }
/* 1567 */     StringBuffer result = new StringBuffer();
/*      */     
/* 1569 */     if (colLabels != null) {
/* 1570 */       result.append(StringUtils.padLeft("", cellSize));
/* 1571 */       for (int j = 0; j < counts[0].length; j++) {
/* 1572 */         String s = colLabels[j].toString();
/* 1573 */         s = StringUtils.padLeftOrTrim(s, cellSize);
/* 1574 */         result.append(s);
/*      */       }
/* 1576 */       if (printTotals) {
/* 1577 */         result.append(StringUtils.padLeftOrTrim("Total", cellSize));
/*      */       }
/* 1579 */       result.append("\n");
/*      */     }
/* 1581 */     for (int i = 0; i < counts.length; i++)
/*      */     {
/* 1583 */       if (rowLabels != null) {
/* 1584 */         String s = rowLabels[i].toString();
/* 1585 */         s = StringUtils.pad(s, cellSize);
/* 1586 */         result.append(s);
/*      */       }
/*      */       
/* 1589 */       for (int j = 0; j < counts[i].length; j++) {
/* 1590 */         result.append(StringUtils.padLeft(nf.format(counts[i][j]), cellSize));
/*      */       }
/*      */       
/* 1593 */       if (printTotals) {
/* 1594 */         result.append(StringUtils.padLeft(nf.format(rowTotals[i]), cellSize));
/*      */       }
/* 1596 */       result.append("\n");
/*      */     }
/*      */     
/* 1599 */     if (printTotals) {
/* 1600 */       result.append(StringUtils.pad("Total", cellSize));
/* 1601 */       for (int j = 0; j < colTotals.length; j++) {
/* 1602 */         result.append(StringUtils.padLeft(nf.format(colTotals[j]), cellSize));
/*      */       }
/* 1604 */       result.append(StringUtils.padLeft(nf.format(total), cellSize));
/*      */     }
/* 1606 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1614 */     Random random = new Random();
/* 1615 */     int length = 100;
/* 1616 */     double[] A = new double[length];
/* 1617 */     double[] B = new double[length];
/* 1618 */     double aAvg = 70.0D;
/* 1619 */     double bAvg = 70.5D;
/* 1620 */     for (int i = 0; i < length; i++) {
/* 1621 */       A[i] = (aAvg + random.nextGaussian());
/* 1622 */       B[i] = (bAvg + random.nextGaussian());
/*      */     }
/* 1624 */     System.out.println("A has length " + A.length + " and mean " + mean(A));
/* 1625 */     System.out.println("B has length " + B.length + " and mean " + mean(B));
/* 1626 */     for (int t = 0; t < 10; t++) {
/* 1627 */       System.out.println("p-value: " + sigLevelByApproxRand(A, B));
/*      */     }
/*      */   }
/*      */   
/*      */   public static int[][] deepCopy(int[][] counts) {
/* 1632 */     int[][] result = new int[counts.length][];
/* 1633 */     for (int i = 0; i < counts.length; i++) {
/* 1634 */       result[i] = new int[counts[i].length];
/* 1635 */       for (int j = 0; j < counts[i].length; j++) {
/* 1636 */         result[i][j] = counts[i][j];
/*      */       }
/*      */     }
/* 1639 */     return result;
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\math\ArrayMath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */