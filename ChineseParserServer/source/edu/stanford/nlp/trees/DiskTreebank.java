/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.io.NumberRangesFileFilter;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.util.FilePathProcessor;
/*     */ import edu.stanford.nlp.util.FileProcessor;
/*     */ import edu.stanford.nlp.util.Timing;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DiskTreebank
/*     */   extends Treebank
/*     */ {
/*     */   private static final boolean PRINT_FILENAMES = false;
/*  30 */   private ArrayList<File> filePaths = new ArrayList();
/*  31 */   private ArrayList<FileFilter> fileFilters = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  37 */   private File currentFile = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final boolean BROKEN_NFS = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DiskTreebank()
/*     */   {
/*  56 */     this(new LabeledScoredTreeReaderFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DiskTreebank(String encoding)
/*     */   {
/*  65 */     this(new LabeledScoredTreeReaderFactory(), encoding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DiskTreebank(TreeReaderFactory trf)
/*     */   {
/*  75 */     super(trf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DiskTreebank(TreeReaderFactory trf, String encoding)
/*     */   {
/*  86 */     super(trf, encoding);
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
/*     */   public DiskTreebank(int initialCapacity)
/*     */   {
/* 101 */     this(initialCapacity, new LabeledScoredTreeReaderFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DiskTreebank(int initialCapacity, TreeReaderFactory trf)
/*     */   {
/* 113 */     this(trf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 121 */     this.filePaths.clear();
/* 122 */     this.fileFilters.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadPath(File path, FileFilter filt)
/*     */   {
/* 133 */     this.filePaths.add(path);
/* 134 */     this.fileFilters.add(filt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void apply(TreeVisitor tp)
/*     */   {
/* 143 */     for (Tree t : this) {
/* 144 */       tp.visitTree(t);
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
/*     */   public File getCurrentFile()
/*     */   {
/* 162 */     return this.currentFile;
/*     */   }
/*     */   
/*     */   private class DiskTreebankIterator implements Iterator<Tree>
/*     */   {
/* 167 */     private int fileUpto = -1;
/*     */     private int treeUpto;
/*     */     private List<String> files;
/*     */     private MemoryTreebank currentFileTrees;
/*     */     private boolean hasNext;
/*     */     
/*     */     private DiskTreebankIterator() {
/* 174 */       this.files = new ArrayList();
/*     */       
/*     */ 
/* 177 */       FileProcessor dtifp = new FileProcessor() {
/*     */         public void processFile(File file) {
/* 179 */           DiskTreebank.DiskTreebankIterator.this.files.add(file.toString());
/*     */         }
/* 181 */       };
/* 182 */       int numPaths = DiskTreebank.this.filePaths.size();
/* 183 */       for (int i = 0; i < numPaths; i++) {
/* 184 */         FilePathProcessor.processPath((File)DiskTreebank.this.filePaths.get(i), (FileFilter)DiskTreebank.this.fileFilters.get(i), dtifp);
/*     */       }
/* 186 */       this.currentFileTrees = new MemoryTreebank(DiskTreebank.this.treeReaderFactory(), DiskTreebank.this.encoding());
/*     */       
/*     */ 
/* 189 */       this.hasNext = primeNextFile();
/*     */     }
/*     */     
/*     */     private boolean primeNextFile() {
/* 193 */       while (this.fileUpto < this.files.size()) {
/* 194 */         if (this.treeUpto < this.currentFileTrees.size()) {
/* 195 */           return true;
/*     */         }
/*     */         
/* 198 */         this.currentFileTrees.clear();
/* 199 */         this.fileUpto += 1;
/* 200 */         this.treeUpto = 0;
/* 201 */         if (this.fileUpto < this.files.size()) {
/* 202 */           String fname = (String)this.files.get(this.fileUpto);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 207 */           DiskTreebank.this.currentFile = new File(fname);
/* 208 */           this.currentFileTrees.loadPath(fname);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 213 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 220 */       return this.hasNext;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Tree next()
/*     */     {
/* 227 */       Tree ret = this.currentFileTrees.get(this.treeUpto++);
/* 228 */       this.hasNext = primeNextFile();
/* 229 */       return ret;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void remove()
/*     */     {
/* 236 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<Tree> iterator()
/*     */   {
/* 248 */     return new DiskTreebankIterator(null);
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
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/* 266 */     if (args.length == 0) {
/* 267 */       System.err.println("This main method will let you variously manipulate and view a treebank.");
/* 268 */       System.err.println("Usage: java DiskTreebank [-flags]* treebankPath fileRanges");
/* 269 */       System.err.println("Useful flags include:");
/* 270 */       System.err.println("\t-maxLength n\t-suffix ext\t-treeReaderFactory class");
/* 271 */       System.err.println("\t-pennPrint\t-encoding enc\t-correct");
/* 272 */       System.err.println("\t-summary\t-decimate");
/* 273 */       return;
/*     */     }
/* 275 */     int i = 0;
/* 276 */     int maxLength = -1;
/* 277 */     boolean normalized = false;
/* 278 */     boolean decimate = false;
/* 279 */     boolean pennPrintTrees = false;
/* 280 */     boolean correct = false;
/* 281 */     boolean summary = false;
/* 282 */     boolean timing = false;
/* 283 */     String decimatePrefix = null;
/* 284 */     String encoding = "UTF-8";
/* 285 */     String suffix = "mrg";
/* 286 */     TreeReaderFactory trf = null;
/*     */     
/* 288 */     while ((i < args.length) && (args[i].startsWith("-"))) {
/* 289 */       if ((args[i].equals("-maxLength")) && (i + 1 < args.length)) {
/* 290 */         maxLength = Integer.parseInt(args[(i + 1)]);
/* 291 */         i += 2;
/* 292 */       } else if (args[i].equals("-normalized")) {
/* 293 */         normalized = true;
/* 294 */         i++;
/* 295 */       } else if ((args[i].equals("-treeReaderFactory")) || (args[i].equals("-trf"))) {
/*     */         try {
/* 297 */           Object o = Class.forName(args[(i + 1)]).newInstance();
/* 298 */           trf = (TreeReaderFactory)o;
/*     */         } catch (Exception e) {
/* 300 */           System.err.println("Couldn't instantiate as TreeReaderFactory: " + args[(i + 1)]);
/* 301 */           return;
/*     */         }
/* 303 */         i += 2;
/* 304 */       } else if (args[i].equals("-suffix")) {
/* 305 */         suffix = args[(i + 1)];
/* 306 */         i += 2;
/* 307 */       } else if (args[i].equals("-decimate")) {
/* 308 */         decimate = true;
/* 309 */         decimatePrefix = args[(i + 1)];
/* 310 */         i += 2;
/* 311 */       } else if (args[i].equals("-encoding")) {
/* 312 */         encoding = args[(i + 1)];
/* 313 */         i += 2;
/* 314 */       } else if (args[i].equals("-correct")) {
/* 315 */         correct = true;
/* 316 */         i++;
/* 317 */       } else if (args[i].equals("-summary")) {
/* 318 */         summary = true;
/* 319 */         i++;
/* 320 */       } else if (args[i].equals("-pennPrint")) {
/* 321 */         pennPrintTrees = true;
/* 322 */         i++;
/* 323 */       } else if (args[i].equals("-timing")) {
/* 324 */         timing = true;
/* 325 */         i++;
/*     */       } else {
/* 327 */         System.err.println("Unknown option: " + args[i]);
/* 328 */         i++;
/*     */       }
/*     */     }
/*     */     
/* 332 */     if (trf == null)
/* 333 */       trf = new TreeReaderFactory()
/*     */       {
/* 335 */         public TreeReader newTreeReader(Reader in) { return new PennTreeReader(in, new LabeledScoredTreeFactory()); }
/*     */       };
/*     */     Treebank treebank;
/*     */     Treebank treebank;
/* 339 */     if (normalized) {
/* 340 */       treebank = new DiskTreebank();
/*     */     } else {
/* 342 */       treebank = new DiskTreebank(trf, encoding);
/*     */     }
/*     */     
/* 345 */     PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, encoding), true);
/*     */     
/* 347 */     if (i + 1 < args.length) {
/* 348 */       treebank.loadPath(args[i], new NumberRangesFileFilter(args[(i + 1)], true));
/*     */     } else {
/* 350 */       treebank.loadPath(args[i], suffix, true);
/*     */     }
/*     */     
/* 353 */     if (summary) {
/* 354 */       System.out.println(treebank.textualSummary());
/*     */     }
/*     */     
/* 357 */     if (correct) {
/* 358 */       treebank = new EnglishPTBTreebankCorrector().transformTrees(treebank);
/*     */     }
/*     */     
/* 361 */     if (pennPrintTrees) {
/* 362 */       treebank.apply(new TreeVisitor() {
/*     */         public void visitTree(Tree tree) {
/* 364 */           tree.pennPrint(this.val$pw);
/* 365 */           this.val$pw.println();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 370 */     if (decimate) {
/* 371 */       Writer w1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(decimatePrefix + "-train.txt"), encoding));
/* 372 */       Writer w2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(decimatePrefix + "-dev.txt"), encoding));
/* 373 */       Writer w3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(decimatePrefix + "-test.txt"), encoding));
/* 374 */       treebank.decimate(w1, w2, w3);
/* 375 */     } else if (maxLength >= 0) {
/* 376 */       for (Tree t : treebank) {
/* 377 */         if (t.yield().length() <= maxLength) {
/* 378 */           System.out.println(t);
/*     */         }
/*     */       }
/* 381 */     } else if (timing) {
/* 382 */       System.out.println();
/* 383 */       Timing.startTime();
/* 384 */       int num = 0;
/* 385 */       for (Tree t : treebank) {
/* 386 */         num += t.yield().length();
/*     */       }
/* 388 */       Timing.endTime("traversing corpus, counting words with iterator");
/* 389 */       System.err.println("There were " + num + " words in the treebank.");
/*     */       
/* 391 */       treebank.apply(new TreeVisitor() {
/* 392 */         int num = 0;
/*     */         
/* 394 */         public void visitTree(Tree t) { this.num += t.yield().length();
/*     */         }
/* 396 */       });
/* 397 */       System.err.println();
/* 398 */       Timing.endTime("traversing corpus, counting words with TreeVisitor");
/* 399 */       System.err.println("There were " + num + " words in the treebank.");
/*     */       
/* 401 */       System.err.println();
/* 402 */       Timing.startTime();
/* 403 */       System.err.println("This treebank contains " + treebank.size() + " trees.");
/* 404 */       Timing.endTime("size of corpus");
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\DiskTreebank.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */