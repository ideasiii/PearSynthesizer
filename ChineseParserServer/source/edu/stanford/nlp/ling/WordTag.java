/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
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
/*     */ public class WordTag
/*     */   implements Label, HasWord, HasTag, Comparable<WordTag>
/*     */ {
/*     */   private String word;
/*     */   private String tag;
/*  27 */   private static String divider = "/";
/*     */   
/*     */   public WordTag(String word) {
/*  30 */     this.word = word;
/*  31 */     setTag(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public WordTag(Label word)
/*     */   {
/*  38 */     this(word.value());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordTag() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordTag(String word, String tag)
/*     */   {
/*  52 */     this(word);
/*  53 */     setTag(tag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WordTag(Label word, Label tag)
/*     */   {
/*  65 */     this(word);
/*  66 */     setTag(tag.value());
/*     */   }
/*     */   
/*     */   public static WordTag valueOf(String s) {
/*  70 */     WordTag result = new WordTag();
/*  71 */     result.setFromString(s);
/*  72 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String value()
/*     */   {
/*  81 */     return this.word;
/*     */   }
/*     */   
/*     */   public String word() {
/*  85 */     return value();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(String value)
/*     */   {
/*  94 */     this.word = value;
/*     */   }
/*     */   
/*     */   public String tag() {
/*  98 */     return this.tag;
/*     */   }
/*     */   
/*     */   public void setWord(String word) {
/* 102 */     setValue(word);
/*     */   }
/*     */   
/*     */   public void setTag(String tag) {
/* 106 */     this.tag = tag;
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
/*     */   public String toString()
/*     */   {
/* 119 */     String tag = tag();
/* 120 */     if (tag == null) {
/* 121 */       return word();
/*     */     }
/* 123 */     return word() + divider + tag;
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
/*     */ 
/*     */   public void setFromString(String wordTagString)
/*     */   {
/* 141 */     int where = wordTagString.lastIndexOf(divider);
/* 142 */     if (where >= 0) {
/* 143 */       setWord(wordTagString.substring(0, where).intern());
/* 144 */       setTag(wordTagString.substring(where + 1).intern());
/*     */     } else {
/* 146 */       setWord(wordTagString.intern());
/* 147 */       setTag(null);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 152 */     if (this == o) return true;
/* 153 */     if (!(o instanceof WordTag)) return false;
/* 154 */     WordTag wordTag = (WordTag)o;
/* 155 */     if (this.tag != null ? !this.tag.equals(wordTag.tag) : wordTag.tag != null) return false;
/* 156 */     if (this.word != null ? !this.word.equals(wordTag.word) : wordTag.word != null) return false;
/* 157 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 162 */     int result = this.word != null ? this.word.hashCode() : 0;
/* 163 */     result = 29 * result + (this.tag != null ? this.tag.hashCode() : 0);
/* 164 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(WordTag wordTag)
/*     */   {
/* 175 */     int first = word().compareTo(wordTag.word());
/* 176 */     if (first != 0) {
/* 177 */       return first;
/*     */     }
/* 179 */     if (tag() == null) {
/* 180 */       if (wordTag.tag() == null) {
/* 181 */         return 0;
/*     */       }
/* 183 */       return -1;
/*     */     }
/* 185 */     return tag().compareTo(wordTag.tag());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class LabelFactoryHolder
/*     */   {
/* 193 */     private static final LabelFactory lf = new WordTagFactory();
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
/* 205 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LabelFactory factory()
/*     */   {
/* 215 */     return LabelFactoryHolder.lf;
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
/* 230 */     divider = divider;
/*     */   }
/*     */   
/*     */   public void read(DataInputStream in)
/*     */   {
/*     */     try {
/* 236 */       this.word = in.readUTF();
/* 237 */       this.tag = in.readUTF();
/*     */     }
/*     */     catch (Exception e) {
/* 240 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void save(DataOutputStream out)
/*     */   {
/*     */     try {
/* 247 */       out.writeUTF(this.word);
/* 248 */       out.writeUTF(this.tag);
/*     */     } catch (Exception e) {
/* 250 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\WordTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */