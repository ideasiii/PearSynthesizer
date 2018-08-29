/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CollinsHeadFinder
/*     */   extends AbstractCollinsHeadFinder
/*     */ {
/*     */   private static final long serialVersionUID = -8747319554557223437L;
/*     */   
/*     */   public CollinsHeadFinder()
/*     */   {
/*  28 */     this(new PennTreebankLanguagePack());
/*     */   }
/*     */   
/*     */   protected int postOperationFix(int headIdx, Tree[] daughterTrees) {
/*  32 */     if (headIdx >= 2) {
/*  33 */       String prevLab = this.tlp.basicCategory(daughterTrees[(headIdx - 1)].value());
/*  34 */       if ((prevLab.equals("CC")) || (prevLab.equals("CONJP"))) {
/*  35 */         int newHeadIdx = headIdx - 2;
/*  36 */         Tree t = daughterTrees[newHeadIdx];
/*  37 */         while ((newHeadIdx >= 0) && (t.isPreTerminal()) && (this.tlp.isPunctuationTag(t.value())))
/*     */         {
/*  39 */           newHeadIdx--;
/*     */         }
/*  41 */         if (newHeadIdx >= 0) {
/*  42 */           headIdx = newHeadIdx;
/*     */         }
/*     */       }
/*     */     }
/*  46 */     return headIdx;
/*     */   }
/*     */   
/*     */   public CollinsHeadFinder(TreebankLanguagePack tlp) {
/*  50 */     super(tlp);
/*     */     
/*  52 */     this.nonTerminalInfo = new HashMap();
/*     */     
/*  54 */     this.nonTerminalInfo.put("ADJP", new String[][] { { "left", "NNS", "QP", "NN", "$", "ADVP", "JJ", "VBN", "VBG", "ADJP", "JJR", "NP", "JJS", "DT", "FW", "RBR", "RBS", "SBAR", "RB" } });
/*  55 */     this.nonTerminalInfo.put("ADVP", new String[][] { { "right", "RB", "RBR", "RBS", "FW", "ADVP", "TO", "CD", "JJR", "JJ", "IN", "NP", "JJS", "NN" } });
/*  56 */     this.nonTerminalInfo.put("CONJP", new String[][] { { "right", "CC", "RB", "IN" } });
/*  57 */     this.nonTerminalInfo.put("FRAG", new String[][] { { "right" } });
/*  58 */     this.nonTerminalInfo.put("INTJ", new String[][] { { "left" } });
/*  59 */     this.nonTerminalInfo.put("LST", new String[][] { { "right", "LS", ":" } });
/*  60 */     this.nonTerminalInfo.put("NAC", new String[][] { { "left", "NN", "NNS", "NNP", "NNPS", "NP", "NAC", "EX", "$", "CD", "QP", "PRP", "VBG", "JJ", "JJS", "JJR", "ADJP", "FW" } });
/*  61 */     this.nonTerminalInfo.put("NX", new String[][] { { "left" } });
/*  62 */     this.nonTerminalInfo.put("PP", new String[][] { { "right", "IN", "TO", "VBG", "VBN", "RP", "FW" } });
/*     */     
/*  64 */     this.nonTerminalInfo.put("PRN", new String[][] { { "left" } });
/*  65 */     this.nonTerminalInfo.put("PRT", new String[][] { { "right", "RP" } });
/*  66 */     this.nonTerminalInfo.put("QP", new String[][] { { "left", "$", "IN", "NNS", "NN", "JJ", "RB", "DT", "CD", "NCD", "QP", "JJR", "JJS" } });
/*  67 */     this.nonTerminalInfo.put("RRC", new String[][] { { "right", "VP", "NP", "ADVP", "ADJP", "PP" } });
/*  68 */     this.nonTerminalInfo.put("S", new String[][] { { "left", "TO", "IN", "VP", "S", "SBAR", "ADJP", "UCP", "NP" } });
/*  69 */     this.nonTerminalInfo.put("SBAR", new String[][] { { "left", "WHNP", "WHPP", "WHADVP", "WHADJP", "IN", "DT", "S", "SQ", "SINV", "SBAR", "FRAG" } });
/*  70 */     this.nonTerminalInfo.put("SBARQ", new String[][] { { "left", "SQ", "S", "SINV", "SBARQ", "FRAG" } });
/*  71 */     this.nonTerminalInfo.put("SINV", new String[][] { { "left", "VBZ", "VBD", "VBP", "VB", "MD", "VP", "S", "SINV", "ADJP", "NP" } });
/*  72 */     this.nonTerminalInfo.put("SQ", new String[][] { { "left", "VBZ", "VBD", "VBP", "VB", "MD", "VP", "SQ" } });
/*  73 */     this.nonTerminalInfo.put("UCP", new String[][] { { "right" } });
/*  74 */     this.nonTerminalInfo.put("VP", new String[][] { { "left", "TO", "VBD", "VBN", "MD", "VBZ", "VB", "VBG", "VBP", "AUX", "AUXG", "VP", "ADJP", "NN", "NNS", "NP" } });
/*  75 */     this.nonTerminalInfo.put("WHADJP", new String[][] { { "left", "CC", "WRB", "JJ", "ADJP" } });
/*  76 */     this.nonTerminalInfo.put("WHADVP", new String[][] { { "right", "CC", "WRB" } });
/*  77 */     this.nonTerminalInfo.put("WHNP", new String[][] { { "left", "WDT", "WP", "WP$", "WHADJP", "WHPP", "WHNP" } });
/*  78 */     this.nonTerminalInfo.put("WHPP", new String[][] { { "right", "IN", "TO", "FW" } });
/*  79 */     this.nonTerminalInfo.put("X", new String[][] { { "right" } });
/*  80 */     this.nonTerminalInfo.put("NP", new String[][] { { "rightdis", "NN", "NNP", "NNPS", "NNS", "NX", "POS", "JJR" }, { "left", "NP" }, { "rightdis", "$", "ADJP", "PRN" }, { "right", "CD" }, { "rightdis", "JJ", "JJS", "RB", "QP" } });
/*  81 */     this.nonTerminalInfo.put("TYPO", new String[][] { { "left" } });
/*  82 */     this.nonTerminalInfo.put("EDITED", new String[][] { { "left" } });
/*  83 */     this.nonTerminalInfo.put("XS", new String[][] { { "right", "IN" } });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  97 */     Treebank treebank = new DiskTreebank();
/*  98 */     edu.stanford.nlp.ling.CategoryWordTag.suppressTerminalDetails = true;
/*  99 */     treebank.loadPath(args[0]);
/* 100 */     HeadFinder chf = new CollinsHeadFinder();
/* 101 */     treebank.apply(new TreeVisitor() {
/*     */       public void visitTree(Tree pt) {
/* 103 */         pt.percolateHeads(this.val$chf);
/* 104 */         pt.pennPrint();
/* 105 */         System.out.println();
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\CollinsHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */