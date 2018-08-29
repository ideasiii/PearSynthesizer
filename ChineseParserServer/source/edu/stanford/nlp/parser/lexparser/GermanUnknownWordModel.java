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
/*     */ public class GermanUnknownWordModel
/*     */   implements Serializable
/*     */ {
/*     */   private static final String encoding = "UTF-8";
/*     */   private static final boolean useFirst = false;
/*  23 */   private static boolean useEnd = true;
/*     */   private static final boolean useGT = false;
/*  25 */   private static boolean useFirstCap = true;
/*     */   
/*  27 */   private static int endLength = 2;
/*     */   
/*     */   private static final String unknown = "UNK";
/*     */   
/*     */   private static final String numberMatch = "[0-9]+\\.?[0-9]*";
/*     */   
/*  33 */   private Map<String, Counter<String>> tagHash = new HashMap();
/*  34 */   private Set seenEnd = new HashSet();
/*     */   
/*  36 */   private Map unknownGT = new HashMap();
/*     */   private static final long serialVersionUID = 221L;
/*     */   
/*     */   public GermanUnknownWordModel(Options.LexOptions op) {
/*  40 */     endLength = op.unknownSuffixSize;
/*  41 */     useEnd = (op.unknownSuffixSize > 0) && (op.useUnknownWordSignatures > 0);
/*  42 */     useFirstCap = op.useUnknownWordSignatures > 0;
/*     */   }
/*     */   
/*     */   public double score(IntTaggedWord itw)
/*     */   {
/*  47 */     return score(itw.toTaggedWord());
/*     */   }
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
/*  59 */     String word = tw.word();
/*  60 */     String tag = tw.tag();
/*     */     
/*     */ 
/*     */     double logProb;
/*     */     
/*     */     double logProb;
/*     */     
/*  67 */     if (word.matches("[0-9]+\\.?[0-9]*")) {
/*     */       double logProb;
/*  69 */       if (tag.equals("CARD")) {
/*  70 */         logProb = 0.0D;
/*     */       } else {
/*  72 */         logProb = Double.NEGATIVE_INFINITY;
/*     */       }
/*     */     } else {
/*     */       double logProb;
/*  76 */       if ((useEnd) || (useFirstCap)) {
/*  77 */         String end = getSignature(word);
/*  78 */         if (!this.seenEnd.contains(end))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*  83 */           end = "UNK";
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  89 */         Counter<String> wordProbs = (Counter)this.tagHash.get(tag);
/*     */         
/*     */         double logProb;
/*     */         
/*  93 */         if (wordProbs == null)
/*     */         {
/*  95 */           logProb = Double.NEGATIVE_INFINITY; } else { double logProb;
/*  96 */           if (wordProbs.keySet().contains(end)) {
/*  97 */             logProb = wordProbs.getCount(end);
/*     */           } else {
/*  99 */             logProb = wordProbs.getCount("UNK");
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 104 */         System.err.println("Warning: no unknown word model in place!\nGiving the combination " + word + " " + tag + " zero probability.");
/* 105 */         logProb = Double.NEGATIVE_INFINITY;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 110 */     return logProb;
/*     */   }
/*     */   
/*     */   private double scoreGT(String tag) {
/*     */     double logProb;
/*     */     double logProb;
/* 116 */     if (this.unknownGT.containsKey(tag)) {
/* 117 */       logProb = ((Double)this.unknownGT.get(tag)).doubleValue();
/*     */     } else {
/* 119 */       logProb = Double.NEGATIVE_INFINITY;
/*     */     }
/* 121 */     return logProb;
/*     */   }
/*     */   
/*     */   private String getSignature(String word) {
/* 125 */     String subStr = "";
/* 126 */     int n = word.length() - 1;
/* 127 */     if (useFirstCap) {
/* 128 */       String first = word.substring(0, 1);
/* 129 */       if (first.equals(first.toUpperCase())) {
/* 130 */         subStr = subStr + "C";
/*     */       } else {
/* 132 */         subStr = subStr + "c";
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 138 */     if (useEnd) {
/* 139 */       subStr = subStr + word.substring(n - endLength > 0 ? n - endLength : 0, n);
/*     */     }
/* 141 */     return subStr;
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
/*     */   public void train(Collection<Tree> trees)
/*     */   {
/* 154 */     if (useEnd) {
/* 155 */       System.out.println("treating unknown word as the average of their equivalents by identity of last three letters.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 161 */     trainUnknownGT(trees);
/*     */     
/* 163 */     HashMap<String, Counter<String>> c = new HashMap();
/*     */     
/* 165 */     Counter<String> tc = new Counter();
/*     */     
/* 167 */     for (Tree t : trees) {
/* 168 */       List words = t.taggedYield();
/* 169 */       for (j = words.iterator(); j.hasNext();) {
/* 170 */         TaggedWord tw = (TaggedWord)j.next();
/* 171 */         String word = tw.word();
/* 172 */         String subString = getSignature(word);
/*     */         
/* 174 */         String tag = tw.tag();
/* 175 */         if (!c.containsKey(tag)) {
/* 176 */           c.put(tag, new Counter());
/*     */         }
/* 178 */         ((Counter)c.get(tag)).incrementCount(subString);
/*     */         
/* 180 */         tc.incrementCount(tag);
/*     */         
/* 182 */         this.seenEnd.add(subString);
/*     */       }
/*     */     }
/*     */     
/*     */     Iterator j;
/* 187 */     for (Iterator i = c.keySet().iterator(); i.hasNext();) {
/* 188 */       tag = (String)i.next();
/* 189 */       wc = (Counter)c.get(tag);
/*     */       
/*     */ 
/* 192 */       if (!this.tagHash.containsKey(tag)) {
/* 193 */         this.tagHash.put(tag, new Counter());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 198 */       tc.incrementCount(tag);
/* 199 */       wc.setCount("UNK", 1.0D);
/*     */       
/*     */ 
/* 202 */       for (j = wc.keySet().iterator(); j.hasNext();) {
/* 203 */         String end = (String)j.next();
/* 204 */         double prob = Math.log(wc.getCount(end) / tc.getCount(tag));
/* 205 */         ((Counter)this.tagHash.get(tag)).setCount(end, prob);
/*     */       }
/*     */     }
/*     */     String tag;
/*     */     Counter wc;
/*     */     Iterator j;
/*     */   }
/*     */   
/*     */   private void trainUnknownGT(Collection trees)
/*     */   {
/* 215 */     Counter twCount = new Counter();
/* 216 */     Counter wtCount = new Counter();
/* 217 */     Counter tagCount = new Counter();
/* 218 */     Counter r1 = new Counter();
/* 219 */     Counter r0 = new Counter();
/* 220 */     Set seenWords = new HashSet();
/*     */     
/* 222 */     int tokens = 0;
/*     */     
/*     */ 
/*     */ 
/* 226 */     for (Iterator i = trees.iterator(); i.hasNext();) {
/* 227 */       Tree t = (Tree)i.next();
/* 228 */       List words = t.taggedYield();
/* 229 */       for (j = words.iterator(); j.hasNext();) {
/* 230 */         tokens++;
/* 231 */         TaggedWord tw = (TaggedWord)j.next();
/* 232 */         WordTag wt = toWordTag(tw);
/* 233 */         String word = wt.word();
/* 234 */         String tag = wt.tag();
/*     */         
/*     */ 
/* 237 */         wtCount.incrementCount(wt);
/* 238 */         twCount.incrementCount(tw);
/*     */         
/* 240 */         tagCount.incrementCount(tag);
/* 241 */         alreadySeen = seenWords.add(word);
/*     */       }
/*     */     }
/*     */     
/*     */     Iterator j;
/*     */     
/*     */     boolean alreadySeen;
/*     */     
/* 249 */     System.out.println("Total tokens: " + tokens);
/* 250 */     System.out.println("Total WordTag types: " + wtCount.keySet().size());
/* 251 */     System.out.println("Total TaggedWord types: " + twCount.keySet().size());
/* 252 */     System.out.println("Total tag types: " + tagCount.keySet().size());
/* 253 */     System.out.println("Total word types: " + seenWords.size());
/*     */     
/*     */ 
/*     */ 
/* 257 */     for (Iterator i = wtCount.keySet().iterator(); i.hasNext();) {
/* 258 */       WordTag wt = (WordTag)i.next();
/* 259 */       if (wtCount.getCount(wt) == 1.0D) {
/* 260 */         r1.incrementCount(wt.tag());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 265 */     for (Iterator i = tagCount.keySet().iterator(); i.hasNext();) {
/* 266 */       tag = (String)i.next();
/* 267 */       for (j = seenWords.iterator(); j.hasNext();) {
/* 268 */         String word = (String)j.next();
/* 269 */         WordTag wt = new WordTag(word, tag);
/*     */         
/* 271 */         if (!wtCount.keySet().contains(wt)) {
/* 272 */           r0.incrementCount(tag);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     String tag;
/*     */     
/*     */     Iterator j;
/*     */     
/* 281 */     for (Iterator i = tagCount.keySet().iterator(); i.hasNext();) {
/* 282 */       String tag = (String)i.next();
/*     */       
/*     */ 
/* 285 */       double logprob = Math.log(r1.getCount(tag) / (tagCount.getCount(tag) * r0.getCount(tag)));
/*     */       
/* 287 */       this.unknownGT.put(tag, new Double(logprob));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static WordTag toWordTag(TaggedWord tw)
/*     */   {
/* 299 */     return new WordTag(tw.word(), tw.tag());
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\GermanUnknownWordModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */