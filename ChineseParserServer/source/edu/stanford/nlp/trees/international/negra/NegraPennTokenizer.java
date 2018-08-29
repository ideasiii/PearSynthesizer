/*    */ package edu.stanford.nlp.trees.international.negra;
/*    */ 
/*    */ import edu.stanford.nlp.process.LexerTokenizer;
/*    */ import edu.stanford.nlp.process.Tokenizer;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.io.Reader;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NegraPennTokenizer
/*    */   extends LexerTokenizer
/*    */ {
/*    */   public NegraPennTokenizer(Reader r)
/*    */   {
/* 20 */     super(new NegraPennLexer(r));
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */     throws IOException
/*    */   {
/* 26 */     Reader in = new FileReader(args[0]);
/* 27 */     Tokenizer st = new NegraPennTokenizer(in);
/*    */     
/* 29 */     while (st.hasNext()) {
/* 30 */       String s = (String)st.next();
/* 31 */       System.out.println(s);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\negra\NegraPennTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */