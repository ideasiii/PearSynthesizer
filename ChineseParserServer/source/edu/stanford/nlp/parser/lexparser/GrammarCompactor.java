/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.fsm.TransducerGraph;
/*     */ import edu.stanford.nlp.fsm.TransducerGraph.Arc;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class GrammarCompactor
/*     */ {
/*     */   Set<TransducerGraph> compactedGraphs;
/*  20 */   public static final Object RAW_COUNTS = new Object();
/*  21 */   public static final Object NORMALIZED_LOG_PROBABILITIES = new Object();
/*     */   
/*  23 */   public Object outputType = RAW_COUNTS;
/*     */   
/*     */   protected Numberer stateNumberer;
/*     */   
/*     */   protected Numberer newStateNumberer;
/*     */   
/*     */   protected String stateSpace;
/*     */   
/*     */   protected edu.stanford.nlp.stats.Distribution inputPrior;
/*     */   
/*  33 */   private static Object END = "END";
/*  34 */   private static Object EPSILON = "EPSILON";
/*  35 */   protected boolean verbose = false;
/*     */   
/*     */   protected abstract TransducerGraph doCompaction(TransducerGraph paramTransducerGraph, List paramList1, List paramList2);
/*     */   
/*     */   public Pair<UnaryGrammar, BinaryGrammar> compactGrammar(Pair<UnaryGrammar, BinaryGrammar> grammar)
/*     */   {
/*  41 */     return compactGrammar(grammar, new java.util.HashMap(), new java.util.HashMap());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pair<UnaryGrammar, BinaryGrammar> compactGrammar(Pair<UnaryGrammar, BinaryGrammar> grammar, Map allTrainPaths, Map allTestPaths)
/*     */   {
/*  53 */     this.inputPrior = computeInputPrior(allTrainPaths);
/*  54 */     BinaryGrammar bg = (BinaryGrammar)grammar.second;
/*  55 */     this.stateSpace = bg.stateSpace();
/*  56 */     this.stateNumberer = Numberer.getGlobalNumberer(this.stateSpace);
/*     */     
/*  58 */     Set<UnaryRule> unaryRules = new java.util.HashSet();
/*  59 */     Set<BinaryRule> binaryRules = new java.util.HashSet();
/*  60 */     Map graphs = convertGrammarToGraphs(grammar, unaryRules, binaryRules);
/*  61 */     this.compactedGraphs = new java.util.HashSet();
/*  62 */     if (this.verbose) {
/*  63 */       System.out.println("There are " + graphs.size() + " categories to compact.");
/*     */     }
/*  65 */     int i = 0;
/*  66 */     for (Iterator graphIter = graphs.entrySet().iterator(); graphIter.hasNext();) {
/*  67 */       Map.Entry entry = (Map.Entry)graphIter.next();
/*  68 */       String cat = (String)entry.getKey();
/*  69 */       TransducerGraph graph = (TransducerGraph)entry.getValue();
/*  70 */       if (this.verbose) {
/*  71 */         System.out.println("About to compact grammar for " + cat + " with numNodes=" + graph.getNodes().size());
/*     */       }
/*  73 */       List trainPaths = (List)allTrainPaths.remove(cat);
/*  74 */       if (trainPaths == null) {
/*  75 */         trainPaths = new java.util.ArrayList();
/*     */       }
/*  77 */       List testPaths = (List)allTestPaths.remove(cat);
/*  78 */       if (testPaths == null) {
/*  79 */         testPaths = new java.util.ArrayList();
/*     */       }
/*  81 */       TransducerGraph compactedGraph = doCompaction(graph, trainPaths, testPaths);
/*  82 */       i++;
/*  83 */       if (this.verbose) {
/*  84 */         System.out.println(i + ". Compacted grammar for " + cat + " from " + graph.getArcs().size() + " arcs to " + compactedGraph.getArcs().size() + " arcs.");
/*     */       }
/*  86 */       graphIter.remove();
/*  87 */       this.compactedGraphs.add(compactedGraph);
/*     */     }
/*  89 */     return convertGraphsToGrammar(this.compactedGraphs, unaryRules, binaryRules);
/*     */   }
/*     */   
/*     */   protected edu.stanford.nlp.stats.Distribution computeInputPrior(Map trainPathMap) {
/*  93 */     Counter result = new Counter();
/*  94 */     for (Iterator catI = trainPathMap.values().iterator(); catI.hasNext();) {
/*  95 */       List pathList = (List)catI.next();
/*  96 */       for (pathI = pathList.iterator(); pathI.hasNext();) {
/*  97 */         List path = (List)pathI.next();
/*  98 */         for (inputI = path.iterator(); inputI.hasNext();) {
/*  99 */           Object input = inputI.next();
/* 100 */           result.incrementCount(input);
/*     */         } } }
/*     */     Iterator pathI;
/*     */     Iterator inputI;
/* 104 */     return edu.stanford.nlp.stats.Distribution.laplaceSmoothedDistribution(result, result.size() * 2, 0.5D);
/*     */   }
/*     */   
/*     */   private double smartNegate(double output) {
/* 108 */     if (this.outputType == NORMALIZED_LOG_PROBABILITIES) {
/* 109 */       return -output;
/*     */     }
/* 111 */     return output;
/*     */   }
/*     */   
/*     */   public static boolean writeFile(TransducerGraph graph, String dir, String name) {
/*     */     try {
/* 116 */       File baseDir = new File(dir);
/* 117 */       if (baseDir.exists()) {
/* 118 */         if (!baseDir.isDirectory()) {
/* 119 */           return false;
/*     */         }
/*     */       }
/* 122 */       else if (!baseDir.mkdirs()) {
/* 123 */         return false;
/*     */       }
/*     */       
/* 126 */       File file = new File(baseDir, name + ".dot");
/*     */       try
/*     */       {
/* 129 */         PrintWriter w = new PrintWriter(new java.io.FileWriter(file));
/* 130 */         String dotString = graph.asDOTString();
/* 131 */         w.print(dotString);
/* 132 */         w.flush();
/* 133 */         w.close();
/*     */       } catch (java.io.FileNotFoundException e) {
/* 135 */         System.err.println("Failed to open file in writeToDOTfile: " + file);
/* 136 */         return false;
/*     */       } catch (java.io.IOException e) {
/* 138 */         System.err.println("Failed to open file in writeToDOTfile: " + file);
/* 139 */         return false;
/*     */       }
/* 141 */       return true;
/*     */     } catch (Exception e) {
/* 143 */       e.printStackTrace(); }
/* 144 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map convertGrammarToGraphs(Pair<UnaryGrammar, BinaryGrammar> grammar, Set unaryRules, Set binaryRules)
/*     */   {
/* 152 */     int numRules = 0;
/* 153 */     UnaryGrammar ug = (UnaryGrammar)grammar.first;
/* 154 */     BinaryGrammar bg = (BinaryGrammar)grammar.second;
/* 155 */     Map graphs = new java.util.HashMap();
/*     */     
/* 157 */     for (Iterator ruleIter = bg.iterator(); ruleIter.hasNext();) {
/* 158 */       BinaryRule rule = (BinaryRule)ruleIter.next();
/* 159 */       numRules++;
/* 160 */       boolean wasAdded = addOneBinaryRule(rule, graphs);
/* 161 */       if (!wasAdded)
/*     */       {
/*     */ 
/* 164 */         binaryRules.add(rule);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 169 */     for (Iterator ruleIter = ug.iterator(); ruleIter.hasNext();) {
/* 170 */       UnaryRule rule = (UnaryRule)ruleIter.next();
/* 171 */       numRules++;
/* 172 */       boolean wasAdded = addOneUnaryRule(rule, graphs);
/* 173 */       if (!wasAdded)
/*     */       {
/*     */ 
/* 176 */         unaryRules.add(rule);
/*     */       }
/*     */     }
/* 179 */     if (this.verbose) {
/* 180 */       System.out.println("Number of raw rules: " + numRules);
/* 181 */       System.out.println("Number of raw states: " + this.stateNumberer.total());
/*     */     }
/* 183 */     return graphs;
/*     */   }
/*     */   
/*     */   protected TransducerGraph getGraphFromMap(Map m, Object o) {
/* 187 */     TransducerGraph graph = (TransducerGraph)m.get(o);
/* 188 */     if (graph == null) {
/* 189 */       graph = new TransducerGraph();
/* 190 */       graph.setEndNode(o);
/* 191 */       m.put(o, graph);
/*     */     }
/* 193 */     return graph;
/*     */   }
/*     */   
/*     */   protected String getTopCategoryOfSyntheticState(String s) {
/* 197 */     if (s.charAt(0) != '@') {
/* 198 */       return null;
/*     */     }
/* 200 */     int bar = s.indexOf('|');
/* 201 */     if (bar < 0) {
/* 202 */       throw new RuntimeException("Grammar format error. Expected bar in state name: " + s);
/*     */     }
/* 204 */     String topcat = s.substring(1, bar);
/* 205 */     return topcat;
/*     */   }
/*     */   
/*     */   protected boolean addOneUnaryRule(UnaryRule rule, Map graphs) {
/* 209 */     String parentString = (String)this.stateNumberer.object(rule.parent);
/* 210 */     String childString = (String)this.stateNumberer.object(rule.child);
/* 211 */     if (isSyntheticState(parentString)) {
/* 212 */       String topcat = getTopCategoryOfSyntheticState(parentString);
/* 213 */       TransducerGraph graph = getGraphFromMap(graphs, topcat);
/* 214 */       Double output = new Double(smartNegate(rule.score()));
/* 215 */       graph.addArc(graph.getStartNode(), parentString, childString, output);
/* 216 */       return true; }
/* 217 */     if (isSyntheticState(childString))
/*     */     {
/* 219 */       TransducerGraph graph = getGraphFromMap(graphs, parentString);
/* 220 */       Double output = new Double(smartNegate(rule.score()));
/* 221 */       graph.addArc(childString, parentString, END, output);
/* 222 */       graph.setEndNode(parentString);
/* 223 */       return true;
/*     */     }
/* 225 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean addOneBinaryRule(BinaryRule rule, Map graphs)
/*     */   {
/* 231 */     String parentString = (String)this.stateNumberer.object(rule.parent);
/* 232 */     String leftString = (String)this.stateNumberer.object(rule.leftChild);
/* 233 */     String rightString = (String)this.stateNumberer.object(rule.rightChild);
/*     */     
/* 235 */     String bracket = null;
/* 236 */     if (Train.markFinalStates) {
/* 237 */       bracket = parentString.substring(parentString.length() - 1, parentString.length());
/*     */     }
/*     */     String input;
/* 240 */     if (isSyntheticState(leftString)) {
/* 241 */       String source = leftString;
/* 242 */       input = rightString + (bracket == null ? ">" : bracket); } else { String input;
/* 243 */       if (isSyntheticState(rightString)) {
/* 244 */         String source = rightString;
/* 245 */         input = leftString + (bracket == null ? "<" : bracket);
/*     */       }
/*     */       else {
/* 248 */         return false; } }
/*     */     String input;
/* 250 */     String source; String target = parentString;
/* 251 */     Double output = new Double(smartNegate(rule.score()));
/* 252 */     String topcat = getTopCategoryOfSyntheticState(source);
/* 253 */     if (topcat == null) {
/* 254 */       throw new RuntimeException("can't have null topcat");
/*     */     }
/* 256 */     TransducerGraph graph = getGraphFromMap(graphs, topcat);
/* 257 */     graph.addArc(source, target, input, output);
/* 258 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean isSyntheticState(String state) {
/* 262 */     return state.charAt(0) == '@';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pair<UnaryGrammar, BinaryGrammar> convertGraphsToGrammar(Set graphs, Set<UnaryRule> unaryRules, Set<BinaryRule> binaryRules)
/*     */   {
/* 274 */     this.newStateNumberer = new Numberer();
/* 275 */     for (UnaryRule rule : unaryRules) {
/* 276 */       Object parent = this.stateNumberer.object(rule.parent);
/* 277 */       rule.parent = this.newStateNumberer.number(parent);
/* 278 */       Object child = this.stateNumberer.object(rule.child);
/* 279 */       rule.child = this.newStateNumberer.number(child);
/*     */     }
/* 281 */     for (BinaryRule rule : binaryRules) {
/* 282 */       Object parent = this.stateNumberer.object(rule.parent);
/* 283 */       rule.parent = this.newStateNumberer.number(parent);
/* 284 */       Object leftChild = this.stateNumberer.object(rule.leftChild);
/* 285 */       rule.leftChild = this.newStateNumberer.number(leftChild);
/* 286 */       Object rightChild = this.stateNumberer.object(rule.rightChild);
/* 287 */       rule.rightChild = this.newStateNumberer.number(rightChild);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 292 */     Map numbs = Numberer.getNumberers();
/* 293 */     numbs.put(this.stateSpace, this.newStateNumberer);
/*     */     
/*     */ 
/* 296 */     for (Iterator graphIter = graphs.iterator(); graphIter.hasNext();) {
/* 297 */       TransducerGraph graph = (TransducerGraph)graphIter.next();
/* 298 */       startNode = graph.getStartNode();
/* 299 */       for (arcIter = graph.getArcs().iterator(); arcIter.hasNext();) {
/* 300 */         TransducerGraph.Arc arc = (TransducerGraph.Arc)arcIter.next();
/* 301 */         Object source = arc.getSourceNode();
/* 302 */         Object target = arc.getTargetNode();
/* 303 */         Object input = arc.getInput();
/* 304 */         String inputString = input.toString();
/* 305 */         double output = ((Double)arc.getOutput()).doubleValue();
/* 306 */         if (source.equals(startNode))
/*     */         {
/* 308 */           UnaryRule ur = new UnaryRule(this.newStateNumberer.number(target), this.newStateNumberer.number(inputString), smartNegate(output));
/* 309 */           unaryRules.add(ur);
/* 310 */         } else if ((inputString.equals(END)) || (inputString.equals(EPSILON)))
/*     */         {
/* 312 */           UnaryRule ur = new UnaryRule(this.newStateNumberer.number(target), this.newStateNumberer.number(source), smartNegate(output));
/* 313 */           unaryRules.add(ur);
/*     */         }
/*     */         else
/*     */         {
/* 317 */           int length = inputString.length();
/* 318 */           char leftOrRight = inputString.charAt(length - 1);
/* 319 */           inputString = inputString.substring(0, length - 1);
/*     */           BinaryRule br;
/* 321 */           if ((leftOrRight == '<') || (leftOrRight == '[')) {
/* 322 */             br = new BinaryRule(this.newStateNumberer.number(target), this.newStateNumberer.number(inputString), this.newStateNumberer.number(source), smartNegate(output)); } else { BinaryRule br;
/* 323 */             if ((leftOrRight == '>') || (leftOrRight == ']')) {
/* 324 */               br = new BinaryRule(this.newStateNumberer.number(target), this.newStateNumberer.number(source), this.newStateNumberer.number(inputString), smartNegate(output));
/*     */             } else
/* 326 */               throw new RuntimeException("Arc input is in unexpected format: " + arc); }
/*     */           BinaryRule br;
/* 328 */           binaryRules.add(br);
/*     */         }
/*     */       } }
/*     */     Object startNode;
/*     */     Iterator arcIter;
/* 333 */     Counter symbolCounter = new Counter();
/* 334 */     if (this.outputType == RAW_COUNTS)
/*     */     {
/*     */ 
/*     */ 
/* 338 */       for (UnaryRule rule : unaryRules) {
/* 339 */         symbolCounter.incrementCount(this.newStateNumberer.object(rule.parent), rule.score);
/*     */       }
/* 341 */       for (BinaryRule rule : binaryRules) {
/* 342 */         symbolCounter.incrementCount(this.newStateNumberer.object(rule.parent), rule.score);
/*     */       }
/*     */     }
/*     */     
/* 346 */     int numStates = this.newStateNumberer.total();
/* 347 */     int numRules = 0;
/* 348 */     UnaryGrammar ug = new UnaryGrammar(numStates);
/* 349 */     BinaryGrammar bg = new BinaryGrammar(numStates);
/* 350 */     for (UnaryRule rule : unaryRules) {
/* 351 */       if (this.outputType == RAW_COUNTS) {
/* 352 */         double count = symbolCounter.getCount(this.newStateNumberer.object(rule.parent));
/* 353 */         rule.score = ((float)Math.log(rule.score / count));
/*     */       }
/* 355 */       ug.addRule(rule);
/* 356 */       numRules++;
/*     */     }
/* 358 */     for (BinaryRule rule : binaryRules) {
/* 359 */       if (this.outputType == RAW_COUNTS) {
/* 360 */         double count = symbolCounter.getCount(this.newStateNumberer.object(rule.parent));
/* 361 */         rule.score = ((float)Math.log((rule.score - Train.ruleDiscount) / count));
/*     */       }
/* 363 */       bg.addRule(rule);
/* 364 */       numRules++;
/*     */     }
/* 366 */     if (this.verbose) {
/* 367 */       System.out.println("Number of minimized rules: " + numRules);
/* 368 */       System.out.println("Number of minimized states: " + this.newStateNumberer.total());
/*     */     }
/*     */     
/* 371 */     ug.purgeRules();
/* 372 */     bg.splitRules();
/* 373 */     return new Pair(ug, bg);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\GrammarCompactor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */