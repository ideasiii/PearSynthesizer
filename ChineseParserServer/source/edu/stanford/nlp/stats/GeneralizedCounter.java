/*      */ package edu.stanford.nlp.stats;
/*      */ 
/*      */ import edu.stanford.nlp.util.MutableDouble;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
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
/*      */ public class GeneralizedCounter
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   32 */   private static final Object[] zeroKey = new Object[0];
/*      */   
/*   34 */   private Map map = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */   private int depth;
/*      */   
/*      */ 
/*      */ 
/*      */   private double total;
/*      */   
/*      */ 
/*      */ 
/*      */   private GeneralizedCounter() {}
/*      */   
/*      */ 
/*      */ 
/*      */   public GeneralizedCounter(int depth)
/*      */   {
/*   52 */     this.depth = depth;
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
/*      */   public Set entrySet()
/*      */   {
/*   66 */     return entrySet(new HashSet(), zeroKey, true);
/*      */   }
/*      */   
/*      */   private Set entrySet(Set s, Object[] key, boolean useLists) {
/*      */     Iterator i;
/*      */     Iterator i;
/*   72 */     if (this.depth == 1)
/*      */     {
/*   74 */       Set keys = this.map.keySet();
/*   75 */       for (i = keys.iterator(); i.hasNext();) {
/*   76 */         Object[] newKey = new Object[key.length + 1];
/*   77 */         if (key.length > 0) {
/*   78 */           System.arraycopy(key, 0, newKey, 0, key.length);
/*      */         }
/*   80 */         Object finalKey = i.next();
/*   81 */         newKey[key.length] = finalKey;
/*   82 */         MutableDouble value = (MutableDouble)this.map.get(finalKey);
/*   83 */         Double value1 = new Double(value.doubleValue());
/*   84 */         if (useLists) {
/*   85 */           s.add(new Entry(Arrays.asList(newKey), value1));
/*      */         } else {
/*   87 */           s.add(new Entry(newKey[0], value1));
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*   92 */       Set keys = this.map.keySet();
/*      */       
/*      */ 
/*   95 */       for (i = keys.iterator(); i.hasNext();) {
/*   96 */         Object o = i.next();
/*   97 */         Object[] newKey = new Object[key.length + 1];
/*   98 */         if (key.length > 0) {
/*   99 */           System.arraycopy(key, 0, newKey, 0, key.length);
/*      */         }
/*  101 */         newKey[key.length] = o;
/*      */         
/*  103 */         conditionalizeHelper(o).entrySet(s, newKey, true);
/*      */       }
/*      */     }
/*      */     
/*  107 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set lowestLevelCounterEntrySet()
/*      */   {
/*  118 */     return lowestLevelCounterEntrySet(new HashSet(), zeroKey, true);
/*      */   }
/*      */   
/*      */ 
/*      */   private Set lowestLevelCounterEntrySet(Set s, Object[] key, boolean useLists)
/*      */   {
/*  124 */     Set keys = this.map.keySet();
/*  125 */     Iterator i; Iterator i; if (this.depth == 2)
/*      */     {
/*  127 */       for (i = keys.iterator(); i.hasNext();) {
/*  128 */         Object[] newKey = new Object[key.length + 1];
/*  129 */         if (key.length > 0) {
/*  130 */           System.arraycopy(key, 0, newKey, 0, key.length);
/*      */         }
/*  132 */         Object finalKey = i.next();
/*  133 */         newKey[key.length] = finalKey;
/*  134 */         Counter c = conditionalizeHelper(finalKey).oneDimensionalCounterView();
/*  135 */         if (useLists) {
/*  136 */           s.add(new Entry(Arrays.asList(newKey), c));
/*      */         } else {
/*  138 */           s.add(new Entry(newKey[0], c));
/*      */         }
/*      */         
/*      */       }
/*      */       
/*      */     } else {
/*  144 */       for (i = keys.iterator(); i.hasNext();) {
/*  145 */         Object o = i.next();
/*  146 */         Object[] newKey = new Object[key.length + 1];
/*  147 */         if (key.length > 0) {
/*  148 */           System.arraycopy(key, 0, newKey, 0, key.length);
/*      */         }
/*  150 */         newKey[key.length] = o;
/*      */         
/*  152 */         conditionalizeHelper(o).lowestLevelCounterEntrySet(s, newKey, true);
/*      */       }
/*      */     }
/*      */     
/*  156 */     return s;
/*      */   }
/*      */   
/*      */   private static class Entry implements Map.Entry {
/*      */     private Object key;
/*      */     private Object value;
/*      */     
/*      */     Entry(Object key, Object value) {
/*  164 */       this.key = key;
/*  165 */       this.value = value;
/*      */     }
/*      */     
/*      */     public Object getKey() {
/*  169 */       return this.key;
/*      */     }
/*      */     
/*      */     public Object getValue() {
/*  173 */       return this.value;
/*      */     }
/*      */     
/*      */     public Object setValue(Object value) {
/*  177 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  181 */       if (this == o) {
/*  182 */         return true;
/*      */       }
/*  184 */       if (!(o instanceof Entry)) {
/*  185 */         return false;
/*      */       }
/*  187 */       Entry e = (Entry)o;
/*      */       
/*  189 */       Object key1 = e.getKey();
/*  190 */       if ((this.key == null) || (!this.key.equals(key1))) {
/*  191 */         return false;
/*      */       }
/*      */       
/*  194 */       Object value1 = e.getValue();
/*  195 */       if ((this.value == null) || (!this.value.equals(value1))) {
/*  196 */         return false;
/*      */       }
/*      */       
/*  199 */       return true;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  203 */       if ((this.key == null) || (this.value == null)) {
/*  204 */         return 0;
/*      */       }
/*  206 */       return this.key.hashCode() ^ this.value.hashCode();
/*      */     }
/*      */     
/*      */     public String toString() {
/*  210 */       return this.key.toString() + "=" + this.value.toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double totalCount()
/*      */   {
/*  220 */     if (depth() == 1) {
/*  221 */       return this.total;
/*      */     }
/*  223 */     double result = 0.0D;
/*  224 */     for (Iterator i = topLevelKeySet().iterator(); i.hasNext();) {
/*  225 */       Object o = i.next();
/*  226 */       result += conditionalizeOnce(o).totalCount();
/*      */     }
/*  228 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set topLevelKeySet()
/*      */   {
/*  240 */     return this.map.keySet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set keySet()
/*      */   {
/*  248 */     return keySet(new HashSet(), zeroKey, true);
/*      */   }
/*      */   
/*      */   private Set keySet(Set s, Object[] key, boolean useList) {
/*      */     Iterator i;
/*      */     Iterator i;
/*  254 */     if (this.depth == 1)
/*      */     {
/*  256 */       Set keys = this.map.keySet();
/*  257 */       for (i = keys.iterator(); i.hasNext();) {
/*  258 */         Object[] newKey = new Object[key.length + 1];
/*  259 */         if (key.length > 0) {
/*  260 */           System.arraycopy(key, 0, newKey, 0, key.length);
/*      */         }
/*  262 */         newKey[key.length] = i.next();
/*  263 */         if (useList) {
/*  264 */           s.add(Arrays.asList(newKey));
/*      */         } else {
/*  266 */           s.add(newKey[0]);
/*      */         }
/*      */       }
/*      */     } else {
/*  270 */       Set keys = this.map.keySet();
/*      */       
/*      */ 
/*  273 */       for (i = keys.iterator(); i.hasNext();) {
/*  274 */         Object o = i.next();
/*  275 */         Object[] newKey = new Object[key.length + 1];
/*  276 */         if (key.length > 0) {
/*  277 */           System.arraycopy(key, 0, newKey, 0, key.length);
/*      */         }
/*  279 */         newKey[key.length] = o;
/*      */         
/*  281 */         conditionalizeHelper(o).keySet(s, newKey, true);
/*      */       }
/*      */     }
/*      */     
/*  285 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public int depth()
/*      */   {
/*  293 */     return this.depth;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  300 */     return this.map.isEmpty();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getCount(Object o)
/*      */   {
/*  309 */     if (this.depth > 1) {
/*  310 */       wrongDepth();
/*      */     }
/*  312 */     Number count = (Number)this.map.get(o);
/*  313 */     if (count != null) {
/*  314 */       return count.doubleValue();
/*      */     }
/*  316 */     return 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getCount(Object o1, Object o2)
/*      */   {
/*  326 */     if (this.depth != 2) {
/*  327 */       wrongDepth();
/*      */     }
/*  329 */     GeneralizedCounter gc1 = (GeneralizedCounter)this.map.get(o1);
/*  330 */     if (gc1 == null) {
/*  331 */       return 0.0D;
/*      */     }
/*  333 */     return gc1.getCount(o2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getCount(Object o1, Object o2, Object o3)
/*      */   {
/*  343 */     if (this.depth != 3) {
/*  344 */       wrongDepth();
/*      */     }
/*  346 */     GeneralizedCounter gc1 = (GeneralizedCounter)this.map.get(o1);
/*  347 */     if (gc1 == null) {
/*  348 */       return 0.0D;
/*      */     }
/*  350 */     return gc1.getCount(o2, o3);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double[] getCounts(List l)
/*      */   {
/*  362 */     if (l.size() != this.depth) {
/*  363 */       wrongDepth();
/*      */     }
/*      */     
/*  366 */     double[] counts = new double[this.depth + 1];
/*      */     
/*  368 */     GeneralizedCounter next = this;
/*  369 */     counts[0] = next.totalCount();
/*  370 */     Iterator i = l.iterator();
/*  371 */     int j = 1;
/*  372 */     Object o = i.next();
/*  373 */     while (i.hasNext()) {
/*  374 */       next = next.conditionalizeHelper(o);
/*  375 */       counts[j] = next.totalCount();
/*  376 */       o = i.next();
/*  377 */       j++;
/*      */     }
/*  379 */     counts[this.depth] = next.getCount(o);
/*      */     
/*  381 */     return counts;
/*      */   }
/*      */   
/*      */   private GeneralizedCounter conditionalizeHelper(Object o)
/*      */   {
/*  386 */     if (this.depth > 1) {
/*  387 */       GeneralizedCounter next = (GeneralizedCounter)this.map.get(o);
/*  388 */       if (next == null)
/*      */       {
/*  390 */         this.map.put(o, next = new GeneralizedCounter(this.depth - 1));
/*      */       }
/*  392 */       return next;
/*      */     }
/*  394 */     throw new RuntimeException("Error -- can't conditionalize a distribution of depth 1");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public GeneralizedCounter conditionalize(List l)
/*      */   {
/*  404 */     int n = l.size();
/*  405 */     if (n >= depth()) {
/*  406 */       throw new RuntimeException("Error -- attempted to conditionalize a GeneralizedCounter of depth " + depth() + " on a vector of length " + n);
/*      */     }
/*  408 */     GeneralizedCounter next = this;
/*  409 */     for (Iterator i = l.iterator(); i.hasNext();) {
/*  410 */       next = next.conditionalizeHelper(i.next());
/*      */     }
/*  412 */     return next;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public GeneralizedCounter conditionalizeOnce(Object o)
/*      */   {
/*  421 */     if (depth() < 1) {
/*  422 */       throw new RuntimeException("Error -- attempted to conditionalize a GeneralizedCounter of depth " + depth());
/*      */     }
/*  424 */     return conditionalizeHelper(o);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void incrementCount(List l, Object o)
/*      */   {
/*  432 */     incrementCount(l, o, 1.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void incrementCount(List l, Object o, double count)
/*      */   {
/*  439 */     if (l.size() != this.depth - 1) {
/*  440 */       wrongDepth();
/*      */     }
/*      */     
/*  443 */     GeneralizedCounter next = this;
/*  444 */     for (Iterator i = l.iterator(); i.hasNext();) {
/*  445 */       next.addToTotal(count);
/*  446 */       Object o2 = i.next();
/*  447 */       next = next.conditionalizeHelper(o2);
/*      */     }
/*  449 */     next.addToTotal(count);
/*      */     
/*  451 */     next.incrementCount1D(o, count);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void incrementCount(List l)
/*      */   {
/*  459 */     incrementCount(l, 1.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void incrementCount(List l, double count)
/*      */   {
/*  466 */     if (l.size() != this.depth) {
/*  467 */       wrongDepth();
/*      */     }
/*      */     
/*  470 */     GeneralizedCounter next = this;
/*  471 */     Iterator i = l.iterator();
/*  472 */     Object o = i.next();
/*  473 */     while (i.hasNext()) {
/*  474 */       next.addToTotal(count);
/*  475 */       next = next.conditionalizeHelper(o);
/*  476 */       o = i.next();
/*      */     }
/*  478 */     next.incrementCount1D(o, count);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void incrementCount2D(Object first, Object second)
/*      */   {
/*  485 */     incrementCount2D(first, second, 1.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void incrementCount2D(Object first, Object second, double count)
/*      */   {
/*  493 */     if (this.depth != 2) {
/*  494 */       wrongDepth();
/*      */     }
/*      */     
/*  497 */     addToTotal(count);
/*  498 */     GeneralizedCounter next = conditionalizeHelper(first);
/*  499 */     next.incrementCount1D(second, count);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void incrementCount3D(Object first, Object second, Object third)
/*      */   {
/*  506 */     incrementCount3D(first, second, third, 1.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void incrementCount3D(Object first, Object second, Object third, double count)
/*      */   {
/*  514 */     if (this.depth != 3) {
/*  515 */       wrongDepth();
/*      */     }
/*      */     
/*  518 */     addToTotal(count);
/*  519 */     GeneralizedCounter next = conditionalizeHelper(first);
/*  520 */     next.incrementCount2D(second, third, count);
/*      */   }
/*      */   
/*      */   private void addToTotal(double d) {
/*  524 */     this.total += d;
/*      */   }
/*      */   
/*      */ 
/*  528 */   private transient MutableDouble tempMDouble = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void incrementCount1D(Object o)
/*      */   {
/*  535 */     incrementCount1D(o, 1.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void incrementCount1D(Object o, double count)
/*      */   {
/*  543 */     if (this.depth > 1) {
/*  544 */       wrongDepth();
/*      */     }
/*      */     
/*  547 */     addToTotal(count);
/*      */     
/*  549 */     if (this.tempMDouble == null) {
/*  550 */       this.tempMDouble = new MutableDouble();
/*      */     }
/*  552 */     this.tempMDouble.set(count);
/*  553 */     MutableDouble oldMDouble = (MutableDouble)this.map.put(o, this.tempMDouble);
/*      */     
/*  555 */     if (oldMDouble != null) {
/*  556 */       this.tempMDouble.set(count + oldMDouble.doubleValue());
/*      */     }
/*      */     
/*  559 */     this.tempMDouble = oldMDouble;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean containsKey(List key)
/*      */   {
/*  571 */     GeneralizedCounter next = this;
/*  572 */     for (int i = 0; i < key.size() - 1; i++) {
/*  573 */       next = next.conditionalizeHelper(key.get(i));
/*  574 */       if (next == null) return false;
/*      */     }
/*  576 */     return next.map.containsKey(key.get(key.size() - 1));
/*      */   }
/*      */   
/*      */   public GeneralizedCounter reverseKeys() {
/*  580 */     GeneralizedCounter result = new GeneralizedCounter();
/*  581 */     Set entries = entrySet();
/*  582 */     for (Iterator iter = entries.iterator(); iter.hasNext();) {
/*  583 */       Entry entry = (Entry)iter.next();
/*  584 */       List list = (List)entry.getKey();
/*  585 */       double count = ((Double)entry.getValue()).doubleValue();
/*  586 */       Collections.reverse(list);
/*  587 */       result.incrementCount(list, count);
/*      */     }
/*  589 */     return result;
/*      */   }
/*      */   
/*      */   private void wrongDepth()
/*      */   {
/*  594 */     throw new RuntimeException("Error -- attempt to operate with key of wrong length. depth=" + this.depth);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Counter counterView()
/*      */   {
/*  606 */     return new CounterView(null);
/*      */   }
/*      */   
/*      */   private class CounterView extends Counter {
/*      */     private CounterView() {}
/*      */     
/*  612 */     public double incrementCount(Object o, double count) { throw new UnsupportedOperationException(); }
/*      */     
/*      */     public void setCount(Object o, double count)
/*      */     {
/*  616 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public double totalCount() {
/*  620 */       return GeneralizedCounter.this.totalCount();
/*      */     }
/*      */     
/*      */     public double getCount(Object o) {
/*  624 */       List o1 = (List)o;
/*  625 */       if (o1.size() != GeneralizedCounter.this.depth) {
/*  626 */         return 0.0D;
/*      */       }
/*  628 */       return GeneralizedCounter.this.getCounts(o1)[GeneralizedCounter.this.depth];
/*      */     }
/*      */     
/*      */     public int size()
/*      */     {
/*  633 */       return GeneralizedCounter.this.map.size();
/*      */     }
/*      */     
/*      */     public Set keySet() {
/*  637 */       return GeneralizedCounter.this.keySet();
/*      */     }
/*      */     
/*      */     public MutableDouble remove(Object o) {
/*  641 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  645 */       if (!(key instanceof List)) {
/*  646 */         return false;
/*      */       }
/*  648 */       return GeneralizedCounter.this.containsKey((List)key);
/*      */     }
/*      */     
/*      */     public void clear()
/*      */     {
/*  653 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/*  657 */       return GeneralizedCounter.this.isEmpty();
/*      */     }
/*      */     
/*      */     public Set entrySet() {
/*  661 */       return GeneralizedCounter.this.entrySet();
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  665 */       if (o == this) {
/*  666 */         return true;
/*      */       }
/*      */       
/*  669 */       if (!(o instanceof Counter)) {
/*  670 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  676 */       return entrySet().equals(((Counter)o).entrySet());
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/*  681 */       int total = 17;
/*  682 */       for (Iterator i = entrySet().iterator(); i.hasNext();) {
/*  683 */         total = 37 * total + i.next().hashCode();
/*      */       }
/*  685 */       return total;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  689 */       StringBuffer sb = new StringBuffer("{");
/*  690 */       for (Iterator i = entrySet().iterator(); i.hasNext();) {
/*  691 */         GeneralizedCounter.Entry e = (GeneralizedCounter.Entry)i.next();
/*  692 */         sb.append(e.toString());
/*  693 */         if (i.hasNext()) {
/*  694 */           sb.append(",");
/*      */         }
/*      */       }
/*  697 */       sb.append("}");
/*  698 */       return sb.toString();
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
/*      */   public Counter oneDimensionalCounterView()
/*      */   {
/*  715 */     if (this.depth != 1) {
/*  716 */       throw new UnsupportedOperationException();
/*      */     }
/*  718 */     return new OneDimensionalCounterView(null);
/*      */   }
/*      */   
/*      */   private class OneDimensionalCounterView extends Counter {
/*      */     private OneDimensionalCounterView() {}
/*      */     
/*  724 */     public double incrementCount(Object o, double count) { throw new UnsupportedOperationException(); }
/*      */     
/*      */     public void setCount(Object o, double count)
/*      */     {
/*  728 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public double totalCount() {
/*  732 */       return GeneralizedCounter.this.totalCount();
/*      */     }
/*      */     
/*      */     public double getCount(Object o) {
/*  736 */       return GeneralizedCounter.this.getCount(o);
/*      */     }
/*      */     
/*      */     public int size() {
/*  740 */       return GeneralizedCounter.this.map.size();
/*      */     }
/*      */     
/*      */     public Set keySet() {
/*  744 */       return GeneralizedCounter.this.keySet(new HashSet(), GeneralizedCounter.zeroKey, false);
/*      */     }
/*      */     
/*      */     public MutableDouble remove(Object o) {
/*  748 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean containsKey(Object key) {
/*  752 */       return GeneralizedCounter.this.map.containsKey(key);
/*      */     }
/*      */     
/*      */     public Object get(Object o) {
/*  756 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Object put(Object o) {
/*  760 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void putAll(Map m) {
/*  764 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void clear() {
/*  768 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/*  772 */       return GeneralizedCounter.this.isEmpty();
/*      */     }
/*      */     
/*      */     public Set entrySet() {
/*  776 */       return GeneralizedCounter.this.entrySet(new HashSet(), GeneralizedCounter.zeroKey, false);
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  780 */       if (o == this) {
/*  781 */         return true;
/*      */       }
/*      */       
/*  784 */       if (!(o instanceof Counter)) {
/*  785 */         return false;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  791 */       return entrySet().equals(((Counter)o).entrySet());
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/*  796 */       int total = 17;
/*  797 */       for (Iterator i = entrySet().iterator(); i.hasNext();) {
/*  798 */         total = 37 * total + i.next().hashCode();
/*      */       }
/*  800 */       return total;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  804 */       StringBuffer sb = new StringBuffer("{");
/*  805 */       for (Iterator i = entrySet().iterator(); i.hasNext();) {
/*  806 */         GeneralizedCounter.Entry e = (GeneralizedCounter.Entry)i.next();
/*  807 */         sb.append(e.toString());
/*  808 */         if (i.hasNext()) {
/*  809 */           sb.append(",");
/*      */         }
/*      */       }
/*  812 */       sb.append("}");
/*  813 */       return sb.toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/*  820 */     return this.map.toString();
/*      */   }
/*      */   
/*      */   public String toString(String param) {
/*  824 */     if (param.equals("contingency")) {
/*  825 */       StringBuffer sb = new StringBuffer();
/*  826 */       Set keys = topLevelKeySet();
/*  827 */       List list = new ArrayList(keys);
/*  828 */       Collections.sort(list);
/*  829 */       for (Iterator it = list.iterator(); it.hasNext();) {
/*  830 */         Object obj = it.next();
/*  831 */         sb.append(obj);
/*  832 */         sb.append(" = ");
/*  833 */         GeneralizedCounter gc = conditionalizeOnce(obj);
/*  834 */         sb.append(gc);
/*  835 */         sb.append("\n");
/*      */       }
/*  837 */       return sb.toString(); }
/*  838 */     if (param.equals("sorted")) {
/*  839 */       StringBuffer sb = new StringBuffer();
/*  840 */       Set keys = topLevelKeySet();
/*  841 */       List list = new ArrayList(keys);
/*  842 */       Collections.sort(list);
/*  843 */       sb.append("{\n");
/*  844 */       for (Iterator it = list.iterator(); it.hasNext();) {
/*  845 */         Object obj = it.next();
/*  846 */         sb.append(obj);
/*  847 */         sb.append(" = ");
/*  848 */         GeneralizedCounter gc = conditionalizeOnce(obj);
/*  849 */         sb.append(gc);
/*  850 */         sb.append("\n");
/*      */       }
/*  852 */       sb.append("}\n");
/*  853 */       return sb.toString();
/*      */     }
/*  855 */     return toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/*  865 */     Object[] a1 = { "a", "b" };
/*  866 */     Object[] a2 = { "a", "b" };
/*      */     
/*  868 */     System.out.println(a1.equals(a2));
/*      */     
/*      */ 
/*  871 */     GeneralizedCounter gc = new GeneralizedCounter(3);
/*  872 */     gc.incrementCount(Arrays.asList(new Object[] { "a", "j", "x" }), 3.0D);
/*  873 */     gc.incrementCount(Arrays.asList(new Object[] { "a", "l", "x" }), 3.0D);
/*  874 */     gc.incrementCount(Arrays.asList(new Object[] { "b", "k", "y" }), 3.0D);
/*  875 */     gc.incrementCount(Arrays.asList(new Object[] { "b", "k", "z" }), 3.0D);
/*      */     
/*  877 */     System.out.println("incremented counts.");
/*      */     
/*  879 */     System.out.println(gc.dumpKeys());
/*      */     
/*  881 */     System.out.println("string representation of generalized counter:");
/*  882 */     System.out.println(gc.toString());
/*      */     
/*      */ 
/*  885 */     gc.printKeySet();
/*      */     
/*  887 */     System.out.println("entry set:\n" + gc.entrySet());
/*      */     
/*      */ 
/*  890 */     arrayPrintDouble(gc.getCounts(Arrays.asList(new Object[] { "a", "j", "x" })));
/*  891 */     arrayPrintDouble(gc.getCounts(Arrays.asList(new Object[] { "a", "j", "z" })));
/*  892 */     arrayPrintDouble(gc.getCounts(Arrays.asList(new Object[] { "b", "k", "w" })));
/*  893 */     arrayPrintDouble(gc.getCounts(Arrays.asList(new Object[] { "b", "k", "z" })));
/*      */     
/*  895 */     GeneralizedCounter gc1 = gc.conditionalize(Arrays.asList(new Object[] { "a" }));
/*  896 */     gc1.incrementCount(Arrays.asList(new Object[] { "j", "x" }));
/*  897 */     gc1.incrementCount2D("j", "z");
/*  898 */     GeneralizedCounter gc2 = gc1.conditionalize(Arrays.asList(new Object[] { "j" }));
/*  899 */     gc2.incrementCount1D("x");
/*  900 */     System.out.println("Pretty-printing gc after incrementing gc1:");
/*  901 */     gc.prettyPrint();
/*  902 */     System.out.println("Total: " + gc.totalCount());
/*      */     
/*  904 */     gc1.printKeySet();
/*  905 */     System.out.println("another entry set:\n" + gc1.entrySet());
/*      */     
/*      */ 
/*  908 */     Counter c = gc.counterView();
/*      */     
/*  910 */     System.out.println("string representation of counter view:");
/*  911 */     System.out.println(c.toString());
/*      */     
/*  913 */     double d1 = c.getCount(Arrays.asList(new Object[] { "a", "j", "x" }));
/*  914 */     double d2 = c.getCount(Arrays.asList(new Object[] { "a", "j", "w" }));
/*      */     
/*  916 */     System.out.println(d1 + " " + d2);
/*      */     
/*      */ 
/*  919 */     Counter c1 = gc1.counterView();
/*      */     
/*  921 */     System.out.println("Count of {j,x} -- should be 3.0\t" + c1.getCount(Arrays.asList(new Object[] { "j", "x" })));
/*      */     
/*      */ 
/*  924 */     System.out.println(c.keySet() + " size " + c.keySet().size());
/*  925 */     System.out.println(c1.keySet() + " size " + c1.keySet().size());
/*      */     
/*  927 */     System.out.println(c1.equals(c));
/*  928 */     System.out.println(c.equals(c1));
/*  929 */     System.out.println(c.equals(c));
/*      */     
/*  931 */     System.out.println("### testing equality of regular Counter...");
/*      */     
/*  933 */     Counter z1 = new Counter();
/*  934 */     Counter z2 = new Counter();
/*      */     
/*  936 */     z1.incrementCount("a1");
/*  937 */     z1.incrementCount("a2");
/*      */     
/*  939 */     z2.incrementCount("b");
/*      */     
/*  941 */     System.out.println(z1.equals(z2));
/*      */     
/*  943 */     System.out.println(z1.toString());
/*  944 */     System.out.println(z1.keySet().toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void printKeySet()
/*      */   {
/*  953 */     Set keys = keySet();
/*  954 */     System.out.println("printing keyset:");
/*  955 */     for (Iterator i = keys.iterator(); i.hasNext();)
/*      */     {
/*  957 */       System.out.println(i.next());
/*      */     }
/*      */   }
/*      */   
/*      */   private static void arrayPrintDouble(double[] o)
/*      */   {
/*  963 */     int i = 0; for (int n = o.length; i < n; i++) {
/*  964 */       System.out.print(o[i] + "\t");
/*      */     }
/*  966 */     System.out.println();
/*      */   }
/*      */   
/*      */   private Set dumpKeys() {
/*  970 */     return this.map.keySet();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void prettyPrint()
/*      */   {
/*  977 */     prettyPrint(new PrintWriter(System.out, true));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void prettyPrint(PrintWriter pw)
/*      */   {
/*  984 */     prettyPrint(pw, "  ");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  991 */   public void prettyPrint(PrintWriter pw, String bufferIncrement) { prettyPrint(pw, "", bufferIncrement); }
/*      */   
/*      */   private void prettyPrint(PrintWriter pw, String buffer, String bufferIncrement) { Iterator i;
/*      */     Iterator i;
/*  995 */     if (this.depth == 1) {
/*  996 */       for (i = entrySet().iterator(); i.hasNext();) {
/*  997 */         Map.Entry e = (Map.Entry)i.next();
/*  998 */         Object key = e.getKey();
/*  999 */         double count = ((Double)e.getValue()).doubleValue();
/* 1000 */         pw.println(buffer + key + "\t" + count);
/*      */       }
/*      */     } else {
/* 1003 */       for (i = topLevelKeySet().iterator(); i.hasNext();) {
/* 1004 */         Object key = i.next();
/* 1005 */         GeneralizedCounter gc1 = conditionalize(Arrays.asList(new Object[] { key }));
/* 1006 */         pw.println(buffer + key + "\t" + gc1.totalCount());
/* 1007 */         gc1.prettyPrint(pw, buffer + bufferIncrement, bufferIncrement);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\stats\GeneralizedCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */