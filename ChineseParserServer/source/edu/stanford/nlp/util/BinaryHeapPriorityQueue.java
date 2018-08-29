/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class BinaryHeapPriorityQueue<E> extends AbstractSet<E> implements PriorityQueue<E>, Iterator<E>
/*     */ {
/*     */   private List<Entry<E>> indexToEntry;
/*     */   private Map<Object, Entry<E>> keyToEntry;
/*     */   
/*     */   private static final class Entry<E>
/*     */   {
/*     */     public E key;
/*     */     public int index;
/*     */     public double priority;
/*     */     
/*     */     public String toString()
/*     */     {
/*  25 */       return this.key + " at " + this.index + " (" + this.priority + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/*  30 */     return size() > 0;
/*     */   }
/*     */   
/*     */   public E next() {
/*  34 */     return (E)removeFirst();
/*     */   }
/*     */   
/*     */   public void remove() {
/*  38 */     throw new UnsupportedOperationException();
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
/*     */   private Entry<E> parent(Entry<E> entry)
/*     */   {
/*  54 */     int index = entry.index;
/*  55 */     return index > 0 ? getEntry((index - 1) / 2) : null;
/*     */   }
/*     */   
/*     */   private Entry<E> leftChild(Entry<E> entry) {
/*  59 */     int leftIndex = entry.index * 2 + 1;
/*  60 */     return leftIndex < size() ? getEntry(leftIndex) : null;
/*     */   }
/*     */   
/*     */   private Entry<E> rightChild(Entry<E> entry) {
/*  64 */     int index = entry.index;
/*  65 */     int rightIndex = index * 2 + 2;
/*  66 */     return rightIndex < size() ? getEntry(rightIndex) : null;
/*     */   }
/*     */   
/*     */   private int compare(Entry<E> entryA, Entry<E> entryB) {
/*  70 */     return compare(entryA.priority, entryB.priority);
/*     */   }
/*     */   
/*     */   private int compare(double a, double b) {
/*  74 */     double diff = a - b;
/*  75 */     if (diff > 0.0D) {
/*  76 */       return 1;
/*     */     }
/*  78 */     if (diff < 0.0D) {
/*  79 */       return -1;
/*     */     }
/*  81 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void swap(Entry<E> entryA, Entry<E> entryB)
/*     */   {
/*  91 */     int indexA = entryA.index;
/*  92 */     int indexB = entryB.index;
/*  93 */     entryA.index = indexB;
/*  94 */     entryB.index = indexA;
/*  95 */     this.indexToEntry.set(indexA, entryB);
/*  96 */     this.indexToEntry.set(indexB, entryA);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void removeLastEntry()
/*     */   {
/* 103 */     Entry<E> entry = (Entry)this.indexToEntry.remove(size() - 1);
/* 104 */     this.keyToEntry.remove(entry.key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Entry<E> getEntry(E key)
/*     */   {
/* 111 */     return (Entry)this.keyToEntry.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Entry<E> getEntry(int index)
/*     */   {
/* 118 */     Entry<E> entry = (Entry)this.indexToEntry.get(index);
/* 119 */     return entry;
/*     */   }
/*     */   
/*     */   private Entry<E> makeEntry(E key) {
/* 123 */     Entry<E> entry = new Entry(null);
/* 124 */     entry.index = size();
/* 125 */     entry.key = key;
/* 126 */     entry.priority = Double.NEGATIVE_INFINITY;
/* 127 */     this.indexToEntry.add(entry);
/* 128 */     this.keyToEntry.put(key, entry);
/* 129 */     return entry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void heapifyUp(Entry<E> entry)
/*     */   {
/* 137 */     while (entry.index != 0)
/*     */     {
/*     */ 
/* 140 */       Entry<E> parentEntry = parent(entry);
/* 141 */       if (compare(entry, parentEntry) <= 0) {
/*     */         break;
/*     */       }
/* 144 */       swap(entry, parentEntry);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void heapifyDown(Entry<E> entry)
/*     */   {
/* 156 */     Entry<E> currentEntry = entry;
/* 157 */     Entry<E> bestEntry = null;
/*     */     do
/*     */     {
/* 160 */       bestEntry = currentEntry;
/*     */       
/* 162 */       Entry<E> leftEntry = leftChild(currentEntry);
/* 163 */       if ((leftEntry != null) && 
/* 164 */         (compare(bestEntry, leftEntry) < 0)) {
/* 165 */         bestEntry = leftEntry;
/*     */       }
/*     */       
/*     */ 
/* 169 */       Entry<E> rightEntry = rightChild(currentEntry);
/* 170 */       if ((rightEntry != null) && 
/* 171 */         (compare(bestEntry, rightEntry) < 0)) {
/* 172 */         bestEntry = rightEntry;
/*     */       }
/*     */       
/*     */ 
/* 176 */       if (bestEntry != currentEntry)
/*     */       {
/* 178 */         swap(bestEntry, currentEntry);
/*     */       }
/*     */       
/*     */     }
/* 182 */     while (bestEntry != currentEntry);
/*     */   }
/*     */   
/*     */ 
/*     */   private void heapify(Entry<E> entry)
/*     */   {
/* 188 */     heapifyUp(entry);
/* 189 */     heapifyDown(entry);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E removeFirst()
/*     */   {
/* 200 */     E first = getFirst();
/* 201 */     remove(first);
/* 202 */     return first;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E getFirst()
/*     */   {
/* 212 */     if (isEmpty()) {
/* 213 */       throw new NoSuchElementException();
/*     */     }
/* 215 */     return (E)getEntry(0).key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getPriority()
/*     */   {
/* 222 */     if (isEmpty()) {
/* 223 */       throw new NoSuchElementException();
/*     */     }
/* 225 */     return getEntry(0).priority;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E getObject(E key)
/*     */   {
/* 237 */     if (!contains(key)) return null;
/* 238 */     Entry<E> e = getEntry(key);
/* 239 */     return (E)e.key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getPriority(E key)
/*     */   {
/* 249 */     Entry entry = getEntry(key);
/* 250 */     if (entry == null) {
/* 251 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/* 253 */     return entry.priority;
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
/*     */   public boolean add(E key)
/*     */   {
/* 267 */     if (contains(key)) {
/* 268 */       return false;
/*     */     }
/* 270 */     makeEntry(key);
/* 271 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean add(E key, double priority)
/*     */   {
/* 279 */     if (add(key)) {
/* 280 */       relaxPriority(key, priority);
/* 281 */       return true;
/*     */     }
/* 283 */     return false;
/*     */   }
/*     */   
/*     */   public boolean remove(Object key)
/*     */   {
/* 288 */     E eKey = (E)key;
/* 289 */     Entry<E> entry = getEntry(eKey);
/* 290 */     if (entry == null) {
/* 291 */       return false;
/*     */     }
/* 293 */     removeEntry(entry);
/* 294 */     return true;
/*     */   }
/*     */   
/*     */   private void removeEntry(Entry<E> entry) {
/* 298 */     Entry<E> lastEntry = getLastEntry();
/* 299 */     if (entry != lastEntry) {
/* 300 */       swap(entry, lastEntry);
/* 301 */       removeLastEntry();
/* 302 */       heapify(lastEntry);
/*     */     } else {
/* 304 */       removeLastEntry();
/*     */     }
/*     */   }
/*     */   
/*     */   private Entry<E> getLastEntry()
/*     */   {
/* 310 */     return getEntry(size() - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean relaxPriority(E key, double priority)
/*     */   {
/* 320 */     Entry<E> entry = getEntry(key);
/* 321 */     if (entry == null) {
/* 322 */       entry = makeEntry(key);
/*     */     }
/* 324 */     if (compare(priority, entry.priority) <= 0) {
/* 325 */       return false;
/*     */     }
/* 327 */     entry.priority = priority;
/* 328 */     heapifyUp(entry);
/* 329 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean decreasePriority(E key, double priority)
/*     */   {
/* 339 */     Entry<E> entry = getEntry(key);
/* 340 */     if (entry == null) {
/* 341 */       entry = makeEntry(key);
/*     */     }
/* 343 */     if (compare(priority, entry.priority) >= 0) {
/* 344 */       return false;
/*     */     }
/* 346 */     entry.priority = priority;
/* 347 */     heapifyDown(entry);
/* 348 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean changePriority(E key, double priority)
/*     */   {
/* 358 */     Entry<E> entry = getEntry(key);
/* 359 */     if (entry == null) {
/* 360 */       entry = makeEntry(key);
/*     */     }
/* 362 */     if (compare(priority, entry.priority) == 0) {
/* 363 */       return false;
/*     */     }
/* 365 */     entry.priority = priority;
/* 366 */     heapify(entry);
/* 367 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 376 */     return this.indexToEntry.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 385 */     return this.indexToEntry.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean contains(Object key)
/*     */   {
/* 392 */     return this.keyToEntry.containsKey(key);
/*     */   }
/*     */   
/*     */   public List<E> toSortedList() {
/* 396 */     List<E> sortedList = new ArrayList(size());
/* 397 */     BinaryHeapPriorityQueue<E> queue = deepCopy();
/* 398 */     while (!queue.isEmpty()) {
/* 399 */       sortedList.add(queue.removeFirst());
/*     */     }
/* 401 */     return sortedList;
/*     */   }
/*     */   
/*     */   public BinaryHeapPriorityQueue<E> deepCopy(MapFactory mapFactory) {
/* 405 */     BinaryHeapPriorityQueue<E> queue = new BinaryHeapPriorityQueue(mapFactory);
/*     */     
/* 407 */     for (Entry<E> entry : this.keyToEntry.values()) {
/* 408 */       queue.relaxPriority(entry.key, entry.priority);
/*     */     }
/* 410 */     return queue;
/*     */   }
/*     */   
/*     */   public BinaryHeapPriorityQueue<E> deepCopy() {
/* 414 */     return deepCopy(MapFactory.HASH_MAP_FACTORY);
/*     */   }
/*     */   
/*     */   public Iterator<E> iterator() {
/* 418 */     return java.util.Collections.unmodifiableCollection(toSortedList()).iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 425 */     this.indexToEntry.clear();
/* 426 */     this.keyToEntry.clear();
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
/*     */   public String toString()
/*     */   {
/* 445 */     return toString(0);
/*     */   }
/*     */   
/*     */   public String toString(int maxKeysToPrint) {
/* 449 */     if (maxKeysToPrint <= 0) maxKeysToPrint = Integer.MAX_VALUE;
/* 450 */     List<E> sortedKeys = toSortedList();
/* 451 */     StringBuilder sb = new StringBuilder("[");
/* 452 */     for (int i = 0; (i < maxKeysToPrint) && (i < sortedKeys.size()); i++) {
/* 453 */       E key = sortedKeys.get(i);
/* 454 */       sb.append(key).append("=").append(getPriority(key));
/* 455 */       if ((i < maxKeysToPrint - 1) && (i < sortedKeys.size() - 1)) {
/* 456 */         sb.append(", ");
/*     */       }
/*     */     }
/* 459 */     sb.append("]");
/* 460 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String toVerticalString() {
/* 464 */     List<E> sortedKeys = toSortedList();
/* 465 */     StringBuilder sb = new StringBuilder();
/* 466 */     for (Iterator<E> keyI = sortedKeys.iterator(); keyI.hasNext();) {
/* 467 */       E key = keyI.next();
/* 468 */       sb.append(key);
/* 469 */       sb.append("\t");
/* 470 */       sb.append(getPriority(key));
/* 471 */       if (keyI.hasNext()) {
/* 472 */         sb.append("\n");
/*     */       }
/*     */     }
/* 475 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public BinaryHeapPriorityQueue()
/*     */   {
/* 480 */     this(MapFactory.HASH_MAP_FACTORY);
/*     */   }
/*     */   
/*     */   public BinaryHeapPriorityQueue(MapFactory mapFactory) {
/* 484 */     this.indexToEntry = new ArrayList();
/* 485 */     this.keyToEntry = mapFactory.newMap();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 489 */     BinaryHeapPriorityQueue<String> queue = new BinaryHeapPriorityQueue();
/*     */     
/* 491 */     queue.add("a", 1.0D);
/* 492 */     System.out.println("Added a:1 " + queue);
/* 493 */     queue.add("b", 2.0D);
/* 494 */     System.out.println("Added b:2 " + queue);
/* 495 */     queue.add("c", 1.5D);
/* 496 */     System.out.println("Added c:1.5 " + queue);
/* 497 */     queue.relaxPriority("a", 3.0D);
/* 498 */     System.out.println("Increased a to 3 " + queue);
/* 499 */     queue.decreasePriority("b", 0.0D);
/* 500 */     System.out.println("Decreased b to 0 " + queue);
/* 501 */     System.out.println("removeFirst()=" + (String)queue.removeFirst());
/* 502 */     System.out.println("queue=" + queue);
/* 503 */     System.out.println("removeFirst()=" + (String)queue.removeFirst());
/* 504 */     System.out.println("queue=" + queue);
/* 505 */     System.out.println("removeFirst()=" + (String)queue.removeFirst());
/* 506 */     System.out.println("queue=" + queue);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\BinaryHeapPriorityQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */