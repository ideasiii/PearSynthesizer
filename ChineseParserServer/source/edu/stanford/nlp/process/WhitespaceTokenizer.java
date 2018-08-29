/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Word;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WhitespaceTokenizer
/*     */   extends AbstractTokenizer<Word>
/*     */ {
/*     */   private WhitespaceLexer lexer;
/*     */   private final boolean eolIsSignificant;
/*     */   
/*     */   static class WhitespaceTokenizerFactory
/*     */     implements TokenizerFactory<Word>
/*     */   {
/*     */     private final boolean eolIsSignificant;
/*     */     
/*     */     public WhitespaceTokenizerFactory()
/*     */     {
/*  37 */       this(false);
/*     */     }
/*     */     
/*     */     public WhitespaceTokenizerFactory(boolean eolIsSignificant) {
/*  41 */       this.eolIsSignificant = eolIsSignificant;
/*     */     }
/*     */     
/*     */     public Iterator<Word> getIterator(Reader r) {
/*  45 */       return getTokenizer(r);
/*     */     }
/*     */     
/*     */     public Tokenizer<Word> getTokenizer(Reader r) {
/*  49 */       return new WhitespaceTokenizer(r, this.eolIsSignificant);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Word getNext()
/*     */   {
/*  63 */     Word token = null;
/*  64 */     if (this.lexer == null) {
/*  65 */       return token;
/*     */     }
/*     */     try {
/*  68 */       token = this.lexer.next();
/*  69 */       while (token == WhitespaceLexer.crValue) {
/*  70 */         if (this.eolIsSignificant) {
/*  71 */           return token;
/*     */         }
/*  73 */         token = this.lexer.next();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/*     */ 
/*  79 */     return token;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WhitespaceTokenizer(Reader r)
/*     */   {
/*  88 */     this(r, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WhitespaceTokenizer(Reader r, boolean eolIsSignificant)
/*     */   {
/*  98 */     this.eolIsSignificant = eolIsSignificant;
/*     */     
/*     */ 
/*     */ 
/* 102 */     if (r != null) {
/* 103 */       this.lexer = new WhitespaceLexer(r);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TokenizerFactory<Word> factory()
/*     */   {
/* 116 */     return new WhitespaceTokenizerFactory(false);
/*     */   }
/*     */   
/*     */   public static TokenizerFactory<Word> factory(boolean eolIsSignificant) {
/* 120 */     return new WhitespaceTokenizerFactory(eolIsSignificant);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/* 135 */     if (args.length < 1) {
/* 136 */       System.err.println("usage: java edu.stanford.nlp.process.WhitespaceTokenizer [-cr] filename");
/* 137 */       return;
/*     */     }
/* 139 */     WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(new InputStreamReader(new FileInputStream(args[(args.length - 1)]), "UTF-8"), args[0].equals("-cr"));
/* 140 */     PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true);
/* 141 */     while (tokenizer.hasNext()) {
/* 142 */       Word w = (Word)tokenizer.next();
/* 143 */       if (w == WhitespaceLexer.crValue) {
/* 144 */         pw.println("***CR***");
/*     */       } else {
/* 146 */         pw.println(w);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\WhitespaceTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */