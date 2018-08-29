/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MapFactory<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4529666940763477360L;
/*  19 */   public static final MapFactory HASH_MAP_FACTORY = new HashMapFactory(null);
/*     */   
/*  21 */   public static final MapFactory IDENTITY_HASH_MAP_FACTORY = new IdentityHashMapFactory(null);
/*     */   
/*  23 */   public static final MapFactory WEAK_HASH_MAP_FACTORY = new WeakHashMapFactory(null);
/*     */   
/*  25 */   public static final MapFactory TREE_MAP_FACTORY = new TreeMapFactory(null);
/*     */   
/*  27 */   public static final MapFactory ARRAY_MAP_FACTORY = new ArrayMapFactory(null);
/*     */   
/*     */   private static class HashMapFactory<K, V> extends MapFactory<K, V> {
/*  30 */     private HashMapFactory() { super(); }
/*     */     
/*     */     private static final long serialVersionUID = -9222344631596580863L;
/*     */     public Map<K, V> newMap()
/*     */     {
/*  35 */       return new HashMap();
/*     */     }
/*     */     
/*     */     public Map<K, V> newMap(int initCapacity) {
/*  39 */       return new HashMap(initCapacity);
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map) {
/*  43 */       map = new HashMap();
/*  44 */       return map;
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map, int initCapacity) {
/*  48 */       map = new HashMap(initCapacity);
/*  49 */       return map;
/*     */     } }
/*     */   
/*     */   public abstract Map<K, V> newMap();
/*     */   
/*  54 */   private static class IdentityHashMapFactory<K, V> extends MapFactory<K, V> { private IdentityHashMapFactory() { super(); }
/*     */     
/*     */     private static final long serialVersionUID = -9222344631596580863L;
/*     */     public Map<K, V> newMap()
/*     */     {
/*  59 */       return new IdentityHashMap();
/*     */     }
/*     */     
/*     */     public Map<K, V> newMap(int initCapacity) {
/*  63 */       return new IdentityHashMap(initCapacity);
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map) {
/*  67 */       map = new IdentityHashMap();
/*  68 */       return map;
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map, int initCapacity) {
/*  72 */       map = new IdentityHashMap(initCapacity);
/*  73 */       return map;
/*     */     } }
/*     */   
/*     */   public abstract Map<K, V> newMap(int paramInt);
/*     */   
/*  78 */   private static class WeakHashMapFactory<K, V> extends MapFactory<K, V> { private WeakHashMapFactory() { super(); }
/*     */     
/*     */     private static final long serialVersionUID = 4790014244304941000L;
/*     */     public Map<K, V> newMap()
/*     */     {
/*  83 */       return new WeakHashMap();
/*     */     }
/*     */     
/*     */     public Map<K, V> newMap(int initCapacity) {
/*  87 */       return new WeakHashMap(initCapacity);
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map)
/*     */     {
/*  92 */       map = new WeakHashMap();
/*  93 */       return map;
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map, int initCapacity) {
/*  97 */       map = new WeakHashMap(initCapacity);
/*  98 */       return map;
/*     */     } }
/*     */   
/*     */   public abstract <K1, V1> Map<K1, V1> setMap(Map<K1, V1> paramMap);
/*     */   
/* 103 */   private static class TreeMapFactory<K, V> extends MapFactory<K, V> { private TreeMapFactory() { super(); }
/*     */     
/*     */     private static final long serialVersionUID = -9138736068025818670L;
/*     */     public Map<K, V> newMap()
/*     */     {
/* 108 */       return new TreeMap();
/*     */     }
/*     */     
/*     */     public Map<K, V> newMap(int initCapacity) {
/* 112 */       return newMap();
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map)
/*     */     {
/* 117 */       map = new TreeMap();
/* 118 */       return map;
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map, int initCapacity) {
/* 122 */       map = new TreeMap();
/* 123 */       return map;
/*     */     } }
/*     */   
/*     */   public abstract <K1, V1> Map<K1, V1> setMap(Map<K1, V1> paramMap, int paramInt);
/*     */   
/* 128 */   private static class ArrayMapFactory<K, V> extends MapFactory<K, V> { private ArrayMapFactory() { super(); }
/*     */     
/*     */     public Map<K, V> newMap() {
/* 131 */       return new ArrayMap();
/*     */     }
/*     */     
/*     */     public Map<K, V> newMap(int initCapacity) {
/* 135 */       return new ArrayMap(initCapacity);
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map) {
/* 139 */       return new ArrayMap();
/*     */     }
/*     */     
/*     */     public <K1, V1> Map<K1, V1> setMap(Map<K1, V1> map, int initCapacity) {
/* 143 */       map = new ArrayMap(initCapacity);
/* 144 */       return map;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\MapFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */