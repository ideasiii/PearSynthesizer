/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.trees.tregex.TregexPattern;
/*    */ import edu.stanford.nlp.trees.tregex.tsurgeon.Tsurgeon;
/*    */ import edu.stanford.nlp.trees.tregex.tsurgeon.TsurgeonPattern;
/*    */ import edu.stanford.nlp.util.Pair;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DependencyTreeTransformer
/*    */   implements TreeTransformer
/*    */ {
/* 23 */   private Pattern NPTmpPattern = Pattern.compile("NP.*-TMP.*");
/* 24 */   private static final Pattern NPAdvPattern = Pattern.compile("NP.*-ADV.*");
/*    */   protected final TreebankLanguagePack tlp;
/*    */   
/*    */   public DependencyTreeTransformer() {
/* 28 */     this.tlp = new PennTreebankLanguagePack();
/*    */   }
/*    */   
/*    */   public Tree transformTree(Tree t)
/*    */   {
/* 33 */     t.setValue(cleanUpRoot(t.value()));
/*    */     
/*    */ 
/* 36 */     stripTag(t, t);
/*    */     
/*    */ 
/* 39 */     return stripEmptyNode(t);
/*    */   }
/*    */   
/*    */   protected String cleanUpRoot(String label) {
/* 43 */     if ((label == null) || (label.equals("TOP"))) {
/* 44 */       return "ROOT";
/*    */     }
/*    */     
/* 47 */     return label;
/*    */   }
/*    */   
/*    */ 
/*    */   protected String cleanUpLabel(String label)
/*    */   {
/* 53 */     boolean nptemp = this.NPTmpPattern.matcher(label).matches();
/* 54 */     boolean npadv = NPAdvPattern.matcher(label).matches();
/* 55 */     label = this.tlp.basicCategory(label);
/* 56 */     if (nptemp) {
/* 57 */       label = label + "-TMP";
/* 58 */     } else if (npadv) {
/* 59 */       label = label + "-ADV";
/*    */     }
/* 61 */     return label;
/*    */   }
/*    */   
/*    */   protected void stripTag(Tree t, Tree root) {
/* 65 */     if (!t.isLeaf()) {
/* 66 */       String label = cleanUpLabel(t.value());
/* 67 */       t.setValue(label);
/* 68 */       for (Tree child : t.getChildrenAsList()) {
/* 69 */         stripTag(child, root);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected Tree stripEmptyNode(Tree t) {
/* 75 */     List<Pair<TregexPattern, TsurgeonPattern>> ops = new ArrayList();
/*    */     
/* 77 */     String patternMatch = "-NONE-=none";
/* 78 */     String operation = "prune none";
/* 79 */     TregexPattern matchPattern = null;
/*    */     try {
/* 81 */       matchPattern = TregexPattern.compile(patternMatch);
/*    */     } catch (Exception e) {
/* 83 */       System.err.println("Error compiling match pattern");
/*    */     }
/* 85 */     TsurgeonPattern p = Tsurgeon.parseOperation(operation);
/* 86 */     ops.add(new Pair(matchPattern, p));
/*    */     
/* 88 */     return Tsurgeon.processPatternsOnTree(ops, t);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\DependencyTreeTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */