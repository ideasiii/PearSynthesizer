/*     */ package edu.stanford.nlp.io;
/*     */ 
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
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
/*     */ public class EncodingPrintWriter
/*     */ {
/*     */   private static final String DEFAULT_ENCODING = "UTF-8";
/*     */   private static PrintWriter cachedErrWriter;
/*  21 */   private static String cachedErrEncoding = "";
/*     */   
/*     */   private static PrintWriter cachedOutWriter;
/*  24 */   private static String cachedOutEncoding = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class err
/*     */   {
/*     */     private static void setupErrWriter(String encoding)
/*     */     {
/*  37 */       if ((EncodingPrintWriter.cachedErrWriter == null) || ((encoding != null) && (EncodingPrintWriter.cachedErrEncoding != encoding)))
/*     */       {
/*  39 */         if (encoding == null) {
/*  40 */           encoding = "UTF-8";
/*     */         }
/*     */         try {
/*  43 */           EncodingPrintWriter.access$002(new PrintWriter(new OutputStreamWriter(System.err, encoding), true));
/*  44 */           EncodingPrintWriter.access$102(encoding);
/*     */         } catch (UnsupportedEncodingException e) {
/*  46 */           System.err.println("Error " + e + "Printing as default encoding.");
/*  47 */           EncodingPrintWriter.access$002(new PrintWriter(new OutputStreamWriter(System.err), true));
/*  48 */           EncodingPrintWriter.access$102("");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public static void println(String o, String encoding) {
/*  54 */       setupErrWriter(encoding);
/*  55 */       EncodingPrintWriter.cachedErrWriter.println(o);
/*     */     }
/*     */     
/*     */     public static void print(String o, String encoding) {
/*  59 */       setupErrWriter(encoding);
/*  60 */       EncodingPrintWriter.cachedErrWriter.print(o);
/*  61 */       EncodingPrintWriter.cachedErrWriter.flush();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class out
/*     */   {
/*     */     private static void setupOutWriter(String encoding)
/*     */     {
/*  75 */       if ((EncodingPrintWriter.cachedOutWriter == null) || ((encoding != null) && (EncodingPrintWriter.cachedOutEncoding != encoding)))
/*     */       {
/*  77 */         if (encoding == null) {
/*  78 */           encoding = "UTF-8";
/*     */         }
/*     */         try {
/*  81 */           EncodingPrintWriter.access$202(new PrintWriter(new OutputStreamWriter(System.err, encoding), true));
/*  82 */           EncodingPrintWriter.access$302(encoding);
/*     */         } catch (UnsupportedEncodingException e) {
/*  84 */           System.err.println("Error " + e + "Printing as default encoding.");
/*  85 */           EncodingPrintWriter.access$202(new PrintWriter(new OutputStreamWriter(System.err), true));
/*  86 */           EncodingPrintWriter.access$302("");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public static void println(String o, String encoding) {
/*  92 */       setupOutWriter(encoding);
/*  93 */       EncodingPrintWriter.cachedOutWriter.println(o);
/*     */     }
/*     */     
/*     */     public static void print(String o, String encoding)
/*     */     {
/*  98 */       setupOutWriter(encoding);
/*  99 */       EncodingPrintWriter.cachedOutWriter.print(o);
/* 100 */       EncodingPrintWriter.cachedOutWriter.flush();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\io\EncodingPrintWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */