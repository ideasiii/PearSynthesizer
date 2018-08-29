/*    */ package edu.stanford.nlp.trees.international.arabic;
/*    */ 
/*    */ import edu.stanford.nlp.trees.FilteringTreeReader;
/*    */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*    */ import edu.stanford.nlp.trees.PennTreeReader;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.TreeReader;
/*    */ import edu.stanford.nlp.trees.TreeReaderFactory;
/*    */ import edu.stanford.nlp.util.Filter;
/*    */ import java.io.Reader;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArabicTreeReaderFactory
/*    */   implements TreeReaderFactory, Serializable
/*    */ {
/*    */   private boolean retainNPTmp;
/*    */   private boolean retainPRD;
/*    */   private boolean changeNoLabels;
/*    */   private boolean filterX;
/*    */   
/*    */   public ArabicTreeReaderFactory()
/*    */   {
/* 25 */     this(false, false, false, false);
/*    */   }
/*    */   
/*    */   public ArabicTreeReaderFactory(boolean retainNPTmp, boolean retainPRD, boolean changeNoLabels, boolean filterX)
/*    */   {
/* 30 */     this.retainNPTmp = retainNPTmp;
/* 31 */     this.retainPRD = retainPRD;
/* 32 */     this.changeNoLabels = changeNoLabels;
/* 33 */     this.filterX = filterX;
/*    */   }
/*    */   
/*    */   public TreeReader newTreeReader(Reader in) {
/* 37 */     TreeReader tr = new PennTreeReader(in, new LabeledScoredTreeFactory(), new ArabicTreeNormalizer(this.retainNPTmp, this.retainPRD, this.changeNoLabels), new ArabicTreebankTokenizer(in));
/* 38 */     if (this.filterX) {
/* 39 */       tr = new FilteringTreeReader(tr, new XFilter());
/*    */     }
/* 41 */     return tr;
/*    */   }
/*    */   
/*    */ 
/*    */   static class XFilter
/*    */     implements Filter<Tree>
/*    */   {
/*    */     public boolean accept(Tree t)
/*    */     {
/* 50 */       return (t.numChildren() != 1) || (!"X".equals(t.firstChild().value()));
/*    */     }
/*    */   }
/*    */   
/*    */   public static class ArabicXFilteringTreeReaderFactory
/*    */     extends ArabicTreeReaderFactory
/*    */   {
/*    */     public ArabicXFilteringTreeReaderFactory()
/*    */     {
/* 59 */       super(false, false, true);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class ArabicRawTreeReaderFactory
/*    */     extends ArabicTreeReaderFactory
/*    */   {
/*    */     public ArabicRawTreeReaderFactory()
/*    */     {
/* 68 */       super(false, true, false);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\arabic\ArabicTreeReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */