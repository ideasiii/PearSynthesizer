/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.LabelFactory;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import edu.stanford.nlp.ling.StringLabelFactory;
/*     */ import edu.stanford.nlp.ling.TaggedWord;
/*     */ import edu.stanford.nlp.process.Function;
/*     */ import edu.stanford.nlp.util.MutableInteger;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class Trees
/*     */ {
/*     */   public static int leftEdge(Tree t, Tree root)
/*     */   {
/*  26 */     MutableInteger i = new MutableInteger(0);
/*  27 */     if (leftEdge(t, root, i)) {
/*  28 */       return i.intValue();
/*     */     }
/*  30 */     return -1;
/*     */   }
/*     */   
/*     */   static boolean leftEdge(Tree t, Tree t1, MutableInteger i)
/*     */   {
/*  35 */     if (t == t1)
/*  36 */       return true;
/*  37 */     if (t1.isLeaf()) {
/*  38 */       int j = t1.yield().size();
/*  39 */       i.set(i.intValue() + j);
/*  40 */       return false;
/*     */     }
/*  42 */     Tree[] kids = t1.children();
/*  43 */     int j = 0; for (int n = kids.length; j < n; j++) {
/*  44 */       if (leftEdge(t, kids[j], i)) {
/*  45 */         return true;
/*     */       }
/*     */     }
/*  48 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int rightEdge(Tree t, Tree root)
/*     */   {
/*  59 */     MutableInteger i = new MutableInteger(root.yield().size());
/*  60 */     if (rightEdge(t, root, i)) {
/*  61 */       return i.intValue();
/*     */     }
/*  63 */     return root.yield().size() + 1;
/*     */   }
/*     */   
/*     */   static boolean rightEdge(Tree t, Tree t1, MutableInteger i)
/*     */   {
/*  68 */     if (t == t1)
/*  69 */       return true;
/*  70 */     if (t1.isLeaf()) {
/*  71 */       int j = t1.yield().size();
/*  72 */       i.set(i.intValue() - j);
/*  73 */       return false;
/*     */     }
/*  75 */     Tree[] kids = t1.children();
/*  76 */     for (int j = kids.length - 1; j >= 0; j--) {
/*  77 */       if (rightEdge(t, kids[j], i)) {
/*  78 */         return true;
/*     */       }
/*     */     }
/*  81 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Tree lexicalize(Tree t, HeadFinder hf)
/*     */   {
/*  91 */     Function<Tree, Tree> a = TreeFunctions.getLabeledTreeToCategoryWordTagTreeFunction();
/*  92 */     Tree t1 = (Tree)a.apply(t);
/*  93 */     t1.percolateHeads(hf);
/*  94 */     return t1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static List<Tree> leaves(Tree t)
/*     */   {
/* 101 */     List<Tree> l = new ArrayList();
/* 102 */     leaves(t, l);
/* 103 */     return l;
/*     */   }
/*     */   
/*     */   private static void leaves(Tree t, List<Tree> l) {
/* 107 */     if (t.isLeaf()) {
/* 108 */       l.add(t);
/*     */     } else {
/* 110 */       Tree[] kids = t.children();
/* 111 */       int j = 0; for (int n = kids.length; j < n; j++) {
/* 112 */         leaves(kids[j], l);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean heads(Tree head, Tree node, HeadFinder hf)
/*     */   {
/* 121 */     if (node.isLeaf()) {
/* 122 */       return false;
/*     */     }
/* 124 */     return heads(head, hf.determineHead(node), hf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Tree maximalProjection(Tree head, Tree root, HeadFinder hf)
/*     */   {
/* 134 */     Tree projection = head;
/* 135 */     if (projection == root) {
/* 136 */       return root;
/*     */     }
/* 138 */     Tree parent = projection.parent(root);
/* 139 */     while (hf.determineHead(parent) == projection) {
/* 140 */       projection = parent;
/* 141 */       if (projection == root) {
/* 142 */         return root;
/*     */       }
/* 144 */       parent = projection.parent(root);
/*     */     }
/* 146 */     return projection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Tree applyToProjections(TreeVisitor v, Tree head, Tree root, HeadFinder hf)
/*     */   {
/* 154 */     Tree projection = head;
/* 155 */     Tree parent = projection.parent(root);
/* 156 */     if ((parent == null) && (projection != root)) {
/* 157 */       return null;
/*     */     }
/* 159 */     v.visitTree(projection);
/* 160 */     if (projection == root) {
/* 161 */       return root;
/*     */     }
/* 163 */     while (hf.determineHead(parent) == projection) {
/* 164 */       projection = parent;
/* 165 */       v.visitTree(projection);
/* 166 */       if (projection == root) {
/* 167 */         return root;
/*     */       }
/* 169 */       parent = projection.parent(root);
/*     */     }
/* 171 */     return projection;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Tree getTerminal(Tree tree, int n)
/*     */   {
/* 178 */     return getTerminal(tree, new MutableInteger(0), n);
/*     */   }
/*     */   
/*     */   static Tree getTerminal(Tree tree, MutableInteger i, int n) {
/* 182 */     if (i.intValue() == n) {
/* 183 */       if (tree.isLeaf()) {
/* 184 */         return tree;
/*     */       }
/* 186 */       return getTerminal(tree.children()[0], i, n);
/*     */     }
/*     */     
/* 189 */     if (tree.isLeaf()) {
/* 190 */       i.set(i.intValue() + tree.yield().size());
/* 191 */       return null;
/*     */     }
/* 193 */     Tree[] kids = tree.children();
/* 194 */     for (int j = 0; j < kids.length; j++) {
/* 195 */       Tree result = getTerminal(kids[j], i, n);
/* 196 */       if (result != null) {
/* 197 */         return result;
/*     */       }
/*     */     }
/* 200 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Tree getPreTerminal(Tree tree, int n)
/*     */   {
/* 209 */     return getPreTerminal(tree, new MutableInteger(0), n);
/*     */   }
/*     */   
/*     */   static Tree getPreTerminal(Tree tree, MutableInteger i, int n) {
/* 213 */     if (i.intValue() == n) {
/* 214 */       if (tree.isPreTerminal()) {
/* 215 */         return tree;
/*     */       }
/* 217 */       return getPreTerminal(tree.children()[0], i, n);
/*     */     }
/*     */     
/* 220 */     if (tree.isPreTerminal()) {
/* 221 */       i.set(i.intValue() + tree.yield().size());
/* 222 */       return null;
/*     */     }
/* 224 */     Tree[] kids = tree.children();
/* 225 */     for (int j = 0; j < kids.length; j++) {
/* 226 */       Tree result = getPreTerminal(kids[j], i, n);
/* 227 */       if (result != null) {
/* 228 */         return result;
/*     */       }
/*     */     }
/* 231 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<String> localTreeAsCatList(Tree t)
/*     */   {
/* 240 */     List<String> l = new ArrayList(t.children().length + 1);
/* 241 */     l.add(t.label().value());
/* 242 */     for (int i = 0; i < t.children().length; i++) {
/* 243 */       l.add(t.children()[i].label().value());
/*     */     }
/* 245 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int objectEqualityIndexOf(Tree parent, Tree daughter)
/*     */   {
/* 253 */     for (int i = 0; i < parent.children().length; i++) {
/* 254 */       if (daughter == parent.children()[i]) {
/* 255 */         return i;
/*     */       }
/*     */     }
/* 258 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toDebugStructureString(Tree t)
/*     */   {
/* 266 */     StringBuilder sb = new StringBuilder();
/* 267 */     String tCl = StringUtils.getShortClassName(t);
/* 268 */     String tfCl = StringUtils.getShortClassName(t.treeFactory());
/* 269 */     String lCl = StringUtils.getShortClassName(t.label());
/* 270 */     String lfCl = StringUtils.getShortClassName(t.label().labelFactory());
/* 271 */     Set<String> otherClasses = new java.util.HashSet();
/* 272 */     for (Tree st : t) {
/* 273 */       String stCl = StringUtils.getShortClassName(st);
/* 274 */       String stfCl = StringUtils.getShortClassName(st.treeFactory());
/* 275 */       String slCl = StringUtils.getShortClassName(st.label());
/* 276 */       String slfCl = StringUtils.getShortClassName(st.label().labelFactory());
/*     */       
/* 278 */       if (!tCl.equals(stCl)) {
/* 279 */         otherClasses.add(stCl);
/*     */       }
/* 281 */       if (!tfCl.equals(stfCl)) {
/* 282 */         otherClasses.add(stfCl);
/*     */       }
/* 284 */       if (!lCl.equals(slCl)) {
/* 285 */         otherClasses.add(slCl);
/*     */       }
/* 287 */       if (!lfCl.equals(slfCl)) {
/* 288 */         otherClasses.add(slfCl);
/*     */       }
/*     */     }
/* 291 */     sb.append("Tree with root of class ").append(tCl).append(" and factory ").append(tfCl);
/* 292 */     sb.append(" with label class ").append(lCl).append(" and factory ").append(lfCl);
/* 293 */     if (!otherClasses.isEmpty()) {
/* 294 */       sb.append(" with the following classes also found within the tree: ").append(otherClasses);
/*     */     }
/* 296 */     return sb.toString();
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
/*     */   public static Tree toFlatTree(Sentence<?> s)
/*     */   {
/* 309 */     return toFlatTree(s, new StringLabelFactory());
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
/*     */   public static Tree toFlatTree(Sentence<?> s, LabelFactory lf)
/*     */   {
/* 322 */     List<Tree> daughters = new ArrayList(s.length());
/* 323 */     for (HasWord word : s) {
/* 324 */       Tree wordNode = new LabeledScoredTreeLeaf(lf.newLabel(word.word()));
/* 325 */       if ((word instanceof TaggedWord)) {
/* 326 */         TaggedWord taggedWord = (TaggedWord)word;
/* 327 */         wordNode = new LabeledScoredTreeNode(new StringLabel(taggedWord.tag()), Collections.singletonList(wordNode));
/*     */       } else {
/* 329 */         wordNode = new LabeledScoredTreeNode(lf.newLabel("WD"), Collections.singletonList(wordNode));
/*     */       }
/* 331 */       daughters.add(wordNode);
/*     */     }
/* 333 */     return new LabeledScoredTreeNode(new StringLabel("S"), daughters);
/*     */   }
/*     */   
/*     */   public static String treeToLatex(Tree t)
/*     */   {
/* 338 */     StringBuilder connections = new StringBuilder();
/* 339 */     StringBuilder hierarchy = new StringBuilder();
/* 340 */     treeToLatexHelper(t, connections, hierarchy, 0, 1, 0);
/* 341 */     return "\\tree" + hierarchy + "\n" + connections + "\n";
/*     */   }
/*     */   
/*     */ 
/*     */   private static int treeToLatexHelper(Tree t, StringBuilder c, StringBuilder h, int n, int nextN, int indent)
/*     */   {
/* 347 */     StringBuilder sb = new StringBuilder();
/* 348 */     for (int i = 0; i < indent; i++)
/* 349 */       sb.append("  ");
/* 350 */     h.append("\n" + sb);
/* 351 */     h.append("{\\" + (t.isLeaf() ? "" : "n") + "tnode{z" + n + "}{" + t.label() + "}");
/* 352 */     if (!t.isLeaf()) {
/* 353 */       for (int k = 0; k < t.children().length; k++) {
/* 354 */         h.append(", ");
/* 355 */         c.append("\\nodeconnect{z" + n + "}{z" + nextN + "}\n");
/* 356 */         nextN = treeToLatexHelper(t.children()[k], c, h, nextN, nextN + 1, indent + 1);
/*     */       }
/*     */     }
/* 359 */     h.append("}");
/* 360 */     return nextN;
/*     */   }
/*     */   
/*     */   public static String treeToLatexEven(Tree t) {
/* 364 */     StringBuilder connections = new StringBuilder();
/* 365 */     StringBuilder hierarchy = new StringBuilder();
/* 366 */     int maxDepth = t.depth();
/* 367 */     treeToLatexEvenHelper(t, connections, hierarchy, 0, 1, 0, 0, maxDepth);
/* 368 */     return "\\tree" + hierarchy + "\n" + connections + "\n";
/*     */   }
/*     */   
/*     */ 
/*     */   private static int treeToLatexEvenHelper(Tree t, StringBuilder c, StringBuilder h, int n, int nextN, int indent, int curDepth, int maxDepth)
/*     */   {
/* 374 */     StringBuilder sb = new StringBuilder();
/* 375 */     for (int i = 0; i < indent; i++)
/* 376 */       sb.append("  ");
/* 377 */     h.append("\n" + sb);
/* 378 */     int tDepth = t.depth();
/* 379 */     if ((tDepth == 0) && (tDepth + curDepth < maxDepth)) {
/* 380 */       for (int pad = 0; pad < maxDepth - tDepth - curDepth; pad++) {
/* 381 */         h.append("{\\ntnode{pad}{}, ");
/*     */       }
/*     */     }
/* 384 */     h.append("{\\ntnode{z" + n + "}{" + t.label() + "}");
/* 385 */     if (!t.isLeaf()) {
/* 386 */       for (int k = 0; k < t.children().length; k++) {
/* 387 */         h.append(", ");
/* 388 */         c.append("\\nodeconnect{z" + n + "}{z" + nextN + "}\n");
/* 389 */         nextN = treeToLatexEvenHelper(t.children()[k], c, h, nextN, nextN + 1, indent + 1, curDepth + 1, maxDepth);
/*     */       }
/*     */     }
/*     */     
/* 393 */     if ((tDepth == 0) && (tDepth + curDepth < maxDepth)) {
/* 394 */       for (int pad = 0; pad < maxDepth - tDepth - curDepth; pad++) {
/* 395 */         h.append("}");
/*     */       }
/*     */     }
/* 398 */     h.append("}");
/* 399 */     return nextN;
/*     */   }
/*     */   
/*     */   static String texTree(Tree t) {
/* 403 */     return treeToLatex(t);
/*     */   }
/*     */   
/*     */   static Tree readTree(String s) throws IOException {
/* 407 */     return new PennTreeReader(new StringReader(s), new LabeledScoredTreeFactory(new StringLabelFactory())).readTree();
/*     */   }
/*     */   
/*     */   static String escape(String s)
/*     */   {
/* 412 */     StringBuilder sb = new StringBuilder();
/* 413 */     for (int i = 0; i < s.length(); i++) {
/* 414 */       char c = s.charAt(i);
/* 415 */       if (c == '^')
/* 416 */         sb.append('\\');
/* 417 */       sb.append(c);
/* 418 */       if (c == '^')
/* 419 */         sb.append("{}");
/*     */     }
/* 421 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws IOException
/*     */   {
/* 426 */     int i = 0;
/* 427 */     while (i < args.length) {
/* 428 */       Tree tree = readTree(args[i]);
/* 429 */       System.out.println(escape(texTree(tree)));
/* 430 */       i++;
/*     */     }
/* 432 */     if (i == 0) {
/* 433 */       Tree tree = new PennTreeReader(new java.io.BufferedReader(new InputStreamReader(System.in)), new LabeledScoredTreeFactory(new StringLabelFactory())).readTree();
/*     */       
/*     */ 
/* 436 */       System.out.println(escape(texTree(tree)));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\Trees.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */