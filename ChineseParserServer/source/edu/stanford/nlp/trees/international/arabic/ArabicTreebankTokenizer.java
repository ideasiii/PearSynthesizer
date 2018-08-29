/*    */ package edu.stanford.nlp.trees.international.arabic;
/*    */ 
/*    */ import edu.stanford.nlp.process.Tokenizer;
/*    */ import edu.stanford.nlp.trees.PennTreebankTokenizer;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.io.Reader;
/*    */ import java.io.StreamTokenizer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArabicTreebankTokenizer
/*    */   extends PennTreebankTokenizer
/*    */ {
/*    */   public ArabicTreebankTokenizer(Reader r)
/*    */   {
/* 20 */     super(r);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object getNext()
/*    */   {
/*    */     try
/*    */     {
/*    */       for (;;)
/*    */       {
/* 31 */         int nextToken = this.st.nextToken();
/* 32 */         switch (nextToken) {
/*    */         case 10: 
/* 34 */           return this.eolString;
/*    */         case -1: 
/* 36 */           return null;
/*    */         case -3: 
/* 38 */           if (this.st.sval.equals(":::")) {
/* 39 */             nextToken = this.st.nextToken();
/* 40 */             nextToken = this.st.nextToken();
/* 41 */             if (!this.st.sval.equals(":::")) {
/* 42 */               System.err.println("ArabicTreebankTokenizer assumptions broken!");
/*    */             }
/*    */           } else {
/* 45 */             return this.st.sval;
/*    */           }
/*    */           break;
/*    */         case -2: 
/* 49 */           return Double.toString(this.st.nval);
/*    */         default: 
/* 51 */           char[] t = { (char)nextToken };
/* 52 */           return new String(t);
/*    */         }
/*    */         
/*    */       }
/*    */       
/*    */ 
/* 58 */       return null;
/*    */     } catch (IOException ioe) {}
/*    */   }
/*    */   
/* 62 */   public static void main(String[] args) throws IOException { Tokenizer att = new ArabicTreebankTokenizer(new FileReader(args[0]));
/* 63 */     while (att.hasNext()) {
/* 64 */       System.out.println(att.next());
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\arabic\ArabicTreebankTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */