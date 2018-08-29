/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.stats.Counters;
/*     */ import edu.stanford.nlp.trees.BobChrisTreeNormalizer;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.PennTreeReader;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeReaderFactory;
/*     */ import edu.stanford.nlp.trees.TreeVisitor;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
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
/*     */ public class SisterAnnotationStats implements TreeVisitor
/*     */ {
/*     */   public static final boolean DO_TAGS = true;
/*  29 */   private Map nodeRules = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  35 */   private Map leftRules = new HashMap();
/*  36 */   private Map rightRules = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  41 */   public static final double[] CUTOFFS = { 250.0D, 500.0D, 1000.0D, 1500.0D };
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
/*  53 */     recurse(t, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void recurse(Tree t, Tree p)
/*     */   {
/*  60 */     if (!t.isLeaf()) { if (!t.isPreTerminal()) {}
/*  61 */     } else { return;
/*     */     }
/*  63 */     if ((p != null) && (!t.label().value().equals("ROOT"))) {
/*  64 */       sisterCounters(t, p);
/*     */     }
/*  66 */     Tree[] kids = t.children();
/*  67 */     for (int i = 0; i < kids.length; i++) {
/*  68 */       recurse(kids[i], t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static List<String> leftSisterLabels(Tree t, Tree p)
/*     */   {
/*  76 */     List<String> l = new ArrayList();
/*  77 */     if (p == null) {
/*  78 */       return l;
/*     */     }
/*  80 */     Tree[] kids = p.children();
/*  81 */     for (int i = 0; i < kids.length; i++) {
/*  82 */       if (kids[i].equals(t)) {
/*     */         break;
/*     */       }
/*  85 */       l.add(0, kids[i].label().value());
/*     */     }
/*     */     
/*  88 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static List<String> rightSisterLabels(Tree t, Tree p)
/*     */   {
/*  95 */     List<String> l = new ArrayList();
/*  96 */     if (p == null) {
/*  97 */       return l;
/*     */     }
/*  99 */     Tree[] kids = p.children();
/* 100 */     for (int i = kids.length - 1; i >= 0; i--) {
/* 101 */       if (kids[i].equals(t)) {
/*     */         break;
/*     */       }
/* 104 */       l.add(kids[i].label().value());
/*     */     }
/*     */     
/* 107 */     return l;
/*     */   }
/*     */   
/*     */   public static List<String> kidLabels(Tree t)
/*     */   {
/* 112 */     Tree[] kids = t.children();
/* 113 */     List<String> l = new ArrayList(kids.length);
/* 114 */     for (int i = 0; i < kids.length; i++) {
/* 115 */       l.add(kids[i].label().value());
/*     */     }
/* 117 */     return l;
/*     */   }
/*     */   
/*     */   protected void sisterCounters(Tree t, Tree p) {
/* 121 */     List rewrite = kidLabels(t);
/* 122 */     List left = leftSisterLabels(t, p);
/* 123 */     List right = rightSisterLabels(t, p);
/*     */     
/* 125 */     String label = t.label().value();
/*     */     
/* 127 */     if (!this.nodeRules.containsKey(label)) {
/* 128 */       this.nodeRules.put(label, new Counter());
/*     */     }
/*     */     
/* 131 */     if (!this.rightRules.containsKey(label)) {
/* 132 */       this.rightRules.put(label, new HashMap());
/*     */     }
/*     */     
/* 135 */     if (!this.leftRules.containsKey(label)) {
/* 136 */       this.leftRules.put(label, new HashMap());
/*     */     }
/*     */     
/*     */ 
/* 140 */     ((Counter)this.nodeRules.get(label)).incrementCount(rewrite);
/*     */     
/*     */ 
/* 143 */     sideCounters(label, rewrite, left, this.leftRules);
/* 144 */     sideCounters(label, rewrite, right, this.rightRules);
/*     */   }
/*     */   
/*     */   protected void sideCounters(String label, List rewrite, List sideSisters, Map sideRules)
/*     */   {
/* 149 */     for (Iterator i = sideSisters.iterator(); i.hasNext();) {
/* 150 */       String sis = (String)i.next();
/*     */       
/* 152 */       if (!((Map)sideRules.get(label)).containsKey(sis)) {
/* 153 */         ((Map)sideRules.get(label)).put(sis, new Counter());
/*     */       }
/*     */       
/* 156 */       ((Counter)((HashMap)sideRules.get(label)).get(sis)).incrementCount(rewrite);
/*     */     }
/*     */   }
/*     */   
/*     */   public void printStats()
/*     */   {
/* 162 */     NumberFormat nf = NumberFormat.getNumberInstance();
/* 163 */     nf.setMaximumFractionDigits(2);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */     StringBuffer[] javaSB = new StringBuffer[CUTOFFS.length];
/* 173 */     for (int i = 0; i < CUTOFFS.length; i++) {
/* 174 */       javaSB[i] = new StringBuffer("  private static String[] sisterSplit" + (i + 1) + " = new String[] {");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 179 */     ArrayList topScores = new ArrayList();
/*     */     
/* 181 */     for (Iterator it = this.nodeRules.keySet().iterator(); it.hasNext();) {
/* 182 */       ArrayList answers = new ArrayList();
/* 183 */       String label = (String)it.next();
/* 184 */       Counter cntr = (Counter)this.nodeRules.get(label);
/* 185 */       double support = cntr.totalCount();
/* 186 */       System.out.println("Node " + label + " support is " + support);
/*     */       
/*     */ 
/* 189 */       for (Iterator it2 = ((HashMap)this.leftRules.get(label)).keySet().iterator(); it2.hasNext();) {
/* 190 */         String sis = (String)it2.next();
/* 191 */         Counter cntr2 = (Counter)((HashMap)this.leftRules.get(label)).get(sis);
/* 192 */         double support2 = cntr2.totalCount();
/*     */         
/*     */ 
/* 195 */         double kl = Counters.klDivergence(cntr2, cntr);
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
/* 213 */         String annotatedLabel = label + "=l=" + sis;
/* 214 */         System.out.println("KL(" + annotatedLabel + "||" + label + ") = " + nf.format(kl) + "\t" + "support(" + sis + ") = " + support2);
/* 215 */         answers.add(new Pair(annotatedLabel, new Double(kl * support2)));
/* 216 */         topScores.add(new Pair(annotatedLabel, new Double(kl * support2)));
/*     */       }
/*     */       
/* 219 */       for (Iterator it2 = ((HashMap)this.rightRules.get(label)).keySet().iterator(); it2.hasNext();) {
/* 220 */         String sis = (String)it2.next();
/* 221 */         Counter cntr2 = (Counter)((HashMap)this.rightRules.get(label)).get(sis);
/* 222 */         double support2 = cntr2.totalCount();
/* 223 */         double kl = Counters.klDivergence(cntr2, cntr);
/* 224 */         String annotatedLabel = label + "=r=" + sis;
/* 225 */         System.out.println("KL(" + annotatedLabel + "||" + label + ") = " + nf.format(kl) + "\t" + "support(" + sis + ") = " + support2);
/* 226 */         answers.add(new Pair(annotatedLabel, new Double(kl * support2)));
/* 227 */         topScores.add(new Pair(annotatedLabel, new Double(kl * support2)));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 233 */       System.out.println("----");
/* 234 */       System.out.println("Sorted descending support * KL");
/* 235 */       Collections.sort(answers, new Comparator() {
/*     */         public int compare(Object o1, Object o2) {
/* 237 */           Pair p1 = (Pair)o1;
/* 238 */           Pair p2 = (Pair)o2;
/* 239 */           Double p12 = (Double)p1.second();
/* 240 */           Double p22 = (Double)p2.second();
/* 241 */           return p22.compareTo(p12);
/*     */         }
/* 243 */       });
/* 244 */       int i = 0; for (int size = answers.size(); i < size; i++) {
/* 245 */         Pair p = (Pair)answers.get(i);
/* 246 */         double psd = ((Double)p.second()).doubleValue();
/* 247 */         System.out.println(p.first() + ": " + nf.format(psd));
/* 248 */         if (psd >= CUTOFFS[0]) {
/* 249 */           String annotatedLabel = (String)p.first();
/* 250 */           for (int j = 0; j < CUTOFFS.length; j++) {
/* 251 */             if (psd < CUTOFFS[j]) {}
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 258 */       System.out.println();
/*     */     }
/*     */     
/*     */ 
/* 262 */     Collections.sort(topScores, new Comparator() {
/*     */       public int compare(Object o1, Object o2) {
/* 264 */         Pair p1 = (Pair)o1;
/* 265 */         Pair p2 = (Pair)o2;
/* 266 */         Double p12 = (Double)p1.second();
/* 267 */         Double p22 = (Double)p2.second();
/* 268 */         return p22.compareTo(p12);
/*     */       }
/* 270 */     });
/* 271 */     String outString = "All enriched categories, sorted by score\n";
/* 272 */     int i = 0; for (int size = topScores.size(); i < size; i++) {
/* 273 */       Pair p = (Pair)topScores.get(i);
/* 274 */       double psd = ((Double)p.second()).doubleValue();
/* 275 */       System.out.println(p.first() + ": " + nf.format(psd));
/*     */     }
/*     */     
/*     */ 
/* 279 */     System.out.println();
/* 280 */     System.out.println("  // Automatically generated by SisterAnnotationStats -- preferably don't edit");
/* 281 */     int k = CUTOFFS.length - 1;
/* 282 */     for (int j = 0; j < topScores.size(); j++) {
/* 283 */       Pair p = (Pair)topScores.get(j);
/* 284 */       double psd = ((Double)p.second()).doubleValue();
/* 285 */       if (psd < CUTOFFS[k]) {
/* 286 */         if (k == 0) {
/*     */           break;
/*     */         }
/* 289 */         k--;
/* 290 */         j--;
/*     */       }
/*     */       else
/*     */       {
/* 294 */         javaSB[k].append("\"").append(p.first());
/* 295 */         javaSB[k].append("\",");
/*     */       }
/*     */     }
/*     */     
/* 299 */     for (int i = 0; i < CUTOFFS.length; i++) {
/* 300 */       int len = javaSB[i].length();
/* 301 */       javaSB[i].replace(len - 2, len, "};");
/* 302 */       System.out.println(javaSB[i]);
/*     */     }
/* 304 */     System.out.print("  public static String[] sisterSplit = ");
/* 305 */     for (int i = CUTOFFS.length; i > 0; i--) {
/* 306 */       if (i == 1) {
/* 307 */         System.out.print("sisterSplit1");
/*     */       } else {
/* 309 */         System.out.print("selectiveSisterSplit" + i + " ? sisterSplit" + i + " : (");
/*     */       }
/*     */     }
/*     */     
/* 313 */     for (int i = CUTOFFS.length; i >= 0; i--) {
/* 314 */       System.out.print(")");
/*     */     }
/* 316 */     System.out.println(";");
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
/*     */   public static void main(String[] args)
/*     */   {
/* 330 */     Counter<String> c = new Counter();
/* 331 */     c.setCount("A", 0.0D);
/* 332 */     c.setCount("B", 1.0D);
/*     */     
/* 334 */     double d = Counters.klDivergence(c, c);
/* 335 */     System.out.println("KL Divergence: " + d);
/*     */     
/*     */ 
/* 338 */     String encoding = "UTF-8";
/* 339 */     if (args.length > 1) {
/* 340 */       encoding = args[1];
/*     */     }
/* 342 */     if (args.length < 1) {
/* 343 */       System.out.println("Usage: ParentAnnotationStats treebankPath");
/*     */     } else {
/* 345 */       SisterAnnotationStats pas = new SisterAnnotationStats();
/* 346 */       Treebank treebank = new edu.stanford.nlp.trees.DiskTreebank(new TreeReaderFactory()
/*     */       {
/* 348 */         public edu.stanford.nlp.trees.TreeReader newTreeReader(Reader in) { return new PennTreeReader(in, new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.StringLabelFactory()), new BobChrisTreeNormalizer()); } }, encoding);
/*     */       
/*     */ 
/* 351 */       treebank.loadPath(args[0]);
/* 352 */       treebank.apply(pas);
/* 353 */       pas.printStats();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\SisterAnnotationStats.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */