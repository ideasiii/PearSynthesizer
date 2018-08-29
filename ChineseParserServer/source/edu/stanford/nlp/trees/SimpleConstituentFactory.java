/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleConstituentFactory
/*    */   implements ConstituentFactory
/*    */ {
/*    */   public Constituent newConstituent(int start, int end)
/*    */   {
/* 15 */     return new SimpleConstituent(start, end);
/*    */   }
/*    */   
/*    */   public Constituent newConstituent(int start, int end, Label label, double score)
/*    */   {
/* 20 */     return new SimpleConstituent(start, end);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\SimpleConstituentFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */