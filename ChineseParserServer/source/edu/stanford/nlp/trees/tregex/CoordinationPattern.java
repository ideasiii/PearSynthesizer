/*     */ package edu.stanford.nlp.trees.tregex;
/*     */ 
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ class CoordinationPattern
/*     */   extends TregexPattern
/*     */ {
/*     */   private boolean isConj;
/*     */   private List children;
/*     */   
/*     */   public CoordinationPattern(List children, boolean isConj)
/*     */   {
/*  16 */     if (children.size() < 2) {
/*  17 */       throw new RuntimeException("Coordination node must have at least 2 children.");
/*     */     }
/*  19 */     this.children = children;
/*  20 */     this.isConj = isConj;
/*     */   }
/*     */   
/*     */   public List getChildren()
/*     */   {
/*  25 */     return this.children;
/*     */   }
/*     */   
/*     */   public String localString() {
/*  29 */     return this.isConj ? "and" : "or";
/*     */   }
/*     */   
/*     */   public String toString() {
/*  33 */     StringBuffer sb = new StringBuffer();
/*  34 */     Iterator iter; if (this.isConj) {
/*  35 */       for (iter = this.children.iterator(); iter.hasNext();) {
/*  36 */         TregexPattern node = (TregexPattern)iter.next();
/*  37 */         sb.append(node.toString());
/*     */       }
/*     */     } else {
/*  40 */       sb.append('[');
/*  41 */       for (Iterator iter = this.children.iterator(); iter.hasNext();) {
/*  42 */         TregexPattern node = (TregexPattern)iter.next();
/*  43 */         sb.append(node.toString());
/*  44 */         if (iter.hasNext()) {
/*  45 */           sb.append(" |");
/*     */         }
/*     */       }
/*  48 */       sb.append(']');
/*     */     }
/*  50 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public TregexMatcher matcher(Tree root, Tree tree, Map<Object, Tree> namesToNodes, VariableStrings variableStrings) {
/*  54 */     return new CoordinationMatcher(this, root, tree, namesToNodes, variableStrings);
/*     */   }
/*     */   
/*     */   private static class CoordinationMatcher extends TregexMatcher
/*     */   {
/*     */     private TregexMatcher[] children;
/*     */     private final CoordinationPattern myNode;
/*     */     private int currChild;
/*     */     private final boolean considerAll;
/*     */     
/*     */     public CoordinationMatcher(CoordinationPattern n, Tree root, Tree tree, Map<Object, Tree> namesToNodes, VariableStrings variableStrings)
/*     */     {
/*  66 */       super(tree, namesToNodes, variableStrings);
/*  67 */       this.myNode = n;
/*  68 */       this.children = new TregexMatcher[this.myNode.children.size()];
/*  69 */       for (int i = 0; i < this.children.length; i++) {
/*  70 */         TregexPattern node = (TregexPattern)this.myNode.children.get(i);
/*  71 */         this.children[i] = node.matcher(root, tree, namesToNodes, variableStrings);
/*     */       }
/*  73 */       this.currChild = 0;
/*  74 */       this.considerAll = (this.myNode.isConj ^ this.myNode.isNegated());
/*     */     }
/*     */     
/*     */     void resetChildIter() {
/*  78 */       this.currChild = 0;
/*  79 */       for (int i = 0; i < this.children.length; i++) {
/*  80 */         this.children[i].resetChildIter();
/*     */       }
/*     */     }
/*     */     
/*     */     void resetChildIter(Tree tree) {
/*  85 */       this.tree = tree;
/*  86 */       this.currChild = 0;
/*  87 */       for (int i = 0; i < this.children.length; i++) {
/*  88 */         this.children[i].resetChildIter(tree);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean matches()
/*     */     {
/*  94 */       if (this.considerAll)
/*     */       {
/*  99 */         for (; 
/*     */             
/*     */ 
/*  99 */             (this.currChild >= 0) && 
/* 100 */               (this.myNode.isNegated() == this.children[this.currChild].matches()); this.currChild -= 1)
/*     */         {
/*     */ 
/*     */ 
/* 103 */           this.children[this.currChild].resetChildIter();
/*     */         }
/*     */         
/* 106 */         if (this.currChild < 0) {
/* 107 */           return this.myNode.isOptional();
/*     */         }
/*     */         
/*     */ 
/* 111 */         while (this.currChild + 1 < this.children.length) {
/* 112 */           this.currChild += 1;
/* 113 */           if (this.myNode.isNegated() == this.children[this.currChild].matches()) {
/* 114 */             this.currChild = -1;
/* 115 */             return this.myNode.isOptional();
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 120 */         if (this.myNode.isNegated()) {
/* 121 */           this.currChild = -1;
/*     */         }
/* 123 */         return true;
/*     */       }
/* 126 */       for (; 
/* 126 */           this.currChild < this.children.length; this.currChild += 1) {
/* 127 */         if (this.myNode.isNegated() != this.children[this.currChild].matches())
/*     */         {
/* 129 */           if (this.myNode.isNegated()) {
/* 130 */             this.currChild = this.children.length;
/*     */           }
/* 132 */           return true;
/*     */         }
/*     */       }
/* 135 */       if (this.myNode.isNegated()) {
/* 136 */         this.currChild = this.children.length;
/*     */       }
/* 138 */       return this.myNode.isOptional();
/*     */     }
/*     */     
/*     */ 
/*     */     public Tree getMatch()
/*     */     {
/* 144 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\CoordinationPattern.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */