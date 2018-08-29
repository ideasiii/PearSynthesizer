/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.ling.LabelFactory;
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
/*    */ public class SimpleTreeFactory
/*    */   implements TreeFactory
/*    */ {
/*    */   public SimpleTreeFactory() {}
/*    */   
/*    */   public SimpleTreeFactory(LabelFactory lf) {}
/*    */   
/*    */   public Tree newLeaf(String word)
/*    */   {
/* 39 */     return new SimpleTree();
/*    */   }
/*    */   
/*    */   public Tree newLeaf(Label word) {
/* 43 */     return new SimpleTree();
/*    */   }
/*    */   
/*    */   public Tree newTreeNode(String parent, List children) {
/* 47 */     return new SimpleTree(null, children);
/*    */   }
/*    */   
/*    */   public Tree newTreeNode(Label parentLabel, List children) {
/* 51 */     return new SimpleTree(parentLabel, children);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\SimpleTreeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */