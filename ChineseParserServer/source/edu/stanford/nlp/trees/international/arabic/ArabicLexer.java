/*     */ package edu.stanford.nlp.trees.international.arabic;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Word;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ArabicLexer
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\004\001\002\002\004\001\001\022\000\001\004\001\000\001\005\t\000\001\005\001\000\001\005V\000\001\003\032\000\001\004ὡ\000\n\004\034\000\001\003\001\003\005\000\001\004࿐\000\001\004쿿\000";
/*  41 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\004\001\002\002\004\001\001\022\000\001\004\001\000\001\005\t\000\001\005\001\000\001\005V\000\001\003\032\000\001\004ὡ\000\n\004\034\000\001\003\001\003\005\000\001\004࿐\000\001\004쿿\000");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  46 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\001\000\001\001\002\002\001\003\001\001";
/*     */   
/*     */   private static int[] zzUnpackAction()
/*     */   {
/*  52 */     int[] result = new int[6];
/*  53 */     int offset = 0;
/*  54 */     offset = zzUnpackAction("\001\000\001\001\002\002\001\003\001\001", offset, result);
/*  55 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/*  59 */     int i = 0;
/*  60 */     int j = offset;
/*  61 */     int l = packed.length();
/*  62 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*  65 */         count > 0)
/*     */     {
/*  63 */       count = packed.charAt(i++);
/*  64 */       int value = packed.charAt(i++);
/*  65 */       result[(j++)] = value;count--;
/*     */     }
/*  67 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\006\000\f\000\022\000\030\000\022";
/*     */   
/*     */   private static int[] zzUnpackRowMap()
/*     */   {
/*  80 */     int[] result = new int[6];
/*  81 */     int offset = 0;
/*  82 */     offset = zzUnpackRowMap("\000\000\000\006\000\f\000\022\000\030\000\022", offset, result);
/*  83 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/*  87 */     int i = 0;
/*  88 */     int j = offset;
/*  89 */     int l = packed.length();
/*  90 */     while (i < l) {
/*  91 */       int high = packed.charAt(i++) << '\020';
/*  92 */       result[(j++)] = (high | packed.charAt(i++));
/*     */     }
/*  94 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 100 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\002\001\003\002\004\001\005\001\006\001\002\007\000\001\004\r\000\001\005\001\000";
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 107 */     int[] result = new int[30];
/* 108 */     int offset = 0;
/* 109 */     offset = zzUnpackTrans("\001\002\001\003\002\004\001\005\001\006\001\002\007\000\001\004\r\000\001\005\001\000", offset, result);
/* 110 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 114 */     int i = 0;
/* 115 */     int j = offset;
/* 116 */     int l = packed.length();
/* 117 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*     */ 
/* 121 */         count > 0)
/*     */     {
/* 118 */       count = packed.charAt(i++);
/* 119 */       int value = packed.charAt(i++);
/* 120 */       value--;
/* 121 */       result[(j++)] = value;count--;
/*     */     }
/* 123 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */   private static final String[] ZZ_ERROR_MSG = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\001\000\002\001\001\t\001\001\001\t";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 148 */     int[] result = new int[6];
/* 149 */     int offset = 0;
/* 150 */     offset = zzUnpackAttribute("\001\000\002\001\001\t\001\001\001\t", offset, result);
/* 151 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 155 */     int i = 0;
/* 156 */     int j = offset;
/* 157 */     int l = packed.length();
/* 158 */     int count; for (; i < l; 
/*     */         
/*     */ 
/* 161 */         count > 0)
/*     */     {
/* 159 */       count = packed.charAt(i++);
/* 160 */       int value = packed.charAt(i++);
/* 161 */       result[(j++)] = value;count--;
/*     */     }
/* 163 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 173 */   private int zzLexicalState = 0;
/*     */   
/*     */ 
/*     */ 
/* 177 */   private char[] zzBuffer = new char['䀀'];
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
/* 210 */   private boolean zzAtBOL = true;
/*     */   
/*     */ 
/*     */   private boolean zzAtEOF;
/*     */   
/*     */ 
/* 216 */   static final Word crValue = new Word("\n");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ArabicLexer(Reader in)
/*     */   {
/* 226 */     this.zzReader = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ArabicLexer(InputStream in)
/*     */   {
/* 236 */     this(new InputStreamReader(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char[] zzUnpackCMap(String packed)
/*     */   {
/* 246 */     char[] map = new char[65536];
/* 247 */     int i = 0;
/* 248 */     int j = 0;
/* 249 */     int count; for (; i < 54; 
/*     */         
/*     */ 
/* 252 */         count > 0)
/*     */     {
/* 250 */       count = packed.charAt(i++);
/* 251 */       char value = packed.charAt(i++);
/* 252 */       map[(j++)] = value;count--;
/*     */     }
/* 254 */     return map;
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
/* 268 */     if (this.zzStartRead > 0) {
/* 269 */       System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 274 */       this.zzEndRead -= this.zzStartRead;
/* 275 */       this.zzCurrentPos -= this.zzStartRead;
/* 276 */       this.zzMarkedPos -= this.zzStartRead;
/* 277 */       this.zzPushbackPos -= this.zzStartRead;
/* 278 */       this.zzStartRead = 0;
/*     */     }
/*     */     
/*     */ 
/* 282 */     if (this.zzCurrentPos >= this.zzBuffer.length)
/*     */     {
/* 284 */       char[] newBuffer = new char[this.zzCurrentPos * 2];
/* 285 */       System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
/* 286 */       this.zzBuffer = newBuffer;
/*     */     }
/*     */     
/*     */ 
/* 290 */     int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
/*     */     
/*     */ 
/* 293 */     if (numRead < 0) {
/* 294 */       return true;
/*     */     }
/*     */     
/* 297 */     this.zzEndRead += numRead;
/* 298 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yyclose()
/*     */     throws IOException
/*     */   {
/* 307 */     this.zzAtEOF = true;
/* 308 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 310 */     if (this.zzReader != null) {
/* 311 */       this.zzReader.close();
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
/* 326 */     this.zzReader = reader;
/* 327 */     this.zzAtBOL = true;
/* 328 */     this.zzAtEOF = false;
/* 329 */     this.zzEndRead = (this.zzStartRead = 0);
/* 330 */     this.zzCurrentPos = (this.zzMarkedPos = this.zzPushbackPos = 0);
/* 331 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 332 */     this.zzLexicalState = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yystate()
/*     */   {
/* 340 */     return this.zzLexicalState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yybegin(int newState)
/*     */   {
/* 350 */     this.zzLexicalState = newState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String yytext()
/*     */   {
/* 358 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 374 */     return this.zzBuffer[(this.zzStartRead + pos)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yylength()
/*     */   {
/* 382 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 403 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 406 */       message = ZZ_ERROR_MSG[0];
/*     */     }
/*     */     
/* 409 */     throw new Error(message);
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
/* 422 */     if (number > yylength()) {
/* 423 */       zzScanError(2);
/*     */     }
/* 425 */     this.zzMarkedPos -= number;
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
/*     */   public Word next()
/*     */     throws IOException
/*     */   {
/* 443 */     int zzEndReadL = this.zzEndRead;
/* 444 */     char[] zzBufferL = this.zzBuffer;
/* 445 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 447 */     int[] zzTransL = ZZ_TRANS;
/* 448 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 449 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     for (;;)
/*     */     {
/* 452 */       int zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 454 */       int zzAction = -1;
/*     */       
/* 456 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 458 */       this.zzState = this.zzLexicalState;
/*     */       
/*     */       int zzInput;
/*     */       for (;;)
/*     */       {
/*     */         int zzInput;
/* 464 */         if (zzCurrentPosL < zzEndReadL) {
/* 465 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/* 466 */         } else { if (this.zzAtEOF) {
/* 467 */             int zzInput = -1;
/* 468 */             break;
/*     */           }
/*     */           
/*     */ 
/* 472 */           this.zzCurrentPos = zzCurrentPosL;
/* 473 */           this.zzMarkedPos = zzMarkedPosL;
/* 474 */           boolean eof = zzRefill();
/*     */           
/* 476 */           zzCurrentPosL = this.zzCurrentPos;
/* 477 */           zzMarkedPosL = this.zzMarkedPos;
/* 478 */           zzBufferL = this.zzBuffer;
/* 479 */           zzEndReadL = this.zzEndRead;
/* 480 */           if (eof) {
/* 481 */             int zzInput = -1;
/* 482 */             break;
/*     */           }
/*     */           
/* 485 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/*     */         }
/*     */         
/* 488 */         int zzNext = zzTransL[(zzRowMapL[this.zzState] + zzCMapL[zzInput])];
/* 489 */         if (zzNext == -1) break;
/* 490 */         this.zzState = zzNext;
/*     */         
/* 492 */         int zzAttributes = zzAttrL[this.zzState];
/* 493 */         if ((zzAttributes & 0x1) == 1) {
/* 494 */           zzAction = this.zzState;
/* 495 */           zzMarkedPosL = zzCurrentPosL;
/* 496 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 503 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 505 */       switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
/*     */       case 1: 
/* 507 */         return new Word(yytext());
/*     */       case 4: 
/*     */         break;
/*     */       case 2: 
/* 511 */         return crValue;
/*     */       case 5: 
/*     */         break;
/*     */       case 3: 
/*     */       case 6: 
/*     */         break;
/*     */       
/*     */       default: 
/* 519 */         if ((zzInput == -1) && (this.zzStartRead == this.zzCurrentPos)) {
/* 520 */           this.zzAtEOF = true;
/*     */           
/* 522 */           return null;
/*     */         }
/*     */         
/*     */ 
/* 526 */         zzScanError(1);
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
/* 542 */     if (argv.length == 0) {
/* 543 */       System.out.println("Usage : java ArabicLexer <inputfile>");
/*     */     }
/*     */     else {
/* 546 */       for (int i = 0; i < argv.length; i++) {
/* 547 */         ArabicLexer scanner = null;
/*     */         try {
/* 549 */           scanner = new ArabicLexer(new FileReader(argv[i]));
/* 550 */           while (!scanner.zzAtEOF) scanner.next();
/*     */         }
/*     */         catch (FileNotFoundException e) {
/* 553 */           System.out.println("File not found : \"" + argv[i] + "\"");
/*     */         }
/*     */         catch (IOException e) {
/* 556 */           System.out.println("IO error scanning file \"" + argv[i] + "\"");
/* 557 */           System.out.println(e);
/*     */         }
/*     */         catch (Exception e) {
/* 560 */           System.out.println("Unexpected exception:");
/* 561 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\arabic\ArabicLexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */