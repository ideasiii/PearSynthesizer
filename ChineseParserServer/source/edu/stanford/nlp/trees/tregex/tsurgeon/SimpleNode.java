/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ class SimpleNode implements Node {
/*    */   protected Node parent;
/*    */   protected Node[] children;
/*    */   protected int id;
/*    */   protected TsurgeonParser parser;
/*    */   
/*    */   public SimpleNode(int i) {
/* 12 */     this.id = i;
/*    */   }
/*    */   
/*    */   public SimpleNode(TsurgeonParser p, int i) {
/* 16 */     this(i);
/* 17 */     this.parser = p;
/*    */   }
/*    */   
/*    */ 
/*    */   public void jjtOpen() {}
/*    */   
/*    */ 
/*    */   public void jjtClose() {}
/*    */   
/* 26 */   public void jjtSetParent(Node n) { this.parent = n; }
/* 27 */   public Node jjtGetParent() { return this.parent; }
/*    */   
/*    */   public void jjtAddChild(Node n, int i) {
/* 30 */     if (this.children == null) {
/* 31 */       this.children = new Node[i + 1];
/* 32 */     } else if (i >= this.children.length) {
/* 33 */       Node[] c = new Node[i + 1];
/* 34 */       System.arraycopy(this.children, 0, c, 0, this.children.length);
/* 35 */       this.children = c;
/*    */     }
/* 37 */     this.children[i] = n;
/*    */   }
/*    */   
/*    */   public Node jjtGetChild(int i) {
/* 41 */     return this.children[i];
/*    */   }
/*    */   
/*    */   public int jjtGetNumChildren() {
/* 45 */     return this.children == null ? 0 : this.children.length;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 54 */   public String toString() { return TsurgeonParserTreeConstants.jjtNodeName[this.id]; }
/* 55 */   public String toString(String prefix) { return prefix + toString(); }
/*    */   
/*    */ 
/*    */ 
/*    */   public void dump(String prefix)
/*    */   {
/* 61 */     System.out.println(toString(prefix));
/* 62 */     if (this.children != null) {
/* 63 */       for (int i = 0; i < this.children.length; i++) {
/* 64 */         SimpleNode n = (SimpleNode)this.children[i];
/* 65 */         if (n != null) {
/* 66 */           n.dump(prefix + " ");
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\SimpleNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */