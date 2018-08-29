/*     */ package edu.stanford.nlp.stats;
/*     */ 
/*     */ import edu.stanford.nlp.util.BinaryHeapPriorityQueue;
/*     */ import edu.stanford.nlp.util.FixedPrioritiesPriorityQueue;
/*     */ import edu.stanford.nlp.util.Index;
/*     */ import edu.stanford.nlp.util.MapFactory;
/*     */ import edu.stanford.nlp.util.MutableDouble;
/*     */ import edu.stanford.nlp.util.PriorityQueue;
/*     */ import edu.stanford.nlp.util.Sets;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Counters
/*     */ {
/*     */   public static <E> Counter<E> union(GenericCounter<E> c1, GenericCounter<E> c2)
/*     */   {
/*  53 */     Counter<E> result = new Counter();
/*  54 */     result.addAll(c1);
/*  55 */     result.addAll(c2);
/*  56 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Counter<E> intersection(GenericCounter<E> c1, GenericCounter<E> c2)
/*     */   {
/*  68 */     Counter<E> result = new Counter();
/*     */     
/*  70 */     for (E key : Sets.union(c1.keySet(), c2.keySet())) {
/*  71 */       double count1 = c1.getCount(key);
/*  72 */       double count2 = c2.getCount(key);
/*  73 */       double minCount = count1 < count2 ? count1 : count2;
/*  74 */       if (minCount > 0.0D) {
/*  75 */         result.setCount(key, minCount);
/*     */       }
/*     */     }
/*  78 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> double jaccardCoefficient(GenericCounter<E> c1, GenericCounter<E> c2)
/*     */   {
/*  90 */     double minCount = 0.0D;double maxCount = 0.0D;
/*  91 */     for (E key : Sets.union(c1.keySet(), c2.keySet())) {
/*  92 */       double count1 = c1.getCount(key);
/*  93 */       double count2 = c2.getCount(key);
/*  94 */       minCount += (count1 < count2 ? count1 : count2);
/*  95 */       maxCount += (count1 > count2 ? count1 : count2);
/*     */     }
/*  97 */     return minCount / maxCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Counter<E> product(GenericCounter<E> c1, GenericCounter<E> c2)
/*     */   {
/* 108 */     Counter<E> result = new Counter();
/* 109 */     for (E key : Sets.intersection(c1.keySet(), c2.keySet())) {
/* 110 */       result.setCount(key, c1.getCount(key) * c2.getCount(key));
/*     */     }
/* 112 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> double dotProduct(GenericCounter<E> c1, GenericCounter<E> c2)
/*     */   {
/* 123 */     double dotProd = 0.0D;
/* 124 */     for (E key : c1.keySet()) {
/* 125 */       double count1 = c1.getCount(key);
/* 126 */       if ((Double.isNaN(count1)) || (Double.isInfinite(count1))) throw new RuntimeException();
/* 127 */       if (count1 != 0.0D) {
/* 128 */         double count2 = c2.getCount(key);
/* 129 */         if ((Double.isNaN(count2)) || (Double.isInfinite(count2))) {
/* 130 */           System.err.println("bad value: " + count2);
/* 131 */           throw new RuntimeException();
/*     */         }
/* 133 */         if (count2 != 0.0D)
/*     */         {
/* 135 */           dotProd += count1 * count2;
/*     */         }
/*     */       }
/*     */     }
/* 139 */     return dotProd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Counter<E> absoluteDifference(GenericCounter<E> c1, GenericCounter<E> c2)
/*     */   {
/* 151 */     Counter<E> result = new Counter();
/*     */     
/* 153 */     for (E key : Sets.union(c1.keySet(), c2.keySet())) {
/* 154 */       double newCount = Math.abs(c1.getCount(key) - c2.getCount(key));
/* 155 */       if (newCount > 0.0D) {
/* 156 */         result.setCount(key, newCount);
/*     */       }
/*     */     }
/* 159 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Counter<E> division(GenericCounter<E> c1, GenericCounter<E> c2)
/*     */   {
/* 171 */     Counter<E> result = new Counter();
/* 172 */     for (E key : Sets.union(c1.keySet(), c2.keySet())) {
/* 173 */       result.setCount(key, c1.getCount(key) / c2.getCount(key));
/*     */     }
/* 175 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> double entropy(GenericCounter<E> c)
/*     */   {
/* 186 */     double entropy = 0.0D;
/* 187 */     double total = c.totalDoubleCount();
/* 188 */     for (E key : c.keySet()) {
/* 189 */       double count = c.getCount(key);
/* 190 */       if (count != 0.0D)
/*     */       {
/*     */ 
/* 193 */         count /= total;
/* 194 */         entropy -= count * (Math.log(count) / Math.log(2.0D));
/*     */       } }
/* 196 */     return entropy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> double crossEntropy(GenericCounter<E> from, GenericCounter<E> to)
/*     */   {
/* 207 */     double tot2 = to.totalDoubleCount();
/* 208 */     double result = 0.0D;
/* 209 */     double log2 = Math.log(2.0D);
/* 210 */     for (E key : from.keySet()) {
/* 211 */       double count1 = from.getCount(key);
/* 212 */       if (count1 != 0.0D)
/*     */       {
/*     */ 
/* 215 */         double count2 = to.getCount(key);
/* 216 */         double logFract = Math.log(count2 / tot2);
/* 217 */         if (logFract == Double.NEGATIVE_INFINITY) {
/* 218 */           return Double.NEGATIVE_INFINITY;
/*     */         }
/* 220 */         result += count1 * (logFract / log2);
/*     */       } }
/* 222 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> double crossEntropy(GenericCounter<E> from, Counter<E> to)
/*     */   {
/* 232 */     double result = 0.0D;
/* 233 */     double log2 = Math.log(2.0D);
/* 234 */     for (E key : from.keySet()) {
/* 235 */       double count1 = from.getCount(key);
/* 236 */       if (count1 != 0.0D)
/*     */       {
/*     */ 
/* 239 */         double prob = to.getCount(key);
/* 240 */         double logFract = Math.log(prob);
/* 241 */         if (logFract == Double.NEGATIVE_INFINITY) {
/* 242 */           return Double.NEGATIVE_INFINITY;
/*     */         }
/* 244 */         result += count1 * (logFract / log2);
/*     */       } }
/* 246 */     return result;
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
/*     */   public static <E> double klDivergence(GenericCounter<E> from, GenericCounter<E> to)
/*     */   {
/* 262 */     double result = 0.0D;
/* 263 */     double tot = from.totalDoubleCount();
/* 264 */     double tot2 = to.totalDoubleCount();
/*     */     
/* 266 */     double log2 = Math.log(2.0D);
/* 267 */     for (E key : from.keySet()) {
/* 268 */       double num = from.getCount(key);
/* 269 */       if (num != 0.0D)
/*     */       {
/*     */ 
/* 272 */         num /= tot;
/* 273 */         double num2 = to.getCount(key);
/* 274 */         num2 /= tot2;
/*     */         
/* 276 */         double logFract = Math.log(num / num2);
/* 277 */         if (logFract == Double.NEGATIVE_INFINITY) {
/* 278 */           return Double.NEGATIVE_INFINITY;
/*     */         }
/* 280 */         result += num * (logFract / log2);
/*     */       } }
/* 282 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> double jensenShannonDivergence(GenericCounter<E> c1, GenericCounter<E> c2)
/*     */   {
/* 294 */     Counter<E> average = average(c1, c2);
/* 295 */     double kl1 = klDivergence(c1, average);
/* 296 */     double kl2 = klDivergence(c2, average);
/* 297 */     return (kl1 + kl2) / 2.0D;
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
/*     */   public static <E> double skewDivergence(GenericCounter<E> c1, GenericCounter<E> c2, double skew)
/*     */   {
/* 311 */     Counter<E> average = linearCombination(c2, skew, c1, 1.0D - skew);
/* 312 */     return klDivergence(c1, average);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Counter<E> L2Normalize(GenericCounter<E> c)
/*     */   {
/* 321 */     double total = 0.0D;
/* 322 */     for (E key : c.keySet()) {
/* 323 */       double count2 = c.getCount(key);
/* 324 */       if (count2 != 0.0D) {
/* 325 */         total += count2 * count2;
/*     */       }
/*     */     }
/* 328 */     return scale(c, 1.0D / Math.sqrt(total));
/*     */   }
/*     */   
/*     */   public static <E> double cosine(GenericCounter<E> c1, GenericCounter<E> c2) {
/* 332 */     double dotProd = 0.0D;
/* 333 */     double lsq1 = 0.0D;
/* 334 */     double lsq2 = 0.0D;
/* 335 */     for (E key : c1.keySet()) {
/* 336 */       double count1 = c1.getCount(key);
/* 337 */       if (count1 != 0.0D) {
/* 338 */         lsq1 += count1 * count1;
/* 339 */         double count2 = c2.getCount(key);
/* 340 */         if (count2 != 0.0D)
/*     */         {
/* 342 */           dotProd += count1 * count2;
/*     */         }
/*     */       }
/*     */     }
/* 346 */     for (E key : c2.keySet()) {
/* 347 */       double count2 = c2.getCount(key);
/* 348 */       if (count2 != 0.0D) {
/* 349 */         lsq2 += count2 * count2;
/*     */       }
/*     */     }
/* 352 */     if ((lsq1 != 0.0D) && (lsq2 != 0.0D)) {
/* 353 */       double denom = Math.sqrt(lsq1) * Math.sqrt(lsq2);
/* 354 */       return dotProd / denom;
/*     */     }
/* 356 */     return 0.0D;
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
/*     */   public static <E> Counter<E> average(GenericCounter<E> c1, GenericCounter<E> c2)
/*     */   {
/* 371 */     Counter<E> average = new Counter();
/* 372 */     Set<E> allKeys = new HashSet(c1.keySet());
/* 373 */     allKeys.addAll(c2.keySet());
/* 374 */     for (E key : allKeys) {
/* 375 */       average.setCount(key, (c1.getCount(key) + c2.getCount(key)) * 0.5D);
/*     */     }
/* 377 */     return average;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Counter<E> linearCombination(GenericCounter<E> c1, double w1, GenericCounter<E> c2, double w2)
/*     */   {
/* 386 */     Counter<E> result = new Counter();
/* 387 */     for (E o : c1.keySet()) {
/* 388 */       result.incrementCount(o, c1.getCount(o) * w1);
/*     */     }
/* 390 */     for (E o : c2.keySet()) {
/* 391 */       result.incrementCount(o, c2.getCount(o) * w2);
/*     */     }
/* 393 */     return result;
/*     */   }
/*     */   
/*     */   public static <E> Counter<E> perturbCounts(GenericCounter<E> c, Random random, double p) {
/* 397 */     Counter<E> result = new Counter(c.getMapFactory());
/* 398 */     for (E key : c.keySet()) {
/* 399 */       double count = c.getCount(key);
/* 400 */       double noise = -Math.log(1.0D - random.nextDouble());
/*     */       
/* 402 */       double perturbedCount = count + noise * p;
/* 403 */       result.setCount(key, perturbedCount);
/*     */     }
/* 405 */     return result;
/*     */   }
/*     */   
/*     */   public static <E> Counter<E> createCounterFromList(List<E> l) {
/* 409 */     return createCounterFromCollection(l);
/*     */   }
/*     */   
/*     */   public static <E> Counter<E> createCounterFromCollection(Collection<E> l) {
/* 413 */     Counter<E> result = new Counter();
/* 414 */     for (E o : l) {
/* 415 */       result.incrementCount(o);
/*     */     }
/* 417 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> List<E> toSortedList(GenericCounter<E> c)
/*     */   {
/* 427 */     List<E> l = new ArrayList(c.keySet());
/* 428 */     Collections.sort(l, c.comparator());
/* 429 */     Collections.reverse(l);
/* 430 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> PriorityQueue<E> toPriorityQueue(GenericCounter<E> c)
/*     */   {
/* 437 */     PriorityQueue<E> queue = new BinaryHeapPriorityQueue();
/* 438 */     for (E key : c.keySet()) {
/* 439 */       double count = c.getCount(key);
/* 440 */       queue.add(key, count);
/*     */     }
/* 442 */     return queue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> void printCounterComparison(GenericCounter<E> a, GenericCounter<E> b)
/*     */   {
/* 452 */     printCounterComparison(a, b, System.err);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> void printCounterComparison(GenericCounter<E> a, GenericCounter<E> b, PrintStream out)
/*     */   {
/* 462 */     if (a.equals(b)) {
/* 463 */       out.println("Counters are equal.");
/* 464 */       return;
/*     */     }
/* 466 */     for (E key : a.keySet()) {
/* 467 */       double aCount = a.getCount(key);
/* 468 */       double bCount = b.getCount(key);
/* 469 */       if (Math.abs(aCount - bCount) > 1.0E-5D) {
/* 470 */         out.println("Counters differ on key " + key + "\t" + a.getCountAsString(key) + " vs. " + b.getCountAsString(key));
/*     */       }
/*     */     }
/*     */     
/* 474 */     Set<E> rest = new HashSet(b.keySet());
/* 475 */     rest.removeAll(a.keySet());
/*     */     
/* 477 */     for (E key : rest) {
/* 478 */       double aCount = a.getCount(key);
/* 479 */       double bCount = b.getCount(key);
/* 480 */       if (Math.abs(aCount - bCount) > 1.0E-5D) {
/* 481 */         out.println("Counters differ on key " + key + "\t" + a.getCountAsString(key) + " vs. " + b.getCountAsString(key));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static <E> Counter<Double> getCountCounts(GenericCounter<E> c) {
/* 487 */     Counter<Double> result = new Counter();
/* 488 */     for (E o : c.keySet()) {
/* 489 */       double count = c.getCount(o);
/* 490 */       result.incrementCount(new Double(count));
/*     */     }
/* 492 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> Counter<E> scale(GenericCounter<E> c, double s)
/*     */   {
/* 499 */     Counter<E> scaled = new Counter(c.getMapFactory());
/* 500 */     for (E key : c.keySet()) {
/* 501 */       scaled.setCount(key, c.getCount(key) * s);
/*     */     }
/* 503 */     return scaled;
/*     */   }
/*     */   
/*     */   public static <E extends Comparable<E>> void printCounterSortedByKeys(GenericCounter<E> c) {
/* 507 */     List<E> keyList = new ArrayList(c.keySet());
/* 508 */     Collections.sort(keyList);
/* 509 */     for (E o : keyList) {
/* 510 */       System.out.println(o + ":" + c.getCountAsString(o));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Counter<E> loadCounter(String filename, Class<E> c)
/*     */     throws RuntimeException
/*     */   {
/* 523 */     Counter<E> counter = new Counter();
/* 524 */     loadIntoCounter(filename, c, counter);
/* 525 */     return counter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> IntCounter<E> loadIntCounter(String filename, Class<E> c)
/*     */     throws Exception
/*     */   {
/* 537 */     IntCounter<E> counter = new IntCounter();
/* 538 */     loadIntoCounter(filename, c, counter);
/* 539 */     return counter;
/*     */   }
/*     */   
/*     */   private static <E> void loadIntoCounter(String filename, Class c, GenericCounter<E> counter)
/*     */     throws RuntimeException
/*     */   {
/*     */     try
/*     */     {
/* 547 */       Constructor m = c.getConstructor(new Class[] { Class.forName("java.lang.String") });
/* 548 */       BufferedReader in = new BufferedReader(new FileReader(filename));
/* 549 */       String line = in.readLine();
/* 550 */       while ((line != null) && (line.length() > 0))
/*     */       {
/* 552 */         String[] fields = line.split("\\p{Space}+");
/*     */         
/* 554 */         E o = m.newInstance((Object[])new String[] { fields[0] });
/* 555 */         counter.setCount(o, fields[1]);
/* 556 */         line = in.readLine();
/*     */       }
/* 558 */       in.close();
/*     */     } catch (Exception e) {
/* 560 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> void saveCounter(GenericCounter<E> c, String filename)
/*     */     throws IOException
/*     */   {
/* 573 */     PrintWriter out = new PrintWriter(new FileWriter(filename));
/* 574 */     for (E key : c.keySet()) {
/* 575 */       out.println(key + " " + c.getCountAsString(key));
/*     */     }
/* 577 */     out.close();
/*     */   }
/*     */   
/*     */   public static void serializeCounter(GenericCounter c, String filename) throws IOException
/*     */   {
/* 582 */     ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
/* 583 */     out.writeObject(c);
/* 584 */     out.close();
/*     */   }
/*     */   
/*     */   public static Counter deserializeCounter(String filename) throws Exception
/*     */   {
/* 589 */     ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
/* 590 */     Counter c = (Counter)in.readObject();
/* 591 */     in.close();
/* 592 */     return c;
/*     */   }
/*     */   
/*     */   public static <E> String toBiggestValuesFirstString(Counter<E> c) {
/* 596 */     return toPriorityQueue(c).toString();
/*     */   }
/*     */   
/*     */   public static <E> String toBiggestValuesFirstString(Counter<E> c, int k) {
/* 600 */     PriorityQueue<E> pq = toPriorityQueue(c);
/* 601 */     PriorityQueue<E> largestK = new BinaryHeapPriorityQueue();
/* 602 */     while ((largestK.size() < k) && (((Iterator)pq).hasNext())) {
/* 603 */       double firstScore = pq.getPriority(pq.getFirst());
/* 604 */       E first = pq.removeFirst();
/* 605 */       largestK.changePriority(first, firstScore);
/*     */     }
/* 607 */     return largestK.toString();
/*     */   }
/*     */   
/*     */   public static <E> String toVerticalString(Counter<E> c) {
/* 611 */     return toVerticalString(c, Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public static <E> String toVerticalString(Counter<E> c, int k) {
/* 615 */     return toVerticalString(c, k, "%g\t%s", false);
/*     */   }
/*     */   
/*     */   public static <E> String toVerticalString(Counter<E> c, String fmt) {
/* 619 */     return toVerticalString(c, Integer.MAX_VALUE, fmt, false);
/*     */   }
/*     */   
/*     */   public static <E> String toVerticalString(Counter<E> c, int k, String fmt) {
/* 623 */     return toVerticalString(c, k, fmt, false);
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
/*     */   public static <E> String toVerticalString(Counter<E> c, int k, String fmt, boolean swap)
/*     */   {
/* 637 */     PriorityQueue<E> q = toPriorityQueue(c);
/* 638 */     List<E> sortedKeys = q.toSortedList();
/* 639 */     StringBuilder sb = new StringBuilder();
/* 640 */     int i = 0;
/* 641 */     for (Iterator<E> keyI = sortedKeys.iterator(); (keyI.hasNext()) && (i < k); i++) {
/* 642 */       E key = keyI.next();
/* 643 */       double val = q.getPriority(key);
/* 644 */       if (swap) {
/* 645 */         sb.append(String.format(fmt, new Object[] { key, Double.valueOf(val) }));
/*     */       } else {
/* 647 */         sb.append(String.format(fmt, new Object[] { Double.valueOf(val), key }));
/*     */       }
/* 649 */       if (keyI.hasNext()) {
/* 650 */         sb.append("\n");
/*     */       }
/*     */     }
/* 653 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Object restrictedArgMax(Counter<E> c, Collection<E> restriction)
/*     */   {
/* 663 */     Object maxKey = null;
/* 664 */     double max = Double.NEGATIVE_INFINITY;
/* 665 */     for (E key : restriction) {
/* 666 */       double count = c.getCount(key);
/* 667 */       if (count > max) {
/* 668 */         max = count;
/* 669 */         maxKey = key;
/*     */       }
/*     */     }
/* 672 */     return maxKey;
/*     */   }
/*     */   
/*     */   public static <T> Counter<T> toCounter(double[] counts, Index<T> index) {
/* 676 */     if (index.size() < counts.length) throw new IllegalArgumentException("Index not large enough to name all the array elements!");
/* 677 */     Counter<T> c = new Counter();
/* 678 */     for (int i = 0; i < counts.length; i++) {
/* 679 */       if (counts[i] != 0.0D) c.setCount(index.get(i), counts[i]);
/*     */     }
/* 681 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T1, T2> TwoDimensionalCounter<T1, T2> scale(TwoDimensionalCounter<T1, T2> c, double d)
/*     */   {
/* 693 */     TwoDimensionalCounter<T1, T2> result = new TwoDimensionalCounter(c.getOuterMapFactory(), c.getInnerMapFactory());
/* 694 */     for (T1 key : c.firstKeySet()) {
/* 695 */       Counter<T2> ctr = c.getCounter(key);
/* 696 */       result.setCounter(key, scale(ctr, d));
/*     */     }
/* 698 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T sample(Counter<T> c, Random rand)
/*     */   {
/* 708 */     double r = rand.nextDouble() * c.totalCount();
/* 709 */     double total = 0.0D;
/* 710 */     for (T t : c.keySet()) {
/* 711 */       total += c.getCount(t);
/* 712 */       if (total >= r) { return t;
/*     */       }
/*     */     }
/* 715 */     return (T)c.keySet().iterator().next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T sample(Counter<T> c)
/*     */   {
/* 725 */     return (T)sample(c, new Random());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Counter<T> powNormalized(Counter<T> c, double temp)
/*     */   {
/* 733 */     Counter<T> d = new Counter();
/* 734 */     for (T t : c.keySet()) {
/* 735 */       d.setCount(t, Math.pow(c.getNormalizedCount(t), temp));
/*     */     }
/* 737 */     return d;
/*     */   }
/*     */   
/*     */   public static <T> Counter<T> pow(Counter<T> c, double temp) {
/* 741 */     Counter<T> d = new Counter();
/* 742 */     for (T t : c.keySet()) {
/* 743 */       d.setCount(t, Math.pow(c.getCount(t), temp));
/*     */     }
/* 745 */     return d;
/*     */   }
/*     */   
/*     */   public static <T> Counter<T> exp(Counter<T> c) {
/* 749 */     Counter<T> d = new Counter();
/* 750 */     for (T t : c.keySet()) {
/* 751 */       d.setCount(t, Math.exp(c.getCount(t)));
/*     */     }
/* 753 */     return d;
/*     */   }
/*     */   
/*     */   public static <T> Counter<T> diff(Counter<T> goldFeatures, Counter<T> guessedFeatures) {
/* 757 */     Counter<T> result = new Counter(goldFeatures);
/* 758 */     result.subtractAll(guessedFeatures, true);
/* 759 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> GenericCounter<T> unmodifiableCounter(GenericCounter<T> counter)
/*     */   {
/* 770 */     new GenericCounter()
/*     */     {
/* 772 */       public Comparator<T> comparator() { return this.val$counter.comparator(); }
/* 773 */       public boolean containsKey(T key) { return this.val$counter.containsKey(key); }
/* 774 */       public double doubleMax() { return this.val$counter.doubleMax(); }
/* 775 */       public double getCount(T key) { return this.val$counter.getCount(key); }
/* 776 */       public String getCountAsString(T key) { return this.val$counter.getCountAsString(key); }
/* 777 */       public MapFactory<T, MutableDouble> getMapFactory() { return this.val$counter.getMapFactory(); }
/* 778 */       public Set<T> keySet() { return this.val$counter.keySet(); }
/*     */       
/* 780 */       public void setCount(T key, String s) { throw new UnsupportedOperationException(); }
/*     */       
/* 782 */       public int size() { return this.val$counter.size(); }
/* 783 */       public double totalDoubleCount() { return this.val$counter.totalDoubleCount(); }
/*     */     };
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
/*     */   public static <E> Counter<E> asCounter(FixedPrioritiesPriorityQueue<E> p)
/*     */   {
/* 797 */     FixedPrioritiesPriorityQueue<E> pq = p.clone();
/* 798 */     Counter<E> counter = new Counter();
/* 799 */     while (pq.hasNext()) {
/* 800 */       double priority = pq.getPriority();
/* 801 */       E element = pq.next();
/* 802 */       counter.incrementCount(element, priority);
/*     */     }
/* 804 */     return counter;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\stats\Counters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */