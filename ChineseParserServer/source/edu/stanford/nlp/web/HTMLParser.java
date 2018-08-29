/*     */ package edu.stanford.nlp.web;
/*     */ 
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
/*     */ import javax.swing.text.MutableAttributeSet;
/*     */ import javax.swing.text.html.HTML.Tag;
/*     */ import javax.swing.text.html.HTMLEditorKit.ParserCallback;
/*     */ import javax.swing.text.html.parser.ParserDelegator;
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
/*     */ public class HTMLParser
/*     */   extends HTMLEditorKit.ParserCallback
/*     */ {
/*     */   protected StringBuffer textBuffer;
/*     */   protected String title;
/*     */   protected boolean isTitle;
/*     */   protected boolean isBody;
/*     */   protected boolean isScript;
/*     */   
/*     */   public HTMLParser()
/*     */   {
/*  37 */     this.title = "";
/*  38 */     this.isTitle = false;
/*  39 */     this.isBody = false;
/*  40 */     this.isScript = false;
/*     */   }
/*     */   
/*     */   public void handleText(char[] data, int pos) {
/*  44 */     if (this.isTitle) {
/*  45 */       this.title = new String(data);
/*  46 */     } else if ((this.isBody) && (!this.isScript)) {
/*  47 */       this.textBuffer.append(data).append(" ");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleStartTag(HTML.Tag tag, MutableAttributeSet attrSet, int pos)
/*     */   {
/*  57 */     if (tag == HTML.Tag.TITLE) {
/*  58 */       this.isTitle = true;
/*  59 */     } else if (tag == HTML.Tag.BODY) {
/*  60 */       this.isBody = true;
/*  61 */     } else if (tag == HTML.Tag.SCRIPT) {
/*  62 */       this.isScript = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void handleEndTag(HTML.Tag tag, int pos)
/*     */   {
/*  72 */     if (tag == HTML.Tag.TITLE) {
/*  73 */       this.isTitle = false;
/*  74 */     } else if (tag == HTML.Tag.BODY) {
/*  75 */       this.isBody = false;
/*  76 */     } else if (tag == HTML.Tag.SCRIPT) {
/*  77 */       this.isScript = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public String parse(URL url) throws IOException {
/*  82 */     return parse(StringUtils.slurpURL(url));
/*     */   }
/*     */   
/*     */   public String parse(Reader r) throws IOException {
/*  86 */     return parse(StringUtils.slurpReader(r));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String parse(String text)
/*     */     throws IOException
/*     */   {
/*  97 */     text = StringUtils.searchAndReplace(text, "/>", ">");
/*  98 */     StringReader r = new StringReader(text);
/*  99 */     this.textBuffer = new StringBuffer(200);
/* 100 */     new ParserDelegator().parse(r, this, true);
/* 101 */     return this.textBuffer.toString();
/*     */   }
/*     */   
/*     */   public String title() {
/* 105 */     return this.title;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws IOException
/*     */   {
/* 110 */     HTMLParser parser = new HTMLParser();
/* 111 */     String result = parser.parse(new URL(args[0]));
/* 112 */     System.out.println(result);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\web\HTMLParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */