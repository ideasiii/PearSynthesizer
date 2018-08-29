/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.ling.StringLabel;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.TreeFactory;
/*    */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*    */ import edu.stanford.nlp.trees.international.negra.NegraPennLanguagePack;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ class NegraPennCollinizer implements edu.stanford.nlp.trees.TreeTransformer
/*    */ {
/* 14 */   private TreebankLanguagePack tlp = new NegraPennLanguagePack();
/*    */   private final boolean deletePunct;
/*    */   
/*    */   public NegraPennCollinizer() {
/* 18 */     this(true);
/*    */   }
/*    */   
/*    */   public NegraPennCollinizer(boolean deletePunct) {
/* 22 */     this.deletePunct = deletePunct;
/*    */   }
/*    */   
/* 25 */   protected TreeFactory tf = new edu.stanford.nlp.trees.LabeledScoredTreeFactory();
/*    */   
/*    */   public Tree transformTree(Tree tree) {
/* 28 */     Label l = tree.label();
/* 29 */     String s = l.value();
/* 30 */     if (tree.isLeaf()) {
/* 31 */       return this.tf.newLeaf(s);
/*    */     }
/* 33 */     s = this.tlp.basicCategory(s);
/* 34 */     if (this.deletePunct)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/* 39 */       if ((tree.isPreTerminal()) && (this.tlp.isEvalBIgnoredPunctuationTag(s))) {
/* 40 */         return null;
/*    */       }
/*    */     }
/*    */     
/* 44 */     if (tree.children()[0].label().value().equals("TOPP")) {
/* 45 */       System.err.println("Found a TOPP");
/* 46 */       tree.setChildren(tree.children()[0].children());
/*    */     }
/*    */     
/*    */ 
/* 50 */     if ((this.tlp.isStartSymbol(s)) && (tree.children().length == 1))
/*    */     {
/* 52 */       return transformTree(tree.children()[0]);
/*    */     }
/* 54 */     List children = new ArrayList();
/* 55 */     for (int cNum = 0; cNum < tree.children().length; cNum++) {
/* 56 */       Tree child = tree.children()[cNum];
/* 57 */       Tree newChild = transformTree(child);
/* 58 */       if (newChild != null) {
/* 59 */         children.add(newChild);
/*    */       }
/*    */     }
/* 62 */     if (children.size() == 0) {
/* 63 */       return null;
/*    */     }
/* 65 */     return this.tf.newTreeNode(new StringLabel(s), children);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\NegraPennCollinizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */