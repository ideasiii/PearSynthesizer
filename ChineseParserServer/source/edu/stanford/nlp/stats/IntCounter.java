/*     */ package edu.stanford.nlp.stats;
/*     */ 
/*     */ import edu.stanford.nlp.util.EntryValueComparator;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import edu.stanford.nlp.util.MapFactory;
/*     */ import edu.stanford.nlp.util.MutableInteger;
/*     */ import java.io.Serializable;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class IntCounter<E>
/*     */   implements Serializable, GenericCounter<E>
/*     */ {
/*     */   private Map<E, MutableInteger> map;
/*     */   private MapFactory mapFactory;
/*     */   private int totalCount;
/*  35 */   private static final Comparator naturalComparator = new NaturalComparator(null);
/*     */   
/*     */ 
/*     */   private static final long serialVersionUID = 4L;
/*     */   
/*     */ 
/*     */ 
/*     */   public IntCounter()
/*     */   {
/*  44 */     this(MapFactory.HASH_MAP_FACTORY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public IntCounter(MapFactory mapFactory)
/*     */   {
/*  51 */     this.mapFactory = mapFactory;
/*  52 */     this.map = mapFactory.newMap();
/*  53 */     this.totalCount = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public IntCounter(IntCounter<E> c)
/*     */   {
/*  60 */     this();
/*  61 */     addAll(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MapFactory getMapFactory()
/*     */   {
/*  68 */     return this.mapFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int totalCount()
/*     */   {
/*  76 */     return this.totalCount;
/*     */   }
/*     */   
/*     */   public double totalDoubleCount() {
/*  80 */     return totalCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int totalCount(Filter filter)
/*     */   {
/*  89 */     int total = 0;
/*  90 */     for (Iterator iter = this.map.keySet().iterator(); iter.hasNext();) {
/*  91 */       Object key = iter.next();
/*  92 */       if (filter.accept(key)) {
/*  93 */         total += getIntCount(key);
/*     */       }
/*     */     }
/*  96 */     return total;
/*     */   }
/*     */   
/*     */   public double totalDoubleCount(Filter filter) {
/* 100 */     return totalCount(filter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double averageCount()
/*     */   {
/* 107 */     return totalCount() / this.map.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getCount(E key)
/*     */   {
/* 117 */     return getIntCount(key);
/*     */   }
/*     */   
/*     */   public String getCountAsString(E key) {
/* 121 */     return Integer.toString(getIntCount(key));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntCount(Object key)
/*     */   {
/* 131 */     MutableInteger count = (MutableInteger)this.map.get(key);
/* 132 */     if (count == null) {
/* 133 */       return 0;
/*     */     }
/* 135 */     return count.intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getNormalizedCount(E key)
/*     */   {
/* 143 */     return getCount(key) / totalCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCount(E key, int count)
/*     */   {
/* 154 */     if (this.tempMInteger == null) {
/* 155 */       this.tempMInteger = new MutableInteger();
/*     */     }
/* 157 */     this.tempMInteger.set(count);
/* 158 */     this.tempMInteger = ((MutableInteger)this.map.put(key, this.tempMInteger));
/*     */     
/*     */ 
/* 161 */     this.totalCount += count;
/* 162 */     if (this.tempMInteger != null) {
/* 163 */       this.totalCount -= this.tempMInteger.intValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCount(E key, String s)
/*     */   {
/* 169 */     setCount(key, Integer.parseInt(s));
/*     */   }
/*     */   
/*     */ 
/* 173 */   private transient MutableInteger tempMInteger = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCounts(Collection<E> keys, int count)
/*     */   {
/* 183 */     for (Iterator<E> iter = keys.iterator(); iter.hasNext();) {
/* 184 */       setCount(iter.next(), count);
/*     */     }
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
/*     */   public void incrementCount(E key, int count)
/*     */   {
/* 201 */     if (this.tempMInteger == null) {
/* 202 */       this.tempMInteger = new MutableInteger();
/*     */     }
/* 204 */     this.tempMInteger.set(count);
/* 205 */     MutableInteger oldMInteger = (MutableInteger)this.map.put(key, this.tempMInteger);
/* 206 */     if (oldMInteger != null) {
/* 207 */       this.tempMInteger.set(count + oldMInteger.intValue());
/*     */     }
/* 209 */     this.tempMInteger = oldMInteger;
/*     */     
/* 211 */     this.totalCount += count;
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
/*     */   public void incrementCount(E key)
/*     */   {
/* 228 */     incrementCount(key, 1);
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
/*     */   public void incrementCounts(Collection<E> keys, int count)
/*     */   {
/* 244 */     for (Iterator<E> iter = keys.iterator(); iter.hasNext();) {
/* 245 */       incrementCount(iter.next(), count);
/*     */     }
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
/*     */   public void incrementCounts(Collection<E> keys)
/*     */   {
/* 261 */     incrementCounts(keys, 1);
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
/*     */   public void decrementCount(E key, int count)
/*     */   {
/* 277 */     incrementCount(key, -count);
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
/*     */   public void decrementCount(E key)
/*     */   {
/* 292 */     decrementCount(key, 1);
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
/*     */   public void decrementCounts(Collection<E> keys, int count)
/*     */   {
/* 308 */     incrementCounts(keys, -count);
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
/*     */   public void decrementCounts(Collection<E> keys)
/*     */   {
/* 323 */     decrementCounts(keys, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAll(IntCounter<E> counter)
/*     */   {
/* 332 */     for (Iterator<E> iter = counter.keySet().iterator(); iter.hasNext();) {
/* 333 */       E key = iter.next();
/* 334 */       int count = counter.getIntCount(key);
/* 335 */       incrementCount(key, count);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void subtractAll(IntCounter<E> counter)
/*     */   {
/* 345 */     for (Iterator<E> iter = this.map.keySet().iterator(); iter.hasNext();) {
/* 346 */       E key = iter.next();
/* 347 */       decrementCount(key, counter.getIntCount(key));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsKey(E key)
/*     */   {
/* 354 */     return this.map.containsKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object remove(E key)
/*     */   {
/* 363 */     return this.map.remove(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void removeAll(Collection<E> c)
/*     */   {
/* 370 */     for (Iterator<E> iter = c.iterator(); iter.hasNext();) {
/* 371 */       remove(iter.next());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 379 */     this.map.clear();
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 384 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 388 */     return size() == 0;
/*     */   }
/*     */   
/*     */   public Set<E> keySet() {
/* 392 */     return this.map.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 398 */     if (this == o) {
/* 399 */       return true;
/*     */     }
/* 401 */     if (!(o instanceof IntCounter)) {
/* 402 */       return false;
/*     */     }
/*     */     
/* 405 */     IntCounter counter = (IntCounter)o;
/*     */     
/* 407 */     return this.map.equals(counter.map);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 411 */     return this.map.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 415 */     return this.map.toString();
/*     */   }
/*     */   
/*     */   public String toString(NumberFormat nf, String preAppend, String postAppend, String keyValSeparator, String itemSeparator)
/*     */   {
/* 420 */     StringBuffer sb = new StringBuffer();
/* 421 */     sb.append(preAppend);
/* 422 */     List list = new ArrayList(this.map.keySet());
/*     */     try {
/* 424 */       Collections.sort(list);
/*     */     }
/*     */     catch (Exception e) {}
/* 427 */     for (Iterator iter = list.iterator(); iter.hasNext();) {
/* 428 */       Object key = iter.next();
/* 429 */       MutableInteger d = (MutableInteger)this.map.get(key);
/* 430 */       sb.append(key + keyValSeparator);
/* 431 */       sb.append(nf.format(d));
/* 432 */       if (iter.hasNext()) {
/* 433 */         sb.append(itemSeparator);
/*     */       }
/*     */     }
/* 436 */     sb.append(postAppend);
/* 437 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String toString(NumberFormat nf)
/*     */   {
/* 442 */     StringBuffer sb = new StringBuffer();
/* 443 */     sb.append("{");
/* 444 */     List list = new ArrayList(this.map.keySet());
/*     */     try {
/* 446 */       Collections.sort(list);
/*     */     }
/*     */     catch (Exception e) {}
/* 449 */     for (Iterator iter = list.iterator(); iter.hasNext();) {
/* 450 */       Object key = iter.next();
/* 451 */       MutableInteger d = (MutableInteger)this.map.get(key);
/* 452 */       sb.append(key + "=");
/* 453 */       sb.append(nf.format(d));
/* 454 */       if (iter.hasNext()) {
/* 455 */         sb.append(", ");
/*     */       }
/*     */     }
/* 458 */     sb.append("}");
/* 459 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 463 */     return new IntCounter(this);
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
/*     */   public void removeZeroCounts()
/*     */   {
/* 480 */     for (Iterator<E> iter = this.map.keySet().iterator(); iter.hasNext();) {
/* 481 */       if (getCount(iter.next()) == 0.0D) {
/* 482 */         iter.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int max()
/*     */   {
/* 491 */     int max = Integer.MIN_VALUE;
/* 492 */     for (Iterator<E> iter = this.map.keySet().iterator(); iter.hasNext();) {
/* 493 */       max = Math.max(max, getIntCount(iter.next()));
/*     */     }
/* 495 */     return max;
/*     */   }
/*     */   
/*     */   public double doubleMax() {
/* 499 */     return max();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int min()
/*     */   {
/* 506 */     int min = Integer.MAX_VALUE;
/* 507 */     for (Iterator iter = this.map.keySet().iterator(); iter.hasNext();) {
/* 508 */       min = Math.min(min, getIntCount(iter.next()));
/*     */     }
/* 510 */     return min;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E argmax(Comparator tieBreaker)
/*     */   {
/* 522 */     int max = Integer.MIN_VALUE;
/* 523 */     E argmax = null;
/* 524 */     for (Iterator<E> iter = keySet().iterator(); iter.hasNext();) {
/* 525 */       E key = iter.next();
/* 526 */       int count = getIntCount(key);
/* 527 */       if ((argmax == null) || (count > max) || ((count == max) && (tieBreaker.compare(key, argmax) < 0))) {
/* 528 */         max = count;
/* 529 */         argmax = key;
/*     */       }
/*     */     }
/* 532 */     return argmax;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E argmax()
/*     */   {
/* 544 */     return (E)argmax(naturalComparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E argmin(Comparator tieBreaker)
/*     */   {
/* 556 */     int min = Integer.MAX_VALUE;
/* 557 */     E argmin = null;
/* 558 */     for (Iterator<E> iter = this.map.keySet().iterator(); iter.hasNext();) {
/* 559 */       E key = iter.next();
/* 560 */       int count = getIntCount(key);
/* 561 */       if ((argmin == null) || (count < min) || ((count == min) && (tieBreaker.compare(key, argmin) < 0))) {
/* 562 */         min = count;
/* 563 */         argmin = key;
/*     */       }
/*     */     }
/* 566 */     return argmin;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E argmin()
/*     */   {
/* 577 */     return (E)argmin(naturalComparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set keysAbove(int countThreshold)
/*     */   {
/* 585 */     Set keys = new HashSet();
/* 586 */     for (Iterator iter = this.map.keySet().iterator(); iter.hasNext();) {
/* 587 */       Object key = iter.next();
/* 588 */       if (getIntCount(key) >= countThreshold) {
/* 589 */         keys.add(key);
/*     */       }
/*     */     }
/* 592 */     return keys;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set keysBelow(int countThreshold)
/*     */   {
/* 600 */     Set keys = new HashSet();
/* 601 */     for (Iterator iter = this.map.keySet().iterator(); iter.hasNext();) {
/* 602 */       Object key = iter.next();
/* 603 */       if (getIntCount(key) <= countThreshold) {
/* 604 */         keys.add(key);
/*     */       }
/*     */     }
/* 607 */     return keys;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set keysAt(int count)
/*     */   {
/* 615 */     Set keys = new HashSet();
/* 616 */     for (Iterator iter = this.map.keySet().iterator(); iter.hasNext();) {
/* 617 */       Object key = iter.next();
/* 618 */       if (getIntCount(key) == count) {
/* 619 */         keys.add(key);
/*     */       }
/*     */     }
/* 622 */     return keys;
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
/*     */   public Comparator comparator(boolean ascending)
/*     */   {
/* 641 */     return new EntryValueComparator(this.map, ascending);
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
/*     */   public Comparator comparator(boolean ascending, boolean useMagnitude)
/*     */   {
/* 661 */     return new EntryValueComparator(this.map, ascending, useMagnitude);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Comparator comparator()
/*     */   {
/* 669 */     return comparator(true);
/*     */   }
/*     */   
/*     */ 
/*     */   private static class NaturalComparator
/*     */     implements Comparator
/*     */   {
/*     */     public int compare(Object o1, Object o2)
/*     */     {
/* 678 */       if ((o1 instanceof Comparable)) {
/* 679 */         return ((Comparable)o1).compareTo(o2);
/*     */       }
/* 681 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\stats\IntCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */