/*    */ package edu.stanford.nlp.trees.international.tuebadz;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.TreeFactory;
/*    */ import edu.stanford.nlp.trees.TreeNormalizer;
/*    */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TueBaDZPennTreeNormalizer
/*    */   extends TreeNormalizer
/*    */ {
/* 18 */   private static String root = "ROOT";
/* 19 */   private static String nonUnaryRoot = "ROOT";
/*    */   protected final TreebankLanguagePack tlp;
/*    */   
/* 22 */   public String rootSymbol() { return root; }
/*    */   
/*    */   public String nonUnaryRootSymbol()
/*    */   {
/* 26 */     return nonUnaryRoot;
/*    */   }
/*    */   
/*    */ 
/*    */   public TueBaDZPennTreeNormalizer()
/*    */   {
/* 32 */     this(new TueBaDZLanguagePack());
/*    */   }
/*    */   
/*    */   public TueBaDZPennTreeNormalizer(TreebankLanguagePack tlp) {
/* 36 */     this.tlp = tlp;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String normalizeTerminal(String leaf)
/*    */   {
/* 45 */     return leaf.intern();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String normalizeNonterminal(String category)
/*    */   {
/* 55 */     return cleanUpLabel(category).intern();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String cleanUpLabel(String label)
/*    */   {
/* 63 */     if (label == null) {
/* 64 */       label = root;
/*    */     }
/*    */     
/* 67 */     return label;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Tree normalizeWholeTree(Tree tree, TreeFactory tf)
/*    */   {
/* 78 */     if ((tree.label().value().equals(root)) && (tree.children().length > 1))
/*    */     {
/*    */ 
/* 81 */       Tree underRoot = tree.treeFactory().newTreeNode(nonUnaryRoot, tree.getChildrenAsList());
/* 82 */       tree.setChildren(new Tree[1]);
/* 83 */       tree.setChild(0, underRoot);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 89 */     return tree;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\tuebadz\TueBaDZPennTreeNormalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */