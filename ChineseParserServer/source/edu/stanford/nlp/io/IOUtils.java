/*     */ package edu.stanford.nlp.io;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOUtils
/*     */ {
/*     */   public static File writeObjectToFile(Serializable o, String filename)
/*     */     throws IOException
/*     */   {
/*  30 */     File file = new File(filename);
/*     */     
/*  32 */     ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(file))));
/*  33 */     oos.writeObject(o);
/*  34 */     oos.close();
/*  35 */     return file;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static File writeObjectToFileNoExceptions(Serializable o, String filename)
/*     */   {
/*  47 */     file = null;
/*  48 */     ObjectOutputStream oos = null;
/*     */     try {
/*  50 */       file = new File(filename);
/*     */       
/*  52 */       oos = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(file))));
/*  53 */       oos.writeObject(o);
/*  54 */       oos.close();
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
/*  65 */       return file;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  56 */       e.printStackTrace();
/*     */     } finally {
/*  58 */       if (oos != null) {
/*     */         try {
/*  60 */           oos.close();
/*     */         }
/*     */         catch (Exception ioe) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static File writeObjectToTempFile(Serializable o, String filename)
/*     */     throws IOException
/*     */   {
/*  76 */     File file = File.createTempFile(filename, ".tmp");
/*  77 */     file.deleteOnExit();
/*  78 */     ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(file))));
/*  79 */     oos.writeObject(o);
/*  80 */     oos.close();
/*  81 */     return file;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static File writeObjectToTempFileNoExceptions(Serializable o, String filename)
/*     */   {
/*     */     try
/*     */     {
/*  93 */       return writeObjectToTempFile(o, filename);
/*     */     } catch (Exception e) {
/*  95 */       System.err.println("Error writing object to file " + filename);
/*  96 */       e.printStackTrace(); }
/*  97 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object readObjectFromFile(File file)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 109 */     ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
/* 110 */     Object o = ois.readObject();
/* 111 */     ois.close();
/* 112 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object readObjectFromFile(String filename)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 122 */     return readObjectFromFile(new File(filename));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object readObjectFromFileNoExceptions(File file)
/*     */   {
/* 133 */     Object o = null;
/*     */     try {
/* 135 */       ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
/* 136 */       o = ois.readObject();
/* 137 */       ois.close();
/*     */     } catch (IOException e) {
/* 139 */       e.printStackTrace();
/*     */     } catch (ClassNotFoundException e) {
/* 141 */       e.printStackTrace();
/*     */     }
/* 143 */     return o;
/*     */   }
/*     */   
/*     */   public static int lineCount(File textFile) throws IOException {
/* 147 */     BufferedReader r = new BufferedReader(new FileReader(textFile));
/* 148 */     int numLines = 0;
/* 149 */     while (r.readLine() != null) {
/* 150 */       numLines++;
/*     */     }
/* 152 */     return numLines; }
/*     */   
/*     */   public static ObjectOutputStream writeStreamFromString(String serializePath) throws IOException { ObjectOutputStream oos;
/*     */     ObjectOutputStream oos;
/* 156 */     if (serializePath.endsWith(".gz")) {
/* 157 */       oos = new ObjectOutputStream(new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(serializePath))));
/*     */     } else {
/* 159 */       oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(serializePath)));
/*     */     }
/*     */     
/* 162 */     return oos;
/*     */   }
/*     */   
/*     */   public static ObjectInputStream readStreamFromString(String filenameOrUrl) throws IOException {
/*     */     InputStream is;
/*     */     InputStream is;
/* 168 */     if (filenameOrUrl.startsWith("http://")) {
/* 169 */       URL u = new URL(filenameOrUrl);
/* 170 */       URLConnection uc = u.openConnection();
/* 171 */       is = uc.getInputStream();
/*     */     } else {
/* 173 */       is = new FileInputStream(filenameOrUrl); }
/*     */     ObjectInputStream in;
/* 175 */     ObjectInputStream in; if (filenameOrUrl.endsWith(".gz")) {
/* 176 */       in = new ObjectInputStream(new GZIPInputStream(new BufferedInputStream(is)));
/*     */     } else {
/* 178 */       in = new ObjectInputStream(new BufferedInputStream(is));
/*     */     }
/* 180 */     return in;
/*     */   }
/*     */   
/*     */   public static BufferedReader readReaderFromString(String textFileOrUrl) throws IOException {
/*     */     InputStream is;
/*     */     InputStream is;
/* 186 */     if (textFileOrUrl.startsWith("http://")) {
/* 187 */       URL u = new URL(textFileOrUrl);
/* 188 */       URLConnection uc = u.openConnection();
/* 189 */       is = uc.getInputStream();
/*     */     } else {
/* 191 */       is = new FileInputStream(textFileOrUrl); }
/*     */     BufferedReader in;
/* 193 */     BufferedReader in; if (textFileOrUrl.endsWith(".gz")) {
/* 194 */       in = new BufferedReader(new InputStreamReader(new GZIPInputStream(is)));
/*     */     } else {
/* 196 */       in = new BufferedReader(new InputStreamReader(is));
/*     */     }
/* 198 */     return in;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\io\IOUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */