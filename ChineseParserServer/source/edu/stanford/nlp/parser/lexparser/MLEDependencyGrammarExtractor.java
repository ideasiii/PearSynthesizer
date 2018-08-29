/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.stats.Counter;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.util.Numberer;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MLEDependencyGrammarExtractor
/*    */   extends AbstractTreeExtractor
/*    */ {
/* 17 */   protected Numberer wordNumberer = Numberer.getGlobalNumberer("words");
/* 18 */   protected Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
/*    */   
/*    */ 
/* 21 */   protected Counter<IntDependency> dependencyCounter = new Counter();
/*    */   
/*    */ 
/*    */   protected TreebankLangParserParams tlpParams;
/*    */   
/*    */ 
/*    */   protected boolean directional;
/*    */   
/*    */   protected boolean useDistance;
/*    */   
/*    */   protected boolean useCoarseDistance;
/*    */   
/*    */ 
/*    */   public MLEDependencyGrammarExtractor(Options op)
/*    */   {
/* 36 */     this.tlpParams = op.tlpParams;
/* 37 */     this.directional = op.directional;
/* 38 */     this.useDistance = op.distance;
/* 39 */     this.useCoarseDistance = op.coarseDistance;
/*    */   }
/*    */   
/*    */   protected void tallyRoot(Tree lt)
/*    */   {
/* 44 */     List<IntDependency> deps = MLEDependencyGrammar.treeToDependencyList(lt);
/* 45 */     for (IntDependency dependency : deps) {
/* 46 */       this.dependencyCounter.incrementCount(dependency);
/*    */     }
/*    */   }
/*    */   
/*    */   public Object formResult() {
/* 51 */     this.wordNumberer.number("UNK");
/* 52 */     MLEDependencyGrammar dg = new MLEDependencyGrammar(this.tlpParams, this.directional, this.useDistance, this.useCoarseDistance);
/* 53 */     for (IntDependency dependency : this.dependencyCounter.keySet()) {
/* 54 */       dg.addRule(dependency, this.dependencyCounter.getCount(dependency));
/*    */     }
/* 56 */     return dg;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\MLEDependencyGrammarExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */