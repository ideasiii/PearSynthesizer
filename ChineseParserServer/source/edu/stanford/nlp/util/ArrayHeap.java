/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class ArrayHeap<E>
/*     */   extends AbstractSet<E>
/*     */   implements Heap<E>
/*     */ {
/*     */   private ArrayList<HeapEntry<E>> indexToEntry;
/*     */   private Map<E, HeapEntry<E>> objectToEntry;
/*     */   private Comparator<E> cmp;
/*     */   
/*     */   private int parent(int index)
/*     */   {
/*  53 */     return (index - 1) / 2;
/*     */   }
/*     */   
/*     */   private int leftChild(int index) {
/*  57 */     return index * 2 + 1;
/*     */   }
/*     */   
/*     */   private int rightChild(int index) {
/*  61 */     return index * 2 + 2;
/*     */   }
/*     */   
/*     */   private HeapEntry<E> parent(HeapEntry<E> entry) {
/*  65 */     int index = entry.index;
/*  66 */     return index > 0 ? (HeapEntry)this.indexToEntry.get((index - 1) / 2) : null;
/*     */   }
/*     */   
/*     */   private HeapEntry<E> leftChild(HeapEntry<E> entry) {
/*  70 */     int index = entry.index;
/*  71 */     int leftIndex = index * 2 + 1;
/*  72 */     return leftIndex < size() ? (HeapEntry)this.indexToEntry.get(leftIndex) : null;
/*     */   }
/*     */   
/*     */   private HeapEntry<E> rightChild(HeapEntry<E> entry) {
/*  76 */     int index = entry.index;
/*  77 */     int rightIndex = index * 2 + 2;
/*  78 */     return rightIndex < size() ? (HeapEntry)this.indexToEntry.get(rightIndex) : null;
/*     */   }
/*     */   
/*     */   private int compare(HeapEntry<E> entryA, HeapEntry<E> entryB) {
/*  82 */     return this.cmp.compare(entryA.object, entryB.object);
/*     */   }
/*     */   
/*     */   private void swap(HeapEntry<E> entryA, HeapEntry<E> entryB) {
/*  86 */     int indexA = entryA.index;
/*  87 */     int indexB = entryB.index;
/*  88 */     entryA.index = indexB;
/*  89 */     entryB.index = indexA;
/*  90 */     this.indexToEntry.set(indexA, entryB);
/*  91 */     this.indexToEntry.set(indexB, entryA);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeLast(HeapEntry<E> entry)
/*     */   {
/* 102 */     this.indexToEntry.remove(entry.index);
/* 103 */     this.objectToEntry.remove(entry.object);
/*     */   }
/*     */   
/*     */   private HeapEntry<E> getEntry(E o) {
/* 107 */     HeapEntry<E> entry = (HeapEntry)this.objectToEntry.get(o);
/* 108 */     if (entry == null) {
/* 109 */       entry = new HeapEntry(null);
/* 110 */       entry.index = size();
/* 111 */       entry.object = o;
/* 112 */       this.indexToEntry.add(entry);
/* 113 */       this.objectToEntry.put(o, entry);
/*     */     }
/* 115 */     return entry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int heapifyUp(HeapEntry<E> entry)
/*     */   {
/* 122 */     int numSwaps = 0;
/*     */     
/* 124 */     while (entry.index != 0)
/*     */     {
/*     */ 
/* 127 */       HeapEntry<E> parentEntry = parent(entry);
/* 128 */       if (compare(entry, parentEntry) >= 0) {
/*     */         break;
/*     */       }
/* 131 */       numSwaps++;
/* 132 */       swap(entry, parentEntry);
/*     */     }
/* 134 */     return numSwaps;
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
/*     */   private void heapifyDown(HeapEntry<E> entry)
/*     */   {
/* 147 */     HeapEntry<E> currentEntry = entry;
/*     */     HeapEntry<E> minEntry;
/*     */     do
/*     */     {
/* 151 */       minEntry = currentEntry;
/*     */       
/* 153 */       HeapEntry<E> leftEntry = leftChild(currentEntry);
/* 154 */       if ((leftEntry != null) && 
/* 155 */         (compare(minEntry, leftEntry) > 0)) {
/* 156 */         minEntry = leftEntry;
/*     */       }
/*     */       
/*     */ 
/* 160 */       HeapEntry<E> rightEntry = rightChild(currentEntry);
/* 161 */       if ((rightEntry != null) && 
/* 162 */         (compare(minEntry, rightEntry) > 0)) {
/* 163 */         minEntry = rightEntry;
/*     */       }
/*     */       
/*     */ 
/* 167 */       if (minEntry != currentEntry)
/*     */       {
/* 169 */         swap(minEntry, currentEntry);
/*     */       }
/*     */       
/*     */     }
/* 173 */     while (minEntry != currentEntry);
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
/*     */   public E extractMin()
/*     */   {
/* 186 */     if (isEmpty()) {
/* 187 */       throw new NoSuchElementException();
/*     */     }
/* 189 */     HeapEntry<E> minEntry = (HeapEntry)this.indexToEntry.get(0);
/* 190 */     int lastIndex = size() - 1;
/* 191 */     if (lastIndex > 0) {
/* 192 */       HeapEntry<E> lastEntry = (HeapEntry)this.indexToEntry.get(lastIndex);
/* 193 */       swap(lastEntry, minEntry);
/* 194 */       removeLast(minEntry);
/* 195 */       heapifyDown(lastEntry);
/*     */     } else {
/* 197 */       removeLast(minEntry);
/*     */     }
/* 199 */     return (E)minEntry.object;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E min()
/*     */   {
/* 209 */     HeapEntry<E> minEntry = (HeapEntry)this.indexToEntry.get(0);
/* 210 */     return (E)minEntry.object;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean add(E o)
/*     */   {
/* 222 */     decreaseKey(o);
/* 223 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int decreaseKey(E o)
/*     */   {
/* 235 */     HeapEntry<E> entry = getEntry(o);
/* 236 */     if ((o != entry.object) && 
/* 237 */       (this.cmp.compare(o, entry.object) < 0)) {
/* 238 */       entry.object = o;
/*     */     }
/*     */     
/* 241 */     return heapifyUp(entry);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 250 */     return this.indexToEntry.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 259 */     return this.indexToEntry.size();
/*     */   }
/*     */   
/*     */   public Iterator<E> iterator() {
/* 263 */     Heap<E> tempHeap = new ArrayHeap(this.cmp, size());
/* 264 */     List<E> tempList = new ArrayList(size());
/* 265 */     for (E obj : this.objectToEntry.keySet()) {
/* 266 */       tempHeap.add(obj);
/*     */     }
/* 268 */     while (!tempHeap.isEmpty()) {
/* 269 */       tempList.add(tempHeap.extractMin());
/*     */     }
/* 271 */     return tempList.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 279 */     this.indexToEntry.clear();
/* 280 */     this.objectToEntry.clear();
/*     */   }
/*     */   
/*     */   public void dump() {
/* 284 */     for (int j = 0; j < this.indexToEntry.size(); j++) {
/* 285 */       System.err.println(" " + j + " " + ((Scored)((HeapEntry)this.indexToEntry.get(j)).object).score());
/*     */     }
/*     */   }
/*     */   
/*     */   public void verify() {
/* 290 */     for (int i = 0; i < this.indexToEntry.size(); i++) {
/* 291 */       if (i != 0)
/*     */       {
/* 293 */         if (compare((HeapEntry)this.indexToEntry.get(i), (HeapEntry)this.indexToEntry.get(parent(i))) < 0) {
/* 294 */           System.err.println("Error in the ordering of the heap! (" + i + ")");
/* 295 */           dump();
/* 296 */           System.exit(0);
/*     */         }
/*     */       }
/*     */       
/* 300 */       if (i != ((HeapEntry)this.indexToEntry.get(i)).index) {
/* 301 */         System.err.println("Error in placement in the heap!");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayHeap(Comparator<E> cmp)
/*     */   {
/* 311 */     this.cmp = cmp;
/* 312 */     this.indexToEntry = new ArrayList();
/* 313 */     this.objectToEntry = new HashMap();
/*     */   }
/*     */   
/*     */   public ArrayHeap(Comparator<E> cmp, int initCapacity) {
/* 317 */     this.cmp = cmp;
/* 318 */     this.indexToEntry = new ArrayList(initCapacity);
/* 319 */     this.objectToEntry = new HashMap(initCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 327 */     ArrayList<E> result = new ArrayList();
/* 328 */     for (E key : this.objectToEntry.keySet())
/* 329 */       result.add(key);
/* 330 */     Collections.sort(result, this.cmp);
/* 331 */     return result.toString();
/*     */   }
/*     */   
/*     */   private static final class HeapEntry<E>
/*     */   {
/*     */     public E object;
/*     */     public int index;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\ArrayHeap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */