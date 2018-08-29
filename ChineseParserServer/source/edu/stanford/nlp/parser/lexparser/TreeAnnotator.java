/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTag;
/*     */ import edu.stanford.nlp.ling.CategoryWordTagFactory;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class TreeAnnotator
/*     */   implements TreeTransformer
/*     */ {
/*     */   private TreeFactory tf;
/*     */   private TreebankLangParserParams tlpParams;
/*     */   private HeadFinder hf;
/*     */   
/*     */   public Tree transformTree(Tree t)
/*     */   {
/*  37 */     Tree copy = t.deeperCopy(this.tf);
/*  38 */     return transformTreeHelper(copy, copy);
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
/*     */ 
/*     */ 
/*     */   private Tree transformTreeHelper(Tree t, Tree root)
/*     */   {
/*  56 */     if (t == null)
/*     */     {
/*  58 */       return null;
/*     */     }
/*     */     
/*  61 */     String cat = t.label().value();
/*  62 */     if (t.isLeaf())
/*     */     {
/*  64 */       Label label = new StringLabel(cat);
/*  65 */       t.setLabel(label);
/*  66 */       return t;
/*     */     }
/*     */     
/*     */     String parentStr;
/*     */     Tree parent;
/*     */     String parentStr;
/*  72 */     if ((root == null) || (t.equals(root))) {
/*  73 */       Tree parent = null;
/*  74 */       parentStr = "";
/*     */     } else {
/*  76 */       parent = t.parent(root);
/*  77 */       parentStr = parent.label().value(); }
/*     */     String grandParentStr;
/*  79 */     String grandParentStr; if ((parent == null) || (parent.equals(root))) {
/*  80 */       grandParentStr = "";
/*     */     } else {
/*  82 */       grandParentStr = parent.parent(root).label().value();
/*     */     }
/*  84 */     String baseParentStr = this.tlpParams.treebankLanguagePack().basicCategory(parentStr);
/*  85 */     String baseGrandParentStr = this.tlpParams.treebankLanguagePack().basicCategory(grandParentStr);
/*     */     
/*     */ 
/*  88 */     if (t.isPreTerminal())
/*     */     {
/*  90 */       Tree childResult = transformTreeHelper(t.children()[0], null);
/*  91 */       String word = childResult.value();
/*     */       
/*  93 */       if (!Train.noTagSplit) {
/*  94 */         if (Train.tagPA) {
/*  95 */           String test = cat + "^" + baseParentStr;
/*  96 */           if ((!Train.tagSelectiveSplit) || (Train.splitters.contains(test))) {
/*  97 */             cat = test;
/*     */           }
/*     */         }
/* 100 */         if ((Train.markUnaryTags) && (parent.numChildren() == 1)) {
/* 101 */           cat = cat + "^U";
/*     */         }
/*     */       }
/*     */       
/* 105 */       Label label = new CategoryWordTag(cat, word, cat);
/* 106 */       t.setLabel(label);
/* 107 */       t.setChild(0, childResult);
/* 108 */       if (Train.noTagSplit) {
/* 109 */         return t;
/*     */       }
/*     */       
/* 112 */       return this.tlpParams.transformTree(t, root);
/*     */     }
/*     */     
/*     */ 
/* 116 */     Tree[] kids = t.children();
/* 117 */     for (int childNum = 0; childNum < kids.length; childNum++) {
/* 118 */       Tree child = kids[childNum];
/* 119 */       Tree childResult = transformTreeHelper(child, root);
/* 120 */       t.setChild(childNum, childResult);
/*     */     }
/*     */     
/* 123 */     Tree headChild = this.hf.determineHead(t);
/*     */     CategoryWordTag headLabel;
/* 125 */     try { headLabel = (CategoryWordTag)headChild.label();
/*     */     } catch (ClassCastException cce) {
/* 127 */       this.tlpParams.pw(System.err).println("\n\nTreeAnnotator ERROR: Ill-formed tree around empty node \"" + headChild.label() + "\" in tree\n" + t);
/* 128 */       throw cce;
/*     */     } catch (NullPointerException npe) {
/* 130 */       this.tlpParams.pw(System.err).println("TreeAnnotator: null head found for tree:\n" + t);
/* 131 */       throw npe;
/*     */     }
/*     */     
/* 134 */     String word = headLabel.word();
/* 135 */     String tag = headLabel.tag();
/*     */     
/* 137 */     String baseCat = this.tlpParams.treebankLanguagePack().basicCategory(cat);
/*     */     
/*     */ 
/*     */ 
/*     */     List<String> leftAnn;
/*     */     
/*     */ 
/*     */     List<String> rightAnn;
/*     */     
/*     */ 
/* 147 */     if ((Train.sisterAnnotate) && (!Train.smoothing) && (baseParentStr.length() > 0)) {
/* 148 */       List<String> leftSis = listBasicCategories(SisterAnnotationStats.leftSisterLabels(t, parent));
/* 149 */       List<String> rightSis = listBasicCategories(SisterAnnotationStats.rightSisterLabels(t, parent));
/*     */       
/* 151 */       leftAnn = new ArrayList();
/* 152 */       rightAnn = new ArrayList();
/*     */       
/* 154 */       for (String s : leftSis)
/*     */       {
/* 156 */         leftAnn.add(baseCat + "=l=" + this.tlpParams.treebankLanguagePack().basicCategory(s));
/*     */       }
/*     */       
/* 159 */       for (String s : rightSis)
/*     */       {
/* 161 */         rightAnn.add(baseCat + "=r=" + this.tlpParams.treebankLanguagePack().basicCategory(s));
/*     */       }
/* 163 */       for (Iterator j = rightAnn.iterator(); j.hasNext();) {}
/*     */       
/*     */ 
/* 166 */       for (String annCat : Train.sisterSplitters)
/*     */       {
/* 168 */         if ((leftAnn.contains(annCat)) || (rightAnn.contains(annCat))) {
/* 169 */           cat = cat + annCat.replaceAll(new StringBuilder().append("^").append(baseCat).toString(), "");
/* 170 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 175 */     if ((Train.PA) && (!Train.smoothing) && (baseParentStr.length() > 0)) {
/* 176 */       String cat2 = baseCat + "^" + baseParentStr;
/* 177 */       if ((!Train.selectiveSplit) || (Train.splitters.contains(cat2))) {
/* 178 */         cat = cat + "^" + baseParentStr;
/*     */       }
/*     */     }
/* 181 */     if ((Train.gPA) && (!Train.smoothing) && (grandParentStr.length() > 0)) {
/* 182 */       if (Train.selectiveSplit) {
/* 183 */         String cat2 = baseCat + "^" + baseParentStr + "~" + baseGrandParentStr;
/* 184 */         if ((cat.contains("^")) && (Train.splitters.contains(cat2))) {
/* 185 */           cat = cat + "~" + baseGrandParentStr;
/*     */         }
/*     */       } else {
/* 188 */         cat = cat + "~" + baseGrandParentStr;
/*     */       }
/*     */     }
/* 191 */     if (Train.markUnary > 0) {
/* 192 */       if ((Train.markUnary == 1) && (kids.length == 1) && (kids[0].depth() >= 2)) {
/* 193 */         cat = cat + "-U";
/* 194 */       } else if ((Train.markUnary == 2) && (parent != null) && (parent.numChildren() == 1) && (t.depth() >= 2)) {
/* 195 */         cat = cat + "-u";
/*     */       }
/*     */     }
/* 198 */     if ((Train.xOverX) && (xOverX(t, baseCat))) {
/* 199 */       cat = cat + "-X";
/*     */     }
/* 201 */     if ((Train.rightRec) && (rightRec(t, baseCat))) {
/* 202 */       cat = cat + "-R";
/*     */     }
/* 204 */     if ((Train.leftRec) && (leftRec(t, baseCat))) {
/* 205 */       cat = cat + "-L";
/*     */     }
/* 207 */     if ((Train.splitPrePreT) && (t.isPrePreTerminal())) {
/* 208 */       cat = cat + "-PPT";
/*     */     }
/* 210 */     Label label = new CategoryWordTag(cat, word, tag);
/* 211 */     t.setLabel(label);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 216 */     return this.tlpParams.transformTree(t, root);
/*     */   }
/*     */   
/*     */   private List<String> listBasicCategories(List<String> l)
/*     */   {
/* 221 */     List<String> l1 = new ArrayList();
/* 222 */     for (String str : l) {
/* 223 */       l1.add(this.tlpParams.treebankLanguagePack().basicCategory(str));
/*     */     }
/* 225 */     return l1;
/*     */   }
/*     */   
/*     */   private static boolean rightRec(Tree t, String baseCat)
/*     */   {
/* 230 */     if (!baseCat.equals("NP"))
/*     */     {
/* 232 */       return false;
/*     */     }
/* 234 */     while (!t.isLeaf()) {
/* 235 */       t = t.lastChild();
/* 236 */       String str = t.label().value();
/* 237 */       if (str.startsWith(baseCat)) {
/* 238 */         return true;
/*     */       }
/*     */     }
/* 241 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean leftRec(Tree t, String baseCat) {
/* 245 */     while (!t.isLeaf()) {
/* 246 */       t = t.firstChild();
/* 247 */       String str = t.label().value();
/* 248 */       if (str.startsWith(baseCat)) {
/* 249 */         return true;
/*     */       }
/*     */     }
/* 252 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean xOverX(Tree t, String baseCat) {
/* 256 */     for (Tree s : t.subTreeList()) {
/* 257 */       if (s != t)
/*     */       {
/*     */ 
/* 260 */         if (s.label().value().startsWith(baseCat))
/* 261 */           return true;
/*     */       }
/*     */     }
/* 264 */     return false;
/*     */   }
/*     */   
/*     */   public TreeAnnotator(HeadFinder hf, TreebankLangParserParams tlpp) {
/* 268 */     this.tlpParams = tlpp;
/* 269 */     this.hf = hf;
/* 270 */     this.tf = new LabeledScoredTreeFactory(new CategoryWordTagFactory());
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\TreeAnnotator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */