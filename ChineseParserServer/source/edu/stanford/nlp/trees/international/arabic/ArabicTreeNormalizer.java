/*     */ package edu.stanford.nlp.trees.international.arabic;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.trees.BobChrisTreeNormalizer;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.tregex.ParseException;
/*     */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*     */ import edu.stanford.nlp.trees.tregex.TregexPattern;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class ArabicTreeNormalizer
/*     */   extends BobChrisTreeNormalizer
/*     */ {
/*     */   private boolean retainNPTmp;
/*     */   private boolean markPRDverb;
/*  33 */   private boolean normalizeConj = false;
/*  34 */   private boolean changeNoLabels = false;
/*  35 */   private Pattern prdPattern = Pattern.compile("^[A-Z]+-PRD");
/*     */   private TregexPattern prdVerbPattern;
/*     */   private static final boolean escape = false;
/*     */   
/*  39 */   public ArabicTreeNormalizer(boolean retainNPTmp, boolean markPRDverb, boolean changeNoLabels) { super(new ArabicTreebankLanguagePack());
/*  40 */     this.retainNPTmp = retainNPTmp;
/*  41 */     this.markPRDverb = markPRDverb;
/*  42 */     this.changeNoLabels = changeNoLabels;
/*     */     try {
/*  44 */       this.prdVerbPattern = TregexPattern.compile("/^V[^P]/ > VP $ /-PRD$/=prd");
/*     */     } catch (ParseException e) {
/*  46 */       System.out.println(e);
/*  47 */       throw new RuntimeException();
/*     */     }
/*     */   }
/*     */   
/*     */   public ArabicTreeNormalizer(boolean retainNPTmp, boolean markPRDverb) {
/*  52 */     this(retainNPTmp, markPRDverb, false);
/*     */   }
/*     */   
/*     */   public ArabicTreeNormalizer(boolean retainNPTmp) {
/*  56 */     this(retainNPTmp, false);
/*     */   }
/*     */   
/*     */   public ArabicTreeNormalizer() {
/*  60 */     this(false);
/*     */   }
/*     */   
/*     */   public String normalizeNonterminal(String category)
/*     */   {
/*  65 */     if (this.changeNoLabels)
/*  66 */       return category;
/*  67 */     if ((this.retainNPTmp) && (category != null) && (category.startsWith("NP-TMP")))
/*  68 */       return "NP-TMP";
/*  69 */     if ((this.markPRDverb) && (category != null) && (this.prdPattern.matcher(category).matches())) {
/*  70 */       return category;
/*     */     }
/*  72 */     return super.normalizeNonterminal(category);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String normalizeTerminal(String leaf)
/*     */   {
/*  82 */     if (this.changeNoLabels) {
/*  83 */       return leaf;
/*     */     }
/*     */     
/*  86 */     return super.normalizeTerminal(leaf);
/*     */   }
/*     */   
/*     */ 
/*  90 */   private static final Collection escapeCharacters = Arrays.asList(new String[] { "/", "*" });
/*     */   
/*     */ 
/*     */   public Tree normalizeWholeTree(Tree tree, TreeFactory tf)
/*     */   {
/*  95 */     tree = super.normalizeWholeTree(tree, tf);
/*     */     
/*     */ 
/*  98 */     for (Tree t : tree) {
/*  99 */       if (t.isPreTerminal())
/*     */       {
/*     */ 
/*     */ 
/* 103 */         if (((t.value().equals("PREP")) || (t.value().equals("IN"))) && (t.firstChild().value().equals("Ely")))
/*     */         {
/* 105 */           System.err.println("ATBNormalizer FIX: correcting Ely to ElY: " + t);
/* 106 */           t.children()[0].label().setValue("ElY");
/* 107 */         } else if (((t.value().equals("PREP")) || (t.value().equals("IN"))) && (t.firstChild().value().equals("<ly")))
/*     */         {
/* 109 */           System.err.println("ATBNormalizer FIX: correcting <ly to <lY: " + t);
/* 110 */           t.children()[0].label().setValue("<lY");
/* 111 */         } else if (((t.value().equals("PREP")) || (t.value().equals("IN"))) && (t.firstChild().value().equals("Aly")))
/*     */         {
/* 113 */           System.err.println("ATBNormalizer FIX: correcting Aly to AlY: " + t);
/* 114 */           t.children()[0].label().setValue("AlY");
/* 115 */         } else if (((t.value().equals("PREP")) || (t.value().equals("IN"))) && (t.firstChild().value().equals("ldy")))
/*     */         {
/* 117 */           System.err.println("ATBNormalizer FIX: correcting ldy to ldY: " + t);
/* 118 */           t.children()[0].label().setValue("ldY");
/* 119 */         } else if ((t.label().value() == null) || (t.label().value().equals(""))) {
/* 120 */           System.err.println("ATBNormalizer ERROR: missing tag: " + t);
/*     */         }
/*     */       }
/* 123 */       if ((!t.isPreTerminal()) && (!t.isLeaf()))
/*     */       {
/*     */ 
/* 126 */         int nk = t.numChildren();
/* 127 */         List<Tree> newKids = new ArrayList(nk);
/* 128 */         for (int j = 0; j < nk; j++) {
/* 129 */           Tree child = t.getChild(j);
/* 130 */           if (child.isLeaf()) {
/* 131 */             newKids.add(tf.newTreeNode("DUMMYTAG", Collections.singletonList(child)));
/*     */           } else {
/* 133 */             newKids.add(child);
/*     */           }
/*     */         }
/* 136 */         t.setChildren(newKids);
/*     */       }
/*     */     }
/* 139 */     if (this.markPRDverb) {
/* 140 */       TregexMatcher m = this.prdVerbPattern.matcher(tree);
/* 141 */       Tree match = null;
/* 142 */       while (m.find()) {
/* 143 */         if (m.getMatch() != match)
/*     */         {
/*     */ 
/* 146 */           match = m.getMatch();
/* 147 */           match.label().setValue(match.label().value() + "-PRDverb");
/* 148 */           Tree prd = m.getNode("prd");
/* 149 */           prd.label().setValue(super.normalizeNonterminal(prd.label().value()));
/*     */         }
/*     */       }
/*     */     }
/* 153 */     if ((this.normalizeConj) && (tree.isPreTerminal()) && (tree.children()[0].label().value().equals("w")) && (wrongConjPattern.matcher(tree.label().value()).matches())) {
/* 154 */       System.err.print("ATBNormalizer ERROR: bad CC remapped tree " + tree + " to ");
/* 155 */       tree.label().setValue("CC");
/* 156 */       System.err.println(tree);
/*     */     }
/* 158 */     if (tree.isPreTerminal()) {
/* 159 */       String val = tree.label().value();
/* 160 */       if ((val.equals("CC")) || (val.equals("PUNC")) || (val.equals("CONJ"))) {
/* 161 */         System.err.println("ATBNormalizer ERROR: bare tagged word: " + tree + " being wrapped in FRAG");
/*     */         
/* 163 */         tree = tf.newTreeNode("FRAG", Collections.singletonList(tree));
/*     */       } else {
/* 165 */         System.err.println("ATBNormalizer ERROR: bare tagged word: " + tree + ": fix it!!");
/*     */       }
/*     */     }
/*     */     
/* 169 */     if (!tree.label().value().equals("ROOT")) {
/* 170 */       tree = tf.newTreeNode("ROOT", Collections.singletonList(tree));
/*     */     }
/* 172 */     return tree;
/*     */   }
/*     */   
/* 175 */   private static final Pattern wrongConjPattern = Pattern.compile("NNP|NO_FUNC|NOFUNC|IN");
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\arabic\ArabicTreeNormalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */