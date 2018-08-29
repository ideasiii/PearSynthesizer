/*     */ package edu.stanford.nlp.trees.international.arabic;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Word;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.process.AbstractTokenizer;
/*     */ import edu.stanford.nlp.process.Tokenizer;
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
/*     */ public class ArabicTokenizer
/*     */   extends AbstractTokenizer<Word>
/*     */ {
/*     */   private ArabicLexer lexer;
/*     */   private final boolean eolIsSignificant;
/*     */   
/*     */   static class ArabicTokenizerFactory
/*     */     implements TokenizerFactory<Word>
/*     */   {
/*     */     private final boolean eolIsSignificant;
/*     */     
/*     */     public ArabicTokenizerFactory()
/*     */     {
/*  32 */       this(false);
/*     */     }
/*     */     
/*     */     public ArabicTokenizerFactory(boolean eolIsSignificant) {
/*  36 */       this.eolIsSignificant = eolIsSignificant;
/*     */     }
/*     */     
/*     */     public Iterator<Word> getIterator(Reader r) {
/*  40 */       return getTokenizer(r);
/*     */     }
/*     */     
/*     */     public Tokenizer<Word> getTokenizer(Reader r) {
/*  44 */       return new ArabicTokenizer(r, this.eolIsSignificant);
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
/*  58 */     Word token = null;
/*  59 */     if (this.lexer == null) {
/*  60 */       return token;
/*     */     }
/*     */     try {
/*  63 */       token = this.lexer.next();
/*  64 */       while (token == ArabicLexer.crValue) {
/*  65 */         if (this.eolIsSignificant) {
/*  66 */           return token;
/*     */         }
/*  68 */         token = this.lexer.next();
/*     */       }
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/*     */ 
/*  74 */     return token;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArabicTokenizer(Reader r)
/*     */   {
/*  83 */     this(r, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArabicTokenizer(Reader r, boolean eolIsSignificant)
/*     */   {
/*  93 */     this.eolIsSignificant = eolIsSignificant;
/*     */     
/*     */ 
/*     */ 
/*  97 */     if (r != null) {
/*  98 */       this.lexer = new ArabicLexer(r);
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
/* 111 */     return new ArabicTokenizerFactory(false);
/*     */   }
/*     */   
/*     */   public static TokenizerFactory<Word> factory(boolean eolIsSignificant) {
/* 115 */     return new ArabicTokenizerFactory(eolIsSignificant);
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
/* 130 */     if (args.length < 1) {
/* 131 */       System.err.println("usage: java edu.stanford.nlp.process.ArabicTokenizer [-cr] filename");
/* 132 */       return;
/*     */     }
/* 134 */     ArabicTokenizer tokenizer = new ArabicTokenizer(new InputStreamReader(new FileInputStream(args[(args.length - 1)]), "UTF-8"), args[0].equals("-cr"));
/* 135 */     PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true);
/* 136 */     while (tokenizer.hasNext()) {
/* 137 */       Word w = (Word)tokenizer.next();
/* 138 */       if (w == ArabicLexer.crValue) {
/* 139 */         pw.println("***CR***");
/*     */       } else {
/* 141 */         pw.println(w);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\arabic\ArabicTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */