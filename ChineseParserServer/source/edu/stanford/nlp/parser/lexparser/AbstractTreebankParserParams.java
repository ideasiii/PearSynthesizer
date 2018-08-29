/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTag;
/*     */ import edu.stanford.nlp.ling.HasTag;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.stats.EquivalenceClasser;
/*     */ import edu.stanford.nlp.trees.Constituent;
/*     */ import edu.stanford.nlp.trees.DependencyTyper;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.LabeledConstituent;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.MemoryTreebank;
/*     */ import edu.stanford.nlp.trees.SimpleConstituent;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreeTokenizerFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class AbstractTreebankParserParams implements TreebankLangParserParams
/*     */ {
/*     */   protected String inputEncoding;
/*     */   protected String outputEncoding;
/*     */   protected TreebankLanguagePack tlp;
/*     */   private static final String leftHeaded = "leftHeaded";
/*     */   private static final String rightHeaded = "rightHeaded";
/*     */   private static final long serialVersionUID = 4299501909017975915L;
/*     */   
/*     */   protected class SubcategoryStripper implements TreeTransformer
/*     */   {
/*     */     protected SubcategoryStripper() {}
/*     */     
/*  44 */     protected TreeFactory tf = new LabeledScoredTreeFactory();
/*     */     
/*     */     public Tree transformTree(Tree tree) {
/*  47 */       Label lab = tree.label();
/*  48 */       String s = lab.value();
/*  49 */       if (tree.isLeaf()) {
/*  50 */         Tree leaf = this.tf.newLeaf(s);
/*  51 */         leaf.setScore(tree.score());
/*  52 */         return leaf;
/*     */       }
/*  54 */       s = AbstractTreebankParserParams.this.treebankLanguagePack().basicCategory(s);
/*  55 */       int numKids = tree.numChildren();
/*  56 */       List<Tree> children = new ArrayList(numKids);
/*  57 */       for (int cNum = 0; cNum < numKids; cNum++) {
/*  58 */         Tree child = tree.getChild(cNum);
/*  59 */         Tree newChild = transformTree(child);
/*     */         
/*     */ 
/*  62 */         children.add(newChild);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  68 */       CategoryWordTag newLabel = new CategoryWordTag(lab);
/*  69 */       newLabel.setCategory(s);
/*  70 */       if ((lab instanceof HasTag)) {
/*  71 */         String tag = ((HasTag)lab).tag();
/*  72 */         tag = AbstractTreebankParserParams.this.treebankLanguagePack().basicCategory(tag);
/*  73 */         newLabel.setTag(tag);
/*     */       }
/*  75 */       Tree node = this.tf.newTreeNode(newLabel, children);
/*  76 */       node.setScore(tree.score());
/*  77 */       return node;
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
/*     */   protected AbstractTreebankParserParams(TreebankLanguagePack tlp)
/*     */   {
/*  94 */     this.tlp = tlp;
/*  95 */     this.inputEncoding = tlp.getEncoding();
/*  96 */     this.outputEncoding = tlp.getEncoding();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setInputEncoding(String encoding)
/*     */   {
/* 103 */     this.inputEncoding = encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setOutputEncoding(String encoding)
/*     */   {
/* 110 */     this.outputEncoding = encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOutputEncoding()
/*     */   {
/* 118 */     return this.outputEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getInputEncoding()
/*     */   {
/* 125 */     return this.inputEncoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract MemoryTreebank memoryTreebank();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemoryTreebank testMemoryTreebank()
/*     */   {
/* 140 */     return memoryTreebank();
/*     */   }
/*     */   
/*     */   public Treebank treebank() {
/* 144 */     return diskTreebank();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PrintWriter pw()
/*     */   {
/* 153 */     return pw(System.out);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PrintWriter pw(OutputStream o)
/*     */   {
/* 162 */     String encoding = this.outputEncoding;
/* 163 */     if (!Charset.isSupported(encoding)) {
/* 164 */       System.out.println("Warning: desired encoding " + encoding + " not accepted. ");
/* 165 */       System.out.println("Using UTF-8 to construct PrintWriter");
/* 166 */       encoding = "UTF-8";
/*     */     }
/*     */     try
/*     */     {
/* 170 */       return new PrintWriter(new OutputStreamWriter(o, encoding), true);
/*     */     } catch (UnsupportedEncodingException e) {
/* 172 */       System.out.println("Warning: desired encoding " + this.outputEncoding + " not accepted. " + e);
/*     */       try {
/* 174 */         return new PrintWriter(new OutputStreamWriter(o, "UTF-8"), true);
/*     */       } catch (UnsupportedEncodingException e1) {
/* 176 */         System.out.println("Something is really wrong.  Your system doesn't even support UTF-8!" + e1); } }
/* 177 */     return new PrintWriter(o, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreebankLanguagePack treebankLanguagePack()
/*     */   {
/* 187 */     return this.tlp;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract HeadFinder headFinder();
/*     */   
/*     */ 
/*     */   public Lexicon lex()
/*     */   {
/* 196 */     return new BaseLexicon();
/*     */   }
/*     */   
/*     */   public Lexicon lex(Options.LexOptions op) {
/* 200 */     return new BaseLexicon(op);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] MLEDependencyGrammarSmoothingParams()
/*     */   {
/* 209 */     return new double[] { 16.0D, 16.0D, 4.0D, 0.6D };
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
/*     */   public static Collection<Constituent> parsevalObjectify(Tree t, TreeTransformer collinizer)
/*     */   {
/* 222 */     return parsevalObjectify(t, collinizer, true);
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
/*     */   public static Collection<Constituent> parsevalObjectify(Tree t, TreeTransformer collinizer, boolean labelConstituents)
/*     */   {
/* 238 */     Collection<Constituent> spans = new ArrayList();
/* 239 */     Tree t1 = collinizer.transformTree(t);
/* 240 */     if (t1 == null) {
/* 241 */       return spans;
/*     */     }
/* 243 */     for (Tree node : t1)
/* 244 */       if ((!node.isLeaf()) && (!node.isPreTerminal()) && ((node == t1) || (node.parent(t1) != null)))
/*     */       {
/*     */ 
/* 247 */         int leftEdge = t1.leftCharEdge(node);
/* 248 */         int rightEdge = t1.rightCharEdge(node);
/* 249 */         if (labelConstituents) {
/* 250 */           spans.add(new LabeledConstituent(leftEdge, rightEdge, node.label()));
/*     */         } else
/* 252 */           spans.add(new SimpleConstituent(leftEdge, rightEdge));
/*     */       }
/* 254 */     return spans;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Collection<List<String>> untypedDependencyObjectify(Tree t, HeadFinder hf, TreeTransformer collinizer)
/*     */   {
/* 262 */     return dependencyObjectify(t, hf, collinizer, new UntypedDependencyTyper(hf));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Collection<List<String>> unorderedUntypedDependencyObjectify(Tree t, HeadFinder hf, TreeTransformer collinizer)
/*     */   {
/* 269 */     return dependencyObjectify(t, hf, collinizer, new UnorderedUntypedDependencyTyper(hf));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Collection<List<String>> typedDependencyObjectify(Tree t, HeadFinder hf, TreeTransformer collinizer)
/*     */   {
/* 276 */     return dependencyObjectify(t, hf, collinizer, new TypedDependencyTyper(hf));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Collection<List<String>> unorderedTypedDependencyObjectify(Tree t, HeadFinder hf, TreeTransformer collinizer)
/*     */   {
/* 283 */     return dependencyObjectify(t, hf, collinizer, new UnorderedTypedDependencyTyper(hf));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <E> Collection<E> dependencyObjectify(Tree t, HeadFinder hf, TreeTransformer collinizer, DependencyTyper<E> typer)
/*     */   {
/* 290 */     Collection<E> deps = new ArrayList();
/* 291 */     Tree t1 = collinizer.transformTree(t);
/* 292 */     if (t1 == null)
/* 293 */       return deps;
/* 294 */     dependencyObjectifyHelper(t1, t1, hf, deps, typer);
/* 295 */     return deps;
/*     */   }
/*     */   
/*     */   private static <E> void dependencyObjectifyHelper(Tree t, Tree root, HeadFinder hf, Collection<E> c, DependencyTyper<E> typer) {
/* 299 */     if ((t.isLeaf()) || (t.isPreTerminal())) {
/* 300 */       return;
/*     */     }
/* 302 */     Tree headDtr = hf.determineHead(t);
/* 303 */     for (Tree child : t.children()) {
/* 304 */       dependencyObjectifyHelper(child, root, hf, c, typer);
/* 305 */       if (child != headDtr) {
/* 306 */         c.add(typer.makeDependency(headDtr, child, root));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class UntypedDependencyTyper implements DependencyTyper<List<String>>
/*     */   {
/*     */     HeadFinder hf;
/*     */     
/*     */     public UntypedDependencyTyper(HeadFinder hf) {
/* 316 */       this.hf = hf;
/*     */     }
/*     */     
/*     */     public List<String> makeDependency(Tree head, Tree dep, Tree root) {
/* 320 */       List<String> result = new ArrayList(3);
/* 321 */       Tree headTerm = head.headTerminal(this.hf);
/* 322 */       Tree depTerm = dep.headTerminal(this.hf);
/* 323 */       boolean headLeft = root.leftCharEdge(headTerm) < root.leftCharEdge(depTerm);
/* 324 */       result.add(headTerm.value());
/* 325 */       result.add(depTerm.value());
/* 326 */       if (headLeft) {
/* 327 */         result.add("leftHeaded");
/*     */       } else
/* 329 */         result.add("rightHeaded");
/* 330 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class UnorderedUntypedDependencyTyper implements DependencyTyper<List<String>>
/*     */   {
/*     */     HeadFinder hf;
/*     */     
/*     */     public UnorderedUntypedDependencyTyper(HeadFinder hf) {
/* 339 */       this.hf = hf;
/*     */     }
/*     */     
/*     */     public List<String> makeDependency(Tree head, Tree dep, Tree root) {
/* 343 */       List<String> result = new ArrayList(3);
/* 344 */       Tree headTerm = head.headTerminal(this.hf);
/* 345 */       Tree depTerm = dep.headTerminal(this.hf);
/* 346 */       result.add(headTerm.value());
/* 347 */       result.add(depTerm.value());
/* 348 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class TypedDependencyTyper
/*     */     implements DependencyTyper<List<String>>
/*     */   {
/*     */     HeadFinder hf;
/*     */     
/*     */     public TypedDependencyTyper(HeadFinder hf)
/*     */     {
/* 360 */       this.hf = hf;
/*     */     }
/*     */     
/*     */     public List<String> makeDependency(Tree head, Tree dep, Tree root)
/*     */     {
/* 365 */       List<String> result = new ArrayList(6);
/* 366 */       Tree headTerm = head.headTerminal(this.hf);
/* 367 */       Tree depTerm = dep.headTerminal(this.hf);
/* 368 */       boolean headLeft = root.leftCharEdge(headTerm) < root.leftCharEdge(depTerm);
/* 369 */       result.add(headTerm.value());
/* 370 */       result.add(depTerm.value());
/* 371 */       result.add(head.parent(root).value());
/* 372 */       result.add(head.value());
/* 373 */       result.add(dep.value());
/* 374 */       if (headLeft) {
/* 375 */         result.add("leftHeaded");
/*     */       } else
/* 377 */         result.add("rightHeaded");
/* 378 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class UnorderedTypedDependencyTyper implements DependencyTyper<List<String>>
/*     */   {
/*     */     HeadFinder hf;
/*     */     
/*     */     public UnorderedTypedDependencyTyper(HeadFinder hf) {
/* 387 */       this.hf = hf;
/*     */     }
/*     */     
/*     */     public List<String> makeDependency(Tree head, Tree dep, Tree root) {
/* 391 */       List<String> result = new ArrayList(6);
/* 392 */       Tree headTerm = head.headTerminal(this.hf);
/* 393 */       Tree depTerm = dep.headTerminal(this.hf);
/* 394 */       result.add(headTerm.value());
/* 395 */       result.add(depTerm.value());
/* 396 */       result.add(head.parent(root).value());
/* 397 */       result.add(head.value());
/* 398 */       result.add(dep.value());
/* 399 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EquivalenceClasser<List<String>> typedDependencyClasser()
/*     */   {
/* 411 */     new EquivalenceClasser() {
/*     */       public Object equivalenceClass(List<String> s) {
/* 413 */         if (((String)s.get(5)).equals("leftHeaded")) {
/* 414 */           return (String)s.get(2) + "(" + (String)s.get(3) + "->" + (String)s.get(4) + ")";
/*     */         }
/* 416 */         return (String)s.get(2) + "(" + (String)s.get(4) + "<-" + (String)s.get(3) + ")";
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract TreeTransformer collinizer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract TreeTransformer collinizerEvalb();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String[] sisterSplitters();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeTransformer subcategoryStripper()
/*     */   {
/* 454 */     return new SubcategoryStripper();
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
/*     */   public abstract Tree transformTree(Tree paramTree1, Tree paramTree2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void display();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int setOptionFlag(String[] paramArrayOfString, int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TokenizerFactory<Tree> treeTokenizerFactory()
/*     */   {
/* 495 */     return new TreeTokenizerFactory(treeReaderFactory());
/*     */   }
/*     */   
/*     */   public Extractor dependencyGrammarExtractor(Options op) {
/* 499 */     return new MLEDependencyGrammarExtractor(op);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\AbstractTreebankParserParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */