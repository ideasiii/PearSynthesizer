/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LabeledScoredConstituentFactory
/*    */   implements ConstituentFactory
/*    */ {
/*    */   public Constituent newConstituent(int start, int end)
/*    */   {
/* 14 */     return new LabeledScoredConstituent(start, end);
/*    */   }
/*    */   
/*    */   public Constituent newConstituent(int start, int end, Label label, double score)
/*    */   {
/* 19 */     return new LabeledScoredConstituent(start, end, label, score);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\LabeledScoredConstituentFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */