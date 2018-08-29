/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*    */ import edu.stanford.nlp.process.AbstractTokenizer;
/*    */ import edu.stanford.nlp.process.Tokenizer;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.io.Reader;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TreeTokenizerFactory
/*    */   implements TokenizerFactory<Tree>
/*    */ {
/*    */   private TreeReaderFactory trf;
/*    */   
/*    */   public TreeTokenizerFactory(TreeReaderFactory trf)
/*    */   {
/* 20 */     this.trf = trf;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Tokenizer<Tree> getTokenizer(final Reader r)
/*    */   {
/* 27 */     new AbstractTokenizer() {
/* 28 */       TreeReader tr = TreeTokenizerFactory.this.trf.newTreeReader(r);
/*    */       
/*    */       public Tree getNext() {
/* 31 */         try { return this.tr.readTree();
/*    */         }
/*    */         catch (IOException e) {
/* 34 */           System.err.println("Error in reading tree."); }
/* 35 */         return null;
/*    */       }
/*    */     };
/*    */   }
/*    */   
/*    */ 
/*    */   public Iterator<Tree> getIterator(Reader r)
/*    */   {
/* 43 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreeTokenizerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */