/*    */ package edu.stanford.nlp.process;
/*    */ 
/*    */ import edu.stanford.nlp.ling.FeatureLabel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FeatureLabelTokenFactory
/*    */   implements LexedTokenFactory<FeatureLabel>
/*    */ {
/*    */   public FeatureLabel makeToken(String str, int begin, int length)
/*    */   {
/* 17 */     FeatureLabel fl = new FeatureLabel();
/* 18 */     fl.setWord(str);
/* 19 */     fl.setCurrent(str);
/* 20 */     fl.setBeginPosition(begin);
/* 21 */     fl.setEndPosition(begin + length);
/* 22 */     return fl;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\FeatureLabelTokenFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */