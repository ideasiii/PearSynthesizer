/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ public class Maps
/*     */ {
/*     */   public static void putIntoValueHashSet(Map map, Object key, Object value)
/*     */   {
/*  24 */     putIntoValueCollection(map, key, value, CollectionFactory.HASH_SET_FACTORY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void putIntoValueArrayList(Map map, Object key, Object value)
/*     */   {
/*  35 */     putIntoValueCollection(map, key, value, CollectionFactory.ARRAY_LIST_FACTORY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void putIntoValueCollection(Map map, Object key, Object value, CollectionFactory cf)
/*     */   {
/*  47 */     Collection c = (Collection)map.get(key);
/*  48 */     if (c == null) {
/*  49 */       c = cf.newCollection();
/*  50 */       map.put(key, c);
/*     */     }
/*  52 */     c.add(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Map compose(Map map1, Map map2)
/*     */   {
/*  63 */     Map composedMap = new HashMap();
/*  64 */     for (Iterator keyI = map1.keySet().iterator(); keyI.hasNext();) {
/*  65 */       Object key = keyI.next();
/*  66 */       composedMap.put(key, map2.get(map1.get(key)));
/*     */     }
/*  68 */     return composedMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Map invert(Map map)
/*     */   {
/*  78 */     Map invertedMap = new HashMap();
/*  79 */     for (Iterator entryI = map.entrySet().iterator(); entryI.hasNext();) {
/*  80 */       Map.Entry entry = (Map.Entry)entryI.next();
/*  81 */       Object key = entry.getKey();
/*  82 */       Object value = entry.getValue();
/*  83 */       invertedMap.put(value, key);
/*     */     }
/*  85 */     return invertedMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Map invertSet(Map map)
/*     */   {
/*  95 */     Map invertedMap = new HashMap();
/*  96 */     for (Iterator entryI = map.entrySet().iterator(); entryI.hasNext();) {
/*  97 */       Map.Entry entry = (Map.Entry)entryI.next();
/*  98 */       Object key = entry.getKey();
/*  99 */       Object value = entry.getValue();
/* 100 */       putIntoValueHashSet(invertedMap, value, key);
/*     */     }
/* 102 */     return invertedMap;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 106 */     Map map1 = new HashMap();
/* 107 */     map1.put("a", "1");
/* 108 */     map1.put("b", "2");
/* 109 */     map1.put("c", "2");
/* 110 */     map1.put("d", "4");
/* 111 */     Map map2 = new HashMap();
/* 112 */     map2.put("1", "x");
/* 113 */     map2.put("2", "y");
/* 114 */     map2.put("3", "z");
/* 115 */     System.out.println("map1: " + map1);
/* 116 */     System.out.println("invert(map1): " + invert(map1));
/* 117 */     System.out.println("invertSet(map1): " + invertSet(map1));
/* 118 */     System.out.println("map2: " + map2);
/* 119 */     System.out.println("compose(map1,map2): " + compose(map1, map2));
/* 120 */     Map setValues = new HashMap();
/* 121 */     Map listValues = new HashMap();
/* 122 */     putIntoValueArrayList(listValues, "a", "1");
/* 123 */     putIntoValueArrayList(listValues, "a", "1");
/* 124 */     putIntoValueArrayList(listValues, "a", "2");
/* 125 */     putIntoValueHashSet(setValues, "a", "1");
/* 126 */     putIntoValueHashSet(setValues, "a", "1");
/* 127 */     putIntoValueHashSet(setValues, "a", "2");
/* 128 */     System.out.println("listValues: " + listValues);
/* 129 */     System.out.println("setValues: " + setValues);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Maps.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */