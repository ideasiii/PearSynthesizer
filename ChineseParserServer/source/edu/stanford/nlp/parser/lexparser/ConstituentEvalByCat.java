/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.LabeledConstituent;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ConstituentEvalByCat extends AbstractEval
/*     */ {
/*     */   LabeledConstituentEval lce;
/*     */   
/*     */   public ConstituentEvalByCat(String str, edu.stanford.nlp.trees.TreebankLanguagePack tlp)
/*     */   {
/*  18 */     super(str);
/*  19 */     this.lce = new LabeledConstituentEval(str, false, tlp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  24 */   private static final Set emptySet = new java.util.HashSet();
/*     */   
/*  26 */   Map<Label, Set<LabeledConstituent>> guessDeps = new java.util.HashMap();
/*  27 */   Map<Label, Set<LabeledConstituent>> goldDeps = new java.util.HashMap();
/*     */   
/*  29 */   Counter<Label> precisions = new Counter();
/*  30 */   Counter<Label> recalls = new Counter();
/*  31 */   Counter<Label> f1s = new Counter();
/*     */   
/*  33 */   Counter<Label> precisions2 = new Counter();
/*  34 */   Counter<Label> recalls2 = new Counter();
/*  35 */   Counter<Label> pnums2 = new Counter();
/*  36 */   Counter<Label> rnums2 = new Counter();
/*     */   
/*  38 */   double num = 0.0D;
/*     */   
/*     */   Set makeObjects(Tree tree)
/*     */   {
/*  42 */     return this.lce.makeObjects(tree);
/*     */   }
/*     */   
/*     */   private Map<Label, Set<LabeledConstituent>> makeObjectsByCat(Tree t)
/*     */   {
/*  47 */     Map<Label, Set<LabeledConstituent>> objMap = new java.util.HashMap();
/*  48 */     Set<LabeledConstituent> objSet = makeObjects(t);
/*  49 */     for (LabeledConstituent lc : objSet) {
/*  50 */       Label l = lc.label();
/*  51 */       if (!objMap.keySet().contains(l)) {
/*  52 */         objMap.put(l, new java.util.HashSet());
/*     */       }
/*  54 */       ((Set)objMap.get(l)).add(lc);
/*     */     }
/*  56 */     return objMap;
/*     */   }
/*     */   
/*     */   public void evaluate(Tree guess, Tree gold, PrintWriter pw)
/*     */   {
/*  61 */     this.guessDeps = makeObjectsByCat(guess);
/*  62 */     this.goldDeps = makeObjectsByCat(gold);
/*     */     
/*  64 */     Set<Label> cats = new java.util.HashSet();
/*  65 */     cats.addAll(this.guessDeps.keySet());
/*  66 */     cats.addAll(this.goldDeps.keySet());
/*     */     
/*  68 */     if (pw != null) {
/*  69 */       pw.println("========================================");
/*  70 */       pw.println("Labeled Bracketed Evaluation by Category");
/*  71 */       pw.println("========================================");
/*     */     }
/*     */     
/*  74 */     this.num += 1.0D;
/*     */     
/*  76 */     for (Label cat : cats) {
/*  77 */       Set thisGuessDeps = (Set)this.guessDeps.get(cat);
/*  78 */       Set thisGoldDeps = (Set)this.goldDeps.get(cat);
/*     */       
/*  80 */       if (thisGuessDeps == null) {
/*  81 */         thisGuessDeps = emptySet;
/*     */       }
/*  83 */       if (thisGoldDeps == null) {
/*  84 */         thisGoldDeps = emptySet;
/*     */       }
/*     */       
/*  87 */       double currentPrecision = precision(thisGuessDeps, thisGoldDeps);
/*  88 */       double currentRecall = precision(thisGoldDeps, thisGuessDeps);
/*     */       
/*  90 */       double currentF1 = (currentPrecision > 0.0D) && (currentRecall > 0.0D) ? 2.0D / (1.0D / currentPrecision + 1.0D / currentRecall) : 0.0D;
/*     */       
/*  92 */       this.precisions.incrementCount(cat, currentPrecision);
/*  93 */       this.recalls.incrementCount(cat, currentRecall);
/*  94 */       this.f1s.incrementCount(cat, currentF1);
/*     */       
/*  96 */       this.precisions2.incrementCount(cat, thisGuessDeps.size() * currentPrecision);
/*  97 */       this.pnums2.incrementCount(cat, thisGuessDeps.size());
/*     */       
/*  99 */       this.recalls2.incrementCount(cat, thisGoldDeps.size() * currentRecall);
/* 100 */       this.rnums2.incrementCount(cat, thisGoldDeps.size());
/*     */       
/* 102 */       if (pw != null) {
/* 103 */         pw.println(cat + "\tP: " + (int)(currentPrecision * 10000.0D) / 100.0D + " (sent ave " + (int)(this.precisions.getCount(cat) * 10000.0D / this.num) / 100.0D + ") (evalb " + (int)(this.precisions2.getCount(cat) * 10000.0D / this.pnums2.getCount(cat)) / 100.0D + ")");
/* 104 */         pw.println("\tR: " + (int)(currentRecall * 10000.0D) / 100.0D + " (sent ave " + (int)(this.recalls.getCount(cat) * 10000.0D / this.num) / 100.0D + ") (evalb " + (int)(this.recalls2.getCount(cat) * 10000.0D / this.rnums2.getCount(cat)) / 100.0D + ")");
/* 105 */         double cF1 = 2.0D / (this.rnums2.getCount(cat) / this.recalls2.getCount(cat) + this.pnums2.getCount(cat) / this.precisions2.getCount(cat));
/* 106 */         String emit = this.str + " F1: " + (int)(currentF1 * 10000.0D) / 100.0D + " (sent ave " + (int)(10000.0D * this.f1s.getCount(cat) / this.num) / 100.0D + ", evalb " + (int)(10000.0D * cF1) / 100.0D + ")";
/* 107 */         pw.println(emit);
/*     */       }
/*     */     }
/* 110 */     if (pw != null) {
/* 111 */       pw.println("========================================");
/*     */     }
/*     */   }
/*     */   
/*     */   public void display(boolean verbose, PrintWriter pw)
/*     */   {
/* 117 */     NumberFormat nf = new java.text.DecimalFormat("0.00");
/* 118 */     Set<Label> cats = new java.util.HashSet();
/* 119 */     cats.addAll(this.precisions.keySet());
/* 120 */     cats.addAll(this.recalls.keySet());
/*     */     
/* 122 */     pw.println("========================================");
/* 123 */     pw.println("Labeled Bracketed Evaluation by Category -- final statistics");
/* 124 */     pw.println("========================================");
/*     */     
/* 126 */     for (Label cat : cats) {
/* 127 */       double pnum2 = this.pnums2.getCount(cat);
/* 128 */       double rnum2 = this.rnums2.getCount(cat);
/* 129 */       double prec = this.precisions2.getCount(cat) / pnum2;
/* 130 */       double rec = this.recalls2.getCount(cat) / rnum2;
/* 131 */       double f = 2.0D / (1.0D / prec + 1.0D / rec);
/*     */       
/* 133 */       pw.println(cat + "\tLP: " + (pnum2 == 0.0D ? " N/A" : nf.format(prec)) + "\tguessed: " + (int)pnum2 + "\tLR: " + (rnum2 == 0.0D ? " N/A" : nf.format(rec)) + "\tgold:  " + (int)rnum2 + "\tF1: " + ((pnum2 == 0.0D) || (rnum2 == 0.0D) ? " N/A" : nf.format(f)));
/*     */     }
/*     */     
/*     */ 
/* 137 */     pw.println("========================================");
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ConstituentEvalByCat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */