/*      */ package edu.stanford.nlp.parser.lexparser;
/*      */ 
/*      */ import edu.stanford.nlp.io.NumberRangesFileFilter;
/*      */ import edu.stanford.nlp.ling.LabeledWord;
/*      */ import edu.stanford.nlp.ling.TaggedWord;
/*      */ import edu.stanford.nlp.stats.Counter;
/*      */ import edu.stanford.nlp.trees.DiskTreebank;
/*      */ import edu.stanford.nlp.trees.Tree;
/*      */ import edu.stanford.nlp.trees.Treebank;
/*      */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*      */ import edu.stanford.nlp.util.Numberer;
/*      */ import edu.stanford.nlp.util.StringUtils;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Writer;
/*      */ import java.text.NumberFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BaseLexicon
/*      */   implements Lexicon
/*      */ {
/*      */   protected static final boolean DEBUG_LEXICON = false;
/*      */   protected static final boolean DEBUG_LEXICON_SCORE = false;
/*      */   private static final boolean DOCUMENT_UNKNOWNS = false;
/*      */   protected static final int nullWord = -1;
/*      */   protected static final short nullTag = -1;
/*      */   protected int unknownLevel;
/*      */   protected int smoothInUnknownsThreshold;
/*      */   protected boolean smartMutation;
/*   58 */   protected int unknownSuffixSize = 0;
/*   59 */   protected int unknownPrefixSize = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public transient List<IntTaggedWord>[] rulesWithWord;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   72 */   protected transient Set<IntTaggedWord> tags = new HashSet();
/*      */   
/*   74 */   protected transient Set<IntTaggedWord> words = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   81 */   public Counter<IntTaggedWord> seenCounter = new Counter();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   88 */   protected Counter<IntTaggedWord> unSeenCounter = new Counter();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   96 */   protected transient int lastSignatureIndex = -1;
/*      */   
/*   98 */   protected transient int lastSentencePosition = -1;
/*      */   
/*  100 */   protected transient int lastWordToSignaturize = -1;
/*      */   
/*  102 */   double[] smooth = { 1.0D, 1.0D };
/*      */   
/*      */ 
/*  105 */   transient double[][] m_TT = (double[][])null;
/*      */   
/*  107 */   transient double[] m_T = null;
/*      */   
/*      */   private static final int MIN_UNKNOWN = 0;
/*      */   
/*      */   private static final int MAX_UNKNOWN = 9;
/*      */   private boolean flexiTag;
/*      */   
/*      */   public BaseLexicon()
/*      */   {
/*  116 */     this(new Options.LexOptions());
/*      */   }
/*      */   
/*      */   public BaseLexicon(Options.LexOptions op) {
/*  120 */     this.flexiTag = op.flexiTag;
/*  121 */     this.unknownLevel = op.useUnknownWordSignatures;
/*  122 */     if ((this.unknownLevel < 0) || (this.unknownLevel > 9)) {
/*  123 */       if (this.unknownLevel < 0) {
/*  124 */         this.unknownLevel = 0;
/*  125 */       } else if (this.unknownLevel > 9) {
/*  126 */         this.unknownLevel = 9;
/*      */       }
/*  128 */       System.err.println("Invalid value for useUnknownWordSignatures");
/*      */     }
/*  130 */     this.smoothInUnknownsThreshold = op.smoothInUnknownsThreshold;
/*  131 */     this.smartMutation = op.smartMutation;
/*  132 */     this.unknownSuffixSize = op.unknownSuffixSize;
/*  133 */     this.unknownPrefixSize = op.unknownPrefixSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isKnown(int word)
/*      */   {
/*  144 */     if (this.rulesWithWord == null) {
/*  145 */       initRulesWithWord();
/*      */     }
/*  147 */     return (word < this.rulesWithWord.length) && (!this.rulesWithWord[word].isEmpty());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isKnown(String word)
/*      */   {
/*  160 */     IntTaggedWord iW = new IntTaggedWord(wordNumberer().number(word), -1);
/*  161 */     return this.seenCounter.getCount(iW) > 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<IntTaggedWord> ruleIteratorByWord(String word, int loc)
/*      */   {
/*  178 */     return ruleIteratorByWord(wordNumberer().number(word), loc);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Iterator<IntTaggedWord> ruleIteratorByWord(int word, int loc)
/*      */   {
/*      */     List<IntTaggedWord> wordTaggings;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     List<IntTaggedWord> wordTaggings;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  197 */     if (isKnown(word)) { List<IntTaggedWord> wordTaggings;
/*  198 */       if (!this.flexiTag)
/*      */       {
/*  200 */         wordTaggings = this.rulesWithWord[word];
/*      */       }
/*      */       else
/*      */       {
/*  204 */         IntTaggedWord iW = new IntTaggedWord(word, -1);
/*  205 */         if (this.seenCounter.getCount(iW) > this.smoothInUnknownsThreshold) {
/*  206 */           return this.rulesWithWord[word].iterator();
/*      */         }
/*      */         
/*  209 */         wordTaggings = new ArrayList(40);
/*  210 */         for (IntTaggedWord iTW2 : this.tags) {
/*  211 */           IntTaggedWord iTW = new IntTaggedWord(word, iTW2.tag);
/*  212 */           if (score(iTW, loc) > Float.NEGATIVE_INFINITY) {
/*  213 */             wordTaggings.add(iTW);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  220 */       wordTaggings = new ArrayList(40);
/*  221 */       for (IntTaggedWord iTW : this.rulesWithWord[this.wordNumberer.number("UNK")]) {
/*  222 */         wordTaggings.add(new IntTaggedWord(word, iTW.tag));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  231 */     return wordTaggings.iterator();
/*      */   }
/*      */   
/*      */   protected void initRulesWithWord() {
/*  235 */     if (Test.verbose) {
/*  236 */       System.err.print("\nInitializing lexicon scores ... ");
/*      */     }
/*      */     
/*  239 */     int unkWord = wordNumberer().number("UNK");
/*  240 */     int numWords = wordNumberer().total();
/*  241 */     this.rulesWithWord = new List[numWords];
/*  242 */     for (int w = 0; w < numWords; w++) {
/*  243 */       this.rulesWithWord[w] = new ArrayList(1);
/*      */     }
/*      */     
/*      */ 
/*  247 */     this.tags = new HashSet();
/*  248 */     for (IntTaggedWord iTW : this.seenCounter.keySet()) {
/*  249 */       if ((iTW.word() == -1) && (iTW.tag() != -1)) {
/*  250 */         this.tags.add(iTW);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  259 */     for (IntTaggedWord iT : this.tags) {
/*  260 */       double types = this.unSeenCounter.getCount(iT);
/*  261 */       if (types > Train.openClassTypesThreshold)
/*      */       {
/*  263 */         IntTaggedWord iTW = new IntTaggedWord(unkWord, iT.tag);
/*  264 */         this.rulesWithWord[iTW.word].add(iTW);
/*      */       }
/*      */     }
/*  267 */     if (Test.verbose) {
/*  268 */       System.err.print("The " + this.rulesWithWord[unkWord].size() + " open class tags are: [");
/*  269 */       for (IntTaggedWord item : this.rulesWithWord[unkWord]) {
/*  270 */         System.err.print(" " + tagNumberer().object(item.tag()));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  276 */       System.err.println(" ] ");
/*      */     }
/*      */     
/*  279 */     for (IntTaggedWord iTW : this.seenCounter.keySet()) {
/*  280 */       if ((iTW.tag() != -1) && (iTW.word() != -1)) {
/*  281 */         this.rulesWithWord[iTW.word].add(iTW);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected List<IntTaggedWord> treeToEvents(Tree tree, boolean keepTagsAsLabels)
/*      */   {
/*  288 */     if (!keepTagsAsLabels) return treeToEvents(tree);
/*  289 */     List<LabeledWord> labeledWords = tree.labeledYield();
/*      */     
/*      */ 
/*      */ 
/*  293 */     return listOfLabeledWordsToEvents(labeledWords);
/*      */   }
/*      */   
/*      */   protected List<IntTaggedWord> treeToEvents(Tree tree) {
/*  297 */     List<TaggedWord> taggedWords = tree.taggedYield();
/*  298 */     return listToEvents(taggedWords);
/*      */   }
/*      */   
/*      */   protected List<IntTaggedWord> listToEvents(List<TaggedWord> taggedWords) {
/*  302 */     List<IntTaggedWord> itwList = new ArrayList();
/*  303 */     for (TaggedWord tw : taggedWords) {
/*  304 */       IntTaggedWord iTW = new IntTaggedWord(wordNumberer().number(tw.word()), tagNumberer().number(tw.tag()));
/*      */       
/*  306 */       itwList.add(iTW);
/*      */     }
/*  308 */     return itwList;
/*      */   }
/*      */   
/*      */   protected List<IntTaggedWord> listOfLabeledWordsToEvents(List<LabeledWord> taggedWords) {
/*  312 */     List<IntTaggedWord> itwList = new ArrayList();
/*  313 */     for (LabeledWord tw : taggedWords) {
/*  314 */       IntTaggedWord iTW = new IntTaggedWord(wordNumberer().number(tw.word()), tagNumberer().number(tw.tag()));
/*      */       
/*  316 */       itwList.add(iTW);
/*      */     }
/*  318 */     return itwList;
/*      */   }
/*      */   
/*      */   public void addAll(List<TaggedWord> tagWords) {
/*  322 */     addAll(tagWords, 1.0D);
/*      */   }
/*      */   
/*      */   public void addAll(List<TaggedWord> taggedWords, double weight) {
/*  326 */     List<IntTaggedWord> tagWords = listToEvents(taggedWords);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void trainWithExpansion(Collection<TaggedWord> taggedWords) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void train(Collection<Tree> trees)
/*      */   {
/*  337 */     train(trees, 1.0D, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void train(Collection<Tree> trees, boolean keepTagsAsLabels)
/*      */   {
/*  344 */     train(trees, 1.0D, keepTagsAsLabels);
/*      */   }
/*      */   
/*      */   public void train(Collection<Tree> trees, double weight) {
/*  348 */     train(trees, weight, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void train(Collection<Tree> trees, double weight, boolean keepTagsAsLabels)
/*      */   {
/*  356 */     int tNum = 0;
/*  357 */     int tSize = trees.size();
/*  358 */     int indexToStartUnkCounting = (int)(tSize * Train.fractionBeforeUnseenCounting);
/*  359 */     Numberer wordNumberer = wordNumberer();
/*  360 */     Numberer tagNumberer = tagNumberer();
/*  361 */     int unkNum = wordNumberer.number("UNK");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  367 */     for (Tree tree : trees) {
/*  368 */       tNum++;
/*  369 */       List<IntTaggedWord> taggedWords = treeToEvents(tree, keepTagsAsLabels);
/*  370 */       int w = 0; for (int sz = taggedWords.size(); w < sz; w++) {
/*  371 */         IntTaggedWord iTW = (IntTaggedWord)taggedWords.get(w);
/*  372 */         this.seenCounter.incrementCount(iTW, weight);
/*  373 */         IntTaggedWord iT = new IntTaggedWord(-1, iTW.tag);
/*  374 */         this.seenCounter.incrementCount(iT, weight);
/*  375 */         IntTaggedWord iW = new IntTaggedWord(iTW.word, -1);
/*  376 */         this.seenCounter.incrementCount(iW, weight);
/*  377 */         IntTaggedWord i = new IntTaggedWord(-1, -1);
/*  378 */         this.seenCounter.incrementCount(i, weight);
/*      */         
/*  380 */         this.tags.add(iT);
/*  381 */         this.words.add(iW);
/*  382 */         if (tNum > indexToStartUnkCounting)
/*      */         {
/*  384 */           if (this.seenCounter.getCount(iW) < 2.0D)
/*      */           {
/*  386 */             int s = getSignatureIndex(iTW.word, w);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  395 */             IntTaggedWord iTS = new IntTaggedWord(s, iTW.tag);
/*  396 */             IntTaggedWord iS = new IntTaggedWord(s, -1);
/*  397 */             this.unSeenCounter.incrementCount(iTS, weight);
/*  398 */             this.unSeenCounter.incrementCount(iT, weight);
/*  399 */             this.unSeenCounter.incrementCount(iS, weight);
/*  400 */             this.unSeenCounter.incrementCount(i, weight);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  414 */     if (this.unSeenCounter.isEmpty()) {
/*  415 */       int numTags = tagNumberer().total();
/*  416 */       for (int tt = 0; tt < numTags; tt++) {
/*  417 */         if (!".$$.".equals(tagNumberer().object(tt))) {
/*  418 */           IntTaggedWord iT = new IntTaggedWord(-1, tt);
/*  419 */           IntTaggedWord i = new IntTaggedWord(-1, -1);
/*  420 */           this.unSeenCounter.incrementCount(iT, weight);
/*  421 */           this.unSeenCounter.incrementCount(i, weight);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  429 */     tune(trees);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void addTagging(boolean seen, IntTaggedWord itw, double count)
/*      */   {
/*  439 */     if (seen) {
/*  440 */       this.seenCounter.incrementCount(itw, count);
/*  441 */       if (itw.tag() == -1) {
/*  442 */         this.words.add(itw);
/*  443 */       } else if (itw.word() == -1) {
/*  444 */         this.tags.add(itw);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  449 */       this.unSeenCounter.incrementCount(itw, count);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected int getSignatureIndex(int wordIndex, int sentencePosition)
/*      */   {
/*  462 */     if ((wordIndex == this.lastWordToSignaturize) && (sentencePosition == this.lastSentencePosition))
/*      */     {
/*      */ 
/*  465 */       return this.lastSignatureIndex;
/*      */     }
/*  467 */     String uwSig = getSignature((String)wordNumberer().object(wordIndex), sentencePosition);
/*  468 */     int sig = wordNumberer().number(uwSig);
/*  469 */     this.lastSignatureIndex = sig;
/*  470 */     this.lastSentencePosition = sentencePosition;
/*  471 */     this.lastWordToSignaturize = wordIndex;
/*  472 */     return sig;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getSignature(String word, int loc)
/*      */   {
/*  499 */     StringBuilder sb = new StringBuilder("UNK");
/*  500 */     switch (this.unknownLevel)
/*      */     {
/*      */ 
/*      */     case 9: 
/*  504 */       boolean allDigitPlus = ArabicUnknownWordSignatures.allDigitPlus(word);
/*  505 */       int leng = word.length();
/*  506 */       if (allDigitPlus) {
/*  507 */         sb.append("-NUM");
/*  508 */       } else if ((word.startsWith("Al")) || (word.startsWith("ال"))) {
/*  509 */         sb.append("-Al");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*  515 */       else if (this.unknownPrefixSize > 0) {
/*  516 */         int min = leng < this.unknownPrefixSize ? leng : this.unknownPrefixSize;
/*  517 */         sb.append("-").append(word.substring(0, min));
/*      */       }
/*      */       
/*      */ 
/*  521 */       sb.append(ArabicUnknownWordSignatures.likelyAdjectivalSuffix(word));
/*  522 */       sb.append(ArabicUnknownWordSignatures.pastTenseVerbNumberSuffix(word));
/*  523 */       sb.append(ArabicUnknownWordSignatures.presentTenseVerbNumberSuffix(word));
/*  524 */       String ans = ArabicUnknownWordSignatures.abstractionNounSuffix(word);
/*  525 */       if (!"".equals(ans)) {
/*  526 */         sb.append(ans);
/*      */       } else {
/*  528 */         sb.append(ArabicUnknownWordSignatures.taaMarbuuTaSuffix(word));
/*      */       }
/*  530 */       if ((this.unknownSuffixSize > 0) && (!allDigitPlus)) {
/*  531 */         int min = leng < this.unknownSuffixSize ? leng : this.unknownSuffixSize;
/*  532 */         sb.append("-").append(word.substring(word.length() - min)); }
/*  533 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 8: 
/*  539 */       if (word.startsWith("Al")) {
/*  540 */         sb.append("-Al");
/*      */       }
/*  542 */       boolean allDigitPlus = ArabicUnknownWordSignatures.allDigitPlus(word);
/*  543 */       if (allDigitPlus) {
/*  544 */         sb.append("-NUM");
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*  550 */         sb.append("-").append(word.charAt(0));
/*      */       }
/*  552 */       sb.append(ArabicUnknownWordSignatures.likelyAdjectivalSuffix(word));
/*  553 */       sb.append(ArabicUnknownWordSignatures.pastTenseVerbNumberSuffix(word));
/*  554 */       sb.append(ArabicUnknownWordSignatures.presentTenseVerbNumberSuffix(word));
/*  555 */       sb.append(ArabicUnknownWordSignatures.taaMarbuuTaSuffix(word));
/*  556 */       sb.append(ArabicUnknownWordSignatures.abstractionNounSuffix(word));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 7: 
/*  562 */       boolean allDigitPlus = ArabicUnknownWordSignatures.allDigitPlus(word);
/*  563 */       if (allDigitPlus) {
/*  564 */         sb.append("-NUM");
/*      */       } else {
/*  566 */         sb.append(word.charAt(word.length() - 1));
/*      */       }
/*  568 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 6: 
/*  575 */       if (word.startsWith("Al")) {
/*  576 */         sb.append("-Al");
/*      */       }
/*  578 */       boolean allDigitPlus = ArabicUnknownWordSignatures.allDigitPlus(word);
/*  579 */       if (allDigitPlus) {
/*  580 */         sb.append("-NUM");
/*      */       } else {
/*  582 */         sb.append(word.charAt(word.length() - 1));
/*      */       }
/*  584 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 5: 
/*  594 */       int wlen = word.length();
/*  595 */       int numCaps = 0;
/*  596 */       boolean hasDigit = false;
/*  597 */       boolean hasDash = false;
/*  598 */       boolean hasLower = false;
/*  599 */       for (int i = 0; i < wlen; i++) {
/*  600 */         char ch = word.charAt(i);
/*  601 */         if (Character.isDigit(ch)) {
/*  602 */           hasDigit = true;
/*  603 */         } else if (ch == '-') {
/*  604 */           hasDash = true;
/*  605 */         } else if (Character.isLetter(ch)) {
/*  606 */           if (Character.isLowerCase(ch)) {
/*  607 */             hasLower = true;
/*  608 */           } else if (Character.isTitleCase(ch)) {
/*  609 */             hasLower = true;
/*  610 */             numCaps++;
/*      */           } else {
/*  612 */             numCaps++;
/*      */           }
/*      */         }
/*      */       }
/*  616 */       char ch0 = word.charAt(0);
/*  617 */       String lowered = word.toLowerCase();
/*  618 */       if ((Character.isUpperCase(ch0)) || (Character.isTitleCase(ch0))) {
/*  619 */         if ((loc == 0) && (numCaps == 1)) {
/*  620 */           sb.append("-INITC");
/*  621 */           if (isKnown(lowered)) {
/*  622 */             sb.append("-KNOWNLC");
/*      */           }
/*      */         } else {
/*  625 */           sb.append("-CAPS");
/*      */         }
/*  627 */       } else if ((!Character.isLetter(ch0)) && (numCaps > 0)) {
/*  628 */         sb.append("-CAPS");
/*  629 */       } else if (hasLower) {
/*  630 */         sb.append("-LC");
/*      */       }
/*  632 */       if (hasDigit) {
/*  633 */         sb.append("-NUM");
/*      */       }
/*  635 */       if (hasDash) {
/*  636 */         sb.append("-DASH");
/*      */       }
/*  638 */       if ((lowered.endsWith("s")) && (wlen >= 3))
/*      */       {
/*  640 */         char ch2 = lowered.charAt(wlen - 2);
/*      */         
/*  642 */         if ((ch2 != 's') && (ch2 != 'i') && (ch2 != 'u')) {
/*  643 */           sb.append("-s");
/*      */         }
/*  645 */       } else if ((word.length() >= 5) && (!hasDash) && ((!hasDigit) || (numCaps <= 0)))
/*      */       {
/*      */ 
/*  648 */         if (lowered.endsWith("ed")) {
/*  649 */           sb.append("-ed");
/*  650 */         } else if (lowered.endsWith("ing")) {
/*  651 */           sb.append("-ing");
/*  652 */         } else if (lowered.endsWith("ion")) {
/*  653 */           sb.append("-ion");
/*  654 */         } else if (lowered.endsWith("er")) {
/*  655 */           sb.append("-er");
/*  656 */         } else if (lowered.endsWith("est")) {
/*  657 */           sb.append("-est");
/*  658 */         } else if (lowered.endsWith("ly")) {
/*  659 */           sb.append("-ly");
/*  660 */         } else if (lowered.endsWith("ity")) {
/*  661 */           sb.append("-ity");
/*  662 */         } else if (lowered.endsWith("y")) {
/*  663 */           sb.append("-y");
/*  664 */         } else if (lowered.endsWith("al")) {
/*  665 */           sb.append("-al");
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       break;
/*      */     case 4: 
/*  676 */       boolean hasDigit = false;
/*  677 */       boolean hasNonDigit = false;
/*  678 */       boolean hasLetter = false;
/*  679 */       boolean hasLower = false;
/*  680 */       boolean hasDash = false;
/*  681 */       boolean hasPeriod = false;
/*  682 */       boolean hasComma = false;
/*  683 */       for (int i = 0; i < word.length(); i++) {
/*  684 */         char ch = word.charAt(i);
/*  685 */         if (Character.isDigit(ch)) {
/*  686 */           hasDigit = true;
/*      */         } else {
/*  688 */           hasNonDigit = true;
/*  689 */           if (Character.isLetter(ch)) {
/*  690 */             hasLetter = true;
/*  691 */             if ((Character.isLowerCase(ch)) || (Character.isTitleCase(ch))) {
/*  692 */               hasLower = true;
/*      */             }
/*      */           }
/*  695 */           else if (ch == '-') {
/*  696 */             hasDash = true;
/*  697 */           } else if (ch == '.') {
/*  698 */             hasPeriod = true;
/*  699 */           } else if (ch == ',') {
/*  700 */             hasComma = true;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  706 */       if ((Character.isUpperCase(word.charAt(0))) || (Character.isTitleCase(word.charAt(0)))) {
/*  707 */         if (!hasLower) {
/*  708 */           sb.append("-AC");
/*  709 */         } else if (loc == 0) {
/*  710 */           sb.append("-SC");
/*      */         } else {
/*  712 */           sb.append("-C");
/*      */         }
/*  714 */       } else if (hasLower) {
/*  715 */         sb.append("-L");
/*  716 */       } else if (hasLetter) {
/*  717 */         sb.append("-U");
/*      */       }
/*      */       else {
/*  720 */         sb.append("-S");
/*      */       }
/*      */       
/*  723 */       if ((hasDigit) && (!hasNonDigit)) {
/*  724 */         sb.append("-N");
/*  725 */       } else if (hasDigit) {
/*  726 */         sb.append("-n");
/*      */       }
/*      */       
/*  729 */       if (hasDash) {
/*  730 */         sb.append("-H");
/*      */       }
/*  732 */       if (hasPeriod) {
/*  733 */         sb.append("-P");
/*      */       }
/*  735 */       if (hasComma) {
/*  736 */         sb.append("-C");
/*      */       }
/*  738 */       if (word.length() > 3)
/*      */       {
/*      */ 
/*  741 */         char ch = word.charAt(word.length() - 1);
/*  742 */         if (Character.isLetter(ch)) {
/*  743 */           sb.append("-");
/*  744 */           sb.append(Character.toLowerCase(ch));
/*      */         } }
/*  746 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 3: 
/*  753 */       sb.append("-");
/*  754 */       char lastClass = '-';
/*      */       
/*  756 */       int num = 0;
/*  757 */       for (int i = 0; i < word.length(); i++) {
/*  758 */         char ch = word.charAt(i);
/*  759 */         char newClass; char newClass; if ((Character.isUpperCase(ch)) || (Character.isTitleCase(ch))) { char newClass;
/*  760 */           if (loc == 0) {
/*  761 */             newClass = 'S';
/*      */           } else
/*  763 */             newClass = 'L';
/*      */         } else { char newClass;
/*  765 */           if (Character.isLetter(ch)) {
/*  766 */             newClass = 'l'; } else { char newClass;
/*  767 */             if (Character.isDigit(ch)) {
/*  768 */               newClass = 'd'; } else { char newClass;
/*  769 */               if (ch == '-') {
/*  770 */                 newClass = 'h'; } else { char newClass;
/*  771 */                 if (ch == '.') {
/*  772 */                   newClass = 'p';
/*      */                 } else
/*  774 */                   newClass = 's';
/*      */               } } } }
/*  776 */         if (newClass != lastClass) {
/*  777 */           lastClass = newClass;
/*  778 */           sb.append(lastClass);
/*  779 */           num = 1;
/*      */         } else {
/*  781 */           if (num < 2) {
/*  782 */             sb.append('+');
/*      */           }
/*  784 */           num++;
/*      */         }
/*      */       }
/*  787 */       if (word.length() > 3)
/*      */       {
/*      */ 
/*  790 */         char ch = Character.toLowerCase(word.charAt(word.length() - 1));
/*  791 */         sb.append('-');
/*  792 */         sb.append(ch); }
/*  793 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     case 2: 
/*  802 */       boolean hasDigit = false;
/*  803 */       boolean hasNonDigit = false;
/*  804 */       boolean hasLower = false;
/*  805 */       int wlen = word.length();
/*  806 */       for (int i = 0; i < wlen; i++) {
/*  807 */         char ch = word.charAt(i);
/*  808 */         if (Character.isDigit(ch)) {
/*  809 */           hasDigit = true;
/*      */         } else {
/*  811 */           hasNonDigit = true;
/*  812 */           if ((Character.isLetter(ch)) && (
/*  813 */             (Character.isLowerCase(ch)) || (Character.isTitleCase(ch)))) {
/*  814 */             hasLower = true;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  819 */       if ((wlen > 0) && ((Character.isUpperCase(word.charAt(0))) || (Character.isTitleCase(word.charAt(0)))))
/*      */       {
/*  821 */         if (!hasLower) {
/*  822 */           sb.append("-ALLC");
/*  823 */         } else if (loc == 0) {
/*  824 */           sb.append("-INIT");
/*      */         } else {
/*  826 */           sb.append("-UC");
/*      */         }
/*  828 */       } else if (hasLower) {
/*  829 */         sb.append("-LC");
/*      */       }
/*      */       
/*  832 */       if (word.indexOf('-') >= 0) {
/*  833 */         sb.append("-DASH");
/*      */       }
/*  835 */       if (hasDigit) {
/*  836 */         if (!hasNonDigit) {
/*  837 */           sb.append("-NUM");
/*      */         } else {
/*  839 */           sb.append("-DIG");
/*      */         }
/*  841 */       } else if (wlen > 3)
/*      */       {
/*      */ 
/*  844 */         char ch = word.charAt(word.length() - 1);
/*  845 */         sb.append(Character.toLowerCase(ch)); }
/*  846 */       break;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     case 1: 
/*  852 */       sb.append("-");
/*  853 */       sb.append(word.substring(Math.max(word.length() - 2, 0), word.length()));
/*  854 */       sb.append("-");
/*  855 */       if (Character.isLowerCase(word.charAt(0))) {
/*  856 */         sb.append("LOWER");
/*      */       }
/*  858 */       else if (Character.isUpperCase(word.charAt(0))) {
/*  859 */         if (loc == 0) {
/*  860 */           sb.append("INIT");
/*      */         } else {
/*  862 */           sb.append("UPPER");
/*      */         }
/*      */       } else {
/*  865 */         sb.append("OTHER");
/*      */       }
/*      */       
/*      */       break;
/*      */     }
/*      */     
/*      */     
/*  872 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void buildPT_T()
/*      */   {
/*  881 */     int numTags = tagNumberer().total();
/*  882 */     this.m_TT = new double[numTags][numTags];
/*  883 */     this.m_T = new double[numTags];
/*  884 */     double[] tmp = new double[numTags];
/*  885 */     for (IntTaggedWord word : this.words) {
/*  886 */       IntTaggedWord iTW = new IntTaggedWord(word.word, -1);
/*  887 */       double tot = 0.0D;
/*  888 */       for (int t = 0; t < numTags; t++) {
/*  889 */         iTW.tag = ((short)t);
/*  890 */         tmp[t] = this.seenCounter.getCount(iTW);
/*  891 */         tot += tmp[t];
/*      */       }
/*  893 */       if (tot >= 10.0D)
/*      */       {
/*      */ 
/*  896 */         for (int t = 0; t < numTags; t++) {
/*  897 */           for (int t2 = 0; t2 < numTags; t2++) {
/*  898 */             if (tmp[t2] > 0.0D) {
/*  899 */               double c = tmp[t] / tot;
/*  900 */               this.m_T[t] += c;
/*  901 */               this.m_TT[t2][t] += c;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float score(IntTaggedWord iTW, int loc)
/*      */   {
/*  933 */     int word = iTW.word;
/*  934 */     short tag = iTW.tag;
/*      */     
/*  936 */     iTW.tag = -1;
/*  937 */     double c_W = this.seenCounter.getCount(iTW);
/*      */     
/*  939 */     iTW.tag = tag;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  953 */     boolean seen = c_W > 0.0D;
/*      */     double pb_W_T;
/*  955 */     double pb_W_T; if (seen)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  961 */       double c_TW = this.seenCounter.getCount(iTW);
/*      */       
/*  963 */       iTW.word = -1;
/*  964 */       double c_T = this.seenCounter.getCount(iTW);
/*  965 */       double c_Tunseen = this.unSeenCounter.getCount(iTW);
/*  966 */       iTW.tag = -1;
/*  967 */       double total = this.seenCounter.getCount(iTW);
/*  968 */       double totalUnseen = this.unSeenCounter.getCount(iTW);
/*  969 */       iTW.tag = tag;
/*  970 */       iTW.word = word;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  975 */       double p_T_U = c_Tunseen / totalUnseen;
/*      */       
/*      */ 
/*      */       double pb_T_W;
/*      */       
/*      */       double pb_T_W;
/*      */       
/*  982 */       if (c_W > this.smoothInUnknownsThreshold)
/*      */       {
/*  984 */         pb_T_W = c_TW / c_W;
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  989 */         if (this.smartMutation) {
/*  990 */           int numTags = tagNumberer().total();
/*  991 */           if ((this.m_TT == null) || (numTags != this.m_T.length)) {
/*  992 */             buildPT_T();
/*      */           }
/*  994 */           p_T_U *= 0.1D;
/*      */           
/*  996 */           for (int t = 0; t < numTags; t++) {
/*  997 */             IntTaggedWord iTW2 = new IntTaggedWord(word, t);
/*  998 */             double p_T_W2 = this.seenCounter.getCount(iTW2) / c_W;
/*  999 */             if (p_T_W2 > 0.0D)
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/* 1004 */               p_T_U += p_T_W2 * this.m_TT[tag][t] / this.m_T[t] * 0.9D;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1012 */         pb_T_W = (c_TW + this.smooth[1] * p_T_U) / (c_W + this.smooth[1]);
/*      */       }
/*      */       
/*      */ 
/* 1016 */       double p_T = c_T / total;
/* 1017 */       double p_W = c_W / total;
/* 1018 */       pb_W_T = Math.log(pb_T_W * p_W / p_T);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1046 */       int sig = getSignatureIndex(iTW.word, loc);
/*      */       
/* 1048 */       iTW.word = sig;
/* 1049 */       double c_TS = this.unSeenCounter.getCount(iTW);
/* 1050 */       iTW.tag = -1;
/* 1051 */       double c_S = this.unSeenCounter.getCount(iTW);
/* 1052 */       iTW.word = -1;
/* 1053 */       double c_U = this.unSeenCounter.getCount(iTW);
/* 1054 */       double total = this.seenCounter.getCount(iTW);
/* 1055 */       iTW.tag = tag;
/* 1056 */       double c_T = this.unSeenCounter.getCount(iTW);
/* 1057 */       double c_Tseen = this.seenCounter.getCount(iTW);
/* 1058 */       iTW.word = word;
/*      */       
/* 1060 */       double p_T_U = c_T / c_U;
/* 1061 */       if (this.unknownLevel == 0) {
/* 1062 */         c_TS = 0.0D;
/* 1063 */         c_S = 0.0D;
/*      */       }
/* 1065 */       double pb_T_S = (c_TS + this.smooth[0] * p_T_U) / (c_S + this.smooth[0]);
/*      */       
/* 1067 */       double p_T = c_Tseen / total;
/* 1068 */       double p_W = 1.0D / total;
/* 1069 */       pb_W_T = Math.log(pb_T_S * p_W / p_T);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1096 */     if (pb_W_T > -100.0D) {
/* 1097 */       return (float)pb_W_T;
/*      */     }
/* 1099 */     return Float.NEGATIVE_INFINITY;
/*      */   }
/*      */   
/* 1102 */   private transient int debugLastWord = -1;
/*      */   
/* 1104 */   private transient int debugLoc = -1;
/*      */   private transient StringBuilder debugProbs;
/*      */   private transient StringBuilder debugNoProbs;
/*      */   private transient String debugPrefix;
/*      */   private static final int STATS_BINS = 15;
/*      */   private static final long serialVersionUID = 40L;
/*      */   
/*      */   public void tune(Collection<Tree> trees)
/*      */   {
/* 1113 */     double bestScore = Double.NEGATIVE_INFINITY;
/* 1114 */     double[] bestSmooth = { 0.0D, 0.0D };
/* 1115 */     for (this.smooth[0] = 1.0D; this.smooth[0] <= 1.0D; this.smooth[0] *= 2.0D) {
/* 1116 */       for (this.smooth[1] = 0.2D; this.smooth[1] <= 0.2D; this.smooth[1] *= 2.0D)
/*      */       {
/*      */ 
/* 1119 */         double score = 0.0D;
/*      */         
/* 1121 */         if (Test.verbose) {
/* 1122 */           System.out.println("Tuning lexicon: s0 " + this.smooth[0] + " s1 " + this.smooth[1] + " is " + score + " " + trees.size() + " trees.");
/*      */         }
/*      */         
/* 1125 */         if (score > bestScore) {
/* 1126 */           System.arraycopy(this.smooth, 0, bestSmooth, 0, this.smooth.length);
/* 1127 */           bestScore = score;
/*      */         }
/*      */       }
/*      */     }
/* 1131 */     System.arraycopy(bestSmooth, 0, this.smooth, 0, bestSmooth.length);
/* 1132 */     if (this.smartMutation) {
/* 1133 */       this.smooth[0] = 8.0D;
/*      */       
/*      */ 
/* 1136 */       this.smooth[1] = 0.1D;
/*      */     }
/* 1138 */     if (Test.unseenSmooth > 0.0D) {
/* 1139 */       this.smooth[0] = Test.unseenSmooth;
/*      */     }
/* 1141 */     if (Test.verbose) {
/* 1142 */       System.out.println("Tuning selected smoothUnseen " + this.smooth[0] + " smoothSeen " + this.smooth[1] + " at " + bestScore);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readData(BufferedReader in)
/*      */     throws IOException
/*      */   {
/* 1152 */     String SEEN = "SEEN";
/*      */     
/* 1154 */     int lineNum = 1;
/*      */     
/*      */ 
/* 1157 */     String line = in.readLine();
/* 1158 */     Pattern p = Pattern.compile("^smooth\\[([0-9])\\] = (.*)$");
/* 1159 */     while ((line != null) && (line.length() > 0)) {
/*      */       try {
/* 1161 */         Matcher m = p.matcher(line);
/* 1162 */         if (m.matches()) {
/* 1163 */           int i = Integer.parseInt(m.group(1));
/* 1164 */           this.smooth[i] = Double.parseDouble(m.group(2));
/*      */         }
/*      */         else {
/* 1167 */           String[] fields = StringUtils.splitOnCharWithQuoting(line, ' ', '"', '\\');
/*      */           
/*      */ 
/* 1170 */           boolean seen = fields[3].equals("SEEN");
/* 1171 */           addTagging(seen, new IntTaggedWord(fields[2], fields[0]), Double.parseDouble(fields[4]));
/*      */         }
/*      */       } catch (RuntimeException e) {
/* 1174 */         throw new IOException("Error on line " + lineNum + ": " + line);
/*      */       }
/* 1176 */       lineNum++;
/* 1177 */       line = in.readLine();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeData(Writer w)
/*      */     throws IOException
/*      */   {
/* 1186 */     PrintWriter out = new PrintWriter(w);
/*      */     
/* 1188 */     for (IntTaggedWord itw : this.seenCounter.keySet()) {
/* 1189 */       out.println(itw.toLexicalEntry() + " SEEN " + this.seenCounter.getCount(itw));
/*      */     }
/* 1191 */     for (IntTaggedWord itw : this.unSeenCounter.keySet()) {
/* 1192 */       out.println(itw.toLexicalEntry() + " UNSEEN " + this.unSeenCounter.getCount(itw));
/*      */     }
/* 1194 */     for (int i = 0; i < this.smooth.length; i++) {
/* 1195 */       out.println("smooth[" + i + "] = " + this.smooth[i]);
/*      */     }
/* 1197 */     out.flush();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int numRules()
/*      */   {
/* 1204 */     if (this.rulesWithWord == null) {
/* 1205 */       initRulesWithWord();
/*      */     }
/* 1207 */     int accumulated = 0;
/* 1208 */     for (List<IntTaggedWord> lis : this.rulesWithWord) {
/* 1209 */       accumulated += lis.size();
/*      */     }
/* 1211 */     return accumulated;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void printLexStats()
/*      */   {
/* 1219 */     if (this.rulesWithWord == null) {
/* 1220 */       initRulesWithWord();
/*      */     }
/* 1222 */     System.out.println("BaseLexicon statistics");
/* 1223 */     System.out.println("unknownLevel is " + this.unknownLevel);
/*      */     
/* 1225 */     System.out.println("Sum of rulesWithWord: " + numRules());
/* 1226 */     System.out.println("Tags size: " + this.tags.size());
/* 1227 */     int wsize = this.words.size();
/* 1228 */     System.out.println("Words size: " + wsize);
/*      */     
/*      */ 
/* 1231 */     System.out.println("rulesWithWord length: " + this.rulesWithWord.length + " [should be sum of words + unknown sigs]");
/*      */     
/* 1233 */     int[] lengths = new int[15];
/* 1234 */     ArrayList[] wArr = new ArrayList[15];
/* 1235 */     for (int j = 0; j < 15; j++) {
/* 1236 */       wArr[j] = new ArrayList();
/*      */     }
/* 1238 */     for (int i = 0; i < this.rulesWithWord.length; i++) {
/* 1239 */       int num = this.rulesWithWord[i].size();
/* 1240 */       if (num > 14) {
/* 1241 */         num = 14;
/*      */       }
/* 1243 */       lengths[num] += 1;
/* 1244 */       if ((wsize <= 20) || (num >= 7)) {
/* 1245 */         wArr[num].add(wordNumberer().object(i));
/*      */       }
/*      */     }
/* 1248 */     System.out.println("Stats on how many taggings for how many words");
/* 1249 */     for (int j = 0; j < 15; j++) {
/* 1250 */       System.out.print(j + " taggings: " + lengths[j] + " words ");
/* 1251 */       if ((wsize <= 20) || (j >= 7)) {
/* 1252 */         System.out.print(wArr[j]);
/*      */       }
/* 1254 */       System.out.println();
/*      */     }
/* 1256 */     NumberFormat nf = NumberFormat.getNumberInstance();
/* 1257 */     nf.setMaximumFractionDigits(0);
/* 1258 */     System.out.println("Unseen counter: " + this.unSeenCounter.toString(nf));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double evaluateCoverage(Collection<Tree> trees, Set missingWords, Set missingTags, Set<IntTaggedWord> missingTW)
/*      */   {
/* 1271 */     List<IntTaggedWord> iTW1 = new ArrayList();
/* 1272 */     for (Tree t : trees) {
/* 1273 */       iTW1.addAll(treeToEvents(t));
/*      */     }
/*      */     
/* 1276 */     int total = 0;
/* 1277 */     int unseen = 0;
/*      */     
/* 1279 */     for (IntTaggedWord itw : iTW1) {
/* 1280 */       total++;
/* 1281 */       if (!this.words.contains(new IntTaggedWord(itw.word(), -1))) {
/* 1282 */         missingWords.add(Numberer.object("word", itw.word()));
/*      */       }
/* 1284 */       if (!this.tags.contains(new IntTaggedWord(-1, itw.tag()))) {
/* 1285 */         missingTags.add(Numberer.object("tag", itw.tag()));
/*      */       }
/*      */       
/* 1288 */       if (this.seenCounter.getCount(itw) == 0.0D) {
/* 1289 */         unseen++;
/* 1290 */         missingTW.add(itw);
/*      */       }
/*      */     }
/* 1293 */     return unseen / total;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 1298 */   int[] tagsToBaseTags = null;
/*      */   private transient Numberer tagNumberer;
/*      */   
/* 1301 */   public int getBaseTag(int tag, TreebankLanguagePack tlp) { if (this.tagsToBaseTags == null) {
/* 1302 */       populateTagsToBaseTags(tlp);
/*      */     }
/* 1304 */     return this.tagsToBaseTags[tag];
/*      */   }
/*      */   
/*      */   private void populateTagsToBaseTags(TreebankLanguagePack tlp) {
/* 1308 */     Numberer tagNumberer = tagNumberer();
/* 1309 */     int total = tagNumberer.total();
/* 1310 */     this.tagsToBaseTags = new int[total];
/* 1311 */     for (int i = 0; i < total; i++) {
/* 1312 */       String tag = (String)tagNumberer.object(i);
/* 1313 */       String baseTag = tlp.basicCategory(tag);
/* 1314 */       int j = tagNumberer.number(baseTag);
/* 1315 */       this.tagsToBaseTags[i] = j;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private transient Numberer wordNumberer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1330 */     if (args.length < 3) {
/* 1331 */       System.err.println("java BaseLexicon treebankPath fileRange unknownWordModel words*");
/* 1332 */       return;
/*      */     }
/* 1334 */     System.out.print("Training BaseLexicon from " + args[0] + " " + args[1] + " ... ");
/* 1335 */     Treebank tb = new DiskTreebank();
/* 1336 */     tb.loadPath(args[0], new NumberRangesFileFilter(args[1], true));
/* 1337 */     BaseLexicon lex = new BaseLexicon();
/* 1338 */     lex.unknownLevel = Integer.parseInt(args[2]);
/* 1339 */     lex.train(tb);
/* 1340 */     System.out.println("done.");
/* 1341 */     System.out.println();
/* 1342 */     Numberer numb = Numberer.getGlobalNumberer("tags");
/* 1343 */     Numberer wNumb = Numberer.getGlobalNumberer("words");
/* 1344 */     NumberFormat nf = NumberFormat.getNumberInstance();
/* 1345 */     nf.setMaximumFractionDigits(4);
/* 1346 */     List<String> impos = new ArrayList();
/* 1347 */     for (int i = 3; i < args.length; i++) { Iterator<IntTaggedWord> it;
/* 1348 */       if (lex.isKnown(args[i])) {
/* 1349 */         System.out.println(args[i] + " is a known word.  Log probabilities [log P(w|t)] for its taggings are:");
/* 1350 */         for (it = lex.ruleIteratorByWord(wNumb.number(args[i]), i - 3); it.hasNext();) {
/* 1351 */           IntTaggedWord iTW = (IntTaggedWord)it.next();
/* 1352 */           System.out.println(StringUtils.pad(iTW, 24) + nf.format(lex.score(iTW, i - 3)));
/*      */         }
/*      */       } else {
/* 1355 */         String sig = lex.getSignature(args[i], i - 3);
/* 1356 */         System.out.println(args[i] + " is an unknown word.  Signature with uwm " + lex.unknownLevel + (i == 3 ? " init" : "non-init") + " is: " + sig);
/* 1357 */         Set<String> tags = numb.objects();
/* 1358 */         impos.clear();
/* 1359 */         List<String> lis = new ArrayList(tags);
/* 1360 */         Collections.sort(lis);
/* 1361 */         for (String tStr : lis) {
/* 1362 */           IntTaggedWord iTW = new IntTaggedWord(args[i], tStr);
/* 1363 */           double score = lex.score(iTW, 1);
/* 1364 */           if (score == Double.NEGATIVE_INFINITY) {
/* 1365 */             impos.add(tStr);
/*      */           } else {
/* 1367 */             System.out.println(StringUtils.pad(iTW, 24) + nf.format(score));
/*      */           }
/*      */         }
/* 1370 */         if (impos.size() > 0) {
/* 1371 */           System.out.println(args[i] + " impossible tags: " + impos);
/*      */         }
/*      */       }
/* 1374 */       System.out.println();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private Numberer tagNumberer()
/*      */   {
/* 1381 */     if (this.tagNumberer == null) {
/* 1382 */       this.tagNumberer = Numberer.getGlobalNumberer("tags");
/*      */     }
/* 1384 */     return this.tagNumberer;
/*      */   }
/*      */   
/*      */ 
/*      */   private Numberer wordNumberer()
/*      */   {
/* 1390 */     if (this.wordNumberer == null) {
/* 1391 */       this.wordNumberer = Numberer.getGlobalNumberer("words");
/*      */     }
/* 1393 */     return this.wordNumberer;
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\BaseLexicon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */