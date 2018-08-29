/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ class AdjoinToHeadNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   AuxiliaryTree adjunctionTree;
/*    */   
/*    */   public AdjoinToHeadNode(AuxiliaryTree t, TsurgeonPattern p)
/*    */   {
/* 15 */     super("adjoin", new TsurgeonPattern[] { p });
/* 16 */     this.adjunctionTree = t;
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 20 */     Tree targetNode = this.children[0].evaluate(t, m);
/* 21 */     Tree parent = targetNode.parent(t);
/* 22 */     AuxiliaryTree ft = this.adjunctionTree.copy(this);
/* 23 */     ft.foot.setChildren(targetNode.getChildrenAsList());
/* 24 */     targetNode.setChildren(ft.tree.getChildrenAsList());
/* 25 */     return t;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 29 */     return super.toString() + "<-" + this.adjunctionTree.toString();
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\AdjoinToHeadNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */