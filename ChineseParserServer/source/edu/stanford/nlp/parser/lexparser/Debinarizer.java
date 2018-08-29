/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.ling.LabelFactory;
/*    */ import edu.stanford.nlp.ling.StringLabelFactory;
/*    */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.TreeFactory;
/*    */ import edu.stanford.nlp.trees.TreeTransformer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Debinarizer
/*    */   implements TreeTransformer
/*    */ {
/*    */   private final TreeFactory tf;
/*    */   private final boolean forceCNF;
/*    */   
/*    */   protected Tree transformTreeHelper(Tree t)
/*    */   {
/* 25 */     if (t.isLeaf()) {
/* 26 */       Tree leaf = this.tf.newLeaf(t.label());
/* 27 */       leaf.setScore(t.score());
/* 28 */       return leaf;
/*    */     }
/* 30 */     List<Tree> newChildren = new ArrayList(20);
/* 31 */     int childNum = 0; for (int numKids = t.numChildren(); childNum < numKids; childNum++) {
/* 32 */       Tree child = t.getChild(childNum);
/* 33 */       Tree newChild = transformTreeHelper(child);
/* 34 */       if ((!newChild.isLeaf()) && (newChild.label().value().indexOf('@') >= 0)) {
/* 35 */         newChildren.addAll(newChild.getChildrenAsList());
/*    */       } else {
/* 37 */         newChildren.add(newChild);
/*    */       }
/*    */     }
/* 40 */     Tree node = this.tf.newTreeNode(t.label(), newChildren);
/* 41 */     node.setScore(t.score());
/* 42 */     return node;
/*    */   }
/*    */   
/*    */   public Tree transformTree(Tree t) {
/* 46 */     Tree result = transformTreeHelper(t);
/* 47 */     if (this.forceCNF) {
/* 48 */       result = new CNFTransformers.FromCNFTransformer().transformTree(result);
/*    */     }
/* 50 */     return new BoundaryRemover().transformTree(result);
/*    */   }
/*    */   
/*    */   public Debinarizer(boolean forceCNF) {
/* 54 */     this(forceCNF, new StringLabelFactory());
/*    */   }
/*    */   
/*    */   public Debinarizer(boolean forceCNF, LabelFactory lf) {
/* 58 */     this.forceCNF = forceCNF;
/* 59 */     this.tf = new LabeledScoredTreeFactory(lf);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Debinarizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */