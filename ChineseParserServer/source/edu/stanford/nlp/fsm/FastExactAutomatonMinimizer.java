/*     */ package edu.stanford.nlp.fsm;
/*     */ 
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class FastExactAutomatonMinimizer implements AutomatonMinimizer
/*     */ {
/*     */   TransducerGraph unminimizedFA;
/*     */   java.util.Map memberToBlock;
/*     */   java.util.LinkedList splits;
/*     */   boolean sparseMode;
/*     */   
/*     */   public FastExactAutomatonMinimizer()
/*     */   {
/*  14 */     this.unminimizedFA = null;
/*  15 */     this.memberToBlock = null;
/*  16 */     this.splits = null;
/*     */     
/*  18 */     this.sparseMode = true; }
/*     */   
/*  20 */   static final Object SINK_NODE = "SINK_NODE";
/*     */   
/*     */   static class Split {
/*     */     Collection members;
/*     */     Object symbol;
/*     */     FastExactAutomatonMinimizer.Block block;
/*     */     
/*     */     public Collection getMembers() {
/*  28 */       return this.members;
/*     */     }
/*     */     
/*     */     public Object getSymbol() {
/*  32 */       return this.symbol;
/*     */     }
/*     */     
/*     */     public FastExactAutomatonMinimizer.Block getBlock() {
/*  36 */       return this.block;
/*     */     }
/*     */     
/*     */     public Split(Collection members, Object symbol, FastExactAutomatonMinimizer.Block block) {
/*  40 */       this.members = members;
/*  41 */       this.symbol = symbol;
/*  42 */       this.block = block;
/*     */     }
/*     */   }
/*     */   
/*     */   static class Block {
/*     */     java.util.Set members;
/*     */     
/*     */     public java.util.Set getMembers() {
/*  50 */       return this.members;
/*     */     }
/*     */     
/*     */     public Block(java.util.Set members) {
/*  54 */       this.members = members;
/*     */     }
/*     */   }
/*     */   
/*     */   protected TransducerGraph getUnminimizedFA() {
/*  59 */     return this.unminimizedFA;
/*     */   }
/*     */   
/*     */   protected Collection getSymbols() {
/*  63 */     return getUnminimizedFA().getInputs();
/*     */   }
/*     */   
/*     */   public TransducerGraph minimizeFA(TransducerGraph unminimizedFA)
/*     */   {
/*  68 */     this.unminimizedFA = unminimizedFA;
/*  69 */     this.splits = new java.util.LinkedList();
/*  70 */     this.memberToBlock = new java.util.HashMap();
/*  71 */     minimize();
/*  72 */     return buildMinimizedFA();
/*     */   }
/*     */   
/*     */   protected TransducerGraph buildMinimizedFA() {
/*  76 */     TransducerGraph minimizedFA = new TransducerGraph();
/*  77 */     TransducerGraph unminimizedFA = getUnminimizedFA();
/*  78 */     for (java.util.Iterator arcI = unminimizedFA.getArcs().iterator(); arcI.hasNext();) {
/*  79 */       TransducerGraph.Arc arc = (TransducerGraph.Arc)arcI.next();
/*  80 */       Object source = projectNode(arc.getSourceNode());
/*  81 */       Object target = projectNode(arc.getTargetNode());
/*     */       try {
/*  83 */         if (minimizedFA.canAddArc(source, target, arc.getInput(), arc.getOutput())) {
/*  84 */           minimizedFA.addArc(source, target, arc.getInput(), arc.getOutput());
/*     */         }
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */     
/*  90 */     minimizedFA.setStartNode(projectNode(unminimizedFA.getStartNode()));
/*  91 */     for (java.util.Iterator endIter = unminimizedFA.getEndNodes().iterator(); endIter.hasNext();) {
/*  92 */       Object o = endIter.next();
/*  93 */       minimizedFA.setEndNode(projectNode(o));
/*     */     }
/*     */     
/*  96 */     return minimizedFA;
/*     */   }
/*     */   
/*     */   protected Object projectNode(Object node) {
/* 100 */     java.util.Set members = getBlock(node).getMembers();
/* 101 */     return members;
/*     */   }
/*     */   
/*     */   protected boolean hasSplit()
/*     */   {
/* 106 */     return this.splits.size() > 0;
/*     */   }
/*     */   
/*     */   protected Split getSplit() {
/* 110 */     return (Split)this.splits.removeFirst();
/*     */   }
/*     */   
/*     */   protected void addSplit(Split split) {
/* 114 */     this.splits.addLast(split);
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
/*     */   protected java.util.Map sortIntoBlocks(Collection nodes)
/*     */   {
/* 127 */     java.util.Map blockToMembers = new java.util.IdentityHashMap();
/* 128 */     for (java.util.Iterator nodeI = nodes.iterator(); nodeI.hasNext();) {
/* 129 */       Object o = nodeI.next();
/* 130 */       Block block = getBlock(o);
/* 131 */       edu.stanford.nlp.util.Maps.putIntoValueHashSet(blockToMembers, block, o);
/*     */     }
/* 133 */     return blockToMembers;
/*     */   }
/*     */   
/*     */   protected void makeBlock(Collection members) {
/* 137 */     Block block = new Block(new java.util.HashSet(members));
/* 138 */     for (java.util.Iterator memberI = block.getMembers().iterator(); memberI.hasNext();) {
/* 139 */       Object member = memberI.next();
/* 140 */       if (member != SINK_NODE)
/*     */       {
/* 142 */         this.memberToBlock.put(member, block);
/*     */       }
/*     */     }
/* 145 */     addSplits(block);
/*     */   }
/*     */   
/*     */   protected void addSplits(Block block) {
/* 149 */     java.util.Map symbolToTarget = new java.util.HashMap();
/* 150 */     for (java.util.Iterator memberI = block.getMembers().iterator(); memberI.hasNext();) {
/* 151 */       Object member = memberI.next();
/* 152 */       for (symbolI = getInverseArcs(member).iterator(); symbolI.hasNext();) {
/* 153 */         TransducerGraph.Arc arc = (TransducerGraph.Arc)symbolI.next();
/* 154 */         Object symbol = arc.getInput();
/* 155 */         Object target = arc.getTargetNode();
/* 156 */         edu.stanford.nlp.util.Maps.putIntoValueArrayList(symbolToTarget, symbol, target);
/*     */       } }
/*     */     java.util.Iterator symbolI;
/* 159 */     for (java.util.Iterator symbolI = symbolToTarget.keySet().iterator(); symbolI.hasNext();) {
/* 160 */       Object symbol = symbolI.next();
/* 161 */       addSplit(new Split((java.util.List)symbolToTarget.get(symbol), symbol, block));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void removeAll(Collection block, Collection members)
/*     */   {
/* 167 */     for (java.util.Iterator memberI = members.iterator(); memberI.hasNext();) {
/* 168 */       Object member = memberI.next();
/* 169 */       block.remove(member);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Collection difference(Collection block, Collection members) {
/* 174 */     java.util.Set difference = new java.util.HashSet();
/* 175 */     for (java.util.Iterator memberI = block.iterator(); memberI.hasNext();) {
/* 176 */       Object member = memberI.next();
/* 177 */       if (!members.contains(member)) {
/* 178 */         difference.add(member);
/*     */       }
/*     */     }
/* 181 */     return difference;
/*     */   }
/*     */   
/*     */   protected Block getBlock(Object o) {
/* 185 */     Block result = (Block)this.memberToBlock.get(o);
/* 186 */     if (result == null) {
/* 187 */       System.out.println("No block found for: " + o);
/* 188 */       System.out.println("But I do have blocks for: ");
/* 189 */       for (java.util.Iterator i = this.memberToBlock.keySet().iterator(); i.hasNext();) {
/* 190 */         System.out.println(i.next());
/*     */       }
/* 192 */       throw new RuntimeException("FastExactAutomatonMinimizer: no block found");
/*     */     }
/* 194 */     return result;
/*     */   }
/*     */   
/*     */   protected Collection getInverseImages(Split split) {
/* 198 */     java.util.List inverseImages = new java.util.ArrayList();
/* 199 */     Object symbol = split.getSymbol();
/* 200 */     Block block = split.getBlock();
/* 201 */     for (java.util.Iterator memberI = split.getMembers().iterator(); memberI.hasNext();) {
/* 202 */       Object member = memberI.next();
/* 203 */       if (block.getMembers().contains(member))
/*     */       {
/*     */ 
/* 206 */         Collection arcs = getInverseArcs(member, symbol);
/* 207 */         for (arcI = arcs.iterator(); arcI.hasNext();) {
/* 208 */           TransducerGraph.Arc arc = (TransducerGraph.Arc)arcI.next();
/* 209 */           Object source = arc.getSourceNode();
/* 210 */           inverseImages.add(source);
/*     */         } } }
/*     */     java.util.Iterator arcI;
/* 213 */     return inverseImages;
/*     */   }
/*     */   
/*     */   protected Collection getInverseArcs(Object member, Object symbol) {
/* 217 */     if (member != SINK_NODE) {
/* 218 */       return getUnminimizedFA().getArcsByTargetAndInput(member, symbol);
/*     */     }
/* 220 */     return getUnminimizedFA().getArcsByInput(symbol);
/*     */   }
/*     */   
/*     */   protected Collection getInverseArcs(Object member) {
/* 224 */     if (member != SINK_NODE) {
/* 225 */       return getUnminimizedFA().getArcsByTarget(member);
/*     */     }
/* 227 */     return getUnminimizedFA().getArcs();
/*     */   }
/*     */   
/*     */   protected void makeInitialBlocks()
/*     */   {
/* 232 */     makeBlock(java.util.Collections.singleton(SINK_NODE));
/*     */     
/* 234 */     java.util.Set endNodes = getUnminimizedFA().getEndNodes();
/* 235 */     makeBlock(endNodes);
/*     */     
/* 237 */     Collection nonFinalNodes = new java.util.HashSet(getUnminimizedFA().getNodes());
/* 238 */     nonFinalNodes.removeAll(endNodes);
/* 239 */     makeBlock(nonFinalNodes);
/*     */   }
/*     */   
/*     */   protected void minimize() {
/* 243 */     makeInitialBlocks();
/* 244 */     java.util.Map inverseImagesByBlock; java.util.Iterator blockI; while (hasSplit()) {
/* 245 */       Split split = getSplit();
/* 246 */       Collection inverseImages = getInverseImages(split);
/* 247 */       inverseImagesByBlock = sortIntoBlocks(inverseImages);
/* 248 */       for (blockI = inverseImagesByBlock.keySet().iterator(); blockI.hasNext();) {
/* 249 */         Block block = (Block)blockI.next();
/* 250 */         Collection members = (Collection)inverseImagesByBlock.get(block);
/* 251 */         if ((members.size() != 0) && (members.size() != block.getMembers().size()))
/*     */         {
/*     */ 
/* 254 */           if (members.size() > block.getMembers().size() - members.size()) {
/* 255 */             members = difference(block.getMembers(), members);
/*     */           }
/* 257 */           removeAll(block.getMembers(), members);
/* 258 */           makeBlock(members);
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 278 */     System.out.println("Starting minimizer test...");
/* 279 */     java.util.List pathList = new java.util.ArrayList();
/* 280 */     TransducerGraph randomFA = TransducerGraph.createRandomGraph(5000, 5, 1.0D, 5, pathList);
/* 281 */     java.util.List outputs = randomFA.getPathOutputs(pathList);
/*     */     
/* 283 */     TransducerGraph.GraphProcessor quasiDeterminizer = new QuasiDeterminizer();
/* 284 */     AutomatonMinimizer minimizer = new FastExactAutomatonMinimizer();
/* 285 */     TransducerGraph.NodeProcessor ntsp = new TransducerGraph.SetToStringNodeProcessor(new edu.stanford.nlp.trees.PennTreebankLanguagePack());
/* 286 */     TransducerGraph.ArcProcessor isp = new TransducerGraph.InputSplittingProcessor();
/* 287 */     TransducerGraph.ArcProcessor ocp = new TransducerGraph.OutputCombiningProcessor();
/*     */     
/* 289 */     TransducerGraph detGraph = quasiDeterminizer.processGraph(randomFA);
/* 290 */     TransducerGraph combGraph = new TransducerGraph(detGraph, ocp);
/* 291 */     TransducerGraph result = minimizer.minimizeFA(combGraph);
/* 292 */     System.out.println("Minimized from " + randomFA.getNodes().size() + " to " + result.getNodes().size());
/* 293 */     result = new TransducerGraph(result, ntsp);
/* 294 */     result = new TransducerGraph(result, isp);
/* 295 */     java.util.List minOutputs = result.getPathOutputs(pathList);
/* 296 */     System.out.println("Equal? " + outputs.equals(minOutputs));
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\fsm\FastExactAutomatonMinimizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */