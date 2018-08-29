/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ class AdjoinNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   private final AuxiliaryTree adjunctionTree;
/*    */   
/*    */   public AdjoinNode(AuxiliaryTree t, TsurgeonPattern p)
/*    */   {
/* 15 */     super("adjoin", new TsurgeonPattern[] { p });
/* 16 */     if ((t == null) || (p == null)) {
/* 17 */       throw new IllegalArgumentException("AdjoinNode: illegal null argument, t=" + t + ", p=" + p);
/*    */     }
/* 19 */     this.adjunctionTree = t;
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 23 */     Tree targetNode = this.children[0].evaluate(t, m);
/* 24 */     Tree parent = targetNode.parent(t);
/* 25 */     AuxiliaryTree ft = this.adjunctionTree.copy(this);
/* 26 */     ft.foot.setChildren(targetNode.getChildrenAsList());
/* 27 */     if (parent == null) {
/* 28 */       return ft.tree;
/*    */     }
/* 30 */     int i = parent.indexOf(targetNode);
/* 31 */     parent.setChild(i, ft.tree);
/* 32 */     return t;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 37 */     return super.toString() + "<-" + this.adjunctionTree.toString();
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\AdjoinNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */