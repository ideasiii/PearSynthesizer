/*    */ package edu.stanford.nlp.ling;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Word
/*    */   extends StringLabel
/*    */   implements HasWord
/*    */ {
/*    */   public static final String EMPTYSTRING = "*t*";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 22 */   public static final Word EMPTY = new Word("*t*");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Word() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Word(String word)
/*    */   {
/* 38 */     super(word);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Word(Label lab)
/*    */   {
/* 49 */     super(lab);
/*    */   }
/*    */   
/*    */   public String word()
/*    */   {
/* 54 */     return value();
/*    */   }
/*    */   
/*    */   public void setWord(String word)
/*    */   {
/* 59 */     setValue(word);
/*    */   }
/*    */   
/*    */ 
/*    */   private static class WordFactoryHolder
/*    */   {
/* 65 */     private static final LabelFactory lf = new WordFactory();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LabelFactory labelFactory()
/*    */   {
/* 76 */     return WordFactoryHolder.lf;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static LabelFactory factory()
/*    */   {
/* 86 */     return WordFactoryHolder.lf;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\Word.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */