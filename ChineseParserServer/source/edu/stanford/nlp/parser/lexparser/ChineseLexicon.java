/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.ling.TaggedWord;
/*    */ import edu.stanford.nlp.stats.Counter;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChineseLexicon
/*    */   extends BaseLexicon
/*    */ {
/*    */   private static final boolean useRandomWalk = false;
/* 18 */   public static boolean useCharBasedUnknownWordModel = false;
/*    */   
/* 20 */   public static boolean useGoodTuringUnknownWordModel = false;
/*    */   
/*    */   private ChineseUnknownWordModel unknown;
/*    */   private static final int STEPS = 1;
/*    */   private RandomWalk probRandomWalk;
/*    */   
/*    */   public ChineseLexicon(Options.LexOptions op)
/*    */   {
/* 28 */     super(op);
/*    */     
/*    */ 
/*    */ 
/* 32 */     this.unknown = new ChineseUnknownWordModel();
/* 33 */     if (useGoodTuringUnknownWordModel) {
/* 34 */       this.unknown.useGoodTuring();
/*    */     }
/* 36 */     this.unknown.useUnicodeType = op.useUnicodeType;
/*    */   }
/*    */   
/*    */ 
/*    */   public void train(Collection<Tree> trees)
/*    */   {
/* 42 */     super.train(trees);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 49 */     this.unknown.train(trees);
/*    */   }
/*    */   
/*    */ 
/*    */   public float score(IntTaggedWord iTW, int loc)
/*    */   {
/* 55 */     double c_W = this.seenCounter.getCount(iTW);
/*    */     
/* 57 */     boolean seen = c_W > 0.0D;
/*    */     
/* 59 */     if (seen)
/*    */     {
/*    */ 
/*    */ 
/* 63 */       return super.score(iTW, loc);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 70 */     double score = this.unknown.score(iTW);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 75 */     return (float)score;
/*    */   }
/*    */   
/*    */   private double scoreRandomWalk(IntTaggedWord itw)
/*    */   {
/* 80 */     TaggedWord tw = itw.toTaggedWord();
/* 81 */     String word = tw.value();
/* 82 */     String tag = tw.tag();
/*    */     
/* 84 */     return this.probRandomWalk.score(tag, word);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ChineseLexicon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */