/*      */ package edu.stanford.nlp.stats;
/*      */ 
/*      */ import edu.stanford.nlp.math.ArrayMath;
/*      */ import edu.stanford.nlp.math.SloppyMath;
/*      */ import edu.stanford.nlp.util.BinaryHeapPriorityQueue;
/*      */ import edu.stanford.nlp.util.EntryValueComparator;
/*      */ import edu.stanford.nlp.util.Filter;
/*      */ import edu.stanford.nlp.util.MapFactory;
/*      */ import edu.stanford.nlp.util.MutableDouble;
/*      */ import edu.stanford.nlp.util.PriorityQueue;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
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
/*      */ public class Counter<E>
/*      */   implements Serializable, GenericCounter<E>, Iterable<E>
/*      */ {
/*      */   Map<E, MutableDouble> map;
/*      */   MapFactory<E, MutableDouble> mapFactory;
/*      */   private double totalCount;
/*   49 */   private static final Comparator hashCodeComparator = new Comparator() {
/*      */     public int compare(Object o1, Object o2) {
/*   51 */       return o1.hashCode() - o2.hashCode();
/*      */     }
/*      */     
/*      */     public boolean equals(Comparator comporator) {
/*   55 */       return comporator == this;
/*      */     }
/*      */   };
/*      */   
/*      */ 
/*      */   private static final long serialVersionUID = 4L;
/*      */   
/*      */ 
/*   63 */   private transient MutableDouble tempMDouble = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Counter()
/*      */   {
/*   71 */     this(MapFactory.HASH_MAP_FACTORY);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Counter(MapFactory<E, MutableDouble> mapFactory)
/*      */   {
/*   78 */     this.mapFactory = mapFactory;
/*   79 */     this.map = mapFactory.newMap();
/*      */     
/*   81 */     this.totalCount = 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Counter(GenericCounter<E> c)
/*      */   {
/*   88 */     this();
/*   89 */     addAll(c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Counter(Collection<E> collection)
/*      */   {
/*   96 */     this();
/*   97 */     addAll(collection);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public MapFactory<E, MutableDouble> getMapFactory()
/*      */   {
/*  104 */     return this.mapFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double totalCount()
/*      */   {
/*  111 */     return this.totalCount;
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
/*      */   public Iterator<E> iterator()
/*      */   {
/*  124 */     return keySet().iterator();
/*      */   }
/*      */   
/*      */   public double totalDoubleCount()
/*      */   {
/*  129 */     return totalCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double totalCount(Filter<E> filter)
/*      */   {
/*  138 */     double total = 0.0D;
/*  139 */     for (E key : this.map.keySet()) {
/*  140 */       if (filter.accept(key)) {
/*  141 */         total += getCount(key);
/*      */       }
/*      */     }
/*  144 */     return total;
/*      */   }
/*      */   
/*      */   public double logSum() {
/*  148 */     double[] toSum = new double[this.map.size()];
/*  149 */     int i = 0;
/*  150 */     for (E key : this.map.keySet()) {
/*  151 */       toSum[(i++)] = getCount(key);
/*      */     }
/*  153 */     return ArrayMath.logSum(toSum);
/*      */   }
/*      */   
/*      */   public void logNormalize() {
/*  157 */     incrementAll(-logSum());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double averageCount()
/*      */   {
/*  164 */     return totalCount() / this.map.size();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getCount(E key)
/*      */   {
/*  174 */     Number count = (Number)this.map.get(key);
/*  175 */     if (count == null) {
/*  176 */       return 0.0D;
/*      */     }
/*  178 */     return count.doubleValue();
/*      */   }
/*      */   
/*      */   public String getCountAsString(E key) {
/*  182 */     return Double.toString(getCount(key));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getNormalizedCount(E key)
/*      */   {
/*  191 */     return getCount(key) / totalCount();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCount(E key, double count)
/*      */   {
/*  202 */     if (this.tempMDouble == null)
/*      */     {
/*  204 */       this.tempMDouble = new MutableDouble();
/*      */     }
/*      */     
/*  207 */     this.tempMDouble.set(count);
/*      */     
/*  209 */     this.tempMDouble = ((MutableDouble)this.map.put(key, this.tempMDouble));
/*      */     
/*      */ 
/*  212 */     this.totalCount += count;
/*  213 */     if (this.tempMDouble != null) {
/*  214 */       this.totalCount -= this.tempMDouble.doubleValue();
/*      */     }
/*      */   }
/*      */   
/*      */   public void setCount(E key, String s) {
/*  219 */     setCount(key, Double.parseDouble(s));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCounts(Collection<E> keys, double count)
/*      */   {
/*  230 */     for (E key : keys) {
/*  231 */       setCount(key, count);
/*      */     }
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
/*      */   public double incrementCount(E key, double count)
/*      */   {
/*  250 */     if (this.tempMDouble == null) {
/*  251 */       this.tempMDouble = new MutableDouble();
/*      */     }
/*  253 */     MutableDouble oldMDouble = (MutableDouble)this.map.put(key, this.tempMDouble);
/*  254 */     this.totalCount += count;
/*  255 */     if (oldMDouble != null) {
/*  256 */       count += oldMDouble.doubleValue();
/*      */     }
/*  258 */     this.tempMDouble.set(count);
/*  259 */     this.tempMDouble = oldMDouble;
/*      */     
/*  261 */     return count;
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
/*      */   public double logIncrementCount(E key, double count)
/*      */   {
/*  277 */     if (this.tempMDouble == null) {
/*  278 */       this.tempMDouble = new MutableDouble();
/*      */     }
/*  280 */     MutableDouble oldMDouble = (MutableDouble)this.map.put(key, this.tempMDouble);
/*  281 */     if (oldMDouble != null) {
/*  282 */       count = SloppyMath.logAdd(count, oldMDouble.doubleValue());
/*  283 */       this.totalCount += count - oldMDouble.doubleValue();
/*      */     } else {
/*  285 */       this.totalCount += count;
/*      */     }
/*  287 */     this.tempMDouble.set(count);
/*  288 */     this.tempMDouble = oldMDouble;
/*      */     
/*  290 */     return count;
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
/*      */   public double incrementCount(E key)
/*      */   {
/*  306 */     return incrementCount(key, 1.0D);
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
/*      */   public void incrementCounts(Collection<E> keys, double count)
/*      */   {
/*  323 */     for (E key : keys) {
/*  324 */       incrementCount(key, count);
/*      */     }
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
/*      */   public void incrementCounts(Collection<E> keys)
/*      */   {
/*  340 */     incrementCounts(keys, 1.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void incrementAll(double count)
/*      */   {
/*  350 */     for (MutableDouble md : this.map.values()) {
/*  351 */       md.set(md.doubleValue() + count);
/*  352 */       this.totalCount += count;
/*      */     }
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
/*      */   public double decrementCount(E key, double count)
/*      */   {
/*  369 */     return incrementCount(key, -count);
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
/*      */   public double decrementCount(E key)
/*      */   {
/*  384 */     return decrementCount(key, 1.0D);
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
/*      */   public void decrementCounts(Collection<E> keys, double count)
/*      */   {
/*  400 */     incrementCounts(keys, -count);
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
/*      */   public void decrementCounts(Collection<E> keys)
/*      */   {
/*  415 */     decrementCounts(keys, 1.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addAll(GenericCounter<E> counter)
/*      */   {
/*  424 */     for (E key : counter.keySet()) {
/*  425 */       incrementCount(key, counter.getCount(key));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addMultiple(GenericCounter<E> counter, double d)
/*      */   {
/*  435 */     for (E key : counter.keySet()) {
/*  436 */       incrementCount(key, counter.getCount(key) * d);
/*  437 */       if (getCount(key) == 0.0D) { remove(key);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void subtractAll(GenericCounter<E> counter)
/*      */   {
/*  447 */     for (E key : counter.keySet()) {
/*  448 */       incrementCount(key, -counter.getCount(key));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void subtractMultiple(GenericCounter<E> counter, double d)
/*      */   {
/*  458 */     for (E key : counter.keySet()) {
/*  459 */       incrementCount(key, -counter.getCount(key) * d);
/*  460 */       if (getCount(key) == 0.0D) { remove(key);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void addAll(Collection<E> collection)
/*      */   {
/*  469 */     for (E key : collection) {
/*  470 */       incrementCount(key);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void multiplyBy(double multiplier)
/*      */   {
/*  478 */     for (E key : this.map.keySet()) {
/*  479 */       setCount(key, getCount(key) * multiplier);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void divideBy(double divisor)
/*      */   {
/*  487 */     for (E key : this.map.keySet()) {
/*  488 */       setCount(key, getCount(key) / divisor);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void divideBy(Counter<E> counter)
/*      */   {
/*  501 */     for (E key : this.map.keySet()) {
/*  502 */       setCount(key, getCount(key) / counter.getCount(key));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void subtractAll(GenericCounter<E> counter, boolean removeZeroKeys)
/*      */   {
/*      */     E key;
/*      */     
/*  512 */     for (Iterator i$ = counter.keySet().iterator(); i$.hasNext(); 
/*      */         
/*  514 */         remove(key))
/*      */     {
/*  512 */       key = i$.next();
/*  513 */       decrementCount(key, counter.getCount(key));
/*  514 */       if ((!removeZeroKeys) || (getCount(key) != 0.0D)) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean containsKey(E key)
/*      */   {
/*  521 */     return this.map.containsKey(key);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public MutableDouble remove(E key)
/*      */   {
/*  529 */     MutableDouble md = (MutableDouble)this.map.remove(key);
/*  530 */     if (md != null) {
/*  531 */       this.totalCount -= md.doubleValue();
/*      */     }
/*  533 */     return md;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void removeAll(Collection<E> keys)
/*      */   {
/*  540 */     for (E key : keys) {
/*  541 */       remove(key);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void clear()
/*      */   {
/*  549 */     this.map.clear();
/*  550 */     this.totalCount = 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int size()
/*      */   {
/*  559 */     return this.map.size();
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  563 */     return size() == 0;
/*      */   }
/*      */   
/*      */   public Set<E> keySet() {
/*  567 */     return this.map.keySet();
/*      */   }
/*      */   
/*      */   public Set<Map.Entry<E, MutableDouble>> entrySet() {
/*  571 */     return this.map.entrySet();
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean equals(Object o)
/*      */   {
/*  577 */     if (this == o) {
/*  578 */       return true;
/*      */     }
/*  580 */     if (!(o instanceof Counter)) {
/*  581 */       return false;
/*      */     }
/*      */     
/*  584 */     Counter<E> counter = (Counter)o;
/*  585 */     if (this.totalCount != counter.totalCount) {
/*  586 */       return false;
/*      */     }
/*  588 */     return this.map.equals(counter.map);
/*      */   }
/*      */   
/*      */   public int hashCode() {
/*  592 */     return this.map.hashCode();
/*      */   }
/*      */   
/*      */   public String toString() {
/*  596 */     return this.map.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString(int maxKeysToPrint)
/*      */   {
/*  607 */     return asBinaryHeapPriorityQueue().toString(maxKeysToPrint);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString(NumberFormat nf, String preAppend, String postAppend, String keyValSeparator, String itemSeparator)
/*      */   {
/*  615 */     StringBuilder sb = new StringBuilder();
/*  616 */     sb.append(preAppend);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  622 */     for (Iterator<E> iter = this.map.keySet().iterator(); iter.hasNext();) {
/*  623 */       E key = iter.next();
/*  624 */       MutableDouble d = (MutableDouble)this.map.get(key);
/*  625 */       sb.append(key);
/*  626 */       sb.append(keyValSeparator);
/*  627 */       sb.append(nf.format(d));
/*  628 */       if (iter.hasNext()) {
/*  629 */         sb.append(itemSeparator);
/*      */       }
/*      */     }
/*  632 */     sb.append(postAppend);
/*  633 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String toString(NumberFormat nf)
/*      */   {
/*  640 */     StringBuilder sb = new StringBuilder();
/*  641 */     sb.append("{");
/*  642 */     List<E> list = new ArrayList(this.map.keySet());
/*      */     try {
/*  644 */       Collections.sort(list);
/*      */     }
/*      */     catch (Exception e) {}
/*  647 */     for (Iterator<E> iter = list.iterator(); iter.hasNext();) {
/*  648 */       E key = iter.next();
/*  649 */       MutableDouble d = (MutableDouble)this.map.get(key);
/*  650 */       sb.append(key);
/*  651 */       sb.append("=");
/*  652 */       sb.append(nf.format(d));
/*  653 */       if (iter.hasNext()) {
/*  654 */         sb.append(", ");
/*      */       }
/*      */     }
/*  657 */     sb.append("}");
/*  658 */     return sb.toString();
/*      */   }
/*      */   
/*      */   public Object clone() {
/*  662 */     return new Counter(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void normalize()
/*      */   {
/*  673 */     double total = totalCount();
/*  674 */     if ((total == 0.0D) || (Double.isNaN(total)) || (total == Double.NEGATIVE_INFINITY) || (total == Double.POSITIVE_INFINITY)) throw new RuntimeException("Can't normalize with bad total: " + total);
/*  675 */     for (E key : this.map.keySet()) {
/*  676 */       setCount(key, getCount(key) / total);
/*      */     }
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
/*      */   public void removeZeroCounts()
/*      */   {
/*  692 */     for (Iterator<E> iter = this.map.keySet().iterator(); iter.hasNext();) {
/*  693 */       if (getCount(iter.next()) == 0.0D) {
/*  694 */         iter.remove();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public PriorityQueue<E> asPriorityQueue()
/*      */   {
/*  704 */     return asBinaryHeapPriorityQueue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public BinaryHeapPriorityQueue<E> asBinaryHeapPriorityQueue()
/*      */   {
/*  712 */     BinaryHeapPriorityQueue<E> pq = new BinaryHeapPriorityQueue();
/*  713 */     for (Map.Entry<E, MutableDouble> entry : this.map.entrySet()) {
/*  714 */       pq.add(entry.getKey(), ((MutableDouble)entry.getValue()).doubleValue());
/*      */     }
/*  716 */     return pq;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double max()
/*      */   {
/*  723 */     double max = Double.NEGATIVE_INFINITY;
/*  724 */     for (E key : this.map.keySet()) {
/*  725 */       max = Math.max(max, getCount(key));
/*      */     }
/*  727 */     return max;
/*      */   }
/*      */   
/*      */   public double doubleMax() {
/*  731 */     return max();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double min()
/*      */   {
/*  738 */     double min = Double.POSITIVE_INFINITY;
/*  739 */     for (E key : this.map.keySet()) {
/*  740 */       min = Math.min(min, getCount(key));
/*      */     }
/*      */     
/*  743 */     return min;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public E argmax(Comparator<E> tieBreaker)
/*      */   {
/*  755 */     double max = Double.NEGATIVE_INFINITY;
/*  756 */     E argmax = null;
/*  757 */     for (E key : this.map.keySet()) {
/*  758 */       double count = getCount(key);
/*  759 */       if ((argmax == null) || (count > max)) {
/*  760 */         max = count;
/*  761 */         argmax = key;
/*      */       }
/*      */     }
/*  764 */     return argmax;
/*      */   }
/*      */   
/*      */   public void retainTop(int num) {
/*  768 */     int numToPurge = size() - num;
/*  769 */     if (numToPurge <= 0) { return;
/*      */     }
/*  771 */     List<E> l = Counters.toSortedList(this);
/*  772 */     Collections.reverse(l);
/*  773 */     for (int i = 0; i < numToPurge; i++) {
/*  774 */       remove(l.get(i));
/*      */     }
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
/*      */   public E argmax()
/*      */   {
/*  789 */     return (E)argmax(hashCodeComparator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public E argmin(Comparator<E> tieBreaker)
/*      */   {
/*  801 */     double min = Double.POSITIVE_INFINITY;
/*  802 */     E argmin = null;
/*      */     
/*  804 */     for (E key : this.map.keySet()) {
/*  805 */       double count = getCount(key);
/*  806 */       if ((argmin == null) || (count < min)) {
/*  807 */         min = count;
/*  808 */         argmin = key;
/*      */       }
/*      */     }
/*      */     
/*  812 */     return argmin;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public E argmin()
/*      */   {
/*  823 */     return (E)argmin(hashCodeComparator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<E> keysAbove(double countThreshold)
/*      */   {
/*  831 */     Set<E> keys = new HashSet();
/*  832 */     for (E key : this.map.keySet()) {
/*  833 */       if (getCount(key) >= countThreshold) {
/*  834 */         keys.add(key);
/*      */       }
/*      */     }
/*  837 */     return keys;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<E> keysBelow(double countThreshold)
/*      */   {
/*  845 */     Set<E> keys = new HashSet();
/*      */     
/*  847 */     for (E key : this.map.keySet()) {
/*  848 */       if (getCount(key) <= countThreshold) {
/*  849 */         keys.add(key);
/*      */       }
/*      */     }
/*  852 */     return keys;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<E> keysAt(double count)
/*      */   {
/*  860 */     Set<E> keys = new HashSet();
/*      */     
/*  862 */     for (E key : this.map.keySet()) {
/*  863 */       if (getCount(key) == count) {
/*  864 */         keys.add(key);
/*      */       }
/*      */     }
/*      */     
/*  868 */     return keys;
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
/*      */   public Comparator<E> comparator(boolean ascending)
/*      */   {
/*  887 */     return new EntryValueComparator(this.map, ascending);
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
/*      */ 
/*      */   public Comparator<E> comparator(boolean ascending, boolean useMagnitude)
/*      */   {
/*  907 */     return new EntryValueComparator(this.map, ascending, useMagnitude);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Comparator<E> comparator()
/*      */   {
/*  915 */     return comparator(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class NaturalComparator
/*      */     implements Comparator
/*      */   {
/*  924 */     public String toString() { return "NaturalComparator"; }
/*      */     
/*  926 */     public int compare(Object o1, Object o2) { if ((o1 instanceof Comparable)) {
/*  927 */         return ((Comparable)o1).compareTo(o2);
/*      */       }
/*  929 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Counter<String> valueOf(String s)
/*      */   {
/*  942 */     Counter<String> result = new Counter();
/*  943 */     String[] lines = s.split("\n");
/*  944 */     for (String line : lines) {
/*  945 */       String[] fields = line.split("\t");
/*  946 */       if (fields.length != 2) throw new RuntimeException("Got unsplittable line: \"" + line + "\"");
/*  947 */       result.setCount(fields[0], Double.parseDouble(fields[1]));
/*      */     }
/*  949 */     return result;
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
/*      */   public static Counter<String> valueOfIgnoreComments(String s)
/*      */   {
/*  962 */     Counter<String> result = new Counter();
/*  963 */     String[] lines = s.split("\n");
/*  964 */     for (String line : lines)
/*  965 */       if (!line.startsWith("#")) {
/*  966 */         String[] fields = line.split("\t");
/*  967 */         if (fields.length != 2) throw new RuntimeException("Got unsplittable line: \"" + line + "\"");
/*  968 */         result.setCount(fields[0], Double.parseDouble(fields[1]));
/*      */       }
/*  970 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Counter<String> fromString(String s)
/*      */   {
/*  979 */     Counter<String> result = new Counter();
/*  980 */     if ((!s.startsWith("{")) || (!s.endsWith("}"))) {
/*  981 */       throw new RuntimeException("invalid format: ||" + s + "||");
/*      */     }
/*  983 */     s = s.substring(1, s.length() - 1);
/*  984 */     String[] lines = s.split(", ");
/*  985 */     for (String line : lines) {
/*  986 */       String[] fields = line.split("=");
/*  987 */       if (fields.length != 2) throw new RuntimeException("Got unsplittable line: \"" + line + "\"");
/*  988 */       result.setCount(fields[0], Double.parseDouble(fields[1]));
/*      */     }
/*  990 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */     throws Exception
/*      */   {
/*  998 */     Counter<String> c = new Counter();
/*  999 */     c.setCount("p", 0.0D);
/* 1000 */     c.setCount("q", 2.0D);
/* 1001 */     System.out.println(c + " -> " + c.totalCount() + " should be {p=0.0, q=2.0} -> 2.0");
/* 1002 */     c.incrementCount("p");
/* 1003 */     System.out.println(c + " -> " + c.totalCount() + " should be {p=1.0, q=2.0} -> 3.0");
/* 1004 */     c.incrementCount("p", 2.0D);
/* 1005 */     System.out.println(c.min() + " " + (String)c.argmin() + " should be 2.0 q");
/* 1006 */     c.setCount("w", -5.0D);
/* 1007 */     c.setCount("x", -2.5D);
/* 1008 */     List<String> biggestKeys = new ArrayList(c.keySet());
/* 1009 */     Collections.sort(biggestKeys, c.comparator(false, true));
/* 1010 */     System.out.println(biggestKeys + " should be [w, p, x, q]");
/* 1011 */     System.out.println(c + " should be {p=3.0, q=2.0, w=-5.0, x=-2.5}");
/* 1012 */     if (args.length > 0)
/*      */     {
/* 1014 */       ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(args[0])));
/* 1015 */       out.writeObject(c);
/* 1016 */       out.close();
/*      */       
/*      */ 
/* 1019 */       ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(args[0])));
/* 1020 */       c = (Counter)in.readObject();
/* 1021 */       in.close();
/* 1022 */       System.out.println(c + " -> " + c.totalCount() + " should be same -> -2.5");
/*      */       
/*      */ 
/* 1025 */       System.out.println(c.min() + " " + (String)c.argmin() + " should be -5 w");
/* 1026 */       c.clear();
/* 1027 */       System.out.println(c + " -> " + c.totalCount() + " should be {} -> 0");
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\stats\Counter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */