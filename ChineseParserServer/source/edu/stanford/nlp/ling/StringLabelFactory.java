/*    */ package edu.stanford.nlp.ling;
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
/*    */ public class StringLabelFactory
/*    */   implements LabelFactory
/*    */ {
/*    */   public Label newLabel(String labelStr)
/*    */   {
/* 20 */     return new StringLabel(labelStr);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Label newLabel(String labelStr, int options)
/*    */   {
/* 33 */     return new StringLabel(labelStr);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Label newLabelFromString(String labelStr)
/*    */   {
/* 46 */     return new StringLabel(labelStr);
/*    */   }
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
/*    */   public Label newLabel(Label oldLabel)
/*    */   {
/* 60 */     return new StringLabel(oldLabel);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\StringLabelFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */