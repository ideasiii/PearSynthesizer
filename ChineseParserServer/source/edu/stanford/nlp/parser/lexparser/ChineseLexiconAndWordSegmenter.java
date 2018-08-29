/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.io.NumberRangeFileFilter;
/*     */ import edu.stanford.nlp.io.NumberRangesFileFilter;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.process.DocumentPreprocessor;
/*     */ import edu.stanford.nlp.process.WordSegmentingTokenizer;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreePrint;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.Timing;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.io.Writer;
/*     */ import java.net.URL;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ChineseLexiconAndWordSegmenter implements Lexicon, WordSegmenter
/*     */ {
/*     */   private final ChineseLexicon chineseLexicon;
/*     */   private final WordSegmenter wordSegmenter;
/*     */   private Options op;
/*     */   private static final long serialVersionUID = -6554995189795187918L;
/*     */   
/*     */   public ChineseLexiconAndWordSegmenter(ChineseLexicon lex, WordSegmenter seg)
/*     */   {
/*  38 */     this.chineseLexicon = lex;
/*  39 */     this.wordSegmenter = seg;
/*  40 */     edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack.setTokenizerFactory(WordSegmentingTokenizer.factory(seg));
/*     */   }
/*     */   
/*     */   public edu.stanford.nlp.ling.Sentence segmentWords(String s) {
/*  44 */     return this.wordSegmenter.segmentWords(s);
/*     */   }
/*     */   
/*     */   public boolean isKnown(int word) {
/*  48 */     return this.chineseLexicon.isKnown(word);
/*     */   }
/*     */   
/*     */   public boolean isKnown(String word) {
/*  52 */     return this.chineseLexicon.isKnown(word);
/*     */   }
/*     */   
/*     */   public Iterator<IntTaggedWord> ruleIteratorByWord(int word, int loc) {
/*  56 */     return this.chineseLexicon.ruleIteratorByWord(word, loc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int numRules()
/*     */   {
/*  63 */     return this.chineseLexicon.numRules();
/*     */   }
/*     */   
/*     */   public void train(java.util.Collection<Tree> trees) {
/*  67 */     this.chineseLexicon.train(trees);
/*  68 */     this.wordSegmenter.train(trees);
/*     */   }
/*     */   
/*     */   public float score(IntTaggedWord iTW, int loc) {
/*  72 */     return this.chineseLexicon.score(iTW, loc);
/*     */   }
/*     */   
/*     */   public void loadSegmenter(String filename) {
/*  76 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void readData(java.io.BufferedReader in) throws IOException {
/*  80 */     this.chineseLexicon.readData(in);
/*     */   }
/*     */   
/*     */   public void writeData(Writer w) throws IOException {
/*  84 */     this.chineseLexicon.writeData(w);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  88 */     in.defaultReadObject();
/*  89 */     edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack.setTokenizerFactory(WordSegmentingTokenizer.factory(this.wordSegmenter));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int numSubArgs(String[] args, int index)
/*     */   {
/*  96 */     int i = index;
/*  97 */     while ((i + 1 < args.length) && (args[(i + 1)].charAt(0) != '-')) {
/*  98 */       i++;
/*     */     }
/* 100 */     return i - index;
/*     */   }
/*     */   
/*     */   public ChineseLexiconAndWordSegmenter(Treebank trainTreebank, Options op) {
/* 104 */     ChineseLexiconAndWordSegmenter cs = getSegmenterDataFromTreebank(trainTreebank, op);
/* 105 */     this.chineseLexicon = cs.chineseLexicon;
/* 106 */     this.wordSegmenter = cs.wordSegmenter;
/*     */   }
/*     */   
/*     */   private static ChineseLexiconAndWordSegmenter getSegmenterDataFromTreebank(Treebank trainTreebank, Options op) {
/* 110 */     System.out.println("Currently " + new java.util.Date());
/*     */     
/* 112 */     Timing.startTime();
/*     */     
/* 114 */     TreebankLangParserParams tlpParams = op.tlpParams;
/* 115 */     if (Test.verbose) {
/* 116 */       System.out.print("Training ");
/* 117 */       System.out.println(trainTreebank.textualSummary());
/*     */     }
/*     */     
/* 120 */     System.out.print("Binarizing trees...");
/*     */     TreeAnnotatorAndBinarizer binarizer;
/* 122 */     TreeAnnotatorAndBinarizer binarizer; if (!Train.leftToRight) {
/* 123 */       binarizer = new TreeAnnotatorAndBinarizer(tlpParams, op.forceCNF, !Train.outsideFactor(), true);
/*     */     } else {
/* 125 */       binarizer = new TreeAnnotatorAndBinarizer(tlpParams.headFinder(), new edu.stanford.nlp.trees.LeftHeadFinder(), tlpParams, op.forceCNF, !Train.outsideFactor(), true);
/*     */     }
/* 127 */     CollinsPuncTransformer collinsPuncTransformer = null;
/* 128 */     if (Train.collinsPunc) {
/* 129 */       collinsPuncTransformer = new CollinsPuncTransformer(tlpParams.treebankLanguagePack());
/*     */     }
/* 131 */     List<Tree> binaryTrainTrees = new java.util.ArrayList();
/*     */     
/*     */ 
/* 134 */     if (Train.selectiveSplit) {
/* 135 */       Train.splitters = ParentAnnotationStats.getSplitCategories(trainTreebank, true, 0, Train.selectiveSplitCutOff, Train.tagSelectiveSplitCutOff, tlpParams.treebankLanguagePack());
/* 136 */       if (Test.verbose) {
/* 137 */         System.err.println("Parent split categories: " + Train.splitters);
/*     */       }
/*     */     }
/* 140 */     if (Train.selectivePostSplit) {
/* 141 */       edu.stanford.nlp.trees.TreeTransformer myTransformer = new TreeAnnotator(tlpParams.headFinder(), tlpParams);
/* 142 */       Treebank annotatedTB = trainTreebank.transform(myTransformer);
/* 143 */       Train.postSplitters = ParentAnnotationStats.getSplitCategories(annotatedTB, true, 0, Train.selectivePostSplitCutOff, Train.tagSelectivePostSplitCutOff, tlpParams.treebankLanguagePack());
/* 144 */       if (Test.verbose) {
/* 145 */         System.err.println("Parent post annotation split categories: " + Train.postSplitters);
/*     */       }
/*     */     }
/* 148 */     if (Train.hSelSplit) {
/* 149 */       binarizer.setDoSelectiveSplit(false);
/* 150 */       for (Tree tree : trainTreebank) {
/* 151 */         if (Train.collinsPunc) {
/* 152 */           tree = collinsPuncTransformer.transformTree(tree);
/*     */         }
/* 154 */         tree = binarizer.transformTree(tree);
/*     */       }
/* 156 */       binarizer.setDoSelectiveSplit(true);
/*     */     }
/* 158 */     for (Tree tree : trainTreebank) {
/* 159 */       if (Train.collinsPunc) {
/* 160 */         tree = collinsPuncTransformer.transformTree(tree);
/*     */       }
/* 162 */       tree = binarizer.transformTree(tree);
/* 163 */       binaryTrainTrees.add(tree);
/*     */     }
/*     */     
/* 166 */     Timing.tick("done.");
/* 167 */     if (Test.verbose) {
/* 168 */       binarizer.dumpStats();
/*     */     }
/* 170 */     System.out.print("Extracting Lexicon...");
/* 171 */     ChineseLexiconAndWordSegmenter clex = (ChineseLexiconAndWordSegmenter)op.tlpParams.lex(op.lexOptions);
/* 172 */     clex.train(binaryTrainTrees);
/* 173 */     Timing.tick("done.");
/* 174 */     return clex;
/*     */   }
/*     */   
/*     */   private static void printArgs(String[] args, PrintStream ps) {
/* 178 */     ps.print("ChineseLexiconAndWordSegmenter invoked with arguments:");
/* 179 */     for (int i = 0; i < args.length; i++) {
/* 180 */       ps.print(" " + args[i]);
/*     */     }
/* 182 */     ps.println();
/*     */   }
/*     */   
/*     */   static void saveSegmenterDataToSerialized(ChineseLexiconAndWordSegmenter cs, String filename) {
/*     */     try {
/* 187 */       System.err.print("Writing segmenter in serialized format to file " + filename + " ");
/* 188 */       ObjectOutputStream out = edu.stanford.nlp.io.IOUtils.writeStreamFromString(filename);
/*     */       
/* 190 */       out.writeObject(cs);
/* 191 */       out.close();
/* 192 */       System.err.println("done.");
/*     */     } catch (IOException ioe) {
/* 194 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   static void saveSegmenterDataToText(ChineseLexiconAndWordSegmenter cs, String filename)
/*     */   {
/*     */     try {
/* 201 */       System.err.print("Writing parser in text grammar format to file " + filename);
/*     */       java.io.OutputStream os;
/* 203 */       java.io.OutputStream os; if (filename.endsWith(".gz"))
/*     */       {
/* 205 */         os = new BufferedOutputStream(new java.util.zip.GZIPOutputStream(new FileOutputStream(filename)));
/*     */       } else {
/* 207 */         os = new BufferedOutputStream(new FileOutputStream(filename));
/*     */       }
/* 209 */       PrintWriter out = new PrintWriter(os);
/* 210 */       String prefix = "BEGIN ";
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */       out.println(prefix + "LEXICON");
/* 218 */       if (cs != null) {
/* 219 */         cs.writeData(out);
/*     */       }
/* 221 */       out.println();
/* 222 */       System.err.print(".");
/* 223 */       out.flush();
/* 224 */       out.close();
/* 225 */       System.err.println("done.");
/*     */     } catch (IOException e) {
/* 227 */       System.err.println("Trouble saving segmenter data to ASCII format.");
/* 228 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private static Treebank makeTreebank(String treebankPath, Options op, FileFilter filt) {
/* 233 */     System.err.println("Training a segmenter from treebank dir: " + treebankPath);
/* 234 */     Treebank trainTreebank = op.tlpParams.memoryTreebank();
/* 235 */     System.err.print("Reading trees...");
/* 236 */     if (filt == null) {
/* 237 */       trainTreebank.loadPath(treebankPath);
/*     */     } else {
/* 239 */       trainTreebank.loadPath(treebankPath, filt);
/*     */     }
/*     */     
/* 242 */     Timing.tick("done [read " + trainTreebank.size() + " trees].");
/* 243 */     return trainTreebank;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseLexiconAndWordSegmenter(String segmenterFileOrUrl, Options op)
/*     */   {
/* 253 */     ChineseLexiconAndWordSegmenter cs = getSegmenterDataFromFile(segmenterFileOrUrl, op);
/* 254 */     this.op = cs.op;
/* 255 */     this.chineseLexicon = cs.chineseLexicon;
/* 256 */     this.wordSegmenter = cs.wordSegmenter;
/*     */   }
/*     */   
/*     */   public static ChineseLexiconAndWordSegmenter getSegmenterDataFromFile(String parserFileOrUrl, Options op) {
/* 260 */     ChineseLexiconAndWordSegmenter cs = getSegmenterDataFromSerializedFile(parserFileOrUrl);
/* 261 */     if (cs == null) {}
/*     */     
/*     */ 
/* 264 */     return cs;
/*     */   }
/*     */   
/*     */   protected static ChineseLexiconAndWordSegmenter getSegmenterDataFromSerializedFile(String serializedFileOrUrl) {
/* 268 */     ChineseLexiconAndWordSegmenter cs = null;
/*     */     try {
/* 270 */       System.err.print("Loading segmenter from serialized file " + serializedFileOrUrl + " ...");
/*     */       java.io.InputStream is;
/*     */       java.io.InputStream is;
/* 273 */       if (serializedFileOrUrl.startsWith("http://")) {
/* 274 */         URL u = new URL(serializedFileOrUrl);
/* 275 */         java.net.URLConnection uc = u.openConnection();
/* 276 */         is = uc.getInputStream();
/*     */       } else {
/* 278 */         is = new java.io.FileInputStream(serializedFileOrUrl); }
/*     */       ObjectInputStream in;
/* 280 */       ObjectInputStream in; if (serializedFileOrUrl.endsWith(".gz"))
/*     */       {
/* 282 */         in = new ObjectInputStream(new java.io.BufferedInputStream(new java.util.zip.GZIPInputStream(is)));
/*     */       } else {
/* 284 */         in = new ObjectInputStream(new java.io.BufferedInputStream(is));
/*     */       }
/* 286 */       cs = (ChineseLexiconAndWordSegmenter)in.readObject();
/* 287 */       in.close();
/* 288 */       System.err.println(" done.");
/* 289 */       return cs;
/*     */     }
/*     */     catch (InvalidClassException ice) {
/* 292 */       System.err.println();
/* 293 */       ice.printStackTrace();
/* 294 */       System.exit(2);
/*     */     }
/*     */     catch (FileNotFoundException fnfe) {
/* 297 */       System.err.println();
/* 298 */       fnfe.printStackTrace();
/* 299 */       System.exit(2);
/*     */ 
/*     */     }
/*     */     catch (StreamCorruptedException sce) {}catch (Exception e)
/*     */     {
/* 304 */       System.err.println();
/* 305 */       e.printStackTrace();
/*     */     }
/* 307 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 318 */     boolean train = false;
/* 319 */     boolean saveToSerializedFile = false;
/* 320 */     boolean saveToTextFile = false;
/* 321 */     String serializedInputFileOrUrl = null;
/* 322 */     String textInputFileOrUrl = null;
/* 323 */     String serializedOutputFileOrUrl = null;
/* 324 */     String textOutputFileOrUrl = null;
/* 325 */     String treebankPath = null;
/* 326 */     Treebank testTreebank = null;
/* 327 */     Treebank tuneTreebank = null;
/* 328 */     String testPath = null;
/* 329 */     FileFilter testFilter = null;
/* 330 */     FileFilter trainFilter = null;
/* 331 */     String encoding = null;
/*     */     
/*     */ 
/* 334 */     edu.stanford.nlp.objectbank.TokenizerFactory tokenizerFactory = null;
/* 335 */     DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor();
/* 336 */     boolean tokenized = false;
/* 337 */     edu.stanford.nlp.process.Function<List<HasWord>, List<HasWord>> escaper = null;
/* 338 */     int tagDelimiter = -1;
/* 339 */     String sentenceDelimiter = "\n";
/* 340 */     boolean fromXML = false;
/* 341 */     int argIndex = 0;
/* 342 */     if (args.length < 1) {
/* 343 */       System.err.println("usage: java edu.stanford.nlp.parser.lexparser.LexicalizedParser parserFileOrUrl filename*");
/* 344 */       System.exit(1);
/*     */     }
/*     */     
/* 347 */     Options op = new Options();
/* 348 */     op.tlpParams = new ChineseTreebankParserParams();
/*     */     
/*     */ 
/* 351 */     while ((argIndex < args.length) && (args[argIndex].charAt(0) == '-')) {
/* 352 */       if (args[argIndex].equalsIgnoreCase("-train")) {
/* 353 */         train = true;
/* 354 */         saveToSerializedFile = true;
/* 355 */         int numSubArgs = numSubArgs(args, argIndex);
/* 356 */         argIndex++;
/* 357 */         if (numSubArgs > 1) {
/* 358 */           treebankPath = args[argIndex];
/* 359 */           argIndex++;
/*     */         } else {
/* 361 */           throw new RuntimeException("Error: -train option must have treebankPath as first argument.");
/*     */         }
/* 363 */         if (numSubArgs == 2) {
/* 364 */           trainFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/* 365 */         } else if (numSubArgs >= 3) {
/*     */           try {
/* 367 */             int low = Integer.parseInt(args[argIndex]);
/* 368 */             int high = Integer.parseInt(args[(argIndex + 1)]);
/* 369 */             trainFilter = new NumberRangeFileFilter(low, high, true);
/* 370 */             argIndex += 2;
/*     */           }
/*     */           catch (NumberFormatException e) {
/* 373 */             trainFilter = new NumberRangesFileFilter(args[argIndex], true);
/* 374 */             argIndex++;
/*     */           }
/*     */         }
/* 377 */       } else if (args[argIndex].equalsIgnoreCase("-encoding")) {
/* 378 */         encoding = args[(argIndex + 1)];
/* 379 */         op.tlpParams.setInputEncoding(encoding);
/* 380 */         op.tlpParams.setOutputEncoding(encoding);
/* 381 */         argIndex += 2;
/* 382 */       } else if (args[argIndex].equalsIgnoreCase("-loadFromSerializedFile"))
/*     */       {
/*     */ 
/* 385 */         serializedInputFileOrUrl = args[(argIndex + 1)];
/* 386 */         argIndex += 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 393 */       else if (args[argIndex].equalsIgnoreCase("-saveToSerializedFile")) {
/* 394 */         saveToSerializedFile = true;
/* 395 */         serializedOutputFileOrUrl = args[(argIndex + 1)];
/* 396 */         argIndex += 2;
/* 397 */       } else if (args[argIndex].equalsIgnoreCase("-saveToTextFile"))
/*     */       {
/* 399 */         saveToTextFile = true;
/* 400 */         textOutputFileOrUrl = args[(argIndex + 1)];
/* 401 */         argIndex += 2;
/* 402 */       } else if (args[argIndex].equalsIgnoreCase("-treebank"))
/*     */       {
/* 404 */         int numSubArgs = numSubArgs(args, argIndex);
/* 405 */         argIndex++;
/* 406 */         if (numSubArgs == 1) {
/* 407 */           testFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/* 408 */         } else if (numSubArgs > 1) {
/* 409 */           testPath = args[(argIndex++)];
/* 410 */           if (numSubArgs == 2) {
/* 411 */             testFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/* 412 */           } else if (numSubArgs >= 3) {
/*     */             try {
/* 414 */               int low = Integer.parseInt(args[argIndex]);
/* 415 */               int high = Integer.parseInt(args[(argIndex + 1)]);
/* 416 */               testFilter = new NumberRangeFileFilter(low, high, true);
/* 417 */               argIndex += 2;
/*     */             }
/*     */             catch (NumberFormatException e) {
/* 420 */               testFilter = new NumberRangesFileFilter(args[(argIndex++)], true);
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/* 425 */         int j = op.tlpParams.setOptionFlag(args, argIndex);
/* 426 */         if (j == argIndex) {
/* 427 */           System.err.println("Unknown option ignored: " + args[argIndex]);
/* 428 */           j++;
/*     */         }
/* 430 */         argIndex = j;
/*     */       }
/*     */     }
/*     */     
/* 434 */     TreebankLangParserParams tlpParams = op.tlpParams;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 439 */     ChineseLexiconAndWordSegmenter cs = null;
/* 440 */     if ((!train) && (Test.verbose)) {
/* 441 */       System.out.println("Currently " + new java.util.Date());
/* 442 */       printArgs(args, System.out);
/*     */     }
/* 444 */     if (train) {
/* 445 */       printArgs(args, System.out);
/*     */       
/* 447 */       if (treebankPath == null)
/*     */       {
/* 449 */         treebankPath = args[argIndex];
/* 450 */         argIndex++;
/* 451 */         if (args.length > argIndex + 1) {
/*     */           try
/*     */           {
/* 454 */             int low = Integer.parseInt(args[argIndex]);
/* 455 */             int high = Integer.parseInt(args[(argIndex + 1)]);
/* 456 */             trainFilter = new NumberRangeFileFilter(low, high, true);
/* 457 */             argIndex += 2;
/*     */           }
/*     */           catch (NumberFormatException e) {
/* 460 */             trainFilter = new NumberRangesFileFilter(args[argIndex], true);
/* 461 */             argIndex++;
/*     */           }
/*     */         }
/*     */       }
/* 465 */       Treebank trainTreebank = makeTreebank(treebankPath, op, trainFilter);
/* 466 */       cs = new ChineseLexiconAndWordSegmenter(trainTreebank, op);
/* 467 */     } else if (textInputFileOrUrl == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 473 */       if (serializedInputFileOrUrl == null)
/*     */       {
/* 475 */         serializedInputFileOrUrl = args[argIndex];
/* 476 */         argIndex++;
/*     */       }
/*     */       try {
/* 479 */         cs = new ChineseLexiconAndWordSegmenter(serializedInputFileOrUrl, op);
/*     */       } catch (IllegalArgumentException e) {
/* 481 */         System.err.println("Error loading segmenter, exiting...");
/* 482 */         System.exit(0);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 488 */     TreePrint treePrint = Test.treePrint(tlpParams);
/*     */     
/* 490 */     if (testFilter != null) {
/* 491 */       if (testPath == null) {
/* 492 */         if (treebankPath == null) {
/* 493 */           throw new RuntimeException("No test treebank path specified...");
/*     */         }
/* 495 */         System.err.println("No test treebank path specified.  Using train path: \"" + treebankPath + "\"");
/* 496 */         testPath = treebankPath;
/*     */       }
/*     */       
/* 499 */       testTreebank = tlpParams.testMemoryTreebank();
/* 500 */       testTreebank.loadPath(testPath, testFilter);
/*     */     }
/*     */     
/* 503 */     Train.sisterSplitters = new java.util.HashSet(java.util.Arrays.asList(tlpParams.sisterSplitters()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 512 */     if (Test.verbose) {
/* 513 */       System.err.println("Lexicon is " + cs.getClass().getName());
/*     */     }
/*     */     
/* 516 */     PrintWriter pwOut = tlpParams.pw();
/* 517 */     PrintWriter pwErr = tlpParams.pw(System.err);
/*     */     
/*     */ 
/*     */ 
/* 521 */     if (saveToTextFile)
/*     */     {
/* 523 */       if (textOutputFileOrUrl != null) {
/* 524 */         saveSegmenterDataToText(cs, textOutputFileOrUrl);
/*     */       } else {
/* 526 */         System.err.println("Usage: must specify a text segmenter data output path");
/*     */       }
/*     */     }
/* 529 */     if (saveToSerializedFile) {
/* 530 */       if ((serializedOutputFileOrUrl == null) && (argIndex < args.length))
/*     */       {
/* 532 */         serializedOutputFileOrUrl = args[argIndex];
/* 533 */         argIndex++;
/*     */       }
/* 535 */       if (serializedOutputFileOrUrl != null) {
/* 536 */         saveSegmenterDataToSerialized(cs, serializedOutputFileOrUrl);
/* 537 */       } else if ((textOutputFileOrUrl == null) && (testTreebank == null))
/*     */       {
/* 539 */         System.err.println("usage: java edu.stanford.nlp.parser.lexparser.ChineseLexiconAndWordSegmenter-train trainFilesPath [start stop] serializedParserFilename");
/*     */       }
/*     */     }
/*     */     
/* 543 */     if ((!Test.verbose) || (
/*     */     
/*     */ 
/* 546 */       (testTreebank != null) || ((argIndex < args.length) && (args[argIndex].equalsIgnoreCase("-treebank")))))
/*     */     {
/* 548 */       if (testTreebank == null)
/*     */       {
/* 550 */         testTreebank = tlpParams.testMemoryTreebank();
/* 551 */         if (args.length < argIndex + 4) {
/* 552 */           testTreebank.loadPath(args[(argIndex + 1)]);
/*     */         } else {
/* 554 */           int testlow = Integer.parseInt(args[(argIndex + 2)]);
/* 555 */           int testhigh = Integer.parseInt(args[(argIndex + 3)]);
/* 556 */           testTreebank.loadPath(args[(argIndex + 1)], new NumberRangeFileFilter(testlow, testhigh, true));
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/* 571 */       int numWords = 0;
/* 572 */       Timing timer = new Timing();
/*     */       
/* 574 */       if (tokenized) {
/* 575 */         tokenizerFactory = edu.stanford.nlp.process.WhitespaceTokenizer.factory();
/*     */       }
/* 577 */       TreebankLanguagePack tlp = tlpParams.treebankLanguagePack();
/* 578 */       if (tokenizerFactory == null) {
/* 579 */         tokenizerFactory = tlp.getTokenizerFactory();
/*     */       }
/* 581 */       documentPreprocessor.setTokenizerFactory(tokenizerFactory);
/* 582 */       documentPreprocessor.setSentenceFinalPuncWords(tlp.sentenceFinalPunctuationWords());
/* 583 */       if (encoding != null) {
/* 584 */         documentPreprocessor.setEncoding(encoding);
/*     */       }
/* 586 */       timer.start();
/* 587 */       for (int i = argIndex; i < args.length; i++) {
/* 588 */         String filename = args[i];
/*     */         try {
/* 590 */           List document = null;
/* 591 */           if (fromXML) {
/* 592 */             document = documentPreprocessor.getSentencesFromXML(filename, sentenceDelimiter, tokenized);
/*     */           } else {
/* 594 */             document = documentPreprocessor.getSentencesFromText(filename, escaper, sentenceDelimiter, tagDelimiter);
/*     */           }
/* 596 */           System.err.println("Segmenting file: " + filename + " with " + document.size() + " sentences.");
/* 597 */           PrintWriter pwo = pwOut;
/* 598 */           if (Test.writeOutputFiles) {
/*     */             try {
/* 600 */               pwo = tlpParams.pw(new FileOutputStream(filename + ".stp"));
/*     */             } catch (IOException ioe) {
/* 602 */               ioe.printStackTrace();
/*     */             }
/*     */           }
/* 605 */           int num = 0;
/* 606 */           treePrint.printHeader(pwo, tlp.getEncoding());
/* 607 */           for (Iterator it = document.iterator(); it.hasNext();) {
/* 608 */             num++;
/* 609 */             List sentence = (List)it.next();
/* 610 */             int len = sentence.size();
/* 611 */             numWords += len;
/*     */             
/* 613 */             pwo.println(new edu.stanford.nlp.ling.Sentence(sentence));
/*     */           }
/* 615 */           treePrint.printFooter(pwo);
/* 616 */           if (Test.writeOutputFiles) {
/* 617 */             pwo.close();
/*     */           }
/*     */         } catch (IOException e) {
/* 620 */           pwErr.println("Couldn't find file: " + filename);
/*     */         }
/*     */       }
/*     */       
/* 624 */       long millis = timer.stop();
/* 625 */       double wordspersec = numWords / (millis / 1000.0D);
/* 626 */       java.text.NumberFormat nf = new java.text.DecimalFormat("0.00");
/* 627 */       pwErr.println("Segmented " + numWords + " words at " + nf.format(wordspersec) + " words per second.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ChineseLexiconAndWordSegmenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */