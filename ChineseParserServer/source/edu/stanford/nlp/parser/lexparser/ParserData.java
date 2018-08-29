/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.util.Numberer;
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParserData
/*    */   implements Serializable
/*    */ {
/*    */   public Lexicon lex;
/*    */   public BinaryGrammar bg;
/*    */   public UnaryGrammar ug;
/*    */   public DependencyGrammar dg;
/*    */   public Map<String, Numberer> numbs;
/*    */   public Options pt;
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ParserData(Lexicon lex, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Map<String, Numberer> numbs, Options pt)
/*    */   {
/* 27 */     this.lex = lex;
/* 28 */     this.bg = bg;
/* 29 */     this.ug = ug;
/* 30 */     this.dg = dg;
/* 31 */     this.numbs = numbs;
/* 32 */     this.pt = pt;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ParserData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */