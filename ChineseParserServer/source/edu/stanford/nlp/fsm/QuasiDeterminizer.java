/*     */ package edu.stanford.nlp.fsm;
/*     */ 
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class QuasiDeterminizer
/*     */   implements TransducerGraph.GraphProcessor
/*     */ {
/*     */   public TransducerGraph processGraph(TransducerGraph graph)
/*     */   {
/*  19 */     Counter lambda = computeLambda(graph);
/*     */     
/*  21 */     TransducerGraph result = pushLambdas(graph, lambda);
/*  22 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Counter computeLambda(TransducerGraph graph)
/*     */   {
/*  29 */     LinkedList queue = new LinkedList();
/*  30 */     Counter lambda = new Counter();
/*  31 */     Counter length = new Counter();
/*  32 */     Map first = new HashMap();
/*  33 */     Set nodes = graph.getNodes();
/*  34 */     for (Iterator nodeIter = nodes.iterator(); nodeIter.hasNext();) {
/*  35 */       Object node = nodeIter.next();
/*  36 */       lambda.setCount(node, 0.0D);
/*  37 */       length.setCount(node, Double.POSITIVE_INFINITY);
/*     */     }
/*  39 */     Set endNodes = graph.getEndNodes();
/*  40 */     for (Iterator endIter = endNodes.iterator(); endIter.hasNext();) {
/*  41 */       Object o = endIter.next();
/*  42 */       lambda.setCount(o, 0.0D);
/*  43 */       length.setCount(o, 0.0D);
/*  44 */       queue.addLast(o);
/*     */     }
/*     */     
/*     */ 
/*  48 */     Object node = null;
/*     */     try {
/*  50 */       node = queue.removeFirst();
/*     */     }
/*     */     catch (NoSuchElementException e) {}
/*  53 */     while (node != null) {
/*  54 */       double oldLen = length.getCount(node);
/*  55 */       Set arcs = graph.getArcsByTarget(node);
/*  56 */       Iterator arcIter; if (arcs != null) {
/*  57 */         for (arcIter = arcs.iterator(); arcIter.hasNext();) {
/*  58 */           TransducerGraph.Arc arc = (TransducerGraph.Arc)arcIter.next();
/*  59 */           Object newNode = arc.getSourceNode();
/*  60 */           Comparable a = (Comparable)arc.getInput();
/*  61 */           double k = ((Double)arc.getOutput()).doubleValue();
/*  62 */           double newLen = length.getCount(newNode);
/*  63 */           if (newLen == Double.POSITIVE_INFINITY)
/*     */           {
/*  65 */             queue.addLast(newNode);
/*     */           }
/*  67 */           Comparable f = (Comparable)first.get(newNode);
/*  68 */           if ((newLen == Double.POSITIVE_INFINITY) || ((newLen == oldLen + 1.0D) && (a.compareTo(f) < 0)))
/*     */           {
/*  70 */             first.put(newNode, a);
/*  71 */             length.setCount(newNode, oldLen + 1.0D);
/*  72 */             lambda.setCount(newNode, k + lambda.getCount(node));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  77 */       node = null;
/*     */       try {
/*  79 */         node = queue.removeFirst();
/*     */       }
/*     */       catch (NoSuchElementException e) {}
/*     */     }
/*  83 */     return lambda;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TransducerGraph pushLambdas(TransducerGraph graph, Counter lambda)
/*     */   {
/*  90 */     TransducerGraph result = (TransducerGraph)graph.clone();
/*  91 */     Set arcs = result.getArcs();
/*  92 */     for (Iterator arcIter = arcs.iterator(); arcIter.hasNext();) {
/*  93 */       TransducerGraph.Arc arc = (TransducerGraph.Arc)arcIter.next();
/*  94 */       double sourceLambda = lambda.getCount(arc.getSourceNode());
/*  95 */       double targetLambda = lambda.getCount(arc.getTargetNode());
/*  96 */       double oldOutput = ((Double)arc.getOutput()).doubleValue();
/*  97 */       double newOutput = oldOutput + targetLambda - sourceLambda;
/*  98 */       arc.setOutput(new Double(newOutput));
/*     */     }
/*     */     
/* 101 */     double startLambda = lambda.getCount(result.getStartNode());
/* 102 */     Iterator arcIter; if (startLambda != 0.0D)
/*     */     {
/* 104 */       Set startArcs = result.getArcsBySource(result.getStartNode());
/* 105 */       for (arcIter = startArcs.iterator(); arcIter.hasNext();) {
/* 106 */         TransducerGraph.Arc arc = (TransducerGraph.Arc)arcIter.next();
/* 107 */         double oldOutput = ((Double)arc.getOutput()).doubleValue();
/* 108 */         double newOutput = oldOutput + startLambda;
/* 109 */         arc.setOutput(new Double(newOutput));
/*     */       }
/*     */     }
/*     */     
/* 113 */     for (Iterator endIter = result.getEndNodes().iterator(); endIter.hasNext();) {
/* 114 */       Object o = endIter.next();
/* 115 */       endLambda = lambda.getCount(o);
/* 116 */       if (endLambda != 0.0D)
/*     */       {
/* 118 */         Set endArcs = result.getArcsByTarget(o);
/* 119 */         for (arcIter = endArcs.iterator(); arcIter.hasNext();) {
/* 120 */           TransducerGraph.Arc arc = (TransducerGraph.Arc)arcIter.next();
/* 121 */           double oldOutput = ((Double)arc.getOutput()).doubleValue();
/* 122 */           double newOutput = oldOutput - endLambda;
/* 123 */           arc.setOutput(new Double(newOutput));
/*     */         }
/*     */       } }
/*     */     double endLambda;
/*     */     Iterator arcIter;
/* 128 */     return result;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 132 */     TransducerGraph.GraphProcessor qd = new QuasiDeterminizer();
/* 133 */     List pathList = new ArrayList();
/* 134 */     TransducerGraph graph = TransducerGraph.createRandomGraph(1000, 10, 1.0D, 10, pathList);
/* 135 */     StringBuffer b = new StringBuffer();
/* 136 */     graph.depthFirstSearch(true, b);
/* 137 */     System.out.println(b.toString());
/* 138 */     System.out.println("Done creating random graph");
/*     */     
/*     */ 
/*     */ 
/* 142 */     TransducerGraph newGraph = qd.processGraph(graph);
/* 143 */     System.out.println("Done quasi-determinizing");
/*     */     
/*     */ 
/*     */ 
/* 147 */     TransducerGraph.testGraphPaths(graph, newGraph, 1000);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\fsm\QuasiDeterminizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */