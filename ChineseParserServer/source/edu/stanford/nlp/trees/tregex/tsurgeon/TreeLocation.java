/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import edu.stanford.nlp.util.Pair;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class TreeLocation
/*    */ {
/*    */   private final String relation;
/*    */   private final TsurgeonPattern p;
/*    */   
/*    */   public TreeLocation(String relation, TsurgeonPattern p)
/*    */   {
/* 20 */     this.relation = relation;
/* 21 */     this.p = p;
/*    */   }
/*    */   
/*    */   void setRoot(TsurgeonPatternRoot root) {
/* 25 */     this.p.setRoot(root);
/*    */   }
/*    */   
/* 28 */   private static final Pattern daughterPattern = Pattern.compile(">-?([0-9]+)");
/*    */   
/*    */   Pair<Tree, Integer> evaluate(Tree t, TregexMatcher tm) {
/* 31 */     int newIndex = -1;
/* 32 */     Tree parent = null;
/* 33 */     Tree relativeNode = this.p.evaluate(t, tm);
/* 34 */     Matcher m = daughterPattern.matcher(this.relation);
/* 35 */     if (m.matches()) {
/* 36 */       newIndex = Integer.parseInt(m.group(1)) - 1;
/* 37 */       parent = relativeNode;
/* 38 */       if (this.relation.charAt(1) == '-')
/* 39 */         newIndex = parent.children().length - newIndex;
/*    */     } else {
/* 41 */       parent = relativeNode.parent(t);
/* 42 */       if (parent == null) {
/* 43 */         throw new RuntimeException("Error: looking for a non-existent parent in tree " + t + " for \"" + toString() + "\"");
/*    */       }
/* 45 */       int index = parent.indexOf(relativeNode);
/* 46 */       if (this.relation.equals("$+")) {
/* 47 */         newIndex = index;
/* 48 */       } else if (this.relation.equals("$-")) {
/* 49 */         newIndex = index + 1;
/*    */       } else {
/* 51 */         throw new RuntimeException("Error: Haven't dealt with relation " + this.relation + " yet.");
/*    */       }
/*    */     }
/* 54 */     return new Pair(parent, Integer.valueOf(newIndex));
/*    */   }
/*    */   
/*    */   public String toString() {
/* 58 */     return this.relation + " " + this.p;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\TreeLocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */