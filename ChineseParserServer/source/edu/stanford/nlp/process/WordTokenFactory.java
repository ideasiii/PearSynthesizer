/*    */ package edu.stanford.nlp.process;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Word;
/*    */ 
/*    */ 
/*    */ public class WordTokenFactory
/*    */   implements LexedTokenFactory<Word>
/*    */ {
/*    */   public Word makeToken(String str, int begin, int length)
/*    */   {
/* 11 */     return new Word(str);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\WordTokenFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */