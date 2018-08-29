/*    */ package edu.stanford.nlp.trees.tregex;
/*    */ 
/*    */ import edu.stanford.nlp.stats.IntCounter;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ class VariableStrings
/*    */ {
/*    */   private Map<Object, String> varsToStrings;
/*    */   private IntCounter numVarsSet;
/*    */   
/*    */   public VariableStrings()
/*    */   {
/* 16 */     this.varsToStrings = new HashMap();
/* 17 */     this.numVarsSet = new IntCounter();
/*    */   }
/*    */   
/*    */   public boolean isSet(Object o) {
/* 21 */     return this.numVarsSet.getCount(o) == 1.0D;
/*    */   }
/*    */   
/*    */   public void setVar(Object var, String string) {
/* 25 */     String oldString = (String)this.varsToStrings.put(var, string);
/* 26 */     if ((oldString != null) && (!oldString.equals(string)))
/* 27 */       throw new RuntimeException("Error -- can't setVar to a different string -- old: " + oldString + " new: " + string);
/* 28 */     this.numVarsSet.incrementCount(var);
/*    */   }
/*    */   
/*    */   public void unsetVar(Object var) {
/* 32 */     if (this.numVarsSet.getCount(var) > 0.0D)
/* 33 */       this.numVarsSet.decrementCount(var);
/* 34 */     if (this.numVarsSet.getCount(var) == 0.0D)
/* 35 */       this.varsToStrings.put(var, null);
/*    */   }
/*    */   
/*    */   public String getString(Object var) {
/* 39 */     return (String)this.varsToStrings.get(var);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\VariableStrings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */