/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CoindexationGenerator
/*    */ {
/* 13 */   private static final Pattern coindexationPattern = Pattern.compile("-([0-9]+)$");
/*    */   private int lastIndex;
/*    */   
/*    */   public void setLastIndex(Tree t)
/*    */   {
/* 18 */     this.lastIndex = 0;
/* 19 */     for (Tree node : t) {
/* 20 */       String value = node.label().value();
/* 21 */       if (value != null) {
/* 22 */         Matcher m = coindexationPattern.matcher(value);
/* 23 */         if (m.find()) {
/* 24 */           int thisIndex = Integer.parseInt(m.group(1));
/* 25 */           this.lastIndex = Math.max(thisIndex, this.lastIndex);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public int generateIndex() {
/* 32 */     this.lastIndex += 1;
/* 33 */     return this.lastIndex;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\CoindexationGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */