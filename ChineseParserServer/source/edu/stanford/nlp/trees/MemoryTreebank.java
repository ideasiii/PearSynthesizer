/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasIndex;
/*     */ import edu.stanford.nlp.util.FilePathProcessor;
/*     */ import edu.stanford.nlp.util.FileProcessor;
/*     */ import edu.stanford.nlp.util.Timing;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MemoryTreebank
/*     */   extends Treebank
/*     */   implements FileProcessor, List<Tree>
/*     */ {
/*     */   private static final boolean PRINT_FILENAMES = false;
/*     */   private List<Tree> parseTrees;
/*     */   
/*     */   public MemoryTreebank()
/*     */   {
/*  38 */     this(new LabeledScoredTreeReaderFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemoryTreebank(String encoding)
/*     */   {
/*  47 */     this(new LabeledScoredTreeReaderFactory(), encoding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemoryTreebank(TreeReaderFactory trf)
/*     */   {
/*  57 */     super(trf);
/*  58 */     this.parseTrees = new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemoryTreebank(TreeReaderFactory trf, String encoding)
/*     */   {
/*  70 */     super(trf, encoding);
/*  71 */     this.parseTrees = new ArrayList();
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
/*     */   public MemoryTreebank(List<Tree> trees, TreeReaderFactory trf, String encoding)
/*     */   {
/*  84 */     super(trf, encoding);
/*  85 */     this.parseTrees = trees;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemoryTreebank(int initialCapacity)
/*     */   {
/*  95 */     this(initialCapacity, new SimpleTreeReaderFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemoryTreebank(int initialCapacity, TreeReaderFactory trf)
/*     */   {
/* 107 */     super(initialCapacity, trf);
/* 108 */     this.parseTrees = new ArrayList(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 116 */     this.parseTrees.clear();
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
/* 127 */     FilePathProcessor.processPath(path, filt, this);
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
/*     */   public void processFile(File file)
/*     */   {
/* 140 */     TreeReader tr = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 149 */       tr = treeReaderFactory().newTreeReader(new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding())));
/* 150 */       int sentIndex = 0;
/* 151 */       Tree pt; while ((pt = tr.readTree()) != null) {
/* 152 */         if ((pt.label() instanceof HasIndex)) {
/* 153 */           HasIndex hi = (HasIndex)pt.label();
/* 154 */           hi.setDocID(file.getName());
/* 155 */           hi.setSentIndex(sentIndex);
/*     */         }
/* 157 */         this.parseTrees.add(pt);
/* 158 */         sentIndex++;
/*     */       }
/*     */       return;
/* 161 */     } catch (IOException e) { System.err.println("loadTree IO Exception: " + e + " in file " + file);
/*     */     } finally {
/*     */       try {
/* 164 */         if (tr != null) {
/* 165 */           tr.close();
/*     */         }
/*     */       }
/*     */       catch (IOException e) {}
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
/*     */   public void load(Reader r)
/*     */   {
/* 182 */     load(r, null);
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
/*     */   public void load(Reader r, String id)
/*     */   {
/* 196 */     TreeReader tr = null;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 201 */       tr = treeReaderFactory().newTreeReader(r);
/* 202 */       int sentIndex = 0;
/* 203 */       Tree pt; while ((pt = tr.readTree()) != null) {
/* 204 */         if ((pt.label() instanceof HasIndex)) {
/* 205 */           HasIndex hi = (HasIndex)pt.label();
/* 206 */           if (id != null) {
/* 207 */             hi.setDocID(id);
/*     */           }
/* 209 */           hi.setSentIndex(sentIndex);
/*     */         }
/* 211 */         this.parseTrees.add(pt);
/* 212 */         sentIndex++;
/*     */       }
/*     */     } catch (IOException e) {
/* 215 */       System.err.println("load IO Exception: " + e);
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
/*     */   public Tree get(int i)
/*     */   {
/* 230 */     return (Tree)this.parseTrees.get(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void apply(TreeVisitor tp)
/*     */   {
/* 240 */     int i = 0; for (int size = this.parseTrees.size(); i < size; i++) {
/* 241 */       tp.visitTree((Tree)this.parseTrees.get(i));
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
/*     */   public Iterator<Tree> iterator()
/*     */   {
/* 257 */     return this.parseTrees.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 269 */     return this.parseTrees.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void add(int index, Tree element)
/*     */   {
/* 276 */     this.parseTrees.add(index, element);
/*     */   }
/*     */   
/*     */   public boolean add(Tree element) {
/* 280 */     return this.parseTrees.add(element);
/*     */   }
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends Tree> c)
/*     */   {
/* 285 */     return this.parseTrees.addAll(index, c);
/*     */   }
/*     */   
/*     */   public int indexOf(Object o) {
/* 289 */     return this.parseTrees.indexOf(o);
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 293 */     return this.parseTrees.lastIndexOf(o);
/*     */   }
/*     */   
/*     */   public Tree remove(int index) {
/* 297 */     return (Tree)this.parseTrees.remove(index);
/*     */   }
/*     */   
/*     */   public Tree set(int index, Tree element) {
/* 301 */     return (Tree)this.parseTrees.set(index, element);
/*     */   }
/*     */   
/*     */   public ListIterator<Tree> listIterator() {
/* 305 */     return this.parseTrees.listIterator();
/*     */   }
/*     */   
/*     */   public ListIterator<Tree> listIterator(int index) {
/* 309 */     return this.parseTrees.listIterator(index);
/*     */   }
/*     */   
/*     */   public List<Tree> subList(int fromIndex, int toIndex) {
/* 313 */     return this.parseTrees.subList(fromIndex, toIndex);
/*     */   }
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
/* 325 */     Treebank mtb = new MemoryTreebank(size(), treeReaderFactory());
/* 326 */     for (Iterator it = iterator(); it.hasNext();) {
/* 327 */       Tree t = (Tree)it.next();
/* 328 */       mtb.add(treeTrans.transformTree(t));
/*     */     }
/* 330 */     return mtb;
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
/* 341 */     Timing.startTime();
/* 342 */     Treebank treebank = new MemoryTreebank(new TreeReaderFactory() {
/*     */       public TreeReader newTreeReader(Reader in) {
/* 344 */         return new PennTreeReader(in, new LabeledScoredTreeFactory());
/*     */       }
/* 346 */     });
/* 347 */     treebank.loadPath(args[0]);
/* 348 */     Timing.endTime();
/* 349 */     System.out.println(treebank);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\MemoryTreebank.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */