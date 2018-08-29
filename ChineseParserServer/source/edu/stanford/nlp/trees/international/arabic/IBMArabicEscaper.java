/*    */ package edu.stanford.nlp.trees.international.arabic;
/*    */ 
/*    */ import edu.stanford.nlp.ling.HasWord;
/*    */ import edu.stanford.nlp.process.Function;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class IBMArabicEscaper
/*    */   implements Function<List<HasWord>, List<HasWord>>
/*    */ {
/* 21 */   private static Pattern p2 = Pattern.compile("\\$[a-z]+_\\((.*?)\\)");
/*    */   
/*    */   private static String escapeString(String w) {
/* 24 */     int wLen = w.length();
/* 25 */     if (wLen > 1) {
/* 26 */       Matcher m2 = p2.matcher(w);
/* 27 */       if (m2.matches()) {
/* 28 */         w = m2.replaceAll("$1");
/* 29 */       } else if (w.charAt(0) == '+') {
/* 30 */         w = w.substring(1);
/*    */ 
/*    */ 
/*    */       }
/* 34 */       else if (w.charAt(wLen - 1) == '#') {
/* 35 */         w = w.substring(0, wLen - 1);
/*    */       }
/*    */     }
/* 38 */     else if (w.equals("(")) {
/* 39 */       w = "-LRB-";
/* 40 */     } else if (w.equals(")")) {
/* 41 */       w = "-RRB-";
/*    */     }
/*    */     
/* 44 */     return w;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<HasWord> apply(List<HasWord> arg)
/*    */   {
/* 52 */     List<HasWord> ans = new ArrayList(arg);
/* 53 */     for (HasWord wd : ans) {
/* 54 */       wd.setWord(escapeString(wd.word()));
/*    */     }
/* 56 */     return ans;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void main(String[] args)
/*    */     throws IOException
/*    */   {
/* 65 */     for (String arg : args) {
/* 66 */       BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arg), "UTF-8"));
/* 67 */       String outFile = arg + ".sent";
/* 68 */       PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8")));
/*    */       String line;
/* 70 */       while ((line = br.readLine()) != null) {
/* 71 */         String[] words = line.split("\\s+");
/* 72 */         for (int i = 0; i < words.length; i++) {
/* 73 */           String w = escapeString(words[i]);
/* 74 */           pw.print(w);
/* 75 */           if (i != words.length - 1) {
/* 76 */             pw.print(" ");
/*    */           }
/*    */         }
/* 79 */         pw.println();
/*    */       }
/* 81 */       br.close();
/* 82 */       pw.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\arabic\IBMArabicEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */