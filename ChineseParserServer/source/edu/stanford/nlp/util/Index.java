/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.Writer;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Index<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Serializable, RandomAccess
/*     */ {
/*  32 */   List<E> objects = new ArrayList();
/*  33 */   Map<Object, Integer> indexes = new HashMap();
/*     */   
/*     */   boolean locked;
/*     */   private static final long serialVersionUID = 5398562825928375260L;
/*     */   
/*     */   public void clear()
/*     */   {
/*  40 */     this.objects.clear();
/*  41 */     this.indexes.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] indices(Collection<E> elems)
/*     */   {
/*  50 */     int[] indices = new int[elems.size()];
/*  51 */     int i = 0;
/*  52 */     for (E elem : elems) {
/*  53 */       indices[(i++)] = indexOf(elem);
/*     */     }
/*  55 */     return indices;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<E> objects(final int[] indices)
/*     */   {
/*  65 */     new AbstractList() {
/*     */       public E get(int index) {
/*  67 */         return (E)Index.this.objects.get(indices[index]);
/*     */       }
/*     */       
/*     */       public int size() {
/*  71 */         return indices.length;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/*  81 */     return this.objects.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public E get(int i)
/*     */   {
/*  90 */     return (E)this.objects.get(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<E> objectsList()
/*     */   {
/* 101 */     return this.objects;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLocked()
/*     */   {
/* 109 */     return this.locked;
/*     */   }
/*     */   
/*     */ 
/*     */   public void lock()
/*     */   {
/* 115 */     this.locked = true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void unlock()
/*     */   {
/* 121 */     this.locked = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int indexOf(E o)
/*     */   {
/* 130 */     return indexOf(o, false);
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
/*     */   public int indexOf(E o, boolean add)
/*     */   {
/* 145 */     Integer index = (Integer)this.indexes.get(o);
/* 146 */     if (index == null) {
/* 147 */       if (add) {
/* 148 */         add(o);
/* 149 */         index = (Integer)this.indexes.get(o);
/*     */       } else {
/* 151 */         return -1;
/*     */       }
/*     */     }
/* 154 */     return index.intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> c)
/*     */   {
/* 165 */     boolean changed = false;
/* 166 */     for (E element : c) {
/* 167 */       changed &= add(element);
/*     */     }
/* 169 */     return changed;
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
/* 181 */     Integer index = (Integer)this.indexes.get(o);
/* 182 */     if ((index == null) && (!this.locked)) {
/* 183 */       index = new Integer(this.objects.size());
/* 184 */       this.objects.add(o);
/* 185 */       this.indexes.put(o, index);
/*     */       
/* 187 */       return true;
/*     */     }
/* 189 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(Object o)
/*     */   {
/* 199 */     return this.indexes.containsKey(o);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Index() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Index(Collection<? extends E> c)
/*     */   {
/* 214 */     this();
/* 215 */     addAll(c);
/*     */   }
/*     */   
/*     */   public void saveToFilename(String file) {
/*     */     try {
/* 220 */       BufferedWriter bw = new BufferedWriter(new FileWriter(file));
/* 221 */       int i = 0; for (int sz = size(); i < sz; i++) {
/* 222 */         bw.write(i + "=" + get(i) + "\n");
/*     */       }
/* 224 */       bw.close();
/*     */     } catch (IOException e) {
/* 226 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static Index<String> loadFromFilename(String file) {
/* 231 */     Index<String> index = new Index();
/*     */     try {
/* 233 */       BufferedReader br = new BufferedReader(new FileReader(file));
/*     */       String line;
/* 235 */       while ((line = br.readLine()) != null) {
/* 236 */         int start = line.indexOf('=');
/* 237 */         if ((start != -1) && (start != line.length() - 1))
/*     */         {
/*     */ 
/* 240 */           index.add(line.substring(start + 1)); }
/*     */       }
/* 242 */       br.close();
/*     */     } catch (Exception e) {
/* 244 */       e.printStackTrace();
/*     */     }
/* 246 */     return index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void saveToWriter(Writer bw)
/*     */     throws IOException
/*     */   {
/* 259 */     int i = 0; for (int sz = size(); i < sz; i++) {
/* 260 */       bw.write(i + "=" + get(i) + "\n");
/*     */     }
/* 262 */     bw.write("\n");
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
/*     */   public static Index<String> loadFromReader(BufferedReader br)
/*     */     throws Exception
/*     */   {
/* 276 */     Index<String> index = new Index();
/* 277 */     String line = br.readLine();
/*     */     
/* 279 */     while ((line != null) && (line.length() > 0)) {
/* 280 */       int start = line.indexOf('=');
/* 281 */       if ((start != -1) && (start != line.length() - 1))
/*     */       {
/*     */ 
/* 284 */         index.add(line.substring(start + 1));
/* 285 */         line = br.readLine();
/*     */       } }
/* 287 */     return index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 295 */     return toString(Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString(int n)
/*     */   {
/* 304 */     StringBuilder buff = new StringBuilder("[");
/* 305 */     int sz = this.objects.size();
/* 306 */     if (n > sz) {
/* 307 */       n = sz;
/*     */     }
/*     */     
/* 310 */     for (int i = 0; i < n; i++) {
/* 311 */       E e = this.objects.get(i);
/* 312 */       buff.append(i).append("=").append(e);
/* 313 */       if (i < sz - 1) buff.append(",");
/*     */     }
/* 315 */     if (i < sz) buff.append("...");
/* 316 */     buff.append("]");
/* 317 */     return buff.toString();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 321 */     List<String> list = new ArrayList();
/* 322 */     list.add("A");
/* 323 */     list.add("B");
/* 324 */     list.add("A");
/* 325 */     list.add("C");
/* 326 */     Index<String> index = new Index(list);
/* 327 */     System.out.println("Index size: " + index.size());
/* 328 */     System.out.println("Index has A? : " + index.contains("A"));
/* 329 */     System.out.println("Index of A: " + index.indexOf("A"));
/* 330 */     System.out.println("Index of B: " + index.indexOf("B"));
/* 331 */     System.out.println("Index of C: " + index.indexOf("C"));
/* 332 */     System.out.println("Object 0: " + (String)index.get(0));
/* 333 */     index = index.unmodifiableView();
/* 334 */     System.out.println("Index size: " + index.size());
/* 335 */     System.out.println("Index has A? : " + index.contains("A"));
/* 336 */     System.out.println("Index of A: " + index.indexOf("A"));
/* 337 */     System.out.println("Index of B: " + index.indexOf("B"));
/* 338 */     System.out.println("Index of C: " + index.indexOf("C"));
/* 339 */     System.out.println("Object 0: " + (String)index.get(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 350 */     return this.objects.iterator();
/*     */   }
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
/* 362 */     Integer oldIndex = (Integer)this.indexes.remove(o);
/* 363 */     if (oldIndex == null) {
/* 364 */       return false;
/*     */     }
/* 366 */     this.objects.set(oldIndex.intValue(), null);
/* 367 */     return true;
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
/*     */   public Index<E> unmodifiableView()
/*     */   {
/* 383 */     Index<E> newIndex = new Index() {
/* 384 */       public void unlock() { throw new UnsupportedOperationException("This is an unmodifiable view!");
/*     */       }
/* 386 */     };
/* 387 */     newIndex.objects = this.objects;
/* 388 */     newIndex.indexes = this.indexes;
/* 389 */     newIndex.lock();
/* 390 */     return newIndex;
/*     */   }
/*     */   
/*     */   private static final long serialVersionUID = 3415903369787491736L;
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Index.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */