/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.Trees;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ class PruneNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   public PruneNode(TsurgeonPattern[] children)
/*    */   {
/* 14 */     super("prune", children);
/*    */   }
/*    */   
/*    */   public PruneNode(List children) {
/* 18 */     this((TsurgeonPattern[])children.toArray(EMPTY_TSURGEON_ARRAY));
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 22 */     boolean prunedWholeTree = false;
/* 23 */     for (TsurgeonPattern child : this.children) {
/* 24 */       Tree nodeToPrune = child.evaluate(t, m);
/* 25 */       if (pruneHelper(t, nodeToPrune) == null)
/* 26 */         prunedWholeTree = true;
/*    */     }
/* 28 */     return prunedWholeTree ? null : t;
/*    */   }
/*    */   
/*    */   private static Tree pruneHelper(Tree root, Tree nodeToPrune) {
/* 32 */     if (nodeToPrune == root)
/* 33 */       return null;
/* 34 */     Tree parent = nodeToPrune.parent(root);
/* 35 */     parent.removeChild(Trees.objectEqualityIndexOf(parent, nodeToPrune));
/* 36 */     if (parent.children().length == 0)
/* 37 */       return pruneHelper(root, parent);
/* 38 */     return root;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\PruneNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */