/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class EvalB
/*    */ {
/*    */   private static PrintWriter goldWriter;
/*    */   private static PrintWriter testWriter;
/*    */   
/*    */   public static void initEVALBfiles(TreebankLangParserParams tlpParams)
/*    */   {
/*    */     try
/*    */     {
/* 15 */       goldWriter = tlpParams.pw(new java.io.FileOutputStream("parses.gld"));
/* 16 */       testWriter = tlpParams.pw(new java.io.FileOutputStream("parses.tst"));
/*    */     } catch (java.io.IOException e) {
/* 18 */       System.exit(0);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void closeEVALBfiles() {
/* 23 */     goldWriter.close();
/* 24 */     testWriter.close();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static void writeEVALBline(Tree gold, Tree test)
/*    */   {
/* 35 */     goldWriter.println(gold.toString());
/* 36 */     testWriter.println(test.toString());
/* 37 */     System.err.println("Wrote EVALB lines.");
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\EvalB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */