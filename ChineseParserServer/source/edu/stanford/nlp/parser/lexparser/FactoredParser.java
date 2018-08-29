/*      */ package edu.stanford.nlp.parser.lexparser;
/*      */ 
/*      */ import edu.stanford.nlp.io.NumberRangeFileFilter;
/*      */ import edu.stanford.nlp.ling.Sentence;
/*      */ import edu.stanford.nlp.ling.SentenceProcessor;
/*      */ import edu.stanford.nlp.ling.TaggedWord;
/*      */ import edu.stanford.nlp.ling.Word;
/*      */ import edu.stanford.nlp.trees.LeftHeadFinder;
/*      */ import edu.stanford.nlp.trees.MemoryTreebank;
/*      */ import edu.stanford.nlp.trees.Tree;
/*      */ import edu.stanford.nlp.trees.TreeLengthComparator;
/*      */ import edu.stanford.nlp.trees.TreeTransformer;
/*      */ import edu.stanford.nlp.trees.Treebank;
/*      */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*      */ import edu.stanford.nlp.util.Numberer;
/*      */ import edu.stanford.nlp.util.Pair;
/*      */ import edu.stanford.nlp.util.Timing;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FactoredParser
/*      */ {
/*      */   public static void main(String[] args)
/*      */   {
/*  771 */     Options op = new Options(new EnglishTreebankParserParams());
/*      */     
/*      */ 
/*      */ 
/*  775 */     System.out.println("Currently " + new Date());
/*  776 */     System.out.print("Invoked with arguments:");
/*  777 */     for (String arg : args) {
/*  778 */       System.out.print(" " + arg);
/*      */     }
/*  780 */     System.out.println();
/*      */     
/*  782 */     String path = "/u/nlp/stuff/corpora/Treebank3/parsed/mrg/wsj";
/*  783 */     int trainLow = 200;int trainHigh = 2199;int testLow = 2200;int testHigh = 2219;
/*  784 */     String serializeFile = null;
/*      */     
/*  786 */     int i = 0;
/*  787 */     while ((i < args.length) && (args[i].startsWith("-"))) {
/*  788 */       if ((args[i].equalsIgnoreCase("-path")) && (i + 1 < args.length)) {
/*  789 */         path = args[(i + 1)];
/*  790 */         i += 2;
/*  791 */       } else if ((args[i].equalsIgnoreCase("-train")) && (i + 2 < args.length)) {
/*  792 */         trainLow = Integer.parseInt(args[(i + 1)]);
/*  793 */         trainHigh = Integer.parseInt(args[(i + 2)]);
/*  794 */         i += 3;
/*  795 */       } else if ((args[i].equalsIgnoreCase("-test")) && (i + 2 < args.length)) {
/*  796 */         testLow = Integer.parseInt(args[(i + 1)]);
/*  797 */         testHigh = Integer.parseInt(args[(i + 2)]);
/*  798 */         i += 3;
/*  799 */       } else if ((args[i].equalsIgnoreCase("-serialize")) && (i + 1 < args.length)) {
/*  800 */         serializeFile = args[(i + 1)];
/*  801 */         i += 2;
/*  802 */       } else if ((args[i].equalsIgnoreCase("-tLPP")) && (i + 1 < args.length)) {
/*      */         try {
/*  804 */           op.tlpParams = ((TreebankLangParserParams)Class.forName(args[(i + 1)]).newInstance());
/*      */         } catch (ClassNotFoundException e) {
/*  806 */           System.err.println("Class not found: " + args[(i + 1)]);
/*      */         } catch (InstantiationException e) {
/*  808 */           System.err.println("Couldn't instantiate: " + args[(i + 1)] + ": " + e.toString());
/*      */         } catch (IllegalAccessException e) {
/*  810 */           System.err.println("illegal access" + e);
/*      */         }
/*  812 */         i += 2;
/*  813 */       } else if (args[i].equals("-encoding"))
/*      */       {
/*  815 */         op.tlpParams.setInputEncoding(args[(i + 1)]);
/*  816 */         op.tlpParams.setOutputEncoding(args[(i + 1)]);
/*  817 */         i += 2;
/*      */       } else {
/*  819 */         i = op.setOptionOrWarn(args, i);
/*      */       }
/*      */     }
/*      */     
/*  823 */     TreebankLanguagePack tlp = op.tlpParams.treebankLanguagePack();
/*      */     
/*  825 */     Train.sisterSplitters = new HashSet(Arrays.asList(op.tlpParams.sisterSplitters()));
/*      */     
/*  827 */     PrintWriter pw = op.tlpParams.pw();
/*      */     
/*  829 */     Test.display();
/*  830 */     Train.display();
/*  831 */     op.display();
/*  832 */     op.tlpParams.display();
/*      */     
/*      */ 
/*  835 */     Treebank trainTreebank = op.tlpParams.memoryTreebank();
/*  836 */     MemoryTreebank testTreebank = op.tlpParams.testMemoryTreebank();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  841 */     Timing.startTime();
/*  842 */     System.err.print("Reading trees...");
/*  843 */     testTreebank.loadPath(path, new NumberRangeFileFilter(testLow, testHigh, true));
/*  844 */     if (Test.increasingLength) {
/*  845 */       Collections.sort(testTreebank, new TreeLengthComparator());
/*      */     }
/*      */     
/*  848 */     trainTreebank.loadPath(path, new NumberRangeFileFilter(trainLow, trainHigh, true));
/*  849 */     Timing.tick("done.");
/*  850 */     System.err.print("Binarizing trees...");
/*  851 */     TreeAnnotatorAndBinarizer binarizer = null;
/*  852 */     if (!Train.leftToRight) {
/*  853 */       binarizer = new TreeAnnotatorAndBinarizer(op.tlpParams, op.forceCNF, !Train.outsideFactor(), true);
/*      */     } else {
/*  855 */       binarizer = new TreeAnnotatorAndBinarizer(op.tlpParams.headFinder(), new LeftHeadFinder(), op.tlpParams, op.forceCNF, !Train.outsideFactor(), true);
/*      */     }
/*  857 */     CollinsPuncTransformer collinsPuncTransformer = null;
/*  858 */     if (Train.collinsPunc) {
/*  859 */       collinsPuncTransformer = new CollinsPuncTransformer(tlp);
/*      */     }
/*  861 */     TreeTransformer debinarizer = new Debinarizer(op.forceCNF);
/*  862 */     List<Tree> binaryTrainTrees = new ArrayList();
/*      */     
/*  864 */     if (Train.selectiveSplit) {
/*  865 */       Train.splitters = ParentAnnotationStats.getSplitCategories(trainTreebank, Train.tagSelectiveSplit, 0, Train.selectiveSplitCutOff, Train.tagSelectiveSplitCutOff, op.tlpParams.treebankLanguagePack());
/*  866 */       if (Train.deleteSplitters != null) {
/*  867 */         List<String> deleted = new ArrayList();
/*  868 */         for (Iterator i$ = Train.deleteSplitters.iterator(); i$.hasNext();) { del = (String)i$.next();
/*  869 */           baseDel = tlp.basicCategory(del);
/*  870 */           checkBasic = del.equals(baseDel);
/*  871 */           for (it = Train.splitters.iterator(); it.hasNext();) {
/*  872 */             String elem = (String)it.next();
/*  873 */             String baseElem = tlp.basicCategory(elem);
/*  874 */             boolean delStr = ((checkBasic) && (baseElem.equals(baseDel))) || (elem.equals(del));
/*      */             
/*  876 */             if (delStr) {
/*  877 */               it.remove();
/*  878 */               deleted.add(elem); } } }
/*      */         String del;
/*      */         String baseDel;
/*      */         boolean checkBasic;
/*  882 */         Iterator<String> it; System.err.println("Removed from vertical splitters: " + deleted);
/*      */       }
/*      */     }
/*  885 */     if (Train.selectivePostSplit) {
/*  886 */       TreeTransformer myTransformer = new TreeAnnotator(op.tlpParams.headFinder(), op.tlpParams);
/*  887 */       Treebank annotatedTB = trainTreebank.transform(myTransformer);
/*  888 */       Train.postSplitters = ParentAnnotationStats.getSplitCategories(annotatedTB, true, 0, Train.selectivePostSplitCutOff, Train.tagSelectivePostSplitCutOff, op.tlpParams.treebankLanguagePack());
/*      */     }
/*      */     
/*  891 */     if (Train.hSelSplit) {
/*  892 */       binarizer.setDoSelectiveSplit(false);
/*  893 */       for (Tree tree : trainTreebank) {
/*  894 */         if (Train.collinsPunc) {
/*  895 */           tree = collinsPuncTransformer.transformTree(tree);
/*      */         }
/*      */         
/*  898 */         tree = binarizer.transformTree(tree);
/*      */       }
/*      */       
/*  901 */       binarizer.setDoSelectiveSplit(true);
/*      */     }
/*  903 */     for (Tree tree : trainTreebank) {
/*  904 */       if (Train.collinsPunc) {
/*  905 */         tree = collinsPuncTransformer.transformTree(tree);
/*      */       }
/*  907 */       tree = binarizer.transformTree(tree);
/*  908 */       binaryTrainTrees.add(tree);
/*      */     }
/*  910 */     if (Test.verbose) {
/*  911 */       binarizer.dumpStats();
/*      */     }
/*      */     
/*  914 */     List<Tree> binaryTestTrees = new ArrayList();
/*  915 */     for (Tree tree : testTreebank) {
/*  916 */       if (Train.collinsPunc) {
/*  917 */         tree = collinsPuncTransformer.transformTree(tree);
/*      */       }
/*  919 */       tree = binarizer.transformTree(tree);
/*  920 */       binaryTestTrees.add(tree);
/*      */     }
/*  922 */     Timing.tick("done.");
/*  923 */     BinaryGrammar bg = null;
/*  924 */     UnaryGrammar ug = null;
/*  925 */     DependencyGrammar dg = null;
/*      */     
/*  927 */     Lexicon lex = null;
/*      */     
/*  929 */     Extractor bgExtractor = new BinaryGrammarExtractor();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  935 */     Extractor dgExtractor = new MLEDependencyGrammarExtractor(op);
/*  936 */     if (op.doPCFG) {
/*  937 */       System.err.print("Extracting PCFG...");
/*  938 */       Pair bgug = null;
/*  939 */       if (Train.cheatPCFG) {
/*  940 */         List allTrees = new ArrayList(binaryTrainTrees);
/*  941 */         allTrees.addAll(binaryTestTrees);
/*  942 */         bgug = (Pair)bgExtractor.extract(allTrees);
/*      */       } else {
/*  944 */         bgug = (Pair)bgExtractor.extract(binaryTrainTrees);
/*      */       }
/*  946 */       bg = (BinaryGrammar)bgug.second;
/*  947 */       bg.splitRules();
/*  948 */       ug = (UnaryGrammar)bgug.first;
/*  949 */       ug.purgeRules();
/*  950 */       Timing.tick("done.");
/*      */     }
/*  952 */     System.err.print("Extracting Lexicon...");
/*  953 */     lex = op.tlpParams.lex(op.lexOptions);
/*  954 */     lex.train(binaryTrainTrees);
/*  955 */     Timing.tick("done.");
/*      */     
/*  957 */     if (op.doDep) {
/*  958 */       System.err.print("Extracting Dependencies...");
/*  959 */       binaryTrainTrees.clear();
/*      */       
/*      */ 
/*  962 */       DependencyGrammar dg1 = (DependencyGrammar)dgExtractor.extract(trainTreebank.iterator(), new TransformTreeDependency(op.tlpParams, true));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  968 */       Timing.tick("done.");
/*      */       
/*      */ 
/*      */ 
/*  972 */       System.out.print("Tuning Dependency Model...");
/*  973 */       dg.tune(binaryTestTrees);
/*      */       
/*  975 */       Timing.tick("done.");
/*      */     }
/*      */     
/*  978 */     BinaryGrammar boundBG = bg;
/*  979 */     UnaryGrammar boundUG = ug;
/*      */     
/*  981 */     GrammarProjection gp = new NullGrammarProjection(bg, ug);
/*      */     
/*      */ 
/*  984 */     if (serializeFile != null) {
/*  985 */       System.err.print("Serializing parser...");
/*  986 */       LexicalizedParser.saveParserDataToSerialized(new ParserData(lex, bg, ug, dg, Numberer.getNumberers(), op), serializeFile);
/*  987 */       Timing.tick("done.");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  992 */     ExhaustivePCFGParser parser = null;
/*  993 */     if (op.doPCFG) {
/*  994 */       parser = new ExhaustivePCFGParser(boundBG, boundUG, lex, op);
/*      */     }
/*      */     
/*      */ 
/*  998 */     ExhaustiveDependencyParser dparser = (op.doDep) && (!Test.useFastFactored) ? new ExhaustiveDependencyParser(dg, lex, op) : null;
/*      */     
/* 1000 */     Scorer scorer = op.doPCFG ? new TwinScorer(new ProjectionScorer(parser, gp), dparser) : null;
/*      */     
/* 1002 */     BiLexPCFGParser bparser = null;
/* 1003 */     if ((op.doPCFG) && (op.doDep)) {
/* 1004 */       bparser = Test.useN5 ? new BiLexPCFGParser.N5BiLexPCFGParser(scorer, parser, dparser, bg, ug, dg, lex, op, gp) : new BiLexPCFGParser(scorer, parser, dparser, bg, ug, dg, lex, op, gp);
/*      */     }
/*      */     
/* 1007 */     LabeledConstituentEval pcfgPE = new LabeledConstituentEval("pcfg  PE", true, tlp);
/* 1008 */     LabeledConstituentEval comboPE = new LabeledConstituentEval("combo PE", true, tlp);
/* 1009 */     AbstractEval pcfgCB = new LabeledConstituentEval.CBEval("pcfg  CB", true, tlp);
/*      */     
/* 1011 */     AbstractEval pcfgTE = new AbstractEval.TaggingEval("pcfg  TE");
/* 1012 */     AbstractEval comboTE = new AbstractEval.TaggingEval("combo TE");
/* 1013 */     AbstractEval pcfgTEnoPunct = new AbstractEval.TaggingEval("pcfg nopunct TE");
/* 1014 */     AbstractEval comboTEnoPunct = new AbstractEval.TaggingEval("combo nopunct TE");
/* 1015 */     AbstractEval depTE = new AbstractEval.TaggingEval("depnd TE");
/*      */     
/* 1017 */     AbstractEval depDE = new AbstractEval.DependencyEval("depnd DE", true, tlp.punctuationWordAcceptFilter());
/* 1018 */     AbstractEval comboDE = new AbstractEval.DependencyEval("combo DE", true, tlp.punctuationWordAcceptFilter());
/*      */     
/* 1020 */     if (Test.evalb) {
/* 1021 */       EvalB.initEVALBfiles(op.tlpParams);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1028 */     SentenceProcessor tagger = null;
/* 1029 */     if (Test.preTag) {
/*      */       try {
/* 1031 */         Class[] argsClass = { String.class };
/* 1032 */         Object[] arguments = { "/u/nlp/data/pos-tagger/wsj3t0-18-bidirectional/train-wsj-0-18.holder" };
/* 1033 */         tagger = (SentenceProcessor)Class.forName("edu.stanford.nlp.tagger.maxent.MaxentTagger").getConstructor(argsClass).newInstance(arguments);
/*      */       } catch (Exception e) {
/* 1035 */         System.err.println(e);
/* 1036 */         System.err.println("Warning: No pretagging of sentences will be done.");
/*      */       }
/*      */     }
/*      */     
/* 1040 */     int tNum = 0; for (int ttSize = testTreebank.size(); tNum < ttSize; tNum++) {
/* 1041 */       Tree tree = testTreebank.get(tNum);
/* 1042 */       int testTreeLen = tree.yield().size();
/* 1043 */       if (testTreeLen <= Test.maxLength)
/*      */       {
/*      */ 
/* 1046 */         Tree binaryTree = (Tree)binaryTestTrees.get(tNum);
/*      */         
/* 1048 */         System.out.println("-------------------------------------");
/* 1049 */         System.out.println("Number: " + (tNum + 1));
/* 1050 */         System.out.println("Length: " + testTreeLen);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1060 */         long timeMil1 = System.currentTimeMillis();
/* 1061 */         Timing.tick("Starting parse.");
/* 1062 */         if (op.doPCFG)
/*      */         {
/* 1064 */           if (Test.forceTags) {
/* 1065 */             if (tagger != null)
/*      */             {
/*      */ 
/* 1068 */               parser.parse(addLast(tagger.processSentence(cutLast(wordify(binaryTree.yield())))));
/*      */             }
/*      */             else {
/* 1071 */               parser.parse(cleanTags(binaryTree.taggedYield(), tlp));
/*      */             }
/*      */           }
/*      */           else {
/* 1075 */             parser.parse(binaryTree.yield());
/*      */           }
/*      */         }
/*      */         
/* 1079 */         if (op.doDep) {
/* 1080 */           dparser.parse(binaryTree.yield());
/*      */         }
/*      */         
/* 1083 */         boolean bothPassed = false;
/* 1084 */         if ((op.doPCFG) && (op.doDep)) {
/* 1085 */           bothPassed = bparser.parse(binaryTree.yield());
/*      */         }
/*      */         
/* 1088 */         long timeMil2 = System.currentTimeMillis();
/* 1089 */         long elapsed = timeMil2 - timeMil1;
/* 1090 */         System.err.println("Time: " + (int)(elapsed / 100L) / 10.0D + " sec.");
/*      */         
/* 1092 */         Tree tree2b = null;
/* 1093 */         Tree tree2 = null;
/*      */         
/* 1095 */         if (op.doPCFG) {
/* 1096 */           tree2b = parser.getBestParse();
/* 1097 */           tree2 = debinarizer.transformTree(tree2b);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1102 */         Tree tree3 = null;
/* 1103 */         Tree tree3db = null;
/* 1104 */         if (op.doDep) {
/* 1105 */           tree3 = dparser.getBestParse();
/*      */           
/* 1107 */           tree3db = debinarizer.transformTree(tree3);
/* 1108 */           tree3.pennPrint(pw);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1113 */         Tree tree4 = null;
/* 1114 */         if ((op.doPCFG) && (op.doDep)) {
/*      */           try {
/* 1116 */             tree4 = bparser.getBestParse();
/* 1117 */             if (tree4 == null) {
/* 1118 */               tree4 = tree2b;
/*      */             }
/*      */           } catch (NullPointerException e) {
/* 1121 */             System.err.println("Blocked, using PCFG parse!");
/* 1122 */             tree4 = tree2b;
/*      */           }
/*      */         }
/* 1125 */         if ((op.doPCFG) && (!bothPassed)) {
/* 1126 */           tree4 = tree2b;
/*      */         }
/*      */         
/* 1129 */         if (op.doDep) {
/* 1130 */           depDE.evaluate(tree3, binaryTree, pw);
/* 1131 */           depTE.evaluate(tree3db, tree, pw);
/*      */         }
/* 1133 */         TreeTransformer tc = op.tlpParams.collinizer();
/* 1134 */         TreeTransformer tcEvalb = op.tlpParams.collinizerEvalb();
/* 1135 */         Tree tree4b = null;
/* 1136 */         if (op.doPCFG)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1144 */           pcfgPE.evaluate(tc.transformTree(tree2), tc.transformTree(tree), pw);
/* 1145 */           pcfgCB.evaluate(tc.transformTree(tree2), tc.transformTree(tree), pw);
/* 1146 */           if (op.doDep) {
/* 1147 */             comboDE.evaluate(bothPassed ? tree4 : tree3, binaryTree, pw);
/* 1148 */             tree4b = tree4;
/* 1149 */             tree4 = debinarizer.transformTree(tree4);
/* 1150 */             if (op.nodePrune) {
/* 1151 */               NodePruner np = new NodePruner(parser, debinarizer);
/* 1152 */               tree4 = np.prune(tree4);
/*      */             }
/*      */             
/* 1155 */             comboPE.evaluate(tc.transformTree(tree4), tc.transformTree(tree), pw);
/*      */           }
/*      */           
/* 1158 */           pcfgTE.evaluate(tcEvalb.transformTree(tree2), tcEvalb.transformTree(tree), pw);
/* 1159 */           pcfgTEnoPunct.evaluate(tc.transformTree(tree2), tc.transformTree(tree), pw);
/*      */           
/* 1161 */           if (op.doDep) {
/* 1162 */             comboTE.evaluate(tcEvalb.transformTree(tree4), tcEvalb.transformTree(tree), pw);
/* 1163 */             comboTEnoPunct.evaluate(tc.transformTree(tree4), tc.transformTree(tree), pw);
/*      */           }
/* 1165 */           System.out.println("PCFG only: " + parser.scoreBinarizedTree(tree2b, 0));
/*      */           
/*      */ 
/* 1168 */           tree2.pennPrint(pw);
/*      */           
/* 1170 */           if (op.doDep) {
/* 1171 */             System.out.println("Combo: " + parser.scoreBinarizedTree(tree4b, 0));
/*      */             
/* 1173 */             tree4.pennPrint(pw);
/*      */           }
/* 1175 */           System.out.println("Correct:" + parser.scoreBinarizedTree(binaryTree, 0));
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1182 */           tree.pennPrint(pw);
/*      */         }
/*      */         
/* 1185 */         if (Test.evalb) {
/* 1186 */           if ((op.doPCFG) && (op.doDep)) {
/* 1187 */             EvalB.writeEVALBline(tcEvalb.transformTree(tree), tcEvalb.transformTree(tree4));
/* 1188 */           } else if (op.doPCFG) {
/* 1189 */             EvalB.writeEVALBline(tcEvalb.transformTree(tree), tcEvalb.transformTree(tree2));
/* 1190 */           } else if (op.doDep) {
/* 1191 */             EvalB.writeEVALBline(tcEvalb.transformTree(tree), tcEvalb.transformTree(tree3db));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1196 */     if (Test.evalb) {
/* 1197 */       EvalB.closeEVALBfiles();
/*      */     }
/*      */     
/*      */ 
/* 1201 */     if (op.doPCFG) {
/* 1202 */       pcfgPE.display(false, pw);
/* 1203 */       System.out.println("Grammar size: " + Numberer.getGlobalNumberer("states").total());
/* 1204 */       pcfgCB.display(false, pw);
/* 1205 */       if (op.doDep) {
/* 1206 */         comboPE.display(false, pw);
/*      */       }
/* 1208 */       pcfgTE.display(false, pw);
/* 1209 */       pcfgTEnoPunct.display(false, pw);
/* 1210 */       if (op.doDep) {
/* 1211 */         comboTE.display(false, pw);
/* 1212 */         comboTEnoPunct.display(false, pw);
/*      */       }
/*      */     }
/* 1215 */     if (op.doDep) {
/* 1216 */       depTE.display(false, pw);
/* 1217 */       depDE.display(false, pw);
/*      */     }
/* 1219 */     if ((op.doPCFG) && (op.doDep)) {
/* 1220 */       comboDE.display(false, pw);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static List<TaggedWord> cleanTags(List twList, TreebankLanguagePack tlp)
/*      */   {
/* 1227 */     int sz = twList.size();
/* 1228 */     List<TaggedWord> l = new ArrayList(sz);
/* 1229 */     for (int i = 0; i < sz; i++) {
/* 1230 */       TaggedWord tw = (TaggedWord)twList.get(i);
/* 1231 */       TaggedWord tw2 = new TaggedWord(tw.word(), tlp.basicCategory(tw.tag()));
/* 1232 */       l.add(tw2);
/*      */     }
/* 1234 */     return l;
/*      */   }
/*      */   
/*      */   private static Sentence wordify(List wList) {
/* 1238 */     Sentence s = new Sentence();
/* 1239 */     for (Object obj : wList) {
/* 1240 */       s.add(new Word(obj.toString()));
/*      */     }
/* 1242 */     return s;
/*      */   }
/*      */   
/*      */   private static Sentence cutLast(Sentence s) {
/* 1246 */     return new Sentence(s.subList(0, s.size() - 1));
/*      */   }
/*      */   
/*      */   private static Sentence addLast(Sentence s) {
/* 1250 */     Sentence s2 = new Sentence(s);
/*      */     
/* 1252 */     s2.add(new Word(".$."));
/* 1253 */     return s2;
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\FactoredParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */