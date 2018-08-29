/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTag;
/*     */ import edu.stanford.nlp.ling.CategoryWordTagFactory;
/*     */ import edu.stanford.nlp.ling.HasTag;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.Word;
/*     */ import edu.stanford.nlp.parser.KBestViterbiParser;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.ScoredObject;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import edu.stanford.nlp.util.Timing;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExhaustiveDependencyParser
/*     */   implements Scorer, KBestViterbiParser
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   private static final boolean DEBUG_MORE = false;
/*  70 */   private Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
/*  71 */   private Numberer wordNumberer = Numberer.getGlobalNumberer("words");
/*     */   
/*     */ 
/*     */   private TreeFactory tf;
/*     */   
/*     */ 
/*     */   private DependencyGrammar dg;
/*     */   
/*     */ 
/*     */   private Lexicon lex;
/*     */   
/*     */ 
/*     */   private Options op;
/*     */   
/*     */   private TreebankLanguagePack tlp;
/*     */   
/*     */   private List sentence;
/*     */   
/*     */   private int[] words;
/*     */   
/*     */   private float[][][] iScoreH;
/*     */   
/*     */   private float[][][] oScoreH;
/*     */   
/*     */   private float[][][] iScoreHSum;
/*     */   
/*     */   private static final boolean doiScoreHSum = false;
/*     */   
/*     */   private int[][] rawDistance;
/*     */   
/*     */   int[][] binDistance;
/*     */   
/*     */   float[][][][][] headScore;
/*     */   
/*     */   float[][][] headStop;
/*     */   
/*     */   private boolean[][][] oPossibleByL;
/*     */   
/*     */   private boolean[][][] oPossibleByR;
/*     */   
/*     */   private boolean[][][] iPossibleByL;
/*     */   
/*     */   private boolean[][][] iPossibleByR;
/*     */   
/* 115 */   private int arraySize = 0;
/* 116 */   private int myMaxLength = 559038737;
/*     */   private static final double TOL = 1.0E-5D;
/*     */   
/* 119 */   float oScore(int start, int end, int head, int tag) { return this.oScoreH[head][this.dg.tagBin(tag)][start] + this.oScoreH[head][this.dg.tagBin(tag)][end]; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   float iScore(int start, int end, int head, int tag)
/*     */   {
/* 130 */     return this.iScoreH[head][this.dg.tagBin(tag)][start] + this.iScoreH[head][this.dg.tagBin(tag)][end];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   float iScoreTotal(int start, int end, int head, int tag)
/*     */   {
/* 141 */     throw new RuntimeException("Summed inner scores not computed");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double oScore(Edge edge)
/*     */   {
/* 148 */     return oScore(edge.start, edge.end, edge.head, edge.tag);
/*     */   }
/*     */   
/*     */   public double iScore(Edge edge) {
/* 152 */     return iScore(edge.start, edge.end, edge.head, edge.tag);
/*     */   }
/*     */   
/*     */   public boolean oPossible(Hook hook) {
/* 156 */     return hook.isPreHook() ? this.oPossibleByR[hook.end][hook.head][this.dg.tagBin(hook.tag)] : this.oPossibleByL[hook.start][hook.head][this.dg.tagBin(hook.tag)];
/*     */   }
/*     */   
/*     */   public boolean iPossible(Hook hook) {
/* 160 */     return hook.isPreHook() ? this.iPossibleByR[hook.start][hook.head][this.dg.tagBin(hook.tag)] : this.iPossibleByL[hook.end][hook.head][this.dg.tagBin(hook.tag)];
/*     */   }
/*     */   
/*     */   public boolean parse(List<? extends HasWord> sentence, String goal) {
/* 164 */     return parse(sentence);
/*     */   }
/*     */   
/*     */   public boolean parse(List<? extends HasWord> sentence) {
/* 168 */     if (Test.verbose) {
/* 169 */       Timing.tick("Starting dependency parse.");
/*     */     }
/* 171 */     this.sentence = sentence;
/* 172 */     int length = sentence.size();
/* 173 */     if (length > this.arraySize) {
/* 174 */       if ((length > Test.maxLength + 1) || (length >= this.myMaxLength)) {
/* 175 */         throw new OutOfMemoryError("Refusal to create such large arrays.");
/*     */       }
/*     */       try {
/* 178 */         createArrays(length + 1);
/*     */       } catch (OutOfMemoryError e) {
/* 180 */         this.myMaxLength = length;
/* 181 */         if (this.arraySize > 0) {
/*     */           try {
/* 183 */             createArrays(this.arraySize);
/*     */           } catch (OutOfMemoryError e2) {
/* 185 */             throw new RuntimeException("CANNOT EVEN CREATE ARRAYS OF ORIGINAL SIZE!!! " + this.arraySize);
/*     */           }
/*     */         }
/* 188 */         throw e;
/*     */       }
/* 190 */       this.arraySize = (length + 1);
/* 191 */       if (Test.verbose) {
/* 192 */         System.err.println("Created dparser arrays of size " + this.arraySize);
/*     */       }
/*     */     }
/*     */     
/* 196 */     if (Test.verbose) {
/* 197 */       System.err.print("Initializing...");
/*     */     }
/*     */     
/*     */ 
/* 201 */     this.words = new int[length];
/* 202 */     int numTags = this.dg.numTagBins();
/*     */     
/*     */ 
/* 205 */     boolean[][] hasTag = new boolean[length][numTags];
/* 206 */     for (int i = 0; i < length; i++)
/*     */     {
/* 208 */       Object o = sentence.get(i);
/* 209 */       if ((o instanceof HasWord)) {
/* 210 */         o = ((HasWord)o).word();
/*     */       }
/* 212 */       this.words[i] = this.wordNumberer.number(o.toString());
/*     */     }
/*     */     
/*     */ 
/* 216 */     for (int head = 0; head < length; head++) {
/* 217 */       for (int tag = 0; tag < numTags; tag++) {
/* 218 */         Arrays.fill(this.iScoreH[head][tag], Float.NEGATIVE_INFINITY);
/* 219 */         Arrays.fill(this.oScoreH[head][tag], Float.NEGATIVE_INFINITY);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 225 */     for (int head = 0; head < length; head++)
/* 226 */       for (int loc = 0; loc <= length; loc++) {
/* 227 */         this.rawDistance[head][loc] = (head >= loc ? head - loc : loc - head - 1);
/* 228 */         this.binDistance[head][loc] = this.dg.distanceBin(this.rawDistance[head][loc]);
/*     */       }
/*     */     String trueTagStr;
/*     */     Iterator<IntTaggedWord> taggingI;
/* 232 */     for (int start = 0; start + 1 <= length; start++) {
/* 233 */       trueTagStr = null;
/* 234 */       if ((sentence.get(start) instanceof HasTag)) {
/* 235 */         trueTagStr = ((HasTag)sentence.get(start)).tag();
/* 236 */         if ("".equals(trueTagStr)) {
/* 237 */           trueTagStr = null;
/*     */         }
/*     */       }
/* 240 */       int word = this.words[start];
/* 241 */       for (taggingI = this.lex.ruleIteratorByWord(word, start); taggingI.hasNext();) {
/* 242 */         IntTaggedWord tagging = (IntTaggedWord)taggingI.next();
/* 243 */         if ((trueTagStr == null) || 
/* 244 */           (this.tlp.basicCategory(tagging.tagString()).equals(trueTagStr)))
/*     */         {
/*     */ 
/*     */ 
/* 248 */           float score = this.lex.score(tagging, start);
/*     */           
/* 250 */           if (score > Float.NEGATIVE_INFINITY) {
/* 251 */             int tag = tagging.tag;
/* 252 */             this.iScoreH[start][this.dg.tagBin(tag)][start] = 0.0F;
/* 253 */             this.iScoreH[start][this.dg.tagBin(tag)][(start + 1)] = 0.0F;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 262 */     for (int hWord = 0; hWord < length; hWord++) {
/* 263 */       for (int hTag = 0; hTag < numTags; hTag++) {
/* 264 */         hasTag[hWord][hTag] = (this.iScoreH[hWord][hTag][hWord] + this.iScoreH[hWord][hTag][(hWord + 1)] > Float.NEGATIVE_INFINITY ? 1 : 0);
/* 265 */         Arrays.fill(this.headStop[hWord][hTag], Float.NEGATIVE_INFINITY);
/* 266 */         for (int aWord = 0; aWord < length; aWord++) {
/* 267 */           for (int dist = 0; dist < this.dg.numDistBins(); dist++) {
/* 268 */             Arrays.fill(this.headScore[dist][hWord][hTag][aWord], Float.NEGATIVE_INFINITY);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 275 */     for (int hWord = 0; hWord < length; hWord++) {
/* 276 */       for (int hTag = 0; hTag < numTags; hTag++)
/*     */       {
/*     */ 
/*     */ 
/* 280 */         if (hasTag[hWord][hTag] != 0)
/*     */         {
/*     */ 
/* 283 */           for (int split = 0; split <= length; split++) {
/* 284 */             if (split <= hWord) {
/* 285 */               this.headStop[hWord][hTag][split] = ((float)this.dg.scoreTB(this.words[hWord], hTag, -2, -2, false, hWord - split));
/*     */             }
/*     */             else {
/* 288 */               this.headStop[hWord][hTag][split] = ((float)this.dg.scoreTB(this.words[hWord], hTag, -2, -2, true, split - hWord - 1));
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 294 */           for (int aWord = 0; aWord < length; aWord++) {
/* 295 */             if (aWord != hWord)
/*     */             {
/*     */ 
/* 298 */               boolean leftHeaded = hWord < aWord;
/*     */               int end;
/*     */               int start;
/* 301 */               int end; if (leftHeaded) {
/* 302 */                 int start = hWord + 1;
/* 303 */                 end = aWord + 1;
/*     */               } else {
/* 305 */                 start = aWord + 1;
/* 306 */                 end = hWord + 1;
/*     */               }
/* 308 */               for (int aTag = 0; aTag < numTags; aTag++)
/* 309 */                 if (hasTag[aWord][aTag] != 0)
/*     */                 {
/*     */ 
/* 312 */                   for (int split = start; split < end; split++)
/*     */                   {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 320 */                     int headDistance = this.rawDistance[hWord][split];
/* 321 */                     int binDist = this.binDistance[hWord][split];
/* 322 */                     this.headScore[binDist][hWord][hTag][aWord][aTag] = ((float)this.dg.scoreTB(this.words[hWord], hTag, this.words[aWord], aTag, leftHeaded, headDistance));
/*     */                     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 328 */                     while ((split + 1 < end) && (this.binDistance[hWord][(split + 1)] == binDist))
/* 329 */                       split++;
/*     */                   } }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 336 */     if (Test.verbose) {
/* 337 */       Timing.tick("done.");
/*     */       
/* 339 */       System.err.print("Starting insides...");
/*     */     }
/*     */     
/* 342 */     for (int diff = 2; diff <= length; diff++)
/*     */     {
/* 344 */       for (int start = 0; start + diff <= length; start++) {
/* 345 */         int end = start + diff;
/*     */         
/*     */ 
/*     */ 
/* 349 */         int endHead = end - 1;
/* 350 */         for (int endTag = 0; endTag < numTags; endTag++) {
/* 351 */           if (hasTag[endHead][endTag] != 0)
/*     */           {
/*     */ 
/*     */ 
/* 355 */             float bestScore = Float.NEGATIVE_INFINITY;
/*     */             
/* 357 */             for (int argHead = start; argHead < endHead; argHead++) {
/* 358 */               for (int argTag = 0; argTag < numTags; argTag++) {
/* 359 */                 if (hasTag[argHead][argTag] != 0)
/*     */                 {
/*     */ 
/* 362 */                   float argLeftScore = this.iScoreH[argHead][argTag][start];
/* 363 */                   if (argLeftScore != Float.NEGATIVE_INFINITY)
/*     */                   {
/*     */ 
/* 366 */                     float stopLeftScore = this.headStop[argHead][argTag][start];
/* 367 */                     if (stopLeftScore != Float.NEGATIVE_INFINITY)
/*     */                     {
/*     */ 
/* 370 */                       for (int split = argHead + 1; split < end; split++)
/*     */                       {
/* 372 */                         float depScore = this.headScore[this.binDistance[endHead][split]][endHead][endTag][argHead][argTag];
/* 373 */                         if (depScore != Float.NEGATIVE_INFINITY)
/*     */                         {
/*     */ 
/* 376 */                           float score = this.iScoreH[endHead][endTag][split] + argLeftScore + this.iScoreH[argHead][argTag][split] + depScore + stopLeftScore + this.headStop[argHead][argTag][split];
/*     */                           
/*     */ 
/*     */ 
/*     */ 
/* 381 */                           if (score > bestScore) {
/* 382 */                             bestScore = score;
/*     */                           }
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 400 */             this.iScoreH[endHead][endTag][start] = bestScore;
/*     */           }
/*     */         }
/*     */         
/* 404 */         int startHead = start;
/* 405 */         for (int startTag = 0; startTag < numTags; startTag++) {
/* 406 */           if (hasTag[startHead][startTag] != 0)
/*     */           {
/*     */ 
/*     */ 
/* 410 */             float bestScore = Float.NEGATIVE_INFINITY;
/*     */             
/* 412 */             for (int argHead = start + 1; argHead < end; argHead++) {
/* 413 */               for (int argTag = 0; argTag < numTags; argTag++) {
/* 414 */                 if (hasTag[argHead][argTag] != 0)
/*     */                 {
/*     */ 
/* 417 */                   float argRightScore = this.iScoreH[argHead][argTag][end];
/* 418 */                   if (argRightScore != Float.NEGATIVE_INFINITY)
/*     */                   {
/*     */ 
/* 421 */                     float stopRightScore = this.headStop[argHead][argTag][end];
/* 422 */                     if (stopRightScore != Float.NEGATIVE_INFINITY)
/*     */                     {
/*     */ 
/* 425 */                       for (int split = start + 1; split <= argHead; split++)
/*     */                       {
/* 427 */                         float depScore = this.headScore[this.binDistance[startHead][split]][startHead][startTag][argHead][argTag];
/* 428 */                         if (depScore != Float.NEGATIVE_INFINITY)
/*     */                         {
/*     */ 
/* 431 */                           float score = this.iScoreH[startHead][startTag][split] + this.iScoreH[argHead][argTag][split] + argRightScore + depScore + stopRightScore + this.headStop[argHead][argTag][split];
/*     */                           
/*     */ 
/*     */ 
/*     */ 
/* 436 */                           if (score > bestScore) {
/* 437 */                             bestScore = score;
/*     */                           }
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 458 */             this.iScoreH[startHead][startTag][end] = bestScore;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 463 */     int goalTag = this.dg.tagBin(this.tagNumberer.number(".$$."));
/* 464 */     if (Test.verbose) {
/* 465 */       Timing.tick("done.");
/* 466 */       System.out.println("Dep  parsing " + length + " words (incl. stop): insideScore " + (this.iScoreH[(length - 1)][goalTag][0] + this.iScoreH[(length - 1)][goalTag][length]));
/*     */     }
/* 468 */     if (!this.op.doPCFG) {
/* 469 */       return hasParse();
/*     */     }
/* 471 */     if (Test.verbose) {
/* 472 */       System.err.print("Starting outsides...");
/*     */     }
/* 474 */     this.oScoreH[(length - 1)][goalTag][0] = 0.0F;
/* 475 */     this.oScoreH[(length - 1)][goalTag][length] = 0.0F;
/* 476 */     for (int diff = length; diff > 1; diff--) {
/* 477 */       for (int start = 0; start + diff <= length; start++) {
/* 478 */         int end = start + diff;
/*     */         
/* 480 */         int endHead = end - 1;
/* 481 */         for (int endTag = 0; endTag < numTags; endTag++) {
/* 482 */           if (hasTag[endHead][endTag] != 0)
/*     */           {
/*     */ 
/* 485 */             for (int argHead = start; argHead < endHead; argHead++) {
/* 486 */               for (int argTag = 0; argTag < numTags; argTag++) {
/* 487 */                 if (hasTag[argHead][argTag] != 0)
/*     */                 {
/*     */ 
/* 490 */                   for (int split = argHead; split <= endHead; split++) {
/* 491 */                     float subScore = this.oScoreH[endHead][endTag][start] + this.headScore[this.binDistance[endHead][split]][endHead][endTag][argHead][argTag] + this.headStop[argHead][argTag][start] + this.headStop[argHead][argTag][split];
/* 492 */                     float scoreRight = subScore + this.iScoreH[argHead][argTag][start] + this.iScoreH[argHead][argTag][split];
/* 493 */                     float scoreMid = subScore + this.iScoreH[argHead][argTag][start] + this.iScoreH[endHead][endTag][split];
/* 494 */                     float scoreLeft = subScore + this.iScoreH[argHead][argTag][split] + this.iScoreH[endHead][endTag][split];
/* 495 */                     if (scoreRight > this.oScoreH[endHead][endTag][split]) {
/* 496 */                       this.oScoreH[endHead][endTag][split] = scoreRight;
/*     */                     }
/* 498 */                     if (scoreMid > this.oScoreH[argHead][argTag][split]) {
/* 499 */                       this.oScoreH[argHead][argTag][split] = scoreMid;
/*     */                     }
/* 501 */                     if (scoreLeft > this.oScoreH[argHead][argTag][start])
/* 502 */                       this.oScoreH[argHead][argTag][start] = scoreLeft;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 509 */         int startHead = start;
/* 510 */         for (int startTag = 0; startTag < numTags; startTag++) {
/* 511 */           if (hasTag[startHead][startTag] != 0)
/*     */           {
/*     */ 
/* 514 */             for (int argHead = startHead + 1; argHead < end; argHead++) {
/* 515 */               for (int argTag = 0; argTag < numTags; argTag++) {
/* 516 */                 if (hasTag[argHead][argTag] != 0)
/*     */                 {
/*     */ 
/* 519 */                   for (int split = startHead + 1; split <= argHead; split++) {
/* 520 */                     float subScore = this.oScoreH[startHead][startTag][end] + this.headScore[this.binDistance[startHead][split]][startHead][startTag][argHead][argTag] + this.headStop[argHead][argTag][split] + this.headStop[argHead][argTag][end];
/* 521 */                     float scoreLeft = subScore + this.iScoreH[argHead][argTag][split] + this.iScoreH[argHead][argTag][end];
/* 522 */                     float scoreMid = subScore + this.iScoreH[startHead][startTag][split] + this.iScoreH[argHead][argTag][end];
/* 523 */                     float scoreRight = subScore + this.iScoreH[startHead][startTag][split] + this.iScoreH[argHead][argTag][split];
/* 524 */                     if (scoreLeft > this.oScoreH[startHead][startTag][split]) {
/* 525 */                       this.oScoreH[startHead][startTag][split] = scoreLeft;
/*     */                     }
/* 527 */                     if (scoreMid > this.oScoreH[argHead][argTag][split]) {
/* 528 */                       this.oScoreH[argHead][argTag][split] = scoreMid;
/*     */                     }
/* 530 */                     if (scoreRight > this.oScoreH[argHead][argTag][end])
/* 531 */                       this.oScoreH[argHead][argTag][end] = scoreRight;
/*     */                   } }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 539 */     if (Test.verbose) {
/* 540 */       Timing.tick("done.");
/* 541 */       System.err.print("Starting half-filters...");
/*     */     }
/* 543 */     for (int loc = 0; loc <= length; loc++) {
/* 544 */       for (int head = 0; head < length; head++) {
/* 545 */         Arrays.fill(this.iPossibleByL[loc][head], false);
/* 546 */         Arrays.fill(this.iPossibleByR[loc][head], false);
/* 547 */         Arrays.fill(this.oPossibleByL[loc][head], false);
/* 548 */         Arrays.fill(this.oPossibleByR[loc][head], false);
/*     */       }
/*     */     }
/* 551 */     for (int head = 0; head < length; head++) {
/* 552 */       for (int tag = 0; tag < numTags; tag++) {
/* 553 */         if (hasTag[head][tag] != 0)
/*     */         {
/*     */ 
/* 556 */           for (int start = 0; start <= head; start++) {
/* 557 */             for (int end = head + 1; end <= length; end++)
/* 558 */               if ((this.iScoreH[head][tag][start] + this.iScoreH[head][tag][end] > Float.NEGATIVE_INFINITY) && (this.oScoreH[head][tag][start] + this.oScoreH[head][tag][end] > Float.NEGATIVE_INFINITY)) {
/* 559 */                 this.iPossibleByR[end][head][tag] = 1;
/* 560 */                 this.iPossibleByL[start][head][tag] = 1;
/* 561 */                 this.oPossibleByR[end][head][tag] = 1;
/* 562 */                 this.oPossibleByL[start][head][tag] = 1;
/*     */               }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 568 */     if (Test.verbose) {
/* 569 */       Timing.tick("done.");
/*     */     }
/* 571 */     return hasParse();
/*     */   }
/*     */   
/*     */   public boolean hasParse() {
/* 575 */     return getBestScore() > Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public double getBestScore() {
/* 579 */     int length = this.sentence.size();
/* 580 */     if (length > this.arraySize) {
/* 581 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/* 583 */     int goalTag = this.tagNumberer.number(".$$.");
/* 584 */     return iScore(0, length, length - 1, goalTag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void displayHeadScores()
/*     */   {
/* 592 */     int numTags = this.tagNumberer.total();
/* 593 */     System.out.println("---- headScore matrix (head x dep, best tags) ----");
/* 594 */     System.out.print(StringUtils.padOrTrim("", 6));
/* 595 */     for (int i = 0; i < this.words.length; i++) {
/* 596 */       System.out.print(" " + StringUtils.padOrTrim(this.wordNumberer.object(this.words[i]).toString(), 2));
/*     */     }
/* 598 */     System.out.println();
/* 599 */     for (int hWord = 0; hWord < this.words.length; hWord++) {
/* 600 */       System.out.print(StringUtils.padOrTrim(this.wordNumberer.object(this.words[hWord]).toString(), 6));
/* 601 */       int bigBD = -1;int bigHTag = -1;int bigATag = -1;
/* 602 */       for (int aWord = 0; aWord < this.words.length; aWord++)
/*     */       {
/*     */ 
/*     */ 
/* 606 */         float biggest = Float.NEGATIVE_INFINITY;
/* 607 */         for (int bd = 0; bd < this.dg.numDistBins(); bd++) {
/* 608 */           for (int hTag = 0; hTag < numTags; hTag++)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 619 */             for (int aTag = 0; aTag < numTags; aTag++) {
/* 620 */               if (this.headScore[bd][hWord][this.dg.tagBin(hTag)][aWord][this.dg.tagBin(aTag)] > biggest) {
/* 621 */                 biggest = this.headScore[bd][hWord][this.dg.tagBin(hTag)][aWord][this.dg.tagBin(aTag)];
/* 622 */                 bigBD = bd;
/* 623 */                 bigHTag = hTag;
/* 624 */                 bigATag = aTag;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 629 */         if (Float.isInfinite(biggest)) {
/* 630 */           System.out.print(" " + StringUtils.padOrTrim("in", 2));
/*     */         } else {
/* 632 */           int score = Math.round(Math.abs(this.headScore[bigBD][hWord][this.dg.tagBin(bigHTag)][aWord][this.dg.tagBin(bigATag)]));
/* 633 */           System.out.print(" " + StringUtils.padOrTrim(Integer.toString(score), 2));
/*     */         }
/*     */       }
/* 636 */       System.out.println();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean matches(double x, double y)
/*     */   {
/* 643 */     return Math.abs(x - y) / (Math.abs(x) + Math.abs(y) + 1.0E-10D) < 1.0E-5D;
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
/*     */   private Tree extractBestParse(int start, int end, int hWord, int hTag)
/*     */   {
/* 657 */     String headWordStr = (String)this.wordNumberer.object(this.words[hWord]);
/* 658 */     String headTagStr = (String)this.tagNumberer.object(hTag);
/* 659 */     Label headLabel = new CategoryWordTag(headWordStr, headWordStr, headTagStr);
/* 660 */     int numTags = this.tagNumberer.total();
/*     */     
/*     */ 
/* 663 */     if (end - start == 1) {
/* 664 */       Tree leaf = this.tf.newLeaf(new Word(headWordStr));
/* 665 */       return this.tf.newTreeNode(headLabel, Collections.singletonList(leaf));
/*     */     }
/*     */     
/* 668 */     List<Tree> children = new ArrayList();
/* 669 */     double bestScore = iScore(start, end, hWord, hTag);
/* 670 */     for (int split = start + 1; split < end; split++) {
/* 671 */       int binD = this.binDistance[hWord][split];
/* 672 */       if (hWord < split) {
/* 673 */         for (int aWord = split; aWord < end; aWord++) {
/* 674 */           for (int aTag = 0; aTag < numTags; aTag++) {
/* 675 */             if (matches(iScore(start, split, hWord, hTag) + iScore(split, end, aWord, aTag) + this.headScore[binD][hWord][this.dg.tagBin(hTag)][aWord][this.dg.tagBin(aTag)] + this.headStop[aWord][this.dg.tagBin(aTag)][split] + this.headStop[aWord][this.dg.tagBin(aTag)][end], bestScore))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 682 */               children.add(extractBestParse(start, split, hWord, hTag));
/* 683 */               children.add(extractBestParse(split, end, aWord, aTag));
/* 684 */               return this.tf.newTreeNode(headLabel, children);
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/* 689 */         for (int aWord = start; aWord < split; aWord++) {
/* 690 */           for (int aTag = 0; aTag < numTags; aTag++) {
/* 691 */             if (matches(iScore(start, split, aWord, aTag) + iScore(split, end, hWord, hTag) + this.headScore[binD][hWord][this.dg.tagBin(hTag)][aWord][this.dg.tagBin(aTag)] + this.headStop[aWord][this.dg.tagBin(aTag)][start] + this.headStop[aWord][this.dg.tagBin(aTag)][split], bestScore))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 697 */               children.add(extractBestParse(start, split, aWord, aTag));
/* 698 */               children.add(extractBestParse(split, end, hWord, hTag));
/*     */               
/* 700 */               return this.tf.newTreeNode(headLabel, children);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 706 */     System.err.println("Problem in ExhaustiveDependencyParser::extractBestParse");
/* 707 */     return null;
/*     */   }
/*     */   
/*     */   private Tree flatten(Tree tree) {
/* 711 */     if ((tree.isLeaf()) || (tree.isPreTerminal())) {
/* 712 */       return tree;
/*     */     }
/* 714 */     List<Tree> newChildren = new ArrayList();
/* 715 */     Tree[] children = tree.children();
/* 716 */     for (int c = 0; c < children.length; c++) {
/* 717 */       Tree child = children[c];
/* 718 */       Tree newChild = flatten(child);
/* 719 */       if ((!newChild.isPreTerminal()) && (newChild.label().toString().equals(tree.label().toString()))) {
/* 720 */         newChildren.addAll(newChild.getChildrenAsList());
/*     */       } else {
/* 722 */         newChildren.add(newChild);
/*     */       }
/*     */     }
/* 725 */     return this.tf.newTreeNode(tree.label(), newChildren);
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
/*     */   public Tree getBestParse()
/*     */   {
/* 742 */     if (!hasParse()) {
/* 743 */       return null;
/*     */     }
/* 745 */     return flatten(extractBestParse(0, this.words.length, this.words.length - 1, this.tagNumberer.number(".$$.")));
/*     */   }
/*     */   
/*     */   public ExhaustiveDependencyParser(DependencyGrammar dg, Lexicon lex, Options op) {
/* 749 */     this.dg = dg;
/* 750 */     this.lex = lex;
/* 751 */     this.op = op;
/* 752 */     this.tlp = op.langpack();
/* 753 */     this.tf = new LabeledScoredTreeFactory(new CategoryWordTagFactory());
/*     */   }
/*     */   
/*     */   private void createArrays(int length) {
/* 757 */     this.iScoreH = (this.oScoreH = this.headStop = this.iScoreHSum = (float[][][])null);
/* 758 */     this.iPossibleByL = (this.iPossibleByR = this.oPossibleByL = this.oPossibleByR = (boolean[][][])null);
/* 759 */     this.headScore = ((float[][][][][])null);
/* 760 */     this.rawDistance = (this.binDistance = (int[][])null);
/*     */     
/* 762 */     int tagNum = this.dg.numTagBins();
/*     */     
/* 764 */     this.iScoreH = new float[length + 1][tagNum][length + 1];
/* 765 */     this.oScoreH = new float[length + 1][tagNum][length + 1];
/*     */     
/*     */ 
/*     */ 
/* 769 */     this.iPossibleByL = new boolean[length + 1][length + 1][tagNum];
/* 770 */     this.iPossibleByR = new boolean[length + 1][length + 1][tagNum];
/* 771 */     this.oPossibleByL = new boolean[length + 1][length + 1][tagNum];
/* 772 */     this.oPossibleByR = new boolean[length + 1][length + 1][tagNum];
/* 773 */     this.headScore = new float[this.dg.numDistBins()][length][tagNum][length][tagNum];
/* 774 */     this.headStop = new float[length + 1][tagNum][length + 1];
/* 775 */     this.rawDistance = new int[length + 1][length + 1];
/* 776 */     this.binDistance = new int[length + 1][length + 1];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ScoredObject<Tree>> getKBestParses(int k)
/*     */   {
/* 787 */     throw new UnsupportedOperationException("Doesn't do k best yet");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ScoredObject<Tree>> getBestParses()
/*     */   {
/* 797 */     throw new UnsupportedOperationException("Doesn't do best parses yet");
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
/*     */   public List<ScoredObject<Tree>> getKGoodParses(int k)
/*     */   {
/* 811 */     throw new UnsupportedOperationException("Doesn't do k good yet");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ScoredObject<Tree>> getKSampledParses(int k)
/*     */   {
/* 822 */     throw new UnsupportedOperationException("Doesn't do k sampled yet");
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ExhaustiveDependencyParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */