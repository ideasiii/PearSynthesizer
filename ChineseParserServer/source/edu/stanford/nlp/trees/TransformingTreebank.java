/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.util.Timing;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformingTreebank
/*     */   extends Treebank
/*     */ {
/*     */   private TreeTransformer transformer;
/*     */   private Treebank tb;
/*     */   private static final boolean VERBOSE = false;
/*     */   
/*     */   public TransformingTreebank()
/*     */   {
/*  41 */     this(new LabeledScoredTreeReaderFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TransformingTreebank(TreeReaderFactory trf)
/*     */   {
/*  51 */     super(trf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TransformingTreebank(Treebank tb, TreeTransformer transformer)
/*     */   {
/*  63 */     this.tb = tb;
/*  64 */     this.transformer = transformer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/*  72 */     this.tb.clear();
/*  73 */     this.transformer = null;
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
/*     */   public void loadPath(File path, FileFilter filt)
/*     */   {
/*  89 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void apply(TreeVisitor tv)
/*     */   {
/*  98 */     for (Tree t : this.tb)
/*     */     {
/* 100 */       Tree tmpT = t.deeperCopy();
/* 101 */       if (this.transformer != null) {
/* 102 */         tmpT = this.transformer.transformTree(tmpT);
/*     */       }
/*     */       
/* 105 */       tv.visitTree(tmpT);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<Tree> iterator()
/*     */   {
/* 112 */     return new TransformingTreebankIterator(this.tb.iterator(), this.transformer);
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
/* 123 */     Timing.startTime();
/* 124 */     Treebank treebank = new DiskTreebank(new TreeReaderFactory() {
/*     */       public TreeReader newTreeReader(Reader in) {
/* 126 */         return new PennTreeReader(in, new LabeledScoredTreeFactory());
/*     */       }
/* 128 */     });
/* 129 */     Treebank treebank2 = new MemoryTreebank(new TreeReaderFactory() {
/*     */       public TreeReader newTreeReader(Reader in) {
/* 131 */         return new PennTreeReader(in, new LabeledScoredTreeFactory());
/*     */       }
/* 133 */     });
/* 134 */     treebank.loadPath(args[0]);
/* 135 */     treebank2.loadPath(args[0]);
/* 136 */     CompositeTreebank c = new CompositeTreebank(treebank, treebank2);
/* 137 */     Timing.endTime();
/* 138 */     TreeTransformer myTransformer = new MyTreeTransformer();
/* 139 */     TreeTransformer myTransformer2 = new MyTreeTransformer2();
/* 140 */     TreeTransformer myTransformer3 = new MyTreeTransformer3();
/* 141 */     Treebank tf1 = c.transform(myTransformer).transform(myTransformer2).transform(myTransformer3);
/* 142 */     Treebank tf2 = new TransformingTreebank(new TransformingTreebank(new TransformingTreebank(c, myTransformer), myTransformer2), myTransformer3);
/* 143 */     TreeTransformer[] tta = { myTransformer, myTransformer2, myTransformer3 };
/* 144 */     TreeTransformer tt3 = new CompositeTreeTransformer(Arrays.asList(tta));
/* 145 */     Treebank tf3 = c.transform(tt3);
/*     */     
/* 147 */     System.out.println("-------------------------");
/* 148 */     System.out.println("COMPOSITE (DISK THEN MEMORY REPEATED VERSION OF) INPUT TREEBANK");
/* 149 */     System.out.println(c);
/* 150 */     System.out.println("-------------------------");
/* 151 */     System.out.println("SLOWLY TRANSFORMED TREEBANK, USING TransformingTreebank() CONSTRUCTOR");
/* 152 */     Treebank tx1 = new TransformingTreebank(c, myTransformer);
/* 153 */     System.out.println(tx1);
/* 154 */     System.out.println("-----");
/* 155 */     Treebank tx2 = new TransformingTreebank(tx1, myTransformer2);
/* 156 */     System.out.println(tx2);
/* 157 */     System.out.println("-----");
/* 158 */     Treebank tx3 = new TransformingTreebank(tx2, myTransformer3);
/* 159 */     System.out.println(tx3);
/* 160 */     System.out.println("-------------------------");
/* 161 */     System.out.println("TRANSFORMED TREEBANK, USING Treebank.transform()");
/* 162 */     System.out.println(tf1);
/* 163 */     System.out.println("-------------------------");
/* 164 */     System.out.println("PRINTING AGAIN TRANSFORMED TREEBANK, USING Treebank.transform()");
/* 165 */     System.out.println(tf1);
/* 166 */     System.out.println("-------------------------");
/* 167 */     System.out.println("TRANSFORMED TREEBANK, USING TransformingTreebank() CONSTRUCTOR");
/* 168 */     System.out.println(tf2);
/* 169 */     System.out.println("-------------------------");
/* 170 */     System.out.println("TRANSFORMED TREEBANK, USING CompositeTreeTransformer");
/* 171 */     System.out.println(tf3);
/* 172 */     System.out.println("-------------------------");
/* 173 */     System.out.println("COMPOSITE (DISK THEN MEMORY REPEATED VERSION OF) INPUT TREEBANK");
/* 174 */     System.out.println(c);
/* 175 */     System.out.println("-------------------------");
/*     */   }
/*     */   
/*     */   private static class TransformingTreebankIterator implements Iterator<Tree>
/*     */   {
/*     */     private Iterator<Tree> iter;
/*     */     private TreeTransformer transformer;
/*     */     
/*     */     TransformingTreebankIterator(Iterator<Tree> iter, TreeTransformer transformer)
/*     */     {
/* 185 */       this.iter = iter;
/* 186 */       this.transformer = transformer;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 190 */       return this.iter.hasNext();
/*     */     }
/*     */     
/*     */     public Tree next()
/*     */     {
/* 195 */       Tree ret = (Tree)this.iter.next();
/*     */       
/* 197 */       if (this.transformer != null) {
/* 198 */         ret = this.transformer.transformTree(ret);
/*     */       }
/*     */       
/* 201 */       return ret;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 205 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TransformingTreebank.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */