/*     */ package edu.stanford.nlp.io;
/*     */ 
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ public class NumberRangesFileFilter
/*     */   implements FileFilter
/*     */ {
/*  38 */   private List<Pair<Integer, Integer>> ranges = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean recursively;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumberRangesFileFilter(String ranges, boolean recurse)
/*     */   {
/*  53 */     this.recursively = recurse;
/*     */     try {
/*  55 */       String[] ra = ranges.split(",");
/*  56 */       for (int i = 0; i < ra.length; i++) {
/*  57 */         String[] one = ra[i].split("-");
/*  58 */         if (one.length > 2) {
/*  59 */           throw new IllegalArgumentException("Too many hyphens");
/*     */         }
/*  61 */         int low = Integer.parseInt(one[0].trim());
/*     */         int high;
/*  63 */         int high; if (one.length == 2) {
/*  64 */           high = Integer.parseInt(one[1].trim());
/*     */         } else {
/*  66 */           high = low;
/*     */         }
/*  68 */         Pair<Integer, Integer> p = new Pair(new Integer(low), new Integer(high));
/*  69 */         this.ranges.add(p);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  73 */       IllegalArgumentException iae = new IllegalArgumentException("Constructor argument not valid: " + ranges);
/*  74 */       iae.initCause(e);
/*  75 */       throw iae;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean accept(File file)
/*     */   {
/*  87 */     if (file.isDirectory()) {
/*  88 */       return this.recursively;
/*     */     }
/*  90 */     String filename = file.getName();
/*  91 */     int k = filename.length() - 1;
/*  92 */     char c = filename.charAt(k);
/*  93 */     while ((k >= 0) && (!Character.isDigit(c))) {
/*  94 */       k--;
/*  95 */       if (k >= 0) {
/*  96 */         c = filename.charAt(k);
/*     */       }
/*     */     }
/*  99 */     if (k < 0) {
/* 100 */       return false;
/*     */     }
/* 102 */     int j = k;
/* 103 */     c = filename.charAt(j);
/* 104 */     while ((j >= 0) && (Character.isDigit(c))) {
/* 105 */       j--;
/* 106 */       if (j >= 0) {
/* 107 */         c = filename.charAt(j);
/*     */       }
/*     */     }
/* 110 */     j++;
/* 111 */     k++;
/* 112 */     String theNumber = filename.substring(j, k);
/* 113 */     int number = Integer.parseInt(theNumber);
/* 114 */     for (Pair<Integer, Integer> p : this.ranges) {
/* 115 */       int low = ((Integer)p.first()).intValue();
/* 116 */       int high = ((Integer)p.second()).intValue();
/* 117 */       if ((number >= low) && (number <= high)) {
/* 118 */         return true;
/*     */       }
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public String toString() {
/*     */     StringBuilder sb;
/*     */     StringBuilder sb;
/* 127 */     if (this.recursively) {
/* 128 */       sb = new StringBuilder("recursively ");
/*     */     } else {
/* 130 */       sb = new StringBuilder();
/*     */     }
/* 132 */     for (Iterator<Pair<Integer, Integer>> it = this.ranges.iterator(); it.hasNext();) {
/* 133 */       Pair<Integer, Integer> p = (Pair)it.next();
/* 134 */       int low = ((Integer)p.first()).intValue();
/* 135 */       int high = ((Integer)p.second()).intValue();
/* 136 */       if (low == high) {
/* 137 */         sb.append(low);
/*     */       } else {
/* 139 */         sb.append(low);
/* 140 */         sb.append("-");
/* 141 */         sb.append(high);
/*     */       }
/* 143 */       if (it.hasNext()) {
/* 144 */         sb.append(",");
/*     */       }
/*     */     }
/* 147 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\io\NumberRangesFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */