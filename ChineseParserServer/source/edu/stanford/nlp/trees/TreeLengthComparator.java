/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Sentence;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TreeLengthComparator
/*    */   implements Comparator
/*    */ {
/*    */   public int compare(Object o1, Object o2)
/*    */   {
/* 25 */     if (o1 == o2) {
/* 26 */       return 0;
/*    */     }
/* 28 */     Tree t1 = (Tree)o1;
/* 29 */     Tree t2 = (Tree)o2;
/* 30 */     int len1 = t1.yield().size();
/* 31 */     int len2 = t2.yield().size();
/* 32 */     if (len1 > len2)
/* 33 */       return 1;
/* 34 */     if (len1 < len2) {
/* 35 */       return -1;
/*    */     }
/* 37 */     return 0;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreeLengthComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */