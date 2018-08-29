/*     */ package edu.stanford.nlp.trees.international.negra;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NegraPennLexer
/*     */   implements Lexer
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int YY_BUFFERSIZE = 16384;
/*     */   public static final int SENTENCE = 1;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String yycmap_packed = "\t\000\001\t\001\013\001\t\001\t\001\n\022\000\001\002\004\f\001\001\002\f\002\b\006\f\n\007\031\f\001\003\007\f\001\000\001\f\004\000\004\f\001\004\b\f\001\005\005\f\001\006\006\f\n\000\001\t!\000\001\f\017\000\001\f\b\000@\fἨ\000\001\t\001\t?\000";
/*  49 */   private static final char[] yycmap = yy_unpack_cmap("\t\000\001\t\001\013\001\t\001\t\001\n\022\000\001\002\004\f\001\001\002\f\002\b\006\f\n\007\031\f\001\003\007\f\001\000\001\f\004\000\004\f\001\004\b\f\001\005\005\f\001\006\006\f\n\000\001\t!\000\001\f\017\000\001\f\b\000@\fἨ\000\001\t\001\t?\000");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private static final int[] yy_rowMap = { 0, 13, 26, 39, 26, 52, 26, 65, 26, 78, 91, 104, 117, 130, 143, 156, 156 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String yy_packed0 = "\001\003\001\004\001\005\005\006\001\007\001\005\001\b\001\005\001\006\r\t\016\000\001\n\001\000\005\006\004\000\001\006\001\000\001\006\001\000\005\006\004\000\001\006\013\000\001\005\002\000\001\006\001\013\005\006\004\000\001\006\003\000\001\f\r\000\001\r\r\000\001\016\r\000\001\017\b\000\001\020\021\000\001\021\005\000";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   private static final int[] yytrans = yy_unpack();
/*     */   
/*     */   private static final int YY_UNKNOWN_ERROR = 0;
/*     */   
/*     */   private static final int YY_ILLEGAL_STATE = 1;
/*     */   
/*     */   private static final int YY_NO_MATCH = 2;
/*     */   
/*     */   private static final int YY_PUSHBACK_2BIG = 3;
/*     */   
/*  74 */   private static final String[] YY_ERROR_MSG = { "Unkown internal scanner error", "Internal error: unknown state", "Error: could not match input", "Error: pushback value was too large" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private static final byte[] YY_ATTRIBUTE = { 0, 0, 9, 1, 9, 1, 9, 1, 9, 1, 0, 0, 0, 0, 0, 0, 1 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Reader yy_reader;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int yy_state;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private int yy_lexical_state = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */   private char[] yy_buffer = new char['䀀'];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int yy_markedPos;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int yy_pushbackPos;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int yy_currentPos;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int yy_startRead;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int yy_endRead;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int yyline;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int yychar;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int yycolumn;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 147 */   private boolean yy_atBOL = true;
/*     */   
/*     */ 
/*     */   private boolean yy_atEOF;
/*     */   
/*     */ 
/*     */ 
/*     */   public void pushBack(int n)
/*     */   {
/* 156 */     yypushback(n);
/*     */   }
/*     */   
/*     */   public int getYYEOF() {
/* 160 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   NegraPennLexer(Reader in)
/*     */   {
/* 171 */     this.yy_reader = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   NegraPennLexer(InputStream in)
/*     */   {
/* 181 */     this(new InputStreamReader(in));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int[] yy_unpack()
/*     */   {
/* 190 */     int[] trans = new int['©'];
/* 191 */     int offset = 0;
/* 192 */     offset = yy_unpack("\001\003\001\004\001\005\005\006\001\007\001\005\001\b\001\005\001\006\r\t\016\000\001\n\001\000\005\006\004\000\001\006\001\000\001\006\001\000\005\006\004\000\001\006\013\000\001\005\002\000\001\006\001\013\005\006\004\000\001\006\003\000\001\f\r\000\001\r\r\000\001\016\r\000\001\017\b\000\001\020\021\000\001\021\005\000", offset, trans);
/* 193 */     return trans;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int yy_unpack(String packed, int offset, int[] trans)
/*     */   {
/* 203 */     int i = 0;
/* 204 */     int j = offset;
/* 205 */     int l = packed.length();
/* 206 */     int count; for (; i < l; 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 212 */         count > 0)
/*     */     {
/* 207 */       count = packed.charAt(i++);
/* 208 */       int value = packed.charAt(i++);
/* 209 */       value--;
/*     */       
/* 211 */       trans[(j++)] = value;
/* 212 */       count--;
/*     */     }
/* 214 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static char[] yy_unpack_cmap(String packed)
/*     */   {
/* 224 */     char[] map = new char[65536];
/* 225 */     int i = 0;
/* 226 */     int j = 0;
/* 227 */     int count; for (; i < 78; 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 232 */         count > 0)
/*     */     {
/* 228 */       count = packed.charAt(i++);
/* 229 */       char value = packed.charAt(i++);
/*     */       
/* 231 */       map[(j++)] = value;
/* 232 */       count--;
/*     */     }
/* 234 */     return map;
/*     */   }
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
/* 247 */     if (this.yy_startRead > 0) {
/* 248 */       System.arraycopy(this.yy_buffer, this.yy_startRead, this.yy_buffer, 0, this.yy_endRead - this.yy_startRead);
/*     */       
/*     */ 
/* 251 */       this.yy_endRead -= this.yy_startRead;
/* 252 */       this.yy_currentPos -= this.yy_startRead;
/* 253 */       this.yy_markedPos -= this.yy_startRead;
/* 254 */       this.yy_pushbackPos -= this.yy_startRead;
/* 255 */       this.yy_startRead = 0;
/*     */     }
/*     */     
/*     */ 
/* 259 */     if (this.yy_currentPos >= this.yy_buffer.length)
/*     */     {
/* 261 */       char[] newBuffer = new char[this.yy_currentPos * 2];
/* 262 */       System.arraycopy(this.yy_buffer, 0, newBuffer, 0, this.yy_buffer.length);
/* 263 */       this.yy_buffer = newBuffer;
/*     */     }
/*     */     
/*     */ 
/* 267 */     int numRead = this.yy_reader.read(this.yy_buffer, this.yy_endRead, this.yy_buffer.length - this.yy_endRead);
/*     */     
/* 269 */     if (numRead < 0) {
/* 270 */       return true;
/*     */     }
/* 272 */     this.yy_endRead += numRead;
/* 273 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yyclose()
/*     */     throws IOException
/*     */   {
/* 282 */     this.yy_atEOF = true;
/* 283 */     this.yy_endRead = this.yy_startRead;
/*     */     
/* 285 */     if (this.yy_reader != null) {
/* 286 */       this.yy_reader.close();
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
/*     */     throws IOException
/*     */   {
/* 302 */     yyclose();
/* 303 */     this.yy_reader = reader;
/* 304 */     this.yy_atBOL = true;
/* 305 */     this.yy_atEOF = false;
/* 306 */     this.yy_endRead = (this.yy_startRead = 0);
/* 307 */     this.yy_currentPos = (this.yy_markedPos = this.yy_pushbackPos = 0);
/* 308 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 309 */     this.yy_lexical_state = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yystate()
/*     */   {
/* 317 */     return this.yy_lexical_state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void yybegin(int newState)
/*     */   {
/* 327 */     this.yy_lexical_state = newState;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String yytext()
/*     */   {
/* 335 */     return new String(this.yy_buffer, this.yy_startRead, this.yy_markedPos - this.yy_startRead);
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
/*     */   public final char yycharat(int pos)
/*     */   {
/* 350 */     return this.yy_buffer[(this.yy_startRead + pos)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int yylength()
/*     */   {
/* 358 */     return this.yy_markedPos - this.yy_startRead;
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
/* 379 */       message = YY_ERROR_MSG[errorCode];
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/* 381 */       message = YY_ERROR_MSG[0];
/*     */     }
/*     */     
/* 384 */     throw new Error(message);
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
/* 397 */     if (number > yylength()) {
/* 398 */       yy_ScanError(3);
/*     */     }
/*     */     
/* 401 */     this.yy_markedPos -= number;
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
/* 420 */     int yy_endRead_l = this.yy_endRead;
/* 421 */     char[] yy_buffer_l = this.yy_buffer;
/* 422 */     char[] yycmap_l = yycmap;
/*     */     
/* 424 */     int[] yytrans_l = yytrans;
/* 425 */     int[] yy_rowMap_l = yy_rowMap;
/* 426 */     byte[] yy_attr_l = YY_ATTRIBUTE;
/*     */     for (;;)
/*     */     {
/* 429 */       int yy_markedPos_l = this.yy_markedPos;
/*     */       
/* 431 */       int yy_action = -1;
/*     */       int yy_currentPos_l;
/* 433 */       int yy_startRead_l = yy_currentPos_l = this.yy_currentPos = this.yy_startRead = yy_markedPos_l;
/*     */       
/* 435 */       this.yy_state = this.yy_lexical_state;
/*     */       
/*     */       int yy_input;
/*     */       for (;;)
/*     */       {
/*     */         int yy_input;
/* 441 */         if (yy_currentPos_l < yy_endRead_l) {
/* 442 */           yy_input = yy_buffer_l[(yy_currentPos_l++)];
/* 443 */         } else { if (this.yy_atEOF) {
/* 444 */             int yy_input = -1;
/* 445 */             break;
/*     */           }
/*     */           
/* 448 */           this.yy_currentPos = yy_currentPos_l;
/* 449 */           this.yy_markedPos = yy_markedPos_l;
/* 450 */           boolean eof = yy_refill();
/*     */           
/* 452 */           yy_currentPos_l = this.yy_currentPos;
/* 453 */           yy_markedPos_l = this.yy_markedPos;
/* 454 */           yy_buffer_l = this.yy_buffer;
/* 455 */           yy_endRead_l = this.yy_endRead;
/* 456 */           if (eof) {
/* 457 */             int yy_input = -1;
/* 458 */             break;
/*     */           }
/* 460 */           yy_input = yy_buffer_l[(yy_currentPos_l++)];
/*     */         }
/*     */         
/* 463 */         int yy_next = yytrans_l[(yy_rowMap_l[this.yy_state] + yycmap_l[yy_input])];
/* 464 */         if (yy_next == -1) {
/*     */           break;
/*     */         }
/* 467 */         this.yy_state = yy_next;
/*     */         
/* 469 */         int yy_attributes = yy_attr_l[this.yy_state];
/* 470 */         if ((yy_attributes & 0x1) == 1) {
/* 471 */           yy_action = this.yy_state;
/* 472 */           yy_markedPos_l = yy_currentPos_l;
/* 473 */           if ((yy_attributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 482 */       this.yy_markedPos = yy_markedPos_l;
/*     */       
/* 484 */       switch (yy_action)
/*     */       {
/*     */ 
/*     */       case 6: 
/* 488 */         return 1;
/*     */       
/*     */       case 18: 
/*     */         break;
/*     */       
/*     */       case 3: 
/*     */       case 5: 
/*     */       case 9: 
/* 496 */         return 1;
/*     */       
/*     */       case 19: 
/*     */         break;
/*     */       
/*     */       case 2: 
/* 502 */         System.err.println("Error: " + yytext());
/* 503 */         return 0;
/*     */       
/*     */       case 20: 
/*     */         break;
/*     */       
/*     */       case 16: 
/* 509 */         return 0;
/*     */       
/*     */       case 21: 
/*     */         break;
/*     */       
/*     */       case 7: 
/* 515 */         return 0;
/*     */       
/*     */       case 22: 
/*     */         break;
/*     */       
/*     */       case 4: 
/* 521 */         return 0;
/*     */       
/*     */       case 23: 
/*     */         break;
/*     */       
/*     */       case 8: 
/* 527 */         System.out.print(yytext());
/*     */       case 24: 
/*     */         break;
/*     */       case 10: case 11: case 12: case 13: 
/*     */       case 14: case 15: case 17: default: 
/* 532 */         if ((yy_input == -1) && (this.yy_startRead == this.yy_currentPos)) {
/* 533 */           this.yy_atEOF = true;
/* 534 */           return -1;
/*     */         }
/* 536 */         yy_ScanError(2);
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
/* 552 */     if (argv.length == 0) {
/* 553 */       System.out.println("Usage : java NegraPennLexer <inputfile>");
/*     */     } else {
/* 555 */       for (int i = 0; i < argv.length; i++) {
/* 556 */         NegraPennLexer scanner = null;
/*     */         try {
/* 558 */           scanner = new NegraPennLexer(new FileReader(argv[i]));
/* 559 */           while (!scanner.yy_atEOF) {
/* 560 */             scanner.yylex();
/*     */           }
/*     */         } catch (FileNotFoundException e) {
/* 563 */           System.out.println("File not found : \"" + argv[i] + "\"");
/*     */         } catch (IOException e) {
/* 565 */           System.out.println("IO error scanning file \"" + argv[i] + "\"");
/* 566 */           System.out.println(e);
/*     */         } catch (Exception e) {
/* 568 */           System.out.println("Unexpected exception:");
/* 569 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\negra\NegraPennLexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */