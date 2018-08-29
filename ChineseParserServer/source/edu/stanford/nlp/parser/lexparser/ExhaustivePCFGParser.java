/*      */ package edu.stanford.nlp.parser.lexparser;
/*      */ 
/*      */ import edu.stanford.nlp.ling.HasTag;
/*      */ import edu.stanford.nlp.ling.HasWord;
/*      */ import edu.stanford.nlp.ling.Label;
/*      */ import edu.stanford.nlp.ling.Sentence;
/*      */ import edu.stanford.nlp.ling.StringLabel;
/*      */ import edu.stanford.nlp.ling.StringLabelFactory;
/*      */ import edu.stanford.nlp.math.SloppyMath;
/*      */ import edu.stanford.nlp.parser.KBestViterbiParser;
/*      */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*      */ import edu.stanford.nlp.trees.Tree;
/*      */ import edu.stanford.nlp.trees.TreeFactory;
/*      */ import edu.stanford.nlp.trees.TreeTransformer;
/*      */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*      */ import edu.stanford.nlp.util.BinaryHeapPriorityQueue;
/*      */ import edu.stanford.nlp.util.Numberer;
/*      */ import edu.stanford.nlp.util.PriorityQueue;
/*      */ import edu.stanford.nlp.util.ScoredObject;
/*      */ import edu.stanford.nlp.util.Timing;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
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
/*      */ public class ExhaustivePCFGParser
/*      */   implements Scorer, KBestViterbiParser
/*      */ {
/*      */   protected String goalStr;
/*      */   protected String stateSpace;
/*      */   protected Numberer stateNumberer;
/*   58 */   protected Numberer wordNumberer = Numberer.getGlobalNumberer("words");
/*   59 */   protected Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
/*      */   
/*      */   protected TreeFactory tf;
/*      */   
/*      */   protected BinaryGrammar bg;
/*      */   
/*      */   protected UnaryGrammar ug;
/*      */   
/*      */   protected Lexicon lex;
/*      */   
/*      */   protected Options op;
/*      */   
/*      */   protected TreebankLanguagePack tlp;
/*      */   
/*      */   protected OutsideRuleFilter orf;
/*      */   
/*      */   protected float[][][] iScore;
/*      */   
/*      */   protected float[][][] oScore;
/*      */   
/*      */   protected float bestScore;
/*      */   protected int[][][] wordsInSpan;
/*      */   protected boolean[][] oFilteredStart;
/*      */   protected boolean[][] oFilteredEnd;
/*      */   protected boolean[][] iPossibleByL;
/*      */   protected boolean[][] iPossibleByR;
/*      */   protected boolean[][] oPossibleByL;
/*      */   protected boolean[][] oPossibleByR;
/*      */   protected int[] words;
/*      */   protected int length;
/*      */   protected boolean[][] tags;
/*   90 */   protected int myMaxLength = 559038737;
/*      */   
/*      */   protected int numStates;
/*   93 */   protected int arraySize = 0;
/*      */   protected static final boolean spillGuts = false;
/*      */   
/*   96 */   public void setGoalString(String goalStr) { this.goalStr = goalStr; }
/*      */   
/*      */   public double oScore(Edge edge)
/*      */   {
/*  100 */     double oS = this.oScore[edge.start][edge.end][edge.state];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  107 */     return oS;
/*      */   }
/*      */   
/*      */   public double iScore(Edge edge) {
/*  111 */     return this.iScore[edge.start][edge.end][edge.state];
/*      */   }
/*      */   
/*      */   public boolean oPossible(Hook hook) {
/*  115 */     return hook.isPreHook() ? this.oPossibleByR[hook.end][hook.state] : this.oPossibleByL[hook.start][hook.state];
/*      */   }
/*      */   
/*      */   public boolean iPossible(Hook hook) {
/*  119 */     return hook.isPreHook() ? this.iPossibleByR[hook.start][hook.subState] : this.iPossibleByL[hook.end][hook.subState];
/*      */   }
/*      */   
/*      */   public boolean oPossibleL(int state, int start)
/*      */   {
/*  124 */     return this.oPossibleByL[start][state];
/*      */   }
/*      */   
/*      */   public boolean oPossibleR(int state, int end) {
/*  128 */     return this.oPossibleByR[end][state];
/*      */   }
/*      */   
/*      */   public boolean iPossibleL(int state, int start) {
/*  132 */     return this.iPossibleByL[start][state];
/*      */   }
/*      */   
/*      */   public boolean iPossibleR(int state, int end) {
/*  136 */     return this.iPossibleByR[end][state];
/*      */   }
/*      */   
/*      */   protected void buildOFilter() {
/*  140 */     this.orf.init();
/*  141 */     for (int start = 0; start < this.length; start++) {
/*  142 */       this.orf.leftAccepting(this.oFilteredStart[start]);
/*  143 */       this.orf.advanceRight(this.tags[start]);
/*      */     }
/*  145 */     for (int end = this.length; end > 0; end--) {
/*  146 */       this.orf.rightAccepting(this.oFilteredEnd[end]);
/*  147 */       this.orf.advanceLeft(this.tags[(end - 1)]);
/*      */     }
/*      */   }
/*      */   
/*      */   public double validateBinarizedTree(Tree tree, int start)
/*      */   {
/*  153 */     if (tree.isLeaf()) {
/*  154 */       return 0.0D;
/*      */     }
/*  156 */     float epsilon = 1.0E-4F;
/*  157 */     if (tree.isPreTerminal()) {
/*  158 */       int tag = Numberer.number("tags", tree.label().value());
/*  159 */       int word = Numberer.number("words", tree.children()[0].label().value());
/*  160 */       IntTaggedWord iTW = new IntTaggedWord(word, tag);
/*  161 */       float score = this.lex.score(iTW, start);
/*  162 */       float bound = this.iScore[start][(start + 1)][Numberer.number(this.stateSpace, tree.label().value())];
/*  163 */       if (score > bound + epsilon) {
/*  164 */         System.out.println("Invalid tagging:");
/*  165 */         System.out.println("  Tag: " + tree.label().value());
/*  166 */         System.out.println("  Word: " + tree.children()[0].label().value());
/*  167 */         System.out.println("  Score: " + score);
/*  168 */         System.out.println("  Bound: " + bound);
/*      */       }
/*  170 */       return score;
/*      */     }
/*  172 */     int parent = Numberer.number(this.stateSpace, tree.label().value());
/*  173 */     int firstChild = Numberer.number(this.stateSpace, tree.children()[0].label().value());
/*  174 */     if (tree.numChildren() == 1) {
/*  175 */       UnaryRule ur = new UnaryRule();
/*  176 */       ur.parent = parent;
/*  177 */       ur.child = firstChild;
/*  178 */       double score = SloppyMath.max(this.ug.scoreRule(ur), -10000.0D) + validateBinarizedTree(tree.children()[0], start);
/*  179 */       double bound = this.iScore[start][(start + tree.yield().size())][parent];
/*  180 */       if (score > bound + epsilon) {
/*  181 */         System.out.println("Invalid unary:");
/*  182 */         System.out.println("  Parent: " + tree.label().value());
/*  183 */         System.out.println("  Child: " + tree.children()[0].label().value());
/*  184 */         System.out.println("  Start: " + start);
/*  185 */         System.out.println("  End: " + (start + tree.yield().size()));
/*  186 */         System.out.println("  Score: " + score);
/*  187 */         System.out.println("  Bound: " + bound);
/*      */       }
/*  189 */       return score;
/*      */     }
/*  191 */     BinaryRule br = new BinaryRule();
/*  192 */     br.parent = parent;
/*  193 */     br.leftChild = firstChild;
/*  194 */     br.rightChild = Numberer.number(this.stateSpace, tree.children()[1].label().value());
/*  195 */     double score = SloppyMath.max(this.bg.scoreRule(br), -10000.0D) + validateBinarizedTree(tree.children()[0], start) + validateBinarizedTree(tree.children()[1], start + tree.children()[0].yield().size());
/*  196 */     double bound = this.iScore[start][(start + tree.yield().size())][parent];
/*  197 */     if (score > bound + epsilon) {
/*  198 */       System.out.println("Invalid binary:");
/*  199 */       System.out.println("  Parent: " + tree.label().value());
/*  200 */       System.out.println("  LChild: " + tree.children()[0].label().value());
/*  201 */       System.out.println("  RChild: " + tree.children()[1].label().value());
/*  202 */       System.out.println("  Start: " + start);
/*  203 */       System.out.println("  End: " + (start + tree.yield().size()));
/*  204 */       System.out.println("  Score: " + score);
/*  205 */       System.out.println("  Bound: " + bound);
/*      */     }
/*  207 */     return score;
/*      */   }
/*      */   
/*      */   public Tree scoreNonBinarizedTree(Tree tree)
/*      */   {
/*  212 */     TreeAnnotatorAndBinarizer binarizer = new TreeAnnotatorAndBinarizer(this.op.tlpParams, this.op.forceCNF, !Train.outsideFactor(), true);
/*  213 */     tree = binarizer.transformTree(tree);
/*  214 */     scoreBinarizedTree(tree, 0);
/*  215 */     return this.op.tlpParams.subcategoryStripper().transformTree(new Debinarizer(this.op.forceCNF).transformTree(tree));
/*      */   }
/*      */   
/*      */ 
/*      */   public double scoreBinarizedTree(Tree tree, int start)
/*      */   {
/*  221 */     if (tree.isLeaf()) {
/*  222 */       return 0.0D;
/*      */     }
/*  224 */     if (tree.isPreTerminal()) {
/*  225 */       int tag = Numberer.number("tags", tree.label().value());
/*  226 */       int word = Numberer.number("words", tree.children()[0].label().value());
/*  227 */       IntTaggedWord iTW = new IntTaggedWord(word, tag);
/*      */       
/*      */ 
/*      */ 
/*  231 */       float score = this.lex.score(iTW, start);
/*  232 */       tree.setScore(score);
/*  233 */       return score;
/*      */     }
/*  235 */     int parent = Numberer.number(this.stateSpace, tree.label().value());
/*  236 */     int firstChild = Numberer.number(this.stateSpace, tree.children()[0].label().value());
/*  237 */     if (tree.numChildren() == 1) {
/*  238 */       UnaryRule ur = new UnaryRule();
/*  239 */       ur.parent = parent;
/*  240 */       ur.child = firstChild;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  246 */       double score = this.ug.scoreRule(ur) + scoreBinarizedTree(tree.children()[0], start);
/*  247 */       tree.setScore(score);
/*  248 */       return score;
/*      */     }
/*  250 */     BinaryRule br = new BinaryRule();
/*  251 */     br.parent = parent;
/*  252 */     br.leftChild = firstChild;
/*  253 */     br.rightChild = Numberer.number(this.stateSpace, tree.children()[1].label().value());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  261 */     double score = this.bg.scoreRule(br) + scoreBinarizedTree(tree.children()[0], start) + scoreBinarizedTree(tree.children()[1], start + tree.children()[0].yield().size());
/*  262 */     tree.setScore(score);
/*  263 */     return score;
/*      */   }
/*      */   
/*      */ 
/*      */   protected static final boolean dumpTagging = false;
/*      */   
/*  269 */   private static long time = System.currentTimeMillis();
/*      */   
/*      */   protected static void tick(String str) {
/*  272 */     long time2 = System.currentTimeMillis();
/*  273 */     long diff = time2 - time;
/*  274 */     time = time2;
/*  275 */     System.err.print("done.  " + diff + "\n" + str);
/*      */   }
/*      */   
/*  278 */   protected boolean floodTags = false;
/*  279 */   protected List sentence = null;
/*  280 */   protected LatticeReader lr = null;
/*      */   
/*  282 */   protected int[][] narrowLExtent = (int[][])null;
/*  283 */   protected int[][] wideLExtent = (int[][])null;
/*  284 */   protected int[][] narrowRExtent = (int[][])null;
/*  285 */   protected int[][] wideRExtent = (int[][])null;
/*      */   
/*  287 */   protected boolean[] isTag = null;
/*      */   
/*      */ 
/*      */   private static final double TOL = 1.0E-5D;
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean parse(List<? extends HasWord> sentence, String goal)
/*      */   {
/*  296 */     return parse(sentence);
/*      */   }
/*      */   
/*      */   public boolean parse(List<? extends HasWord> sentence) {
/*  300 */     this.lr = null;
/*      */     
/*  302 */     if (sentence != this.sentence) {
/*  303 */       this.sentence = sentence;
/*  304 */       this.floodTags = false;
/*      */     }
/*  306 */     if (Test.verbose) {
/*  307 */       Timing.tick("Starting pcfg parse.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  312 */     this.length = sentence.size();
/*  313 */     if (this.length > this.arraySize) {
/*  314 */       considerCreatingArrays(this.length);
/*      */     }
/*  316 */     int goal = this.stateNumberer.number(this.goalStr);
/*  317 */     if (Test.verbose)
/*      */     {
/*      */ 
/*  320 */       System.err.print("Initializing PCFG...");
/*      */     }
/*      */     
/*  323 */     this.words = new int[this.length];
/*  324 */     int unk = 0;
/*  325 */     StringBuilder unkWords = new StringBuilder("[");
/*  326 */     for (int i = 0; i < this.length; i++) {
/*  327 */       Object o = sentence.get(i);
/*  328 */       if ((o instanceof HasWord)) {
/*  329 */         o = ((HasWord)o).word();
/*      */       }
/*  331 */       String s = o.toString();
/*  332 */       if ((Test.verbose) && (!this.lex.isKnown(this.wordNumberer.number(s)))) {
/*  333 */         unk++;
/*  334 */         unkWords.append(" ");
/*  335 */         unkWords.append(s);
/*  336 */         unkWords.append(" { ");
/*  337 */         for (int jj = 0; jj < s.length(); jj++) {
/*  338 */           char ch = s.charAt(jj);
/*  339 */           unkWords.append(Character.getType(ch)).append(" ");
/*      */         }
/*  341 */         unkWords.append("}");
/*      */       }
/*  343 */       this.words[i] = this.wordNumberer.number(s);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  352 */     for (int start = 0; start < this.length; start++) {
/*  353 */       for (int end = start + 1; end <= this.length; end++) {
/*  354 */         Arrays.fill(this.iScore[start][end], Float.NEGATIVE_INFINITY);
/*  355 */         if ((this.op.doDep) && (!Test.useFastFactored)) {
/*  356 */           Arrays.fill(this.oScore[start][end], Float.NEGATIVE_INFINITY);
/*      */         }
/*  358 */         if (Test.lengthNormalization) {
/*  359 */           Arrays.fill(this.wordsInSpan[start][end], 1);
/*      */         }
/*      */       }
/*      */     }
/*  363 */     for (int loc = 0; loc <= this.length; loc++) {
/*  364 */       Arrays.fill(this.narrowLExtent[loc], -1);
/*  365 */       Arrays.fill(this.wideLExtent[loc], this.length + 1);
/*  366 */       Arrays.fill(this.narrowRExtent[loc], this.length + 1);
/*  367 */       Arrays.fill(this.wideRExtent[loc], -1);
/*      */     }
/*      */     
/*      */ 
/*  371 */     if (Test.verbose) {
/*  372 */       Timing.tick("done.");
/*  373 */       unkWords.append(" ]");
/*  374 */       this.op.tlpParams.pw(System.err).println("Unknown words: " + unk + " " + unkWords);
/*  375 */       System.err.print("Starting filters...");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  381 */     initializeChart(sentence);
/*      */     
/*      */ 
/*  384 */     if (Test.verbose) {
/*  385 */       Timing.tick("done.");
/*  386 */       System.err.print("Starting insides...");
/*      */     }
/*      */     
/*  389 */     doInsideScores();
/*  390 */     if (Test.verbose)
/*      */     {
/*  392 */       Timing.tick("done.");
/*  393 */       System.out.println("PCFG parsing " + this.length + " words (incl. stop): insideScore = " + this.iScore[0][this.length][goal]);
/*      */     }
/*  395 */     this.bestScore = this.iScore[0][this.length][goal];
/*  396 */     boolean succeeded = hasParse();
/*  397 */     if ((Test.doRecovery) && (!succeeded) && (!this.floodTags)) {
/*  398 */       this.floodTags = true;
/*  399 */       System.err.println("Trying recovery parse...");
/*  400 */       return parse(sentence);
/*      */     }
/*  402 */     if ((!this.op.doDep) || (Test.useFastFactored)) {
/*  403 */       return succeeded;
/*      */     }
/*  405 */     if (Test.verbose) {
/*  406 */       System.err.print("Starting outsides...");
/*      */     }
/*      */     
/*  409 */     this.oScore[0][this.length][goal] = 0.0F;
/*  410 */     doOutsideScores();
/*      */     
/*      */ 
/*  413 */     if (Test.verbose)
/*      */     {
/*  415 */       Timing.tick("done.");
/*  416 */       System.err.print("Starting half-filters...");
/*      */     }
/*  418 */     for (int loc = 0; loc <= this.length; loc++) {
/*  419 */       Arrays.fill(this.iPossibleByL[loc], false);
/*  420 */       Arrays.fill(this.iPossibleByR[loc], false);
/*  421 */       Arrays.fill(this.oPossibleByL[loc], false);
/*  422 */       Arrays.fill(this.oPossibleByR[loc], false);
/*      */     }
/*  424 */     for (int start = 0; start < this.length; start++) {
/*  425 */       for (int end = start + 1; end <= this.length; end++) {
/*  426 */         for (int state = 0; state < this.numStates; state++) {
/*  427 */           if ((this.iScore[start][end][state] > Float.NEGATIVE_INFINITY) && (this.oScore[start][end][state] > Float.NEGATIVE_INFINITY)) {
/*  428 */             this.iPossibleByL[start][state] = 1;
/*  429 */             this.iPossibleByR[end][state] = 1;
/*  430 */             this.oPossibleByL[start][state] = 1;
/*  431 */             this.oPossibleByR[end][state] = 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  436 */     if (Test.verbose) {
/*  437 */       Timing.tick("done.");
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  465 */     return succeeded;
/*      */   }
/*      */   
/*      */   public boolean parse(LatticeReader lr) {
/*  469 */     this.sentence = null;
/*  470 */     if (lr != this.lr) {
/*  471 */       this.lr = lr;
/*  472 */       this.floodTags = false;
/*      */     }
/*      */     
/*  475 */     if (Test.verbose) {
/*  476 */       Timing.tick("Starting pcfg parse.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  481 */     this.length = lr.getNumStates();
/*  482 */     if (this.length > this.arraySize) {
/*  483 */       considerCreatingArrays(this.length);
/*      */     }
/*  485 */     int goal = this.stateNumberer.number(this.goalStr);
/*  486 */     if (Test.verbose)
/*      */     {
/*      */ 
/*  489 */       System.out.print("Initializing PCFG...");
/*      */     }
/*      */     
/*  492 */     List lWords = lr.getLatticeWords();
/*  493 */     this.words = new int[lWords.size()];
/*  494 */     int unk = 0;
/*  495 */     StringBuilder unkWords = new StringBuilder("[");
/*  496 */     int i = 0; for (int size = lWords.size(); i < size; i++) {
/*  497 */       String s = ((LatticeReader.LatticeWord)lWords.get(i)).word;
/*  498 */       if ((Test.verbose) && (!this.lex.isKnown(this.wordNumberer.number(s)))) {
/*  499 */         unk++;
/*  500 */         unkWords.append(" ");
/*  501 */         unkWords.append(s);
/*      */       }
/*  503 */       this.words[i] = this.wordNumberer.number(s);
/*      */     }
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
/*  519 */     for (int start = 0; start < this.length; start++) {
/*  520 */       for (int end = start + 1; end <= this.length; end++) {
/*  521 */         Arrays.fill(this.iScore[start][end], Float.NEGATIVE_INFINITY);
/*  522 */         if ((this.op.doDep) && (!Test.useFastFactored)) {
/*  523 */           Arrays.fill(this.oScore[start][end], Float.NEGATIVE_INFINITY);
/*      */         }
/*      */       }
/*      */     }
/*  527 */     for (int loc = 0; loc <= this.length; loc++) {
/*  528 */       Arrays.fill(this.narrowLExtent[loc], -1);
/*  529 */       Arrays.fill(this.wideLExtent[loc], this.length + 1);
/*  530 */       Arrays.fill(this.narrowRExtent[loc], this.length + 1);
/*  531 */       Arrays.fill(this.wideRExtent[loc], -1);
/*      */     }
/*      */     
/*      */ 
/*  535 */     if (Test.verbose) {
/*  536 */       Timing.tick("done.");
/*  537 */       unkWords.append(" ]");
/*  538 */       this.op.tlpParams.pw().println("Unknown words: " + unk + " " + unkWords);
/*  539 */       System.out.print("Starting filters...");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  545 */     initializeChart(lr);
/*      */     
/*      */ 
/*  548 */     if (Test.verbose) {
/*  549 */       Timing.tick("done.");
/*  550 */       System.out.print("Starting insides...");
/*      */     }
/*      */     
/*  553 */     doInsideScores();
/*  554 */     if (Test.verbose)
/*      */     {
/*  556 */       Timing.tick("done.");
/*  557 */       System.out.println("PCFG " + this.length + " words (incl. stop) iScore " + this.iScore[0][this.length][goal]);
/*      */     }
/*  559 */     this.bestScore = this.iScore[0][this.length][goal];
/*  560 */     boolean succeeded = hasParse();
/*  561 */     if ((Test.doRecovery) && (this.bestScore == Float.NEGATIVE_INFINITY) && (!this.floodTags)) {
/*  562 */       this.floodTags = true;
/*  563 */       System.err.println("Trying recovery parse...");
/*  564 */       return parse(lr);
/*      */     }
/*  566 */     if ((!this.op.doDep) || (Test.useFastFactored)) {
/*  567 */       return succeeded;
/*      */     }
/*  569 */     if (Test.verbose) {
/*  570 */       System.err.print("Starting outsides...");
/*      */     }
/*      */     
/*  573 */     this.oScore[0][this.length][goal] = 0.0F;
/*  574 */     doOutsideScores();
/*      */     
/*      */ 
/*  577 */     if (Test.verbose)
/*      */     {
/*  579 */       Timing.tick("Done.");
/*  580 */       System.out.print("Starting half-filters...");
/*      */     }
/*  582 */     for (int loc = 0; loc <= this.length; loc++) {
/*  583 */       Arrays.fill(this.iPossibleByL[loc], false);
/*  584 */       Arrays.fill(this.iPossibleByR[loc], false);
/*  585 */       Arrays.fill(this.oPossibleByL[loc], false);
/*  586 */       Arrays.fill(this.oPossibleByR[loc], false);
/*      */     }
/*  588 */     for (int start = 0; start < this.length; start++) {
/*  589 */       for (int end = start + 1; end <= this.length; end++) {
/*  590 */         for (int state = 0; state < this.numStates; state++) {
/*  591 */           if ((this.iScore[start][end][state] > Float.NEGATIVE_INFINITY) && (this.oScore[start][end][state] > Float.NEGATIVE_INFINITY)) {
/*  592 */             this.iPossibleByL[start][state] = 1;
/*  593 */             this.iPossibleByR[end][state] = 1;
/*  594 */             this.oPossibleByL[start][state] = 1;
/*  595 */             this.oPossibleByR[end][state] = 1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  600 */     if (Test.verbose) {
/*  601 */       Timing.tick("done.");
/*      */     }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  629 */     return succeeded;
/*      */   }
/*      */   
/*      */   private void doOutsideScores() {
/*  633 */     for (int diff = this.length; diff >= 1; diff--) {
/*  634 */       for (int start = 0; start + diff <= this.length; start++) {
/*  635 */         int end = start + diff;
/*      */         
/*  637 */         for (int s = 0; s < this.numStates; s++) {
/*  638 */           float oS = this.oScore[start][end][s];
/*  639 */           if (oS != Float.NEGATIVE_INFINITY)
/*      */           {
/*      */ 
/*  642 */             UnaryRule[] rules = this.ug.closedRulesByParent(s);
/*  643 */             for (UnaryRule ur : rules) {
/*  644 */               float pS = ur.score;
/*  645 */               float tot = oS + pS;
/*  646 */               if ((tot > this.oScore[start][end][ur.child]) && (this.iScore[start][end][ur.child] > Float.NEGATIVE_INFINITY)) {
/*  647 */                 this.oScore[start][end][ur.child] = tot;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*  652 */         for (int s = 0; s < this.numStates; s++) {
/*  653 */           int min1 = this.narrowRExtent[start][s];
/*  654 */           if (end >= min1)
/*      */           {
/*      */ 
/*  657 */             BinaryRule[] rules = this.bg.splitRulesWithLC(s);
/*  658 */             for (BinaryRule br : rules) {
/*  659 */               float oS = this.oScore[start][end][br.parent];
/*  660 */               if (oS != Float.NEGATIVE_INFINITY)
/*      */               {
/*      */ 
/*  663 */                 int max1 = this.narrowLExtent[end][br.rightChild];
/*  664 */                 if (max1 >= min1)
/*      */                 {
/*      */ 
/*  667 */                   int min = min1;
/*  668 */                   int max = max1;
/*  669 */                   if (max - min > 2) {
/*  670 */                     int min2 = this.wideLExtent[end][br.rightChild];
/*  671 */                     min = min1 > min2 ? min1 : min2;
/*  672 */                     if (max1 >= min)
/*      */                     {
/*      */ 
/*  675 */                       int max2 = this.wideRExtent[start][br.leftChild];
/*  676 */                       max = max1 < max2 ? max1 : max2;
/*  677 */                       if (max < min) {}
/*      */                     }
/*      */                   }
/*      */                   else {
/*  681 */                     float pS = br.score;
/*  682 */                     for (int split = min; split <= max; split++) {
/*  683 */                       float lS = this.iScore[start][split][br.leftChild];
/*  684 */                       if (lS != Float.NEGATIVE_INFINITY)
/*      */                       {
/*      */ 
/*  687 */                         float rS = this.iScore[split][end][br.rightChild];
/*  688 */                         if (rS != Float.NEGATIVE_INFINITY)
/*      */                         {
/*      */ 
/*  691 */                           float totL = pS + rS + oS;
/*  692 */                           if (totL > this.oScore[start][split][br.leftChild]) {
/*  693 */                             this.oScore[start][split][br.leftChild] = totL;
/*      */                           }
/*  695 */                           float totR = pS + lS + oS;
/*  696 */                           if (totR > this.oScore[split][end][br.rightChild])
/*  697 */                             this.oScore[split][end][br.rightChild] = totR;
/*      */                         }
/*      */                       }
/*      */                     }
/*      */                   } } } } } }
/*  702 */         for (int s = 0; s < this.numStates; s++) {
/*  703 */           int max1 = this.narrowLExtent[end][s];
/*  704 */           if (max1 >= start)
/*      */           {
/*      */ 
/*  707 */             BinaryRule[] rules = this.bg.splitRulesWithRC(s);
/*  708 */             for (BinaryRule br : rules) {
/*  709 */               float oS = this.oScore[start][end][br.parent];
/*  710 */               if (oS != Float.NEGATIVE_INFINITY)
/*      */               {
/*      */ 
/*  713 */                 int min1 = this.narrowRExtent[start][br.leftChild];
/*  714 */                 if (max1 >= min1)
/*      */                 {
/*      */ 
/*  717 */                   int min = min1;
/*  718 */                   int max = max1;
/*  719 */                   if (max - min > 2) {
/*  720 */                     int min2 = this.wideLExtent[end][br.rightChild];
/*  721 */                     min = min1 > min2 ? min1 : min2;
/*  722 */                     if (max1 >= min)
/*      */                     {
/*      */ 
/*  725 */                       int max2 = this.wideRExtent[start][br.leftChild];
/*  726 */                       max = max1 < max2 ? max1 : max2;
/*  727 */                       if (max < min) {}
/*      */                     }
/*      */                   }
/*      */                   else {
/*  731 */                     float pS = br.score;
/*  732 */                     for (int split = min; split <= max; split++) {
/*  733 */                       float lS = this.iScore[start][split][br.leftChild];
/*  734 */                       if (lS != Float.NEGATIVE_INFINITY)
/*      */                       {
/*      */ 
/*  737 */                         float rS = this.iScore[split][end][br.rightChild];
/*  738 */                         if (rS != Float.NEGATIVE_INFINITY)
/*      */                         {
/*      */ 
/*  741 */                           float totL = pS + rS + oS;
/*  742 */                           if (totL > this.oScore[start][split][br.leftChild]) {
/*  743 */                             this.oScore[start][split][br.leftChild] = totL;
/*      */                           }
/*  745 */                           float totR = pS + lS + oS;
/*  746 */                           if (totR > this.oScore[split][end][br.rightChild]) {
/*  747 */                             this.oScore[split][end][br.rightChild] = totR;
/*      */                           }
/*      */                         }
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
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
/*      */   void doInsideScores()
/*      */   {
/*  805 */     for (int diff = 2; diff <= this.length; diff++)
/*      */     {
/*      */ 
/*  808 */       for (int start = 0; start < (diff == this.length ? 1 : this.length - diff); start++)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  813 */         int end = start + diff;
/*      */         
/*  815 */         if (Test.constraints != null) {
/*  816 */           boolean skip = false;
/*  817 */           for (Test.Constraint c : Test.constraints) {
/*  818 */             if (((start > c.start) && (start < c.end) && (end > c.end)) || ((end > c.start) && (end < c.end) && (start < c.start))) {
/*  819 */               skip = true;
/*  820 */               break;
/*      */             }
/*      */           }
/*  823 */           if (skip) {}
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  828 */           for (int leftState = 0; leftState < this.numStates; leftState++)
/*      */           {
/*  830 */             int narrowR = this.narrowRExtent[start][leftState];
/*  831 */             boolean iPossibleL = narrowR < end;
/*  832 */             if (iPossibleL)
/*      */             {
/*      */ 
/*  835 */               BinaryRule[] leftRules = this.bg.splitRulesWithLC(leftState);
/*      */               
/*  837 */               for (BinaryRule r : leftRules)
/*      */               {
/*      */ 
/*  840 */                 int narrowL = this.narrowLExtent[end][r.rightChild];
/*  841 */                 boolean iPossibleR = narrowL >= narrowR;
/*  842 */                 if (iPossibleR)
/*      */                 {
/*      */ 
/*  845 */                   int min1 = narrowR;
/*  846 */                   int min2 = this.wideLExtent[end][r.rightChild];
/*  847 */                   int min = min1 > min2 ? min1 : min2;
/*  848 */                   if (min <= narrowL)
/*      */                   {
/*      */ 
/*  851 */                     int max1 = this.wideRExtent[start][leftState];
/*  852 */                     int max2 = narrowL;
/*  853 */                     int max = max1 < max2 ? max1 : max2;
/*  854 */                     if (min <= max)
/*      */                     {
/*      */ 
/*  857 */                       float pS = r.score;
/*  858 */                       int parentState = r.parent;
/*  859 */                       float oldIScore = this.iScore[start][end][parentState];
/*  860 */                       float bestIScore = oldIScore;
/*      */                       
/*      */                       boolean foundBetter;
/*      */                       boolean foundBetter;
/*  864 */                       if (!Test.lengthNormalization)
/*      */                       {
/*  866 */                         for (int split = min; split <= max; split++)
/*      */                         {
/*  868 */                           if (Test.constraints != null) {
/*  869 */                             boolean skip = false;
/*  870 */                             for (Test.Constraint c : Test.constraints) {
/*  871 */                               if (((start < c.start) && (end >= c.end)) || ((start <= c.start) && (end > c.end) && (split > c.start) && (split < c.end))) {
/*  872 */                                 skip = true;
/*  873 */                                 break;
/*      */                               }
/*  875 */                               if ((start == c.start) && (split == c.end)) {
/*  876 */                                 String tag = (String)this.stateNumberer.object(leftState);
/*  877 */                                 Matcher m = c.state.matcher(tag);
/*  878 */                                 if (!m.matches()) {
/*  879 */                                   skip = true;
/*  880 */                                   break;
/*      */                                 }
/*      */                               }
/*  883 */                               if ((split == c.start) && (end == c.end)) {
/*  884 */                                 String tag = (String)this.stateNumberer.object(r.rightChild);
/*  885 */                                 Matcher m = c.state.matcher(tag);
/*  886 */                                 if (!m.matches()) {
/*  887 */                                   skip = true;
/*  888 */                                   break;
/*      */                                 }
/*      */                               }
/*      */                             }
/*  892 */                             if (skip) {}
/*      */ 
/*      */                           }
/*      */                           else
/*      */                           {
/*  897 */                             float lS = this.iScore[start][split][leftState];
/*  898 */                             if (lS != Float.NEGATIVE_INFINITY)
/*      */                             {
/*      */ 
/*  901 */                               float rS = this.iScore[split][end][r.rightChild];
/*  902 */                               if (rS != Float.NEGATIVE_INFINITY)
/*      */                               {
/*      */ 
/*  905 */                                 float tot = pS + lS + rS;
/*  906 */                                 if (tot > bestIScore)
/*  907 */                                   bestIScore = tot;
/*      */                               }
/*      */                             } } }
/*  910 */                         foundBetter = bestIScore > oldIScore;
/*      */                       }
/*      */                       else {
/*  913 */                         int bestWordsInSpan = this.wordsInSpan[start][end][parentState];
/*  914 */                         float oldNormIScore = oldIScore / bestWordsInSpan;
/*  915 */                         float bestNormIScore = oldNormIScore;
/*      */                         
/*  917 */                         for (int split = min; split <= max; split++) {
/*  918 */                           float lS = this.iScore[start][split][leftState];
/*  919 */                           if (lS != Float.NEGATIVE_INFINITY)
/*      */                           {
/*      */ 
/*      */ 
/*  923 */                             float rS = this.iScore[split][end][r.rightChild];
/*  924 */                             if (rS != Float.NEGATIVE_INFINITY)
/*      */                             {
/*      */ 
/*  927 */                               float tot = pS + lS + rS;
/*  928 */                               int newWordsInSpan = this.wordsInSpan[start][split][leftState] + this.wordsInSpan[split][end][r.rightChild];
/*  929 */                               float normTot = tot / newWordsInSpan;
/*  930 */                               if (normTot > bestNormIScore) {
/*  931 */                                 bestIScore = tot;
/*  932 */                                 bestNormIScore = normTot;
/*  933 */                                 bestWordsInSpan = newWordsInSpan;
/*      */                               }
/*      */                             } } }
/*  936 */                         foundBetter = bestNormIScore > oldNormIScore;
/*  937 */                         if (foundBetter) {
/*  938 */                           this.wordsInSpan[start][end][parentState] = bestWordsInSpan;
/*      */                         }
/*      */                       }
/*  941 */                       if (foundBetter) {
/*  942 */                         this.iScore[start][end][parentState] = bestIScore;
/*      */                         
/*      */ 
/*  945 */                         if (oldIScore == Float.NEGATIVE_INFINITY) {
/*  946 */                           if (start > this.narrowLExtent[end][parentState]) {
/*  947 */                             this.narrowLExtent[end][parentState] = start;
/*  948 */                             this.wideLExtent[end][parentState] = start;
/*      */                           }
/*  950 */                           else if (start < this.wideLExtent[end][parentState]) {
/*  951 */                             this.wideLExtent[end][parentState] = start;
/*      */                           }
/*      */                           
/*  954 */                           if (end < this.narrowRExtent[start][parentState]) {
/*  955 */                             this.narrowRExtent[start][parentState] = end;
/*  956 */                             this.wideRExtent[start][parentState] = end;
/*      */                           }
/*  958 */                           else if (end > this.wideRExtent[start][parentState]) {
/*  959 */                             this.wideRExtent[start][parentState] = end;
/*      */                           }
/*      */                         }
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               } } }
/*  967 */           for (int rightState = 0; rightState < this.numStates; rightState++) {
/*  968 */             int narrowL = this.narrowLExtent[end][rightState];
/*  969 */             boolean iPossibleR = narrowL > start;
/*  970 */             if (iPossibleR)
/*      */             {
/*      */ 
/*  973 */               BinaryRule[] rightRules = this.bg.splitRulesWithRC(rightState);
/*      */               
/*  975 */               for (BinaryRule r : rightRules)
/*      */               {
/*      */ 
/*  978 */                 int narrowR = this.narrowRExtent[start][r.leftChild];
/*  979 */                 boolean iPossibleL = narrowR <= narrowL;
/*  980 */                 if (iPossibleL)
/*      */                 {
/*      */ 
/*  983 */                   int min1 = narrowR;
/*  984 */                   int min2 = this.wideLExtent[end][rightState];
/*  985 */                   int min = min1 > min2 ? min1 : min2;
/*  986 */                   if (min <= narrowL)
/*      */                   {
/*      */ 
/*  989 */                     int max1 = this.wideRExtent[start][r.leftChild];
/*  990 */                     int max2 = narrowL;
/*  991 */                     int max = max1 < max2 ? max1 : max2;
/*  992 */                     if (min <= max)
/*      */                     {
/*      */ 
/*  995 */                       float pS = r.score;
/*  996 */                       int parentState = r.parent;
/*  997 */                       float oldIScore = this.iScore[start][end][parentState];
/*  998 */                       float bestIScore = oldIScore;
/*      */                       boolean foundBetter;
/*      */                       boolean foundBetter;
/* 1001 */                       if (!Test.lengthNormalization)
/*      */                       {
/* 1003 */                         for (int split = min; split <= max; split++)
/*      */                         {
/* 1005 */                           if (Test.constraints != null) {
/* 1006 */                             boolean skip = false;
/* 1007 */                             for (Test.Constraint c : Test.constraints) {
/* 1008 */                               if (((start < c.start) && (end >= c.end)) || ((start <= c.start) && (end > c.end) && (split > c.start) && (split < c.end))) {
/* 1009 */                                 skip = true;
/* 1010 */                                 break;
/*      */                               }
/* 1012 */                               if ((start == c.start) && (split == c.end)) {
/* 1013 */                                 String tag = (String)this.stateNumberer.object(r.leftChild);
/* 1014 */                                 Matcher m = c.state.matcher(tag);
/* 1015 */                                 if (!m.matches())
/*      */                                 {
/* 1017 */                                   skip = true;
/* 1018 */                                   break;
/*      */                                 }
/*      */                               }
/* 1021 */                               if ((split == c.start) && (end == c.end)) {
/* 1022 */                                 String tag = (String)this.stateNumberer.object(rightState);
/* 1023 */                                 Matcher m = c.state.matcher(tag);
/* 1024 */                                 if (!m.matches())
/*      */                                 {
/* 1026 */                                   skip = true;
/* 1027 */                                   break;
/*      */                                 }
/*      */                               }
/*      */                             }
/* 1031 */                             if (skip) {}
/*      */ 
/*      */                           }
/*      */                           else
/*      */                           {
/* 1036 */                             float lS = this.iScore[start][split][r.leftChild];
/* 1037 */                             if (lS != Float.NEGATIVE_INFINITY)
/*      */                             {
/*      */ 
/* 1040 */                               float rS = this.iScore[split][end][rightState];
/* 1041 */                               if (rS != Float.NEGATIVE_INFINITY)
/*      */                               {
/*      */ 
/* 1044 */                                 float tot = pS + lS + rS;
/* 1045 */                                 if (tot > bestIScore)
/* 1046 */                                   bestIScore = tot;
/*      */                               }
/*      */                             } } }
/* 1049 */                         foundBetter = bestIScore > oldIScore;
/*      */                       }
/*      */                       else {
/* 1052 */                         int bestWordsInSpan = this.wordsInSpan[start][end][parentState];
/* 1053 */                         float oldNormIScore = oldIScore / bestWordsInSpan;
/* 1054 */                         float bestNormIScore = oldNormIScore;
/* 1055 */                         for (int split = min; split <= max; split++) {
/* 1056 */                           float lS = this.iScore[start][split][r.leftChild];
/* 1057 */                           if (lS != Float.NEGATIVE_INFINITY)
/*      */                           {
/*      */ 
/* 1060 */                             float rS = this.iScore[split][end][rightState];
/* 1061 */                             if (rS != Float.NEGATIVE_INFINITY)
/*      */                             {
/*      */ 
/* 1064 */                               float tot = pS + lS + rS;
/* 1065 */                               int newWordsInSpan = this.wordsInSpan[start][split][r.leftChild] + this.wordsInSpan[split][end][rightState];
/* 1066 */                               float normTot = tot / newWordsInSpan;
/* 1067 */                               if (normTot > bestNormIScore) {
/* 1068 */                                 bestIScore = tot;
/* 1069 */                                 bestNormIScore = normTot;
/* 1070 */                                 bestWordsInSpan = newWordsInSpan;
/*      */                               }
/*      */                             } } }
/* 1073 */                         foundBetter = bestNormIScore > oldNormIScore;
/* 1074 */                         if (foundBetter) {
/* 1075 */                           this.wordsInSpan[start][end][parentState] = bestWordsInSpan;
/*      */                         }
/*      */                       }
/* 1078 */                       if (foundBetter) {
/* 1079 */                         this.iScore[start][end][parentState] = bestIScore;
/*      */                         
/* 1081 */                         if (oldIScore == Float.NEGATIVE_INFINITY) {
/* 1082 */                           if (start > this.narrowLExtent[end][parentState]) {
/* 1083 */                             this.narrowLExtent[end][parentState] = start;
/* 1084 */                             this.wideLExtent[end][parentState] = start;
/*      */                           }
/* 1086 */                           else if (start < this.wideLExtent[end][parentState]) {
/* 1087 */                             this.wideLExtent[end][parentState] = start;
/*      */                           }
/*      */                           
/* 1090 */                           if (end < this.narrowRExtent[start][parentState]) {
/* 1091 */                             this.narrowRExtent[start][parentState] = end;
/* 1092 */                             this.wideRExtent[start][parentState] = end;
/*      */                           }
/* 1094 */                           else if (end > this.wideRExtent[start][parentState]) {
/* 1095 */                             this.wideRExtent[start][parentState] = end;
/*      */                           }
/*      */                         }
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           
/* 1106 */           for (int state = 0; state < this.numStates; state++) {
/* 1107 */             float iS = this.iScore[start][end][state];
/* 1108 */             if (iS != Float.NEGATIVE_INFINITY)
/*      */             {
/*      */ 
/*      */ 
/* 1112 */               UnaryRule[] unaries = this.ug.closedRulesByChild(state);
/* 1113 */               for (UnaryRule ur : unaries)
/*      */               {
/* 1115 */                 if (Test.constraints != null) {
/* 1116 */                   boolean skip = false;
/* 1117 */                   for (Test.Constraint c : Test.constraints) {
/* 1118 */                     if ((start == c.start) && (end == c.end)) {
/* 1119 */                       String tag = (String)this.stateNumberer.object(ur.parent);
/* 1120 */                       Matcher m = c.state.matcher(tag);
/* 1121 */                       if (!m.matches())
/*      */                       {
/* 1123 */                         skip = true;
/* 1124 */                         break;
/*      */                       }
/*      */                     }
/*      */                   }
/* 1128 */                   if (skip) {}
/*      */ 
/*      */                 }
/*      */                 else
/*      */                 {
/* 1133 */                   int parentState = ur.parent;
/* 1134 */                   float pS = ur.score;
/* 1135 */                   float tot = iS + pS;
/* 1136 */                   float cur = this.iScore[start][end][parentState];
/*      */                   boolean foundBetter;
/* 1138 */                   if (Test.lengthNormalization) {
/* 1139 */                     int totWordsInSpan = this.wordsInSpan[start][end][state];
/* 1140 */                     float normTot = tot / totWordsInSpan;
/* 1141 */                     int curWordsInSpan = this.wordsInSpan[start][end][parentState];
/* 1142 */                     float normCur = cur / curWordsInSpan;
/* 1143 */                     boolean foundBetter = normTot > normCur;
/* 1144 */                     if (foundBetter) {
/* 1145 */                       this.wordsInSpan[start][end][parentState] = this.wordsInSpan[start][end][state];
/*      */                     }
/*      */                   } else {
/* 1148 */                     foundBetter = tot > cur;
/*      */                   }
/* 1150 */                   if (foundBetter)
/*      */                   {
/* 1152 */                     this.iScore[start][end][parentState] = tot;
/* 1153 */                     if (cur == Float.NEGATIVE_INFINITY) {
/* 1154 */                       if (start > this.narrowLExtent[end][parentState]) {
/* 1155 */                         this.narrowLExtent[end][parentState] = start;
/* 1156 */                         this.wideLExtent[end][parentState] = start;
/*      */                       }
/* 1158 */                       else if (start < this.wideLExtent[end][parentState]) {
/* 1159 */                         this.wideLExtent[end][parentState] = start;
/*      */                       }
/*      */                       
/* 1162 */                       if (end < this.narrowRExtent[start][parentState]) {
/* 1163 */                         this.narrowRExtent[start][parentState] = end;
/* 1164 */                         this.wideRExtent[start][parentState] = end;
/*      */                       }
/* 1166 */                       else if (end > this.wideRExtent[start][parentState]) {
/* 1167 */                         this.wideRExtent[start][parentState] = end;
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 } }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void initializeChart(LatticeReader lr) {
/* 1180 */     List<LatticeReader.LatticeWord> latticeWords = lr.getLatticeWords();
/* 1181 */     for (LatticeReader.LatticeWord lWord : latticeWords) {
/* 1182 */       int start = lWord.startNode;int end = lWord.endNode;
/* 1183 */       String word = lWord.word;
/* 1184 */       for (int state = 0; state < this.numStates; state++) {
/* 1185 */         if (this.isTag[state] != 0) {
/* 1186 */           IntTaggedWord itw = new IntTaggedWord(this.wordNumberer.number(word), Numberer.translate(this.stateSpace, "tags", state));
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1191 */           float newScore = this.lex.score(itw, start) + (float)lWord.am;
/* 1192 */           if (newScore > this.iScore[start][end][state]) {
/* 1193 */             this.iScore[start][end][state] = newScore;
/* 1194 */             this.narrowRExtent[start][state] = (start + 1);
/* 1195 */             this.narrowLExtent[end][state] = (end - 1);
/* 1196 */             this.wideRExtent[start][state] = (start + 1);
/* 1197 */             this.wideLExtent[end][state] = (end - 1);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1202 */       if ((this.floodTags) && (!Test.noRecoveryTagging)) {
/* 1203 */         for (int state = 0; state < this.numStates; state++) {
/* 1204 */           float iS = this.iScore[start][end][state];
/* 1205 */           if ((iS == Float.NEGATIVE_INFINITY) && (this.isTag[state] != 0)) {
/* 1206 */             this.iScore[start][end][state] = -1000.0F;
/* 1207 */             this.narrowRExtent[start][state] = end;
/* 1208 */             this.narrowLExtent[end][state] = start;
/* 1209 */             this.wideRExtent[start][state] = end;
/* 1210 */             this.wideLExtent[end][state] = start;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1217 */       for (int state = 0; state < this.numStates; state++) {
/* 1218 */         float iS = this.iScore[start][end][state];
/* 1219 */         if (iS != Float.NEGATIVE_INFINITY)
/*      */         {
/*      */ 
/* 1222 */           UnaryRule[] unaries = this.ug.closedRulesByChild(state);
/* 1223 */           for (UnaryRule ur : unaries) {
/* 1224 */             int parentState = ur.parent;
/* 1225 */             float pS = ur.score;
/* 1226 */             float tot = iS + pS;
/* 1227 */             if (tot > this.iScore[start][end][parentState]) {
/* 1228 */               this.iScore[start][end][parentState] = tot;
/* 1229 */               this.narrowRExtent[start][parentState] = end;
/* 1230 */               this.narrowLExtent[end][parentState] = start;
/* 1231 */               this.wideRExtent[start][parentState] = end;
/* 1232 */               this.wideLExtent[end][parentState] = start;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void initializeChart(List sentence) {
/* 1241 */     int boundary = this.wordNumberer.number(".$.");
/*      */     
/* 1243 */     for (int start = 0; start + 1 <= this.length; start++) {
/* 1244 */       if (Test.maxSpanForTags > 1)
/*      */       {
/* 1246 */         for (int end = start + 1; ((end < this.length - 1) && (end - start <= Test.maxSpanForTags)) || (start + 1 == end); end++) {
/* 1247 */           StringBuilder word = new StringBuilder();
/*      */           
/* 1249 */           for (int i = start; i < end; i++) {
/* 1250 */             if ((sentence.get(i) instanceof StringLabel)) {
/* 1251 */               word.append(((StringLabel)sentence.get(i)).value());
/*      */             } else {
/* 1253 */               word.append((String)sentence.get(i));
/*      */             }
/*      */           }
/* 1256 */           for (int state = 0; state < this.numStates; state++) {
/* 1257 */             float iS = this.iScore[start][end][state];
/* 1258 */             if ((iS == Float.NEGATIVE_INFINITY) && (this.isTag[state] != 0)) {
/* 1259 */               IntTaggedWord itw = new IntTaggedWord(this.wordNumberer.number(word.toString()), Numberer.translate(this.stateSpace, "tags", state));
/* 1260 */               this.iScore[start][end][state] = this.lex.score(itw, start);
/* 1261 */               if (this.iScore[start][end][state] > Float.NEGATIVE_INFINITY) {
/* 1262 */                 this.narrowRExtent[start][state] = (start + 1);
/* 1263 */                 this.narrowLExtent[end][state] = (end - 1);
/* 1264 */                 this.wideRExtent[start][state] = (start + 1);
/* 1265 */                 this.wideLExtent[end][state] = (end - 1);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1273 */         int word = this.words[start];
/* 1274 */         int end = start + 1;
/* 1275 */         Arrays.fill(this.tags[start], false);
/* 1276 */         String trueTagStr = null;
/* 1277 */         if ((sentence.get(start) instanceof HasTag)) {
/* 1278 */           trueTagStr = ((HasTag)sentence.get(start)).tag();
/* 1279 */           if ("".equals(trueTagStr)) {
/* 1280 */             trueTagStr = null;
/*      */           }
/*      */         }
/* 1283 */         boolean assignedSomeTag = false;
/*      */         Iterator<IntTaggedWord> taggingI;
/* 1285 */         if ((!this.floodTags) || (word == boundary))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1291 */           for (taggingI = this.lex.ruleIteratorByWord(word, start); taggingI.hasNext();) {
/* 1292 */             IntTaggedWord tagging = (IntTaggedWord)taggingI.next();
/* 1293 */             int state = Numberer.translate("tags", this.stateSpace, tagging.tag);
/* 1294 */             if ((trueTagStr == null) || 
/* 1295 */               (this.tlp.basicCategory(tagging.tagString()).equals(trueTagStr)))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1303 */               float lexScore = this.lex.score(tagging, start);
/* 1304 */               if (lexScore > Float.NEGATIVE_INFINITY) {
/* 1305 */                 assignedSomeTag = true;
/* 1306 */                 this.iScore[start][end][state] = lexScore;
/* 1307 */                 this.narrowRExtent[start][state] = end;
/* 1308 */                 this.narrowLExtent[end][state] = start;
/* 1309 */                 this.wideRExtent[start][state] = end;
/* 1310 */                 this.wideLExtent[end][state] = start;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 1316 */               int tag = tagging.tag;
/* 1317 */               this.tags[start][tag] = 1;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1326 */         if (!assignedSomeTag)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1336 */           for (int state = 0; state < this.numStates; state++) {
/* 1337 */             if ((this.isTag[state] != 0) && (this.iScore[start][end][state] == Float.NEGATIVE_INFINITY)) {
/* 1338 */               if (trueTagStr != null) {
/* 1339 */                 String tagString = (String)this.stateNumberer.object(state);
/* 1340 */                 if (!this.tlp.basicCategory(tagString).equals(trueTagStr)) {}
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/* 1345 */                 float lexScore = this.lex.score(new IntTaggedWord(word, Numberer.translate(this.stateSpace, "tags", state)), start);
/* 1346 */                 if (lexScore > Float.NEGATIVE_INFINITY) {
/* 1347 */                   this.iScore[start][end][state] = lexScore;
/* 1348 */                   this.narrowRExtent[start][state] = end;
/* 1349 */                   this.narrowLExtent[end][state] = start;
/* 1350 */                   this.wideRExtent[start][state] = end;
/* 1351 */                   this.wideLExtent[end][state] = start;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1361 */         if (this.op.dcTags) {
/* 1362 */           for (int state = 0; state < this.numStates; state++) {
/* 1363 */             if (this.isTag[state] != 0) {
/* 1364 */               int tmp830_828 = state; float[] tmp830_827 = this.iScore[start][end];tmp830_827[tmp830_828] = ((float)(tmp830_827[tmp830_828] * (1.0D + Test.depWeight)));
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1369 */         if ((this.floodTags) && (!Test.noRecoveryTagging) && (word != boundary))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1377 */           for (int state = 0; state < this.numStates; state++) {
/* 1378 */             if ((this.isTag[state] != 0) && (this.iScore[start][end][state] == Float.NEGATIVE_INFINITY)) {
/* 1379 */               this.iScore[start][end][state] = -1000.0F;
/* 1380 */               this.narrowRExtent[start][state] = end;
/* 1381 */               this.narrowLExtent[end][state] = start;
/* 1382 */               this.wideRExtent[start][state] = end;
/* 1383 */               this.wideLExtent[end][state] = start;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1392 */         for (int state = 0; state < this.numStates; state++) {
/* 1393 */           float iS = this.iScore[start][end][state];
/* 1394 */           if (iS != Float.NEGATIVE_INFINITY)
/*      */           {
/*      */ 
/* 1397 */             UnaryRule[] unaries = this.ug.closedRulesByChild(state);
/* 1398 */             for (UnaryRule ur : unaries) {
/* 1399 */               int parentState = ur.parent;
/* 1400 */               float pS = ur.score;
/* 1401 */               float tot = iS + pS;
/* 1402 */               if (tot > this.iScore[start][end][parentState]) {
/* 1403 */                 this.iScore[start][end][parentState] = tot;
/* 1404 */                 this.narrowRExtent[start][parentState] = end;
/* 1405 */                 this.narrowLExtent[end][parentState] = start;
/* 1406 */                 this.wideRExtent[start][parentState] = end;
/* 1407 */                 this.wideLExtent[end][parentState] = start;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean hasParse()
/*      */   {
/* 1419 */     return getBestScore() > Double.NEGATIVE_INFINITY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static boolean matches(double x, double y)
/*      */   {
/* 1426 */     return Math.abs(x - y) / (Math.abs(x) + Math.abs(y) + 1.0E-10D) < 1.0E-5D;
/*      */   }
/*      */   
/*      */   public double getBestScore()
/*      */   {
/* 1431 */     return getBestScore(this.goalStr);
/*      */   }
/*      */   
/*      */   public double getBestScore(String stateName) {
/* 1435 */     if (this.length > this.arraySize) {
/* 1436 */       return Double.NEGATIVE_INFINITY;
/*      */     }
/* 1438 */     if (!this.stateNumberer.hasSeen(stateName)) {
/* 1439 */       return Double.NEGATIVE_INFINITY;
/*      */     }
/* 1441 */     int goal = this.stateNumberer.number(stateName);
/* 1442 */     return this.iScore[0][this.length][goal];
/*      */   }
/*      */   
/*      */   public Tree getBestParse()
/*      */   {
/* 1447 */     int start = 0;
/* 1448 */     int end = this.length;
/*      */     
/* 1450 */     int goal = this.stateNumberer.number(this.goalStr);
/* 1451 */     Tree internalTree = extractBestParse(goal, start, end);
/*      */     
/* 1453 */     if (internalTree == null) {
/* 1454 */       System.err.println("Warning: no parse found in ExhaustivePCFGParser.extractBestParse");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1459 */     return internalTree;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Tree extractBestParse(int goal, int start, int end)
/*      */   {
/* 1468 */     double bestScore = this.iScore[start][end][goal];
/* 1469 */     double normBestScore = Test.lengthNormalization ? bestScore / this.wordsInSpan[start][end][goal] : bestScore;
/* 1470 */     String goalStr = (String)this.stateNumberer.object(goal);
/*      */     
/*      */ 
/*      */ 
/* 1474 */     if ((end - start <= Test.maxSpanForTags) && (this.tagNumberer.hasSeen(goalStr))) {
/* 1475 */       if (Test.maxSpanForTags > 1) {
/* 1476 */         Tree wordNode = null;
/* 1477 */         if (this.sentence != null) {
/* 1478 */           StringBuilder word = new StringBuilder();
/* 1479 */           for (int i = start; i < end; i++) {
/* 1480 */             if ((this.sentence.get(i) instanceof StringLabel)) {
/* 1481 */               word.append(((StringLabel)this.sentence.get(i)).value());
/*      */             } else {
/* 1483 */               word.append((String)this.sentence.get(i));
/*      */             }
/*      */           }
/* 1486 */           wordNode = this.tf.newLeaf(new StringLabel(word.toString()));
/* 1487 */         } else if (this.lr != null) {
/* 1488 */           List<LatticeReader.LatticeWord> latticeWords = this.lr.getWordsOverSpan(start, end);
/* 1489 */           for (LatticeReader.LatticeWord lWord : latticeWords) {
/* 1490 */             IntTaggedWord itw = new IntTaggedWord(this.wordNumberer.number(lWord.word), Numberer.translate(this.stateSpace, "tags", goal));
/* 1491 */             float tagScore = this.lex.score(itw, start);
/* 1492 */             if (matches(bestScore, tagScore + (float)lWord.am)) {
/* 1493 */               wordNode = this.tf.newLeaf(new StringLabel(lWord.word));
/* 1494 */               break;
/*      */             }
/*      */           }
/* 1497 */           if (wordNode == null) {
/* 1498 */             throw new RuntimeException("could not find matching word from lattice in parse reconstruction");
/*      */           }
/*      */         } else {
/* 1501 */           throw new RuntimeException("attempt to get word when sentence and lattice are null!");
/*      */         }
/* 1503 */         Tree tagNode = this.tf.newTreeNode(new StringLabel(goalStr), Collections.singletonList(wordNode));
/* 1504 */         tagNode.setScore(bestScore);
/* 1505 */         return tagNode;
/*      */       }
/* 1507 */       IntTaggedWord tagging = new IntTaggedWord(this.words[start], this.tagNumberer.number(goalStr));
/* 1508 */       float tagScore = this.lex.score(tagging, start);
/* 1509 */       if ((tagScore > Float.NEGATIVE_INFINITY) || (this.floodTags))
/*      */       {
/* 1511 */         String wordStr = (String)this.wordNumberer.object(this.words[start]);
/* 1512 */         Tree wordNode = this.tf.newLeaf(new StringLabel(wordStr));
/* 1513 */         Tree tagNode = this.tf.newTreeNode(new StringLabel(goalStr), Collections.singletonList(wordNode));
/*      */         
/* 1515 */         tagNode.setScore(bestScore);
/*      */         
/* 1517 */         return tagNode;
/*      */       }
/*      */     }
/*      */     
/*      */     Iterator<BinaryRule> binaryI;
/* 1522 */     for (int split = start + 1; split < end; split++) {
/* 1523 */       for (binaryI = this.bg.ruleIteratorByParent(goal); binaryI.hasNext();) {
/* 1524 */         BinaryRule br = (BinaryRule)binaryI.next();
/* 1525 */         double score = br.score + this.iScore[start][split][br.leftChild] + this.iScore[split][end][br.rightChild];
/*      */         boolean matches;
/* 1527 */         boolean matches; if (Test.lengthNormalization) {
/* 1528 */           double normScore = score / (this.wordsInSpan[start][split][br.leftChild] + this.wordsInSpan[split][end][br.rightChild]);
/* 1529 */           matches = matches(normScore, normBestScore);
/*      */         } else {
/* 1531 */           matches = matches(score, bestScore);
/*      */         }
/* 1533 */         if (matches)
/*      */         {
/* 1535 */           Tree leftChildTree = extractBestParse(br.leftChild, start, split);
/* 1536 */           Tree rightChildTree = extractBestParse(br.rightChild, split, end);
/* 1537 */           List<Tree> children = new ArrayList();
/* 1538 */           children.add(leftChildTree);
/* 1539 */           children.add(rightChildTree);
/* 1540 */           Tree result = this.tf.newTreeNode(new StringLabel(goalStr), children);
/* 1541 */           result.setScore(score);
/*      */           
/* 1543 */           return result;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1553 */     for (Iterator<UnaryRule> unaryI = this.ug.ruleIteratorByParent(goal); unaryI.hasNext();) {
/* 1554 */       UnaryRule ur = (UnaryRule)unaryI.next();
/*      */       
/* 1556 */       double score = ur.score + this.iScore[start][end][ur.child];
/*      */       boolean matches;
/* 1558 */       boolean matches; if (Test.lengthNormalization) {
/* 1559 */         double normScore = score / this.wordsInSpan[start][end][ur.child];
/* 1560 */         matches = matches(normScore, normBestScore);
/*      */       } else {
/* 1562 */         matches = matches(score, bestScore);
/*      */       }
/* 1564 */       if ((ur.child != ur.parent) && (matches))
/*      */       {
/* 1566 */         Tree childTree = extractBestParse(ur.child, start, end);
/* 1567 */         Tree result = this.tf.newTreeNode(new StringLabel(goalStr), Collections.singletonList(childTree));
/*      */         
/*      */ 
/* 1570 */         result.setScore(score);
/* 1571 */         return result;
/*      */       }
/*      */     }
/* 1574 */     System.err.println("Warning: no parse found in ExhaustivePCFGParser.extractBestParse: failing on: [" + start + ", " + end + "] looking for " + goalStr);
/* 1575 */     return null;
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
/*      */   protected List<Tree> extractBestParses(int goal, int start, int end)
/*      */   {
/* 1616 */     double bestScore = this.iScore[start][end][goal];
/* 1617 */     String goalStr = (String)this.stateNumberer.object(goal);
/*      */     
/*      */ 
/* 1620 */     if ((end - start == 1) && (this.tagNumberer.hasSeen(goalStr))) {
/* 1621 */       IntTaggedWord tagging = new IntTaggedWord(this.words[start], this.tagNumberer.number(goalStr));
/* 1622 */       float tagScore = this.lex.score(tagging, start);
/* 1623 */       if ((tagScore > Float.NEGATIVE_INFINITY) || (this.floodTags))
/*      */       {
/* 1625 */         String wordStr = (String)this.wordNumberer.object(this.words[start]);
/* 1626 */         Tree wordNode = this.tf.newLeaf(new StringLabel(wordStr));
/* 1627 */         Tree tagNode = this.tf.newTreeNode(new StringLabel(goalStr), Collections.singletonList(wordNode));
/*      */         
/*      */ 
/* 1630 */         return Collections.singletonList(tagNode);
/*      */       }
/*      */     }
/*      */     
/* 1634 */     List<Tree> bestTrees = new ArrayList();
/* 1635 */     Iterator<BinaryRule> binaryI; List<Tree> rightChildTrees; Iterator i$; Tree leftChildTree; for (int split = start + 1; split < end; split++) {
/* 1636 */       for (binaryI = this.bg.ruleIteratorByParent(goal); binaryI.hasNext();) {
/* 1637 */         BinaryRule br = (BinaryRule)binaryI.next();
/* 1638 */         double score = br.score + this.iScore[start][split][br.leftChild] + this.iScore[split][end][br.rightChild];
/* 1639 */         if (matches(score, bestScore))
/*      */         {
/* 1641 */           List<Tree> leftChildTrees = extractBestParses(br.leftChild, start, split);
/* 1642 */           rightChildTrees = extractBestParses(br.rightChild, split, end);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1647 */           for (i$ = leftChildTrees.iterator(); i$.hasNext();) { leftChildTree = (Tree)i$.next();
/* 1648 */             for (Tree rightChildTree : rightChildTrees) {
/* 1649 */               List<Tree> children = new ArrayList();
/* 1650 */               children.add(leftChildTree);
/* 1651 */               children.add(rightChildTree);
/* 1652 */               Tree result = this.tf.newTreeNode(new StringLabel(goalStr), children);
/*      */               
/* 1654 */               bestTrees.add(result);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1661 */     for (Iterator<UnaryRule> unaryI = this.ug.ruleIteratorByParent(goal); unaryI.hasNext();) {
/* 1662 */       UnaryRule ur = (UnaryRule)unaryI.next();
/* 1663 */       double score = ur.score + this.iScore[start][end][ur.child];
/* 1664 */       if ((ur.child != ur.parent) && (matches(score, bestScore)))
/*      */       {
/* 1666 */         List<Tree> childTrees = extractBestParses(ur.child, start, end);
/* 1667 */         for (Tree childTree : childTrees) {
/* 1668 */           Tree result = this.tf.newTreeNode(new StringLabel(goalStr), Collections.singletonList(childTree));
/*      */           
/*      */ 
/* 1671 */           bestTrees.add(result);
/*      */         }
/*      */       }
/*      */     }
/* 1675 */     if (bestTrees.isEmpty()) {
/* 1676 */       System.err.println("Warning: no parse found in ExhaustivePCFGParser.extractBestParse: failing on: [" + start + ", " + end + "] looking for " + goalStr);
/*      */     }
/* 1678 */     return bestTrees;
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
/*      */   public List<ScoredObject<Tree>> getKGoodParses(int k)
/*      */   {
/* 1691 */     return getKBestParses(k);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<ScoredObject<Tree>> getKSampledParses(int k)
/*      */   {
/* 1702 */     throw new UnsupportedOperationException("ExhaustivePCFGParser doesn't sample.");
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
/*      */   public List<ScoredObject<Tree>> getKBestParses(int k)
/*      */   {
/* 1721 */     this.cand = new HashMap();
/* 1722 */     this.dHat = new HashMap();
/*      */     
/* 1724 */     int start = 0;
/* 1725 */     int end = this.length;
/* 1726 */     int goal = this.stateNumberer.number(this.goalStr);
/*      */     
/* 1728 */     Vertex v = new Vertex(goal, start, end);
/* 1729 */     List<ScoredObject<Tree>> kBestTrees = new ArrayList();
/* 1730 */     for (int i = 1; i <= k; i++) {
/* 1731 */       Tree internalTree = getTree(v, i, k);
/* 1732 */       if (internalTree == null)
/*      */         break;
/* 1734 */       kBestTrees.add(new ScoredObject(internalTree, ((Derivation)((LinkedList)this.dHat.get(v)).get(i - 1)).score));
/*      */     }
/* 1736 */     return kBestTrees;
/*      */   }
/*      */   
/*      */   private Tree getTree(Vertex v, int k, int kPrime)
/*      */   {
/* 1741 */     lazyKthBest(v, k, kPrime);
/* 1742 */     String goalStr = (String)this.stateNumberer.object(v.goal);
/* 1743 */     int start = v.start;
/*      */     
/*      */ 
/* 1746 */     List<Derivation> dHatV = (List)this.dHat.get(v);
/*      */     
/* 1748 */     if (this.isTag[v.goal] != 0) {
/* 1749 */       IntTaggedWord tagging = new IntTaggedWord(this.words[start], this.tagNumberer.number(goalStr));
/* 1750 */       float tagScore = this.lex.score(tagging, start);
/* 1751 */       if ((tagScore > Float.NEGATIVE_INFINITY) || (this.floodTags))
/*      */       {
/* 1753 */         String wordStr = (String)this.wordNumberer.object(this.words[start]);
/* 1754 */         Tree wordNode = this.tf.newLeaf(new StringLabel(wordStr));
/* 1755 */         return this.tf.newTreeNode(new StringLabel(goalStr), Collections.singletonList(wordNode));
/*      */       }
/*      */       
/* 1758 */       if (!$assertionsDisabled) { throw new AssertionError();
/*      */       }
/*      */     }
/*      */     
/* 1762 */     if (k - 1 >= dHatV.size()) {
/* 1763 */       return null;
/*      */     }
/*      */     
/* 1766 */     Derivation d = (Derivation)dHatV.get(k - 1);
/*      */     
/* 1768 */     List<Tree> children = new ArrayList();
/* 1769 */     for (int i = 0; i < d.arc.size(); i++) {
/* 1770 */       Vertex child = (Vertex)d.arc.tails.get(i);
/* 1771 */       Tree t = getTree(child, ((Integer)d.j.get(i)).intValue(), kPrime);
/* 1772 */       assert (t != null);
/* 1773 */       children.add(t);
/*      */     }
/*      */     
/* 1776 */     return this.tf.newTreeNode(new StringLabel(goalStr), children);
/*      */   }
/*      */   
/*      */   private static class Vertex {
/*      */     public final int goal;
/*      */     public final int start;
/*      */     public final int end;
/*      */     
/*      */     public Vertex(int goal, int start, int end) {
/* 1785 */       this.goal = goal;
/* 1786 */       this.start = start;
/* 1787 */       this.end = end;
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/* 1791 */       if (!(o instanceof Vertex)) return false;
/* 1792 */       Vertex v = (Vertex)o;
/* 1793 */       return (v.goal == this.goal) && (v.start == this.start) && (v.end == this.end);
/*      */     }
/*      */     
/* 1796 */     private int hc = -1;
/*      */     
/*      */     public int hashCode() {
/* 1799 */       if (this.hc == -1) {
/* 1800 */         this.hc = (this.goal + 17 * (this.start + 17 * this.end));
/*      */       }
/* 1802 */       return this.hc;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1806 */       return this.goal + "[" + this.start + "," + this.end + "]";
/*      */     }
/*      */   }
/*      */   
/*      */   private static class Arc {
/*      */     public final List<ExhaustivePCFGParser.Vertex> tails;
/*      */     public final ExhaustivePCFGParser.Vertex head;
/*      */     public final double ruleScore;
/*      */     
/*      */     public Arc(List<ExhaustivePCFGParser.Vertex> tails, ExhaustivePCFGParser.Vertex head, double ruleScore) {
/* 1816 */       this.tails = Collections.unmodifiableList(tails);
/* 1817 */       this.head = head;
/* 1818 */       this.ruleScore = ruleScore;
/*      */     }
/*      */     
/*      */     public boolean equals(Object o)
/*      */     {
/* 1823 */       if (!(o instanceof Arc)) return false;
/* 1824 */       Arc a = (Arc)o;
/* 1825 */       return (a.head.equals(this.head)) && (a.tails.equals(this.tails));
/*      */     }
/*      */     
/* 1828 */     private int hc = -1;
/*      */     
/*      */     public int hashCode() {
/* 1831 */       if (this.hc == -1) {
/* 1832 */         this.hc = (this.head.hashCode() + 17 * this.tails.hashCode());
/*      */       }
/* 1834 */       return this.hc;
/*      */     }
/*      */     
/* 1837 */     public int size() { return this.tails.size(); }
/*      */   }
/*      */   
/*      */   private static class Derivation {
/*      */     public final ExhaustivePCFGParser.Arc arc;
/*      */     public final List<Integer> j;
/*      */     public final double score;
/*      */     public final List<Double> childrenScores;
/*      */     
/*      */     public Derivation(ExhaustivePCFGParser.Arc arc, List<Integer> j, double score, List<Double> childrenScores) {
/* 1847 */       this.arc = arc;
/* 1848 */       this.j = Collections.unmodifiableList(j);
/* 1849 */       this.score = score;
/* 1850 */       this.childrenScores = Collections.unmodifiableList(childrenScores);
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/* 1854 */       if (!(o instanceof Derivation)) return false;
/* 1855 */       Derivation d = (Derivation)o;
/* 1856 */       if (((this.arc == null) && (d.arc != null)) || ((this.arc != null) && (d.arc == null))) return false;
/* 1857 */       return ((this.arc == null) && (d.arc == null)) || ((d.arc.equals(this.arc)) && (d.j.equals(this.j)));
/*      */     }
/*      */     
/* 1860 */     private int hc = -1;
/*      */     
/*      */     public int hashCode() {
/* 1863 */       if (this.hc == -1) {
/* 1864 */         this.hc = ((this.arc == null ? 0 : this.arc.hashCode()) + 17 * this.j.hashCode());
/*      */       }
/* 1866 */       return this.hc;
/*      */     }
/*      */   }
/*      */   
/*      */   private List<Arc> getBackwardsStar(Vertex v)
/*      */   {
/* 1872 */     List<Arc> bs = new ArrayList();
/*      */     
/*      */ 
/* 1875 */     if (this.isTag[v.goal] != 0) {
/* 1876 */       List<Vertex> tails = new ArrayList();
/* 1877 */       double score = this.iScore[v.start][v.end][v.goal];
/* 1878 */       Arc arc = new Arc(tails, v, score);
/* 1879 */       bs.add(arc);
/*      */     }
/*      */     
/*      */ 
/* 1883 */     for (int split = v.start + 1; split < v.end; split++) {
/* 1884 */       for (BinaryRule br : this.bg.ruleListByParent(v.goal)) {
/* 1885 */         Vertex lChild = new Vertex(br.leftChild, v.start, split);
/* 1886 */         Vertex rChild = new Vertex(br.rightChild, split, v.end);
/* 1887 */         List<Vertex> tails = new ArrayList();
/* 1888 */         tails.add(lChild);
/* 1889 */         tails.add(rChild);
/* 1890 */         Arc arc = new Arc(tails, v, br.score);
/* 1891 */         bs.add(arc);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1896 */     for (UnaryRule ur : this.ug.rulesByParent(v.goal)) {
/* 1897 */       Vertex child = new Vertex(ur.child, v.start, v.end);
/* 1898 */       List<Vertex> tails = new ArrayList();
/* 1899 */       tails.add(child);
/* 1900 */       Arc arc = new Arc(tails, v, ur.score);
/* 1901 */       bs.add(arc);
/*      */     }
/*      */     
/* 1904 */     return bs;
/*      */   }
/*      */   
/* 1907 */   private Map<Vertex, PriorityQueue<Derivation>> cand = new HashMap();
/* 1908 */   private Map<Vertex, LinkedList<Derivation>> dHat = new HashMap();
/*      */   
/*      */   private PriorityQueue<Derivation> getCandidates(Vertex v, int k) {
/* 1911 */     PriorityQueue<Derivation> candV = (PriorityQueue)this.cand.get(v);
/* 1912 */     if (candV == null) {
/* 1913 */       candV = new BinaryHeapPriorityQueue();
/* 1914 */       List<Arc> bsV = getBackwardsStar(v);
/*      */       
/* 1916 */       for (Arc arc : bsV) {
/* 1917 */         int size = arc.size();
/* 1918 */         double score = arc.ruleScore;
/* 1919 */         List<Double> childrenScores = new ArrayList();
/* 1920 */         for (int i = 0; i < size; i++) {
/* 1921 */           Vertex child = (Vertex)arc.tails.get(i);
/* 1922 */           double s = this.iScore[child.start][child.end][child.goal];
/* 1923 */           childrenScores.add(Double.valueOf(s));
/* 1924 */           score += s;
/*      */         }
/* 1926 */         if (score != Double.NEGATIVE_INFINITY) {
/* 1927 */           List<Integer> j = new ArrayList();
/* 1928 */           for (int i = 0; i < size; i++) {
/* 1929 */             j.add(Integer.valueOf(1));
/*      */           }
/* 1931 */           Derivation d = new Derivation(arc, j, score, childrenScores);
/* 1932 */           candV.add(d, score);
/*      */         } }
/* 1934 */       PriorityQueue<Derivation> tmp = new BinaryHeapPriorityQueue();
/* 1935 */       for (int i = 0; i < k; i++) {
/* 1936 */         if (candV.isEmpty()) break;
/* 1937 */         Derivation d = (Derivation)candV.removeFirst();
/* 1938 */         tmp.add(d, d.score);
/*      */       }
/* 1940 */       candV = tmp;
/* 1941 */       this.cand.put(v, candV);
/*      */     }
/* 1943 */     return candV;
/*      */   }
/*      */   
/*      */   private void lazyKthBest(Vertex v, int k, int kPrime)
/*      */   {
/* 1948 */     PriorityQueue<Derivation> candV = getCandidates(v, kPrime);
/*      */     
/* 1950 */     LinkedList<Derivation> dHatV = (LinkedList)this.dHat.get(v);
/* 1951 */     if (dHatV == null) {
/* 1952 */       dHatV = new LinkedList();
/* 1953 */       this.dHat.put(v, dHatV);
/*      */     }
/* 1955 */     while (dHatV.size() < k) {
/* 1956 */       if (!dHatV.isEmpty()) {
/* 1957 */         Derivation derivation = (Derivation)dHatV.getLast();
/* 1958 */         lazyNext(candV, derivation, kPrime);
/*      */       }
/* 1960 */       if (candV.isEmpty()) break;
/* 1961 */       Derivation d = (Derivation)candV.removeFirst();
/* 1962 */       dHatV.add(d);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void lazyNext(PriorityQueue<Derivation> candV, Derivation derivation, int kPrime)
/*      */   {
/* 1970 */     List<Vertex> tails = derivation.arc.tails;
/* 1971 */     int i = 0; for (int sz = derivation.arc.size(); i < sz; i++) {
/* 1972 */       List<Integer> j = new ArrayList(derivation.j);
/* 1973 */       j.set(i, Integer.valueOf(((Integer)j.get(i)).intValue() + 1));
/* 1974 */       Vertex Ti = (Vertex)tails.get(i);
/* 1975 */       lazyKthBest(Ti, ((Integer)j.get(i)).intValue(), kPrime);
/* 1976 */       LinkedList<Derivation> dHatTi = (LinkedList)this.dHat.get(Ti);
/*      */       
/* 1978 */       if (((Integer)j.get(i)).intValue() - 1 < dHatTi.size()) {
/* 1979 */         Derivation d = (Derivation)dHatTi.get(((Integer)j.get(i)).intValue() - 1);
/* 1980 */         double newScore = derivation.score - ((Double)derivation.childrenScores.get(i)).doubleValue() + d.score;
/* 1981 */         List<Double> childrenScores = new ArrayList(derivation.childrenScores);
/* 1982 */         childrenScores.set(i, Double.valueOf(d.score));
/* 1983 */         Derivation newDerivation = new Derivation(derivation.arc, j, newScore, childrenScores);
/* 1984 */         if ((!candV.contains(newDerivation)) && (newScore > Double.NEGATIVE_INFINITY)) {
/* 1985 */           candV.add(newDerivation, newScore);
/*      */         }
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
/*      */ 
/*      */   public List<ScoredObject<Tree>> getBestParses()
/*      */   {
/* 2002 */     int start = 0;
/* 2003 */     int end = this.length;
/* 2004 */     int goal = this.stateNumberer.number(this.goalStr);
/* 2005 */     double bestScore = this.iScore[start][end][goal];
/* 2006 */     List<Tree> internalTrees = extractBestParses(goal, start, end);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2012 */     List<ScoredObject<Tree>> scoredTrees = new ArrayList(internalTrees.size());
/* 2013 */     for (Tree tr : internalTrees) {
/* 2014 */       scoredTrees.add(new ScoredObject(tr, bestScore));
/*      */     }
/* 2016 */     return scoredTrees;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ExhaustivePCFGParser(BinaryGrammar bg, UnaryGrammar ug, Lexicon lex, Options op)
/*      */   {
/* 2024 */     this.op = op;
/* 2025 */     this.tlp = op.langpack();
/* 2026 */     this.goalStr = this.tlp.startSymbol();
/* 2027 */     this.stateSpace = bg.stateSpace();
/* 2028 */     this.stateNumberer = Numberer.getGlobalNumberer(this.stateSpace);
/* 2029 */     this.bg = bg;
/* 2030 */     this.ug = ug;
/* 2031 */     this.lex = lex;
/* 2032 */     this.tf = new LabeledScoredTreeFactory(new StringLabelFactory());
/*      */     
/* 2034 */     this.numStates = this.stateNumberer.total();
/* 2035 */     this.isTag = new boolean[this.numStates];
/* 2036 */     for (int state = 0; state < this.numStates; state++) {
/* 2037 */       this.isTag[state] = this.tagNumberer.hasSeen(this.stateNumberer.object(state));
/*      */     }
/*      */   }
/*      */   
/*      */   public void nudgeDownArraySize()
/*      */   {
/*      */     try {
/* 2044 */       if (this.arraySize > 2) {
/* 2045 */         considerCreatingArrays(this.arraySize - 2);
/*      */       }
/*      */     } catch (OutOfMemoryError oome) {
/* 2048 */       oome.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   private void considerCreatingArrays(int length) {
/* 2053 */     if ((length > Test.maxLength + 1) || (length >= this.myMaxLength)) {
/* 2054 */       throw new OutOfMemoryError("Refusal to create such large arrays.");
/*      */     }
/*      */     try {
/* 2057 */       createArrays(length + 1);
/*      */     } catch (OutOfMemoryError e) {
/* 2059 */       this.myMaxLength = length;
/* 2060 */       if (this.arraySize > 0) {
/*      */         try {
/* 2062 */           createArrays(this.arraySize);
/*      */         } catch (OutOfMemoryError e2) {
/* 2064 */           throw new RuntimeException("CANNOT EVEN CREATE ARRAYS OF ORIGINAL SIZE!!");
/*      */         }
/*      */       }
/* 2067 */       throw e;
/*      */     }
/* 2069 */     this.arraySize = (length + 1);
/* 2070 */     if (Test.verbose) {
/* 2071 */       System.err.println("Created PCFG parser arrays of size " + this.arraySize);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void createArrays(int length)
/*      */   {
/* 2078 */     clearArrays();
/*      */     
/* 2080 */     int numTags = this.tagNumberer.total();
/*      */     
/*      */ 
/* 2083 */     this.iScore = new float[length + 1][length + 1][];
/* 2084 */     for (int start = 0; start <= length; start++) {
/* 2085 */       for (int end = start + 1; end <= length; end++) {
/* 2086 */         this.iScore[start][end] = new float[this.numStates];
/*      */       }
/*      */     }
/*      */     
/* 2090 */     if ((this.op.doDep) && (!Test.useFastFactored))
/*      */     {
/* 2092 */       this.oScore = new float[length + 1][length + 1][];
/* 2093 */       for (int start = 0; start <= length; start++) {
/* 2094 */         for (int end = start + 1; end <= length; end++) {
/* 2095 */           this.oScore[start][end] = new float[this.numStates];
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2100 */     this.iPossibleByL = new boolean[length + 1][this.numStates];
/* 2101 */     this.iPossibleByR = new boolean[length + 1][this.numStates];
/* 2102 */     this.narrowRExtent = new int[length + 1][this.numStates];
/* 2103 */     this.wideRExtent = new int[length + 1][this.numStates];
/* 2104 */     this.narrowLExtent = new int[length + 1][this.numStates];
/* 2105 */     this.wideLExtent = new int[length + 1][this.numStates];
/* 2106 */     if ((this.op.doDep) && (!Test.useFastFactored)) {
/* 2107 */       this.oPossibleByL = new boolean[length + 1][this.numStates];
/* 2108 */       this.oPossibleByR = new boolean[length + 1][this.numStates];
/*      */       
/* 2110 */       this.oFilteredStart = new boolean[length + 1][this.numStates];
/* 2111 */       this.oFilteredEnd = new boolean[length + 1][this.numStates];
/*      */     }
/* 2113 */     this.tags = new boolean[length + 1][numTags];
/*      */     
/* 2115 */     if (Test.lengthNormalization) {
/* 2116 */       this.wordsInSpan = new int[length + 1][length + 1][];
/* 2117 */       for (int start = 0; start <= length; start++) {
/* 2118 */         for (int end = start + 1; end <= length; end++) {
/* 2119 */           this.wordsInSpan[start][end] = new int[this.numStates];
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void clearArrays()
/*      */   {
/* 2127 */     this.iScore = (this.oScore = (float[][][])null);
/* 2128 */     this.iPossibleByL = (this.iPossibleByR = this.oFilteredEnd = this.oFilteredStart = this.oPossibleByL = this.oPossibleByR = this.tags = (boolean[][])null);
/* 2129 */     this.narrowRExtent = (this.wideRExtent = this.narrowLExtent = this.wideLExtent = (int[][])null);
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ExhaustivePCFGParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */