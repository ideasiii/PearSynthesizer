/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ import edu.stanford.nlp.util.CollectionUtils;
/*     */ import edu.stanford.nlp.util.Interner;
/*     */ import edu.stanford.nlp.util.MapFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FeatureLabel
/*     */   extends AbstractMapLabel
/*     */ {
/*  25 */   public static String TOSTRING_FORMAT = null;
/*  26 */   public Collection features = null;
/*     */   
/*     */   private static final long serialVersionUID = 19L;
/*     */   
/*     */ 
/*     */   public FeatureLabel() {}
/*     */   
/*     */   public FeatureLabel(MapFactory mapFactory)
/*     */   {
/*  35 */     super(mapFactory);
/*     */   }
/*     */   
/*     */   public Set keySet() {
/*  39 */     return this.map.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FeatureLabel(String[] keys, String[] values)
/*     */   {
/*  48 */     for (int i = 0; (i < keys.length) && (i < values.length); i++) {
/*  49 */       if (keys[i] != null)
/*     */       {
/*     */ 
/*  52 */         put(keys[i], values[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static FeatureLabel valueOf(String s, MapFactory mf)
/*     */     throws Exception
/*     */   {
/*  60 */     return new FeatureLabel(CollectionUtils.getMapFromString(s, Class.forName("java.lang.String"), Class.forName("java.lang.String"), mf));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FeatureLabel(AbstractMapLabel other)
/*     */   {
/*  69 */     super(other.mapFactory);
/*  70 */     this.map = this.mapFactory.newMap(5);
/*  71 */     this.map.putAll(other.map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FeatureLabel(Map map)
/*     */   {
/*  80 */     this.map = map;
/*     */   }
/*     */   
/*     */   public static String[] mapStringToArray(String map) {
/*  84 */     String[] m = map.split("[,;]");
/*  85 */     int maxIndex = 0;
/*  86 */     String[] keys = new String[m.length];
/*  87 */     int[] indices = new int[m.length];
/*  88 */     for (int i = 0; i < m.length; i++) {
/*  89 */       int index = m[i].lastIndexOf("=");
/*  90 */       keys[i] = m[i].substring(0, index);
/*  91 */       indices[i] = Integer.parseInt(m[i].substring(index + 1));
/*  92 */       if (indices[i] > maxIndex) {
/*  93 */         maxIndex = indices[i];
/*     */       }
/*     */     }
/*  96 */     String[] mapArr = new String[maxIndex + 1];
/*  97 */     Arrays.fill(mapArr, null);
/*  98 */     for (int i = 0; i < m.length; i++) {
/*  99 */       mapArr[indices[i]] = keys[i];
/*     */     }
/* 101 */     return mapArr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String word()
/*     */   {
/* 108 */     return getString("word");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String answer()
/*     */   {
/* 115 */     return getString("answer");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String goldAnswer()
/*     */   {
/* 122 */     return getString("goldAnswer");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setWord(String word)
/*     */   {
/* 129 */     put("word", word);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAnswer(String answer)
/*     */   {
/* 136 */     put("answer", answer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setGoldAnswer(String goldAnswer)
/*     */   {
/* 143 */     put("goldAnswer", goldAnswer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String before()
/*     */   {
/* 154 */     return getString("before");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBefore(String before)
/*     */   {
/* 165 */     this.map.put("before", before);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prependBefore(String before)
/*     */   {
/* 174 */     String oldBefore = before();
/* 175 */     setBefore(before + oldBefore);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String current()
/*     */   {
/* 186 */     return getString("current");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCurrent(String current)
/*     */   {
/* 197 */     this.map.put("current", current);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String after()
/*     */   {
/* 208 */     return getString("after");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAfter(String after)
/*     */   {
/* 219 */     this.map.put("after", after);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendAfter(String after)
/*     */   {
/* 228 */     String oldAfter = after();
/* 229 */     setAfter(oldAfter + after);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String ner()
/*     */   {
/* 240 */     return getString("ner");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNER(String ner)
/*     */   {
/* 251 */     this.map.put("ner", ner);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String coref()
/*     */   {
/* 262 */     return getString("coref");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toOriginalString(List<FeatureLabel> sentence)
/*     */   {
/* 271 */     StringBuilder text = new StringBuilder();
/* 272 */     int i = 0; for (int sz = sentence.size(); i < sz; i++) {
/* 273 */       FeatureLabel iw = (FeatureLabel)sentence.get(i);
/* 274 */       text.append(iw.before());
/* 275 */       text.append(iw.current());
/* 276 */       if (i == sz - 1) {
/* 277 */         text.append(iw.after());
/*     */       }
/*     */     }
/* 280 */     return text.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toSentence(List<? extends FeatureLabel> sentence)
/*     */   {
/* 288 */     StringBuilder text = new StringBuilder();
/* 289 */     int i = 0; for (int sz = sentence.size(); i < sz; i++) {
/* 290 */       FeatureLabel iw = (FeatureLabel)sentence.get(i);
/* 291 */       text.append(iw.word());
/* 292 */       if (i < sz - 1) {
/* 293 */         text.append(" ");
/*     */       }
/*     */     }
/* 296 */     return text.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String value()
/*     */   {
/* 302 */     return getString("value");
/*     */   }
/*     */   
/*     */   public void setValue(String value) {
/* 306 */     put("value", value);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 310 */     return toString(TOSTRING_FORMAT);
/*     */   }
/*     */   
/*     */   public String toString(String format) {
/* 314 */     if ((format == null) || (format.equals(""))) {
/* 315 */       StringBuffer sb = new StringBuffer("{");
/* 316 */       List sortedKeys = new ArrayList(this.map.keySet());
/* 317 */       Collections.sort(sortedKeys);
/* 318 */       boolean first = true;
/* 319 */       for (Object k : sortedKeys) {
/* 320 */         if (!first) sb.append(", ");
/* 321 */         sb.append(k).append("=").append(this.map.get(k));
/* 322 */         first = false;
/*     */       }
/* 324 */       sb.append("}");
/* 325 */       return sb.toString();
/*     */     }
/* 327 */     if (format.equals("word"))
/* 328 */       return word();
/* 329 */     if (format.equals("wordtag")) {
/* 330 */       String tag = tag();
/* 331 */       if ((tag != null) && (tag.length() > 0)) {
/* 332 */         return word() + "/" + tag;
/*     */       }
/* 334 */       return word();
/*     */     }
/*     */     
/* 337 */     return this.map.toString();
/*     */   }
/*     */   
/*     */   public void setFromString(String labelStr)
/*     */   {
/* 342 */     put("value", labelStr);
/*     */   }
/*     */   
/*     */   public LabelFactory labelFactory() {
/* 346 */     return new FeatureLabelFactory(null);
/*     */   }
/*     */   
/*     */   public static LabelFactory factory() {
/* 350 */     return new FeatureLabelFactory(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void internValues(Interner interner)
/*     */   {
/* 358 */     Map newMap = this.mapFactory.newMap(5);
/* 359 */     for (Object o : this.map.entrySet()) {
/* 360 */       Map.Entry entry = (Map.Entry)o;
/* 361 */       Object key = entry.getKey();
/* 362 */       Object value = entry.getValue();
/* 363 */       newMap.put(key, interner.intern(value));
/*     */     }
/* 365 */     this.map = newMap;
/*     */   }
/*     */   
/*     */   private static class FeatureLabelFactory
/*     */     implements LabelFactory
/*     */   {
/*     */     public Label newLabel(String labelStr)
/*     */     {
/* 373 */       FeatureLabel result = new FeatureLabel();
/* 374 */       result.setValue(labelStr);
/* 375 */       return result;
/*     */     }
/*     */     
/*     */     public Label newLabel(String labelStr, int options) {
/* 379 */       FeatureLabel result = new FeatureLabel();
/* 380 */       result.setValue(labelStr);
/* 381 */       return result;
/*     */     }
/*     */     
/*     */     public Label newLabelFromString(String encodedLabelStr) {
/* 385 */       FeatureLabel result = new FeatureLabel();
/* 386 */       result.setValue(encodedLabelStr);
/* 387 */       return result;
/*     */     }
/*     */     
/*     */     public Label newLabel(Label oldLabel) {
/* 391 */       FeatureLabel result = new FeatureLabel();
/* 392 */       result.setValue(oldLabel.value());
/* 393 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String lemma()
/*     */   {
/* 400 */     return getString("lemma");
/*     */   }
/*     */   
/*     */   public String tag()
/*     */   {
/* 405 */     return getString("tag");
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
/*     */   public String getString(Object attribute)
/*     */   {
/* 418 */     String v = (String)this.map.get(attribute);
/* 419 */     if (v == null) {
/* 420 */       return "";
/*     */     }
/* 422 */     return v;
/*     */   }
/*     */   
/*     */   public void set(Object attribute, Object value) {
/* 426 */     put(attribute, value);
/*     */   }
/*     */   
/*     */   public Map map() {
/* 430 */     return this.map;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 434 */     if (this == o) return true;
/* 435 */     if (!(o instanceof FeatureLabel)) return false;
/* 436 */     FeatureLabel featureLabel = (FeatureLabel)o;
/*     */     
/* 438 */     return this.map == null ? false : featureLabel.map == null ? true : this.map.equals(featureLabel.map);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 442 */     return this.map != null ? this.map.hashCode() : 7;
/*     */   }
/*     */   
/*     */ 
/*     */   public void remove(String key)
/*     */   {
/* 448 */     this.map.remove(key);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\FeatureLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */