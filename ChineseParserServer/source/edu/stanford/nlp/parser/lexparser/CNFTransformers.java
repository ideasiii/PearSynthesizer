/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class CNFTransformers
/*     */ {
/*     */   static class ToCNFTransformer implements edu.stanford.nlp.trees.TreeTransformer
/*     */   {
/*     */     public Tree transformTree(Tree t)
/*     */     {
/*  16 */       if (t.isLeaf()) {
/*  17 */         return t.treeFactory().newLeaf(t.label());
/*     */       }
/*  19 */       Tree[] children = t.children();
/*  20 */       if ((children.length > 1) || (t.isPreTerminal()) || (t.label().value().startsWith("ROOT"))) {
/*  21 */         Label label = t.label();
/*  22 */         Tree[] transformedChildren = new Tree[children.length];
/*  23 */         for (int childIndex = 0; childIndex < children.length; childIndex++) {
/*  24 */           Tree child = children[childIndex];
/*  25 */           transformedChildren[childIndex] = transformTree(child);
/*     */         }
/*  27 */         return t.treeFactory().newTreeNode(label, java.util.Arrays.asList(transformedChildren));
/*     */       }
/*  29 */       Tree tree = t;
/*  30 */       List<String> conjoinedList = new java.util.ArrayList();
/*  31 */       while ((tree.children().length == 1) && (!tree.isPrePreTerminal())) {
/*  32 */         String nodeString = tree.label().value();
/*  33 */         if (!nodeString.startsWith("@")) {
/*  34 */           conjoinedList.add(nodeString);
/*     */         }
/*  36 */         tree = tree.children()[0];
/*     */       }
/*  38 */       String nodeString = tree.label().value();
/*  39 */       if (!nodeString.startsWith("@")) {
/*  40 */         conjoinedList.add(nodeString);
/*     */       }
/*     */       String conjoinedLabels;
/*  43 */       if (conjoinedList.size() > 1) {
/*  44 */         StringBuilder conjoinedLabelsBuilder = new StringBuilder();
/*  45 */         for (String s : conjoinedList) {
/*  46 */           conjoinedLabelsBuilder.append("&");
/*  47 */           conjoinedLabelsBuilder.append(s);
/*     */         }
/*  49 */         conjoinedLabels = conjoinedLabelsBuilder.toString(); } else { String conjoinedLabels;
/*  50 */         if (conjoinedList.size() == 1) {
/*  51 */           conjoinedLabels = (String)conjoinedList.iterator().next();
/*     */         } else
/*  53 */           return transformTree(t.children()[0]); }
/*     */       String conjoinedLabels;
/*  55 */       children = tree.children();
/*  56 */       Label label = t.label().labelFactory().newLabel(conjoinedLabels);
/*  57 */       Tree[] transformedChildren = new Tree[children.length];
/*  58 */       for (int childIndex = 0; childIndex < children.length; childIndex++) {
/*  59 */         Tree child = children[childIndex];
/*  60 */         transformedChildren[childIndex] = transformTree(child);
/*     */       }
/*  62 */       return t.treeFactory().newTreeNode(label, java.util.Arrays.asList(transformedChildren));
/*     */     }
/*     */   }
/*     */   
/*     */   static class FromCNFTransformer implements edu.stanford.nlp.trees.TreeTransformer {
/*     */     public Tree transformTree(Tree t) {
/*  68 */       if (t.isLeaf()) {
/*  69 */         return t.treeFactory().newLeaf(t.label());
/*     */       }
/*  71 */       Tree[] children = t.children();
/*  72 */       Tree[] transformedChildren = new Tree[children.length];
/*  73 */       for (int childIndex = 0; childIndex < children.length; childIndex++) {
/*  74 */         Tree child = children[childIndex];
/*  75 */         transformedChildren[childIndex] = transformTree(child);
/*     */       }
/*  77 */       Label label = t.label();
/*  78 */       if (!label.value().startsWith("&")) {
/*  79 */         return t.treeFactory().newTreeNode(label, java.util.Arrays.asList(transformedChildren));
/*     */       }
/*  81 */       String[] nodeStrings = label.value().split("&");
/*  82 */       int i = nodeStrings.length - 1;
/*  83 */       label = t.label().labelFactory().newLabel(nodeStrings[i]);
/*  84 */       Tree result = t.treeFactory().newTreeNode(label, java.util.Arrays.asList(transformedChildren));
/*  85 */       while (i > 1) {
/*  86 */         i--;
/*  87 */         label = t.label().labelFactory().newLabel(nodeStrings[i]);
/*  88 */         result = t.treeFactory().newTreeNode(label, java.util.Collections.singletonList(result));
/*     */       }
/*  90 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  95 */     edu.stanford.nlp.ling.CategoryWordTag.printWordTag = false;
/*  96 */     String path = args[0];
/*  97 */     List trees = TreebankAnnotator.getTrees(path, 200, 219, 0, 10);
/*  98 */     List annotatedTrees = new TreebankAnnotator(new Options(), path).annotateTrees(trees);
/*  99 */     for (Iterator annotatedTreesI = annotatedTrees.iterator(); annotatedTreesI.hasNext();) {
/* 100 */       Tree tree = (Tree)annotatedTreesI.next();
/* 101 */       System.out.println("ORIGINAL:\n");
/* 102 */       tree.pennPrint();
/* 103 */       System.out.println("CNFed:\n");
/* 104 */       Tree cnfTree = new ToCNFTransformer().transformTree(tree);
/* 105 */       cnfTree.pennPrint();
/* 106 */       System.out.println("UnCNFed:\n");
/* 107 */       Tree unCNFTree = new FromCNFTransformer().transformTree(cnfTree);
/* 108 */       unCNFTree.pennPrint();
/* 109 */       System.out.println("\n\n");
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\CNFTransformers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */