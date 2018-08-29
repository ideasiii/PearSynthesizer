/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.ling.LabelFactory;
/*    */ import edu.stanford.nlp.ling.StringLabel;
/*    */ import java.util.List;
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
/*    */ public class LabeledScoredTreeFactory
/*    */   extends SimpleTreeFactory
/*    */ {
/*    */   private LabelFactory lf;
/*    */   
/*    */   public LabeledScoredTreeFactory()
/*    */   {
/* 25 */     this(StringLabel.factory());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LabeledScoredTreeFactory(LabelFactory lf)
/*    */   {
/* 35 */     this.lf = lf;
/*    */   }
/*    */   
/*    */   public Tree newLeaf(String word) {
/* 39 */     return newLeaf(this.lf.newLabel(word));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Tree newLeaf(Label label)
/*    */   {
/* 50 */     return new LabeledScoredTreeLeaf(label);
/*    */   }
/*    */   
/*    */   public Tree newTreeNode(String parent, List children) {
/* 54 */     return newTreeNode(this.lf.newLabel(parent), children);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Tree newTreeNode(Label parentLabel, List children)
/*    */   {
/* 67 */     return new LabeledScoredTreeNode(parentLabel, children);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\LabeledScoredTreeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */