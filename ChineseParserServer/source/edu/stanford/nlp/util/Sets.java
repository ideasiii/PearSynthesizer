/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Sets
/*    */ {
/*    */   public static <E, F> Set<Pair<E, F>> cross(Set<E> s1, Set<F> s2)
/*    */   {
/* 23 */     Set<Pair<E, F>> s = new HashSet();
/* 24 */     for (Iterator i$ = s1.iterator(); i$.hasNext();) { o1 = i$.next();
/* 25 */       for (F o2 : s2)
/* 26 */         s.add(new Pair(o1, o2));
/*    */     }
/*    */     E o1;
/* 29 */     return s;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static <E> Set<E> diff(Set<E> s1, Set<E> s2)
/*    */   {
/* 36 */     Set<E> s = new HashSet();
/* 37 */     for (E o : s1) {
/* 38 */       if (!s2.contains(o)) {
/* 39 */         s.add(o);
/*    */       }
/*    */     }
/* 42 */     return s;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static <E> Set<E> union(Set<E> s1, Set<E> s2)
/*    */   {
/* 49 */     Set<E> s = new HashSet();
/* 50 */     s.addAll(s1);
/* 51 */     s.addAll(s2);
/* 52 */     return s;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static <E> Set<E> intersection(Set<E> s1, Set<E> s2)
/*    */   {
/* 59 */     Set<E> s = new HashSet();
/* 60 */     s.addAll(s1);
/* 61 */     s.retainAll(s2);
/* 62 */     return s;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static <E> Set<Set<E>> powerSet(Set<E> s)
/*    */   {
/* 69 */     if (s.isEmpty()) {
/* 70 */       Set<Set<E>> h = new HashSet();
/* 71 */       Set<E> h0 = new HashSet(0);
/* 72 */       h.add(h0);
/* 73 */       return h;
/*    */     }
/* 75 */     Iterator<E> i = s.iterator();
/* 76 */     E elt = i.next();
/* 77 */     s.remove(elt);
/* 78 */     Set<Set<E>> pow = powerSet(s);
/* 79 */     Set<Set<E>> pow1 = powerSet(s);
/*    */     
/* 81 */     for (Set<E> t : pow1)
/*    */     {
/* 83 */       t.add(elt);
/* 84 */       pow.add(t);
/*    */     }
/* 86 */     s.add(elt);
/* 87 */     return pow;
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 92 */     Set<String> h = new HashSet();
/* 93 */     h.add("a");
/* 94 */     h.add("b");
/* 95 */     h.add("c");
/* 96 */     Set<Set<String>> pow = powerSet(h);
/* 97 */     System.out.println(pow);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Sets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */