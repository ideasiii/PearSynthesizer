/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Beam<T>
/*    */   extends AbstractSet<T>
/*    */ {
/*    */   protected int maxBeamSize;
/*    */   protected Heap<T> elements;
/*    */   
/*    */   public int capacity()
/*    */   {
/* 22 */     return this.maxBeamSize;
/*    */   }
/*    */   
/*    */   public int size() {
/* 26 */     return this.elements.size();
/*    */   }
/*    */   
/*    */   public Iterator<T> iterator() {
/* 30 */     return asSortedList().iterator();
/*    */   }
/*    */   
/*    */   public List<T> asSortedList() {
/* 34 */     LinkedList<T> list = new LinkedList();
/* 35 */     for (Iterator<T> i = this.elements.iterator(); i.hasNext();) {
/* 36 */       list.addFirst(i.next());
/*    */     }
/* 38 */     return list;
/*    */   }
/*    */   
/*    */   public boolean add(T o) {
/* 42 */     boolean added = true;
/* 43 */     this.elements.add(o);
/* 44 */     while (size() > capacity()) {
/* 45 */       Object dumped = this.elements.extractMin();
/* 46 */       if (dumped.equals(o)) {
/* 47 */         added = false;
/*    */       }
/*    */     }
/* 50 */     return added;
/*    */   }
/*    */   
/*    */   public boolean remove(Object o)
/*    */   {
/* 55 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public Beam() {
/* 59 */     this(100);
/*    */   }
/*    */   
/*    */   public Beam(int maxBeamSize) {
/* 63 */     this(maxBeamSize, ScoredComparator.ASCENDING_COMPARATOR);
/*    */   }
/*    */   
/*    */   public Beam(int maxBeamSize, Comparator cmp) {
/* 67 */     this.elements = new ArrayHeap(cmp);
/* 68 */     this.maxBeamSize = maxBeamSize;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 72 */     Beam<ScoredObject> b = new Beam(2, ScoredComparator.ASCENDING_COMPARATOR);
/* 73 */     b.add(new ScoredObject("1", 1.0D));
/* 74 */     b.add(new ScoredObject("2", 2.0D));
/* 75 */     b.add(new ScoredObject("3", 3.0D));
/* 76 */     b.add(new ScoredObject("0", 0.0D));
/* 77 */     for (Iterator<ScoredObject> bI = b.iterator(); bI.hasNext();) {
/* 78 */       ScoredObject sO = (ScoredObject)bI.next();
/* 79 */       System.out.println(sO);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Beam.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */