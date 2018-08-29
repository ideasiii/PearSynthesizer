/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasTag;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.ling.TaggedWord;
/*     */ import edu.stanford.nlp.ling.WordTag;
/*     */ import edu.stanford.nlp.parser.KBestViterbiParser;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class AbstractEval
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   protected final String str;
/*     */   protected final boolean runningAverages;
/*  29 */   private double precision = 0.0D;
/*  30 */   private double recall = 0.0D;
/*  31 */   private double f1 = 0.0D;
/*  32 */   private double num = 0.0D;
/*  33 */   private double exact = 0.0D;
/*     */   
/*  35 */   private double precision2 = 0.0D;
/*  36 */   private double recall2 = 0.0D;
/*  37 */   private double pnum2 = 0.0D;
/*  38 */   private double rnum2 = 0.0D;
/*     */   
/*     */   public AbstractEval() {
/*  41 */     this(true);
/*     */   }
/*     */   
/*     */   public AbstractEval(boolean runningAverages) {
/*  45 */     this("", runningAverages);
/*     */   }
/*     */   
/*     */   public AbstractEval(String str) {
/*  49 */     this(str, true);
/*     */   }
/*     */   
/*     */   public AbstractEval(String str, boolean runningAverages) {
/*  53 */     this.str = str;
/*  54 */     this.runningAverages = runningAverages;
/*     */   }
/*     */   
/*     */   public double getSentAveF1() {
/*  58 */     return this.f1 / this.num;
/*     */   }
/*     */   
/*     */   public double getEvalbF1() {
/*  62 */     return 2.0D / (this.rnum2 / this.recall2 + this.pnum2 / this.precision2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getEvalbF1Percent()
/*     */   {
/*  69 */     return getEvalbF1() * 100.0D;
/*     */   }
/*     */   
/*     */   public double getExact() {
/*  73 */     return this.exact / this.num;
/*     */   }
/*     */   
/*     */   public double getExactPercent() {
/*  77 */     return getExact() * 100.0D;
/*     */   }
/*     */   
/*     */   public int getNum() {
/*  81 */     return (int)this.num;
/*     */   }
/*     */   
/*     */   protected double precision(Set s1, Set s2)
/*     */   {
/*  86 */     double n = 0.0D;
/*  87 */     double p = 0.0D;
/*  88 */     for (Object o1 : s1) {
/*  89 */       if (s2.contains(o1)) {
/*  90 */         p += 1.0D;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */       n += 1.0D;
/*     */     }
/*     */     
/* 102 */     return n > 0.0D ? p / n : 0.0D;
/*     */   }
/*     */   
/*     */   abstract Set makeObjects(Tree paramTree);
/*     */   
/*     */   public void evaluate(Tree guess, Tree gold) {
/* 108 */     evaluate(guess, gold, new PrintWriter(System.out, true));
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
/*     */   public void evaluate(Tree guess, Tree gold, PrintWriter pw)
/*     */   {
/* 122 */     Set dep1 = makeObjects(guess);
/* 123 */     Set dep2 = makeObjects(gold);
/* 124 */     double curPrecision = precision(dep1, dep2);
/* 125 */     double curRecall = precision(dep2, dep1);
/* 126 */     double curF1 = (curPrecision > 0.0D) && (curRecall > 0.0D) ? 2.0D / (1.0D / curPrecision + 1.0D / curRecall) : 0.0D;
/* 127 */     this.precision += curPrecision;
/* 128 */     this.recall += curRecall;
/* 129 */     this.f1 += curF1;
/* 130 */     this.num += 1.0D;
/*     */     
/* 132 */     this.precision2 += dep1.size() * curPrecision;
/* 133 */     this.pnum2 += dep1.size();
/*     */     
/* 135 */     this.recall2 += dep2.size() * curRecall;
/* 136 */     this.rnum2 += dep2.size();
/*     */     
/* 138 */     if (curF1 > 0.9999D) {
/* 139 */       this.exact += 1.0D;
/*     */     }
/* 141 */     if (pw != null) {
/* 142 */       pw.print(" P: " + (int)(curPrecision * 10000.0D) / 100.0D);
/* 143 */       if (this.runningAverages) {
/* 144 */         pw.println(" (sent ave " + (int)(this.precision * 10000.0D / this.num) / 100.0D + ") (evalb " + (int)(this.precision2 * 10000.0D / this.pnum2) / 100.0D + ")");
/*     */       }
/* 146 */       pw.print(" R: " + (int)(curRecall * 10000.0D) / 100.0D);
/* 147 */       if (this.runningAverages) {
/* 148 */         pw.print(" (sent ave " + (int)(this.recall * 10000.0D / this.num) / 100.0D + ") (evalb " + (int)(this.recall2 * 10000.0D / this.rnum2) / 100.0D + ")");
/*     */       }
/* 150 */       pw.println();
/* 151 */       double cF1 = 2.0D / (this.rnum2 / this.recall2 + this.pnum2 / this.precision2);
/* 152 */       pw.print(this.str + " F1: " + (int)(curF1 * 10000.0D) / 100.0D);
/* 153 */       if (this.runningAverages) {
/* 154 */         pw.print(" (sent ave " + (int)(10000.0D * this.f1 / this.num) / 100.0D + ", evalb " + (int)(10000.0D * cF1) / 100.0D + ")   Exact: " + (int)(10000.0D * this.exact / this.num) / 100.0D);
/*     */       }
/* 156 */       pw.println(" N: " + getNum());
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
/*     */   public void display(boolean verbose)
/*     */   {
/* 192 */     display(verbose, new PrintWriter(System.out, true));
/*     */   }
/*     */   
/*     */   public void display(boolean verbose, PrintWriter pw) {
/* 196 */     double prec = this.precision2 / this.pnum2;
/* 197 */     double rec = this.recall2 / this.rnum2;
/* 198 */     double f = 2.0D / (1.0D / prec + 1.0D / rec);
/*     */     
/*     */ 
/*     */ 
/* 202 */     pw.println(this.str + " summary evalb: LP: " + (int)(10000.0D * prec) / 100.0D + " LR: " + (int)(10000.0D * rec) / 100.0D + " F1: " + (int)(10000.0D * f) / 100.0D + " Exact: " + (int)(10000.0D * this.exact / this.num) / 100.0D + " N: " + getNum());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class DependencyEval
/*     */     extends AbstractEval
/*     */   {
/*     */     private static final boolean DEBUG = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Filter<String> punctFilter;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Set makeObjects(Tree tree)
/*     */     {
/* 230 */       Set deps = new HashSet();
/* 231 */       for (Tree node : tree.subTreeList())
/*     */       {
/*     */ 
/* 234 */         if ((!node.isLeaf()) && (node.children().length >= 2))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 239 */           String head = ((HasWord)node.label()).word();
/* 240 */           boolean seenHead = false;
/* 241 */           for (int cNum = 0; cNum < node.children().length; cNum++) {
/* 242 */             Tree child = node.children()[cNum];
/* 243 */             String arg = ((HasWord)child.label()).word();
/* 244 */             if ((head.equals(arg)) && (!seenHead)) {
/* 245 */               seenHead = true;
/* 246 */             } else if (!this.punctFilter.accept(arg)) {
/* 247 */               deps.add(new edu.stanford.nlp.trees.UnnamedDependency(head, arg));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 254 */       return deps;
/*     */     }
/*     */     
/*     */ 
/*     */     public DependencyEval(String str, boolean runningAverages, Filter<String> punctFilter)
/*     */     {
/* 260 */       super(runningAverages);
/* 261 */       this.punctFilter = punctFilter;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TaggingEval extends AbstractEval
/*     */   {
/*     */     private static final boolean DEBUG = false;
/*     */     private static final boolean DEBUG_MORE = false;
/*     */     private final Lexicon lex;
/*     */     private final boolean useTag;
/*     */     
/*     */     Set makeObjects(Tree tree)
/*     */     {
/*     */       List twList;
/*     */       List twList;
/* 276 */       if (this.useTag) {
/* 277 */         twList = myExtractor(tree);
/*     */       } else {
/* 279 */         twList = tree.taggedYield();
/*     */       }
/* 281 */       Set set = new HashSet();
/* 282 */       int i = 0; for (int sz = twList.size(); i < sz; i++) {
/* 283 */         TaggedWord tw = (TaggedWord)twList.get(i);
/*     */         
/* 285 */         Pair positionWT = new Pair(new Integer(i), new WordTag(tw.value(), tw.tag()));
/*     */         
/*     */ 
/*     */ 
/* 289 */         set.add(positionWT);
/*     */       }
/*     */       
/* 292 */       return set;
/*     */     }
/*     */     
/*     */     public TaggingEval(String str) {
/* 296 */       this(str, true, null);
/*     */     }
/*     */     
/*     */     public TaggingEval(String str, boolean runningAverages, Lexicon lex) {
/* 300 */       this(str, runningAverages, lex, false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public TaggingEval(String str, boolean runningAverages, Lexicon lex, boolean useTag)
/*     */     {
/* 308 */       super(runningAverages);
/* 309 */       this.lex = lex;
/* 310 */       this.useTag = useTag;
/*     */     }
/*     */     
/*     */     private static Sentence myExtractor(Tree t) {
/* 314 */       return myExtractor(t, new Sentence());
/*     */     }
/*     */     
/*     */     private static Sentence myExtractor(Tree t, Sentence ty) {
/* 318 */       Tree[] kids = t.children();
/*     */       
/* 320 */       if ((kids.length == 1) && (kids[0].isLeaf())) {
/* 321 */         if ((t.label() instanceof HasTag))
/*     */         {
/* 323 */           ty.add(new TaggedWord(kids[0].label().value(), ((HasTag)t.label()).tag()));
/*     */         }
/*     */         else {
/* 326 */           ty.add(new TaggedWord(kids[0].label().value(), t.label().value()));
/*     */         }
/*     */       } else {
/* 329 */         for (int i = 0; i < kids.length; i++) {
/* 330 */           myExtractor(kids[i], ty);
/*     */         }
/*     */       }
/* 333 */       return ty;
/*     */     }
/*     */     
/*     */     public void evaluate(Tree guess, Tree gold, PrintWriter pw) {
/* 337 */       Sentence sGold = gold.taggedYield();
/*     */       Sentence sGuess;
/* 339 */       Sentence sGuess; if (this.useTag) {
/* 340 */         sGuess = myExtractor(guess);
/*     */       } else {
/* 342 */         sGuess = guess.taggedYield();
/*     */       }
/* 344 */       if (sGuess.size() != sGold.size()) {
/* 345 */         pw.println("Warning: yield length differs:");
/* 346 */         pw.println("Guess: " + sGuess);
/* 347 */         pw.println("Gold: " + sGold);
/*     */       }
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
/* 365 */       super.evaluate(guess, gold, pw);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class RuleErrorEval
/*     */     extends AbstractEval
/*     */   {
/* 373 */     private boolean verbose = false;
/*     */     
/* 375 */     private Counter over = new Counter();
/* 376 */     private Counter under = new Counter();
/*     */     
/*     */     protected static String localize(Tree tree) {
/* 379 */       if (tree.isLeaf()) {
/* 380 */         return "";
/*     */       }
/* 382 */       StringBuffer sb = new StringBuffer();
/* 383 */       sb.append(tree.label());
/* 384 */       sb.append(" ->");
/* 385 */       for (int i = 0; i < tree.children().length; i++) {
/* 386 */         sb.append(" ");
/* 387 */         sb.append(tree.children()[i].label());
/*     */       }
/* 389 */       return sb.toString();
/*     */     }
/*     */     
/*     */     Set makeObjects(Tree tree) {
/* 393 */       Set localTrees = new HashSet();
/* 394 */       for (Tree st : tree.subTreeList()) {
/* 395 */         localTrees.add(localize(st));
/*     */       }
/* 397 */       return localTrees;
/*     */     }
/*     */     
/*     */     public void evaluate(Tree t1, Tree t2, PrintWriter pw) {
/* 401 */       Set s1 = makeObjects(t1);
/* 402 */       Set s2 = makeObjects(t2);
/* 403 */       for (Object o1 : s1) {
/* 404 */         if (!s2.contains(o1)) {
/* 405 */           this.over.incrementCount(o1);
/*     */         }
/*     */       }
/* 408 */       for (Object o2 : s2) {
/* 409 */         if (!s1.contains(o2)) {
/* 410 */           this.under.incrementCount(o2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void display(Counter c, int num, PrintWriter pw) {
/* 416 */       List rules = new ArrayList(c.keySet());
/* 417 */       Collections.sort(rules, c.comparator(false));
/* 418 */       int rSize = rules.size();
/* 419 */       if (num > rSize) {
/* 420 */         num = rSize;
/*     */       }
/* 422 */       for (int i = 0; i < num; i++) {
/* 423 */         pw.println(rules.get(i) + " " + c.getCount(rules.get(i)));
/*     */       }
/*     */     }
/*     */     
/*     */     public void display(boolean verbose, PrintWriter pw) {
/* 428 */       this.verbose = verbose;
/* 429 */       pw.println("Most frequently underproposed rules:");
/* 430 */       display(this.under, verbose ? 100 : 10, pw);
/* 431 */       pw.println("Most frequently overproposed rules:");
/* 432 */       display(this.over, verbose ? 100 : 10, pw);
/*     */     }
/*     */     
/*     */     public RuleErrorEval(String str) {
/* 436 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class CatErrorEval
/*     */     extends AbstractEval
/*     */   {
/* 446 */     private Counter over = new Counter();
/* 447 */     private Counter under = new Counter();
/*     */     
/*     */     Set makeObjects(Tree tree)
/*     */     {
/* 451 */       return null;
/*     */     }
/*     */     
/*     */     List myMakeObjects(Tree tree) {
/* 455 */       List cats = new LinkedList();
/* 456 */       for (Tree st : tree.subTreeList()) {
/* 457 */         cats.add(st.value());
/*     */       }
/* 459 */       return cats;
/*     */     }
/*     */     
/*     */     public void evaluate(Tree t1, Tree t2, PrintWriter pw) {
/* 463 */       List s1 = myMakeObjects(t1);
/* 464 */       List s2 = myMakeObjects(t2);
/* 465 */       List del2 = new LinkedList(s2);
/*     */       
/*     */ 
/* 468 */       for (Object o1 : s1) {
/* 469 */         if (!del2.remove(o1)) {
/* 470 */           this.over.incrementCount(o1);
/*     */         }
/*     */       }
/* 473 */       for (Object o2 : s2) {
/* 474 */         if (!s1.remove(o2)) {
/* 475 */           this.under.incrementCount(o2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void display(Counter c, PrintWriter pw) {
/* 481 */       List cats = new ArrayList(c.keySet());
/* 482 */       Collections.sort(cats, c.comparator(false));
/* 483 */       for (Object ob : cats) {
/* 484 */         pw.println(ob + " " + c.getCount(ob));
/*     */       }
/*     */     }
/*     */     
/*     */     public void display(boolean verbose, PrintWriter pw) {
/* 489 */       pw.println("Most frequently underproposed categories:");
/* 490 */       display(this.under, pw);
/* 491 */       pw.println("Most frequently overproposed categories:");
/* 492 */       display(this.over, pw);
/*     */     }
/*     */     
/*     */     public CatErrorEval(String str) {
/* 496 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class ScoreEval
/*     */     extends AbstractEval
/*     */   {
/* 505 */     double totScore = 0.0D;
/* 506 */     double n = 0.0D;
/* 507 */     NumberFormat nf = new DecimalFormat("0.000");
/*     */     
/*     */     Set makeObjects(Tree tree) {
/* 510 */       return null;
/*     */     }
/*     */     
/*     */     public void recordScore(KBestViterbiParser parser, PrintWriter pw) {
/* 514 */       double score = parser.getBestScore();
/* 515 */       this.totScore += score;
/* 516 */       this.n += 1.0D;
/* 517 */       if (pw != null) {
/* 518 */         pw.print(this.str + " score: " + this.nf.format(score));
/* 519 */         if (this.runningAverages) {
/* 520 */           pw.print(" average score: " + this.nf.format(this.totScore / this.n));
/*     */         }
/* 522 */         pw.println();
/*     */       }
/*     */     }
/*     */     
/*     */     public void display(boolean verbose, PrintWriter pw) {
/* 527 */       if (pw != null) {
/* 528 */         pw.println(this.str + " total score: " + this.nf.format(this.totScore) + " average score: " + (this.n == 0.0D ? "N/A" : this.nf.format(this.totScore / this.n)));
/*     */       }
/*     */     }
/*     */     
/*     */     public ScoreEval(String str, boolean runningAverages)
/*     */     {
/* 534 */       super(runningAverages);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\AbstractEval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */