/*     */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ class AuxiliaryTree
/*     */ {
/*     */   private final String originalTreeString;
/*     */   final Tree tree;
/*     */   final Tree foot;
/*     */   private final IdentityHashMap<Tree, String> nodesToNames;
/*     */   private final Map<String, Tree> namesToNodes;
/*     */   private static final String footNodeCharacter = "@";
/*     */   
/*     */   public AuxiliaryTree(Tree tree, boolean mustHaveFoot)
/*     */   {
/*  23 */     this.originalTreeString = tree.toString();
/*  24 */     this.tree = tree;
/*  25 */     this.foot = findFootNode(tree);
/*  26 */     if ((this.foot == null) && (mustHaveFoot)) {
/*  27 */       throw new RuntimeException("Error -- no foot node found for " + this.originalTreeString);
/*     */     }
/*  29 */     this.namesToNodes = new java.util.HashMap();
/*  30 */     this.nodesToNames = new IdentityHashMap();
/*  31 */     initializeNamesNodesMaps(tree);
/*     */   }
/*     */   
/*     */   private AuxiliaryTree(Tree tree, Tree foot, Map<String, Tree> namesToNodes, String originalTreeString) {
/*  35 */     this.originalTreeString = originalTreeString;
/*  36 */     this.tree = tree;
/*  37 */     this.foot = foot;
/*  38 */     this.namesToNodes = namesToNodes;
/*  39 */     this.nodesToNames = null;
/*     */   }
/*     */   
/*     */   public Map<String, Tree> namesToNodes() {
/*  43 */     return this.namesToNodes;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  47 */     return this.originalTreeString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AuxiliaryTree copy(TsurgeonPattern p)
/*     */   {
/*  56 */     Map<String, Tree> newNamesToNodes = new java.util.HashMap();
/*  57 */     Pair<Tree, Tree> result = copyHelper(this.tree, newNamesToNodes);
/*     */     
/*     */ 
/*  60 */     p.root.newNodeNames.putAll(newNamesToNodes);
/*  61 */     return new AuxiliaryTree((Tree)result.first(), (Tree)result.second(), newNamesToNodes, this.originalTreeString);
/*     */   }
/*     */   
/*     */ 
/*     */   private Pair<Tree, Tree> copyHelper(Tree node, Map<String, Tree> newNamesToNodes)
/*     */   {
/*  67 */     Tree newFoot = null;
/*  68 */     Tree clone; Tree clone; if (node.isLeaf()) {
/*  69 */       if (node == this.foot) {
/*  70 */         Tree clone = node.treeFactory().newTreeNode(node.label(), new java.util.ArrayList(0));
/*  71 */         newFoot = clone;
/*     */       } else {
/*  73 */         clone = node.treeFactory().newLeaf(node.label().labelFactory().newLabel(node.label()));
/*     */       }
/*     */     } else {
/*  76 */       java.util.List<Tree> newChildren = new java.util.ArrayList(node.children().length);
/*  77 */       for (Tree child : node.children()) {
/*  78 */         Pair<Tree, Tree> newChild = copyHelper(child, newNamesToNodes);
/*  79 */         newChildren.add(newChild.first());
/*  80 */         if (newChild.second() != null) {
/*  81 */           if (newFoot != null) {
/*  82 */             System.err.println("Error -- two feet found when copying auxiliary tree " + this.tree.toString() + "; using last foot found.");
/*     */           }
/*  84 */           newFoot = (Tree)newChild.second();
/*     */         }
/*     */       }
/*  87 */       clone = node.treeFactory().newTreeNode(node.label().labelFactory().newLabel(node.label()), newChildren);
/*  88 */       if (this.nodesToNames.containsKey(node)) {
/*  89 */         newNamesToNodes.put(this.nodesToNames.get(node), clone);
/*     */       }
/*     */     }
/*  92 */     return new Pair(clone, newFoot);
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
/* 103 */   private static final Pattern footNodeLabelPattern = Pattern.compile("^(.*)@$");
/* 104 */   private static final Pattern escapedFootNodeCharacter = Pattern.compile("\\@");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Tree findFootNode(Tree t)
/*     */   {
/* 114 */     Tree footNode = findFootNodeHelper(t);
/* 115 */     Tree result = footNode;
/* 116 */     if (footNode != null) {
/* 117 */       Tree parent = footNode.parent(t);
/* 118 */       int i = parent.indexOf(footNode);
/* 119 */       Tree newFootNode = footNode.treeFactory().newTreeNode(footNode.label(), java.util.Collections.emptyList());
/* 120 */       parent.setChild(i, newFootNode);
/* 121 */       result = newFootNode;
/*     */     }
/* 123 */     return result;
/*     */   }
/*     */   
/*     */   private Tree findFootNodeHelper(Tree t) {
/* 127 */     Tree foundDtr = null;
/* 128 */     if (t.isLeaf()) {
/* 129 */       Matcher m = footNodeLabelPattern.matcher(t.label().value());
/* 130 */       if (m.matches()) {
/* 131 */         t.label().setValue(m.group(1));
/* 132 */         return t;
/*     */       }
/* 134 */       return null;
/*     */     }
/*     */     
/* 137 */     for (Tree child : t.children()) {
/* 138 */       Tree thisFoundDtr = findFootNodeHelper(child);
/* 139 */       if (thisFoundDtr != null) {
/* 140 */         if (foundDtr != null) {
/* 141 */           throw new RuntimeException("Error -- two foot nodes in subtree" + t.toString());
/*     */         }
/* 143 */         foundDtr = thisFoundDtr;
/*     */       }
/*     */     }
/*     */     
/* 147 */     Matcher m = escapedFootNodeCharacter.matcher(t.label().value());
/* 148 */     t.label().setValue(m.replaceAll("@"));
/* 149 */     return foundDtr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */   private static String nameIntroducingChar = "=";
/* 158 */   static Pattern namePattern = Pattern.compile(nameIntroducingChar + "(.+)$");
/* 159 */   static Pattern escapedNameChar = Pattern.compile("\\" + nameIntroducingChar);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final void initializeNamesNodesMaps(Tree t)
/*     */   {
/* 166 */     for (Tree node : t.subTreeList()) {
/* 167 */       Matcher m = namePattern.matcher(node.label().value());
/* 168 */       if (m.find()) {
/* 169 */         this.namesToNodes.put(m.group(1), node);
/* 170 */         this.nodesToNames.put(node, m.group(1));
/* 171 */         node.label().setValue(m.replaceFirst(""));
/*     */       }
/* 173 */       Matcher m1 = escapedNameChar.matcher(node.label().value());
/* 174 */       node.label().setValue(m1.replaceAll(nameIntroducingChar));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\AuxiliaryTree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */