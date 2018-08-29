/*    */ package edu.stanford.nlp.io;
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
/*    */ public class RuntimeIOException
/*    */   extends RuntimeException
/*    */ {
/*    */   public RuntimeIOException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RuntimeIOException(String message)
/*    */   {
/* 27 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RuntimeIOException(Throwable cause)
/*    */   {
/* 36 */     super(cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RuntimeIOException(String message, Throwable cause)
/*    */   {
/* 46 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\io\RuntimeIOException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */