/*    */ package edu.stanford.nlp.io;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileFilter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NumberRangeFileFilter
/*    */   implements FileFilter
/*    */ {
/*    */   private int minimum;
/*    */   private int maximum;
/*    */   private boolean recursively;
/*    */   
/*    */   public NumberRangeFileFilter(int min, int max, boolean recurse)
/*    */   {
/* 34 */     this.minimum = min;
/* 35 */     this.maximum = max;
/* 36 */     this.recursively = recurse;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean accept(File file)
/*    */   {
/* 46 */     if (file.isDirectory()) {
/* 47 */       return this.recursively;
/*    */     }
/* 49 */     String filename = file.getName();
/* 50 */     int k = filename.length() - 1;
/* 51 */     char c = filename.charAt(k);
/* 52 */     while ((k >= 0) && ((c < '0') || (c > '9'))) {
/* 53 */       k--;
/* 54 */       if (k >= 0) {
/* 55 */         c = filename.charAt(k);
/*    */       }
/*    */     }
/* 58 */     if (k < 0) {
/* 59 */       return false;
/*    */     }
/* 61 */     int j = k;
/* 62 */     c = filename.charAt(j);
/* 63 */     while ((j >= 0) && (c >= '0') && (c <= '9')) {
/* 64 */       j--;
/* 65 */       if (j >= 0) {
/* 66 */         c = filename.charAt(j);
/*    */       }
/*    */     }
/* 69 */     j++;
/* 70 */     k++;
/* 71 */     String theNumber = filename.substring(j, k);
/* 72 */     int number = Integer.parseInt(theNumber);
/* 73 */     return (number >= this.minimum) && (number <= this.maximum);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\io\NumberRangeFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */