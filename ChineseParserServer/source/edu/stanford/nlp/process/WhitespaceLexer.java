/*     */ package edu.stanford.nlp.process;
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
/*     */ class WhitespaceLexer
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\004\001\002\002\004\001\001\022\000\001\004d\000\001\003\032\000\001\004ὡ\000\n\004\034\000\001\003\001\003\005\000\001\004࿐\000\001\004쿿\000";
/*  38 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\004\001\002\002\004\001\001\022\000\001\004d\000\001\003\032\000\001\004ὡ\000\n\004\034\000\001\003\001\003\005\000\001\004࿐\000\001\004쿿\000");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\001\000\001\001\002\002\001\003";
/*     */   
/*     */   private static int[] zzUnpackAction()
/*     */   {
/*  49 */     int[] result = new int[5];
/*  50 */     int offset = 0;
/*  51 */     offset = zzUnpackAction("\001\000\001\001\002\002\001\003", offset, result);
/*  52 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/*  56 */     int i = 0;
/*  57 */     int j = offset;
/*  58 */     int l = packed.length();
/*  59 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*  62 */         count > 0)
/*     */     {
/*  60 */       count = packed.charAt(i++);
/*  61 */       int value = packed.charAt(i++);
/*  62 */       result[(j++)] = value;count--;
/*     */     }
/*  64 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\005\000\n\000\017\000\024";
/*     */   
/*     */   private static int[] zzUnpackRowMap()
/*     */   {
/*  77 */     int[] result = new int[5];
/*  78 */     int offset = 0;
/*  79 */     offset = zzUnpackRowMap("\000\000\000\005\000\n\000\017\000\024", offset, result);
/*  80 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/*  84 */     int i = 0;
/*  85 */     int j = offset;
/*  86 */     int l = packed.length();
/*  87 */     while (i < l) {
/*  88 */       int high = packed.charAt(i++) << '\020';
/*  89 */       result[(j++)] = (high | packed.charAt(i++));
/*     */     }
/*  91 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  97 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\002\001\003\002\004\001\005\001\002\006\000\001\004\013\000\001\005";
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 104 */     int[] result = new int[25];
/* 105 */     int offset = 0;
/* 106 */     offset = zzUnpackTrans("\001\002\001\003\002\004\001\005\001\002\006\000\001\004\013\000\001\005", offset, result);
/* 107 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 111 */     int i = 0;
/* 112 */     int j = offset;
/* 113 */     int l = packed.length();
/* 114 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*     */ 
/* 118 */         count > 0)
/*     */     {
/* 115 */       count = packed.charAt(i++);
/* 116 */       int value = packed.charAt(i++);
/* 117 */       value--;
/* 118 */       result[(j++)] = value;count--;
/*     */     }
/* 120 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */   private static final String[] ZZ_ERROR_MSG = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\001\000\002\001\001\t\001\001";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 145 */     int[] result = new int[5];
/* 146 */     int offset = 0;
/* 147 */     offset = zzUnpackAttribute("\001\000\002\001\001\t\001\001", offset, result);
/* 148 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 152 */     int i = 0;
/* 153 */     int j = offset;
/* 154 */     int l = packed.length();
/* 155 */     int count; for (; i < l; 
/*     */         
/*     */ 
/* 158 */         count > 0)
/*     */     {
/* 156 */       count = packed.charAt(i++);
/* 157 */       int value = packed.charAt(i++);
/* 158 */       result[(j++)] = value;count--;
/*     */     }
/* 160 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 170 */   private int zzLexicalState = 0;
/*     */   
/*     */ 
/*     */ 
/* 174 */   private char[] zzBuffer = new char['䀀'];
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
/* 207 */   private boolean zzAtBOL = true;
/*     */   
/*     */ 
/*     */   private boolean zzAtEOF;
/*     */   
/*     */ 
/* 213 */   static final Word crValue = new Word("\n");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   WhitespaceLexer(Reader in)
/*     */   {
/* 223 */     this.zzReader = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   WhitespaceLexer(InputStream in)
/*     */   {
/* 233 */     this(new InputStreamReader(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char[] zzUnpackCMap(String packed)
/*     */   {
/* 243 */     char[] map = new char[65536];
/* 244 */     int i = 0;
/* 245 */     int j = 0;
/* 246 */     int count; for (; i < 42; 
/*     */         
/*     */ 
/* 249 */         count > 0)
/*     */     {
/* 247 */       count = packed.charAt(i++);
/* 248 */       char value = packed.charAt(i++);
/* 249 */       map[(j++)] = value;count--;
/*     */     }
/* 251 */     return map;
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
/* 265 */     if (this.zzStartRead > 0) {
/* 266 */       System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 271 */       this.zzEndRead -= this.zzStartRead;
/* 272 */       this.zzCurrentPos -= this.zzStartRead;
/* 273 */       this.zzMarkedPos -= this.zzStartRead;
/* 274 */       this.zzPushbackPos -= this.zzStartRead;
/* 275 */       this.zzStartRead = 0;
/*     */     }
/*     */     
/*     */ 
/* 279 */     if (this.zzCurrentPos >= this.zzBuffer.length)
/*     */     {
/* 281 */       char[] newBuffer = new char[this.zzCurrentPos * 2];
/* 282 */       System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
/* 283 */       this.zzBuffer = newBuffer;
/*     */     }
/*     */     
/*     */ 
/* 287 */     int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
/*     */     
/*     */ 
/* 290 */     if (numRead < 0) {
/* 291 */       return true;
/*     */     }
/*     */     
/* 294 */     this.zzEndRead += numRead;
/* 295 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yyclose()
/*     */     throws IOException
/*     */   {
/* 304 */     this.zzAtEOF = true;
/* 305 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 307 */     if (this.zzReader != null) {
/* 308 */       this.zzReader.close();
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
/* 323 */     this.zzReader = reader;
/* 324 */     this.zzAtBOL = true;
/* 325 */     this.zzAtEOF = false;
/* 326 */     this.zzEndRead = (this.zzStartRead = 0);
/* 327 */     this.zzCurrentPos = (this.zzMarkedPos = this.zzPushbackPos = 0);
/* 328 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 329 */     this.zzLexicalState = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yystate()
/*     */   {
/* 337 */     return this.zzLexicalState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yybegin(int newState)
/*     */   {
/* 347 */     this.zzLexicalState = newState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String yytext()
/*     */   {
/* 355 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 371 */     return this.zzBuffer[(this.zzStartRead + pos)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yylength()
/*     */   {
/* 379 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 400 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 403 */       message = ZZ_ERROR_MSG[0];
/*     */     }
/*     */     
/* 406 */     throw new Error(message);
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
/* 419 */     if (number > yylength()) {
/* 420 */       zzScanError(2);
/*     */     }
/* 422 */     this.zzMarkedPos -= number;
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
/* 440 */     int zzEndReadL = this.zzEndRead;
/* 441 */     char[] zzBufferL = this.zzBuffer;
/* 442 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 444 */     int[] zzTransL = ZZ_TRANS;
/* 445 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 446 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     for (;;)
/*     */     {
/* 449 */       int zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 451 */       int zzAction = -1;
/*     */       
/* 453 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 455 */       this.zzState = this.zzLexicalState;
/*     */       
/*     */       int zzInput;
/*     */       for (;;)
/*     */       {
/*     */         int zzInput;
/* 461 */         if (zzCurrentPosL < zzEndReadL) {
/* 462 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/* 463 */         } else { if (this.zzAtEOF) {
/* 464 */             int zzInput = -1;
/* 465 */             break;
/*     */           }
/*     */           
/*     */ 
/* 469 */           this.zzCurrentPos = zzCurrentPosL;
/* 470 */           this.zzMarkedPos = zzMarkedPosL;
/* 471 */           boolean eof = zzRefill();
/*     */           
/* 473 */           zzCurrentPosL = this.zzCurrentPos;
/* 474 */           zzMarkedPosL = this.zzMarkedPos;
/* 475 */           zzBufferL = this.zzBuffer;
/* 476 */           zzEndReadL = this.zzEndRead;
/* 477 */           if (eof) {
/* 478 */             int zzInput = -1;
/* 479 */             break;
/*     */           }
/*     */           
/* 482 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/*     */         }
/*     */         
/* 485 */         int zzNext = zzTransL[(zzRowMapL[this.zzState] + zzCMapL[zzInput])];
/* 486 */         if (zzNext == -1) break;
/* 487 */         this.zzState = zzNext;
/*     */         
/* 489 */         int zzAttributes = zzAttrL[this.zzState];
/* 490 */         if ((zzAttributes & 0x1) == 1) {
/* 491 */           zzAction = this.zzState;
/* 492 */           zzMarkedPosL = zzCurrentPosL;
/* 493 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 500 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 502 */       switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
/*     */       case 1: 
/* 504 */         return new Word(yytext());
/*     */       case 4: 
/*     */         break;
/*     */       case 2: 
/* 508 */         return crValue;
/*     */       case 5: 
/*     */         break;
/*     */       case 3: 
/*     */       case 6: 
/*     */         break;
/*     */       
/*     */       default: 
/* 516 */         if ((zzInput == -1) && (this.zzStartRead == this.zzCurrentPos)) {
/* 517 */           this.zzAtEOF = true;
/*     */           
/* 519 */           return null;
/*     */         }
/*     */         
/*     */ 
/* 523 */         zzScanError(1);
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
/* 539 */     if (argv.length == 0) {
/* 540 */       System.out.println("Usage : java WhitespaceLexer <inputfile>");
/*     */     }
/*     */     else {
/* 543 */       for (int i = 0; i < argv.length; i++) {
/* 544 */         WhitespaceLexer scanner = null;
/*     */         try {
/* 546 */           scanner = new WhitespaceLexer(new FileReader(argv[i]));
/* 547 */           while (!scanner.zzAtEOF) scanner.next();
/*     */         }
/*     */         catch (FileNotFoundException e) {
/* 550 */           System.out.println("File not found : \"" + argv[i] + "\"");
/*     */         }
/*     */         catch (IOException e) {
/* 553 */           System.out.println("IO error scanning file \"" + argv[i] + "\"");
/* 554 */           System.out.println(e);
/*     */         }
/*     */         catch (Exception e) {
/* 557 */           System.out.println("Unexpected exception:");
/* 558 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\WhitespaceLexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */