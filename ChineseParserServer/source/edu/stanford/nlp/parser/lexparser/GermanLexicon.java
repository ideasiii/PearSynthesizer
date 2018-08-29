/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.stats.Counter;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class GermanLexicon
/*    */   extends BaseLexicon
/*    */ {
/*    */   private GermanUnknownWordModel unknown;
/*    */   private static final long serialVersionUID = 221L;
/*    */   
/*    */   public GermanLexicon(Options.LexOptions op)
/*    */   {
/* 18 */     super(op);
/* 19 */     this.unknown = new GermanUnknownWordModel(op);
/*    */   }
/*    */   
/*    */   public void train(Collection<Tree> trees)
/*    */   {
/* 24 */     super.train(trees);
/* 25 */     this.unknown.train(trees);
/*    */   }
/*    */   
/*    */   public float score(IntTaggedWord iTW, int loc)
/*    */   {
/* 30 */     double c_W = this.seenCounter.getCount(iTW);
/*    */     
/* 32 */     boolean seen = c_W > 0.0D;
/*    */     
/* 34 */     if (seen) {
/* 35 */       return super.score(iTW, loc);
/*    */     }
/* 37 */     return (float)this.unknown.score(iTW);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\GermanLexicon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */