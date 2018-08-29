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
/*    */ public class CategoryWordTagFactory
/*    */   implements LabelFactory
/*    */ {
/*    */   public Label newLabel(String labelStr)
/*    */   {
/* 19 */     return new CategoryWordTag(labelStr);
/*    */   }
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
/* 31 */     return new CategoryWordTag(labelStr);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Label newLabelFromString(String labelStr)
/*    */   {
/* 41 */     CategoryWordTag cwt = new CategoryWordTag();
/* 42 */     cwt.setFromString(labelStr);
/* 43 */     return cwt;
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
/*    */ 
/*    */   public Label newLabel(String word, String tag, String category)
/*    */   {
/* 58 */     return new CategoryWordTag(category, word, tag);
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
/*    */   public Label newLabel(Label oldLabel)
/*    */   {
/* 71 */     return new CategoryWordTag(oldLabel);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\CategoryWordTagFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */