/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.TaggedWord;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.Serializable;
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
/*     */ public class IntTaggedWord
/*     */   implements Serializable, Comparable<IntTaggedWord>
/*     */ {
/*     */   public static final int ANY_WORD_INT = -1;
/*     */   public static final int ANY_TAG_INT = -1;
/*     */   public static final int STOP_WORD_INT = -2;
/*     */   public static final int STOP_TAG_INT = -2;
/*     */   public static final String ANY = ".*.";
/*     */   public static final String STOP = "STOP";
/*     */   private static Numberer wordNumberer;
/*     */   private static Numberer tagNumberer;
/*     */   public int word;
/*     */   public short tag;
/*     */   
/*     */   Numberer wordNumberer()
/*     */   {
/*  33 */     if (wordNumberer == null) {
/*  34 */       wordNumberer = Numberer.getGlobalNumberer("words");
/*     */     }
/*  36 */     return wordNumberer;
/*     */   }
/*     */   
/*     */   Numberer tagNumberer() {
/*  40 */     if (tagNumberer == null) {
/*  41 */       tagNumberer = Numberer.getGlobalNumberer("tags");
/*     */     }
/*  43 */     return tagNumberer;
/*     */   }
/*     */   
/*     */   static void resetNumberers() {
/*  47 */     wordNumberer = null;
/*  48 */     tagNumberer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tag()
/*     */   {
/*  56 */     return this.tag;
/*     */   }
/*     */   
/*     */ 
/*  60 */   public int word() { return this.word; }
/*     */   
/*     */   public String wordString() {
/*     */     String wordStr;
/*     */     String wordStr;
/*  65 */     if (this.word >= 0) {
/*  66 */       wordStr = (String)wordNumberer().object(this.word); } else { String wordStr;
/*  67 */       if (this.word == -1) {
/*  68 */         wordStr = ".*.";
/*     */       } else
/*  70 */         wordStr = "STOP";
/*     */     }
/*  72 */     return wordStr;
/*     */   }
/*     */   
/*     */   public String tagString() { String tagStr;
/*     */     String tagStr;
/*  77 */     if (this.tag >= 0) {
/*  78 */       tagStr = (String)tagNumberer().object(this.tag); } else { String tagStr;
/*  79 */       if (this.tag == -1) {
/*  80 */         tagStr = ".*.";
/*     */       } else
/*  82 */         tagStr = "STOP";
/*     */     }
/*  84 */     return tagStr;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  88 */     return this.word ^ this.tag << 16;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  92 */     if (this == o)
/*  93 */       return true;
/*  94 */     if ((o instanceof IntTaggedWord)) {
/*  95 */       IntTaggedWord i = (IntTaggedWord)o;
/*  96 */       return (this.word == i.word) && (this.tag == i.tag);
/*     */     }
/*  98 */     return false;
/*     */   }
/*     */   
/*     */   public int compareTo(IntTaggedWord that)
/*     */   {
/* 103 */     if (this.tag != that.tag) {
/* 104 */       return this.tag - that.tag;
/*     */     }
/* 106 */     return this.word - that.word;
/*     */   }
/*     */   
/*     */ 
/* 110 */   private static final char[] charsToEscape = { '"' };
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public String toLexicalEntry() {
/* 114 */     String wordStr = wordString();
/* 115 */     String tagStr = tagString();
/* 116 */     return "\"" + StringUtils.escapeString(tagStr, charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(wordStr, charsToEscape, '\\') + "\"";
/*     */   }
/*     */   
/*     */   public String toString() {
/* 120 */     return wordString() + "/" + tagString();
/*     */   }
/*     */   
/*     */   public String toString(String arg) {
/* 124 */     if (arg.equals("verbose")) {
/* 125 */       return wordString() + "[" + this.word + "]/" + tagString() + "[" + this.tag + "]";
/*     */     }
/* 127 */     return toString();
/*     */   }
/*     */   
/*     */   public IntTaggedWord(int word, int tag)
/*     */   {
/* 132 */     this.word = word;
/* 133 */     this.tag = ((short)tag);
/*     */   }
/*     */   
/*     */   public TaggedWord toTaggedWord() {
/* 137 */     String wordStr = wordString();
/* 138 */     String tagStr = tagString();
/* 139 */     return new TaggedWord(wordStr, tagStr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntTaggedWord(String s, char splitChar)
/*     */   {
/* 148 */     this(extractWord(s, splitChar), extractTag(s, splitChar));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String extractWord(String s, char splitChar)
/*     */   {
/* 156 */     int n = s.lastIndexOf(splitChar);
/* 157 */     String result = s.substring(0, n);
/*     */     
/* 159 */     return result;
/*     */   }
/*     */   
/*     */   private static String extractTag(String s, char splitChar) {
/* 163 */     int n = s.lastIndexOf(splitChar);
/* 164 */     String result = s.substring(n + 1);
/*     */     
/* 166 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public IntTaggedWord(String wordString, String tagString)
/*     */   {
/* 173 */     if (wordString.equals(".*.")) {
/* 174 */       this.word = -1;
/* 175 */     } else if (wordString.equals("STOP")) {
/* 176 */       this.word = -2;
/*     */     } else {
/* 178 */       this.word = wordNumberer().number(wordString);
/*     */     }
/* 180 */     if (tagString.equals(".*.")) {
/* 181 */       this.tag = -1;
/* 182 */     } else if (tagString.equals("STOP")) {
/* 183 */       this.tag = -2;
/*     */     } else {
/* 185 */       this.tag = ((short)tagNumberer().number(tagString));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\IntTaggedWord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */