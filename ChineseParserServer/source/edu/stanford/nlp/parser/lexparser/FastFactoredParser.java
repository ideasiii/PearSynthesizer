/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTagFactory;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.parser.KBestViterbiParser;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.util.Beam;
/*     */ import edu.stanford.nlp.util.ScoredObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class FastFactoredParser
/*     */   implements KBestViterbiParser
/*     */ {
/*     */   protected static final boolean VERBOSE = false;
/*     */   protected ExhaustivePCFGParser pparser;
/*     */   protected GrammarProjection projection;
/*     */   protected MLEDependencyGrammar dg;
/*     */   protected Options op;
/*     */   private int numToFind;
/*     */   
/*     */   protected int project(int state)
/*     */   {
/*  43 */     return this.projection.project(state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree getBestParse()
/*     */   {
/*  52 */     return (Tree)((ScoredObject)this.nGoodTrees.get(0)).object();
/*     */   }
/*     */   
/*     */   public double getBestScore() {
/*  56 */     return ((ScoredObject)this.nGoodTrees.get(0)).score();
/*     */   }
/*     */   
/*     */   public boolean hasParse()
/*     */   {
/*  61 */     return !this.nGoodTrees.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*  65 */   private List<ScoredObject<Tree>> nGoodTrees = new ArrayList();
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
/*  78 */     if (k <= this.nGoodTrees.size()) {
/*  79 */       return this.nGoodTrees.subList(0, k);
/*     */     }
/*  81 */     throw new UnsupportedOperationException("FastFactoredParser: cannot provide " + k + " good parses.");
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
/*     */   private double depScoreTree(Tree tr)
/*     */   {
/*  95 */     Tree cwtTree = tr.deeperCopy(new LabeledScoredTreeFactory(), new CategoryWordTagFactory());
/*  96 */     cwtTree.percolateHeads(this.binHeadFinder);
/*     */     
/*     */ 
/*  99 */     List<IntDependency> deps = MLEDependencyGrammar.treeToDependencyList(cwtTree);
/*     */     
/* 101 */     return this.dg.scoreAll(deps);
/*     */   }
/*     */   
/* 104 */   private final HeadFinder binHeadFinder = new BinaryHeadFinder(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class BinaryHeadFinder
/*     */     implements HeadFinder
/*     */   {
/*     */     public Tree determineHead(Tree t)
/*     */     {
/* 119 */       if (t.numChildren() == 1) {
/* 120 */         return t.firstChild();
/*     */       }
/* 122 */       String lval = t.firstChild().label().value();
/* 123 */       if ((lval != null) && (lval.startsWith("@"))) {
/* 124 */         return t.firstChild();
/*     */       }
/* 126 */       String rval = t.lastChild().label().value();
/* 127 */       if ((rval.startsWith("@")) || (rval.equals(".$$."))) {
/* 128 */         return t.lastChild();
/*     */       }
/*     */       
/*     */ 
/* 132 */       throw new IllegalStateException("BinaryHeadFinder: unexpected tree: " + t);
/*     */     }
/*     */     
/*     */     public Tree determineHead(Tree t, Tree parent) {
/* 136 */       return determineHead(t);
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
/* 148 */     return parse(sentence);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean parse(List<? extends HasWord> words)
/*     */   {
/* 160 */     this.nGoodTrees.clear();
/*     */     
/* 162 */     int numParsesToConsider = this.numToFind * Test.fastFactoredCandidateMultiplier + Test.fastFactoredCandidateAddend;
/*     */     
/* 164 */     if (this.pparser.hasParse()) {
/* 165 */       List<ScoredObject<Tree>> pcfgBest = this.pparser.getKBestParses(numParsesToConsider);
/* 166 */       Beam<ScoredObject<Tree>> goodParses = new Beam(this.numToFind);
/*     */       
/* 168 */       for (ScoredObject<Tree> candidate : pcfgBest) {
/* 169 */         double depScore = depScoreTree((Tree)candidate.object());
/* 170 */         ScoredObject<Tree> x = new ScoredObject(candidate.object(), candidate.score() + depScore);
/* 171 */         goodParses.add(x);
/*     */       }
/* 173 */       this.nGoodTrees = goodParses.asSortedList();
/*     */     }
/* 175 */     return !this.nGoodTrees.isEmpty();
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
/* 186 */     throw new UnsupportedOperationException();
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
/* 197 */     throw new UnsupportedOperationException();
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
/* 208 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   FastFactoredParser(ExhaustivePCFGParser pparser, MLEDependencyGrammar dg, Options op, int numToFind)
/*     */   {
/* 213 */     this(pparser, dg, op, numToFind, new NullGrammarProjection(null, null));
/*     */   }
/*     */   
/*     */   FastFactoredParser(ExhaustivePCFGParser pparser, MLEDependencyGrammar dg, Options op, int numToFind, GrammarProjection projection) {
/* 217 */     this.pparser = pparser;
/* 218 */     this.projection = projection;
/* 219 */     this.dg = dg;
/* 220 */     this.op = op;
/* 221 */     this.numToFind = numToFind;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\FastFactoredParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */