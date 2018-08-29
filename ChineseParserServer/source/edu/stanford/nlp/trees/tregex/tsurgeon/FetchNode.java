/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Map;
/*    */ 
/*    */ class FetchNode extends TsurgeonPattern
/*    */ {
/*    */   public FetchNode(String nodeName)
/*    */   {
/* 12 */     super(nodeName, new TsurgeonPattern[0]);
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 16 */     Tree result = (Tree)this.root.newNodeNames.get(this.label);
/* 17 */     if (result == null) {
/* 18 */       result = m.getNode(this.label);
/*    */     }
/* 20 */     if (result == null) {
/* 21 */       System.err.println("Warning -- null node fetched by tsurgeon operation for node: " + this);
/*    */     }
/* 23 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\FetchNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */