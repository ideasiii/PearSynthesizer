/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class TreeGraph
/*     */   implements Serializable
/*     */ {
/*     */   protected TreeGraphNode root;
/*  31 */   private Map<Integer, TreeGraphNode> indexMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeGraph(Tree t)
/*     */   {
/*  43 */     this.root = new TreeGraphNode(t, (TreeGraphNode)null);
/*  44 */     this.root.setTreeGraph(this);
/*  45 */     this.root.indexNodes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeGraphNode root()
/*     */   {
/*  54 */     return this.root;
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
/*     */   public void addNodeToIndexMap(int index, TreeGraphNode node)
/*     */   {
/*  67 */     this.indexMap.put(new Integer(index), node);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeGraphNode getNodeByIndex(int index)
/*     */   {
/*  79 */     return (TreeGraphNode)this.indexMap.get(new Integer(index));
/*     */   }
/*     */   
/*     */   public Collection<TreeGraphNode> getNodes() {
/*  83 */     return this.indexMap.values();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  95 */     return this.root.toPrettyString(0).substring(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     Tree t;
/*     */     
/*     */     try
/*     */     {
/* 106 */       t = Tree.valueOf("(S (NP (NNP Sam)) (VP (VBD died) (NP-TMP (NN today))))");
/*     */     } catch (Exception e) {
/* 108 */       System.err.println("Horrible error: " + e);
/* 109 */       e.printStackTrace();
/* 110 */       return;
/*     */     }
/*     */     
/* 113 */     t.pennPrint();
/*     */     
/* 115 */     System.out.println("----------------------------");
/* 116 */     TreeGraph tg = new TreeGraph(t);
/* 117 */     System.out.println(tg);
/*     */     
/* 119 */     tg.root.percolateHeads(new SemanticHeadFinder());
/* 120 */     System.out.println("----------------------------");
/* 121 */     System.out.println(tg);
/*     */     
/* 123 */     TreeGraphNode node1 = tg.getNodeByIndex(1);
/* 124 */     TreeGraphNode node4 = tg.getNodeByIndex(4);
/* 125 */     node1.addArc("1to4", node4);
/* 126 */     node1.addArc("1TO4", node4);
/* 127 */     node4.addArc("4to1", node1);
/* 128 */     System.out.println("----------------------------");
/* 129 */     System.out.println("arcs from 1 to 4: " + node1.arcLabelsToNode(node4));
/* 130 */     System.out.println("arcs from 4 to 1: " + node4.arcLabelsToNode(node1));
/* 131 */     System.out.println("arcs from 0 to 4: " + tg.root.arcLabelsToNode(node4));
/* 132 */     for (int i = 0; i <= 9; i++) {
/* 133 */       System.out.println("parent of " + i + ": " + tg.getNodeByIndex(i).parent());
/* 134 */       System.out.println("highest node with same head as " + i + ": " + tg.getNodeByIndex(i).highestNodeWithSameHead());
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreeGraph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */