/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.process.PTBTokenizer.PTBTokenizerFactory;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
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
/*     */ public class DocumentReader
/*     */ {
/*     */   protected Reader in;
/*     */   protected TokenizerFactory tokenizerFactory;
/*     */   protected boolean keepOriginalText;
/*     */   
/*     */   public DocumentReader()
/*     */   {
/*  44 */     this(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public DocumentReader(Reader in)
/*     */   {
/*  51 */     this(in, PTBTokenizer.PTBTokenizerFactory.newPTBTokenizerFactory(), true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DocumentReader(Reader in, TokenizerFactory tokenizerFactory, boolean keepOriginalText)
/*     */   {
/*  63 */     if (in != null) {
/*  64 */       setReader(in);
/*     */     }
/*  66 */     setTokenizerFactory(tokenizerFactory);
/*  67 */     this.keepOriginalText = keepOriginalText;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Reader getReader()
/*     */   {
/*  74 */     return this.in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReader(Reader in)
/*     */   {
/*  84 */     this.in = getBufferedReader(in);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TokenizerFactory getTokenizerFactory()
/*     */   {
/*  91 */     return this.tokenizerFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTokenizerFactory(TokenizerFactory tokenizerFactory)
/*     */   {
/*  98 */     this.tokenizerFactory = tokenizerFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean getKeepOriginalText()
/*     */   {
/* 105 */     return this.keepOriginalText;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setKeepOriginalText(boolean keepOriginalText)
/*     */   {
/* 112 */     this.keepOriginalText = keepOriginalText;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Document readDocument()
/*     */     throws IOException
/*     */   {
/* 125 */     String text = readNextDocumentText();
/* 126 */     if (text == null) {
/* 127 */       return null;
/*     */     }
/* 129 */     return parseDocumentText(text);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String readNextDocumentText()
/*     */     throws IOException
/*     */   {
/* 140 */     return readText(this.in);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Document parseDocumentText(String text)
/*     */   {
/* 152 */     return new BasicDocument().init(text, this.keepOriginalText);
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
/*     */   public static BufferedReader getBufferedReader(Reader in)
/*     */   {
/* 168 */     if (in == null) {
/* 169 */       return null;
/*     */     }
/* 171 */     if (!(in instanceof BufferedReader)) {
/* 172 */       in = new BufferedReader(in);
/*     */     }
/* 174 */     return (BufferedReader)in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String readText(Reader in)
/*     */     throws IOException
/*     */   {
/* 183 */     if (in == null) {
/* 184 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 188 */     BufferedReader br = getBufferedReader(in);
/*     */     
/*     */ 
/* 191 */     StringBuffer sb = new StringBuffer(16000);
/*     */     int c;
/* 193 */     while ((c = br.read()) >= 0) {
/* 194 */       sb.append((char)c);
/*     */     }
/*     */     
/* 197 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Reader getReader(String text)
/*     */   {
/* 204 */     return new StringReader(text);
/*     */   }
/*     */   
/*     */ 
/*     */   public static Reader getReader(File file)
/*     */     throws FileNotFoundException
/*     */   {
/* 211 */     return new FileReader(file);
/*     */   }
/*     */   
/*     */ 
/*     */   public static Reader getReader(URL url)
/*     */     throws IOException
/*     */   {
/* 218 */     return getReader(url.openStream());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Reader getReader(InputStream in)
/*     */   {
/* 225 */     return new InputStreamReader(in);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Reader getReader(Object in)
/*     */     throws FileNotFoundException, IOException
/*     */   {
/* 238 */     if ((in instanceof File)) {
/* 239 */       return getReader((File)in);
/*     */     }
/* 241 */     if ((in instanceof String)) {
/* 242 */       return getReader(new File((String)in));
/*     */     }
/* 244 */     if ((in instanceof URL)) {
/* 245 */       return getReader((URL)in);
/*     */     }
/* 247 */     if ((in instanceof InputStream)) {
/* 248 */       return getReader((InputStream)in);
/*     */     }
/* 250 */     if ((in instanceof Reader)) {
/* 251 */       return (Reader)in;
/*     */     }
/* 253 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\DocumentReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */