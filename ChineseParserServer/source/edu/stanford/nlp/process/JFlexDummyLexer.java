/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.io.Lexer;
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
/*     */ class JFlexDummyLexer
/*     */   implements Lexer
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int YY_BUFFERSIZE = 16384;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String yycmap_packed = "\t\000\001\001\001\001\003\001\022\000\001\001d\000\001\001ᾢ\000\002\001?\000";
/*  36 */   private static final char[] yycmap = yy_unpack_cmap("\t\000\001\001\001\001\003\001\022\000\001\001d\000\001\001ᾢ\000\002\001?\000");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  41 */   private static final int[] yy_rowMap = { 0, 2, 4 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String yy_packed0 = "\001\002\001\003\001\002\002\000\001\003";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private static final int[] yytrans = yy_unpack();
/*     */   
/*     */   private static final int YY_UNKNOWN_ERROR = 0;
/*     */   
/*     */   private static final int YY_ILLEGAL_STATE = 1;
/*     */   
/*     */   private static final int YY_NO_MATCH = 2;
/*     */   
/*     */   private static final int YY_PUSHBACK_2BIG = 3;
/*     */   
/*  64 */   private static final String[] YY_ERROR_MSG = { "Unkown internal scanner error", "Internal error: unknown state", "Error: could not match input", "Error: pushback value was too large" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private static final byte[] YY_ATTRIBUTE = { 0, 1, 1 };
/*     */   
/*     */ 
/*     */ 
/*     */   private Reader yy_reader;
/*     */   
/*     */ 
/*     */ 
/*     */   private int yy_state;
/*     */   
/*     */ 
/*  85 */   private int yy_lexical_state = 0;
/*     */   
/*     */ 
/*     */ 
/*  89 */   private char[] yy_buffer = new char['䀀'];
/*     */   
/*     */ 
/*     */ 
/*     */   private int yy_markedPos;
/*     */   
/*     */ 
/*     */ 
/*     */   private int yy_pushbackPos;
/*     */   
/*     */ 
/*     */ 
/*     */   private int yy_currentPos;
/*     */   
/*     */ 
/*     */ 
/*     */   private int yy_startRead;
/*     */   
/*     */ 
/*     */ 
/*     */   private int yy_endRead;
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
/* 122 */   private boolean yy_atBOL = true;
/*     */   
/*     */   private boolean yy_atEOF;
/*     */   
/*     */ 
/*     */   public void pushBack(int n)
/*     */   {
/* 129 */     yypushback(n);
/*     */   }
/*     */   
/*     */   public int getYYEOF() {
/* 133 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   JFlexDummyLexer(Reader in)
/*     */   {
/* 144 */     this.yy_reader = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   JFlexDummyLexer(InputStream in)
/*     */   {
/* 154 */     this(new InputStreamReader(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int[] yy_unpack()
/*     */   {
/* 163 */     int[] trans = new int[6];
/* 164 */     int offset = 0;
/* 165 */     offset = yy_unpack("\001\002\001\003\001\002\002\000\001\003", offset, trans);
/* 166 */     return trans;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int yy_unpack(String packed, int offset, int[] trans)
/*     */   {
/* 176 */     int i = 0;
/* 177 */     int j = offset;
/* 178 */     int l = packed.length();
/* 179 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*     */ 
/* 183 */         count > 0)
/*     */     {
/* 180 */       count = packed.charAt(i++);
/* 181 */       int value = packed.charAt(i++);
/* 182 */       value--;
/* 183 */       trans[(j++)] = value;count--;
/*     */     }
/* 185 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char[] yy_unpack_cmap(String packed)
/*     */   {
/* 195 */     char[] map = new char[65536];
/* 196 */     int i = 0;
/* 197 */     int j = 0;
/* 198 */     int count; for (; i < 22; 
/*     */         
/*     */ 
/* 201 */         count > 0)
/*     */     {
/* 199 */       count = packed.charAt(i++);
/* 200 */       char value = packed.charAt(i++);
/* 201 */       map[(j++)] = value;count--;
/*     */     }
/* 203 */     return map;
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
/*     */   private boolean yy_refill()
/*     */     throws IOException
/*     */   {
/* 217 */     if (this.yy_startRead > 0) {
/* 218 */       System.arraycopy(this.yy_buffer, this.yy_startRead, this.yy_buffer, 0, this.yy_endRead - this.yy_startRead);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 223 */       this.yy_endRead -= this.yy_startRead;
/* 224 */       this.yy_currentPos -= this.yy_startRead;
/* 225 */       this.yy_markedPos -= this.yy_startRead;
/* 226 */       this.yy_pushbackPos -= this.yy_startRead;
/* 227 */       this.yy_startRead = 0;
/*     */     }
/*     */     
/*     */ 
/* 231 */     if (this.yy_currentPos >= this.yy_buffer.length)
/*     */     {
/* 233 */       char[] newBuffer = new char[this.yy_currentPos * 2];
/* 234 */       System.arraycopy(this.yy_buffer, 0, newBuffer, 0, this.yy_buffer.length);
/* 235 */       this.yy_buffer = newBuffer;
/*     */     }
/*     */     
/*     */ 
/* 239 */     int numRead = this.yy_reader.read(this.yy_buffer, this.yy_endRead, this.yy_buffer.length - this.yy_endRead);
/*     */     
/*     */ 
/* 242 */     if (numRead < 0) {
/* 243 */       return true;
/*     */     }
/*     */     
/* 246 */     this.yy_endRead += numRead;
/* 247 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yyclose()
/*     */     throws IOException
/*     */   {
/* 256 */     this.yy_atEOF = true;
/* 257 */     this.yy_endRead = this.yy_startRead;
/*     */     
/* 259 */     if (this.yy_reader != null) {
/* 260 */       this.yy_reader.close();
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
/*     */   public final void yyreset(Reader reader)
/*     */     throws IOException
/*     */   {
/* 275 */     yyclose();
/* 276 */     this.yy_reader = reader;
/* 277 */     this.yy_atBOL = true;
/* 278 */     this.yy_atEOF = false;
/* 279 */     this.yy_endRead = (this.yy_startRead = 0);
/* 280 */     this.yy_currentPos = (this.yy_markedPos = this.yy_pushbackPos = 0);
/* 281 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 282 */     this.yy_lexical_state = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yystate()
/*     */   {
/* 290 */     return this.yy_lexical_state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yybegin(int newState)
/*     */   {
/* 300 */     this.yy_lexical_state = newState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String yytext()
/*     */   {
/* 308 */     return new String(this.yy_buffer, this.yy_startRead, this.yy_markedPos - this.yy_startRead);
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
/* 324 */     return this.yy_buffer[(this.yy_startRead + pos)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yylength()
/*     */   {
/* 332 */     return this.yy_markedPos - this.yy_startRead;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void yy_ScanError(int errorCode)
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
/* 353 */       message = YY_ERROR_MSG[errorCode];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 356 */       message = YY_ERROR_MSG[0];
/*     */     }
/*     */     
/* 359 */     throw new Error(message);
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
/*     */   private void yypushback(int number)
/*     */   {
/* 372 */     if (number > yylength()) {
/* 373 */       yy_ScanError(3);
/*     */     }
/* 375 */     this.yy_markedPos -= number;
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
/*     */   public int yylex()
/*     */     throws IOException
/*     */   {
/* 394 */     int yy_endRead_l = this.yy_endRead;
/* 395 */     char[] yy_buffer_l = this.yy_buffer;
/* 396 */     char[] yycmap_l = yycmap;
/*     */     
/* 398 */     int[] yytrans_l = yytrans;
/* 399 */     int[] yy_rowMap_l = yy_rowMap;
/* 400 */     byte[] yy_attr_l = YY_ATTRIBUTE;
/*     */     for (;;)
/*     */     {
/* 403 */       int yy_markedPos_l = this.yy_markedPos;
/*     */       
/* 405 */       int yy_action = -1;
/*     */       int yy_currentPos_l;
/* 407 */       int yy_startRead_l = yy_currentPos_l = this.yy_currentPos = this.yy_startRead = yy_markedPos_l;
/*     */       
/*     */ 
/* 410 */       this.yy_state = this.yy_lexical_state;
/*     */       
/*     */       int yy_input;
/*     */       for (;;)
/*     */       {
/*     */         int yy_input;
/* 416 */         if (yy_currentPos_l < yy_endRead_l) {
/* 417 */           yy_input = yy_buffer_l[(yy_currentPos_l++)];
/* 418 */         } else { if (this.yy_atEOF) {
/* 419 */             int yy_input = -1;
/* 420 */             break;
/*     */           }
/*     */           
/*     */ 
/* 424 */           this.yy_currentPos = yy_currentPos_l;
/* 425 */           this.yy_markedPos = yy_markedPos_l;
/* 426 */           boolean eof = yy_refill();
/*     */           
/* 428 */           yy_currentPos_l = this.yy_currentPos;
/* 429 */           yy_markedPos_l = this.yy_markedPos;
/* 430 */           yy_buffer_l = this.yy_buffer;
/* 431 */           yy_endRead_l = this.yy_endRead;
/* 432 */           if (eof) {
/* 433 */             int yy_input = -1;
/* 434 */             break;
/*     */           }
/*     */           
/* 437 */           yy_input = yy_buffer_l[(yy_currentPos_l++)];
/*     */         }
/*     */         
/* 440 */         int yy_next = yytrans_l[(yy_rowMap_l[this.yy_state] + yycmap_l[yy_input])];
/* 441 */         if (yy_next == -1) break;
/* 442 */         this.yy_state = yy_next;
/*     */         
/* 444 */         int yy_attributes = yy_attr_l[this.yy_state];
/* 445 */         if ((yy_attributes & 0x1) == 1) {
/* 446 */           yy_action = this.yy_state;
/* 447 */           yy_markedPos_l = yy_currentPos_l;
/* 448 */           if ((yy_attributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 455 */       this.yy_markedPos = yy_markedPos_l;
/*     */       
/* 457 */       switch (yy_action)
/*     */       {
/*     */       case 1: 
/* 460 */         return 1;
/*     */       case 4: 
/*     */         break;
/*     */       case 2: case 5: 
/*     */         break;
/*     */       case 3: default: 
/* 466 */         if ((yy_input == -1) && (this.yy_startRead == this.yy_currentPos)) {
/* 467 */           this.yy_atEOF = true;
/* 468 */           return -1;
/*     */         }
/*     */         
/* 471 */         yy_ScanError(2);
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
/* 487 */     if (argv.length == 0) {
/* 488 */       System.out.println("Usage : java JFlexDummyLexer <inputfile>");
/*     */     }
/*     */     else {
/* 491 */       for (int i = 0; i < argv.length; i++) {
/* 492 */         JFlexDummyLexer scanner = null;
/*     */         try {
/* 494 */           scanner = new JFlexDummyLexer(new FileReader(argv[i]));
/* 495 */           while (!scanner.yy_atEOF) scanner.yylex();
/*     */         }
/*     */         catch (FileNotFoundException e) {
/* 498 */           System.out.println("File not found : \"" + argv[i] + "\"");
/*     */         }
/*     */         catch (IOException e) {
/* 501 */           System.out.println("IO error scanning file \"" + argv[i] + "\"");
/* 502 */           System.out.println(e);
/*     */         }
/*     */         catch (Exception e) {
/* 505 */           System.out.println("Unexpected exception:");
/* 506 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\JFlexDummyLexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */