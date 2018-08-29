/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.ling.StringLabelFactory;
/*    */ import edu.stanford.nlp.trees.DiskTreebank;
/*    */ import edu.stanford.nlp.trees.HeadFinder;
/*    */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*    */ import edu.stanford.nlp.trees.MemoryTreebank;
/*    */ import edu.stanford.nlp.trees.PennTreeReader;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.TreeReader;
/*    */ import edu.stanford.nlp.trees.TreeReaderFactory;
/*    */ import edu.stanford.nlp.trees.TreeTransformer;
/*    */ import edu.stanford.nlp.trees.international.tuebadz.TueBaDZHeadFinder;
/*    */ import edu.stanford.nlp.trees.international.tuebadz.TueBaDZLanguagePack;
/*    */ import edu.stanford.nlp.trees.international.tuebadz.TueBaDZPennTreeNormalizer;
/*    */ import java.io.PrintStream;
/*    */ import java.io.Reader;
/*    */ import java.io.Serializable;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TueBaDZParserParams
/*    */   extends AbstractTreebankParserParams
/*    */ {
/*    */   private static final long serialVersionUID = 7303189408025355170L;
/*    */   
/*    */   public TueBaDZParserParams()
/*    */   {
/* 36 */     super(new TueBaDZLanguagePack());
/*    */   }
/*    */   
/*    */   public List defaultTestSentence()
/*    */   {
/* 41 */     return Arrays.asList(new String[] { "Veruntreute", "die", "AWO", "Spendengeld", "?" });
/*    */   }
/*    */   
/*    */   public String[] sisterSplitters() {
/* 45 */     return new String[0];
/*    */   }
/*    */   
/*    */   public TreeTransformer collinizer() {
/* 49 */     return new TreeCollinizer(this.tlp);
/*    */   }
/*    */   
/*    */   public TreeTransformer collinizerEvalb() {
/* 53 */     return new TreeCollinizer(this.tlp);
/*    */   }
/*    */   
/*    */   public MemoryTreebank memoryTreebank() {
/* 57 */     return new MemoryTreebank(treeReaderFactory());
/*    */   }
/*    */   
/*    */ 
/* 61 */   public DiskTreebank diskTreebank() { return new DiskTreebank(treeReaderFactory()); }
/*    */   
/*    */   private static class TueBaDZTreeReaderFactory implements TreeReaderFactory, Serializable {
/*    */     private static final long serialVersionUID = 1614799885744961795L;
/*    */     
/*    */     public TreeReader newTreeReader(Reader in) {
/* 67 */       TueBaDZPennTreeNormalizer tn = new TueBaDZPennTreeNormalizer();
/* 68 */       return new PennTreeReader(in, new LabeledScoredTreeFactory(new StringLabelFactory()), tn);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public TreeReaderFactory treeReaderFactory()
/*    */   {
/* 76 */     return new TueBaDZTreeReaderFactory(null);
/*    */   }
/*    */   
/*    */   public int setOptionFlag(String[] args, int i)
/*    */   {
/* 81 */     return i;
/*    */   }
/*    */   
/*    */   public void display() {
/* 85 */     System.out.println("TueBaDZParserParams (no options).");
/*    */   }
/*    */   
/*    */   public HeadFinder headFinder()
/*    */   {
/* 90 */     return new TueBaDZHeadFinder();
/*    */   }
/*    */   
/*    */ 
/*    */   public Tree transformTree(Tree t, Tree root)
/*    */   {
/* 96 */     return t;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\TueBaDZParserParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */