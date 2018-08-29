/*    */ package edu.stanford.nlp.trees.tregex;
/*    */ 
/*    */ import edu.stanford.nlp.process.Function;
/*    */ import edu.stanford.nlp.trees.CollinsHeadFinder;
/*    */ import edu.stanford.nlp.trees.HeadFinder;
/*    */ import edu.stanford.nlp.trees.PennTreebankLanguagePack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TregexPatternCompiler
/*    */ {
/* 16 */   private Function<String, String> basicCatFunction = new PennTreebankLanguagePack().getBasicCategoryFunction();
/* 17 */   private HeadFinder headFinder = new CollinsHeadFinder();
/*    */   
/* 19 */   public static TregexPatternCompiler defaultCompiler = new TregexPatternCompiler();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public TregexPatternCompiler() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public TregexPatternCompiler(Function<String, String> basicCatFunction)
/*    */   {
/* 30 */     this.basicCatFunction = basicCatFunction;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TregexPatternCompiler(HeadFinder headFinder)
/*    */   {
/* 39 */     this.headFinder = headFinder;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TregexPatternCompiler(HeadFinder headFinder, Function<String, String> basicCatFunction)
/*    */   {
/* 49 */     this.headFinder = headFinder;
/* 50 */     this.basicCatFunction = basicCatFunction;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TregexPattern compile(String tregex)
/*    */     throws ParseException
/*    */   {
/* 62 */     TregexPattern.setBasicCatFunction(this.basicCatFunction);
/* 63 */     Relation.setHeadFinder(this.headFinder);
/* 64 */     TregexPattern pattern = TregexParser.parse(tregex);
/* 65 */     pattern.setPatternString(tregex);
/* 66 */     return pattern;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\TregexPatternCompiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */