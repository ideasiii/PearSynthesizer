/*     */ package edu.stanford.nlp.trees.international.pennchinese;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CHTBLexer
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int PREAMBLE = 7;
/*     */   public static final int DOCNO = 1;
/*     */   public static final int DATEINHEADER = 6;
/*     */   public static final int YYINITIAL = 0;
/*     */   public static final int SRCID = 4;
/*     */   public static final int HEADER = 5;
/*     */   public static final int DATETIME = 3;
/*     */   public static final int DOCTYPE = 2;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\031\001\027\001\030\001\030\001\026\022\000\001\031\002\024\001\000\001\024\001\000\001\033\001\024\001\032\001\032\001\033\001\025\001\025\001\025\001\025\001\b\n\025\002\033\001\001\001\033\001\002\001\024\001\000\001\005\001\022\001\n\001\006\001\004\002\025\001\003\001\f\002\025\001\023\001\020\001\013\001\t\001\017\001\025\001\007\001\021\001\r\004\025\001\016\001\025\005\000\001\024\032\025\003\000\001\024\006\000\001\030\033\000\026\024\001\024\b\024\027\000\001\024ʛ\000\001\024᲌\000\030\024\007\024\t\024\001\024\001\024F\024ð\000 \024¶\000\001\024ˉ\000\024 \000+\024\001\0244\024਀\000@\024À\0000\024ː\000ᧀ\024@\000冰\024䏋\000\001\024᪴\000 \024°\000\001\024^\024\024\r\000\001\024\002\000";
/*  53 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\031\001\027\001\030\001\030\001\026\022\000\001\031\002\024\001\000\001\024\001\000\001\033\001\024\001\032\001\032\001\033\001\025\001\025\001\025\001\025\001\b\n\025\002\033\001\001\001\033\001\002\001\024\001\000\001\005\001\022\001\n\001\006\001\004\002\025\001\003\001\f\002\025\001\023\001\020\001\013\001\t\001\017\001\025\001\007\001\021\001\r\004\025\001\016\001\025\005\000\001\024\032\025\003\000\001\024\006\000\001\030\033\000\026\024\001\024\b\024\027\000\001\024ʛ\000\001\024᲌\000\030\024\007\024\t\024\001\024\001\024F\024ð\000 \024¶\000\001\024ˉ\000\024 \000+\024\001\0244\024਀\000@\024À\0000\024ː\000ᧀ\024@\000冰\024䏋\000\001\024᪴\000 \024°\000\001\024^\024\024\r\000\001\024\002\000");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\b\000\001\001\003\002\003\003\002\002\001\003\007\001\001\000\005\002\b\000\005\002\b\000\005\002\b\000\007\002\b\000\001\002\001\004\005\002\004\000\001\005\004\000\002\002\001\006\002\002\001\007\002\000\001\b\003\000\001\t\002\000\001\n\003\002\001\013\005\000\001\002\001\f\001\002\005\000\001\002\001\r\001\016\006\000\001\017";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int[] zzUnpackAction()
/*     */   {
/*  69 */     int[] result = new int[''];
/*  70 */     int offset = 0;
/*  71 */     offset = zzUnpackAction("\b\000\001\001\003\002\003\003\002\002\001\003\007\001\001\000\005\002\b\000\005\002\b\000\005\002\b\000\007\002\b\000\001\002\001\004\005\002\004\000\001\005\004\000\002\002\001\006\002\002\001\007\002\000\001\b\003\000\001\t\002\000\001\n\003\002\001\013\005\000\001\002\001\f\001\002\005\000\001\002\001\r\001\016\006\000\001\017", offset, result);
/*  72 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/*  76 */     int i = 0;
/*  77 */     int j = offset;
/*  78 */     int l = packed.length();
/*  79 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*  82 */         count > 0)
/*     */     {
/*  80 */       count = packed.charAt(i++);
/*  81 */       int value = packed.charAt(i++);
/*  82 */       result[(j++)] = value;count--;
/*     */     }
/*  84 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\034\0008\000T\000p\000\000¨\000Ä\000à\000ü\000Ę\000Ĵ\000Ő\000à\000Ŭ\000à\000ƈ\000Ƥ\000ǀ\000ǜ\000Ǹ\000Ȕ\000Ȱ\000Ɍ\000ɨ\000ʄ\000ʠ\000ʼ\000˘\000˴\000̐\000̬\000͈\000ͤ\000΀\000Μ\000θ\000ϔ\000ϰ\000Ќ\000Ш\000ф\000Ѡ\000Ѽ\000Ҙ\000Ҵ\000Ӑ\000Ӭ\000Ԉ\000Ԥ\000Հ\000՜\000ո\000֔\000ְ\000׌\000ר\000؄\000ؠ\000ؼ\000٘\000ٴ\000ڐ\000ڬ\000ۈ\000ۤ\000܀\000ܜ\000ܸ\000ݔ\000ݰ\000ތ\000ި\000߄\000ߠ\000߼\000࠘\000࠴\000ࡐ\000࡬\000࢈\000à\000ࢤ\000ࣀ\000ࣜ\000ࣸ\000औ\000र\000ौ\000२\000঄\000à\000ঠ\000়\000৘\000৴\000ਐ\000ਬ\000à\000ੈ\000੤\000à\000઀\000જ\000à\000સ\000૔\000૰\000à\000ଌ\000ନ\000à\000ୄ\000ୠ\000୼\000à\000஘\000ழ\000ௐ\000௬\000ఈ\000త\000à\000ీ\000౜\000౸\000ಔ\000ರ\000ೌ\000೨\000à\000à\000ഄ\000ഠ\000഼\000൘\000൴\000ඐ\000à";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int[] zzUnpackRowMap()
/*     */   {
/* 114 */     int[] result = new int[''];
/* 115 */     int offset = 0;
/* 116 */     offset = zzUnpackRowMap("\000\000\000\034\0008\000T\000p\000\000¨\000Ä\000à\000ü\000Ę\000Ĵ\000Ő\000à\000Ŭ\000à\000ƈ\000Ƥ\000ǀ\000ǜ\000Ǹ\000Ȕ\000Ȱ\000Ɍ\000ɨ\000ʄ\000ʠ\000ʼ\000˘\000˴\000̐\000̬\000͈\000ͤ\000΀\000Μ\000θ\000ϔ\000ϰ\000Ќ\000Ш\000ф\000Ѡ\000Ѽ\000Ҙ\000Ҵ\000Ӑ\000Ӭ\000Ԉ\000Ԥ\000Հ\000՜\000ո\000֔\000ְ\000׌\000ר\000؄\000ؠ\000ؼ\000٘\000ٴ\000ڐ\000ڬ\000ۈ\000ۤ\000܀\000ܜ\000ܸ\000ݔ\000ݰ\000ތ\000ި\000߄\000ߠ\000߼\000࠘\000࠴\000ࡐ\000࡬\000࢈\000à\000ࢤ\000ࣀ\000ࣜ\000ࣸ\000औ\000र\000ौ\000२\000঄\000à\000ঠ\000়\000৘\000৴\000ਐ\000ਬ\000à\000ੈ\000੤\000à\000઀\000જ\000à\000સ\000૔\000૰\000à\000ଌ\000ନ\000à\000ୄ\000ୠ\000୼\000à\000஘\000ழ\000ௐ\000௬\000ఈ\000త\000à\000ీ\000౜\000౸\000ಔ\000ರ\000ೌ\000೨\000à\000à\000ഄ\000ഠ\000഼\000൘\000൴\000ඐ\000à", offset, result);
/* 117 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 121 */     int i = 0;
/* 122 */     int j = offset;
/* 123 */     int l = packed.length();
/* 124 */     while (i < l) {
/* 125 */       int high = packed.charAt(i++) << '\020';
/* 126 */       result[(j++)] = (high | packed.charAt(i++));
/*     */     }
/* 128 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 134 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\t\001\n\001\t\021\013\001\f\001\013\001\r\002\016\001\017\001\020\001\021\001\022\001\023\033\022\001\024\033\022\001\025\033\022\001\026\033\022\001\027\033\022\001\030\033\022\001\031\032\022\034\000\001\032\001\033\001\000\001\034\002\033\001\035\b\033\001\036\001\033\001\037\002\033\001\032\001\033\005\032\001\033\001\000\001\021\001\000\021\013\001\f\001\013\005\000\001\021\003\000\023\f\035\000\001\016\035\000\001\017\003\000\001\021\001\000\021\021\001\000\001\021\005\000\001\021\001\022\001\000\032\022\b\000\001 \033\000\001!\033\000\001\"\033\000\001#\031\000\001$\001\000\001%\033\000\001&\033\000\001'\023\000\002\032\001\016\032\032\001\033\001\016\021\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\001(\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\002\033\001)\003\033\001*\n\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\004\033\001+\f\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\004\033\001,\f\033\001\032\001\033\005\032\001\033\006\000\001-\033\000\001.\033\000\001/&\000\0010\017\000\0011\031\000\0012\036\000\0013$\000\0014\f\000\001\032\001\033\001\016\002\033\0015\016\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\n\033\0016\006\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\007\033\0017\t\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\0018\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\007\033\0019\t\033\001\032\001\033\005\032\001\033\t\000\001:\033\000\001;\027\000\001<\035\000\001=!\000\001>\022\000\001?\034\000\001@\035\000\001A\024\000\001\032\001\033\001\016\003\033\001B\r\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\001C\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\b\033\001D\001E\001F\006\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\002\033\001G\016\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\t\033\001H\007\033\001\032\001\033\005\032\001\033\n\000\001I\033\000\001J\036\000\001K\030\000\001L\025\000\001M\034\000\001N#\000\001O\022\000\001P\027\000\001\032\001\033\001\016\001\033\001Q\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001R\005\033\001S\013\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\006\033\001T\n\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\003\033\001T\r\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\013\033\001U\005\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\r\033\001V\003\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\003\033\001W\r\033\001\032\001\033\005\032\001\033\013\000\001X\001Y\034\000\001Z\022\000\001[#\000\001Y\021\000\001\\\005\000\001]\031\000\001^\031\000\001_\034\000\001`\026\000\001\032\001\033\001\016\004\033\001a\f\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\n\033\001b\006\033\001\032\001\033\005\032\001\033\001\032\001\033\001c\021\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\f\033\001d\004\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\017\033\001e\001\033\001\032\001\033\005\032\001\033\001\032\001\033\001f\021\033\001\032\001\033\005\032\001\033\t\000\001g\030\000\001g#\000\001h\017\000\001i\005\000\001j \000\001k\022\000\001l\031\000\001m\005\000\001n#\000\001o\013\000\001\032\001\033\001p\021\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\t\033\001q\007\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\001r\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\020\033\001s\001\032\001\033\005\032\001\033\002\000\001t(\000\001u\031\000\001v\032\000\001w\026\000\001g!\000\001x \000\001y\t\000\001\032\001\033\001\016\r\033\001z\003\033\001\032\001\033\005\032\001\033\001\032\001\033\001{\021\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\001|\017\033\001\032\001\033\005\032\001\033\004\000\001}#\000\001~\037\000\001\027\000\001\"\000\001\b\000\001\032\001\033\001\016\001\033\001\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\021\033\001\032\001\033\005\032\001\033\002\000\001)\000\001\017\000\001'\000\001\017\000\001\027\000\001\032\001\033\001R\021\033\001\032\001\033\005\032\001\033\004\000\001\031\000\001\\\035\000\001\031\000\001\033\000\001i\033\000\001m\031\000";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int[] zzUnpackTrans()
/*     */   {
/* 214 */     int[] result = new int['ඬ'];
/* 215 */     int offset = 0;
/* 216 */     offset = zzUnpackTrans("\001\t\001\n\001\t\021\013\001\f\001\013\001\r\002\016\001\017\001\020\001\021\001\022\001\023\033\022\001\024\033\022\001\025\033\022\001\026\033\022\001\027\033\022\001\030\033\022\001\031\032\022\034\000\001\032\001\033\001\000\001\034\002\033\001\035\b\033\001\036\001\033\001\037\002\033\001\032\001\033\005\032\001\033\001\000\001\021\001\000\021\013\001\f\001\013\005\000\001\021\003\000\023\f\035\000\001\016\035\000\001\017\003\000\001\021\001\000\021\021\001\000\001\021\005\000\001\021\001\022\001\000\032\022\b\000\001 \033\000\001!\033\000\001\"\033\000\001#\031\000\001$\001\000\001%\033\000\001&\033\000\001'\023\000\002\032\001\016\032\032\001\033\001\016\021\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\001(\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\002\033\001)\003\033\001*\n\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\004\033\001+\f\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\004\033\001,\f\033\001\032\001\033\005\032\001\033\006\000\001-\033\000\001.\033\000\001/&\000\0010\017\000\0011\031\000\0012\036\000\0013$\000\0014\f\000\001\032\001\033\001\016\002\033\0015\016\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\n\033\0016\006\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\007\033\0017\t\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\0018\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\007\033\0019\t\033\001\032\001\033\005\032\001\033\t\000\001:\033\000\001;\027\000\001<\035\000\001=!\000\001>\022\000\001?\034\000\001@\035\000\001A\024\000\001\032\001\033\001\016\003\033\001B\r\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\001C\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\b\033\001D\001E\001F\006\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\002\033\001G\016\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\t\033\001H\007\033\001\032\001\033\005\032\001\033\n\000\001I\033\000\001J\036\000\001K\030\000\001L\025\000\001M\034\000\001N#\000\001O\022\000\001P\027\000\001\032\001\033\001\016\001\033\001Q\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001R\005\033\001S\013\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\006\033\001T\n\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\003\033\001T\r\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\013\033\001U\005\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\r\033\001V\003\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\003\033\001W\r\033\001\032\001\033\005\032\001\033\013\000\001X\001Y\034\000\001Z\022\000\001[#\000\001Y\021\000\001\\\005\000\001]\031\000\001^\031\000\001_\034\000\001`\026\000\001\032\001\033\001\016\004\033\001a\f\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\n\033\001b\006\033\001\032\001\033\005\032\001\033\001\032\001\033\001c\021\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\f\033\001d\004\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\017\033\001e\001\033\001\032\001\033\005\032\001\033\001\032\001\033\001f\021\033\001\032\001\033\005\032\001\033\t\000\001g\030\000\001g#\000\001h\017\000\001i\005\000\001j \000\001k\022\000\001l\031\000\001m\005\000\001n#\000\001o\013\000\001\032\001\033\001p\021\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\t\033\001q\007\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\001r\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\020\033\001s\001\032\001\033\005\032\001\033\002\000\001t(\000\001u\031\000\001v\032\000\001w\026\000\001g!\000\001x \000\001y\t\000\001\032\001\033\001\016\r\033\001z\003\033\001\032\001\033\005\032\001\033\001\032\001\033\001{\021\033\001\032\001\033\005\032\001\033\001\032\001\033\001\016\001\033\001|\017\033\001\032\001\033\005\032\001\033\004\000\001}#\000\001~\037\000\001\027\000\001\"\000\001\b\000\001\032\001\033\001\016\001\033\001\017\033\001\032\001\033\005\032\001\033\001\032\001\033\001\021\033\001\032\001\033\005\032\001\033\002\000\001)\000\001\017\000\001'\000\001\017\000\001\027\000\001\032\001\033\001R\021\033\001\032\001\033\005\032\001\033\004\000\001\031\000\001\\\035\000\001\031\000\001\033\000\001i\033\000\001m\031\000", offset, result);
/* 217 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 221 */     int i = 0;
/* 222 */     int j = offset;
/* 223 */     int l = packed.length();
/* 224 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*     */ 
/* 228 */         count > 0)
/*     */     {
/* 225 */       count = packed.charAt(i++);
/* 226 */       int value = packed.charAt(i++);
/* 227 */       value--;
/* 228 */       result[(j++)] = value;count--;
/*     */     }
/* 230 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 240 */   private static final String[] ZZ_ERROR_MSG = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 249 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\b\000\001\t\004\001\001\t\001\001\001\t\t\001\001\000\005\001\b\000\005\001\b\000\005\001\b\000\007\001\b\000\001\001\001\t\005\001\004\000\001\t\004\000\002\001\001\t\002\001\001\t\002\000\001\t\003\000\001\t\002\000\001\t\003\001\001\t\005\000\001\001\001\t\001\001\005\000\001\001\002\t\006\000\001\t";
/*     */   
/*     */   private Reader zzReader;
/*     */   
/*     */   private int zzState;
/*     */   
/*     */ 
/*     */   private static int[] zzUnpackAttribute()
/*     */   {
/* 260 */     int[] result = new int[''];
/* 261 */     int offset = 0;
/* 262 */     offset = zzUnpackAttribute("\b\000\001\t\004\001\001\t\001\001\001\t\t\001\001\000\005\001\b\000\005\001\b\000\005\001\b\000\007\001\b\000\001\001\001\t\005\001\004\000\001\t\004\000\002\001\001\t\002\001\001\t\002\000\001\t\003\000\001\t\002\000\001\t\003\001\001\t\005\000\001\001\001\t\001\001\005\000\001\001\002\t\006\000\001\t", offset, result);
/* 263 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 267 */     int i = 0;
/* 268 */     int j = offset;
/* 269 */     int l = packed.length();
/* 270 */     int count; for (; i < l; 
/*     */         
/*     */ 
/* 273 */         count > 0)
/*     */     {
/* 271 */       count = packed.charAt(i++);
/* 272 */       int value = packed.charAt(i++);
/* 273 */       result[(j++)] = value;count--;
/*     */     }
/* 275 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 285 */   private int zzLexicalState = 0;
/*     */   
/*     */ 
/*     */ 
/* 289 */   private char[] zzBuffer = new char['䀀'];
/*     */   
/*     */ 
/*     */ 
/*     */   private int zzMarkedPos;
/*     */   
/*     */ 
/*     */ 
/*     */   private int zzPushbackPos;
/*     */   
/*     */ 
/*     */ 
/*     */   private int zzCurrentPos;
/*     */   
/*     */ 
/*     */ 
/*     */   private int zzStartRead;
/*     */   
/*     */ 
/*     */ 
/*     */   private int zzEndRead;
/*     */   
/*     */ 
/*     */ 
/*     */   private int yyline;
/*     */   
/*     */ 
/*     */   private int yychar;
/*     */   
/*     */ 
/*     */   private int yycolumn;
/*     */   
/*     */ 
/* 322 */   private boolean zzAtBOL = true;
/*     */   
/*     */   private boolean zzAtEOF;
/*     */   
/*     */   public static final int IGNORE = 0;
/*     */   
/*     */   public static final int ACCEPT = 1;
/*     */   
/*     */ 
/*     */   public void pushback(int n)
/*     */   {
/* 333 */     yypushback(n);
/*     */   }
/*     */   
/*     */   public String match() {
/* 337 */     return yytext();
/*     */   }
/*     */   
/*     */   private static void reportError(String yytext) {
/*     */     try {
/* 342 */       PrintWriter p = new PrintWriter(new OutputStreamWriter(System.err, "GB18030"), true);
/*     */       
/* 344 */       p.println("chtbl.flex tokenization error: \"" + yytext + "\"");
/*     */     } catch (UnsupportedEncodingException e) {
/* 346 */       System.err.println("chtbl.flex tokenization and encoding present error");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   CHTBLexer(Reader in)
/*     */   {
/* 359 */     this.zzReader = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   CHTBLexer(InputStream in)
/*     */   {
/* 369 */     this(new InputStreamReader(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char[] zzUnpackCMap(String packed)
/*     */   {
/* 379 */     char[] map = new char[65536];
/* 380 */     int i = 0;
/* 381 */     int j = 0;
/* 382 */     int count; for (; i < 202; 
/*     */         
/*     */ 
/* 385 */         count > 0)
/*     */     {
/* 383 */       count = packed.charAt(i++);
/* 384 */       char value = packed.charAt(i++);
/* 385 */       map[(j++)] = value;count--;
/*     */     }
/* 387 */     return map;
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
/*     */   private boolean zzRefill()
/*     */     throws IOException
/*     */   {
/* 401 */     if (this.zzStartRead > 0) {
/* 402 */       System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 407 */       this.zzEndRead -= this.zzStartRead;
/* 408 */       this.zzCurrentPos -= this.zzStartRead;
/* 409 */       this.zzMarkedPos -= this.zzStartRead;
/* 410 */       this.zzPushbackPos -= this.zzStartRead;
/* 411 */       this.zzStartRead = 0;
/*     */     }
/*     */     
/*     */ 
/* 415 */     if (this.zzCurrentPos >= this.zzBuffer.length)
/*     */     {
/* 417 */       char[] newBuffer = new char[this.zzCurrentPos * 2];
/* 418 */       System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
/* 419 */       this.zzBuffer = newBuffer;
/*     */     }
/*     */     
/*     */ 
/* 423 */     int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
/*     */     
/*     */ 
/* 426 */     if (numRead < 0) {
/* 427 */       return true;
/*     */     }
/*     */     
/* 430 */     this.zzEndRead += numRead;
/* 431 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yyclose()
/*     */     throws IOException
/*     */   {
/* 440 */     this.zzAtEOF = true;
/* 441 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 443 */     if (this.zzReader != null) {
/* 444 */       this.zzReader.close();
/*     */     }
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
/*     */   public final void yyreset(Reader reader)
/*     */   {
/* 459 */     this.zzReader = reader;
/* 460 */     this.zzAtBOL = true;
/* 461 */     this.zzAtEOF = false;
/* 462 */     this.zzEndRead = (this.zzStartRead = 0);
/* 463 */     this.zzCurrentPos = (this.zzMarkedPos = this.zzPushbackPos = 0);
/* 464 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 465 */     this.zzLexicalState = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yystate()
/*     */   {
/* 473 */     return this.zzLexicalState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yybegin(int newState)
/*     */   {
/* 483 */     this.zzLexicalState = newState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String yytext()
/*     */   {
/* 491 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/*     */   public final char yycharat(int pos)
/*     */   {
/* 507 */     return this.zzBuffer[(this.zzStartRead + pos)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yylength()
/*     */   {
/* 515 */     return this.zzMarkedPos - this.zzStartRead;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void zzScanError(int errorCode)
/*     */   {
/*     */     String message;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 536 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 539 */       message = ZZ_ERROR_MSG[0];
/*     */     }
/*     */     
/* 542 */     throw new Error(message);
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
/*     */   public void yypushback(int number)
/*     */   {
/* 555 */     if (number > yylength()) {
/* 556 */       zzScanError(2);
/*     */     }
/* 558 */     this.zzMarkedPos -= number;
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
/*     */   public int yylex()
/*     */     throws IOException
/*     */   {
/* 576 */     int zzEndReadL = this.zzEndRead;
/* 577 */     char[] zzBufferL = this.zzBuffer;
/* 578 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 580 */     int[] zzTransL = ZZ_TRANS;
/* 581 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 582 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     for (;;)
/*     */     {
/* 585 */       int zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 587 */       int zzAction = -1;
/*     */       
/* 589 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 591 */       this.zzState = this.zzLexicalState;
/*     */       
/*     */       int zzInput;
/*     */       for (;;)
/*     */       {
/*     */         int zzInput;
/* 597 */         if (zzCurrentPosL < zzEndReadL) {
/* 598 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/* 599 */         } else { if (this.zzAtEOF) {
/* 600 */             int zzInput = -1;
/* 601 */             break;
/*     */           }
/*     */           
/*     */ 
/* 605 */           this.zzCurrentPos = zzCurrentPosL;
/* 606 */           this.zzMarkedPos = zzMarkedPosL;
/* 607 */           boolean eof = zzRefill();
/*     */           
/* 609 */           zzCurrentPosL = this.zzCurrentPos;
/* 610 */           zzMarkedPosL = this.zzMarkedPos;
/* 611 */           zzBufferL = this.zzBuffer;
/* 612 */           zzEndReadL = this.zzEndRead;
/* 613 */           if (eof) {
/* 614 */             int zzInput = -1;
/* 615 */             break;
/*     */           }
/*     */           
/* 618 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/*     */         }
/*     */         
/* 621 */         int zzNext = zzTransL[(zzRowMapL[this.zzState] + zzCMapL[zzInput])];
/* 622 */         if (zzNext == -1) break;
/* 623 */         this.zzState = zzNext;
/*     */         
/* 625 */         int zzAttributes = zzAttrL[this.zzState];
/* 626 */         if ((zzAttributes & 0x1) == 1) {
/* 627 */           zzAction = this.zzState;
/* 628 */           zzMarkedPosL = zzCurrentPosL;
/* 629 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 636 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 638 */       switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
/*     */       case 5: 
/* 640 */         yybegin(6);return 0;
/*     */       case 16: 
/*     */         break;
/*     */       case 1: 
/* 644 */         reportError(yytext());
/*     */       case 17: 
/*     */         break;
/*     */       case 15: 
/* 648 */         yybegin(0);return 0;
/*     */       case 18: 
/*     */         break;
/*     */       case 2: 
/* 652 */         return 1;
/*     */       case 19: 
/*     */         break;
/*     */       case 13: 
/* 656 */         yybegin(7);return 0;
/*     */       case 20: 
/*     */         break;
/*     */       
/*     */       case 10: 
/* 661 */         yybegin(5);return 0;
/*     */       case 21: 
/*     */         break;
/*     */       
/*     */       case 6: 
/* 666 */         yybegin(1);return 0;
/*     */       case 22: 
/*     */         break;
/*     */       
/*     */       case 14: 
/* 671 */         yybegin(0);return 0;
/*     */       case 23: 
/*     */         break;
/*     */       
/*     */       case 8: 
/* 676 */         yybegin(0);return 0;
/*     */       case 24: 
/*     */         break;
/*     */       
/*     */       case 12: 
/* 681 */         yybegin(2);return 0;
/*     */       case 25: 
/*     */         break;
/*     */       
/*     */       case 11: 
/* 686 */         yybegin(0);return 0;
/*     */       case 26: 
/*     */         break;
/*     */       case 3: 
/* 690 */         return 0;
/*     */       case 27: 
/*     */         break;
/*     */       
/*     */       case 4: 
/* 695 */         yybegin(3);return 0;
/*     */       case 28: 
/*     */         break;
/*     */       case 9: 
/* 699 */         yybegin(5);return 0;
/*     */       case 29: 
/*     */         break;
/*     */       
/*     */       case 7: 
/* 704 */         yybegin(4);return 0;
/*     */       case 30: 
/*     */         break;
/*     */       default: 
/* 708 */         if ((zzInput == -1) && (this.zzStartRead == this.zzCurrentPos)) {
/* 709 */           this.zzAtEOF = true;
/* 710 */           return -1;
/*     */         }
/*     */         
/* 713 */         zzScanError(1);
/*     */       }
/*     */       
/*     */     }
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
/*     */   public static void main(String[] argv)
/*     */   {
/* 729 */     if (argv.length == 0) {
/* 730 */       System.out.println("Usage : java CHTBLexer <inputfile>");
/*     */     }
/*     */     else {
/* 733 */       for (int i = 0; i < argv.length; i++) {
/* 734 */         CHTBLexer scanner = null;
/*     */         try {
/* 736 */           scanner = new CHTBLexer(new FileReader(argv[i]));
/* 737 */           while (!scanner.zzAtEOF) scanner.yylex();
/*     */         }
/*     */         catch (FileNotFoundException e) {
/* 740 */           System.out.println("File not found : \"" + argv[i] + "\"");
/*     */         }
/*     */         catch (IOException e) {
/* 743 */           System.out.println("IO error scanning file \"" + argv[i] + "\"");
/* 744 */           System.out.println(e);
/*     */         }
/*     */         catch (Exception e) {
/* 747 */           System.out.println("Unexpected exception:");
/* 748 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\CHTBLexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */