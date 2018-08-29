/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ 
/*    */ 
/*    */ class ReplaceNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   public ReplaceNode(TsurgeonPattern oldNode, TsurgeonPattern newNode)
/*    */   {
/* 12 */     super("replace", new TsurgeonPattern[] { oldNode, newNode });
/*    */   }
/*    */   
/*    */   public ReplaceNode(TsurgeonPattern oldNode, AuxiliaryTree t) {
/* 16 */     this(oldNode, new HoldTreeNode(t));
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 20 */     Tree oldNode = this.children[0].evaluate(t, m);
/* 21 */     Tree newNode = this.children[1].evaluate(t, m);
/* 22 */     if (oldNode == t)
/* 23 */       return newNode;
/* 24 */     Tree parent = oldNode.parent(t);
/* 25 */     int i = parent.indexOf(oldNode);
/* 26 */     parent.removeChild(i);
/* 27 */     parent.insertDtr(newNode, i);
/* 28 */     return t;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\ReplaceNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */