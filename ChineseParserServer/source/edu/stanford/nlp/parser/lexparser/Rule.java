/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Rule
/*    */   implements Serializable
/*    */ {
/* 13 */   public int parent = -1;
/*    */   
/* 15 */   public float score = NaN.0F;
/*    */   
/*    */ 
/*    */   public float score()
/*    */   {
/* 20 */     return this.score;
/*    */   }
/*    */   
/*    */   public boolean isUnary() {
/* 24 */     return false;
/*    */   }
/*    */   
/*    */   static class ScoreComparator implements Comparator<Rule>
/*    */   {
/*    */     public int compare(Rule r1, Rule r2) {
/* 30 */       if (r1.score() < r2.score())
/* 31 */         return -1;
/* 32 */       if (r1.score() == r2.score()) {
/* 33 */         return 0;
/*    */       }
/* 35 */       return 1;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 43 */   private static Comparator<Rule> scoreComparator = new ScoreComparator();
/*    */   private static final long serialVersionUID = 2L;
/*    */   
/* 46 */   public static Comparator<Rule> scoreComparator() { return scoreComparator; }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Rule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */