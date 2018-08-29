/*    */ package edu.stanford.nlp.process;
/*    */ 
/*    */ import edu.stanford.nlp.io.Lexer;
/*    */ import edu.stanford.nlp.io.RuntimeIOException;
/*    */ import java.io.BufferedReader;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LexerTokenizer
/*    */   extends AbstractTokenizer<String>
/*    */ {
/*    */   private Lexer lexer;
/*    */   
/*    */   protected String getNext()
/*    */   {
/* 29 */     String token = null;
/*    */     try {
/* 31 */       int a = 0;
/* 32 */       while ((a = this.lexer.yylex()) == 0) {}
/*    */       
/*    */ 
/* 35 */       if (a == this.lexer.getYYEOF()) {
/* 36 */         token = null;
/*    */       } else {
/* 38 */         token = this.lexer.yytext();
/*    */       }
/*    */     }
/*    */     catch (IOException e) {}
/*    */     
/* 43 */     return token;
/*    */   }
/*    */   
/*    */ 
/*    */   public LexerTokenizer(Lexer l)
/*    */   {
/* 49 */     if (l == null) {
/* 50 */       throw new IllegalArgumentException("You can't make a Tokenizer out of a null Lexer!");
/*    */     }
/* 52 */     this.lexer = l;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public LexerTokenizer(Lexer l, Reader r)
/*    */   {
/* 60 */     this(l);
/*    */     try
/*    */     {
/* 63 */       l.yyreset(r);
/*    */     } catch (IOException e) {
/* 65 */       throw new RuntimeIOException(e.getMessage());
/*    */     }
/*    */     
/* 68 */     getNext();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static void main(String[] args)
/*    */     throws IOException
/*    */   {
/* 76 */     Tokenizer<String> t = new LexerTokenizer(new JFlexDummyLexer((Reader)null), new BufferedReader(new FileReader(args[0])));
/* 77 */     while (t.hasNext()) {
/* 78 */       System.out.println("token " + (String)t.next());
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\LexerTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */