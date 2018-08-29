/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.io.ExtensionFileFilter;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.util.Sets;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Treebank
/*     */   extends AbstractCollection<Tree>
/*     */ {
/*     */   private TreeReaderFactory trf;
/*  36 */   private String encoding = "UTF-8";
/*     */   
/*     */ 
/*     */   public static final String DEFAULT_TREE_FILE_SUFFIX = "mrg";
/*     */   
/*     */ 
/*     */   public Treebank()
/*     */   {
/*  44 */     this(new LabeledScoredTreeReaderFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Treebank(TreeReaderFactory trf)
/*     */   {
/*  55 */     this.trf = trf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Treebank(TreeReaderFactory trf, String encoding)
/*     */   {
/*  67 */     this.trf = trf;
/*  68 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Treebank(int initialCapacity)
/*     */   {
/*  79 */     this(initialCapacity, new LabeledScoredTreeReaderFactory());
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
/*     */   public Treebank(int initialCapacity, TreeReaderFactory trf)
/*     */   {
/*  92 */     this.trf = trf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TreeReaderFactory treeReaderFactory()
/*     */   {
/* 104 */     return this.trf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String encoding()
/*     */   {
/* 112 */     return this.encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void clear();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadPath(String pathName)
/*     */   {
/* 130 */     loadPath(new File(pathName));
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
/*     */   public void loadPath(File path)
/*     */   {
/* 144 */     loadPath(path, "mrg", true);
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
/*     */   public void loadPath(String pathName, String suffix, boolean recursively)
/*     */   {
/* 161 */     loadPath(new File(pathName), new ExtensionFileFilter(suffix, recursively));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadPath(File path, String suffix, boolean recursively)
/*     */   {
/* 173 */     loadPath(path, new ExtensionFileFilter(suffix, recursively));
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
/*     */   public void loadPath(String pathName, FileFilter filt)
/*     */   {
/* 186 */     loadPath(new File(pathName), filt);
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
/*     */   public abstract void loadPath(File paramFile, FileFilter paramFileFilter);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void apply(TreeVisitor paramTreeVisitor);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Treebank transform(TreeTransformer treeTrans)
/*     */   {
/* 217 */     return new TransformingTreebank(this, treeTrans);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 226 */     final StringBuilder sb = new StringBuilder();
/* 227 */     apply(new TreeVisitor() {
/*     */       public void visitTree(Tree t) {
/* 229 */         sb.append(t.toString());
/* 230 */         sb.append("\n");
/*     */       }
/* 232 */     });
/* 233 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static final class CounterTreeProcessor implements TreeVisitor
/*     */   {
/*     */     int i;
/*     */     
/*     */     public void visitTree(Tree t) {
/* 241 */       this.i += 1;
/*     */     }
/*     */     
/*     */     public int total() {
/* 245 */       return this.i;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 256 */     CounterTreeProcessor counter = new CounterTreeProcessor(null);
/* 257 */     apply(counter);
/* 258 */     return counter.total();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void decimate(Writer trainW, Writer devW, Writer testW)
/*     */     throws IOException
/*     */   {
/* 266 */     PrintWriter trainPW = new PrintWriter(trainW, true);
/* 267 */     PrintWriter devPW = new PrintWriter(devW, true);
/* 268 */     PrintWriter testPW = new PrintWriter(testW, true);
/* 269 */     int i = 0;
/* 270 */     for (Tree t : this) {
/* 271 */       if (i == 8) {
/* 272 */         t.pennPrint(devPW);
/* 273 */       } else if (i == 9) {
/* 274 */         t.pennPrint(testPW);
/*     */       } else {
/* 276 */         t.pennPrint(trainPW);
/*     */       }
/* 278 */       i = (i + 1) % 10;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String textualSummary()
/*     */   {
/* 287 */     return textualSummary(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String textualSummary(TreebankLanguagePack tlp)
/*     */   {
/* 295 */     int numTrees = 0;
/* 296 */     int numNonUnaryRoots = 0;
/* 297 */     Counter<Tree> nonUnaries = new Counter();
/* 298 */     Counter<String> roots = new Counter();
/* 299 */     Counter<String> starts = new Counter();
/* 300 */     Counter<String> puncts = new Counter();
/* 301 */     int numUnenclosedLeaves = 0;
/* 302 */     int numLeaves = 0;
/* 303 */     int numNonPhrasal = 0;
/* 304 */     int numWords = 0;
/* 305 */     int numTags = 0;
/* 306 */     int shortestSentence = Integer.MAX_VALUE;
/* 307 */     int longestSentence = 0;
/* 308 */     Set<String> words = new HashSet();
/* 309 */     Counter<String> tags = new Counter();
/* 310 */     Counter<String> cats = new Counter();
/* 311 */     Tree leafEg = null;
/* 312 */     for (Tree t : this) {
/* 313 */       roots.incrementCount(t.value());
/* 314 */       numTrees++;
/* 315 */       int leng = t.yield().length();
/* 316 */       if (leng < shortestSentence) {
/* 317 */         shortestSentence = leng;
/*     */       }
/* 319 */       if (leng > longestSentence) {
/* 320 */         longestSentence = leng;
/*     */       }
/* 322 */       if (t.numChildren() > 1) {
/* 323 */         numNonUnaryRoots++;
/* 324 */         nonUnaries.incrementCount(t.localTree());
/* 325 */       } else if (t.isLeaf()) {
/* 326 */         numUnenclosedLeaves++;
/*     */       } else {
/* 328 */         Tree t2 = t.firstChild();
/* 329 */         if (t2.isLeaf()) {
/* 330 */           numLeaves++;
/* 331 */           leafEg = t;
/* 332 */         } else if (t2.isPreTerminal()) {
/* 333 */           numNonPhrasal++;
/*     */         }
/* 335 */         starts.incrementCount(t2.value());
/*     */       }
/* 337 */       for (Tree subtree : t) {
/* 338 */         if (subtree.isLeaf()) {
/* 339 */           numWords++;
/* 340 */           words.add(subtree.value());
/* 341 */         } else if (subtree.isPreTerminal()) {
/* 342 */           numTags++;
/* 343 */           tags.incrementCount(subtree.value());
/* 344 */           if ((tlp != null) && (tlp.isPunctuationTag(subtree.value()))) {
/* 345 */             puncts.incrementCount(subtree.firstChild().value());
/*     */           }
/* 347 */         } else if (subtree.isPhrasal()) {
/* 348 */           cats.incrementCount(subtree.value());
/*     */         } else {
/* 350 */           throw new IllegalStateException("Bad tree in treebank!: " + subtree);
/*     */         }
/*     */       }
/*     */     }
/* 354 */     StringWriter sw = new StringWriter(2000);
/* 355 */     PrintWriter pw = new PrintWriter(sw);
/* 356 */     NumberFormat nf = NumberFormat.getNumberInstance();
/* 357 */     nf.setMaximumFractionDigits(0);
/* 358 */     pw.println("Treebank has " + numTrees + " trees and " + numWords + " words (tokens)");
/* 359 */     if (numTags != numWords) {
/* 360 */       pw.println("  Warning! numTags differs and is " + numTags);
/*     */     }
/* 362 */     if (roots.size() == 1) {
/* 363 */       String root = (String)roots.keySet().toArray()[0];
/* 364 */       pw.println("  The root category is: " + root);
/*     */     } else {
/* 366 */       pw.println("  Warning! " + roots.size() + " different roots in treebank: " + roots.toString(nf));
/*     */     }
/* 368 */     if (numNonUnaryRoots > 0) {
/* 369 */       pw.println("  Warning! " + numNonUnaryRoots + " trees without unary initial rewrite.  Subtrees: " + nonUnaries.toString(nf));
/*     */     }
/* 371 */     if ((numUnenclosedLeaves > 0) || (numLeaves > 0) || (numNonPhrasal > 0)) {
/* 372 */       pw.println("  Warning! Non-phrasal trees: " + numUnenclosedLeaves + " bare leaves; " + numLeaves + " root rewrites as leaf; and " + numNonPhrasal + " root rewrites as tagged word");
/* 373 */       if (numLeaves > 0) {
/* 374 */         pw.println("  Example bad root rewrites as leaf: " + leafEg);
/*     */       }
/*     */     }
/* 377 */     pw.println("  Sentences range from " + shortestSentence + " to " + longestSentence + " words, with an average length of " + numWords * 100 / numTrees / 100.0D + " words.");
/* 378 */     pw.println("  " + cats.size() + " phrasal category types, " + tags.size() + " tag types, and " + words.size() + " word types");
/* 379 */     String[] empties = { "*", "0", "*T*", "*RNR*", "*U*", "*?*", "*EXP*", "*ICH*", "*NOT*", "*PPA*", "*OP*", "*pro*", "*PRO*" };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 386 */     Set<String> knownEmpties = new HashSet(Arrays.asList(empties));
/* 387 */     Set emptiesIntersection = Sets.intersection(words, knownEmpties);
/* 388 */     if (emptiesIntersection.size() > 0) {
/* 389 */       pw.println("  Caution! " + emptiesIntersection.size() + " word types are known empty elements: " + emptiesIntersection);
/*     */     }
/*     */     
/*     */ 
/* 393 */     Set joint = Sets.intersection(cats.keySet(), tags.keySet());
/* 394 */     if (joint.size() > 0) {
/* 395 */       pw.println("  Warning! " + joint.size() + " items are tags and categories: " + joint);
/*     */     }
/* 397 */     pw.println("    Cats: " + cats.toString(nf));
/* 398 */     pw.println("    Tags: " + tags.toString(nf));
/* 399 */     pw.println("    " + starts.size() + " start categories: " + starts.toString(nf));
/* 400 */     if (!puncts.isEmpty()) {
/* 401 */       pw.println("    Puncts: " + puncts.toString(nf));
/*     */     }
/* 403 */     return sw.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean remove(Object o)
/*     */   {
/* 411 */     throw new UnsupportedOperationException("Treebank is read-only");
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\Treebank.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */