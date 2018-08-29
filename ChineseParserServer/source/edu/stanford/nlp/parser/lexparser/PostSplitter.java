/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTag;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PostSplitter
/*     */   implements TreeTransformer
/*     */ {
/*  25 */   private Counter<String> nonTerms = new Counter();
/*     */   private TreebankLangParserParams tlpParams;
/*     */   private TreeFactory tf;
/*     */   private HeadFinder hf;
/*     */   
/*     */   public Tree transformTree(Tree t) {
/*  31 */     this.tf = t.treeFactory();
/*  32 */     return transformTreeHelper(t, t);
/*     */   }
/*     */   
/*     */ 
/*     */   public Tree transformTreeHelper(Tree t, Tree root)
/*     */   {
/*     */     String parentStr;
/*     */     Tree parent;
/*     */     String parentStr;
/*  41 */     if ((root == null) || (t.equals(root))) {
/*  42 */       Tree parent = null;
/*  43 */       parentStr = "";
/*     */     } else {
/*  45 */       parent = t.parent(root);
/*  46 */       parentStr = parent.label().value(); }
/*     */     String grandParentStr;
/*  48 */     String grandParentStr; if ((parent == null) || (parent.equals(root))) {
/*  49 */       Tree grandParent = null;
/*  50 */       grandParentStr = "";
/*     */     } else {
/*  52 */       Tree grandParent = parent.parent(root);
/*  53 */       grandParentStr = grandParent.label().value();
/*     */     }
/*  55 */     String cat = t.label().value();
/*  56 */     String baseParentStr = this.tlpParams.treebankLanguagePack().basicCategory(parentStr);
/*  57 */     String baseGrandParentStr = this.tlpParams.treebankLanguagePack().basicCategory(grandParentStr);
/*  58 */     if (t.isLeaf()) {
/*  59 */       return this.tf.newLeaf(new StringLabel(t.label().value()));
/*     */     }
/*  61 */     String word = t.headTerminal(this.hf).value();
/*  62 */     if (t.isPreTerminal()) {
/*  63 */       this.nonTerms.incrementCount(t.label().value());
/*     */     } else {
/*  65 */       this.nonTerms.incrementCount(t.label().value());
/*  66 */       if ((Train.postPA) && (!Train.smoothing) && (baseParentStr.length() > 0)) { String cat2;
/*     */         String cat2;
/*  68 */         if (Train.postSplitWithBaseCategory) {
/*  69 */           cat2 = cat + "^" + baseParentStr;
/*     */         } else {
/*  71 */           cat2 = cat + "^" + parentStr;
/*     */         }
/*  73 */         if ((!Train.selectivePostSplit) || (Train.postSplitters.contains(cat2))) {
/*  74 */           cat = cat2;
/*     */         }
/*     */       }
/*  77 */       if ((Train.postGPA) && (!Train.smoothing) && (grandParentStr.length() > 0)) { String cat2;
/*     */         String cat2;
/*  79 */         if (Train.postSplitWithBaseCategory) {
/*  80 */           cat2 = cat + "~" + baseGrandParentStr;
/*     */         } else {
/*  82 */           cat2 = cat + "~" + grandParentStr;
/*     */         }
/*  84 */         if (Train.selectivePostSplit) {
/*  85 */           if ((cat.indexOf("^") >= 0) && (Train.postSplitters.contains(cat2))) {
/*  86 */             cat = cat2;
/*     */           }
/*     */         } else {
/*  89 */           cat = cat2;
/*     */         }
/*     */       }
/*     */     }
/*  93 */     Tree result = this.tf.newTreeNode(new CategoryWordTag(cat, word, cat), Collections.EMPTY_LIST);
/*  94 */     ArrayList<Tree> newKids = new ArrayList();
/*  95 */     Tree[] kids = t.children();
/*  96 */     for (int i = 0; i < kids.length; i++) {
/*  97 */       newKids.add(transformTreeHelper(kids[i], root));
/*     */     }
/*  99 */     result.setChildren(newKids);
/* 100 */     return result;
/*     */   }
/*     */   
/*     */   public void dumpStats() {
/* 104 */     System.out.println("%% Counts of nonterminals:");
/* 105 */     List<String> biggestCounts = new ArrayList(this.nonTerms.keySet());
/* 106 */     Collections.sort(biggestCounts, this.nonTerms.comparator(false));
/* 107 */     for (String str : biggestCounts) {
/* 108 */       System.out.println(str + ": " + this.nonTerms.getCount(str));
/*     */     }
/*     */   }
/*     */   
/*     */   public PostSplitter(TreebankLangParserParams tlpParams) {
/* 113 */     this.tlpParams = tlpParams;
/* 114 */     this.hf = tlpParams.headFinder();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\PostSplitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */