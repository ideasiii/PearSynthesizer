/*    */ package edu.stanford.nlp.process;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class TokenizerAdapter
/*    */   extends AbstractTokenizer
/*    */ {
/*    */   protected StreamTokenizer st;
/* 18 */   protected String eolString = "<EOL>";
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
/*    */   public TokenizerAdapter(StreamTokenizer st)
/*    */   {
/* 31 */     this.st = st;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object getNext()
/*    */   {
/*    */     try
/*    */     {
/* 42 */       int nextToken = this.st.nextToken();
/* 43 */       switch (nextToken) {
/*    */       case 10: 
/* 45 */         return this.eolString;
/*    */       case -1: 
/* 47 */         return null;
/*    */       case -3: 
/* 49 */         return this.st.sval;
/*    */       case -2: 
/* 51 */         return Double.toString(this.st.nval);
/*    */       }
/* 53 */       char[] t = { (char)nextToken };
/* 54 */       return new String(t);
/*    */     }
/*    */     catch (IOException ioe) {}
/*    */     
/*    */ 
/* 59 */     return null;
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
/*    */   public void setEolString(String eolString)
/*    */   {
/* 72 */     if (eolString == null) {
/* 73 */       throw new IllegalArgumentException("eolString cannot be null");
/*    */     }
/* 75 */     this.eolString = eolString;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isEol(String str)
/*    */   {
/* 87 */     return this.eolString.equals(str);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\TokenizerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */