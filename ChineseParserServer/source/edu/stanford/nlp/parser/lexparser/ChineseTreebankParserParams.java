/*      */ package edu.stanford.nlp.parser.lexparser;
/*      */ 
/*      */ import edu.stanford.nlp.io.EncodingPrintWriter.err;
/*      */ import edu.stanford.nlp.ling.CategoryWordTag;
/*      */ import edu.stanford.nlp.ling.Label;
/*      */ import edu.stanford.nlp.ling.StringLabelFactory;
/*      */ import edu.stanford.nlp.stats.Counter;
/*      */ import edu.stanford.nlp.trees.DiskTreebank;
/*      */ import edu.stanford.nlp.trees.HeadFinder;
/*      */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*      */ import edu.stanford.nlp.trees.MemoryTreebank;
/*      */ import edu.stanford.nlp.trees.Tree;
/*      */ import edu.stanford.nlp.trees.TreeFactory;
/*      */ import edu.stanford.nlp.trees.TreeReaderFactory;
/*      */ import edu.stanford.nlp.trees.TreeTransformer;
/*      */ import edu.stanford.nlp.trees.Treebank;
/*      */ import edu.stanford.nlp.trees.international.pennchinese.CHTBTokenizer;
/*      */ import edu.stanford.nlp.trees.international.pennchinese.CharacterLevelTagExtender;
/*      */ import edu.stanford.nlp.trees.international.pennchinese.ChineseCollinizer;
/*      */ import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;
/*      */ import edu.stanford.nlp.trees.international.pennchinese.FragDiscardingPennTreeReader;
/*      */ import edu.stanford.nlp.trees.international.pennchinese.SunJurafskyChineseHeadFinder;
/*      */ import edu.stanford.nlp.util.Filter;
/*      */ import edu.stanford.nlp.util.StringUtils;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ public class ChineseTreebankParserParams extends AbstractTreebankParserParams
/*      */ {
/*      */   private ChineseTreebankLanguagePack ctlp;
/*   36 */   public boolean charTags = false;
/*   37 */   public boolean useCharacterBasedLexicon = false;
/*   38 */   public boolean useMaxentLexicon = false;
/*   39 */   public boolean useMaxentDepGrammar = false;
/*   40 */   public boolean segmentMarkov = false;
/*   41 */   public boolean segmentMaxMatch = false;
/*   42 */   public boolean sunJurafskyHeadFinder = false;
/*   43 */   public boolean bikelHeadFinder = false;
/*   44 */   public boolean discardFrags = false;
/*   45 */   public boolean useSimilarWordMap = false;
/*      */   private Lexicon lex;
/*      */   private WordSegmenter segmenter;
/*      */   
/*      */   private static void printlnErr(String s)
/*      */   {
/*   51 */     EncodingPrintWriter.err.println(s, "GB18030");
/*      */   }
/*      */   
/*      */   public ChineseTreebankParserParams() {
/*   55 */     super(new ChineseTreebankLanguagePack());
/*   56 */     this.ctlp = ((ChineseTreebankLanguagePack)super.treebankLanguagePack());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public HeadFinder headFinder()
/*      */   {
/*   63 */     if (this.sunJurafskyHeadFinder)
/*   64 */       return new SunJurafskyChineseHeadFinder();
/*   65 */     if (this.bikelHeadFinder) {
/*   66 */       return new edu.stanford.nlp.trees.international.pennchinese.BikelChineseHeadFinder();
/*      */     }
/*   68 */     return new edu.stanford.nlp.trees.international.pennchinese.ChineseHeadFinder();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Lexicon lex(Options.LexOptions op)
/*      */   {
/*   76 */     if (this.useCharacterBasedLexicon) {
/*   77 */       return this.lex = new ChineseCharacterBasedLexicon();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*   82 */     ChineseLexicon clex = new ChineseLexicon(op);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   89 */     WordSegmenter seg = this.segmenter;
/*      */     
/*   91 */     if (seg != null) {
/*   92 */       this.lex = new ChineseLexiconAndWordSegmenter(clex, seg);
/*      */     } else {
/*   94 */       this.lex = clex;
/*      */     }
/*      */     
/*   97 */     return this.lex;
/*      */   }
/*      */   
/*      */   public double[] MLEDependencyGrammarSmoothingParams() {
/*  101 */     return new double[] { 5.8D, 17.7D, 6.5D, 0.4D };
/*      */   }
/*      */   
/*      */   public TreeReaderFactory treeReaderFactory() {
/*  105 */     new TreeReaderFactory() {
/*      */       public edu.stanford.nlp.trees.TreeReader newTreeReader(Reader in) {
/*  107 */         edu.stanford.nlp.trees.TreeNormalizer tn = new ChineseTreebankParserParams.CTBErrorCorrectingTreeNormalizer(ChineseTreebankParserParams.splitNPTMP, ChineseTreebankParserParams.splitPPTMP, ChineseTreebankParserParams.splitXPTMP, ChineseTreebankParserParams.this.charTags);
/*  108 */         if (ChineseTreebankParserParams.this.discardFrags) {
/*  109 */           return new FragDiscardingPennTreeReader(in, new LabeledScoredTreeFactory(new StringLabelFactory()), tn, new CHTBTokenizer(in));
/*      */         }
/*  111 */         return new edu.stanford.nlp.trees.PennTreeReader(in, new LabeledScoredTreeFactory(new StringLabelFactory()), tn, new CHTBTokenizer(in));
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class CTBErrorCorrectingTreeNormalizer
/*      */     extends edu.stanford.nlp.trees.BobChrisTreeNormalizer
/*      */   {
/*  128 */     private static final Pattern NPTmpPattern = Pattern.compile("NP.*-TMP.*");
/*  129 */     private static final Pattern PPTmpPattern = Pattern.compile("PP.*-TMP.*");
/*  130 */     private static final Pattern TmpPattern = Pattern.compile(".*-TMP.*");
/*      */     
/*      */ 
/*      */     private CharacterLevelTagExtender tagExtender;
/*      */     
/*      */ 
/*      */     private boolean splitNPTMP;
/*      */     
/*      */ 
/*      */     private boolean splitPPTMP;
/*      */     
/*      */ 
/*      */     private boolean splitXPTMP;
/*      */     
/*      */ 
/*      */     public CTBErrorCorrectingTreeNormalizer(boolean splitNPTMP, boolean splitPPTMP, boolean splitXPTMP, boolean charTags)
/*      */     {
/*  147 */       this.splitNPTMP = splitNPTMP;
/*  148 */       this.splitPPTMP = splitPPTMP;
/*  149 */       this.splitXPTMP = splitXPTMP;
/*  150 */       if (charTags) {
/*  151 */         this.tagExtender = new CharacterLevelTagExtender();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     protected String cleanUpLabel(String label)
/*      */     {
/*  164 */       if (label == null) {
/*  165 */         return "ROOT";
/*      */       }
/*  167 */       boolean nptemp = NPTmpPattern.matcher(label).matches();
/*  168 */       boolean pptemp = PPTmpPattern.matcher(label).matches();
/*  169 */       boolean anytemp = TmpPattern.matcher(label).matches();
/*  170 */       label = this.tlp.basicCategory(label);
/*  171 */       if ((anytemp) && (this.splitXPTMP)) {
/*  172 */         label = label + "-TMP";
/*  173 */       } else if ((pptemp) && (this.splitPPTMP)) {
/*  174 */         label = label + "-TMP";
/*  175 */       } else if ((nptemp) && (this.splitNPTMP)) {
/*  176 */         label = label + "-TMP";
/*      */       }
/*  178 */       return label;
/*      */     }
/*      */     
/*      */ 
/*      */     public Tree normalizeWholeTree(Tree tree, TreeFactory tf)
/*      */     {
/*  184 */       Tree newTree = tree.prune(new Filter() {
/*      */         public boolean accept(Tree t) {
/*  186 */           Tree[] kids = t.children();
/*  187 */           Label l = t.label();
/*  188 */           if ((l != null) && (l.value() != null) && (l.value().matches("-NONE-.*")) && (!t.isLeaf()) && (kids.length == 1) && (kids[0].isLeaf()))
/*      */           {
/*      */ 
/*      */ 
/*  192 */             if (!l.value().equals("-NONE-")) {
/*  193 */               ChineseTreebankParserParams.printlnErr("Deleting errant node " + l.value() + " as if -NONE-: " + t);
/*      */             }
/*  195 */             return false;
/*      */           }
/*  197 */           return true; } }, tf).spliceOut(new Filter()
/*      */       {
/*      */ 
/*      */         public boolean accept(Tree t)
/*      */         {
/*  202 */           if ((t.isLeaf()) || (t.isPreTerminal()) || (t.children().length != 1)) {
/*  203 */             return true;
/*      */           }
/*  205 */           return (t.label() == null) || (!t.label().equals(t.children()[0].label())); } }, tf);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  210 */       Tree[] kids = newTree.children();
/*  211 */       if (kids.length > 1)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  223 */         ChineseTreebankParserParams.printlnErr("Possible error: non-unary initial rewrite: " + newTree.localTree());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  232 */       for (Tree subtree : newTree) {
/*  233 */         if (subtree.isPreTerminal()) {
/*  234 */           if (subtree.value().matches("NP")) {
/*  235 */             if (ChineseTreebankLanguagePack.chineseDouHaoAcceptFilter().accept(subtree.firstChild().value())) {
/*  236 */               ChineseTreebankParserParams.printlnErr("Correcting error: NP preterminal over douhao; preterminal changed to PU: " + subtree);
/*  237 */               subtree.setValue("PU");
/*  238 */             } else if (subtree.parent(newTree).value().matches("NP")) {
/*  239 */               ChineseTreebankParserParams.printlnErr("Correcting error: NP preterminal w/ NP parent; preterminal changed to NN: " + subtree.parent(newTree));
/*  240 */               subtree.setValue("NN");
/*      */             } else {
/*  242 */               ChineseTreebankParserParams.printlnErr("Correcting error: NP preterminal w/o NP parent, changing preterminal to NN: " + subtree.parent(newTree));
/*      */               
/*      */ 
/*  245 */               subtree.setValue("NN");
/*      */             }
/*  247 */           } else if (subtree.value().matches("PU")) {
/*  248 */             if (subtree.firstChild().value().matches("他")) {
/*  249 */               ChineseTreebankParserParams.printlnErr("Correcting error: \"他\" under PU tag; tag changed to PN: " + subtree);
/*  250 */               subtree.setValue("PN");
/*  251 */             } else if (subtree.firstChild().value().matches("tw|半穴式")) {
/*  252 */               ChineseTreebankParserParams.printlnErr("Correcting error: \"" + subtree.firstChild().value() + "\" under PU tag; tag changed to NN: " + subtree);
/*  253 */               subtree.setValue("NN");
/*  254 */             } else if (subtree.firstChild().value().matches("33")) {
/*  255 */               ChineseTreebankParserParams.printlnErr("Correcting error: \"33\" under PU tag; tag changed to CD: " + subtree);
/*  256 */               subtree.setValue("CD");
/*      */             }
/*      */           }
/*  259 */         } else if (subtree.value().matches("NN")) {
/*  260 */           ChineseTreebankParserParams.printlnErr("Correcting error: NN phrasal tag changed to NP: " + subtree);
/*  261 */           subtree.setValue("NP");
/*  262 */         } else if (subtree.value().matches("MSP")) {
/*  263 */           ChineseTreebankParserParams.printlnErr("Correcting error: MSP phrasal tag changed to VP: " + subtree);
/*  264 */           subtree.setValue("VP");
/*      */         }
/*      */       }
/*      */       
/*  268 */       if (this.tagExtender != null) {
/*  269 */         newTree = this.tagExtender.transformTree(newTree);
/*      */       }
/*  271 */       return newTree;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DiskTreebank diskTreebank()
/*      */   {
/*  281 */     String encoding = this.inputEncoding;
/*  282 */     if (!Charset.isSupported(encoding)) {
/*  283 */       printlnErr("Warning: desired encoding " + encoding + " not accepted. ");
/*  284 */       printlnErr("Using UTF-8 to construct DiskTreebank");
/*  285 */       encoding = "UTF-8";
/*      */     }
/*      */     
/*  288 */     return new DiskTreebank(treeReaderFactory(), encoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MemoryTreebank memoryTreebank()
/*      */   {
/*  297 */     String encoding = this.inputEncoding;
/*  298 */     if (!Charset.isSupported(encoding)) {
/*  299 */       System.out.println("Warning: desired encoding " + encoding + " not accepted. ");
/*  300 */       System.out.println("Using UTF-8 to construct MemoryTreebank");
/*  301 */       encoding = "UTF-8";
/*      */     }
/*      */     
/*  304 */     return new MemoryTreebank(treeReaderFactory(), encoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeTransformer collinizer()
/*      */   {
/*  312 */     return new ChineseCollinizer(this.ctlp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public TreeTransformer collinizerEvalb()
/*      */   {
/*  319 */     return new ChineseCollinizer(this.ctlp, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] sisterSplitters()
/*      */   {
/*  399 */     return StringUtils.EMPTY_STRING_ARRAY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Tree transformTree(Tree t, Tree root)
/*      */   {
/*  408 */     if ((t == null) || (t.isLeaf())) {
/*  409 */       return t;
/*      */     }
/*      */     
/*      */     String parentStr;
/*      */     
/*      */     Tree parent;
/*      */     String parentStr;
/*  416 */     if ((root == null) || (t.equals(root))) {
/*  417 */       Tree parent = null;
/*  418 */       parentStr = "";
/*      */     } else {
/*  420 */       parent = t.parent(root);
/*  421 */       parentStr = parent.label().value(); }
/*      */     String grandParentStr;
/*  423 */     Tree grandParent; String grandParentStr; if ((parent == null) || (parent.equals(root))) {
/*  424 */       Tree grandParent = null;
/*  425 */       grandParentStr = "";
/*      */     } else {
/*  427 */       grandParent = parent.parent(root);
/*  428 */       grandParentStr = grandParent.label().value();
/*      */     }
/*      */     
/*  431 */     String baseParentStr = this.ctlp.basicCategory(parentStr);
/*  432 */     String baseGrandParentStr = this.ctlp.basicCategory(grandParentStr);
/*      */     
/*  434 */     CategoryWordTag lab = (CategoryWordTag)t.label();
/*  435 */     String word = lab.word();
/*  436 */     String tag = lab.tag();
/*  437 */     String baseTag = this.ctlp.basicCategory(tag);
/*  438 */     String category = lab.value();
/*  439 */     String baseCategory = this.ctlp.basicCategory(category);
/*      */     
/*  441 */     if (t.isPreTerminal()) {
/*  442 */       List leftAunts = listBasicCategories(SisterAnnotationStats.leftSisterLabels(parent, grandParent));
/*  443 */       List rightAunts = listBasicCategories(SisterAnnotationStats.rightSisterLabels(parent, grandParent));
/*      */       
/*      */ 
/*  446 */       if ((chineseSplitPunct) && (baseTag.equals("PU"))) {
/*  447 */         if (ChineseTreebankLanguagePack.chineseDouHaoAcceptFilter().accept(word)) {
/*  448 */           tag = tag + "-DOU";
/*      */         }
/*  450 */         else if (ChineseTreebankLanguagePack.chineseCommaAcceptFilter().accept(word)) {
/*  451 */           tag = tag + "-COMMA";
/*      */         }
/*  453 */         else if (ChineseTreebankLanguagePack.chineseColonAcceptFilter().accept(word)) {
/*  454 */           tag = tag + "-COLON";
/*      */         }
/*  456 */         else if (ChineseTreebankLanguagePack.chineseQuoteMarkAcceptFilter().accept(word)) {
/*  457 */           if (chineseSplitPunctLR) {
/*  458 */             if (ChineseTreebankLanguagePack.chineseLeftQuoteMarkAcceptFilter().accept(word)) {
/*  459 */               tag = tag + "-LQUOTE";
/*      */             } else {
/*  461 */               tag = tag + "-RQUOTE";
/*      */             }
/*      */           } else {
/*  464 */             tag = tag + "-QUOTE";
/*      */           }
/*      */         }
/*  467 */         else if (ChineseTreebankLanguagePack.chineseEndSentenceAcceptFilter().accept(word)) {
/*  468 */           tag = tag + "-ENDSENT";
/*      */         }
/*  470 */         else if (ChineseTreebankLanguagePack.chineseParenthesisAcceptFilter().accept(word)) {
/*  471 */           if (chineseSplitPunctLR) {
/*  472 */             if (ChineseTreebankLanguagePack.chineseLeftParenthesisAcceptFilter().accept(word)) {
/*  473 */               tag = tag + "-LPAREN";
/*      */             } else {
/*  475 */               tag = tag + "-RPAREN";
/*      */             }
/*      */           } else {
/*  478 */             tag = tag + "-PAREN";
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*  484 */         else if (ChineseTreebankLanguagePack.chineseDashAcceptFilter().accept(word)) {
/*  485 */           tag = tag + "-DASH";
/*      */         }
/*  487 */         else if (ChineseTreebankLanguagePack.chineseOtherAcceptFilter().accept(word)) {
/*  488 */           tag = tag + "-OTHER";
/*      */         } else {
/*  490 */           printlnErr("Unknown punct (you should add it to CTLP): " + tag + " " + word);
/*  491 */           edu.stanford.nlp.misc.SeeChars.seeChars(word, "GB18030");
/*      */         }
/*  493 */       } else if ((chineseSplitDouHao) && 
/*  494 */         (ChineseTreebankLanguagePack.chineseDouHaoAcceptFilter().accept(word)) && (baseTag.equals("PU"))) {
/*  495 */         tag = tag + "-DOU";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  501 */       if (tagWordSize) {
/*  502 */         int l = word.length();
/*  503 */         tag = tag + "-" + l + "CHARS";
/*      */       }
/*      */       
/*  506 */       if ((mergeNNVV & baseTag.equals("NN"))) {
/*  507 */         tag = "VV";
/*      */       }
/*      */       
/*  510 */       if (((chineseSelectiveTagPA) || (chineseVerySelectiveTagPA)) && ((baseTag.equals("CC")) || (baseTag.equals("P")))) {
/*  511 */         tag = tag + "-" + baseParentStr;
/*      */       }
/*  513 */       if ((chineseSelectiveTagPA) && (baseTag.equals("VV"))) {
/*  514 */         tag = tag + "-" + baseParentStr;
/*      */       }
/*      */       
/*  517 */       if ((markMultiNtag) && (tag.startsWith("N"))) {
/*  518 */         for (int i = 0; i < parent.numChildren(); i++) {
/*  519 */           if ((parent.children()[i].label().value().startsWith("N")) && (parent.children()[i] != t)) {
/*  520 */             tag = tag + "=N";
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  526 */       if ((markVVsisterIP) && (baseTag.equals("VV"))) {
/*  527 */         boolean seenIP = false;
/*  528 */         for (int i = 0; i < parent.numChildren(); i++) {
/*  529 */           if (parent.children()[i].label().value().startsWith("IP")) {
/*  530 */             seenIP = true;
/*      */           }
/*      */         }
/*  533 */         if (seenIP) {
/*  534 */           tag = tag + "-IP";
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  539 */       if ((markPsisterIP) && (baseTag.equals("P"))) {
/*  540 */         boolean seenIP = false;
/*  541 */         for (int i = 0; i < parent.numChildren(); i++) {
/*  542 */           if (parent.children()[i].label().value().startsWith("IP")) {
/*  543 */             seenIP = true;
/*      */           }
/*      */         }
/*  546 */         if (seenIP) {
/*  547 */           tag = tag + "-IP";
/*      */         }
/*      */       }
/*      */       
/*  551 */       if ((markADgrandchildOfIP) && (baseTag.equals("AD")) && (baseGrandParentStr.equals("IP"))) {
/*  552 */         tag = tag + "~IP";
/*      */       }
/*      */       
/*      */ 
/*  556 */       if ((gpaAD) && (baseTag.equals("AD"))) {
/*  557 */         tag = tag + "~" + baseGrandParentStr;
/*      */       }
/*      */       
/*      */ 
/*  561 */       if ((markPostverbalP) && (leftAunts.contains("VV")) && (baseTag.equals("P")))
/*      */       {
/*  563 */         tag = tag + "^=lVV";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  568 */       Label label = new CategoryWordTag(tag, word, tag);
/*  569 */       t.setLabel(label);
/*      */     }
/*      */     else {
/*  572 */       Tree[] kids = t.children();
/*      */       
/*      */ 
/*  575 */       List leftSis = listBasicCategories(SisterAnnotationStats.leftSisterLabels(t, parent));
/*  576 */       List rightSis = listBasicCategories(SisterAnnotationStats.rightSisterLabels(t, parent));
/*      */       
/*  578 */       if ((paRootDtr) && (baseParentStr.equals("ROOT"))) {
/*  579 */         category = category + "^ROOT";
/*      */       }
/*      */       
/*  582 */       if ((markIPsisterBA) && (baseCategory.equals("IP")) && 
/*  583 */         (leftSis.contains("BA"))) {
/*  584 */         category = category + "=BA";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  589 */       if ((dominatesV) && (hasV(t.preTerminalYield())))
/*      */       {
/*  591 */         category = category + "-v";
/*      */       }
/*      */       
/*  594 */       if ((markIPsisterVVorP) && (baseCategory.equals("IP")) && (
/*  595 */         (leftSis.contains("VV")) || (leftSis.contains("P")))) {
/*  596 */         category = category + "=VVP";
/*      */       }
/*      */       
/*      */ 
/*  600 */       if ((markIPsisDEC) && (baseCategory.equals("IP")) && 
/*  601 */         (rightSis.contains("DEC"))) {
/*  602 */         category = category + "=DEC";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  607 */       if ((chineseSplitVP3) && (category.startsWith("VP"))) {
/*  608 */         boolean hasCC = false;
/*  609 */         boolean hasPU = false;
/*  610 */         boolean hasLexV = false;
/*  611 */         for (int i = 0; i < kids.length; i++) {
/*  612 */           if (kids[i].label().value().startsWith("CC")) {
/*  613 */             hasCC = true;
/*      */           }
/*  615 */           if (kids[i].label().value().startsWith("PU")) {
/*  616 */             hasPU = true;
/*      */           }
/*  618 */           if (StringUtils.lookingAt(kids[i].label().value(), "(V[ACEV]|VCD|VCP|VNV|VPT|VRD|VSB)")) {
/*  619 */             hasLexV = true;
/*      */           }
/*      */         }
/*  622 */         if ((hasCC) || ((hasPU) && (!hasLexV))) {
/*  623 */           category = category + "-CRD";
/*      */         }
/*  625 */         else if (hasLexV) {
/*  626 */           category = category + "-COMP";
/*      */         }
/*      */         else {
/*  629 */           category = category + "-ADJT";
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  634 */       if ((markVPadjunct) && (parentStr.startsWith("VP"))) {
/*  635 */         Tree[] sisters = parent.children();
/*  636 */         boolean hasVPsister = false;
/*  637 */         boolean hasCC = false;
/*  638 */         boolean hasPU = false;
/*  639 */         boolean hasLexV = false;
/*  640 */         for (int i = 0; i < sisters.length; i++) {
/*  641 */           if (sisters[i].label().value().startsWith("VP")) {
/*  642 */             hasVPsister = true;
/*      */           }
/*  644 */           if (sisters[i].label().value().startsWith("CC")) {
/*  645 */             hasCC = true;
/*      */           }
/*  647 */           if (sisters[i].label().value().startsWith("PU")) {
/*  648 */             hasPU = true;
/*      */           }
/*  650 */           if (StringUtils.lookingAt(sisters[i].label().value(), "(V[ACEV]|VCD|VCP|VNV|VPT|VRD|VSB)")) {
/*  651 */             hasLexV = true;
/*      */           }
/*      */         }
/*  654 */         if ((hasVPsister) && (!hasCC) && (!hasPU) && (!hasLexV)) {
/*  655 */           category = category + "-VPADJ";
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  660 */       if ((markNPmodNP) && (baseCategory.equals("NP")) && (baseParentStr.equals("NP")) && 
/*  661 */         (rightSis.contains("NP"))) {
/*  662 */         category = category + "=MODIFIERNP";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  667 */       if ((markModifiedNP) && (baseCategory.equals("NP")) && (baseParentStr.equals("NP")) && 
/*  668 */         (rightSis.size() == 0) && ((leftSis.contains("ADJP")) || (leftSis.contains("NP")) || (leftSis.contains("DNP")) || (leftSis.contains("QP")) || (leftSis.contains("CP")) || (leftSis.contains("PP")))) {
/*  669 */         category = category + "=MODIFIEDNP";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  674 */       if ((markNPconj) && (baseCategory.equals("NP")) && (baseParentStr.equals("NP")) && (
/*  675 */         (rightSis.contains("CC")) || (rightSis.contains("PU")) || (leftSis.contains("CC")) || (leftSis.contains("PU")))) {
/*  676 */         category = category + "=CONJ";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  681 */       if ((markIPconj) && (baseCategory.equals("IP")) && (baseParentStr.equals("IP"))) {
/*  682 */         Tree[] sisters = parent.children();
/*  683 */         boolean hasCommaSis = false;
/*  684 */         boolean hasIPSis = false;
/*  685 */         for (int i = 0; i < sisters.length; i++) {
/*  686 */           if ((this.ctlp.basicCategory(sisters[i].label().value()).equals("PU")) && (ChineseTreebankLanguagePack.chineseCommaAcceptFilter().accept(sisters[i].children()[0].label().toString()))) {
/*  687 */             hasCommaSis = true;
/*      */           }
/*      */           
/*  690 */           if ((this.ctlp.basicCategory(sisters[i].label().value()).equals("IP")) && (sisters[i] != t)) {
/*  691 */             hasIPSis = true;
/*      */           }
/*      */         }
/*  694 */         if ((hasCommaSis) && (hasIPSis)) {
/*  695 */           category = category + "-CONJ";
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  700 */       if ((unaryIP) && (baseCategory.equals("IP")) && (t.numChildren() == 1)) {
/*  701 */         category = category + "-U";
/*      */       }
/*      */       
/*  704 */       if ((unaryCP) && (baseCategory.equals("CP")) && (t.numChildren() == 1)) {
/*  705 */         category = category + "-U";
/*      */       }
/*      */       
/*      */ 
/*  709 */       if ((splitBaseNP) && (baseCategory.equals("NP")) && 
/*  710 */         (t.isPrePreTerminal())) {
/*  711 */         category = category + "-B";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  717 */       if ((markPostverbalPP) && (leftSis.contains("VV")) && (baseCategory.equals("PP")))
/*      */       {
/*  719 */         category = category + "=lVV";
/*      */       }
/*      */       
/*  722 */       if (((markADgrandchildOfIP) || (gpaAD)) && (listBasicCategories(SisterAnnotationStats.kidLabels(t)).contains("AD"))) {
/*  723 */         category = category + "^ADVP";
/*      */       }
/*      */       
/*  726 */       if (markCC)
/*      */       {
/*      */ 
/*      */ 
/*  730 */         for (int i = 1; i < kids.length - 1; i++) {
/*  731 */           String cat2 = kids[i].label().value();
/*  732 */           if (cat2.startsWith("CC")) {
/*  733 */             category = category + "-CC";
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  738 */       Label label = new CategoryWordTag(category, word, tag);
/*  739 */       t.setLabel(label);
/*      */     }
/*  741 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  749 */   public static boolean chineseSplitDouHao = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  754 */   public static boolean chineseSplitPunct = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  759 */   public static boolean chineseSplitPunctLR = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  765 */   public static boolean markVVsisterIP = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  770 */   public static boolean markPsisterIP = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  776 */   public static boolean markIPsisterVVorP = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  782 */   public static boolean markADgrandchildOfIP = false;
/*      */   
/*      */ 
/*      */ 
/*  786 */   public static boolean gpaAD = true;
/*      */   
/*      */ 
/*      */ 
/*  790 */   public static boolean chineseVerySelectiveTagPA = false;
/*  791 */   public static boolean chineseSelectiveTagPA = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  797 */   public static boolean markIPsisterBA = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  806 */   public static boolean markVPadjunct = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  811 */   public static boolean markNPmodNP = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  817 */   public static boolean markModifiedNP = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  822 */   public static boolean markNPconj = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  828 */   public static boolean markMultiNtag = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  833 */   public static boolean markIPsisDEC = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  839 */   public static boolean markIPconj = false;
/*  840 */   public static boolean markIPadjsubj = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  845 */   public static boolean chineseSplitVP3 = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  868 */   public static boolean mergeNNVV = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  875 */   public static boolean unaryIP = false;
/*  876 */   public static boolean unaryCP = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  882 */   public static boolean paRootDtr = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  890 */   public static boolean markPostverbalP = false;
/*  891 */   public static boolean markPostverbalPP = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  901 */   public static boolean splitBaseNP = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  906 */   public static boolean tagWordSize = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  912 */   public static boolean markCC = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  920 */   public static boolean splitNPTMP = false;
/*  921 */   public static boolean splitPPTMP = false;
/*  922 */   public static boolean splitXPTMP = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  928 */   public static boolean dominatesV = false;
/*      */   
/*      */   public void display()
/*      */   {
/*  932 */     String chineseParams = "Using ChineseTreebankParserParams chineseSplitDouHao=" + chineseSplitDouHao + " chineseSplitPunct=" + chineseSplitPunct + " chineseSplitPunctLR=" + chineseSplitPunctLR + " markVVsisterIP=" + markVVsisterIP + " markVPadjunct=" + markVPadjunct + " chineseSplitVP3=" + chineseSplitVP3 + " mergeNNVV=" + mergeNNVV + " unaryIP=" + unaryIP + " unaryCP=" + unaryCP + " paRootDtr=" + paRootDtr + " markPsisterIP=" + markPsisterIP + " markIPsisterVVorP=" + markIPsisterVVorP + " markADgrandchildOfIP=" + markADgrandchildOfIP + " gpaAD=" + gpaAD + " markIPsisterBA=" + markIPsisterBA + " markNPmodNP=" + markNPmodNP + " markNPconj=" + markNPconj + " markMultiNtag=" + markMultiNtag + " markIPsisDEC=" + markIPsisDEC + " markIPconj=" + markIPconj + " markIPadjsubj=" + markIPadjsubj + " markPostverbalP=" + markPostverbalP + " markPostverbalPP=" + markPostverbalPP + " baseNP=" + splitBaseNP + " headFinder=" + (this.bikelHeadFinder ? "bikel" : this.sunJurafskyHeadFinder ? "sunJurafsky" : "levy") + " discardFrags=" + this.discardFrags + " dominatesV=" + dominatesV;
/*      */     
/*      */ 
/*  935 */     printlnErr(chineseParams);
/*      */   }
/*      */   
/*      */   private List<String> listBasicCategories(List<String> l)
/*      */   {
/*  940 */     List<String> l1 = new ArrayList();
/*  941 */     for (String s : l) {
/*  942 */       l1.add(this.ctlp.basicCategory(s));
/*      */     }
/*  944 */     return l1;
/*      */   }
/*      */   
/*      */   private static boolean hasV(List tags)
/*      */   {
/*  949 */     int i = 0; for (int tsize = tags.size(); i < tsize; i++) {
/*  950 */       String str = tags.get(i).toString();
/*  951 */       if (str.startsWith("V")) {
/*  952 */         return true;
/*      */       }
/*      */     }
/*  955 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int setOptionFlag(String[] args, int i)
/*      */   {
/*  972 */     if (args[i].equalsIgnoreCase("-paRootDtr")) {
/*  973 */       paRootDtr = true;
/*  974 */       i++;
/*  975 */     } else if (args[i].equalsIgnoreCase("-unaryIP")) {
/*  976 */       unaryIP = true;
/*  977 */       i++;
/*  978 */     } else if (args[i].equalsIgnoreCase("-unaryCP")) {
/*  979 */       unaryCP = true;
/*  980 */       i++;
/*  981 */     } else if (args[i].equalsIgnoreCase("-markPostverbalP")) {
/*  982 */       markPostverbalP = true;
/*  983 */       i++;
/*  984 */     } else if (args[i].equalsIgnoreCase("-markPostverbalPP")) {
/*  985 */       markPostverbalPP = true;
/*  986 */       i++;
/*  987 */     } else if (args[i].equalsIgnoreCase("-baseNP")) {
/*  988 */       splitBaseNP = true;
/*  989 */       i++;
/*  990 */     } else if (args[i].equalsIgnoreCase("-markVVsisterIP")) {
/*  991 */       markVVsisterIP = true;
/*  992 */       i++;
/*  993 */     } else if (args[i].equalsIgnoreCase("-markPsisterIP")) {
/*  994 */       markPsisterIP = true;
/*  995 */       i++;
/*  996 */     } else if (args[i].equalsIgnoreCase("-markIPsisterVVorP")) {
/*  997 */       markIPsisterVVorP = true;
/*  998 */       i++;
/*  999 */     } else if (args[i].equalsIgnoreCase("-markIPsisterBA")) {
/* 1000 */       markIPsisterBA = true;
/* 1001 */       i++;
/* 1002 */     } else if (args[i].equalsIgnoreCase("-dominatesV")) {
/* 1003 */       dominatesV = true;
/* 1004 */       i++;
/* 1005 */     } else if (args[i].equalsIgnoreCase("-gpaAD")) {
/* 1006 */       gpaAD = true;
/* 1007 */       i++;
/* 1008 */     } else if (args[i].equalsIgnoreCase("-markVPadjunct")) {
/* 1009 */       markVPadjunct = true;
/* 1010 */       i++;
/* 1011 */     } else if (args[i].equalsIgnoreCase("-markNPmodNP")) {
/* 1012 */       markNPmodNP = true;
/* 1013 */       i++;
/* 1014 */     } else if (args[i].equalsIgnoreCase("-markModifiedNP")) {
/* 1015 */       markModifiedNP = true;
/* 1016 */       i++;
/* 1017 */     } else if (args[i].equalsIgnoreCase("-markNPconj")) {
/* 1018 */       markNPconj = true;
/* 1019 */       i++;
/* 1020 */     } else if (args[i].equalsIgnoreCase("-chineseSplitPunct")) {
/* 1021 */       chineseSplitPunct = true;
/* 1022 */       i++;
/* 1023 */     } else if (args[i].equalsIgnoreCase("-chineseSplitPunctLR")) {
/* 1024 */       chineseSplitPunct = true;
/* 1025 */       chineseSplitPunctLR = true;
/* 1026 */       i++;
/* 1027 */     } else if (args[i].equalsIgnoreCase("-chineseSelectiveTagPA")) {
/* 1028 */       chineseSelectiveTagPA = true;
/* 1029 */       i++;
/* 1030 */     } else if (args[i].equalsIgnoreCase("-chineseVerySelectiveTagPA")) {
/* 1031 */       chineseVerySelectiveTagPA = true;
/* 1032 */       i++;
/* 1033 */     } else if (args[i].equalsIgnoreCase("-markIPsisDEC")) {
/* 1034 */       markIPsisDEC = true;
/* 1035 */       i++;
/* 1036 */     } else if (args[i].equalsIgnoreCase("-chineseSplitVP3")) {
/* 1037 */       chineseSplitVP3 = true;
/* 1038 */       i++;
/* 1039 */     } else if (args[i].equalsIgnoreCase("-tagWordSize")) {
/* 1040 */       tagWordSize = true;
/* 1041 */       i++;
/* 1042 */     } else if (args[i].equalsIgnoreCase("-vanilla")) {
/* 1043 */       chineseSplitDouHao = false;
/* 1044 */       chineseSplitPunct = false;
/* 1045 */       chineseSplitPunctLR = false;
/* 1046 */       markVVsisterIP = false;
/* 1047 */       markPsisterIP = false;
/* 1048 */       markIPsisterVVorP = false;
/* 1049 */       markADgrandchildOfIP = false;
/* 1050 */       gpaAD = false;
/* 1051 */       markIPsisterBA = false;
/* 1052 */       markVPadjunct = false;
/* 1053 */       markNPmodNP = false;
/* 1054 */       markModifiedNP = false;
/* 1055 */       markNPconj = false;
/* 1056 */       markMultiNtag = false;
/* 1057 */       markIPsisDEC = false;
/* 1058 */       markIPconj = false;
/* 1059 */       markIPadjsubj = false;
/* 1060 */       chineseSplitVP3 = false;
/* 1061 */       mergeNNVV = false;
/* 1062 */       unaryIP = false;
/* 1063 */       unaryCP = false;
/* 1064 */       paRootDtr = false;
/* 1065 */       markPostverbalP = false;
/* 1066 */       markPostverbalPP = false;
/* 1067 */       splitBaseNP = false;
/*      */       
/* 1069 */       i++;
/* 1070 */     } else if (args[i].equalsIgnoreCase("-acl03chinese")) {
/* 1071 */       Train.markovOrder = 1;
/* 1072 */       Train.markovFactor = true;
/* 1073 */       chineseSplitDouHao = false;
/* 1074 */       chineseSplitPunct = true;
/* 1075 */       chineseSplitPunctLR = true;
/* 1076 */       markVVsisterIP = true;
/* 1077 */       markPsisterIP = true;
/* 1078 */       markIPsisterVVorP = true;
/* 1079 */       markADgrandchildOfIP = false;
/* 1080 */       gpaAD = true;
/* 1081 */       markIPsisterBA = false;
/* 1082 */       markVPadjunct = true;
/* 1083 */       markNPmodNP = true;
/* 1084 */       markModifiedNP = true;
/* 1085 */       markNPconj = true;
/* 1086 */       markMultiNtag = false;
/* 1087 */       markIPsisDEC = true;
/* 1088 */       markIPconj = false;
/* 1089 */       markIPadjsubj = false;
/* 1090 */       chineseSplitVP3 = true;
/* 1091 */       mergeNNVV = false;
/* 1092 */       unaryIP = true;
/* 1093 */       unaryCP = true;
/* 1094 */       paRootDtr = true;
/* 1095 */       markPostverbalP = false;
/* 1096 */       markPostverbalPP = false;
/* 1097 */       splitBaseNP = false;
/*      */       
/* 1099 */       i++;
/* 1100 */     } else if (args[i].equalsIgnoreCase("-chineseFactored")) {
/* 1101 */       chineseSplitDouHao = false;
/* 1102 */       chineseSplitPunct = true;
/* 1103 */       chineseSplitPunctLR = true;
/* 1104 */       markVVsisterIP = true;
/* 1105 */       markPsisterIP = true;
/* 1106 */       markIPsisterVVorP = true;
/* 1107 */       markADgrandchildOfIP = false;
/* 1108 */       gpaAD = true;
/* 1109 */       markIPsisterBA = true;
/* 1110 */       markVPadjunct = true;
/* 1111 */       markNPmodNP = true;
/* 1112 */       markModifiedNP = true;
/* 1113 */       markNPconj = true;
/* 1114 */       markMultiNtag = false;
/* 1115 */       markIPsisDEC = true;
/* 1116 */       markIPconj = false;
/* 1117 */       markIPadjsubj = false;
/* 1118 */       chineseSplitVP3 = true;
/* 1119 */       mergeNNVV = false;
/* 1120 */       unaryIP = true;
/* 1121 */       unaryCP = true;
/* 1122 */       paRootDtr = true;
/* 1123 */       markPostverbalP = false;
/* 1124 */       markPostverbalPP = false;
/* 1125 */       splitBaseNP = false;
/*      */       
/* 1127 */       chineseVerySelectiveTagPA = true;
/* 1128 */       i++;
/* 1129 */     } else if (args[i].equalsIgnoreCase("-chinesePCFG")) {
/* 1130 */       Train.markovOrder = 2;
/* 1131 */       Train.markovFactor = true;
/* 1132 */       Train.HSEL_CUT = 5;
/* 1133 */       Train.PA = true;
/* 1134 */       Train.gPA = true;
/* 1135 */       Train.selectiveSplit = false;
/* 1136 */       chineseSplitDouHao = false;
/* 1137 */       chineseSplitPunct = true;
/* 1138 */       chineseSplitPunctLR = true;
/* 1139 */       markVVsisterIP = true;
/* 1140 */       markPsisterIP = false;
/* 1141 */       markIPsisterVVorP = true;
/* 1142 */       markADgrandchildOfIP = false;
/* 1143 */       gpaAD = false;
/* 1144 */       markIPsisterBA = true;
/* 1145 */       markVPadjunct = true;
/* 1146 */       markNPmodNP = true;
/* 1147 */       markModifiedNP = true;
/* 1148 */       markNPconj = false;
/* 1149 */       markMultiNtag = false;
/* 1150 */       markIPsisDEC = false;
/* 1151 */       markIPconj = false;
/* 1152 */       markIPadjsubj = false;
/* 1153 */       chineseSplitVP3 = false;
/* 1154 */       mergeNNVV = false;
/* 1155 */       unaryIP = false;
/* 1156 */       unaryCP = false;
/* 1157 */       paRootDtr = false;
/* 1158 */       markPostverbalP = false;
/* 1159 */       markPostverbalPP = false;
/* 1160 */       splitBaseNP = false;
/*      */       
/* 1162 */       chineseVerySelectiveTagPA = true;
/* 1163 */       i++;
/* 1164 */     } else if (args[i].equalsIgnoreCase("-sunHead")) {
/* 1165 */       this.sunJurafskyHeadFinder = true;
/* 1166 */       i++;
/* 1167 */     } else if (args[i].equalsIgnoreCase("-bikelHead")) {
/* 1168 */       this.bikelHeadFinder = true;
/* 1169 */       i++;
/* 1170 */     } else if (args[i].equalsIgnoreCase("-discardFrags")) {
/* 1171 */       this.discardFrags = true;
/* 1172 */       i++;
/* 1173 */     } else if (args[i].equalsIgnoreCase("-charLex")) {
/* 1174 */       this.useCharacterBasedLexicon = true;
/* 1175 */       i++;
/* 1176 */     } else if (args[i].equalsIgnoreCase("-charUnk")) {
/* 1177 */       ChineseLexicon.useCharBasedUnknownWordModel = true;
/* 1178 */       i++;
/* 1179 */     } else if (args[i].equalsIgnoreCase("-gtUnknown")) {
/* 1180 */       ChineseLexicon.useGoodTuringUnknownWordModel = true;
/* 1181 */       i++;
/* 1182 */     } else if (args[i].equalsIgnoreCase("-maxentUnk"))
/*      */     {
/* 1184 */       i++;
/* 1185 */     } else if (args[i].equalsIgnoreCase("-tuneSigma"))
/*      */     {
/* 1187 */       i++;
/* 1188 */     } else if ((args[i].equalsIgnoreCase("-trainCountThresh")) && (i + 1 < args.length))
/*      */     {
/* 1190 */       i += 2;
/* 1191 */     } else if (args[i].equalsIgnoreCase("-markCC")) {
/* 1192 */       markCC = true;
/* 1193 */       i++;
/* 1194 */     } else if ((args[i].equalsIgnoreCase("-segmentMarkov")) || (args[i].equalsIgnoreCase("-segmentWords"))) {
/* 1195 */       this.segmentMarkov = true;
/*      */       try {
/* 1197 */         this.segmenter = ((WordSegmenter)Class.forName("edu.stanford.nlp.parser.lexparser.ChineseMarkovWordSegmenter").newInstance());
/*      */       } catch (Exception e) {
/* 1199 */         printlnErr("Couldn't instantiate segmenter edu.stanford.nlp.parser.lexparser.ChineseMarkovWordSegmenter: " + e);
/*      */       }
/* 1201 */       i++;
/* 1202 */     } else if (args[i].equalsIgnoreCase("-segmentMaxMatch")) {
/* 1203 */       this.segmentMaxMatch = true;
/*      */       try {
/* 1205 */         this.segmenter = ((WordSegmenter)Class.forName("edu.stanford.nlp.parser.lexparser.MaxMatchSegmenter").newInstance());
/*      */       } catch (Exception e) {
/* 1207 */         printlnErr("Couldn't instantiate segmenter edu.stanford.nlp.parser.lexparser.MaxMatchSegmenter: " + e);
/*      */       }
/* 1209 */       i++;
/* 1210 */     } else if (args[i].equalsIgnoreCase("-maxentLex"))
/*      */     {
/* 1212 */       i++;
/* 1213 */     } else if (args[i].equalsIgnoreCase("-fixUnkFunctionWords"))
/*      */     {
/* 1215 */       i++;
/* 1216 */     } else if (args[i].equalsIgnoreCase("-similarWordSmoothing")) {
/* 1217 */       this.useSimilarWordMap = true;
/* 1218 */       i++;
/* 1219 */     } else if (args[i].equalsIgnoreCase("-maxentLexSeenTagsOnly"))
/*      */     {
/*      */ 
/* 1222 */       i++;
/* 1223 */     } else if ((args[i].equalsIgnoreCase("-maxentLexFeatLevel")) && (i + 1 < args.length))
/*      */     {
/* 1225 */       i += 2;
/* 1226 */     } else if ((args[i].equalsIgnoreCase("-maxentDepGrammarFeatLevel")) && (i + 1 < args.length)) {
/* 1227 */       this.depGramFeatureLevel = Integer.parseInt(args[(i + 1)]);
/* 1228 */       i += 2;
/* 1229 */     } else if (args[i].equalsIgnoreCase("-maxentDepGrammar"))
/*      */     {
/* 1231 */       i++;
/* 1232 */     } else if (args[i].equalsIgnoreCase("-splitNPTMP")) {
/* 1233 */       splitNPTMP = true;
/* 1234 */       i++;
/* 1235 */     } else if (args[i].equalsIgnoreCase("-splitPPTMP")) {
/* 1236 */       splitPPTMP = true;
/* 1237 */       i++;
/* 1238 */     } else if (args[i].equalsIgnoreCase("-splitXPTMP")) {
/* 1239 */       splitXPTMP = true;
/* 1240 */       i++;
/* 1241 */     } else if (args[i].equalsIgnoreCase("-segmenter")) {
/*      */       try {
/* 1243 */         this.segmenter = ((WordSegmenter)Class.forName(args[(i + 1)]).newInstance());
/*      */       } catch (Exception e) {
/* 1245 */         printlnErr("Couldn't instantiate segmenter " + args[(i + 1)] + ": " + e);
/*      */       }
/* 1247 */       i += 2;
/*      */     }
/*      */     
/* 1250 */     return i;
/*      */   }
/*      */   
/* 1253 */   private int depGramFeatureLevel = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final long serialVersionUID = 2L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Extractor dependencyGrammarExtractor(Options op)
/*      */   {
/* 1275 */     if (this.useSimilarWordMap) {
/* 1276 */       new MLEDependencyGrammarExtractor(op) {
/*      */         public Object formResult() {
/* 1278 */           this.wordNumberer.number("UNK");
/* 1279 */           ChineseSimWordAvgDepGrammar dg = new ChineseSimWordAvgDepGrammar(this.tlpParams, this.directional, this.useDistance, this.useCoarseDistance);
/* 1280 */           if (ChineseTreebankParserParams.this.lex == null) {
/* 1281 */             throw new RuntimeException("Attempt to create ChineseSimWordAvgDepGrammar before Lexicon!!!");
/*      */           }
/* 1283 */           dg.setLex(ChineseTreebankParserParams.this.lex);
/*      */           
/* 1285 */           for (IntDependency dependency : this.dependencyCounter.keySet()) {
/* 1286 */             dg.addRule(dependency, this.dependencyCounter.getCount(dependency));
/*      */           }
/* 1288 */           return dg;
/*      */         }
/*      */       };
/*      */     }
/*      */     
/* 1293 */     return new MLEDependencyGrammarExtractor(op);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public List defaultTestSentence()
/*      */   {
/* 1301 */     return java.util.Arrays.asList(new String[] { "锟斤拷", "锟斤拷", "学校", "锟斤拷", "学习", "锟斤拷" });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1312 */     TreebankLangParserParams tlpp = new ChineseTreebankParserParams();
/* 1313 */     System.out.println("Default encoding is: " + tlpp.diskTreebank().encoding());
/*      */     
/*      */ 
/* 1316 */     if (args.length < 2) {
/* 1317 */       printlnErr("Usage: edu.stanford.nlp.parser.lexparser.ChineseTreebankParserParams treesPath fileRange");
/*      */     } else {
/* 1319 */       Treebank m = tlpp.diskTreebank();
/* 1320 */       m.loadPath(args[0], new edu.stanford.nlp.io.NumberRangesFileFilter(args[1], false));
/*      */       
/* 1322 */       for (Tree t : m) {
/* 1323 */         t.pennPrint(tlpp.pw());
/*      */       }
/* 1325 */       System.out.println("There were " + m.size() + " trees.");
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ChineseTreebankParserParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */