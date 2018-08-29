/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.stats.EquivalenceClassEval.EqualityChecker;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WordCatEqualityChecker
/*    */   implements EquivalenceClassEval.EqualityChecker
/*    */ {
/*    */   public boolean areEqual(Object o, Object o2)
/*    */   {
/* 16 */     WordCatConstituent span = (WordCatConstituent)o;
/* 17 */     WordCatConstituent span2 = (WordCatConstituent)o2;
/* 18 */     if (span.type != span2.type)
/* 19 */       return false;
/* 20 */     if ((span.start() != span2.start()) || (span.end() != span2.end()))
/* 21 */       return false;
/* 22 */     if ((span.type != "word") && (!span.value().equals(span2.value()))) {
/* 23 */       return false;
/*    */     }
/* 25 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\WordCatEqualityChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */