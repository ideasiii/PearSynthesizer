/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.ling.BasicDocument;
/*     */ import edu.stanford.nlp.ling.Document;
/*     */ import edu.stanford.nlp.ling.Word;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StripTagsProcessor
/*     */   extends AbstractListProcessor
/*     */ {
/*  23 */   public static final Set blockTags = new HashSet(Arrays.asList(new String[] { "blockquote", "br", "div", "h1", "h2", "h3", "h4", "h5", "h6", "hr", "li", "ol", "p", "pre", "table", "tr", "ul" }));
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean markLineBreaks;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StripTagsProcessor()
/*     */   {
/*  34 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StripTagsProcessor(boolean markLineBreaks)
/*     */   {
/*  41 */     setMarkLineBreaks(markLineBreaks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getMarkLineBreaks()
/*     */   {
/*  49 */     return this.markLineBreaks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMarkLineBreaks(boolean markLineBreaks)
/*     */   {
/*  57 */     this.markLineBreaks = markLineBreaks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List process(List in)
/*     */   {
/*  65 */     List out = new ArrayList();
/*  66 */     boolean justInsertedNewline = false;
/*  67 */     for (Iterator iter = in.iterator(); iter.hasNext();) {
/*  68 */       Word w = (Word)iter.next();
/*  69 */       String ws = w.word();
/*  70 */       if ((ws.startsWith("<")) && (ws.endsWith(">"))) {
/*  71 */         if ((this.markLineBreaks) && (!justInsertedNewline))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*  76 */           int tagStartIndex = 1;
/*  77 */           while ((tagStartIndex < ws.length()) && (!Character.isLetter(ws.charAt(tagStartIndex)))) {
/*  78 */             tagStartIndex++;
/*     */           }
/*  80 */           if (tagStartIndex != ws.length())
/*     */           {
/*     */ 
/*     */ 
/*  84 */             int tagEndIndex = ws.length() - 1;
/*  85 */             while ((tagEndIndex > tagStartIndex) && (!Character.isLetterOrDigit(ws.charAt(tagEndIndex)))) {
/*  86 */               tagEndIndex--;
/*     */             }
/*     */             
/*     */ 
/*  90 */             String tagName = ws.substring(tagStartIndex, tagEndIndex + 1).toLowerCase();
/*  91 */             if (blockTags.contains(tagName)) {
/*  92 */               out.add(new Word("\n"));
/*  93 */               justInsertedNewline = true;
/*     */             }
/*     */           }
/*     */         }
/*  97 */       } else { out.add(w);
/*  98 */         justInsertedNewline = false;
/*     */       }
/*     */     }
/* 101 */     return out;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 108 */     Document htmlDoc = new BasicDocument().init("top text <h1>HEADING text</h1> this is <p>new paragraph<br>next line<br/>xhtml break etc.");
/* 109 */     System.out.println("Before:");
/* 110 */     System.out.println(htmlDoc);
/* 111 */     Document txtDoc = new StripTagsProcessor(true).processDocument(htmlDoc);
/* 112 */     System.out.println("After:");
/* 113 */     System.out.println(txtDoc);
/* 114 */     Document sentences = new WordToSentenceProcessor().processDocument(txtDoc);
/* 115 */     System.out.println("Sentences:");
/* 116 */     System.out.println(sentences);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\StripTagsProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */