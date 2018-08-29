/*     */ package edu.stanford.nlp.process;
/*     */ 
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
/*     */ class PTB2TextLexer
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\n\000\001\006\025\000\001\001\001\005\002\000\001\025\001\024\001\000\001\002\001\017\001\020\002\000\001\005\001\013\001\004\013\000\001\005\001\005\003\000\001\005\002\000\001\016\001\021\b\000\001\f\001\000\001\t\003\000\001\r\001\000\001\n\006\000\001\022\001\000\001\023\002\000\001\003\r\000\001\007\005\000\001\bﾋ\000";
/*  38 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\n\000\001\006\025\000\001\001\001\005\002\000\001\025\001\024\001\000\001\002\001\017\001\020\002\000\001\005\001\013\001\004\013\000\001\005\001\005\003\000\001\005\002\000\001\016\001\021\b\000\001\f\001\000\001\t\003\000\001\r\001\000\001\n\006\000\001\022\001\000\001\023\002\000\001\003\r\000\001\007\005\000\001\bﾋ\000");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */   
/*     */ 
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\001\000\b\001\001\000\002\002\003\000\001\003\001\004\001\005\001\006\002\001\001\007\001\b\001\t\001\n\004\000\002\001\001\013\001\f\002\000\002\001\002\000";
/*     */   
/*     */ 
/*     */   private static int[] zzUnpackAction()
/*     */   {
/*  51 */     int[] result = new int[39];
/*  52 */     int offset = 0;
/*  53 */     offset = zzUnpackAction("\001\000\b\001\001\000\002\002\003\000\001\003\001\004\001\005\001\006\002\001\001\007\001\b\001\t\001\n\004\000\002\001\001\013\001\f\002\000\002\001\002\000", offset, result);
/*  54 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/*  58 */     int i = 0;
/*  59 */     int j = offset;
/*  60 */     int l = packed.length();
/*  61 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*  64 */         count > 0)
/*     */     {
/*  62 */       count = packed.charAt(i++);
/*  63 */       int value = packed.charAt(i++);
/*  64 */       result[(j++)] = value;count--;
/*     */     }
/*  66 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\026\000,\000B\000X\000n\000\000\000°\000Æ\000Ü\000X\000ò\000Ĉ\000Ğ\000X\000X\000X\000X\000Ĵ\000Ŋ\000X\000X\000X\000X\000Š\000Ŷ\000ƌ\000Ƣ\000Ƹ\000ǎ\000X\000X\000Ǥ\000Ǻ\000Ȑ\000Ȧ\000ȼ\000ɒ";
/*     */   
/*     */ 
/*     */ 
/*     */   private static int[] zzUnpackRowMap()
/*     */   {
/*  83 */     int[] result = new int[39];
/*  84 */     int offset = 0;
/*  85 */     offset = zzUnpackRowMap("\000\000\000\026\000,\000B\000X\000n\000\000\000°\000Æ\000Ü\000X\000ò\000Ĉ\000Ğ\000X\000X\000X\000X\000Ĵ\000Ŋ\000X\000X\000X\000X\000Š\000Ŷ\000ƌ\000Ƣ\000Ƹ\000ǎ\000X\000X\000Ǥ\000Ǻ\000Ȑ\000Ȧ\000ȼ\000ɒ", offset, result);
/*  86 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/*  90 */     int i = 0;
/*  91 */     int j = offset;
/*  92 */     int l = packed.length();
/*  93 */     while (i < l) {
/*  94 */       int high = packed.charAt(i++) << '\020';
/*  95 */       result[(j++)] = (high | packed.charAt(i++));
/*     */     }
/*  97 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\002\001\003\001\002\001\004\002\002\001\005\004\002\001\006\003\002\001\007\002\002\001\b\002\002\001\t\001\002\001\000\004\002\001\000\017\002\002\000\001\n\001\000\001\013\001\f\001\000\001\r\001\000\001\016\001\000\001\017\004\000\001\020\002\000\001\021\001\022\001\000\001\002\001\023\001\002\001\024\002\002\001\000\017\002\026\000\001\002\001\000\004\002\001\000\005\002\001\025\n\002\001\026\004\002\001\000\020\002\001\027\004\002\001\000\020\002\001\030\004\002\001\000\017\002\001\f\001\000\001\031\003\f\001\000\017\f\004\000\001\032\023\000\001\033\025\000\001\034 \000\001\035\b\000\001\002\001\031\004\002\001\000\020\002\001\000\004\002\001\000\006\002\001\036\003\002\001\037\004\002\004\000\001\f\031\000\001 \027\000\001!\030\000\001\"\003\000\001#\004\000\001\002\001\000\004\002\001\000\007\002\001$\b\002\001\000\004\002\001\000\007\002\001%\007\002\016\000\001&\025\000\001'\007\000\001\002\001\000\004\002\001\000\004\002\001\007\013\002\001\000\004\002\001\000\004\002\001\b\n\002\013\000\001\020\025\000\001\021\n\000";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int[] zzUnpackTrans()
/*     */   {
/* 126 */     int[] result = new int['ɨ'];
/* 127 */     int offset = 0;
/* 128 */     offset = zzUnpackTrans("\001\002\001\003\001\002\001\004\002\002\001\005\004\002\001\006\003\002\001\007\002\002\001\b\002\002\001\t\001\002\001\000\004\002\001\000\017\002\002\000\001\n\001\000\001\013\001\f\001\000\001\r\001\000\001\016\001\000\001\017\004\000\001\020\002\000\001\021\001\022\001\000\001\002\001\023\001\002\001\024\002\002\001\000\017\002\026\000\001\002\001\000\004\002\001\000\005\002\001\025\n\002\001\026\004\002\001\000\020\002\001\027\004\002\001\000\020\002\001\030\004\002\001\000\017\002\001\f\001\000\001\031\003\f\001\000\017\f\004\000\001\032\023\000\001\033\025\000\001\034 \000\001\035\b\000\001\002\001\031\004\002\001\000\020\002\001\000\004\002\001\000\006\002\001\036\003\002\001\037\004\002\004\000\001\f\031\000\001 \027\000\001!\030\000\001\"\003\000\001#\004\000\001\002\001\000\004\002\001\000\007\002\001$\b\002\001\000\004\002\001\000\007\002\001%\007\002\016\000\001&\025\000\001'\007\000\001\002\001\000\004\002\001\000\004\002\001\007\013\002\001\000\004\002\001\000\004\002\001\b\n\002\013\000\001\020\025\000\001\021\n\000", offset, result);
/* 129 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 133 */     int i = 0;
/* 134 */     int j = offset;
/* 135 */     int l = packed.length();
/* 136 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*     */ 
/* 140 */         count > 0)
/*     */     {
/* 137 */       count = packed.charAt(i++);
/* 138 */       int value = packed.charAt(i++);
/* 139 */       value--;
/* 140 */       result[(j++)] = value;count--;
/*     */     }
/* 142 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */   private static final String[] ZZ_ERROR_MSG = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\001\000\003\001\001\t\004\001\001\000\001\001\001\t\003\000\004\t\002\001\004\t\004\000\002\001\002\t\002\000\002\001\002\000";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute()
/*     */   {
/* 169 */     int[] result = new int[39];
/* 170 */     int offset = 0;
/* 171 */     offset = zzUnpackAttribute("\001\000\003\001\001\t\004\001\001\000\001\001\001\t\003\000\004\t\002\001\004\t\004\000\002\001\002\t\002\000\002\001\002\000", offset, result);
/* 172 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 176 */     int i = 0;
/* 177 */     int j = offset;
/* 178 */     int l = packed.length();
/* 179 */     int count; for (; i < l; 
/*     */         
/*     */ 
/* 182 */         count > 0)
/*     */     {
/* 180 */       count = packed.charAt(i++);
/* 181 */       int value = packed.charAt(i++);
/* 182 */       result[(j++)] = value;count--;
/*     */     }
/* 184 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 194 */   private int zzLexicalState = 0;
/*     */   
/*     */ 
/*     */ 
/* 198 */   private char[] zzBuffer = new char['䀀'];
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
/* 231 */   private boolean zzAtBOL = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean zzAtEOF;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PTB2TextLexer(Reader in)
/*     */   {
/* 262 */     this.zzReader = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   PTB2TextLexer(InputStream in)
/*     */   {
/* 272 */     this(new InputStreamReader(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char[] zzUnpackCMap(String packed)
/*     */   {
/* 282 */     char[] map = new char[65536];
/* 283 */     int i = 0;
/* 284 */     int j = 0;
/* 285 */     int count; for (; i < 86; 
/*     */         
/*     */ 
/* 288 */         count > 0)
/*     */     {
/* 286 */       count = packed.charAt(i++);
/* 287 */       char value = packed.charAt(i++);
/* 288 */       map[(j++)] = value;count--;
/*     */     }
/* 290 */     return map;
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
/* 304 */     if (this.zzStartRead > 0) {
/* 305 */       System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 310 */       this.zzEndRead -= this.zzStartRead;
/* 311 */       this.zzCurrentPos -= this.zzStartRead;
/* 312 */       this.zzMarkedPos -= this.zzStartRead;
/* 313 */       this.zzPushbackPos -= this.zzStartRead;
/* 314 */       this.zzStartRead = 0;
/*     */     }
/*     */     
/*     */ 
/* 318 */     if (this.zzCurrentPos >= this.zzBuffer.length)
/*     */     {
/* 320 */       char[] newBuffer = new char[this.zzCurrentPos * 2];
/* 321 */       System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
/* 322 */       this.zzBuffer = newBuffer;
/*     */     }
/*     */     
/*     */ 
/* 326 */     int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
/*     */     
/*     */ 
/* 329 */     if (numRead < 0) {
/* 330 */       return true;
/*     */     }
/*     */     
/* 333 */     this.zzEndRead += numRead;
/* 334 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yyclose()
/*     */     throws IOException
/*     */   {
/* 343 */     this.zzAtEOF = true;
/* 344 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 346 */     if (this.zzReader != null) {
/* 347 */       this.zzReader.close();
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
/* 362 */     this.zzReader = reader;
/* 363 */     this.zzAtBOL = true;
/* 364 */     this.zzAtEOF = false;
/* 365 */     this.zzEndRead = (this.zzStartRead = 0);
/* 366 */     this.zzCurrentPos = (this.zzMarkedPos = this.zzPushbackPos = 0);
/* 367 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 368 */     this.zzLexicalState = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yystate()
/*     */   {
/* 376 */     return this.zzLexicalState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yybegin(int newState)
/*     */   {
/* 386 */     this.zzLexicalState = newState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String yytext()
/*     */   {
/* 394 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 410 */     return this.zzBuffer[(this.zzStartRead + pos)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yylength()
/*     */   {
/* 418 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 439 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 442 */       message = ZZ_ERROR_MSG[0];
/*     */     }
/*     */     
/* 445 */     throw new Error(message);
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
/* 458 */     if (number > yylength()) {
/* 459 */       zzScanError(2);
/*     */     }
/* 461 */     this.zzMarkedPos -= number;
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
/*     */   public String next()
/*     */     throws IOException
/*     */   {
/* 479 */     int zzEndReadL = this.zzEndRead;
/* 480 */     char[] zzBufferL = this.zzBuffer;
/* 481 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 483 */     int[] zzTransL = ZZ_TRANS;
/* 484 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 485 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     for (;;)
/*     */     {
/* 488 */       int zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 490 */       int zzAction = -1;
/*     */       
/* 492 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 494 */       this.zzState = this.zzLexicalState;
/*     */       
/*     */       int zzInput;
/*     */       for (;;)
/*     */       {
/*     */         int zzInput;
/* 500 */         if (zzCurrentPosL < zzEndReadL) {
/* 501 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/* 502 */         } else { if (this.zzAtEOF) {
/* 503 */             int zzInput = -1;
/* 504 */             break;
/*     */           }
/*     */           
/*     */ 
/* 508 */           this.zzCurrentPos = zzCurrentPosL;
/* 509 */           this.zzMarkedPos = zzMarkedPosL;
/* 510 */           boolean eof = zzRefill();
/*     */           
/* 512 */           zzCurrentPosL = this.zzCurrentPos;
/* 513 */           zzMarkedPosL = this.zzMarkedPos;
/* 514 */           zzBufferL = this.zzBuffer;
/* 515 */           zzEndReadL = this.zzEndRead;
/* 516 */           if (eof) {
/* 517 */             int zzInput = -1;
/* 518 */             break;
/*     */           }
/*     */           
/* 521 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/*     */         }
/*     */         
/* 524 */         int zzNext = zzTransL[(zzRowMapL[this.zzState] + zzCMapL[zzInput])];
/* 525 */         if (zzNext == -1) break;
/* 526 */         this.zzState = zzNext;
/*     */         
/* 528 */         int zzAttributes = zzAttrL[this.zzState];
/* 529 */         if ((zzAttributes & 0x1) == 1) {
/* 530 */           zzAction = this.zzState;
/* 531 */           zzMarkedPosL = zzCurrentPosL;
/* 532 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 539 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 541 */       switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
/*     */       case 5: 
/* 543 */         return "%";
/*     */       case 13: 
/*     */         break;
/*     */       case 1: 
/* 547 */         return yytext();
/*     */       case 14: 
/*     */         break;
/*     */       case 2: 
/* 551 */         return yytext().substring(1, yytext().length());
/*     */       case 15: 
/*     */         break;
/*     */       case 7: 
/* 555 */         return "(";
/*     */       case 16: 
/*     */         break;
/*     */       case 9: 
/* 559 */         return "$";
/*     */       case 17: 
/*     */         break;
/*     */       case 11: 
/* 563 */         return "n't";
/*     */       case 18: 
/*     */         break;
/*     */       case 4: 
/* 567 */         return "]";
/*     */       case 19: 
/*     */         break;
/*     */       case 6: 
/* 571 */         return "`";
/*     */       case 20: 
/*     */         break;
/*     */       case 12: 
/* 575 */         return "N'T";
/*     */       case 21: 
/*     */         break;
/*     */       case 8: 
/* 579 */         return "[";
/*     */       case 22: 
/*     */         break;
/*     */       case 10: 
/* 583 */         return "\"";
/*     */       case 23: 
/*     */         break;
/*     */       case 3: 
/* 587 */         return ")";
/*     */       case 24: 
/*     */         break;
/*     */       default: 
/* 591 */         if ((zzInput == -1) && (this.zzStartRead == this.zzCurrentPos)) {
/* 592 */           this.zzAtEOF = true;
/* 593 */           return null;
/*     */         }
/*     */         
/* 596 */         zzScanError(1);
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
/* 612 */     if (argv.length == 0) {
/* 613 */       System.out.println("Usage : java PTB2TextLexer <inputfile>");
/*     */     }
/*     */     else {
/* 616 */       for (int i = 0; i < argv.length; i++) {
/* 617 */         PTB2TextLexer scanner = null;
/*     */         try {
/* 619 */           scanner = new PTB2TextLexer(new FileReader(argv[i]));
/* 620 */           while (!scanner.zzAtEOF) scanner.next();
/*     */         }
/*     */         catch (FileNotFoundException e) {
/* 623 */           System.out.println("File not found : \"" + argv[i] + "\"");
/*     */         }
/*     */         catch (IOException e) {
/* 626 */           System.out.println("IO error scanning file \"" + argv[i] + "\"");
/* 627 */           System.out.println(e);
/*     */         }
/*     */         catch (Exception e) {
/* 630 */           System.out.println("Unexpected exception:");
/* 631 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\PTB2TextLexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */