/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.TreeFactory;
/*    */ import edu.stanford.nlp.trees.TreeTransformer;
/*    */ import edu.stanford.nlp.trees.TreebankLanguagePack;
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
/*    */ class TreeCollinizer
/*    */   implements TreeTransformer
/*    */ {
/*    */   private TreebankLanguagePack tlp;
/*    */   private final boolean deletePunct;
/*    */   private final boolean fixCollinsBaseNP;
/*    */   
/*    */   public TreeCollinizer(TreebankLanguagePack tlp)
/*    */   {
/* 28 */     this(tlp, true, false);
/*    */   }
/*    */   
/*    */   public TreeCollinizer(TreebankLanguagePack tlp, boolean deletePunct, boolean fixCollinsBaseNP)
/*    */   {
/* 33 */     this.tlp = tlp;
/* 34 */     this.deletePunct = deletePunct;
/* 35 */     this.fixCollinsBaseNP = fixCollinsBaseNP;
/*    */   }
/*    */   
/* 38 */   protected TreeFactory tf = new LabeledScoredTreeFactory();
/*    */   
/*    */   public Tree transformTree(Tree tree) {
/* 41 */     String s = tree.value();
/* 42 */     if (tree.isLeaf()) {
/* 43 */       return this.tf.newLeaf(s);
/*    */     }
/* 45 */     s = this.tlp.basicCategory(s);
/* 46 */     if (this.deletePunct)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/* 51 */       if ((tree.isPreTerminal()) && (this.tlp.isEvalBIgnoredPunctuationTag(s))) {
/* 52 */         return null;
/*    */       }
/*    */     }
/*    */     
/* 56 */     if ((this.fixCollinsBaseNP) && (s.equals("NP"))) {
/* 57 */       Tree[] kids = tree.children();
/* 58 */       if ((kids.length == 1) && (this.tlp.basicCategory(kids[0].value()).equals("NP"))) {
/* 59 */         return transformTree(kids[0]);
/*    */       }
/*    */     }
/*    */     
/* 63 */     if (this.tlp.isStartSymbol(s))
/*    */     {
/* 65 */       return transformTree(tree.children()[0]);
/*    */     }
/*    */     
/* 68 */     if (s.equals("PRT")) {
/* 69 */       s = "ADVP";
/*    */     }
/* 71 */     List<Tree> children = new ArrayList();
/* 72 */     int cNum = 0; for (int numKids = tree.numChildren(); cNum < numKids; cNum++) {
/* 73 */       Tree child = tree.children()[cNum];
/* 74 */       Tree newChild = transformTree(child);
/* 75 */       if (newChild != null) {
/* 76 */         children.add(newChild);
/*    */       }
/*    */     }
/* 79 */     if (children.size() == 0) {
/* 80 */       return null;
/*    */     }
/* 82 */     return this.tf.newTreeNode(s, children);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\TreeCollinizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */