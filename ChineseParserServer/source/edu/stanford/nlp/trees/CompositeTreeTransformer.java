/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompositeTreeTransformer
/*    */   implements TreeTransformer
/*    */ {
/* 15 */   private final List<TreeTransformer> transformers = new ArrayList();
/*    */   
/*    */   public CompositeTreeTransformer() {}
/*    */   
/*    */   public CompositeTreeTransformer(List<TreeTransformer> tt)
/*    */   {
/* 21 */     this.transformers.addAll(tt);
/*    */   }
/*    */   
/*    */   public void addTransformer(TreeTransformer tt) {
/* 25 */     this.transformers.add(tt);
/*    */   }
/*    */   
/*    */   public Tree transformTree(Tree t) {
/* 29 */     for (TreeTransformer tt : this.transformers) {
/* 30 */       t = tt.transformTree(t);
/*    */     }
/* 32 */     return t;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\CompositeTreeTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */