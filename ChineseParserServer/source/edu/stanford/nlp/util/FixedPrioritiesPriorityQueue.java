/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedPrioritiesPriorityQueue<E>
/*     */   extends AbstractSet<E>
/*     */   implements PriorityQueue<E>, Iterator<E>, Serializable, Cloneable
/*     */ {
/*     */   private int size;
/*     */   private int capacity;
/*     */   private List<E> elements;
/*     */   private double[] priorities;
/*     */   
/*     */   public FixedPrioritiesPriorityQueue()
/*     */   {
/*  30 */     this(15);
/*     */   }
/*     */   
/*     */   public FixedPrioritiesPriorityQueue(int capacity) {
/*  34 */     int legalCapacity = 0;
/*  35 */     while (legalCapacity < capacity) {
/*  36 */       legalCapacity = 2 * legalCapacity + 1;
/*     */     }
/*  38 */     grow(legalCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*  48 */     return !isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public E next()
/*     */   {
/*  56 */     return (E)removeFirst();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void remove()
/*     */   {
/*  63 */     throw new UnsupportedOperationException();
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
/*     */   public boolean add(E key, double priority)
/*     */   {
/*  77 */     if (this.size == this.capacity) {
/*  78 */       grow(2 * this.capacity + 1);
/*     */     }
/*  80 */     this.elements.add(key);
/*  81 */     this.priorities[this.size] = priority;
/*  82 */     heapifyUp(this.size);
/*  83 */     this.size += 1;
/*  84 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean changePriority(E key, double priority)
/*     */   {
/*  91 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public E getFirst()
/*     */   {
/*  99 */     if (size() > 0)
/* 100 */       return (E)this.elements.get(0);
/* 101 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getPriority(Object key)
/*     */   {
/* 109 */     for (int i = 0; i < this.elements.size(); i++) {
/* 110 */       if (this.elements.get(i).equals(key)) {
/* 111 */         return this.priorities[i];
/*     */       }
/*     */     }
/* 114 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getPriority()
/*     */   {
/* 121 */     if (size() > 0)
/* 122 */       return this.priorities[0];
/* 123 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean relaxPriority(E key, double priority)
/*     */   {
/* 130 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public E removeFirst()
/*     */   {
/* 137 */     E first = getFirst();
/* 138 */     swap(0, this.size - 1);
/* 139 */     this.size -= 1;
/* 140 */     this.elements.remove(this.size);
/* 141 */     heapifyDown(0);
/* 142 */     return first;
/*     */   }
/*     */   
/*     */   public List<E> toSortedList() {
/* 146 */     List<E> list = new ArrayList();
/* 147 */     while (hasNext()) {
/* 148 */       list.add(next());
/*     */     }
/* 150 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 160 */     return this.size;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 164 */     this.size = 0;
/* 165 */     grow(15);
/*     */   }
/*     */   
/*     */   public Iterator<E> iterator() {
/* 169 */     return Collections.unmodifiableCollection(toSortedList()).iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void grow(int newCapacity)
/*     */   {
/* 176 */     List<E> newElements = new ArrayList(newCapacity);
/* 177 */     double[] newPriorities = new double[newCapacity];
/* 178 */     if (this.size > 0) {
/* 179 */       newElements.addAll(this.elements);
/* 180 */       System.arraycopy(this.priorities, 0, newPriorities, 0, this.priorities.length);
/*     */     }
/* 182 */     this.elements = newElements;
/* 183 */     this.priorities = newPriorities;
/* 184 */     this.capacity = newCapacity;
/*     */   }
/*     */   
/*     */   private int parent(int loc) {
/* 188 */     return (loc - 1) / 2;
/*     */   }
/*     */   
/*     */   private int leftChild(int loc) {
/* 192 */     return 2 * loc + 1;
/*     */   }
/*     */   
/*     */   private int rightChild(int loc) {
/* 196 */     return 2 * loc + 2;
/*     */   }
/*     */   
/*     */   private void heapifyUp(int loc) {
/* 200 */     if (loc == 0) return;
/* 201 */     int parent = parent(loc);
/* 202 */     if (this.priorities[loc] > this.priorities[parent]) {
/* 203 */       swap(loc, parent);
/* 204 */       heapifyUp(parent);
/*     */     }
/*     */   }
/*     */   
/*     */   private void heapifyDown(int loc) {
/* 209 */     int max = loc;
/* 210 */     int leftChild = leftChild(loc);
/* 211 */     if (leftChild < size()) {
/* 212 */       double priority = this.priorities[loc];
/* 213 */       double leftChildPriority = this.priorities[leftChild];
/* 214 */       if (leftChildPriority > priority)
/* 215 */         max = leftChild;
/* 216 */       int rightChild = rightChild(loc);
/* 217 */       if (rightChild < size()) {
/* 218 */         double rightChildPriority = this.priorities[rightChild(loc)];
/* 219 */         if ((rightChildPriority > priority) && (rightChildPriority > leftChildPriority))
/* 220 */           max = rightChild;
/*     */       }
/*     */     }
/* 223 */     if (max == loc)
/* 224 */       return;
/* 225 */     swap(loc, max);
/* 226 */     heapifyDown(max);
/*     */   }
/*     */   
/*     */   private void swap(int loc1, int loc2) {
/* 230 */     double tempPriority = this.priorities[loc1];
/* 231 */     E tempElement = this.elements.get(loc1);
/* 232 */     this.priorities[loc1] = this.priorities[loc2];
/* 233 */     this.elements.set(loc1, this.elements.get(loc2));
/* 234 */     this.priorities[loc2] = tempPriority;
/* 235 */     this.elements.set(loc2, tempElement);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 245 */     return toString(size());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString(int maxKeysToPrint)
/*     */   {
/* 255 */     FixedPrioritiesPriorityQueue<E> pq = clone();
/* 256 */     StringBuilder sb = new StringBuilder("[");
/* 257 */     int numKeysPrinted = 0;
/* 258 */     while ((numKeysPrinted < maxKeysToPrint) && (pq.hasNext())) {
/* 259 */       double priority = pq.getPriority();
/* 260 */       E element = pq.next();
/* 261 */       sb.append(element.toString());
/* 262 */       sb.append(" : ");
/* 263 */       sb.append(priority);
/* 264 */       if (numKeysPrinted < size() - 1)
/* 265 */         sb.append(", ");
/* 266 */       numKeysPrinted++;
/*     */     }
/* 268 */     if (numKeysPrinted < size())
/* 269 */       sb.append("...");
/* 270 */     sb.append("]");
/* 271 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FixedPrioritiesPriorityQueue<E> clone()
/*     */   {
/* 279 */     FixedPrioritiesPriorityQueue<E> clonePQ = new FixedPrioritiesPriorityQueue();
/* 280 */     clonePQ.size = this.size;
/* 281 */     clonePQ.capacity = this.capacity;
/* 282 */     clonePQ.elements = new ArrayList(this.capacity);
/* 283 */     clonePQ.priorities = new double[this.capacity];
/* 284 */     if (size() > 0) {
/* 285 */       clonePQ.elements.addAll(this.elements);
/* 286 */       System.arraycopy(this.priorities, 0, clonePQ.priorities, 0, size());
/*     */     }
/* 288 */     return clonePQ;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 295 */     FixedPrioritiesPriorityQueue<String> pq = new FixedPrioritiesPriorityQueue();
/* 296 */     System.out.println(pq);
/* 297 */     pq.add("one", 1.0D);
/* 298 */     System.out.println(pq);
/* 299 */     pq.add("three", 3.0D);
/* 300 */     System.out.println(pq);
/* 301 */     pq.add("one", 1.1D);
/* 302 */     System.out.println(pq);
/* 303 */     pq.add("two", 2.0D);
/* 304 */     System.out.println(pq);
/* 305 */     System.out.println(pq.toString(2));
/* 306 */     while (pq.hasNext()) {
/* 307 */       System.out.println((String)pq.next());
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\FixedPrioritiesPriorityQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */