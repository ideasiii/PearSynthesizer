/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasTag;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ public class MLEDependencyGrammar
/*     */   extends AbstractDependencyGrammar
/*     */ {
/*     */   private static final boolean useSmoothTagProjection = false;
/*     */   private static final boolean useUnigramWordSmoothing = false;
/*     */   protected int numWordTokens;
/*     */   protected Counter<IntDependency> argCounter;
/*     */   protected Counter<IntDependency> stopCounter;
/*  40 */   public double smooth_aT_hTWd = 32.0D;
/*     */   
/*     */ 
/*     */ 
/*  44 */   public double smooth_aTW_hTWd = 16.0D;
/*  45 */   public double smooth_stop = 4.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   public double interp = 0.6D;
/*     */   
/*     */ 
/*     */ 
/*  54 */   public double smooth_aTW_aT = 96.0D;
/*  55 */   public double smooth_aTW_hTd = 32.0D;
/*  56 */   public double smooth_aT_hTd = 32.0D;
/*  57 */   public double smooth_aPTW_aPT = 16.0D;
/*     */   
/*     */ 
/*     */ 
/*     */   public MLEDependencyGrammar(TreebankLangParserParams tlpParams, boolean directional, boolean distance, boolean coarseDistance)
/*     */   {
/*  63 */     this(LexicalizedParser.basicCategoryTagsInDependencyGrammar ? new BasicCategoryTagProjection(tlpParams.treebankLanguagePack()) : new TestTagProjection(), tlpParams, directional, distance, coarseDistance);
/*     */   }
/*     */   
/*     */   public MLEDependencyGrammar(TagProjection tagProjection, TreebankLangParserParams tlpParams, boolean directional, boolean useDistance, boolean useCoarseDistance)
/*     */   {
/*  68 */     super(tlpParams.treebankLanguagePack(), tagProjection, directional, useDistance, useCoarseDistance);
/*  69 */     this.argCounter = new Counter();
/*  70 */     this.stopCounter = new Counter();
/*     */     
/*  72 */     double[] smoothParams = tlpParams.MLEDependencyGrammarSmoothingParams();
/*  73 */     this.smooth_aT_hTWd = smoothParams[0];
/*  74 */     this.smooth_aTW_hTWd = smoothParams[1];
/*  75 */     this.smooth_stop = smoothParams[2];
/*  76 */     this.interp = smoothParams[3];
/*     */     
/*     */ 
/*  79 */     this.smoothTP = new BasicCategoryTagProjection(tlpParams.treebankLanguagePack());
/*     */   }
/*     */   
/*     */   public String toString() {
/*  83 */     NumberFormat nf = NumberFormat.getNumberInstance();
/*  84 */     nf.setMaximumFractionDigits(2);
/*  85 */     StringBuilder sb = new StringBuilder(2000);
/*  86 */     String cl = getClass().getName();
/*  87 */     sb.append(cl.substring(cl.lastIndexOf(".") + 1)).append("[tagbins=");
/*  88 */     sb.append(this.numTagBins).append(",wordTokens=").append(this.numWordTokens).append("; head -> arg\n");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  98 */     sb.append("]");
/*  99 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public boolean pruneTW(IntTaggedWord argTW) {
/* 103 */     String[] punctTags = this.tlp.punctuationTags();
/* 104 */     for (String punctTag : punctTags) {
/* 105 */       if (argTW.tag == tagNumberer().number(punctTag)) {
/* 106 */         return true;
/*     */       }
/*     */     }
/* 109 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */   static transient EndHead tempEndHead = new EndHead();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static EndHead treeToDependencyHelper(Tree tree, List<IntDependency> depList, int loc)
/*     */   {
/* 129 */     if ((tree.isLeaf()) || (tree.isPreTerminal())) {
/* 130 */       tempEndHead.head = loc;
/* 131 */       tempEndHead.end = (loc + 1);
/* 132 */       return tempEndHead;
/*     */     }
/* 134 */     Tree[] kids = tree.children();
/* 135 */     if (kids.length == 1) {
/* 136 */       return treeToDependencyHelper(kids[0], depList, loc);
/*     */     }
/* 138 */     tempEndHead = treeToDependencyHelper(kids[0], depList, loc);
/* 139 */     int lHead = tempEndHead.head;
/* 140 */     int split = tempEndHead.end;
/* 141 */     tempEndHead = treeToDependencyHelper(kids[1], depList, tempEndHead.end);
/* 142 */     int end = tempEndHead.end;
/* 143 */     int rHead = tempEndHead.head;
/* 144 */     String hTag = ((HasTag)tree.label()).tag();
/* 145 */     String lTag = ((HasTag)kids[0].label()).tag();
/* 146 */     String rTag = ((HasTag)kids[1].label()).tag();
/* 147 */     String hWord = ((HasWord)tree.label()).word();
/* 148 */     String lWord = ((HasWord)kids[0].label()).word();
/* 149 */     String rWord = ((HasWord)kids[1].label()).word();
/* 150 */     boolean leftHeaded = hWord.equals(lWord);
/* 151 */     String aTag = leftHeaded ? rTag : lTag;
/* 152 */     String aWord = leftHeaded ? rWord : lWord;
/* 153 */     int hT = tagNumberer().number(hTag);
/* 154 */     int aT = tagNumberer().number(aTag);
/* 155 */     int hW = wordNumberer().hasSeen(hWord) ? wordNumberer().number(hWord) : wordNumberer().number("UNK");
/* 156 */     int aW = wordNumberer().hasSeen(aWord) ? wordNumberer().number(aWord) : wordNumberer().number("UNK");
/* 157 */     int head = leftHeaded ? lHead : rHead;
/* 158 */     int arg = leftHeaded ? rHead : lHead;
/* 159 */     IntDependency dependency = new IntDependency(hW, hT, aW, aT, leftHeaded, leftHeaded ? split - head - 1 : head - split);
/* 160 */     depList.add(dependency);
/* 161 */     IntDependency stopL = new IntDependency(aW, aT, -2, -2, false, leftHeaded ? arg - split : arg - loc);
/* 162 */     depList.add(stopL);
/* 163 */     IntDependency stopR = new IntDependency(aW, aT, -2, -2, true, leftHeaded ? end - arg - 1 : split - arg - 1);
/* 164 */     depList.add(stopR);
/*     */     
/* 166 */     tempEndHead.head = head;
/* 167 */     return tempEndHead;
/*     */   }
/*     */   
/*     */ 
/*     */   public void dumpSizes()
/*     */   {
/* 173 */     System.out.println("arg counter " + this.argCounter.size());
/* 174 */     System.out.println("stop counter " + this.stopCounter.size());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<IntDependency> treeToDependencyList(Tree tree)
/*     */   {
/* 186 */     List<IntDependency> depList = new ArrayList();
/* 187 */     treeToDependencyHelper(tree, depList, 0);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */     return depList;
/*     */   }
/*     */   
/*     */   public double scoreAll(Collection<IntDependency> deps) {
/* 197 */     double totalScore = 0.0D;
/* 198 */     for (IntDependency d : deps)
/*     */     {
/*     */ 
/*     */ 
/* 202 */       double score = score(d);
/* 203 */       if (score > Double.NEGATIVE_INFINITY) {
/* 204 */         totalScore += score;
/*     */       }
/*     */     }
/* 207 */     return totalScore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void tune(Collection<Tree> trees)
/*     */   {
/* 216 */     List<IntDependency> deps = new ArrayList();
/* 217 */     for (Tree tree : trees) {
/* 218 */       deps.addAll(treeToDependencyList(tree));
/*     */     }
/*     */     
/* 221 */     double bestScore = Double.NEGATIVE_INFINITY;
/* 222 */     double bestSmooth_stop = 0.0D;
/* 223 */     double bestSmooth_aTW_hTWd = 0.0D;
/* 224 */     double bestSmooth_aT_hTWd = 0.0D;
/* 225 */     double bestInterp = 0.0D;
/*     */     
/* 227 */     double bestSmooth_aTW_aT = 0.0D;
/* 228 */     double bestSmooth_aTW_hTd = 0.0D;
/* 229 */     double bestSmooth_aT_hTd = 0.0D;
/*     */     
/* 231 */     System.err.println("Tuning smooth_stop...");
/* 232 */     for (this.smooth_stop = 0.01D; this.smooth_stop < 100.0D; this.smooth_stop *= 1.25D) {
/* 233 */       double totalScore = 0.0D;
/* 234 */       for (IntDependency dep : deps) {
/* 235 */         if (!rootTW(dep.head)) {
/* 236 */           double stopProb = getStopProb(dep);
/* 237 */           if (!dep.arg.equals(stopTW)) {
/* 238 */             stopProb = 1.0D - stopProb;
/*     */           }
/* 240 */           if (stopProb > 0.0D) {
/* 241 */             totalScore += Math.log(stopProb);
/*     */           }
/*     */         }
/*     */       }
/* 245 */       if (totalScore > bestScore) {
/* 246 */         bestScore = totalScore;
/* 247 */         bestSmooth_stop = this.smooth_stop;
/*     */       }
/*     */     }
/* 250 */     this.smooth_stop = bestSmooth_stop;
/* 251 */     System.err.println("Tuning selected smooth_stop: " + this.smooth_stop);
/*     */     
/* 253 */     for (Iterator<IntDependency> iter = deps.iterator(); iter.hasNext();) {
/* 254 */       IntDependency dep = (IntDependency)iter.next();
/* 255 */       if (dep.arg.equals(stopTW)) {
/* 256 */         iter.remove();
/*     */       }
/*     */     }
/*     */     
/* 260 */     System.err.println("Tuning other parameters...");
/*     */     
/*     */ 
/* 263 */     bestScore = Double.NEGATIVE_INFINITY;
/* 264 */     for (this.smooth_aTW_hTWd = 0.5D; this.smooth_aTW_hTWd < 100.0D; this.smooth_aTW_hTWd *= 1.25D) {
/* 265 */       System.err.print(".");
/* 266 */       for (this.smooth_aT_hTWd = 0.5D; this.smooth_aT_hTWd < 100.0D; this.smooth_aT_hTWd *= 1.25D) {
/* 267 */         for (this.interp = 0.02D; this.interp < 1.0D; this.interp += 0.02D) {
/* 268 */           double totalScore = 0.0D;
/* 269 */           for (IntDependency dep : deps) {
/* 270 */             double score = score(dep);
/* 271 */             if (score > Double.NEGATIVE_INFINITY) {
/* 272 */               totalScore += score;
/*     */             }
/*     */           }
/* 275 */           if (totalScore > bestScore) {
/* 276 */             bestScore = totalScore;
/* 277 */             bestInterp = this.interp;
/* 278 */             bestSmooth_aTW_hTWd = this.smooth_aTW_hTWd;
/* 279 */             bestSmooth_aT_hTWd = this.smooth_aT_hTWd;
/* 280 */             System.err.println("Current best interp: " + this.interp + " with score " + totalScore);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 285 */     this.smooth_aTW_hTWd = bestSmooth_aTW_hTWd;
/* 286 */     this.smooth_aT_hTWd = bestSmooth_aT_hTWd;
/* 287 */     this.interp = bestInterp;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 331 */     System.err.println("\nTuning selected smooth_aTW_hTWd: " + this.smooth_aTW_hTWd + " smooth_aT_hTWd: " + this.smooth_aT_hTWd + " interp: " + this.interp + " smooth_aTW_aT: " + this.smooth_aTW_aT + " smooth_aTW_hTd: " + this.smooth_aTW_hTd + " smooth_aT_hTd: " + this.smooth_aT_hTd);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addRule(IntDependency dependency, double count)
/*     */   {
/* 340 */     if (!this.directional) {
/* 341 */       dependency.leftHeaded = false;
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
/* 352 */     expandDependency(dependency, count);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 358 */   protected transient List<IntTaggedWord> tagITWList = null;
/*     */   
/*     */   private TagProjection smoothTP;
/*     */   
/*     */   private Numberer smoothTPNumberer;
/*     */   
/*     */   private static final String TP_PREFIX = ".*TP*.";
/*     */   private static final boolean verbose = false;
/*     */   protected static final double MIN_PROBABILITY = 1.0E-40D;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   private IntTaggedWord getCachedITW(short tag)
/*     */   {
/* 371 */     if (this.tagITWList == null) {
/* 372 */       this.tagITWList = new ArrayList(this.numTagBins + 2);
/* 373 */       for (int i = 0; i < this.numTagBins + 2; i++) {
/* 374 */         this.tagITWList.add(i, null);
/*     */       }
/*     */     }
/* 377 */     IntTaggedWord headT = (IntTaggedWord)this.tagITWList.get(tagBin(tag) + 2);
/* 378 */     if (headT == null) {
/* 379 */       headT = new IntTaggedWord(-1, tagBin(tag));
/* 380 */       this.tagITWList.set(tagBin(tag) + 2, headT);
/*     */     }
/* 382 */     return headT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void expandDependency(IntDependency dependency, double count)
/*     */   {
/* 393 */     if ((dependency.head == null) || (dependency.arg == null)) {
/* 394 */       return;
/*     */     }
/*     */     
/* 397 */     if (dependency.arg.word != -2) {
/* 398 */       expandArg(dependency, valenceBin(dependency.distance), count);
/*     */     }
/* 400 */     expandStop(dependency, distanceBin(dependency.distance), count, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private short tagProject(short tag)
/*     */   {
/* 408 */     if (this.smoothTPNumberer == null) {
/* 409 */       this.smoothTPNumberer = new Numberer(tagNumberer());
/*     */     }
/* 411 */     if (tag < 0) {
/* 412 */       return tag;
/*     */     }
/* 414 */     String tagStr = (String)this.smoothTPNumberer.object(tag);
/* 415 */     String binStr = ".*TP*." + this.smoothTP.project(tagStr);
/* 416 */     return (short)this.smoothTPNumberer.number(binStr);
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
/*     */   private void expandArg(IntDependency dependency, short valBinDist, double count)
/*     */   {
/* 429 */     IntTaggedWord headT = getCachedITW(dependency.head.tag);
/* 430 */     IntTaggedWord argT = getCachedITW(dependency.arg.tag);
/* 431 */     IntTaggedWord head = new IntTaggedWord(dependency.head.word, tagBin(dependency.head.tag));
/* 432 */     IntTaggedWord arg = new IntTaggedWord(dependency.arg.word, tagBin(dependency.arg.tag));
/* 433 */     boolean leftHeaded = dependency.leftHeaded;
/*     */     
/*     */ 
/* 436 */     this.argCounter.incrementCount(intern(head, arg, leftHeaded, valBinDist), count);
/* 437 */     this.argCounter.incrementCount(intern(headT, arg, leftHeaded, valBinDist), count);
/* 438 */     this.argCounter.incrementCount(intern(head, argT, leftHeaded, valBinDist), count);
/* 439 */     this.argCounter.incrementCount(intern(headT, argT, leftHeaded, valBinDist), count);
/*     */     
/* 441 */     this.argCounter.incrementCount(intern(head, wildTW, leftHeaded, valBinDist), count);
/* 442 */     this.argCounter.incrementCount(intern(headT, wildTW, leftHeaded, valBinDist), count);
/*     */     
/*     */ 
/* 445 */     this.argCounter.incrementCount(intern(wildTW, arg, false, (short)-1), count);
/* 446 */     this.argCounter.incrementCount(intern(wildTW, argT, false, (short)-1), count);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 468 */     this.numWordTokens += 1;
/*     */   }
/*     */   
/*     */   private void expandStop(IntDependency dependency, short distBinDist, double count, boolean wildForStop) {
/* 472 */     IntTaggedWord headT = getCachedITW(dependency.head.tag);
/* 473 */     IntTaggedWord head = new IntTaggedWord(dependency.head.word, tagBin(dependency.head.tag));
/* 474 */     IntTaggedWord arg = new IntTaggedWord(dependency.arg.word, tagBin(dependency.arg.tag));
/*     */     
/* 476 */     boolean leftHeaded = dependency.leftHeaded;
/*     */     
/* 478 */     if (arg.word == -2) {
/* 479 */       this.stopCounter.incrementCount(intern(head, arg, leftHeaded, distBinDist), count);
/* 480 */       this.stopCounter.incrementCount(intern(headT, arg, leftHeaded, distBinDist), count);
/*     */     }
/* 482 */     if ((wildForStop) || (arg.word != -2)) {
/* 483 */       this.stopCounter.incrementCount(intern(head, wildTW, leftHeaded, distBinDist), count);
/* 484 */       this.stopCounter.incrementCount(intern(headT, wildTW, leftHeaded, distBinDist), count);
/*     */     }
/*     */   }
/*     */   
/*     */   public double countHistory(IntDependency dependency) {
/* 489 */     short hTBackup = dependency.head.tag;
/* 490 */     IntTaggedWord aTWBackup = dependency.arg;
/* 491 */     short dist = dependency.distance;
/* 492 */     dependency.head.tag = ((short)tagBin(dependency.head.tag));
/* 493 */     dependency.distance = valenceBin(dist);
/*     */     
/* 495 */     dependency.arg = wildTW;
/*     */     
/* 497 */     double s = this.argCounter.getCount(dependency);
/*     */     
/* 499 */     dependency.head.tag = hTBackup;
/* 500 */     dependency.arg = aTWBackup;
/* 501 */     dependency.distance = dist;
/*     */     
/* 503 */     return s;
/*     */   }
/*     */   
/*     */   public double scoreTB(IntDependency dependency)
/*     */   {
/* 508 */     return Test.depWeight * Math.log(probTB(dependency));
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
/*     */   protected double probTB(IntDependency dependency)
/*     */   {
/* 523 */     if (!this.directional) {
/* 524 */       dependency.leftHeaded = false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 531 */     boolean leftHeaded = dependency.leftHeaded;
/* 532 */     short distance = dependency.distance;
/* 533 */     int hW = dependency.head.word;
/* 534 */     int aW = dependency.arg.word;
/* 535 */     short hT = dependency.head.tag;
/* 536 */     short aT = dependency.arg.tag;
/*     */     
/* 538 */     IntTaggedWord aTW = dependency.arg;
/* 539 */     IntTaggedWord hTW = dependency.head;
/*     */     
/* 541 */     boolean isRoot = rootTW(dependency.head);
/*     */     double pb_stop_hTWds;
/* 543 */     double pb_stop_hTWds; if (isRoot) {
/* 544 */       pb_stop_hTWds = 0.0D;
/*     */     } else {
/* 546 */       pb_stop_hTWds = getStopProb(dependency);
/*     */     }
/*     */     
/* 549 */     if (dependency.arg.word == -2)
/*     */     {
/* 551 */       return pb_stop_hTWds;
/*     */     }
/*     */     
/* 554 */     double pb_go_hTWds = 1.0D - pb_stop_hTWds;
/*     */     
/*     */ 
/*     */ 
/* 558 */     dependency.distance = valenceBin(distance);
/* 559 */     short binDistance = dependency.distance;
/* 560 */     IntDependency copy = new IntDependency(dependency.head, dependency.arg, dependency.leftHeaded, dependency.distance);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 574 */     double c_aTW_hTWd = this.argCounter.getCount(dependency);
/* 575 */     dependency.arg.word = -1;
/* 576 */     double c_aT_hTWd = this.argCounter.getCount(dependency);
/* 577 */     dependency.arg.word = aW;
/* 578 */     dependency.arg = wildTW;
/* 579 */     double c_hTWd = this.argCounter.getCount(dependency);
/* 580 */     dependency.arg = aTW;
/*     */     
/* 582 */     if (!dependency.equals(copy)) {
/* 583 */       throw new RuntimeException("Dependencies not equal: " + dependency + " and " + copy);
/*     */     }
/*     */     
/* 586 */     dependency.head.word = -1;
/* 587 */     double c_aTW_hTd = this.argCounter.getCount(dependency);
/* 588 */     dependency.arg.word = -1;
/* 589 */     double c_aT_hTd = this.argCounter.getCount(dependency);
/* 590 */     dependency.arg.word = aW;
/* 591 */     dependency.arg = wildTW;
/* 592 */     double c_hTd = this.argCounter.getCount(dependency);
/* 593 */     dependency.arg = aTW;
/* 594 */     dependency.head.word = hW;
/*     */     
/* 596 */     if (!dependency.equals(copy)) {
/* 597 */       throw new RuntimeException("Dependencies not equal: " + dependency + " and " + copy);
/*     */     }
/*     */     
/*     */ 
/* 601 */     short aPT = Short.MIN_VALUE;
/* 602 */     short hPT = Short.MIN_VALUE;
/* 603 */     double c_aPTW_hPTd = NaN.0D;
/* 604 */     double c_aPT_hPTd = NaN.0D;
/* 605 */     double c_hPTd = NaN.0D;
/* 606 */     double c_aPTW_aPT = NaN.0D;
/* 607 */     double c_aPT = NaN.0D;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 633 */     dependency.head = wildTW;
/* 634 */     dependency.leftHeaded = false;
/* 635 */     dependency.distance = -1;
/* 636 */     double c_aTW = this.argCounter.getCount(dependency);
/* 637 */     dependency.arg.word = -1;
/* 638 */     double c_aT = this.argCounter.getCount(dependency);
/* 639 */     dependency.arg.word = aW;
/* 640 */     dependency.arg.tag = -1;
/* 641 */     double c_aW = this.argCounter.getCount(dependency);
/* 642 */     dependency.arg.tag = aT;
/* 643 */     dependency.head = hTW;
/* 644 */     dependency.leftHeaded = leftHeaded;
/* 645 */     dependency.distance = binDistance;
/*     */     
/* 647 */     if (!dependency.equals(copy)) {
/* 648 */       throw new RuntimeException("Dependencies not equal: " + dependency + " and " + copy);
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
/* 672 */     dependency.distance = distance;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 701 */     double p_aTW_aT = c_aTW > 0.0D ? c_aTW / c_aT : 1.0D;
/* 702 */     double p_aTW_hTd = c_hTd > 0.0D ? c_aTW_hTd / c_hTd : 0.0D;
/* 703 */     double p_aT_hTd = c_hTd > 0.0D ? c_aT_hTd / c_hTd : 0.0D;
/*     */     
/*     */ 
/* 706 */     double pb_aTW_hTWd = (c_aTW_hTWd + this.smooth_aTW_hTWd * p_aTW_hTd) / (c_hTWd + this.smooth_aTW_hTWd);
/* 707 */     double pb_aT_hTWd = (c_aT_hTWd + this.smooth_aT_hTWd * p_aT_hTd) / (c_hTWd + this.smooth_aT_hTWd);
/*     */     
/* 709 */     double score = (this.interp * pb_aTW_hTWd + (1.0D - this.interp) * p_aTW_aT * pb_aT_hTWd) * pb_go_hTWds;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 739 */     if ((Test.prunePunc) && (pruneTW(aTW))) {
/* 740 */       return 1.0D;
/*     */     }
/*     */     
/* 743 */     if (Double.isNaN(score)) {
/* 744 */       score = 0.0D;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 750 */     if (score < 1.0E-40D) {
/* 751 */       score = 0.0D;
/*     */     }
/*     */     
/* 754 */     return score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected double getStopProb(IntDependency dependency)
/*     */   {
/* 764 */     int hW = dependency.head.word;
/* 765 */     IntTaggedWord aTW = dependency.arg;
/* 766 */     short distance = dependency.distance;
/*     */     
/* 768 */     dependency.distance = distanceBin(distance);
/* 769 */     dependency.arg = stopTW;
/* 770 */     double c_stop_hTWds = this.stopCounter.getCount(dependency);
/* 771 */     dependency.head.word = -1;
/* 772 */     double c_stop_hTds = this.stopCounter.getCount(dependency);
/* 773 */     dependency.head.word = hW;
/* 774 */     dependency.arg = wildTW;
/* 775 */     double c_hTWds = this.stopCounter.getCount(dependency);
/* 776 */     dependency.head.word = -1;
/* 777 */     double c_hTds = this.stopCounter.getCount(dependency);
/*     */     
/* 779 */     dependency.head.word = hW;
/* 780 */     dependency.arg = aTW;
/* 781 */     dependency.distance = distance;
/*     */     
/* 783 */     double p_stop_hTds = c_hTds > 0.0D ? c_stop_hTds / c_hTds : 1.0D;
/*     */     
/* 785 */     double pb_stop_hTWds = (c_stop_hTWds + this.smooth_stop * p_stop_hTds) / (c_hTWds + this.smooth_stop);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 791 */     return pb_stop_hTWds;
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 795 */     stream.defaultReadObject();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 800 */     Counter<IntDependency> compressedArgC = this.argCounter;
/* 801 */     this.argCounter = new Counter();
/* 802 */     Counter<IntDependency> compressedStopC = this.stopCounter;
/* 803 */     this.stopCounter = new Counter();
/* 804 */     for (IntDependency d : compressedArgC.keySet()) {
/* 805 */       double count = compressedArgC.getCount(d);
/* 806 */       expandArg(d, d.distance, count);
/*     */     }
/*     */     
/* 809 */     for (IntDependency d : compressedStopC.keySet()) {
/* 810 */       double count = compressedStopC.getCount(d);
/* 811 */       expandStop(d, d.distance, count, false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 818 */     this.expandDependencyMap = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void writeObject(ObjectOutputStream stream)
/*     */     throws IOException
/*     */   {
/* 826 */     Counter<IntDependency> fullArgCounter = this.argCounter;
/* 827 */     this.argCounter = new Counter();
/* 828 */     for (IntDependency dependency : fullArgCounter.keySet()) {
/* 829 */       if ((dependency.head != wildTW) && (dependency.arg != wildTW) && (dependency.head.word != -1) && (dependency.arg.word != -1))
/*     */       {
/* 831 */         this.argCounter.incrementCount(dependency, fullArgCounter.getCount(dependency));
/*     */       }
/*     */     }
/*     */     
/* 835 */     Counter<IntDependency> fullStopCounter = this.stopCounter;
/* 836 */     this.stopCounter = new Counter();
/* 837 */     for (IntDependency dependency : fullStopCounter.keySet()) {
/* 838 */       if (dependency.head.word != -1) {
/* 839 */         this.stopCounter.incrementCount(dependency, fullStopCounter.getCount(dependency));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 847 */     stream.defaultWriteObject();
/*     */     
/* 849 */     this.argCounter = fullArgCounter;
/* 850 */     this.stopCounter = fullStopCounter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readData(BufferedReader in)
/*     */     throws IOException
/*     */   {
/* 858 */     String LEFT = "left";
/* 859 */     int lineNum = 1;
/*     */     
/* 861 */     boolean doingStop = false;
/* 862 */     IntDependency tempDependency = new IntDependency(-2, -2, -2, -2, false, 0);
/*     */     
/* 864 */     for (String line = in.readLine(); (line != null) && (line.length() > 0); line = in.readLine()) {
/*     */       try {
/* 866 */         if (line.equals("BEGIN_STOP")) {
/* 867 */           doingStop = true;
/* 868 */           continue;
/*     */         }
/* 870 */         String[] fields = StringUtils.splitOnCharWithQuoting(line, ' ', '"', '\\');
/*     */         
/*     */ 
/* 873 */         tempDependency.leftHeaded = fields[3].equals("left");
/* 874 */         short distance = (short)Integer.parseInt(fields[4]);
/* 875 */         tempDependency.head = new IntTaggedWord(fields[0], '/');
/* 876 */         tempDependency.arg = new IntTaggedWord(fields[2], '/');
/*     */         
/* 878 */         double count = Double.parseDouble(fields[5]);
/* 879 */         if (doingStop) {
/* 880 */           expandStop(tempDependency, distance, count, false);
/*     */         } else {
/* 882 */           expandArg(tempDependency, distance, count);
/*     */         }
/*     */       } catch (Exception e) {
/* 885 */         e.printStackTrace();
/* 886 */         throw new IOException("Error on line " + lineNum + ": " + line);
/*     */       }
/*     */       
/* 889 */       lineNum++;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeData(PrintWriter out)
/*     */     throws IOException
/*     */   {
/* 899 */     for (IntDependency dependency : this.argCounter.keySet()) {
/* 900 */       if ((dependency.head != wildTW) && (dependency.arg != wildTW) && (dependency.head.word != -1) && (dependency.arg.word != -1))
/*     */       {
/* 902 */         double count = this.argCounter.getCount(dependency);
/* 903 */         out.println(dependency + " " + count);
/*     */       }
/*     */     }
/*     */     
/* 907 */     out.println("BEGIN_STOP");
/*     */     
/* 909 */     for (IntDependency dependency : this.stopCounter.keySet()) {
/* 910 */       if (dependency.head.word != -1) {
/* 911 */         double count = this.stopCounter.getCount(dependency);
/* 912 */         out.println(dependency + " " + count);
/*     */       }
/*     */     }
/*     */     
/* 916 */     out.flush();
/*     */   }
/*     */   
/*     */   static class EndHead
/*     */   {
/*     */     public int end;
/*     */     public int head;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\MLEDependencyGrammar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */