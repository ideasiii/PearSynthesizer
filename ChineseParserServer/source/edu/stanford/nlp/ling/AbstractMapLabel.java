/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ import edu.stanford.nlp.util.IntPair;
/*     */ import edu.stanford.nlp.util.MapFactory;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ public abstract class AbstractMapLabel
/*     */   implements Label, HasWord, HasTag, HasCategory, HasContext, Serializable
/*     */ {
/*     */   static final int initCapacity = 5;
/*     */   public static final String VALUE_KEY = "value";
/*     */   public static final String TAG_KEY = "tag";
/*     */   public static final String WORD_KEY = "word";
/*     */   public static final String LEMMA_KEY = "lemma";
/*     */   public static final String CATEGORY_KEY = "cat";
/*     */   public static final String PROJ_CAT_KEY = "pcat";
/*     */   public static final String HEAD_WORD_KEY = "hw";
/*     */   public static final String HEAD_TAG_KEY = "ht";
/*     */   public static final String INDEX_KEY = "idx";
/*     */   public static final String ARG_KEY = "arg";
/*     */   public static final String MARKING_KEY = "mark";
/*     */   public static final String SEMANTIC_HEAD_WORD_KEY = "shw";
/*     */   public static final String SEMANTIC_HEAD_POS_KEY = "shp";
/*     */   public static final String VERB_SENSE_KEY = "vs";
/*     */   public static final String CATEGORY_FUNCTIONAL_TAG_KEY = "cft";
/*     */   public static final String NER_KEY = "ner";
/*     */   public static final String COREF_KEY = "coref";
/*     */   public static final String SHAPE_KEY = "shape";
/*     */   public static final String LEFT_TERM_KEY = "LEFT_TERM";
/*     */   public static final String PARENT_KEY = "PARENT";
/*     */   public static final String SPAN_KEY = "SPAN";
/*     */   public static final String BEFORE_KEY = "before";
/*     */   public static final String AFTER_KEY = "after";
/*     */   public static final String CURRENT_KEY = "current";
/*     */   public static final String ANSWER_KEY = "answer";
/*     */   public static final String GOLDANSWER_KEY = "goldAnswer";
/*     */   public static final String FEATURES_KEY = "features";
/*     */   public static final String INTERPRETATION_KEY = "interpretation";
/*     */   public static final String ROLE_KEY = "srl";
/*     */   public static final String GAZETTEER_KEY = "gazetteer";
/*     */   public static final String STEM_KEY = "stem";
/*     */   public static final String POLARITY_KEY = "polarity";
/*     */   public static final String CH_CHAR_KEY = "char";
/*     */   public static final String CH_ORIG_SEG_KEY = "orig_seg";
/*     */   public static final String CH_SEG_KEY = "seg";
/*     */   public static final String BEGIN_POSITION_KEY = "BEGIN_POS";
/*     */   public static final String END_POSITION_KEY = "END_POS";
/*     */   protected Map map;
/*     */   protected MapFactory mapFactory;
/*     */   private static final long serialVersionUID = -980833749513621054L;
/*     */   
/*     */   protected AbstractMapLabel()
/*     */   {
/* 219 */     this(null);
/*     */   }
/*     */   
/*     */   protected AbstractMapLabel(MapFactory mapFactory) {
/* 223 */     if (mapFactory == null) {
/* 224 */       this.mapFactory = MapFactory.HASH_MAP_FACTORY;
/*     */     } else {
/* 226 */       this.mapFactory = mapFactory;
/*     */     }
/* 228 */     this.map = this.mapFactory.newMap(5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map map()
/*     */   {
/* 239 */     return this.map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/* 266 */     Object v = this.map.get(key);
/* 267 */     if (v == null) {
/* 268 */       return "";
/*     */     }
/* 270 */     return v;
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
/*     */   public Object put(Object key, Object value)
/*     */   {
/* 286 */     return this.map.put(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String value()
/*     */   {
/* 295 */     return (String)this.map.get("value");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(String value)
/*     */   {
/* 304 */     this.map.put("value", value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFromString(String str)
/*     */   {
/* 313 */     setValue(str);
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
/*     */   public String category()
/*     */   {
/* 326 */     Object cat = this.map.get("cat");
/* 327 */     if ((cat != null) && ((cat instanceof String))) {
/* 328 */       return (String)cat;
/*     */     }
/* 330 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCategory(String category)
/*     */   {
/* 340 */     this.map.put("cat", category);
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
/*     */   public String word()
/*     */   {
/* 353 */     return (String)this.map.get("word");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWord(String word)
/*     */   {
/* 362 */     this.map.put("word", word);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntPair span()
/*     */   {
/* 371 */     return (IntPair)this.map.get("SPAN");
/*     */   }
/*     */   
/*     */   public void setSpan(String span) {
/* 375 */     this.map.put("SPAN", span);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object headWord()
/*     */   {
/* 386 */     return this.map.get("hw");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setHeadWord(Object headWordPtr)
/*     */   {
/* 393 */     this.map.put("hw", headWordPtr);
/* 394 */     if ((headWordPtr instanceof HasWord)) {
/* 395 */       setWord(((HasWord)headWordPtr).word());
/* 396 */     } else if ((headWordPtr instanceof Label)) {
/* 397 */       setWord(((Label)headWordPtr).value());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSemanticWord()
/*     */   {
/* 405 */     Object word = this.map.get("shw");
/* 406 */     return word != null ? word.toString() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSemanticWord(String hWord)
/*     */   {
/* 413 */     this.map.put("shw", hWord);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSemanticTag()
/*     */   {
/* 421 */     Object word = this.map.get("shp");
/* 422 */     return word != null ? word.toString() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSemanticTag(String hTag)
/*     */   {
/* 429 */     this.map.put("shp", hTag);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String tag()
/*     */   {
/* 440 */     Object tag = this.map.get("tag");
/* 441 */     if ((tag != null) && ((tag instanceof String))) {
/* 442 */       return (String)tag;
/*     */     }
/* 444 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTag(String tag)
/*     */   {
/* 456 */     this.map.put("tag", tag);
/*     */   }
/*     */   
/*     */   public Object headTag() {
/* 460 */     return this.map.get("ht");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setHeadTag(Object headTagPtr)
/*     */   {
/* 467 */     this.map.put("ht", headTagPtr);
/* 468 */     if ((headTagPtr instanceof HasTag)) {
/* 469 */       setTag(((HasTag)headTagPtr).tag());
/* 470 */     } else if ((headTagPtr instanceof Label)) {
/* 471 */       setTag(((Label)headTagPtr).value());
/*     */     }
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
/* 483 */     Object ner = this.map.get("ner");
/* 484 */     return (String)ner;
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
/* 495 */     this.map.put("ner", ner);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String shape()
/*     */   {
/* 506 */     return (String)this.map.get("shape");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShape(String shape)
/*     */   {
/* 517 */     this.map.put("shape", shape);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int index()
/*     */   {
/* 528 */     Object index = this.map.get("idx");
/* 529 */     if ((index != null) && ((index instanceof Integer))) {
/* 530 */       return ((Integer)index).intValue();
/*     */     }
/* 532 */     return -1;
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
/*     */   public void setIndex(int index)
/*     */   {
/* 546 */     this.map.put("idx", new Integer(index));
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
/*     */   public int beginPosition()
/*     */   {
/* 559 */     Object index = this.map.get("BEGIN_POS");
/* 560 */     if ((index != null) && ((index instanceof Integer))) {
/* 561 */       return ((Integer)index).intValue();
/*     */     }
/* 563 */     return -1;
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
/*     */   public void setBeginPosition(int beginPos)
/*     */   {
/* 577 */     this.map.put("BEGIN_POS", new Integer(beginPos));
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
/*     */   public int endPosition()
/*     */   {
/* 590 */     Object index = this.map.get("END_POS");
/* 591 */     if ((index != null) && ((index instanceof Integer))) {
/* 592 */       return ((Integer)index).intValue();
/*     */     }
/* 594 */     return -1;
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
/*     */   public void setEndPosition(int endPos)
/*     */   {
/* 608 */     this.map.put("END_POS", new Integer(endPos));
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
/*     */   public String before()
/*     */   {
/* 622 */     Object before = this.map.get("before");
/* 623 */     if (before == null) {
/* 624 */       before = "";
/*     */     }
/* 626 */     return (String)before;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBefore(String before)
/*     */   {
/* 636 */     this.map.put("before", before);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prependBefore(String before)
/*     */   {
/* 645 */     String oldBefore = before();
/* 646 */     if (oldBefore == null) {
/* 647 */       oldBefore = "";
/*     */     }
/* 649 */     setBefore(before + oldBefore);
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
/* 660 */     Object current = this.map.get("current");
/* 661 */     if (current == null) {
/* 662 */       current = "";
/*     */     }
/* 664 */     return (String)current;
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
/* 675 */     this.map.put("current", current);
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
/* 686 */     Object after = this.map.get("after");
/* 687 */     if (after == null) {
/* 688 */       after = "";
/*     */     }
/* 690 */     return (String)after;
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
/* 701 */     this.map.put("after", after);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void appendAfter(String after)
/*     */   {
/* 710 */     String oldAfter = after();
/* 711 */     if (oldAfter == null) {
/* 712 */       oldAfter = "";
/*     */     }
/* 714 */     setAfter(oldAfter + after);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String answer()
/*     */   {
/* 721 */     return (String)get("answer");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAnswer(String answer)
/*     */   {
/* 728 */     put("answer", answer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String goldAnswer()
/*     */   {
/* 735 */     return (String)get("goldAnswer");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setGoldAnswer(String goldAnswer)
/*     */   {
/* 742 */     put("goldAnswer", goldAnswer);
/*     */   }
/*     */   
/*     */   public Collection getFeatures() {
/* 746 */     return (Collection)this.map.get("features");
/*     */   }
/*     */   
/*     */   public void setFeatures(Collection features) {
/* 750 */     this.map.put("features", features);
/*     */   }
/*     */   
/*     */   public Object interpretation() {
/* 754 */     return this.map.get("interpretation");
/*     */   }
/*     */   
/*     */   public void setInterpretation(Object interpretation) {
/* 758 */     this.map.put("interpretation", interpretation);
/*     */   }
/*     */   
/*     */   public String getLemma() {
/* 762 */     return (String)this.map.get("lemma");
/*     */   }
/*     */   
/*     */   public void setLemma(String lemma) {
/* 766 */     this.map.put("lemma", lemma);
/*     */   }
/*     */   
/*     */   public String getRole() {
/* 770 */     return (String)this.map.get("srl");
/*     */   }
/*     */   
/*     */   public void setRole(String role) {
/* 774 */     this.map.put("srl", role);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\AbstractMapLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */