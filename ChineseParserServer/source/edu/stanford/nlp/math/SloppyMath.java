/*     */ package edu.stanford.nlp.math;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*     */ public final class SloppyMath
/*     */ {
/*     */   static final double LOGTOLERANCE = 30.0D;
/*     */   static final float LOGTOLERANCE_F = 20.0F;
/*     */   
/*     */   public static double round(double x, int precision)
/*     */   {
/*  22 */     double power = Math.pow(10.0D, precision);
/*  23 */     return Math.round(x * power) / power;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int max(int a, int b, int c)
/*     */   {
/*  32 */     int ma = a;
/*  33 */     if (b > ma) {
/*  34 */       ma = b;
/*     */     }
/*  36 */     if (c > ma) {
/*  37 */       ma = c;
/*     */     }
/*  39 */     return ma;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int min(int a, int b, int c)
/*     */   {
/*  49 */     int mi = a;
/*  50 */     if (b < mi) {
/*  51 */       mi = b;
/*     */     }
/*  53 */     if (c < mi) {
/*  54 */       mi = c;
/*     */     }
/*  56 */     return mi;
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
/*     */   public static float max(float a, float b)
/*     */   {
/*  73 */     return a >= b ? a : b;
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
/*     */   public static double max(double a, double b)
/*     */   {
/*  89 */     return a >= b ? a : b;
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
/*     */   public static float min(float a, float b)
/*     */   {
/* 105 */     return a <= b ? a : b;
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
/*     */   public static double min(double a, double b)
/*     */   {
/* 121 */     return a <= b ? a : b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isDangerous(double d)
/*     */   {
/* 130 */     return (Double.isInfinite(d)) || (Double.isNaN(d)) || (d == 0.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isVeryDangerous(double d)
/*     */   {
/* 139 */     return (Double.isInfinite(d)) || (Double.isNaN(d));
/*     */   }
/*     */   
/*     */   public static boolean isCloseTo(double a, double b) {
/* 143 */     if (a > b) {
/* 144 */       return a - b < 1.0E-4D;
/*     */     }
/* 146 */     return b - a < 1.0E-4D;
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
/*     */   public static double gamma(double n)
/*     */   {
/* 160 */     return Math.sqrt(6.283185307179586D / n) * Math.pow(n / 2.718281828459045D * Math.sqrt(n * Math.sinh(1.0D / n + 0.0D * Math.pow(n, 6.0D))), n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float logAdd(float lx, float ly)
/*     */   {
/*     */     float negDiff;
/*     */     
/*     */ 
/*     */ 
/*     */     float max;
/*     */     
/*     */ 
/*     */     float negDiff;
/*     */     
/*     */ 
/* 178 */     if (lx > ly) {
/* 179 */       float max = lx;
/* 180 */       negDiff = ly - lx;
/*     */     } else {
/* 182 */       max = ly;
/* 183 */       negDiff = lx - ly;
/*     */     }
/* 185 */     if (max == Double.NEGATIVE_INFINITY)
/* 186 */       return max;
/* 187 */     if (negDiff < -20.0F) {
/* 188 */       return max;
/*     */     }
/* 190 */     return max + (float)Math.log(1.0D + Math.exp(negDiff));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double logAdd(double lx, double ly)
/*     */   {
/*     */     double negDiff;
/*     */     
/*     */ 
/*     */ 
/*     */     double max;
/*     */     
/*     */ 
/*     */ 
/*     */     double negDiff;
/*     */     
/*     */ 
/*     */ 
/* 210 */     if (lx > ly) {
/* 211 */       double max = lx;
/* 212 */       negDiff = ly - lx;
/*     */     } else {
/* 214 */       max = ly;
/* 215 */       negDiff = lx - ly;
/*     */     }
/* 217 */     if (max == Double.NEGATIVE_INFINITY)
/* 218 */       return max;
/* 219 */     if (negDiff < -30.0D) {
/* 220 */       return max;
/*     */     }
/* 222 */     return max + Math.log(1.0D + Math.exp(negDiff));
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
/*     */   public static int nChooseK(int n, int k)
/*     */   {
/* 235 */     k = Math.min(k, n - k);
/* 236 */     if (k == 0) {
/* 237 */       return 1;
/*     */     }
/* 239 */     int accum = n;
/* 240 */     for (int i = 1; i < k; i++) {
/* 241 */       accum *= (n - i);
/* 242 */       accum /= i;
/*     */     }
/* 244 */     return accum / k;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int intPow(int b, int e)
/*     */   {
/* 256 */     if (e == 0) {
/* 257 */       return 1;
/*     */     }
/* 259 */     int result = 1;
/* 260 */     int currPow = b;
/*     */     do {
/* 262 */       if ((e & 0x1) == 1) result *= currPow;
/* 263 */       currPow *= currPow;
/* 264 */       e >>= 1;
/* 265 */     } while (e > 0);
/* 266 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static float intPow(float b, int e)
/*     */   {
/* 278 */     if (e == 0) {
/* 279 */       return 1.0F;
/*     */     }
/* 281 */     float result = 1.0F;
/* 282 */     float currPow = b;
/*     */     do {
/* 284 */       if ((e & 0x1) == 1) result *= currPow;
/* 285 */       currPow *= currPow;
/* 286 */       e >>= 1;
/* 287 */     } while (e > 0);
/* 288 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double intPow(double b, int e)
/*     */   {
/* 300 */     if (e == 0) {
/* 301 */       return 1.0D;
/*     */     }
/* 303 */     float result = 1.0F;
/* 304 */     double currPow = b;
/*     */     do {
/* 306 */       if ((e & 0x1) == 1) result = (float)(result * currPow);
/* 307 */       currPow *= currPow;
/* 308 */       e >>= 1;
/* 309 */     } while (e > 0);
/* 310 */     return result;
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
/*     */   public static double hypergeometric(int k, int n, int r, int m)
/*     */   {
/* 328 */     if ((k < 0) || (r > n) || (m > n) || (n <= 0) || (m < 0) || (r < 0)) {
/* 329 */       throw new IllegalArgumentException("Invalid hypergeometric");
/*     */     }
/*     */     
/*     */ 
/* 333 */     if (m > n / 2) {
/* 334 */       m = n - m;
/* 335 */       k = r - k;
/*     */     }
/* 337 */     if (r > n / 2) {
/* 338 */       r = n - r;
/* 339 */       k = m - k;
/*     */     }
/* 341 */     if (m > r) {
/* 342 */       int temp = m;
/* 343 */       m = r;
/* 344 */       r = temp;
/*     */     }
/*     */     
/*     */ 
/* 348 */     if ((k < m + r - n) || (k > m)) {
/* 349 */       return 0.0D;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 356 */     if (r == n) {
/* 357 */       if (k == m) {
/* 358 */         return 1.0D;
/*     */       }
/* 360 */       return 0.0D;
/*     */     }
/* 362 */     if (r == n - 1) {
/* 363 */       if (k == m)
/* 364 */         return (n - m) / n;
/* 365 */       if (k == m - 1) {
/* 366 */         return m / n;
/*     */       }
/* 368 */       return 0.0D;
/*     */     }
/* 370 */     if (m == 1) {
/* 371 */       if (k == 0)
/* 372 */         return (n - r) / n;
/* 373 */       if (k == 1) {
/* 374 */         return r / n;
/*     */       }
/* 376 */       return 0.0D;
/*     */     }
/* 378 */     if (m == 0) {
/* 379 */       if (k == 0) {
/* 380 */         return 1.0D;
/*     */       }
/* 382 */       return 0.0D;
/*     */     }
/* 384 */     if (k == 0) {
/* 385 */       double ans = 1.0D;
/* 386 */       for (int m0 = 0; m0 < m; m0++) {
/* 387 */         ans *= (n - r - m0);
/* 388 */         ans /= (n - m0);
/*     */       }
/* 390 */       return ans;
/*     */     }
/*     */     
/* 393 */     double ans = 1.0D;
/*     */     
/*     */ 
/*     */ 
/* 397 */     int nr = n - r; for (int n0 = n; nr > n - r - (m - k); n0--)
/*     */     {
/* 399 */       ans *= nr;
/*     */       
/* 401 */       ans /= n0;nr--;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 404 */     for (int k0 = 0; k0 < k; k0++) {
/* 405 */       ans *= (m - k0);
/*     */       
/* 407 */       ans /= (n - (m - k0) + 1);
/*     */       
/* 409 */       ans *= (r - k0);
/*     */       
/* 411 */       ans /= (k0 + 1);
/*     */     }
/*     */     
/* 414 */     return ans;
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
/*     */   public static double exactBinomial(int k, int n, double p)
/*     */   {
/* 427 */     double total = 0.0D;
/* 428 */     for (int m = k; m <= n; m++) {
/* 429 */       double nChooseM = 1.0D;
/* 430 */       for (int r = 1; r <= m; r++) {
/* 431 */         nChooseM *= (n - r + 1);
/* 432 */         nChooseM /= r;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 437 */       total += nChooseM * Math.pow(p, m) * Math.pow(1.0D - p, n - m);
/*     */     }
/* 439 */     return total;
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
/*     */   public static double oneTailedFishersExact(int k, int n, int r, int m)
/*     */   {
/* 458 */     if ((k < 0) || (k < m + r - n) || (k > r) || (k > m) || (r > n) || (m > n)) {
/* 459 */       throw new IllegalArgumentException("Invalid Fisher's exact: k=" + k + " n=" + n + " r=" + r + " m=" + m + " k<0=" + (k < 0) + " k<(m+r)-n=" + (k < m + r - n) + " k>r=" + (k > r) + " k>m=" + (k > m) + " r>n=" + (r > n) + "m>n=" + (m > n));
/*     */     }
/*     */     
/* 462 */     if (m > n / 2) {
/* 463 */       m = n - m;
/* 464 */       k = r - k;
/*     */     }
/* 466 */     if (r > n / 2) {
/* 467 */       r = n - r;
/* 468 */       k = m - k;
/*     */     }
/* 470 */     if (m > r) {
/* 471 */       int temp = m;
/* 472 */       m = r;
/* 473 */       r = temp;
/*     */     }
/*     */     
/*     */ 
/* 477 */     double total = 0.0D;
/* 478 */     if (k > m / 2)
/*     */     {
/* 480 */       for (int k0 = k; k0 <= m; k0++)
/*     */       {
/*     */ 
/* 483 */         total += hypergeometric(k0, n, r, m);
/*     */       }
/*     */     }
/*     */     else {
/* 487 */       int min = Math.max(0, m + r - n);
/* 488 */       for (int k0 = min; k0 < k; k0++)
/*     */       {
/*     */ 
/* 491 */         total += hypergeometric(k0, n, r, m);
/*     */       }
/* 493 */       total = 1.0D - total;
/*     */     }
/* 495 */     return total;
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
/*     */   public static double chiSquare2by2(int k, int n, int r, int m)
/*     */   {
/* 510 */     int[][] cg = { { k, r - k }, { m - k, n - (k + (r - k) + (m - k)) } };
/* 511 */     int[] cgr = { r, n - r };
/* 512 */     int[] cgc = { m, n - m };
/* 513 */     double total = 0.0D;
/* 514 */     for (int i = 0; i < 2; i++) {
/* 515 */       for (int j = 0; j < 2; j++) {
/* 516 */         double exp = cgr[i] * cgc[j] / n;
/* 517 */         total += (cg[i][j] - exp) * (cg[i][j] - exp) / exp;
/*     */       }
/*     */     }
/* 520 */     return total;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double sigmoid(double x)
/*     */   {
/* 532 */     if (x < 0.0D) {
/* 533 */       double num = Math.exp(x);
/* 534 */       return num / (1.0D + num);
/*     */     }
/*     */     
/* 537 */     double den = 1.0D + Math.exp(-x);
/* 538 */     return 1.0D / den;
/*     */   }
/*     */   
/*     */   public static double poisson(int x, double lambda)
/*     */   {
/* 543 */     if ((x < 0) || (lambda <= 0.0D)) throw new RuntimeException("Bad arguments: " + x + " and " + lambda);
/* 544 */     double p = Math.exp(-lambda) * Math.pow(lambda, x) / factorial(x);
/* 545 */     if ((Double.isInfinite(p)) || (p <= 0.0D)) throw new RuntimeException(Math.exp(-lambda) + " " + Math.pow(lambda, x) + " " + factorial(x));
/* 546 */     return p;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double factorial(int x)
/*     */   {
/* 555 */     double result = 1.0D;
/* 556 */     for (int i = x; i > 1; i--) {
/* 557 */       result *= i;
/*     */     }
/* 559 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 571 */     if (args.length == 0) {
/* 572 */       System.err.println("Usage: java edu.stanford.nlp.math.SloppyMath [-logAdd|-fishers k n r m|-binomial r n p");
/* 573 */     } else if (args[0].equals("-logAdd")) {
/* 574 */       System.out.println("Log adds of neg infinity numbers, etc.");
/* 575 */       System.out.println("(logs) -Inf + -Inf = " + logAdd(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
/* 576 */       System.out.println("(logs) -Inf + -7 = " + logAdd(Double.NEGATIVE_INFINITY, -7.0D));
/* 577 */       System.out.println("(logs) -7 + -Inf = " + logAdd(-7.0D, Double.NEGATIVE_INFINITY));
/* 578 */       System.out.println("(logs) -50 + -7 = " + logAdd(-50.0D, -7.0D));
/* 579 */       System.out.println("(logs) -11 + -7 = " + logAdd(-11.0D, -7.0D));
/* 580 */       System.out.println("(logs) -7 + -11 = " + logAdd(-7.0D, -11.0D));
/* 581 */       System.out.println("real 1/2 + 1/2 = " + logAdd(Math.log(0.5D), Math.log(0.5D)));
/* 582 */     } else if (args[0].equals("-fishers")) {
/* 583 */       int k = Integer.parseInt(args[1]);
/* 584 */       int n = Integer.parseInt(args[2]);
/* 585 */       int r = Integer.parseInt(args[3]);
/* 586 */       int m = Integer.parseInt(args[4]);
/* 587 */       double ans = hypergeometric(k, n, r, m);
/* 588 */       System.out.println("hypg(" + k + "; " + n + ", " + r + ", " + m + ") = " + ans);
/* 589 */       ans = oneTailedFishersExact(k, n, r, m);
/* 590 */       System.out.println("1-tailed Fisher's exact(" + k + "; " + n + ", " + r + ", " + m + ") = " + ans);
/* 591 */       double ansChi = chiSquare2by2(k, n, r, m);
/* 592 */       System.out.println("chiSquare(" + k + "; " + n + ", " + r + ", " + m + ") = " + ansChi);
/*     */       
/* 594 */       System.out.println("Swapping arguments should give same hypg:");
/* 595 */       ans = hypergeometric(k, n, r, m);
/* 596 */       System.out.println("hypg(" + k + "; " + n + ", " + m + ", " + r + ") = " + ans);
/* 597 */       int othrow = n - m;
/* 598 */       int othcol = n - r;
/* 599 */       int cell12 = m - k;
/* 600 */       int cell21 = r - k;
/* 601 */       int cell22 = othrow - (r - k);
/* 602 */       ans = hypergeometric(cell12, n, othcol, m);
/* 603 */       System.out.println("hypg(" + cell12 + "; " + n + ", " + othcol + ", " + m + ") = " + ans);
/* 604 */       ans = hypergeometric(cell21, n, r, othrow);
/* 605 */       System.out.println("hypg(" + cell21 + "; " + n + ", " + r + ", " + othrow + ") = " + ans);
/* 606 */       ans = hypergeometric(cell22, n, othcol, othrow);
/* 607 */       System.out.println("hypg(" + cell22 + "; " + n + ", " + othcol + ", " + othrow + ") = " + ans);
/* 608 */     } else if (args[0].equals("-binomial")) {
/* 609 */       int k = Integer.parseInt(args[1]);
/* 610 */       int n = Integer.parseInt(args[2]);
/* 611 */       double p = Double.parseDouble(args[3]);
/* 612 */       double ans = exactBinomial(k, n, p);
/* 613 */       System.out.println("Binomial p(X >= " + k + "; " + n + ", " + p + ") = " + ans);
/*     */     } else {
/* 615 */       System.err.println("Unknown option: " + args[0]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\math\SloppyMath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */