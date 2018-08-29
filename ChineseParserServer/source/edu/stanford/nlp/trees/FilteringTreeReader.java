/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.util.Filter;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FilteringTreeReader
/*    */   implements TreeReader
/*    */ {
/*    */   private TreeReader tr;
/*    */   private Filter<Tree> f;
/*    */   
/*    */   public FilteringTreeReader(TreeReader tr, Filter<Tree> f)
/*    */   {
/* 22 */     this.tr = tr;
/* 23 */     this.f = f;
/*    */   }
/*    */   
/*    */ 
/*    */   public Tree readTree()
/*    */     throws IOException
/*    */   {
/*    */     Tree t;
/*    */     
/*    */     do
/*    */     {
/* 34 */       t = this.tr.readTree();
/* 35 */     } while ((t != null) && (!this.f.accept(t)));
/* 36 */     return t;
/*    */   }
/*    */   
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 43 */     this.tr.close();
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\FilteringTreeReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */