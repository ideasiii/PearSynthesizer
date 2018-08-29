/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.process.Function;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractTreeExtractor
/*    */   implements Extractor
/*    */ {
/*    */   protected void tallyLeaf(Tree lt) {}
/*    */   
/*    */   protected void tallyPreTerminal(Tree lt) {}
/*    */   
/*    */   protected void tallyInternalNode(Tree lt) {}
/*    */   
/*    */   protected void tallyRoot(Tree lt) {}
/*    */   
/*    */   public Object formResult()
/*    */   {
/* 24 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   protected void tallyLocalTree(Tree lt)
/*    */   {
/* 30 */     if (lt.isLeaf())
/*    */     {
/* 32 */       tallyLeaf(lt);
/* 33 */     } else if (lt.isPreTerminal())
/*    */     {
/* 35 */       tallyPreTerminal(lt);
/*    */     }
/*    */     else {
/* 38 */       tallyInternalNode(lt);
/*    */     }
/*    */   }
/*    */   
/*    */   public void tallyTree(Tree t) {
/* 43 */     tallyRoot(t);
/* 44 */     for (Tree localTree : t.subTreeList()) {
/* 45 */       tallyLocalTree(localTree);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void tallyTrees(Collection<Tree> trees) {
/* 50 */     for (Tree tree : trees) {
/* 51 */       tallyTree(tree);
/*    */     }
/*    */   }
/*    */   
/*    */   protected void tallyTreeIterator(Iterator<Tree> treeIterator, Function<Tree, Tree> f) {
/* 56 */     while (treeIterator.hasNext()) {
/* 57 */       Tree tree = (Tree)treeIterator.next();
/*    */       try {
/* 59 */         tree = (Tree)f.apply(tree);
/*    */       } catch (Exception e) {
/* 61 */         if (Test.verbose)
/* 62 */           e.printStackTrace();
/*    */       }
/* 64 */       continue;
/*    */       
/* 66 */       tallyTree(tree);
/*    */     }
/*    */   }
/*    */   
/*    */   public Object extract() {
/* 71 */     return formResult();
/*    */   }
/*    */   
/*    */   public Object extract(Collection<Tree> treeList) {
/* 75 */     tallyTrees(treeList);
/* 76 */     return formResult();
/*    */   }
/*    */   
/* 79 */   protected double weight = 1.0D;
/*    */   
/*    */   public Object extract(Collection<Tree> trees1, Collection<Tree> trees2, double weight) {
/* 82 */     tallyTrees(trees1);
/* 83 */     this.weight = weight;
/* 84 */     tallyTrees(trees2);
/* 85 */     return formResult();
/*    */   }
/*    */   
/*    */   public Object extract(Iterator<Tree> treeIterator, Function<Tree, Tree> f) {
/* 89 */     tallyTreeIterator(treeIterator, f);
/* 90 */     return formResult();
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\AbstractTreeExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */