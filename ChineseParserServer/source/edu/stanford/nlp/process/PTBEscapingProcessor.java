/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.ling.BasicDocument;
/*     */ import edu.stanford.nlp.ling.Document;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PTBEscapingProcessor
/*     */   extends AbstractListProcessor
/*     */   implements Function<List<HasWord>, List<HasWord>>
/*     */ {
/*     */   protected Map<String, String> stringSubs;
/*     */   protected char[] oldChars;
/*  25 */   protected static final String[] oldStrings = { "(", ")", "[", "]", "{", "}", "*", "/", "_" };
/*  26 */   protected static final String[] newStrings = { "-LRB-", "-RRB-", "-LCB-", "-RCB-", "-LCB-", "-RCB-", "\\*", "\\/", "-" };
/*     */   
/*     */ 
/*  29 */   protected static final char[] defaultOldChars = { '*', '/' };
/*     */   
/*  31 */   protected boolean fixQuotes = true;
/*     */   
/*     */   public PTBEscapingProcessor() {
/*  34 */     this(makeStringMap(), defaultOldChars, true);
/*     */   }
/*     */   
/*     */   public PTBEscapingProcessor(Map<String, String> stringSubs, char[] oldChars, boolean fixQuotes) {
/*  38 */     this.stringSubs = stringSubs;
/*  39 */     this.oldChars = oldChars;
/*  40 */     this.fixQuotes = fixQuotes;
/*     */   }
/*     */   
/*     */   protected static Map<String, String> makeStringMap() {
/*  44 */     Map<String, String> map = new HashMap();
/*  45 */     for (int i = 0; i < oldStrings.length; i++) {
/*  46 */       map.put(oldStrings[i], newStrings[i]);
/*     */     }
/*  48 */     return map;
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
/*     */   public List<HasWord> apply(List<HasWord> hasWordsList)
/*     */   {
/*  64 */     return process(hasWordsList);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List process(List input)
/*     */   {
/*  72 */     List output = new ArrayList();
/*     */     
/*     */ 
/*  75 */     for (Iterator i = input.iterator(); i.hasNext();) {
/*  76 */       HasWord h = (HasWord)i.next();
/*  77 */       String s = h.word();
/*  78 */       String newS = (String)this.stringSubs.get(s);
/*  79 */       if (newS != null) {
/*  80 */         h.setWord(newS);
/*     */       } else {
/*  82 */         h.setWord(escapeString(s));
/*     */       }
/*  84 */       output.add(h);
/*     */     }
/*  86 */     if (this.fixQuotes) {
/*  87 */       return fixQuotes(output);
/*     */     }
/*  89 */     return output;
/*     */   }
/*     */   
/*     */   private List fixQuotes(List input) {
/*  93 */     int inputSize = input.size();
/*  94 */     LinkedList result = new LinkedList();
/*  95 */     if (inputSize == 0) {
/*  96 */       return result;
/*     */     }
/*     */     
/*     */ 
/* 100 */     if (((HasWord)input.get(inputSize - 1)).word().equals("\""))
/*     */     {
/* 102 */       boolean begin = false;
/* 103 */       for (int i = inputSize - 1; i >= 0; i--) {
/* 104 */         HasWord hw = (HasWord)input.get(i);
/* 105 */         String tok = hw.word();
/* 106 */         if (tok.equals("\"")) {
/* 107 */           if (begin) {
/* 108 */             hw.setWord("``");
/* 109 */             begin = false;
/*     */           } else {
/* 111 */             hw.setWord("''");
/* 112 */             begin = true;
/*     */           }
/*     */         }
/* 115 */         result.addFirst(hw);
/*     */       }
/*     */     }
/*     */     else {
/* 119 */       boolean begin = true;
/* 120 */       for (int i = 0; i < inputSize; i++) {
/* 121 */         HasWord hw = (HasWord)input.get(i);
/* 122 */         String tok = hw.word();
/* 123 */         if (tok.equals("\"")) {
/* 124 */           if (begin) {
/* 125 */             hw.setWord("``");
/* 126 */             begin = false;
/*     */           } else {
/* 128 */             hw.setWord("''");
/* 129 */             begin = true;
/*     */           }
/*     */         }
/* 132 */         result.addLast(hw);
/*     */       }
/*     */     }
/* 135 */     return result;
/*     */   }
/*     */   
/*     */   private String escapeString(String s) {
/* 139 */     StringBuilder buff = new StringBuilder();
/* 140 */     for (int i = 0; i < s.length(); i++) {
/* 141 */       char curChar = s.charAt(i);
/* 142 */       if (curChar == '\\')
/*     */       {
/* 144 */         buff.append(curChar);
/* 145 */         i++;
/* 146 */         if (i < s.length()) {
/* 147 */           curChar = s.charAt(i);
/* 148 */           buff.append(curChar);
/*     */         }
/*     */       }
/*     */       else {
/* 152 */         for (int j = 0; j < this.oldChars.length; j++) {
/* 153 */           if (curChar == this.oldChars[j]) {
/* 154 */             buff.append('\\');
/* 155 */             break;
/*     */           }
/*     */         }
/*     */         
/* 159 */         buff.append(curChar);
/*     */       }
/*     */     }
/* 162 */     return buff.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 173 */     if (args.length != 1) {
/* 174 */       System.out.println("usage: java edu.stanford.nlp.process.PTBEscapingProcessor fileOrUrl");
/* 175 */       System.exit(0);
/*     */     }
/* 177 */     String filename = args[0];
/*     */     try {
/* 179 */       Document d = null;
/* 180 */       if (filename.startsWith("http://")) {
/* 181 */         Document dpre = new BasicDocument(WhitespaceTokenizer.factory()).init(new URL(filename));
/* 182 */         Processor notags = new StripTagsProcessor();
/* 183 */         d = notags.processDocument(dpre);
/*     */       } else {
/* 185 */         d = new BasicDocument(WhitespaceTokenizer.factory()).init(new File(filename));
/*     */       }
/* 187 */       Processor proc = new PTBEscapingProcessor();
/* 188 */       Document newD = proc.processDocument(d);
/* 189 */       for (it = newD.iterator(); it.hasNext();) {
/* 190 */         HasWord word = (HasWord)it.next();
/* 191 */         System.out.println(word);
/*     */       }
/*     */     } catch (Exception e) { Iterator it;
/* 194 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\PTBEscapingProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */