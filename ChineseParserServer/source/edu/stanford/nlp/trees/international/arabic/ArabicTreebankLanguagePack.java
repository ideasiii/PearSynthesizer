/*     */ package edu.stanford.nlp.trees.international.arabic;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.trees.AbstractTreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.PennTreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.io.PrintStream;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class ArabicTreebankLanguagePack
/*     */   extends AbstractTreebankLanguagePack
/*     */ {
/*     */   private boolean detPlusNounIsBasicCategory;
/*     */   
/*     */   public ArabicTreebankLanguagePack()
/*     */   {
/*  30 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArabicTreebankLanguagePack(boolean detPlusNounIsBasicCategory)
/*     */   {
/*  41 */     this.detPlusNounIsBasicCategory = detPlusNounIsBasicCategory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private static String[] pennPunctTags = { "PUNC", "," };
/*     */   
/*  49 */   private static String[] pennSFPunctTags = { "." };
/*     */   
/*  51 */   private static String[] collinsPunctTags = { "PUNC" };
/*     */   
/*  53 */   private static String[] pennPunctWords = { "''", "'", "``", "`", "-LRB-", "-RRB-", "-LCB-", "-RCB-", ".", "?", "!", ",", ":", "-", "--", "...", ";", "%", "&", "\"", "\"__", "%", "&", "*", "+", ",", "-", "-RRB-_", "-RRB-__", "-_", "-__", ".", "..", "......", "/", "05,", "115,", "1985,", "1998,", "30,", "910,192,2", "910,492,2", ":", ":_", ":__", ";", "?\"", "?\".", "?", "?.", "En", "_", "A", "=" };
/*     */   
/*  55 */   private static String[] pennSFPunctWords = { ".", "!", "?", "?\"", "?\".", "?", "?." };
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
/*  66 */   private static char[] annotationIntroducingChars = { '-', '=', '|', '#', '^', '~', '+' };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private static String[] pennStartSymbols = { "ROOT" };
/*     */   
/*     */ 
/*     */   private TokenizerFactory<? extends HasWord> tf;
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] punctuationTags()
/*     */   {
/*  80 */     return pennPunctTags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] punctuationWords()
/*     */   {
/*  90 */     return pennPunctWords;
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
/* 101 */     return pennSFPunctTags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] sentenceFinalPunctuationWords()
/*     */   {
/* 111 */     return pennSFPunctWords;
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
/*     */   public String[] evalBIgnoredPunctuationTags()
/*     */   {
/* 124 */     return collinsPunctTags;
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
/*     */   public char[] labelAnnotationIntroducingCharacters()
/*     */   {
/* 139 */     return annotationIntroducingChars;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] startSymbols()
/*     */   {
/* 149 */     return pennStartSymbols;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTokenizerFactory(TokenizerFactory<? extends HasWord> tf)
/*     */   {
/* 155 */     this.tf = tf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenizerFactory<? extends HasWord> getTokenizerFactory()
/*     */   {
/* 167 */     if (this.tf == null) {
/* 168 */       this.tf = ArabicTokenizer.factory();
/*     */     }
/* 170 */     return this.tf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String treebankFileExtension()
/*     */   {
/* 178 */     return "tree";
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 182 */     TreebankLanguagePack tlp = new PennTreebankLanguagePack();
/* 183 */     System.out.println("Start symbol: " + tlp.startSymbol());
/* 184 */     String start = tlp.startSymbol();
/* 185 */     System.out.println("Should be true: " + tlp.isStartSymbol(start));
/* 186 */     String[] strs = { "-", "-LLB-", "NP-2", "NP=3", "NP-LGS", "NP-TMP=3" };
/* 187 */     for (String str : strs) {
/* 188 */       System.out.println("String: " + str + " basic: " + tlp.basicCategory(str) + " basicAndFunc: " + tlp.categoryAndFunction(str));
/*     */     }
/*     */   }
/*     */   
/* 192 */   private static final Pattern detPlusNounPattern = Pattern.compile("^DET\\+NOUN");
/*     */   static final String detPlusNoun = "DET+NOUN";
/*     */   private static final long serialVersionUID = 9081305982861675328L;
/*     */   
/*     */   public String basicCategory(String category) {
/* 197 */     if ((this.detPlusNounIsBasicCategory) && (detPlusNounPattern.matcher(category).find()))
/*     */     {
/* 199 */       return "DET+NOUN";
/*     */     }
/* 201 */     return super.basicCategory(category);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 206 */     return "ArabicTreebankLanguagePack";
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\arabic\ArabicTreebankLanguagePack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */