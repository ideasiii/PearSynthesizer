/*    */ package edu.stanford.nlp.process;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public abstract class AbstractTokenizer<T>
/*    */   implements Tokenizer<T>
/*    */ {
/* 19 */   protected T nextToken = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected abstract T getNext();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public T next()
/*    */   {
/* 36 */     if (this.nextToken == null) {
/* 37 */       this.nextToken = getNext();
/*    */     }
/* 39 */     T result = this.nextToken;
/* 40 */     this.nextToken = getNext();
/* 41 */     if (result == null) {
/* 42 */       throw new NoSuchElementException();
/*    */     }
/* 44 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 51 */     if (this.nextToken == null) {
/* 52 */       this.nextToken = getNext();
/*    */     }
/* 54 */     return this.nextToken != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void remove()
/*    */   {
/* 61 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public T peek()
/*    */   {
/* 72 */     if (this.nextToken == null) {
/* 73 */       this.nextToken = getNext();
/*    */     }
/* 75 */     if (this.nextToken == null) {
/* 76 */       throw new NoSuchElementException();
/*    */     }
/* 78 */     return (T)this.nextToken;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<T> tokenize()
/*    */   {
/* 86 */     List<T> result = new ArrayList();
/* 87 */     while (hasNext()) {
/* 88 */       result.add(next());
/*    */     }
/* 90 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\AbstractTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */