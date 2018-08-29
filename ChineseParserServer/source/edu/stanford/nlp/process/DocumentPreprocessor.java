/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Word;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.objectbank.XMLBeginEndIterator;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import edu.stanford.nlp.web.HTMLParser;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DocumentPreprocessor
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private TokenizerFactory tokenizerFactory;
/*     */   private String encoding;
/*     */   private String[] sentenceFinalPuncWords;
/*     */   
/*     */   public DocumentPreprocessor(TokenizerFactory tokenizerFactory)
/*     */   {
/*  37 */     this.tokenizerFactory = tokenizerFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DocumentPreprocessor()
/*     */   {
/*  44 */     this.tokenizerFactory = PTBTokenizer.factory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DocumentPreprocessor(boolean suppressEscaping)
/*     */   {
/*  55 */     this.tokenizerFactory = PTBTokenizer.factory(false, false, suppressEscaping);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/*  64 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */   public void setSentenceFinalPuncWords(String[] sentenceFinalPuncWords)
/*     */   {
/*  69 */     this.sentenceFinalPuncWords = sentenceFinalPuncWords;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTokenizerFactory(TokenizerFactory newTokenizerFactory)
/*     */   {
/*  79 */     this.tokenizerFactory = newTokenizerFactory;
/*     */   }
/*     */   
/*     */   public void usePTBTokenizer() {
/*  83 */     this.tokenizerFactory = PTBTokenizer.factory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void useWhitespaceTokenizer()
/*     */   {
/*  90 */     this.tokenizerFactory = WhitespaceTokenizer.factory();
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
/*     */   public List<Word> getWordsFromText(String fileOrURL)
/*     */     throws IOException
/*     */   {
/* 105 */     return getWordsFromText(fileOrURLToReader(fileOrURL));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Word> getWordsFromText(Reader input)
/*     */   {
/* 113 */     Tokenizer tokenizer = this.tokenizerFactory.getTokenizer(new BufferedReader(input));
/* 114 */     return tokenizer.tokenize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<List<? extends HasWord>> getSentencesFromText(String fileOrURL)
/*     */     throws IOException
/*     */   {
/* 125 */     return getSentencesFromText(fileOrURLToReader(fileOrURL));
/*     */   }
/*     */   
/*     */   public List<List<? extends HasWord>> getSentencesFromText(String fileOrURL, boolean doPTBEscaping, String sentenceDelimiter, int tagDelimiter) throws IOException {
/* 129 */     return getSentencesFromText(fileOrURLToReader(fileOrURL), doPTBEscaping, sentenceDelimiter, tagDelimiter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<List<? extends HasWord>> getSentencesFromText(Reader input)
/*     */   {
/* 137 */     return getSentencesFromText(input, false, null, -1);
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
/*     */   public List<List<? extends HasWord>> getSentencesFromText(String input, Function<List<HasWord>, List<HasWord>> escaper, String sentenceDelimiter, int tagDelimiter)
/*     */     throws IOException
/*     */   {
/* 152 */     return getSentencesFromText(fileOrURLToReader(input), escaper, sentenceDelimiter, tagDelimiter);
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
/*     */   public List<List<? extends HasWord>> getSentencesFromText(Reader input, Function<List<HasWord>, List<HasWord>> escaper, String sentenceDelimiter, int tagDelimiter)
/*     */   {
/* 176 */     if (escaper == null) {
/* 177 */       escaper = new NullEscaper(null);
/*     */     }
/* 179 */     ListEscaper listEscaper = new ListEscaper(escaper);
/*     */     
/* 181 */     if ((this.tokenizerFactory instanceof WhitespaceTokenizer.WhitespaceTokenizerFactory))
/*     */     {
/* 183 */       if (sentenceDelimiter == null)
/*     */       {
/* 185 */         Tokenizer tokenizer = new WhitespaceTokenizer(input, false);
/* 186 */         List<HasWord> words = tokenizer.tokenize();
/* 187 */         if (tagDelimiter >= 0)
/*     */         {
/* 189 */           WordToTaggedWordProcessor wttwp = new WordToTaggedWordProcessor((char)tagDelimiter);
/* 190 */           words = wttwp.process(words);
/*     */         }
/* 192 */         words = (List)escaper.apply(words);
/*     */         WordToSentenceProcessor sp;
/*     */         WordToSentenceProcessor sp;
/* 195 */         if (this.sentenceFinalPuncWords != null) {
/* 196 */           sp = new WordToSentenceProcessor(new HashSet(Arrays.asList(this.sentenceFinalPuncWords)));
/*     */         } else {
/* 198 */           sp = new WordToSentenceProcessor();
/*     */         }
/*     */         
/* 201 */         return sp.process(words);
/*     */       }
/*     */       
/* 204 */       Tokenizer tokenizer = new WhitespaceTokenizer(input, sentenceDelimiter.equals("\n"));
/* 205 */       List<HasWord> words = tokenizer.tokenize();
/* 206 */       List sentences = splitListsOnToken(words, sentenceDelimiter);
/* 207 */       if (tagDelimiter >= 0) {
/* 208 */         sentences = tagSplitSentences(sentences, tagDelimiter);
/*     */       }
/* 210 */       sentences = listEscaper.apply(sentences);
/* 211 */       return sentences;
/*     */     }
/*     */     
/*     */ 
/* 215 */     if (tagDelimiter >= 0) {
/* 216 */       throw new RuntimeException("Can't read tags from untokenized document.");
/*     */     }
/*     */     
/* 219 */     if (sentenceDelimiter == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 224 */       Tokenizer tokenizer = this.tokenizerFactory.getTokenizer(new BufferedReader(input));
/* 225 */       List words = tokenizer.tokenize();
/* 226 */       words = (List)escaper.apply(words);
/*     */       WordToSentenceProcessor sp;
/*     */       WordToSentenceProcessor sp;
/* 229 */       if (this.sentenceFinalPuncWords != null) {
/* 230 */         sp = new WordToSentenceProcessor(new HashSet(Arrays.asList(this.sentenceFinalPuncWords)));
/*     */       } else {
/* 232 */         sp = new WordToSentenceProcessor();
/*     */       }
/*     */       
/* 235 */       return sp.process(words);
/*     */     }
/*     */     
/*     */ 
/* 239 */     Tokenizer tokenizer = new WhitespaceTokenizer(input, true);
/* 240 */     List tokens = tokenizer.tokenize();
/* 241 */     List<String> sentences = glueSentences(splitListsOnToken(tokens, sentenceDelimiter));
/*     */     
/* 243 */     return tokenizeSentences(sentences);
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
/*     */   public List<Word> getWordsFromString(String input)
/*     */   {
/* 256 */     Tokenizer tokenizer = this.tokenizerFactory.getTokenizer(new BufferedReader(new StringReader(input)));
/* 257 */     return tokenizer.tokenize();
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
/*     */   public List<List<? extends HasWord>> getSentencesFromXML(String fileOrURL, String splitOnTag)
/*     */     throws IOException
/*     */   {
/* 277 */     return getSentencesFromXML(fileOrURL, splitOnTag, null, true);
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
/*     */   public List<List<? extends HasWord>> getSentencesFromXML(String fileOrURL, String splitOnTag, boolean doPTBEscaping)
/*     */     throws IOException
/*     */   {
/* 292 */     return getSentencesFromXML(fileOrURL, splitOnTag, null, doPTBEscaping);
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
/*     */   public List<List<? extends HasWord>> getSentencesFromXML(String fileOrURL, String splitOnTag, String sentenceDelimiter, boolean doPTBEscaping)
/*     */     throws IOException
/*     */   {
/* 306 */     return getSentencesFromXML(fileOrURLToReader(fileOrURL), splitOnTag, sentenceDelimiter, doPTBEscaping);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<List<? extends HasWord>> getSentencesFromXML(Reader input, String splitOnTag, String sentenceDelimiter, boolean doPTBEscaping)
/*     */   {
/*     */     Function<List<HasWord>, List<HasWord>> escaper;
/*     */     
/*     */ 
/*     */     Function<List<HasWord>, List<HasWord>> escaper;
/*     */     
/*     */ 
/* 320 */     if (doPTBEscaping) {
/* 321 */       escaper = new PTBEscapingProcessor();
/*     */     } else {
/* 323 */       escaper = new NullEscaper(null);
/*     */     }
/* 325 */     return getSentencesFromXML(input, escaper, splitOnTag, sentenceDelimiter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<List<? extends HasWord>> getSentencesFromXML(String fileOrURL, Function<List<HasWord>, List<HasWord>> escaper, String splitOnTag)
/*     */     throws IOException
/*     */   {
/* 338 */     return getSentencesFromXML(fileOrURLToReader(fileOrURL), escaper, splitOnTag, null);
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
/*     */   public List<List<? extends HasWord>> getSentencesFromXML(String fileOrURL, Function<List<HasWord>, List<HasWord>> escaper, String splitOnTag, String sentenceDelimiter)
/*     */     throws IOException
/*     */   {
/* 356 */     return getSentencesFromXML(fileOrURLToReader(fileOrURL), escaper, splitOnTag, sentenceDelimiter);
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
/*     */   public List<List<? extends HasWord>> getSentencesFromXML(Reader input, Function<List<HasWord>, List<HasWord>> escaper, String splitOnTag, String sentenceDelimiter)
/*     */   {
/* 374 */     XMLBeginEndIterator xmlIter = new XMLBeginEndIterator(input, splitOnTag);
/* 375 */     List<List<? extends HasWord>> lis = new ArrayList();
/* 376 */     if ("onePerElement".equals(sentenceDelimiter)) {
/* 377 */       sentenceDelimiter = ".$.onePerElement.$.";
/*     */     }
/*     */     
/* 380 */     while (xmlIter.hasNext())
/*     */     {
/* 382 */       String s = (String)xmlIter.next();
/*     */       
/* 384 */       List<List<? extends HasWord>> section = getSentencesFromText(new BufferedReader(new StringReader(s)), escaper, sentenceDelimiter, -1);
/*     */       
/* 386 */       for (List<? extends HasWord> individual : section) {
/* 387 */         lis.add(individual);
/*     */       }
/*     */     }
/* 390 */     return lis;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Word> getWordsFromHTML(String fileOrURL)
/*     */     throws IOException
/*     */   {
/* 399 */     return getWordsFromHTML(fileOrURLToReader(fileOrURL));
/*     */   }
/*     */   
/*     */   public List<Word> getWordsFromHTML(Reader input) {
/* 403 */     HTMLParser parser = new HTMLParser();
/*     */     try {
/* 405 */       String s = parser.parse(input);
/* 406 */       return getWordsFromText(new StringReader(s));
/*     */     } catch (IOException e) {
/* 408 */       System.err.println("IOException" + e.getMessage());
/*     */     }
/* 410 */     return null;
/*     */   }
/*     */   
/*     */   public List<List<? extends HasWord>> getSentencesFromHTML(String fileOrURL) throws IOException {
/* 414 */     return getSentencesFromHTML(fileOrURLToReader(fileOrURL));
/*     */   }
/*     */   
/*     */   public List<List<? extends HasWord>> getSentencesFromHTML(Reader input) {
/* 418 */     HTMLParser parser = new HTMLParser();
/*     */     try {
/* 420 */       String s = parser.parse(input);
/* 421 */       return getSentencesFromText(new StringReader(s));
/*     */     } catch (IOException e) {
/* 423 */       System.err.println("IOException" + e.getMessage());
/*     */     }
/* 425 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<List<? extends HasWord>> getSentencesFromText(Reader fileOrURL, boolean doPTBEscaping, String sentenceDelimiter, int tagDelimiter)
/*     */   {
/*     */     Function<List<HasWord>, List<HasWord>> escaper;
/*     */     
/*     */     Function<List<HasWord>, List<HasWord>> escaper;
/*     */     
/* 436 */     if (doPTBEscaping) {
/* 437 */       escaper = new PTBEscapingProcessor();
/*     */     } else {
/* 439 */       escaper = new NullEscaper(null);
/*     */     }
/* 441 */     return getSentencesFromText(fileOrURL, escaper, sentenceDelimiter, tagDelimiter);
/*     */   }
/*     */   
/*     */   private static class NullEscaper implements Function<List<HasWord>, List<HasWord>>
/*     */   {
/*     */     public List<HasWord> apply(List<HasWord> hasWords) {
/* 447 */       return hasWords;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ListEscaper implements Function<List<List<HasWord>>, List<List<HasWord>>> {
/*     */     Function<List<HasWord>, List<HasWord>> f;
/*     */     
/* 454 */     public ListEscaper(Function<List<HasWord>, List<HasWord>> f) { this.f = f; }
/*     */     
/*     */ 
/*     */ 
/*     */     public List<List<HasWord>> apply(List<List<HasWord>> lists)
/*     */     {
/* 460 */       List<List<HasWord>> result = new ArrayList(lists.size());
/* 461 */       for (List<HasWord> l : lists) {
/* 462 */         result.add(this.f.apply(l));
/*     */       }
/* 464 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static List<List<HasWord>> splitListsOnToken(List<HasWord> tokens, String sentenceDelimiter)
/*     */   {
/* 473 */     List<List<HasWord>> result = new ArrayList();
/* 474 */     List<HasWord> sentence = new ArrayList();
/* 475 */     for (HasWord word : tokens) {
/* 476 */       if (word.word().equals(sentenceDelimiter))
/*     */       {
/*     */ 
/* 479 */         result.add(sentence);
/* 480 */         sentence = new ArrayList();
/*     */       } else {
/* 482 */         sentence.add(word);
/*     */       }
/*     */     }
/* 485 */     if (!sentence.isEmpty()) {
/* 486 */       result.add(sentence);
/*     */     }
/* 488 */     return result;
/*     */   }
/*     */   
/*     */   private static List<String> glueSentences(List<List<HasWord>> sentences) {
/* 492 */     List<String> result = new ArrayList();
/* 493 */     for (List<HasWord> sentence : sentences) {
/* 494 */       result.add(glueSentence(sentence));
/*     */     }
/* 496 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String glueSentence(List<HasWord> sentence)
/*     */   {
/* 506 */     StringBuilder result = new StringBuilder();
/* 507 */     if (!sentence.isEmpty()) {
/* 508 */       HasWord word = (HasWord)sentence.get(0);
/* 509 */       String s = word.word();
/* 510 */       result.append(s);
/* 511 */       int i = 1; for (int sz = sentence.size(); i < sz; i++) {
/* 512 */         word = (HasWord)sentence.get(i);
/* 513 */         s = word.word();
/* 514 */         result.append(" ").append(s);
/*     */       }
/*     */     }
/* 517 */     return result.toString();
/*     */   }
/*     */   
/*     */   private List tokenizeSentences(List<String> sentences)
/*     */   {
/* 522 */     List result = new ArrayList();
/* 523 */     for (String sentence : sentences) {
/* 524 */       Tokenizer tok = this.tokenizerFactory.getTokenizer(new StringReader(sentence));
/* 525 */       result.add(tok.tokenize());
/*     */     }
/* 527 */     return result;
/*     */   }
/*     */   
/*     */   private static List<List<? extends HasWord>> tagSplitSentences(List<List<HasWord>> sentences, int tagDelimiter) {
/* 531 */     List<List<? extends HasWord>> result = new ArrayList();
/* 532 */     WordToTaggedWordProcessor wttwp = new WordToTaggedWordProcessor((char)tagDelimiter);
/* 533 */     for (List<HasWord> sentence : sentences) {
/* 534 */       sentence = wttwp.process(sentence);
/* 535 */       result.add(sentence);
/*     */     }
/* 537 */     return result;
/*     */   }
/*     */   
/* 540 */   private static final Pattern urlPattern = Pattern.compile("(?:ht|f)tps?://.*?");
/*     */   private static final int PLAIN = 0;
/*     */   private static final int XML = 1;
/*     */   private static final int HTML = 2;
/*     */   
/*     */   private Reader fileOrURLToReader(String fileOrURL) throws IOException {
/* 546 */     Matcher m = urlPattern.matcher(fileOrURL);
/* 547 */     if (m.matches()) {
/* 548 */       URL url = new URL(fileOrURL);
/* 549 */       return new BufferedReader(new StringReader(StringUtils.slurpURL(url)));
/*     */     }
/* 551 */     if (this.encoding == null) {
/* 552 */       return new FileReader(fileOrURL);
/*     */     }
/* 554 */     return new BufferedReader(new InputStreamReader(new FileInputStream(fileOrURL), this.encoding));
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
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/* 583 */     if (args.length == 0) {
/* 584 */       System.err.println("usage: DocumentPreprocessor -file filename [-xml tag|-html] [-noSplitSentence]");
/* 585 */       return;
/*     */     }
/*     */     
/* 588 */     boolean splitSentences = true;
/* 589 */     boolean suppressEscaping = false;
/* 590 */     String xmlTag = null;
/*     */     
/*     */ 
/* 593 */     int fileType = 0;
/*     */     
/* 595 */     String file = null;
/*     */     
/* 597 */     for (int i = 0; i < args.length; i++) {
/* 598 */       if (args[i].equals("-file")) {
/* 599 */         file = args[(++i)];
/* 600 */       } else if (args[i].equals("-xml")) {
/* 601 */         fileType = 1;
/* 602 */         xmlTag = args[(++i)];
/* 603 */       } else if (args[i].equals("-html")) {
/* 604 */         fileType = 2;
/* 605 */       } else if (args[i].equals("-noSplitSentence")) {
/* 606 */         splitSentences = false;
/* 607 */       } else if (args[i].equals("-suppressEscaping")) {
/* 608 */         suppressEscaping = true;
/*     */       }
/*     */     }
/*     */     
/* 612 */     DocumentPreprocessor docPreprocessor = new DocumentPreprocessor(suppressEscaping);
/*     */     
/*     */ 
/* 615 */     List<List<? extends HasWord>> docs = new ArrayList();
/*     */     List<? extends HasWord> doc;
/* 617 */     switch (fileType) {
/*     */     case 0: 
/* 619 */       if (splitSentences) {
/* 620 */         docs = docPreprocessor.getSentencesFromText(file);
/*     */       } else {
/* 622 */         doc = docPreprocessor.getWordsFromText(file);
/* 623 */         docs.add(doc);
/*     */       }
/* 625 */       break;
/*     */     case 1: 
/* 627 */       boolean doPTBEscaping = !suppressEscaping;
/* 628 */       docs = docPreprocessor.getSentencesFromXML(file, xmlTag, doPTBEscaping);
/* 629 */       break;
/*     */     case 2: 
/* 631 */       if (splitSentences) {
/* 632 */         docs = docPreprocessor.getSentencesFromHTML(file);
/*     */       } else {
/* 634 */         doc = docPreprocessor.getWordsFromHTML(file);
/* 635 */         docs.add(doc);
/*     */       }
/*     */       break;
/*     */     }
/*     */     
/* 640 */     System.err.println("Read in " + docs.size() + " sentences.");
/* 641 */     for (List lis : docs) {
/* 642 */       System.err.println("Length: " + lis.size());
/* 643 */       System.out.println(lis);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\DocumentPreprocessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */