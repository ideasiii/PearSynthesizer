/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.stats.EquivalenceClasser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WordCatEquivalenceClasser
/*    */   implements EquivalenceClasser
/*    */ {
/*    */   public Object equivalenceClass(Object o)
/*    */   {
/* 14 */     WordCatConstituent lb = (WordCatConstituent)o;
/* 15 */     return lb.type;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\WordCatEquivalenceClasser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */