/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTag;
/*     */ import edu.stanford.nlp.trees.DiskTreebank;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.MemoryTreebank;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeReaderFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.international.negra.NegraLabel;
/*     */ import edu.stanford.nlp.trees.international.negra.NegraPennLanguagePack;
/*     */ import edu.stanford.nlp.trees.international.negra.NegraPennTokenizer;
/*     */ import edu.stanford.nlp.trees.international.negra.NegraPennTreeNormalizer;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class NegraPennTreebankParserParams extends AbstractTreebankParserParams
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  24 */   public static boolean markRC = false;
/*  25 */   public static boolean markZuVP = false;
/*     */   
/*  27 */   private NegraPennLanguagePack nplp = (NegraPennLanguagePack)treebankLanguagePack();
/*     */   
/*     */   public NegraPennTreebankParserParams() {
/*  30 */     super(new NegraPennLanguagePack());
/*     */     
/*  32 */     setOutputEncoding("UTF-8");
/*  33 */     this.nplp = ((NegraPennLanguagePack)treebankLanguagePack());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HeadFinder headFinder()
/*     */   {
/*  41 */     return new edu.stanford.nlp.trees.international.negra.NegraHeadFinder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lexicon lex(Options.LexOptions op)
/*     */   {
/*  49 */     return new GermanLexicon(op);
/*     */   }
/*     */   
/*  52 */   private NegraPennTreeReaderFactory treeReaderFactory = new NegraPennTreeReaderFactory(null);
/*     */   private static final long serialVersionUID = 2L;
/*     */   
/*  55 */   public TreeReaderFactory treeReaderFactory() { return this.treeReaderFactory; }
/*     */   
/*     */   private static class NegraPennTreeReaderFactory
/*     */     implements TreeReaderFactory, java.io.Serializable
/*     */   {
/*  60 */     boolean treeNormalizerInsertNPinPP = false;
/*     */     
/*  62 */     boolean treeNormalizerLeaveGF = false;
/*     */     
/*     */     public edu.stanford.nlp.trees.TreeReader newTreeReader(Reader in) {
/*  65 */       NegraPennTreeNormalizer tn = new NegraPennTreeNormalizer();
/*  66 */       if (this.treeNormalizerLeaveGF) {
/*  67 */         tn.setLeaveGF(true);
/*     */       }
/*  69 */       if (this.treeNormalizerInsertNPinPP) {
/*  70 */         tn.setInsertNPinPP(true);
/*     */       }
/*     */       
/*  73 */       return new edu.stanford.nlp.trees.PennTreeReader(in, new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.StringLabelFactory()), tn, new NegraPennTokenizer(in));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MemoryTreebank memoryTreebank()
/*     */   {
/*  82 */     return new MemoryTreebank(this.treeReaderFactory, this.inputEncoding);
/*     */   }
/*     */   
/*     */ 
/*     */   public DiskTreebank diskTreebank()
/*     */   {
/*  88 */     return new DiskTreebank(this.treeReaderFactory, this.inputEncoding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TreeTransformer collinizer()
/*     */   {
/*  95 */     return new NegraPennCollinizer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TreeTransformer collinizerEvalb()
/*     */   {
/* 102 */     return new NegraPennCollinizer(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public edu.stanford.nlp.trees.TreebankLanguagePack treebankLanguagePack()
/*     */   {
/* 109 */     return new NegraPennLanguagePack();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] sisterSplitters()
/*     */   {
/* 116 */     return new String[0];
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
/*     */   public int setOptionFlag(String[] args, int i)
/*     */   {
/* 129 */     if (args[i].equalsIgnoreCase("-leaveGF")) {
/* 130 */       this.treeReaderFactory.treeNormalizerLeaveGF = true;
/* 131 */       i++;
/* 132 */     } else if (args[i].equalsIgnoreCase("-markZuVP")) {
/* 133 */       markZuVP = true;
/* 134 */       i++;
/* 135 */     } else if (args[i].equalsIgnoreCase("-markRC")) {
/* 136 */       markRC = true;
/* 137 */       i++;
/* 138 */     } else if (args[i].equalsIgnoreCase("-insertNPinPP")) {
/* 139 */       this.treeReaderFactory.treeNormalizerInsertNPinPP = true;
/* 140 */       i++;
/*     */     }
/* 142 */     return i;
/*     */   }
/*     */   
/*     */   public void display() {
/* 146 */     System.out.println("markZuVP=" + markZuVP);
/* 147 */     System.out.println("insertNPinPP=" + this.treeReaderFactory.treeNormalizerInsertNPinPP);
/* 148 */     System.out.println("leaveGF=" + this.treeReaderFactory.treeNormalizerLeaveGF);
/*     */   }
/*     */   
/*     */   private String basicCat(String str)
/*     */   {
/* 153 */     return this.nplp.basicCategory(str);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree transformTree(Tree t, Tree root)
/*     */   {
/* 162 */     if ((t == null) || (t.isLeaf())) {
/* 163 */       return t;
/*     */     }
/*     */     
/* 166 */     List<String> annotations = new ArrayList();
/*     */     
/* 168 */     CategoryWordTag lab = (CategoryWordTag)t.label();
/* 169 */     String word = lab.word();
/* 170 */     String tag = lab.tag();
/* 171 */     String cat = lab.value();
/* 172 */     String baseCat = this.nplp.basicCategory(cat);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 182 */     if (t.isPhrasal())
/*     */     {
/* 184 */       List childBasicCats = childBasicCats(t);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */       if ((markZuVP) && (baseCat.equals("VP")) && ((childBasicCats.contains("VZ")) || (childBasicCats.contains("VVIZU"))))
/*     */       {
/* 194 */         annotations.add("-ZU");
/*     */       }
/*     */       
/*     */ 
/* 198 */       if ((markRC) && ((t.label() instanceof NegraLabel)) && (baseCat.equals("S")) && (((NegraLabel)t.label()).getEdge() != null) && (((NegraLabel)t.label()).getEdge().equals("RC")))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 205 */         annotations.add("-RC");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 211 */     for (String annotation : annotations) {
/* 212 */       cat = cat + annotation;
/*     */     }
/*     */     
/* 215 */     t.setLabel(new CategoryWordTag(cat, word, tag));
/* 216 */     return t;
/*     */   }
/*     */   
/*     */   private List<String> childBasicCats(Tree t) {
/* 220 */     Tree[] kids = t.children();
/* 221 */     List<String> l = new ArrayList();
/* 222 */     int i = 0; for (int n = kids.length; i < n; i++) {
/* 223 */       l.add(basicCat(kids[i].label().value()));
/*     */     }
/* 225 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List defaultTestSentence()
/*     */   {
/* 233 */     return java.util.Arrays.asList(new String[] { "Solch", "einen", "Zuspruch", "hat", "Angela", "Merkel", "lange", "nicht", "mehr", "erlebt", "." });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 240 */     TreebankLangParserParams tlpp = new NegraPennTreebankParserParams();
/* 241 */     Treebank tb = tlpp.memoryTreebank();
/* 242 */     tb.loadPath(args[0]);
/* 243 */     for (Tree aTb : tb) {
/* 244 */       aTb.pennPrint();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\NegraPennTreebankParserParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */