/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.process.TokenizerAdapter;
/*    */ import java.io.Reader;
/*    */ import java.io.StreamTokenizer;
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
/*    */ public class PennTreebankTokenizer
/*    */   extends TokenizerAdapter
/*    */ {
/*    */   private static class EnglishTreebankStreamTokenizer
/*    */     extends StreamTokenizer
/*    */   {
/*    */     private EnglishTreebankStreamTokenizer(Reader r)
/*    */     {
/* 31 */       super();
/*    */       
/* 33 */       resetSyntax();
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 41 */       wordChars(33, 39);
/* 42 */       wordChars(42, 47);
/* 43 */       wordChars(48, 57);
/* 44 */       wordChars(58, 64);
/* 45 */       wordChars(65, 90);
/* 46 */       wordChars(91, 96);
/* 47 */       wordChars(97, 122);
/* 48 */       wordChars(123, 126);
/* 49 */       wordChars(128, 255);
/*    */       
/*    */ 
/*    */ 
/* 53 */       whitespaceChars(0, 32);
/*    */     }
/*    */   }
/*    */   
/*    */   public PennTreebankTokenizer(Reader r) {
/* 58 */     super(new EnglishTreebankStreamTokenizer(r, null));
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\PennTreebankTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */