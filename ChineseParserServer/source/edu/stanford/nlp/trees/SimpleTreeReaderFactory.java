/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import java.io.Reader;
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
/*    */ public class SimpleTreeReaderFactory
/*    */   implements TreeReaderFactory
/*    */ {
/*    */   public TreeReader newTreeReader(Reader in)
/*    */   {
/* 25 */     return new PennTreeReader(in);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\SimpleTreeReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */