/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public class Numberer
/*     */   implements Serializable
/*     */ {
/*  26 */   private static Map<String, Numberer> numbererMap = new HashMap();
/*     */   private int total;
/*     */   private Map intToObject;
/*     */   private Map objectToInt;
/*     */   private MutableInteger tempInt;
/*     */   private boolean locked;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static Map<String, Numberer> getNumberers()
/*     */   {
/*  36 */     return numbererMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setNumberers(Map<String, Numberer> numbs)
/*     */   {
/*  43 */     numbererMap = numbs;
/*     */   }
/*     */   
/*     */   public static void setGlobalNumberer(String key, Numberer numb) {
/*  47 */     numbererMap.put(key, numb);
/*     */   }
/*     */   
/*     */   public static Numberer getGlobalNumberer(String type) {
/*  51 */     Numberer n = (Numberer)numbererMap.get(type);
/*  52 */     if (n == null) {
/*  53 */       n = new Numberer();
/*  54 */       numbererMap.put(type, n);
/*     */     }
/*  56 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int number(String type, Object o)
/*     */   {
/*  66 */     return getGlobalNumberer(type).number(o);
/*     */   }
/*     */   
/*     */   public static Object object(String type, int n) {
/*  70 */     return getGlobalNumberer(type).object(n);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int translate(String sourceType, String targetType, int n)
/*     */   {
/*  81 */     return getGlobalNumberer(targetType).number(getGlobalNumberer(sourceType).object(n));
/*     */   }
/*     */   
/*     */   public int total()
/*     */   {
/*  86 */     return this.total;
/*     */   }
/*     */   
/*     */   public void lock()
/*     */   {
/*  91 */     this.locked = true;
/*     */   }
/*     */   
/*     */   public void unlock()
/*     */   {
/*  96 */     this.locked = false;
/*     */   }
/*     */   
/*     */   public boolean hasSeen(Object o)
/*     */   {
/* 101 */     return this.objectToInt.keySet().contains(o);
/*     */   }
/*     */   
/*     */   public Set objects() {
/* 105 */     return this.objectToInt.keySet();
/*     */   }
/*     */   
/*     */   public int number(Object o)
/*     */   {
/* 110 */     MutableInteger i = (MutableInteger)this.objectToInt.get(o);
/* 111 */     if (i == null) {
/* 112 */       if (this.locked) {
/* 113 */         throw new NoSuchElementException("Numberer locked but trying to number unseen object " + o.toString());
/*     */       }
/* 115 */       i = new MutableInteger(this.total);
/* 116 */       this.total += 1;
/* 117 */       this.objectToInt.put(o, i);
/* 118 */       this.intToObject.put(i, o);
/*     */     }
/* 120 */     return i.intValue();
/*     */   }
/*     */   
/*     */   public Object object(int n)
/*     */   {
/* 125 */     this.tempInt.set(n);
/* 126 */     return this.intToObject.get(this.tempInt);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 131 */     StringBuilder sb = new StringBuilder();
/* 132 */     sb.append("[");
/* 133 */     for (int i = 0; i < this.total; i++) {
/* 134 */       sb.append(i);
/* 135 */       sb.append("->");
/* 136 */       sb.append(object(i));
/* 137 */       if (i < this.total - 1) {
/* 138 */         sb.append(", ");
/*     */       }
/*     */     }
/* 141 */     sb.append("]");
/* 142 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public Numberer()
/*     */   {
/* 148 */     this.tempInt = new MutableInteger();
/* 149 */     this.intToObject = new HashMap();
/* 150 */     this.objectToInt = new HashMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Numberer(Numberer numb)
/*     */   {
/* 160 */     this.tempInt = new MutableInteger();
/* 161 */     this.intToObject = new HashMap(numb.total());
/* 162 */     this.objectToInt = new HashMap(numb.total());
/* 163 */     for (int i = 0; i < numb.total(); i++) {
/* 164 */       Object obj = numb.object(i);
/* 165 */       int x = number(obj);
/* 166 */       if (i != x) {
/* 167 */         throw new IllegalStateException("Something bung!\n");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Numberer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */