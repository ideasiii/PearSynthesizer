/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.ling.TaggedWord;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.stats.Distribution;
/*     */ import edu.stanford.nlp.stats.EquivalenceClassEval;
/*     */ import edu.stanford.nlp.stats.GeneralizedCounter;
/*     */ import edu.stanford.nlp.trees.MemoryTreebank;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeToBracketProcessor;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ChineseCharacterBasedLexicon implements Lexicon
/*     */ {
/*  26 */   public static PrintWriter pw = null;
/*     */   
/*  28 */   private static double lengthPenalty = 5.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  33 */   private static int penaltyType = 0;
/*     */   
/*     */   private Map<List, Distribution> charDistributions;
/*     */   
/*     */   private Set knownChars;
/*     */   
/*     */   private Distribution POSDistribution;
/*  40 */   private static boolean useUnknownCharModel = true;
/*     */   private static final int CONTEXT_LENGTH = 2;
/*     */   
/*     */   public static void printStats(Collection<Tree> trees)
/*     */   {
/*  45 */     Counter<Integer> wordLengthCounter = new Counter();
/*  46 */     Counter<TaggedWord> wordCounter = new Counter();
/*  47 */     Counter<Symbol> charCounter = new Counter();
/*  48 */     int counter = 0;
/*  49 */     for (Tree tree : trees) {
/*  50 */       counter++;
/*  51 */       List taggedWords = tree.taggedYield(new ArrayList());
/*  52 */       int i = 0; for (int size = taggedWords.size(); i < size; i++) {
/*  53 */         TaggedWord taggedWord = (TaggedWord)taggedWords.get(i);
/*  54 */         String word = taggedWord.word();
/*  55 */         if (!word.equals(".$."))
/*     */         {
/*     */ 
/*  58 */           wordCounter.incrementCount(taggedWord);
/*  59 */           wordLengthCounter.incrementCount(new Integer(word.length()));
/*  60 */           int j = 0; for (int length = word.length(); j < length; j++) {
/*  61 */             Symbol sym = Symbol.cannonicalSymbol(word.charAt(j));
/*  62 */             charCounter.incrementCount(sym);
/*     */           }
/*  64 */           charCounter.incrementCount(Symbol.END_WORD);
/*     */         }
/*     */       }
/*     */     }
/*  68 */     Set singletonChars = charCounter.keysBelow(1.5D);
/*  69 */     Set singletonWords = wordCounter.keysBelow(1.5D);
/*     */     
/*  71 */     Counter<String> singletonWordPOSes = new Counter();
/*  72 */     for (Iterator singletonWordIter = singletonWords.iterator(); singletonWordIter.hasNext();) {
/*  73 */       TaggedWord taggedWord = (TaggedWord)singletonWordIter.next();
/*  74 */       singletonWordPOSes.incrementCount(taggedWord.tag());
/*     */     }
/*  76 */     Distribution singletonWordPOSDist = Distribution.getDistribution(singletonWordPOSes);
/*     */     
/*  78 */     Counter singletonCharRads = new Counter();
/*  79 */     for (Iterator singletonCharIter = singletonChars.iterator(); singletonCharIter.hasNext();) {
/*  80 */       Symbol s = (Symbol)singletonCharIter.next();
/*  81 */       singletonCharRads.incrementCount(new Character(edu.stanford.nlp.trees.international.pennchinese.RadicalMap.getRadical(s.getCh())));
/*     */     }
/*  83 */     Distribution singletonCharRadDist = Distribution.getDistribution(singletonCharRads);
/*     */     
/*  85 */     Distribution wordLengthDist = Distribution.getDistribution(wordLengthCounter);
/*     */     
/*  87 */     NumberFormat percent = new java.text.DecimalFormat("##.##%");
/*     */     
/*  89 */     pw.println("There are " + singletonChars.size() + " singleton chars out of " + (int)charCounter.totalCount() + " tokens and " + charCounter.size() + " types found in " + counter + " trees.");
/*  90 */     pw.println("Thus singletonChars comprise " + percent.format(singletonChars.size() / charCounter.totalCount()) + " of tokens and " + percent.format(singletonChars.size() / charCounter.size()) + " of types.");
/*  91 */     pw.println();
/*  92 */     pw.println("There are " + singletonWords.size() + " singleton words out of " + (int)wordCounter.totalCount() + " tokens and " + wordCounter.size() + " types.");
/*  93 */     pw.println("Thus singletonWords comprise " + percent.format(singletonWords.size() / wordCounter.totalCount()) + " of tokens and " + percent.format(singletonWords.size() / wordCounter.size()) + " of types.");
/*  94 */     pw.println();
/*  95 */     pw.println("Distribution over singleton word POS:");
/*  96 */     pw.println(singletonWordPOSDist.toString());
/*  97 */     pw.println();
/*  98 */     pw.println("Distribution over singleton char radicals:");
/*  99 */     pw.println(singletonCharRadDist.toString());
/* 100 */     pw.println();
/* 101 */     pw.println("Distribution over word length:");
/* 102 */     pw.println(wordLengthDist);
/*     */   }
/*     */   
/*     */   public void train(Collection<Tree> trees) {
/* 106 */     edu.stanford.nlp.util.Numberer tagNumberer = edu.stanford.nlp.util.Numberer.getGlobalNumberer("tags");
/*     */     
/* 108 */     edu.stanford.nlp.util.Timing.tick("Counting characters...");
/* 109 */     Counter charCounter = new Counter();
/*     */     
/*     */ 
/* 112 */     for (Tree tree : trees) {
/* 113 */       List labels = tree.yield(new ArrayList());
/* 114 */       int i = 0; for (int size = labels.size(); i < size; i++) {
/* 115 */         String word = ((edu.stanford.nlp.ling.Label)labels.get(i)).value();
/* 116 */         if (!word.equals(".$."))
/*     */         {
/*     */ 
/* 119 */           int j = 0; for (int length = word.length(); j < length; j++) {
/* 120 */             Symbol sym = Symbol.cannonicalSymbol(word.charAt(j));
/* 121 */             charCounter.incrementCount(sym);
/*     */           }
/* 123 */           charCounter.incrementCount(Symbol.END_WORD);
/*     */         }
/*     */       }
/*     */     }
/* 127 */     Set singletons = charCounter.keysBelow(1.5D);
/* 128 */     this.knownChars = new java.util.HashSet(charCounter.keySet());
/*     */     
/* 130 */     edu.stanford.nlp.util.Timing.tick("Counting nGrams...");
/* 131 */     GeneralizedCounter[] POSspecificCharNGrams = new GeneralizedCounter[3];
/* 132 */     for (int i = 0; i <= 2; i++) {
/* 133 */       POSspecificCharNGrams[i] = new GeneralizedCounter(i + 2);
/*     */     }
/*     */     
/* 136 */     Counter POSCounter = new Counter();
/* 137 */     List context = new ArrayList(3);
/* 138 */     for (Iterator iterator = trees.iterator(); iterator.hasNext();) {
/* 139 */       Tree tree = (Tree)iterator.next();
/*     */       
/* 141 */       List words = tree.taggedYield();
/* 142 */       for (wordIter = words.iterator(); wordIter.hasNext();) {
/* 143 */         TaggedWord taggedWord = (TaggedWord)wordIter.next();
/* 144 */         String word = taggedWord.word();
/* 145 */         String tag = taggedWord.tag();
/* 146 */         tagNumberer.number(tag);
/* 147 */         if (!word.equals(".$."))
/*     */         {
/*     */ 
/* 150 */           POSCounter.incrementCount(tag);
/* 151 */           int i = 0; for (int size = word.length(); i <= size; i++)
/*     */           {
/* 153 */             Symbol unknownCharClass = null;
/* 154 */             context.clear();
/* 155 */             context.add(tag);
/* 156 */             Symbol sym; if (i < size) {
/* 157 */               char thisCh = word.charAt(i);
/* 158 */               Symbol sym = Symbol.cannonicalSymbol(thisCh);
/* 159 */               if (singletons.contains(sym)) {
/* 160 */                 unknownCharClass = unknownCharClass(sym);
/* 161 */                 charCounter.incrementCount(unknownCharClass);
/*     */               }
/*     */             } else {
/* 164 */               sym = Symbol.END_WORD;
/*     */             }
/* 166 */             POSspecificCharNGrams[0].incrementCount(context, sym);
/* 167 */             if (unknownCharClass != null) {
/* 168 */               POSspecificCharNGrams[0].incrementCount(context, unknownCharClass);
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 174 */             for (int j = 1; j <= 2; j++) {
/* 175 */               if (i - j < 0) {
/* 176 */                 context.add(Symbol.BEGIN_WORD);
/* 177 */                 POSspecificCharNGrams[j].incrementCount(context, sym);
/* 178 */                 if (unknownCharClass == null) break;
/* 179 */                 POSspecificCharNGrams[j].incrementCount(context, unknownCharClass); break;
/*     */               }
/*     */               
/*     */ 
/* 183 */               Symbol prev = Symbol.cannonicalSymbol(word.charAt(i - j));
/* 184 */               if (singletons.contains(prev)) {
/* 185 */                 context.add(unknownCharClass(prev));
/*     */               } else {
/* 187 */                 context.add(prev);
/*     */               }
/* 189 */               POSspecificCharNGrams[j].incrementCount(context, sym);
/* 190 */               if (unknownCharClass != null) {
/* 191 */                 POSspecificCharNGrams[j].incrementCount(context, unknownCharClass);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Iterator wordIter;
/* 199 */     this.POSDistribution = Distribution.getDistribution(POSCounter);
/* 200 */     edu.stanford.nlp.util.Timing.tick("Creating character prior distribution...");
/*     */     
/* 202 */     this.charDistributions = new java.util.HashMap();
/*     */     
/*     */ 
/* 205 */     int numberOfKeys = charCounter.size() + singletons.size();
/* 206 */     Distribution prior = Distribution.goodTuringSmoothedCounter(charCounter, numberOfKeys);
/* 207 */     this.charDistributions.put(java.util.Collections.EMPTY_LIST, prior);
/*     */     Iterator it;
/* 209 */     for (int i = 0; i <= 2; i++) {
/* 210 */       Set counterEntries = POSspecificCharNGrams[i].lowestLevelCounterEntrySet();
/* 211 */       edu.stanford.nlp.util.Timing.tick("Creating " + counterEntries.size() + " character " + (i + 1) + "-gram distributions...");
/* 212 */       for (it = counterEntries.iterator(); it.hasNext();) {
/* 213 */         java.util.Map.Entry<List, Counter> entry = (java.util.Map.Entry)it.next();
/* 214 */         context = (List)entry.getKey();
/* 215 */         Counter c = (Counter)entry.getValue();
/* 216 */         Distribution thisPrior = (Distribution)this.charDistributions.get(context.subList(0, context.size() - 1));
/* 217 */         double priorWeight = thisPrior.getNumberOfKeys() / 200;
/* 218 */         Distribution newDist = Distribution.dynamicCounterWithDirichletPrior(c, thisPrior, priorWeight);
/* 219 */         this.charDistributions.put(context, newDist);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Distribution getPOSDistribution() {
/* 225 */     return this.POSDistribution;
/*     */   }
/*     */   
/*     */   public static boolean isForeign(String s) {
/* 229 */     for (int i = 0; i < s.length(); i++) {
/* 230 */       int num = Character.getNumericValue(s.charAt(i));
/* 231 */       if ((num < 10) || (num > 35)) {
/* 232 */         return false;
/*     */       }
/*     */     }
/* 235 */     return true;
/*     */   }
/*     */   
/*     */   private Symbol unknownCharClass(Symbol ch) {
/* 239 */     if (useUnknownCharModel) {
/* 240 */       return new Symbol(Character.toString(edu.stanford.nlp.trees.international.pennchinese.RadicalMap.getRadical(ch.getCh()))).intern();
/*     */     }
/* 242 */     return Symbol.UNKNOWN;
/*     */   }
/*     */   
/*     */ 
/* 246 */   protected static NumberFormat formatter = new java.text.DecimalFormat("0.000");
/*     */   private static final long serialVersionUID = -5357655683145854069L;
/*     */   
/* 249 */   public float score(IntTaggedWord iTW, int loc) { TaggedWord tw = iTW.toTaggedWord();
/* 250 */     String word = tw.word();
/* 251 */     String tag = tw.tag();
/* 252 */     assert (!word.equals(".$."));
/* 253 */     char[] chars = word.toCharArray();
/* 254 */     List charList = new ArrayList(chars.length + 2 + 1);
/*     */     
/*     */ 
/*     */ 
/* 258 */     charList.add(Symbol.END_WORD);
/* 259 */     for (int i = chars.length - 1; i >= 0; i--) {
/* 260 */       Symbol ch = Symbol.cannonicalSymbol(chars[i]);
/* 261 */       if (this.knownChars.contains(ch)) {
/* 262 */         charList.add(ch);
/*     */       } else {
/* 264 */         charList.add(unknownCharClass(ch));
/*     */       }
/*     */     }
/* 267 */     for (int i = 0; i < 2; i++) {
/* 268 */       charList.add(Symbol.BEGIN_WORD);
/*     */     }
/*     */     
/* 271 */     double score = 0.0D;
/* 272 */     int i = 0; for (int size = charList.size(); i < size - 2; i++) {
/* 273 */       Symbol nextChar = (Symbol)charList.get(i);
/* 274 */       charList.set(i, tag);
/* 275 */       double charScore = getBackedOffDist(charList.subList(i, i + 2 + 1)).probabilityOf(nextChar);
/* 276 */       score += Math.log(charScore);
/*     */     }
/*     */     
/* 279 */     switch (penaltyType)
/*     */     {
/*     */     case 0: 
/*     */       break;
/*     */     case 1: 
/* 284 */       score -= chars.length * (chars.length + 1) * (lengthPenalty / 2.0D);
/* 285 */       break;
/*     */     
/*     */     case 2: 
/* 288 */       score -= (chars.length - 1) * lengthPenalty;
/*     */     }
/*     */     
/* 291 */     return (float)score;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Distribution getBackedOffDist(List context)
/*     */   {
/* 299 */     for (int i = 3; i >= 0; i--) {
/* 300 */       List l = context.subList(0, i);
/* 301 */       if (this.charDistributions.containsKey(l)) {
/* 302 */         return (Distribution)this.charDistributions.get(l);
/*     */       }
/*     */     }
/* 305 */     throw new RuntimeException("OOPS... no prior distribution...?");
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String sampleFrom(String tag)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 39	java/lang/StringBuilder
/*     */     //   3: dup
/*     */     //   4: invokespecial 40	java/lang/StringBuilder:<init>	()V
/*     */     //   7: astore_2
/*     */     //   8: new 8	java/util/ArrayList
/*     */     //   11: dup
/*     */     //   12: iconst_3
/*     */     //   13: invokespecial 80	java/util/ArrayList:<init>	(I)V
/*     */     //   16: astore_3
/*     */     //   17: aload_3
/*     */     //   18: aload_1
/*     */     //   19: invokeinterface 86 2 0
/*     */     //   24: pop
/*     */     //   25: iconst_0
/*     */     //   26: istore 4
/*     */     //   28: iload 4
/*     */     //   30: iconst_2
/*     */     //   31: if_icmpge +19 -> 50
/*     */     //   34: aload_3
/*     */     //   35: getstatic 90	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon$Symbol:BEGIN_WORD	Ledu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon$Symbol;
/*     */     //   38: invokeinterface 86 2 0
/*     */     //   43: pop
/*     */     //   44: iinc 4 1
/*     */     //   47: goto -19 -> 28
/*     */     //   50: aload_0
/*     */     //   51: aload_3
/*     */     //   52: invokespecial 124	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon:getBackedOffDist	(Ljava/util/List;)Ledu/stanford/nlp/stats/Distribution;
/*     */     //   55: astore 4
/*     */     //   57: aload 4
/*     */     //   59: invokevirtual 135	edu/stanford/nlp/stats/Distribution:sampleFrom	()Ljava/lang/Object;
/*     */     //   62: checkcast 30	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon$Symbol
/*     */     //   65: astore 5
/*     */     //   67: aload 5
/*     */     //   69: getstatic 23	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon$Symbol:END_WORD	Ledu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon$Symbol;
/*     */     //   72: if_acmpeq +142 -> 214
/*     */     //   75: aload_2
/*     */     //   76: aload 5
/*     */     //   78: invokevirtual 32	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon$Symbol:getCh	()C
/*     */     //   81: invokevirtual 136	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */     //   84: pop
/*     */     //   85: getstatic 127	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon:penaltyType	I
/*     */     //   88: lookupswitch	default:+62->150, 1:+28->116, 2:+49->137
/*     */     //   116: invokestatic 137	java/lang/Math:random	()D
/*     */     //   119: getstatic 128	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon:lengthPenalty	D
/*     */     //   122: aload_2
/*     */     //   123: invokevirtual 138	java/lang/StringBuilder:length	()I
/*     */     //   126: i2d
/*     */     //   127: invokestatic 139	java/lang/Math:pow	(DD)D
/*     */     //   130: dcmpl
/*     */     //   131: ifle +19 -> 150
/*     */     //   134: goto +80 -> 214
/*     */     //   137: invokestatic 137	java/lang/Math:random	()D
/*     */     //   140: getstatic 128	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon:lengthPenalty	D
/*     */     //   143: dcmpl
/*     */     //   144: ifle +6 -> 150
/*     */     //   147: goto +67 -> 214
/*     */     //   150: iconst_1
/*     */     //   151: istore 6
/*     */     //   153: iload 6
/*     */     //   155: iconst_2
/*     */     //   156: if_icmpge +28 -> 184
/*     */     //   159: aload_3
/*     */     //   160: iload 6
/*     */     //   162: iconst_1
/*     */     //   163: iadd
/*     */     //   164: aload_3
/*     */     //   165: iload 6
/*     */     //   167: invokeinterface 12 2 0
/*     */     //   172: invokeinterface 123 3 0
/*     */     //   177: pop
/*     */     //   178: iinc 6 1
/*     */     //   181: goto -28 -> 153
/*     */     //   184: aload_3
/*     */     //   185: iconst_1
/*     */     //   186: aload 5
/*     */     //   188: invokeinterface 123 3 0
/*     */     //   193: pop
/*     */     //   194: aload_0
/*     */     //   195: aload_3
/*     */     //   196: invokespecial 124	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon:getBackedOffDist	(Ljava/util/List;)Ledu/stanford/nlp/stats/Distribution;
/*     */     //   199: astore 4
/*     */     //   201: aload 4
/*     */     //   203: invokevirtual 135	edu/stanford/nlp/stats/Distribution:sampleFrom	()Ljava/lang/Object;
/*     */     //   206: checkcast 30	edu/stanford/nlp/parser/lexparser/ChineseCharacterBasedLexicon$Symbol
/*     */     //   209: astore 5
/*     */     //   211: goto -144 -> 67
/*     */     //   214: aload_2
/*     */     //   215: invokevirtual 51	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   218: areturn
/*     */     // Line number table:
/*     */     //   Java source line #315	-> byte code offset #0
/*     */     //   Java source line #316	-> byte code offset #8
/*     */     //   Java source line #319	-> byte code offset #17
/*     */     //   Java source line #320	-> byte code offset #25
/*     */     //   Java source line #321	-> byte code offset #34
/*     */     //   Java source line #320	-> byte code offset #44
/*     */     //   Java source line #323	-> byte code offset #50
/*     */     //   Java source line #324	-> byte code offset #57
/*     */     //   Java source line #326	-> byte code offset #67
/*     */     //   Java source line #327	-> byte code offset #75
/*     */     //   Java source line #328	-> byte code offset #85
/*     */     //   Java source line #330	-> byte code offset #116
/*     */     //   Java source line #331	-> byte code offset #134
/*     */     //   Java source line #335	-> byte code offset #137
/*     */     //   Java source line #336	-> byte code offset #147
/*     */     //   Java source line #340	-> byte code offset #150
/*     */     //   Java source line #341	-> byte code offset #159
/*     */     //   Java source line #340	-> byte code offset #178
/*     */     //   Java source line #343	-> byte code offset #184
/*     */     //   Java source line #344	-> byte code offset #194
/*     */     //   Java source line #345	-> byte code offset #201
/*     */     //   Java source line #348	-> byte code offset #214
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	219	0	this	ChineseCharacterBasedLexicon
/*     */     //   0	219	1	tag	String
/*     */     //   7	208	2	buf	StringBuilder
/*     */     //   16	180	3	context	List
/*     */     //   26	19	4	i	int
/*     */     //   55	147	4	d	Distribution
/*     */     //   65	145	5	gen	Symbol
/*     */     //   151	28	6	i	int
/*     */   }
/*     */   
/*     */   public String sampleFrom()
/*     */   {
/* 358 */     String POS = (String)this.POSDistribution.sampleFrom();
/* 359 */     return sampleFrom(POS);
/*     */   }
/*     */   
/*     */   public Iterator<IntTaggedWord> ruleIteratorByWord(int word, int loc)
/*     */   {
/* 364 */     throw new UnsupportedOperationException("ChineseCharacterBasedLexicon has no rule iterator!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int numRules()
/*     */   {
/* 372 */     return 0;
/*     */   }
/*     */   
/*     */   public void tune(List trees) {}
/*     */   
/*     */   private Distribution getWordLengthDistribution()
/*     */   {
/* 379 */     int samples = 0;
/* 380 */     Counter<Integer> c = new Counter();
/* 381 */     while (samples++ < 10000) {
/* 382 */       String s = sampleFrom();
/* 383 */       c.incrementCount(new Integer(s.length()));
/* 384 */       if (samples % 1000 == 0) {
/* 385 */         System.out.print(".");
/*     */       }
/*     */     }
/* 388 */     System.out.println();
/* 389 */     Distribution genWordLengthDist = Distribution.getDistribution(c);
/* 390 */     return genWordLengthDist;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws java.io.IOException {
/* 394 */     Map<String, Integer> flagsToNumArgs = new java.util.HashMap();
/* 395 */     flagsToNumArgs.put("-parser", new Integer(3));
/* 396 */     flagsToNumArgs.put("-lex", new Integer(3));
/* 397 */     flagsToNumArgs.put("-test", new Integer(2));
/* 398 */     flagsToNumArgs.put("-out", new Integer(1));
/* 399 */     flagsToNumArgs.put("-lengthPenalty", new Integer(1));
/* 400 */     flagsToNumArgs.put("-penaltyType", new Integer(1));
/* 401 */     flagsToNumArgs.put("-maxLength", new Integer(1));
/* 402 */     flagsToNumArgs.put("-stats", new Integer(2));
/*     */     
/* 404 */     Map<String, String[]> argMap = edu.stanford.nlp.util.StringUtils.argsToMap(args, flagsToNumArgs);
/*     */     
/* 406 */     boolean eval = argMap.containsKey("-eval");
/*     */     
/* 408 */     if (argMap.containsKey("-out")) {
/* 409 */       pw = new PrintWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(((String[])argMap.get("-out"))[0]), "GB18030"), true);
/*     */     }
/*     */     
/* 412 */     System.err.println("ChineseCharacterBasedLexicon called with args:");
/*     */     
/* 414 */     ChineseTreebankParserParams ctpp = new ChineseTreebankParserParams();
/* 415 */     for (int i = 0; i < args.length; i++) {
/* 416 */       ctpp.setOptionFlag(args, i);
/* 417 */       System.err.print(" " + args[i]);
/*     */     }
/* 419 */     System.err.println();
/* 420 */     Options op = new Options(ctpp);
/*     */     
/* 422 */     if (argMap.containsKey("-stats")) {
/* 423 */       String[] statArgs = (String[])argMap.get("-stats");
/* 424 */       MemoryTreebank rawTrainTreebank = op.tlpParams.memoryTreebank();
/* 425 */       java.io.FileFilter trainFilt = new edu.stanford.nlp.io.NumberRangesFileFilter(statArgs[1], false);
/* 426 */       rawTrainTreebank.loadPath(new java.io.File(statArgs[0]), trainFilt);
/* 427 */       System.err.println("Done reading trees.");
/*     */       MemoryTreebank trainTreebank;
/* 429 */       if (argMap.containsKey("-annotate")) {
/* 430 */         MemoryTreebank trainTreebank = new MemoryTreebank();
/* 431 */         TreeAnnotator annotator = new TreeAnnotator(ctpp.headFinder(), ctpp);
/* 432 */         for (Tree tree : rawTrainTreebank) {
/* 433 */           trainTreebank.add(annotator.transformTree(tree));
/*     */         }
/* 435 */         System.err.println("Done annotating trees.");
/*     */       } else {
/* 437 */         trainTreebank = rawTrainTreebank;
/*     */       }
/* 439 */       printStats(trainTreebank);
/* 440 */       System.exit(0);
/*     */     }
/*     */     
/* 443 */     int maxLength = 1000000;
/*     */     
/* 445 */     if (argMap.containsKey("-norm")) {
/* 446 */       Test.lengthNormalization = true;
/*     */     }
/* 448 */     if (argMap.containsKey("-maxLength")) {
/* 449 */       maxLength = Integer.parseInt(((String[])argMap.get("-maxLength"))[0]);
/*     */     }
/* 451 */     Test.maxLength = 120;
/* 452 */     boolean combo = argMap.containsKey("-combo");
/* 453 */     if (combo) {
/* 454 */       ctpp.useCharacterBasedLexicon = true;
/* 455 */       Test.maxSpanForTags = 10;
/* 456 */       op.doDep = false;
/* 457 */       op.dcTags = false;
/*     */     }
/*     */     
/* 460 */     if (argMap.containsKey("-rad")) {
/* 461 */       useUnknownCharModel = true;
/*     */     }
/*     */     
/* 464 */     LexicalizedParser lp = null;
/* 465 */     Lexicon lex = null;
/* 466 */     if (argMap.containsKey("-parser")) {
/* 467 */       String[] parserArgs = (String[])argMap.get("-parser");
/* 468 */       if (parserArgs.length > 1) {
/* 469 */         java.io.FileFilter trainFilt = new edu.stanford.nlp.io.NumberRangesFileFilter(parserArgs[1], false);
/* 470 */         lp = new LexicalizedParser(parserArgs[0], trainFilt, op);
/* 471 */         if (parserArgs.length == 3) {
/* 472 */           String filename = parserArgs[2];
/* 473 */           System.err.println("Writing parser in serialized format to file " + filename + " ");
/* 474 */           System.err.flush();
/* 475 */           ObjectOutputStream out = edu.stanford.nlp.io.IOUtils.writeStreamFromString(filename);
/* 476 */           out.writeObject(lp.parserData());
/* 477 */           out.close();
/* 478 */           System.err.println("done.");
/*     */         }
/*     */       } else {
/* 481 */         String parserFile = parserArgs[0];
/* 482 */         lp = new LexicalizedParser(parserFile, op);
/*     */       }
/* 484 */       lex = lp.getLexicon();
/* 485 */       op = lp.getOp();
/* 486 */       ctpp = (ChineseTreebankParserParams)op.tlpParams;
/*     */     }
/*     */     
/* 489 */     if (argMap.containsKey("-lex")) {
/* 490 */       String[] lexArgs = (String[])argMap.get("-lex");
/* 491 */       if (lexArgs.length > 1) {
/* 492 */         lex = ctpp.lex(op.lexOptions);
/* 493 */         MemoryTreebank rawTrainTreebank = op.tlpParams.memoryTreebank();
/* 494 */         java.io.FileFilter trainFilt = new edu.stanford.nlp.io.NumberRangesFileFilter(lexArgs[1], false);
/* 495 */         rawTrainTreebank.loadPath(new java.io.File(lexArgs[0]), trainFilt);
/* 496 */         System.err.println("Done reading trees.");
/*     */         MemoryTreebank trainTreebank;
/* 498 */         if (argMap.containsKey("-annotate")) {
/* 499 */           MemoryTreebank trainTreebank = new MemoryTreebank();
/* 500 */           TreeAnnotator annotator = new TreeAnnotator(ctpp.headFinder(), ctpp);
/* 501 */           for (Iterator iter = rawTrainTreebank.iterator(); iter.hasNext();) {
/* 502 */             Tree tree = (Tree)iter.next();
/* 503 */             tree = annotator.transformTree(tree);
/* 504 */             trainTreebank.add(tree);
/*     */           }
/* 506 */           System.err.println("Done annotating trees.");
/*     */         } else {
/* 508 */           trainTreebank = rawTrainTreebank;
/*     */         }
/* 510 */         lex.train(trainTreebank);
/* 511 */         System.err.println("Done training lexicon.");
/* 512 */         if (lexArgs.length == 3) {
/* 513 */           String filename = lexArgs.length == 3 ? lexArgs[2] : "parsers/chineseCharLex.ser.gz";
/* 514 */           System.err.println("Writing lexicon in serialized format to file " + filename + " ");
/* 515 */           System.err.flush();
/* 516 */           ObjectOutputStream out = edu.stanford.nlp.io.IOUtils.writeStreamFromString(filename);
/* 517 */           out.writeObject(lex);
/* 518 */           out.close();
/* 519 */           System.err.println("done.");
/*     */         }
/*     */       } else {
/* 522 */         String lexFile = lexArgs.length == 1 ? lexArgs[0] : "parsers/chineseCharLex.ser.gz";
/* 523 */         System.err.println("Reading Lexicon from file " + lexFile);
/* 524 */         java.io.ObjectInputStream in = edu.stanford.nlp.io.IOUtils.readStreamFromString(lexFile);
/*     */         try {
/* 526 */           lex = (Lexicon)in.readObject();
/*     */         } catch (ClassNotFoundException e) {
/* 528 */           throw new RuntimeException("Bad serialized file: " + lexFile);
/*     */         }
/* 530 */         in.close();
/*     */       }
/*     */     }
/*     */     
/* 534 */     if (argMap.containsKey("-lengthPenalty")) {
/* 535 */       lengthPenalty = Double.parseDouble(((String[])argMap.get("-lengthPenalty"))[0]);
/*     */     }
/*     */     
/* 538 */     if (argMap.containsKey("-penaltyType")) {
/* 539 */       penaltyType = Integer.parseInt(((String[])argMap.get("-penaltyType"))[0]);
/*     */     }
/*     */     
/* 542 */     if (argMap.containsKey("-test")) {
/* 543 */       boolean segmentWords = (ctpp.segmentMarkov) || (ctpp.segmentMaxMatch);
/* 544 */       boolean parse = lp != null;
/* 545 */       assert ((parse) || (segmentWords));
/*     */       
/*     */ 
/* 548 */       WordSegmenter seg = null;
/* 549 */       if (segmentWords) {
/* 550 */         seg = (WordSegmenter)lex;
/*     */       }
/* 552 */       String[] testArgs = (String[])argMap.get("-test");
/* 553 */       MemoryTreebank testTreebank = op.tlpParams.memoryTreebank();
/* 554 */       java.io.FileFilter testFilt = new edu.stanford.nlp.io.NumberRangesFileFilter(testArgs[1], false);
/* 555 */       testTreebank.loadPath(new java.io.File(testArgs[0]), testFilt);
/* 556 */       TreeTransformer subcategoryStripper = op.tlpParams.subcategoryStripper();
/* 557 */       TreeTransformer collinizer = ctpp.collinizer();
/*     */       
/* 559 */       edu.stanford.nlp.trees.WordCatEquivalenceClasser eqclass = new edu.stanford.nlp.trees.WordCatEquivalenceClasser();
/* 560 */       edu.stanford.nlp.trees.WordCatEqualityChecker eqcheck = new edu.stanford.nlp.trees.WordCatEqualityChecker();
/* 561 */       EquivalenceClassEval basicEval = new EquivalenceClassEval(eqclass, eqcheck, "basic");
/* 562 */       EquivalenceClassEval collinsEval = new EquivalenceClassEval(eqclass, eqcheck, "collinized");
/* 563 */       List<String> evalTypes = new ArrayList(3);
/* 564 */       boolean goodPOS = false;
/* 565 */       if (segmentWords) {
/* 566 */         evalTypes.add("word");
/* 567 */         if ((ctpp.segmentMarkov) && (!parse)) {
/* 568 */           evalTypes.add("tag");
/* 569 */           goodPOS = true;
/*     */         }
/*     */       }
/* 572 */       if (parse) {
/* 573 */         evalTypes.add("tag");
/* 574 */         evalTypes.add("cat");
/* 575 */         if (combo) {
/* 576 */           evalTypes.add("word");
/* 577 */           goodPOS = true;
/*     */         }
/*     */       }
/* 580 */       TreeToBracketProcessor proc = new TreeToBracketProcessor(evalTypes);
/*     */       
/* 582 */       System.err.println("Testing...");
/* 583 */       for (Tree goldTop : testTreebank) {
/* 584 */         Tree gold = goldTop.firstChild();
/* 585 */         Sentence goldSentence = gold.yield();
/* 586 */         if (goldSentence.length() > maxLength) {
/* 587 */           System.err.println("Skipping sentence; too long: " + goldSentence.length());
/*     */         }
/*     */         else {
/* 590 */           System.err.println("Processing sentence; length: " + goldSentence.length());
/*     */           Sentence s;
/*     */           Sentence s;
/* 593 */           if (segmentWords) {
/* 594 */             StringBuilder goldCharBuf = new StringBuilder();
/* 595 */             for (Iterator wordIter = goldSentence.iterator(); wordIter.hasNext();) {
/* 596 */               edu.stanford.nlp.ling.StringLabel word = (edu.stanford.nlp.ling.StringLabel)wordIter.next();
/* 597 */               goldCharBuf.append(word.value());
/*     */             }
/* 599 */             String goldChars = goldCharBuf.toString();
/* 600 */             s = seg.segmentWords(goldChars);
/*     */           } else {
/* 602 */             s = goldSentence;
/*     */           }
/*     */           Tree tree;
/* 605 */           if (parse) {
/* 606 */             lp.parse(s);
/* 607 */             Tree tree = lp.getBestParse();
/* 608 */             if (tree == null) {
/* 609 */               throw new RuntimeException("PARSER RETURNED NULL!!!");
/*     */             }
/*     */           } else {
/* 612 */             tree = edu.stanford.nlp.trees.Trees.toFlatTree(s);
/* 613 */             tree = subcategoryStripper.transformTree(tree);
/*     */           }
/*     */           
/* 616 */           if (pw != null) {
/* 617 */             if (parse) {
/* 618 */               tree.pennPrint(pw);
/*     */             } else {
/* 620 */               Iterator sentIter = s.iterator();
/*     */               for (;;) {
/* 622 */                 edu.stanford.nlp.ling.Word word = (edu.stanford.nlp.ling.Word)sentIter.next();
/* 623 */                 pw.print(word.word());
/* 624 */                 if (!sentIter.hasNext()) break;
/* 625 */                 pw.print(" ");
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 631 */             pw.println();
/*     */           }
/*     */           
/* 634 */           if (eval)
/*     */           {
/* 636 */             Collection ourBrackets = proc.allBrackets(tree);
/* 637 */             Collection goldBrackets = proc.allBrackets(gold);
/* 638 */             if (goodPOS) {
/* 639 */               ourBrackets.addAll(proc.commonWordTagTypeBrackets(tree, gold));
/* 640 */               goldBrackets.addAll(proc.commonWordTagTypeBrackets(gold, tree));
/*     */             }
/*     */             
/* 643 */             basicEval.eval(ourBrackets, goldBrackets);
/* 644 */             System.out.println("\nScores:");
/* 645 */             basicEval.displayLast();
/*     */             
/* 647 */             Tree collinsTree = collinizer.transformTree(tree);
/* 648 */             Tree collinsGold = collinizer.transformTree(gold);
/* 649 */             ourBrackets = proc.allBrackets(collinsTree);
/* 650 */             goldBrackets = proc.allBrackets(collinsGold);
/* 651 */             if (goodPOS) {
/* 652 */               ourBrackets.addAll(proc.commonWordTagTypeBrackets(collinsTree, collinsGold));
/* 653 */               goldBrackets.addAll(proc.commonWordTagTypeBrackets(collinsGold, collinsTree));
/*     */             }
/*     */             
/* 656 */             collinsEval.eval(ourBrackets, goldBrackets);
/* 657 */             System.out.println("\nCollinized scores:");
/* 658 */             collinsEval.displayLast();
/*     */             
/* 660 */             System.out.println();
/*     */           }
/*     */         } }
/* 663 */       if (eval) {
/* 664 */         basicEval.display();
/* 665 */         System.out.println();
/* 666 */         collinsEval.display();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void readData(java.io.BufferedReader in) throws java.io.IOException {
/* 672 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void writeData(java.io.Writer w) throws java.io.IOException {
/* 676 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isKnown(int word) {
/* 680 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public boolean isKnown(String word) {
/* 684 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */   private static class Symbol
/*     */     implements java.io.Serializable
/*     */   {
/*     */     private static final int UNKNOWN_TYPE = 0;
/*     */     private static final int DIGIT_TYPE = 1;
/*     */     private static final int LETTER_TYPE = 2;
/*     */     private static final int BEGIN_WORD_TYPE = 3;
/*     */     private static final int END_WORD_TYPE = 4;
/*     */     private static final int CHAR_TYPE = 5;
/*     */     private static final int UNK_CLASS_TYPE = 6;
/*     */     private char ch;
/*     */     private String unkClass;
/*     */     int type;
/* 701 */     public static final Symbol UNKNOWN = new Symbol(0);
/* 702 */     public static final Symbol DIGIT = new Symbol(1);
/* 703 */     public static final Symbol LETTER = new Symbol(2);
/* 704 */     public static final Symbol BEGIN_WORD = new Symbol(3);
/* 705 */     public static final Symbol END_WORD = new Symbol(4);
/*     */     
/* 707 */     public static Interner interner = new Interner();
/*     */     private static final long serialVersionUID = 8925032621317022510L;
/*     */     
/* 710 */     public Symbol(char ch) { this.type = 5;
/* 711 */       this.ch = ch;
/*     */     }
/*     */     
/*     */     public Symbol(String unkClass) {
/* 715 */       this.type = 6;
/* 716 */       this.unkClass = unkClass;
/*     */     }
/*     */     
/*     */     public Symbol(int type) {
/* 720 */       assert (type != 5);
/* 721 */       this.type = type;
/*     */     }
/*     */     
/*     */     public static Symbol cannonicalSymbol(char ch) {
/* 725 */       if (Character.isDigit(ch)) {
/* 726 */         return DIGIT;
/*     */       }
/*     */       
/* 729 */       if ((Character.getNumericValue(ch) >= 10) && (Character.getNumericValue(ch) <= 35)) {
/* 730 */         return LETTER;
/*     */       }
/*     */       
/* 733 */       return new Symbol(ch);
/*     */     }
/*     */     
/*     */     public char getCh() {
/* 737 */       if (this.type == 5) {
/* 738 */         return this.ch;
/*     */       }
/* 740 */       return '*';
/*     */     }
/*     */     
/*     */     public Symbol intern()
/*     */     {
/* 745 */       return (Symbol)interner.intern(this);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 749 */       if (this.type == 5)
/* 750 */         return "[u" + this.ch + "]";
/* 751 */       if (this.type == 6) {
/* 752 */         return "UNK:" + this.unkClass;
/*     */       }
/* 754 */       return Integer.toString(this.type);
/*     */     }
/*     */     
/*     */     private Object readResolve() throws java.io.ObjectStreamException
/*     */     {
/* 759 */       switch (this.type) {
/*     */       case 5: 
/* 761 */         return intern();
/*     */       case 6: 
/* 763 */         return intern();
/*     */       case 0: 
/* 765 */         return UNKNOWN;
/*     */       case 1: 
/* 767 */         return DIGIT;
/*     */       case 2: 
/* 769 */         return LETTER;
/*     */       case 3: 
/* 771 */         return BEGIN_WORD;
/*     */       case 4: 
/* 773 */         return END_WORD;
/*     */       }
/* 775 */       throw new java.io.InvalidObjectException("ILLEGAL VALUE IN SERIALIZED SYMBOL");
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 780 */       if (this == o) {
/* 781 */         return true;
/*     */       }
/* 783 */       if (!(o instanceof Symbol)) {
/* 784 */         return false;
/*     */       }
/*     */       
/* 787 */       Symbol symbol = (Symbol)o;
/*     */       
/* 789 */       if (this.ch != symbol.ch) {
/* 790 */         return false;
/*     */       }
/* 792 */       if (this.type != symbol.type) {
/* 793 */         return false;
/*     */       }
/* 795 */       if (this.unkClass != null ? !this.unkClass.equals(symbol.unkClass) : symbol.unkClass != null) {
/* 796 */         return false;
/*     */       }
/*     */       
/* 799 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 804 */       int result = this.ch;
/* 805 */       result = 29 * result + (this.unkClass != null ? this.unkClass.hashCode() : 0);
/* 806 */       result = 29 * result + this.type;
/* 807 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ChineseCharacterBasedLexicon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */