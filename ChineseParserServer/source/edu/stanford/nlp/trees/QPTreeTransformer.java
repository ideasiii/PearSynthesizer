/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QPTreeTransformer
/*     */   implements TreeTransformer
/*     */ {
/*     */   public Tree transformTree(Tree t)
/*     */   {
/*  39 */     return QPtransform(t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree QPtransform(Tree t)
/*     */   {
/*  53 */     boolean notDone = true;
/*  54 */     Tree toReturn = new LabeledScoredTreeNode();
/*  55 */     while (notDone) {
/*  56 */       Tree qp = findQP(t, t);
/*  57 */       if (qp != null) {
/*  58 */         toReturn = qp;
/*  59 */         t = qp;
/*     */       } else {
/*  61 */         notDone = false;
/*  62 */         toReturn = t;
/*     */       }
/*     */     }
/*  65 */     return toReturn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Tree findQP(Tree t, Tree root)
/*     */   {
/*  81 */     if (t.value().startsWith("QP"))
/*     */     {
/*  83 */       List<Tree> children = t.getChildrenAsList();
/*  84 */       if ((children.size() >= 3) && (((Tree)children.get(0)).isPreTerminal()))
/*     */       {
/*  86 */         String child1 = ((Tree)children.get(0)).value();
/*  87 */         String child2 = ((Tree)children.get(1)).value();
/*  88 */         String child3 = ((Tree)children.get(2)).value();
/*  89 */         if (((child3.startsWith("CD")) || (child3.startsWith("DT"))) && ((child1.startsWith("RB")) || (child1.startsWith("JJ")) || (child1.startsWith("IN"))) && ((child2.startsWith("IN")) || (child2.startsWith("JJ"))))
/*     */         {
/*     */ 
/*  92 */           Tree newQP = transformQP(t);
/*  93 */           t = newQP;
/*  94 */           return root;
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/*  99 */       for (Tree child : t.getChildrenAsList()) {
/* 100 */         Tree cur = findQP(child, root);
/* 101 */         if (cur != null) {
/* 102 */           return cur;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 107 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private Tree transformQP(Tree t)
/*     */   {
/* 113 */     List<Tree> children = t.getChildrenAsList();
/*     */     
/*     */ 
/* 116 */     Tree left = new LabeledScoredTreeNode(new StringLabel("XS"), null);
/* 117 */     for (int i = 0; i < 2; i++) {
/* 118 */       left.addChild((Tree)children.get(i));
/*     */     }
/*     */     
/*     */ 
/* 122 */     for (int i = 0; i < 2; i++) {
/* 123 */       t.removeChild(0);
/*     */     }
/*     */     
/*     */ 
/* 127 */     t.addChild(0, left);
/* 128 */     return t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 135 */     QPTreeTransformer transformer = new QPTreeTransformer();
/* 136 */     Treebank tb = new MemoryTreebank();
/* 137 */     Properties props = StringUtils.argsToProperties(args);
/* 138 */     String treeFileName = props.getProperty("treeFile");
/*     */     
/* 140 */     if (treeFileName != null) {
/*     */       try {
/* 142 */         TreeReader tr = new PennTreeReader(new BufferedReader(new InputStreamReader(new FileInputStream(treeFileName))), new LabeledScoredTreeFactory());
/*     */         Tree t;
/* 144 */         while ((t = tr.readTree()) != null) {
/* 145 */           tb.add(t);
/*     */         }
/*     */       } catch (IOException e) {
/* 148 */         throw new RuntimeException("File problem: " + e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 153 */     for (Tree t : tb) {
/* 154 */       System.out.println("Original tree");
/* 155 */       t.pennPrint();
/* 156 */       System.out.println();
/* 157 */       System.out.println("Tree transformed");
/* 158 */       Tree tree = transformer.transformTree(t);
/* 159 */       tree.pennPrint();
/* 160 */       System.out.println();
/* 161 */       System.out.println("----------------------------");
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\QPTreeTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */