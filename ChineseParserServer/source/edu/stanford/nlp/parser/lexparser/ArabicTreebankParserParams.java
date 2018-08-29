/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.io.RuntimeIOException;
/*     */ import edu.stanford.nlp.ling.HasTag;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.process.Function;
/*     */ import edu.stanford.nlp.process.WordSegmentingTokenizer;
/*     */ import edu.stanford.nlp.trees.DiskTreebank;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.MemoryTreebank;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeReaderFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.international.arabic.ArabicHeadFinder;
/*     */ import edu.stanford.nlp.trees.international.arabic.ArabicTreeReaderFactory;
/*     */ import edu.stanford.nlp.trees.international.arabic.ArabicTreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.tregex.ParseException;
/*     */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*     */ import edu.stanford.nlp.trees.tregex.TregexPattern;
/*     */ import edu.stanford.nlp.trees.tregex.TregexPatternCompiler;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ public class ArabicTreebankParserParams
/*     */   extends AbstractTreebankParserParams
/*     */ {
/*  59 */   private String optionsString = "ArabicTreebankParserParams\n";
/*     */   
/*  61 */   private boolean retainNPTmp = false;
/*  62 */   private boolean retainPRD = false;
/*  63 */   private boolean changeNoLabels = false;
/*  64 */   private boolean collinizerRetainsPunctuation = false;
/*  65 */   private Pattern collinizerPruneRegex = null;
/*  66 */   private boolean discardX = false;
/*     */   
/*     */   public ArabicTreebankParserParams() {
/*  69 */     super(new ArabicTreebankLanguagePack());
/*  70 */     initializeAnnotationPatterns();
/*     */   }
/*     */   
/*     */   public TreeReaderFactory treeReaderFactory() {
/*  74 */     return new ArabicTreeReaderFactory(this.retainNPTmp, this.retainPRD, this.changeNoLabels, this.discardX);
/*     */   }
/*     */   
/*     */   public MemoryTreebank memoryTreebank()
/*     */   {
/*  79 */     return new MemoryTreebank(treeReaderFactory());
/*     */   }
/*     */   
/*     */   public DiskTreebank diskTreebank() {
/*  83 */     return new DiskTreebank(treeReaderFactory());
/*     */   }
/*     */   
/*  86 */   Class<? extends HeadFinder> headFinderClass = ArabicHeadFinder.class;
/*     */   
/*     */   public HeadFinder headFinder() {
/*     */     try {
/*  90 */       return (HeadFinder)this.headFinderClass.newInstance();
/*     */     }
/*     */     catch (Exception e) {
/*  93 */       System.err.println("Error while instantiating class " + this.headFinderClass + ": " + e);
/*  94 */       System.err.println("Using ArabicHeadFinder instead."); }
/*  95 */     return new ArabicHeadFinder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ArabicCollinizer
/*     */     implements TreeTransformer, Serializable
/*     */   {
/*     */     private TreebankLanguagePack tlp;
/*     */     
/*     */ 
/*     */     private boolean retainPunctuation;
/*     */     
/*     */ 
/*     */     private Pattern collinizerPruneRegex;
/*     */     
/*     */ 
/*     */     public ArabicCollinizer(TreebankLanguagePack tlp, boolean retainPunctuation, Pattern collinizerPruneRegex)
/*     */     {
/* 114 */       this.tlp = tlp;
/* 115 */       this.retainPunctuation = retainPunctuation;
/* 116 */       this.collinizerPruneRegex = collinizerPruneRegex;
/*     */     }
/*     */     
/*     */     public Tree transformTree(Tree t) {
/* 120 */       if (this.tlp.isStartSymbol(t.value())) {
/* 121 */         t = t.firstChild();
/*     */       }
/* 123 */       Tree result = t.deepCopy();
/* 124 */       result = result.prune(new Filter() {
/*     */         public boolean accept(Tree tree) {
/* 126 */           return (ArabicTreebankParserParams.ArabicCollinizer.this.collinizerPruneRegex == null) || (tree.label() == null) || (!ArabicTreebankParserParams.ArabicCollinizer.this.collinizerPruneRegex.matcher(tree.label().value()).matches());
/*     */         }
/*     */       });
/* 129 */       if (result == null) {
/* 130 */         return null;
/*     */       }
/* 132 */       for (Tree node : result)
/*     */       {
/* 134 */         if ((node.label() != null) && (!node.isLeaf())) {
/* 135 */           node.label().setValue(this.tlp.basicCategory(node.label().value()));
/*     */         }
/* 137 */         if (node.label().value().equals("ADVP")) {
/* 138 */           node.label().setValue("PRT");
/*     */         }
/*     */       }
/*     */       
/* 142 */       if (this.retainPunctuation) {
/* 143 */         return result;
/*     */       }
/* 145 */       result.prune(new Filter() {
/* 146 */         final Filter<String> punctLabelFilter = ArabicTreebankParserParams.ArabicCollinizer.this.tlp.punctuationTagRejectFilter();
/*     */         
/*     */         public boolean accept(Tree tree) {
/* 149 */           return this.punctLabelFilter.accept(tree.value());
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeTransformer collinizer()
/*     */   {
/* 161 */     return new ArabicCollinizer(this.tlp, this.collinizerRetainsPunctuation, this.collinizerPruneRegex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TreeTransformer collinizerEvalb()
/*     */   {
/* 168 */     return collinizer();
/*     */   }
/*     */   
/*     */   public String[] sisterSplitters() {
/* 172 */     return new String[0];
/*     */   }
/*     */   
/* 175 */   private Map<TregexPattern, Function<TregexMatcher, String>> activeAnnotations = new HashMap();
/*     */   
/*     */   public Tree transformTree(Tree t, Tree root) {
/* 178 */     String newCategory = t.label().value();
/* 179 */     for (Map.Entry<TregexPattern, Function<TregexMatcher, String>> e : this.activeAnnotations.entrySet()) {
/* 180 */       TregexMatcher m = ((TregexPattern)e.getKey()).matcher(root);
/* 181 */       if (m.matchesAt(t)) {
/* 182 */         newCategory = newCategory + (String)((Function)e.getValue()).apply(m);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 187 */     t.label().setValue(newCategory);
/*     */     
/*     */ 
/* 190 */     if (t.isPreTerminal()) {
/* 191 */       HasTag lab = (HasTag)t.label();
/* 192 */       lab.setTag(newCategory);
/*     */     }
/*     */     
/* 195 */     return t;
/*     */   }
/*     */   
/*     */   public void display() {
/* 199 */     System.err.println(this.optionsString);
/*     */   }
/*     */   
/* 202 */   private Map<String, Pair<TregexPattern, Function<TregexMatcher, String>>> annotationPatterns = new HashMap();
/*     */   
/*     */   private static final String genitiveNodeTregexString = "@NP > @NP $- /^N/";
/*     */   
/*     */   private static final String copularVerbForms = "/^(kAn|kAnt|ykwn|sykwn|tkwn|ykn|stkwn|ykwnw|ybdw|tbdw|sybdw|stbdw|bdY|ybdy|tbdy|stbdy|sybdy)$/";
/*     */   
/*     */   private static final String sbarVerbForms = "/^(qAl|\\>DAf|AEln|\\>wDH|ymkn|\\>Eln|\\*krt|\\>kd|AElnt|Akd|qAlt|\\>DAft|AfAd|y\\*kr|yjb|\\{Etbr|\\>wDHt|AEtbr|sbq|\\*kr|tAbE|nqlt|SrH|r\\>Y|\\>fAd|AfAdt|yqwl|\\>kdt|\\>Elnt|Akdt|yrY|tEtbr|AEtqd|yEtbr|tfyd|ytwqE|AEtbrt|ynbgy|Tlbt|qrr|ktbt|\\>blg|\\>\\$Ar|ywDH|t\\&kd|Tlb|r\\>t|yEny|nryd|nEtbr|yftrD|k\\$f|\\{Etbrt|AwDH|ytEyn|ykfy|y\\&kd|yErf|ydrk|tZhr|tqwl|tbd\\>|nEtqd|nErf|AErf|Elm|Awrdt|AwDHt|AqtrH|yryd|yErfAn|yElm|ybd\\>tstTyE|tHAwl|tEny|nrY|n\\>ml|)$/";
/*     */   
/*     */   private void initializeAnnotationPatterns()
/*     */   {
/*     */     try
/*     */     {
/* 214 */       this.annotationPatterns.put("-markFem", new Pair(TregexPattern.compile("__ <<# /p$/"), new SimpleStringFunction("-FEM")));
/* 215 */       this.annotationPatterns.put("-markGappedVP", new Pair(TregexPattern.compile("@VP > @VP $- __ $ /^(CC|CONJ)/ !< /^V/"), new SimpleStringFunction("-gappedVP")));
/* 216 */       this.annotationPatterns.put("-markGappedVPConjoiners", new Pair(TregexPattern.compile("/^(CC|CONJ)/ $ (@VP > @VP $- __ !< /^V/)"), new SimpleStringFunction("-gappedVP")));
/* 217 */       this.annotationPatterns.put("-gpAnnotatePrepositions", new Pair(TregexPattern.compile("IN > (__ > __=gp)"), new AddRelativeNodeFunction("^^", "gp")));
/* 218 */       this.annotationPatterns.put("-gpEquivalencePrepositions", new Pair(TregexPattern.compile("IN > (__ > __=gp)"), new AddEquivalencedNodeFunction("^^", "gp")));
/* 219 */       this.annotationPatterns.put("-genitiveMark", new Pair(TregexPattern.compile("@NP > @NP $- /^N/"), new SimpleStringFunction("-genitive")));
/* 220 */       this.annotationPatterns.put("-markGenitiveParent", new Pair(TregexPattern.compile("@NP < (@NP > @NP $- /^N/)"), new SimpleStringFunction("-genitiveParent")));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 225 */       this.annotationPatterns.put("-maSdrMark", new Pair(tregexPatternCompiler.compile("/^N/ <<# (/^[t\\u062a].+[y\\u064a].$/ > @NN|NOUN|DTNN)"), new SimpleStringFunction("-maSdr")));
/*     */       
/* 227 */       this.annotationPatterns.put("-maSdrMark2", new Pair(tregexPatternCompiler.compile("/^N/ <<# (/^(?:[t\\u062a].+[y\\u064a].|<.{3,}|A.{3,})$/ > @NN|NOUN|DTNN)"), new SimpleStringFunction("-maSdr")));
/* 228 */       this.annotationPatterns.put("-maSdrMark3", new Pair(tregexPatternCompiler.compile("/^N/ <<# (/^(?:[t\\u062a<A].{3,})$/ > @NN|NOUN|DTNN)"), new SimpleStringFunction("-maSdr")));
/* 229 */       this.annotationPatterns.put("-maSdrMark4", new Pair(tregexPatternCompiler.compile("/^N/ <<# (/^(?:[t\\u062a<A].{3,})$/ > (@NN|NOUN|DTNN > (@NP < @NP)))"), new SimpleStringFunction("-maSdr")));
/* 230 */       this.annotationPatterns.put("-maSdrMark5", new Pair(tregexPatternCompiler.compile("/^N/ <<# (__ > (@NN|NOUN|DTNN > (@NP < @NP)))"), new SimpleStringFunction("-maSdr")));
/* 231 */       this.annotationPatterns.put("-mjjMark", new Pair(tregexPatternCompiler.compile("@JJ|DTJJ < /^m/ $+ PP ># ADJP "), new SimpleStringFunction("-mjj")));
/*     */       
/* 233 */       this.annotationPatterns.put("-splitPUNC", new Pair(tregexPatternCompiler.compile("@PUNC < __=term"), new AnnotatePunctuationFunction(null)));
/* 234 */       this.annotationPatterns.put("-markPPwithPPdescendant", new Pair(tregexPatternCompiler.compile("__ !< @PP << @PP [ >> @PP | == @PP ]"), new SimpleStringFunction("-inPPdominatesPP")));
/* 235 */       this.annotationPatterns.put("-markNPwithSdescendant", new Pair(tregexPatternCompiler.compile("__ !< @S << @S [ >> @NP | == @NP ]"), new SimpleStringFunction("-inNPdominatesS")));
/* 236 */       this.annotationPatterns.put("-markContainsVerb", new Pair(tregexPatternCompiler.compile("__ << (/^[CIP]?V/ < (__ !< __))"), new SimpleStringFunction("-containsV")));
/* 237 */       this.annotationPatterns.put("-retainNPTmp", new Pair(tregexPatternCompiler.compile("__ >># /^NP-TMP/"), new SimpleStringFunction("-TMP")));
/* 238 */       this.annotationPatterns.put("-markRightRecursiveNP", new Pair(tregexPatternCompiler.compile("__ <<- @NP [>>- @NP | == @NP]"), new SimpleStringFunction("-rrNP")));
/* 239 */       this.annotationPatterns.put("-markBaseNP", new Pair(tregexPatternCompiler.compile("@NP !< @NP !< @VP !< @SBAR !< @ADJP !< @ADVP !< @S !< @QP !< @UCP !< @PP"), new SimpleStringFunction("-base")));
/* 240 */       this.annotationPatterns.put("-markContainsSBAR", new Pair(tregexPatternCompiler.compile("__ << @SBAR"), new SimpleStringFunction("-containsSBAR")));
/* 241 */       this.annotationPatterns.put("-markPhrasalNodesDominatedBySBAR", new Pair(tregexPatternCompiler.compile("__ < (__ < __) >> @SBAR"), new SimpleStringFunction("-domBySBAR")));
/* 242 */       this.annotationPatterns.put("-markCoordinateNPs", new Pair(tregexPatternCompiler.compile("@NP < @CC|CONJ"), new SimpleStringFunction("-coord")));
/* 243 */       this.annotationPatterns.put("-splitCC", new Pair(tregexPatternCompiler.compile("@CC|CONJ < __=term"), new AddRelativeNodeFunction("-", "term")));
/* 244 */       this.annotationPatterns.put("-markCopularVerbTags", new Pair(tregexPatternCompiler.compile("/^V/ < /^(kAn|kAnt|ykwn|sykwn|tkwn|ykn|stkwn|ykwnw|ybdw|tbdw|sybdw|stbdw|bdY|ybdy|tbdy|stbdy|sybdy)$/"), new SimpleStringFunction("-copular")));
/* 245 */       this.annotationPatterns.put("-markSBARVerbTags", new Pair(tregexPatternCompiler.compile("/^V/ < /^(qAl|\\>DAf|AEln|\\>wDH|ymkn|\\>Eln|\\*krt|\\>kd|AElnt|Akd|qAlt|\\>DAft|AfAd|y\\*kr|yjb|\\{Etbr|\\>wDHt|AEtbr|sbq|\\*kr|tAbE|nqlt|SrH|r\\>Y|\\>fAd|AfAdt|yqwl|\\>kdt|\\>Elnt|Akdt|yrY|tEtbr|AEtqd|yEtbr|tfyd|ytwqE|AEtbrt|ynbgy|Tlbt|qrr|ktbt|\\>blg|\\>\\$Ar|ywDH|t\\&kd|Tlb|r\\>t|yEny|nryd|nEtbr|yftrD|k\\$f|\\{Etbrt|AwDH|ytEyn|ykfy|y\\&kd|yErf|ydrk|tZhr|tqwl|tbd\\>|nEtqd|nErf|AErf|Elm|Awrdt|AwDHt|AqtrH|yryd|yErfAn|yElm|ybd\\>tstTyE|tHAwl|tEny|nrY|n\\>ml|)$/"), new SimpleStringFunction("-SBARverb")));
/* 246 */       this.annotationPatterns.put("-markNounNPargTakers", new Pair(tregexPatternCompiler.compile("NN|NNS|NNP|NNPS|DTNN|DTNNS|DTNNP|DTNNPS ># (@NP < NP)"), new SimpleStringFunction("-NounNParg")));
/* 247 */       this.annotationPatterns.put("-markNounAdjVPheads", new Pair(tregexPatternCompiler.compile("NN|NNS|NNP|NNPS|JJ|DTJJ|DTNN|DTNNS|DTNNP|DTNNPS ># @VP"), new SimpleStringFunction("-VHead")));
/*     */       
/*     */ 
/* 250 */       this.annotationPatterns.put("-markPronominalNP", new Pair(tregexPatternCompiler.compile("@NP < @PRP"), new SimpleStringFunction("-PRP")));
/*     */     } catch (ParseException e) {
/* 252 */       System.err.println("Parse exception on annotation pattern initialization:" + e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */   private static final TregexPatternCompiler tregexPatternCompiler = new TregexPatternCompiler(new ArabicHeadFinder());
/*     */   private static final String markPRDverbString = "-markPRDverbs";
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   private static class SimpleStringFunction implements Function<TregexMatcher, String> {
/*     */     private String result;
/*     */     
/* 269 */     public SimpleStringFunction(String result) { this.result = result; }
/*     */     
/*     */ 
/*     */ 
/*     */     public String apply(TregexMatcher tregexMatcher)
/*     */     {
/* 275 */       return this.result;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 279 */       return "SimpleStringFunction[" + this.result + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AddRelativeNodeFunction implements Function<TregexMatcher, String>
/*     */   {
/*     */     private String annotationMark;
/*     */     private Object key;
/*     */     
/*     */     public AddRelativeNodeFunction(String annotationMark, Object key)
/*     */     {
/* 290 */       this.annotationMark = annotationMark;
/* 291 */       this.key = key;
/*     */     }
/*     */     
/*     */     public String apply(TregexMatcher m) {
/* 295 */       return this.annotationMark + m.getNode(this.key).label().value();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 299 */       return "AddRelativeNodeFunction[" + this.annotationMark + "," + this.key + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AddEquivalencedNodeFunction
/*     */     implements Function<TregexMatcher, String>
/*     */   {
/*     */     private String annotationMark;
/*     */     private Object key;
/*     */     
/*     */     public AddEquivalencedNodeFunction(String annotationMark, Object key)
/*     */     {
/* 311 */       this.annotationMark = annotationMark;
/* 312 */       this.key = key;
/*     */     }
/*     */     
/*     */     public String apply(TregexMatcher m) {
/* 316 */       String node = m.getNode(this.key).label().value();
/*     */       String mark;
/* 318 */       String mark; if (node.startsWith("S")) {
/* 319 */         mark = "Setc"; } else { String mark;
/* 320 */         if (node.startsWith("VP")) {
/* 321 */           mark = "VP";
/*     */         } else
/* 323 */           mark = "NPetc";
/*     */       }
/* 325 */       return this.annotationMark + mark;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 329 */       return "AddEquivalencedNodeFunction[" + this.annotationMark + "," + this.key + "]";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class AnnotatePunctuationFunction implements Function<TregexMatcher, String>
/*     */   {
/*     */     static final String key = "term";
/* 336 */     private static final Pattern endOfSentence = Pattern.compile("^(\\.|\\?.*)$");
/* 337 */     private static final Pattern comma = Pattern.compile("^,$");
/*     */     
/* 339 */     private static final Pattern dash = Pattern.compile("^-.*$");
/* 340 */     private static final Pattern quote = Pattern.compile("^\"$");
/*     */     
/*     */ 
/*     */ 
/* 344 */     private static final Pattern lrb = Pattern.compile("^-LRB-$");
/* 345 */     private static final Pattern rrb = Pattern.compile("^-RRB-$");
/*     */     
/*     */     public String apply(TregexMatcher m)
/*     */     {
/* 349 */       String punc = m.getNode("term").label().value();
/* 350 */       if (endOfSentence.matcher(punc).matches())
/* 351 */         return "-eos";
/* 352 */       if (comma.matcher(punc).matches()) {
/* 353 */         return "-comma";
/*     */       }
/*     */       
/*     */ 
/* 357 */       if (lrb.matcher(punc).matches())
/* 358 */         return "-lrb";
/* 359 */       if (rrb.matcher(punc).matches())
/* 360 */         return "-rrb";
/* 361 */       if (dash.matcher(punc).matches())
/* 362 */         return "-dash";
/* 363 */       if (quote.matcher(punc).matches()) {
/* 364 */         return "-quote";
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 371 */       return "";
/*     */     }
/*     */     
/*     */     public String toString() {
/* 375 */       return "AnnotatePunctuationFunction";
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
/*     */   public int setOptionFlag(String[] args, int i)
/*     */   {
/* 393 */     boolean didSomething = true;
/* 394 */     while ((i < args.length) && (didSomething)) {
/* 395 */       didSomething = false;
/* 396 */       if (this.annotationPatterns.keySet().contains(args[i])) {
/* 397 */         Pair<TregexPattern, Function<TregexMatcher, String>> p = (Pair)this.annotationPatterns.get(args[i]);
/* 398 */         this.activeAnnotations.put(p.first(), p.second());
/* 399 */         didSomething = true;
/* 400 */         this.optionsString = (this.optionsString + "Option " + args[i] + " added annotation pattern " + p.first() + " with annotation " + p.second() + "\n");
/* 401 */       } else if (args[i].equals("-retainNPTmp")) {
/* 402 */         this.optionsString += "Retaining NP-TMP marking.\n";
/* 403 */         this.retainNPTmp = true;
/* 404 */         didSomething = true;
/* 405 */       } else if (args[i].equals("-discardX")) {
/* 406 */         this.optionsString += "Discarding X trees.\n";
/* 407 */         this.discardX = true;
/* 408 */         didSomething = true;
/* 409 */       } else if (args[i].equals("-changeNoLabels")) {
/* 410 */         this.optionsString += "Change no labels.\n";
/* 411 */         this.changeNoLabels = true;
/* 412 */         didSomething = true;
/* 413 */       } else if (args[i].equals("-markPRDverbs")) {
/* 414 */         this.optionsString += "Mark PRD.\n";
/* 415 */         this.retainPRD = true;
/* 416 */         didSomething = true;
/* 417 */       } else if (args[i].equals("-collinizerRetainsPunctuation")) {
/* 418 */         this.optionsString += "Collinizer retains punctuation.\n";
/* 419 */         this.collinizerRetainsPunctuation = true;
/* 420 */         didSomething = true;
/* 421 */       } else if (args[i].equals("-collinizerPruneRegex")) {
/* 422 */         this.optionsString = (this.optionsString + "Collinizer prune regex: " + args[(i + 1)] + "\n");
/* 423 */         this.collinizerPruneRegex = Pattern.compile(args[(i + 1)]);
/* 424 */         i++;
/* 425 */         didSomething = true;
/* 426 */       } else if (args[i].equals("-hf")) {
/*     */         try {
/* 428 */           this.headFinderClass = Class.forName(args[(i + 1)]).asSubclass(HeadFinder.class);
/* 429 */           this.optionsString = (this.optionsString + "HeadFinder class: " + args[(i + 1)] + "\n");
/*     */         }
/*     */         catch (ClassNotFoundException e) {
/* 432 */           System.err.println("Error -- can't find HeadFinder class" + args[(i + 1)]);
/*     */         }
/* 434 */         i++;
/* 435 */         didSomething = true;
/* 436 */       } else if (args[i].equals("-arabicFactored"))
/*     */       {
/* 438 */         String[] opts = { "-discardX", "-markNounNPargTakers", "-genitiveMark", "-splitPUNC", "-markContainsVerb", "-splitCC", "-markContainsSBAR" };
/*     */         
/* 440 */         setOptionFlag(opts, 0);
/* 441 */         didSomething = true;
/* 442 */       } else if (args[i].equals("-arabicTokenizerModel")) {
/* 443 */         String modelFile = args[(i + 1)];
/*     */         try {
/* 445 */           WordSegmenter aSeg = (WordSegmenter)Class.forName("edu.stanford.nlp.wordseg.ArabicSegmenter").newInstance();
/* 446 */           aSeg.loadSegmenter(modelFile);
/* 447 */           TokenizerFactory aTF = WordSegmentingTokenizer.factory(aSeg);
/* 448 */           ((ArabicTreebankLanguagePack)treebankLanguagePack()).setTokenizerFactory(aTF);
/*     */         } catch (RuntimeIOException ex) {
/* 450 */           System.err.println("Couldn't load ArabicSegmenter " + modelFile);
/* 451 */           ex.printStackTrace();
/*     */         } catch (Exception e) {
/* 453 */           System.err.println("Couldn't instantiate segmenter: edu.stanford.nlp.wordseg.ArabicSegmenter");
/* 454 */           e.printStackTrace();
/*     */         }
/* 456 */         i++;
/* 457 */         didSomething = true;
/*     */       }
/* 459 */       if (didSomething) {
/* 460 */         i++;
/*     */       }
/*     */     }
/* 463 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<? extends HasWord> defaultTestSentence()
/*     */   {
/* 472 */     return Sentence.toSentence(new String[] { "w", "lm", "tfd", "mElwmAt", "En", "ADrAr", "Aw", "DHAyA", "HtY", "AlAn", "." });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 484 */     int maxLength = Integer.parseInt(args[1]);
/* 485 */     TreebankLangParserParams tlpp = new ArabicTreebankParserParams();
/* 486 */     tlpp.setOptionFlag(args, 2);
/* 487 */     DiskTreebank trees = tlpp.diskTreebank();
/* 488 */     trees.loadPath(args[0]);
/*     */     
/*     */ 
/* 491 */     PrintWriter pw = tlpp.pw();
/*     */     
/* 493 */     for (Tree t : trees) {
/* 494 */       if (t.yield().size() <= maxLength) {
/* 495 */         pw.println(t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lexicon lex()
/*     */   {
/* 505 */     return new BaseLexicon();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lexicon lex(Options.LexOptions op)
/*     */   {
/* 514 */     return new BaseLexicon(op);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ArabicTreebankParserParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */