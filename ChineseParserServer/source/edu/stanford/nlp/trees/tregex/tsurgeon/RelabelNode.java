/*    */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ class RelabelNode
/*    */   extends TsurgeonPattern
/*    */ {
/* 14 */   private static final Pattern regexPattern = Pattern.compile("^/(.*)/$");
/* 15 */   private static final Pattern quotexPattern = Pattern.compile("^\\|(.*)\\|$");
/*    */   
/* 17 */   private boolean fixedNewLabel = false;
/*    */   private String newLabel;
/*    */   private Pattern labelRegex;
/*    */   private int groupNumber;
/*    */   
/*    */   public RelabelNode(TsurgeonPattern child, String newLabel)
/*    */   {
/* 24 */     super("relabel", new TsurgeonPattern[] { child });
/* 25 */     Matcher m = quotexPattern.matcher(newLabel);
/* 26 */     if (m.matches()) {
/* 27 */       this.newLabel = m.group(1);
/*    */     } else {
/* 29 */       this.newLabel = newLabel;
/*    */     }
/* 31 */     this.fixedNewLabel = true;
/*    */   }
/*    */   
/*    */   public RelabelNode(TsurgeonPattern child, String labelRegex, int groupNumber) {
/* 35 */     super("relabel", new TsurgeonPattern[] { child });
/* 36 */     this.fixedNewLabel = false;
/* 37 */     Matcher m = regexPattern.matcher(labelRegex);
/* 38 */     if (m.matches()) {
/* 39 */       this.labelRegex = Pattern.compile(m.group(1));
/* 40 */       this.groupNumber = groupNumber;
/*    */     } else {
/* 42 */       throw new RuntimeException("Illegal label regex: " + labelRegex);
/*    */     }
/*    */   }
/*    */   
/*    */   public Tree evaluate(Tree t, TregexMatcher tm) {
/* 47 */     Tree nodeToRelabel = this.children[0].evaluate(t, tm);
/* 48 */     if (this.fixedNewLabel) {
/* 49 */       nodeToRelabel.label().setValue(this.newLabel);
/*    */     }
/*    */     else {
/* 52 */       Matcher m = this.labelRegex.matcher(nodeToRelabel.label().value());
/* 53 */       if (m.find())
/* 54 */         nodeToRelabel.label().setValue(m.group(this.groupNumber));
/*    */     }
/* 56 */     return t;
/*    */   }
/*    */   
/*    */   public String toString() { String result;
/*    */     String result;
/* 61 */     if (this.fixedNewLabel) {
/* 62 */       result = this.label + "(" + this.children[0].toString() + "," + this.newLabel + ")";
/*    */     } else
/* 64 */       result = this.label + "(" + this.children[0].toString() + "," + this.labelRegex.toString() + "," + this.groupNumber + ")";
/* 65 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\RelabelNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */