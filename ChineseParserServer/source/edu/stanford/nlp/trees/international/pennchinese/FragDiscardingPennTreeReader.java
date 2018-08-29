/*    */ package edu.stanford.nlp.trees.international.pennchinese;
/*    */ 
/*    */ import edu.stanford.nlp.process.Tokenizer;
/*    */ import edu.stanford.nlp.trees.PennTreeReader;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.TreeFactory;
/*    */ import edu.stanford.nlp.trees.TreeNormalizer;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class FragDiscardingPennTreeReader extends PennTreeReader
/*    */ {
/*    */   private static PrintWriter pw;
/*    */   
/*    */   public FragDiscardingPennTreeReader(java.io.Reader in, TreeFactory tf, TreeNormalizer tn, Tokenizer tk)
/*    */   {
/* 16 */     super(in, tf, tn, tk);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Tree readTree()
/*    */     throws java.io.IOException
/*    */   {
/* 32 */     Tree tr = super.readTree();
/* 33 */     while ((tr != null) && (tr.firstChild().value().equals("FRAG"))) {
/* 34 */       if (pw != null) {
/* 35 */         pw.println("Discarding Tree:");
/* 36 */         tr.pennPrint(pw);
/*    */       }
/* 38 */       tr = super.readTree();
/*    */     }
/* 40 */     return tr;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\FragDiscardingPennTreeReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */