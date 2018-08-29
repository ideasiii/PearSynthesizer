/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.ling.FeatureLabel;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Word;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class PTBTokenizer<T>
/*     */   extends AbstractTokenizer<T>
/*     */ {
/*     */   private boolean tokenizeCRs;
/*     */   private boolean invertible;
/*     */   private boolean suppressEscaping;
/*     */   private PTBLexer lexer;
/*     */   private LexedTokenFactory<T> tokenFactory;
/*     */   
/*     */   public static PTBTokenizer<Word> newPTBTokenizer(Reader r)
/*     */   {
/*  45 */     return newPTBTokenizer(r, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PTBTokenizer<Word> newPTBTokenizer(Reader r, boolean tokenizeCRs)
/*     */   {
/*  54 */     return new PTBTokenizer(r, tokenizeCRs, new WordTokenFactory());
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
/*     */   public static PTBTokenizer<FeatureLabel> newPTBTokenizer(Reader r, boolean tokenizeCRs, boolean invertible)
/*     */   {
/*  67 */     return new PTBTokenizer(r, tokenizeCRs, invertible, new FeatureLabelTokenFactory());
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
/*     */ 
/*     */ 
/*     */   public PTBTokenizer(Reader r, boolean tokenizeCRs, LexedTokenFactory<T> tokenFactory)
/*     */   {
/*  83 */     this(r, tokenizeCRs, false, tokenFactory);
/*     */   }
/*     */   
/*     */ 
/*     */   private PTBTokenizer(Reader r, boolean tokenizeCRs, boolean invertible, LexedTokenFactory<T> tokenFactory)
/*     */   {
/*  89 */     this(r, tokenizeCRs, invertible, false, tokenFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private PTBTokenizer(Reader r, boolean tokenizeCRs, boolean invertible, boolean suppressEscaping, LexedTokenFactory<T> tokenFactory)
/*     */   {
/*  96 */     this.tokenizeCRs = tokenizeCRs;
/*  97 */     this.tokenFactory = tokenFactory;
/*  98 */     this.invertible = invertible;
/*  99 */     this.suppressEscaping = suppressEscaping;
/* 100 */     setSource(r);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected T getNext()
/*     */   {
/* 110 */     if (this.lexer == null) {
/* 111 */       return null;
/*     */     }
/* 113 */     T token = null;
/*     */     try {
/* 115 */       token = this.lexer.next();
/*     */       
/* 117 */       while ((!this.tokenizeCRs) && ("*CR*".equals(((HasWord)token).word()))) {
/* 118 */         token = this.lexer.next();
/*     */       }
/*     */     } catch (Exception e) {
/* 121 */       this.nextToken = null;
/*     */     }
/*     */     
/* 124 */     return token;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSource(Reader r)
/*     */   {
/* 131 */     if (this.invertible) {
/* 132 */       this.lexer = new PTBLexer(r, this.invertible, this.tokenizeCRs);
/*     */     } else {
/* 134 */       this.lexer = new PTBLexer(r, this.tokenFactory, this.tokenizeCRs, this.suppressEscaping);
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
/*     */   public static String ptb2Text(String ptbText)
/*     */   {
/* 147 */     StringBuilder sb = new StringBuilder(ptbText.length());
/* 148 */     PTB2TextLexer lexer = new PTB2TextLexer(new StringReader(ptbText));
/*     */     try { String token;
/* 150 */       while ((token = lexer.next()) != null) {
/* 151 */         sb.append(token);
/*     */       }
/*     */     } catch (IOException e) {
/* 154 */       e.printStackTrace();
/*     */     }
/* 156 */     return sb.toString();
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
/*     */   public static String ptb2Text(List ptbWords)
/*     */   {
/* 169 */     int i = 0; for (int sz = ptbWords.size(); i < sz; i++) {
/* 170 */       if ((ptbWords.get(i) instanceof Word)) {
/* 171 */         ptbWords.set(i, ((Word)ptbWords.get(i)).word());
/*     */       }
/*     */     }
/*     */     
/* 175 */     return ptb2Text(StringUtils.join(ptbWords));
/*     */   }
/*     */   
/*     */   public static TokenizerFactory<Word> factory() {
/* 179 */     return PTBTokenizerFactory.newPTBTokenizerFactory();
/*     */   }
/*     */   
/*     */   public static TokenizerFactory<Word> factory(boolean tokenizeCRs) {
/* 183 */     return PTBTokenizerFactory.newPTBTokenizerFactory(tokenizeCRs);
/*     */   }
/*     */   
/*     */   public static <T> TokenizerFactory<T> factory(boolean tokenizeCRs, LexedTokenFactory<T> factory)
/*     */   {
/* 188 */     return new PTBTokenizerFactory(tokenizeCRs, factory);
/*     */   }
/*     */   
/*     */   public static TokenizerFactory<FeatureLabel> factory(boolean tokenizeCRs, boolean invertible) {
/* 192 */     return PTBTokenizerFactory.newPTBTokenizerFactory(tokenizeCRs, invertible);
/*     */   }
/*     */   
/*     */   public static TokenizerFactory<Word> factory(boolean tokenizeCRs, boolean invertible, boolean suppressEscaping) {
/* 196 */     return PTBTokenizerFactory.newPTBTokenizerFactory(tokenizeCRs, invertible, suppressEscaping);
/*     */   }
/*     */   
/*     */   public static class PTBTokenizerFactory<T>
/*     */     implements TokenizerFactory<T>
/*     */   {
/*     */     protected boolean tokenizeCRs;
/*     */     protected boolean invertible;
/* 204 */     protected boolean suppressEscaping = false;
/*     */     
/*     */ 
/*     */     protected LexedTokenFactory<T> factory;
/*     */     
/*     */ 
/*     */     public static PTBTokenizerFactory<Word> newPTBTokenizerFactory()
/*     */     {
/* 212 */       return newPTBTokenizerFactory(false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static PTBTokenizerFactory<Word> newPTBTokenizerFactory(boolean tokenizeCRs)
/*     */     {
/* 221 */       return new PTBTokenizerFactory(tokenizeCRs, new WordTokenFactory());
/*     */     }
/*     */     
/*     */     public PTBTokenizerFactory(boolean tokenizeCRs, LexedTokenFactory<T> factory) {
/* 225 */       this(tokenizeCRs, false, false, factory);
/*     */     }
/*     */     
/*     */     public static PTBTokenizerFactory<FeatureLabel> newPTBTokenizerFactory(boolean tokenizeCRs, boolean invertible) {
/* 229 */       return new PTBTokenizerFactory(tokenizeCRs, invertible, new FeatureLabelTokenFactory());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public static PTBTokenizerFactory<Word> newPTBTokenizerFactory(boolean tokenizeCRs, boolean invertible, boolean suppressEscaping)
/*     */     {
/* 236 */       return new PTBTokenizerFactory(tokenizeCRs, invertible, suppressEscaping, new WordTokenFactory());
/*     */     }
/*     */     
/*     */     private PTBTokenizerFactory(boolean tokenizeCRs, boolean invertible, LexedTokenFactory<T> factory) {
/* 240 */       this(tokenizeCRs, invertible, false, factory);
/*     */     }
/*     */     
/*     */     private PTBTokenizerFactory(boolean tokenizeCRs, boolean invertible, boolean suppressEscaping, LexedTokenFactory<T> factory) {
/* 244 */       this.tokenizeCRs = tokenizeCRs;
/* 245 */       this.invertible = invertible;
/* 246 */       this.suppressEscaping = suppressEscaping;
/* 247 */       this.factory = factory;
/*     */     }
/*     */     
/*     */     public Iterator<T> getIterator(Reader r)
/*     */     {
/* 252 */       return getTokenizer(r);
/*     */     }
/*     */     
/*     */     public Tokenizer<T> getTokenizer(Reader r) {
/* 256 */       return new PTBTokenizer(r, this.tokenizeCRs, this.invertible, this.suppressEscaping, this.factory, null);
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
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/* 288 */     if (args.length < 1) {
/* 289 */       System.err.println("usage: java edu.stanford.nlp.process.PTBTokenizer [options]* filename+");
/* 290 */       System.err.println("  options: -nl|-preserveLines|-dump|-ioFileList|-charset|-parseInside");
/* 291 */       return;
/*     */     }
/* 293 */     int i = 0;
/* 294 */     String charset = "utf-8";
/* 295 */     Pattern parseInsideBegin = null;
/* 296 */     Pattern parseInsideEnd = null;
/* 297 */     boolean tokenizeNL = false;
/* 298 */     boolean preserveLines = false;
/* 299 */     boolean inputOutputFileList = false;
/* 300 */     boolean dump = false;
/*     */     
/* 302 */     while (args[i].charAt(0) == '-') {
/* 303 */       if ("-nl".equals(args[i])) {
/* 304 */         tokenizeNL = true;
/* 305 */       } else if ("-preserveLines".equals(args[i])) {
/* 306 */         preserveLines = true;
/* 307 */         tokenizeNL = true;
/* 308 */       } else if ("-dump".equals(args[i])) {
/* 309 */         dump = true;
/* 310 */       } else if ("-ioFileList".equals(args[i])) {
/* 311 */         inputOutputFileList = true;
/* 312 */       } else if (("-charset".equals(args[i])) && (i < args.length - 1)) {
/* 313 */         i++;
/* 314 */         charset = args[i];
/* 315 */       } else if (("-parseInside".equals(args[i])) && (i < args.length - 1)) {
/* 316 */         i++;
/*     */         try {
/* 318 */           parseInsideBegin = Pattern.compile("<(?:" + args[i] + ")>");
/* 319 */           parseInsideEnd = Pattern.compile("</(?:" + args[i] + ")>");
/*     */         } catch (Exception e) {
/* 321 */           parseInsideBegin = null;
/* 322 */           parseInsideEnd = null;
/*     */         }
/*     */       } else {
/* 325 */         System.err.println("Unknown option: " + args[i]);
/*     */       }
/* 327 */       i++;
/*     */     }
/* 329 */     ArrayList<String> inputFileList = new ArrayList();
/* 330 */     ArrayList<String> outputFileList = null;
/*     */     
/* 332 */     if (inputOutputFileList) {
/* 333 */       outputFileList = new ArrayList();
/* 334 */       for (int j = i; j < args.length; j++) {
/* 335 */         BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(args[j]), charset));
/*     */         String inLine;
/* 337 */         while ((inLine = r.readLine()) != null) {
/* 338 */           String[] fields = inLine.split("\\s+");
/* 339 */           inputFileList.add(fields[0]);
/* 340 */           outputFileList.add(fields[1]);
/*     */         }
/* 342 */         r.close();
/*     */       }
/*     */     } else {
/* 345 */       for (int j = i; j < args.length; j++) inputFileList.add(args[j]);
/*     */     }
/* 347 */     int j = 0; for (int sz = inputFileList.size(); j < sz; j++) {
/* 348 */       Reader r = new BufferedReader(new InputStreamReader(new FileInputStream((String)inputFileList.get(j)), charset));
/*     */       PrintWriter out;
/*     */       PrintWriter out;
/* 351 */       if (outputFileList == null) {
/* 352 */         out = new PrintWriter(System.out, true);
/*     */       } else {
/* 354 */         out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream((String)outputFileList.get(j)), charset)), true);
/*     */       }
/*     */       
/* 357 */       PTBTokenizer<FeatureLabel> tokenizer = newPTBTokenizer(r, tokenizeNL, true);
/* 358 */       boolean printing = true;
/* 359 */       if (parseInsideBegin != null) {
/* 360 */         printing = false;
/*     */       }
/* 362 */       boolean beginLine = true;
/* 363 */       while (tokenizer.hasNext()) {
/* 364 */         Object obj = tokenizer.next();
/* 365 */         String str = ((HasWord)obj).word();
/*     */         
/* 367 */         if ((parseInsideBegin != null) && (parseInsideBegin.matcher(str).matches())) {
/* 368 */           printing = true;
/* 369 */         } else if ((parseInsideEnd != null) && (parseInsideEnd.matcher(str).matches())) {
/* 370 */           printing = false;
/* 371 */         } else if (printing) {
/* 372 */           if (dump)
/*     */           {
/* 374 */             str = obj.toString();
/*     */           }
/* 376 */           if (preserveLines) {
/* 377 */             if ("*CR*".equals(str)) {
/* 378 */               beginLine = true;
/* 379 */               out.println("");
/*     */             } else {
/* 381 */               if (!beginLine) {
/* 382 */                 out.print(" ");
/*     */               } else {
/* 384 */                 beginLine = false;
/*     */               }
/* 386 */               out.print(str);
/*     */             }
/*     */           } else {
/* 389 */             out.println(str);
/*     */           }
/*     */         }
/*     */       }
/* 393 */       r.close();
/* 394 */       if (outputFileList != null) out.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\PTBTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */