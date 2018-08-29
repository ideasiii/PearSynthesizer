/*     */ package edu.stanford.nlp.fsm;
/*     */ 
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.Maps;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TransducerGraph implements Cloneable
/*     */ {
/*  22 */   public static final Object EPSILON_INPUT = "EPSILON";
/*     */   
/*  24 */   private static final Object DEFAULT_START_NODE = "START";
/*     */   
/*  26 */   private static Random r = new Random();
/*     */   
/*     */   private Set arcs;
/*     */   
/*     */   private Map arcsBySource;
/*     */   private Map arcsByTarget;
/*     */   private Map arcsByInput;
/*     */   private Map arcsBySourceAndInput;
/*     */   private Map arcsByTargetAndInput;
/*     */   private Object startNode;
/*     */   private Set endNodes;
/*  37 */   private boolean checkDeterminism = false;
/*     */   
/*     */   public void setDeterminism(boolean checkDeterminism) {
/*  40 */     this.checkDeterminism = checkDeterminism;
/*     */   }
/*     */   
/*     */   public TransducerGraph() {
/*  44 */     this.arcs = new HashSet();
/*  45 */     this.arcsBySource = new HashMap();
/*  46 */     this.arcsByTarget = new HashMap();
/*  47 */     this.arcsByInput = new HashMap();
/*  48 */     this.arcsBySourceAndInput = new HashMap();
/*  49 */     this.arcsByTargetAndInput = new HashMap();
/*  50 */     this.endNodes = new HashSet();
/*  51 */     setStartNode(DEFAULT_START_NODE);
/*     */   }
/*     */   
/*     */   public TransducerGraph(TransducerGraph other) {
/*  55 */     this(other, (ArcProcessor)null);
/*     */   }
/*     */   
/*     */   public TransducerGraph(TransducerGraph other, ArcProcessor arcProcessor) {
/*  59 */     this(other.getArcs(), other.getStartNode(), other.getEndNodes(), arcProcessor, null);
/*     */   }
/*     */   
/*     */   public TransducerGraph(TransducerGraph other, NodeProcessor nodeProcessor) {
/*  63 */     this(other.getArcs(), other.getStartNode(), other.getEndNodes(), null, nodeProcessor);
/*     */   }
/*     */   
/*     */   public TransducerGraph(Set newArcs, Object startNode, Set endNodes, ArcProcessor arcProcessor, NodeProcessor nodeProcessor) {
/*  67 */     this();
/*  68 */     ArcProcessor arcProcessor2 = null;
/*  69 */     if (nodeProcessor != null) {
/*  70 */       arcProcessor2 = new NodeProcessorWrappingArcProcessor(nodeProcessor);
/*     */     }
/*  72 */     for (Iterator iter = newArcs.iterator(); iter.hasNext();) {
/*  73 */       Arc a = (Arc)iter.next();
/*  74 */       a = new Arc(a);
/*  75 */       if (arcProcessor != null) {
/*  76 */         a = arcProcessor.processArc(a);
/*     */       }
/*  78 */       if (arcProcessor2 != null) {
/*  79 */         a = arcProcessor2.processArc(a);
/*     */       }
/*  81 */       addArc(a);
/*     */     }
/*  83 */     if (nodeProcessor != null) {
/*  84 */       this.startNode = nodeProcessor.processNode(startNode);
/*     */     } else
/*  86 */       this.startNode = startNode;
/*     */     Iterator endIter;
/*  88 */     if (nodeProcessor != null) {
/*  89 */       if (endNodes != null) {
/*  90 */         for (endIter = endNodes.iterator(); endIter.hasNext();) {
/*  91 */           Object o = endIter.next();
/*  92 */           this.endNodes.add(nodeProcessor.processNode(o));
/*     */         }
/*     */       }
/*     */     }
/*  96 */     else if (endNodes != null) {
/*  97 */       this.endNodes.addAll(endNodes);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TransducerGraph(Set newArcs)
/*     */   {
/* 106 */     this(newArcs, null, null, null, null);
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 110 */     TransducerGraph result = new TransducerGraph(this, (ArcProcessor)null);
/* 111 */     return result;
/*     */   }
/*     */   
/*     */   public Set getArcs() {
/* 115 */     return this.arcs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set getNodes()
/*     */   {
/* 122 */     Set result = new HashSet();
/* 123 */     result.addAll(this.arcsBySource.keySet());
/* 124 */     result.addAll(this.arcsByTarget.keySet());
/* 125 */     return result;
/*     */   }
/*     */   
/*     */   public Set getInputs() {
/* 129 */     return this.arcsByInput.keySet();
/*     */   }
/*     */   
/*     */   public void setStartNode(Object o) {
/* 133 */     this.startNode = o;
/*     */   }
/*     */   
/*     */   public void setEndNode(Object o)
/*     */   {
/* 138 */     this.endNodes.add(o);
/*     */   }
/*     */   
/*     */   public Object getStartNode() {
/* 142 */     return this.startNode;
/*     */   }
/*     */   
/*     */   public Set getEndNodes()
/*     */   {
/* 147 */     return this.endNodes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set getArcsByInput(Object node)
/*     */   {
/* 154 */     return ensure(this.arcsByInput.get(node));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set getArcsBySource(Object node)
/*     */   {
/* 161 */     return ensure(this.arcsBySource.get(node));
/*     */   }
/*     */   
/*     */   protected Set ensure(Object o) {
/* 165 */     Set s = (Set)o;
/* 166 */     if (s == null) {
/* 167 */       return Collections.EMPTY_SET;
/*     */     }
/* 169 */     return s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set getArcsByTarget(Object node)
/*     */   {
/* 176 */     return ensure(this.arcsByTarget.get(node));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Arc getArcBySourceAndInput(Object node, Object input)
/*     */   {
/* 183 */     return (Arc)this.arcsBySourceAndInput.get(new Pair(node, input));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set getArcsByTargetAndInput(Object node, Object input)
/*     */   {
/* 190 */     return ensure(this.arcsByTargetAndInput.get(new Pair(node, input)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Arc getArc(Object source, Object target)
/*     */   {
/* 197 */     Set arcsFromSource = (Set)this.arcsBySource.get(source);
/* 198 */     Set arcsToTarget = (Set)this.arcsByTarget.get(target);
/* 199 */     HashSet result = new HashSet();
/* 200 */     result.addAll(arcsFromSource);
/* 201 */     result.retainAll(arcsToTarget);
/* 202 */     if (result.size() < 1) {
/* 203 */       return null;
/*     */     }
/* 205 */     if (result.size() > 1) {
/* 206 */       throw new RuntimeException("Problem in TransducerGraph data structures.");
/*     */     }
/*     */     
/* 209 */     Iterator iterator = result.iterator();
/* 210 */     return (Arc)iterator.next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean addArc(Object source, Object target, Object input, Object output)
/*     */   {
/* 217 */     Arc a = new Arc(source, target, input, output);
/* 218 */     return addArc(a);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean addArc(Arc a)
/*     */   {
/* 226 */     Object source = a.getSourceNode();
/* 227 */     Object target = a.getTargetNode();
/* 228 */     Object input = a.getInput();
/* 229 */     if ((source == null) || (target == null) || (input == null)) {
/* 230 */       return false;
/*     */     }
/*     */     
/* 233 */     if (this.arcs.contains(a)) {
/* 234 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 238 */     Pair p = new Pair(source, input);
/* 239 */     if ((this.arcsBySourceAndInput.containsKey(p)) && (this.checkDeterminism)) {
/* 240 */       throw new RuntimeException("Creating nondeterminism while inserting arc " + a + " because it already has arc " + this.arcsBySourceAndInput.get(p) + this.checkDeterminism);
/*     */     }
/* 242 */     this.arcsBySourceAndInput.put(p, a);
/* 243 */     Maps.putIntoValueHashSet(this.arcsBySource, source, a);
/* 244 */     p = new Pair(target, input);
/* 245 */     Maps.putIntoValueHashSet(this.arcsByTargetAndInput, p, a);
/* 246 */     Maps.putIntoValueHashSet(this.arcsByTarget, target, a);
/* 247 */     Maps.putIntoValueHashSet(this.arcsByInput, input, a);
/*     */     
/* 249 */     this.arcs.add(a);
/* 250 */     return true;
/*     */   }
/*     */   
/*     */   public boolean removeArc(Arc a) {
/* 254 */     Object source = a.getSourceNode();
/* 255 */     Object target = a.getTargetNode();
/* 256 */     Object input = a.getInput();
/*     */     
/* 258 */     if (!this.arcs.remove(a)) {
/* 259 */       return false;
/*     */     }
/*     */     
/* 262 */     Pair p = new Pair(source, input);
/* 263 */     if (!this.arcsBySourceAndInput.containsKey(p)) {
/* 264 */       return false;
/*     */     }
/* 266 */     this.arcsBySourceAndInput.remove(p);
/*     */     
/* 268 */     Set s = (Set)this.arcsBySource.get(source);
/* 269 */     if (s == null) {
/* 270 */       return false;
/*     */     }
/* 272 */     if (!s.remove(a)) {
/* 273 */       return false;
/*     */     }
/*     */     
/* 276 */     p = new Pair(target, input);
/* 277 */     s = (Set)this.arcsByTargetAndInput.get(p);
/* 278 */     if (s == null) {
/* 279 */       return false;
/*     */     }
/* 281 */     if (!s.remove(a)) {
/* 282 */       return false;
/*     */     }
/*     */     
/* 285 */     s = (Set)this.arcsByTarget.get(target);
/* 286 */     if (s == null) {
/* 287 */       return false;
/*     */     }
/* 289 */     s = (Set)this.arcsByInput.get(input);
/* 290 */     if (s == null) {
/* 291 */       return false;
/*     */     }
/* 293 */     if (!s.remove(a)) {
/* 294 */       return false;
/*     */     }
/* 296 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canAddArc(Object source, Object target, Object input, Object output) {
/* 300 */     Arc a = new Arc(source, target, input, output);
/* 301 */     if (this.arcs.contains(a))
/*     */     {
/* 303 */       return false;
/*     */     }
/* 305 */     Pair p = new Pair(source, input);
/* 306 */     return !this.arcsBySourceAndInput.containsKey(p);
/*     */   }
/*     */   
/*     */   public static class Arc {
/*     */     protected Object sourceNode;
/*     */     protected Object targetNode;
/*     */     protected Object input;
/*     */     protected Object output;
/*     */     
/*     */     public Object getSourceNode() {
/* 316 */       return this.sourceNode;
/*     */     }
/*     */     
/*     */     public Object getTargetNode() {
/* 320 */       return this.targetNode;
/*     */     }
/*     */     
/*     */     public Object getInput() {
/* 324 */       return this.input;
/*     */     }
/*     */     
/*     */     public Object getOutput() {
/* 328 */       return this.output;
/*     */     }
/*     */     
/*     */     public void setSourceNode(Object o) {
/* 332 */       this.sourceNode = o;
/*     */     }
/*     */     
/*     */     public void setTargetNode(Object o) {
/* 336 */       this.targetNode = o;
/*     */     }
/*     */     
/*     */     public void setInput(Object o) {
/* 340 */       this.input = o;
/*     */     }
/*     */     
/*     */     public void setOutput(Object o) {
/* 344 */       this.output = o;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 348 */       return this.sourceNode.hashCode() ^ this.targetNode.hashCode() << 16 ^ this.input.hashCode() << 16;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 352 */       if (o == this) {
/* 353 */         return true;
/*     */       }
/* 355 */       if (!(o instanceof Arc)) {
/* 356 */         return false;
/*     */       }
/* 358 */       Arc a = (Arc)o;
/* 359 */       return (this.sourceNode == null ? a.sourceNode == null : this.sourceNode.equals(a.sourceNode)) && (this.targetNode == null ? a.targetNode == null : this.targetNode.equals(a.targetNode)) && (this.input == null ? a.input == null : this.input.equals(a.input));
/*     */     }
/*     */     
/*     */     protected Arc(Arc a)
/*     */     {
/* 364 */       this(a.getSourceNode(), a.getTargetNode(), a.getInput(), a.getOutput());
/*     */     }
/*     */     
/*     */     protected Arc(Object sourceNode, Object targetNode) {
/* 368 */       this(sourceNode, targetNode, null, null);
/*     */     }
/*     */     
/*     */     protected Arc(Object sourceNode, Object targetNode, Object input) {
/* 372 */       this(sourceNode, targetNode, input, null);
/*     */     }
/*     */     
/*     */     protected Arc(Object sourceNode, Object targetNode, Object input, Object output) {
/* 376 */       this.sourceNode = sourceNode;
/* 377 */       this.targetNode = targetNode;
/* 378 */       this.input = input;
/* 379 */       this.output = output;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 383 */       return this.sourceNode + " --> " + this.targetNode + " (" + this.input + " : " + this.output + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface ArcProcessor
/*     */   {
/*     */     public abstract TransducerGraph.Arc processArc(TransducerGraph.Arc paramArc);
/*     */   }
/*     */   
/*     */   public static class OutputCombiningProcessor implements TransducerGraph.ArcProcessor
/*     */   {
/*     */     public TransducerGraph.Arc processArc(TransducerGraph.Arc a)
/*     */     {
/* 396 */       a = new TransducerGraph.Arc(a);
/* 397 */       a.setInput(new Pair(a.getInput(), a.getOutput()));
/* 398 */       a.setOutput(null);
/* 399 */       return a;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class InputSplittingProcessor implements TransducerGraph.ArcProcessor {
/*     */     public TransducerGraph.Arc processArc(TransducerGraph.Arc a) {
/* 405 */       a = new TransducerGraph.Arc(a);
/* 406 */       Pair p = (Pair)a.getInput();
/* 407 */       a.setInput(p.first);
/* 408 */       a.setOutput(p.second);
/* 409 */       return a;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NodeProcessorWrappingArcProcessor implements TransducerGraph.ArcProcessor {
/*     */     TransducerGraph.NodeProcessor nodeProcessor;
/*     */     
/*     */     public NodeProcessorWrappingArcProcessor(TransducerGraph.NodeProcessor nodeProcessor) {
/* 417 */       this.nodeProcessor = nodeProcessor;
/*     */     }
/*     */     
/*     */     public TransducerGraph.Arc processArc(TransducerGraph.Arc a) {
/* 421 */       a = new TransducerGraph.Arc(a);
/* 422 */       a.setSourceNode(this.nodeProcessor.processNode(a.getSourceNode()));
/* 423 */       a.setTargetNode(this.nodeProcessor.processNode(a.getTargetNode()));
/* 424 */       return a;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface NodeProcessor {
/*     */     public abstract Object processNode(Object paramObject);
/*     */   }
/*     */   
/*     */   public static class SetToStringNodeProcessor implements TransducerGraph.NodeProcessor {
/*     */     private TreebankLanguagePack tlp;
/*     */     
/*     */     public SetToStringNodeProcessor(TreebankLanguagePack tlp) {
/* 436 */       this.tlp = tlp;
/*     */     }
/*     */     
/*     */     public Object processNode(Object node) {
/* 440 */       Set s = null;
/* 441 */       if ((node instanceof Set)) {
/* 442 */         s = (Set)node;
/*     */       }
/* 444 */       else if ((node instanceof Block)) {
/* 445 */         Block b = (Block)node;
/* 446 */         s = b.getMembers();
/*     */       } else {
/* 448 */         throw new RuntimeException("Unexpected node class");
/*     */       }
/*     */       
/* 451 */       Object sampleNode = s.iterator().next();
/* 452 */       if (s.size() == 1) {
/* 453 */         if ((sampleNode instanceof Block)) {
/* 454 */           return processNode(sampleNode);
/*     */         }
/* 456 */         return sampleNode;
/*     */       }
/*     */       
/*     */ 
/* 460 */       if ((sampleNode instanceof String)) {
/* 461 */         String str = (String)sampleNode;
/* 462 */         if (str.charAt(0) != '@')
/*     */         {
/* 464 */           return this.tlp.basicCategory(str) + "-" + s.hashCode();
/*     */         }
/*     */       }
/*     */       
/* 468 */       return "@NodeSet-" + s.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ObjectToSetNodeProcessor implements TransducerGraph.NodeProcessor
/*     */   {
/*     */     public Object processNode(Object node) {
/* 475 */       return Collections.singleton(node);
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface GraphProcessor
/*     */   {
/*     */     public abstract TransducerGraph processGraph(TransducerGraph paramTransducerGraph);
/*     */   }
/*     */   
/*     */   public static class NormalizingGraphProcessor implements TransducerGraph.GraphProcessor {
/* 485 */     boolean forward = true;
/*     */     
/*     */     public NormalizingGraphProcessor(boolean forwardNormalization) {
/* 488 */       this.forward = forwardNormalization;
/*     */     }
/*     */     
/*     */     public TransducerGraph processGraph(TransducerGraph g) {
/* 492 */       g = new TransducerGraph(g);
/* 493 */       Set nodes = g.getNodes();
/* 494 */       for (Iterator nodeIter = nodes.iterator(); nodeIter.hasNext();) {
/* 495 */         Object node = nodeIter.next();
/* 496 */         Set myArcs = null;
/* 497 */         if (this.forward) {
/* 498 */           myArcs = g.getArcsBySource(node);
/*     */         } else {
/* 500 */           myArcs = g.getArcsByTarget(node);
/*     */         }
/*     */         
/* 503 */         total = 0.0D;
/* 504 */         for (Iterator arcIter = myArcs.iterator(); arcIter.hasNext();) {
/* 505 */           TransducerGraph.Arc a = (TransducerGraph.Arc)arcIter.next();
/* 506 */           total += ((Double)a.getOutput()).doubleValue();
/*     */         }
/*     */         
/* 509 */         for (arcIter = myArcs.iterator(); arcIter.hasNext();) {
/* 510 */           TransducerGraph.Arc a = (TransducerGraph.Arc)arcIter.next();
/* 511 */           a.setOutput(new Double(Math.log(((Double)a.getOutput()).doubleValue() / total))); } }
/*     */       double total;
/*     */       Iterator arcIter;
/* 514 */       return g;
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 519 */     StringBuffer sb = new StringBuffer();
/* 520 */     depthFirstSearch(true, sb);
/* 521 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/* 525 */   private boolean dotWeightInverted = false;
/*     */   
/*     */   public void setDotWeightingInverted(boolean inverted) {
/* 528 */     this.dotWeightInverted = true;
/*     */   }
/*     */   
/*     */   public String asDOTString() {
/* 532 */     NumberFormat nf = NumberFormat.getNumberInstance();
/* 533 */     nf.setMaximumFractionDigits(3);
/* 534 */     nf.setMinimumFractionDigits(1);
/* 535 */     StringBuffer result = new StringBuffer();
/* 536 */     Set nodes = getNodes();
/* 537 */     result.append("digraph G {\n");
/*     */     
/*     */ 
/*     */ 
/* 541 */     int sz = this.arcs.size();
/* 542 */     int ht = 105;
/* 543 */     int mag = 250;
/* 544 */     while (sz > mag) {
/* 545 */       ht += 105;
/* 546 */       mag *= 2;
/*     */     }
/* 548 */     int wd = 8;
/* 549 */     mag = 500;
/* 550 */     while (sz > mag) {
/* 551 */       wd += 8;
/* 552 */       mag *= 4;
/*     */     }
/* 554 */     double htd = ht / 10.0D;
/* 555 */     result.append("size = \"" + wd + "," + htd + "\";\n");
/* 556 */     result.append("graph [rankdir = \"LR\"];\n");
/* 557 */     result.append("graph [ranksep = \"0.2\"];\n");
/* 558 */     for (Iterator nodeI = nodes.iterator(); nodeI.hasNext();) {
/* 559 */       Object node = nodeI.next();
/* 560 */       String cleanString = StringUtils.fileNameClean(node.toString());
/* 561 */       result.append(cleanString);
/* 562 */       result.append(" [ ");
/*     */       
/*     */ 
/*     */ 
/* 566 */       result.append("label=\"" + node.toString() + "\"");
/* 567 */       result.append("height=\"0.3\", width=\"0.3\"");
/* 568 */       result.append(" ];\n");
/* 569 */       for (arcI = getArcsBySource(node).iterator(); arcI.hasNext();) {
/* 570 */         Arc arc = (Arc)arcI.next();
/* 571 */         result.append(StringUtils.fileNameClean(arc.getSourceNode().toString()));
/* 572 */         result.append(" -> ");
/* 573 */         result.append(StringUtils.fileNameClean(arc.getTargetNode().toString()));
/* 574 */         result.append(" [ ");
/* 575 */         result.append("label=\"");
/* 576 */         result.append(arc.getInput());
/* 577 */         result.append(" : ");
/*     */         
/* 579 */         Object output = arc.getOutput();
/* 580 */         String wt = "";
/* 581 */         if ((output instanceof Number)) {
/* 582 */           double dd = ((Number)output).doubleValue();
/* 583 */           if (dd == -0.0D) {
/* 584 */             result.append(nf.format(0.0D));
/*     */           } else
/* 586 */             result.append(nf.format(output));
/*     */           int weight;
/*     */           int weight;
/* 589 */           if (this.dotWeightInverted) {
/* 590 */             weight = (int)(20.0D - dd);
/*     */           } else {
/* 592 */             weight = (int)dd;
/*     */           }
/* 594 */           if (weight > 0) {
/* 595 */             wt = ", weight = \"" + weight + "\"";
/*     */           }
/* 597 */           if (((this.dotWeightInverted) && (dd <= 2.0D)) || ((!this.dotWeightInverted) && (dd >= 20.0D))) {
/* 598 */             wt = wt + ", style=bold";
/*     */           }
/*     */         } else {
/* 601 */           result.append(output);
/*     */         }
/* 603 */         result.append("\"");
/* 604 */         result.append(wt);
/*     */         
/* 606 */         if (arc.getInput().toString().equals("EPSILON")) {
/* 607 */           result.append(", style = \"dashed\" ");
/*     */         } else {
/* 609 */           result.append(", style = \"solid\" ");
/*     */         }
/*     */         
/* 612 */         result.append("];\n");
/*     */       } }
/*     */     Iterator arcI;
/* 615 */     result.append("}\n");
/* 616 */     return result.toString();
/*     */   }
/*     */   
/*     */   public double inFlow(Object node) {
/* 620 */     Set arcs = getArcsByTarget(node);
/* 621 */     return sumOutputs(arcs);
/*     */   }
/*     */   
/*     */   public double outFlow(Object node) {
/* 625 */     Set arcs = getArcsBySource(node);
/* 626 */     return sumOutputs(arcs);
/*     */   }
/*     */   
/*     */   private double sumOutputs(Set arcs) {
/* 630 */     double sum = 0.0D;
/* 631 */     for (Iterator arcI = arcs.iterator(); arcI.hasNext();) {
/* 632 */       Arc arc = (Arc)arcI.next();
/* 633 */       sum += ((Double)arc.getOutput()).doubleValue();
/*     */     }
/* 635 */     return sum;
/*     */   }
/*     */   
/*     */   public double getSourceTotal(Object node) {
/* 639 */     double result = 0.0D;
/* 640 */     Set arcs = getArcsBySource(node);
/* 641 */     if (arcs.size() == 0) {
/* 642 */       System.err.println("No outbound arcs from node.");
/* 643 */       return result;
/*     */     }
/* 645 */     for (Iterator arcIter = arcs.iterator(); arcIter.hasNext();) {
/* 646 */       Arc arc = (Arc)arcIter.next();
/* 647 */       result += ((Double)arc.getOutput()).doubleValue();
/*     */     }
/* 649 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getOutputOfPathInGraph(List path)
/*     */   {
/* 656 */     double score = 0.0D;
/* 657 */     Object node = getStartNode();
/* 658 */     for (Iterator j = path.iterator(); j.hasNext();) {
/* 659 */       Object input = j.next();
/* 660 */       Arc arc = getArcBySourceAndInput(node, input);
/* 661 */       if (arc == null) {
/* 662 */         System.out.println(" NOT ACCEPTED :" + path);
/* 663 */         return Double.NEGATIVE_INFINITY;
/*     */       }
/* 665 */       score += ((Double)arc.getOutput()).doubleValue();
/* 666 */       node = arc.getTargetNode();
/*     */     }
/* 668 */     return score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List sampleUniformPathFromGraph()
/*     */   {
/* 675 */     List list = new ArrayList();
/* 676 */     Object node = getStartNode();
/* 677 */     Set endNodes = getEndNodes();
/* 678 */     while (!endNodes.contains(node)) {
/* 679 */       List arcs = new ArrayList(getArcsBySource(node));
/* 680 */       Arc arc = (Arc)arcs.get(r.nextInt(arcs.size()));
/* 681 */       list.add(arc.getInput());
/* 682 */       node = arc.getTargetNode();
/*     */     }
/* 684 */     return list;
/*     */   }
/*     */   
/*     */   public Map samplePathsFromGraph(int numPaths) {
/* 688 */     Map result = new HashMap();
/* 689 */     for (int i = 0; i < numPaths; i++) {
/* 690 */       List l = sampleUniformPathFromGraph();
/* 691 */       result.put(l, new Double(getOutputOfPathInGraph(l)));
/*     */     }
/* 693 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void printPathOutputs(List pathList, TransducerGraph graph, boolean printPaths)
/*     */   {
/* 700 */     int i = 0;
/* 701 */     for (Iterator iter = pathList.iterator(); iter.hasNext();) {
/* 702 */       List path = (List)iter.next();
/* 703 */       Iterator j; if (printPaths) {
/* 704 */         for (j = path.iterator(); j.hasNext();) {
/* 705 */           System.out.print(j.next() + " ");
/*     */         }
/*     */       } else {
/* 708 */         System.out.print(i++ + " ");
/*     */       }
/* 710 */       System.out.print("output: " + graph.getOutputOfPathInGraph(path));
/* 711 */       System.out.println();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List getPathOutputs(List pathList)
/*     */   {
/* 719 */     List outputList = new ArrayList();
/* 720 */     for (Iterator iter = pathList.iterator(); iter.hasNext();) {
/* 721 */       List path = (List)iter.next();
/* 722 */       outputList.add(new Double(getOutputOfPathInGraph(path)));
/*     */     }
/* 724 */     return outputList;
/*     */   }
/*     */   
/*     */   public static boolean testGraphPaths(TransducerGraph sourceGraph, TransducerGraph testGraph, int numPaths) {
/* 728 */     for (int i = 0; i < numPaths; i++) {
/* 729 */       List path = sourceGraph.sampleUniformPathFromGraph();
/* 730 */       double score = sourceGraph.getOutputOfPathInGraph(path);
/* 731 */       double newScore = testGraph.getOutputOfPathInGraph(path);
/* 732 */       if ((score - newScore) / (score + newScore) > 1.0E-10D) {
/* 733 */         System.out.println("Problem: " + score + " vs. " + newScore + " on " + path);
/* 734 */         return false;
/*     */       }
/*     */     }
/* 737 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canAddPath(List path)
/*     */   {
/* 745 */     Object node = getStartNode();
/* 746 */     for (int j = 0; j < path.size() - 1; j++) {
/* 747 */       Object input = path.get(j);
/* 748 */       Arc arc = getArcBySourceAndInput(node, input);
/* 749 */       if (arc == null) {
/* 750 */         return true;
/*     */       }
/* 752 */       node = arc.getTargetNode();
/*     */     }
/* 754 */     Object input = path.get(path.size() - 1);
/* 755 */     Arc arc = getArcBySourceAndInput(node, input);
/* 756 */     if (arc == null) {
/* 757 */       return true;
/*     */     }
/* 759 */     if (getEndNodes().contains(arc.getTargetNode())) {
/* 760 */       return true;
/*     */     }
/* 762 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TransducerGraph createGraphFromPaths(List paths, int markovOrder)
/*     */   {
/* 772 */     Counter pathCounter = edu.stanford.nlp.stats.Counters.createCounterFromList(paths);
/* 773 */     return createGraphFromPaths(pathCounter, markovOrder);
/*     */   }
/*     */   
/*     */   public static TransducerGraph createGraphFromPaths(Counter pathCounter, int markovOrder) {
/* 777 */     TransducerGraph graph = new TransducerGraph();
/* 778 */     for (Iterator pathIter = pathCounter.keySet().iterator(); pathIter.hasNext();) {
/* 779 */       List path = (List)pathIter.next();
/* 780 */       double count = pathCounter.getCount(path);
/* 781 */       addOnePathToGraph(path, count, markovOrder, graph);
/*     */     }
/* 783 */     return graph;
/*     */   }
/*     */   
/*     */   public static void addOnePathToGraph(List path, double count, int markovOrder, TransducerGraph graph)
/*     */   {
/* 788 */     Object source = graph.getStartNode();
/*     */     
/* 790 */     for (int j = 0; j < path.size(); j++) {
/* 791 */       Object input = path.get(j);
/* 792 */       Arc a = graph.getArcBySourceAndInput(source, input);
/* 793 */       if (a != null)
/*     */       {
/* 795 */         a.output = new Double(((Double)a.output).doubleValue() + count); } else { Object target;
/*     */         Object target;
/* 797 */         if (input.equals(EPSILON_INPUT)) {
/* 798 */           target = "END"; } else { Object target;
/* 799 */           if (markovOrder == 0)
/*     */           {
/* 801 */             target = source; } else { Object target;
/* 802 */             if (markovOrder > 0)
/*     */             {
/* 804 */               target = path.subList(j < markovOrder ? 0 : j - markovOrder + 1, j + 1);
/*     */             }
/*     */             else
/* 807 */               target = path.subList(0, j + 1);
/*     */           } }
/* 809 */         Double output = new Double(count);
/* 810 */         a = new Arc(source, target, input, output);
/* 811 */         graph.addArc(a);
/*     */       }
/* 813 */       source = a.getTargetNode();
/*     */     }
/* 815 */     graph.setEndNode(source);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TransducerGraph createRandomGraph(int numPaths, int pathLengthMean, double pathLengthVariance, int numInputs, List pathList)
/*     */   {
/* 825 */     int pathLength = (int)(r.nextGaussian() * pathLengthVariance + pathLengthMean);
/*     */     
/*     */ 
/* 828 */     for (int i = 0; i < numPaths; i++)
/*     */     {
/* 830 */       List path = new ArrayList();
/* 831 */       for (int j = 0; j < pathLength; j++) {
/* 832 */         Object input = new Integer(r.nextInt(numInputs));
/* 833 */         path.add(input);
/*     */       }
/* 835 */       pathList.add(path);
/*     */     }
/* 837 */     return createGraphFromPaths(pathList, -1);
/*     */   }
/*     */   
/*     */   public static List createRandomPaths(int numPaths, int pathLengthMean, double pathLengthVariance, int numInputs) {
/* 841 */     List pathList = new ArrayList();
/*     */     
/*     */ 
/* 844 */     int pathLength = (int)(r.nextGaussian() * pathLengthVariance + pathLengthMean);
/*     */     
/*     */ 
/* 847 */     for (int i = 0; i < numPaths; i++)
/*     */     {
/* 849 */       List path = new ArrayList();
/* 850 */       for (int j = 0; j < pathLength; j++) {
/* 851 */         Object input = new Integer(r.nextInt(numInputs));
/* 852 */         path.add(input);
/*     */       }
/*     */       
/* 855 */       Object input = EPSILON_INPUT;
/* 856 */       path.add(input);
/* 857 */       pathList.add(path);
/*     */     }
/* 859 */     return pathList;
/*     */   }
/*     */   
/*     */   public void depthFirstSearch(boolean forward, StringBuffer b) { Iterator endIter;
/* 863 */     if (forward) {
/* 864 */       depthFirstSearchHelper(getStartNode(), new HashSet(), 0, true, b);
/*     */     } else {
/* 866 */       for (endIter = getEndNodes().iterator(); endIter.hasNext();) {
/* 867 */         Object o = endIter.next();
/* 868 */         depthFirstSearchHelper(o, new HashSet(), 0, false, b);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void depthFirstSearchHelper(Object node, Set marked, int level, boolean forward, StringBuffer b)
/*     */   {
/* 877 */     if (marked.contains(node)) {
/* 878 */       return;
/*     */     }
/* 880 */     marked.add(node);
/*     */     Set arcs;
/* 882 */     Set arcs; if (forward) {
/* 883 */       arcs = getArcsBySource(node);
/*     */     } else {
/* 885 */       arcs = getArcsByTarget(node);
/*     */     }
/* 887 */     if (arcs == null) {
/* 888 */       return;
/*     */     }
/* 890 */     for (Iterator iter = arcs.iterator(); iter.hasNext();) {
/* 891 */       Arc newArc = (Arc)iter.next();
/*     */       
/* 893 */       for (int i = 0; i < level; i++) {
/* 894 */         b.append("  ");
/*     */       }
/* 896 */       if (getEndNodes().contains(newArc.getTargetNode())) {
/* 897 */         b.append(newArc + " END\n");
/*     */       } else {
/* 899 */         b.append(newArc + "\n");
/*     */       }
/* 901 */       if (forward) {
/* 902 */         depthFirstSearchHelper(newArc.getTargetNode(), marked, level + 1, forward, b);
/*     */       } else {
/* 904 */         depthFirstSearchHelper(newArc.getSourceNode(), marked, level + 1, forward, b);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 913 */     List pathList = new ArrayList();
/* 914 */     TransducerGraph graph = createRandomGraph(1000, 10, 0.0D, 10, pathList);
/* 915 */     System.out.println("Done creating random graph");
/* 916 */     printPathOutputs(pathList, graph, true);
/* 917 */     System.out.println("Depth first search from start node");
/* 918 */     StringBuffer b = new StringBuffer();
/* 919 */     graph.depthFirstSearch(true, b);
/* 920 */     System.out.println(b.toString());
/* 921 */     b = new StringBuffer();
/* 922 */     System.out.println("Depth first search back from end node");
/* 923 */     graph.depthFirstSearch(false, b);
/* 924 */     System.out.println(b.toString());
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\fsm\TransducerGraph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */