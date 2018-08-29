/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ 
/*    */ 
/*    */ class HoldTreeNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   AuxiliaryTree subTree;
/*    */   
/*    */   public HoldTreeNode(AuxiliaryTree t)
/*    */   {
/* 14 */     super("hold", new TsurgeonPattern[0]);
/* 15 */     this.subTree = t;
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 19 */     return this.subTree.copy(this).tree;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 23 */     return this.subTree.toString();
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\HoldTreeNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */