/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.ling.Word;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.stats.Distribution;
/*     */ import edu.stanford.nlp.stats.GeneralizedCounter;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ChineseMarkovWordSegmenter implements WordSegmenter
/*     */ {
/*     */   private Distribution initialPOSDist;
/*     */   private Map markovPOSDists;
/*     */   private ChineseCharacterBasedLexicon lex;
/*     */   private Set POSes;
/*     */   private static final long serialVersionUID = 1559606198270645508L;
/*     */   
/*     */   public ChineseMarkovWordSegmenter(ChineseCharacterBasedLexicon lex)
/*     */   {
/*  29 */     this.lex = lex;
/*     */   }
/*     */   
/*     */   public ChineseMarkovWordSegmenter() {
/*  33 */     this.lex = new ChineseCharacterBasedLexicon();
/*     */   }
/*     */   
/*     */   public void train(Collection<Tree> trees) {
/*  37 */     Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
/*  38 */     this.lex.train(trees);
/*  39 */     Counter initial = new Counter();
/*  40 */     GeneralizedCounter ruleCounter = new GeneralizedCounter(2);
/*  41 */     for (Tree tree : trees) {
/*  42 */       List<Label> tags = tree.preTerminalYield();
/*  43 */       last = null;
/*  44 */       for (Label tagLabel : tags) {
/*  45 */         String tag = tagLabel.value();
/*  46 */         tagNumberer.number(tag);
/*  47 */         if (last == null) {
/*  48 */           initial.incrementCount(tag);
/*     */         } else {
/*  50 */           ruleCounter.incrementCount2D(last, tag);
/*     */         }
/*  52 */         last = tag;
/*     */       } }
/*     */     String last;
/*  55 */     int numTags = tagNumberer.total();
/*  56 */     this.POSes = new java.util.HashSet(tagNumberer.objects());
/*  57 */     this.initialPOSDist = Distribution.laplaceSmoothedDistribution(initial, numTags, 0.5D);
/*  58 */     this.markovPOSDists = new java.util.HashMap();
/*     */     
/*  60 */     Set entries = ruleCounter.lowestLevelCounterEntrySet();
/*  61 */     for (Iterator iter = entries.iterator(); iter.hasNext();) {
/*  62 */       Map.Entry entry = (Map.Entry)iter.next();
/*     */       
/*  64 */       Distribution d = Distribution.laplaceSmoothedDistribution((Counter)entry.getValue(), numTags, 0.5D);
/*  65 */       this.markovPOSDists.put(((List)entry.getKey()).get(0), d);
/*     */     }
/*     */   }
/*     */   
/*     */   public Sentence segmentWords(String s) {
/*  70 */     return segmentWordsWithMarkov(s);
/*     */   }
/*     */   
/*     */   private Sentence basicSegmentWords(String s)
/*     */   {
/*  75 */     Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
/*  76 */     int length = s.length();
/*     */     
/*     */ 
/*  79 */     double[][] scores = new double[length][length + 1];
/*     */     
/*  81 */     int[][] splitBacktrace = new int[length][length + 1];
/*     */     
/*  83 */     int[][] POSbacktrace = new int[length][length + 1];
/*  84 */     for (int i = 0; i < length; i++) {
/*  85 */       java.util.Arrays.fill(scores[i], Double.NEGATIVE_INFINITY);
/*     */     }
/*     */     
/*  88 */     for (int diff = 1; diff <= 10; diff++) { int end;
/*  89 */       String word; Iterator iter; for (int start = 0; start + diff <= length; start++) {
/*  90 */         end = start + diff;
/*  91 */         StringBuilder wordBuf = new StringBuilder();
/*  92 */         for (int pos = start; pos < end; pos++) {
/*  93 */           wordBuf.append(s.charAt(pos));
/*     */         }
/*  95 */         word = wordBuf.toString();
/*     */         
/*  97 */         for (iter = this.POSes.iterator(); iter.hasNext();) {
/*  98 */           String tag = (String)iter.next();
/*  99 */           IntTaggedWord itw = new IntTaggedWord(word, tag);
/* 100 */           double newScore = this.lex.score(itw, 0) + Math.log(this.lex.getPOSDistribution().probabilityOf(tag));
/* 101 */           if (newScore > scores[start][end]) {
/* 102 */             scores[start][end] = newScore;
/* 103 */             splitBacktrace[start][end] = end;
/* 104 */             POSbacktrace[start][end] = itw.tag();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 110 */     for (int diff = 2; diff <= length; diff++) {
/* 111 */       for (int start = 0; start + diff <= length; start++) {
/* 112 */         int end = start + diff;
/* 113 */         for (int split = start + 1; (split < end) && (split - start <= 10); split++) {
/* 114 */           if (splitBacktrace[start][split] == split)
/*     */           {
/*     */ 
/* 117 */             double newScore = scores[start][split] + scores[split][end];
/* 118 */             if (newScore > scores[start][end]) {
/* 119 */               scores[start][end] = newScore;
/* 120 */               splitBacktrace[start][end] = split;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 126 */     List words = new java.util.ArrayList();
/* 127 */     int start = 0;
/* 128 */     while (start < length) {
/* 129 */       int end = splitBacktrace[start][length];
/* 130 */       StringBuffer wordBuf = new StringBuffer();
/* 131 */       for (int pos = start; pos < end; pos++) {
/* 132 */         wordBuf.append(s.charAt(pos));
/*     */       }
/* 134 */       String word = wordBuf.toString();
/* 135 */       String tag = (String)tagNumberer.object(POSbacktrace[start][end]);
/*     */       
/* 137 */       words.add(new edu.stanford.nlp.ling.TaggedWord(word, tag));
/* 138 */       start = end;
/*     */     }
/*     */     
/* 141 */     return new Sentence(words);
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
/*     */   private Sentence<Word> segmentWordsWithMarkov(String s)
/*     */   {
/* 158 */     Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
/* 159 */     int length = s.length();
/*     */     
/* 161 */     int numTags = this.POSes.size();
/*     */     
/* 163 */     double[][][] scores = new double[length][length + 1][numTags];
/*     */     
/* 165 */     int[][][] splitBacktrace = new int[length][length + 1][numTags];
/*     */     
/* 167 */     int[][][] POSbacktrace = new int[length][length + 1][numTags];
/* 168 */     for (int i = 0; i < length; i++) {
/* 169 */       for (int j = 0; j < length + 1; j++) {
/* 170 */         java.util.Arrays.fill(scores[i][j], Double.NEGATIVE_INFINITY);
/*     */       }
/*     */     }
/*     */     
/* 174 */     for (int diff = 1; diff <= 10; diff++) { int end;
/* 175 */       String word; Iterator iter; for (int start = 0; start + diff <= length; start++) {
/* 176 */         end = start + diff;
/* 177 */         StringBuilder wordBuf = new StringBuilder();
/* 178 */         for (int pos = start; pos < end; pos++) {
/* 179 */           wordBuf.append(s.charAt(pos));
/*     */         }
/* 181 */         word = wordBuf.toString();
/*     */         
/* 183 */         for (iter = this.POSes.iterator(); iter.hasNext();) {
/* 184 */           String tag = (String)iter.next();
/* 185 */           IntTaggedWord itw = new IntTaggedWord(word, tag);
/* 186 */           double score = this.lex.score(itw, 0);
/* 187 */           if (start == 0) {
/* 188 */             score += Math.log(this.initialPOSDist.probabilityOf(tag));
/*     */           }
/* 190 */           scores[start][end][itw.tag()] = score;
/* 191 */           splitBacktrace[start][end][itw.tag()] = end;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 196 */     for (int diff = 2; diff <= length; diff++) {
/* 197 */       for (int start = 0; start + diff <= length; start++) {
/* 198 */         int end = start + diff;
/* 199 */         Iterator iter; int tagNum; Distribution rTagDist; Iterator iter2; for (int split = start + 1; (split < end) && (split - start <= 10); split++)
/*     */         {
/* 201 */           for (iter = this.POSes.iterator(); iter.hasNext();) {
/* 202 */             String tag = (String)iter.next();
/* 203 */             tagNum = tagNumberer.number(tag);
/* 204 */             if (splitBacktrace[start][split][tagNum] == split)
/*     */             {
/*     */ 
/* 207 */               rTagDist = (Distribution)this.markovPOSDists.get(tag);
/* 208 */               if (rTagDist != null)
/*     */               {
/*     */ 
/*     */ 
/* 212 */                 for (iter2 = this.POSes.iterator(); iter2.hasNext();) {
/* 213 */                   String rTag = (String)iter2.next();
/* 214 */                   int rTagNum = tagNumberer.number(rTag);
/* 215 */                   double newScore = scores[start][split][tagNum] + scores[split][end][rTagNum] + Math.log(rTagDist.probabilityOf(rTag));
/* 216 */                   if (newScore > scores[start][end][tagNum]) {
/* 217 */                     scores[start][end][tagNum] = newScore;
/* 218 */                     splitBacktrace[start][end][tagNum] = split;
/* 219 */                     POSbacktrace[start][end][tagNum] = rTagNum;
/*     */                   }
/*     */                 } }
/*     */             }
/*     */           } }
/*     */       }
/*     */     }
/* 226 */     int nextPOS = edu.stanford.nlp.math.ArrayMath.argmax(scores[0][length]);
/* 227 */     Sentence<Word> words = new Sentence();
/*     */     
/* 229 */     int start = 0;
/* 230 */     while (start < length) {
/* 231 */       int split = splitBacktrace[start][length][nextPOS];
/* 232 */       StringBuilder wordBuf = new StringBuilder();
/* 233 */       for (int i = start; i < split; i++) {
/* 234 */         wordBuf.append(s.charAt(i));
/*     */       }
/* 236 */       String word = wordBuf.toString();
/*     */       
/*     */ 
/* 239 */       words.add(new Word(word));
/* 240 */       if (split < length) {
/* 241 */         nextPOS = POSbacktrace[start][length][nextPOS];
/*     */       }
/* 243 */       start = split;
/*     */     }
/*     */     
/* 246 */     return words;
/*     */   }
/*     */   
/*     */   private Distribution getSegmentedWordLengthDistribution(Treebank tb) {
/* 250 */     edu.stanford.nlp.trees.international.pennchinese.CharacterLevelTagExtender ext = new edu.stanford.nlp.trees.international.pennchinese.CharacterLevelTagExtender();
/* 251 */     Counter c = new Counter();
/* 252 */     for (Iterator iterator = tb.iterator(); iterator.hasNext();) {
/* 253 */       Tree gold = (Tree)iterator.next();
/* 254 */       StringBuffer goldChars = new StringBuffer();
/* 255 */       Sentence goldYield = gold.yield();
/* 256 */       for (Iterator wordIter = goldYield.iterator(); wordIter.hasNext();) {
/* 257 */         Word word = (Word)wordIter.next();
/* 258 */         goldChars.append(word);
/*     */       }
/* 260 */       Sentence ourWords = segmentWords(goldChars.toString());
/* 261 */       for (int i = 0; i < ourWords.size(); i++) {
/* 262 */         c.incrementCount(new Integer(ourWords.get(i).toString().length()));
/*     */       }
/*     */     }
/* 265 */     Distribution wordLengthDist = Distribution.getDistribution(c);
/* 266 */     return wordLengthDist;
/*     */   }
/*     */   
/*     */   public void loadSegmenter(String filename) {
/* 270 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ChineseMarkovWordSegmenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */