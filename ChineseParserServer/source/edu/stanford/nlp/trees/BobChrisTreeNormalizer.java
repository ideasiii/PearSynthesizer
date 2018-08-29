/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.util.Filter;
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
/*     */ 
/*     */ public class BobChrisTreeNormalizer
/*     */   extends TreeNormalizer
/*     */ {
/*     */   protected final TreebankLanguagePack tlp;
/*     */   
/*     */   public BobChrisTreeNormalizer()
/*     */   {
/*  45 */     this(new PennTreebankLanguagePack());
/*     */   }
/*     */   
/*     */   public BobChrisTreeNormalizer(TreebankLanguagePack tlp) {
/*  49 */     this.tlp = tlp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String normalizeTerminal(String leaf)
/*     */   {
/*  59 */     return leaf.intern();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String normalizeNonterminal(String category)
/*     */   {
/*  69 */     return cleanUpLabel(category).intern();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree normalizeWholeTree(Tree tree, TreeFactory tf)
/*     */   {
/*  80 */     tree.prune(new Filter()
/*     */     {
/*     */       public boolean accept(Object obj) {
/*  83 */         Tree t = (Tree)obj;
/*  84 */         Tree[] kids = t.children();
/*  85 */         Label l = t.label();
/*  86 */         if ((l != null) && (l.value() != null) && (l.value().equals("-NONE-")) && (!t.isLeaf()) && (kids.length == 1) && (kids[0].isLeaf()))
/*     */         {
/*  88 */           return false;
/*     */         }
/*  90 */         return true; } }, tf).spliceOut(new Filter()
/*     */     {
/*     */ 
/*     */       public boolean accept(Tree t)
/*     */       {
/*     */ 
/*  96 */         if ((t.isLeaf()) || (t.isPreTerminal())) {
/*  97 */           return true;
/*     */         }
/*     */         
/* 100 */         if (("EDITED".equals(t.label().value())) || ("CODE".equals(t.label().value()))) {
/* 101 */           return false;
/*     */         }
/* 103 */         if (t.numChildren() != 1) {
/* 104 */           return true;
/*     */         }
/* 106 */         if ((t.label() != null) && (t.label().value() != null) && (t.label().value().equals(t.children()[0].label().value()))) {
/* 107 */           return false;
/*     */         }
/* 109 */         return true; } }, tf);
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
/*     */   protected String cleanUpLabel(String label)
/*     */   {
/* 123 */     if (label == null) {
/* 124 */       label = "ROOT";
/*     */     }
/*     */     else {
/* 127 */       label = this.tlp.basicCategory(label);
/*     */     }
/* 129 */     return label;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\BobChrisTreeNormalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */