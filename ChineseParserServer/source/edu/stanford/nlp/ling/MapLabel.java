/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ import edu.stanford.nlp.io.IOUtils;
/*     */ import edu.stanford.nlp.util.MapFactory;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapLabel
/*     */   extends AbstractMapLabel
/*     */ {
/*  23 */   private static String printOptions = "value-index";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapLabel()
/*     */   {
/*  31 */     this((String)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MapLabel(MapFactory mapFactory)
/*     */   {
/*  38 */     this((String)null, mapFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapLabel(String value)
/*     */   {
/*  47 */     setValue(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapLabel(String value, MapFactory mapFactory)
/*     */   {
/*  56 */     super(mapFactory);
/*  57 */     setValue(value);
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
/*     */   public MapLabel(Label label)
/*     */   {
/*  81 */     this(label, MapFactory.HASH_MAP_FACTORY);
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
/*     */   public MapLabel(Label label, MapFactory mapFactory)
/*     */   {
/* 105 */     super(mapFactory);
/* 106 */     if ((label instanceof AbstractMapLabel)) {
/* 107 */       AbstractMapLabel ml = (AbstractMapLabel)label;
/* 108 */       if (mapFactory == null) {
/* 109 */         this.mapFactory = ml.mapFactory;
/* 110 */         this.map = mapFactory.newMap(5);
/*     */       }
/* 112 */       this.map.putAll(ml.map());
/*     */     } else {
/* 114 */       this.map = mapFactory.newMap(5);
/* 115 */       if ((label instanceof HasCategory)) {
/* 116 */         this.map.put("cat", ((HasCategory)label).category());
/*     */       }
/* 118 */       if ((label instanceof HasTag)) {
/* 119 */         setTag(((HasTag)label).tag());
/*     */       }
/* 121 */       if ((label instanceof HasWord)) {
/* 122 */         this.map.put("word", ((HasWord)label).word());
/*     */       }
/*     */     }
/* 125 */     setValue(label.value());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class LabelFactoryHolder
/*     */   {
/* 132 */     static final LabelFactory lf = new MapLabelFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabelFactory labelFactory()
/*     */   {
/* 143 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LabelFactory factory()
/*     */   {
/* 153 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 162 */     return toString(printOptions);
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
/*     */   public String toString(String format)
/*     */   {
/* 187 */     StringBuilder buf = new StringBuilder();
/* 188 */     if (format.equals("value")) {
/* 189 */       buf.append(value());
/* 190 */     } else if (format.equals("{map}")) {
/* 191 */       Map map2 = new TreeMap(asStringComparator);
/* 192 */       map2.putAll(this.map);
/* 193 */       buf.append(map2);
/* 194 */     } else if (format.equals("value{map}")) {
/* 195 */       buf.append(value());
/* 196 */       Map map2 = new TreeMap(asStringComparator);
/* 197 */       map2.putAll(this.map);
/* 198 */       map2.remove("value");
/* 199 */       buf.append(map2);
/* 200 */     } else if (format.equals("value-index")) {
/* 201 */       buf.append(value());
/* 202 */       Object index = this.map.get("idx");
/* 203 */       if ((index != null) && ((index instanceof Integer))) {
/* 204 */         buf.append("-").append(((Integer)index).intValue());
/*     */       }
/* 206 */     } else if (format.equals("value-index{map}")) {
/* 207 */       buf.append(value());
/* 208 */       Object index = this.map.get("idx");
/* 209 */       if ((index != null) && ((index instanceof Integer))) {
/* 210 */         buf.append("-").append(((Integer)index).intValue());
/*     */       }
/* 212 */       Map map2 = new TreeMap(asStringComparator);
/* 213 */       map2.putAll(this.map);
/* 214 */       map2.remove("idx");
/* 215 */       map2.remove("value");
/* 216 */       if (!map2.isEmpty()) {
/* 217 */         buf.append(map2);
/*     */       }
/* 219 */     } else if (format.equals("word")) {
/* 220 */       buf.append(word());
/*     */     }
/* 222 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public static void setPrintOptions(String po) {
/* 226 */     printOptions = po;
/*     */   }
/*     */   
/* 229 */   private static final Comparator asStringComparator = new Comparator() {
/*     */     public int compare(Object o1, Object o2) {
/* 231 */       return o1.toString().compareTo(o2.toString());
/*     */     }
/*     */   };
/*     */   
/*     */   private static final long serialVersionUID = 1289283452485202162L;
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/* 239 */     System.out.println("Testing different constructors");
/* 240 */     System.out.println("  MapLabel from zero-arg constructor:      " + new MapLabel());
/* 241 */     System.out.println("  MapLabel from String constructor:        " + new MapLabel("StringValue"));
/* 242 */     System.out.println("  MapLabel from Label constructor:         " + new MapLabel(new StringLabel("StringLabelValue")));
/* 243 */     System.out.println("  MapLabel from MapLabel constructor:      " + new MapLabel(new MapLabel("MapLabelValue")));
/* 244 */     MapLabel label = new MapLabel(new CategoryWordTag("cat", "word", "tag"));
/* 245 */     System.out.println("MapLabel from CategoryWordTag constructor: " + label);
/* 246 */     System.out.println("That same label in value{map} format:      " + label.toString("value{map}"));
/* 247 */     label.setIndex(666);
/* 248 */     System.out.println("Add index key 666 to that label:           " + label.toString("value{map}"));
/* 249 */     System.out.println("That label in {map} format:                " + label.toString("{map}"));
/* 250 */     System.out.println("That label in word format:                 " + label.toString("word"));
/* 251 */     System.out.println("That label in value-index format:          " + label.toString("value-index"));
/* 252 */     System.out.println("That label in value-index{map} format:     " + label.toString("value-index{map}"));
/*     */     
/* 254 */     int oldValue = label.index();
/* 255 */     label.setIndex(777);
/* 256 */     System.out.println("Changed the index from " + oldValue + ":                " + label.toString("value-index"));
/* 257 */     Object oldVal = label.put("idx", "sixsixsix");
/* 258 */     System.out.println("Changed the index from " + oldVal + " to string:      " + label.toString("value-index"));
/* 259 */     System.out.println("That label in value{map} format:           " + label.toString("value{map}"));
/* 260 */     label.map.remove("idx");
/* 261 */     System.out.println("Removed index key:                         " + label.toString("value-index"));
/* 262 */     System.out.println("That label in value{map} format:           " + label.toString("value{map}"));
/*     */     
/* 264 */     oldVal = label.put("foo", "bar");
/* 265 */     System.out.println("Changed foo from " + oldVal + ":                     " + label.toString("value-index{map}"));
/* 266 */     label.put("self", label);
/* 267 */     System.out.println("Add map entry with self as value:          " + label.toString("value-index{map}"));
/* 268 */     label.setHeadWord(new CategoryWordTag("cat", "rose", "tag"));
/* 269 */     System.out.println("Setting headWord to rose:                  " + label.toString("value{map}"));
/*     */     
/* 271 */     System.out.println("Testing serialization...");
/*     */     try {
/* 273 */       File testFile = IOUtils.writeObjectToTempFile(label, "testfile");
/* 274 */       MapLabel newLabel = (MapLabel)IOUtils.readObjectFromFile(testFile);
/* 275 */       System.out.println("New label:                                 " + newLabel.toString("value-index{map}"));
/*     */     } catch (Exception e) {
/* 277 */       System.err.println(e);
/*     */     }
/*     */     
/* 280 */     System.out.println("Done.");
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\MapLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */