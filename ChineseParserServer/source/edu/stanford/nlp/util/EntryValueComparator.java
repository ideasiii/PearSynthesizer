/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntryValueComparator
/*     */   implements Comparator
/*     */ {
/*     */   private boolean ascending;
/*     */   private boolean useMagnitude;
/*     */   private Map m;
/*     */   
/*     */   public EntryValueComparator()
/*     */   {
/*  40 */     this(null, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntryValueComparator(boolean ascending)
/*     */   {
/*  48 */     this(null, ascending);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntryValueComparator(Map m)
/*     */   {
/*  56 */     this(m, true);
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
/*     */ 
/*     */ 
/*     */   public EntryValueComparator(Map m, boolean ascending)
/*     */   {
/*  75 */     this(m, ascending, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntryValueComparator(Map m, boolean ascending, boolean useMagnitude)
/*     */   {
/*  96 */     this.m = m;
/*  97 */     this.ascending = ascending;
/*  98 */     this.useMagnitude = useMagnitude;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compare(Object o1, Object o2)
/*     */   {
/*     */     Object v1;
/*     */     
/*     */ 
/*     */ 
/*     */     Object v1;
/*     */     
/*     */ 
/*     */ 
/* 114 */     if ((o1 instanceof Map.Entry)) {
/* 115 */       v1 = ((Map.Entry)o1).getValue();
/* 116 */     } else if (this.m != null) {
/* 117 */       Object v1 = this.m.get(o1);
/* 118 */       if (v1 == null) {
/* 119 */         throw new RuntimeException("Key not found in map.");
/*     */       }
/*     */     } else {
/* 122 */       v1 = o1; }
/*     */     Object v2;
/* 124 */     Object v2; if ((o2 instanceof Map.Entry)) {
/* 125 */       v2 = ((Map.Entry)o2).getValue();
/* 126 */     } else if (this.m != null) {
/* 127 */       Object v2 = this.m.get(o2);
/* 128 */       if (v2 == null) {
/* 129 */         throw new RuntimeException("Key not found in map.");
/*     */       }
/*     */     } else {
/* 132 */       v2 = o2;
/*     */     }
/*     */     
/* 135 */     if (this.useMagnitude) {
/* 136 */       v1 = new Double(Math.abs(((Number)v1).doubleValue()));
/* 137 */       v2 = new Double(Math.abs(((Number)v2).doubleValue()));
/*     */     }
/*     */     int result;
/*     */     int result;
/* 141 */     if (this.ascending) {
/* 142 */       result = ((Comparable)v1).compareTo(v2);
/*     */     } else {
/* 144 */       result = ((Comparable)v2).compareTo(v1);
/*     */     }
/* 146 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\EntryValueComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */