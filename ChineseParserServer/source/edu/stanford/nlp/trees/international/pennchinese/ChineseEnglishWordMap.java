/*     */ package edu.stanford.nlp.trees.international.pennchinese;
/*     */ 
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class ChineseEnglishWordMap
/*     */   implements Serializable
/*     */ {
/*  48 */   private Map<String, Set<String>> map = new HashMap(10000);
/*     */   
/*     */   private static final String defaultPath = "cedict_ts.u8";
/*     */   
/*     */   private static final String defaultPath2 = "/u/nlp/data/chinese-english-dictionary/cedict_ts.u8";
/*     */   private static final String ENV_VARIABLE = "CEDICT";
/*     */   private static final String defaultPattern = "[^ ]+ ([^ ]+)[^/]+/(.+)/";
/*     */   private static final String defaultDelimiter = "[/;]";
/*     */   private static final String defaultCharset = "UTF-8";
/*  57 */   private static final String[] punctuations = { "（.*?）", "\\(.*?\\)", "<.*?>", "[″⃝○◯‹〈⟨›〉⟩«⟪»⟫⌈⌋⟦⟧〰～“‶”″⌇〜〒⧄《》　]", "^to " };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final boolean DEBUG = false;
/*     */   
/*     */ 
/*     */ 
/*  66 */   private boolean normalized = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class SingletonHolder
/*     */   {
/*  74 */     private static final ChineseEnglishWordMap INSTANCE = new ChineseEnglishWordMap();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ChineseEnglishWordMap getInstance()
/*     */   {
/*  86 */     return SingletonHolder.INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsKey(String key)
/*     */   {
/*  95 */     key = key.toLowerCase();
/*  96 */     key = key.trim();
/*  97 */     return this.map.containsKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getAllTranslations(String key)
/*     */   {
/* 106 */     key = key.toLowerCase();
/* 107 */     key = key.trim();
/* 108 */     return (Set)this.map.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFirstTranslation(String key)
/*     */   {
/* 117 */     key = key.toLowerCase();
/* 118 */     key = key.trim();
/* 119 */     Set<String> strings = (Set)this.map.get(key);
/* 120 */     if (strings == null) return null;
/* 121 */     return (String)strings.iterator().next();
/*     */   }
/*     */   
/*     */   public void readCEDict(String dictPath) {
/* 125 */     readCEDict(dictPath, "[^ ]+ ([^ ]+)[^/]+/(.+)/", "[/;]", "UTF-8");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String normalize(String t)
/*     */   {
/* 133 */     if (!this.normalized) {
/* 134 */       return t;
/*     */     }
/* 136 */     for (String punc : punctuations) {
/* 137 */       t = t.replaceAll(punc, "");
/*     */     }
/* 139 */     t = t.trim();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 144 */     return t;
/*     */   }
/*     */   
/*     */   private Set<String> normalize(Set<String> trans) {
/* 148 */     if (!this.normalized) {
/* 149 */       return trans;
/*     */     }
/*     */     
/* 152 */     Set<String> set = new HashSet();
/*     */     
/* 154 */     for (String t : trans) {
/* 155 */       t = normalize(t);
/* 156 */       if (!t.equals("")) {
/* 157 */         set.add(t);
/*     */       }
/*     */     }
/* 160 */     return set;
/*     */   }
/*     */   
/*     */   public void readCEDict(String dictPath, String pattern, String delimiter, String charset) {
/*     */     try {
/* 165 */       BufferedReader infile = new BufferedReader(new InputStreamReader(new FileInputStream(dictPath), charset));
/*     */       
/* 167 */       Pattern p = Pattern.compile(pattern);
/* 168 */       for (String line = infile.readLine(); line != null; line = infile.readLine()) {
/* 169 */         Matcher m = p.matcher(line);
/* 170 */         if (m.matches()) {
/* 171 */           String word = m.group(1).toLowerCase();
/* 172 */           word = word.trim();
/* 173 */           String transGroup = m.group(2);
/* 174 */           String[] trans = transGroup.split(delimiter);
/*     */           
/* 176 */           if (this.map.containsKey(word)) {
/* 177 */             Set<String> oldtrans = (Set)this.map.get(word);
/* 178 */             for (String t : trans) {
/* 179 */               t = normalize(t);
/* 180 */               if ((!t.equals("")) && 
/* 181 */                 (!oldtrans.contains(t))) {
/* 182 */                 oldtrans.add(t);
/*     */               }
/*     */             }
/*     */           }
/*     */           else {
/* 187 */             Set<String> transList = new LinkedHashSet(Arrays.asList(trans));
/* 188 */             String normW = normalize(word);
/* 189 */             Set<String> normSet = normalize(transList);
/* 190 */             if ((!normW.equals("")) && (normSet.size() > 0)) {
/* 191 */               this.map.put(normW, normSet);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 196 */       infile.close();
/*     */     } catch (IOException e) {
/* 198 */       throw new RuntimeException("IOException reading CEDict from file " + dictPath, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseEnglishWordMap()
/*     */   {
/* 210 */     File f = new File("cedict_ts.u8");
/* 211 */     String path; String path; if (f.canRead()) {
/* 212 */       path = "cedict_ts.u8";
/*     */     } else {
/* 214 */       f = new File("/u/nlp/data/chinese-english-dictionary/cedict_ts.u8");
/* 215 */       String path; if (f.canRead()) {
/* 216 */         path = "/u/nlp/data/chinese-english-dictionary/cedict_ts.u8";
/*     */       } else {
/* 218 */         path = System.getenv("CEDICT");
/* 219 */         f = new File(path);
/* 220 */         if (!f.canRead()) {
/* 221 */           throw new RuntimeException("ChineseEnglishWordMap cannot find dictionary");
/*     */         }
/*     */       }
/*     */     }
/* 225 */     readCEDict(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseEnglishWordMap(String dictPath)
/*     */   {
/* 233 */     readCEDict(dictPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseEnglishWordMap(String dictPath, boolean normalized)
/*     */   {
/* 242 */     this.normalized = normalized;
/* 243 */     readCEDict(dictPath);
/*     */   }
/*     */   
/*     */   public ChineseEnglishWordMap(String dictPath, String pattern, String delimiter, String charset) {
/* 247 */     readCEDict(dictPath, pattern, delimiter, charset);
/*     */   }
/*     */   
/*     */   public ChineseEnglishWordMap(String dictPath, String pattern, String delimiter, String charset, boolean normalized) {
/* 251 */     this.normalized = normalized;
/* 252 */     readCEDict(dictPath, pattern, delimiter, charset);
/*     */   }
/*     */   
/*     */   private static boolean isDigits(String in)
/*     */   {
/* 257 */     int i = 0; for (int len = in.length(); i < len; i++) {
/* 258 */       if (!Character.isDigit(in.charAt(i))) {
/* 259 */         return false;
/*     */       }
/*     */     }
/* 262 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Set<String>> getReverseMap()
/*     */   {
/* 271 */     Set<Map.Entry<String, Set<String>>> entries = this.map.entrySet();
/* 272 */     Map<String, Set<String>> rMap = new HashMap(entries.size());
/* 273 */     for (Map.Entry<String, Set<String>> me : entries) {
/* 274 */       k = (String)me.getKey();
/* 275 */       Set<String> transList = (Set)me.getValue();
/* 276 */       for (String trans : transList) {
/* 277 */         Set<String> entry = (Set)rMap.get(trans);
/* 278 */         if (entry == null)
/*     */         {
/* 280 */           Set<String> toAdd = new LinkedHashSet(6);
/* 281 */           toAdd.add(k);
/* 282 */           rMap.put(trans, toAdd);
/*     */         } else {
/* 284 */           entry.add(k);
/*     */         }
/*     */       } }
/*     */     String k;
/* 288 */     return rMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int addMap(Map<String, Set<String>> addM)
/*     */   {
/* 295 */     int newTrans = 0;
/*     */     
/* 297 */     for (Map.Entry<String, Set<String>> me : addM.entrySet()) {
/* 298 */       String k = (String)me.getKey();
/* 299 */       Set<String> addList = (Set)me.getValue();
/* 300 */       origList = (Set)this.map.get(k);
/* 301 */       if (origList == null) {
/* 302 */         this.map.put(k, new LinkedHashSet(addList));
/* 303 */         Set<String> newList = (Set)this.map.get(k);
/* 304 */         if ((newList != null) && (newList.size() != 0)) {
/* 305 */           newTrans += addList.size();
/*     */         }
/*     */       } else {
/* 308 */         for (String toAdd : addList)
/* 309 */           if (!origList.contains(toAdd)) {
/* 310 */             origList.add(toAdd);
/* 311 */             newTrans++;
/*     */           }
/*     */       }
/*     */     }
/*     */     Set<String> origList;
/* 316 */     return newTrans;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 322 */     return this.map.toString();
/*     */   }
/*     */   
/*     */   public int size() {
/* 326 */     return this.map.size();
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
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/* 345 */     Map<String, Integer> flagsToNumArgs = new HashMap();
/* 346 */     flagsToNumArgs.put("-dictPath", Integer.valueOf(1));
/* 347 */     flagsToNumArgs.put("-encoding", Integer.valueOf(1));
/* 348 */     Map<String, String[]> argMap = StringUtils.argsToMap(args, flagsToNumArgs);
/* 349 */     String[] otherArgs = (String[])argMap.get(null);
/* 350 */     if (otherArgs.length < 1) {
/* 351 */       System.err.println("usage: ChineseEnglishWordMap [-all] [-dictPath path] [-encoding enc_string] inputFile");
/* 352 */       System.exit(1);
/*     */     }
/* 354 */     String filename = otherArgs[0];
/* 355 */     boolean allTranslations = argMap.containsKey("-all");
/* 356 */     String charset = "UTF-8";
/* 357 */     if (argMap.containsKey("-encoding")) {
/* 358 */       charset = ((String[])argMap.get("-encoding"))[0];
/*     */     }
/* 360 */     BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charset));
/*     */     
/* 362 */     TreebankLanguagePack tlp = new ChineseTreebankLanguagePack();
/* 363 */     String[] dpString = (String[])argMap.get("-dictPath");
/* 364 */     ChineseEnglishWordMap cewm = dpString == null ? new ChineseEnglishWordMap() : new ChineseEnglishWordMap(dpString[0]);
/* 365 */     int totalWords = 0;int coveredWords = 0;
/*     */     
/* 367 */     PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out, charset), true);
/*     */     
/* 369 */     for (String line = r.readLine(); line != null; line = r.readLine()) {
/* 370 */       String[] words = line.split("\\s", 1000);
/* 371 */       for (String word : words) {
/* 372 */         totalWords++;
/* 373 */         if (word.length() != 0) {
/* 374 */           pw.print(StringUtils.pad(word + ':', 8));
/* 375 */           if (tlp.isPunctuationWord(word)) {
/* 376 */             totalWords--;
/* 377 */             pw.print(word);
/* 378 */           } else if (isDigits(word)) {
/* 379 */             pw.print(word + " [NUMBER]");
/* 380 */           } else if (cewm.containsKey(word)) {
/* 381 */             coveredWords++;
/* 382 */             List<String> trans; if (allTranslations) {
/* 383 */               trans = new ArrayList(cewm.getAllTranslations(word));
/* 384 */               for (String s : trans) {
/* 385 */                 pw.print((trans.indexOf(s) > 0 ? "|" : "") + s);
/*     */               }
/*     */             } else {
/* 388 */               pw.print(cewm.getFirstTranslation(word));
/*     */             }
/*     */           } else {
/* 391 */             pw.print("[UNK]");
/*     */           }
/* 393 */           pw.println();
/*     */         } }
/* 395 */       pw.println();
/*     */     }
/* 397 */     r.close();
/* 398 */     System.err.print("Finished translating " + totalWords + " words (");
/* 399 */     System.err.println(coveredWords + " were in dictionary).");
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\ChineseEnglishWordMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */