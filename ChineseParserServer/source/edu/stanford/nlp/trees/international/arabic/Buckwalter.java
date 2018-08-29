/*     */ package edu.stanford.nlp.trees.international.arabic;
/*     */ 
/*     */ import edu.stanford.nlp.io.EncodingPrintWriter.err;
/*     */ import edu.stanford.nlp.io.EncodingPrintWriter.out;
/*     */ import edu.stanford.nlp.process.Function;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
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
/*     */ public class Buckwalter
/*     */   implements Function<String, String>
/*     */ {
/*  42 */   private char[] arabicChars = { 'ء', 'آ', 'أ', 'ؤ', 'إ', 'ئ', 'ا', 'ب', 'ة', 'ت', 'ث', 'ج', 'ح', 'خ', 'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض', 'ط', 'ظ', 'ع', 'غ', 'ـ', 'ف', 'ق', 'ك', 'ل', 'م', 'ن', 'ه', 'و', 'ى', 'ي', 'ً', 'ٌ', 'ٍ', 'َ', 'ُ', 'ِ', 'ّ', 'ْ', 'ٰ', 'ٱ', 'پ', 'چ', 'ژ', 'ڤ', 'گ', 'إ', 'أ', 'ؤ', '،', '؛', '؟', '٪', '٫', '٬', '٭', '۔', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩', '«', '»' };
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
/*  65 */   private char[] buckChars = { '\'', '|', '>', '&', '<', '}', 'A', 'b', 'p', 't', 'v', 'j', 'H', 'x', 'd', '*', 'r', 'z', 's', '$', 'S', 'D', 'T', 'Z', 'E', 'g', '_', 'f', 'q', 'k', 'l', 'm', 'n', 'h', 'w', 'Y', 'y', 'F', 'N', 'K', 'a', 'u', 'i', '~', 'o', '`', '{', 'P', 'J', 'R', 'V', 'G', 'I', 'O', 'W', ',', ';', '?', '%', '.', ',', '*', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '"', '"' };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean u2b;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HashMap<Character, Character> a2b;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HashMap<Character, Character> b2a;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Counter<String> unmappable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final boolean DEBUG = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final boolean PASS_ASCII_IN_UNICODE = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String apply(String in)
/*     */   {
/* 126 */     return convert(in, this.u2b);
/*     */   }
/*     */   
/*     */   private String convert(String in, boolean unicodeToBuckwalter)
/*     */   {
/* 131 */     StringBuilder res = new StringBuilder(in.length());
/*     */     
/* 133 */     int i = 0; for (int sz = in.length(); i < sz; i++) {
/* 134 */       Character inCh = new Character(in.charAt(i));
/* 135 */       Character outCh; Character outCh; if (unicodeToBuckwalter) { Character outCh;
/* 136 */         if (inCh.charValue() < '') {
/* 137 */           outCh = inCh;
/*     */         } else {
/* 139 */           outCh = (Character)this.a2b.get(inCh);
/*     */         }
/*     */       } else {
/* 142 */         outCh = (Character)this.b2a.get(inCh);
/*     */       }
/* 144 */       if (outCh == null)
/*     */       {
/* 146 */         String key = inCh + "[U+" + StringUtils.padLeft(Integer.toString(inCh.charValue(), 16).toUpperCase(), 4, '0') + "]";
/*     */         
/* 148 */         this.unmappable.incrementCount(key);
/*     */         
/* 150 */         res.append(inCh);
/*     */       } else {
/* 152 */         res.append(outCh);
/*     */       }
/*     */     }
/* 155 */     return res.toString();
/*     */   }
/*     */   
/*     */   public String buckwalterToUnicode(String in) {
/* 159 */     return convert(in, false);
/*     */   }
/*     */   
/*     */   public String unicodeToBuckwalter(String in) {
/* 163 */     return convert(in, true);
/*     */   }
/*     */   
/*     */   public Buckwalter() {
/* 167 */     this(false);
/*     */   }
/*     */   
/*     */   public Buckwalter(boolean unicodeToBuckwalter)
/*     */   {
/* 108 */     if (this.arabicChars.length != this.buckChars.length) {
/* 109 */       throw new RuntimeException("Buckwalter: Bad char arrays");
/*     */     }
/* 111 */     this.a2b = new HashMap(this.arabicChars.length);
/* 112 */     this.b2a = new HashMap(this.buckChars.length);
/* 113 */     for (int i = 0; i < this.arabicChars.length; i++) {
/* 114 */       Character ca = new Character(this.arabicChars[i]);
/* 115 */       Character cb = new Character(this.buckChars[i]);
/* 116 */       this.a2b.put(ca, cb);
/* 117 */       this.b2a.put(cb, ca);
/*     */     }
/*     */     
/* 120 */     this.unmappable = new Counter();
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
/* 171 */     this.u2b = unicodeToBuckwalter;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/* 175 */     if ((args.length < 1) || ((!args[0].equals("-a2b")) && (!args[0].equals("-b2a"))))
/*     */     {
/* 177 */       System.err.println("usage: java Buckwalter [-a2b|-b2a] words+ OR, as a filter, just [-a2b|-b2a]");
/* 178 */       return;
/*     */     }
/*     */     
/*     */ 
/* 182 */     Buckwalter b = new Buckwalter(args[0].equals("-a2b"));
/* 183 */     if (args.length > 1) {
/* 184 */       for (int j = 1; j < args.length; j++) {
/* 185 */         EncodingPrintWriter.out.println(args[j] + " -> " + b.apply(args[j]), "utf-8");
/*     */       }
/*     */     } else {
/* 188 */       BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
/*     */       String line;
/* 190 */       while ((line = br.readLine()) != null) {
/* 191 */         EncodingPrintWriter.out.println(b.apply(line), "utf-8");
/*     */       }
/*     */     }
/*     */     
/* 195 */     if (b.unmappable.keySet().size() > 0) {
/* 196 */       EncodingPrintWriter.err.println("Characters that could not be converted [passed through!]:", "utf-8");
/* 197 */       EncodingPrintWriter.err.println(b.unmappable.toString(), "utf-8");
/*     */     } else {
/* 199 */       EncodingPrintWriter.err.println("All characters successfully converted!", "utf-8");
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\arabic\Buckwalter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */