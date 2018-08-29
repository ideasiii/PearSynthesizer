/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ import edu.stanford.nlp.io.ExtensionFileFilter;
/*    */ import java.io.File;
/*    */ import java.io.FileFilter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilePathProcessor
/*    */ {
/*    */   public static void processPath(String pathStr, String suffix, boolean recursively, FileProcessor processor)
/*    */   {
/* 29 */     processPath(new File(pathStr), new ExtensionFileFilter(suffix, recursively), processor);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void processPath(File path, String suffix, boolean recursively, FileProcessor processor)
/*    */   {
/* 44 */     processPath(path, new ExtensionFileFilter(suffix, recursively), processor);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void processPath(File path, FileFilter filter, FileProcessor processor)
/*    */   {
/* 65 */     if (path.isDirectory())
/*    */     {
/* 67 */       File[] directoryListing = path.listFiles(filter);
/* 68 */       if (directoryListing == null) {
/* 69 */         throw new IllegalArgumentException("Directory access problem for: " + path);
/*    */       }
/* 71 */       for (int i = 0; i < directoryListing.length; i++) {
/* 72 */         processPath(directoryListing[i], filter, processor);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 77 */       processor.processFile(path);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\FilePathProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */