/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import edu.stanford.nlp.math.SloppyMath;
/*     */ import edu.stanford.nlp.parser.KBestViterbiParser;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.ArrayHeap;
/*     */ import edu.stanford.nlp.util.Heap;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.ScoredObject;
/*     */ import edu.stanford.nlp.util.Timing;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class BiLexPCFGParser implements KBestViterbiParser
/*     */ {
/*     */   protected static final boolean VERBOSE = false;
/*     */   protected static final boolean VERY_VERBOSE = false;
/*     */   protected HookChart chart;
/*     */   protected Heap<Item> agenda;
/*     */   protected int length;
/*     */   protected int[] words;
/*     */   protected Edge goal;
/*     */   protected Interner interner;
/*     */   protected Scorer scorer;
/*     */   protected ExhaustivePCFGParser fscorer;
/*     */   protected ExhaustiveDependencyParser dparser;
/*     */   protected GrammarProjection projection;
/*     */   protected BinaryGrammar bg;
/*     */   protected UnaryGrammar ug;
/*     */   protected DependencyGrammar dg;
/*     */   protected Lexicon lex;
/*     */   protected Options op;
/*     */   protected List<IntTaggedWord>[] taggedWordList;
/*  45 */   protected Numberer wordNumberer = Numberer.getGlobalNumberer("words");
/*  46 */   protected Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
/*  47 */   protected Numberer stateNumberer = Numberer.getGlobalNumberer("states");
/*     */   
/*  49 */   protected TreeFactory tf = new LabeledScoredTreeFactory();
/*     */   
/*     */ 
/*  52 */   protected long relaxHook1 = 0L;
/*  53 */   protected long relaxHook2 = 0L;
/*  54 */   protected long relaxHook3 = 0L;
/*  55 */   protected long relaxHook4 = 0L;
/*     */   
/*  57 */   protected long builtHooks = 0L;
/*  58 */   protected long builtEdges = 0L;
/*  59 */   protected long extractedHooks = 0L;
/*  60 */   protected long extractedEdges = 0L;
/*     */   
/*     */   private static final double TOL = 1.0E-10D;
/*     */   
/*     */   protected static boolean better(double x, double y)
/*     */   {
/*  66 */     return (x - y) / (Math.abs(x) + Math.abs(y) + 1.0E-100D) > 1.0E-10D;
/*     */   }
/*     */   
/*     */   public double getBestScore()
/*     */   {
/*  71 */     if (this.goal == null) {
/*  72 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/*  74 */     return this.goal.score();
/*     */   }
/*     */   
/*     */ 
/*     */   protected Tree extractParse(Edge edge)
/*     */   {
/*  80 */     String head = (String)this.wordNumberer.object(this.words[edge.head]);
/*  81 */     String tag = (String)this.tagNumberer.object(edge.tag);
/*  82 */     String state = (String)this.stateNumberer.object(edge.state);
/*  83 */     Label label = new edu.stanford.nlp.ling.CategoryWordTag(state, head, tag);
/*  84 */     if ((edge.backEdge == null) && (edge.backHook == null))
/*     */     {
/*  86 */       List<Tree> childList = Collections.singletonList(this.tf.newLeaf(new StringLabel(head)));
/*  87 */       return this.tf.newTreeNode(label, childList);
/*     */     }
/*  89 */     if (edge.backHook == null)
/*     */     {
/*  91 */       List<Tree> childList = Collections.singletonList(extractParse(edge.backEdge));
/*  92 */       return this.tf.newTreeNode(label, childList);
/*     */     }
/*     */     
/*  95 */     List<Tree> children = new ArrayList();
/*  96 */     if (edge.backHook.isPreHook()) {
/*  97 */       children.add(extractParse(edge.backEdge));
/*  98 */       children.add(extractParse(edge.backHook.backEdge));
/*     */     } else {
/* 100 */       children.add(extractParse(edge.backHook.backEdge));
/* 101 */       children.add(extractParse(edge.backEdge));
/*     */     }
/* 103 */     return this.tf.newTreeNode(label, children);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree getBestParse()
/*     */   {
/* 112 */     return extractParse(this.goal);
/*     */   }
/*     */   
/*     */   public boolean hasParse()
/*     */   {
/* 117 */     return (this.goal != null) && (this.goal.iScore != Double.NEGATIVE_INFINITY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 122 */   protected List<Edge> nGoodTrees = new LinkedList();
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
/*     */   public List<ScoredObject<Tree>> getKGoodParses(int k)
/*     */   {
/* 135 */     List<ScoredObject<Tree>> nGoodTreesList = new ArrayList(Test.printFactoredKGood);
/* 136 */     for (Edge e : this.nGoodTrees) {
/* 137 */       nGoodTreesList.add(new ScoredObject(extractParse(e), e.iScore));
/*     */     }
/* 139 */     return nGoodTreesList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ScoredObject<Tree>> getKBestParses(int k)
/*     */   {
/* 150 */     throw new UnsupportedOperationException("BiLexPCFGParser doesn't support k best parses");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ScoredObject<Tree>> getBestParses()
/*     */   {
/* 161 */     throw new UnsupportedOperationException("BiLexPCFGParser doesn't support best parses");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ScoredObject<Tree>> getKSampledParses(int k)
/*     */   {
/* 172 */     throw new UnsupportedOperationException("BiLexPCFGParser doesn't support k sampled parses");
/*     */   }
/*     */   
/*     */ 
/* 176 */   protected Edge tempEdge = new Edge();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void combine(Edge edge, Hook hook)
/*     */   {
/* 183 */     if (hook.isPreHook()) {
/* 184 */       this.tempEdge.start = edge.start;
/* 185 */       this.tempEdge.end = hook.end;
/*     */     } else {
/* 187 */       this.tempEdge.start = hook.start;
/* 188 */       this.tempEdge.end = edge.end;
/*     */     }
/* 190 */     this.tempEdge.state = hook.state;
/* 191 */     this.tempEdge.head = hook.head;
/* 192 */     this.tempEdge.tag = hook.tag;
/* 193 */     this.tempEdge.iScore = (hook.iScore + edge.iScore);
/* 194 */     this.tempEdge.backEdge = edge;
/* 195 */     this.tempEdge.backHook = hook;
/* 196 */     relaxTempEdge();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void relaxTempEdge()
/*     */   {
/* 203 */     Edge resultEdge = (Edge)this.interner.intern(this.tempEdge);
/*     */     
/*     */ 
/*     */ 
/* 207 */     if (resultEdge == this.tempEdge) {
/* 208 */       this.tempEdge = new Edge();
/* 209 */       discoverEdge(resultEdge);
/*     */     }
/* 211 */     else if ((better(this.tempEdge.iScore, resultEdge.iScore)) && (resultEdge.oScore > Double.NEGATIVE_INFINITY))
/*     */     {
/* 213 */       double back = resultEdge.iScore;
/* 214 */       Edge backE = resultEdge.backEdge;
/* 215 */       Hook backH = resultEdge.backHook;
/* 216 */       resultEdge.iScore = this.tempEdge.iScore;
/* 217 */       resultEdge.backEdge = this.tempEdge.backEdge;
/* 218 */       resultEdge.backHook = this.tempEdge.backHook;
/*     */       try {
/* 220 */         this.agenda.decreaseKey(resultEdge);
/*     */       }
/*     */       catch (NullPointerException e) {}
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
/*     */   protected void discoverEdge(Edge edge)
/*     */   {
/* 240 */     edge.oScore = this.scorer.oScore(edge);
/* 241 */     this.agenda.add(edge);
/* 242 */     this.builtEdges += 1L;
/*     */   }
/*     */   
/*     */   protected void discoverHook(Hook hook) {
/* 246 */     hook.oScore = buildOScore(hook);
/* 247 */     if (hook.oScore == Double.NEGATIVE_INFINITY) {
/* 248 */       this.relaxHook4 += 1L;
/*     */     }
/* 250 */     this.builtHooks += 1L;
/* 251 */     this.agenda.add(hook);
/*     */   }
/*     */   
/* 254 */   protected Edge iTemp = new Edge();
/* 255 */   protected Edge oTemp = new Edge();
/*     */   
/*     */   protected double buildOScore(Hook hook) {
/* 258 */     double bestOScore = Double.NEGATIVE_INFINITY;
/* 259 */     this.iTemp.head = hook.head;
/* 260 */     this.iTemp.tag = hook.tag;
/* 261 */     this.iTemp.state = hook.subState;
/* 262 */     this.oTemp.head = hook.head;
/* 263 */     this.oTemp.tag = hook.tag;
/* 264 */     this.oTemp.state = hook.state;
/* 265 */     if (hook.isPreHook()) {
/* 266 */       this.iTemp.end = hook.start;
/* 267 */       this.oTemp.end = hook.end;
/* 268 */       for (int start = 0; start <= hook.head; start++) {
/* 269 */         this.iTemp.start = start;
/* 270 */         this.oTemp.start = start;
/* 271 */         double oScore = this.scorer.oScore(this.oTemp) + this.scorer.iScore(this.iTemp);
/*     */         
/* 273 */         bestOScore = SloppyMath.max(bestOScore, oScore);
/*     */       }
/*     */     } else {
/* 276 */       this.iTemp.start = hook.end;
/* 277 */       this.oTemp.start = hook.start;
/* 278 */       for (int end = hook.head + 1; end <= this.length; end++) {
/* 279 */         this.iTemp.end = end;
/* 280 */         this.oTemp.end = end;
/* 281 */         double oScore = this.scorer.oScore(this.oTemp) + this.scorer.iScore(this.iTemp);
/* 282 */         bestOScore = SloppyMath.max(bestOScore, oScore);
/*     */       }
/*     */     }
/* 285 */     return bestOScore;
/*     */   }
/*     */   
/* 288 */   protected Hook tempHook = new Hook();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void projectHooks(Edge edge)
/*     */   {
/* 295 */     List<BinaryRule> ruleList = this.bg.ruleListByLeftChild(edge.state);
/* 296 */     int r = 0; for (int rsz = ruleList.size(); r < rsz; r++)
/*     */     {
/* 298 */       BinaryRule br = (BinaryRule)ruleList.get(r);
/* 299 */       if ((this.fscorer.oPossibleL(project(br.parent), edge.start)) && (this.fscorer.iPossibleL(project(br.rightChild), edge.end)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 304 */         for (int head = edge.end; head < this.length; head++)
/*     */         {
/*     */ 
/*     */ 
/* 308 */           int hdi = 0; for (int sz = this.taggedWordList[head].size(); hdi < sz; hdi++) {
/* 309 */             IntTaggedWord iTW = (IntTaggedWord)this.taggedWordList[head].get(hdi);
/* 310 */             int tag = iTW.tag;
/* 311 */             this.tempHook.start = edge.start;
/* 312 */             this.tempHook.end = edge.end;
/* 313 */             this.tempHook.head = head;
/* 314 */             this.tempHook.tag = tag;
/* 315 */             this.tempHook.state = br.parent;
/* 316 */             this.tempHook.subState = br.rightChild;
/* 317 */             if (this.chart.isBuiltL(this.tempHook.subState, this.tempHook.end, this.tempHook.head, this.tempHook.tag))
/*     */             {
/*     */ 
/* 320 */               this.tempHook.iScore = (edge.iScore + br.score + this.dparser.headScore[this.dparser.binDistance[head][edge.end]][head][this.dg.tagBin(tag)][edge.head][this.dg.tagBin(edge.tag)] + this.dparser.headStop[edge.head][this.dg.tagBin(edge.tag)][edge.start] + this.dparser.headStop[edge.head][this.dg.tagBin(edge.tag)][edge.end]);
/* 321 */               this.tempHook.backEdge = edge;
/* 322 */               relaxTempHook();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 329 */     ruleList = this.bg.ruleListByRightChild(edge.state);
/* 330 */     int r = 0; for (int rlSize = ruleList.size(); r < rlSize; r++)
/*     */     {
/* 332 */       BinaryRule br = (BinaryRule)ruleList.get(r);
/* 333 */       if ((this.fscorer.oPossibleR(project(br.parent), edge.end)) && (this.fscorer.iPossibleR(project(br.leftChild), edge.start)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 338 */         for (int head = 0; head < edge.start; head++)
/*     */         {
/*     */ 
/*     */ 
/* 342 */           int hdi = 0; for (int sz = this.taggedWordList[head].size(); hdi < sz; hdi++) {
/* 343 */             IntTaggedWord iTW = (IntTaggedWord)this.taggedWordList[head].get(hdi);
/* 344 */             int tag = iTW.tag;
/* 345 */             this.tempHook.start = edge.start;
/* 346 */             this.tempHook.end = edge.end;
/* 347 */             this.tempHook.head = head;
/* 348 */             this.tempHook.tag = tag;
/* 349 */             this.tempHook.state = br.parent;
/* 350 */             this.tempHook.subState = br.leftChild;
/* 351 */             if (this.chart.isBuiltR(this.tempHook.subState, this.tempHook.start, this.tempHook.head, this.tempHook.tag))
/*     */             {
/*     */ 
/* 354 */               this.tempHook.iScore = (edge.iScore + br.score + this.dparser.headScore[this.dparser.binDistance[head][edge.start]][head][this.dg.tagBin(tag)][edge.head][this.dg.tagBin(edge.tag)] + this.dparser.headStop[edge.head][this.dg.tagBin(edge.tag)][edge.start] + this.dparser.headStop[edge.head][this.dg.tagBin(edge.tag)][edge.end]);
/* 355 */               this.tempHook.backEdge = edge;
/* 356 */               relaxTempHook();
/*     */             }
/*     */           }
/*     */         } }
/*     */     }
/*     */   }
/*     */   
/* 363 */   protected void registerReal(Edge real) { this.chart.registerRealEdge(real); }
/*     */   
/*     */ 
/*     */   protected void triggerHooks(Edge edge)
/*     */   {
/* 368 */     boolean newL = !this.chart.isBuiltL(edge.state, edge.start, edge.head, edge.tag);
/* 369 */     boolean newR = !this.chart.isBuiltR(edge.state, edge.end, edge.head, edge.tag);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 378 */     this.chart.registerEdgeIndexes(edge);
/* 379 */     if (newR)
/*     */     {
/* 381 */       BinaryRule[] rules = this.bg.splitRulesWithLC(edge.state);
/* 382 */       BinaryRule br; Iterator realI; for (int i = 0; i < rules.length; i++) {
/* 383 */         br = rules[i];
/* 384 */         Collection realEdges = this.chart.getRealEdgesWithL(br.rightChild, edge.end);
/* 385 */         for (realI = realEdges.iterator(); realI.hasNext();) {
/* 386 */           Edge real = (Edge)realI.next();
/* 387 */           this.tempHook.start = real.start;
/* 388 */           this.tempHook.end = real.end;
/* 389 */           this.tempHook.state = br.parent;
/* 390 */           this.tempHook.subState = br.leftChild;
/* 391 */           this.tempHook.head = edge.head;
/* 392 */           this.tempHook.tag = edge.tag;
/* 393 */           this.tempHook.backEdge = real;
/* 394 */           this.tempHook.iScore = (real.iScore + br.score + this.dparser.headScore[this.dparser.binDistance[edge.head][edge.end]][edge.head][this.dg.tagBin(edge.tag)][real.head][this.dg.tagBin(real.tag)] + this.dparser.headStop[real.head][this.dg.tagBin(real.tag)][real.start] + this.dparser.headStop[real.head][this.dg.tagBin(real.tag)][real.end]);
/* 395 */           relaxTempHook();
/*     */         }
/*     */       }
/*     */     }
/* 399 */     if (newL)
/*     */     {
/* 401 */       BinaryRule[] rules = this.bg.splitRulesWithRC(edge.state);
/* 402 */       BinaryRule br; Iterator realI; for (int i = 0; i < rules.length; i++) {
/* 403 */         br = rules[i];
/* 404 */         Collection realEdges = this.chart.getRealEdgesWithR(br.leftChild, edge.start);
/* 405 */         for (realI = realEdges.iterator(); realI.hasNext();) {
/* 406 */           Edge real = (Edge)realI.next();
/* 407 */           this.tempHook.start = real.start;
/* 408 */           this.tempHook.end = real.end;
/* 409 */           this.tempHook.state = br.parent;
/* 410 */           this.tempHook.subState = br.rightChild;
/* 411 */           this.tempHook.head = edge.head;
/* 412 */           this.tempHook.tag = edge.tag;
/* 413 */           this.tempHook.backEdge = real;
/* 414 */           this.tempHook.iScore = (real.iScore + br.score + this.dparser.headScore[this.dparser.binDistance[edge.head][edge.start]][edge.head][this.dg.tagBin(edge.tag)][real.head][this.dg.tagBin(real.tag)] + this.dparser.headStop[real.head][this.dg.tagBin(real.tag)][real.start] + this.dparser.headStop[real.head][this.dg.tagBin(real.tag)][real.end]);
/* 415 */           relaxTempHook();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void triggerAllHooks(Edge edge)
/*     */   {
/* 423 */     boolean newL = !this.chart.isBuiltL(edge.state, edge.start, edge.head, edge.tag);
/* 424 */     boolean newR = !this.chart.isBuiltR(edge.state, edge.end, edge.head, edge.tag);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 433 */     this.chart.registerEdgeIndexes(edge);
/* 434 */     Iterator<BinaryRule> rI; if (newR)
/*     */     {
/* 436 */       for (rI = this.bg.ruleIteratorByLeftChild(edge.state); rI.hasNext();) {
/* 437 */         br = (BinaryRule)rI.next();
/* 438 */         Collection edges = this.chart.getRealEdgesWithL(br.rightChild, edge.end);
/* 439 */         for (realI = edges.iterator(); realI.hasNext();) {
/* 440 */           Edge real = (Edge)realI.next();
/* 441 */           this.tempHook.start = real.start;
/* 442 */           this.tempHook.end = real.end;
/* 443 */           this.tempHook.state = br.parent;
/* 444 */           this.tempHook.subState = br.leftChild;
/* 445 */           this.tempHook.head = edge.head;
/* 446 */           this.tempHook.tag = edge.tag;
/* 447 */           this.tempHook.backEdge = real;
/* 448 */           this.tempHook.iScore = (real.iScore + br.score + this.dparser.headScore[this.dparser.binDistance[edge.head][edge.end]][edge.head][this.dg.tagBin(edge.tag)][real.head][this.dg.tagBin(real.tag)] + this.dparser.headStop[real.head][this.dg.tagBin(real.tag)][real.start] + this.dparser.headStop[real.head][this.dg.tagBin(real.tag)][real.end]);
/* 449 */           relaxTempHook(); } } }
/*     */     BinaryRule br;
/*     */     Iterator realI;
/*     */     Iterator rI;
/* 453 */     if (newL)
/*     */     {
/* 455 */       for (rI = this.bg.ruleIteratorByRightChild(edge.state); rI.hasNext();) {
/* 456 */         br = (BinaryRule)rI.next();
/* 457 */         Collection<Edge> edges = this.chart.getRealEdgesWithR(br.leftChild, edge.start);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 462 */         for (Edge real : edges) {
/* 463 */           this.tempHook.start = real.start;
/* 464 */           this.tempHook.end = real.end;
/* 465 */           this.tempHook.state = br.parent;
/* 466 */           this.tempHook.subState = br.rightChild;
/* 467 */           this.tempHook.head = edge.head;
/* 468 */           this.tempHook.tag = edge.tag;
/* 469 */           this.tempHook.backEdge = real;
/* 470 */           this.tempHook.iScore = (real.iScore + br.score + this.dparser.headScore[this.dparser.binDistance[edge.head][edge.start]][edge.head][this.dg.tagBin(edge.tag)][real.head][this.dg.tagBin(real.tag)] + this.dparser.headStop[real.head][this.dg.tagBin(real.tag)][real.start] + this.dparser.headStop[real.head][this.dg.tagBin(real.tag)][real.end]);
/* 471 */           relaxTempHook();
/*     */         }
/*     */       }
/*     */     }
/*     */     BinaryRule br;
/*     */   }
/*     */   
/*     */   protected void relaxTempHook() {
/* 479 */     this.relaxHook1 += 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 484 */     if ((!this.scorer.oPossible(this.tempHook)) || (!this.scorer.iPossible(this.tempHook))) {
/* 485 */       return;
/*     */     }
/*     */     
/* 488 */     this.relaxHook2 += 1L;
/* 489 */     Hook resultHook = (Hook)this.interner.intern(this.tempHook);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 496 */     if (resultHook == this.tempHook) {
/* 497 */       this.relaxHook3 += 1L;
/* 498 */       this.tempHook = new Hook();
/* 499 */       discoverHook(resultHook);
/*     */     }
/* 501 */     if (better(this.tempHook.iScore, resultHook.iScore)) {
/* 502 */       resultHook.iScore = this.tempHook.iScore;
/* 503 */       resultHook.backEdge = this.tempHook.backEdge;
/*     */       try {
/* 505 */         this.agenda.decreaseKey(resultHook);
/*     */       }
/*     */       catch (NullPointerException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */   protected void projectUnaries(Edge edge) {
/* 512 */     for (Iterator rI = this.ug.ruleIteratorByChild(edge.state); rI.hasNext();) {
/* 513 */       UnaryRule ur = (UnaryRule)rI.next();
/* 514 */       if (ur.child != ur.parent)
/*     */       {
/*     */ 
/* 517 */         this.tempEdge.start = edge.start;
/* 518 */         this.tempEdge.end = edge.end;
/* 519 */         this.tempEdge.head = edge.head;
/* 520 */         this.tempEdge.tag = edge.tag;
/* 521 */         this.tempEdge.state = ur.parent;
/* 522 */         this.tempEdge.backEdge = edge;
/* 523 */         this.tempEdge.backHook = null;
/* 524 */         this.tempEdge.iScore = (edge.iScore + ur.score);
/* 525 */         relaxTempEdge();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void processEdge(Edge edge)
/*     */   {
/* 534 */     this.chart.addEdge(edge);
/*     */     
/* 536 */     for (Hook hook : this.chart.getPreHooks(edge)) {
/* 537 */       combine(edge, hook);
/*     */     }
/* 539 */     for (Hook hook : this.chart.getPostHooks(edge)) {
/* 540 */       combine(edge, hook);
/*     */     }
/*     */     
/*     */ 
/* 544 */     projectUnaries(edge);
/* 545 */     if ((!this.bg.isSynthetic(edge.state)) && (!this.op.freeDependencies)) {
/* 546 */       projectHooks(edge);
/* 547 */       registerReal(edge);
/*     */     }
/* 549 */     if (this.op.freeDependencies) {
/* 550 */       projectHooks(edge);
/* 551 */       registerReal(edge);
/* 552 */       triggerAllHooks(edge);
/*     */     } else {
/* 554 */       triggerHooks(edge);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void processHook(Hook hook)
/*     */   {
/* 561 */     this.chart.addHook(hook);
/* 562 */     Collection edges = this.chart.getEdges(hook);
/* 563 */     for (Iterator edgeI = edges.iterator(); edgeI.hasNext();) {
/* 564 */       Edge edge = (Edge)edgeI.next();
/* 565 */       combine(edge, hook);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void processItem(Item item) {
/* 570 */     if (item.isEdge()) {
/* 571 */       processEdge((Edge)item);
/*     */     } else {
/* 573 */       processHook((Hook)item);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void discoverItem(Item item) {
/* 578 */     if (item.isEdge()) {
/* 579 */       discoverEdge((Edge)item);
/*     */     } else {
/* 581 */       discoverHook((Hook)item);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Item makeInitialItem(int pos, int tag, int state, double iScore) {
/* 586 */     Edge edge = new Edge();
/* 587 */     edge.start = pos;
/* 588 */     edge.end = (pos + 1);
/* 589 */     edge.state = state;
/* 590 */     edge.head = pos;
/* 591 */     edge.tag = tag;
/* 592 */     edge.iScore = iScore;
/* 593 */     return edge;
/*     */   }
/*     */   
/*     */   protected List<Item> makeInitialItems(List wordList) {
/* 597 */     List<Item> itemList = new ArrayList();
/* 598 */     int length = wordList.size();
/* 599 */     int numTags = this.tagNumberer.total();
/* 600 */     this.words = new int[length];
/* 601 */     this.taggedWordList = new List[length];
/* 602 */     int terminalCount = 0;
/* 603 */     int word; Iterator<IntTaggedWord> tagI; for (int i = 0; i < length; i++) {
/* 604 */       this.taggedWordList[i] = new ArrayList(numTags);
/* 605 */       String wordStr = "";
/* 606 */       Object wordObject = wordList.get(i);
/* 607 */       if ((wordObject instanceof HasWord)) {
/* 608 */         wordStr = ((HasWord)wordObject).word();
/*     */       } else {
/* 610 */         wordStr = wordObject.toString();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 616 */       if (!this.wordNumberer.hasSeen(wordStr)) {
/* 617 */         wordStr = "UNK";
/*     */       }
/* 619 */       word = this.wordNumberer.number(wordStr);
/* 620 */       this.words[i] = word;
/* 621 */       for (tagI = this.lex.ruleIteratorByWord(word, i); tagI.hasNext();) {
/* 622 */         IntTaggedWord tagging = (IntTaggedWord)tagI.next();
/* 623 */         int tag = tagging.tag;
/*     */         
/*     */ 
/*     */ 
/* 627 */         int state = Numberer.translate("tags", "states", tag);
/*     */         
/*     */ 
/* 630 */         this.tempEdge.state = state;
/* 631 */         this.tempEdge.head = i;
/* 632 */         this.tempEdge.start = i;
/* 633 */         this.tempEdge.end = (i + 1);
/* 634 */         this.tempEdge.tag = tag;
/* 635 */         itemList.add(makeInitialItem(i, tag, state, this.scorer.iScore(this.tempEdge)));
/* 636 */         terminalCount++;
/* 637 */         this.taggedWordList[i].add(new IntTaggedWord(word, tag));
/*     */       }
/*     */     }
/* 640 */     if (Test.verbose) {
/* 641 */       System.err.println("Terminals (# of tag edges in chart): " + terminalCount);
/*     */     }
/*     */     
/* 644 */     return itemList;
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
/*     */   protected void scoreDependencies() {}
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
/*     */   protected void setGoal(int length)
/*     */   {
/* 684 */     this.goal = new Edge();
/* 685 */     this.goal.start = 0;
/* 686 */     this.goal.end = length;
/* 687 */     this.goal.state = this.stateNumberer.number(this.op.langpack().startSymbol());
/* 688 */     this.goal.tag = this.tagNumberer.number(".$$.");
/* 689 */     this.goal.head = (length - 1);
/*     */   }
/*     */   
/*     */   protected void initialize(List words)
/*     */   {
/* 694 */     this.length = words.size();
/* 695 */     this.interner = new Interner();
/* 696 */     this.agenda = new ArrayHeap(edu.stanford.nlp.util.ScoredComparator.DESCENDING_COMPARATOR);
/* 697 */     this.chart = new HookChart();
/* 698 */     setGoal(this.length);
/* 699 */     List<Item> initialItems = makeInitialItems(words);
/* 700 */     scoreDependencies();
/* 701 */     int i = 0; for (int iiSize = initialItems.size(); i < iiSize; i++) {
/* 702 */       Item item = (Item)initialItems.get(i);
/* 703 */       item = (Item)this.interner.intern(item);
/*     */       
/* 705 */       discoverItem(item);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean parse(List<? extends HasWord> sentence, String goal)
/*     */   {
/* 717 */     return parse(sentence);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean parse(List<? extends HasWord> words)
/*     */   {
/* 727 */     int nGoodRemaining = 0;
/* 728 */     if (Test.printFactoredKGood > 0) {
/* 729 */       nGoodRemaining = Test.printFactoredKGood;
/* 730 */       this.nGoodTrees.clear();
/*     */     }
/*     */     
/* 733 */     int spanFound = 0;
/* 734 */     long last = 0L;
/* 735 */     int exHook = 0;
/* 736 */     this.relaxHook1 = 0L;
/* 737 */     this.relaxHook2 = 0L;
/* 738 */     this.relaxHook3 = 0L;
/* 739 */     this.relaxHook4 = 0L;
/* 740 */     this.builtHooks = 0L;
/* 741 */     this.builtEdges = 0L;
/* 742 */     this.extractedHooks = 0L;
/* 743 */     this.extractedEdges = 0L;
/* 744 */     if (Test.verbose) {
/* 745 */       Timing.tick("Starting combined parse.");
/*     */     }
/* 747 */     this.dparser.binDistance = this.dparser.binDistance;
/* 748 */     initialize(words);
/* 749 */     while (!this.agenda.isEmpty()) {
/* 750 */       Item item = (Item)this.agenda.extractMin();
/* 751 */       if (!item.isEdge()) {
/* 752 */         exHook++;
/* 753 */         this.extractedHooks += 1L;
/*     */       } else {
/* 755 */         this.extractedEdges += 1L;
/*     */       }
/* 757 */       if (this.relaxHook1 > last + 1000000L) {
/* 758 */         last = this.relaxHook1;
/* 759 */         if (Test.verbose) {
/* 760 */           System.err.println("Proposed hooks:   " + this.relaxHook1);
/* 761 */           System.err.println("Unfiltered hooks: " + this.relaxHook2);
/* 762 */           System.err.println("Built hooks:      " + this.relaxHook3);
/* 763 */           System.err.println("Waste hooks:      " + this.relaxHook4);
/* 764 */           System.err.println("Extracted hooks:  " + exHook);
/*     */         }
/*     */       }
/* 767 */       if (item.end - item.start > spanFound) {
/* 768 */         spanFound = item.end - item.start;
/* 769 */         if (Test.verbose) {
/* 770 */           System.err.print(spanFound + " ");
/*     */         }
/*     */       }
/*     */       
/* 774 */       if (item.equals(this.goal)) {
/* 775 */         if (Test.verbose) {
/* 776 */           System.err.println("Found goal!");
/* 777 */           System.err.println("Comb iScore " + item.iScore);
/* 778 */           Timing.tick("Done, parse found.");
/* 779 */           System.err.println("Built items:      " + (this.builtEdges + this.builtHooks));
/* 780 */           System.err.println("Built hooks:      " + this.builtHooks);
/* 781 */           System.err.println("Built edges:      " + this.builtEdges);
/* 782 */           System.err.println("Extracted items:  " + (this.extractedEdges + this.extractedHooks));
/* 783 */           System.err.println("Extracted hooks:  " + this.extractedHooks);
/* 784 */           System.err.println("Extracted edges:  " + this.extractedEdges);
/*     */         }
/*     */         
/* 787 */         if (Test.printFactoredKGood <= 0) {
/* 788 */           this.goal = ((Edge)item);
/* 789 */           this.interner = null;
/* 790 */           this.agenda = null;
/* 791 */           return true;
/*     */         }
/*     */         
/* 794 */         this.goal = ((Edge)item);
/* 795 */         this.nGoodTrees.add(this.goal);
/* 796 */         nGoodRemaining--;
/* 797 */         if (nGoodRemaining <= 0)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 805 */           this.interner = null;
/* 806 */           this.agenda = null;
/* 807 */           return true;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 812 */       if (item.score() == Double.NEGATIVE_INFINITY)
/*     */       {
/* 814 */         if (this.nGoodTrees.size() > 0)
/*     */         {
/*     */ 
/*     */ 
/* 818 */           this.goal = ((Edge)this.nGoodTrees.get(0));
/* 819 */           this.interner = null;
/* 820 */           this.agenda = null;
/* 821 */           return true;
/*     */         }
/* 823 */         System.err.println("FactoredParser: no consistent parse [hit A*-blocked edges, aborting].");
/* 824 */         if (Test.verbose) {
/* 825 */           Timing.tick("FactoredParser: no consistent parse [hit A*-blocked edges, aborting].");
/*     */         }
/* 827 */         return false;
/*     */       }
/*     */       
/* 830 */       if ((Test.MAX_ITEMS > 0) && (this.builtEdges + this.builtHooks >= Test.MAX_ITEMS))
/*     */       {
/* 832 */         if (this.nGoodTrees.size() > 0) {
/* 833 */           System.err.println("DEBUG: aborting search because of reaching the MAX_ITEMS work limit [" + Test.MAX_ITEMS + " items]");
/*     */           
/* 835 */           this.goal = ((Edge)this.nGoodTrees.get(0));
/* 836 */           this.interner = null;
/* 837 */           this.agenda = null;
/* 838 */           return true;
/*     */         }
/* 840 */         System.err.println("FactoredParser: exceeded MAX_ITEMS work limit [" + Test.MAX_ITEMS + " items]; aborting.");
/*     */         
/* 842 */         if (Test.verbose) {
/* 843 */           Timing.tick("FactoredParser: exceeded MAX_ITEMS work limit [" + Test.MAX_ITEMS + " items]; aborting.");
/*     */         }
/*     */         
/* 846 */         return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 854 */       processItem(item);
/*     */     }
/*     */     
/*     */ 
/* 858 */     if (this.nGoodTrees.size() > 0) {
/* 859 */       System.err.println("DEBUG: aborting search because of empty agenda");
/* 860 */       this.goal = ((Edge)this.nGoodTrees.get(0));
/* 861 */       this.interner = null;
/* 862 */       this.agenda = null;
/* 863 */       return true;
/*     */     }
/* 865 */     System.err.println("FactoredParser: emptied agenda, no parse found!");
/* 866 */     if (Test.verbose) {
/* 867 */       Timing.tick("FactoredParser: emptied agenda, no parse found!");
/*     */     }
/* 869 */     return false;
/*     */   }
/*     */   
/*     */   protected void postMortem()
/*     */   {
/* 874 */     int numHooks = 0;
/* 875 */     int numEdges = 0;
/* 876 */     int numUnmatchedHooks = 0;
/* 877 */     int total = this.agenda.size();
/* 878 */     int done = 0;
/* 879 */     while (!this.agenda.isEmpty()) {
/* 880 */       Item item = (Item)this.agenda.extractMin();
/* 881 */       done++;
/*     */       
/*     */ 
/* 884 */       if (item.isEdge()) {
/* 885 */         numEdges++;
/*     */       } else {
/* 887 */         numHooks++;
/* 888 */         Collection edges = this.chart.getEdges((Hook)item);
/* 889 */         if (edges.size() == 0) {
/* 890 */           numUnmatchedHooks++;
/*     */         }
/*     */       }
/*     */     }
/* 894 */     System.err.println("--- Agenda Post-Mortem ---");
/* 895 */     System.err.println("Edges:           " + numEdges);
/* 896 */     System.err.println("Hooks:           " + numHooks);
/* 897 */     System.err.println("Unmatched Hooks: " + numUnmatchedHooks);
/*     */   }
/*     */   
/*     */   protected int project(int state) {
/* 901 */     return this.projection.project(state);
/*     */   }
/*     */   
/*     */   BiLexPCFGParser(Scorer scorer, ExhaustivePCFGParser fscorer, ExhaustiveDependencyParser dparser, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Lexicon lex, Options op) {
/* 905 */     this(scorer, fscorer, dparser, bg, ug, dg, lex, op, new NullGrammarProjection(bg, ug));
/*     */   }
/*     */   
/*     */   BiLexPCFGParser(Scorer scorer, ExhaustivePCFGParser fscorer, ExhaustiveDependencyParser dparser, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Lexicon lex, Options op, GrammarProjection projection) {
/* 909 */     this.fscorer = fscorer;
/* 910 */     this.projection = projection;
/* 911 */     this.dparser = dparser;
/* 912 */     this.scorer = scorer;
/* 913 */     this.bg = bg;
/* 914 */     this.ug = ug;
/* 915 */     this.dg = dg;
/* 916 */     this.lex = lex;
/* 917 */     this.op = op;
/*     */   }
/*     */   
/*     */   public static class N5BiLexPCFGParser extends BiLexPCFGParser
/*     */   {
/*     */     protected void relaxTempHook() {
/* 923 */       this.relaxHook1 += 1L;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 928 */       if ((!this.scorer.oPossible(this.tempHook)) || (!this.scorer.iPossible(this.tempHook))) {
/* 929 */         return;
/*     */       }
/*     */       
/* 932 */       this.relaxHook2 += 1L;
/* 933 */       Hook resultHook = this.tempHook;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 938 */       if (resultHook == this.tempHook) {
/* 939 */         this.relaxHook3 += 1L;
/* 940 */         this.tempHook = new Hook();
/* 941 */         processHook(resultHook);
/* 942 */         this.builtHooks += 1L;
/*     */       }
/*     */     }
/*     */     
/*     */     N5BiLexPCFGParser(Scorer scorer, ExhaustivePCFGParser fscorer, ExhaustiveDependencyParser leach, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Lexicon lex, Options op) {
/* 947 */       super(fscorer, leach, bg, ug, dg, lex, op, new NullGrammarProjection(bg, ug));
/*     */     }
/*     */     
/*     */     N5BiLexPCFGParser(Scorer scorer, ExhaustivePCFGParser fscorer, ExhaustiveDependencyParser leach, BinaryGrammar bg, UnaryGrammar ug, DependencyGrammar dg, Lexicon lex, Options op, GrammarProjection proj) {
/* 951 */       super(fscorer, leach, bg, ug, dg, lex, op, proj);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\BiLexPCFGParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */