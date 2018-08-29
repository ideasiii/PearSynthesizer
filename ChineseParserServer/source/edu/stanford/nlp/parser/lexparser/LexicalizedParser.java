/*      */ package edu.stanford.nlp.parser.lexparser;
/*      */ 
/*      */ import edu.stanford.nlp.fsm.ExactGrammarCompactor;
/*      */ import edu.stanford.nlp.io.IOUtils;
/*      */ import edu.stanford.nlp.io.NumberRangeFileFilter;
/*      */ import edu.stanford.nlp.io.NumberRangesFileFilter;
/*      */ import edu.stanford.nlp.ling.CategoryWordTagFactory;
/*      */ import edu.stanford.nlp.ling.HasTag;
/*      */ import edu.stanford.nlp.ling.HasWord;
/*      */ import edu.stanford.nlp.ling.Sentence;
/*      */ import edu.stanford.nlp.ling.Word;
/*      */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*      */ import edu.stanford.nlp.parser.KBestViterbiParser;
/*      */ import edu.stanford.nlp.parser.ViterbiParser;
/*      */ import edu.stanford.nlp.process.DocumentPreprocessor;
/*      */ import edu.stanford.nlp.process.Function;
/*      */ import edu.stanford.nlp.process.WhitespaceTokenizer;
/*      */ import edu.stanford.nlp.trees.CompositeTreeTransformer;
/*      */ import edu.stanford.nlp.trees.CompositeTreebank;
/*      */ import edu.stanford.nlp.trees.DiskTreebank;
/*      */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*      */ import edu.stanford.nlp.trees.LeftHeadFinder;
/*      */ import edu.stanford.nlp.trees.Tree;
/*      */ import edu.stanford.nlp.trees.TreeFactory;
/*      */ import edu.stanford.nlp.trees.TreePrint;
/*      */ import edu.stanford.nlp.trees.TreeTransformer;
/*      */ import edu.stanford.nlp.trees.Treebank;
/*      */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*      */ import edu.stanford.nlp.util.Numberer;
/*      */ import edu.stanford.nlp.util.Pair;
/*      */ import edu.stanford.nlp.util.ScoredObject;
/*      */ import edu.stanford.nlp.util.Timing;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.FileFilter;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidClassException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Serializable;
/*      */ import java.io.StreamCorruptedException;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.zip.GZIPOutputStream;
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
/*      */ public class LexicalizedParser
/*      */   implements ViterbiParser, Function<Object, Tree>
/*      */ {
/*   84 */   static boolean basicCategoryTagsInDependencyGrammar = false;
/*   85 */   static String trainTreeFile = null;
/*      */   
/*   87 */   private boolean fallbackToPCFG = false;
/*      */   
/*      */   protected ExhaustivePCFGParser pparser;
/*      */   protected ExhaustiveDependencyParser dparser;
/*      */   protected KBestViterbiParser bparser;
/*      */   protected TreeTransformer debinarizer;
/*      */   private TreeTransformer subcategoryStripper;
/*      */   private transient ParserData pd;
/*      */   private Options op;
/*      */   private static final String SERIALIZED_PARSER_PROPERTY = "edu.stanford.nlp.SerializedLexicalizedParser";
/*      */   private static final String DEFAULT_PARSER_LOC = "/u/nlp/data/lexparser/englishPCFG.ser.gz";
/*      */   
/*      */   public Options getOp()
/*      */   {
/*  101 */     return this.op;
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
/*      */   public Tree apply(Object in)
/*      */   {
/*      */     List<Word> lst;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  123 */     if ((in instanceof String)) {
/*  124 */       DocumentPreprocessor dp = new DocumentPreprocessor(this.op.tlpParams.treebankLanguagePack().getTokenizerFactory());
/*  125 */       lst = dp.getWordsFromString((String)in); } else { List<Word> lst;
/*  126 */       if ((in instanceof List)) {
/*  127 */         lst = (List)in;
/*      */       } else {
/*  129 */         throw new IllegalArgumentException("Can only parse Sentence/List/String");
/*      */       }
/*      */     }
/*      */     List<Word> lst;
/*  133 */     if (parse(lst)) {
/*  134 */       return getBestParse();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  140 */     TreeFactory lstf = new LabeledScoredTreeFactory();
/*  141 */     List<Tree> lst2 = new ArrayList();
/*  142 */     for (Object obj : lst) {
/*  143 */       String s = obj.toString();
/*  144 */       Tree t = lstf.newLeaf(s);
/*  145 */       Tree t2 = lstf.newTreeNode("X", Collections.singletonList(t));
/*  146 */       lst2.add(t2);
/*      */     }
/*  148 */     return lstf.newTreeNode("X", lst2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreePrint getTreePrint()
/*      */   {
/*  156 */     return Test.treePrint(this.op.tlpParams);
/*      */   }
/*      */   
/*  159 */   private static boolean parseSucceeded = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean parse(List<? extends HasWord> sentence, String goal)
/*      */   {
/*  171 */     return parse(sentence);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean parse(String sentence)
/*      */   {
/*  183 */     DocumentPreprocessor dp = new DocumentPreprocessor(this.op.tlpParams.treebankLanguagePack().getTokenizerFactory());
/*      */     
/*  185 */     return parse(dp.getWordsFromString(sentence));
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
/*      */   public boolean parse(List<? extends HasWord> sentence)
/*      */   {
/*  214 */     int length = sentence.size();
/*  215 */     if (length == 0) {
/*  216 */       throw new UnsupportedOperationException("Can't parse a zero-length sentence!");
/*      */     }
/*  218 */     List<HasWord> sentenceB = new ArrayList(sentence);
/*  219 */     if (Test.addMissingFinalPunctuation) {
/*  220 */       addSentenceFinalPunctIfNeeded(sentenceB, length);
/*      */     }
/*  222 */     if (length > Test.maxLength) {
/*  223 */       throw new UnsupportedOperationException("Sentence too long: length " + length);
/*      */     }
/*  225 */     TreePrint treePrint = getTreePrint();
/*  226 */     PrintWriter pwOut = this.op.tlpParams.pw();
/*  227 */     parseSucceeded = false;
/*  228 */     sentenceB.add(new Word(".$."));
/*  229 */     if (this.op.doPCFG) {
/*  230 */       if (!this.pparser.parse(sentenceB)) {
/*  231 */         return parseSucceeded;
/*      */       }
/*  233 */       if (Test.verbose) {
/*  234 */         System.out.println("PParser output");
/*      */         
/*  236 */         treePrint.printTree(this.debinarizer.transformTree(this.pparser.getBestParse()), pwOut);
/*      */       }
/*      */     }
/*  239 */     if ((this.op.doDep) && (!Test.useFastFactored)) {
/*  240 */       if (!this.dparser.parse(sentenceB)) {
/*  241 */         return parseSucceeded;
/*      */       }
/*      */       
/*      */ 
/*  245 */       if (Test.verbose) {
/*  246 */         System.out.println("DParser output");
/*  247 */         treePrint.printTree(this.dparser.getBestParse(), pwOut);
/*      */       }
/*      */     }
/*  250 */     if ((this.op.doPCFG) && (this.op.doDep)) {
/*  251 */       if (!this.bparser.parse(sentenceB)) {
/*  252 */         return parseSucceeded;
/*      */       }
/*  254 */       parseSucceeded = true;
/*      */     }
/*      */     
/*  257 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean parse(LatticeReader lr)
/*      */   {
/*  267 */     TreePrint treePrint = getTreePrint();
/*  268 */     PrintWriter pwOut = this.op.tlpParams.pw();
/*  269 */     parseSucceeded = false;
/*  270 */     if (lr.getNumStates() > Test.maxLength + 1) {
/*  271 */       throw new UnsupportedOperationException("Lattice too big: " + lr.getNumStates());
/*      */     }
/*  273 */     if (this.op.doPCFG) {
/*  274 */       if (!this.pparser.parse(lr)) {
/*  275 */         return parseSucceeded;
/*      */       }
/*  277 */       if (Test.verbose) {
/*  278 */         System.out.println("PParser output");
/*  279 */         treePrint.printTree(this.debinarizer.transformTree(this.pparser.getBestParse()), pwOut);
/*      */       }
/*      */     }
/*  282 */     return true;
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
/*      */   private void addSentenceFinalPunctIfNeeded(List<HasWord> sentence, int length)
/*      */   {
/*  295 */     int start = length - 3;
/*  296 */     if (start < 0) start = 0;
/*  297 */     TreebankLanguagePack tlp = this.op.tlpParams.treebankLanguagePack();
/*  298 */     for (int i = length - 1; i >= start; i--) {
/*  299 */       Object item = sentence.get(i);
/*      */       
/*      */ 
/*      */ 
/*  303 */       String tag = null;
/*  304 */       if ((item instanceof HasTag)) {
/*  305 */         tag = ((HasTag)item).tag();
/*      */       }
/*  307 */       if ((tag != null) && (!"".equals(tag))) {
/*  308 */         if (!tlp.isSentenceFinalPunctuationTag(tag)) {}
/*      */ 
/*      */       }
/*  311 */       else if ((item instanceof HasWord)) {
/*  312 */         String str = ((HasWord)item).word();
/*  313 */         if (tlp.isPunctuationWord(str)) {
/*  314 */           return;
/*      */         }
/*      */       } else {
/*  317 */         String str = item.toString();
/*  318 */         if (tlp.isPunctuationWord(str)) {
/*  319 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  324 */     if (Test.verbose) {
/*  325 */       System.err.println("Adding missing final punctuation to sentence.");
/*      */     }
/*  327 */     String[] sfpWords = tlp.sentenceFinalPunctuationWords();
/*  328 */     if (sfpWords.length > 0) {
/*  329 */       sentence.add(new Word(sfpWords[0]));
/*      */     }
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
/*      */   public Tree getBestParse()
/*      */   {
/*  345 */     if ((this.bparser != null) && (parseSucceeded)) {
/*  346 */       Tree binaryTree = this.bparser.getBestParse();
/*  347 */       Tree tree = this.debinarizer.transformTree(binaryTree);
/*  348 */       if (this.op.nodePrune) {
/*  349 */         NodePruner np = new NodePruner(this.pparser, this.debinarizer);
/*  350 */         tree = np.prune(tree);
/*      */       }
/*  352 */       return this.subcategoryStripper.transformTree(tree); }
/*  353 */     if ((this.pparser != null) && (this.pparser.hasParse()) && (this.fallbackToPCFG))
/*  354 */       return getBestPCFGParse();
/*  355 */     if ((this.dparser != null) && (this.dparser.hasParse())) {
/*  356 */       return getBestDependencyParse(true);
/*      */     }
/*  358 */     throw new NoSuchElementException();
/*      */   }
/*      */   
/*      */ 
/*      */   public List<ScoredObject<Tree>> getKGoodFactoredParses(int k)
/*      */   {
/*  364 */     if (this.bparser == null) {
/*  365 */       return null;
/*      */     }
/*  367 */     List<ScoredObject<Tree>> binaryTrees = this.bparser.getKGoodParses(k);
/*  368 */     if (binaryTrees == null) {
/*  369 */       return null;
/*      */     }
/*  371 */     List<ScoredObject<Tree>> trees = new ArrayList(k);
/*  372 */     for (ScoredObject<Tree> tp : binaryTrees) {
/*  373 */       Tree t = this.debinarizer.transformTree((Tree)tp.object());
/*  374 */       t = this.subcategoryStripper.transformTree(t);
/*  375 */       trees.add(new ScoredObject(t, tp.score()));
/*      */     }
/*  377 */     return trees;
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
/*      */   public List<ScoredObject<Tree>> getKBestPCFGParses(int k)
/*      */   {
/*  390 */     if (this.pparser == null) {
/*  391 */       return null;
/*      */     }
/*  393 */     List<ScoredObject<Tree>> binaryTrees = this.pparser.getKBestParses(k);
/*  394 */     if (binaryTrees == null) {
/*  395 */       return null;
/*      */     }
/*  397 */     List<ScoredObject<Tree>> trees = new ArrayList(k);
/*  398 */     for (ScoredObject<Tree> p : binaryTrees) {
/*  399 */       Tree t = this.debinarizer.transformTree((Tree)p.object());
/*  400 */       t = this.subcategoryStripper.transformTree(t);
/*  401 */       trees.add(new ScoredObject(t, p.score()));
/*      */     }
/*  403 */     return trees;
/*      */   }
/*      */   
/*      */   public Tree getBestPCFGParse()
/*      */   {
/*  408 */     return getBestPCFGParse(true);
/*      */   }
/*      */   
/*      */   public Tree getBestPCFGParse(boolean stripSubcategories) {
/*  412 */     if (this.pparser == null) {
/*  413 */       return null;
/*      */     }
/*  415 */     Tree binaryTree = this.pparser.getBestParse();
/*  416 */     if (binaryTree == null) {
/*  417 */       return null;
/*      */     }
/*  419 */     Tree t = this.debinarizer.transformTree(binaryTree);
/*  420 */     if (stripSubcategories) {
/*  421 */       t = this.subcategoryStripper.transformTree(t);
/*      */     }
/*  423 */     return t;
/*      */   }
/*      */   
/*      */   public double getPCFGScore() {
/*  427 */     return this.pparser.getBestScore();
/*      */   }
/*      */   
/*      */   public double getPCFGScore(String goalStr) {
/*  431 */     return this.pparser.getBestScore(goalStr);
/*      */   }
/*      */   
/*      */   public Tree getBestDependencyParse() {
/*  435 */     return getBestDependencyParse(false);
/*      */   }
/*      */   
/*      */   public Tree getBestDependencyParse(boolean debinarize) {
/*  439 */     Tree t = this.dparser != null ? this.dparser.getBestParse() : null;
/*  440 */     if ((debinarize) && (t != null)) {
/*  441 */       t = this.debinarizer.transformTree(t);
/*      */     }
/*  443 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LexicalizedParser()
/*      */   {
/*  454 */     this(new Options());
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
/*      */   public LexicalizedParser(Options op)
/*      */   {
/*  471 */     this.op = op;
/*  472 */     String source = System.getProperty("edu.stanford.nlp.SerializedLexicalizedParser");
/*  473 */     if (source == null) {
/*  474 */       source = "/u/nlp/data/lexparser/englishPCFG.ser.gz";
/*      */     }
/*  476 */     this.pd = getParserDataFromFile(source, op);
/*  477 */     this.op = this.pd.pt;
/*  478 */     makeParsers();
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
/*      */   public LexicalizedParser(String parserFileOrUrl, Options op)
/*      */   {
/*  491 */     this.op = op;
/*      */     
/*  493 */     this.pd = getParserDataFromFile(parserFileOrUrl, op);
/*  494 */     this.op = this.pd.pt;
/*  495 */     makeParsers();
/*      */   }
/*      */   
/*      */   public LexicalizedParser(String parserFileOrUrl) {
/*  499 */     this(parserFileOrUrl, new Options());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxLength(int maxLength)
/*      */   {
/*  510 */     Test.maxLength = maxLength;
/*      */   }
/*      */   
/*      */   public static ParserData getParserDataFromFile(String parserFileOrUrl, Options op) {
/*  514 */     ParserData pd = getParserDataFromSerializedFile(parserFileOrUrl);
/*  515 */     if (pd == null) {
/*  516 */       pd = getParserDataFromTextFile(parserFileOrUrl, op);
/*      */     }
/*  518 */     return pd;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LexicalizedParser(String parserFileOrUrl, boolean isTextGrammar, Options op)
/*      */   {
/*  528 */     this.op = op;
/*  529 */     if (isTextGrammar) {
/*  530 */       this.pd = getParserDataFromTextFile(parserFileOrUrl, op);
/*      */     } else {
/*  532 */       this.pd = getParserDataFromSerializedFile(parserFileOrUrl);
/*  533 */       this.op = this.pd.pt;
/*      */     }
/*  535 */     makeParsers();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LexicalizedParser(ParserData pd)
/*      */   {
/*  545 */     this.pd = pd;
/*  546 */     makeParsers();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LexicalizedParser(ObjectInputStream in)
/*      */     throws Exception
/*      */   {
/*  558 */     this((ParserData)in.readObject());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LexicalizedParser(Treebank trainTreebank, GrammarCompactor compactor, Options op)
/*      */   {
/*  567 */     this(trainTreebank, compactor, op, null);
/*      */   }
/*      */   
/*      */   public LexicalizedParser(String treebankPath, FileFilter filt, Options op) {
/*  571 */     this(makeTreebank(treebankPath, op, filt), op);
/*      */   }
/*      */   
/*      */   private static Treebank makeTreebank(String treebankPath, Options op, FileFilter filt) {
/*  575 */     System.err.println("Training a parser from treebank dir: " + treebankPath);
/*  576 */     Treebank trainTreebank = op.tlpParams.diskTreebank();
/*  577 */     System.err.print("Reading trees...");
/*  578 */     if (filt == null) {
/*  579 */       trainTreebank.loadPath(treebankPath);
/*      */     } else {
/*  581 */       trainTreebank.loadPath(treebankPath, filt);
/*      */     }
/*      */     
/*  584 */     Timing.tick("done [read " + trainTreebank.size() + " trees].");
/*  585 */     return trainTreebank;
/*      */   }
/*      */   
/*      */   private static DiskTreebank makeSecondaryTreebank(String treebankPath, Options op, FileFilter filt) {
/*  589 */     System.err.println("Additionally training using secondary disk treebank: " + treebankPath + " " + filt);
/*  590 */     DiskTreebank trainTreebank = op.tlpParams.diskTreebank();
/*  591 */     if (filt == null) {
/*  592 */       trainTreebank.loadPath(treebankPath);
/*      */     } else {
/*  594 */       trainTreebank.loadPath(treebankPath, filt);
/*      */     }
/*  596 */     return trainTreebank;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public LexicalizedParser(Treebank trainTreebank, GrammarCompactor compactor, Options op, Treebank tuneTreebank)
/*      */   {
/*  608 */     this.op = op;
/*  609 */     this.pd = getParserDataFromTreebank(trainTreebank, compactor, tuneTreebank);
/*  610 */     makeParsers();
/*      */   }
/*      */   
/*      */   public LexicalizedParser(Treebank trainTreebank, DiskTreebank secondaryTrainTreebank, double weight, GrammarCompactor compactor, Options op) {
/*  614 */     this.op = op;
/*  615 */     this.pd = getParserDataFromTreebank(trainTreebank, secondaryTrainTreebank, weight, compactor);
/*  616 */     makeParsers();
/*      */   }
/*      */   
/*      */   public LexicalizedParser(Treebank trainTreebank, Options op) {
/*  620 */     this(trainTreebank, null, op);
/*      */   }
/*      */   
/*      */   public ParserData parserData() {
/*  624 */     return this.pd;
/*      */   }
/*      */   
/*      */   public Lexicon getLexicon() {
/*  628 */     return this.pd.lex;
/*      */   }
/*      */   
/*      */   static void saveParserDataToSerialized(ParserData pd, String filename) {
/*      */     try {
/*  633 */       System.err.print("Writing parser in serialized format to file " + filename + " ");
/*  634 */       ObjectOutputStream out = IOUtils.writeStreamFromString(filename);
/*  635 */       out.writeObject(pd);
/*  636 */       out.close();
/*  637 */       System.err.println("done.");
/*      */     } catch (IOException ioe) {
/*  639 */       ioe.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   static void saveParserDataToText(ParserData pd, String filename) {
/*      */     try {
/*  645 */       System.err.print("Writing parser in text grammar format to file " + filename);
/*      */       OutputStream os;
/*  647 */       OutputStream os; if (filename.endsWith(".gz"))
/*      */       {
/*  649 */         os = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(filename)));
/*      */       } else {
/*  651 */         os = new BufferedOutputStream(new FileOutputStream(filename));
/*      */       }
/*  653 */       PrintWriter out = new PrintWriter(os);
/*  654 */       String prefix = "BEGIN ";
/*  655 */       out.println(prefix + "OPTIONS");
/*  656 */       if (pd.pt != null) {
/*  657 */         pd.pt.writeData(out);
/*      */       }
/*  659 */       out.println();
/*  660 */       System.err.print(".");
/*  661 */       out.println(prefix + "LEXICON");
/*  662 */       if (pd.lex != null) {
/*  663 */         pd.lex.writeData(out);
/*      */       }
/*  665 */       out.println();
/*  666 */       System.err.print(".");
/*  667 */       out.println(prefix + "UNARY_GRAMMAR");
/*  668 */       if (pd.ug != null) {
/*  669 */         pd.ug.writeData(out);
/*      */       }
/*  671 */       out.println();
/*  672 */       System.err.print(".");
/*  673 */       out.println(prefix + "BINARY_GRAMMAR");
/*  674 */       if (pd.bg != null) {
/*  675 */         pd.bg.writeData(out);
/*      */       }
/*  677 */       out.println();
/*  678 */       System.err.print(".");
/*  679 */       out.println(prefix + "DEPENDENCY_GRAMMAR");
/*  680 */       if (pd.dg != null) {
/*  681 */         pd.dg.writeData(out);
/*      */       }
/*  683 */       out.println();
/*  684 */       System.err.print(".");
/*  685 */       out.flush();
/*  686 */       out.close();
/*  687 */       System.err.println("done.");
/*      */     } catch (IOException e) {
/*  689 */       System.err.println("Trouble saving parser data to ASCII format.");
/*  690 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   protected static ParserData getParserDataFromPetrovFiles(String grammarFile, String lexiconFile) {
/*      */     try {
/*  696 */       Options op = new Options();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  701 */       BufferedReader gIn = new BufferedReader(new FileReader(grammarFile));
/*  702 */       String line = gIn.readLine();
/*  703 */       Numberer stateNumberer = Numberer.getGlobalNumberer("states");
/*  704 */       List<UnaryRule> unaryRules = new ArrayList();
/*  705 */       List<BinaryRule> binaryRules = new ArrayList();
/*  706 */       while (line != null) {
/*  707 */         String[] fields = line.split("\\s+");
/*  708 */         if (fields.length == 4)
/*      */         {
/*  710 */           double score = Double.parseDouble(fields[3]);
/*  711 */           UnaryRule ur = new UnaryRule(stateNumberer.number(new String(fields[0])), stateNumberer.number(new String(fields[2])), score);
/*  712 */           unaryRules.add(ur);
/*  713 */         } else if (fields.length == 5)
/*      */         {
/*  715 */           double score = Double.parseDouble(fields[4]);
/*  716 */           BinaryRule br = new BinaryRule(stateNumberer.number(new String(fields[0])), stateNumberer.number(new String(fields[2])), stateNumberer.number(new String(fields[3])), score);
/*      */           
/*  718 */           binaryRules.add(br);
/*      */         } else {
/*  720 */           throw new RuntimeException("Bad line format: " + line);
/*      */         }
/*  722 */         line = gIn.readLine();
/*      */       }
/*      */       
/*  725 */       BinaryGrammar bg = new BinaryGrammar(stateNumberer.total());
/*  726 */       for (BinaryRule br : binaryRules) {
/*  727 */         bg.addRule(br);
/*      */       }
/*  729 */       bg.splitRules();
/*      */       
/*  731 */       UnaryGrammar ug = new UnaryGrammar(stateNumberer.total());
/*  732 */       for (UnaryRule ur : unaryRules) {
/*  733 */         ug.addRule(ur);
/*      */       }
/*  735 */       ug.purgeRules();
/*      */       
/*      */ 
/*  738 */       Lexicon lex = new PetrovLexicon();
/*  739 */       lex.readData(new BufferedReader(new FileReader(lexiconFile)));
/*      */       
/*  741 */       return new ParserData(lex, bg, ug, null, null, op);
/*      */     } catch (IOException e) {
/*  743 */       e.printStackTrace();
/*      */     }
/*  745 */     return null;
/*      */   }
/*      */   
/*      */   protected static ParserData getParserDataFromTextFile(String textFileOrUrl, Options op) {
/*      */     try {
/*  750 */       System.err.print("Loading parser from text file " + textFileOrUrl + " ");
/*  751 */       BufferedReader in = IOUtils.readReaderFromString(textFileOrUrl);
/*  752 */       Timing.startTime();
/*  753 */       String line = in.readLine();
/*  754 */       if (!line.startsWith("BEGIN")) {
/*  755 */         throw new RuntimeException("Didn't expect " + line);
/*      */       }
/*  757 */       op.readData(in);
/*  758 */       System.err.print(".");
/*  759 */       line = in.readLine();
/*  760 */       if (!line.startsWith("BEGIN")) {
/*  761 */         throw new RuntimeException("Didn't expect " + line);
/*      */       }
/*  763 */       Lexicon lex = op.tlpParams.lex(op.lexOptions);
/*  764 */       lex.readData(in);
/*  765 */       System.err.print(".");
/*  766 */       line = in.readLine();
/*  767 */       if (!line.startsWith("BEGIN")) {
/*  768 */         throw new RuntimeException("Didn't expect " + line);
/*      */       }
/*  770 */       UnaryGrammar ug = new UnaryGrammar(op.numStates);
/*  771 */       ug.readData(in);
/*  772 */       System.err.print(".");
/*  773 */       line = in.readLine();
/*  774 */       if (!line.startsWith("BEGIN")) {
/*  775 */         throw new RuntimeException("Didn't expect " + line);
/*      */       }
/*  777 */       BinaryGrammar bg = new BinaryGrammar(op.numStates);
/*  778 */       bg.readData(in);
/*  779 */       System.err.print(".");
/*  780 */       line = in.readLine();
/*  781 */       if (!line.startsWith("BEGIN")) {
/*  782 */         throw new RuntimeException("Didn't expect " + line);
/*      */       }
/*  784 */       DependencyGrammar dg = new MLEDependencyGrammar(op.tlpParams, op.directional, op.distance, op.coarseDistance);
/*  785 */       dg.readData(in);
/*  786 */       System.err.print(".");
/*  787 */       Map<String, Numberer> numbs = Numberer.getNumberers();
/*  788 */       in.close();
/*  789 */       System.err.println(" done.");
/*  790 */       return new ParserData(lex, bg, ug, dg, numbs, op);
/*      */     } catch (IOException e) {
/*  792 */       e.printStackTrace();
/*      */     }
/*  794 */     return null;
/*      */   }
/*      */   
/*      */   public static ParserData getParserDataFromSerializedFile(String serializedFileOrUrl)
/*      */   {
/*      */     try {
/*  800 */       Timing tim = new Timing();
/*  801 */       System.err.print("Loading parser from serialized file " + serializedFileOrUrl + " ...");
/*  802 */       ObjectInputStream in = IOUtils.readStreamFromString(serializedFileOrUrl);
/*  803 */       ParserData pd = (ParserData)in.readObject();
/*      */       
/*  805 */       in.close();
/*  806 */       System.err.println(" done [" + tim.toSecondsString() + " sec].");
/*  807 */       return pd;
/*      */     }
/*      */     catch (InvalidClassException ice) {
/*  810 */       System.err.println();
/*  811 */       ice.printStackTrace();
/*  812 */       System.exit(2);
/*      */     }
/*      */     catch (FileNotFoundException fnfe) {
/*  815 */       System.err.println();
/*  816 */       fnfe.printStackTrace();
/*  817 */       System.exit(2);
/*      */     }
/*      */     catch (StreamCorruptedException sce)
/*      */     {
/*  821 */       System.err.println();
/*      */     } catch (Exception e) {
/*  823 */       System.err.println();
/*  824 */       e.printStackTrace();
/*      */     }
/*  826 */     return null;
/*      */   }
/*      */   
/*      */   private static void printOptions(boolean train, Options op)
/*      */   {
/*  831 */     op.display();
/*  832 */     if (train) {
/*  833 */       Train.display();
/*      */     } else {
/*  835 */       Test.display();
/*      */     }
/*  837 */     op.tlpParams.display();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Pair<List<Tree>, List<Tree>> getAnnotatedBinaryTreebankFromTreebank(Treebank trainTreebank, Treebank tuneTreebank, Options op)
/*      */   {
/*  846 */     Timing.startTime();
/*      */     
/*  848 */     TreebankLangParserParams tlpParams = op.tlpParams;
/*  849 */     TreebankLanguagePack tlp = tlpParams.treebankLanguagePack();
/*      */     
/*  851 */     if (Test.verbose) {
/*  852 */       System.out.print("Training ");
/*  853 */       System.out.println(trainTreebank.textualSummary(tlp));
/*      */     }
/*      */     
/*  856 */     System.err.print("Binarizing trees...");
/*      */     TreeAnnotatorAndBinarizer binarizer;
/*  858 */     TreeAnnotatorAndBinarizer binarizer; if (!Train.leftToRight) {
/*  859 */       binarizer = new TreeAnnotatorAndBinarizer(tlpParams, op.forceCNF, !Train.outsideFactor(), true);
/*      */     } else {
/*  861 */       binarizer = new TreeAnnotatorAndBinarizer(tlpParams.headFinder(), new LeftHeadFinder(), tlpParams, op.forceCNF, !Train.outsideFactor(), true);
/*      */     }
/*  863 */     CollinsPuncTransformer collinsPuncTransformer = null;
/*  864 */     if (Train.collinsPunc) {
/*  865 */       collinsPuncTransformer = new CollinsPuncTransformer(tlp);
/*      */     }
/*  867 */     List<Tree> binaryTrainTrees = new ArrayList();
/*  868 */     List<Tree> binaryTuneTrees = new ArrayList();
/*      */     
/*  870 */     if (Train.selectiveSplit) {
/*  871 */       Train.splitters = ParentAnnotationStats.getSplitCategories(trainTreebank, Train.tagSelectiveSplit, 0, Train.selectiveSplitCutOff, Train.tagSelectiveSplitCutOff, tlp);
/*  872 */       if (Train.deleteSplitters != null) {
/*  873 */         List<String> deleted = new ArrayList();
/*  874 */         for (Iterator i$ = Train.deleteSplitters.iterator(); i$.hasNext();) { del = (String)i$.next();
/*  875 */           baseDel = tlp.basicCategory(del);
/*  876 */           checkBasic = del.equals(baseDel);
/*  877 */           for (it = Train.splitters.iterator(); it.hasNext();) {
/*  878 */             String elem = (String)it.next();
/*  879 */             String baseElem = tlp.basicCategory(elem);
/*  880 */             boolean delStr = ((checkBasic) && (baseElem.equals(baseDel))) || (elem.equals(del));
/*  881 */             if (delStr) {
/*  882 */               it.remove();
/*  883 */               deleted.add(elem); } } }
/*      */         String del;
/*      */         String baseDel;
/*      */         boolean checkBasic;
/*  887 */         Iterator<String> it; if (Test.verbose) {
/*  888 */           System.err.println("Removed from vertical splitters: " + deleted);
/*      */         }
/*      */       }
/*  891 */       if (Test.verbose) {
/*  892 */         List<String> list = new ArrayList(Train.splitters);
/*  893 */         Collections.sort(list);
/*  894 */         System.err.println("Parent split categories: " + list);
/*      */       }
/*      */     }
/*  897 */     if (Train.selectivePostSplit)
/*      */     {
/*  899 */       TreeTransformer myTransformer = new TreeAnnotator(tlpParams.headFinder(), tlpParams);
/*  900 */       Treebank annotatedTB = trainTreebank.transform(myTransformer);
/*  901 */       Train.postSplitters = ParentAnnotationStats.getSplitCategories(annotatedTB, true, 0, Train.selectivePostSplitCutOff, Train.tagSelectivePostSplitCutOff, tlp);
/*  902 */       if (Test.verbose) {
/*  903 */         System.err.println("Parent post annotation split categories: " + Train.postSplitters);
/*      */       }
/*      */     }
/*  906 */     if (Train.hSelSplit)
/*      */     {
/*  908 */       int ptt = Train.printTreeTransformations;
/*  909 */       Train.printTreeTransformations = 0;
/*  910 */       binarizer.setDoSelectiveSplit(false);
/*  911 */       for (Tree tree : trainTreebank) {
/*  912 */         if (Train.collinsPunc) {
/*  913 */           tree = collinsPuncTransformer.transformTree(tree);
/*      */         }
/*  915 */         binarizer.transformTree(tree);
/*      */       }
/*  917 */       binarizer.setDoSelectiveSplit(true);
/*  918 */       Train.printTreeTransformations = ptt;
/*      */     }
/*      */     
/*  921 */     for (Tree tree : trainTreebank) {
/*  922 */       if (Train.collinsPunc) {
/*  923 */         tree = collinsPuncTransformer.transformTree(tree);
/*      */       }
/*  925 */       tree = binarizer.transformTree(tree);
/*  926 */       if (tree.yield().size() - 1 <= trainLengthLimit)
/*      */       {
/*  928 */         binaryTrainTrees.add(tree);
/*      */       }
/*      */     }
/*  931 */     if (Train.printAnnotatedStateCounts) {
/*  932 */       binarizer.printStateCounts();
/*      */     }
/*  934 */     if (Train.printAnnotatedRuleCounts) {
/*  935 */       binarizer.printRuleCounts();
/*      */     }
/*      */     
/*  938 */     if (tuneTreebank != null) {
/*  939 */       for (Tree tree : tuneTreebank) {
/*  940 */         if (Train.collinsPunc) {
/*  941 */           tree = collinsPuncTransformer.transformTree(tree);
/*      */         }
/*  943 */         tree = binarizer.transformTree(tree);
/*  944 */         if (tree.yield().size() - 1 <= trainLengthLimit) {
/*  945 */           binaryTuneTrees.add(tree);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  950 */     Timing.tick("done.");
/*  951 */     if (Test.verbose) {
/*  952 */       binarizer.dumpStats();
/*      */     }
/*      */     
/*  955 */     return new Pair(binaryTrainTrees, binaryTuneTrees);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final ParserData getParserDataFromTreebank(Treebank trainTreebank, GrammarCompactor compactor, Treebank tuneTreebank)
/*      */   {
/*  962 */     System.err.println("Currently " + new Date());
/*  963 */     printOptions(true, this.op);
/*  964 */     Pair<List<Tree>, List<Tree>> pair = getAnnotatedBinaryTreebankFromTreebank(trainTreebank, tuneTreebank, this.op);
/*  965 */     List<Tree> binaryTrainTrees = (List)pair.first();
/*  966 */     List<Tree> binaryTuneTrees = (List)pair.second();
/*      */     
/*      */ 
/*      */ 
/*  970 */     Extractor bgExtractor = new BinaryGrammarExtractor();
/*      */     
/*  972 */     Extractor dgExtractor = this.op.tlpParams.dependencyGrammarExtractor(this.op);
/*      */     
/*  974 */     System.err.print("Extracting PCFG...");
/*  975 */     Pair<UnaryGrammar, BinaryGrammar> bgug = (Pair)bgExtractor.extract(binaryTrainTrees);
/*  976 */     Timing.tick("done.");
/*  977 */     if (compactor != null) {
/*  978 */       System.err.print("Compacting grammar...");
/*  979 */       bgug = compactor.compactGrammar(bgug);
/*  980 */       Timing.tick("done.");
/*      */     }
/*  982 */     System.err.print("Compiling grammar...");
/*  983 */     BinaryGrammar bg = (BinaryGrammar)bgug.second;
/*  984 */     bg.splitRules();
/*  985 */     UnaryGrammar ug = (UnaryGrammar)bgug.first;
/*      */     
/*      */ 
/*  988 */     ug.purgeRules();
/*      */     
/*      */ 
/*  991 */     Timing.tick("done");
/*      */     
/*  993 */     System.err.print("Extracting Lexicon...");
/*  994 */     Lexicon lex = this.op.tlpParams.lex(this.op.lexOptions);
/*  995 */     lex.train(binaryTrainTrees);
/*  996 */     Timing.tick("done.");
/*      */     
/*  998 */     DependencyGrammar dg = null;
/*  999 */     if (this.op.doDep) {
/* 1000 */       System.err.print("Extracting Dependencies...");
/* 1001 */       dg = (DependencyGrammar)dgExtractor.extract(binaryTrainTrees);
/*      */       
/* 1003 */       Timing.tick("done.");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1008 */       if (tuneTreebank != null) {
/* 1009 */         System.err.print("Tuning Dependency Model...");
/* 1010 */         dg.tune(binaryTuneTrees);
/* 1011 */         Timing.tick("done.");
/*      */       }
/*      */     }
/* 1014 */     Map<String, Numberer> numbs = Numberer.getNumberers();
/*      */     
/* 1016 */     System.err.println("Done training parser.");
/* 1017 */     if (trainTreeFile != null) {
/*      */       try {
/* 1019 */         System.err.print("Writing out binary trees to " + trainTreeFile + "...");
/* 1020 */         IOUtils.writeObjectToFile((Serializable)binaryTrainTrees, trainTreeFile);
/* 1021 */         Timing.tick("done.");
/*      */       } catch (Exception e) {
/* 1023 */         System.err.println("Problem writing out binary trees.");
/*      */       }
/*      */     }
/* 1026 */     return new ParserData(lex, bg, ug, dg, numbs, this.op);
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
/*      */   protected final ParserData getParserDataFromTreebank(Treebank trainTreebank, DiskTreebank secondaryTrainTreebank, double weight, GrammarCompactor compactor)
/*      */   {
/* 1042 */     System.err.println("Currently " + new Date());
/* 1043 */     printOptions(true, this.op);
/* 1044 */     Timing.startTime();
/*      */     
/*      */ 
/* 1047 */     TreebankLangParserParams tlpParams = this.op.tlpParams;
/* 1048 */     TreebankLanguagePack tlp = tlpParams.treebankLanguagePack();
/*      */     
/* 1050 */     if (Test.verbose) {
/* 1051 */       System.err.print("Training ");
/* 1052 */       System.err.println(trainTreebank.textualSummary(tlp));
/* 1053 */       System.err.print("Secondary training ");
/* 1054 */       System.err.println(secondaryTrainTreebank.textualSummary(tlp));
/*      */     }
/*      */     
/* 1057 */     CompositeTreeTransformer trainTransformer = new CompositeTreeTransformer();
/*      */     
/* 1059 */     if (Train.collinsPunc) {
/* 1060 */       CollinsPuncTransformer collinsPuncTransformer = new CollinsPuncTransformer(tlp);
/* 1061 */       trainTransformer.addTransformer(collinsPuncTransformer);
/*      */     }
/*      */     TreeAnnotatorAndBinarizer binarizer;
/*      */     TreeAnnotatorAndBinarizer binarizer;
/* 1065 */     if (!Train.leftToRight) {
/* 1066 */       binarizer = new TreeAnnotatorAndBinarizer(tlpParams, this.op.forceCNF, !Train.outsideFactor(), true);
/*      */     } else {
/* 1068 */       binarizer = new TreeAnnotatorAndBinarizer(tlpParams.headFinder(), new LeftHeadFinder(), tlpParams, this.op.forceCNF, !Train.outsideFactor(), true);
/*      */     }
/* 1070 */     trainTransformer.addTransformer(binarizer);
/*      */     
/* 1072 */     CompositeTreebank wholeTreebank = new CompositeTreebank(trainTreebank, secondaryTrainTreebank);
/* 1073 */     if (Train.selectiveSplit) {
/* 1074 */       Train.splitters = ParentAnnotationStats.getSplitCategories(wholeTreebank, Train.tagSelectiveSplit, 0, Train.selectiveSplitCutOff, Train.tagSelectiveSplitCutOff, tlp);
/* 1075 */       if (Train.deleteSplitters != null) {
/* 1076 */         List<String> deleted = new ArrayList();
/* 1077 */         for (Iterator i$ = Train.deleteSplitters.iterator(); i$.hasNext();) { del = (String)i$.next();
/* 1078 */           baseDel = tlp.basicCategory(del);
/* 1079 */           checkBasic = del.equals(baseDel);
/* 1080 */           for (it = Train.splitters.iterator(); it.hasNext();) {
/* 1081 */             String elem = (String)it.next();
/* 1082 */             String baseElem = tlp.basicCategory(elem);
/* 1083 */             boolean delStr = ((checkBasic) && (baseElem.equals(baseDel))) || (elem.equals(del));
/* 1084 */             if (delStr) {
/* 1085 */               it.remove();
/* 1086 */               deleted.add(elem); } } }
/*      */         String del;
/*      */         String baseDel;
/*      */         boolean checkBasic;
/* 1090 */         Iterator<String> it; if (Test.verbose) {
/* 1091 */           System.err.println("Removed from vertical splitters: " + deleted);
/*      */         }
/*      */       }
/* 1094 */       if (Test.verbose) {
/* 1095 */         List<String> list = new ArrayList(Train.splitters);
/* 1096 */         Collections.sort(list);
/* 1097 */         System.err.println("Parent split categories: " + list);
/*      */       }
/*      */     }
/*      */     
/* 1101 */     Treebank transformedWholeTreebank = wholeTreebank;
/*      */     
/* 1103 */     if (Train.selectivePostSplit)
/*      */     {
/* 1105 */       TreeTransformer annotator = new TreeAnnotator(tlpParams.headFinder(), tlpParams);
/*      */       
/* 1107 */       transformedWholeTreebank = transformedWholeTreebank.transform(annotator);
/* 1108 */       Train.postSplitters = ParentAnnotationStats.getSplitCategories(transformedWholeTreebank, true, 0, Train.selectivePostSplitCutOff, Train.tagSelectivePostSplitCutOff, tlp);
/* 1109 */       if (Test.verbose) {
/* 1110 */         System.err.println("Parent post annotation split categories: " + Train.postSplitters);
/*      */       }
/*      */     }
/* 1113 */     if (Train.hSelSplit)
/*      */     {
/* 1115 */       int ptt = Train.printTreeTransformations;
/* 1116 */       Train.printTreeTransformations = 0;
/* 1117 */       binarizer.setDoSelectiveSplit(false);
/* 1118 */       for (Tree tree : transformedWholeTreebank) {
/* 1119 */         trainTransformer.transformTree(tree);
/*      */       }
/* 1121 */       binarizer.setDoSelectiveSplit(true);
/* 1122 */       Train.printTreeTransformations = ptt;
/*      */     }
/*      */     
/* 1125 */     trainTreebank = trainTreebank.transform(trainTransformer);
/*      */     
/* 1127 */     Treebank transformedSecondaryTrainTreebank = secondaryTrainTreebank.transform(trainTransformer);
/*      */     
/*      */ 
/*      */ 
/* 1131 */     BinaryGrammarExtractor bgExtractor = new BinaryGrammarExtractor();
/*      */     
/* 1133 */     MLEDependencyGrammarExtractor dgExtractor = new MLEDependencyGrammarExtractor(this.op);
/*      */     
/* 1135 */     System.err.print("Extracting PCFG...");
/* 1136 */     Pair<UnaryGrammar, BinaryGrammar> bgug = (Pair)bgExtractor.extract(trainTreebank, transformedSecondaryTrainTreebank, weight);
/* 1137 */     Timing.tick("done.");
/* 1138 */     if (compactor != null) {
/* 1139 */       System.err.print("Compacting grammar...");
/* 1140 */       bgug = compactor.compactGrammar(bgug);
/* 1141 */       Timing.tick("done.");
/*      */     }
/* 1143 */     System.err.print("Compiling grammar...");
/* 1144 */     BinaryGrammar bg = (BinaryGrammar)bgug.second;
/* 1145 */     bg.splitRules();
/* 1146 */     UnaryGrammar ug = (UnaryGrammar)bgug.first;
/* 1147 */     ug.purgeRules();
/* 1148 */     Timing.tick("done");
/* 1149 */     System.err.print("Extracting Lexicon...");
/* 1150 */     Lexicon lex = this.op.tlpParams.lex(this.op.lexOptions);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1157 */     CompositeTreebank wholeBinaryTreebank = new CompositeTreebank(trainTreebank, transformedSecondaryTrainTreebank);
/* 1158 */     lex.train(wholeBinaryTreebank);
/* 1159 */     Timing.tick("done.");
/*      */     
/* 1161 */     DependencyGrammar dg = null;
/* 1162 */     if (this.op.doDep) {
/* 1163 */       System.err.print("Extracting Dependencies...");
/* 1164 */       dg = (DependencyGrammar)dgExtractor.extract(trainTreebank, transformedSecondaryTrainTreebank, weight);
/* 1165 */       Timing.tick("done.");
/*      */     }
/* 1167 */     Map<String, Numberer> numbs = Numberer.getNumberers();
/*      */     
/* 1169 */     System.err.println("Done training parser.");
/* 1170 */     return new ParserData(lex, bg, ug, dg, numbs, this.op);
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
/*      */   public void reset()
/*      */   {
/* 1193 */     makeParsers();
/*      */   }
/*      */   
/*      */   private void makeParsers() {
/* 1197 */     if (this.pd == null) {
/* 1198 */       throw new IllegalArgumentException("Error loading parser data: pd null");
/*      */     }
/* 1200 */     Numberer.setNumberers(this.pd.numbs);
/* 1201 */     BinaryGrammar bg = this.pd.bg;
/* 1202 */     bg.splitRules();
/* 1203 */     UnaryGrammar ug = this.pd.ug;
/* 1204 */     Lexicon lex = this.pd.lex;
/* 1205 */     DependencyGrammar dg = this.pd.dg;
/* 1206 */     this.op = this.pd.pt;
/* 1207 */     if (this.op.doPCFG) {
/* 1208 */       if (Test.iterativeCKY) {
/* 1209 */         this.pparser = new IterativeCKYPCFGParser(bg, ug, lex, this.op);
/*      */       } else {
/* 1211 */         this.pparser = new ExhaustivePCFGParser(bg, ug, lex, this.op);
/*      */       }
/*      */     }
/* 1214 */     if (this.op.doDep) {
/* 1215 */       if (!Test.useFastFactored) {
/* 1216 */         this.dparser = new ExhaustiveDependencyParser(dg, lex, this.op);
/*      */       }
/* 1218 */       if (this.op.doPCFG) {
/* 1219 */         if (Test.useFastFactored) {
/* 1220 */           MLEDependencyGrammar mledg = (MLEDependencyGrammar)dg;
/* 1221 */           int numToFind = 1;
/* 1222 */           if (Test.printFactoredKGood > 0) {
/* 1223 */             numToFind = Test.printFactoredKGood;
/*      */           }
/* 1225 */           this.bparser = new FastFactoredParser(this.pparser, mledg, this.op, numToFind);
/*      */         } else {
/* 1227 */           Scorer scorer = new TwinScorer(this.pparser, this.dparser);
/*      */           
/* 1229 */           if (Test.useN5) {
/* 1230 */             this.bparser = new BiLexPCFGParser.N5BiLexPCFGParser(scorer, this.pparser, this.dparser, bg, ug, dg, lex, this.op);
/*      */           } else {
/* 1232 */             this.bparser = new BiLexPCFGParser(scorer, this.pparser, this.dparser, bg, ug, dg, lex, this.op);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1237 */     this.fallbackToPCFG = true;
/*      */     
/* 1239 */     this.debinarizer = new Debinarizer(this.op.forceCNF, new CategoryWordTagFactory());
/* 1240 */     this.subcategoryStripper = this.op.tlpParams.subcategoryStripper();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static Sentence getInputSentence(Tree t)
/*      */   {
/* 1248 */     if (Test.forceTags) {
/* 1249 */       if (Test.preTag)
/*      */       {
/* 1251 */         throw new RuntimeException("Sorry -- haven't implemented support for external tagger yet.");
/*      */       }
/* 1253 */       return t.taggedYield();
/*      */     }
/*      */     
/* 1256 */     return t.yield();
/*      */   }
/*      */   
/*      */ 
/*      */   private static int numSubArgs(String[] args, int index)
/*      */   {
/* 1262 */     int i = index;
/* 1263 */     while ((i + 1 < args.length) && (args[(i + 1)].charAt(0) != '-')) {
/* 1264 */       i++;
/*      */     }
/* 1266 */     return i - index;
/*      */   }
/*      */   
/*      */   private static void printOutOfMemory(PrintWriter pw) {
/* 1270 */     pw.println();
/* 1271 */     pw.println("*******************************************************");
/* 1272 */     pw.println("***  WARNING!! OUT OF MEMORY! THERE WAS NOT ENOUGH  ***");
/* 1273 */     pw.println("***  MEMORY TO RUN ALL PARSERS.  EITHER GIVE THE    ***");
/* 1274 */     pw.println("***  JVM MORE MEMORY, SET THE MAXIMUM SENTENCE      ***");
/* 1275 */     pw.println("***  LENGTH WITH -maxLength, OR PERHAPS YOU ARE     ***");
/* 1276 */     pw.println("***  HAPPY TO HAVE THE PARSER FALL BACK TO USING    ***");
/* 1277 */     pw.println("***  A SIMPLER PARSER FOR VERY LONG SENTENCES.      ***");
/* 1278 */     pw.println("*******************************************************");
/* 1279 */     pw.println();
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
/*      */   public double testOnTreebank(Treebank testTreebank)
/*      */   {
/* 1292 */     System.err.println("Testing on treebank");
/* 1293 */     Timing treebankTotalTtimer = new Timing();
/* 1294 */     TreePrint treePrint = getTreePrint();
/* 1295 */     TreebankLangParserParams tlpParams = this.op.tlpParams;
/* 1296 */     TreebankLanguagePack tlp = this.op.langpack();
/* 1297 */     if (Test.verbose) {
/* 1298 */       System.out.print("Testing ");
/* 1299 */       System.out.println(testTreebank.textualSummary(tlp));
/*      */     }
/* 1301 */     if (Test.evalb) {
/* 1302 */       EvalB.initEVALBfiles(tlpParams);
/*      */     }
/* 1304 */     PrintWriter pwOut = tlpParams.pw();
/* 1305 */     PrintWriter pwErr = tlpParams.pw(System.err);
/* 1306 */     TreeTransformer tc = tlpParams.collinizer();
/* 1307 */     TreeTransformer br = new BoundaryRemover();
/*      */     
/*      */ 
/* 1310 */     boolean runningAverages = Boolean.parseBoolean(Test.evals.getProperty("runningAverages"));
/* 1311 */     boolean summary = Boolean.parseBoolean(Test.evals.getProperty("summary"));
/* 1312 */     boolean tsv = Boolean.parseBoolean(Test.evals.getProperty("tsv"));
/* 1313 */     AbstractEval pcfgLB = null;
/* 1314 */     if (Boolean.parseBoolean(Test.evals.getProperty("pcfgLB"))) {
/* 1315 */       pcfgLB = new LabeledConstituentEval("pcfg LP/LR", runningAverages, tlp);
/*      */     }
/* 1317 */     AbstractEval pcfgCB = null;
/* 1318 */     if (Boolean.parseBoolean(Test.evals.getProperty("pcfgCB"))) {
/* 1319 */       pcfgCB = new LabeledConstituentEval.CBEval("pcfg CB", runningAverages, tlp);
/*      */     }
/* 1321 */     AbstractEval pcfgDA = null;
/* 1322 */     if (Boolean.parseBoolean(Test.evals.getProperty("pcfgDA"))) {
/* 1323 */       pcfgDA = new AbstractEval.DependencyEval("pcfg DA", runningAverages, tlp.punctuationWordAcceptFilter());
/*      */     }
/* 1325 */     AbstractEval pcfgTA = null;
/* 1326 */     if (Boolean.parseBoolean(Test.evals.getProperty("pcfgTA"))) {
/* 1327 */       pcfgTA = new AbstractEval.TaggingEval("pcfg Tag", runningAverages, this.pd.lex);
/*      */     }
/* 1329 */     AbstractEval depDA = null;
/* 1330 */     if (Boolean.parseBoolean(Test.evals.getProperty("depDA"))) {
/* 1331 */       depDA = new AbstractEval.DependencyEval("dep DA", runningAverages, tlp.punctuationWordAcceptFilter());
/*      */     }
/* 1333 */     AbstractEval depTA = null;
/* 1334 */     if (Boolean.parseBoolean(Test.evals.getProperty("depTA"))) {
/* 1335 */       depTA = new AbstractEval.TaggingEval("dep Tag", runningAverages, this.pd.lex, true);
/*      */     }
/* 1337 */     LabeledConstituentEval factLB = null;
/* 1338 */     if (Boolean.parseBoolean(Test.evals.getProperty("factLB"))) {
/* 1339 */       factLB = new LabeledConstituentEval("factor LP/LR", runningAverages, tlp);
/*      */     }
/* 1341 */     AbstractEval factCB = null;
/* 1342 */     if (Boolean.parseBoolean(Test.evals.getProperty("factCB"))) {
/* 1343 */       factCB = new LabeledConstituentEval.CBEval("fact CB", runningAverages, tlp);
/*      */     }
/* 1345 */     AbstractEval factDA = null;
/* 1346 */     if (Boolean.parseBoolean(Test.evals.getProperty("factDA"))) {
/* 1347 */       factDA = new AbstractEval.DependencyEval("factor DA", runningAverages, tlp.punctuationWordAcceptFilter());
/*      */     }
/* 1349 */     AbstractEval factTA = null;
/* 1350 */     if (Boolean.parseBoolean(Test.evals.getProperty("factTA"))) {
/* 1351 */       factTA = new AbstractEval.TaggingEval("factor Tag", runningAverages, this.pd.lex);
/*      */     }
/* 1353 */     AbstractEval pcfgRUO = null;
/* 1354 */     if (Boolean.parseBoolean(Test.evals.getProperty("pcfgRUO"))) {
/* 1355 */       pcfgRUO = new AbstractEval.RuleErrorEval("pcfg Rule under/over");
/*      */     }
/* 1357 */     AbstractEval pcfgCUO = null;
/* 1358 */     if (Boolean.parseBoolean(Test.evals.getProperty("pcfgCUO"))) {
/* 1359 */       pcfgCUO = new AbstractEval.CatErrorEval("pcfg Category under/over");
/*      */     }
/* 1361 */     AbstractEval pcfgCatE = null;
/* 1362 */     if (Boolean.parseBoolean(Test.evals.getProperty("pcfgCatE"))) {
/* 1363 */       pcfgCatE = new ConstituentEvalByCat("pcfg Category Eval", tlp);
/*      */     }
/* 1365 */     AbstractEval.ScoreEval pcfgLL = null;
/* 1366 */     if (Boolean.parseBoolean(Test.evals.getProperty("pcfgLL"))) {
/* 1367 */       pcfgLL = new AbstractEval.ScoreEval("pcfgLL", runningAverages);
/*      */     }
/* 1369 */     AbstractEval.ScoreEval depLL = null;
/* 1370 */     if (Boolean.parseBoolean(Test.evals.getProperty("depLL"))) {
/* 1371 */       depLL = new AbstractEval.ScoreEval("depLL", runningAverages);
/*      */     }
/* 1373 */     AbstractEval.ScoreEval factLL = null;
/* 1374 */     if (Boolean.parseBoolean(Test.evals.getProperty("factLL"))) {
/* 1375 */       factLL = new AbstractEval.ScoreEval("factLL", runningAverages);
/*      */     }
/*      */     
/* 1378 */     AbstractEval kGoodLB = new LabeledConstituentEval("kGood LP/LR", false, tlp);
/*      */     
/*      */     TreeAnnotatorAndBinarizer binarizerOnly;
/*      */     TreeAnnotatorAndBinarizer binarizerOnly;
/* 1382 */     if (!Train.leftToRight) {
/* 1383 */       binarizerOnly = new TreeAnnotatorAndBinarizer(tlpParams, this.op.forceCNF, false, false);
/*      */     } else {
/* 1385 */       binarizerOnly = new TreeAnnotatorAndBinarizer(tlpParams.headFinder(), new LeftHeadFinder(), tlpParams, this.op.forceCNF, false, false);
/*      */     }
/* 1387 */     boolean saidMemMessage = false;
/* 1388 */     Timing timer = new Timing();
/* 1389 */     for (Tree goldTree : testTreebank) {
/* 1390 */       boolean outsideLengthBound = false;
/* 1391 */       Sentence s = getInputSentence(goldTree);
/* 1392 */       timer.start();
/* 1393 */       pwErr.println("Parsing [len. " + s.length() + "]: " + s);
/* 1394 */       Tree tree = null;
/*      */       try {
/* 1396 */         if (!parse(s)) {
/* 1397 */           pwErr.print("Sentence couldn't be parsed by grammar.");
/* 1398 */           if ((this.pparser != null) && (this.pparser.hasParse()) && (this.fallbackToPCFG)) {
/* 1399 */             pwErr.println("... falling back to PCFG parse.");
/* 1400 */             tree = getBestPCFGParse();
/*      */           } else {
/* 1402 */             pwErr.println();
/*      */           }
/*      */         } else {
/* 1405 */           tree = getBestParse();
/* 1406 */           if (this.bparser != null) pwErr.println("FactoredParser parse score is " + this.bparser.getBestScore());
/*      */         }
/*      */       } catch (OutOfMemoryError e) {
/* 1409 */         if (Test.maxLength != 559038737)
/*      */         {
/*      */ 
/* 1412 */           pwErr.print("NOT ENOUGH MEMORY TO PARSE SENTENCES OF LENGTH ");
/* 1413 */           pwErr.println(Test.maxLength);
/* 1414 */           throw e;
/*      */         }
/* 1416 */         if (!saidMemMessage) {
/* 1417 */           printOutOfMemory(pwErr);
/* 1418 */           saidMemMessage = true;
/*      */         }
/* 1420 */         if ((this.pparser.hasParse()) && (this.fallbackToPCFG)) {
/*      */           try {
/* 1422 */             String what = "dependency";
/* 1423 */             if (this.dparser.hasParse()) {
/* 1424 */               what = "factored";
/*      */             }
/* 1426 */             pwErr.println("Sentence too long for " + what + " parser.  Falling back to PCFG parse...");
/* 1427 */             tree = getBestPCFGParse();
/*      */           } catch (OutOfMemoryError oome) {
/* 1429 */             oome.printStackTrace();
/* 1430 */             pwErr.println("No memory to gather PCFG parse. Skipping...");
/* 1431 */             this.pparser.nudgeDownArraySize();
/*      */           }
/*      */         } else {
/* 1434 */           pwErr.println("Sentence has no parse using PCFG grammar (or no PCFG fallback).  Skipping...");
/*      */         }
/* 1436 */         pwErr.println();
/*      */       }
/*      */       catch (UnsupportedOperationException uoe) {
/* 1439 */         pwErr.println("Sentence too long (or zero words).");
/* 1440 */         outsideLengthBound = true;
/*      */       }
/*      */       
/* 1443 */       if (Test.verbose) {
/* 1444 */         pwOut.println("ComboParser best");
/* 1445 */         Tree ot = tree;
/* 1446 */         if ((ot != null) && (!tlpParams.treebankLanguagePack().isStartSymbol(ot.value()))) {
/* 1447 */           ot = ot.treeFactory().newTreeNode(tlpParams.treebankLanguagePack().startSymbol(), Collections.singletonList(ot));
/*      */         }
/* 1449 */         treePrint.printTree(ot, pwOut);
/*      */       } else {
/* 1451 */         treePrint.printTree(tree, pwOut); }
/*      */       Tree transGoldTree;
/* 1453 */       int ii; if (tree != null) {
/*      */         Tree transGoldTree;
/*      */         int iii;
/* 1456 */         if (Test.printAllBestParses) {
/* 1457 */           List<ScoredObject<Tree>> parses = this.pparser.getBestParses();
/* 1458 */           int sz = parses.size();
/* 1459 */           if (sz > 1) {
/* 1460 */             pwOut.println("There were " + sz + " best PCFG parses with score " + ((ScoredObject)parses.get(0)).score() + ".");
/* 1461 */             transGoldTree = tc.transformTree(goldTree);
/* 1462 */             iii = 0;
/* 1463 */             for (ScoredObject<Tree> sot : parses) {
/* 1464 */               iii++;
/* 1465 */               Tree tb = (Tree)sot.object();
/* 1466 */               Tree tbd = this.debinarizer.transformTree(tb);
/* 1467 */               tbd = this.subcategoryStripper.transformTree(tbd);
/* 1468 */               pwOut.println("PCFG Parse #" + iii + " with score " + tbd.score());
/* 1469 */               tbd.pennPrint(pwOut);
/* 1470 */               Tree tbtr = tc.transformTree(tbd);
/*      */               
/* 1472 */               kGoodLB.evaluate(tbtr, transGoldTree, pwErr);
/*      */             } } }
/*      */         Tree transGoldTree;
/*      */         int i;
/* 1476 */         if (Test.printPCFGkBest > 0) {
/* 1477 */           List<ScoredObject<Tree>> trees = getKBestPCFGParses(Test.printPCFGkBest);
/* 1478 */           transGoldTree = tc.transformTree(goldTree);
/* 1479 */           i = 0;
/* 1480 */           for (ScoredObject<Tree> tp : trees) {
/* 1481 */             i++;
/* 1482 */             pwOut.println("PCFG Parse #" + i + " with score " + tp.score());
/* 1483 */             Tree tbd = (Tree)tp.object();
/* 1484 */             tbd.pennPrint(pwOut);
/* 1485 */             Tree tbtr = tc.transformTree(tbd);
/* 1486 */             kGoodLB.evaluate(tbtr, transGoldTree, pwErr);
/*      */           }
/*      */         }
/* 1489 */         if ((Test.printFactoredKGood > 0) && (this.bparser.hasParse()))
/*      */         {
/* 1491 */           List<ScoredObject<Tree>> trees = getKGoodFactoredParses(Test.printFactoredKGood);
/* 1492 */           transGoldTree = tc.transformTree(goldTree);
/* 1493 */           ii = 0;
/* 1494 */           for (ScoredObject<Tree> tp : trees) {
/* 1495 */             ii++;
/* 1496 */             pwOut.println("Factored Parse #" + ii + " with score " + tp.score());
/* 1497 */             Tree tbd = (Tree)tp.object();
/* 1498 */             tbd.pennPrint(pwOut);
/* 1499 */             Tree tbtr = tc.transformTree(tbd);
/* 1500 */             kGoodLB.evaluate(tbtr, transGoldTree, pwOut);
/*      */           }
/*      */         }
/*      */       }
/* 1504 */       if ((Test.verbose) && (!outsideLengthBound)) {
/* 1505 */         pwOut.println("Correct parse");
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1510 */         treePrint.printTree(goldTree, pwOut);
/*      */       }
/* 1512 */       timer.report("Parsing Sentence");
/* 1513 */       if (tree != null) {
/* 1514 */         Tree transGoldTree = tc.transformTree(goldTree);
/* 1515 */         if (transGoldTree == null) {
/* 1516 */           pwErr.println("Couldn't transform gold tree for evaluation, skipping eval. Gold tree was:");
/* 1517 */           goldTree.pennPrint(pwErr);
/* 1518 */           continue;
/*      */         }
/* 1520 */         Tree treePCFG = getBestPCFGParse();
/* 1521 */         if (treePCFG != null) {
/* 1522 */           Tree treePCFGeval = tc.transformTree(treePCFG);
/* 1523 */           if (pcfgLB != null) {
/* 1524 */             pcfgLB.evaluate(treePCFGeval, transGoldTree, pwErr);
/*      */           }
/* 1526 */           if (pcfgCB != null) {
/* 1527 */             pcfgCB.evaluate(treePCFGeval, transGoldTree, pwErr);
/*      */           }
/* 1529 */           if (pcfgDA != null)
/*      */           {
/* 1531 */             Tree pcfgTreeB = this.pparser.getBestParse();
/* 1532 */             Tree cwtPCFGTreeB = pcfgTreeB.deeperCopy(new LabeledScoredTreeFactory(), new CategoryWordTagFactory());
/*      */             
/* 1534 */             cwtPCFGTreeB.percolateHeads(tlpParams.headFinder());
/* 1535 */             Tree goldTreeB = binarizerOnly.transformTree(goldTree);
/* 1536 */             pcfgDA.evaluate(cwtPCFGTreeB, goldTreeB, pwErr);
/*      */           }
/* 1538 */           if (pcfgTA != null) {
/* 1539 */             pcfgTA.evaluate(treePCFGeval, transGoldTree, pwErr);
/*      */           }
/* 1541 */           if ((pcfgLL != null) && (this.pparser != null)) {
/* 1542 */             pcfgLL.recordScore(this.pparser, pwErr);
/*      */           }
/* 1544 */           if (pcfgRUO != null) {
/* 1545 */             pcfgRUO.evaluate(treePCFGeval, transGoldTree, pwErr);
/*      */           }
/* 1547 */           if (pcfgCUO != null) {
/* 1548 */             pcfgCUO.evaluate(treePCFGeval, transGoldTree, pwErr);
/*      */           }
/* 1550 */           if (pcfgCatE != null) {
/* 1551 */             pcfgCatE.evaluate(treePCFGeval, transGoldTree, pwErr);
/*      */           }
/*      */         }
/*      */         
/* 1555 */         Tree treeDep = getBestDependencyParse();
/* 1556 */         if (treeDep != null) {
/* 1557 */           Tree goldTreeB = binarizerOnly.transformTree(goldTree);
/* 1558 */           if (depDA != null) {
/* 1559 */             depDA.evaluate(treeDep, goldTreeB, pwErr);
/*      */           }
/* 1561 */           if (depTA != null) {
/* 1562 */             Tree undoneTree = this.debinarizer.transformTree(treeDep);
/* 1563 */             undoneTree = this.subcategoryStripper.transformTree(undoneTree);
/*      */             
/* 1565 */             depTA.evaluate(undoneTree, goldTree, pwErr);
/*      */           }
/* 1567 */           if ((depLL != null) && (this.dparser != null))
/* 1568 */             depLL.recordScore(this.dparser, pwErr);
/*      */           Tree factTreeB;
/*      */           Tree factTreeB;
/* 1571 */           if ((this.bparser != null) && (parseSucceeded)) {
/* 1572 */             factTreeB = this.bparser.getBestParse();
/*      */           } else {
/* 1574 */             factTreeB = treeDep;
/*      */           }
/* 1576 */           if (factDA != null) {
/* 1577 */             factDA.evaluate(factTreeB, goldTreeB, pwErr);
/*      */           }
/*      */         }
/* 1580 */         if (factLB != null) {
/* 1581 */           factLB.evaluate(tc.transformTree(tree), transGoldTree, pwErr);
/*      */         }
/* 1583 */         if (factTA != null)
/*      */         {
/* 1585 */           factTA.evaluate(tree, br.transformTree(goldTree), pwErr);
/*      */         }
/* 1587 */         if ((factLL != null) && (this.bparser != null)) {
/* 1588 */           factLL.recordScore(this.bparser, pwErr);
/*      */         }
/* 1590 */         if (factCB != null) {
/* 1591 */           factCB.evaluate(tc.transformTree(tree), transGoldTree, pwErr);
/*      */         }
/* 1593 */         if (Test.evalb)
/*      */         {
/* 1595 */           nanScores(tree);
/* 1596 */           EvalB.writeEVALBline(goldTree, tree);
/*      */         }
/*      */       }
/* 1599 */       pwErr.println();
/*      */     }
/* 1601 */     treebankTotalTtimer.done("Testing on treebank");
/* 1602 */     if (saidMemMessage) {
/* 1603 */       printOutOfMemory(pwErr);
/*      */     }
/* 1605 */     if (Test.evalb) {
/* 1606 */       EvalB.closeEVALBfiles();
/*      */     }
/* 1608 */     if (summary) {
/* 1609 */       if (pcfgLB != null) pcfgLB.display(false, pwErr);
/* 1610 */       if (pcfgCB != null) pcfgCB.display(false, pwErr);
/* 1611 */       if (pcfgDA != null) pcfgDA.display(false, pwErr);
/* 1612 */       if (pcfgTA != null) pcfgTA.display(false, pwErr);
/* 1613 */       if ((pcfgLL != null) && (this.pparser != null)) pcfgLL.display(false, pwErr);
/* 1614 */       if (depDA != null) depDA.display(false, pwErr);
/* 1615 */       if (depTA != null) depTA.display(false, pwErr);
/* 1616 */       if ((depLL != null) && (this.dparser != null)) depLL.display(false, pwErr);
/* 1617 */       if (factLB != null) factLB.display(false, pwErr);
/* 1618 */       if (factCB != null) factCB.display(false, pwErr);
/* 1619 */       if (factDA != null) factDA.display(false, pwErr);
/* 1620 */       if (factTA != null) factTA.display(false, pwErr);
/* 1621 */       if ((factLL != null) && (this.bparser != null)) factLL.display(false, pwErr);
/* 1622 */       if (pcfgCatE != null) { pcfgCatE.display(false, pwErr);
/*      */       }
/*      */     }
/* 1625 */     if (pcfgRUO != null) pcfgRUO.display(true, pwErr);
/* 1626 */     if (pcfgCUO != null) pcfgCUO.display(true, pwErr);
/* 1627 */     if (tsv) {
/* 1628 */       NumberFormat nf = new DecimalFormat("0.00");
/* 1629 */       pwErr.println("factF1\tfactDA\tfactEx\tpcfgF1\tdepDA\tfactTA\tnum");
/* 1630 */       if (factLB != null) pwErr.print(nf.format(factLB.getEvalbF1Percent()));
/* 1631 */       pwErr.print("\t");
/* 1632 */       if ((this.dparser != null) && (factDA != null)) pwErr.print(nf.format(factDA.getEvalbF1Percent()));
/* 1633 */       pwErr.print("\t");
/* 1634 */       if (factLB != null) pwErr.print(nf.format(factLB.getExactPercent()));
/* 1635 */       pwErr.print("\t");
/* 1636 */       if (pcfgLB != null) pwErr.print(nf.format(pcfgLB.getEvalbF1Percent()));
/* 1637 */       pwErr.print("\t");
/* 1638 */       if ((this.dparser != null) && (depDA != null)) pwErr.print(nf.format(depDA.getEvalbF1Percent()));
/* 1639 */       pwErr.print("\t");
/* 1640 */       if (factTA != null) pwErr.print(nf.format(factTA.getEvalbF1Percent()));
/* 1641 */       pwErr.print("\t");
/* 1642 */       if (factLB != null) pwErr.print(factLB.getNum());
/* 1643 */       pwErr.println();
/*      */     }
/*      */     
/* 1646 */     double f1 = 0.0D;
/* 1647 */     if (factLB != null) {
/* 1648 */       f1 = factLB.getEvalbF1();
/*      */     }
/* 1650 */     return f1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void nanScores(Tree tree)
/*      */   {
/* 1657 */     tree.setScore(NaN.0D);
/* 1658 */     Tree[] kids = tree.children();
/* 1659 */     for (int i = 0; i < kids.length; i++) {
/* 1660 */       nanScores(kids[i]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseFiles(String[] args, int argIndex, boolean tokenized, TokenizerFactory tokenizerFactory, DocumentPreprocessor documentPreprocessor, String elementDelimiter, String sentenceDelimiter, Function<List<HasWord>, List<HasWord>> escaper, int tagDelimiter)
/*      */   {
/* 1669 */     PrintWriter pwOut = this.op.tlpParams.pw();
/* 1670 */     PrintWriter pwErr = this.op.tlpParams.pw(System.err);
/* 1671 */     TreePrint treePrint = getTreePrint();
/* 1672 */     int numWords = 0;
/* 1673 */     int numSents = 0;
/* 1674 */     int numUnparsable = 0;
/* 1675 */     int numNoMemory = 0;
/* 1676 */     int numFallback = 0;
/* 1677 */     int numSkipped = 0;
/* 1678 */     Timing timer = new Timing();
/* 1679 */     TreebankLanguagePack tlp = this.op.tlpParams.treebankLanguagePack();
/*      */     
/* 1681 */     if (tokenized) {
/* 1682 */       tokenizerFactory = WhitespaceTokenizer.factory();
/*      */     }
/* 1684 */     if (tokenizerFactory == null) {
/* 1685 */       tokenizerFactory = tlp.getTokenizerFactory();
/*      */     }
/* 1687 */     if (Test.verbose) {
/* 1688 */       System.err.println("parseFiles: Tokenizer factory is: " + tokenizerFactory);
/* 1689 */       System.err.println("Sentence final words are: " + Arrays.asList(tlp.sentenceFinalPunctuationWords()));
/* 1690 */       System.err.println("File encoding is: " + this.op.tlpParams.getInputEncoding());
/*      */     }
/* 1692 */     documentPreprocessor.setTokenizerFactory(tokenizerFactory);
/* 1693 */     documentPreprocessor.setSentenceFinalPuncWords(tlp.sentenceFinalPunctuationWords());
/* 1694 */     documentPreprocessor.setEncoding(this.op.tlpParams.getInputEncoding());
/* 1695 */     boolean saidMemMessage = false;
/*      */     
/*      */ 
/* 1698 */     boolean runningAverages = Boolean.parseBoolean(Test.evals.getProperty("runningAverages"));
/* 1699 */     boolean summary = Boolean.parseBoolean(Test.evals.getProperty("summary"));
/* 1700 */     AbstractEval.ScoreEval pcfgLL = null;
/* 1701 */     if (Boolean.parseBoolean(Test.evals.getProperty("pcfgLL"))) {
/* 1702 */       pcfgLL = new AbstractEval.ScoreEval("pcfgLL", runningAverages);
/*      */     }
/* 1704 */     AbstractEval.ScoreEval depLL = null;
/* 1705 */     if (Boolean.parseBoolean(Test.evals.getProperty("depLL"))) {
/* 1706 */       depLL = new AbstractEval.ScoreEval("depLL", runningAverages);
/*      */     }
/* 1708 */     AbstractEval.ScoreEval factLL = null;
/* 1709 */     if (Boolean.parseBoolean(Test.evals.getProperty("factLL"))) {
/* 1710 */       factLL = new AbstractEval.ScoreEval("factLL", runningAverages);
/*      */     }
/*      */     
/* 1713 */     timer.start();
/* 1714 */     for (int i = argIndex; i < args.length; i++) {
/* 1715 */       String filename = args[i];
/*      */       try { List<List<? extends HasWord>> document;
/*      */         List<List<? extends HasWord>> document;
/* 1718 */         if (elementDelimiter != null) {
/* 1719 */           document = documentPreprocessor.getSentencesFromXML(filename, escaper, elementDelimiter, sentenceDelimiter);
/*      */         } else {
/* 1721 */           document = documentPreprocessor.getSentencesFromText(filename, escaper, sentenceDelimiter, tagDelimiter);
/*      */         }
/* 1723 */         System.err.println("Parsing file: " + filename + " with " + document.size() + " sentences.");
/* 1724 */         PrintWriter pwo = pwOut;
/* 1725 */         if (Test.writeOutputFiles) {
/* 1726 */           String ext = Test.outputFilesExtension == null ? "stp" : Test.outputFilesExtension;
/*      */           
/* 1728 */           String fname = filename + "." + ext;
/* 1729 */           if (Test.outputFilesDirectory != null) {
/* 1730 */             String fseparator = System.getProperty("file.separator");
/* 1731 */             if ((fseparator == null) || ("".equals(fseparator))) {
/* 1732 */               fseparator = "/";
/*      */             }
/* 1734 */             int ind = fname.lastIndexOf(fseparator);
/* 1735 */             fname = fname.substring(ind + 1);
/* 1736 */             if (!"".equals(Test.outputFilesDirectory)) {
/* 1737 */               fname = Test.outputFilesDirectory + fseparator + fname;
/*      */             }
/*      */           }
/*      */           try {
/* 1741 */             pwo = this.op.tlpParams.pw(new FileOutputStream(fname));
/*      */           } catch (IOException ioe) {
/* 1743 */             ioe.printStackTrace();
/*      */           }
/*      */         }
/* 1746 */         treePrint.printHeader(pwo, this.op.tlpParams.getOutputEncoding());
/* 1747 */         int num = 0;
/* 1748 */         for (List sentence : document) {
/* 1749 */           num++;
/* 1750 */           numSents++;
/* 1751 */           int len = sentence.size();
/* 1752 */           numWords += len;
/* 1753 */           pwErr.println("Parsing [sent. " + num + " len. " + len + "]: " + sentence);
/*      */           
/*      */ 
/*      */ 
/* 1757 */           Tree ansTree = null;
/*      */           try {
/* 1759 */             if (!parse(sentence)) {
/* 1760 */               pwErr.print("Sentence couldn't be parsed by grammar.");
/* 1761 */               if ((this.pparser != null) && (this.pparser.hasParse()) && (this.fallbackToPCFG)) {
/* 1762 */                 pwErr.println("... falling back to PCFG parse.");
/* 1763 */                 ansTree = getBestPCFGParse();
/* 1764 */                 numFallback++;
/*      */               } else {
/* 1766 */                 pwErr.println();
/* 1767 */                 numUnparsable++;
/*      */               }
/*      */             }
/*      */             else {
/* 1771 */               ansTree = getBestParse();
/*      */             }
/* 1773 */             if ((pcfgLL != null) && (this.pparser != null)) {
/* 1774 */               pcfgLL.recordScore(this.pparser, pwErr);
/*      */             }
/* 1776 */             if ((depLL != null) && (this.dparser != null)) {
/* 1777 */               depLL.recordScore(this.dparser, pwErr);
/*      */             }
/* 1779 */             if ((factLL != null) && (this.bparser != null)) {
/* 1780 */               factLL.recordScore(this.bparser, pwErr);
/*      */             }
/*      */           } catch (OutOfMemoryError e) {
/* 1783 */             if (Test.maxLength != 559038737)
/*      */             {
/* 1785 */               pwErr.println("NOT ENOUGH MEMORY TO PARSE SENTENCES OF LENGTH " + Test.maxLength);
/* 1786 */               pwo.println("NOT ENOUGH MEMORY TO PARSE SENTENCES OF LENGTH " + Test.maxLength);
/* 1787 */               throw e;
/*      */             }
/* 1789 */             if (!saidMemMessage) {
/* 1790 */               printOutOfMemory(pwErr);
/* 1791 */               saidMemMessage = true;
/*      */             }
/* 1793 */             if ((this.pparser.hasParse()) && (this.fallbackToPCFG)) {
/*      */               try {
/* 1795 */                 String what = "dependency";
/* 1796 */                 if (this.dparser.hasParse()) {
/* 1797 */                   what = "factored";
/*      */                 }
/* 1799 */                 pwErr.println("Sentence too long for " + what + " parser.  Falling back to PCFG parse...");
/* 1800 */                 ansTree = getBestPCFGParse();
/* 1801 */                 numFallback++;
/*      */               } catch (OutOfMemoryError oome) {
/* 1803 */                 oome.printStackTrace();
/* 1804 */                 numNoMemory++;
/* 1805 */                 pwErr.println("No memory to gather PCFG parse. Skipping...");
/* 1806 */                 pwo.println("Sentence skipped:  no PCFG fallback.");
/* 1807 */                 this.pparser.nudgeDownArraySize();
/*      */               }
/*      */             } else {
/* 1810 */               pwErr.println("Sentence has no parse using PCFG grammar (or no PCFG fallback).  Skipping...");
/* 1811 */               pwo.println("Sentence skipped: no PCFG fallback.");
/* 1812 */               numSkipped++;
/*      */             }
/*      */           }
/*      */           catch (UnsupportedOperationException uoe) {
/* 1816 */             pwErr.println("Sentence too long (or zero words).");
/* 1817 */             pwo.println("Sentence skipped: too long (or zero words).");
/* 1818 */             numWords -= len;
/* 1819 */             numSkipped++;
/*      */           }
/*      */           try {
/* 1822 */             treePrint.printTree(ansTree, Integer.toString(num), pwo);
/*      */           } catch (RuntimeException re) {
/* 1824 */             pwErr.println("TreePrint.printTree skipped: out of memory");
/* 1825 */             re.printStackTrace();
/* 1826 */             numNoMemory++;
/*      */             try {
/* 1828 */               treePrint.printTree(null, Integer.toString(num), pwo);
/*      */             } catch (Exception e) {
/* 1830 */               pwo.println("Sentence skipped: out of memory and error calling TreePrint.");
/* 1831 */               e.printStackTrace();
/*      */             }
/*      */           }
/*      */           
/* 1835 */           if ((Test.printPCFGkBest > 0) && (this.pparser.hasParse())) {
/* 1836 */             List<ScoredObject<Tree>> trees = getKBestPCFGParses(Test.printPCFGkBest);
/* 1837 */             treePrint.printTrees(trees, Integer.toString(num), pwo);
/* 1838 */           } else if ((Test.printFactoredKGood > 0) && (this.bparser.hasParse()))
/*      */           {
/* 1840 */             List<ScoredObject<Tree>> trees = getKGoodFactoredParses(Test.printFactoredKGood);
/* 1841 */             treePrint.printTrees(trees, Integer.toString(num), pwo);
/*      */           }
/*      */         }
/* 1844 */         treePrint.printFooter(pwo);
/* 1845 */         if (Test.writeOutputFiles) {
/* 1846 */           pwo.close();
/*      */         }
/* 1848 */         System.err.println("Parsed file: " + filename + " [" + num + " sentences].");
/*      */       }
/*      */       catch (IOException e) {
/* 1851 */         pwErr.println("ERROR: Couldn't open file: " + filename);
/*      */       }
/*      */     }
/* 1854 */     long millis = timer.stop();
/*      */     
/* 1856 */     if (summary) {
/* 1857 */       if (pcfgLL != null) pcfgLL.display(false, pwErr);
/* 1858 */       if (depLL != null) depLL.display(false, pwErr);
/* 1859 */       if (factLL != null) { factLL.display(false, pwErr);
/*      */       }
/*      */     }
/* 1862 */     if (saidMemMessage) {
/* 1863 */       printOutOfMemory(pwErr);
/*      */     }
/* 1865 */     double wordspersec = numWords / (millis / 1000.0D);
/* 1866 */     double sentspersec = numSents / (millis / 1000.0D);
/* 1867 */     NumberFormat nf = new DecimalFormat("0.00");
/* 1868 */     pwErr.println("Parsed " + numWords + " words in " + numSents + " sentences (" + nf.format(wordspersec) + " wds/sec; " + nf.format(sentspersec) + " sents/sec).");
/*      */     
/*      */ 
/* 1871 */     if (numFallback > 0) {
/* 1872 */       pwErr.println("  " + numFallback + " sentences were parsed by fallback to PCFG.");
/*      */     }
/* 1874 */     if ((numUnparsable > 0) || (numNoMemory > 0) || (numSkipped > 0)) {
/* 1875 */       pwErr.println("  " + (numUnparsable + numNoMemory + numSkipped) + " sentences were not parsed:");
/* 1876 */       if (numUnparsable > 0) {
/* 1877 */         pwErr.println("    " + numUnparsable + " were not parsable with non-zero probability.");
/*      */       }
/* 1879 */       if (numNoMemory > 0) {
/* 1880 */         pwErr.println("    " + numNoMemory + " were skipped because of insufficient memory.");
/*      */       }
/* 1882 */       if (numSkipped > 0) {
/* 1883 */         pwErr.println("    " + numSkipped + " were skipped as length 0 or greater than " + Test.maxLength);
/*      */       }
/*      */     }
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
/*      */   public void setOptionFlags(String... flags)
/*      */   {
/* 1917 */     this.op.setOptions(flags);
/*      */   }
/*      */   
/*      */   private static void printArgs(String[] args, PrintStream ps)
/*      */   {
/* 1922 */     ps.print("LexicalizedParser invoked with arguments:");
/* 1923 */     for (String arg : args) {
/* 1924 */       ps.print(" " + arg);
/*      */     }
/* 1926 */     ps.println();
/*      */   }
/*      */   
/* 1929 */   private static int trainLengthLimit = 100000;
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
/*      */   public static void main(String[] args)
/*      */   {
/* 2084 */     boolean train = false;
/* 2085 */     boolean saveToSerializedFile = false;
/* 2086 */     boolean saveToTextFile = false;
/* 2087 */     String serializedInputFileOrUrl = null;
/* 2088 */     String textInputFileOrUrl = null;
/* 2089 */     String serializedOutputFileOrUrl = null;
/* 2090 */     String textOutputFileOrUrl = null;
/* 2091 */     String treebankPath = null;
/* 2092 */     Treebank testTreebank = null;
/* 2093 */     Treebank tuneTreebank = null;
/* 2094 */     String testPath = null;
/* 2095 */     FileFilter testFilter = null;
/* 2096 */     String tunePath = null;
/* 2097 */     FileFilter tuneFilter = null;
/* 2098 */     FileFilter trainFilter = null;
/* 2099 */     String secondaryTreebankPath = null;
/* 2100 */     double secondaryTreebankWeight = 1.0D;
/* 2101 */     FileFilter secondaryTrainFilter = null;
/*      */     
/*      */ 
/* 2104 */     TokenizerFactory tokenizerFactory = null;
/* 2105 */     DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor();
/* 2106 */     boolean tokenized = false;
/* 2107 */     Function<List<HasWord>, List<HasWord>> escaper = null;
/* 2108 */     int tagDelimiter = -1;
/* 2109 */     String sentenceDelimiter = null;
/* 2110 */     String elementDelimiter = null;
/* 2111 */     int argIndex = 0;
/* 2112 */     if (args.length < 1) {
/* 2113 */       System.err.println("usage: java edu.stanford.nlp.parser.lexparser.LexicalizedParser parserFileOrUrl filename*");
/* 2114 */       System.exit(1);
/*      */     }
/*      */     
/* 2117 */     Options op = new Options();
/* 2118 */     String encoding = null;
/*      */     
/* 2120 */     while ((argIndex < args.length) && (args[argIndex].charAt(0) == '-')) {
/* 2121 */       if ((args[argIndex].equalsIgnoreCase("-train")) || (args[argIndex].equalsIgnoreCase("-trainTreebank")))
/*      */       {
/* 2123 */         train = true;
/* 2124 */         int numSubArgs = numSubArgs(args, argIndex);
/* 2125 */         argIndex++;
/* 2126 */         if (numSubArgs >= 1) {
/* 2127 */           treebankPath = args[argIndex];
/* 2128 */           argIndex++;
/*      */         } else {
/* 2130 */           throw new RuntimeException("Error: -train option must have treebankPath as first argument.");
/*      */         }
/* 2132 */         if (numSubArgs == 2) {
/* 2133 */           trainFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/* 2134 */         } else if (numSubArgs >= 3) {
/*      */           try {
/* 2136 */             int low = Integer.parseInt(args[argIndex]);
/* 2137 */             int high = Integer.parseInt(args[(argIndex + 1)]);
/* 2138 */             trainFilter = new NumberRangeFileFilter(low, high, true);
/* 2139 */             argIndex += 2;
/*      */           }
/*      */           catch (NumberFormatException e) {
/* 2142 */             trainFilter = new NumberRangesFileFilter(args[argIndex], true);
/* 2143 */             argIndex++;
/*      */           }
/*      */         }
/* 2146 */       } else if (args[argIndex].equalsIgnoreCase("-train2"))
/*      */       {
/* 2148 */         int numSubArgs = numSubArgs(args, argIndex);
/* 2149 */         argIndex++;
/* 2150 */         if (numSubArgs < 3) {
/* 2151 */           throw new RuntimeException("Error: -train2 <treebankPath> <ranges> <weight>.");
/*      */         }
/* 2153 */         secondaryTreebankPath = args[(argIndex++)];
/* 2154 */         secondaryTrainFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/* 2155 */         secondaryTreebankWeight = Double.parseDouble(args[(argIndex++)]);
/* 2156 */       } else if ((args[argIndex].equalsIgnoreCase("-tLPP")) && (argIndex + 1 < args.length)) {
/*      */         try {
/* 2158 */           op.tlpParams = ((TreebankLangParserParams)Class.forName(args[(argIndex + 1)]).newInstance());
/*      */         } catch (ClassNotFoundException e) {
/* 2160 */           System.err.println("Class not found: " + args[(argIndex + 1)]);
/*      */         } catch (InstantiationException e) {
/* 2162 */           System.err.println("Couldn't instantiate: " + args[(argIndex + 1)] + ": " + e.toString());
/*      */         } catch (IllegalAccessException e) {
/* 2164 */           System.err.println("Illegal access" + e);
/*      */         }
/* 2166 */         argIndex += 2;
/* 2167 */       } else if (args[argIndex].equalsIgnoreCase("-encoding"))
/*      */       {
/*      */ 
/* 2170 */         encoding = args[(argIndex + 1)];
/* 2171 */         op.tlpParams.setInputEncoding(encoding);
/* 2172 */         op.tlpParams.setOutputEncoding(encoding);
/* 2173 */         argIndex += 2;
/* 2174 */       } else if (args[argIndex].equalsIgnoreCase("-useBasicCategoryTagsInDependencyGrammar")) {
/* 2175 */         basicCategoryTagsInDependencyGrammar = true;
/* 2176 */         argIndex++;
/* 2177 */       } else if (args[argIndex].equalsIgnoreCase("-tokenized")) {
/* 2178 */         tokenized = true;
/* 2179 */         argIndex++;
/* 2180 */       } else if (args[argIndex].equalsIgnoreCase("-escaper")) {
/*      */         try {
/* 2182 */           escaper = (Function)Class.forName(args[(argIndex + 1)]).newInstance();
/*      */         } catch (Exception e) {
/* 2184 */           System.err.println("Couldn't instantiate escaper " + args[(argIndex + 1)] + ": " + e);
/*      */         }
/* 2186 */         argIndex += 2;
/* 2187 */       } else if (args[argIndex].equalsIgnoreCase("-tokenizerFactory")) {
/*      */         try {
/* 2189 */           tokenizerFactory = (TokenizerFactory)Class.forName(args[(argIndex + 1)]).newInstance();
/*      */         } catch (Exception e) {
/* 2191 */           System.err.println("Couldn't instantiate TokenizerFactory " + args[(argIndex + 1)]);
/*      */         }
/* 2193 */         argIndex += 2;
/* 2194 */       } else if (args[argIndex].equalsIgnoreCase("-sentences")) {
/* 2195 */         sentenceDelimiter = args[(argIndex + 1)];
/* 2196 */         if (sentenceDelimiter.equalsIgnoreCase("newline")) {
/* 2197 */           sentenceDelimiter = "\n";
/*      */         }
/* 2199 */         argIndex += 2;
/* 2200 */       } else if (args[argIndex].equalsIgnoreCase("-parseInside")) {
/* 2201 */         elementDelimiter = args[(argIndex + 1)];
/* 2202 */         argIndex += 2;
/* 2203 */       } else if (args[argIndex].equalsIgnoreCase("-tagSeparator")) {
/* 2204 */         tagDelimiter = args[(argIndex + 1)].charAt(0);
/* 2205 */         argIndex += 2;
/* 2206 */       } else if (args[argIndex].equalsIgnoreCase("-loadFromSerializedFile"))
/*      */       {
/*      */ 
/* 2209 */         serializedInputFileOrUrl = args[(argIndex + 1)];
/* 2210 */         argIndex += 2;
/* 2211 */       } else if (args[argIndex].equalsIgnoreCase("-loadFromTextFile"))
/*      */       {
/*      */ 
/* 2214 */         textInputFileOrUrl = args[(argIndex + 1)];
/* 2215 */         argIndex += 2;
/* 2216 */       } else if (args[argIndex].equalsIgnoreCase("-saveToSerializedFile")) {
/* 2217 */         saveToSerializedFile = true;
/* 2218 */         if (numSubArgs(args, argIndex) < 1) {
/* 2219 */           System.err.println("Missing path: -saveToSerialized filename");
/*      */         } else {
/* 2221 */           serializedOutputFileOrUrl = args[(argIndex + 1)];
/*      */         }
/* 2223 */         argIndex += 2;
/* 2224 */       } else if (args[argIndex].equalsIgnoreCase("-saveToTextFile"))
/*      */       {
/* 2226 */         saveToTextFile = true;
/* 2227 */         textOutputFileOrUrl = args[(argIndex + 1)];
/* 2228 */         argIndex += 2;
/* 2229 */       } else if (args[argIndex].equalsIgnoreCase("-saveTrainTrees"))
/*      */       {
/* 2231 */         trainTreeFile = args[(argIndex + 1)];
/* 2232 */         argIndex += 2;
/* 2233 */       } else if (args[argIndex].equalsIgnoreCase("-trainLength"))
/*      */       {
/* 2235 */         trainLengthLimit = Integer.parseInt(args[(argIndex + 1)]);
/* 2236 */         argIndex += 2;
/* 2237 */       } else if (args[argIndex].equalsIgnoreCase("-lengthNormalization")) {
/* 2238 */         Test.lengthNormalization = true;
/* 2239 */         argIndex++;
/* 2240 */       } else if ((args[argIndex].equalsIgnoreCase("-treebank")) || (args[argIndex].equalsIgnoreCase("-testTreebank")) || (args[argIndex].equalsIgnoreCase("-test")))
/*      */       {
/*      */ 
/*      */ 
/* 2244 */         int numSubArgs = numSubArgs(args, argIndex);
/* 2245 */         if ((numSubArgs > 0) && (numSubArgs < 3)) {
/* 2246 */           argIndex++;
/* 2247 */           testPath = args[(argIndex++)];
/* 2248 */           if (numSubArgs == 2) {
/* 2249 */             testFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/* 2250 */           } else if (numSubArgs == 3) {
/*      */             try {
/* 2252 */               int low = Integer.parseInt(args[argIndex]);
/* 2253 */               int high = Integer.parseInt(args[(argIndex + 1)]);
/* 2254 */               testFilter = new NumberRangeFileFilter(low, high, true);
/* 2255 */               argIndex += 2;
/*      */             }
/*      */             catch (NumberFormatException e) {
/* 2258 */               testFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/*      */             }
/*      */           }
/*      */         } else {
/* 2262 */           throw new IllegalArgumentException("Bad arguments after -testTreebank");
/*      */         }
/* 2264 */       } else if (args[argIndex].equalsIgnoreCase("-tune"))
/*      */       {
/* 2266 */         int numSubArgs = numSubArgs(args, argIndex);
/* 2267 */         argIndex++;
/* 2268 */         if (numSubArgs == 1) {
/* 2269 */           tuneFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/* 2270 */         } else if (numSubArgs > 1) {
/* 2271 */           tunePath = args[(argIndex++)];
/* 2272 */           if (numSubArgs == 2) {
/* 2273 */             tuneFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/* 2274 */           } else if (numSubArgs >= 3) {
/*      */             try {
/* 2276 */               int low = Integer.parseInt(args[argIndex]);
/* 2277 */               int high = Integer.parseInt(args[(argIndex + 1)]);
/* 2278 */               tuneFilter = new NumberRangeFileFilter(low, high, true);
/* 2279 */               argIndex += 2;
/*      */             }
/*      */             catch (NumberFormatException e) {
/* 2282 */               tuneFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/*      */             }
/*      */           }
/*      */         }
/*      */       } else {
/* 2287 */         argIndex = op.setOptionOrWarn(args, argIndex);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2294 */     if (tuneFilter != null) {
/* 2295 */       if (tunePath == null) {
/* 2296 */         if (treebankPath == null) {
/* 2297 */           throw new RuntimeException("No tune treebank path specified...");
/*      */         }
/* 2299 */         System.err.println("No tune treebank path specified.  Using train path: \"" + treebankPath + "\"");
/* 2300 */         tunePath = treebankPath;
/*      */       }
/*      */       
/* 2303 */       tuneTreebank = op.tlpParams.testMemoryTreebank();
/* 2304 */       tuneTreebank.loadPath(tunePath, tuneFilter);
/*      */     }
/*      */     
/* 2307 */     if ((!train) && (Test.verbose)) {
/* 2308 */       System.err.println("Currently " + new Date());
/* 2309 */       printArgs(args, System.err);
/*      */     }
/* 2311 */     LexicalizedParser lp = null;
/* 2312 */     if (train) {
/* 2313 */       printArgs(args, System.err);
/*      */       
/* 2315 */       GrammarCompactor compactor = null;
/* 2316 */       if (Train.compactGrammar() == 3) {
/* 2317 */         compactor = new ExactGrammarCompactor(false, false);
/*      */       }
/* 2319 */       Treebank trainTreebank = makeTreebank(treebankPath, op, trainFilter);
/* 2320 */       if (secondaryTreebankPath != null) {
/* 2321 */         DiskTreebank secondaryTrainTreebank = makeSecondaryTreebank(secondaryTreebankPath, op, secondaryTrainFilter);
/* 2322 */         lp = new LexicalizedParser(trainTreebank, secondaryTrainTreebank, secondaryTreebankWeight, compactor, op);
/*      */       } else {
/* 2324 */         lp = new LexicalizedParser(trainTreebank, compactor, op, tuneTreebank);
/*      */       }
/* 2326 */     } else if (textInputFileOrUrl != null)
/*      */     {
/* 2328 */       lp = new LexicalizedParser(textInputFileOrUrl, true, op);
/*      */     }
/*      */     else {
/* 2331 */       if ((serializedInputFileOrUrl == null) && (argIndex < args.length))
/*      */       {
/* 2333 */         serializedInputFileOrUrl = args[argIndex];
/* 2334 */         argIndex++;
/*      */       }
/* 2336 */       if (serializedInputFileOrUrl == null) {
/* 2337 */         System.err.println("No grammar specified, exiting...");
/* 2338 */         System.exit(0);
/*      */       }
/*      */       try {
/* 2341 */         lp = new LexicalizedParser(serializedInputFileOrUrl, op);
/* 2342 */         op = lp.op;
/*      */       } catch (IllegalArgumentException e) {
/* 2344 */         System.err.println("Error loading parser, exiting...");
/* 2345 */         System.exit(0);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2353 */     if (encoding != null) {
/* 2354 */       op.tlpParams.setInputEncoding(encoding);
/* 2355 */       op.tlpParams.setOutputEncoding(encoding);
/*      */     }
/*      */     
/* 2358 */     if ((testFilter != null) || (testPath != null)) {
/* 2359 */       if (testPath == null) {
/* 2360 */         if (treebankPath == null) {
/* 2361 */           throw new RuntimeException("No test treebank path specified...");
/*      */         }
/* 2363 */         System.err.println("No test treebank path specified.  Using train path: \"" + treebankPath + "\"");
/* 2364 */         testPath = treebankPath;
/*      */       }
/*      */       
/* 2367 */       testTreebank = op.tlpParams.testMemoryTreebank();
/* 2368 */       testTreebank.loadPath(testPath, testFilter);
/*      */     }
/*      */     
/* 2371 */     Train.sisterSplitters = new HashSet(Arrays.asList(op.tlpParams.sisterSplitters()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2378 */     if (saveToTextFile)
/*      */     {
/* 2380 */       if (textOutputFileOrUrl != null) {
/* 2381 */         saveParserDataToText(lp.pd, textOutputFileOrUrl);
/*      */       } else {
/* 2383 */         System.err.println("Usage: must specify a text grammar output path");
/*      */       }
/*      */     }
/* 2386 */     if (saveToSerializedFile) {
/* 2387 */       if (serializedOutputFileOrUrl != null) {
/* 2388 */         saveParserDataToSerialized(lp.pd, serializedOutputFileOrUrl);
/* 2389 */       } else if ((textOutputFileOrUrl == null) && (testTreebank == null))
/*      */       {
/* 2391 */         System.err.println("usage: java edu.stanford.nlp.parser.lexparser.LexicalizedParser -train trainFilesPath [fileRange] -saveToSerializedFile serializedParserFilename");
/*      */       }
/*      */     }
/*      */     
/* 2395 */     if (Test.verbose)
/*      */     {
/*      */ 
/* 2398 */       String lexNumRules = lp.pparser != null ? Integer.toString(lp.pparser.lex.numRules()) : "";
/* 2399 */       System.err.println("Grammar\tStates\tTags\tWords\tUnaryR\tBinaryR\tTaggings");
/* 2400 */       System.err.println("Grammar\t" + Numberer.getGlobalNumberer("states").total() + "\t" + Numberer.getGlobalNumberer("tags").total() + "\t" + Numberer.getGlobalNumberer("words").total() + "\t" + (lp.pparser != null ? Integer.valueOf(lp.pparser.ug.numRules()) : "") + "\t" + (lp.pparser != null ? Integer.valueOf(lp.pparser.bg.numRules()) : "") + "\t" + lexNumRules);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2407 */       System.err.println("ParserPack is " + op.tlpParams.getClass().getName());
/* 2408 */       System.err.println("Lexicon is " + lp.pd.lex.getClass().getName());
/* 2409 */       System.err.println("Tags are: " + Numberer.getGlobalNumberer("tags"));
/* 2410 */       printOptions(false, op);
/*      */     }
/*      */     
/* 2413 */     if (testTreebank != null)
/*      */     {
/* 2415 */       lp.testOnTreebank(testTreebank);
/* 2416 */     } else if (argIndex >= args.length)
/*      */     {
/* 2418 */       PrintWriter pwOut = op.tlpParams.pw();
/* 2419 */       PrintWriter pwErr = op.tlpParams.pw(System.err);
/* 2420 */       if (lp.parse(op.tlpParams.defaultTestSentence())) {
/* 2421 */         Test.treePrint(op.tlpParams).printTree(lp.getBestParse(), pwOut);
/*      */       } else {
/* 2423 */         pwErr.println("Error. Can't parse test sentence: " + op.tlpParams.defaultTestSentence());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2428 */       lp.parseFiles(args, argIndex, tokenized, tokenizerFactory, documentPreprocessor, elementDelimiter, sentenceDelimiter, escaper, tagDelimiter);
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\LexicalizedParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */