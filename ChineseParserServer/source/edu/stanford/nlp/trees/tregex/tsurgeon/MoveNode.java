/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.Trees;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import edu.stanford.nlp.util.Pair;
/*    */ 
/*    */ class MoveNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   TreeLocation l;
/*    */   
/*    */   public MoveNode(TsurgeonPattern child, TreeLocation l)
/*    */   {
/* 15 */     super("move", new TsurgeonPattern[] { child });
/* 16 */     this.l = l;
/*    */   }
/*    */   
/*    */   protected void setRoot(TsurgeonPatternRoot root) {
/* 20 */     super.setRoot(root);
/* 21 */     this.l.setRoot(root);
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 25 */     Tree nodeToMove = this.children[0].evaluate(t, m);
/* 26 */     Tree oldParent = nodeToMove.parent(t);
/* 27 */     oldParent.removeChild(Trees.objectEqualityIndexOf(oldParent, nodeToMove));
/* 28 */     Pair<Tree, Integer> position = this.l.evaluate(t, m);
/* 29 */     ((Tree)position.first()).insertDtr(nodeToMove, ((Integer)position.second()).intValue());
/* 30 */     return t;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 34 */     return this.label + "(" + this.children[0] + " " + this.l + ")";
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\MoveNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */