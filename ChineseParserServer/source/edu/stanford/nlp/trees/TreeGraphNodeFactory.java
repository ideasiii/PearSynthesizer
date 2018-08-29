/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.ling.LabelFactory;
/*    */ import edu.stanford.nlp.ling.MapLabel;
/*    */ import edu.stanford.nlp.ling.MapLabelFactory;
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
/*    */ public class TreeGraphNodeFactory
/*    */   implements TreeFactory
/*    */ {
/*    */   private MapLabelFactory mlf;
/*    */   
/*    */   public TreeGraphNodeFactory()
/*    */   {
/* 30 */     this(MapLabel.factory());
/*    */   }
/*    */   
/*    */   public TreeGraphNodeFactory(LabelFactory mlf) {
/* 34 */     this.mlf = ((MapLabelFactory)mlf);
/*    */   }
/*    */   
/*    */   public Tree newLeaf(String word)
/*    */   {
/* 39 */     return newLeaf(this.mlf.newLabel(word));
/*    */   }
/*    */   
/*    */   public Tree newLeaf(Label label)
/*    */   {
/* 44 */     return new TreeGraphNode(label);
/*    */   }
/*    */   
/*    */   public Tree newTreeNode(String parent, List children)
/*    */   {
/* 49 */     return newTreeNode(this.mlf.newLabel(parent), children);
/*    */   }
/*    */   
/*    */   public Tree newTreeNode(Label parentLabel, List children)
/*    */   {
/* 54 */     return new TreeGraphNode(parentLabel, children);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreeGraphNodeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */