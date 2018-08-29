/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasIndex;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.process.Tokenizer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PennTreeReader
/*     */   implements TreeReader
/*     */ {
/*     */   private Reader in;
/*     */   private Tokenizer st;
/*     */   private TreeNormalizer tn;
/*     */   private TreeFactory tf;
/*     */   private static final boolean DEBUG = false;
/*     */   private int wordIndex;
/*     */   
/*     */   public PennTreeReader(Reader in)
/*     */   {
/*  44 */     this(in, new SimpleTreeFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PennTreeReader(Reader in, TreeFactory tf)
/*     */   {
/*  55 */     this(in, tf, null, new PennTreebankTokenizer(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PennTreeReader(Reader in, Tokenizer st)
/*     */   {
/*  65 */     this(in, new SimpleTreeFactory(), null, st);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PennTreeReader(Reader in, TreeFactory tf, TreeNormalizer tn)
/*     */   {
/*  77 */     this(in, tf, tn, new PennTreebankTokenizer(in));
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
/*     */   public PennTreeReader(Reader in, TreeFactory tf, TreeNormalizer tn, Tokenizer st)
/*     */   {
/*  90 */     this.in = in;
/*  91 */     this.tf = tf;
/*  92 */     this.tn = tn;
/*  93 */     this.st = st;
/*     */     
/*  95 */     String first = (String)st.peek();
/*  96 */     if ((first != null) && (first.startsWith("*x*x*x")))
/*     */     {
/*     */ 
/*     */ 
/* 100 */       int foundCount = 0;
/* 101 */       while ((foundCount < 4) && (st.hasNext())) {
/* 102 */         first = (String)st.next();
/* 103 */         if ((first != null) && (first.startsWith("*x*x*x"))) {
/* 104 */           foundCount++;
/*     */         }
/*     */       }
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
/*     */   public Tree readTree()
/*     */     throws IOException
/*     */   {
/* 126 */     Tree tr = null;
/* 127 */     while (tr == null) {
/* 128 */       if (!this.st.hasNext()) {
/* 129 */         return null;
/*     */       }
/* 131 */       tr = readTreeHelper();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 136 */     return tr;
/*     */   }
/*     */   
/*     */   private Tree readTreeHelper() throws IOException {
/* 140 */     this.wordIndex = 0;
/* 141 */     Tree tr = readTree((String)this.st.next());
/* 142 */     if ((tr == null) || (this.tn == null)) {
/* 143 */       return tr;
/*     */     }
/* 145 */     return this.tn.normalizeWholeTree(tr, this.tf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Tree readTree(String token)
/*     */     throws IOException
/*     */   {
/* 157 */     if (token == null)
/* 158 */       return null;
/* 159 */     if (token.equals("("))
/*     */     {
/* 161 */       String name = (String)this.st.peek();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 166 */       if ((name.equals("(")) || (name.equals(")"))) {
/* 167 */         name = null;
/*     */       }
/*     */       else {
/* 170 */         name = (String)this.st.next();
/*     */       }
/* 172 */       if (this.tn != null) {
/* 173 */         name = this.tn.normalizeNonterminal(name);
/*     */       }
/* 175 */       return this.tf.newTreeNode(name, readTrees()); }
/*     */     String name;
/* 177 */     String name; if (this.tn != null) {
/* 178 */       name = this.tn.normalizeTerminal(token);
/*     */     } else {
/* 180 */       name = token;
/*     */     }
/* 182 */     Tree leaf = this.tf.newLeaf(name);
/* 183 */     if ((leaf.label() instanceof HasIndex)) {
/* 184 */       HasIndex hi = (HasIndex)leaf.label();
/* 185 */       hi.setIndex(this.wordIndex);
/*     */     }
/* 187 */     this.wordIndex += 1;
/* 188 */     return leaf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<Tree> readTrees()
/*     */     throws IOException
/*     */   {
/* 198 */     List<Tree> parseTrees = new ArrayList();
/*     */     
/* 200 */     String nextToken = null;
/* 201 */     String fullToken = "";
/* 202 */     while (this.st.hasNext()) {
/* 203 */       nextToken = (String)this.st.next();
/* 204 */       if (nextToken.equals(")")) {
/*     */         break;
/*     */       }
/* 207 */       if (nextToken.equals("(")) {
/* 208 */         if (!fullToken.equals("")) {
/* 209 */           parseTrees.add(readTree(fullToken));
/* 210 */           fullToken = "";
/*     */         }
/* 212 */         parseTrees.add(readTree(nextToken));
/*     */       }
/*     */       else {
/* 215 */         fullToken = fullToken + (fullToken.equals("") ? "" : " ") + nextToken;
/*     */       }
/*     */     }
/* 218 */     if (!nextToken.equals(")")) {
/* 219 */       throw new IOException("Expecting right paren found eof");
/*     */     }
/* 221 */     if (!fullToken.equals("")) {
/* 222 */       parseTrees.add(readTree(fullToken));
/*     */     }
/*     */     
/* 225 */     return parseTrees;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 233 */     this.in.close();
/*     */   }
/*     */   
/*     */   public static TokenizerFactory<Tree> tokenizerFactory(TreeFactory tf, final TreeNormalizer tn, final Tokenizer stringTokenizer) {
/* 237 */     new TreeTokenizerFactory(new TreeReaderFactory() {
/*     */       public TreeReader newTreeReader(Reader in) {
/* 239 */         return new PennTreeReader(in, this.val$tf, tn, stringTokenizer);
/*     */       }
/*     */     });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<Tree> asTreeIterator()
/*     */   {
/* 283 */     new Iterator() {
/* 284 */       private Tree next = advance();
/*     */       
/* 286 */       public boolean hasNext() { return this.next != null; }
/*     */       
/*     */       public Tree next() {
/* 289 */         Tree t = this.next;
/* 290 */         this.next = advance();
/* 291 */         return t;
/*     */       }
/*     */       
/* 294 */       public void remove() { throw new UnsupportedOperationException(); }
/*     */       
/*     */       private Tree advance() {
/* 297 */         Tree t = PennTreeReader.this.readTreeThrowRuntime();
/* 298 */         if (t == null) PennTreeReader.this.closeThrowRuntime();
/* 299 */         return t;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private Tree readTreeThrowRuntime() {
/*     */     Tree t;
/*     */     try {
/* 307 */       t = readTree();
/*     */     } catch (IOException e) {
/* 309 */       throw new RuntimeException(e);
/*     */     }
/* 311 */     return t;
/*     */   }
/*     */   
/*     */   private void closeThrowRuntime() {
/*     */     try {
/* 316 */       close();
/*     */     } catch (IOException e) {
/* 318 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 329 */       TreeFactory tf = new LabeledScoredTreeFactory();
/* 330 */       Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));
/* 331 */       TreeReader tr = new PennTreeReader(r, tf);
/* 332 */       Tree t = tr.readTree();
/* 333 */       while (t != null) {
/* 334 */         System.out.println(t);
/* 335 */         System.out.println();
/* 336 */         t = tr.readTree();
/*     */       }
/* 338 */       r.close();
/*     */     } catch (IOException ioe) {
/* 340 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\PennTreeReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */