/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import edu.stanford.nlp.util.Pair;
/*    */ 
/*    */ class InsertNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   TreeLocation l;
/*    */   
/*    */   public InsertNode(TsurgeonPattern child, TreeLocation l)
/*    */   {
/* 14 */     super("insert", new TsurgeonPattern[] { child });
/* 15 */     this.l = l;
/*    */   }
/*    */   
/*    */   protected void setRoot(TsurgeonPatternRoot root) {
/* 19 */     super.setRoot(root);
/* 20 */     this.l.setRoot(root);
/*    */   }
/*    */   
/*    */   public InsertNode(AuxiliaryTree t, TreeLocation l) {
/* 24 */     this(new HoldTreeNode(t), l);
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 28 */     Tree nodeToInsert = this.children[0].evaluate(t, m);
/* 29 */     Pair<Tree, Integer> position = this.l.evaluate(t, m);
/* 30 */     ((Tree)position.first()).insertDtr(nodeToInsert, ((Integer)position.second()).intValue());
/* 31 */     return t;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 35 */     return this.label + "(" + this.children[0] + " " + this.l + ")";
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\InsertNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */