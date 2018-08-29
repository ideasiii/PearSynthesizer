/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.Trees;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ExciseNode
/*    */   extends TsurgeonPattern
/*    */ {
/*    */   public ExciseNode(TsurgeonPattern top, TsurgeonPattern bottom)
/*    */   {
/* 18 */     super("excise", new TsurgeonPattern[] { top, bottom });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ExciseNode(TsurgeonPattern node)
/*    */   {
/* 26 */     super("excise", new TsurgeonPattern[] { node, node });
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher m) {
/* 30 */     Tree topNode = this.children[0].evaluate(t, m);
/* 31 */     Tree bottomNode = this.children[1].evaluate(t, m);
/* 32 */     if (Tsurgeon.verbose) {
/* 33 */       System.err.println("Excising...original tree:");
/* 34 */       t.pennPrint(System.err);
/* 35 */       System.err.println("top: " + topNode + "\nbottom:" + bottomNode);
/*    */     }
/* 37 */     if (topNode == t)
/* 38 */       return null;
/* 39 */     Tree parent = topNode.parent(t);
/* 40 */     if (Tsurgeon.verbose)
/* 41 */       System.err.println("Parent: " + parent);
/* 42 */     int i = Trees.objectEqualityIndexOf(parent, topNode);
/* 43 */     parent.removeChild(i);
/* 44 */     for (Tree child : bottomNode.children()) {
/* 45 */       parent.addChild(i, child);
/* 46 */       i++;
/*    */     }
/* 48 */     if (Tsurgeon.verbose)
/* 49 */       t.pennPrint(System.err);
/* 50 */     return t;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\ExciseNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */