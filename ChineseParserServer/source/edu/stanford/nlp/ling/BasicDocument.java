/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.process.PTBTokenizer;
/*     */ import edu.stanford.nlp.process.Tokenizer;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public class BasicDocument
/*     */   extends ArrayList
/*     */   implements Document
/*     */ {
/*  35 */   protected String title = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String originalText;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  45 */   protected final List labels = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TokenizerFactory tokenizerFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicDocument()
/*     */   {
/*  59 */     this(PTBTokenizer.factory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicDocument(TokenizerFactory tokenizerFactory)
/*     */   {
/*  68 */     setTokenizerFactory(tokenizerFactory);
/*     */   }
/*     */   
/*     */   public BasicDocument(Document d) {
/*  72 */     this(d);
/*     */   }
/*     */   
/*     */   public BasicDocument(Collection d) {
/*  76 */     this();
/*  77 */     addAll(d);
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
/*     */   public BasicDocument init(String text, String title, boolean keepOriginalText)
/*     */   {
/*  91 */     setTitle(title);
/*     */     
/*     */ 
/*  94 */     if (keepOriginalText) {
/*  95 */       this.originalText = text;
/*     */     } else {
/*  97 */       this.originalText = null;
/*     */     }
/*     */     
/*     */ 
/* 101 */     parse(text == null ? "" : text);
/*     */     
/* 103 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicDocument init(String text, String title)
/*     */   {
/* 110 */     return init(text, title, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicDocument init(String text, boolean keepOriginalText)
/*     */   {
/* 117 */     return init(text, null, keepOriginalText);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicDocument init(String text)
/*     */   {
/* 124 */     return init(text, null, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicDocument init()
/*     */   {
/* 131 */     return init((String)null, null, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicDocument init(Reader textReader, String title, boolean keepOriginalText)
/*     */     throws IOException
/*     */   {
/* 140 */     return init(DocumentReader.readText(textReader), title, keepOriginalText);
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDocument init(Reader textReader, String title)
/*     */     throws IOException
/*     */   {
/* 147 */     return init(textReader, title, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDocument init(Reader textReader, boolean keepOriginalText)
/*     */     throws IOException
/*     */   {
/* 154 */     return init(textReader, null, keepOriginalText);
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDocument init(Reader textReader)
/*     */     throws IOException
/*     */   {
/* 161 */     return init(textReader, null, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicDocument init(File textFile, String title, boolean keepOriginalText)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 170 */     Reader in = DocumentReader.getReader(textFile);
/* 171 */     BasicDocument bd = init(in, title, keepOriginalText);
/* 172 */     in.close();
/* 173 */     return bd;
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDocument init(File textFile, String title)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 180 */     return init(textFile, title, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDocument init(File textFile, boolean keepOriginalText)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 187 */     return init(textFile, textFile.getCanonicalPath(), keepOriginalText);
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDocument init(File textFile)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 194 */     return init(textFile, textFile.getCanonicalPath(), true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicDocument init(URL textURL, String title, boolean keepOriginalText)
/*     */     throws IOException
/*     */   {
/* 203 */     return init(DocumentReader.getReader(textURL), title, keepOriginalText);
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDocument init(URL textURL, String title)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 210 */     return init(textURL, title, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDocument init(URL textURL, boolean keepOriginalText)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 217 */     return init(textURL, textURL.toExternalForm(), keepOriginalText);
/*     */   }
/*     */   
/*     */ 
/*     */   public BasicDocument init(URL textURL)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 224 */     return init(textURL, textURL.toExternalForm(), true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicDocument init(List words, String title)
/*     */   {
/* 232 */     setTitle(title);
/*     */     
/* 234 */     this.originalText = null;
/*     */     
/* 236 */     addAll(words);
/* 237 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicDocument init(List words)
/*     */   {
/* 244 */     return init(words, null);
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
/*     */   protected void parse(String text)
/*     */   {
/* 259 */     Tokenizer toke = this.tokenizerFactory.getTokenizer(new StringReader(text));
/* 260 */     addAll(toke.tokenize());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection asFeatures()
/*     */   {
/* 267 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object label()
/*     */   {
/* 275 */     return this.labels.size() > 0 ? this.labels.get(0) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection labels()
/*     */   {
/* 283 */     return this.labels;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLabel(Object label)
/*     */   {
/* 292 */     this.labels.clear();
/* 293 */     addLabel(label);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLabels(Collection labels)
/*     */   {
/* 301 */     this.labels.clear();
/* 302 */     if (labels != null) {
/* 303 */       this.labels.addAll(labels);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addLabel(Object label)
/*     */   {
/* 311 */     if (label != null) {
/* 312 */       this.labels.add(label);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String title()
/*     */   {
/* 321 */     return this.title;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitle(String title)
/*     */   {
/* 329 */     if (title == null) {
/* 330 */       this.title = "";
/*     */     } else {
/* 332 */       this.title = title;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TokenizerFactory tokenizerFactory()
/*     */   {
/* 340 */     return this.tokenizerFactory;
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
/*     */   public void setTokenizerFactory(TokenizerFactory tokenizerFactory)
/*     */   {
/* 354 */     this.tokenizerFactory = tokenizerFactory;
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
/*     */   public Document blankDocument()
/*     */   {
/*     */     BasicDocument bd;
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
/*     */     try
/*     */     {
/* 384 */       bd = (BasicDocument)getClass().newInstance();
/*     */     } catch (Exception e) {
/* 386 */       bd = new BasicDocument();
/*     */     }
/*     */     
/*     */ 
/* 390 */     bd.setTitle(title());
/* 391 */     bd.setLabels(labels());
/* 392 */     bd.setTokenizerFactory(this.tokenizerFactory);
/* 393 */     return bd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String originalText()
/*     */   {
/* 401 */     return this.originalText;
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
/*     */   public String presentableText()
/*     */   {
/* 416 */     StringBuffer sb = new StringBuffer();
/* 417 */     Iterator iter = iterator();
/* 418 */     while (iter.hasNext()) {
/* 419 */       Object cur = iter.next();
/* 420 */       if ((cur instanceof HasWord)) {
/* 421 */         if (sb.length() > 0) {
/* 422 */           sb.append(' ');
/*     */         }
/* 424 */         sb.append(((HasWord)cur).word());
/*     */       }
/*     */     }
/* 427 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 436 */       printState(new BasicDocument().init("this is the text", "this is the title [String]", true));
/* 437 */       printState(new BasicDocument().init(new StringReader("this is the text"), "this is the title [Reader]", true));
/*     */       
/* 439 */       File f = File.createTempFile("BasicDocumentTestFile", null);
/* 440 */       f.deleteOnExit();
/* 441 */       PrintWriter out = new PrintWriter(new FileWriter(f));
/* 442 */       out.print("this is the text");
/* 443 */       out.flush();
/* 444 */       out.close();
/* 445 */       printState(new BasicDocument().init(f, "this is the title [File]", true));
/* 446 */       printState(new BasicDocument().init(new URL("http://www.stanford.edu/~jsmarr/BasicDocumentTestFile.txt"), "this is the title [URL]", true));
/*     */     } catch (Exception e) {
/* 448 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void printState(BasicDocument bd)
/*     */     throws Exception
/*     */   {
/* 457 */     System.err.println("BasicDocument:");
/* 458 */     System.err.println("\tTitle: " + bd.title());
/* 459 */     System.err.println("\tLabels: " + bd.labels());
/* 460 */     System.err.println("\tOriginalText: " + bd.originalText());
/* 461 */     System.err.println("\tWords: " + bd);
/* 462 */     System.err.println();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\BasicDocument.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */