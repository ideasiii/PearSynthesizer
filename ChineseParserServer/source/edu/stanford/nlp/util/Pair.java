/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.Serializable;
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
/*     */ public class Pair<T1, T2>
/*     */   implements Comparable, Serializable
/*     */ {
/*     */   public T1 first;
/*     */   public T2 second;
/*     */   private static final long serialVersionUID = 1360822168806852921L;
/*     */   
/*     */   public Pair() {}
/*     */   
/*     */   public Pair(T1 first, T2 second)
/*     */   {
/*  39 */     this.first = first;
/*  40 */     this.second = second;
/*     */   }
/*     */   
/*     */   public T1 first() {
/*  44 */     return (T1)this.first;
/*     */   }
/*     */   
/*     */   public T2 second() {
/*  48 */     return (T2)this.second;
/*     */   }
/*     */   
/*     */   public void setFirst(T1 o) {
/*  52 */     this.first = o;
/*     */   }
/*     */   
/*     */   public void setSecond(T2 o) {
/*  56 */     this.second = o;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  60 */     return "(" + this.first + "," + this.second + ")";
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  64 */     if ((o instanceof Pair)) {
/*  65 */       Pair p = (Pair)o;
/*  66 */       return (this.first == null ? p.first == null : this.first.equals(p.first)) && (this.second == null ? p.second == null : this.second.equals(p.second));
/*     */     }
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  73 */     return (this.first == null ? 0 : this.first.hashCode()) << 16 ^ (this.second == null ? 0 : this.second.hashCode());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Pair<String, String> readStringPair(DataInputStream in)
/*     */   {
/*  82 */     Pair<String, String> p = new Pair();
/*     */     try {
/*  84 */       p.first = in.readUTF();
/*  85 */       p.second = in.readUTF();
/*     */     } catch (Exception e) {
/*  87 */       e.printStackTrace();
/*     */     }
/*  89 */     return p;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void save(DataOutputStream out)
/*     */   {
/*     */     try
/*     */     {
/* 101 */       out.writeUTF(this.first.toString());
/* 102 */       out.writeUTF(this.second.toString());
/*     */     } catch (Exception e) {
/* 104 */       e.printStackTrace();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(Object o)
/*     */   {
/* 130 */     Pair another = (Pair)o;
/* 131 */     int comp = ((Comparable)first()).compareTo(another.first());
/* 132 */     if (comp != 0) {
/* 133 */       return comp;
/*     */     }
/* 135 */     return ((Comparable)second()).compareTo(another.second());
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
/*     */   public static Pair<String, String> stringIntern(Pair<String, String> p)
/*     */   {
/* 148 */     return new InternedPair(p, null);
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
/*     */   public static Pair<String, String> internedStringPair(String first, String second)
/*     */   {
/* 167 */     return new InternedPair(first, second, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class InternedPair
/*     */     extends Pair<String, String>
/*     */   {
/*     */     private static final long serialVersionUID = 1360822168806852922L;
/*     */     
/*     */ 
/*     */     private InternedPair(Pair<String, String> p)
/*     */     {
/* 180 */       super(p.second);
/* 181 */       internStrings();
/*     */     }
/*     */     
/*     */     private InternedPair(String first, String second) {
/* 185 */       super(second);
/* 186 */       internStrings();
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 190 */       internStrings();
/* 191 */       return this;
/*     */     }
/*     */     
/*     */     private void internStrings() {
/* 195 */       if (this.first != null) {
/* 196 */         this.first = ((String)this.first).intern();
/*     */       }
/* 198 */       if (this.second != null) {
/* 199 */         this.second = ((String)this.second).intern();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Pair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */