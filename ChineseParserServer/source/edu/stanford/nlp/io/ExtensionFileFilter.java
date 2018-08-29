/*    */ package edu.stanford.nlp.io;
/*    */ 
/*    */ import java.io.File;
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
/*    */ public class ExtensionFileFilter
/*    */   extends javax.swing.filechooser.FileFilter
/*    */   implements java.io.FileFilter
/*    */ {
/*    */   private String extension;
/*    */   private boolean recursively;
/*    */   
/*    */   public ExtensionFileFilter(String ext, boolean recurse)
/*    */   {
/* 27 */     if (ext != null) {
/* 28 */       if (ext.startsWith(".")) {
/* 29 */         this.extension = ext;
/*    */       } else {
/* 31 */         this.extension = ("." + ext);
/*    */       }
/*    */     }
/* 34 */     this.recursively = recurse;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public ExtensionFileFilter(String ext)
/*    */   {
/* 41 */     this(ext, true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean accept(File file)
/*    */   {
/* 51 */     if (file.isDirectory())
/* 52 */       return this.recursively;
/* 53 */     if (this.extension == null) {
/* 54 */       return true;
/*    */     }
/* 56 */     return file.getName().endsWith(this.extension);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescription()
/*    */   {
/* 68 */     String ucExt = this.extension.substring(1).toUpperCase();
/* 69 */     return ucExt + " Files (*" + this.extension + ")";
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\io\ExtensionFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */