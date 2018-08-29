/*     */ package edu.stanford.nlp.trees.tregex;
/*     */ 
/*     */ import edu.stanford.nlp.ling.StringLabelFactory;
/*     */ import edu.stanford.nlp.process.Function;
/*     */ import edu.stanford.nlp.trees.CollinsHeadFinder;
/*     */ import edu.stanford.nlp.trees.DiskTreebank;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.MemoryTreebank;
/*     */ import edu.stanford.nlp.trees.PennTreeReader;
/*     */ import edu.stanford.nlp.trees.PennTreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreePrint;
/*     */ import edu.stanford.nlp.trees.TreeReader;
/*     */ import edu.stanford.nlp.trees.TreeReaderFactory;
/*     */ import edu.stanford.nlp.trees.TreeVisitor;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import edu.stanford.nlp.util.Timing;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public abstract class TregexPattern
/*     */   implements Serializable
/*     */ {
/*     */   static Function<String, String> currentBasicCatFunction;
/*     */   
/*     */   static void setBasicCatFunction(Function<String, String> f)
/*     */   {
/* 288 */     currentBasicCatFunction = f;
/*     */   }
/*     */   
/* 291 */   private boolean neg = false;
/* 292 */   private boolean opt = false;
/*     */   private String patternString;
/*     */   
/*     */   void negate() {
/* 296 */     this.neg = true;
/* 297 */     if ((this.neg) && (this.opt)) {
/* 298 */       throw new RuntimeException("Node cannot be both negated and optional.");
/*     */     }
/*     */   }
/*     */   
/*     */   void makeOptional() {
/* 303 */     this.opt = true;
/* 304 */     if ((this.neg) && (this.opt)) {
/* 305 */       throw new RuntimeException("Node cannot be both negated and optional.");
/*     */     }
/*     */   }
/*     */   
/*     */   private void prettyPrint(PrintWriter pw, int indent) {
/* 310 */     for (int i = 0; i < indent; i++) {
/* 311 */       pw.print("   ");
/*     */     }
/* 313 */     if (this.neg) {
/* 314 */       pw.print('!');
/*     */     }
/* 316 */     if (this.opt) {
/* 317 */       pw.print('?');
/*     */     }
/* 319 */     pw.println(localString());
/* 320 */     for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
/* 321 */       TregexPattern child = (TregexPattern)iter.next();
/* 322 */       child.prettyPrint(pw, indent + 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   abstract List getChildren();
/*     */   
/*     */ 
/*     */   abstract String localString();
/*     */   
/*     */ 
/*     */   boolean isNegated()
/*     */   {
/* 335 */     return this.neg;
/*     */   }
/*     */   
/*     */   boolean isOptional() {
/* 339 */     return this.opt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract TregexMatcher matcher(Tree paramTree1, Tree paramTree2, Map<Object, Tree> paramMap, VariableStrings paramVariableStrings);
/*     */   
/*     */ 
/*     */ 
/*     */   public TregexMatcher matcher(Tree t)
/*     */   {
/* 351 */     return matcher(t, t, new HashMap(), new VariableStrings());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TregexPattern compile(String tregex)
/*     */     throws ParseException
/*     */   {
/* 364 */     return TregexPatternCompiler.defaultCompiler.compile(tregex);
/*     */   }
/*     */   
/*     */   public String pattern() {
/* 368 */     return this.patternString;
/*     */   }
/*     */   
/*     */   public void setPatternString(String patternString) {
/* 372 */     this.patternString = patternString;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String toString();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prettyPrint(PrintWriter pw)
/*     */   {
/* 385 */     prettyPrint(pw, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prettyPrint(PrintStream ps)
/*     */   {
/* 393 */     prettyPrint(new PrintWriter(new OutputStreamWriter(ps), true));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prettyPrint()
/*     */   {
/* 401 */     prettyPrint(System.out);
/*     */   }
/*     */   
/* 404 */   private static Pattern codePattern = Pattern.compile("([0-9]+):([0-9]+)");
/*     */   
/* 406 */   private static void extractSubtrees(List<String> codeStrings, String treeFile) { List<Pair<Integer, Integer>> codes = new ArrayList();
/* 407 */     for (String s : codeStrings) {
/* 408 */       Matcher m = codePattern.matcher(s);
/* 409 */       if (m.matches()) {
/* 410 */         codes.add(new Pair(Integer.valueOf(Integer.parseInt(m.group(1))), Integer.valueOf(Integer.parseInt(m.group(2)))));
/*     */       } else
/* 412 */         throw new RuntimeException("Error: illegal node code " + s);
/*     */     }
/* 414 */     TreeReaderFactory trf = new TreeMatcher.TRegexTreeReaderFactory();
/* 415 */     MemoryTreebank treebank = new MemoryTreebank(trf);
/* 416 */     treebank.loadPath(treeFile, null, true);
/* 417 */     for (Pair<Integer, Integer> code : codes) {
/* 418 */       Tree t = treebank.get(((Integer)code.first()).intValue() - 1);
/* 419 */       t.getNodeNumber(((Integer)code.second()).intValue()).pennPrint();
/*     */     }
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
/*     */   static Treebank treebank;
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
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/* 473 */     Timing.startTime();
/*     */     
/* 475 */     String treePrintFormats = "";
/* 476 */     String printNonMatchingTreesOption = "-v";
/* 477 */     String subtreeCodeOption = "-x";
/* 478 */     String extractSubtreesOption = "-extract";
/* 479 */     String extractSubtreesFileOption = "-extractFile";
/* 480 */     String inputFileOption = "-i";
/* 481 */     String headFinderOption = "-hf";
/* 482 */     String headFinderArgOption = "-hfArg";
/* 483 */     String trfOption = "-trf";
/* 484 */     String headFinderClassName = null;
/* 485 */     String[] headFinderArgs = StringUtils.EMPTY_STRING_ARRAY;
/* 486 */     String treeReaderFactoryClassName = null;
/* 487 */     String printHandleOption = "-h";
/* 488 */     String markHandleOption = "-k";
/* 489 */     String encodingOption = "-encoding";
/* 490 */     String encoding = "UTF-8";
/* 491 */     Map<String, Integer> flagMap = new HashMap();
/* 492 */     flagMap.put(extractSubtreesOption, Integer.valueOf(2));
/* 493 */     flagMap.put(extractSubtreesFileOption, Integer.valueOf(2));
/* 494 */     flagMap.put(subtreeCodeOption, Integer.valueOf(0));
/* 495 */     flagMap.put(printNonMatchingTreesOption, Integer.valueOf(0));
/* 496 */     flagMap.put(encodingOption, Integer.valueOf(1));
/* 497 */     flagMap.put(inputFileOption, Integer.valueOf(1));
/* 498 */     flagMap.put(printHandleOption, Integer.valueOf(1));
/* 499 */     flagMap.put(markHandleOption, Integer.valueOf(2));
/* 500 */     flagMap.put(headFinderOption, Integer.valueOf(1));
/* 501 */     flagMap.put(headFinderArgOption, Integer.valueOf(1));
/* 502 */     flagMap.put(trfOption, Integer.valueOf(1));
/* 503 */     Map<String, String[]> argsMap = StringUtils.argsToMap(args, flagMap);
/* 504 */     args = (String[])argsMap.get(null);
/*     */     
/* 506 */     if (argsMap.containsKey(extractSubtreesOption)) {
/* 507 */       List<String> subTreeStrings = Arrays.asList(new String[] { ((String[])argsMap.get(extractSubtreesOption))[0] });
/* 508 */       extractSubtrees(subTreeStrings, ((String[])argsMap.get(extractSubtreesOption))[1]);
/* 509 */       return;
/*     */     }
/* 511 */     if (argsMap.containsKey(extractSubtreesFileOption)) {
/* 512 */       List<String> subTreeStrings = Arrays.asList(StringUtils.slurpFile(((String[])argsMap.get(extractSubtreesFileOption))[0]).split("\n|\r|\n\r"));
/* 513 */       extractSubtrees(subTreeStrings, ((String[])argsMap.get(extractSubtreesFileOption))[0]);
/* 514 */       return;
/*     */     }
/*     */     
/* 517 */     if (args.length < 1) {
/* 518 */       System.err.println("Usage: java edu.stanford.nlp.trees.tregex.TregexPattern [-T] [-C] [-w] [-f] [-o] [-n] [-s] [-h handle]* pattern [filepath]");
/* 519 */       System.exit(0);
/*     */     }
/* 521 */     String matchString = args[0];
/*     */     
/* 523 */     if (argsMap.containsKey(headFinderOption)) {
/* 524 */       headFinderClassName = ((String[])argsMap.get(headFinderOption))[0];
/* 525 */       System.err.println("Using head finder " + headFinderClassName + "...");
/*     */     }
/* 527 */     if (argsMap.containsKey(headFinderArgOption)) {
/* 528 */       headFinderArgs = (String[])argsMap.get(headFinderArgOption);
/*     */     }
/* 530 */     if (argsMap.containsKey(trfOption)) {
/* 531 */       treeReaderFactoryClassName = ((String[])argsMap.get(trfOption))[0];
/* 532 */       System.err.println("Using tree reader factory " + treeReaderFactoryClassName + "...");
/*     */     }
/* 534 */     if (argsMap.containsKey("-T")) {
/* 535 */       TRegexTreeVisitor.printTree = true;
/*     */     }
/* 537 */     if (argsMap.containsKey(encodingOption)) {
/* 538 */       encoding = ((String[])argsMap.get(encodingOption))[0];
/* 539 */       System.err.println("set encoding to " + encoding);
/*     */     }
/* 541 */     if (argsMap.containsKey(inputFileOption)) {
/* 542 */       String inputFile = ((String[])argsMap.get(inputFileOption))[0];
/* 543 */       matchString = StringUtils.slurpFile(inputFile, encoding);
/* 544 */       String[] newArgs = new String[args.length + 1];
/* 545 */       System.arraycopy(args, 0, newArgs, 1, args.length);
/* 546 */       args = newArgs;
/*     */     }
/* 548 */     if (argsMap.containsKey("-C")) {
/* 549 */       TRegexTreeVisitor.printMatches = false;
/* 550 */       TRegexTreeVisitor.access$002(true);
/*     */     }
/*     */     
/* 553 */     if (argsMap.containsKey("-v")) {
/* 554 */       TRegexTreeVisitor.printNonMatchingTrees = true;
/*     */     }
/* 556 */     if (argsMap.containsKey("-x")) {
/* 557 */       TRegexTreeVisitor.printSubtreeCode = true;
/* 558 */       TRegexTreeVisitor.printMatches = false;
/*     */     }
/* 560 */     if (argsMap.containsKey("-w")) {
/* 561 */       TRegexTreeVisitor.printWholeTree = true;
/*     */     }
/* 563 */     if (argsMap.containsKey("-f")) {
/* 564 */       TRegexTreeVisitor.printFilename = true;
/*     */     }
/* 566 */     if (argsMap.containsKey("-o"))
/* 567 */       TRegexTreeVisitor.oneMatchPerRootNode = true;
/* 568 */     if (argsMap.containsKey("-n"))
/* 569 */       TRegexTreeVisitor.reportTreeNumbers = true;
/* 570 */     if (argsMap.containsKey("-u")) {
/* 571 */       treePrintFormats = treePrintFormats + "rootSymbolOnly,";
/* 572 */     } else if (argsMap.containsKey("-s")) {
/* 573 */       treePrintFormats = treePrintFormats + "oneline,";
/* 574 */     } else if (argsMap.containsKey("-t")) {
/* 575 */       treePrintFormats = treePrintFormats + "words,";
/*     */     } else {
/* 577 */       treePrintFormats = treePrintFormats + "penn,";
/*     */     }
/* 579 */     HeadFinder hf = new CollinsHeadFinder();
/* 580 */     if (headFinderClassName != null) {
/* 581 */       Class[] hfArgClasses = new Class[headFinderArgs.length];
/* 582 */       for (int i = 0; i < hfArgClasses.length; i++) hfArgClasses[i] = String.class;
/*     */       try {
/* 584 */         hf = (HeadFinder)Class.forName(headFinderClassName).getConstructor(hfArgClasses).newInstance((Object[])headFinderArgs);
/*     */       } catch (Exception e) {
/* 586 */         throw new RuntimeException("Error occurred while constructing HeadFinder: " + e);
/*     */       }
/*     */     }
/* 589 */     TRegexTreeVisitor.tp = new TreePrint(treePrintFormats, new PennTreebankLanguagePack());
/*     */     
/*     */     try
/*     */     {
/* 593 */       TregexPatternCompiler tpc = new TregexPatternCompiler(hf);
/* 594 */       TregexPattern p = tpc.compile(matchString);
/* 595 */       System.err.println("Pattern string:\n" + p.pattern());
/* 596 */       System.err.println("Parsed representation:");
/* 597 */       p.prettyPrint(System.err);
/*     */       
/* 599 */       String[] handles = (String[])argsMap.get(printHandleOption);
/* 600 */       if (args.length == 1) {
/* 601 */         System.err.println("using default tree");
/* 602 */         TreeReader r = new PennTreeReader(new StringReader("(VP (VP (VBZ Try) (NP (NP (DT this) (NN wine)) (CC and) (NP (DT these) (NNS snails)))) (PUNCT .))"), new LabeledScoredTreeFactory(new StringLabelFactory()));
/* 603 */         Tree t = r.readTree();
/* 604 */         treebank = new MemoryTreebank();
/* 605 */         treebank.add(t);
/*     */       } else {
/* 607 */         int last = args.length - 1;
/* 608 */         System.err.println("Reading trees from file(s) " + args[last]);
/* 609 */         TreeReaderFactory trf = new TreeMatcher.TRegexTreeReaderFactory();
/* 610 */         if (treeReaderFactoryClassName != null) {
/*     */           try {
/* 612 */             trf = (TreeReaderFactory)Class.forName(treeReaderFactoryClassName).newInstance();
/*     */           } catch (Exception e) {
/* 614 */             throw new RuntimeException("Error occurred while constructing TreeReaderFactory: " + e);
/*     */           }
/*     */         }
/* 617 */         treebank = new DiskTreebank(trf, encoding);
/* 618 */         treebank.loadPath(args[last], null, true);
/*     */       }
/* 620 */       TRegexTreeVisitor vis = new TRegexTreeVisitor(p, handles, encoding);
/*     */       
/* 622 */       treebank.apply(vis);
/* 623 */       Timing.endTime();
/* 624 */       if (TRegexTreeVisitor.printMatches)
/* 625 */         System.err.println("There were " + vis.numMatches() + " matches in total.");
/* 626 */       if (TRegexTreeVisitor.printNumMatchesToStdOut)
/* 627 */         System.out.println(vis.numMatches());
/*     */     } catch (IOException e) {
/* 629 */       e.printStackTrace();
/*     */     } catch (ParseException e) {
/* 631 */       System.err.println("Error parsing expression: " + args[0]);
/* 632 */       System.err.println("Parse exception: " + e.toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static class TRegexTreeVisitor
/*     */     implements TreeVisitor
/*     */   {
/* 640 */     private static boolean printNumMatchesToStdOut = false;
/* 641 */     static boolean printNonMatchingTrees = false;
/* 642 */     static boolean printSubtreeCode = false;
/* 643 */     static boolean printTree = false;
/* 644 */     static boolean printWholeTree = false;
/* 645 */     static boolean printMatches = true;
/* 646 */     static boolean printFilename = false;
/* 647 */     static boolean oneMatchPerRootNode = false;
/* 648 */     static boolean reportTreeNumbers = false;
/*     */     
/*     */     static TreePrint tp;
/*     */     
/*     */     PrintWriter pw;
/* 653 */     int treeNumber = 0;
/*     */     TregexPattern p;
/*     */     String[] handles;
/*     */     int numMatches;
/*     */     
/*     */     TRegexTreeVisitor(TregexPattern p, String[] handles, String encoding)
/*     */     {
/* 660 */       this.p = p;
/* 661 */       this.handles = handles;
/*     */       try {
/* 663 */         this.pw = new PrintWriter(new OutputStreamWriter(System.out, encoding), true);
/*     */       }
/*     */       catch (UnsupportedEncodingException e) {
/* 666 */         System.err.println("Error -- encoding " + encoding + " is unsupported.  Using ASCII print writer instead.");
/* 667 */         this.pw = new PrintWriter(System.out, true);
/*     */       }
/* 669 */       tp.setPrintWriter(this.pw);
/*     */     }
/*     */     
/*     */     public void visitTree(Tree t) {
/* 673 */       this.treeNumber += 1;
/* 674 */       if (printTree) {
/* 675 */         this.pw.print(this.treeNumber + ":");
/* 676 */         this.pw.println("Next tree read:");
/* 677 */         tp.printTree(t, this.pw);
/*     */       }
/* 679 */       TregexMatcher match = this.p.matcher(t);
/* 680 */       if (printNonMatchingTrees) {
/* 681 */         if (match.find()) {
/* 682 */           this.numMatches += 1;
/*     */         } else
/* 684 */           tp.printTree(t, this.pw);
/* 685 */         return;
/*     */       }
/* 687 */       Tree lastMatchingRootNode = null;
/* 688 */       while (match.find()) {
/* 689 */         if (oneMatchPerRootNode) {
/* 690 */           if (lastMatchingRootNode != match.getMatch())
/*     */           {
/*     */ 
/* 693 */             lastMatchingRootNode = match.getMatch(); }
/*     */         } else {
/* 695 */           this.numMatches += 1;
/* 696 */           if ((printFilename) && ((TregexPattern.treebank instanceof DiskTreebank))) {
/* 697 */             DiskTreebank dtb = (DiskTreebank)TregexPattern.treebank;
/* 698 */             this.pw.print("# ");
/* 699 */             this.pw.println(dtb.getCurrentFile());
/*     */           }
/* 701 */           if (printSubtreeCode) {
/* 702 */             this.pw.println(this.treeNumber + ":" + match.getMatch().nodeNumber(t));
/*     */           }
/* 704 */           if (printMatches) {
/* 705 */             if (reportTreeNumbers) {
/* 706 */               this.pw.print(this.treeNumber + ": ");
/*     */             }
/* 708 */             if (printTree) {
/* 709 */               this.pw.println("Found a full match:");
/*     */             }
/* 711 */             if (printWholeTree) {
/* 712 */               tp.printTree(t, this.pw);
/* 713 */             } else if (this.handles != null) {
/* 714 */               if (printTree) {
/* 715 */                 this.pw.println("Here's the node you were interested in:");
/*     */               }
/* 717 */               for (String handle : this.handles) {
/* 718 */                 Tree labeledNode = match.getNode(handle);
/* 719 */                 if (labeledNode == null) {
/* 720 */                   System.err.println("ACK!!  There is no matched node \"" + this.handles + "\"!  Did you specify such a label in the pattern?");
/*     */                 } else {
/* 722 */                   tp.printTree(labeledNode, this.pw);
/*     */                 }
/*     */               }
/*     */             } else {
/* 726 */               tp.printTree(match.getMatch(), this.pw);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public int numMatches() {
/* 734 */       return this.numMatches;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\TregexPattern.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */