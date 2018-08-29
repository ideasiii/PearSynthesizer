/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.stats.Counters;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import edu.stanford.nlp.util.PriorityQueue;
/*     */ import java.io.PrintStream;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ParentAnnotationStats implements edu.stanford.nlp.trees.TreeVisitor
/*     */ {
/*     */   private TreebankLanguagePack tlp;
/*     */   
/*     */   private ParentAnnotationStats()
/*     */   {
/*  28 */     this(null);
/*     */   }
/*     */   
/*     */   private ParentAnnotationStats(TreebankLanguagePack tlp) {
/*  32 */     this.tlp = tlp;
/*     */   }
/*     */   
/*  35 */   private static boolean doTags = false;
/*     */   
/*  37 */   private Map nodeRules = new HashMap();
/*  38 */   private Map pRules = new HashMap();
/*  39 */   private Map gPRules = new HashMap();
/*     */   
/*     */ 
/*  42 */   private Map tagNodeRules = new HashMap();
/*  43 */   private Map tagPRules = new HashMap();
/*  44 */   private Map tagGPRules = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  49 */   public static final double[] CUTOFFS = { 100.0D, 200.0D, 500.0D, 1000.0D };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final double SUPPCUTOFF = 100.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void visitTree(Tree t)
/*     */   {
/*  61 */     processTreeHelper("TOP", "TOP", t);
/*     */   }
/*     */   
/*     */   public static List kidLabels(Tree t) {
/*  65 */     Tree[] kids = t.children();
/*  66 */     List l = new ArrayList(kids.length);
/*  67 */     for (int i = 0; i < kids.length; i++) {
/*  68 */       l.add(kids[i].label().value());
/*     */     }
/*  70 */     return l;
/*     */   }
/*     */   
/*     */   public void processTreeHelper(String gP, String p, Tree t) {
/*  74 */     if ((!t.isLeaf()) && ((doTags) || (!t.isPreTerminal()))) { Map gpr;
/*     */       Map nr;
/*     */       Map pr;
/*     */       Map gpr;
/*  78 */       if (t.isPreTerminal()) {
/*  79 */         Map nr = this.tagNodeRules;
/*  80 */         Map pr = this.tagPRules;
/*  81 */         gpr = this.tagGPRules;
/*     */       } else {
/*  83 */         nr = this.nodeRules;
/*  84 */         pr = this.pRules;
/*  85 */         gpr = this.gPRules;
/*     */       }
/*  87 */       String n = t.label().value();
/*  88 */       if (this.tlp != null) {
/*  89 */         p = this.tlp.basicCategory(p);
/*  90 */         gP = this.tlp.basicCategory(gP);
/*     */       }
/*  92 */       List kidn = kidLabels(t);
/*  93 */       Counter cntr = (Counter)nr.get(n);
/*  94 */       if (cntr == null) {
/*  95 */         cntr = new Counter();
/*  96 */         nr.put(n, cntr);
/*     */       }
/*  98 */       cntr.incrementCount(kidn);
/*  99 */       List pairStr = new ArrayList(2);
/* 100 */       pairStr.add(n);
/* 101 */       pairStr.add(p);
/* 102 */       cntr = (Counter)pr.get(pairStr);
/* 103 */       if (cntr == null) {
/* 104 */         cntr = new Counter();
/* 105 */         pr.put(pairStr, cntr);
/*     */       }
/* 107 */       cntr.incrementCount(kidn);
/* 108 */       List tripleStr = new ArrayList(3);
/* 109 */       tripleStr.add(n);
/* 110 */       tripleStr.add(p);
/* 111 */       tripleStr.add(gP);
/* 112 */       cntr = (Counter)gpr.get(tripleStr);
/* 113 */       if (cntr == null) {
/* 114 */         cntr = new Counter();
/* 115 */         gpr.put(tripleStr, cntr);
/*     */       }
/* 117 */       cntr.incrementCount(kidn);
/* 118 */       Tree[] kids = t.children();
/* 119 */       for (int i = 0; i < kids.length; i++) {
/* 120 */         processTreeHelper(p, n, kids[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void printStats()
/*     */   {
/* 127 */     NumberFormat nf = NumberFormat.getNumberInstance();
/* 128 */     nf.setMaximumFractionDigits(2);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */     StringBuffer[] javaSB = new StringBuffer[CUTOFFS.length];
/* 138 */     for (int i = 0; i < CUTOFFS.length; i++) {
/* 139 */       javaSB[i] = new StringBuffer("  private static String[] splitters" + (i + 1) + " = new String[] {");
/*     */     }
/*     */     
/* 142 */     Counter allScores = new Counter();
/*     */     
/* 144 */     for (Iterator it = this.nodeRules.keySet().iterator(); it.hasNext();) {
/* 145 */       ArrayList answers = new ArrayList();
/* 146 */       String node = (String)it.next();
/* 147 */       Counter cntr = (Counter)this.nodeRules.get(node);
/* 148 */       double support = cntr.totalCount();
/* 149 */       System.out.println("Node " + node + " support is " + support);
/* 150 */       for (Iterator it2 = this.pRules.keySet().iterator(); it2.hasNext();) {
/* 151 */         List key = (List)it2.next();
/* 152 */         if (key.get(0).equals(node)) {
/* 153 */           Counter cntr2 = (Counter)this.pRules.get(key);
/* 154 */           double support2 = cntr2.totalCount();
/* 155 */           double kl = Counters.klDivergence(cntr2, cntr);
/* 156 */           System.out.println("KL(" + key + "||" + node + ") = " + nf.format(kl) + "\t" + "support(" + key + ") = " + support2);
/* 157 */           double score = kl * support2;
/* 158 */           answers.add(new Pair(key, new Double(score)));
/* 159 */           allScores.setCount(key, score);
/*     */         }
/*     */       }
/* 162 */       System.out.println("----");
/* 163 */       System.out.println("Sorted descending support * KL");
/* 164 */       Collections.sort(answers, new Comparator() {
/*     */         public int compare(Object o1, Object o2) {
/* 166 */           Pair p1 = (Pair)o1;
/* 167 */           Pair p2 = (Pair)o2;
/* 168 */           Double p12 = (Double)p1.second();
/* 169 */           Double p22 = (Double)p2.second();
/* 170 */           return p22.compareTo(p12);
/*     */         }
/* 172 */       });
/* 173 */       int i = 0; for (int size = answers.size(); i < size; i++) {
/* 174 */         Pair p = (Pair)answers.get(i);
/* 175 */         double psd = ((Double)p.second()).doubleValue();
/* 176 */         System.out.println(p.first() + ": " + nf.format(psd));
/* 177 */         if (psd >= CUTOFFS[0]) {
/* 178 */           List lst = (List)p.first();
/* 179 */           String nd = (String)lst.get(0);
/* 180 */           String par = (String)lst.get(1);
/* 181 */           for (int j = 0; j < CUTOFFS.length; j++) {
/* 182 */             if (psd >= CUTOFFS[j]) {
/* 183 */               javaSB[j].append("\"").append(nd).append("^");
/* 184 */               javaSB[j].append(par).append("\", ");
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 189 */       System.out.println();
/*     */     }
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
/* 247 */     for (Iterator it = this.pRules.keySet().iterator(); it.hasNext();) {
/* 248 */       ArrayList answers = new ArrayList();
/* 249 */       List node = (List)it.next();
/* 250 */       Counter cntr = (Counter)this.pRules.get(node);
/* 251 */       double support = cntr.totalCount();
/* 252 */       if (support >= 100.0D)
/*     */       {
/*     */ 
/* 255 */         System.out.println("Node " + node + " support is " + support);
/* 256 */         for (Iterator it2 = this.gPRules.keySet().iterator(); it2.hasNext();) {
/* 257 */           List key = (List)it2.next();
/* 258 */           if ((key.get(0).equals(node.get(0))) && (key.get(1).equals(node.get(1)))) {
/* 259 */             Counter cntr2 = (Counter)this.gPRules.get(key);
/* 260 */             double support2 = cntr2.totalCount();
/* 261 */             double kl = Counters.klDivergence(cntr2, cntr);
/* 262 */             System.out.println("KL(" + key + "||" + node + ") = " + nf.format(kl) + "\t" + "support(" + key + ") = " + support2);
/* 263 */             double score = kl * support2;
/* 264 */             answers.add(new Pair(key, new Double(score)));
/* 265 */             allScores.setCount(key, score);
/*     */           }
/*     */         }
/* 268 */         System.out.println("----");
/* 269 */         System.out.println("Sorted descending support * KL");
/* 270 */         Collections.sort(answers, new Comparator() {
/*     */           public int compare(Object o1, Object o2) {
/* 272 */             Pair p1 = (Pair)o1;
/* 273 */             Pair p2 = (Pair)o2;
/* 274 */             Double p12 = (Double)p1.second();
/* 275 */             Double p22 = (Double)p2.second();
/* 276 */             return p22.compareTo(p12);
/*     */           }
/* 278 */         });
/* 279 */         int i = 0; for (int size = answers.size(); i < size; i++) {
/* 280 */           Pair p = (Pair)answers.get(i);
/* 281 */           double psd = ((Double)p.second()).doubleValue();
/* 282 */           System.out.println(p.first() + ": " + nf.format(psd));
/* 283 */           if (psd >= CUTOFFS[0]) {
/* 284 */             List lst = (List)p.first();
/* 285 */             String nd = (String)lst.get(0);
/* 286 */             String par = (String)lst.get(1);
/* 287 */             String gpar = (String)lst.get(2);
/* 288 */             for (int j = 0; j < CUTOFFS.length; j++) {
/* 289 */               if (psd >= CUTOFFS[j]) {
/* 290 */                 javaSB[j].append("\"").append(nd).append("^");
/* 291 */                 javaSB[j].append(par).append("~");
/* 292 */                 javaSB[j].append(gpar).append("\", ");
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 297 */         System.out.println();
/*     */       } }
/* 299 */     System.out.println();
/*     */     
/* 301 */     System.out.println("All scores:");
/* 302 */     PriorityQueue pq = Counters.toPriorityQueue(allScores);
/* 303 */     while (!pq.isEmpty()) {
/* 304 */       Object key = pq.getFirst();
/* 305 */       double score = pq.getPriority(key);
/* 306 */       pq.removeFirst();
/* 307 */       System.out.println(key + "\t" + score);
/*     */     }
/*     */     
/* 310 */     System.out.println("  // Automatically generated by ParentAnnotationStats -- preferably don't edit");
/* 311 */     for (int i = 0; i < CUTOFFS.length; i++) {
/* 312 */       int len = javaSB[i].length();
/* 313 */       javaSB[i].replace(len - 2, len, "};");
/* 314 */       System.out.println(javaSB[i]);
/*     */     }
/* 316 */     System.out.print("  public static HashSet splitters = new HashSet(Arrays.asList(");
/* 317 */     for (int i = CUTOFFS.length; i > 0; i--) {
/* 318 */       if (i == 1) {
/* 319 */         System.out.print("splitters1");
/*     */       } else {
/* 321 */         System.out.print("selectiveSplit" + i + " ? splitters" + i + " : (");
/*     */       }
/*     */     }
/*     */     
/* 325 */     for (int i = CUTOFFS.length; i >= 0; i--) {
/* 326 */       System.out.print(")");
/*     */     }
/* 328 */     System.out.println(";");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void getSplitters(double cutOff, Map nr, Map pr, Map gpr, Set splitters)
/*     */   {
/* 335 */     for (Iterator it = nr.keySet().iterator(); it.hasNext();) {
/* 336 */       ArrayList answers = new ArrayList();
/* 337 */       String node = (String)it.next();
/* 338 */       Counter cntr = (Counter)nr.get(node);
/* 339 */       double support = cntr.totalCount();
/* 340 */       for (Iterator it2 = pr.keySet().iterator(); it2.hasNext();) {
/* 341 */         List key = (List)it2.next();
/* 342 */         if (key.get(0).equals(node)) {
/* 343 */           Counter cntr2 = (Counter)pr.get(key);
/* 344 */           double support2 = cntr2.totalCount();
/* 345 */           double kl = Counters.klDivergence(cntr2, cntr);
/* 346 */           answers.add(new Pair(key, new Double(kl * support2)));
/*     */         }
/*     */       }
/* 349 */       Collections.sort(answers, new Comparator() {
/*     */         public int compare(Object o1, Object o2) {
/* 351 */           Pair p1 = (Pair)o1;
/* 352 */           Pair p2 = (Pair)o2;
/* 353 */           Double p12 = (Double)p1.second();
/* 354 */           Double p22 = (Double)p2.second();
/* 355 */           return p22.compareTo(p12);
/*     */         }
/* 357 */       });
/* 358 */       int i = 0; for (int size = answers.size(); i < size; i++) {
/* 359 */         Pair p = (Pair)answers.get(i);
/* 360 */         double psd = ((Double)p.second()).doubleValue();
/* 361 */         if (psd >= cutOff) {
/* 362 */           List lst = (List)p.first();
/* 363 */           String nd = (String)lst.get(0);
/* 364 */           String par = (String)lst.get(1);
/* 365 */           String name = nd + "^" + par;
/* 366 */           splitters.add(name);
/*     */         }
/*     */       }
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 417 */     for (Iterator it = pr.keySet().iterator(); it.hasNext();) {
/* 418 */       ArrayList answers = new ArrayList();
/* 419 */       List node = (List)it.next();
/* 420 */       Counter cntr = (Counter)pr.get(node);
/* 421 */       double support = cntr.totalCount();
/* 422 */       if (support >= 100.0D)
/*     */       {
/*     */ 
/* 425 */         for (Iterator it2 = gpr.keySet().iterator(); it2.hasNext();) {
/* 426 */           List key = (List)it2.next();
/* 427 */           if ((key.get(0).equals(node.get(0))) && (key.get(1).equals(node.get(1))))
/*     */           {
/* 429 */             Counter cntr2 = (Counter)gpr.get(key);
/* 430 */             double support2 = cntr2.totalCount();
/* 431 */             double kl = Counters.klDivergence(cntr2, cntr);
/* 432 */             answers.add(new Pair(key, new Double(kl * support2)));
/*     */           }
/*     */         }
/* 435 */         Collections.sort(answers, new Comparator() {
/*     */           public int compare(Object o1, Object o2) {
/* 437 */             Pair p1 = (Pair)o1;
/* 438 */             Pair p2 = (Pair)o2;
/* 439 */             Double p12 = (Double)p1.second();
/* 440 */             Double p22 = (Double)p2.second();
/* 441 */             return p22.compareTo(p12);
/*     */           }
/* 443 */         });
/* 444 */         int i = 0; for (int size = answers.size(); i < size; i++) {
/* 445 */           Pair p = (Pair)answers.get(i);
/* 446 */           double psd = ((Double)p.second()).doubleValue();
/* 447 */           if (psd >= cutOff) {
/* 448 */             List lst = (List)p.first();
/* 449 */             String nd = (String)lst.get(0);
/* 450 */             String par = (String)lst.get(1);
/* 451 */             String gpar = (String)lst.get(2);
/* 452 */             String name = nd + "^" + par + "~" + gpar;
/* 453 */             splitters.add(name);
/*     */           }
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
/*     */   public static void main(String[] args)
/*     */   {
/* 470 */     if (args.length < 1) {
/* 471 */       System.out.println("Usage: java edu.stanford.nlp.parser.lexparser.ParentAnnotationStats [-tags] treebankPath");
/*     */     } else {
/* 473 */       int i = 0;
/* 474 */       boolean useCutOff = false;
/* 475 */       double cutOff = 0.0D;
/* 476 */       while (args[i].startsWith("-")) {
/* 477 */         if (args[i].equals("-tags")) {
/* 478 */           doTags = true;
/* 479 */           i++;
/* 480 */         } else if ((args[i].equals("-cutOff")) && (i + 1 < args.length)) {
/* 481 */           useCutOff = true;
/* 482 */           cutOff = Double.parseDouble(args[(i + 1)]);
/* 483 */           i += 2;
/*     */         } else {
/* 485 */           System.err.println("Unknown option: " + args[i]);
/* 486 */           i++;
/*     */         }
/*     */       }
/*     */       
/* 490 */       Treebank treebank = new edu.stanford.nlp.trees.DiskTreebank(new edu.stanford.nlp.trees.TreeReaderFactory() {
/*     */         public edu.stanford.nlp.trees.TreeReader newTreeReader(java.io.Reader in) {
/* 492 */           return new edu.stanford.nlp.trees.PennTreeReader(in, new edu.stanford.nlp.trees.LabeledScoredTreeFactory(new edu.stanford.nlp.ling.StringLabelFactory()), new edu.stanford.nlp.trees.BobChrisTreeNormalizer());
/*     */         }
/* 494 */       });
/* 495 */       treebank.loadPath(args[i]);
/*     */       
/* 497 */       if (useCutOff) {
/* 498 */         Set<String> splitters = getSplitCategories(treebank, doTags, 0, cutOff, cutOff, null);
/* 499 */         System.out.println(splitters);
/*     */       } else {
/* 501 */         ParentAnnotationStats pas = new ParentAnnotationStats();
/* 502 */         treebank.apply(pas);
/* 503 */         pas.printStats();
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
/*     */   public static Set<String> getSplitCategories(Treebank t, double cutOff, TreebankLanguagePack tlp)
/*     */   {
/* 521 */     return getSplitCategories(t, true, 0, cutOff, cutOff, tlp);
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
/*     */   public static Set<String> getSplitCategories(Treebank t, boolean doTags, int algorithm, double phrasalCutOff, double tagCutOff, TreebankLanguagePack tlp)
/*     */   {
/* 536 */     doTags = doTags;
/* 537 */     ParentAnnotationStats pas = new ParentAnnotationStats(tlp);
/* 538 */     t.apply(pas);
/* 539 */     Set<String> splitters = new java.util.HashSet();
/* 540 */     pas.getSplitters(phrasalCutOff, pas.nodeRules, pas.pRules, pas.gPRules, splitters);
/* 541 */     pas.getSplitters(tagCutOff, pas.tagNodeRules, pas.tagPRules, pas.tagGPRules, splitters);
/* 542 */     return splitters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<String> getEnglishSplitCategories(String treebankRoot)
/*     */   {
/* 553 */     TreebankLangParserParams tlpParams = new EnglishTreebankParserParams();
/* 554 */     Treebank trees = tlpParams.memoryTreebank();
/* 555 */     trees.loadPath(treebankRoot, new edu.stanford.nlp.io.NumberRangeFileFilter(200, 2199, true));
/* 556 */     return getSplitCategories(trees, 300.0D, tlpParams.treebankLanguagePack());
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ParentAnnotationStats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */