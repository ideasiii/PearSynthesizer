/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractIterator<E>
/*    */   implements Iterator<E>
/*    */ {
/*    */   public abstract boolean hasNext();
/*    */   
/*    */   public abstract E next();
/*    */   
/*    */   public void remove()
/*    */   {
/* 19 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\AbstractIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */