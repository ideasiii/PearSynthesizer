/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ 
/*    */ public class WordCatConstituent
/*    */   extends LabeledConstituent
/*    */ {
/*    */   public String type;
/*    */   
/*    */   public static final String wordType = "word";
/*    */   
/*    */   public static final String tagType = "tag";
/*    */   
/*    */   public static final String catType = "cat";
/*    */   
/*    */   public static final String goodWordTagType = "goodWordTag";
/*    */   
/*    */   public WordCatConstituent(Tree subTree, Tree root, String type)
/*    */   {
/* 19 */     setStart(Trees.leftEdge(subTree, root));
/* 20 */     setEnd(Trees.rightEdge(subTree, root));
/* 21 */     setFromString(subTree.value());
/* 22 */     this.type = type;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\WordCatConstituent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */