/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Word;
/*     */ import edu.stanford.nlp.ling.WordLemmaTag;
/*     */ import edu.stanford.nlp.ling.WordTag;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
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
/*     */ public class Morphology
/*     */   implements Function
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private Morpha lexer;
/*     */   private static Morpha staticLexer;
/*     */   private static final long serialVersionUID = 2L;
/*     */   
/*     */   public Morphology()
/*     */   {
/*  44 */     this.lexer = new Morpha(System.in);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Morphology(Reader in)
/*     */   {
/*  51 */     this.lexer = new Morpha(in);
/*     */   }
/*     */   
/*     */   public Morphology(String filename) {
/*     */     try {
/*  56 */       this.lexer = new Morpha(new FileReader(filename));
/*     */     } catch (Exception e) {
/*  58 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public Word next() throws IOException {
/*  63 */     String nx = this.lexer.next();
/*  64 */     if (nx == null) {
/*  65 */       return null;
/*     */     }
/*  67 */     return new Word(nx);
/*     */   }
/*     */   
/*     */   static boolean isProper(String posTag)
/*     */   {
/*  72 */     return (posTag.equals("NNP")) || (posTag.equals("NNPS")) || (posTag.equals("NP"));
/*     */   }
/*     */   
/*     */   public Word stem(Word w) {
/*     */     try {
/*  77 */       this.lexer.yyreset(new StringReader(w.value()));
/*  78 */       this.lexer.yybegin(3);
/*  79 */       String wordRes = this.lexer.next();
/*  80 */       return new Word(wordRes);
/*     */     } catch (Exception e) {
/*  82 */       e.printStackTrace();
/*     */     }
/*  84 */     return w;
/*     */   }
/*     */   
/*     */   public WordTag stem(WordTag wT) {
/*  88 */     return stem(wT.word(), wT.tag());
/*     */   }
/*     */   
/*     */   public WordTag stem(String word, String tag) {
/*  92 */     return stem(word, tag, this.lexer, this.lexer.option(1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static WordTag stem(String word, String tag, Morpha lexer, boolean lowercase)
/*     */   {
/* 103 */     boolean wordHasForbiddenChar = (word.indexOf("_") >= 0) || (word.indexOf(" ") >= 0);
/*     */     
/* 105 */     String quotedWord = word;
/* 106 */     if (wordHasForbiddenChar) {
/*     */       try
/*     */       {
/* 109 */         quotedWord = quotedWord.replaceAll("_", "ॠ");
/* 110 */         quotedWord = quotedWord.replaceAll(" ", "ॡ");
/*     */       } catch (Exception e) {
/* 112 */         System.err.println("stem: Didn't work");
/*     */       }
/*     */     }
/* 115 */     String wordtag = quotedWord + "_" + tag;
/*     */     try
/*     */     {
/* 118 */       lexer.setOption(1, lowercase);
/* 119 */       lexer.yyreset(new StringReader(wordtag));
/* 120 */       lexer.yybegin(4);
/* 121 */       String wordRes = lexer.next();
/* 122 */       String tagRes = lexer.next();
/* 123 */       if (wordHasForbiddenChar) {
/*     */         try
/*     */         {
/* 126 */           wordRes = wordRes.replaceAll("ॠ", "_");
/* 127 */           wordRes = wordRes.replaceAll("ॡ", " ");
/*     */         } catch (Exception e) {
/* 129 */           System.err.println("stem: Didn't work");
/*     */         }
/*     */       }
/* 132 */       return new WordTag(wordRes, tag);
/*     */     } catch (Throwable e) {
/* 134 */       System.err.println("Morphology.stem() had error on word " + word + "/" + tag);
/*     */       
/* 136 */       e.printStackTrace(); }
/* 137 */     return new WordTag(word, tag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static WordTag stemStatic(String word, String tag)
/*     */   {
/* 147 */     if (staticLexer == null) {
/* 148 */       staticLexer = new Morpha(System.in);
/*     */     }
/* 150 */     return stem(word, tag, staticLexer, staticLexer.option(1));
/*     */   }
/*     */   
/*     */   public static WordTag stemStatic(String word, String tag, boolean lowercase)
/*     */   {
/* 155 */     if (staticLexer == null) {
/* 156 */       staticLexer = new Morpha(System.in);
/*     */     }
/* 158 */     return stem(word, tag, staticLexer, lowercase);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static WordTag stemStatic(WordTag wT)
/*     */   {
/* 166 */     return stemStatic(wT.word(), wT.tag());
/*     */   }
/*     */   
/*     */   public Object apply(Object in)
/*     */   {
/* 171 */     if ((in instanceof WordTag)) {
/* 172 */       return stem((WordTag)in);
/*     */     }
/* 174 */     if ((in instanceof Word)) {
/* 175 */       return stem((Word)in);
/*     */     }
/* 177 */     return in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public WordLemmaTag lemmatize(WordTag wT)
/*     */   {
/* 184 */     String tag = wT.tag();
/* 185 */     String word = wT.word();
/* 186 */     String lemma = stem(wT).word();
/* 187 */     return new WordLemmaTag(word, lemma, tag);
/*     */   }
/*     */   
/*     */   public static WordLemmaTag lemmatizeStatic(WordTag wT) {
/* 191 */     String tag = wT.tag();
/* 192 */     String word = wT.word();
/* 193 */     String lemma = stemStatic(wT).word();
/* 194 */     return new WordLemmaTag(word, lemma, tag);
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
/* 209 */     if (args.length == 0) {
/* 210 */       System.err.println("java Morphology [-rebuildVerbTable file|-stem word+|file+]");
/* 211 */     } else if ((args.length == 2) && (args[0].equals("-rebuildVerbTable"))) {
/* 212 */       String verbs = StringUtils.slurpFile(args[1]);
/* 213 */       String[] words = verbs.split("\\s+");
/* 214 */       System.out.print(" private static String[] verbStems = new String[] { ");
/* 215 */       for (int i = 0; i < words.length; i++) {
/* 216 */         System.out.print("\"" + words[i] + "\"");
/* 217 */         if (i != words.length - 1) {
/* 218 */           System.out.print(", ");
/* 219 */           if (i % 5 == 0) {
/* 220 */             System.out.println();
/* 221 */             System.out.print("    ");
/*     */           }
/*     */         }
/*     */       }
/* 225 */       System.out.println(" };");
/* 226 */     } else if (args[0].equals("-stem")) {
/* 227 */       for (int i = 1; i < args.length; i++) {
/* 228 */         System.out.println(args[i] + " --> " + stemStatic(WordTag.valueOf(args[i])));
/*     */       }
/*     */     } else {
/* 231 */       for (String arg : args) {
/* 232 */         Morphology morph = new Morphology(arg);
/*     */         Word next;
/* 234 */         while ((next = morph.next()) != null) {
/* 235 */           System.out.print(next);
/* 236 */           System.out.print(" ");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\Morphology.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */