/*    */ package edu.stanford.nlp.fsm;
/*    */ 
/*    */ import edu.stanford.nlp.parser.lexparser.GrammarCompactor;
/*    */ import edu.stanford.nlp.trees.PennTreebankLanguagePack;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ public class ExactGrammarCompactor
/*    */   extends GrammarCompactor
/*    */ {
/* 13 */   TransducerGraph.GraphProcessor quasiDeterminizer = new QuasiDeterminizer();
/* 14 */   AutomatonMinimizer minimizer = new FastExactAutomatonMinimizer();
/* 15 */   TransducerGraph.NodeProcessor ntsp = new TransducerGraph.SetToStringNodeProcessor(new PennTreebankLanguagePack());
/* 16 */   TransducerGraph.NodeProcessor otsp = new TransducerGraph.ObjectToSetNodeProcessor();
/* 17 */   TransducerGraph.ArcProcessor isp = new TransducerGraph.InputSplittingProcessor();
/* 18 */   TransducerGraph.ArcProcessor ocp = new TransducerGraph.OutputCombiningProcessor();
/* 19 */   private boolean saveGraphs = false;
/*    */   
/*    */   public ExactGrammarCompactor(boolean saveGraphs, boolean verbose) {
/* 22 */     this.saveGraphs = saveGraphs;
/* 23 */     this.verbose = verbose;
/* 24 */     this.outputType = NORMALIZED_LOG_PROBABILITIES;
/*    */   }
/*    */   
/*    */   protected TransducerGraph doCompaction(TransducerGraph graph, List l1, List l3) {
/* 28 */     TransducerGraph result = graph;
/* 29 */     if (this.saveGraphs) {
/* 30 */       writeFile(result, "unminimized", (String)result.getEndNodes().iterator().next());
/*    */     }
/* 32 */     result = this.quasiDeterminizer.processGraph(result);
/* 33 */     result = new TransducerGraph(result, this.ocp);
/* 34 */     result = this.minimizer.minimizeFA(result);
/*    */     
/* 36 */     result = new TransducerGraph(result, this.ntsp);
/* 37 */     result = new TransducerGraph(result, this.isp);
/* 38 */     if (this.saveGraphs) {
/* 39 */       writeFile(result, "exactminimized", (String)result.getEndNodes().iterator().next());
/*    */     }
/*    */     
/*    */ 
/* 43 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\fsm\ExactGrammarCompactor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */