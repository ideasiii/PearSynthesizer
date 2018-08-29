/*     */ package edu.stanford.nlp.stats;
/*     */ 
/*     */ import edu.stanford.nlp.math.ArrayMath;
/*     */ import edu.stanford.nlp.math.SloppyMath;
/*     */ import edu.stanford.nlp.util.MapFactory;
/*     */ import edu.stanford.nlp.util.MutableDouble;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ public class TwoDimensionalCounter<K1, K2>
/*     */   implements Serializable
/*     */ {
/*     */   public static final long serialVersionUID = 1L;
/*     */   private Map<K1, Counter<K2>> map;
/*     */   private double total;
/*     */   private MapFactory<K1, Counter<K2>> outerMF;
/*     */   private MapFactory<K2, MutableDouble> innerMF;
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  42 */     if (!(o instanceof TwoDimensionalCounter)) {
/*  43 */       return false;
/*     */     }
/*  45 */     return ((TwoDimensionalCounter)o).map.equals(this.map);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  49 */     return this.map.hashCode() + 17;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Counter<K2> getCounter(K1 o)
/*     */   {
/*  57 */     Counter<K2> c = (Counter)this.map.get(o);
/*  58 */     if (c == null) {
/*  59 */       c = new Counter(this.innerMF);
/*  60 */       this.map.put(o, c);
/*     */     }
/*  62 */     return c;
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<K1, Counter<K2>>> entrySet() {
/*  66 */     return this.map.entrySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/*  73 */     int result = 0;
/*  74 */     for (K1 o : firstKeySet()) {
/*  75 */       Counter<K2> c = (Counter)this.map.get(o);
/*  76 */       result += c.size();
/*     */     }
/*  78 */     return result;
/*     */   }
/*     */   
/*     */   public boolean containsKey(K1 o1, K2 o2) {
/*  82 */     if (!this.map.containsKey(o1)) return false;
/*  83 */     Counter<K2> c = (Counter)this.map.get(o1);
/*  84 */     if (!c.containsKey(o2)) return false;
/*  85 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void incrementCount(K1 o1, K2 o2)
/*     */   {
/*  93 */     incrementCount(o1, o2, 1.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void incrementCount(K1 o1, K2 o2, double count)
/*     */   {
/* 102 */     Counter<K2> c = getCounter(o1);
/* 103 */     c.incrementCount(o2, count);
/* 104 */     this.total += count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void decrementCount(K1 o1, K2 o2)
/*     */   {
/* 112 */     incrementCount(o1, o2, -1.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void decrementCount(K1 o1, K2 o2, double count)
/*     */   {
/* 121 */     incrementCount(o1, o2, -count);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCount(K1 o1, K2 o2, double count)
/*     */   {
/* 130 */     Counter<K2> c = getCounter(o1);
/* 131 */     double oldCount = getCount(o1, o2);
/* 132 */     this.total -= oldCount;
/* 133 */     c.setCount(o2, count);
/* 134 */     this.total += count;
/*     */   }
/*     */   
/*     */   public double remove(K1 o1, K2 o2) {
/* 138 */     Counter<K2> c = getCounter(o1);
/* 139 */     double oldCount = getCount(o1, o2);
/* 140 */     this.total -= oldCount;
/* 141 */     c.remove(o2);
/* 142 */     if (c.size() == 0) {
/* 143 */       this.map.remove(o1);
/*     */     }
/* 145 */     return oldCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getCount(K1 o1, K2 o2)
/*     */   {
/* 154 */     Counter c = getCounter(o1);
/* 155 */     return c.getCount(o2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double totalCount()
/*     */   {
/* 164 */     return this.total;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double totalCount(K1 k1)
/*     */   {
/* 171 */     Counter c = getCounter(k1);
/* 172 */     return c.totalCount();
/*     */   }
/*     */   
/*     */   public Set<K1> firstKeySet() {
/* 176 */     return this.map.keySet();
/*     */   }
/*     */   
/*     */   public Counter<K2> setCounter(K1 o, Counter<K2> c) {
/* 180 */     Counter<K2> old = getCounter(o);
/* 181 */     this.total -= old.totalCount();
/* 182 */     this.map.put(o, c);
/* 183 */     this.total += c.totalCount();
/* 184 */     return old;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <K1, K2> TwoDimensionalCounter<K2, K1> reverseIndexOrder(TwoDimensionalCounter<K1, K2> cc)
/*     */   {
/* 194 */     TwoDimensionalCounter<K2, K1> result = new TwoDimensionalCounter(cc.outerMF, cc.innerMF);
/*     */     
/*     */ 
/* 197 */     for (Iterator i$ = cc.firstKeySet().iterator(); i$.hasNext();) { key1 = i$.next();
/* 198 */       c = cc.getCounter(key1);
/* 199 */       for (K2 key2 : c.keySet()) {
/* 200 */         double count = c.getCount(key2);
/* 201 */         result.setCount(key2, key1, count); } }
/*     */     K1 key1;
/*     */     Counter<K2> c;
/* 204 */     return result;
/*     */   }
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
/* 216 */     StringBuilder buff = new StringBuilder();
/* 217 */     for (Iterator i$ = this.map.keySet().iterator(); i$.hasNext();) { key1 = i$.next();
/* 218 */       c = getCounter(key1);
/* 219 */       for (K2 key2 : c.keySet()) {
/* 220 */         double score = c.getCount(key2);
/* 221 */         buff.append(key1 + "\t" + key2 + "\t" + score + "\n"); } }
/*     */     K1 key1;
/*     */     Counter<K2> c;
/* 224 */     return buff.toString();
/*     */   }
/*     */   
/*     */   public String toMatrixString(int cellSize) {
/* 228 */     List<K1> firstKeys = new ArrayList(firstKeySet());
/* 229 */     List<K2> secondKeys = new ArrayList(secondKeySet());
/* 230 */     Collections.sort(firstKeys);
/* 231 */     Collections.sort(secondKeys);
/* 232 */     double[][] counts = toMatrix(firstKeys, secondKeys);
/* 233 */     return ArrayMath.toString(counts, cellSize, firstKeys.toArray(), secondKeys.toArray(), new DecimalFormat(), true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[][] toMatrix(List<K1> firstKeys, List<K2> secondKeys)
/*     */   {
/* 244 */     double[][] counts = new double[firstKeys.size()][secondKeys.size()];
/* 245 */     for (int i = 0; i < firstKeys.size(); i++) {
/* 246 */       for (int j = 0; j < secondKeys.size(); j++) {
/* 247 */         counts[i][j] = getCount(firstKeys.get(i), secondKeys.get(j));
/*     */       }
/*     */     }
/* 250 */     return counts;
/*     */   }
/*     */   
/*     */   public String toCSVString(NumberFormat nf) {
/* 254 */     List<K1> firstKeys = new ArrayList(firstKeySet());
/* 255 */     List<K2> secondKeys = new ArrayList(secondKeySet());
/* 256 */     Collections.sort(firstKeys);
/* 257 */     Collections.sort(secondKeys);
/* 258 */     StringBuilder b = new StringBuilder();
/* 259 */     String[] headerRow = new String[secondKeys.size() + 1];
/* 260 */     headerRow[0] = "";
/* 261 */     for (int j = 0; j < secondKeys.size(); j++) {
/* 262 */       headerRow[(j + 1)] = secondKeys.get(j).toString();
/*     */     }
/* 264 */     b.append(StringUtils.toCSVString(headerRow)).append("\n");
/* 265 */     for (int i = 0; i < firstKeys.size(); i++) {
/* 266 */       String[] row = new String[secondKeys.size() + 1];
/* 267 */       K1 rowLabel = firstKeys.get(i);
/* 268 */       row[0] = rowLabel.toString();
/* 269 */       for (int j = 0; j < secondKeys.size(); j++) {
/* 270 */         K2 colLabel = secondKeys.get(j);
/* 271 */         row[(j + 1)] = nf.format(getCount(rowLabel, colLabel));
/*     */       }
/* 273 */       b.append(StringUtils.toCSVString(row)).append("\n");
/*     */     }
/* 275 */     return b.toString();
/*     */   }
/*     */   
/*     */   public Set<K2> secondKeySet() {
/* 279 */     Set<K2> result = new HashSet();
/* 280 */     for (K1 k1 : firstKeySet()) {
/* 281 */       for (K2 k2 : getCounter(k1).keySet()) {
/* 282 */         result.add(k2);
/*     */       }
/*     */     }
/* 285 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 289 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   public Counter<Pair<K1, K2>> flatten() {
/* 293 */     Counter<Pair<K1, K2>> result = new Counter();
/* 294 */     for (Iterator i$ = firstKeySet().iterator(); i$.hasNext();) { key1 = i$.next();
/* 295 */       inner = getCounter(key1);
/* 296 */       for (K2 key2 : inner.keySet())
/* 297 */         result.setCount(new Pair(key1, key2), inner.getCount(key2)); }
/*     */     K1 key1;
/*     */     Counter<K2> inner;
/* 300 */     return result;
/*     */   }
/*     */   
/*     */   public void addAll(TwoDimensionalCounter<K1, K2> c) {
/* 304 */     for (K1 key : c.firstKeySet()) {
/* 305 */       Counter<K2> inner = c.getCounter(key);
/* 306 */       Counter<K2> myInner = getCounter(key);
/* 307 */       myInner.addAll(inner);
/* 308 */       this.total += inner.totalCount();
/*     */     }
/*     */   }
/*     */   
/*     */   public void addAll(K1 key, Counter<K2> c) {
/* 313 */     Counter<K2> myInner = getCounter(key);
/* 314 */     myInner.addAll(c);
/* 315 */     this.total += c.totalCount();
/*     */   }
/*     */   
/*     */   public void subtractAll(K1 key, Counter<K2> c) {
/* 319 */     Counter<K2> myInner = getCounter(key);
/* 320 */     myInner.subtractAll(c);
/* 321 */     this.total -= c.totalCount();
/*     */   }
/*     */   
/*     */ 
/*     */   public void subtractAll(TwoDimensionalCounter<K1, K2> c, boolean removeKeys)
/*     */   {
/* 327 */     for (K1 key : c.firstKeySet()) {
/* 328 */       Counter<K2> inner = c.getCounter(key);
/* 329 */       Counter<K2> myInner = getCounter(key);
/* 330 */       myInner.subtractAll(inner, removeKeys);
/* 331 */       this.total -= inner.totalCount();
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeZeroCounts() {
/* 336 */     Set<K1> firstKeySet = new HashSet(firstKeySet());
/* 337 */     for (K1 k1 : firstKeySet) {
/* 338 */       Counter<K2> c = getCounter(k1);
/* 339 */       c.removeZeroCounts();
/* 340 */       if (c.size() == 0) this.map.remove(k1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void remove(K1 key) {
/* 345 */     Counter<K2> counter = (Counter)this.map.get(key);
/* 346 */     if (counter != null) this.total -= counter.totalCount();
/* 347 */     this.map.remove(key);
/*     */   }
/*     */   
/*     */   public void clean() {
/* 351 */     for (K1 key1 : new HashSet(this.map.keySet())) {
/* 352 */       Counter<K2> c = (Counter)this.map.get(key1);
/* 353 */       for (K2 key2 : new HashSet(c.keySet())) {
/* 354 */         if (SloppyMath.isCloseTo(0.0D, c.getCount(key2))) {
/* 355 */           c.remove(key2);
/*     */         }
/*     */       }
/* 358 */       if (c.keySet().isEmpty()) {
/* 359 */         this.map.remove(key1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public MapFactory<K1, Counter<K2>> getOuterMapFactory() {
/* 365 */     return this.outerMF;
/*     */   }
/*     */   
/*     */   public MapFactory<K2, MutableDouble> getInnerMapFactory() {
/* 369 */     return this.innerMF;
/*     */   }
/*     */   
/*     */   public TwoDimensionalCounter() {
/* 373 */     this(MapFactory.HASH_MAP_FACTORY, MapFactory.HASH_MAP_FACTORY);
/*     */   }
/*     */   
/*     */   public TwoDimensionalCounter(MapFactory<K1, Counter<K2>> outerFactory, MapFactory<K2, MutableDouble> innerFactory) {
/* 377 */     this.innerMF = innerFactory;
/* 378 */     this.outerMF = outerFactory;
/* 379 */     this.map = outerFactory.newMap();
/* 380 */     this.total = 0.0D;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 384 */     TwoDimensionalCounter<String, String> cc = new TwoDimensionalCounter();
/* 385 */     cc.setCount("a", "c", 1.0D);
/* 386 */     cc.setCount("b", "c", 1.0D);
/* 387 */     cc.setCount("a", "d", 1.0D);
/* 388 */     cc.setCount("a", "d", -1.0D);
/* 389 */     cc.setCount("b", "d", 1.0D);
/* 390 */     System.out.println(cc);
/* 391 */     cc.incrementCount("b", "d", 1.0D);
/* 392 */     System.out.println(cc);
/* 393 */     TwoDimensionalCounter<String, String> cc2 = reverseIndexOrder(cc);
/* 394 */     System.out.println(cc2);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\stats\TwoDimensionalCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */