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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WordFactory
/*    */   implements LabelFactory
/*    */ {
/*    */   public Label newLabel(String word)
/*    */   {
/* 27 */     return new Word(word);
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
/*    */   public Label newLabel(String word, int options)
/*    */   {
/* 40 */     return new Word(word);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Label newLabelFromString(String word)
/*    */   {
/* 52 */     return new Word(word);
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
/* 66 */     return new Word(oldLabel);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\WordFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */