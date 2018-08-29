/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTag;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.TaggedWord;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreeReaderFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TreeBinarizer implements TreeTransformer
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private HeadFinder hf;
/*     */   private TreeFactory tf;
/*     */   private TreebankLanguagePack tlp;
/*     */   private boolean insideFactor;
/*     */   private boolean markovFactor;
/*     */   private int markovOrder;
/*     */   private boolean useWrappingLabels;
/*     */   private double selectiveSplitThreshold;
/*     */   private boolean markFinalStates;
/*     */   private boolean unaryAtTop;
/*  35 */   private boolean doSelectiveSplit = false;
/*  36 */   private Counter<String> stateCounter = new Counter();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDoSelectiveSplit(boolean doSelectiveSplit)
/*     */   {
/*  47 */     this.doSelectiveSplit = doSelectiveSplit;
/*  48 */     if (!doSelectiveSplit) {
/*  49 */       this.stateCounter = new Counter();
/*     */     }
/*     */   }
/*     */   
/*     */   private static String join(List treeList) {
/*  54 */     StringBuilder sb = new StringBuilder();
/*  55 */     for (Iterator i = treeList.iterator(); i.hasNext();) {
/*  56 */       Tree t = (Tree)i.next();
/*  57 */       sb.append(t.label().value());
/*  58 */       if (i.hasNext()) {
/*  59 */         sb.append(" ");
/*     */       }
/*     */     }
/*  62 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void localTreeString(Tree t, StringBuilder sb, int level) {
/*  66 */     sb.append("\n");
/*  67 */     for (int i = 0; i < level; i++) {
/*  68 */       sb.append("  ");
/*     */     }
/*  70 */     sb.append("(").append(t.label());
/*  71 */     if ((level == 0) || (isSynthetic(t.label().value())))
/*     */     {
/*  73 */       for (int c = 0; c < t.numChildren(); c++) {
/*  74 */         localTreeString(t.getChild(c), sb, level + 1);
/*     */       }
/*     */     }
/*  77 */     sb.append(")");
/*     */   }
/*     */   
/*     */   protected static boolean isSynthetic(String label) {
/*  81 */     return label.indexOf('@') > -1;
/*     */   }
/*     */   
/*     */ 
/*     */   Tree binarizeLocalTree(Tree t, int headNum, TaggedWord head)
/*     */   {
/*  87 */     if (this.markovFactor) {
/*  88 */       String topCat = t.label().value();
/*  89 */       Label newLabel = new CategoryWordTag(topCat, head.word(), head.tag());
/*  90 */       t.setLabel(newLabel);
/*     */       Tree t2;
/*  92 */       Tree t2; if (this.insideFactor) {
/*  93 */         t2 = markovInsideBinarizeLocalTreeNew(t, headNum, 0, t.numChildren() - 1, true);
/*     */       }
/*     */       else {
/*  96 */         t2 = markovOutsideBinarizeLocalTree(t, head, headNum, topCat, new LinkedList(), false);
/*     */       }
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
/* 109 */       return t2;
/*     */     }
/* 111 */     if (this.insideFactor) {
/* 112 */       return insideBinarizeLocalTree(t, headNum, head, 0, 0);
/*     */     }
/* 114 */     return outsideBinarizeLocalTree(t, t.label().value(), t.label().value(), headNum, head, 0, "", 0, "");
/*     */   }
/*     */   
/*     */   private Tree markovOutsideBinarizeLocalTree(Tree t, TaggedWord head, int headLoc, String topCat, LinkedList<Tree> ll, boolean doneLeft) {
/* 118 */     String word = head.word();
/* 119 */     String tag = head.tag();
/* 120 */     List<Tree> newChildren = new ArrayList(2);
/*     */     
/* 122 */     if (headLoc == 0) {
/* 123 */       if (!doneLeft)
/*     */       {
/* 125 */         if (this.tlp.isStartSymbol(topCat)) {
/* 126 */           return markovOutsideBinarizeLocalTree(t, head, headLoc, topCat, new LinkedList(), true);
/*     */         }
/* 128 */         String headStr = t.getChild(headLoc).label().value();
/* 129 */         String subLabelStr = "@" + topCat + ": " + headStr + " ]";
/* 130 */         Label subLabel = new CategoryWordTag(subLabelStr, word, tag);
/* 131 */         Tree subTree = this.tf.newTreeNode(subLabel, t.getChildrenAsList());
/* 132 */         newChildren.add(markovOutsideBinarizeLocalTree(subTree, head, headLoc, topCat, new LinkedList(), true));
/* 133 */         return this.tf.newTreeNode(t.label(), newChildren);
/*     */       }
/*     */       
/* 136 */       int len = t.numChildren();
/*     */       
/* 138 */       if (len == 1) {
/* 139 */         return this.tf.newTreeNode(t.label(), Collections.singletonList(t.getChild(0)));
/*     */       }
/* 141 */       ll.addFirst(t.getChild(len - 1));
/* 142 */       if (ll.size() > this.markovOrder) {
/* 143 */         ll.removeLast();
/*     */       }
/*     */       
/* 146 */       String headStr = t.getChild(headLoc).label().value();
/* 147 */       String rightStr = (len > this.markovOrder - 1 ? "... " : "") + join(ll);
/* 148 */       String subLabelStr = "@" + topCat + ": " + headStr + " " + rightStr;
/* 149 */       Label subLabel = new CategoryWordTag(subLabelStr, word, tag);
/* 150 */       Tree subTree = this.tf.newTreeNode(subLabel, t.getChildrenAsList().subList(0, len - 1));
/* 151 */       newChildren.add(markovOutsideBinarizeLocalTree(subTree, head, headLoc, topCat, ll, true));
/* 152 */       newChildren.add(t.getChild(len - 1));
/* 153 */       return this.tf.newTreeNode(t.label(), newChildren);
/*     */     }
/* 155 */     if (headLoc > 0) {
/* 156 */       ll.addLast(t.getChild(0));
/* 157 */       if (ll.size() > this.markovOrder) {
/* 158 */         ll.removeFirst();
/*     */       }
/*     */       
/* 161 */       String headStr = t.getChild(headLoc).label().value();
/* 162 */       String leftStr = join(ll) + (headLoc > this.markovOrder - 1 ? " ..." : "");
/* 163 */       String subLabelStr = "@" + topCat + ": " + leftStr + " " + headStr + " ]";
/* 164 */       Label subLabel = new CategoryWordTag(subLabelStr, word, tag);
/* 165 */       Tree subTree = this.tf.newTreeNode(subLabel, t.getChildrenAsList().subList(1, t.numChildren()));
/* 166 */       newChildren.add(t.getChild(0));
/* 167 */       newChildren.add(markovOutsideBinarizeLocalTree(subTree, head, headLoc - 1, topCat, ll, false));
/* 168 */       return this.tf.newTreeNode(t.label(), newChildren);
/*     */     }
/* 170 */     return t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Tree markovInsideBinarizeLocalTreeNew(Tree t, int headLoc, int left, int right, boolean starting)
/*     */   {
/* 178 */     Tree[] children = t.children();
/* 179 */     if (starting)
/*     */     {
/* 181 */       if ((left == headLoc) && (right == headLoc)) {
/* 182 */         return t;
/*     */       }
/* 184 */       if (this.unaryAtTop)
/*     */       {
/* 186 */         Tree result = this.tf.newTreeNode(t.label(), Collections.singletonList(markovInsideBinarizeLocalTreeNew(t, headLoc, left, right, false)));
/* 187 */         return result;
/*     */       }
/*     */     }
/*     */     
/* 191 */     List<Tree> newChildren = null;
/*     */     
/* 193 */     if ((left == headLoc) && (right == headLoc))
/*     */     {
/* 195 */       newChildren = Collections.singletonList(children[headLoc]);
/* 196 */     } else if (left < headLoc)
/*     */     {
/* 198 */       newChildren = new ArrayList(2);
/* 199 */       newChildren.add(children[left]);
/* 200 */       newChildren.add(markovInsideBinarizeLocalTreeNew(t, headLoc, left + 1, right, false));
/* 201 */     } else if (right > headLoc)
/*     */     {
/* 203 */       newChildren = new ArrayList(2);
/* 204 */       newChildren.add(markovInsideBinarizeLocalTreeNew(t, headLoc, left, right - 1, false));
/* 205 */       newChildren.add(children[right]);
/*     */     }
/*     */     else {
/* 208 */       System.err.println("UHOH, bad parameters passed to markovInsideBinarizeLocalTree");
/*     */     }
/*     */     
/*     */     Label label;
/*     */     Label label;
/* 213 */     if (starting) {
/* 214 */       label = t.label();
/*     */     } else {
/* 216 */       label = makeSyntheticLabel(t, left, right, headLoc, this.markovOrder);
/*     */     }
/* 218 */     if (this.doSelectiveSplit) {
/* 219 */       double stateCount = this.stateCounter.getCount(label.value());
/* 220 */       if (stateCount < this.selectiveSplitThreshold) {
/* 221 */         if ((starting) && (!this.unaryAtTop))
/*     */         {
/* 223 */           label = t.label();
/*     */         } else {
/* 225 */           label = makeSyntheticLabel(t, left, right, headLoc, this.markovOrder - 1);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 230 */       this.stateCounter.incrementCount(label.value(), 1.0D);
/*     */     }
/*     */     
/* 233 */     Tree result = this.tf.newTreeNode(label, newChildren);
/* 234 */     return result;
/*     */   }
/*     */   
/*     */   private Label makeSyntheticLabel(Tree t, int left, int right, int headLoc, int markovOrder) {
/*     */     Label result;
/*     */     Label result;
/* 240 */     if (this.useWrappingLabels) {
/* 241 */       result = makeSyntheticLabel2(t, left, right, headLoc, markovOrder);
/*     */     } else {
/* 243 */       result = makeSyntheticLabel1(t, left, right, headLoc, markovOrder);
/*     */     }
/*     */     
/* 246 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Label makeSyntheticLabel1(Tree t, int left, int right, int headLoc, int markovOrder)
/*     */   {
/* 258 */     String topCat = t.label().value();
/* 259 */     Tree[] children = t.children();
/*     */     String leftString;
/* 261 */     String leftString; if (left == 0) {
/* 262 */       leftString = "[ ";
/*     */     } else
/* 264 */       leftString = " ";
/*     */     String rightString;
/*     */     String rightString;
/* 267 */     if (right == children.length - 1) {
/* 268 */       rightString = " ]";
/*     */     } else {
/* 270 */       rightString = " ";
/*     */     }
/* 272 */     for (int i = 0; i < markovOrder; i++) {
/* 273 */       if (left < headLoc) {
/* 274 */         leftString = leftString + children[left].label().value() + " ";
/* 275 */         left++;
/* 276 */       } else { if (right <= headLoc) break;
/* 277 */         rightString = " " + children[right].label().value() + rightString;
/* 278 */         right--;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 283 */     if (right > headLoc) {
/* 284 */       rightString = "..." + rightString;
/*     */     }
/* 286 */     if (left < headLoc) {
/* 287 */       leftString = leftString + "...";
/*     */     }
/* 289 */     String labelStr = "@" + topCat + "| " + leftString + "[" + t.getChild(headLoc).label().value() + "]" + rightString;
/* 290 */     String word = ((CategoryWordTag)t.label()).word();
/* 291 */     String tag = ((CategoryWordTag)t.label()).tag();
/* 292 */     return new CategoryWordTag(labelStr, word, tag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Label makeSyntheticLabel2(Tree t, int left, int right, int headLoc, int markovOrder)
/*     */   {
/* 300 */     String topCat = t.label().value();
/* 301 */     Tree[] children = t.children();
/*     */     
/* 303 */     int i = 0;
/* 304 */     String finalPiece; String finalPiece; if (this.markFinalStates)
/*     */     {
/* 306 */       if ((headLoc != 0) && (left == 0))
/*     */       {
/* 308 */         String finalPiece = " " + children[left].label().value() + "[";
/* 309 */         left++;
/* 310 */         i++;
/* 311 */       } else if ((headLoc == 0) && (right > headLoc) && (right == children.length - 1))
/*     */       {
/* 313 */         String finalPiece = " " + children[right].label().value() + "]";
/* 314 */         right--;
/* 315 */         i++;
/*     */       } else {
/* 317 */         finalPiece = "";
/*     */       }
/*     */     } else {
/* 320 */       finalPiece = "";
/*     */     }
/*     */     
/* 323 */     String middlePiece = "";
/* 324 */     for (; i < markovOrder; i++) {
/* 325 */       if (left < headLoc) {
/* 326 */         middlePiece = " " + children[left].label().value() + "<" + middlePiece;
/* 327 */         left++;
/* 328 */       } else { if (right <= headLoc) break;
/* 329 */         middlePiece = " " + children[right].label().value() + ">" + middlePiece;
/* 330 */         right--;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 335 */     if ((right > headLoc) || (left < headLoc)) {
/* 336 */       middlePiece = " ..." + middlePiece;
/*     */     }
/* 338 */     String headStr = t.getChild(headLoc).label().value();
/*     */     
/*     */ 
/*     */ 
/* 342 */     int leng = 4 + topCat.length() + headStr.length() + middlePiece.length() + finalPiece.length();
/* 343 */     StringBuilder sb = new StringBuilder(leng);
/* 344 */     sb.append("@").append(topCat).append("| ").append(headStr).append("_").append(middlePiece).append(finalPiece);
/* 345 */     String labelStr = sb.toString();
/*     */     
/*     */ 
/* 348 */     String word = ((CategoryWordTag)t.label()).word();
/* 349 */     String tag = ((CategoryWordTag)t.label()).tag();
/* 350 */     return new CategoryWordTag(labelStr, word, tag);
/*     */   }
/*     */   
/*     */   private Tree insideBinarizeLocalTree(Tree t, int headNum, TaggedWord head, int leftProcessed, int rightProcessed) {
/* 354 */     String word = head.word();
/* 355 */     String tag = head.tag();
/* 356 */     List<Tree> newChildren = new ArrayList(2);
/* 357 */     if (t.numChildren() <= leftProcessed + rightProcessed + 2) {
/* 358 */       Tree leftChild = t.getChild(leftProcessed);
/* 359 */       newChildren.add(leftChild);
/* 360 */       if (t.numChildren() == leftProcessed + rightProcessed + 1)
/*     */       {
/* 362 */         String finalCat = t.label().value();
/* 363 */         return this.tf.newTreeNode(new CategoryWordTag(finalCat, word, tag), newChildren);
/*     */       }
/*     */       
/* 366 */       Tree rightChild = t.getChild(leftProcessed + 1);
/* 367 */       newChildren.add(rightChild);
/* 368 */       String labelStr = t.label().value();
/* 369 */       if ((leftProcessed != 0) || (rightProcessed != 0)) {
/* 370 */         labelStr = "@ " + leftChild.label().value() + " " + rightChild.label().value();
/*     */       }
/* 372 */       return this.tf.newTreeNode(new CategoryWordTag(labelStr, word, tag), newChildren);
/*     */     }
/* 374 */     if (headNum > leftProcessed)
/*     */     {
/* 376 */       Tree leftChild = t.getChild(leftProcessed);
/* 377 */       Tree rightChild = insideBinarizeLocalTree(t, headNum, head, leftProcessed + 1, rightProcessed);
/* 378 */       newChildren.add(leftChild);
/* 379 */       newChildren.add(rightChild);
/* 380 */       String labelStr = "@ " + leftChild.label().value() + " " + rightChild.label().value().substring(2);
/* 381 */       if ((leftProcessed == 0) && (rightProcessed == 0)) {
/* 382 */         labelStr = t.label().value();
/*     */       }
/* 384 */       return this.tf.newTreeNode(new CategoryWordTag(labelStr, word, tag), newChildren);
/*     */     }
/*     */     
/* 387 */     Tree leftChild = insideBinarizeLocalTree(t, headNum, head, leftProcessed, rightProcessed + 1);
/* 388 */     Tree rightChild = t.getChild(t.numChildren() - rightProcessed - 1);
/* 389 */     newChildren.add(leftChild);
/* 390 */     newChildren.add(rightChild);
/* 391 */     String labelStr = "@ " + leftChild.label().value().substring(2) + " " + rightChild.label().value();
/* 392 */     if ((leftProcessed == 0) && (rightProcessed == 0)) {
/* 393 */       labelStr = t.label().value();
/*     */     }
/* 395 */     return this.tf.newTreeNode(new CategoryWordTag(labelStr, word, tag), newChildren);
/*     */   }
/*     */   
/*     */   private Tree outsideBinarizeLocalTree(Tree t, String labelStr, String finalCat, int headNum, TaggedWord head, int leftProcessed, String leftStr, int rightProcessed, String rightStr)
/*     */   {
/* 400 */     List<Tree> newChildren = new ArrayList(2);
/* 401 */     Label label = new CategoryWordTag(labelStr, head.word(), head.tag());
/*     */     
/* 403 */     if (t.numChildren() - leftProcessed - rightProcessed <= 2)
/*     */     {
/* 405 */       newChildren.add(t.getChild(leftProcessed));
/* 406 */       if (t.numChildren() - leftProcessed - rightProcessed == 2) {
/* 407 */         newChildren.add(t.getChild(leftProcessed + 1));
/*     */       }
/* 409 */       return this.tf.newTreeNode(label, newChildren);
/*     */     }
/* 411 */     if (headNum > leftProcessed)
/*     */     {
/* 413 */       Tree leftChild = t.getChild(leftProcessed);
/* 414 */       String childLeftStr = leftStr + " " + leftChild.label().value();
/* 415 */       String childLabelStr = "@" + finalCat + " :" + childLeftStr + " ..." + rightStr;
/* 416 */       Tree rightChild = outsideBinarizeLocalTree(t, childLabelStr, finalCat, headNum, head, leftProcessed + 1, childLeftStr, rightProcessed, rightStr);
/* 417 */       newChildren.add(leftChild);
/* 418 */       newChildren.add(rightChild);
/* 419 */       return this.tf.newTreeNode(label, newChildren);
/*     */     }
/*     */     
/* 422 */     Tree rightChild = t.getChild(t.numChildren() - rightProcessed - 1);
/* 423 */     String childRightStr = " " + rightChild.label().value() + rightStr;
/* 424 */     String childLabelStr = "@" + finalCat + " :" + leftStr + " ..." + childRightStr;
/* 425 */     Tree leftChild = outsideBinarizeLocalTree(t, childLabelStr, finalCat, headNum, head, leftProcessed, leftStr, rightProcessed + 1, childRightStr);
/* 426 */     newChildren.add(leftChild);
/* 427 */     newChildren.add(rightChild);
/* 428 */     return this.tf.newTreeNode(label, newChildren);
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
/*     */   public Tree transformTree(Tree t)
/*     */   {
/* 442 */     if (t == null) {
/* 443 */       return null;
/*     */     }
/*     */     
/* 446 */     String cat = t.label().value();
/*     */     
/* 448 */     if (t.isLeaf()) {
/* 449 */       Label label = new edu.stanford.nlp.ling.StringLabel(cat);
/* 450 */       return this.tf.newLeaf(label);
/*     */     }
/*     */     
/* 453 */     if (t.isPreTerminal()) {
/* 454 */       Tree childResult = transformTree(t.getChild(0));
/* 455 */       String word = childResult.value();
/* 456 */       List<Tree> newChildren = new ArrayList(1);
/* 457 */       newChildren.add(childResult);
/* 458 */       return this.tf.newTreeNode(new CategoryWordTag(cat, word, cat), newChildren);
/*     */     }
/*     */     
/* 461 */     Tree headChild = this.hf.determineHead(t);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 468 */     if ((headChild == null) && (!t.label().value().startsWith(this.tlp.startSymbol()))) {
/* 469 */       System.err.println("### No head found for:");
/* 470 */       t.pennPrint();
/*     */     }
/* 472 */     int headNum = -1;
/* 473 */     Tree[] kids = t.children();
/* 474 */     List<Tree> newChildren = new ArrayList(kids.length);
/* 475 */     for (int childNum = 0; childNum < kids.length; childNum++) {
/* 476 */       Tree child = kids[childNum];
/* 477 */       Tree childResult = transformTree(child);
/* 478 */       if (child == headChild) {
/* 479 */         headNum = childNum;
/*     */       }
/* 481 */       newChildren.add(childResult);
/*     */     }
/*     */     Tree result;
/*     */     Tree result;
/* 485 */     if (t.label().value().startsWith(this.tlp.startSymbol()))
/*     */     {
/* 487 */       CategoryWordTag label = (CategoryWordTag)t.label();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 496 */       result = this.tf.newTreeNode(label, newChildren);
/*     */     } else {
/* 498 */       CategoryWordTag headLabel = (CategoryWordTag)headChild.label();
/* 499 */       String word = headLabel.word();
/* 500 */       String tag = headLabel.tag();
/* 501 */       Label label = new CategoryWordTag(cat, word, tag);
/* 502 */       result = this.tf.newTreeNode(label, newChildren);
/*     */       
/*     */ 
/*     */ 
/* 506 */       TaggedWord head = new TaggedWord(word, tag);
/* 507 */       result = binarizeLocalTree(result, headNum, head);
/*     */     }
/* 509 */     return result;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeBinarizer(HeadFinder hf, TreebankLanguagePack tlp, boolean insideFactor, boolean markovFactor, int markovOrder, boolean useWrappingLabels, boolean unaryAtTop, double selectiveSplitThreshold, boolean markFinalStates)
/*     */   {
/* 532 */     this.hf = hf;
/* 533 */     this.tlp = tlp;
/* 534 */     this.tf = new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.CategoryWordTagFactory());
/* 535 */     this.insideFactor = insideFactor;
/* 536 */     this.markovFactor = markovFactor;
/* 537 */     this.markovOrder = markovOrder;
/* 538 */     this.useWrappingLabels = useWrappingLabels;
/* 539 */     this.unaryAtTop = unaryAtTop;
/* 540 */     this.selectiveSplitThreshold = selectiveSplitThreshold;
/* 541 */     this.markFinalStates = markFinalStates;
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
/*     */   public static void main(String[] args)
/*     */   {
/* 559 */     TreebankLangParserParams tlpp = null;
/*     */     
/*     */ 
/*     */ 
/* 563 */     TreeReaderFactory trf = new TreeReaderFactory() {
/*     */       public edu.stanford.nlp.trees.TreeReader newTreeReader(java.io.Reader in) {
/* 565 */         return new edu.stanford.nlp.trees.PennTreeReader(in, new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.CategoryWordTagFactory()), new edu.stanford.nlp.trees.BobChrisTreeNormalizer());
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 571 */     };
/* 572 */     String fileExt = "mrg";
/* 573 */     HeadFinder hf = new edu.stanford.nlp.trees.ModCollinsHeadFinder();
/* 574 */     TreebankLanguagePack tlp = new edu.stanford.nlp.trees.PennTreebankLanguagePack();
/* 575 */     boolean insideFactor = false;
/* 576 */     boolean mf = false;
/* 577 */     int mo = 1;
/* 578 */     boolean uwl = false;
/* 579 */     boolean uat = false;
/* 580 */     double sst = 20.0D;
/* 581 */     boolean mfs = false;
/*     */     
/* 583 */     int i = 0;
/* 584 */     while ((i < args.length) && (args[i].startsWith("-"))) {
/* 585 */       if ((args[i].equalsIgnoreCase("-tlp")) && (i + 1 < args.length)) {
/*     */         try {
/* 587 */           tlp = (TreebankLanguagePack)Class.forName(args[(i + 1)]).newInstance();
/*     */         } catch (Exception e) {
/* 589 */           System.err.println("Couldn't instantiate: " + args[(i + 1)]);
/*     */         }
/* 591 */         i++;
/* 592 */       } else if ((args[i].equalsIgnoreCase("-tlpp")) && (i + 1 < args.length)) {
/*     */         try {
/* 594 */           tlpp = (TreebankLangParserParams)Class.forName(args[(i + 1)]).newInstance();
/*     */         } catch (Exception e) {
/* 596 */           System.err.println("Couldn't instantiate: " + args[(i + 1)]);
/*     */         }
/* 598 */         i++;
/* 599 */       } else if (args[i].equalsIgnoreCase("-insideFactor")) {
/* 600 */         insideFactor = true;
/* 601 */       } else if ((args[i].equalsIgnoreCase("-markovOrder")) && (i + 1 < args.length)) {
/* 602 */         i++;
/* 603 */         mo = Integer.parseInt(args[i]);
/*     */       } else {
/* 605 */         System.err.println("Unknown option:" + args[i]);
/*     */       }
/* 607 */       i++;
/*     */     }
/* 609 */     if (i >= args.length) {
/* 610 */       System.err.println("usage: java TreeBinarizer [-tlpp class|-markovOrder int|...] treebankPath");
/* 611 */       System.exit(0);
/*     */     }
/*     */     Treebank treebank;
/* 614 */     if (tlpp != null) {
/* 615 */       Treebank treebank = tlpp.memoryTreebank();
/* 616 */       tlp = tlpp.treebankLanguagePack();
/* 617 */       fileExt = tlp.treebankFileExtension();
/* 618 */       hf = tlpp.headFinder();
/*     */     } else {
/* 620 */       treebank = new edu.stanford.nlp.trees.DiskTreebank(trf);
/*     */     }
/* 622 */     treebank.loadPath(args[i], fileExt, true);
/*     */     
/* 624 */     TreeTransformer tt = new TreeBinarizer(hf, tlp, insideFactor, mf, mo, uwl, uat, sst, mfs);
/*     */     
/*     */ 
/* 627 */     for (Tree t : treebank) {
/* 628 */       Tree newT = tt.transformTree(t);
/* 629 */       System.out.println("Original tree:");
/* 630 */       t.pennPrint();
/* 631 */       System.out.println("Binarized tree:");
/* 632 */       newT.pennPrint();
/* 633 */       System.out.println();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\TreeBinarizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */