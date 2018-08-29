/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.TaggedWord;
/*     */ import edu.stanford.nlp.ling.WordTag;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChineseUnknownWordModel
/*     */   implements Serializable
/*     */ {
/*     */   private static final String encoding = "GB18030";
/*     */   private static final boolean VERBOSE = false;
/*  29 */   private boolean useFirst = true;
/*  30 */   private boolean useGT = false;
/*  31 */   boolean useUnicodeType = false;
/*     */   
/*     */ 
/*     */   private static final String unknown = "UNK";
/*     */   
/*     */ 
/*     */   private static final String dateMatch = ".*[年月日号]";
/*     */   
/*     */   private static final String numberMatch = ".*[０１２３４５６７８９１一二三四五六七八九十百千万亿].*";
/*     */   
/*     */   private static final String ordinalMatch = "第.*";
/*     */   
/*     */   private static final String properNameMatch = ".*·.*";
/*     */   
/*  45 */   private Map<String, Counter<String>> tagHash = new HashMap();
/*  46 */   private Set seenFirst = new HashSet();
/*  47 */   private Map unknownGT = new HashMap();
/*     */   
/*     */   private static final long serialVersionUID = 221L;
/*     */   
/*     */ 
/*     */   void useGoodTuring()
/*     */   {
/*  54 */     this.useGT = true;
/*  55 */     this.useFirst = false;
/*     */   }
/*     */   
/*     */   public double score(IntTaggedWord itw)
/*     */   {
/*  60 */     return score(itw.toTaggedWord());
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
/*     */   public double score(TaggedWord tw)
/*     */   {
/*  74 */     String word = tw.word();
/*  75 */     String tag = tw.tag();
/*     */     
/*     */     double logProb;
/*     */     double logProb;
/*  79 */     if (word.matches(".*[年月日号]")) {
/*     */       double logProb;
/*  81 */       if (tag.equals("NT")) {
/*  82 */         logProb = 0.0D;
/*     */       } else
/*  84 */         logProb = Double.NEGATIVE_INFINITY;
/*     */     } else { double logProb;
/*  86 */       if (word.matches(".*[０１２３４５６７８９１一二三四五六七八九十百千万亿].*")) {
/*     */         double logProb;
/*  88 */         if ((tag.equals("CD")) && (!word.matches("第.*"))) {
/*  89 */           logProb = 0.0D; } else { double logProb;
/*  90 */           if ((tag.equals("OD")) && (word.matches("第.*"))) {
/*  91 */             logProb = 0.0D;
/*     */           } else
/*  93 */             logProb = Double.NEGATIVE_INFINITY;
/*     */         } } else { double logProb;
/*  95 */         if (word.matches(".*·.*")) {
/*     */           double logProb;
/*  97 */           if (tag.equals("NR")) {
/*  98 */             logProb = 0.0D;
/*     */           } else {
/* 100 */             logProb = Double.NEGATIVE_INFINITY;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*     */           double logProb;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */           if (this.useFirst) {
/* 136 */             String first = word.substring(0, 1);
/* 137 */             if (this.useUnicodeType) {
/* 138 */               char ch = word.charAt(0);
/* 139 */               int type = Character.getType(ch);
/* 140 */               if (type != 5)
/*     */               {
/* 142 */                 first = Integer.toString(type);
/*     */               }
/*     */             }
/* 145 */             if (!this.seenFirst.contains(first)) { double logProb;
/* 146 */               if (this.useGT) {
/* 147 */                 logProb = scoreGT(tag);
/*     */               }
/*     */               else {
/* 150 */                 first = "UNK";
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 155 */               Counter<String> wordProbs = (Counter)this.tagHash.get(tag);
/*     */               
/*     */               double logProb;
/*     */               
/* 159 */               if (wordProbs == null)
/*     */               {
/* 161 */                 logProb = Double.NEGATIVE_INFINITY; } else { double logProb;
/* 162 */                 if (wordProbs.containsKey(first)) {
/* 163 */                   logProb = wordProbs.getCount(first);
/*     */                 } else
/* 165 */                   logProb = wordProbs.getCount("UNK");
/*     */               } } } else { double logProb;
/* 167 */             if (this.useGT) {
/* 168 */               logProb = scoreGT(tag);
/*     */             }
/*     */             else
/* 171 */               logProb = Double.NEGATIVE_INFINITY;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 176 */     return logProb;
/*     */   }
/*     */   
/*     */   private double scoreGT(String tag) {
/*     */     double logProb;
/*     */     double logProb;
/* 182 */     if (this.unknownGT.containsKey(tag)) {
/* 183 */       logProb = ((Double)this.unknownGT.get(tag)).doubleValue();
/*     */     } else {
/* 185 */       logProb = Double.NEGATIVE_INFINITY;
/*     */     }
/* 187 */     return logProb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void train(Collection<Tree> trees)
/*     */   {
/* 197 */     if (this.useFirst) {
/* 198 */       System.err.println("ChineseUWM: treating unknown word as the average of their equivalents by first-character identity. useUnicodeType: " + this.useUnicodeType);
/*     */     }
/* 200 */     if (this.useGT) {
/* 201 */       System.err.println("ChineseUWM: using Good-Turing smoothing for unknown words.");
/*     */     }
/*     */     
/* 204 */     trainUnknownGT(trees);
/*     */     
/* 206 */     HashMap c = new HashMap();
/*     */     
/* 208 */     Counter tc = new Counter();
/*     */     
/* 210 */     for (Tree t : trees) {
/* 211 */       List words = t.taggedYield();
/* 212 */       for (j = words.iterator(); j.hasNext();) {
/* 213 */         TaggedWord tw = (TaggedWord)j.next();
/* 214 */         String word = tw.word();
/* 215 */         String first = tw.word().substring(0, 1);
/* 216 */         if (this.useUnicodeType) {
/* 217 */           char ch = word.charAt(0);
/* 218 */           int type = Character.getType(ch);
/* 219 */           if (type != 5)
/*     */           {
/* 221 */             first = Integer.toString(type);
/*     */           }
/*     */         }
/* 224 */         String tag = tw.tag();
/* 225 */         if (!c.containsKey(tag)) {
/* 226 */           c.put(tag, new Counter());
/*     */         }
/* 228 */         ((Counter)c.get(tag)).incrementCount(first);
/*     */         
/* 230 */         tc.incrementCount(tag);
/*     */         
/* 232 */         this.seenFirst.add(first);
/*     */       }
/*     */     }
/*     */     Iterator j;
/* 236 */     for (Iterator i = c.keySet().iterator(); i.hasNext();) {
/* 237 */       tag = (String)i.next();
/* 238 */       wc = (Counter)c.get(tag);
/*     */       
/*     */ 
/* 241 */       if (!this.tagHash.containsKey(tag)) {
/* 242 */         this.tagHash.put(tag, new Counter());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 247 */       tc.incrementCount(tag);
/* 248 */       wc.setCount("UNK", 1.0D);
/*     */       
/*     */ 
/* 251 */       for (j = wc.keySet().iterator(); j.hasNext();) {
/* 252 */         String first = (String)j.next();
/* 253 */         double prob = Math.log(wc.getCount(first) / tc.getCount(tag));
/* 254 */         ((Counter)this.tagHash.get(tag)).setCount(first, prob);
/*     */       }
/*     */     }
/*     */     String tag;
/*     */     Counter wc;
/*     */     Iterator j;
/*     */   }
/*     */   
/*     */   private void trainUnknownGT(Collection<Tree> trees)
/*     */   {
/* 264 */     Counter twCount = new Counter();
/* 265 */     Counter wtCount = new Counter();
/* 266 */     Counter tagCount = new Counter();
/* 267 */     Counter r1 = new Counter();
/* 268 */     Counter r0 = new Counter();
/* 269 */     Set seenWords = new HashSet();
/*     */     
/* 271 */     int tokens = 0;
/*     */     
/*     */ 
/*     */ 
/* 275 */     for (Tree t : trees) {
/* 276 */       List words = t.taggedYield();
/* 277 */       for (j = words.iterator(); j.hasNext();) {
/* 278 */         tokens++;
/* 279 */         TaggedWord tw = (TaggedWord)j.next();
/* 280 */         WordTag wt = toWordTag(tw);
/* 281 */         String word = wt.word();
/* 282 */         String tag = wt.tag();
/*     */         
/*     */ 
/* 285 */         wtCount.incrementCount(wt);
/* 286 */         twCount.incrementCount(tw);
/*     */         
/* 288 */         tagCount.incrementCount(tag);
/* 289 */         alreadySeen = seenWords.add(word);
/*     */       }
/*     */     }
/*     */     
/*     */     Iterator j;
/*     */     
/*     */     boolean alreadySeen;
/*     */     
/* 297 */     System.err.println("Total tokens: " + tokens + " [num words + numSent (boundarySymbols)]");
/* 298 */     System.err.println("Total WordTag types: " + wtCount.keySet().size());
/* 299 */     System.err.println("Total TaggedWord types: " + twCount.keySet().size() + " [should equal word types!]");
/* 300 */     System.err.println("Total tag types: " + tagCount.keySet().size());
/* 301 */     System.err.println("Total word types: " + seenWords.size());
/*     */     
/*     */ 
/*     */ 
/* 305 */     for (Iterator i = wtCount.keySet().iterator(); i.hasNext();) {
/* 306 */       WordTag wt = (WordTag)i.next();
/* 307 */       if (wtCount.getCount(wt) == 1.0D) {
/* 308 */         r1.incrementCount(wt.tag());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 313 */     for (Iterator i = tagCount.keySet().iterator(); i.hasNext();) {
/* 314 */       tag = (String)i.next();
/* 315 */       for (j = seenWords.iterator(); j.hasNext();) {
/* 316 */         String word = (String)j.next();
/* 317 */         WordTag wt = new WordTag(word, tag);
/*     */         
/* 319 */         if (!wtCount.containsKey(wt)) {
/* 320 */           r0.incrementCount(tag);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     String tag;
/*     */     
/*     */     Iterator j;
/*     */     
/* 329 */     for (Iterator i = tagCount.keySet().iterator(); i.hasNext();) {
/* 330 */       String tag = (String)i.next();
/*     */       
/*     */ 
/* 333 */       double logprob = Math.log(r1.getCount(tag) / (tagCount.getCount(tag) * r0.getCount(tag)));
/*     */       
/* 335 */       this.unknownGT.put(tag, new Double(logprob));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 345 */     System.out.println("Testing unknown matching");
/* 346 */     String s = "刘·革命";
/* 347 */     if (s.matches(".*·.*")) {
/* 348 */       System.out.println("hooray names!");
/*     */     } else {
/* 350 */       System.out.println("Uh-oh names!");
/*     */     }
/* 352 */     String s1 = "３０００";
/* 353 */     if (s1.matches(".*[０１２３４５６７８９１一二三四五六七八九十百千万亿].*")) {
/* 354 */       System.out.println("hooray numbers!");
/*     */     } else {
/* 356 */       System.out.println("Uh-oh numbers!");
/*     */     }
/* 358 */     String s11 = "百分之四十三点二";
/* 359 */     if (s1.matches(".*[０１２３４５６７８９１一二三四五六七八九十百千万亿].*")) {
/* 360 */       System.out.println("hooray numbers!");
/*     */     } else {
/* 362 */       System.out.println("Uh-oh numbers!");
/*     */     }
/* 364 */     String s12 = "百分之三十八点六";
/* 365 */     if (s1.matches(".*[０１２３４５６７８９１一二三四五六七八九十百千万亿].*")) {
/* 366 */       System.out.println("hooray numbers!");
/*     */     } else {
/* 368 */       System.out.println("Uh-oh numbers!");
/*     */     }
/* 370 */     String s2 = "三月";
/* 371 */     if (s2.matches(".*[年月日号]")) {
/* 372 */       System.out.println("hooray dates!");
/*     */     } else {
/* 374 */       System.out.println("Uh-oh dates!");
/*     */     }
/*     */     
/* 377 */     System.out.println("Testing tagged word");
/* 378 */     Counter c = new Counter();
/* 379 */     TaggedWord tw1 = new TaggedWord("w", "t");
/* 380 */     c.incrementCount(tw1);
/* 381 */     TaggedWord tw2 = new TaggedWord("w", "t2");
/* 382 */     System.out.println(c.containsKey(tw2));
/* 383 */     System.out.println(tw1.equals(tw2));
/*     */     
/* 385 */     WordTag wt1 = toWordTag(tw1);
/* 386 */     WordTag wt2 = toWordTag(tw2);
/* 387 */     WordTag wt3 = new WordTag("w", "t2");
/* 388 */     System.out.println(wt1.equals(wt2));
/* 389 */     System.out.println(wt2.equals(wt3));
/*     */   }
/*     */   
/*     */   private static WordTag toWordTag(TaggedWord tw) {
/* 393 */     return new WordTag(tw.word(), tw.tag());
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ChineseUnknownWordModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */