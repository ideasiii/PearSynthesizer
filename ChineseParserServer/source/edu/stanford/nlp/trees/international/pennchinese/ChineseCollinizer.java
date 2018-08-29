/*    */ package edu.stanford.nlp.trees.international.pennchinese;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.ling.StringLabel;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ChineseCollinizer
/*    */   implements TreeTransformer
/*    */ {
/*    */   private static final boolean VERBOSE = false;
/*    */   private final boolean deletePunct;
/*    */   ChineseTreebankLanguagePack ctlp;
/* 32 */   protected TreeFactory tf = new LabeledScoredTreeFactory();
/*    */   
/*    */   public ChineseCollinizer(ChineseTreebankLanguagePack ctlp)
/*    */   {
/* 36 */     this(ctlp, true);
/*    */   }
/*    */   
/*    */   public ChineseCollinizer(ChineseTreebankLanguagePack ctlp, boolean deletePunct) {
/* 40 */     this.deletePunct = deletePunct;
/* 41 */     this.ctlp = ctlp;
/*    */   }
/*    */   
/*    */   public Tree transformTree(Tree tree)
/*    */   {
/* 46 */     return transformTree(tree, true);
/*    */   }
/*    */   
/*    */   private Tree transformTree(Tree tree, boolean isRoot) {
/* 50 */     String label = tree.label().value();
/*    */     
/*    */ 
/*    */ 
/* 54 */     if (tree.isLeaf()) {
/* 55 */       if ((this.deletePunct) && (this.ctlp.isPunctuationWord(label))) {
/* 56 */         return null;
/*    */       }
/* 58 */       return this.tf.newLeaf(new StringLabel(label));
/*    */     }
/*    */     
/* 61 */     if ((tree.isPreTerminal()) && (this.deletePunct) && (this.ctlp.isPunctuationTag(label)))
/*    */     {
/* 63 */       return null;
/*    */     }
/* 65 */     List children = new ArrayList();
/*    */     
/* 67 */     if ((label.matches("ROOT.*")) && (tree.numChildren() == 1)) {
/* 68 */       return transformTree(tree.children()[0], true);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 74 */     label = label.replaceFirst("[^A-Z].*$", "");
/*    */     
/* 76 */     label = label.replaceFirst("PRN", "ADVP");
/*    */     
/*    */ 
/*    */ 
/* 80 */     for (int cNum = 0; cNum < tree.children().length; cNum++) {
/* 81 */       Tree child = tree.children()[cNum];
/* 82 */       Tree newChild = transformTree(child, false);
/* 83 */       if (newChild != null) {
/* 84 */         children.add(newChild);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 89 */     if ((children.size() == 0) && (!isRoot))
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/* 94 */       return null;
/*    */     }
/* 96 */     return this.tf.newTreeNode(new StringLabel(label), children);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\ChineseCollinizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */