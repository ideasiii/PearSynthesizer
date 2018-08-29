/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
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
/*     */ public class CollectionUtils
/*     */ {
/*     */   public static boolean incrementCount(Map map, Object key, int delta)
/*     */   {
/*  37 */     boolean created = false;
/*  38 */     Integer count = (Integer)map.get(key);
/*  39 */     if (count == null) {
/*  40 */       count = new Integer(0);
/*  41 */       created = true;
/*     */     }
/*  43 */     map.put(key, new Integer(count.intValue() + delta));
/*  44 */     return created;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean incrementCount(Map map, Object key)
/*     */   {
/*  53 */     return incrementCount(map, key, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getString(List list, int index)
/*     */   {
/*  62 */     return (String)list.get(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Integer getInteger(List list, int index)
/*     */   {
/*  69 */     return (Integer)list.get(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int getInt(List list, int index)
/*     */   {
/*  76 */     return getInteger(list, index).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Double getDouble(List list, int index)
/*     */   {
/*  83 */     return (Double)list.get(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static double getdouble(List list, int index)
/*     */   {
/*  90 */     return getDouble(list, index).doubleValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean getBoolean(List list, int index)
/*     */   {
/*  97 */     return ((Boolean)list.get(index)).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getString(Map map, Object key)
/*     */   {
/* 106 */     return (String)map.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Integer getInteger(Map map, Object key)
/*     */   {
/* 113 */     return (Integer)map.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int getInt(Map list, Object key)
/*     */   {
/* 120 */     return getInteger(list, key).intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Double getDouble(Map map, Object key)
/*     */   {
/* 127 */     return (Double)map.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static double getdouble(Map map, Object key)
/*     */   {
/* 134 */     return getDouble(map, key).doubleValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean getBoolean(Map map, Object key)
/*     */   {
/* 141 */     return ((Boolean)map.get(key)).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public static List asList(int[] a)
/*     */   {
/* 147 */     List result = new ArrayList();
/* 148 */     for (int i = 0; i < a.length; i++) {
/* 149 */       result.add(new Integer(a[i]));
/*     */     }
/* 151 */     return result;
/*     */   }
/*     */   
/*     */   public static List asList(double[] a) {
/* 155 */     List result = new ArrayList();
/* 156 */     for (int i = 0; i < a.length; i++) {
/* 157 */       result.add(new Double(a[i]));
/*     */     }
/* 159 */     return result;
/*     */   }
/*     */   
/*     */   public static List asList(Object... args)
/*     */   {
/* 164 */     List result = new ArrayList();
/* 165 */     for (int i = 0; i < args.length; i++) {
/* 166 */       result.add(args[i]);
/*     */     }
/* 168 */     return result;
/*     */   }
/*     */   
/*     */   public static <T> List<T> makeList(T e)
/*     */   {
/* 173 */     List<T> s = new ArrayList();
/* 174 */     s.add(e);
/* 175 */     return s;
/*     */   }
/*     */   
/*     */   public static <T> List<T> makeList(T e1, T e2)
/*     */   {
/* 180 */     List<T> s = new ArrayList();
/* 181 */     s.add(e1);
/* 182 */     s.add(e2);
/* 183 */     return s;
/*     */   }
/*     */   
/*     */   public static <T> List<T> makeList(T e1, T e2, T e3)
/*     */   {
/* 188 */     List<T> s = new ArrayList();
/* 189 */     s.add(e1);
/* 190 */     s.add(e2);
/* 191 */     s.add(e3);
/* 192 */     return s;
/*     */   }
/*     */   
/*     */   public static Set asSet(Object[] o)
/*     */   {
/* 197 */     return new HashSet(Arrays.asList(o));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Collection loadCollection(String filename, Class c, CollectionFactory cf)
/*     */     throws Exception
/*     */   {
/* 209 */     return loadCollection(new File(filename), c, cf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Collection loadCollection(File file, Class c, CollectionFactory cf)
/*     */     throws Exception
/*     */   {
/* 217 */     Constructor m = c.getConstructor(new Class[] { Class.forName("java.lang.String") });
/* 218 */     Collection result = cf.newCollection();
/* 219 */     BufferedReader in = new BufferedReader(new FileReader(file));
/* 220 */     String line = in.readLine();
/* 221 */     while ((line != null) && (line.length() > 0)) {
/*     */       try {
/* 223 */         Object o = m.newInstance(new Object[] { line });
/* 224 */         result.add(o);
/*     */       } catch (Exception e) {
/* 226 */         System.err.println("Couldn't build object from line: " + line);
/* 227 */         e.printStackTrace();
/*     */       }
/* 229 */       line = in.readLine();
/*     */     }
/* 231 */     in.close();
/* 232 */     return result;
/*     */   }
/*     */   
/*     */   public static Map getMapFromString(String s, Class keyClass, Class valueClass, MapFactory mapFactory) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
/* 236 */     Constructor keyC = keyClass.getConstructor(new Class[] { Class.forName("java.lang.String") });
/* 237 */     Constructor valueC = valueClass.getConstructor(new Class[] { Class.forName("java.lang.String") });
/* 238 */     if (s.charAt(0) != '{')
/* 239 */       throw new RuntimeException("");
/* 240 */     s = s.substring(1);
/* 241 */     String[] fields = s.split("\\s+");
/* 242 */     Map m = mapFactory.newMap();
/*     */     
/* 244 */     for (int i = 0; i < fields.length; i++)
/*     */     {
/* 246 */       fields[i] = fields[i].substring(0, fields[i].length() - 1);
/* 247 */       String[] a = fields[i].split("=");
/* 248 */       Object key = keyC.newInstance(new Object[] { a[0] });
/*     */       Object value;
/* 250 */       Object value; if (a.length > 1) {
/* 251 */         value = valueC.newInstance(new Object[] { a[1] });
/*     */       } else {
/* 253 */         value = "";
/*     */       }
/* 255 */       m.put(key, value);
/*     */     }
/* 257 */     return m;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean containsObject(Collection c, Object o)
/*     */   {
/* 263 */     for (Object o1 : c) {
/* 264 */       if (o == o1) {
/* 265 */         return true;
/*     */       }
/*     */     }
/* 268 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean removeObject(List l, Object o)
/*     */   {
/* 280 */     int i = 0;
/* 281 */     for (Object o1 : l) {
/* 282 */       if (o == o1) {
/* 283 */         l.remove(i);
/* 284 */         return true;
/*     */       }
/*     */       
/* 287 */       i++;
/*     */     }
/* 289 */     return false;
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
/*     */   public static int getIndex(List l, Object o)
/*     */   {
/* 302 */     int i = 0;
/* 303 */     for (Object o1 : l) {
/* 304 */       if (o == o1) {
/* 305 */         return i;
/*     */       }
/* 307 */       i++;
/*     */     }
/* 309 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Collection<E> sampleWithoutReplacement(Collection<E> c, int n)
/*     */   {
/* 320 */     return sampleWithoutReplacement(c, n, new Random());
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
/*     */   public static <E> Collection<E> sampleWithoutReplacement(Collection<E> c, int n, Random r)
/*     */   {
/* 333 */     if (n < 0)
/* 334 */       throw new IllegalArgumentException("n < 0: " + n);
/* 335 */     if (n > c.size())
/* 336 */       throw new IllegalArgumentException("n > size of collection: " + n + ", " + c.size());
/* 337 */     List<E> copy = new ArrayList(c.size());
/* 338 */     copy.addAll(c);
/* 339 */     Collection<E> result = new ArrayList(n);
/* 340 */     for (int k = 0; k < n; k++) {
/* 341 */       double d = r.nextDouble();
/* 342 */       int x = (int)(d * copy.size());
/* 343 */       result.add(copy.remove(x));
/*     */     }
/* 345 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Collection<E> sampleWithReplacement(Collection<E> c, int n)
/*     */   {
/* 356 */     return sampleWithReplacement(c, n, new Random());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Collection<E> sampleWithReplacement(Collection<E> c, int n, Random r)
/*     */   {
/* 367 */     if (n < 0)
/* 368 */       throw new IllegalArgumentException("n < 0: " + n);
/* 369 */     List<E> copy = new ArrayList(c.size());
/* 370 */     copy.addAll(c);
/* 371 */     Collection<E> result = new ArrayList(n);
/* 372 */     for (int k = 0; k < n; k++) {
/* 373 */       double d = r.nextDouble();
/* 374 */       int x = (int)(d * copy.size());
/* 375 */       result.add(copy.get(x));
/*     */     }
/* 377 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isSubList(List l1, List l)
/*     */   {
/* 388 */     Iterator it = l.iterator();
/* 389 */     Iterator it1 = l1.iterator();
/* 390 */     for (; it1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 398 */         goto 50)
/*     */     {
/* 391 */       Object o1 = it1.next();
/* 392 */       if (!it.hasNext())
/* 393 */         return false;
/* 394 */       Object o = it.next();
/* 395 */       if (((o == null) && (o1 != null)) || (!o.equals(o1))) {
/* 396 */         if (!it.hasNext())
/* 397 */           return false;
/* 398 */         o = it.next();
/*     */       }
/*     */     }
/* 401 */     return true;
/*     */   }
/*     */   
/*     */   public static String toVerticalString(Map m) {
/* 405 */     StringBuilder b = new StringBuilder();
/* 406 */     Set<Map.Entry> entries = m.entrySet();
/* 407 */     for (Map.Entry e : entries) {
/* 408 */       b.append(e.getKey() + "=" + e.getValue() + "\n");
/*     */     }
/* 410 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static int compareLists(List<? extends Comparable> list1, List<? extends Comparable> list2)
/*     */   {
/* 415 */     if ((list1 == null) && (list2 == null)) return 0;
/* 416 */     if ((list1 == null) || (list2 == null)) {
/* 417 */       throw new IllegalArgumentException();
/*     */     }
/* 419 */     int size1 = list1.size();
/* 420 */     int size2 = list2.size();
/* 421 */     int size = Math.min(size1, size2);
/* 422 */     for (int i = 0; i < size; i++) {
/* 423 */       int c = ((Comparable)list1.get(i)).compareTo(list2.get(i));
/* 424 */       if (c != 0) return c;
/*     */     }
/* 426 */     if (size1 < size2) return -1;
/* 427 */     if (size1 > size2) return 1;
/* 428 */     return 0;
/*     */   }
/*     */   
/*     */   public static <C extends Comparable> Comparator<List<C>> getListComparator() {
/* 432 */     new Comparator() {
/*     */       public int compare(List<C> list1, List<C> list2) {
/* 434 */         return CollectionUtils.compareLists(list1, list2);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 440 */     Collection<String> c = new ArrayList();
/* 441 */     c.add("a");
/* 442 */     c.add("b");
/* 443 */     c.add("c");
/* 444 */     c.add("d");
/* 445 */     c.add("e");
/* 446 */     c.add("f");
/* 447 */     c.add("g");
/* 448 */     c.add("h");
/* 449 */     c.add("i");
/* 450 */     for (int i = 0; i < 10; i++) {
/* 451 */       System.out.println(sampleWithoutReplacement(c, 4));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\CollectionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */