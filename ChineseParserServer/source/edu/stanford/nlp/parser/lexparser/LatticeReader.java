/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ public class LatticeReader
/*     */ {
/*  16 */   public static boolean DEBUG = false;
/*  17 */   public static boolean PRETTYPRINT = false;
/*     */   public static final boolean USESUM = true;
/*     */   public static final boolean USEMAX = false;
/*  20 */   private static boolean mergeType = true;
/*     */   
/*     */   public static final String SILENCE = "<SIL>";
/*     */   private int numStates;
/*     */   private List<LatticeWord> latticeWords;
/*     */   private int[] nodeTimes;
/*     */   private ArrayList<LatticeWord>[] wordsAtTime;
/*     */   private ArrayList<LatticeWord>[] wordsStartAt;
/*     */   private ArrayList<LatticeWord>[] wordsEndAt;
/*     */   
/*     */   private void readInput(BufferedReader in)
/*     */     throws Exception
/*     */   {
/*  33 */     String line = in.readLine();
/*  34 */     while (line.trim().startsWith("#")) {
/*  35 */       line = in.readLine();
/*     */     }
/*     */     
/*     */ 
/*  39 */     this.latticeWords = new ArrayList();
/*     */     
/*  41 */     Pattern wordLinePattern = Pattern.compile("(\\d+)\\s+(\\d+)\\s+lm=(-?\\d+\\.\\d+),am=(-?\\d+\\.\\d+)\\s+([^( ]+)(?:\\((\\d+)\\))?.*");
/*  42 */     Matcher wordLineMatcher = wordLinePattern.matcher(line);
/*     */     
/*  44 */     while (wordLineMatcher.matches()) {
/*  45 */       int startNode = Integer.parseInt(wordLineMatcher.group(1)) - 1;
/*  46 */       int endNode = Integer.parseInt(wordLineMatcher.group(2)) - 1;
/*  47 */       double lm = Double.parseDouble(wordLineMatcher.group(3));
/*  48 */       double am = Double.parseDouble(wordLineMatcher.group(4));
/*  49 */       String word = wordLineMatcher.group(5).toLowerCase();
/*  50 */       String pronun = wordLineMatcher.group(6);
/*     */       
/*  52 */       if (word.equalsIgnoreCase("<s>")) {
/*  53 */         line = in.readLine();
/*  54 */         wordLineMatcher = wordLinePattern.matcher(line);
/*     */       }
/*     */       else {
/*  57 */         if (word.equalsIgnoreCase("</s>")) {
/*  58 */           word = ".$.";
/*     */         }
/*     */         int pronunciation;
/*     */         int pronunciation;
/*  62 */         if (pronun == null) {
/*  63 */           pronunciation = 0;
/*     */         } else {
/*  65 */           pronunciation = Integer.parseInt(pronun);
/*     */         }
/*     */         
/*  68 */         LatticeWord lw = new LatticeWord(word, startNode, endNode, lm, am, pronunciation);
/*  69 */         if (DEBUG) {
/*  70 */           System.err.println(lw);
/*     */         }
/*  72 */         this.latticeWords.add(lw);
/*     */         
/*  74 */         line = in.readLine();
/*  75 */         wordLineMatcher = wordLinePattern.matcher(line);
/*     */       }
/*     */     }
/*     */     
/*  79 */     this.numStates = Integer.parseInt(line.trim());
/*  80 */     if (DEBUG) {
/*  81 */       System.err.println(this.numStates);
/*     */     }
/*     */     
/*     */ 
/*  85 */     this.nodeTimes = new int[this.numStates];
/*     */     
/*  87 */     Pattern nodeTimePattern = Pattern.compile("(\\d+)\\s+t=(\\d+)\\s*");
/*     */     
/*     */ 
/*  90 */     for (int i = 0; i < this.numStates; i++) {
/*  91 */       Matcher nodeTimeMatcher = nodeTimePattern.matcher(in.readLine());
/*     */       
/*  93 */       if (!nodeTimeMatcher.matches()) {
/*  94 */         System.err.println("Input File Error");
/*  95 */         System.exit(1);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 100 */       this.nodeTimes[i] = Integer.parseInt(nodeTimeMatcher.group(2));
/*     */       
/* 102 */       if (DEBUG) {
/* 103 */         System.err.println(i + "\tt=" + this.nodeTimes[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void mergeSimultaneousNodes()
/*     */   {
/* 110 */     int[] indexMap = new int[this.nodeTimes.length];
/*     */     
/* 112 */     indexMap[0] = 0;
/* 113 */     int prevNode = 0;
/* 114 */     int prevTime = this.nodeTimes[0];
/* 115 */     if (DEBUG) {
/* 116 */       System.err.println("0 (" + this.nodeTimes[0] + ")" + "-->" + 0 + " (" + this.nodeTimes[0] + ") ++");
/*     */     }
/* 118 */     for (int i = 1; i < this.nodeTimes.length; i++) {
/* 119 */       if (prevTime == this.nodeTimes[i]) {
/* 120 */         indexMap[i] = prevNode;
/* 121 */         if (DEBUG) {
/* 122 */           System.err.println(i + " (" + this.nodeTimes[i] + ")" + "-->" + prevNode + " (" + this.nodeTimes[prevNode] + ") **");
/*     */         }
/*     */       } else {
/* 125 */         indexMap[i] = (prevNode = i);
/* 126 */         prevTime = this.nodeTimes[i];
/* 127 */         if (DEBUG) {
/* 128 */           System.err.println(i + " (" + this.nodeTimes[i] + ")" + "-->" + prevNode + " (" + this.nodeTimes[prevNode] + ") ++");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 133 */     for (LatticeWord lw : this.latticeWords) {
/* 134 */       lw.startNode = indexMap[lw.startNode];
/* 135 */       lw.endNode = indexMap[lw.endNode];
/* 136 */       if (DEBUG) {
/* 137 */         System.err.println(lw);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeEmptyNodes() {
/* 143 */     int[] indexMap = new int[this.numStates];
/* 144 */     int j = 0;
/* 145 */     for (int i = 0; i < this.numStates; i++) {
/* 146 */       indexMap[i] = j;
/* 147 */       if ((this.wordsStartAt[i].size() != 0) || (this.wordsEndAt[i].size() != 0)) {
/* 148 */         j++;
/*     */       }
/*     */     }
/*     */     
/* 152 */     for (LatticeWord lw : this.latticeWords) {
/* 153 */       this.wordsStartAt[lw.startNode].remove(lw);
/* 154 */       this.wordsEndAt[lw.endNode].remove(lw);
/* 155 */       for (int i = lw.startNode; i < lw.endNode; i++) {
/* 156 */         this.wordsAtTime[i].remove(lw);
/*     */       }
/*     */       
/* 159 */       lw.startNode = indexMap[lw.startNode];
/* 160 */       lw.endNode = indexMap[lw.endNode];
/* 161 */       this.wordsStartAt[lw.startNode].add(lw);
/* 162 */       this.wordsEndAt[lw.endNode].add(lw);
/* 163 */       for (int i = lw.startNode; i < lw.endNode; i++) {
/* 164 */         this.wordsAtTime[i].add(lw);
/*     */       }
/*     */     }
/*     */     
/* 168 */     this.numStates = j;
/* 169 */     ArrayList[] tmp = this.wordsAtTime;
/* 170 */     this.wordsAtTime = new ArrayList[this.numStates];
/* 171 */     System.arraycopy(tmp, 0, this.wordsAtTime, 0, this.numStates);
/*     */     
/* 173 */     tmp = this.wordsStartAt;
/* 174 */     this.wordsStartAt = new ArrayList[this.numStates];
/* 175 */     System.arraycopy(tmp, 0, this.wordsStartAt, 0, this.numStates);
/*     */     
/* 177 */     tmp = this.wordsEndAt;
/* 178 */     this.wordsEndAt = new ArrayList[this.numStates];
/* 179 */     System.arraycopy(tmp, 0, this.wordsEndAt, 0, this.numStates);
/*     */   }
/*     */   
/*     */   private void buildWordTimeArrays()
/*     */   {
/* 184 */     buildWordsAtTime();
/* 185 */     buildWordsStartAt();
/* 186 */     buildWordsEndAt();
/*     */   }
/*     */   
/*     */   private void buildWordsAtTime() {
/* 190 */     this.wordsAtTime = new ArrayList[this.numStates];
/* 191 */     for (int i = 0; i < this.wordsAtTime.length; i++) {
/* 192 */       this.wordsAtTime[i] = new ArrayList();
/*     */     }
/*     */     
/* 195 */     for (LatticeWord lw : this.latticeWords) {
/* 196 */       for (int j = lw.startNode; j <= lw.endNode; j++) {
/* 197 */         this.wordsAtTime[j].add(lw);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void buildWordsStartAt() {
/* 203 */     this.wordsStartAt = new ArrayList[this.numStates];
/* 204 */     for (int i = 0; i < this.wordsStartAt.length; i++) {
/* 205 */       this.wordsStartAt[i] = new ArrayList();
/*     */     }
/*     */     
/* 208 */     for (LatticeWord lw : this.latticeWords) {
/* 209 */       this.wordsStartAt[lw.startNode].add(lw);
/*     */     }
/*     */   }
/*     */   
/*     */   private void buildWordsEndAt() {
/* 214 */     this.wordsEndAt = new ArrayList[this.numStates];
/* 215 */     for (int i = 0; i < this.wordsEndAt.length; i++) {
/* 216 */       this.wordsEndAt[i] = new ArrayList();
/*     */     }
/*     */     
/* 219 */     for (LatticeWord lw : this.latticeWords) {
/* 220 */       this.wordsEndAt[lw.endNode].add(lw);
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeRedundency()
/*     */   {
/* 226 */     boolean changed = true;
/*     */     
/* 228 */     while (changed) {
/* 229 */       changed = false;
/* 230 */       for (int i = 0; i < this.wordsAtTime.length; i++) {
/* 231 */         if (this.wordsAtTime[i].size() >= 2)
/*     */         {
/*     */ 
/* 234 */           for (int j = 0; j < this.wordsAtTime[i].size() - 1; j++) {
/* 235 */             LatticeWord w1 = (LatticeWord)this.wordsAtTime[i].get(j);
/* 236 */             for (int k = j + 1; k < this.wordsAtTime[i].size(); k++) {
/* 237 */               LatticeWord w2 = (LatticeWord)this.wordsAtTime[i].get(k);
/* 238 */               if ((w1.word.equalsIgnoreCase(w2.word)) && 
/* 239 */                 (removeRedundentPair(w1, w2)))
/*     */               {
/*     */ 
/* 242 */                 changed = true;
/*     */                 
/*     */ 
/* 245 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean removeRedundentPair(LatticeWord w1, LatticeWord w2)
/*     */   {
/* 257 */     if (DEBUG) {
/* 258 */       System.err.println("trying to remove:");
/* 259 */       System.err.println(w1);
/* 260 */       System.err.println(w2);
/*     */     }
/*     */     
/* 263 */     int w1Start = w1.startNode;
/* 264 */     int w2Start = w2.startNode;
/* 265 */     int w1End = w1.endNode;
/* 266 */     int w2End = w2.endNode;
/*     */     int oldStart;
/*     */     int newStart;
/*     */     int oldStart;
/* 270 */     if (w1Start < w2Start) {
/* 271 */       int newStart = w2Start;
/* 272 */       oldStart = w1Start;
/*     */     } else {
/* 274 */       newStart = w1Start;
/* 275 */       oldStart = w2Start; }
/*     */     int oldEnd;
/*     */     int newEnd;
/*     */     int oldEnd;
/* 279 */     if (w1End < w2End) {
/* 280 */       int newEnd = w1End;
/* 281 */       oldEnd = w2End;
/*     */     } else {
/* 283 */       newEnd = w2End;
/* 284 */       oldEnd = w1End;
/*     */     }
/*     */     
/*     */ 
/* 288 */     for (LatticeWord lw : this.wordsStartAt[oldStart]) {
/* 289 */       if ((lw.endNode < newStart) || ((lw.endNode == newStart) && (lw.endNode != lw.startNode))) {
/* 290 */         if (DEBUG) {
/* 291 */           System.err.println("failed");
/*     */         }
/* 293 */         return false;
/*     */       }
/*     */     }
/* 296 */     for (LatticeWord lw : this.wordsEndAt[oldEnd]) {
/* 297 */       if ((lw.startNode > newEnd) || ((lw.startNode == newEnd) && (lw.endNode != lw.startNode))) {
/* 298 */         if (DEBUG) {
/* 299 */           System.err.println("failed");
/*     */         }
/* 301 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 306 */     changeStartTimes(this.wordsStartAt[oldEnd], newEnd);
/* 307 */     changeEndTimes(this.wordsEndAt[oldStart], newStart);
/*     */     
/*     */ 
/* 310 */     changeStartTimes(this.wordsStartAt[oldStart], newStart);
/* 311 */     changeEndTimes(this.wordsEndAt[oldEnd], newEnd);
/*     */     
/* 313 */     if (DEBUG) {
/* 314 */       System.err.println("succeeded");
/*     */     }
/* 316 */     return true;
/*     */   }
/*     */   
/*     */   private void changeStartTimes(List<LatticeWord> words, int newStartTime)
/*     */   {
/* 321 */     ArrayList<LatticeWord> toRemove = new ArrayList();
/* 322 */     for (LatticeWord lw : words) {
/* 323 */       this.latticeWords.remove(lw);
/* 324 */       int oldStartTime = lw.startNode;
/* 325 */       lw.startNode = newStartTime;
/*     */       
/* 327 */       if (this.latticeWords.contains(lw)) {
/* 328 */         if (DEBUG) {
/* 329 */           System.err.println("duplicate found");
/*     */         }
/* 331 */         LatticeWord twin = (LatticeWord)this.latticeWords.get(this.latticeWords.indexOf(lw));
/*     */         
/* 333 */         lw.startNode = oldStartTime;
/* 334 */         twin.merge(lw);
/*     */         
/* 336 */         toRemove.add(lw);
/* 337 */         this.wordsEndAt[lw.endNode].remove(lw);
/* 338 */         for (int i = lw.startNode; i <= lw.endNode; i++) {
/* 339 */           this.wordsAtTime[i].remove(lw);
/*     */         }
/*     */       } else {
/* 342 */         if (oldStartTime < newStartTime) {
/* 343 */           for (int i = oldStartTime; i < newStartTime; i++) {
/* 344 */             this.wordsAtTime[i].remove(lw);
/*     */           }
/*     */         } else {
/* 347 */           for (int i = newStartTime; i < oldStartTime; i++) {
/* 348 */             this.wordsAtTime[i].add(lw);
/*     */           }
/*     */         }
/* 351 */         this.latticeWords.add(lw);
/* 352 */         if (oldStartTime != newStartTime)
/*     */         {
/* 354 */           toRemove.add(lw);
/* 355 */           this.wordsStartAt[newStartTime].add(lw);
/*     */         }
/*     */       }
/*     */     }
/* 359 */     words.removeAll(toRemove);
/*     */   }
/*     */   
/*     */   private void changeEndTimes(List<LatticeWord> words, int newEndTime) {
/* 363 */     ArrayList<LatticeWord> toRemove = new ArrayList();
/* 364 */     for (LatticeWord lw : words) {
/* 365 */       this.latticeWords.remove(lw);
/* 366 */       int oldEndTime = lw.endNode;
/* 367 */       lw.endNode = newEndTime;
/*     */       
/* 369 */       if (this.latticeWords.contains(lw)) {
/* 370 */         if (DEBUG) {
/* 371 */           System.err.println("duplicate found");
/*     */         }
/* 373 */         LatticeWord twin = (LatticeWord)this.latticeWords.get(this.latticeWords.indexOf(lw));
/*     */         
/* 375 */         lw.endNode = oldEndTime;
/* 376 */         twin.merge(lw);
/* 377 */         this.wordsStartAt[lw.startNode].remove(lw);
/*     */         
/* 379 */         toRemove.add(lw);
/* 380 */         for (int i = lw.startNode; i <= lw.endNode; i++) {
/* 381 */           this.wordsAtTime[i].remove(lw);
/*     */         }
/*     */       } else {
/* 384 */         if (oldEndTime > newEndTime) {
/* 385 */           for (int i = newEndTime + 1; i <= oldEndTime; i++) {
/* 386 */             this.wordsAtTime[i].remove(lw);
/*     */           }
/*     */         } else {
/* 389 */           for (int i = oldEndTime + 1; i <= newEndTime; i++) {
/* 390 */             this.wordsAtTime[i].add(lw);
/*     */           }
/*     */         }
/* 393 */         this.latticeWords.add(lw);
/* 394 */         if (oldEndTime != newEndTime)
/*     */         {
/* 396 */           toRemove.add(lw);
/* 397 */           this.wordsEndAt[newEndTime].add(lw);
/*     */         }
/*     */       }
/*     */     }
/* 401 */     words.removeAll(toRemove);
/*     */   }
/*     */   
/*     */   private void removeSilence() {
/* 405 */     ArrayList<LatticeWord> silences = new ArrayList();
/* 406 */     for (LatticeWord lw : this.latticeWords) {
/* 407 */       if (lw.word.equalsIgnoreCase("<SIL>")) {
/* 408 */         silences.add(lw);
/*     */       }
/*     */     }
/* 411 */     for (LatticeWord lw : silences)
/*     */     {
/* 413 */       changeEndTimes(this.wordsEndAt[lw.startNode], lw.endNode);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 418 */     silences.clear();
/* 419 */     for (LatticeWord lw : this.latticeWords) {
/* 420 */       if (lw.word.equalsIgnoreCase("<SIL>")) {
/* 421 */         silences.add(lw);
/*     */       }
/*     */     }
/* 424 */     for (LatticeWord lw : silences) {
/* 425 */       if (lw.word.equalsIgnoreCase("<SIL>")) {
/* 426 */         this.latticeWords.remove(lw);
/* 427 */         this.wordsStartAt[lw.startNode].remove(lw);
/* 428 */         this.wordsEndAt[lw.endNode].remove(lw);
/* 429 */         for (int j = lw.startNode; j <= lw.endNode; j++) {
/* 430 */           this.wordsAtTime[j].remove(lw);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private int mergeDuplicates() {
/* 437 */     int numMerged = 0;
/* 438 */     for (int i = 0; i < this.latticeWords.size() - 1; i++) {
/* 439 */       LatticeWord first = (LatticeWord)this.latticeWords.get(i);
/* 440 */       for (int j = i + 1; j < this.latticeWords.size(); j++) {
/* 441 */         LatticeWord second = (LatticeWord)this.latticeWords.get(j);
/* 442 */         if (first.equals(second)) {
/* 443 */           if (DEBUG) {
/* 444 */             System.err.println("removed duplicate");
/*     */           }
/* 446 */           first.merge(second);
/* 447 */           this.latticeWords.remove(j);
/* 448 */           this.wordsStartAt[second.startNode].remove(second);
/* 449 */           this.wordsEndAt[second.endNode].remove(second);
/* 450 */           for (int k = second.startNode; k <= second.endNode; k++) {
/* 451 */             this.wordsAtTime[k].remove(second);
/*     */           }
/* 453 */           numMerged++;
/* 454 */           j--;
/*     */         }
/*     */       }
/*     */     }
/* 458 */     return numMerged;
/*     */   }
/*     */   
/*     */   public void printWords() {
/* 462 */     Collections.sort(this.latticeWords);
/* 463 */     System.out.println("Words: ");
/* 464 */     for (LatticeWord lw : this.latticeWords) {
/* 465 */       System.out.println(lw);
/*     */     }
/*     */   }
/*     */   
/*     */   private double getProb(LatticeWord lw) {
/* 470 */     return lw.am * 100.0D + lw.lm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processLattice()
/*     */   {
/* 479 */     buildWordTimeArrays();
/*     */     
/* 481 */     removeSilence();
/*     */     
/* 483 */     mergeDuplicates();
/*     */     
/* 485 */     removeRedundency();
/*     */     
/* 487 */     removeEmptyNodes();
/*     */     
/* 489 */     if (PRETTYPRINT) {
/* 490 */       printWords();
/*     */     }
/*     */   }
/*     */   
/*     */   public LatticeReader(String filename)
/*     */     throws Exception
/*     */   {
/* 497 */     this(filename, true, false, false);
/*     */   }
/*     */   
/*     */   public LatticeReader(String filename, boolean mergeType) throws Exception {
/* 501 */     this(filename, mergeType, false, false);
/*     */   }
/*     */   
/*     */   public LatticeReader(String filename, boolean mergeType, boolean debug, boolean prettyPrint) throws Exception {
/* 505 */     DEBUG = debug;
/* 506 */     PRETTYPRINT = prettyPrint;
/* 507 */     mergeType = mergeType;
/*     */     
/* 509 */     BufferedReader in = new BufferedReader(new FileReader(filename));
/*     */     
/* 511 */     readInput(in);
/*     */     
/* 513 */     if (PRETTYPRINT) {
/* 514 */       printWords();
/*     */     }
/*     */     
/* 517 */     processLattice();
/*     */   }
/*     */   
/*     */   public List<LatticeWord> getLatticeWords()
/*     */   {
/* 522 */     return this.latticeWords;
/*     */   }
/*     */   
/*     */   public int getNumStates() {
/* 526 */     return this.numStates;
/*     */   }
/*     */   
/*     */   public List<LatticeWord> getWordsOverSpan(int a, int b) {
/* 530 */     ArrayList<LatticeWord> words = new ArrayList();
/* 531 */     for (LatticeWord lw : this.wordsStartAt[a]) {
/* 532 */       if (lw.endNode == b) {
/* 533 */         words.add(lw);
/*     */       }
/*     */     }
/* 536 */     return words;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception
/*     */   {
/* 541 */     boolean mergeType = true;
/* 542 */     boolean prettyPrint = true;
/* 543 */     boolean debug = false;
/* 544 */     String parseGram = null;
/* 545 */     String filename = args[0];
/*     */     
/* 547 */     for (int i = 1; i < args.length; i++) {
/* 548 */       if (args[i].equalsIgnoreCase("-debug")) {
/* 549 */         debug = true;
/* 550 */       } else if (args[i].equalsIgnoreCase("-useMax")) {
/* 551 */         mergeType = false;
/* 552 */       } else if (args[i].equalsIgnoreCase("-useSum")) {
/* 553 */         mergeType = true;
/* 554 */       } else if (args[i].equalsIgnoreCase("-noPrettyPrint")) {
/* 555 */         prettyPrint = false;
/* 556 */       } else if (args[i].equalsIgnoreCase("-parser")) {
/* 557 */         parseGram = args[(++i)];
/*     */       } else {
/* 559 */         System.err.println("unrecognized flag: " + args[i]);
/* 560 */         System.err.println("usage: java LatticeReader <file> [ -debug ] [ -useMax ] [ -useSum ] [ -noPrettyPrint ] [ -parser parserFile ]");
/* 561 */         System.exit(0);
/*     */       }
/*     */     }
/*     */     
/* 565 */     LatticeReader lr = new LatticeReader(filename, mergeType, debug, prettyPrint);
/*     */     
/* 567 */     if (parseGram != null) {
/* 568 */       Options op = new Options();
/* 569 */       op.doDep = false;
/* 570 */       Test.maxLength = 80;
/* 571 */       LexicalizedParser lp = new LexicalizedParser(parseGram, op);
/* 572 */       Test.maxSpanForTags = 80;
/* 573 */       lp.parse(lr);
/* 574 */       Tree t = lp.getBestParse();
/* 575 */       t.pennPrint();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LatticeWord implements Comparable {
/*     */     public String word;
/*     */     public int startNode;
/*     */     public int endNode;
/*     */     public double lm;
/*     */     public double am;
/*     */     public int pronunciation;
/*     */     
/*     */     public LatticeWord(String word, int startNode, int endNode, double lm, double am, int pronunciation) {
/* 588 */       this.word = word;
/* 589 */       this.startNode = startNode;
/* 590 */       this.endNode = endNode;
/* 591 */       this.lm = lm;
/* 592 */       this.am = am;
/* 593 */       this.pronunciation = pronunciation;
/*     */     }
/*     */     
/*     */     public void merge(LatticeWord lw) {
/* 597 */       if (!LatticeReader.mergeType) {
/* 598 */         this.am = Math.max(this.am, lw.am);
/* 599 */         lw.am = this.am;
/* 600 */       } else if (LatticeReader.mergeType == true) {
/* 601 */         double tmp = lw.am;
/* 602 */         lw.am += this.am;
/* 603 */         this.am += tmp;
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString() {
/* 608 */       StringBuffer sb = new StringBuffer();
/* 609 */       sb.append(this.startNode).append("\t");
/* 610 */       sb.append(this.endNode).append("\t");
/* 611 */       sb.append("lm=").append(this.lm).append(",");
/* 612 */       sb.append("am=").append(this.am).append("\t");
/* 613 */       sb.append(this.word);
/* 614 */       return sb.toString();
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 618 */       if (!(o instanceof LatticeWord)) {
/* 619 */         return false;
/*     */       }
/* 621 */       LatticeWord other = (LatticeWord)o;
/* 622 */       if (!this.word.equalsIgnoreCase(other.word)) {
/* 623 */         return false;
/*     */       }
/* 625 */       if (this.startNode != other.startNode) {
/* 626 */         return false;
/*     */       }
/* 628 */       if (this.endNode != other.endNode) {
/* 629 */         return false;
/*     */       }
/*     */       
/* 632 */       return true;
/*     */     }
/*     */     
/*     */     public int compareTo(Object o) {
/* 636 */       LatticeWord other = (LatticeWord)o;
/* 637 */       if (this.startNode < other.startNode)
/* 638 */         return -1;
/* 639 */       if (this.startNode > other.startNode) {
/* 640 */         return 1;
/*     */       }
/*     */       
/* 643 */       if (this.endNode < other.endNode)
/* 644 */         return -1;
/* 645 */       if (this.endNode > other.endNode) {
/* 646 */         return 1;
/*     */       }
/*     */       
/* 649 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\LatticeReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */