/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.CategoryWordTagFactory;
/*    */ import edu.stanford.nlp.ling.LabelFactory;
/*    */ import java.io.Reader;
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
/*    */ public class LabeledScoredTreeReaderFactory
/*    */   implements TreeReaderFactory
/*    */ {
/*    */   private final LabelFactory lf;
/*    */   
/*    */   public LabeledScoredTreeReaderFactory()
/*    */   {
/* 25 */     this.lf = new CategoryWordTagFactory();
/*    */   }
/*    */   
/*    */   public LabeledScoredTreeReaderFactory(LabelFactory lf) {
/* 29 */     this.lf = lf;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TreeReader newTreeReader(Reader in)
/*    */   {
/* 41 */     return new PennTreeReader(in, new LabeledScoredTreeFactory(this.lf), new BobChrisTreeNormalizer());
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\LabeledScoredTreeReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */