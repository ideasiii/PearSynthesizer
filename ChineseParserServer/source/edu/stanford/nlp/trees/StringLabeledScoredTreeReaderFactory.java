/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.StringLabelFactory;
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
/*    */ 
/*    */ 
/*    */ public class StringLabeledScoredTreeReaderFactory
/*    */   implements TreeReaderFactory
/*    */ {
/*    */   public TreeReader newTreeReader(Reader in)
/*    */   {
/* 24 */     return new PennTreeReader(in, new LabeledScoredTreeFactory(new StringLabelFactory()));
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\StringLabeledScoredTreeReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */