/*     */ package edu.stanford.nlp.trees.international.arabic;
/*     */ 
/*     */ import edu.stanford.nlp.trees.AbstractCollinsHeadFinder;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArabicHeadFinder
/*     */   extends AbstractCollinsHeadFinder
/*     */ {
/*     */   protected TagSet tagSet;
/*     */   
/*     */   public static abstract enum TagSet
/*     */   {
/*  31 */     BIES_COLLAPSED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  39 */     ORIGINAL;
/*     */     
/*     */     private TagSet() {}
/*     */     
/*     */     abstract String prep();
/*     */     
/*     */     abstract String noun();
/*     */     
/*     */     abstract String adj();
/*     */     
/*     */     abstract String det();
/*     */     
/*     */     abstract String detPlusNoun();
/*     */     
/*     */     abstract TreebankLanguagePack langPack();
/*     */     
/*     */     static TagSet tagSet(String str) {
/*  56 */       if (str.equals("BIES_COLLAPSED"))
/*  57 */         return BIES_COLLAPSED;
/*  58 */       if (str.equals("ORIGINAL"))
/*  59 */         return ORIGINAL;
/*  60 */       throw new IllegalArgumentException("Don't know anything about tagset " + str);
/*     */     }
/*     */   }
/*     */   
/*     */   public ArabicHeadFinder() {
/*  65 */     this(new ArabicTreebankLanguagePack());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArabicHeadFinder(String tagSet)
/*     */   {
/*  73 */     this(TagSet.tagSet(tagSet));
/*     */   }
/*     */   
/*     */   public ArabicHeadFinder(TagSet tagSet) {
/*  77 */     this(tagSet.langPack(), tagSet);
/*     */   }
/*     */   
/*     */   protected ArabicHeadFinder(TreebankLanguagePack tlp)
/*     */   {
/*  82 */     this(tlp, TagSet.BIES_COLLAPSED);
/*     */   }
/*     */   
/*     */   protected ArabicHeadFinder(TreebankLanguagePack tlp, TagSet tagSet) {
/*  86 */     super(tlp);
/*  87 */     this.tagSet = tagSet;
/*     */     
/*     */ 
/*  90 */     this.nonTerminalInfo = new HashMap();
/*  91 */     this.nonTerminalInfo.put("SUBROOT", new String[][] { { "right", "S" } });
/*  92 */     this.nonTerminalInfo.put("NX", new String[][] { { "left", "DT", "DTNN", "DTNNS", "DTNNP", "DTNNPS", "DTJJ" } });
/*  93 */     this.nonTerminalInfo.put("ADJP", new String[][] { { "right", tagSet.adj(), "ADJP", tagSet.noun(), "NNP", "NOFUNC", "NNPS", "NNS", "DTNN", "DTNNS", "DTNNP", "DTNNPS", "DTJJ" }, { "right", "RB", "CD", "DTRB", "DTCD" }, { "right", "DT" } });
/*  94 */     this.nonTerminalInfo.put("ADVP", new String[][] { { "left", "WRB", "RB", "ADVP", "WHADVP", "DTRB" }, { "left", "CD", "RP", tagSet.noun(), "CC", tagSet.adj(), "IN", "NP", "NNP", "NOFUNC", "DTRP", "DTNN", "DTNNP", "DTNNPS", "DTNNS", "DTJJ" } });
/*  95 */     this.nonTerminalInfo.put("CONJP", new String[][] { { "right", "IN", "RB", tagSet.noun(), "NNS", "NNP", "NNPS", "DTRB", "DTNN", "DTNNS", "DTNNP", "DTNNPS" } });
/*  96 */     this.nonTerminalInfo.put("FRAG", new String[][] { { "left", tagSet.noun(), "NNPS", "NNP", "NNS", "DTNN", "DTNNS", "DTNNP", "DTNNPS" }, { "left", "VBP" } });
/*  97 */     this.nonTerminalInfo.put("INTJ", new String[][] { { "left", "RP", "UH", "DTRP" } });
/*  98 */     this.nonTerminalInfo.put("NAC", new String[][] { { "left", "NP", "SBAR", "PP", "ADJP", "S", "PRT", "UCP" }, { "left", "ADVP" } });
/*  99 */     this.nonTerminalInfo.put("WHADVP", new String[][] { { "left", "WRB", "WP" }, { "right", "CC" }, { "left", "IN" } });
/*     */     
/* 101 */     this.nonTerminalInfo.put("UCP", new String[][] { { "left" } });
/* 102 */     this.nonTerminalInfo.put("X", new String[][] { { "left" } });
/* 103 */     this.nonTerminalInfo.put("LST", new String[][] { { "left" } });
/*     */     
/* 105 */     this.nonTerminalInfo.put("DTNN", new String[][] { { "right" } });
/* 106 */     this.nonTerminalInfo.put("DTNNS", new String[][] { { "right" } });
/* 107 */     this.nonTerminalInfo.put("DTNNP", new String[][] { { "right" } });
/* 108 */     this.nonTerminalInfo.put("DTNNPS", new String[][] { { "right" } });
/* 109 */     this.nonTerminalInfo.put("DTJJ", new String[][] { { "right" } });
/* 110 */     this.nonTerminalInfo.put("DTRP", new String[][] { { "right" } });
/* 111 */     this.nonTerminalInfo.put("DTRB", new String[][] { { "right" } });
/* 112 */     this.nonTerminalInfo.put("DTCD", new String[][] { { "right" } });
/* 113 */     this.nonTerminalInfo.put("DTIN", new String[][] { { "right" } });
/* 114 */     this.nonTerminalInfo.put("PP", new String[][] { { "left", tagSet.prep(), "PP", "PRT", "X" }, { "left", "NNP", "RP", tagSet.noun() }, { "left", "NP" } });
/* 115 */     this.nonTerminalInfo.put("PRN", new String[][] { { "left", "NP" } });
/* 116 */     this.nonTerminalInfo.put("PRT", new String[][] { { "left", "RP", "PRT", "IN", "DTRP" } });
/* 117 */     this.nonTerminalInfo.put("QP", new String[][] { { "right", "CD", tagSet.noun(), tagSet.adj(), "NNS", "NNP", "NNPS", "DTNN", "DTNNS", "DTNNP", "DTNNPS", "DTJJ" } });
/* 118 */     this.nonTerminalInfo.put("S", new String[][] { { "left", "VP", "S" }, { "right", "PP", "ADVP", "SBAR", "UCP", "ADJP" } });
/* 119 */     this.nonTerminalInfo.put("SQ", new String[][] { { "left", "VP", "PP" } });
/* 120 */     this.nonTerminalInfo.put("SBAR", new String[][] { { "left", "WHNP", "WHADVP", "RP", "IN", "SBAR", "CC", "WP", "WHPP", "ADVP", "PRT", "RB", "X", "DTRB", "DTRP" }, { "left", tagSet.noun(), "NNP", "NNS", "NNPS", "DTNN", "DTNNS", "DTNNP", "DTNNPS" }, { "left", "S" } });
/* 121 */     this.nonTerminalInfo.put("SBARQ", new String[][] { { "left", "WHNP", "WHADVP", "RP", "IN", "SBAR", "CC", "WP", "WHPP", "ADVP", "PRT", "RB", "X" }, { "left", tagSet.noun(), "NNP", "NNS", "NNPS", "DTNN", "DTNNS", "DTNNP", "DTNNPS" }, { "left", "S" } });
/*     */     
/* 123 */     this.nonTerminalInfo.put("WHNP", new String[][] { { "right", "WP" } });
/* 124 */     this.nonTerminalInfo.put("WHPP", new String[][] { { "right" } });
/* 125 */     this.nonTerminalInfo.put("VP", new String[][] { { "left", "VBD", "VBN", "VBP", "VP", "RB", "X", "VB" }, { "left", "IN" }, { "left", "NNP", "NOFUNC", tagSet.noun(), "DTNN", "DTNNP", "DTNNPS", "DTNNS" } });
/*     */     
/*     */ 
/* 128 */     this.nonTerminalInfo.put("NP", new String[][] { { "left", tagSet.noun(), tagSet.detPlusNoun(), "NNS", "NNP", "NNPS", "NP", "PRP", "WHNP", "QP", "WP", "DTNN", "DTNNS", "DTNNPS", "DTNNP", "NOFUNC" }, { "left", tagSet.adj(), "DTJJ" }, { "right", "CD", "DTCD" }, { "left", "PRP$" }, { "right", "DT" } });
/*     */     
/*     */ 
/* 131 */     this.nonTerminalInfo.put("EDITED", new String[][] { { "left" } });
/* 132 */     this.nonTerminalInfo.put("ROOT", new String[][] { { "left" } });
/*     */     
/*     */ 
/* 135 */     this.nonTerminalInfo.put("SINV", new String[][] { { "left", "ADJP", "VP" } });
/*     */   }
/*     */   
/*     */ 
/* 139 */   private Pattern predPattern = Pattern.compile(".*-PRD$");
/*     */   
/*     */ 
/*     */ 
/*     */   protected Tree findMarkedHead(Tree t)
/*     */   {
/* 145 */     String cat = t.value();
/* 146 */     if (cat.equals("S")) {
/* 147 */       Tree[] kids = t.children();
/* 148 */       for (Tree kid : kids) {
/* 149 */         if (this.predPattern.matcher(kid.value()).matches()) {
/* 150 */           return kid;
/*     */         }
/*     */       }
/*     */     }
/* 154 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\arabic\ArabicHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */