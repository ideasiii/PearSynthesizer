/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.process.PTBTokenizer;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ 
/*     */ public class PennTreebankLanguagePack
/*     */   extends AbstractTreebankLanguagePack
/*     */   implements Serializable
/*     */ {
/*  26 */   private static String[] pennPunctTags = { "''", "``", "-LRB-", "-RRB-", ".", ":", "," };
/*     */   
/*  28 */   private static String[] pennSFPunctTags = { "." };
/*     */   
/*  30 */   private static String[] collinsPunctTags = { "''", "``", ".", ":", "," };
/*     */   
/*  32 */   private static String[] pennPunctWords = { "''", "'", "``", "`", "-LRB-", "-RRB-", "-LCB-", "-RCB-", ".", "?", "!", ",", ":", "-", "--", "...", ";" };
/*     */   
/*  34 */   private static String[] pennSFPunctWords = { ".", "!", "?" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  42 */   private static char[] annotationIntroducingChars = { '-', '=', '|', '#', '^', '~', '_' };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private static String[] pennStartSymbols = { "ROOT", "TOP" };
/*     */   
/*     */ 
/*     */   private static final long serialVersionUID = 9081305982861675328L;
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] punctuationTags()
/*     */   {
/*  56 */     return pennPunctTags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] punctuationWords()
/*     */   {
/*  66 */     return pennPunctWords;
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
/*  77 */     return pennSFPunctTags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] sentenceFinalPunctuationWords()
/*     */   {
/*  87 */     return pennSFPunctWords;
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
/* 100 */     return collinsPunctTags;
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
/* 115 */     return annotationIntroducingChars;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] startSymbols()
/*     */   {
/* 125 */     return pennStartSymbols;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenizerFactory getTokenizerFactory()
/*     */   {
/* 134 */     return PTBTokenizer.factory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String treebankFileExtension()
/*     */   {
/* 142 */     return "mrg";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GrammaticalStructureFactory grammaticalStructureFactory()
/*     */   {
/* 151 */     return new GrammaticalStructureFactory("edu.stanford.nlp.trees.EnglishGrammaticalStructure");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GrammaticalStructureFactory grammaticalStructureFactory(Filter<String> puncFilter)
/*     */   {
/* 160 */     return new GrammaticalStructureFactory("edu.stanford.nlp.trees.EnglishGrammaticalStructure", puncFilter);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 166 */     TreebankLanguagePack tlp = new PennTreebankLanguagePack();
/* 167 */     System.out.println("Start symbol: " + tlp.startSymbol());
/* 168 */     String start = tlp.startSymbol();
/* 169 */     System.out.println("Should be true: " + tlp.isStartSymbol(start));
/* 170 */     String[] strs = { "-", "-LLB-", "NP-2", "NP=3", "NP-LGS", "NP-TMP=3" };
/* 171 */     for (int i = 0; i < strs.length; i++) {
/* 172 */       String str = strs[i];
/* 173 */       System.out.println("String: " + str + " basic: " + tlp.basicCategory(str) + " basicAndFunc: " + tlp.categoryAndFunction(str));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\PennTreebankLanguagePack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */