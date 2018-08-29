/*    */ package edu.stanford.nlp.trees.international.pennchinese;
/*    */ 
/*    */ import edu.stanford.nlp.io.EncodingPrintWriter.out;
/*    */ import edu.stanford.nlp.process.AbstractTokenizer;
/*    */ import edu.stanford.nlp.process.Tokenizer;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.Reader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CHTBTokenizer
/*    */   extends AbstractTokenizer
/*    */ {
/* 19 */   private CHTBLexer lexer = null;
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
/*    */   public CHTBTokenizer(Reader r)
/*    */   {
/* 33 */     this.lexer = new CHTBLexer(r);
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
/* 44 */       int a = 0;
/* 45 */       while ((a = this.lexer.yylex()) == 0) {}
/*    */       
/*    */ 
/* 48 */       if (a == -1) {
/* 49 */         return null;
/*    */       }
/*    */       
/* 52 */       return this.lexer.match();
/*    */     }
/*    */     catch (IOException ioe) {}
/*    */     
/*    */ 
/* 57 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void main(String[] args)
/*    */     throws IOException
/*    */   {
/* 68 */     String encoding = args[1];
/* 69 */     Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), encoding));
/*    */     
/* 71 */     Tokenizer st = new CHTBTokenizer(in);
/*    */     
/* 73 */     while (st.hasNext()) {
/* 74 */       String s = (String)st.next();
/* 75 */       EncodingPrintWriter.out.println(s, encoding);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\CHTBTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */