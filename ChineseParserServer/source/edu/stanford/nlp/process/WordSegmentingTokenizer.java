/*    */ package edu.stanford.nlp.process;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Sentence;
/*    */ import edu.stanford.nlp.ling.Word;
/*    */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*    */ import edu.stanford.nlp.parser.lexparser.WordSegmenter;
/*    */ import java.io.Reader;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ public class WordSegmentingTokenizer
/*    */   extends AbstractTokenizer
/*    */ {
/*    */   private Iterator wordIter;
/*    */   private Tokenizer tok;
/*    */   private WordSegmenter wordSegmenter;
/*    */   
/*    */   protected Object getNext()
/*    */   {
/* 20 */     while ((this.wordIter == null) || (!this.wordIter.hasNext())) {
/* 21 */       if (!this.tok.hasNext()) {
/* 22 */         return null;
/*    */       }
/* 24 */       String s = ((Word)this.tok.next()).word();
/* 25 */       if (s == null) {
/* 26 */         return null;
/*    */       }
/* 28 */       Sentence se = segmentWords(s);
/* 29 */       this.wordIter = se.iterator();
/*    */     }
/* 31 */     return this.wordIter.next();
/*    */   }
/*    */   
/*    */   public WordSegmentingTokenizer(WordSegmenter wordSegmenter, Reader r) {
/* 35 */     this.wordSegmenter = wordSegmenter;
/* 36 */     this.tok = new WhitespaceTokenizer(r);
/*    */   }
/*    */   
/*    */   public Sentence segmentWords(String s) {
/* 40 */     return this.wordSegmenter.segmentWords(s);
/*    */   }
/*    */   
/*    */   public static TokenizerFactory factory(WordSegmenter wordSegmenter) {
/* 44 */     return new WordSegmentingTokenizerFactory(wordSegmenter);
/*    */   }
/*    */   
/*    */   private static class WordSegmentingTokenizerFactory implements TokenizerFactory {
/*    */     WordSegmenter wordSegmenter;
/*    */     
/*    */     public WordSegmentingTokenizerFactory(WordSegmenter wordSegmenter) {
/* 51 */       this.wordSegmenter = wordSegmenter;
/*    */     }
/*    */     
/*    */     public Iterator getIterator(Reader r) {
/* 55 */       return getTokenizer(r);
/*    */     }
/*    */     
/*    */     public Tokenizer getTokenizer(Reader r) {
/* 59 */       return new WordSegmentingTokenizer(this.wordSegmenter, r);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\WordSegmentingTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */