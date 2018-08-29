/*     */ package edu.stanford.nlp.ling;
/*     */ 
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
/*     */ public class Sentence<T extends HasWord>
/*     */   extends ArrayList<T>
/*     */ {
/*     */   public Sentence() {}
/*     */   
/*     */   public Sentence(Collection<T> w)
/*     */   {
/*  36 */     super(w);
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
/*     */   public static Sentence<TaggedWord> toSentence(List<String> lex, List<String> tags)
/*     */   {
/*  52 */     Sentence<TaggedWord> sent = new Sentence();
/*  53 */     int ls = lex.size();
/*  54 */     int ts = tags.size();
/*  55 */     if (ls != ts) {
/*  56 */       throw new IllegalArgumentException("Sentence.toSentence: lengths differ");
/*     */     }
/*  58 */     for (int i = 0; i < ls; i++) {
/*  59 */       sent.add(new TaggedWord((String)lex.get(i), (String)tags.get(i)));
/*     */     }
/*  61 */     return sent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Sentence<Word> toSentence(String... words)
/*     */   {
/*  72 */     Sentence<Word> sent = new Sentence();
/*  73 */     for (String str : words) {
/*  74 */       sent.add(new Word(str));
/*     */     }
/*  76 */     return sent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWords(Collection<T> wordList)
/*     */   {
/*  85 */     clear();
/*  86 */     addAll(wordList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T getHasWord(int index)
/*     */   {
/*  98 */     return (HasWord)get(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int length()
/*     */   {
/* 109 */     return size();
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
/*     */   public String toString()
/*     */   {
/* 123 */     return toString(true);
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
/*     */   public String toString(boolean justValue)
/*     */   {
/* 140 */     StringBuilder s = new StringBuilder();
/* 141 */     for (Iterator wordIterator = iterator(); wordIterator.hasNext();) {
/* 142 */       Object o = wordIterator.next();
/* 143 */       if ((justValue) && ((o instanceof Label))) {
/* 144 */         s.append(((Label)o).value());
/*     */       } else {
/* 146 */         s.append(o.toString());
/*     */       }
/* 148 */       if (wordIterator.hasNext()) {
/* 149 */         s.append(" ");
/*     */       }
/*     */     }
/* 152 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\Sentence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */