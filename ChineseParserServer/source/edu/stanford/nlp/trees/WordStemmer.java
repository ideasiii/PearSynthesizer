/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.CategoryWordTagFactory;
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.ling.WordTag;
/*    */ import edu.stanford.nlp.process.Morphology;
/*    */ import java.io.PrintStream;
/*    */ import java.io.Reader;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WordStemmer
/*    */   implements TreeVisitor, TreeReaderFactory
/*    */ {
/*    */   private Morphology morpha;
/*    */   
/*    */   public WordStemmer()
/*    */   {
/* 19 */     this.morpha = new Morphology();
/*    */   }
/*    */   
/*    */   public void visitTree(Tree t) {
/* 23 */     processTree(t, null);
/*    */   }
/*    */   
/*    */   private void processTree(Tree t, String tag) {
/* 27 */     if (t.isPreTerminal()) {
/* 28 */       tag = t.label().value();
/*    */     }
/* 30 */     if (t.isLeaf()) {
/* 31 */       WordTag wt = this.morpha.stem(t.label().value(), tag);
/* 32 */       t.label().setValue(wt.word());
/*    */     } else {
/* 34 */       for (Tree kid : t.children()) {
/* 35 */         processTree(kid, tag);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public TreeReader newTreeReader(Reader in) {
/* 41 */     return new PennTreeReader(in, new LabeledScoredTreeFactory(new CategoryWordTagFactory()), new BobChrisTreeNormalizer());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 49 */     WordStemmer ls = new WordStemmer();
/* 50 */     Treebank treebank = new DiskTreebank(ls);
/* 51 */     treebank.loadPath(args[0]);
/* 52 */     for (Tree tree : treebank) {
/* 53 */       ls.visitTree(tree);
/* 54 */       System.out.println(tree);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\WordStemmer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */