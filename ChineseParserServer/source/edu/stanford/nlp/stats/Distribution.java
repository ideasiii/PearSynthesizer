/*     */ package edu.stanford.nlp.stats;
/*     */ 
/*     */ import edu.stanford.nlp.util.MutableDouble;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Distribution<E>
/*     */   implements Serializable, Sampler<E>
/*     */ {
/*     */   static final long serialVersionUID = 6707148234288637809L;
/*     */   private int numberOfKeys;
/*     */   private double reservedMass;
/*     */   protected Counter<E> counter;
/*     */   private static final int NUM_ENTRIES_IN_STRING = 20;
/*  29 */   private static boolean verbose = false;
/*     */   
/*     */   public Counter<E> getCounter() {
/*  32 */     return this.counter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public E drawSample()
/*     */   {
/*  40 */     return (E)sampleFrom();
/*     */   }
/*     */   
/*     */   public String toString(NumberFormat nf)
/*     */   {
/*  45 */     return this.counter.toString(nf);
/*     */   }
/*     */   
/*     */   public double getReservedMass() {
/*  49 */     return this.reservedMass;
/*     */   }
/*     */   
/*     */   public int getNumberOfKeys() {
/*  53 */     return this.numberOfKeys;
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<E> keySet()
/*     */   {
/*  59 */     return this.counter.keySet();
/*     */   }
/*     */   
/*     */   public boolean containsKey(E key) {
/*  63 */     return this.counter.containsKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getCount(E key)
/*     */   {
/*  73 */     return this.counter.getCount(key);
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
/*     */   public static <E> Distribution<E> getDistributionFromPartiallySpecifiedCounter(Counter<E> c, int numKeys)
/*     */   {
/*  86 */     double total = c.totalDoubleCount();
/*  87 */     Distribution<E> d; if (total >= 1.0D) {
/*  88 */       Distribution<E> d = getDistribution(c);
/*  89 */       d.numberOfKeys = numKeys;
/*     */     } else {
/*  91 */       d = new Distribution();
/*  92 */       d.numberOfKeys = numKeys;
/*  93 */       d.counter = c;
/*  94 */       d.reservedMass = (1.0D - total);
/*     */     }
/*  96 */     return d;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Distribution<E> getUniformDistribution(Set<E> s)
/*     */   {
/* 106 */     Distribution<E> norm = new Distribution();
/* 107 */     norm.counter = new Counter();
/* 108 */     norm.numberOfKeys = s.size();
/* 109 */     norm.reservedMass = 0.0D;
/* 110 */     double total = s.size();
/* 111 */     double count = 1.0D / total;
/* 112 */     for (E key : s) {
/* 113 */       norm.counter.setCount(key, count);
/*     */     }
/* 115 */     return norm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Distribution<E> getPerturbedUniformDistribution(Set<E> s, Random r)
/*     */   {
/* 123 */     Distribution<E> norm = new Distribution();
/* 124 */     norm.counter = new Counter();
/* 125 */     norm.numberOfKeys = s.size();
/* 126 */     norm.reservedMass = 0.0D;
/* 127 */     double total = s.size();
/* 128 */     double prob = 1.0D / total;
/* 129 */     double stdev = prob / 1000.0D;
/* 130 */     for (E key : s) {
/* 131 */       norm.counter.setCount(key, prob + r.nextGaussian() * stdev);
/*     */     }
/* 133 */     return norm;
/*     */   }
/*     */   
/*     */   public static <E> Distribution<E> getPerturbedDistribution(GenericCounter<E> wordCounter, Random r) {
/* 137 */     Distribution<E> norm = new Distribution();
/* 138 */     norm.counter = new Counter();
/* 139 */     norm.numberOfKeys = wordCounter.size();
/* 140 */     norm.reservedMass = 0.0D;
/* 141 */     double totalCount = wordCounter.totalDoubleCount();
/* 142 */     double stdev = 1.0D / norm.numberOfKeys / 1000.0D;
/* 143 */     for (E key : wordCounter.keySet()) {
/* 144 */       double prob = wordCounter.getCount(key) / totalCount;
/* 145 */       double perturbedProb = prob + r.nextGaussian() * stdev;
/* 146 */       if (perturbedProb < 0.0D) {
/* 147 */         perturbedProb = 0.0D;
/*     */       }
/* 149 */       norm.counter.setCount(key, perturbedProb);
/*     */     }
/* 151 */     return norm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Distribution<E> getDistribution(GenericCounter<E> counter)
/*     */   {
/* 162 */     return getDistributionWithReservedMass(counter, 0.0D);
/*     */   }
/*     */   
/*     */   public static <E> Distribution<E> getDistributionWithReservedMass(GenericCounter<E> counter, double reservedMass) {
/* 166 */     Distribution<E> norm = new Distribution();
/* 167 */     norm.counter = new Counter();
/* 168 */     norm.numberOfKeys = counter.size();
/* 169 */     norm.reservedMass = reservedMass;
/* 170 */     double total = counter.totalDoubleCount() * (1.0D + reservedMass);
/* 171 */     if (total == 0.0D) {
/* 172 */       total = 1.0D;
/*     */     }
/* 174 */     for (E key : counter.keySet()) {
/* 175 */       double count = counter.getCount(key) / total;
/*     */       
/* 177 */       norm.counter.setCount(key, count);
/*     */     }
/* 179 */     return norm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Distribution<E> getDistributionFromLogValues(GenericCounter<E> counter)
/*     */   {
/* 190 */     Counter<E> c = new Counter();
/*     */     
/*     */ 
/* 193 */     double max = counter.doubleMax();
/* 194 */     for (E key : counter.keySet()) {
/* 195 */       double count = Math.exp(counter.getCount(key) - max);
/* 196 */       c.setCount(key, count);
/*     */     }
/* 198 */     return getDistribution(c);
/*     */   }
/*     */   
/*     */   public static <E> Distribution<E> absolutelyDiscountedDistribution(GenericCounter<E> counter, int numberOfKeys, double discount) {
/* 202 */     Distribution<E> norm = new Distribution();
/* 203 */     norm.counter = new Counter();
/* 204 */     double total = counter.totalDoubleCount();
/* 205 */     double reservedMass = 0.0D;
/* 206 */     for (E key : counter.keySet()) {
/* 207 */       double count = counter.getCount(key);
/* 208 */       if (count > discount) {
/* 209 */         double newCount = (count - discount) / total;
/* 210 */         norm.counter.setCount(key, newCount);
/*     */         
/* 212 */         reservedMass += discount;
/*     */       } else {
/* 214 */         reservedMass += count;
/*     */       }
/*     */     }
/*     */     
/* 218 */     norm.numberOfKeys = numberOfKeys;
/* 219 */     norm.reservedMass = (reservedMass / total);
/* 220 */     if (verbose) {
/* 221 */       System.err.println("unseenKeys=" + (norm.numberOfKeys - norm.counter.size()) + " seenKeys=" + norm.counter.size() + " reservedMass=" + norm.reservedMass);
/* 222 */       double zeroCountProb = norm.reservedMass / (numberOfKeys - norm.counter.size());
/* 223 */       System.err.println("0 count prob: " + zeroCountProb);
/* 224 */       if (discount >= 1.0D) {
/* 225 */         System.err.println("1 count prob: " + zeroCountProb);
/*     */       } else {
/* 227 */         System.err.println("1 count prob: " + (1.0D - discount) / total);
/*     */       }
/* 229 */       if (discount >= 2.0D) {
/* 230 */         System.err.println("2 count prob: " + zeroCountProb);
/*     */       } else {
/* 232 */         System.err.println("2 count prob: " + (2.0D - discount) / total);
/*     */       }
/* 234 */       if (discount >= 3.0D) {
/* 235 */         System.err.println("3 count prob: " + zeroCountProb);
/*     */       } else {
/* 237 */         System.err.println("3 count prob: " + (3.0D - discount) / total);
/*     */       }
/*     */     }
/*     */     
/* 241 */     return norm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Distribution<E> laplaceSmoothedDistribution(GenericCounter<E> counter, int numberOfKeys)
/*     */   {
/* 253 */     return laplaceSmoothedDistribution(counter, numberOfKeys, 1.0D);
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
/*     */   public static <E> Distribution<E> laplaceSmoothedDistribution(GenericCounter<E> counter, int numberOfKeys, double lambda)
/*     */   {
/* 266 */     Distribution<E> norm = new Distribution();
/* 267 */     norm.counter = new Counter();
/* 268 */     double total = counter.totalDoubleCount();
/* 269 */     double newTotal = total + lambda * numberOfKeys;
/* 270 */     double reservedMass = (numberOfKeys - counter.size()) * lambda / newTotal;
/* 271 */     if (verbose) {
/* 272 */       System.err.println(numberOfKeys - counter.size() + " * " + lambda + " / (" + total + " + ( " + lambda + " * " + numberOfKeys + ") )");
/*     */     }
/* 274 */     norm.numberOfKeys = numberOfKeys;
/* 275 */     norm.reservedMass = reservedMass;
/* 276 */     if (verbose) {
/* 277 */       System.err.println("reserved mass=" + reservedMass);
/*     */     }
/* 279 */     for (E key : counter.keySet()) {
/* 280 */       double count = counter.getCount(key);
/* 281 */       norm.counter.setCount(key, (count + lambda) / newTotal);
/*     */     }
/* 283 */     if (verbose) {
/* 284 */       System.err.println("unseenKeys=" + (norm.numberOfKeys - norm.counter.size()) + " seenKeys=" + norm.counter.size() + " reservedMass=" + norm.reservedMass);
/* 285 */       System.err.println("0 count prob: " + lambda / newTotal);
/* 286 */       System.err.println("1 count prob: " + (1.0D + lambda) / newTotal);
/* 287 */       System.err.println("2 count prob: " + (2.0D + lambda) / newTotal);
/* 288 */       System.err.println("3 count prob: " + (3.0D + lambda) / newTotal);
/*     */     }
/* 290 */     return norm;
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
/*     */   public static <E> Distribution<E> laplaceWithExplicitUnknown(GenericCounter<E> counter, double lambda, E UNK)
/*     */   {
/* 304 */     Distribution<E> norm = new Distribution();
/* 305 */     norm.counter = new Counter();
/* 306 */     double total = counter.totalDoubleCount() + lambda * (counter.size() - 1);
/* 307 */     norm.numberOfKeys = counter.size();
/* 308 */     norm.reservedMass = 0.0D;
/* 309 */     for (E key : counter.keySet()) {
/* 310 */       if (key.equals(UNK)) {
/* 311 */         norm.counter.setCount(key, counter.getCount(key) / total);
/*     */       } else {
/* 313 */         norm.counter.setCount(key, (counter.getCount(key) + lambda) / total);
/*     */       }
/*     */     }
/* 316 */     return norm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Distribution<E> goodTuringSmoothedCounter(GenericCounter<E> counter, int numberOfKeys)
/*     */   {
/* 328 */     int[] countCounts = getCountCounts(counter);
/*     */     
/*     */ 
/*     */ 
/* 332 */     for (int i = 1; i <= 10; i++) {
/* 333 */       if (countCounts[i] < 3) {
/* 334 */         return laplaceSmoothedDistribution(counter, numberOfKeys, 0.5D);
/*     */       }
/*     */     }
/*     */     
/* 338 */     double observedMass = counter.totalDoubleCount();
/* 339 */     double reservedMass = countCounts[1] / observedMass;
/*     */     
/*     */ 
/*     */ 
/* 343 */     double[] adjustedFreq = new double[10];
/* 344 */     for (int freq = 1; freq < 10; freq++) {
/* 345 */       adjustedFreq[freq] = ((freq + 1) * countCounts[(freq + 1)] / countCounts[freq]);
/* 346 */       observedMass -= (freq - adjustedFreq[freq]) * countCounts[freq];
/*     */     }
/*     */     
/* 349 */     double normFactor = (1.0D - reservedMass) / observedMass;
/*     */     
/* 351 */     Distribution<E> norm = new Distribution();
/* 352 */     norm.counter = new Counter();
/*     */     
/*     */ 
/* 355 */     for (E key : counter.keySet()) {
/* 356 */       int origFreq = (int)Math.round(counter.getCount(key));
/* 357 */       if (origFreq < 10) {
/* 358 */         norm.counter.setCount(key, adjustedFreq[origFreq] * normFactor);
/*     */       } else {
/* 360 */         norm.counter.setCount(key, origFreq * normFactor);
/*     */       }
/*     */     }
/*     */     
/* 364 */     norm.numberOfKeys = numberOfKeys;
/* 365 */     norm.reservedMass = reservedMass;
/* 366 */     return norm;
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
/*     */   public static <E> Distribution<E> goodTuringWithExplicitUnknown(GenericCounter<E> counter, E UNK)
/*     */   {
/* 381 */     int[] countCounts = getCountCounts(counter);
/*     */     
/*     */ 
/*     */ 
/* 385 */     for (int i = 1; i <= 10; i++) {
/* 386 */       if (countCounts[i] < 3) {
/* 387 */         return laplaceWithExplicitUnknown(counter, 0.5D, UNK);
/*     */       }
/*     */     }
/*     */     
/* 391 */     double observedMass = counter.totalDoubleCount();
/*     */     
/*     */ 
/*     */ 
/* 395 */     double[] adjustedFreq = new double[10];
/* 396 */     for (int freq = 1; freq < 10; freq++) {
/* 397 */       adjustedFreq[freq] = ((freq + 1) * countCounts[(freq + 1)] / countCounts[freq]);
/* 398 */       observedMass -= (freq - adjustedFreq[freq]) * countCounts[freq];
/*     */     }
/*     */     
/* 401 */     Distribution<E> norm = new Distribution();
/* 402 */     norm.counter = new Counter();
/*     */     
/*     */ 
/* 405 */     for (E key : counter.keySet()) {
/* 406 */       int origFreq = (int)Math.round(counter.getCount(key));
/* 407 */       if (origFreq < 10) {
/* 408 */         norm.counter.setCount(key, adjustedFreq[origFreq] / observedMass);
/*     */       } else {
/* 410 */         norm.counter.setCount(key, origFreq / observedMass);
/*     */       }
/*     */     }
/*     */     
/* 414 */     norm.numberOfKeys = counter.size();
/* 415 */     norm.reservedMass = 0.0D;
/* 416 */     return norm;
/*     */   }
/*     */   
/*     */   private static <E> int[] getCountCounts(GenericCounter<E> counter)
/*     */   {
/* 421 */     int[] countCounts = new int[11];
/* 422 */     for (int i = 0; i <= 10; i++) {
/* 423 */       countCounts[i] = 0;
/*     */     }
/* 425 */     for (E key : counter.keySet()) {
/* 426 */       int count = (int)Math.round(counter.getCount(key));
/* 427 */       if (count <= 10) {
/* 428 */         countCounts[count] += 1;
/*     */       }
/*     */     }
/* 431 */     return countCounts;
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
/*     */ 
/*     */   public static <E> Distribution<E> distributionWithDirichletPrior(GenericCounter<E> c, Distribution<E> prior, double weight)
/*     */   {
/* 451 */     Distribution<E> norm = new Distribution();
/* 452 */     double totalWeight = c.totalDoubleCount() + weight;
/* 453 */     if ((prior instanceof DynamicDistribution)) {
/* 454 */       throw new UnsupportedOperationException("Cannot make normalized counter with Dynamic prior.");
/*     */     }
/* 456 */     norm.counter = Counters.linearCombination(c, 1.0D / totalWeight, prior.counter, weight / totalWeight);
/* 457 */     norm.numberOfKeys = prior.numberOfKeys;
/* 458 */     norm.reservedMass = (prior.reservedMass * weight / totalWeight);
/*     */     
/* 460 */     return norm;
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
/*     */   public static <E> Distribution<E> dynamicCounterWithDirichletPrior(GenericCounter<E> c, Distribution<E> prior, double weight)
/*     */   {
/* 476 */     double totalWeight = c.totalDoubleCount() + weight;
/* 477 */     Distribution<E> norm = new DynamicDistribution(prior, weight / totalWeight);
/* 478 */     norm.counter = new Counter();
/*     */     
/*     */ 
/* 481 */     for (E key : c.keySet()) {
/* 482 */       double count = c.getCount(key) / totalWeight;
/* 483 */       prior.addToKeySet(key);
/* 484 */       norm.counter.setCount(key, count);
/*     */     }
/* 486 */     norm.numberOfKeys = prior.numberOfKeys;
/* 487 */     return norm;
/*     */   }
/*     */   
/*     */   private static class DynamicDistribution<E> extends Distribution<E> {
/*     */     private Distribution<E> prior;
/*     */     private double priorMultiplier;
/*     */     
/*     */     public DynamicDistribution(Distribution<E> prior, double priorMultiplier) {
/* 495 */       super();
/* 496 */       this.prior = prior;
/* 497 */       this.priorMultiplier = priorMultiplier;
/*     */     }
/*     */     
/*     */     public double probabilityOf(E o) {
/* 501 */       return this.counter.getCount(o) + this.prior.probabilityOf(o) * this.priorMultiplier;
/*     */     }
/*     */     
/*     */     public double totalCount() {
/* 505 */       return this.counter.totalCount() + this.prior.totalCount() * this.priorMultiplier;
/*     */     }
/*     */     
/*     */     public Set<E> keySet() {
/* 509 */       return this.prior.keySet();
/*     */     }
/*     */     
/*     */     public void addToKeySet(E o) {
/* 513 */       this.prior.addToKeySet(o);
/*     */     }
/*     */     
/*     */     public boolean containsKey(E key) {
/* 517 */       return this.prior.containsKey(key);
/*     */     }
/*     */     
/*     */     public Object argMax() {
/* 521 */       return Counters.linearCombination(this.counter, 1.0D, this.prior.counter, this.priorMultiplier).argmax();
/*     */     }
/*     */     
/*     */     public E sampleFrom() {
/* 525 */       double d = Math.random();
/* 526 */       Set<E> s = this.prior.keySet();
/* 527 */       for (E o : s) {
/* 528 */         d -= probabilityOf(o);
/* 529 */         if (d < 0.0D) {
/* 530 */           return o;
/*     */         }
/*     */       }
/* 533 */       System.err.println("ERROR: Distribution sums to less than 1");
/* 534 */       System.err.println("Sampled " + d + "      sum is " + totalCount());
/* 535 */       throw new RuntimeException("");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Distribution<E> distributionFromLogisticCounter(GenericCounter<E> cntr)
/*     */   {
/* 544 */     double expSum = 0.0D;
/* 545 */     int numKeys = 0;
/* 546 */     for (E key : cntr.keySet()) {
/* 547 */       expSum += Math.exp(cntr.getCount(key));
/* 548 */       numKeys++;
/*     */     }
/* 550 */     Distribution<E> probs = new Distribution();
/* 551 */     probs.counter = new Counter();
/* 552 */     probs.reservedMass = 0.0D;
/* 553 */     probs.numberOfKeys = numKeys;
/* 554 */     for (E key : cntr.keySet()) {
/* 555 */       probs.counter.setCount(key, Math.exp(cntr.getCount(key)) / expSum);
/*     */     }
/* 557 */     return probs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E sampleFrom()
/*     */   {
/* 567 */     double d = Math.random();
/* 568 */     for (Iterator<Map.Entry<E, MutableDouble>> entryIter = this.counter.entrySet().iterator(); entryIter.hasNext();) {
/* 569 */       Map.Entry<E, MutableDouble> entry = (Map.Entry)entryIter.next();
/* 570 */       d -= ((MutableDouble)entry.getValue()).doubleValue();
/* 571 */       if (d < 0.0D) {
/* 572 */         return (E)entry.getKey();
/*     */       }
/*     */     }
/* 575 */     throw new RuntimeException("ERROR: Distribution sums to more than 1");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double probabilityOf(E key)
/*     */   {
/* 585 */     if (this.counter.containsKey(key)) {
/* 586 */       return this.counter.getCount(key);
/*     */     }
/* 588 */     int remainingKeys = this.numberOfKeys - this.counter.size();
/* 589 */     if (remainingKeys <= 0) {
/* 590 */       return 0.0D;
/*     */     }
/* 592 */     return this.reservedMass / remainingKeys;
/*     */   }
/*     */   
/*     */ 
/*     */   public E argmax()
/*     */   {
/* 598 */     return (E)this.counter.argmax();
/*     */   }
/*     */   
/*     */   public double totalCount() {
/* 602 */     return this.counter.totalCount() + this.reservedMass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addToKeySet(E o)
/*     */   {
/* 611 */     if (!this.counter.containsKey(o)) {
/* 612 */       this.counter.setCount(o, 0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 617 */     if (this == o) {
/* 618 */       return true;
/*     */     }
/* 620 */     if (!(o instanceof Distribution)) {
/* 621 */       return false;
/*     */     }
/* 623 */     return equals((Distribution)o);
/*     */   }
/*     */   
/*     */   public boolean equals(Distribution distribution) {
/* 627 */     if (this.numberOfKeys != distribution.numberOfKeys) {
/* 628 */       return false;
/*     */     }
/* 630 */     if (this.reservedMass != distribution.reservedMass) {
/* 631 */       return false;
/*     */     }
/* 633 */     if (!this.counter.equals(distribution.counter)) {
/* 634 */       return false;
/*     */     }
/*     */     
/* 637 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 641 */     int result = this.numberOfKeys;
/* 642 */     long temp = Double.doubleToLongBits(this.reservedMass);
/* 643 */     result = 29 * result + (int)(temp ^ temp >>> 32);
/* 644 */     result = 29 * result + this.counter.hashCode();
/* 645 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 653 */     NumberFormat nf = new DecimalFormat("0.0##E0");
/* 654 */     List<E> keyList = new ArrayList(keySet());
/* 655 */     Collections.sort(keyList, new Comparator() {
/*     */       public int compare(E o1, E o2) {
/* 657 */         if (Distribution.this.probabilityOf(o1) < Distribution.this.probabilityOf(o2)) {
/* 658 */           return 1;
/*     */         }
/* 660 */         return -1;
/*     */       }
/*     */       
/* 663 */     });
/* 664 */     StringBuffer sb = new StringBuffer();
/* 665 */     sb.append("[");
/* 666 */     for (int i = 0; i < 20; i++) {
/* 667 */       if (keyList.size() <= i) {
/*     */         break;
/*     */       }
/* 670 */       E o = keyList.get(i);
/* 671 */       double prob = probabilityOf(o);
/* 672 */       sb.append(o + ":" + nf.format(prob) + " ");
/*     */     }
/* 674 */     sb.append("]");
/* 675 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 682 */     Counter<String> c = new Counter();
/*     */     
/* 684 */     double p = 1000.0D;
/*     */     
/* 686 */     String UNK = "!*UNKNOWN*!";
/* 687 */     Set<String> s = new HashSet();
/* 688 */     s.add(UNK);
/*     */     
/*     */ 
/* 691 */     for (int rank = 1; rank < 2000; rank++) {
/* 692 */       String i = String.valueOf(rank);
/* 693 */       c.setCount(i, Math.round(1000.0D / rank));
/* 694 */       s.add(i);
/*     */     }
/*     */     
/* 697 */     for (int rank = 2000; rank <= 4000; rank++) {
/* 698 */       String i = String.valueOf(rank);
/* 699 */       s.add(i);
/*     */     }
/*     */     
/* 702 */     Distribution<String> n = getDistribution(c);
/* 703 */     Distribution<String> prior = getUniformDistribution(s);
/* 704 */     Distribution<String> dir1 = distributionWithDirichletPrior(c, prior, 4000.0D);
/* 705 */     Distribution<String> dir2 = dynamicCounterWithDirichletPrior(c, prior, 4000.0D);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 712 */     c.setCount(UNK, 45.0D);
/* 713 */     Distribution<String> add1 = laplaceWithExplicitUnknown(c, 0.5D, UNK);
/* 714 */     Distribution<String> gt = goodTuringWithExplicitUnknown(c, UNK);
/*     */     
/*     */ 
/*     */ 
/* 718 */     System.out.println("Freq    Norm                  Add1                  Dir1                  Dir2                  G-T");
/* 719 */     for (int i = 1; i < 5; i++) {
/* 720 */       System.out.print(Math.round(1000.0D / i));
/* 721 */       System.out.print("   ");
/* 722 */       String in = String.valueOf(i);
/* 723 */       System.out.print(n.probabilityOf(String.valueOf(in)));
/* 724 */       System.out.print("  ");
/* 725 */       System.out.print(add1.probabilityOf(in));
/* 726 */       System.out.print("  ");
/* 727 */       System.out.print(dir1.probabilityOf(in));
/* 728 */       System.out.print("  ");
/* 729 */       System.out.print(dir2.probabilityOf(in));
/* 730 */       System.out.print("  ");
/* 731 */       System.out.print(gt.probabilityOf(in));
/* 732 */       System.out.println("  ");
/*     */     }
/* 734 */     System.out.println();
/* 735 */     System.out.println("--------------------------------------------");
/* 736 */     System.out.println();
/* 737 */     System.out.print(0);
/* 738 */     System.out.print("   ");
/* 739 */     System.out.print(n.probabilityOf(UNK));
/* 740 */     System.out.print("  ");
/* 741 */     System.out.print(add1.probabilityOf(UNK));
/* 742 */     System.out.print("  ");
/* 743 */     System.out.print(dir1.probabilityOf(UNK));
/* 744 */     System.out.print("  ");
/* 745 */     System.out.print(dir2.probabilityOf(UNK));
/* 746 */     System.out.print("  ");
/* 747 */     System.out.print(gt.probabilityOf(UNK));
/* 748 */     System.out.println();
/* 749 */     System.out.println("--------------------------------------------");
/* 750 */     System.out.println();
/* 751 */     System.out.print(1);
/* 752 */     System.out.print("   ");
/* 753 */     String last = String.valueOf(1500);
/* 754 */     System.out.print(n.probabilityOf(last));
/* 755 */     System.out.print("  ");
/* 756 */     System.out.print(add1.probabilityOf(last));
/* 757 */     System.out.print("  ");
/* 758 */     System.out.print(dir1.probabilityOf(last));
/* 759 */     System.out.print("  ");
/* 760 */     System.out.print(dir2.probabilityOf(last));
/* 761 */     System.out.print("  ");
/* 762 */     System.out.print(gt.probabilityOf(last));
/* 763 */     System.out.println();
/*     */     
/* 765 */     System.out.println("Totals:");
/*     */     
/* 767 */     System.out.print(n.totalCount());
/* 768 */     System.out.print("  ");
/* 769 */     System.out.print(add1.totalCount());
/* 770 */     System.out.print("  ");
/* 771 */     System.out.print(dir1.totalCount());
/* 772 */     System.out.print("  ");
/* 773 */     System.out.print(dir2.totalCount());
/* 774 */     System.out.print("  ");
/* 775 */     System.out.print(gt.totalCount());
/* 776 */     System.out.println();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\stats\Distribution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */