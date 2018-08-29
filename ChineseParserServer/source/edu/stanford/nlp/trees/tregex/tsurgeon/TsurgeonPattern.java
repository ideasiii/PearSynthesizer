/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class TsurgeonPattern
/*    */ {
/* 15 */   protected static final TsurgeonPattern[] EMPTY_TSURGEON_ARRAY = new TsurgeonPattern[0];
/*    */   TsurgeonPatternRoot root;
/*    */   String label;
/*    */   TsurgeonPattern[] children;
/*    */   
/*    */   TsurgeonPattern(String label, TsurgeonPattern[] children)
/*    */   {
/* 22 */     this.label = label;
/* 23 */     this.children = children;
/*    */   }
/*    */   
/*    */   protected void setRoot(TsurgeonPatternRoot root) {
/* 27 */     this.root = root;
/* 28 */     for (TsurgeonPattern child : this.children) {
/* 29 */       child.setRoot(root);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString() {
/* 34 */     StringBuilder resultSB = new StringBuilder();
/* 35 */     resultSB.append(this.label);
/* 36 */     if (this.children.length > 0) {
/* 37 */       resultSB.append("(");
/* 38 */       for (int i = 0; i < this.children.length; i++) {
/* 39 */         resultSB.append(this.children[i]);
/* 40 */         if (i < this.children.length - 1) {
/* 41 */           resultSB.append(", ");
/*    */         }
/*    */       }
/* 44 */       resultSB.append(")");
/*    */     }
/* 46 */     return resultSB.toString();
/*    */   }
/*    */   
/*    */   public abstract Tree evaluate(Tree paramTree, TregexMatcher paramTregexMatcher);
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\TsurgeonPattern.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */