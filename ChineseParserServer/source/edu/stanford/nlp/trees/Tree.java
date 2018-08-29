/*      */ package edu.stanford.nlp.trees;
/*      */ 
/*      */ import edu.stanford.nlp.ling.AbstractMapLabel;
/*      */ import edu.stanford.nlp.ling.HasTag;
/*      */ import edu.stanford.nlp.ling.HasWord;
/*      */ import edu.stanford.nlp.ling.Label;
/*      */ import edu.stanford.nlp.ling.LabelFactory;
/*      */ import edu.stanford.nlp.ling.LabeledWord;
/*      */ import edu.stanford.nlp.ling.MapLabel;
/*      */ import edu.stanford.nlp.ling.Sentence;
/*      */ import edu.stanford.nlp.ling.TaggedWord;
/*      */ import edu.stanford.nlp.ling.Word;
/*      */ import edu.stanford.nlp.util.Filter;
/*      */ import edu.stanford.nlp.util.Filters;
/*      */ import edu.stanford.nlp.util.IntPair;
/*      */ import edu.stanford.nlp.util.MutableInteger;
/*      */ import edu.stanford.nlp.util.Scored;
/*      */ import edu.stanford.nlp.util.StringUtils;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Serializable;
/*      */ import java.io.StringReader;
/*      */ import java.io.StringWriter;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Tree
/*      */   extends AbstractCollection<Tree>
/*      */   implements Label, Labeled, Scored, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 5441849457648722744L;
/*   47 */   protected static final Tree[] ZEROCHILDREN = new Tree[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int initialPrintStringBufferSize = 500;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int indentIncr = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isLeaf()
/*      */   {
/*   63 */     Tree[] kids = children();
/*   64 */     return kids.length == 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int numChildren()
/*      */   {
/*   76 */     return children().length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUnaryRewrite()
/*      */   {
/*   87 */     return numChildren() == 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPreTerminal()
/*      */   {
/*   98 */     Tree[] kids = children();
/*   99 */     return (kids.length == 1) && (kids[0].isLeaf());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPrePreTerminal()
/*      */   {
/*  112 */     Tree[] kids = children();
/*  113 */     if (kids.length == 0) {
/*  114 */       return false;
/*      */     }
/*  116 */     for (Tree kid : kids) {
/*  117 */       if (!kid.isPreTerminal()) {
/*  118 */         return false;
/*      */       }
/*      */     }
/*  121 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isPhrasal()
/*      */   {
/*  136 */     Tree[] kids = children();
/*  137 */     return (kids != null) && (kids.length != 0) && ((kids.length != 1) || (!kids[0].isLeaf()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equals(Object o)
/*      */   {
/*  150 */     if (o == this)
/*  151 */       return true;
/*  152 */     if (!(o instanceof Tree)) {
/*  153 */       return false;
/*      */     }
/*  155 */     Tree t = (Tree)o;
/*  156 */     if (!label().equals(t.label())) {
/*  157 */       return false;
/*      */     }
/*  159 */     Tree[] mykids = children();
/*  160 */     Tree[] theirkids = t.children();
/*      */     
/*  162 */     if (mykids.length != theirkids.length) {
/*  163 */       return false;
/*      */     }
/*  165 */     for (int i = 0; i < mykids.length; i++) {
/*  166 */       if (!mykids[i].equals(theirkids[i])) {
/*  167 */         return false;
/*      */       }
/*      */     }
/*  170 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  182 */     Label l = label();
/*  183 */     int hc = l == null ? 1 : l.hashCode();
/*  184 */     Tree[] kids = children();
/*  185 */     for (int i = 0; i < kids.length; i++) {
/*  186 */       l = kids[i].label();
/*  187 */       int hc2 = l == null ? i : l.hashCode();
/*  188 */       hc ^= hc2 << i;
/*      */     }
/*  190 */     return hc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int indexOf(Tree tree)
/*      */   {
/*  203 */     Tree[] kids = children();
/*  204 */     for (int i = 0; i < kids.length; i++) {
/*  205 */       if (kids[i].equals(tree)) {
/*  206 */         return i;
/*      */       }
/*      */     }
/*  209 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract Tree[] children();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Tree> getChildrenAsList()
/*      */   {
/*  238 */     Tree[] kids = children();
/*  239 */     int length = kids.length;
/*  240 */     ArrayList<Tree> kidsList = new ArrayList(length);
/*  241 */     for (int i = 0; i < length; i++) {
/*  242 */       kidsList.add(kids[i]);
/*      */     }
/*  244 */     return kidsList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setChildren(Tree[] children)
/*      */   {
/*  263 */     throw new UnsupportedOperationException();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setChildren(List<Tree> childTreesList)
/*      */   {
/*  285 */     if ((childTreesList == null) || (childTreesList.size() == 0)) {
/*  286 */       setChildren(ZEROCHILDREN);
/*      */     } else {
/*  288 */       int leng = childTreesList.size();
/*  289 */       Tree[] childTrees = new Tree[leng];
/*  290 */       childTreesList.toArray(childTrees);
/*  291 */       setChildren(childTrees);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Label label()
/*      */   {
/*  304 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setLabel(Label label) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double score()
/*      */   {
/*  325 */     return NaN.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setScore(double score) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree firstChild()
/*      */   {
/*  345 */     Tree[] kids = children();
/*  346 */     if (kids.length == 0) {
/*  347 */       return null;
/*      */     }
/*  349 */     return kids[0];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree lastChild()
/*      */   {
/*  360 */     Tree[] kids = children();
/*  361 */     if (kids.length == 0) {
/*  362 */       return null;
/*      */     }
/*  364 */     return kids[(kids.length - 1)];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree upperMostUnary(Tree root)
/*      */   {
/*  379 */     Tree parent = parent(root);
/*  380 */     if (parent == null) {
/*  381 */       return this;
/*      */     }
/*  383 */     if (parent.numChildren() > 1) {
/*  384 */       return this;
/*      */     }
/*  386 */     return parent.upperMostUnary(root);
/*      */   }
/*      */   
/*      */   public void setSpans() {
/*  390 */     constituentsNodes(0);
/*      */   }
/*      */   
/*      */   public IntPair getSpan() {
/*  394 */     return (IntPair)((MapLabel)label()).get("SPAN");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set constituents()
/*      */   {
/*  404 */     return constituents(new SimpleConstituentFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Constituent> constituents(ConstituentFactory cf)
/*      */   {
/*  418 */     Set<Constituent> constituentsSet = new HashSet();
/*  419 */     constituents(constituentsSet, 0, cf);
/*  420 */     return constituentsSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int constituentsNodes(int left)
/*      */   {
/*  431 */     if (isLeaf())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  436 */       MapLabel l = (MapLabel)label();
/*  437 */       l.put("SPAN", new IntPair(left, left));
/*  438 */       return left + 1;
/*      */     }
/*  440 */     int position = left;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  446 */     Tree[] kids = children();
/*  447 */     for (int i = 0; i < kids.length; i++)
/*      */     {
/*      */ 
/*  450 */       position = kids[i].constituentsNodes(position);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  455 */     MapLabel l = (MapLabel)label();
/*  456 */     l.put("SPAN", new IntPair(left, position - 1));
/*      */     
/*  458 */     return position;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int constituents(Set<Constituent> constituentsSet, int left, ConstituentFactory cf)
/*      */   {
/*  477 */     if (isLeaf())
/*      */     {
/*      */ 
/*      */ 
/*  481 */       return left + 1;
/*      */     }
/*  483 */     int position = left;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  489 */     Tree[] kids = children();
/*  490 */     for (int i = 0; i < kids.length; i++)
/*      */     {
/*      */ 
/*  493 */       position = kids[i].constituents(constituentsSet, position, cf);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  498 */     constituentsSet.add(cf.newConstituent(left, position, label(), score()));
/*      */     
/*  500 */     return position;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree localTree()
/*      */   {
/*  514 */     Tree[] kids = children();
/*  515 */     Tree[] newKids = new Tree[kids.length];
/*  516 */     TreeFactory tf = treeFactory();
/*  517 */     int i = 0; for (int n = kids.length; i < n; i++) {
/*  518 */       newKids[i] = tf.newTreeNode(kids[i].label(), Arrays.asList(ZEROCHILDREN));
/*      */     }
/*  520 */     return tf.newTreeNode(label(), Arrays.asList(newKids));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Tree> localTrees()
/*      */   {
/*  534 */     Set<Tree> set = new HashSet();
/*  535 */     for (Tree st : this) {
/*  536 */       if (st.isPhrasal()) {
/*  537 */         set.add(st.localTree());
/*      */       }
/*      */     }
/*  540 */     return set;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toStructureDebugString()
/*      */   {
/*  548 */     String leafLabels = null;
/*  549 */     String tagLabels = null;
/*  550 */     String phraseLabels = null;
/*  551 */     String leaves = null;
/*  552 */     String nodes = null;
/*  553 */     for (Tree st : this) {
/*  554 */       if (st.isPhrasal()) {
/*  555 */         if (nodes == null) {
/*  556 */           nodes = StringUtils.getShortClassName(st);
/*  557 */         } else if (!nodes.equals(StringUtils.getShortClassName(st))) {
/*  558 */           nodes = "mixed";
/*      */         }
/*  560 */         Label lab = st.label();
/*  561 */         if (phraseLabels == null) {
/*  562 */           if (lab == null) {
/*  563 */             phraseLabels = "null";
/*      */           } else {
/*  565 */             phraseLabels = StringUtils.getShortClassName(lab);
/*      */           }
/*  567 */         } else if (!phraseLabels.equals(StringUtils.getShortClassName(lab))) {
/*  568 */           phraseLabels = "mixed";
/*      */         }
/*  570 */       } else if (st.isPreTerminal()) {
/*  571 */         if (nodes == null) {
/*  572 */           nodes = StringUtils.getShortClassName(st);
/*  573 */         } else if (!nodes.equals(StringUtils.getShortClassName(st))) {
/*  574 */           nodes = "mixed";
/*      */         }
/*  576 */         Label lab = st.label();
/*  577 */         if (tagLabels == null) {
/*  578 */           if (lab == null) {
/*  579 */             tagLabels = "null";
/*      */           } else {
/*  581 */             tagLabels = StringUtils.getShortClassName(lab);
/*      */           }
/*  583 */         } else if (!tagLabels.equals(StringUtils.getShortClassName(lab))) {
/*  584 */           tagLabels = "mixed";
/*      */         }
/*  586 */       } else if (st.isLeaf()) {
/*  587 */         if (leaves == null) {
/*  588 */           leaves = StringUtils.getShortClassName(st);
/*  589 */         } else if (!leaves.equals(StringUtils.getShortClassName(st))) {
/*  590 */           leaves = "mixed";
/*      */         }
/*  592 */         Label lab = st.label();
/*  593 */         if (leafLabels == null) {
/*  594 */           if (lab == null) {
/*  595 */             leafLabels = "null";
/*      */           } else {
/*  597 */             leafLabels = StringUtils.getShortClassName(lab);
/*      */           }
/*  599 */         } else if (!leafLabels.equals(StringUtils.getShortClassName(lab))) {
/*  600 */           leafLabels = "mixed";
/*      */         }
/*      */       } else {
/*  603 */         throw new IllegalStateException("Bad tree: " + this);
/*      */       }
/*      */     }
/*  606 */     return "Tree with " + nodes + " interior nodes and " + leaves + " leaves, and " + phraseLabels + " phrase labels, " + tagLabels + " tag labels, and " + leafLabels + " leaf labels.";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public StringBuffer toStringBuffer(StringBuffer sb)
/*      */   {
/*  630 */     sb.append("(");
/*  631 */     if (label() != null) {
/*  632 */       sb.append(label());
/*      */     }
/*  634 */     Tree[] kids = children();
/*  635 */     if (kids != null) {
/*  636 */       for (int i = 0; i < kids.length; i++) {
/*  637 */         sb.append(" ");
/*  638 */         kids[i].toStringBuffer(sb);
/*      */       }
/*      */     }
/*  641 */     return sb.append(")");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  653 */     return toStringBuffer(new StringBuffer(500)).toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private String makeIndentString(int indent)
/*      */   {
/*  661 */     StringBuffer sb = new StringBuffer(indent);
/*  662 */     for (int i = 0; i < 2; i++) {
/*  663 */       sb.append(" ");
/*      */     }
/*  665 */     return sb.toString();
/*      */   }
/*      */   
/*      */   public void printLocalTree()
/*      */   {
/*  670 */     printLocalTree(new PrintWriter(System.out, true));
/*      */   }
/*      */   
/*      */   public void printLocalTree(PrintWriter pw) {
/*  674 */     pw.print("(" + label() + " ");
/*  675 */     Tree[] kids = children();
/*  676 */     for (int i = 0; i < kids.length; i++) {
/*  677 */       pw.print("(" + kids[i].label() + ") ");
/*      */     }
/*  679 */     pw.println(")");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void indentedListPrint()
/*      */   {
/*  688 */     indentedListPrint(new PrintWriter(System.out, true), false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void indentedListPrint(PrintWriter pw, boolean printScores)
/*      */   {
/*  700 */     indentedListPrint("", makeIndentString(2), pw, printScores);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void indentedListPrint(String indent, String pad, PrintWriter pw, boolean printScores)
/*      */   {
/*  717 */     StringBuilder sb = new StringBuilder(indent);
/*  718 */     Label label = label();
/*  719 */     if (label != null) {
/*  720 */       sb.append(label.toString());
/*      */     }
/*  722 */     if (printScores) {
/*  723 */       sb.append("  ");
/*  724 */       sb.append(score());
/*      */     }
/*  726 */     pw.println(sb.toString());
/*  727 */     Tree[] children = children();
/*  728 */     String newIndent = indent + pad;
/*  729 */     int i = 0; for (int n = children.length; i < n; i++) {
/*  730 */       children[i].indentedListPrint(newIndent, pad, pw, printScores);
/*      */     }
/*      */   }
/*      */   
/*      */   private static void displayChildren(Tree[] trChildren, int indent, boolean parentLabelNull, PrintWriter pw)
/*      */   {
/*  736 */     boolean firstSibling = true;
/*  737 */     boolean leftSibIsPreTerm = true;
/*  738 */     for (int i = 0; i < trChildren.length; i++) {
/*  739 */       Tree currentTree = trChildren[i];
/*  740 */       currentTree.display(indent, parentLabelNull, firstSibling, leftSibIsPreTerm, false, pw);
/*  741 */       leftSibIsPreTerm = trChildren[i].isPreTerminal();
/*      */       
/*  743 */       if ((trChildren[i].label() != null) && (trChildren[i].label().toString().startsWith("CC"))) {
/*  744 */         leftSibIsPreTerm = false;
/*      */       }
/*  746 */       firstSibling = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String nodeString()
/*      */   {
/*  758 */     if ((label() != null) && (label().toString() != null)) {
/*  759 */       return label().toString();
/*      */     }
/*  761 */     return "";
/*      */   }
/*      */   
/*  764 */   public static boolean DISPLAY_SCORES = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void display(int indent, boolean parentLabelNull, boolean firstSibling, boolean leftSiblingPreTerminal, boolean topLevel, PrintWriter pw)
/*      */   {
/*  771 */     boolean suppressIndent = (parentLabelNull) || ((firstSibling) && (isPreTerminal())) || ((leftSiblingPreTerminal) && (isPreTerminal()) && ((label() == null) || (!label().toString().startsWith("CC"))));
/*  772 */     if (suppressIndent) {
/*  773 */       pw.print(" ");
/*      */     }
/*      */     else {
/*  776 */       if (!topLevel) {
/*  777 */         pw.println();
/*      */       }
/*  779 */       for (int i = 0; i < indent; i++) {
/*  780 */         pw.print("  ");
/*      */       }
/*      */     }
/*      */     
/*  784 */     if ((isLeaf()) || (isPreTerminal())) {
/*  785 */       pw.print(toString());
/*  786 */       pw.flush();
/*  787 */       return;
/*      */     }
/*  789 */     pw.print("(");
/*  790 */     pw.print(nodeString());
/*      */     
/*  792 */     displayChildren(children(), indent + 1, (label() == null) || (label().toString() == null), pw);
/*  793 */     pw.print(")");
/*  794 */     pw.flush();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void pennPrint(PrintWriter pw)
/*      */   {
/*  812 */     display(0, false, false, false, true, pw);
/*  813 */     pw.println();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void pennPrint(PrintStream ps)
/*      */   {
/*  831 */     pennPrint(new PrintWriter(new OutputStreamWriter(ps), true));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String pennString()
/*      */   {
/*  838 */     StringWriter sw = new StringWriter();
/*  839 */     pennPrint(new PrintWriter(sw));
/*  840 */     return sw.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void pennPrint()
/*      */   {
/*  856 */     pennPrint(System.out);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int depth()
/*      */   {
/*  869 */     if (isLeaf()) {
/*  870 */       return 0;
/*      */     }
/*  872 */     int maxDepth = 0;
/*  873 */     Tree[] kids = children();
/*  874 */     for (int i = 0; i < kids.length; i++) {
/*  875 */       int curDepth = kids[i].depth();
/*  876 */       if (curDepth > maxDepth) {
/*  877 */         maxDepth = curDepth;
/*      */       }
/*      */     }
/*  880 */     return maxDepth + 1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int depth(Tree node)
/*      */   {
/*  891 */     Tree p = node.parent(this);
/*  892 */     if (this == node) return 0;
/*  893 */     if (p == null) return -1;
/*  894 */     int depth = 1;
/*  895 */     while (this != p) {
/*  896 */       p = p.parent(this);
/*  897 */       depth++;
/*      */     }
/*  899 */     return depth;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree headTerminal(HeadFinder hf, Tree parent)
/*      */   {
/*  911 */     if (isLeaf()) {
/*  912 */       return this;
/*      */     }
/*  914 */     Tree head = hf.determineHead(this, parent);
/*  915 */     if (head != null) {
/*  916 */       return head.headTerminal(hf, parent);
/*      */     }
/*  918 */     System.err.println("Head is null: " + this);
/*  919 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree headTerminal(HeadFinder hf)
/*      */   {
/*  931 */     return headTerminal(hf, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree headPreTerminal(HeadFinder hf)
/*      */   {
/*  946 */     if (isPreTerminal())
/*  947 */       return this;
/*  948 */     if (isLeaf()) {
/*  949 */       throw new IllegalArgumentException("Called headPreTerminal on a leaf: " + this);
/*      */     }
/*  951 */     Tree head = hf.determineHead(this);
/*  952 */     if (head != null) {
/*  953 */       return head.headPreTerminal(hf);
/*      */     }
/*  955 */     System.err.println("Head preterminal is null: " + this);
/*  956 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void percolateHeads(HeadFinder hf)
/*      */   {
/*  973 */     Label cwt = label();
/*  974 */     if (isLeaf()) {
/*  975 */       if ((cwt instanceof HasWord)) {
/*  976 */         HasWord w = (HasWord)cwt;
/*  977 */         if (w.word() == null) {
/*  978 */           w.setWord(cwt.value());
/*      */         }
/*      */       }
/*      */     } else {
/*  982 */       Tree[] kids = children();
/*  983 */       for (int i = 0; i < kids.length; i++) {
/*  984 */         kids[i].percolateHeads(hf);
/*      */       }
/*  986 */       Tree head = hf.determineHead(this);
/*  987 */       if (head != null) {
/*  988 */         Label headCwt = head.label();
/*  989 */         String headTag = null;
/*  990 */         if ((headCwt instanceof HasTag)) {
/*  991 */           headTag = ((HasTag)headCwt).tag();
/*      */         }
/*  993 */         if ((headTag == null) && (head.isLeaf()))
/*      */         {
/*  995 */           headTag = cwt.value();
/*      */         }
/*  997 */         String headWord = null;
/*  998 */         if ((headCwt instanceof HasWord)) {
/*  999 */           headWord = ((HasWord)headCwt).word();
/*      */         }
/* 1001 */         if ((headWord == null) && (head.isLeaf()))
/*      */         {
/*      */ 
/*      */ 
/* 1005 */           headWord = headCwt.value();
/*      */         }
/* 1007 */         if ((cwt instanceof HasWord)) {
/* 1008 */           ((HasWord)cwt).setWord(headWord);
/*      */         }
/* 1010 */         if ((cwt instanceof HasTag)) {
/* 1011 */           ((HasTag)cwt).setTag(headTag);
/*      */         }
/*      */       } else {
/* 1014 */         System.err.println("Head is null: " + this);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Dependency> dependencies()
/*      */   {
/* 1029 */     return dependencies(Filters.acceptFilter());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Dependency> dependencies(Filter f)
/*      */   {
/* 1043 */     return dependencies(f, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Dependency> dependencies(HeadFinder hf)
/*      */   {
/* 1055 */     return dependencies(Filters.acceptFilter(), hf);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Dependency> dependencies(Filter f, HeadFinder hf)
/*      */   {
/* 1071 */     Set<Dependency> deps = new HashSet();
/* 1072 */     for (Tree node : subTrees()) {
/* 1073 */       if ((!node.isLeaf()) && (node.children().length >= 2))
/*      */       {
/*      */ 
/*      */ 
/* 1077 */         Label l = node.label();
/* 1078 */         Word w = null;
/* 1079 */         if (hf != null) {
/* 1080 */           Tree hwt = node.headTerminal(hf);
/* 1081 */           if (hwt != null) {
/* 1082 */             w = new Word(hwt.label());
/*      */           }
/*      */         }
/* 1085 */         else if ((l instanceof HasWord)) {
/* 1086 */           w = new Word(((HasWord)l).word());
/*      */         }
/*      */         
/* 1089 */         Tree[] kids = node.children();
/* 1090 */         boolean seenHead = false;
/* 1091 */         for (int cNum = 0; cNum < kids.length; cNum++) {
/* 1092 */           Tree child = kids[cNum];
/* 1093 */           Label dl = child.label();
/* 1094 */           Word dw = null;
/* 1095 */           if (hf != null) {
/* 1096 */             Tree dwt = child.headTerminal(hf);
/* 1097 */             if (dwt != null) {
/* 1098 */               dw = new Word(dwt.label());
/*      */             }
/*      */           }
/* 1101 */           else if ((dl instanceof HasWord)) {
/* 1102 */             dw = new Word(((HasWord)dl).word());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1107 */           if ((w != null) && (w.word() != null) && (dw != null) && (w.word().equals(dw.word())) && (!seenHead)) {
/* 1108 */             seenHead = true;
/*      */           } else {
/* 1110 */             Dependency p = new UnnamedDependency(w, dw);
/* 1111 */             if (f.accept(p))
/* 1112 */               deps.add(p);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1117 */     return deps;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Dependency> taggedDependencies()
/*      */   {
/* 1131 */     return taggedDependencies(Filters.acceptFilter());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Dependency> taggedDependencies(Filter<Dependency> f)
/*      */   {
/* 1148 */     Set<Dependency> deps = new HashSet();
/* 1149 */     for (Tree node : this) {
/* 1150 */       if ((!node.isLeaf()) && (node.children().length >= 2))
/*      */       {
/*      */ 
/*      */ 
/* 1154 */         Label l = node.label();
/* 1155 */         TaggedWord tw = null;
/* 1156 */         if (((l instanceof HasWord)) && ((l instanceof HasTag))) {
/* 1157 */           tw = new TaggedWord(((HasWord)l).word(), ((HasTag)l).tag());
/*      */         }
/*      */         
/* 1160 */         boolean seenHead = false;
/* 1161 */         for (Tree child : node.children()) {
/* 1162 */           Label dl = child.label();
/* 1163 */           TaggedWord dtw = null;
/* 1164 */           if (((dl instanceof HasWord)) && ((dl instanceof HasTag))) {
/* 1165 */             dtw = new TaggedWord(((HasWord)dl).word(), ((HasTag)dl).tag());
/*      */           }
/* 1167 */           if ((tw != null) && (tw.word() != null) && (dtw != null) && (tw.word().equals(dtw.word())) && (tw.tag().equals(dtw.tag())) && (!seenHead)) {
/* 1168 */             seenHead = true;
/*      */           } else {
/* 1170 */             Dependency p = new UnnamedDependency(tw, dtw);
/* 1171 */             if (f.accept(p))
/* 1172 */               deps.add(p);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1177 */     return deps;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Dependency> taggedDependencies(HeadFinder hf)
/*      */   {
/* 1189 */     return taggedDependencies(Filters.acceptFilter(), hf);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Dependency> taggedDependencies(Filter<Dependency> f, HeadFinder hf)
/*      */   {
/* 1204 */     Set<Dependency> deps = new HashSet();
/* 1205 */     for (Tree node : this) {
/* 1206 */       if ((!node.isLeaf()) && (node.children().length >= 2))
/*      */       {
/*      */ 
/*      */ 
/* 1210 */         Label l = node.label();
/* 1211 */         TaggedWord w = null;
/* 1212 */         if (hf != null) {
/* 1213 */           Tree hwt = node.headPreTerminal(hf);
/* 1214 */           if (hwt != null) {
/* 1215 */             w = new TaggedWord(hwt.children()[0].label(), hwt.label());
/*      */           }
/*      */         }
/* 1218 */         else if (((l instanceof HasWord)) && ((l instanceof HasTag))) {
/* 1219 */           w = new TaggedWord(((HasWord)l).word(), ((HasTag)l).tag());
/*      */         }
/*      */         
/*      */ 
/* 1223 */         boolean seenHead = false;
/* 1224 */         for (Tree child : node.children()) {
/* 1225 */           Label dl = child.label();
/* 1226 */           TaggedWord dw = null;
/* 1227 */           if (hf != null) {
/* 1228 */             Tree dwt = child.headPreTerminal(hf);
/* 1229 */             if (dwt != null) {
/* 1230 */               dw = new TaggedWord(dwt.children()[0].label(), dwt.label());
/*      */             }
/*      */           }
/* 1233 */           else if (((dl instanceof HasWord)) && ((dl instanceof HasTag))) {
/* 1234 */             dw = new TaggedWord(((HasWord)dl).word(), ((HasTag)dl).tag());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1239 */           if ((w != null) && (w.word() != null) && (w.tag() != null) && (dw != null) && (w.word().equals(dw.word())) && (w.tag().equals(dw.tag())) && (!seenHead)) {
/* 1240 */             seenHead = true;
/*      */           } else {
/* 1242 */             Dependency p = new UnnamedDependency(w, dw);
/* 1243 */             if (f.accept(p))
/* 1244 */               deps.add(p);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1249 */     return deps;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Dependency> mapDependencies(Filter<Dependency> f, HeadFinder hf)
/*      */   {
/* 1271 */     Set<Dependency> deps = new HashSet();
/* 1272 */     for (Tree node : this) {
/* 1273 */       if ((!node.isLeaf()) && (node.children().length >= 2))
/*      */       {
/*      */ 
/*      */ 
/* 1277 */         Label l = node.label();
/*      */         
/*      */         MapLabel ml;
/* 1280 */         if (hf != null) {
/* 1281 */           Tree hwt = node.headPreTerminal(hf);
/*      */           
/* 1283 */           if (hwt != null) {
/* 1284 */             MapLabel ml = new MapLabel(hwt.firstChild().label());
/* 1285 */             ml.setWord(hwt.firstChild().label().value());
/* 1286 */             ml.setTag(hwt.label().value());
/*      */           } else {
/* 1288 */             throw new IllegalArgumentException("mapDependencies: headFinder failed!");
/*      */           }
/*      */         }
/* 1291 */         else if (((l instanceof HasWord)) && ((l instanceof HasTag))) {
/* 1292 */           MapLabel ml = new MapLabel(((HasWord)l).word());
/* 1293 */           ml.setWord(((HasWord)l).word());
/* 1294 */           ml.setTag(((HasTag)l).tag());
/*      */         } else {
/* 1296 */           throw new IllegalArgumentException("mapDependencies: no head info in label!");
/*      */         }
/*      */         
/*      */         MapLabel ml;
/*      */         
/* 1301 */         boolean seenHead = false;
/* 1302 */         for (Tree child : node.children()) {
/* 1303 */           Label dl = child.label();
/* 1304 */           MapLabel dml = null;
/* 1305 */           if (hf != null) {
/* 1306 */             Tree dwt = child.headPreTerminal(hf);
/* 1307 */             if (dwt != null) {
/* 1308 */               dml = new MapLabel(dwt.firstChild().label());
/* 1309 */               dml.setWord(dwt.firstChild().label().value());
/* 1310 */               dml.setTag(dwt.label().value());
/*      */             }
/*      */           }
/* 1313 */           else if (((dl instanceof HasWord)) && ((dl instanceof HasTag))) {
/* 1314 */             dml = new MapLabel(((HasWord)dl).word());
/* 1315 */             dml.setWord(((HasWord)dl).word());
/* 1316 */             dml.setTag(((HasTag)dl).tag());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1321 */           if ((ml.value() != null) && (ml.tag() != null) && (dml != null) && (ml.value().equals(dml.value())) && (ml.tag().equals(dml.tag())) && (!seenHead)) {
/* 1322 */             seenHead = true;
/*      */           } else {
/* 1324 */             Dependency p = new UnnamedDependency(ml, dml);
/* 1325 */             if (f.accept(p))
/* 1326 */               deps.add(p);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1331 */     return deps;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <X extends HasWord> Sentence<X> yield()
/*      */   {
/* 1345 */     return yield(new Sentence());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <X extends HasWord> Sentence<X> yield(Sentence<X> y)
/*      */   {
/* 1366 */     if (isLeaf()) {
/* 1367 */       Label lab = label();
/*      */       
/*      */ 
/* 1370 */       if ((lab instanceof HasWord)) {
/* 1371 */         y.add((HasWord)lab);
/*      */       } else {
/* 1373 */         y.add(new Word(lab));
/*      */       }
/*      */     } else {
/* 1376 */       Tree[] kids = children();
/* 1377 */       for (int i = 0; i < kids.length; i++) {
/* 1378 */         kids[i].yield(y);
/*      */       }
/*      */     }
/* 1381 */     return y;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> List<T> yield(List<T> y)
/*      */   {
/* 1400 */     if (isLeaf()) {
/* 1401 */       y.add(label());
/*      */     } else {
/* 1403 */       Tree[] kids = children();
/* 1404 */       for (int i = 0; i < kids.length; i++) {
/* 1405 */         kids[i].yield(y);
/*      */       }
/*      */     }
/* 1408 */     return y;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Sentence<TaggedWord> taggedYield()
/*      */   {
/* 1422 */     return (Sentence)taggedYield(new Sentence());
/*      */   }
/*      */   
/*      */   public List<LabeledWord> labeledYield() {
/* 1426 */     return labeledYield(new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <X extends List<TaggedWord>> X taggedYield(X ty)
/*      */   {
/* 1447 */     Tree[] kids = children();
/*      */     
/* 1449 */     if ((kids.length == 1) && (kids[0].isLeaf())) {
/* 1450 */       ty.add(new TaggedWord(kids[0].label(), label()));
/*      */     } else {
/* 1452 */       for (Tree kid : kids) {
/* 1453 */         kid.taggedYield(ty);
/*      */       }
/*      */     }
/* 1456 */     return ty;
/*      */   }
/*      */   
/*      */   public List<LabeledWord> labeledYield(List<LabeledWord> ty) {
/* 1460 */     Tree[] kids = children();
/*      */     
/* 1462 */     if ((kids.length == 1) && (kids[0].isLeaf())) {
/* 1463 */       ty.add(new LabeledWord(kids[0].label(), label()));
/*      */     } else {
/* 1465 */       for (Tree kid : kids) {
/* 1466 */         kid.labeledYield(ty);
/*      */       }
/*      */     }
/* 1469 */     return ty;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Label> preTerminalYield()
/*      */   {
/* 1481 */     return preTerminalYield(new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Label> preTerminalYield(List<Label> y)
/*      */   {
/* 1497 */     if (isPreTerminal()) {
/* 1498 */       y.add(label());
/*      */     } else {
/* 1500 */       Tree[] kids = children();
/* 1501 */       for (int i = 0; i < kids.length; i++) {
/* 1502 */         kids[i].preTerminalYield(y);
/*      */       }
/*      */     }
/* 1505 */     return y;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Tree> getLeaves()
/*      */   {
/* 1516 */     return getLeaves(new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Tree> getLeaves(List<Tree> list)
/*      */   {
/* 1528 */     if (isLeaf()) {
/* 1529 */       list.add(this);
/*      */     } else {
/* 1531 */       Tree[] kids = children();
/* 1532 */       for (int i = 0; i < kids.length; i++) {
/* 1533 */         kids[i].getLeaves(list);
/*      */       }
/*      */     }
/* 1536 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<Label> labels()
/*      */   {
/* 1548 */     Set<Label> n = new HashSet();
/* 1549 */     n.add(label());
/* 1550 */     Tree[] kids = children();
/* 1551 */     for (int i = 0; i < kids.length; i++) {
/* 1552 */       n.addAll(kids[i].labels());
/*      */     }
/* 1554 */     return n;
/*      */   }
/*      */   
/*      */   public void setLabels(Collection c)
/*      */   {
/* 1559 */     throw new UnsupportedOperationException("Can't set Tree labels");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree flatten()
/*      */   {
/* 1577 */     return flatten(treeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree flatten(TreeFactory tf)
/*      */   {
/* 1597 */     if ((isLeaf()) || (isPreTerminal())) {
/* 1598 */       return this;
/*      */     }
/* 1600 */     Tree[] kids = children();
/* 1601 */     List<Tree> newChildren = new ArrayList(kids.length);
/* 1602 */     for (int c = 0; c < kids.length; c++) {
/* 1603 */       Tree child = kids[c];
/* 1604 */       if ((child.isLeaf()) || (child.isPreTerminal())) {
/* 1605 */         newChildren.add(child);
/*      */       } else {
/* 1607 */         Tree newChild = child.flatten(tf);
/* 1608 */         if (label().equals(newChild.label())) {
/* 1609 */           newChildren.addAll(newChild.getChildrenAsList());
/*      */         } else {
/* 1611 */           newChildren.add(newChild);
/*      */         }
/*      */       }
/*      */     }
/* 1615 */     return tf.newTreeNode(label(), newChildren);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<Tree> subTrees()
/*      */   {
/* 1632 */     return (Set)subTrees(new HashSet());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Tree> subTreeList()
/*      */   {
/* 1648 */     return (List)subTrees(new ArrayList());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Collection<Tree> subTrees(Collection<Tree> n)
/*      */   {
/* 1665 */     n.add(this);
/* 1666 */     Tree[] kids = children();
/* 1667 */     for (int i = 0; i < kids.length; i++) {
/* 1668 */       kids[i].subTrees(n);
/*      */     }
/* 1670 */     return n;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree deeperCopy()
/*      */   {
/* 1682 */     return deeperCopy(treeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree deeperCopy(TreeFactory tf)
/*      */   {
/* 1699 */     return deeperCopy(tf, label().labelFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree deeperCopy(TreeFactory tf, LabelFactory lf)
/*      */   {
/* 1716 */     Label label = lf.newLabel(label());
/* 1717 */     if (isLeaf()) {
/* 1718 */       return tf.newLeaf(label);
/*      */     }
/* 1720 */     Tree[] kids = children();
/* 1721 */     List<Tree> newKids = new ArrayList(kids.length);
/* 1722 */     int i = 0; for (int n = kids.length; i < n; i++) {
/* 1723 */       newKids.add(kids[i].deeperCopy(tf, lf));
/*      */     }
/* 1725 */     return tf.newTreeNode(label, newKids);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree deepCopy()
/*      */   {
/* 1739 */     return deepCopy(treeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree deepCopy(TreeFactory tf)
/*      */   {
/*      */     Tree t;
/*      */     
/*      */ 
/*      */ 
/*      */     Tree t;
/*      */     
/*      */ 
/*      */ 
/* 1755 */     if (isLeaf()) {
/* 1756 */       t = tf.newLeaf(label());
/*      */     } else {
/* 1758 */       Tree[] kids = children();
/* 1759 */       List<Tree> newKids = new ArrayList(kids.length);
/* 1760 */       int i = 0; for (int n = kids.length; i < n; i++) {
/* 1761 */         newKids.add(kids[i].deepCopy(tf));
/*      */       }
/* 1763 */       t = tf.newTreeNode(label(), newKids);
/*      */     }
/* 1765 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree transform(TreeTransformer transformer)
/*      */   {
/* 1780 */     return transform(transformer, treeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree transform(TreeTransformer transformer, TreeFactory tf)
/*      */   {
/*      */     Tree t;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     Tree t;
/*      */     
/*      */ 
/*      */ 
/* 1798 */     if (isLeaf()) {
/* 1799 */       t = tf.newLeaf(label());
/*      */     } else {
/* 1801 */       Tree[] kids = children();
/* 1802 */       List<Tree> newKids = new ArrayList(kids.length);
/* 1803 */       for (int i = 0; i < kids.length; i++) {
/* 1804 */         newKids.add(kids[i].transform(transformer, tf));
/*      */       }
/* 1806 */       t = tf.newTreeNode(label(), newKids);
/*      */     }
/* 1808 */     return transformer.transformTree(t);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree spliceOut(Filter nodeFilter)
/*      */   {
/* 1822 */     return spliceOut(nodeFilter, treeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree spliceOut(Filter nodeFilter, TreeFactory tf)
/*      */   {
/* 1844 */     List<Tree> l = spliceOutHelper(nodeFilter, tf);
/* 1845 */     if (l.isEmpty())
/* 1846 */       return null;
/* 1847 */     if (l.size() == 1) {
/* 1848 */       return (Tree)l.get(0);
/*      */     }
/*      */     
/* 1851 */     return tf.newTreeNode((Label)null, l);
/*      */   }
/*      */   
/*      */ 
/*      */   private List<Tree> spliceOutHelper(Filter nodeFilter, TreeFactory tf)
/*      */   {
/* 1857 */     Tree[] kids = children();
/* 1858 */     List<Tree> l = new ArrayList();
/* 1859 */     for (int i = 0; i < kids.length; i++) {
/* 1860 */       l.addAll(kids[i].spliceOutHelper(nodeFilter, tf));
/*      */     }
/*      */     
/* 1863 */     if (nodeFilter.accept(this)) {
/*      */       Tree t;
/*      */       Tree t;
/* 1866 */       if (l.size() != 0) {
/* 1867 */         t = tf.newTreeNode(label(), l);
/*      */       } else {
/* 1869 */         t = tf.newLeaf(label());
/*      */       }
/* 1871 */       l = new ArrayList(1);
/* 1872 */       l.add(t);
/* 1873 */       return l;
/*      */     }
/*      */     
/* 1876 */     return l;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree prune(Filter filter)
/*      */   {
/* 1902 */     return prune(filter, treeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree prune(Filter filter, TreeFactory tf)
/*      */   {
/* 1920 */     if (!filter.accept(this)) {
/* 1921 */       return null;
/*      */     }
/*      */     
/* 1924 */     List<Tree> l = new ArrayList();
/* 1925 */     Tree[] kids = children();
/* 1926 */     for (int i = 0; i < kids.length; i++) {
/* 1927 */       Tree prunedChild = kids[i].prune(filter, tf);
/* 1928 */       if (prunedChild != null) {
/* 1929 */         l.add(prunedChild);
/*      */       }
/*      */     }
/*      */     
/* 1933 */     if ((l.isEmpty()) && (kids.length != 0)) {
/* 1934 */       return null;
/*      */     }
/*      */     
/* 1937 */     if (isLeaf()) {
/* 1938 */       return tf.newLeaf(label());
/*      */     }
/* 1940 */     return tf.newTreeNode(label(), l);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract TreeFactory treeFactory();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree parent()
/*      */   {
/* 1964 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree parent(Tree root)
/*      */   {
/* 1981 */     Tree[] kids = root.children();
/* 1982 */     return parentHelper(root, kids, this);
/*      */   }
/*      */   
/*      */   private static Tree parentHelper(Tree parent, Tree[] kids, Tree node)
/*      */   {
/* 1987 */     int i = 0; for (int n = kids.length; i < n; i++) {
/* 1988 */       if (kids[i] == node) {
/* 1989 */         return parent;
/*      */       }
/* 1991 */       Tree ret = node.parent(kids[i]);
/* 1992 */       if (ret != null) {
/* 1993 */         return ret;
/*      */       }
/*      */     }
/* 1996 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int size()
/*      */   {
/* 2011 */     int size = 1;
/* 2012 */     Tree[] kids = children();
/* 2013 */     int i = 0; for (int n = kids.length; i < n; i++) {
/* 2014 */       size += kids[i].size();
/*      */     }
/* 2016 */     return size;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree ancestor(int height, Tree root)
/*      */   {
/* 2030 */     if (height < 0) {
/* 2031 */       throw new IllegalArgumentException("ancestor: height cannot be negative");
/*      */     }
/* 2033 */     if (height == 0) {
/* 2034 */       return this;
/*      */     }
/* 2036 */     Tree par = parent(root);
/* 2037 */     if (par == null) {
/* 2038 */       return null;
/*      */     }
/* 2040 */     return par.ancestor(height - 1, root);
/*      */   }
/*      */   
/*      */   private static class TreeIterator implements Iterator<Tree>
/*      */   {
/*      */     private List<Tree> treeStack;
/*      */     
/*      */     private TreeIterator(Tree t) {
/* 2048 */       this.treeStack = new ArrayList();
/* 2049 */       this.treeStack.add(t);
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 2053 */       return !this.treeStack.isEmpty();
/*      */     }
/*      */     
/*      */     public Tree next() {
/* 2057 */       int lastIndex = this.treeStack.size() - 1;
/* 2058 */       Tree tr = (Tree)this.treeStack.remove(lastIndex);
/* 2059 */       Tree[] kids = tr.children();
/*      */       
/* 2061 */       for (int i = kids.length - 1; i >= 0; i--) {
/* 2062 */         this.treeStack.add(kids[i]);
/*      */       }
/* 2064 */       return tr;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void remove()
/*      */     {
/* 2071 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public String toString() {
/* 2075 */       return "TreeIterator";
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<Tree> iterator()
/*      */   {
/* 2092 */     return new TreeIterator(this, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Tree valueOf(String str)
/*      */     throws IOException
/*      */   {
/* 2109 */     return valueOf(str, new StringLabeledScoredTreeReaderFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Tree valueOf(String str, TreeReaderFactory trf)
/*      */     throws IOException
/*      */   {
/* 2124 */     return trf.newTreeReader(new StringReader(str)).readTree();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree getChild(int i)
/*      */   {
/* 2135 */     Tree[] kids = children();
/* 2136 */     return kids[i];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree removeChild(int i)
/*      */   {
/* 2149 */     Tree[] kids = children();
/* 2150 */     Tree kid = kids[i];
/* 2151 */     Tree[] newKids = new Tree[kids.length - 1];
/* 2152 */     for (int j = 0; j < newKids.length; j++) {
/* 2153 */       if (j < i) {
/* 2154 */         newKids[j] = kids[j];
/*      */       } else {
/* 2156 */         newKids[j] = kids[(j + 1)];
/*      */       }
/*      */     }
/* 2159 */     setChildren(newKids);
/* 2160 */     return kid;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addChild(int i, Tree t)
/*      */   {
/* 2172 */     Tree[] kids = children();
/* 2173 */     Tree[] newKids = new Tree[kids.length + 1];
/* 2174 */     System.arraycopy(kids, 0, newKids, 0, i);
/* 2175 */     newKids[i] = t;
/* 2176 */     System.arraycopy(kids, i, newKids, i + 1, kids.length - i);
/* 2177 */     setChildren(newKids);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addChild(Tree t)
/*      */   {
/* 2186 */     addChild(children().length, t);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree setChild(int i, Tree t)
/*      */   {
/* 2200 */     Tree[] kids = children();
/* 2201 */     Tree old = kids[i];
/* 2202 */     kids[i] = t;
/* 2203 */     return old;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean dominates(Tree t)
/*      */   {
/* 2212 */     return dominationPath(t) != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Tree> dominationPath(Tree t)
/*      */   {
/* 2222 */     Tree[] result = dominationPathHelper(t, 0);
/* 2223 */     if (result == null) {
/* 2224 */       return null;
/*      */     }
/* 2226 */     return Arrays.asList(result);
/*      */   }
/*      */   
/*      */ 
/*      */   private Tree[] dominationPathHelper(Tree t, int depth)
/*      */   {
/* 2232 */     Tree[] kids = children();
/* 2233 */     for (int i = kids.length - 1; i >= 0; i--) {
/* 2234 */       Tree t1 = kids[i];
/* 2235 */       if (t1 == null)
/* 2236 */         return null;
/*      */       Tree[] result;
/* 2238 */       if ((result = t1.dominationPath(t, depth + 1)) != null) {
/* 2239 */         result[depth] = this;
/* 2240 */         return result;
/*      */       }
/*      */     }
/* 2243 */     return null;
/*      */   }
/*      */   
/*      */   private Tree[] dominationPath(Tree t, int depth) {
/* 2247 */     if (this == t) {
/* 2248 */       Tree[] result = new Tree[depth + 1];
/* 2249 */       result[depth] = this;
/* 2250 */       return result;
/*      */     }
/* 2252 */     return dominationPathHelper(t, depth);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Tree> pathNodeToNode(Tree t1, Tree t2)
/*      */   {
/* 2262 */     if ((!contains(t1)) || (!contains(t2))) {
/* 2263 */       return null;
/*      */     }
/* 2265 */     if (t1 == t2) {
/* 2266 */       return Collections.singletonList(t1);
/*      */     }
/* 2268 */     if (this == t1) {
/* 2269 */       return dominationPath(t2);
/*      */     }
/* 2271 */     if (this == t2) {
/* 2272 */       List<Tree> path = dominationPath(t1);
/* 2273 */       Collections.reverse(path);
/* 2274 */       return path;
/*      */     }
/* 2276 */     Tree joinNode = joinNode(t1, t2);
/* 2277 */     if (joinNode == null) {
/* 2278 */       return null;
/*      */     }
/* 2280 */     List<Tree> t1DomPath = joinNode.dominationPath(t1);
/* 2281 */     List<Tree> t2DomPath = joinNode.dominationPath(t2);
/* 2282 */     if ((t1DomPath == null) || (t2DomPath == null)) {
/* 2283 */       return null;
/*      */     }
/* 2285 */     ArrayList<Tree> path = new ArrayList();
/* 2286 */     path.addAll(t1DomPath);
/* 2287 */     Collections.reverse(path);
/* 2288 */     path.remove(joinNode);
/* 2289 */     path.addAll(t2DomPath);
/* 2290 */     return path;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree joinNode(Tree t1, Tree t2)
/*      */   {
/* 2304 */     if ((!contains(t1)) || (!contains(t2))) {
/* 2305 */       return null;
/*      */     }
/* 2307 */     if ((this == t1) || (this == t2)) {
/* 2308 */       return this;
/*      */     }
/* 2310 */     Tree joinNode = null;
/* 2311 */     List<Tree> t1DomPath = dominationPath(t1);
/* 2312 */     List<Tree> t2DomPath = dominationPath(t2);
/* 2313 */     if ((t1DomPath == null) || (t2DomPath == null)) {
/* 2314 */       return null;
/*      */     }
/* 2316 */     Iterator<Tree> it1 = t1DomPath.iterator();
/* 2317 */     Iterator<Tree> it2 = t2DomPath.iterator();
/* 2318 */     while ((it1.hasNext()) && (it2.hasNext())) {
/* 2319 */       Tree n1 = (Tree)it1.next();
/* 2320 */       Tree n2 = (Tree)it2.next();
/* 2321 */       if (n1 != n2) {
/*      */         break;
/*      */       }
/* 2324 */       joinNode = n1;
/*      */     }
/* 2326 */     return joinNode;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean cCommands(Tree t1, Tree t2)
/*      */   {
/* 2336 */     List<Tree> sibs = t1.siblings(this);
/* 2337 */     if (sibs == null) {
/* 2338 */       return false;
/*      */     }
/* 2340 */     for (Tree sib : sibs) {
/* 2341 */       if ((sib == t2) || (sib.contains(t2))) {
/* 2342 */         return true;
/*      */       }
/*      */     }
/* 2345 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<Tree> siblings(Tree root)
/*      */   {
/* 2358 */     Tree parent = parent(root);
/* 2359 */     if (parent == null) {
/* 2360 */       return null;
/*      */     }
/* 2362 */     List<Tree> siblings = parent.getChildrenAsList();
/* 2363 */     siblings.remove(this);
/* 2364 */     return siblings;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void insertDtr(Tree dtr, int position)
/*      */   {
/* 2372 */     Tree[] kids = children();
/* 2373 */     if (position > kids.length) {
/* 2374 */       throw new IllegalArgumentException("Can't insert tree after the " + position + "th daughter in " + this + "; only " + kids.length + " daughters exist!");
/*      */     }
/* 2376 */     Tree[] newKids = new Tree[kids.length + 1];
/* 2377 */     for (int i = 0; 
/* 2378 */         i < position; i++) {
/* 2379 */       newKids[i] = kids[i];
/*      */     }
/* 2381 */     newKids[i] = dtr;
/* 2382 */     for (; i < kids.length; i++) {
/* 2383 */       newKids[(i + 1)] = kids[i];
/*      */     }
/* 2385 */     setChildren(newKids);
/*      */   }
/*      */   
/*      */ 
/*      */   public String value()
/*      */   {
/* 2391 */     Label lab = label();
/* 2392 */     if (lab == null) {
/* 2393 */       return null;
/*      */     }
/* 2395 */     return lab.value();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setValue(String value)
/*      */   {
/* 2401 */     Label lab = label();
/* 2402 */     if (lab != null) {
/* 2403 */       lab.setValue(value);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setFromString(String labelStr)
/*      */   {
/* 2409 */     Label lab = label();
/* 2410 */     if (lab != null) {
/* 2411 */       lab.setFromString(labelStr);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LabelFactory labelFactory()
/*      */   {
/* 2422 */     Label lab = label();
/* 2423 */     if (lab == null) {
/* 2424 */       return null;
/*      */     }
/* 2426 */     return lab.labelFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int leftCharEdge(Tree node)
/*      */   {
/* 2435 */     MutableInteger i = new MutableInteger(0);
/* 2436 */     if (leftCharEdge(node, i)) {
/* 2437 */       return i.intValue();
/*      */     }
/* 2439 */     return -1;
/*      */   }
/*      */   
/*      */   private boolean leftCharEdge(Tree node, MutableInteger i)
/*      */   {
/* 2444 */     if (this == node)
/* 2445 */       return true;
/* 2446 */     if (isLeaf()) {
/* 2447 */       i.set(i.intValue() + value().length());
/* 2448 */       return false;
/*      */     }
/* 2450 */     for (Tree child : children()) {
/* 2451 */       if (child.leftCharEdge(node, i)) {
/* 2452 */         return true;
/*      */       }
/*      */     }
/* 2455 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int rightCharEdge(Tree node)
/*      */   {
/* 2464 */     List s = yield();
/* 2465 */     int length = 0;
/* 2466 */     for (Object leaf : s) {
/* 2467 */       length += ((Label)leaf).value().length();
/*      */     }
/* 2469 */     MutableInteger i = new MutableInteger(length);
/* 2470 */     if (rightCharEdge(node, i)) {
/* 2471 */       return i.intValue();
/*      */     }
/* 2473 */     return -1;
/*      */   }
/*      */   
/*      */   private boolean rightCharEdge(Tree node, MutableInteger i)
/*      */   {
/* 2478 */     if (this == node)
/* 2479 */       return true;
/* 2480 */     if (isLeaf()) {
/* 2481 */       i.set(i.intValue() - label().value().length());
/* 2482 */       return false;
/*      */     }
/* 2484 */     for (int j = children().length - 1; j >= 0; j--) {
/* 2485 */       if (children()[j].rightCharEdge(node, i)) {
/* 2486 */         return true;
/*      */       }
/*      */     }
/* 2489 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int nodeNumber(Tree root)
/*      */   {
/* 2500 */     MutableInteger i = new MutableInteger(1);
/* 2501 */     if (nodeNumberHelper(root, i)) {
/* 2502 */       return i.intValue();
/*      */     }
/* 2504 */     return -1;
/*      */   }
/*      */   
/*      */   private boolean nodeNumberHelper(Tree t, MutableInteger i) {
/* 2508 */     if (this == t)
/* 2509 */       return true;
/* 2510 */     i.incValue(1);
/* 2511 */     for (int j = 0; j < t.children().length; j++) {
/* 2512 */       if (nodeNumberHelper(t.children()[j], i))
/* 2513 */         return true;
/*      */     }
/* 2515 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree getNodeNumber(int i)
/*      */   {
/* 2528 */     return getNodeNumberHelper(new MutableInteger(1), i);
/*      */   }
/*      */   
/*      */   private Tree getNodeNumberHelper(MutableInteger i, int target) {
/* 2532 */     int i1 = i.intValue();
/* 2533 */     if (i1 == target)
/* 2534 */       return this;
/* 2535 */     if (i1 > target)
/* 2536 */       throw new IndexOutOfBoundsException("Error -- tree does not contain " + i + " nodes.");
/* 2537 */     i.incValue(1);
/* 2538 */     for (int j = 0; j < children().length; j++) {
/* 2539 */       Tree temp = children()[j].getNodeNumberHelper(i, target);
/* 2540 */       if (temp != null)
/* 2541 */         return temp;
/*      */     }
/* 2543 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void indexLeaves()
/*      */   {
/* 2554 */     indexLeaves(1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int indexLeaves(int startIndex)
/*      */   {
/* 2569 */     if (isLeaf()) {
/* 2570 */       AbstractMapLabel afl = (AbstractMapLabel)label();
/* 2571 */       int oldIndex = afl.index();
/* 2572 */       if (oldIndex >= 0) {
/* 2573 */         startIndex = oldIndex;
/*      */       } else {
/* 2575 */         afl.setIndex(startIndex);
/*      */       }
/* 2577 */       startIndex++;
/*      */     } else {
/* 2579 */       Tree[] kids = children();
/* 2580 */       for (int i = 0; i < kids.length; i++) {
/* 2581 */         startIndex = kids[i].indexLeaves(startIndex);
/*      */       }
/*      */     }
/* 2584 */     return startIndex;
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\Tree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */