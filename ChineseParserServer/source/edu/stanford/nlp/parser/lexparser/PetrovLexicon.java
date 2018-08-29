/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.Timing;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PetrovLexicon
/*     */   implements Lexicon
/*     */ {
/*  21 */   static String UNKNOWN_WORD = "UNK";
/*     */   
/*     */   Numberer tagNumberer;
/*     */   
/*     */   Numberer wordNumberer;
/*     */   
/*     */   Counter<Integer> wordCounter;
/*     */   
/*     */   Counter<Integer> tagCounter;
/*     */   
/*     */   Counter<Integer> unseenTagCounter;
/*     */   
/*     */   Counter<IntTaggedWord> tagAndWordCounter;
/*     */   
/*     */   Counter<IntTaggedWord> unseenTagAndSignatureCounter;
/*     */   
/*  37 */   int smoothInUnknownsThreshold = 10;
/*  38 */   double smooth = 0.1D;
/*     */   List[] rulesWithWord;
/*     */   
/*     */   public boolean isKnown(int word)
/*     */   {
/*  43 */     return this.wordCounter.getCount(Integer.valueOf(word)) > 0.0D;
/*     */   }
/*     */   
/*     */   public boolean isKnown(String word) {
/*  47 */     return isKnown(this.wordNumberer.number(word));
/*     */   }
/*     */   
/*     */   public Iterator<IntTaggedWord> ruleIteratorByWord(int word, int loc) {
/*  51 */     if (isKnown(word)) {
/*  52 */       List<IntTaggedWord> rules = new ArrayList();
/*     */       
/*  54 */       return rules.iterator();
/*     */     }
/*  56 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initRulesWithWord() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public int numRules()
/*     */   {
/*  68 */     if (this.rulesWithWord == null) {
/*  69 */       initRulesWithWord();
/*     */     }
/*  71 */     int accumulated = 0;
/*  72 */     for (List<IntTaggedWord> lis : this.rulesWithWord) {
/*  73 */       accumulated += lis.size();
/*     */     }
/*  75 */     return accumulated;
/*     */   }
/*     */   
/*     */   public void train(Collection<Tree> trees)
/*     */   {
/*  80 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public float score(IntTaggedWord iTW, int loc)
/*     */   {
/*  87 */     int word = iTW.word();
/*  88 */     int tag = iTW.tag();
/*  89 */     double wc = this.wordCounter.getCount(Integer.valueOf(word));
/*  90 */     double totalSeen = this.tagCounter.totalCount();
/*  91 */     double totalUnseen = this.unseenTagCounter.totalCount();
/*  92 */     if (wc > 0.0D) {
/*  93 */       double probTagGivenWord = Double.NEGATIVE_INFINITY;
/*  94 */       double twc = this.tagAndWordCounter.getCount(iTW);
/*  95 */       if (wc > this.smoothInUnknownsThreshold) {
/*  96 */         probTagGivenWord = twc / wc;
/*     */       } else {
/*  98 */         double probTagGivenUnseen = this.unseenTagCounter.getCount(Integer.valueOf(tag)) / totalUnseen;
/*  99 */         probTagGivenWord = (twc + this.smooth * probTagGivenUnseen) / (wc + this.smooth);
/*     */       }
/* 101 */       double tc = this.tagCounter.getCount(Integer.valueOf(tag));
/* 102 */       double probTag = tc / totalSeen;
/* 103 */       double probWord = wc / totalSeen;
/* 104 */       return (float)(probTagGivenWord * probWord / probTag);
/*     */     }
/*     */     
/*     */ 
/* 108 */     int signature = getSignature(word, loc);
/* 109 */     double sc = this.wordCounter.getCount(Integer.valueOf(signature));
/* 110 */     IntTaggedWord siTW = new IntTaggedWord(signature, tag);
/* 111 */     double tsc = this.unseenTagAndSignatureCounter.getCount(siTW);
/* 112 */     double probTagGivenUnseen = this.unseenTagCounter.getCount(Integer.valueOf(tag)) / totalUnseen;
/* 113 */     double probTagGivenWord = (tsc + this.smooth * probTagGivenUnseen) / (sc + this.smooth);
/* 114 */     double tc = this.unseenTagCounter.getCount(Integer.valueOf(tag));
/* 115 */     double probTag = tc / totalUnseen;
/* 116 */     double probWord = wc / totalUnseen;
/* 117 */     return (float)(probTagGivenWord * probWord / probTag);
/*     */   }
/*     */   
/*     */   public void writeData(Writer w) throws IOException
/*     */   {
/* 122 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void readData(BufferedReader in) throws IOException {
/* 126 */     Timing t = new Timing();
/* 127 */     t.start();
/* 128 */     System.err.println("Loading in PetrovLexicon from file...");
/* 129 */     int numSeenWords = 0;
/* 130 */     int numUnseenWords = 0;
/* 131 */     this.wordCounter = new Counter();
/* 132 */     this.tagCounter = new Counter();
/* 133 */     this.unseenTagCounter = new Counter();
/* 134 */     this.tagAndWordCounter = new Counter();
/* 135 */     this.unseenTagAndSignatureCounter = new Counter();
/* 136 */     String line = in.readLine();
/*     */     
/*     */ 
/* 139 */     int WC = 0;
/* 140 */     int TC = 1;
/* 141 */     int UTC = 2;
/* 142 */     int TWC = 3;
/* 143 */     int UTSC = 4;
/* 144 */     int status = -1;
/* 145 */     while (line != null) {
/* 146 */       if (line.startsWith("-------"))
/*     */       {
/* 148 */         status = -1;
/* 149 */       } else if (status == -1) {
/* 150 */         if (line.startsWith("WORD-COUNTER")) {
/* 151 */           status = WC;
/* 152 */         } else if (line.startsWith("TAG-COUNTER")) {
/* 153 */           status = TC;
/* 154 */         } else if (line.startsWith("UNSEEN-TAG-COUNTER")) {
/* 155 */           status = UTC;
/* 156 */         } else if (line.startsWith("TAG-AND-WORD-COUNTER")) {
/* 157 */           status = TWC;
/* 158 */         } else if (line.startsWith("UNSEEN-TAG-AND-SIGNATURE-COUNTER")) {
/* 159 */           status = UTSC;
/*     */         } else
/* 161 */           throw new RuntimeException("Unrecognized header: " + line);
/* 162 */       } else if (status == WC) {
/* 163 */         int space = line.indexOf(' ');
/* 164 */         String wordString = new String(line.substring(0, space));
/* 165 */         if (wordString.startsWith("UNK")) {
/* 166 */           numUnseenWords++;
/*     */         } else {
/* 168 */           numSeenWords++;
/*     */         }
/* 170 */         int word = this.wordNumberer.number(wordString);
/* 171 */         double count = Double.parseDouble(line.substring(space + 1));
/* 172 */         this.wordCounter.setCount(Integer.valueOf(word), count);
/* 173 */       } else if (status == TC) {
/* 174 */         int space = line.indexOf(' ');
/* 175 */         int tag = this.tagNumberer.number(new String(line.substring(0, space)));
/* 176 */         double count = Double.parseDouble(line.substring(space + 1));
/* 177 */         this.tagCounter.setCount(Integer.valueOf(tag), count);
/* 178 */       } else if (status == UTC) {
/* 179 */         int space = line.indexOf(' ');
/* 180 */         int tag = this.tagNumberer.number(new String(line.substring(0, space)));
/* 181 */         double count = Double.parseDouble(line.substring(space + 1));
/* 182 */         this.unseenTagCounter.setCount(Integer.valueOf(tag), count);
/* 183 */       } else if (status == TWC) {
/* 184 */         int space = line.indexOf(' ');
/* 185 */         int bracket = line.indexOf('[');
/* 186 */         String baseTag = line.substring(0, space);
/* 187 */         String word = new String(line.substring(space + 1, bracket - 1));
/* 188 */         String[] fields = line.substring(bracket + 1, line.length() - 1).split(" ,");
/* 189 */         for (int i = 0; i < fields.length; i++) {
/* 190 */           String tag = baseTag + "_" + i;
/* 191 */           IntTaggedWord itw = new IntTaggedWord(this.wordNumberer.number(word), this.tagNumberer.number(tag));
/* 192 */           double count = Double.parseDouble(fields[i]);
/* 193 */           this.tagAndWordCounter.setCount(itw, count);
/*     */         }
/* 195 */       } else if (status == UTSC)
/*     */       {
/* 197 */         int space = line.indexOf(' ');
/* 198 */         int bracket = line.indexOf('[');
/* 199 */         String baseTag = new String(line.substring(0, space));
/* 200 */         String word = new String(line.substring(space + 1, bracket - 1));
/* 201 */         String[] fields = line.substring(bracket + 1, line.length() - 1).split(" ,");
/* 202 */         for (int i = 0; i < fields.length; i++) {
/* 203 */           String tag = baseTag + "_" + i;
/* 204 */           IntTaggedWord itw = new IntTaggedWord(this.wordNumberer.number(word), this.tagNumberer.number(tag));
/* 205 */           double count = Double.parseDouble(fields[i]);
/* 206 */           this.unseenTagAndSignatureCounter.setCount(itw, count);
/*     */         }
/*     */       }
/* 209 */       line = in.readLine();
/*     */     }
/*     */     
/* 212 */     t.stop("Done loading.");
/* 213 */     System.err.println("numSeenWords: " + numSeenWords);
/* 214 */     System.err.println("numUnseenWords: " + numUnseenWords);
/* 215 */     System.err.println("wordCounter: " + this.wordCounter.size() + " keys and " + this.wordCounter.totalCount() + " total count.");
/* 216 */     System.err.println("tagCounter: " + this.tagCounter.size() + " keys and " + this.tagCounter.totalCount() + " total count.");
/* 217 */     System.err.println("unseenTagCounter: " + this.unseenTagCounter.size() + " keys and " + this.unseenTagCounter.totalCount() + " total count.");
/* 218 */     System.err.println("tagAndWordCounter: " + this.tagAndWordCounter.size() + " keys and " + this.tagAndWordCounter.totalCount() + " total count.");
/* 219 */     System.err.println("unseenTagAndSignatureCounter: " + this.unseenTagAndSignatureCounter.size() + " keys and " + this.unseenTagAndSignatureCounter.totalCount() + " total count.");
/*     */   }
/*     */   
/* 222 */   int lastWord = -1;
/* 223 */   int lastSignature = -1;
/*     */   
/* 225 */   public int getSignature(int word, int loc) { if (word == this.lastWord) return this.lastSignature;
/* 226 */     String wordString = (String)this.wordNumberer.object(word);
/* 227 */     String signatureString = getSignature(wordString, loc);
/* 228 */     int signature = this.wordNumberer.number(Integer.valueOf(word));
/* 229 */     this.lastWord = word;
/* 230 */     this.lastSignature = signature;
/* 231 */     return signature;
/*     */   }
/*     */   
/*     */   public String getSignature(String word, int loc) {
/* 235 */     StringBuilder sb = new StringBuilder();
/* 236 */     int wlen = word.length();
/* 237 */     int numCaps = 0;
/* 238 */     boolean hasDigit = false;
/* 239 */     boolean hasDash = false;
/* 240 */     boolean hasLower = false;
/* 241 */     for (int i = 0; i < wlen; i++) {
/* 242 */       char ch = word.charAt(i);
/* 243 */       if (Character.isDigit(ch)) {
/* 244 */         hasDigit = true;
/* 245 */       } else if (ch == '-') {
/* 246 */         hasDash = true;
/* 247 */       } else if (Character.isLetter(ch)) {
/* 248 */         if (Character.isLowerCase(ch)) {
/* 249 */           hasLower = true;
/* 250 */         } else if (Character.isTitleCase(ch)) {
/* 251 */           hasLower = true;
/* 252 */           numCaps++;
/*     */         } else {
/* 254 */           numCaps++;
/*     */         }
/*     */       }
/*     */     }
/* 258 */     char ch0 = word.charAt(0);
/* 259 */     String lowered = word.toLowerCase();
/* 260 */     if ((Character.isUpperCase(ch0)) || (Character.isTitleCase(ch0))) {
/* 261 */       if ((loc == 0) && (numCaps == 1)) {
/* 262 */         sb.append("-INITC");
/* 263 */         if (isKnown(lowered)) {
/* 264 */           sb.append("-KNOWNLC");
/*     */         }
/*     */       } else {
/* 267 */         sb.append("-CAPS");
/*     */       }
/* 269 */     } else if ((!Character.isLetter(ch0)) && (numCaps > 0)) {
/* 270 */       sb.append("-CAPS");
/* 271 */     } else if (hasLower) {
/* 272 */       sb.append("-LC");
/*     */     }
/* 274 */     if (hasDigit) {
/* 275 */       sb.append("-NUM");
/*     */     }
/* 277 */     if (hasDash) {
/* 278 */       sb.append("-DASH");
/*     */     }
/* 280 */     if ((lowered.endsWith("s")) && (wlen >= 3))
/*     */     {
/* 282 */       char ch2 = lowered.charAt(wlen - 2);
/*     */       
/* 284 */       if ((ch2 != 's') && (ch2 != 'i') && (ch2 != 'u')) {
/* 285 */         sb.append("-s");
/*     */       }
/* 287 */     } else if ((word.length() >= 5) && (!hasDash) && ((!hasDigit) || (numCaps <= 0)))
/*     */     {
/*     */ 
/* 290 */       if (lowered.endsWith("ed")) {
/* 291 */         sb.append("-ed");
/* 292 */       } else if (lowered.endsWith("ing")) {
/* 293 */         sb.append("-ing");
/* 294 */       } else if (lowered.endsWith("ion")) {
/* 295 */         sb.append("-ion");
/* 296 */       } else if (lowered.endsWith("er")) {
/* 297 */         sb.append("-er");
/* 298 */       } else if (lowered.endsWith("est")) {
/* 299 */         sb.append("-est");
/* 300 */       } else if (lowered.endsWith("ly")) {
/* 301 */         sb.append("-ly");
/* 302 */       } else if (lowered.endsWith("ity")) {
/* 303 */         sb.append("-ity");
/* 304 */       } else if (lowered.endsWith("y")) {
/* 305 */         sb.append("-y");
/* 306 */       } else if (lowered.endsWith("al")) {
/* 307 */         sb.append("-al");
/*     */       }
/*     */     }
/* 310 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public PetrovLexicon() {
/* 314 */     this.tagNumberer = Numberer.getGlobalNumberer("tags");
/* 315 */     this.wordNumberer = Numberer.getGlobalNumberer("words");
/* 316 */     this.wordCounter = new Counter();
/* 317 */     this.tagCounter = new Counter();
/* 318 */     this.unseenTagCounter = new Counter();
/* 319 */     this.tagAndWordCounter = new Counter();
/* 320 */     this.unseenTagAndSignatureCounter = new Counter();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\PetrovLexicon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */