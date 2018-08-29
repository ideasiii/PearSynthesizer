/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdentityHashSet<E>
/*     */   extends AbstractSet<E>
/*     */   implements Set<E>, Cloneable, Serializable
/*     */ {
/*     */   private transient IdentityHashMap map;
/*     */   static final long serialVersionUID = -5024744406713321676L;
/*     */   
/*     */   public IdentityHashSet()
/*     */   {
/*  40 */     this.map = new IdentityHashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IdentityHashSet(int expectedMaxSize)
/*     */   {
/*  51 */     this.map = new IdentityHashMap(expectedMaxSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IdentityHashSet(Collection<? extends E> c)
/*     */   {
/*  62 */     this.map = new IdentityHashMap();
/*  63 */     addAll(c);
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
/*     */   public boolean add(E o)
/*     */   {
/*  80 */     if (this.map.containsKey(o)) {
/*  81 */       return false;
/*     */     }
/*  83 */     internalAdd(o);
/*  84 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/*  91 */     this.map.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 100 */     Iterator<E> it = iterator();
/* 101 */     IdentityHashSet<E> clone = new IdentityHashSet(size() * 2);
/* 102 */     while (it.hasNext()) {
/* 103 */       clone.internalAdd(it.next());
/*     */     }
/* 105 */     return clone;
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
/*     */   public boolean contains(Object o)
/*     */   {
/* 120 */     return this.map.containsKey(o);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 128 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 137 */     return this.map.keySet().iterator();
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
/*     */   public boolean remove(Object o)
/*     */   {
/* 151 */     return this.map.remove(o) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 159 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/* 164 */     Integer x = new Integer(3);
/* 165 */     Integer y = new Integer(4);
/* 166 */     Integer z = new Integer(5);
/* 167 */     List a = Arrays.asList(new Integer[] { x, y, z });
/* 168 */     List b = Arrays.asList(new String[] { "Larry", "Moe", "Curly" });
/* 169 */     List c = Arrays.asList(new Integer[] { x, y, z });
/* 170 */     List d = Arrays.asList(new String[] { "Larry", "Moe", "Curly" });
/* 171 */     HashSet hs = new HashSet();
/* 172 */     IdentityHashSet ihs = new IdentityHashSet();
/* 173 */     hs.add(a);
/* 174 */     hs.add(b);
/* 175 */     ihs.add(a);
/* 176 */     ihs.add(b);
/* 177 */     System.out.println("List a is " + a);
/* 178 */     System.out.println("List b is " + b);
/* 179 */     System.out.println("List c is " + c);
/* 180 */     System.out.println("List d is " + d);
/* 181 */     System.out.println("HashSet hs contains a and b: " + hs);
/* 182 */     System.out.println("IdentityHashSet ihs contains a and b: " + ihs);
/* 183 */     System.out.println("hs contains a? " + hs.contains(a));
/* 184 */     System.out.println("hs contains b? " + hs.contains(b));
/* 185 */     System.out.println("hs contains c? " + hs.contains(c));
/* 186 */     System.out.println("hs contains d? " + hs.contains(d));
/* 187 */     System.out.println("ihs contains a? " + ihs.contains(a));
/* 188 */     System.out.println("ihs contains b? " + ihs.contains(b));
/* 189 */     System.out.println("ihs contains c? " + ihs.contains(c));
/* 190 */     System.out.println("ihs contains d? " + ihs.contains(d));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void internalAdd(Object o)
/*     */   {
/* 202 */     this.map.put(o, Boolean.TRUE);
/*     */   }
/*     */   
/*     */ 
/*     */   private void writeObject(ObjectOutputStream s)
/*     */     throws IOException
/*     */   {
/* 209 */     Iterator it = iterator();
/* 210 */     s.writeInt(size() * 2);
/* 211 */     s.writeInt(size());
/* 212 */     while (it.hasNext()) {
/* 213 */       s.writeObject(it.next());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readObject(ObjectInputStream s)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 224 */     int expectedMaxSize = s.readInt();
/* 225 */     int size = s.readInt();
/*     */     
/* 227 */     this.map = new IdentityHashMap(expectedMaxSize);
/* 228 */     for (int i = 0; i < size; i++) {
/* 229 */       Object o = s.readObject();
/* 230 */       internalAdd(o);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\IdentityHashSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */