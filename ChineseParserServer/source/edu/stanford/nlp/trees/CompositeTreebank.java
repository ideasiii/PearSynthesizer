/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileFilter;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class CompositeTreebank extends Treebank
/*    */ {
/*    */   private Treebank t1;
/*    */   private Treebank t2;
/*    */   
/*    */   public CompositeTreebank(Treebank t1, Treebank t2)
/*    */   {
/* 15 */     this.t1 = t1;
/* 16 */     this.t2 = t2;
/*    */   }
/*    */   
/*    */   public void clear() {
/* 20 */     this.t1.clear();
/* 21 */     this.t2.clear();
/*    */   }
/*    */   
/*    */   public void loadPath(File path, FileFilter filt) {
/* 25 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public void apply(TreeVisitor tp) {
/* 29 */     for (Tree tree : this) {
/* 30 */       tp.visitTree(tree);
/*    */     }
/*    */   }
/*    */   
/*    */   public Iterator<Tree> iterator()
/*    */   {
/* 36 */     return new CompositeTreebankIterator(this.t1, this.t2);
/*    */   }
/*    */   
/*    */   private class CompositeTreebankIterator implements Iterator<Tree> {
/*    */     private Iterator<Tree> it1;
/*    */     private Iterator<Tree> it2;
/*    */     
/*    */     public CompositeTreebankIterator(Collection<Tree> c1) {
/* 44 */       this.it1 = c1.iterator();
/* 45 */       this.it2 = c2.iterator();
/*    */     }
/*    */     
/*    */     public boolean hasNext() {
/* 49 */       return (this.it1.hasNext()) || (this.it2.hasNext());
/*    */     }
/*    */     
/*    */     public Tree next() {
/* 53 */       Tree tree = this.it1.hasNext() ? (Tree)this.it1.next() : (Tree)this.it2.next();
/* 54 */       return tree;
/*    */     }
/*    */     
/*    */     public void remove() {
/* 58 */       throw new UnsupportedOperationException();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\CompositeTreebank.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */