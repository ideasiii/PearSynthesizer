/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.Trees;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ class DeleteNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   public DeleteNode(TsurgeonPattern[] children)
/*    */   {
/* 14 */     super("delete", children);
/*    */   }
/*    */   
/*    */   public DeleteNode(List<TsurgeonPattern> children) {
/* 18 */     this((TsurgeonPattern[])children.toArray(EMPTY_TSURGEON_ARRAY));
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m)
/*    */   {
/* 23 */     Tree result = t;
/* 24 */     for (TsurgeonPattern child : this.children) {
/* 25 */       Tree nodeToDelete = child.evaluate(t, m);
/* 26 */       if (nodeToDelete == t) {
/* 27 */         result = null;
/*    */       }
/* 29 */       Tree parent = nodeToDelete.parent(t);
/* 30 */       parent.removeChild(Trees.objectEqualityIndexOf(parent, nodeToDelete));
/*    */     }
/* 32 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\DeleteNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */