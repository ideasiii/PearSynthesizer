/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
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
/*     */ public class Interner<T>
/*     */ {
/*  30 */   protected static Interner interner = new Interner();
/*     */   
/*     */ 
/*     */ 
/*     */   public static Interner getGlobal()
/*     */   {
/*  36 */     return interner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Interner setGlobal(Interner interner)
/*     */   {
/*  44 */     Interner oldInterner = interner;
/*  45 */     interner = interner;
/*  46 */     return oldInterner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object globalIntern(Object o)
/*     */   {
/*  55 */     return getGlobal().intern(o);
/*     */   }
/*     */   
/*     */ 
/*  59 */   protected Map<T, T> map = new WeakHashMap();
/*     */   
/*  61 */   public void clear() { this.map = new WeakHashMap(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T intern(T o)
/*     */   {
/*  69 */     T i = this.map.get(o);
/*  70 */     if (i == null) {
/*  71 */       i = o;
/*  72 */       this.map.put(i, i);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  77 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<T> internAll(Set<T> s)
/*     */   {
/*  86 */     Set<T> result = new HashSet();
/*  87 */     for (T o : s) {
/*  88 */       result.add(intern(o));
/*     */     }
/*  90 */     return result;
/*     */   }
/*     */   
/*     */   public int size() {
/*  94 */     return this.map.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 101 */     for (int i = 0; i < args.length; i++) {
/* 102 */       String str = args[i];
/* 103 */       System.out.println(globalIntern(str) == str);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Interner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */