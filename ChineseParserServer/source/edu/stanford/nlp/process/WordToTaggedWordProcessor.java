/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.ling.BasicDocument;
/*     */ import edu.stanford.nlp.ling.Document;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.TaggedWord;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class WordToTaggedWordProcessor
/*     */   extends AbstractListProcessor<HasWord, HasWord>
/*     */ {
/*     */   protected char splitChar;
/*     */   
/*     */   public List<HasWord> process(List<HasWord> words)
/*     */   {
/*  45 */     List<HasWord> result = new ArrayList();
/*  46 */     for (HasWord w : words) {
/*  47 */       result.add(splitTag(w));
/*     */     }
/*  49 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private HasWord splitTag(HasWord w)
/*     */   {
/*  56 */     if (this.splitChar == 0) {
/*  57 */       return w;
/*     */     }
/*  59 */     String s = w.word();
/*  60 */     int split = s.lastIndexOf(this.splitChar);
/*  61 */     if (split <= 0) {
/*  62 */       return w;
/*     */     }
/*  64 */     String word = s.substring(0, split);
/*  65 */     String tag = s.substring(split + 1, s.length());
/*  66 */     return new TaggedWord(word, tag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordToTaggedWordProcessor()
/*     */   {
/*  75 */     this('/');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordToTaggedWordProcessor(char splitChar)
/*     */   {
/*  85 */     this.splitChar = splitChar;
/*     */   }
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
/*  97 */     if (args.length != 1) {
/*  98 */       System.out.println("usage: java edu.stanford.nlp.process.WordToTaggedWordProcessor fileOrUrl");
/*  99 */       System.exit(0);
/*     */     }
/* 101 */     String filename = args[0];
/*     */     try { Document d;
/*     */       Document d;
/* 104 */       if (filename.startsWith("http://")) {
/* 105 */         Document dpre = new BasicDocument().init(new URL(filename));
/* 106 */         Processor notags = new StripTagsProcessor();
/* 107 */         d = notags.processDocument(dpre);
/*     */       } else {
/* 109 */         d = new BasicDocument().init(new File(filename));
/*     */       }
/* 111 */       Processor proc = new WordToTaggedWordProcessor();
/* 112 */       Document sentd = proc.processDocument(d);
/*     */       
/* 114 */       i = 0;
/* 115 */       for (it = sentd.iterator(); it.hasNext();) {
/* 116 */         HasWord w = (HasWord)it.next();
/* 117 */         System.out.println(i + ": " + w);
/* 118 */         i++;
/*     */       } } catch (Exception e) { int i;
/*     */       Iterator it;
/* 121 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\WordToTaggedWordProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */