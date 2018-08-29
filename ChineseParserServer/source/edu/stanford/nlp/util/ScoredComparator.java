/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ScoredComparator
/*    */   implements Comparator, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private static final boolean ASCENDING = true;
/*    */   private static final boolean DESCENDING = false;
/* 22 */   public static final ScoredComparator ASCENDING_COMPARATOR = new ScoredComparator(true);
/*    */   
/* 24 */   public static final ScoredComparator DESCENDING_COMPARATOR = new ScoredComparator(false);
/*    */   private final boolean ascending;
/*    */   
/*    */   private ScoredComparator(boolean ascending)
/*    */   {
/* 29 */     this.ascending = ascending;
/*    */   }
/*    */   
/*    */   public int compare(Object o1, Object o2) {
/* 33 */     if (o1 == o2) {
/* 34 */       return 0;
/*    */     }
/* 36 */     double d1 = ((Scored)o1).score();
/* 37 */     double d2 = ((Scored)o2).score();
/* 38 */     if (this.ascending) {
/* 39 */       if (d1 < d2) {
/* 40 */         return -1;
/*    */       }
/* 42 */       if (d1 > d2) {
/* 43 */         return 1;
/*    */       }
/*    */     } else {
/* 46 */       if (d1 < d2) {
/* 47 */         return 1;
/*    */       }
/* 49 */       if (d1 > d2) {
/* 50 */         return -1;
/*    */       }
/*    */     }
/* 53 */     return 0;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 57 */     if ((o instanceof ScoredComparator)) {
/* 58 */       ScoredComparator sc = (ScoredComparator)o;
/* 59 */       if (this.ascending == sc.ascending) {
/* 60 */         return true;
/*    */       }
/*    */     }
/* 63 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 71 */     if (this.ascending) {
/* 72 */       return 8388608;
/*    */     }
/* 74 */     return 8388609;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 79 */     return "ScoredComparator(" + (this.ascending ? "ascending" : "descending") + ")";
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\ScoredComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */