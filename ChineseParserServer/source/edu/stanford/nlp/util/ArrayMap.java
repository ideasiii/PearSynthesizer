/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class ArrayMap<K, V> extends java.util.AbstractMap<K, V>
/*     */ {
/*     */   Map.Entry[] entryArray;
/*     */   int capacity;
/*     */   int size;
/*     */   
/*     */   final class Entry<K, V> implements Map.Entry<K, V>
/*     */   {
/*     */     K key;
/*     */     V value;
/*     */     
/*     */     public K getKey()
/*     */     {
/*  21 */       return (K)this.key;
/*     */     }
/*     */     
/*     */     public V getValue() {
/*  25 */       return (V)this.value;
/*     */     }
/*     */     
/*     */     public V setValue(V o) {
/*  29 */       V old = this.value;
/*  30 */       this.value = o;
/*  31 */       return old;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/*  35 */       return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/*  39 */       if (!(o instanceof Entry))
/*  40 */         return false;
/*  41 */       return (getKey() == null ? ((Entry)o).getKey() == null : getKey().equals(((Entry)o).getKey())) && (getValue() == null ? ((Entry)o).getValue() == null : getValue().equals(((Entry)o).getValue()));
/*     */     }
/*     */     
/*     */     Entry(V key)
/*     */     {
/*  46 */       this.key = key;
/*  47 */       this.value = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayMap()
/*     */   {
/*  53 */     this.size = 0;
/*  54 */     this.capacity = 2;
/*  55 */     this.entryArray = new Map.Entry[2];
/*     */   }
/*     */   
/*     */   public ArrayMap(int capacity) {
/*  59 */     this.size = 0;
/*  60 */     this.capacity = capacity;
/*  61 */     this.entryArray = new Map.Entry[capacity];
/*     */   }
/*     */   
/*     */   public ArrayMap(Map<? extends K, ? extends V> m) {
/*  65 */     this.size = 0;
/*  66 */     this.capacity = m.size();
/*  67 */     this.entryArray = new Map.Entry[m.size()];
/*  68 */     putAll(m);
/*     */   }
/*     */   
/*     */   public ArrayMap(K[] keys, V[] values) {
/*  72 */     if (keys.length != values.length) throw new IllegalArgumentException("different number of keys and values.");
/*  73 */     this.size = keys.length;
/*  74 */     this.capacity = this.size;
/*  75 */     this.entryArray = new Map.Entry[this.size];
/*  76 */     for (int i = 0; i < keys.length; i++) {
/*  77 */       this.entryArray[i] = new Entry(keys[i], values[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set entrySet()
/*     */   {
/*  83 */     return new java.util.HashSet(Arrays.asList(this.entryArray).subList(0, this.size));
/*     */   }
/*     */   
/*     */   public int size() {
/*  87 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  91 */     return this.size == 0;
/*     */   }
/*     */   
/*     */   private void resize() {
/*  95 */     Object[] oldEntryArray = this.entryArray;
/*  96 */     int newCapacity = 2 * this.size;
/*  97 */     if (newCapacity == 0) newCapacity = 1;
/*  98 */     this.entryArray = new Map.Entry[newCapacity];
/*  99 */     System.arraycopy(oldEntryArray, 0, this.entryArray, 0, this.size);
/* 100 */     this.capacity = newCapacity;
/*     */   }
/*     */   
/*     */   public V put(K key, V val) {
/* 104 */     for (int i = 0; i < this.size; i++) {
/* 105 */       if (key.equals(this.entryArray[i].getKey())) {
/* 106 */         return (V)this.entryArray[i].setValue(val);
/*     */       }
/*     */     }
/* 109 */     if (this.capacity <= this.size) {
/* 110 */       resize();
/*     */     }
/* 112 */     this.entryArray[this.size] = new Entry(key, val);
/* 113 */     this.size += 1;
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   public V get(Object key) {
/* 118 */     for (int i = 0; i < this.size; i++) {
/* 119 */       if (key == null ? this.entryArray[i].getKey() == null : key.equals(this.entryArray[i].getKey())) {
/* 120 */         return (V)this.entryArray[i].getValue();
/*     */       }
/*     */     }
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public V remove(Object key) {
/* 127 */     for (int i = 0; i < this.size; i++) {
/* 128 */       if (key == null ? this.entryArray[i].getKey() == null : key.equals(this.entryArray[i].getKey())) {
/* 129 */         V value = this.entryArray[i].getValue();
/* 130 */         if (this.size > 1) {
/* 131 */           this.entryArray[i] = this.entryArray[(this.size - 1)];
/*     */         }
/* 133 */         this.size -= 1;
/* 134 */         return value;
/*     */       }
/*     */     }
/* 137 */     return null;
/*     */   }
/*     */   
/* 140 */   protected int hashCodeCache = 0;
/*     */   
/*     */   public int hashCode() {
/* 143 */     if (this.hashCodeCache == 0) {
/* 144 */       int hashCode = 0;
/* 145 */       for (int i = 0; i < this.size; i++) {
/* 146 */         hashCode += this.entryArray[i].hashCode();
/*     */       }
/* 148 */       this.hashCodeCache = hashCode;
/*     */     }
/* 150 */     return this.hashCodeCache;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 154 */     Map m = (Map)o;
/* 155 */     for (int i = 0; i < this.size; i++) {
/* 156 */       Object mVal = m.get(this.entryArray[i].getKey());
/* 157 */       if (mVal == null) {
/* 158 */         if (this.entryArray[i] != null) {
/* 159 */           return false;
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 164 */       else if (!m.get(this.entryArray[i].getKey()).equals(this.entryArray[i].getValue())) {
/* 165 */         return false;
/*     */       }
/*     */     }
/* 168 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\ArrayMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */