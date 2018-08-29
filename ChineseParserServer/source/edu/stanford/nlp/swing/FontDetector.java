/*    */ package edu.stanford.nlp.swing;
/*    */ 
/*    */ import java.awt.Font;
/*    */ import java.awt.GraphicsEnvironment;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FontDetector
/*    */ {
/*    */   public static final int NUM_LANGUAGES = 1;
/*    */   public static final int CHINESE = 0;
/* 18 */   public static final String[][] unicodeRanges = new String[1][];
/*    */   
/*    */   static {
/* 21 */     unicodeRanges[0] = { "、", "！", "￮", "ʹ", "ㄦ" };
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
/*    */   public static List<Font> supportedFonts(int language)
/*    */   {
/* 35 */     if ((language < 0) || (language > 1)) {
/* 36 */       throw new IllegalArgumentException();
/*    */     }
/*    */     
/* 39 */     List<Font> fonts = new ArrayList();
/* 40 */     Font[] systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
/* 41 */     for (int i = 0; i < systemFonts.length; i++) {
/* 42 */       boolean canDisplay = true;
/* 43 */       for (int j = 0; j < unicodeRanges[language].length; j++) {
/* 44 */         if (systemFonts[i].canDisplayUpTo(unicodeRanges[language][j]) != -1) {
/* 45 */           canDisplay = false;
/* 46 */           break;
/*    */         }
/*    */       }
/* 49 */       if (canDisplay) {
/* 50 */         fonts.add(systemFonts[i]);
/*    */       }
/*    */     }
/* 53 */     return fonts;
/*    */   }
/*    */   
/*    */   public static boolean hasFont(String fontName) {
/* 57 */     Font[] systemFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
/* 58 */     for (int i = 0; i < systemFonts.length; i++) {
/* 59 */       if (systemFonts[i].getName().equalsIgnoreCase(fontName)) {
/* 60 */         return true;
/*    */       }
/*    */     }
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 67 */     List<Font> fonts = supportedFonts(0);
/* 68 */     System.err.println("Has MS Mincho? " + hasFont("MS Mincho"));
/* 69 */     for (Font font : fonts) {
/* 70 */       System.out.println(font.getName());
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\swing\FontDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */