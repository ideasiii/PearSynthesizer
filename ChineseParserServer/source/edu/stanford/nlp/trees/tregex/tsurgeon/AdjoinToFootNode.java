/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ class AdjoinToFootNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   AuxiliaryTree adjunctionTree;
/*    */   
/*    */   public AdjoinToFootNode(AuxiliaryTree t, TsurgeonPattern p)
/*    */   {
/* 15 */     super("adjoin", new TsurgeonPattern[] { p });
/* 16 */     this.adjunctionTree = t;
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 20 */     Tree targetNode = this.children[0].evaluate(t, m);
/* 21 */     Tree parent = targetNode.parent(t);
/* 22 */     AuxiliaryTree ft = this.adjunctionTree.copy(this);
/*    */     
/* 24 */     Tree parentOfFoot = ft.foot.parent(ft.tree);
/* 25 */     if (parentOfFoot == null) {
/* 26 */       System.err.println("Warning: adjoin to foot for depth-1 auxiliary tree has no effect.");
/* 27 */       return t;
/*    */     }
/* 29 */     int i = parentOfFoot.indexOf(ft.foot);
/* 30 */     if (parent == null) {
/* 31 */       parentOfFoot.setChild(i, targetNode);
/* 32 */       return ft.tree;
/*    */     }
/*    */     
/* 35 */     int j = parent.indexOf(targetNode);
/* 36 */     parent.setChild(j, ft.tree);
/* 37 */     parentOfFoot.setChild(i, targetNode);
/* 38 */     return t;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 43 */     return super.toString() + "<-" + this.adjunctionTree.toString();
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\AdjoinToFootNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */