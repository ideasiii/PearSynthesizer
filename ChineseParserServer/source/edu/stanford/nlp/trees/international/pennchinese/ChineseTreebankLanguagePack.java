/*     */ package edu.stanford.nlp.trees.international.pennchinese;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.trees.AbstractTreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.GrammaticalStructureFactory;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import edu.stanford.nlp.util.Filters;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChineseTreebankLanguagePack
/*     */   extends AbstractTreebankLanguagePack
/*     */   implements Serializable
/*     */ {
/*     */   private static TokenizerFactory tf;
/*     */   public static final String ENCODING = "GB18030";
/*     */   
/*     */   public static void setTokenizerFactory(TokenizerFactory tf)
/*     */   {
/*  24 */     tf = tf;
/*     */   }
/*     */   
/*     */   public TokenizerFactory<? extends HasWord> getTokenizerFactory() {
/*  28 */     if (tf != null) {
/*  29 */       return tf;
/*     */     }
/*  31 */     return super.getTokenizerFactory();
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
/*     */   public String getEncoding()
/*     */   {
/*  44 */     return "GB18030";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPunctuationTag(String str)
/*     */   {
/*  54 */     return str.equals("PU");
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
/*     */   public boolean isPunctuationWord(String str)
/*     */   {
/*  67 */     return (chineseCommaAcceptFilter().accept(str)) || (chineseEndSentenceAcceptFilter().accept(str)) || (chineseDouHaoAcceptFilter().accept(str)) || (chineseQuoteMarkAcceptFilter().accept(str)) || (chineseParenthesisAcceptFilter().accept(str)) || (chineseColonAcceptFilter().accept(str)) || (chineseDashAcceptFilter().accept(str)) || (chineseOtherAcceptFilter().accept(str));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSentenceFinalPunctuationTag(String str)
/*     */   {
/*  79 */     return chineseEndSentenceAcceptFilter().accept(str);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] punctuationTags()
/*     */   {
/*  89 */     return tags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] punctuationWords()
/*     */   {
/*  99 */     return punctWords;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] sentenceFinalPunctuationTags()
/*     */   {
/* 110 */     return tags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] sentenceFinalPunctuationWords()
/*     */   {
/* 120 */     return endSentence;
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
/*     */   public boolean isEvalBIgnoredPunctuationTag(String str)
/*     */   {
/* 134 */     return Filters.collectionAcceptFilter(tags).accept(str);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */   private static final char[] annotationIntroducingChars = { '-', '=', '|', '#', '^', '~' };
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
/*     */   public char[] labelAnnotationIntroducingCharacters()
/*     */   {
/* 157 */     return annotationIntroducingChars;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 165 */   private static final String[] startSymbols = { "ROOT" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] startSymbols()
/*     */   {
/* 173 */     return startSymbols;
/*     */   }
/*     */   
/*     */ 
/* 177 */   private static final String[] tags = { "PU" };
/* 178 */   private static final String[] comma = { ",", "，", "　" };
/* 179 */   private static final String[] endSentence = { "。", "．", "！", "？", "?", "!", "." };
/* 180 */   private static final String[] douHao = { "、" };
/* 181 */   private static final String[] quoteMark = { "“", "”", "‘", "’", "《", "》", "『", "』", "〈", "〉", "「", "」", "＂", "＜", "＞", "`", "＇" };
/* 182 */   private static final String[] parenthesis = { "（", "）", "-LRB-", "-RRB-", "【", "】" };
/* 183 */   private static final String[] colon = { "：", "；", "∶", ":" };
/* 184 */   private static final String[] dash = { "…", "—", "——", "———", "－", "－－", "──", "━", "━━", "—－", "-", "----", "~", "……", "～" };
/* 185 */   private static final String[] other = { "·", "／", "／", "＊", "＆", "/", "//", "*" };
/*     */   
/* 187 */   private static String[] leftQuoteMark = { "“", "‘", "《", "『", "〈", "「", "＜", "`" };
/* 188 */   private static String[] rightQuoteMark = { "”", "’", "》", "』", "〉", "」", "＞", "＇" };
/* 189 */   private static String[] leftParenthesis = { "（", "-LRB-", "【" };
/* 190 */   private static String[] rightParenthesis = { "）", "-RRB-", "】" };
/*     */   private static final String[] punctWords;
/*     */   private static final long serialVersionUID = 5757403475523638802L;
/*     */   
/*     */   static
/*     */   {
/* 196 */     int n = tags.length + comma.length + endSentence.length + douHao.length + quoteMark.length + parenthesis.length + colon.length + dash.length + other.length;
/* 197 */     punctWords = new String[n];
/* 198 */     int m = 0;
/* 199 */     System.arraycopy(tags, 0, punctWords, m, tags.length);
/* 200 */     m += tags.length;
/* 201 */     System.arraycopy(comma, 0, punctWords, m, comma.length);
/* 202 */     m += comma.length;
/* 203 */     System.arraycopy(endSentence, 0, punctWords, m, endSentence.length);
/* 204 */     m += endSentence.length;
/* 205 */     System.arraycopy(douHao, 0, punctWords, m, douHao.length);
/* 206 */     m += douHao.length;
/* 207 */     System.arraycopy(quoteMark, 0, punctWords, m, quoteMark.length);
/* 208 */     m += quoteMark.length;
/* 209 */     System.arraycopy(parenthesis, 0, punctWords, m, parenthesis.length);
/* 210 */     m += parenthesis.length;
/* 211 */     System.arraycopy(colon, 0, punctWords, m, colon.length);
/* 212 */     m += colon.length;
/* 213 */     System.arraycopy(dash, 0, punctWords, m, dash.length);
/* 214 */     m += dash.length;
/* 215 */     System.arraycopy(other, 0, punctWords, m, other.length);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseCommaAcceptFilter() {
/* 219 */     return Filters.collectionAcceptFilter(comma);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseEndSentenceAcceptFilter() {
/* 223 */     return Filters.collectionAcceptFilter(endSentence);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseDouHaoAcceptFilter() {
/* 227 */     return Filters.collectionAcceptFilter(douHao);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseQuoteMarkAcceptFilter() {
/* 231 */     return Filters.collectionAcceptFilter(quoteMark);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseParenthesisAcceptFilter() {
/* 235 */     return Filters.collectionAcceptFilter(parenthesis);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseColonAcceptFilter() {
/* 239 */     return Filters.collectionAcceptFilter(colon);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseDashAcceptFilter() {
/* 243 */     return Filters.collectionAcceptFilter(dash);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseOtherAcceptFilter() {
/* 247 */     return Filters.collectionAcceptFilter(other);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseLeftParenthesisAcceptFilter()
/*     */   {
/* 252 */     return Filters.collectionAcceptFilter(leftParenthesis);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseRightParenthesisAcceptFilter() {
/* 256 */     return Filters.collectionAcceptFilter(rightParenthesis);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseLeftQuoteMarkAcceptFilter() {
/* 260 */     return Filters.collectionAcceptFilter(leftQuoteMark);
/*     */   }
/*     */   
/*     */   public static Filter<String> chineseRightQuoteMarkAcceptFilter() {
/* 264 */     return Filters.collectionAcceptFilter(rightQuoteMark);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String treebankFileExtension()
/*     */   {
/* 272 */     return "fid";
/*     */   }
/*     */   
/*     */ 
/*     */   public GrammaticalStructureFactory grammaticalStructureFactory()
/*     */   {
/* 278 */     return new GrammaticalStructureFactory("edu.stanford.nlp.trees.international.pennchinese.ChineseGrammaticalStructure");
/*     */   }
/*     */   
/*     */   public GrammaticalStructureFactory grammaticalStructureFactory(Filter<String> puncFilt) {
/* 282 */     return new GrammaticalStructureFactory("edu.stanford.nlp.trees.international.pennchinese.ChineseGrammaticalStructure", puncFilt);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\ChineseTreebankLanguagePack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */