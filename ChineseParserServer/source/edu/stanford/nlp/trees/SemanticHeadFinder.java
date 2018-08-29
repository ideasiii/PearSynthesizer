/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasTag;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
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
/*     */ public class SemanticHeadFinder
/*     */   extends ModCollinsHeadFinder
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private HashSet<String> verbalAuxiliaries;
/*     */   private HashSet<String> copulars;
/*     */   private HashSet<String> verbalTags;
/*     */   private static final long serialVersionUID = 5721799188009249808L;
/*     */   
/*     */   public SemanticHeadFinder()
/*     */   {
/*  53 */     this(new PennTreebankLanguagePack(), true);
/*     */   }
/*     */   
/*     */   public SemanticHeadFinder(boolean cop) {
/*  57 */     this(new PennTreebankLanguagePack(), cop);
/*     */   }
/*     */   
/*     */   public SemanticHeadFinder(TreebankLanguagePack tlp, boolean cop) {
/*  61 */     super(tlp);
/*  62 */     ruleChanges();
/*     */     
/*     */ 
/*  65 */     this.verbalAuxiliaries = new HashSet();
/*  66 */     this.verbalAuxiliaries.addAll(Arrays.asList(new String[] { "will", "wo", "shall", "may", "might", "should", "would", "can", "could", "ca", "must", "has", "have", "had", "having", "be", "being", "been", "get", "gets", "getting", "got", "gotten", "do", "does", "did", "to", "'ve", "'d", "'ll" }));
/*     */     
/*     */ 
/*  69 */     this.copulars = new HashSet();
/*  70 */     if (cop) {
/*  71 */       this.copulars.addAll(Arrays.asList(new String[] { "be", "being", "Being", "am", "are", "is", "was", "were", "'m", "'re", "'s", "s", "seem", "seems", "seemed", "appear", "appears", "appeared", "stay", "stays", "stayed", "remain", "remains", "remained", "resemble", "resembles", "resembled", "become", "becomes", "became" }));
/*     */     }
/*     */     
/*     */ 
/*  75 */     this.verbalTags = new HashSet();
/*     */     
/*  77 */     this.verbalTags.addAll(Arrays.asList(new String[] { "TO", "MD", "VB", "VBD", "VBP", "VBZ", "VBG", "VBN", "AUX", "AUXG" }));
/*     */   }
/*     */   
/*     */ 
/*     */   private void ruleChanges()
/*     */   {
/*  83 */     this.nonTerminalInfo.remove("NP");
/*  84 */     this.nonTerminalInfo.put("NP", new String[][] { { "rightdis", "NN", "NNP", "NNPS", "NNS", "NX", "JJR" }, { "left", "NP", "PRP" }, { "rightdis", "$", "ADJP", "PRN" }, { "right", "CD" }, { "rightdis", "JJ", "JJS", "RB", "QP", "DT", "WDT", "RBR", "ADVP" }, { "left", "POS" } });
/*     */     
/*  86 */     this.nonTerminalInfo.remove("WHNP");
/*  87 */     this.nonTerminalInfo.put("WHNP", new String[][] { { "rightdis", "NN", "NNP", "NNPS", "NNS", "NX", "POS", "JJR" }, { "left", "NP" }, { "rightdis", "$", "ADJP", "PRN" }, { "right", "CD" }, { "rightdis", "JJ", "JJS", "RB", "QP" }, { "left", "WHNP", "WHPP", "WHADJP", "WP$", "WP", "WDT" } });
/*     */     
/*  89 */     this.nonTerminalInfo.remove("WHADJP");
/*  90 */     this.nonTerminalInfo.put("WHADJP", new String[][] { { "left", "ADJP", "JJ", "WRB", "CC" } });
/*     */     
/*  92 */     this.nonTerminalInfo.remove("ADJP");
/*  93 */     this.nonTerminalInfo.put("ADJP", new String[][] { { "left", "$", "JJ", "NNS", "NN", "QP", "VBN", "VBG", "ADJP", "JJR", "NP", "JJS", "DT", "FW", "RBR", "RBS", "SBAR", "RB" } });
/*     */     
/*  95 */     this.nonTerminalInfo.remove("QP");
/*  96 */     this.nonTerminalInfo.put("QP", new String[][] { { "right", "$", "NNS", "NN", "CD", "JJ", "PDT", "DT", "IN", "RB", "NCD", "QP", "JJR", "JJS" } });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 101 */     this.nonTerminalInfo.remove("S");
/* 102 */     this.nonTerminalInfo.put("S", new String[][] { { "left", "VP", "S", "FRAG", "SBAR", "ADJP", "UCP", "TO" }, { "right", "NP" } });
/*     */     
/* 104 */     this.nonTerminalInfo.remove("SBAR");
/* 105 */     this.nonTerminalInfo.put("SBAR", new String[][] { { "left", "S", "SQ", "SINV", "SBAR", "FRAG", "WHNP", "WHPP", "WHADVP", "WHADJP", "IN", "DT" } });
/*     */     
/* 107 */     this.nonTerminalInfo.remove("SQ");
/* 108 */     this.nonTerminalInfo.put("SQ", new String[][] { { "left", "VP", "SQ", "VB", "VBZ", "VBD", "VBP", "MD" } });
/*     */     
/*     */ 
/* 111 */     this.nonTerminalInfo.remove("UCP");
/* 112 */     this.nonTerminalInfo.put("UCP", new String[][] { { "left" } });
/*     */     
/*     */ 
/* 115 */     this.nonTerminalInfo.remove("CONJP");
/* 116 */     this.nonTerminalInfo.put("CONJP", new String[][] { { "right", "TO", "RB", "IN", "CC" } });
/*     */     
/*     */ 
/* 119 */     this.nonTerminalInfo.remove("FRAG");
/* 120 */     this.nonTerminalInfo.put("FRAG", new String[][] { { "left" } });
/*     */     
/*     */ 
/* 123 */     this.nonTerminalInfo.remove("PP");
/* 124 */     this.nonTerminalInfo.put("PP", new String[][] { { "right", "IN", "TO", "VBG", "VBN", "RP", "FW" }, { "left", "PP" } });
/*     */     
/*     */ 
/* 127 */     this.nonTerminalInfo.put("XS", new String[][] { { "right", "IN" } });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int postOperationFix(int headIdx, Tree[] daughterTrees)
/*     */   {
/* 136 */     if (headIdx >= 2) {
/* 137 */       String prevLab = this.tlp.basicCategory(daughterTrees[(headIdx - 1)].value());
/* 138 */       if ((prevLab.equals("CC")) || (prevLab.equals("CONJP"))) {
/* 139 */         int newHeadIdx = headIdx - 2;
/* 140 */         Tree t = daughterTrees[newHeadIdx];
/* 141 */         while ((newHeadIdx >= 0) && (t.isPreTerminal()) && (this.tlp.isPunctuationTag(t.value()))) {
/* 142 */           newHeadIdx--;
/*     */         }
/* 144 */         while ((newHeadIdx >= 2) && (this.tlp.isPunctuationTag(daughterTrees[(newHeadIdx - 1)].value()))) {
/* 145 */           newHeadIdx -= 2;
/*     */         }
/* 147 */         if (newHeadIdx >= 0) {
/* 148 */           headIdx = newHeadIdx;
/*     */         }
/*     */       }
/*     */     }
/* 152 */     return headIdx;
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
/*     */   protected Tree determineNonTrivialHead(Tree t, Tree parent)
/*     */   {
/* 165 */     String motherCat = this.tlp.basicCategory(t.label().value());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */     if ((motherCat.equals("VP")) || (motherCat.equals("SQ")) || (motherCat.equals("SINV")))
/*     */     {
/* 174 */       Tree[] kids = t.children();
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
/* 185 */       if (hasVerbalAuxiliary(kids, this.verbalAuxiliaries))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 190 */         String[] how = { "left", "VP", "ADJP" };
/* 191 */         Tree pti = traverseLocate(kids, how, false);
/*     */         
/*     */ 
/*     */ 
/* 195 */         if (pti != null) {
/* 196 */           return pti;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */       if ((hasVerbalAuxiliary(kids, this.copulars)) && (!isExistential(t, parent)) && (!isWHQ(t, parent))) { String[] how;
/*     */         String[] how;
/* 208 */         if (motherCat.equals("SQ")) {
/* 209 */           how = new String[] { "right", "VP", "ADJP", "NP", "WHADJP", "WHNP" };
/*     */         } else {
/* 211 */           how = new String[] { "left", "VP", "ADJP", "NP", "WHADJP", "WHNP" };
/*     */         }
/* 213 */         Tree pti = traverseLocate(kids, how, false);
/*     */         
/*     */ 
/*     */ 
/* 217 */         if (pti != null) {
/* 218 */           return pti;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 230 */     return super.determineNonTrivialHead(t, parent);
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
/*     */   private boolean isExistential(Tree t, Tree parent)
/*     */   {
/* 243 */     boolean toReturn = false;
/* 244 */     String motherCat = this.tlp.basicCategory(t.label().value());
/*     */     
/* 246 */     if ((motherCat.equals("VP")) && (parent != null))
/*     */     {
/* 248 */       Tree[] kids = parent.children();
/*     */       
/* 250 */       for (Tree kid : kids) {
/* 251 */         if (kid.value().equals("VP")) break;
/* 252 */         List<Label> tags = kid.preTerminalYield();
/* 253 */         for (Label tag : tags) {
/* 254 */           if (tag.value().equals("EX")) {
/* 255 */             toReturn = true;
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/* 263 */     else if ((motherCat.startsWith("SQ")) && (parent != null))
/*     */     {
/* 265 */       Tree[] kids = parent.children();
/*     */       
/* 267 */       for (Tree kid : kids) {
/* 268 */         if (!kid.value().startsWith("VB")) {
/* 269 */           List<Label> tags = kid.preTerminalYield();
/* 270 */           for (Label tag : tags) {
/* 271 */             if (tag.value().equals("EX")) {
/* 272 */               toReturn = true;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 283 */     return toReturn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isWHQ(Tree t, Tree parent)
/*     */   {
/* 294 */     if (t == null) return false;
/* 295 */     boolean toReturn = false;
/* 296 */     if ((t.value().startsWith("SQ")) && 
/* 297 */       (parent != null) && (parent.value().equals("SBARQ"))) {
/* 298 */       Tree[] kids = parent.children();
/* 299 */       for (Tree kid : kids)
/*     */       {
/* 301 */         if (kid.value().startsWith("WH")) {
/* 302 */           toReturn = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 312 */     return toReturn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isVerbalAuxiliary(Tree t)
/*     */   {
/* 322 */     Tree[] trees = { t };
/* 323 */     return hasVerbalAuxiliary(trees, this.verbalAuxiliaries);
/*     */   }
/*     */   
/*     */   private boolean hasVerbalAuxiliary(Tree[] kids, HashSet<String> verbalSet)
/*     */   {
/* 328 */     for (int i = 0; i < kids.length; i++)
/*     */     {
/* 330 */       Label kidLabel = kids[i].label();
/*     */       
/* 332 */       String cat = this.tlp.basicCategory(kidLabel.value());
/* 333 */       String word = null;
/* 334 */       if ((kidLabel instanceof HasWord)) {
/* 335 */         word = ((HasWord)kidLabel).word();
/*     */       }
/* 337 */       if (word == null) {
/* 338 */         Label htl = kids[i].headTerminal(this).label();
/* 339 */         if ((htl instanceof HasWord)) {
/* 340 */           word = ((HasWord)htl).word();
/*     */         }
/* 342 */         if (word == null) {
/* 343 */           word = htl.value();
/*     */         }
/*     */       }
/*     */       
/* 347 */       String tag = null;
/* 348 */       if ((kidLabel instanceof HasTag)) {
/* 349 */         tag = ((HasTag)kidLabel).tag();
/*     */       }
/* 351 */       if (tag == null) {
/* 352 */         tag = kids[i].headPreTerminal(this).value();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 357 */       String lcWord = word.toLowerCase();
/*     */       
/* 359 */       if ((!"PP".equals(cat)) && (this.verbalTags.contains(tag)) && (verbalSet.contains(lcWord))) {
/* 360 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 364 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\SemanticHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */