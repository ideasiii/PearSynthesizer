/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.ling.BasicDocument;
/*     */ import edu.stanford.nlp.ling.Document;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class WordToSentenceProcessor
/*     */   extends AbstractListProcessor
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private Set sentenceBoundaryTokens;
/*     */   private Set sentenceBoundaryFollowers;
/*     */   private Set sentenceBoundaryToDiscard;
/*     */   private Pattern sentenceRegionBeginPattern;
/*     */   private Pattern sentenceRegionEndPattern;
/*     */   
/*     */   public List process(List words)
/*     */   {
/*  86 */     List sentences = new ArrayList();
/*  87 */     List currentSentence = null;
/*  88 */     List lastSentence = null;
/*  89 */     boolean insideRegion = false;
/*  90 */     for (Iterator iter = words.iterator(); iter.hasNext();) {
/*  91 */       Object o = iter.next();
/*     */       String w;
/*  93 */       if ((o instanceof HasWord)) {
/*  94 */         HasWord h = (HasWord)o;
/*  95 */         w = h.word(); } else { String w;
/*  96 */         if ((o instanceof String)) {
/*  97 */           w = (String)o;
/*     */         } else {
/*  99 */           throw new RuntimeException("Expected token to be either Word or String.");
/*     */         }
/*     */       }
/*     */       
/*     */       String w;
/* 104 */       if (currentSentence == null) {
/* 105 */         currentSentence = new Sentence();
/*     */       }
/* 107 */       if ((this.sentenceRegionBeginPattern != null) && (!insideRegion)) {
/* 108 */         if (this.sentenceRegionBeginPattern.matcher(w).matches()) {
/* 109 */           insideRegion = true;
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */       }
/* 116 */       else if ((this.sentenceBoundaryFollowers.contains(w)) && (lastSentence != null) && (currentSentence.isEmpty())) {
/* 117 */         lastSentence.add(o);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 122 */         boolean newSent = false;
/* 123 */         if (this.sentenceBoundaryToDiscard.contains(w)) {
/* 124 */           newSent = true;
/* 125 */         } else if ((this.sentenceRegionEndPattern != null) && (this.sentenceRegionEndPattern.matcher(w).matches())) {
/* 126 */           insideRegion = false;
/* 127 */           newSent = true;
/* 128 */         } else if (this.sentenceBoundaryTokens.contains(w)) {
/* 129 */           currentSentence.add(o);
/*     */           
/*     */ 
/*     */ 
/* 133 */           newSent = true;
/*     */         } else {
/* 135 */           currentSentence.add(o);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 140 */         if ((newSent) && (currentSentence.size() > 0))
/*     */         {
/*     */ 
/*     */ 
/* 144 */           sentences.add(currentSentence);
/*     */           
/* 146 */           lastSentence = currentSentence;
/* 147 */           currentSentence = null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 154 */     if ((currentSentence != null) && (currentSentence.size() > 0)) {
/* 155 */       sentences.add(currentSentence);
/*     */     }
/* 157 */     return sentences;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordToSentenceProcessor()
/*     */   {
/* 166 */     this(new HashSet(Arrays.asList(new String[] { ".", "?", "!" })));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordToSentenceProcessor(Set boundaryTokens)
/*     */   {
/* 177 */     this(boundaryTokens, new HashSet(Arrays.asList(new String[] { ")", "]", "\"", "'", "''", "-RRB-", "-RSB-" })));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordToSentenceProcessor(Set boundaryTokens, Set boundaryFollowers)
/*     */   {
/* 187 */     this(boundaryTokens, boundaryFollowers, Collections.singleton("\n"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordToSentenceProcessor(Set boundaryTokens, Set boundaryFollowers, Set boundaryToDiscard)
/*     */   {
/* 198 */     this(boundaryTokens, boundaryFollowers, boundaryToDiscard, null, null);
/*     */   }
/*     */   
/*     */   public WordToSentenceProcessor(Pattern regionBeginPattern, Pattern regionEndPattern) {
/* 202 */     this(Collections.EMPTY_SET, Collections.EMPTY_SET, Collections.EMPTY_SET, regionBeginPattern, regionEndPattern);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private WordToSentenceProcessor(Set boundaryTokens, Set boundaryFollowers, Set boundaryToDiscard, Pattern regionBeginPattern, Pattern regionEndPattern)
/*     */   {
/* 214 */     this.sentenceBoundaryTokens = boundaryTokens;
/* 215 */     this.sentenceBoundaryFollowers = boundaryFollowers;
/* 216 */     this.sentenceBoundaryToDiscard = boundaryToDiscard;
/* 217 */     this.sentenceRegionBeginPattern = regionBeginPattern;
/* 218 */     this.sentenceRegionEndPattern = regionEndPattern;
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
/*     */   public static void main(String[] args)
/*     */   {
/* 245 */     if (args.length == 0) {
/* 246 */       System.out.println("usage: java edu.stanford.nlp.process.WordToSentenceProcessor fileOrUrl");
/* 247 */       System.exit(0);
/*     */     }
/*     */     try { Iterator it;
/* 250 */       for (String filename : args) { Document d;
/*     */         Document d;
/* 252 */         if (filename.startsWith("http://")) {
/* 253 */           Document dpre = new BasicDocument().init(new URL(filename));
/* 254 */           Processor notags = new StripTagsProcessor();
/* 255 */           d = notags.processDocument(dpre);
/*     */         } else {
/* 257 */           d = new BasicDocument().init(new File(filename));
/*     */         }
/* 259 */         WordToSentenceProcessor proc = new WordToSentenceProcessor();
/* 260 */         List sentd = proc.processDocument(d);
/* 261 */         for (it = sentd.iterator(); it.hasNext();) {
/* 262 */           Sentence sent = (Sentence)it.next();
/* 263 */           System.out.println(sent);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 267 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\WordToSentenceProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */