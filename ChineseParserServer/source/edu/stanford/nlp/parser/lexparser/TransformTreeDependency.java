/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.process.Function;
/*    */ import edu.stanford.nlp.trees.LeftHeadFinder;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TransformTreeDependency
/*    */   implements Function<Tree, Tree>
/*    */ {
/*    */   TreeAnnotatorAndBinarizer binarizer;
/*    */   CollinsPuncTransformer collinsPuncTransformer;
/*    */   
/*    */   public TransformTreeDependency(TreebankLangParserParams tlpParams, boolean forceCNF)
/*    */   {
/* 19 */     if (!Train.leftToRight) {
/* 20 */       this.binarizer = new TreeAnnotatorAndBinarizer(tlpParams, forceCNF, !Train.outsideFactor(), true);
/*    */     } else {
/* 22 */       this.binarizer = new TreeAnnotatorAndBinarizer(tlpParams.headFinder(), new LeftHeadFinder(), tlpParams, forceCNF, !Train.outsideFactor(), true);
/*    */     }
/* 24 */     if (Train.collinsPunc) {
/* 25 */       this.collinsPuncTransformer = new CollinsPuncTransformer(tlpParams.treebankLanguagePack());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public Tree apply(Tree tree)
/*    */   {
/* 32 */     if (Train.hSelSplit) {
/* 33 */       this.binarizer.setDoSelectiveSplit(false);
/* 34 */       if (Train.collinsPunc) {
/* 35 */         tree = this.collinsPuncTransformer.transformTree(tree);
/*    */       }
/* 37 */       this.binarizer.transformTree(tree);
/* 38 */       this.binarizer.setDoSelectiveSplit(true);
/*    */     }
/*    */     
/* 41 */     if (Train.collinsPunc) {
/* 42 */       tree = this.collinsPuncTransformer.transformTree(tree);
/*    */     }
/* 44 */     tree = this.binarizer.transformTree(tree);
/* 45 */     return tree;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\TransformTreeDependency.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */