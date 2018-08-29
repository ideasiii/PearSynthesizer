/*     */ package edu.stanford.nlp.stats;
/*     */ 
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import edu.stanford.nlp.util.Sets;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ public class EquivalenceClassEval
/*     */ {
/*     */   public void setBagEval(boolean bagEval)
/*     */   {
/*  44 */     this.bagEval = bagEval;
/*     */   }
/*     */   
/*  47 */   private boolean bagEval = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  53 */   public static final EquivalenceClasser NULL_EQUIVALENCE_CLASSER = new EquivalenceClasser() {
/*     */     public Object equivalenceClass(Object o) {
/*  55 */       return null;
/*     */     }
/*     */   };
/*     */   
/*  59 */   private boolean verbose = false;
/*     */   
/*     */   EquivalenceClasser eq;
/*     */   EquivalenceClassEval.Eval.CollectionContainsChecker checker;
/*     */   String summaryName;
/*     */   Counter guessed;
/*     */   Counter guessedCorrect;
/*     */   Counter gold;
/*     */   Counter goldCorrect;
/*     */   
/*     */   public EquivalenceClassEval()
/*     */   {
/*  71 */     this(NULL_EQUIVALENCE_CLASSER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EquivalenceClassEval(EquivalenceClasser eq)
/*     */   {
/*  80 */     this(eq, "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EquivalenceClassEval(EqualityChecker e)
/*     */   {
/*  88 */     this(NULL_EQUIVALENCE_CLASSER, e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EquivalenceClassEval(EquivalenceClasser eq, String name)
/*     */   {
/*  96 */     this(eq, DEFAULT_CHECKER, name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EquivalenceClassEval(EquivalenceClasser eq, EqualityChecker e)
/*     */   {
/* 104 */     this(eq, e, "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EquivalenceClassEval(EquivalenceClasser eq, EqualityChecker e, String summaryName)
/*     */   {
/* 112 */     this(eq, new EquivalenceClassEval.Eval.CollectionContainsChecker(e), summaryName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   EquivalenceClassEval(EquivalenceClasser eq, EquivalenceClassEval.Eval.CollectionContainsChecker checker, String summaryName)
/*     */   {
/* 121 */     this.guessed = new Counter();
/* 122 */     this.guessedCorrect = new Counter();
/* 123 */     this.gold = new Counter();
/* 124 */     this.goldCorrect = new Counter();
/*     */     
/* 126 */     this.lastPrecision = new Counter();
/* 127 */     this.lastRecall = new Counter();
/* 128 */     this.lastF1 = new Counter();
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
/* 378 */     numberFormat.setMaximumFractionDigits(4);
/* 379 */     numberFormat.setMinimumFractionDigits(4);
/* 380 */     numberFormat.setMinimumIntegerDigits(1);
/* 381 */     numberFormat.setMaximumIntegerDigits(1);this.eq = eq;this.checker = checker;this.summaryName = summaryName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Counter lastPrecision;
/*     */   
/*     */ 
/*     */   private Counter lastRecall;
/*     */   
/*     */   private Counter lastF1;
/*     */   
/*     */   private Counter previousGuessed;
/*     */   
/*     */   private Counter previousGuessedCorrect;
/*     */   
/*     */   private Counter previousGold;
/*     */   
/*     */   private Counter previousGoldCorrect;
/*     */   
/*     */   public void eval(Collection guesses, Collection golds)
/*     */   {
/* 143 */     eval(guesses, golds, new PrintWriter(System.out, true));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void eval(Collection guesses, Collection golds, PrintWriter pw)
/*     */   {
/* 153 */     if (this.verbose) {
/* 154 */       System.out.println("evaluating precision...");
/*     */     }
/* 156 */     Pair precision = evalPrecision(guesses, golds);
/* 157 */     this.previousGuessed = ((Counter)precision.first());
/* 158 */     this.guessed.addAll(this.previousGuessed);
/* 159 */     this.previousGuessedCorrect = ((Counter)precision.second());
/* 160 */     this.guessedCorrect.addAll(this.previousGuessedCorrect);
/*     */     
/* 162 */     if (this.verbose) {
/* 163 */       System.out.println("evaluating recall...");
/*     */     }
/* 165 */     Pair recall = evalPrecision(golds, guesses);
/* 166 */     this.previousGold = ((Counter)recall.first());
/* 167 */     this.gold.addAll(this.previousGold);
/* 168 */     this.previousGoldCorrect = ((Counter)recall.second());
/* 169 */     this.goldCorrect.addAll(this.previousGoldCorrect);
/*     */   }
/*     */   
/*     */   Pair evalPrecision(Collection guesses, Collection golds)
/*     */   {
/* 174 */     Collection internalGuesses = null;
/* 175 */     Collection internalGolds = null;
/* 176 */     if (this.bagEval) {
/* 177 */       internalGuesses = new ArrayList(guesses.size());
/* 178 */       internalGolds = new ArrayList(golds.size());
/*     */     }
/*     */     else {
/* 181 */       internalGuesses = new HashSet(guesses.size());
/* 182 */       internalGolds = new HashSet(golds.size());
/*     */     }
/* 184 */     internalGuesses.addAll(guesses);
/* 185 */     internalGolds.addAll(golds);
/* 186 */     Counter thisGuessed = new Counter();
/* 187 */     Counter thisCorrect = new Counter();
/* 188 */     for (Object o : internalGuesses) {
/* 189 */       Object equivalenceClass = this.eq.equivalenceClass(o);
/* 190 */       thisGuessed.incrementCount(equivalenceClass);
/* 191 */       if (this.checker.contained(o, internalGolds)) {
/* 192 */         thisCorrect.incrementCount(equivalenceClass);
/* 193 */         removeItem(o, internalGolds, this.checker);
/*     */       }
/* 195 */       else if (this.verbose) {
/* 196 */         System.out.println("Eval missed " + o);
/*     */       }
/*     */     }
/*     */     
/* 200 */     return new Pair(thisGuessed, thisCorrect);
/*     */   }
/*     */   
/*     */ 
/*     */   private static void removeItem(Object o, Collection c, EquivalenceClassEval.Eval.CollectionContainsChecker checker)
/*     */   {
/* 206 */     for (Object o1 : c) {
/* 207 */       if (checker.contained(o, Collections.singleton(o1))) {
/* 208 */         c.remove(o1);
/* 209 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void display()
/*     */   {
/* 219 */     display(new PrintWriter(System.out, true));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void display(PrintWriter pw)
/*     */   {
/* 226 */     pw.println("*********Final " + this.summaryName + " eval stats by antecedent category***********");
/* 227 */     Set keys = new HashSet();
/* 228 */     keys.addAll(this.guessed.keySet());
/* 229 */     keys.addAll(this.gold.keySet());
/* 230 */     displayHelper(keys, pw, this.guessed, this.guessedCorrect, this.gold, this.goldCorrect);
/* 231 */     pw.println("Finished final " + this.summaryName + " eval stats.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void displayLast()
/*     */   {
/* 238 */     displayLast(new PrintWriter(System.out, true));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void displayLast(PrintWriter pw)
/*     */   {
/* 245 */     Set keys = new HashSet();
/* 246 */     keys.addAll(this.previousGuessed.keySet());
/* 247 */     keys.addAll(this.previousGold.keySet());
/* 248 */     displayHelper(keys, pw, this.previousGuessed, this.previousGuessedCorrect, this.previousGold, this.previousGoldCorrect);
/*     */   }
/*     */   
/*     */   public double precision(Object key) {
/* 252 */     return percentage(key, this.guessed, this.guessedCorrect);
/*     */   }
/*     */   
/*     */   public double recall(Object key) {
/* 256 */     return percentage(key, this.gold, this.goldCorrect);
/*     */   }
/*     */   
/*     */   public double lastPrecision(Object key) {
/* 260 */     return percentage(key, this.previousGuessed, this.previousGuessedCorrect);
/*     */   }
/*     */   
/*     */   public Counter lastPrecision() {
/* 264 */     Counter result = new Counter();
/* 265 */     result.addAll(this.previousGuessedCorrect);
/* 266 */     result.divideBy(this.previousGuessed);
/* 267 */     return result;
/*     */   }
/*     */   
/*     */   public double lastRecall(Object key) {
/* 271 */     return percentage(key, this.previousGold, this.previousGoldCorrect);
/*     */   }
/*     */   
/*     */   public Counter lastRecall() {
/* 275 */     Counter result = new Counter();
/* 276 */     result.addAll(this.previousGoldCorrect);
/* 277 */     result.divideBy(this.previousGold);
/* 278 */     return result;
/*     */   }
/*     */   
/*     */   public double lastNumGuessed(Object key) {
/* 282 */     return this.previousGuessed.getCount(key);
/*     */   }
/*     */   
/*     */   public Counter lastNumGuessed() {
/* 286 */     return this.previousGuessed;
/*     */   }
/*     */   
/*     */   public Counter lastNumGuessedCorrect() {
/* 290 */     return this.previousGuessedCorrect;
/*     */   }
/*     */   
/*     */   public double lastNumGolds(Object key) {
/* 294 */     return this.previousGold.getCount(key);
/*     */   }
/*     */   
/*     */   public Counter lastNumGolds() {
/* 298 */     return this.previousGold;
/*     */   }
/*     */   
/*     */   public Counter lastNumGoldsCorrect() {
/* 302 */     return this.previousGoldCorrect;
/*     */   }
/*     */   
/*     */   public double f1(Object key)
/*     */   {
/* 307 */     return f1(precision(key), recall(key));
/*     */   }
/*     */   
/*     */   public double lastF1(Object key) {
/* 311 */     return f1(lastPrecision(key), lastRecall(key));
/*     */   }
/*     */   
/*     */   public Counter lastF1() {
/* 315 */     Counter result = new Counter();
/* 316 */     Set keys = Sets.union(this.previousGuessed.keySet(), this.previousGold.keySet());
/* 317 */     for (Object key : keys) {
/* 318 */       result.setCount(key, lastF1(key));
/*     */     }
/* 320 */     return result;
/*     */   }
/*     */   
/*     */   public static double f1(double precision, double recall) {
/* 324 */     return (precision == 0.0D) || (recall == 0.0D) ? 0.0D : 2.0D * precision * recall / (precision + recall);
/*     */   }
/*     */   
/*     */   public static Counter f1(Counter precision, Counter recall) {
/* 328 */     Counter result = new Counter();
/* 329 */     for (Object key : Sets.intersection(precision.keySet(), recall.keySet())) {
/* 330 */       result.setCount(key, f1(precision.getCount(key), recall.getCount(key)));
/*     */     }
/* 332 */     return result;
/*     */   }
/*     */   
/*     */   private double percentage(Object key, Counter guessed, Counter guessedCorrect) {
/* 336 */     double thisGuessed = guessed.getCount(key);
/* 337 */     double thisGuessedCorrect = guessedCorrect.getCount(key);
/* 338 */     return thisGuessed == 0.0D ? 0.0D : thisGuessedCorrect / thisGuessed;
/*     */   }
/*     */   
/*     */   private void displayHelper(Set keys, PrintWriter pw, Counter guessed, Counter guessedCorrect, Counter gold, Counter goldCorrect) {
/* 342 */     Map pads = getPads(keys);
/* 343 */     for (Iterator i = keys.iterator(); i.hasNext();) {
/* 344 */       Object key = i.next();
/* 345 */       double thisGuessed = guessed.getCount(key);
/* 346 */       double thisGuessedCorrect = guessedCorrect.getCount(key);
/* 347 */       double precision = thisGuessed == 0.0D ? 0.0D : thisGuessedCorrect / thisGuessed;
/* 348 */       this.lastPrecision.setCount(key, precision);
/* 349 */       double thisGold = gold.getCount(key);
/* 350 */       double thisGoldCorrect = goldCorrect.getCount(key);
/* 351 */       double recall = thisGold == 0.0D ? 0.0D : thisGoldCorrect / thisGold;
/* 352 */       this.lastRecall.setCount(key, recall);
/* 353 */       double f1 = f1(precision, recall);
/* 354 */       this.lastF1.setCount(key, f1);
/* 355 */       String pad = (String)pads.get(key);
/* 356 */       pw.println(key + pad + "\t" + "P: " + formatNumber(precision) + "\ton " + formatCount(thisGuessed) + " objects\tR: " + formatNumber(recall) + "\ton " + formatCount(thisGold) + " objects\tF1: " + formatNumber(f1));
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
/* 375 */   private static NumberFormat numberFormat = NumberFormat.getNumberInstance();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String formatNumber(double d)
/*     */   {
/* 386 */     return numberFormat.format(d);
/*     */   }
/*     */   
/*     */   private static int formatCount(double d) {
/* 390 */     return (int)Math.round(d);
/*     */   }
/*     */   
/*     */   private static Map getPads(Set keys)
/*     */   {
/* 395 */     Map pads = new HashMap();
/* 396 */     int max = 0;
/* 397 */     for (Object key : keys) {
/* 398 */       String keyString = key == null ? "null" : key.toString();
/* 399 */       if (keyString.length() > max) {
/* 400 */         max = keyString.length();
/*     */       }
/*     */     }
/* 403 */     for (Object key : keys) {
/* 404 */       String keyString = key == null ? "null" : key.toString();
/* 405 */       int diff = max - keyString.length();
/* 406 */       String pad = "";
/* 407 */       for (int j = 0; j < diff; j++) {
/* 408 */         pad = pad + " ";
/*     */       }
/* 410 */       pads.put(key, pad);
/*     */     }
/* 412 */     return pads;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 416 */     Pattern p = Pattern.compile("^([^:]*):(.*)$");
/* 417 */     Collection guesses = Arrays.asList(new String[] { "S:a", "S:b", "VP:c", "VP:d", "S:a" });
/* 418 */     Collection golds = Arrays.asList(new String[] { "S:a", "S:b", "S:b", "VP:d", "VP:a" });
/* 419 */     EqualityChecker e = new EqualityChecker() {
/*     */       public boolean areEqual(Object o1, Object o2) {
/* 421 */         Matcher m1 = this.val$p.matcher((String)o1);
/* 422 */         m1.find();
/* 423 */         String s1 = m1.group(2);
/* 424 */         System.out.println(s1);
/* 425 */         Matcher m2 = this.val$p.matcher((String)o2);
/* 426 */         m2.find();
/* 427 */         String s2 = m2.group(2);
/* 428 */         System.out.println(s2);
/* 429 */         return s1.equals(s2);
/*     */       }
/* 431 */     };
/* 432 */     EquivalenceClasser eq = new EquivalenceClasser() {
/*     */       public Object equivalenceClass(Object o) {
/* 434 */         Matcher m = this.val$p.matcher((String)o);
/* 435 */         m.find();
/* 436 */         return m.group(1);
/*     */       }
/* 438 */     };
/* 439 */     EquivalenceClassEval eval = new EquivalenceClassEval(eq, e, "testing");
/* 440 */     eval.setBagEval(false);
/* 441 */     eval.eval(guesses, golds);
/* 442 */     eval.displayLast();
/* 443 */     eval.display();
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
/* 466 */   public static final EqualityChecker DEFAULT_CHECKER = new EqualityChecker()
/*     */   {
/* 468 */     public boolean areEqual(Object o1, Object o2) { return o1.equals(o2); }
/*     */   };
/*     */   
/*     */   public static abstract interface Factory { public abstract EquivalenceClassEval equivalenceClassEval();
/*     */   }
/*     */   
/* 474 */   static class Eval { private boolean bagEval = false;
/*     */     CollectionContainsChecker checker;
/*     */     
/* 477 */     public Eval(EquivalenceClassEval.EqualityChecker e) { this(false, e); }
/*     */     
/*     */     public Eval()
/*     */     {
/* 481 */       this(EquivalenceClassEval.DEFAULT_CHECKER);
/*     */     }
/*     */     
/*     */     public Eval(boolean bagEval) {
/* 485 */       this(false, EquivalenceClassEval.DEFAULT_CHECKER);
/*     */     }
/*     */     
/*     */     public Eval(boolean bagEval, EquivalenceClassEval.EqualityChecker e) {
/* 489 */       this.checker = new CollectionContainsChecker(e);
/* 490 */       this.bagEval = bagEval;
/*     */     }
/*     */     
/*     */ 
/*     */     static class CollectionContainsChecker
/*     */     {
/*     */       EquivalenceClassEval.EqualityChecker e;
/*     */       
/*     */       public CollectionContainsChecker(EquivalenceClassEval.EqualityChecker e)
/*     */       {
/* 500 */         this.e = e;
/*     */       }
/*     */       
/*     */       public boolean contained(Object obj, Collection coll) {
/* 504 */         for (Object o : coll) {
/* 505 */           if (this.e.areEqual(obj, o)) {
/* 506 */             return true;
/*     */           }
/*     */         }
/* 509 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 513 */     double guessed = 0.0D;
/* 514 */     double guessedCorrect = 0.0D;
/* 515 */     double gold = 0.0D;
/* 516 */     double goldCorrect = 0.0D;
/*     */     double lastPrecision;
/*     */     double lastRecall;
/*     */     double lastF1;
/*     */     
/*     */     public void eval(Collection guesses, Collection golds)
/*     */     {
/* 523 */       eval(guesses, golds, new PrintWriter(System.out, true));
/*     */     }
/*     */     
/*     */     public void eval(Collection guesses, Collection golds, PrintWriter pw)
/*     */     {
/* 528 */       double precision = evalPrecision(guesses, golds);
/* 529 */       this.lastPrecision = precision;
/* 530 */       double recall = evalRecall(guesses, golds);
/* 531 */       this.lastRecall = recall;
/* 532 */       double f1 = 2.0D * precision * recall / (precision + recall);
/* 533 */       this.lastF1 = f1;
/* 534 */       this.guessed += guesses.size();
/* 535 */       this.guessedCorrect += (guesses.size() == 0.0D ? 0.0D : precision * guesses.size());
/* 536 */       this.gold += golds.size();
/* 537 */       this.goldCorrect += (golds.size() == 0.0D ? 0.0D : recall * golds.size());
/* 538 */       pw.println("This example:\tP:\t" + precision + " R:\t" + recall + " F1:\t" + f1);
/* 539 */       double cumPrecision = this.guessedCorrect / this.guessed;
/* 540 */       double cumRecall = this.goldCorrect / this.gold;
/* 541 */       double cumF1 = 2.0D * cumPrecision * cumRecall / (cumPrecision + cumRecall);
/* 542 */       pw.println("Cumulative:\tP:\t" + cumPrecision + " R:\t" + cumRecall + " F1:\t" + cumF1);
/*     */     }
/*     */     
/*     */     public double evalPrecision(Collection guesses, Collection golds) {
/*     */       Collection internalGolds;
/*     */       Collection internalGuesses;
/*     */       Collection internalGolds;
/* 549 */       if (this.bagEval) {
/* 550 */         Collection internalGuesses = new ArrayList(guesses.size());
/* 551 */         internalGolds = new ArrayList(golds.size());
/*     */       } else {
/* 553 */         internalGuesses = new HashSet(guesses.size());
/* 554 */         internalGolds = new HashSet(golds.size());
/*     */       }
/* 556 */       internalGuesses.addAll(guesses);
/* 557 */       internalGolds.addAll(golds);
/* 558 */       double thisGuessed = 0.0D;
/* 559 */       double thisGuessedCorrect = 0.0D;
/* 560 */       for (Iterator i = internalGuesses.iterator(); i.hasNext();) {
/* 561 */         Object o = i.next();
/* 562 */         thisGuessed += 1.0D;
/* 563 */         if (this.checker.contained(o, internalGolds)) {
/* 564 */           thisGuessedCorrect += 1.0D;
/* 565 */           EquivalenceClassEval.removeItem(o, internalGolds, this.checker);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 570 */       return thisGuessedCorrect / thisGuessed;
/*     */     }
/*     */     
/*     */     public double evalRecall(Collection guesses, Collection golds)
/*     */     {
/* 575 */       double thisGold = 0.0D;
/* 576 */       double thisGoldCorrect = 0.0D;
/* 577 */       for (Object o : golds) {
/* 578 */         thisGold += 1.0D;
/* 579 */         if (guesses.contains(o)) {
/* 580 */           thisGoldCorrect += 1.0D;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 585 */       return thisGoldCorrect / thisGold;
/*     */     }
/*     */     
/*     */     public void display() {
/* 589 */       display(new PrintWriter(System.out, true));
/*     */     }
/*     */     
/*     */     public void display(PrintWriter pw) {
/* 593 */       double precision = this.guessedCorrect / this.guessed;
/* 594 */       double recall = this.goldCorrect / this.gold;
/* 595 */       double f1 = 2.0D * precision * recall / (precision + recall);
/* 596 */       pw.println("*********Final eval stats***********");
/* 597 */       pw.println("P:\t" + precision + " R:\t" + recall + " F1:\t" + f1);
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
/*     */   public Factory factory()
/*     */   {
/* 611 */     new Factory() {
/* 612 */       boolean bagEval1 = EquivalenceClassEval.this.bagEval;
/* 613 */       EquivalenceClasser eq1 = EquivalenceClassEval.this.eq;
/* 614 */       EquivalenceClassEval.Eval.CollectionContainsChecker checker1 = EquivalenceClassEval.this.checker;
/* 615 */       String summaryName1 = EquivalenceClassEval.this.summaryName;
/*     */       
/*     */       public EquivalenceClassEval equivalenceClassEval() {
/* 618 */         EquivalenceClassEval e = new EquivalenceClassEval(this.eq1, this.checker1, this.summaryName1);
/* 619 */         e.setBagEval(this.bagEval1);
/* 620 */         return e;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static abstract interface EqualityChecker<T>
/*     */   {
/*     */     public abstract boolean areEqual(T paramT1, T paramT2);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\stats\EquivalenceClassEval.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */