/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.io.NumberRangeFileFilter;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.ling.WordFactory;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TreebankAnnotator
/*     */ {
/*     */   TreeTransformer treeTransformer;
/*     */   TreeTransformer treeUnTransformer;
/*     */   TreeTransformer collinizer;
/*     */   
/*     */   public List annotateTrees(List trees)
/*     */   {
/*  24 */     List annotatedTrees = new ArrayList();
/*  25 */     for (Iterator treeI = trees.iterator(); treeI.hasNext();) {
/*  26 */       Tree tree = (Tree)treeI.next();
/*  27 */       annotatedTrees.add(this.treeTransformer.transformTree(tree));
/*     */     }
/*  29 */     return annotatedTrees;
/*     */   }
/*     */   
/*     */   public List deannotateTrees(List trees) {
/*  33 */     List deannotatedTrees = new ArrayList();
/*  34 */     for (Iterator treeI = trees.iterator(); treeI.hasNext();) {
/*  35 */       Tree tree = (Tree)treeI.next();
/*  36 */       deannotatedTrees.add(this.treeUnTransformer.transformTree(tree));
/*     */     }
/*  38 */     return deannotatedTrees;
/*     */   }
/*     */   
/*     */   public static Pair extractGrammars(List trees)
/*     */   {
/*  43 */     BinaryGrammarExtractor binaryGrammarExtractor = new BinaryGrammarExtractor();
/*  44 */     Pair pair = (Pair)binaryGrammarExtractor.extract(trees);
/*  45 */     return pair;
/*     */   }
/*     */   
/*     */   public static Lexicon extractLexicon(List trees, Options op) {
/*  49 */     Lexicon lexicon = op.tlpParams.lex(op.lexOptions);
/*  50 */     lexicon.train(trees);
/*  51 */     return lexicon;
/*     */   }
/*     */   
/*     */   public static List getTrees(String path, int low, int high, int minLength, int maxLength) {
/*  55 */     Treebank treebank = new edu.stanford.nlp.trees.DiskTreebank(new edu.stanford.nlp.trees.TreeReaderFactory() {
/*     */       public edu.stanford.nlp.trees.TreeReader newTreeReader(Reader in) {
/*  57 */         return new edu.stanford.nlp.trees.PennTreeReader(in, new LabeledScoredTreeFactory(new WordFactory()), new edu.stanford.nlp.trees.BobChrisTreeNormalizer());
/*     */       }
/*  59 */     });
/*  60 */     treebank.loadPath(path, new NumberRangeFileFilter(low, high, true));
/*  61 */     List trees = new ArrayList();
/*  62 */     for (Iterator treeI = treebank.iterator(); treeI.hasNext();) {
/*  63 */       Tree tree = (Tree)treeI.next();
/*  64 */       if ((tree.yield().size() <= maxLength) && (tree.yield().size() >= minLength)) {
/*  65 */         trees.add(tree);
/*     */       }
/*     */     }
/*  68 */     return trees;
/*     */   }
/*     */   
/*     */   public static List removeDependencyRoots(List trees) {
/*  72 */     List prunedTrees = new ArrayList();
/*  73 */     for (Iterator treeI = trees.iterator(); treeI.hasNext();) {
/*  74 */       Tree tree = (Tree)treeI.next();
/*  75 */       prunedTrees.add(removeDependencyRoot(tree));
/*     */     }
/*  77 */     return prunedTrees;
/*     */   }
/*     */   
/*     */   static Tree removeDependencyRoot(Tree tree) {
/*  81 */     List childList = tree.getChildrenAsList();
/*  82 */     Tree last = (Tree)childList.get(childList.size() - 1);
/*  83 */     if (!last.label().value().equals(".$$.")) {
/*  84 */       return tree;
/*     */     }
/*  86 */     List lastGoneList = childList.subList(0, childList.size() - 1);
/*  87 */     tree.setChildren(lastGoneList);
/*  88 */     return tree;
/*     */   }
/*     */   
/*     */   public Tree collinize(Tree tree) {
/*  92 */     return this.collinizer.transformTree(tree);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreebankAnnotator(Options op, String treebankRoot)
/*     */   {
/* 100 */     Train.splitters = ParentAnnotationStats.getEnglishSplitCategories(treebankRoot);
/* 101 */     Train.sisterSplitters = new java.util.HashSet(java.util.Arrays.asList(op.tlpParams.sisterSplitters()));
/* 102 */     op.setOptions(new String[] { "-acl03pcfg", "-cnf" });
/* 103 */     this.treeTransformer = new TreeAnnotatorAndBinarizer(op.tlpParams, op.forceCNF, !Train.outsideFactor(), true);
/*     */     
/* 105 */     this.treeUnTransformer = new Debinarizer(op.forceCNF);
/* 106 */     this.collinizer = op.tlpParams.collinizer();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/* 111 */     edu.stanford.nlp.ling.CategoryWordTag.printWordTag = false;
/* 112 */     String path = args[0];
/* 113 */     List trees = getTrees(path, 200, 219, 0, 10);
/* 114 */     ((Tree)trees.iterator().next()).pennPrint();
/* 115 */     Options op = new Options();
/* 116 */     List annotatedTrees = removeDependencyRoots(new TreebankAnnotator(op, path).annotateTrees(trees));
/* 117 */     ((Tree)annotatedTrees.iterator().next()).pennPrint();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\TreebankAnnotator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */