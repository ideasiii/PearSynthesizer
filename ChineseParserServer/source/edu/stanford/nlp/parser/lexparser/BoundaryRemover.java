/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.TreeFactory;
/*    */ import edu.stanford.nlp.trees.TreeTransformer;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BoundaryRemover
/*    */   implements TreeTransformer
/*    */ {
/*    */   public Tree transformTree(Tree tree)
/*    */   {
/* 22 */     Tree last = tree.lastChild();
/* 23 */     if ((last.label().value().equals(".$$.")) || (last.label().value().equals(".$.")))
/*    */     {
/* 25 */       List<Tree> childList = tree.getChildrenAsList();
/* 26 */       List<Tree> lastGoneList = childList.subList(0, childList.size() - 1);
/* 27 */       return tree.treeFactory().newTreeNode(tree.label(), lastGoneList);
/*    */     }
/* 29 */     return tree;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\BoundaryRemover.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */