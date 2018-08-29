/*    */ package edu.stanford.nlp.trees.international.pennchinese;
/*    */ 
/*    */ import edu.stanford.nlp.ling.HasWord;
/*    */ import edu.stanford.nlp.process.Function;
/*    */ import edu.stanford.nlp.util.StringUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public class ChineseEscaper
/*    */   implements Function<List<HasWord>, List<HasWord>>
/*    */ {
/* 25 */   private static Pattern p2 = Pattern.compile("\\$[a-z]+_\\((.*?)\\|\\|.*?\\)");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<HasWord> apply(List<HasWord> arg)
/*    */   {
/* 32 */     List<HasWord> ans = new ArrayList(arg);
/* 33 */     for (HasWord wd : ans) {
/* 34 */       String w = wd.word();
/* 35 */       Matcher m2 = p2.matcher(w);
/*    */       
/* 37 */       if (m2.find())
/*    */       {
/* 39 */         w = m2.replaceAll("$1");
/*    */       }
/*    */       
/* 42 */       String newW = StringUtils.tr(w, "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", "！＂＃＄％＆＇（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［＼］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～");
/*    */       
/*    */ 
/* 45 */       wd.setWord(newW);
/*    */     }
/* 47 */     return ans;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\ChineseEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */