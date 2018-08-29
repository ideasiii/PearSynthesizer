/*    */ package edu.stanford.nlp.misc;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.List;
/*    */ 
/*    */ public class SeeChars
/*    */ {
/*    */   public static void seeChars(String str, String outputEncoding)
/*    */   {
/*    */     PrintWriter pw;
/*    */     try
/*    */     {
/* 14 */       pw = new PrintWriter(new java.io.OutputStreamWriter(System.out, outputEncoding), true);
/*    */     } catch (java.io.UnsupportedEncodingException uee) {
/* 16 */       System.err.println("Unsupported encoding: " + outputEncoding);
/* 17 */       pw = new PrintWriter(System.out, true);
/*    */     }
/* 19 */     seeChars(str, pw);
/*    */   }
/*    */   
/*    */   public static void seeChars(String str, PrintWriter pw) {
/* 23 */     int numCodePoints = str.codePointCount(0, str.length());
/* 24 */     for (int i = 0; i < numCodePoints; i++) {
/* 25 */       int index = str.offsetByCodePoints(0, i);
/* 26 */       int ch = str.codePointAt(index);
/* 27 */       seeCodePoint(ch, pw);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void seeList(List sentence, String outputEncoding) {
/* 32 */     int ii = 0; for (int len = sentence.size(); ii < len; ii++) {
/* 33 */       System.out.println("Word " + ii + " in " + outputEncoding);
/* 34 */       seeChars(sentence.get(ii).toString(), outputEncoding);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void seeCodePoint(int ch, PrintWriter pw) { String chstr;
/*    */     String chstr;
/* 40 */     if (ch == 10) {
/* 41 */       chstr = "nl"; } else { String chstr;
/* 42 */       if (ch == 13) {
/* 43 */         chstr = "cr";
/*    */       } else {
/* 45 */         char[] chArr = Character.toChars(ch);
/* 46 */         chstr = new String(chArr);
/*    */       } }
/* 48 */     int ty = Character.getType(ch);
/* 49 */     String tyStr = "";
/* 50 */     switch (ty) {
/*    */     case 5: 
/* 52 */       tyStr = " other char";
/* 53 */       break;
/*    */     case 20: 
/* 55 */       tyStr = " dash punct";
/* 56 */       break;
/*    */     case 21: 
/* 58 */       tyStr = " start punct";
/* 59 */       break;
/*    */     case 22: 
/* 61 */       tyStr = " end punct";
/* 62 */       break;
/*    */     case 24: 
/* 64 */       tyStr = " other punct";
/* 65 */       break;
/*    */     }
/*    */     
/* 68 */     pw.println("Character " + ch + " [" + chstr + ", U+" + Integer.toHexString(ch).toUpperCase() + ", valid=" + Character.isValidCodePoint(ch) + ", suppl=" + Character.isSupplementaryCodePoint(ch) + ", mirror=" + Character.isMirrored(ch) + ", type=" + Character.getType(ch) + tyStr + "]");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 78 */       if (args.length < 2) {
/* 79 */         System.err.println("usage: java SeeChars file inCharEncoding [outCharEncoding]");
/*    */       } else {
/* 81 */         java.io.Reader r = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(args[0]), args[1]));
/* 82 */         String outEncoding = args.length >= 3 ? args[2] : args[1];
/* 83 */         PrintWriter pw = new PrintWriter(new java.io.OutputStreamWriter(System.out, outEncoding), true);
/*    */         int ch;
/* 85 */         while ((ch = r.read()) >= 0) {
/* 86 */           seeCodePoint(ch, pw);
/*    */         }
/*    */       }
/*    */     } catch (Exception e) {
/* 90 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\misc\SeeChars.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */