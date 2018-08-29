/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ import edu.stanford.nlp.process.Morphology;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WordLemmaTag
/*     */   implements Label, Comparable<WordLemmaTag>, HasWord, HasTag
/*     */ {
/*     */   private String word;
/*     */   private String lemma;
/*     */   private String tag;
/*  22 */   private static String divider = "/";
/*     */   
/*     */   public WordLemmaTag(String word) {
/*  25 */     this.word = word;
/*  26 */     this.lemma = null;
/*  27 */     setTag(null);
/*     */   }
/*     */   
/*     */   public WordLemmaTag(Label word) {
/*  31 */     this(word.value());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordLemmaTag() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordLemmaTag(String word, String tag)
/*     */   {
/*  45 */     WordTag wT = new WordTag(word, tag);
/*  46 */     this.word = word;
/*  47 */     this.lemma = Morphology.stemStatic(wT).word();
/*  48 */     setTag(tag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordLemmaTag(String word, String lemma, String tag)
/*     */   {
/*  60 */     this(word);
/*  61 */     this.lemma = lemma;
/*  62 */     setTag(tag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordLemmaTag(Label word, Label tag)
/*     */   {
/*  74 */     this(word);
/*  75 */     WordTag wT = new WordTag(word, tag);
/*  76 */     this.lemma = Morphology.stemStatic(wT).word();
/*  77 */     setTag(tag.value());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String value()
/*     */   {
/*  87 */     return this.word;
/*     */   }
/*     */   
/*     */   public String word() {
/*  91 */     return value();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(String value)
/*     */   {
/* 100 */     this.word = value;
/*     */   }
/*     */   
/*     */   public void setWord(String word) {
/* 104 */     setValue(word);
/*     */   }
/*     */   
/*     */   public void setLemma(String lemma) {
/* 108 */     this.lemma = lemma;
/*     */   }
/*     */   
/*     */   public void setTag(String tag) {
/* 112 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public String tag() {
/* 116 */     return this.tag;
/*     */   }
/*     */   
/*     */   public String lemma() {
/* 120 */     return this.lemma;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 130 */     return word() + divider + this.lemma + divider + this.tag;
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
/*     */   public void setFromString(String labelStr)
/*     */   {
/* 146 */     int first = labelStr.indexOf(divider);
/* 147 */     int second = labelStr.lastIndexOf(divider);
/* 148 */     if (first == second) {
/* 149 */       setWord(labelStr.substring(0, first));
/* 150 */       setTag(labelStr.substring(first + 1));
/* 151 */       setLemma(Morphology.stemStatic(labelStr.substring(0, first), labelStr.substring(first + 1)).word());
/* 152 */     } else if (first >= 0) {
/* 153 */       setWord(labelStr.substring(0, first));
/* 154 */       setLemma(labelStr.substring(first + 1, second));
/* 155 */       setTag(labelStr.substring(second + 1));
/*     */     } else {
/* 157 */       setWord(labelStr);
/* 158 */       setLemma(null);
/* 159 */       setTag(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 168 */     return ((obj instanceof WordLemmaTag)) && (word().equals(((WordLemmaTag)obj).word())) && (lemma().equals(((WordLemmaTag)obj).lemma())) && (tag().equals(((WordLemmaTag)obj).tag()));
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
/*     */   public int compareTo(WordLemmaTag wordLemmaTag)
/*     */   {
/* 181 */     int first = word().compareTo(wordLemmaTag.word());
/* 182 */     if (first != 0)
/* 183 */       return first;
/* 184 */     int second = lemma().compareTo(wordLemmaTag.lemma());
/* 185 */     if (second != 0) {
/* 186 */       return second;
/*     */     }
/* 188 */     return tag().compareTo(wordLemmaTag.tag());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabelFactory labelFactory()
/*     */   {
/* 200 */     return new WordLemmaTagFactory();
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
/*     */   public static void setDivider(String divider)
/*     */   {
/* 215 */     divider = divider;
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/* 220 */     WordLemmaTag wLT = new WordLemmaTag();
/* 221 */     wLT.setFromString("hunter/NN");
/*     */     
/* 223 */     System.out.println(wLT.word());
/* 224 */     System.out.println(wLT.lemma());
/* 225 */     System.out.println(wLT.tag());
/*     */     
/* 227 */     WordLemmaTag wLT2 = new WordLemmaTag();
/* 228 */     wLT2.setFromString("bought/buy/V");
/* 229 */     System.out.println(wLT2.word());
/* 230 */     System.out.println(wLT2.lemma());
/* 231 */     System.out.println(wLT2.tag());
/*     */     
/* 233 */     WordLemmaTag wLT3 = new WordLemmaTag();
/* 234 */     wLT2.setFromString("life");
/* 235 */     System.out.println(wLT3.word());
/* 236 */     System.out.println(wLT3.lemma());
/* 237 */     System.out.println(wLT3.tag());
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\WordLemmaTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */