/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class Filters
/*     */ {
/*     */   public static <T> Filter<T> acceptFilter()
/*     */   {
/*  25 */     return new CategoricalFilter(true, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <T> Filter<T> rejectFilter()
/*     */   {
/*  32 */     return new CategoricalFilter(false, null);
/*     */   }
/*     */   
/*     */   private static final class CategoricalFilter<T> implements Filter<T>
/*     */   {
/*     */     private final boolean judgment;
/*     */     
/*     */     private CategoricalFilter(boolean judgment) {
/*  40 */       this.judgment = judgment;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean accept(T obj)
/*     */     {
/*  49 */       return this.judgment;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Filter<E> collectionAcceptFilter(E[] objs)
/*     */   {
/*  58 */     return new CollectionAcceptFilter(Arrays.asList(objs), true, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> Filter<E> collectionAcceptFilter(Collection<E> objs)
/*     */   {
/*  65 */     return new CollectionAcceptFilter(objs, true, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> Filter collectionRejectFilter(E[] objs)
/*     */   {
/*  72 */     return new CollectionAcceptFilter(Arrays.asList(objs), false, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> Filter<E> collectionRejectFilter(Collection<E> objs)
/*     */   {
/*  79 */     return new CollectionAcceptFilter(objs, false, null);
/*     */   }
/*     */   
/*     */   private static final class CollectionAcceptFilter<E> implements Filter<E>, Serializable {
/*     */     private final Collection<E> args;
/*     */     private final boolean judgment;
/*     */     private static final long serialVersionUID = -8870550963937943540L;
/*     */     
/*     */     private CollectionAcceptFilter(Collection<E> c, boolean judgment) {
/*  88 */       this.args = new HashSet(c);
/*  89 */       this.judgment = judgment;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean accept(E obj)
/*     */     {
/*  98 */       if (this.args.contains(obj)) {
/*  99 */         return this.judgment;
/*     */       }
/* 101 */       return !this.judgment;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Filter andFilter(Filter<E> f1, Filter<E> f2)
/*     */   {
/* 113 */     return new CombinedFilter(f1, f2, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> Filter orFilter(Filter<E> f1, Filter<E> f2)
/*     */   {
/* 120 */     return new CombinedFilter(f1, f2, false);
/*     */   }
/*     */   
/*     */   private static class CombinedFilter<E> implements Filter<E>
/*     */   {
/*     */     private Filter<E> f1;
/*     */     private Filter<E> f2;
/*     */     private boolean conjunction;
/*     */     
/*     */     public CombinedFilter(Filter<E> f1, Filter<E> f2, boolean conjunction)
/*     */     {
/* 131 */       this.f1 = f1;
/* 132 */       this.f2 = f2;
/* 133 */       this.conjunction = conjunction;
/*     */     }
/*     */     
/*     */     public boolean accept(E o) {
/* 137 */       if (this.conjunction) {
/* 138 */         return (this.f1.accept(o)) && (this.f2.accept(o));
/*     */       }
/* 140 */       return (this.f1.accept(o)) || (this.f2.accept(o));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> Filter<E> notFilter(Filter<E> filter)
/*     */   {
/* 148 */     return new NegatedFilter(filter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> Filter<E> switchedFilter(Filter<E> filter, boolean negated)
/*     */   {
/* 155 */     return new NegatedFilter(filter, negated);
/*     */   }
/*     */   
/*     */   private static class NegatedFilter<E>
/*     */     implements Filter<E>
/*     */   {
/*     */     private Filter<E> filter;
/*     */     private boolean negated;
/*     */     
/*     */     public NegatedFilter(Filter<E> filter, boolean negated)
/*     */     {
/* 166 */       this.filter = filter;
/* 167 */       this.negated = negated;
/*     */     }
/*     */     
/*     */     public NegatedFilter(Filter<E> filter) {
/* 171 */       this(filter, true);
/*     */     }
/*     */     
/*     */     public boolean accept(E o) {
/* 175 */       return this.negated ^ this.filter.accept(o);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Object[] filter(E[] elems, Filter<E> filter)
/*     */   {
/* 185 */     List<E> filtered = new ArrayList();
/* 186 */     for (E elem : elems) {
/* 187 */       if (filter.accept(elem)) {
/* 188 */         filtered.add(elem);
/*     */       }
/*     */     }
/* 191 */     return filtered.toArray((Object[])Array.newInstance(elems.getClass().getComponentType(), filtered.size()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> void retainAll(Collection<E> elems, Filter<E> filter)
/*     */   {
/* 198 */     for (Iterator<E> iter = elems.iterator(); iter.hasNext();) {
/* 199 */       E elem = iter.next();
/* 200 */       if (!filter.accept(elem)) {
/* 201 */         iter.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Filters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */