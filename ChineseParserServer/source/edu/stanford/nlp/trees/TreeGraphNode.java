/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.LabelFactory;
/*     */ import edu.stanford.nlp.ling.MapLabel;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeGraphNode
/*     */   extends Tree
/*     */ {
/*     */   protected MapLabel label;
/*  37 */   protected TreeGraphNode parent = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */   protected TreeGraphNode[] children = ZERO_TGN_CHILDREN;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TreeGraph tg;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   protected static final TreeGraphNode[] ZERO_TGN_CHILDREN = new TreeGraphNode[0];
/*     */   
/*  59 */   private static LabelFactory mlf = MapLabel.factory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeGraphNode() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeGraphNode(Label label)
/*     */   {
/*  74 */     this.label = ((MapLabel)mlf.newLabel(label));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeGraphNode(Label label, List children)
/*     */   {
/*  86 */     this(label);
/*  87 */     setChildren(children);
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
/*     */   public TreeGraphNode(Tree t, TreeGraphNode parent)
/*     */   {
/* 100 */     this.parent = parent;
/* 101 */     Tree[] tKids = t.children();
/* 102 */     int numKids = tKids.length;
/* 103 */     this.children = new TreeGraphNode[numKids];
/* 104 */     for (int i = 0; i < numKids; i++) {
/* 105 */       this.children[i] = new TreeGraphNode(tKids[i], this);
/* 106 */       if (t.isPreTerminal()) {
/* 107 */         this.children[i].label.setTag(t.label().value());
/*     */       }
/*     */     }
/* 110 */     this.label = ((MapLabel)mlf.newLabel(t.label()));
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
/*     */   public boolean equals(Object o)
/*     */   {
/* 123 */     if (o == this) {
/* 124 */       return true;
/*     */     }
/* 126 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Label label()
/*     */   {
/* 137 */     return this.label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLabel(MapLabel label)
/*     */   {
/* 146 */     this.label = label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int index()
/*     */   {
/* 153 */     return this.label.index();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void setIndex(int index)
/*     */   {
/* 160 */     this.label.setIndex(index);
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
/*     */   private int indexLeaves(int startIndex)
/*     */   {
/* 173 */     if (isLeaf()) {
/* 174 */       int oldIndex = index();
/* 175 */       if (oldIndex >= 0) {
/* 176 */         startIndex = oldIndex;
/*     */       } else {
/* 178 */         setIndex(startIndex);
/*     */       }
/* 180 */       if (this.tg != null) {
/* 181 */         this.tg.addNodeToIndexMap(startIndex, this);
/*     */       }
/* 183 */       startIndex++;
/*     */     } else {
/* 185 */       for (int i = 0; i < this.children.length; i++) {
/* 186 */         startIndex = this.children[i].indexLeaves(startIndex);
/*     */       }
/*     */     }
/* 189 */     return startIndex;
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
/*     */   private int indexNodes(int startIndex)
/*     */   {
/* 204 */     if (index() < 0) {
/* 205 */       if (this.tg != null) {
/* 206 */         this.tg.addNodeToIndexMap(startIndex, this);
/*     */       }
/* 208 */       setIndex(startIndex++);
/*     */     }
/* 210 */     if (!isLeaf()) {
/* 211 */       for (int i = 0; i < this.children.length; i++) {
/* 212 */         startIndex = this.children[i].indexNodes(startIndex);
/*     */       }
/*     */     }
/* 215 */     return startIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void indexNodes()
/*     */   {
/* 226 */     indexNodes(indexLeaves(1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Tree parent()
/*     */   {
/* 233 */     return this.parent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setParent(TreeGraphNode parent)
/*     */   {
/* 240 */     this.parent = parent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Tree[] children()
/*     */   {
/* 247 */     return this.children;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChildren(Tree[] children)
/*     */   {
/* 258 */     if (children == null) {
/* 259 */       this.children = ZERO_TGN_CHILDREN;
/*     */     } else {
/* 261 */       this.children = ((TreeGraphNode[])children);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TreeGraph treeGraph()
/*     */   {
/* 270 */     return this.tg;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setTreeGraph(TreeGraph tg)
/*     */   {
/* 279 */     this.tg = tg;
/* 280 */     for (int i = 0; i < this.children.length; i++) {
/* 281 */       this.children[i].setTreeGraph(tg);
/*     */     }
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
/*     */   public boolean addArc(Object arcLabel, TreeGraphNode node)
/*     */   {
/* 295 */     if (node == null) {
/* 296 */       return false;
/*     */     }
/* 298 */     if (!treeGraph().equals(node.treeGraph())) {
/* 299 */       System.err.println("Warning: you are trying to add an arc from node " + this + " to node " + node + ", but they do not belong to the same TreeGraph!");
/*     */     }
/* 301 */     Map map = this.label.map();
/* 302 */     if (!map.containsKey(arcLabel)) {
/* 303 */       map.put(arcLabel, new HashSet());
/*     */     }
/* 305 */     return ((Set)map.get(arcLabel)).add(node);
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
/*     */   public Set followArcToSet(Object arcLabel)
/*     */   {
/* 320 */     Map map = ((MapLabel)label()).map();
/* 321 */     if (!map.containsKey(arcLabel)) {
/* 322 */       return null;
/*     */     }
/*     */     
/* 325 */     Object value = map.get(arcLabel);
/* 326 */     if (!(value instanceof Set)) {
/* 327 */       return null;
/*     */     }
/* 329 */     Set valueSet = (Set)value;
/*     */     
/* 331 */     Iterator it = valueSet.iterator();
/* 332 */     while (it.hasNext()) {
/* 333 */       Object next = it.next();
/* 334 */       if (!(next instanceof TreeGraphNode)) {
/* 335 */         it.remove();
/*     */       }
/*     */     }
/* 338 */     return valueSet;
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
/*     */   public TreeGraphNode followArcToNode(Object arcLabel)
/*     */   {
/* 356 */     Set valueSet = followArcToSet(arcLabel);
/* 357 */     if (valueSet == null) {
/* 358 */       return null;
/*     */     }
/* 360 */     Object firstValue = new ArrayList(valueSet).get(0);
/* 361 */     return safeCast(firstValue);
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
/*     */   public Set arcLabelsToNode(TreeGraphNode destNode)
/*     */   {
/* 375 */     Set arcLabels = new HashSet();
/* 376 */     for (Iterator it = ((MapLabel)label()).map().entrySet().iterator(); it.hasNext();) {
/* 377 */       Map.Entry arc = (Map.Entry)it.next();
/* 378 */       Object val = arc.getValue();
/* 379 */       if ((val != null) && ((val instanceof Set)) && (((Set)val).contains(destNode))) {
/* 380 */         Object key = arc.getKey();
/* 381 */         if (key != null) {
/* 382 */           arcLabels.add(key);
/*     */         }
/*     */       }
/*     */     }
/* 386 */     return arcLabels;
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
/*     */   public Object arcLabelToNode(TreeGraphNode destNode)
/*     */   {
/* 402 */     Set arcLabels = arcLabelsToNode(destNode);
/* 403 */     if (arcLabels == null) {
/* 404 */       return null;
/*     */     }
/* 406 */     return new ArrayList(arcLabels).get(0);
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
/*     */   public void percolateHeads(HeadFinder hf)
/*     */   {
/* 428 */     if (isLeaf()) {
/* 429 */       TreeGraphNode hwn = headWordNode();
/* 430 */       if (hwn == null) {
/* 431 */         setHeadWordNode(this);
/*     */       }
/*     */     } else {
/* 434 */       Tree[] kids = children();
/* 435 */       for (int i = 0; i < kids.length; i++) {
/* 436 */         kids[i].percolateHeads(hf);
/*     */       }
/* 438 */       TreeGraphNode head = safeCast(hf.determineHead(this, this.parent));
/* 439 */       if (head != null)
/*     */       {
/* 441 */         TreeGraphNode hwn = head.headWordNode();
/* 442 */         if ((hwn == null) && (head.isLeaf())) {
/* 443 */           setHeadWordNode(head);
/*     */         } else {
/* 445 */           setHeadWordNode(hwn);
/*     */         }
/*     */         
/* 448 */         TreeGraphNode htn = head.headTagNode();
/* 449 */         if ((htn == null) && (head.isLeaf())) {
/* 450 */           setHeadTagNode(this);
/*     */         } else {
/* 452 */           setHeadTagNode(htn);
/*     */         }
/*     */       }
/*     */       else {
/* 456 */         System.err.println("Head is null: " + this);
/*     */       }
/*     */     }
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
/*     */   public Set dependencies(Filter f, HeadFinder hf)
/*     */   {
/* 471 */     Set deps = new HashSet();
/* 472 */     for (Iterator it = subTrees().iterator(); it.hasNext();)
/*     */     {
/* 474 */       TreeGraphNode node = safeCast(it.next());
/* 475 */       if ((node != null) && (!node.isLeaf()) && (node.children().length >= 2))
/*     */       {
/*     */         TreeGraphNode headWordNode;
/*     */         
/*     */         TreeGraphNode headWordNode;
/* 480 */         if (hf != null) {
/* 481 */           headWordNode = safeCast(node.headTerminal(hf));
/*     */         } else {
/* 483 */           headWordNode = node.headWordNode();
/*     */         }
/*     */         
/* 486 */         Tree[] kids = node.children();
/* 487 */         for (int i = 0; i < kids.length; i++) {
/* 488 */           TreeGraphNode kid = safeCast(kids[i]);
/* 489 */           if (kid != null)
/*     */           {
/*     */             TreeGraphNode kidHeadWordNode;
/*     */             TreeGraphNode kidHeadWordNode;
/* 493 */             if (hf != null) {
/* 494 */               kidHeadWordNode = safeCast(kid.headTerminal(hf));
/*     */             } else {
/* 496 */               kidHeadWordNode = kid.headWordNode();
/*     */             }
/*     */             
/* 499 */             if ((headWordNode != null) && (headWordNode != kidHeadWordNode)) {
/* 500 */               Dependency d = new UnnamedDependency(headWordNode, kidHeadWordNode);
/* 501 */               if (f.accept(d))
/* 502 */                 deps.add(d);
/*     */             }
/*     */           }
/*     */         }
/*     */       } }
/* 507 */     return deps;
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
/*     */   public TreeGraphNode headWordNode()
/*     */   {
/* 522 */     TreeGraphNode hwn = safeCast(this.label.headWord());
/* 523 */     if ((hwn == null) || ((hwn.treeGraph() != null) && (!hwn.treeGraph().equals(treeGraph())))) {
/* 524 */       return null;
/*     */     }
/* 526 */     return hwn;
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
/*     */   private void setHeadWordNode(TreeGraphNode hwn)
/*     */   {
/* 541 */     this.label.setHeadWord(hwn);
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
/*     */   public TreeGraphNode headTagNode()
/*     */   {
/* 556 */     TreeGraphNode htn = safeCast(this.label.headTag());
/* 557 */     if ((htn == null) || ((htn.treeGraph() != null) && (!htn.treeGraph().equals(treeGraph())))) {
/* 558 */       return null;
/*     */     }
/* 560 */     return htn;
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
/*     */   private void setHeadTagNode(TreeGraphNode htn)
/*     */   {
/* 575 */     this.label.setHeadTag(htn);
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
/*     */   private TreeGraphNode safeCast(Object t)
/*     */   {
/* 588 */     if ((t == null) || (!(t instanceof TreeGraphNode))) {
/* 589 */       return null;
/*     */     }
/* 591 */     return (TreeGraphNode)t;
/*     */   }
/*     */   
/*     */   public TreeGraphNode highestNodeWithSameHead() {
/* 595 */     TreeGraphNode parent = null;
/* 596 */     for (TreeGraphNode node = this;; node = parent) {
/* 597 */       parent = safeCast(node.parent());
/* 598 */       if ((parent == null) || (parent.headWordNode() != node.headWordNode())) {
/* 599 */         return node;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TreeFactoryHolder
/*     */   {
/* 606 */     static final TreeGraphNodeFactory tgnf = new TreeGraphNodeFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeFactory treeFactory()
/*     */   {
/*     */     LabelFactory lf;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     LabelFactory lf;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 626 */     if (label() != null) {
/* 627 */       lf = label().labelFactory();
/*     */     } else {
/* 629 */       lf = MapLabel.factory();
/*     */     }
/* 631 */     return new TreeGraphNodeFactory(lf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TreeFactory factory()
/*     */   {
/* 642 */     return TreeFactoryHolder.tgnf;
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
/*     */   public static TreeFactory factory(LabelFactory lf)
/*     */   {
/* 655 */     return new TreeGraphNodeFactory(lf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toPrettyString(int indentLevel)
/*     */   {
/* 667 */     StringBuilder buf = new StringBuilder("\n");
/* 668 */     for (int i = 0; i < indentLevel; i++) {
/* 669 */       buf.append("  ");
/*     */     }
/* 671 */     if ((this.children == null) || (this.children.length == 0)) {
/* 672 */       buf.append(this.label.toString("value-index{map}"));
/*     */     } else {
/* 674 */       buf.append("(").append(this.label.toString("value-index{map}"));
/* 675 */       for (int i = 0; i < this.children.length; i++) {
/* 676 */         buf.append(" ").append(this.children[i].toPrettyString(indentLevel + 1));
/*     */       }
/* 678 */       buf.append(")");
/*     */     }
/* 680 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toOneLineString()
/*     */   {
/* 690 */     StringBuilder buf = new StringBuilder();
/* 691 */     if ((this.children == null) || (this.children.length == 0)) {
/* 692 */       buf.append(this.label);
/*     */     } else {
/* 694 */       buf.append("(").append(this.label);
/* 695 */       for (int i = 0; i < this.children.length; i++) {
/* 696 */         buf.append(" ").append(this.children[i].toString());
/*     */       }
/* 698 */       buf.append(")");
/*     */     }
/* 700 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 704 */     return this.label.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 712 */       TreeReader tr = new PennTreeReader(new StringReader("(S (NP (NNP Sam)) (VP (VBD died) (NP (NN today))))"), new LabeledScoredTreeFactory());
/* 713 */       Tree t = tr.readTree();
/* 714 */       System.out.println(t);
/* 715 */       TreeGraphNode tgn = new TreeGraphNode(t, (TreeGraphNode)null);
/* 716 */       System.out.println(tgn.toPrettyString(0));
/* 717 */       tgn.indexNodes();
/* 718 */       System.out.println(tgn.toPrettyString(0));
/* 719 */       tgn.percolateHeads(new SemanticHeadFinder());
/* 720 */       System.out.println(tgn.toPrettyString(0));
/*     */     } catch (Exception e) {
/* 722 */       System.err.println("Horrible error: " + e);
/* 723 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreeGraphNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */