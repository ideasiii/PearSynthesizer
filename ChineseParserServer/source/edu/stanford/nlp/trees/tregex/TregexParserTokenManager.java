/*     */ package edu.stanford.nlp.trees.tregex;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class TregexParserTokenManager implements TregexParserConstants
/*     */ {
/*   7 */   public static java.io.PrintStream debugStream = System.out;
/*   8 */   public static void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
/*     */   
/*     */   private static final int jjStopStringLiteralDfa_0(int pos, long active0) {
/*  11 */     switch (pos)
/*     */     {
/*     */     case 0: 
/*  14 */       if ((active0 & 0x8) != 0L)
/*  15 */         return 6;
/*  16 */       if ((active0 & 0x40000) != 0L)
/*  17 */         return 1;
/*  18 */       return -1;
/*     */     }
/*  20 */     return -1;
/*     */   }
/*     */   
/*     */   private static final int jjStartNfa_0(int pos, long active0)
/*     */   {
/*  25 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
/*     */   }
/*     */   
/*     */   private static final int jjStopAtPos(int pos, int kind) {
/*  29 */     jjmatchedKind = kind;
/*  30 */     jjmatchedPos = pos;
/*  31 */     return pos + 1;
/*     */   }
/*     */   
/*     */   private static final int jjStartNfaWithStates_0(int pos, int kind, int state) {
/*  35 */     jjmatchedKind = kind;
/*  36 */     jjmatchedPos = pos;
/*  37 */     try { curChar = SimpleCharStream.readChar();
/*  38 */     } catch (IOException e) { return pos + 1; }
/*  39 */     return jjMoveNfa_0(state, pos + 1);
/*     */   }
/*     */   
/*     */   private static final int jjMoveStringLiteralDfa0_0() {
/*  43 */     switch (curChar)
/*     */     {
/*     */     case '\t': 
/*  46 */       return jjStartNfaWithStates_0(0, 3, 6);
/*     */     case '\n': 
/*  48 */       return jjStopAtPos(0, 11);
/*     */     case '!': 
/*  50 */       return jjStopAtPos(0, 14);
/*     */     case '#': 
/*  52 */       return jjStopAtPos(0, 16);
/*     */     case '%': 
/*  54 */       return jjStopAtPos(0, 17);
/*     */     case '&': 
/*  56 */       return jjStopAtPos(0, 21);
/*     */     case '(': 
/*  58 */       return jjStopAtPos(0, 12);
/*     */     case ')': 
/*  60 */       return jjStopAtPos(0, 13);
/*     */     case '=': 
/*  62 */       return jjStartNfaWithStates_0(0, 18, 1);
/*     */     case '?': 
/*  64 */       return jjStopAtPos(0, 22);
/*     */     case '@': 
/*  66 */       return jjStopAtPos(0, 15);
/*     */     case '[': 
/*  68 */       return jjStopAtPos(0, 23);
/*     */     case ']': 
/*  70 */       return jjStopAtPos(0, 24);
/*     */     case '_': 
/*  72 */       return jjMoveStringLiteralDfa1_0(256L);
/*     */     case '|': 
/*  74 */       return jjStopAtPos(0, 20);
/*     */     case '~': 
/*  76 */       return jjStopAtPos(0, 19);
/*     */     }
/*  78 */     return jjMoveNfa_0(0, 0);
/*     */   }
/*     */   
/*     */   private static final int jjMoveStringLiteralDfa1_0(long active0) {
/*     */     try {
/*  83 */       curChar = SimpleCharStream.readChar();
/*     */     } catch (IOException e) {
/*  85 */       jjStopStringLiteralDfa_0(0, active0);
/*  86 */       return 1;
/*     */     }
/*  88 */     switch (curChar)
/*     */     {
/*     */     case '_': 
/*  91 */       if ((active0 & 0x100) != 0L) {
/*  92 */         return jjStopAtPos(1, 8);
/*     */       }
/*     */       break;
/*     */     }
/*     */     
/*  97 */     return jjStartNfa_0(0, active0);
/*     */   }
/*     */   
/*     */   private static final void jjCheckNAdd(int state) {
/* 101 */     if (jjrounds[state] != jjround)
/*     */     {
/* 103 */       jjstateSet[(jjnewStateCnt++)] = state;
/* 104 */       jjrounds[state] = jjround;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final void jjAddStates(int start, int end) {
/*     */     do {
/* 110 */       jjstateSet[(jjnewStateCnt++)] = jjnextStates[start];
/* 111 */     } while (start++ != end);
/*     */   }
/*     */   
/*     */   private static final void jjCheckNAddTwoStates(int state1, int state2) {
/* 115 */     jjCheckNAdd(state1);
/* 116 */     jjCheckNAdd(state2);
/*     */   }
/*     */   
/*     */   private static final void jjCheckNAddStates(int start, int end) {
/*     */     do {
/* 121 */       jjCheckNAdd(jjnextStates[start]);
/* 122 */     } while (start++ != end);
/*     */   }
/*     */   
/*     */   private static final void jjCheckNAddStates(int start) {
/* 126 */     jjCheckNAdd(jjnextStates[start]);
/* 127 */     jjCheckNAdd(jjnextStates[(start + 1)]); }
/*     */   
/* 129 */   static final long[] jjbitVec0 = { -2L, -1L, -1L, -1L };
/*     */   
/*     */ 
/* 132 */   static final long[] jjbitVec2 = { 0L, 0L, -1L, -1L };
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int jjMoveNfa_0(int startState, int curPos)
/*     */   {
/* 138 */     int startsAt = 0;
/* 139 */     jjnewStateCnt = 42;
/* 140 */     int i = 1;
/* 141 */     jjstateSet[0] = startState;
/* 142 */     int kind = Integer.MAX_VALUE;
/*     */     for (;;)
/*     */     {
/* 145 */       if (++jjround == Integer.MAX_VALUE)
/* 146 */         ReInitRounds();
/* 147 */       if (curChar < '@')
/*     */       {
/* 149 */         long l = 1L << curChar;
/*     */         do
/*     */         {
/* 152 */           switch (jjstateSet[(--i)])
/*     */           {
/*     */           case 0: 
/* 155 */             if ((0x8002C84FFFFDBFF & l) != 0L)
/*     */             {
/* 157 */               if (kind > 7)
/* 158 */                 kind = 7;
/* 159 */               jjCheckNAdd(6);
/*     */             }
/* 161 */             else if ((0x3FF000000000000 & l) != 0L)
/*     */             {
/* 163 */               if (kind > 6)
/* 164 */                 kind = 6;
/* 165 */               jjCheckNAdd(4);
/*     */             }
/* 167 */             else if ((0x5400501000000000 & l) != 0L)
/*     */             {
/* 169 */               if (kind > 4) {
/* 170 */                 kind = 4;
/*     */               }
/* 172 */             } else if (curChar == '/') {
/* 173 */               jjCheckNAddStates(0, 2);
/* 174 */             } else if (curChar == '=') {
/* 175 */               jjstateSet[(jjnewStateCnt++)] = 1; }
/* 176 */             if (curChar == '>') {
/* 177 */               jjCheckNAddStates(3, 14);
/* 178 */             } else if (curChar == '<') {
/* 179 */               jjCheckNAddStates(15, 26);
/* 180 */             } else if (curChar == ',') {
/* 181 */               jjCheckNAddTwoStates(20, 23);
/* 182 */             } else if (curChar == '.') {
/* 183 */               jjCheckNAddTwoStates(18, 23);
/* 184 */             } else if (curChar == '$') {
/* 185 */               jjCheckNAddStates(27, 34);
/* 186 */             } else if (curChar == '-')
/* 187 */               jjCheckNAdd(4);
/*     */             break;
/*     */           case 1: 
/* 190 */             if ((curChar == '=') && (kind > 4))
/* 191 */               kind = 4;
/*     */             break;
/*     */           case 2: 
/* 194 */             if (curChar == '=')
/* 195 */               jjstateSet[(jjnewStateCnt++)] = 1;
/*     */             break;
/*     */           case 3: 
/* 198 */             if (curChar == '-')
/* 199 */               jjCheckNAdd(4);
/*     */             break;
/*     */           case 4: 
/* 202 */             if ((0x3FF000000000000 & l) != 0L)
/*     */             {
/* 204 */               if (kind > 6)
/* 205 */                 kind = 6;
/* 206 */               jjCheckNAdd(4); }
/* 207 */             break;
/*     */           case 5: 
/* 209 */             if ((0x8002C84FFFFDBFF & l) != 0L)
/*     */             {
/* 211 */               if (kind > 7)
/* 212 */                 kind = 7;
/* 213 */               jjCheckNAdd(6); }
/* 214 */             break;
/*     */           case 6: 
/* 216 */             if ((0x5FFFADECFFFFDBFF & l) != 0L)
/*     */             {
/* 218 */               if (kind > 7)
/* 219 */                 kind = 7;
/* 220 */               jjCheckNAdd(6); }
/* 221 */             break;
/*     */           case 7: 
/*     */           case 8: 
/* 224 */             if (curChar == '/')
/* 225 */               jjCheckNAddStates(0, 2);
/*     */             break;
/*     */           case 10: 
/* 228 */             if ((0xFFFF7FFFFFFFDBFF & l) != 0L)
/* 229 */               jjCheckNAddStates(0, 2);
/*     */             break;
/*     */           case 11: 
/* 232 */             if ((curChar == '/') && (kind > 9))
/* 233 */               kind = 9;
/*     */             break;
/*     */           case 13: 
/* 236 */             if (curChar == '$')
/* 237 */               jjCheckNAddStates(27, 34);
/*     */             break;
/*     */           case 14: 
/* 240 */             if ((curChar == '+') && (kind > 4))
/* 241 */               kind = 4;
/*     */             break;
/*     */           case 15: 
/* 244 */             if (curChar == '+')
/* 245 */               jjCheckNAdd(14);
/*     */             break;
/*     */           case 16: 
/* 248 */             if ((curChar == '-') && (kind > 4))
/* 249 */               kind = 4;
/*     */             break;
/*     */           case 17: 
/* 252 */             if (curChar == '-')
/* 253 */               jjCheckNAdd(16);
/*     */             break;
/*     */           case 18: 
/* 256 */             if ((curChar == '.') && (kind > 4))
/* 257 */               kind = 4;
/*     */             break;
/*     */           case 19: 
/* 260 */             if (curChar == '.')
/* 261 */               jjCheckNAdd(18);
/*     */             break;
/*     */           case 20: 
/* 264 */             if ((curChar == ',') && (kind > 4))
/* 265 */               kind = 4;
/*     */             break;
/*     */           case 21: 
/* 268 */             if (curChar == ',')
/* 269 */               jjCheckNAdd(20);
/*     */             break;
/*     */           case 22: 
/* 272 */             if (curChar == '.')
/* 273 */               jjCheckNAddTwoStates(18, 23);
/*     */             break;
/*     */           case 23: 
/* 276 */             if ((curChar == '+') && (kind > 5))
/* 277 */               kind = 5;
/*     */             break;
/*     */           case 24: 
/* 280 */             if (curChar == ',')
/* 281 */               jjCheckNAddTwoStates(20, 23);
/*     */             break;
/*     */           case 25: 
/* 284 */             if (curChar == '<')
/* 285 */               jjCheckNAddStates(15, 26);
/*     */             break;
/*     */           case 26: 
/* 288 */             if ((curChar == '<') && (kind > 4))
/* 289 */               kind = 4;
/*     */             break;
/*     */           case 27: 
/* 292 */             if (curChar == '<')
/* 293 */               jjCheckNAdd(20);
/*     */             break;
/*     */           case 28: 
/* 296 */             if (curChar == '<')
/* 297 */               jjCheckNAdd(16);
/*     */             break;
/*     */           case 30: 
/* 300 */             if (curChar == '<')
/* 301 */               jjCheckNAdd(29);
/*     */             break;
/*     */           case 31: 
/* 304 */             if ((curChar == ':') && (kind > 4))
/* 305 */               kind = 4;
/*     */             break;
/*     */           case 32: 
/* 308 */             if (curChar == '<')
/* 309 */               jjCheckNAdd(31);
/*     */             break;
/*     */           case 33: 
/* 312 */             if ((curChar == '#') && (kind > 4))
/* 313 */               kind = 4;
/*     */             break;
/*     */           case 34: 
/* 316 */             if (curChar == '<')
/* 317 */               jjCheckNAdd(33);
/*     */             break;
/*     */           case 35: 
/* 320 */             if (curChar == '>')
/* 321 */               jjCheckNAddStates(3, 14);
/*     */             break;
/*     */           case 36: 
/* 324 */             if ((curChar == '>') && (kind > 4))
/* 325 */               kind = 4;
/*     */             break;
/*     */           case 37: 
/* 328 */             if (curChar == '>')
/* 329 */               jjCheckNAdd(20);
/*     */             break;
/*     */           case 38: 
/* 332 */             if (curChar == '>')
/* 333 */               jjCheckNAdd(16);
/*     */             break;
/*     */           case 39: 
/* 336 */             if (curChar == '>')
/* 337 */               jjCheckNAdd(29);
/*     */             break;
/*     */           case 40: 
/* 340 */             if (curChar == '>')
/* 341 */               jjCheckNAdd(31);
/*     */             break;
/*     */           case 41: 
/* 344 */             if (curChar == '>') {
/* 345 */               jjCheckNAdd(33);
/*     */             }
/*     */             break;
/*     */           }
/* 349 */         } while (i != startsAt);
/*     */       }
/* 351 */       else if (curChar < '')
/*     */       {
/* 353 */         long l = 1L << (curChar & 0x3F);
/*     */         do
/*     */         {
/* 356 */           switch (jjstateSet[(--i)])
/*     */           {
/*     */           case 0: 
/* 359 */             if ((0xAFFFFFFF57FFFFFE & l) != 0L)
/*     */             {
/* 361 */               if (kind > 7)
/* 362 */                 kind = 7;
/* 363 */               jjCheckNAdd(6);
/*     */             }
/* 365 */             if ((0x7FFFFFE07FFFFFE & l) != 0L)
/*     */             {
/* 367 */               if (kind > 10)
/* 368 */                 kind = 10;
/* 369 */               jjCheckNAdd(12);
/*     */             }
/*     */             break;
/*     */           case 5: 
/* 373 */             if ((0xAFFFFFFF57FFFFFE & l) != 0L)
/*     */             {
/* 375 */               if (kind > 7)
/* 376 */                 kind = 7;
/* 377 */               jjCheckNAdd(6); }
/* 378 */             break;
/*     */           case 6: 
/* 380 */             if ((0xFFFFFFFF97FFFFFF & l) != 0L)
/*     */             {
/* 382 */               if (kind > 7)
/* 383 */                 kind = 7;
/* 384 */               jjCheckNAdd(6); }
/* 385 */             break;
/*     */           case 9: 
/* 387 */             if (curChar == '\\')
/* 388 */               jjstateSet[(jjnewStateCnt++)] = 8;
/*     */             break;
/*     */           case 10: 
/* 391 */             jjAddStates(0, 2);
/* 392 */             break;
/*     */           case 12: 
/* 394 */             if ((0x7FFFFFE07FFFFFE & l) != 0L)
/*     */             {
/* 396 */               if (kind > 10)
/* 397 */                 kind = 10;
/* 398 */               jjCheckNAdd(12); }
/* 399 */             break;
/*     */           case 29: 
/* 401 */             if ((curChar == '`') && (kind > 4)) {
/* 402 */               kind = 4;
/*     */             }
/*     */             break;
/*     */           }
/* 406 */         } while (i != startsAt);
/*     */       }
/*     */       else
/*     */       {
/* 410 */         int hiByte = curChar >> '\b';
/* 411 */         int i1 = hiByte >> 6;
/* 412 */         long l1 = 1L << (hiByte & 0x3F);
/* 413 */         int i2 = (curChar & 0xFF) >> '\006';
/* 414 */         long l2 = 1L << (curChar & 0x3F);
/*     */         do
/*     */         {
/* 417 */           switch (jjstateSet[(--i)])
/*     */           {
/*     */           case 0: 
/*     */           case 6: 
/* 421 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2))
/*     */             {
/* 423 */               if (kind > 7)
/* 424 */                 kind = 7;
/* 425 */               jjCheckNAdd(6); }
/* 426 */             break;
/*     */           case 10: 
/* 428 */             if (jjCanMove_0(hiByte, i1, i2, l1, l2)) {
/* 429 */               jjAddStates(0, 2);
/*     */             }
/*     */             break;
/*     */           }
/* 433 */         } while (i != startsAt);
/*     */       }
/* 435 */       if (kind != Integer.MAX_VALUE)
/*     */       {
/* 437 */         jjmatchedKind = kind;
/* 438 */         jjmatchedPos = curPos;
/* 439 */         kind = Integer.MAX_VALUE;
/*     */       }
/* 441 */       curPos++;
/* 442 */       if ((i = jjnewStateCnt) == (startsAt = 42 - (jjnewStateCnt = startsAt)))
/* 443 */         return curPos;
/* 444 */       try { curChar = SimpleCharStream.readChar(); } catch (IOException e) {} }
/* 445 */     return curPos;
/*     */   }
/*     */   
/* 448 */   static final int[] jjnextStates = { 9, 10, 11, 36, 37, 38, 20, 16, 29, 39, 31, 40, 33, 41, 23, 26, 27, 28, 20, 16, 29, 30, 31, 32, 33, 34, 23, 15, 17, 14, 16, 18, 19, 20, 21 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
/*     */   {
/* 455 */     switch (hiByte)
/*     */     {
/*     */     case 0: 
/* 458 */       return (jjbitVec2[i2] & l2) != 0L;
/*     */     }
/* 460 */     if ((jjbitVec0[i1] & l1) != 0L)
/* 461 */       return true;
/* 462 */     return false;
/*     */   }
/*     */   
/* 465 */   public static final String[] jjstrLiteralImages = { "", null, null, null, null, null, null, null, "__", null, null, "\n", "(", ")", "!", "@", "#", "%", "=", "~", "|", "&", "?", "[", "]" };
/*     */   
/*     */ 
/*     */ 
/* 469 */   public static final String[] lexStateNames = { "DEFAULT" };
/*     */   
/*     */ 
/* 472 */   static final long[] jjtoToken = { 33554417L };
/*     */   
/*     */ 
/* 475 */   static final long[] jjtoSkip = { 14L };
/*     */   
/*     */   protected static SimpleCharStream input_stream;
/*     */   
/* 479 */   private static final int[] jjrounds = new int[42];
/* 480 */   private static final int[] jjstateSet = new int[84];
/*     */   protected static char curChar;
/*     */   
/*     */   public TregexParserTokenManager(SimpleCharStream stream) {
/* 484 */     if (input_stream != null)
/* 485 */       throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", 1);
/* 486 */     input_stream = stream;
/*     */   }
/*     */   
/*     */   public TregexParserTokenManager(SimpleCharStream stream, int lexState) {
/* 490 */     this(stream);
/* 491 */     SwitchTo(lexState);
/*     */   }
/*     */   
/*     */   public static void ReInit(SimpleCharStream stream) {
/* 495 */     jjmatchedPos = jjnewStateCnt = 0;
/* 496 */     curLexState = defaultLexState;
/* 497 */     input_stream = stream;
/* 498 */     ReInitRounds();
/*     */   }
/*     */   
/*     */   private static final void ReInitRounds()
/*     */   {
/* 503 */     jjround = -2147483647;
/* 504 */     for (int i = 42; i-- > 0;)
/* 505 */       jjrounds[i] = Integer.MIN_VALUE;
/*     */   }
/*     */   
/*     */   public static void ReInit(SimpleCharStream stream, int lexState) {
/* 509 */     ReInit(stream);
/* 510 */     SwitchTo(lexState);
/*     */   }
/*     */   
/*     */   public static void SwitchTo(int lexState) {
/* 514 */     if ((lexState >= 1) || (lexState < 0)) {
/* 515 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*     */     }
/* 517 */     curLexState = lexState;
/*     */   }
/*     */   
/*     */   protected static Token jjFillToken()
/*     */   {
/* 522 */     Token t = Token.newToken(jjmatchedKind);
/* 523 */     t.kind = jjmatchedKind;
/* 524 */     String im = jjstrLiteralImages[jjmatchedKind];
/* 525 */     t.image = (im == null ? SimpleCharStream.GetImage() : im);
/* 526 */     t.beginLine = SimpleCharStream.getBeginLine();
/* 527 */     t.beginColumn = SimpleCharStream.getBeginColumn();
/* 528 */     t.endLine = SimpleCharStream.getEndLine();
/* 529 */     t.endColumn = SimpleCharStream.getEndColumn();
/* 530 */     return t;
/*     */   }
/*     */   
/* 533 */   static int curLexState = 0;
/* 534 */   static int defaultLexState = 0;
/*     */   
/*     */   static int jjnewStateCnt;
/*     */   static int jjround;
/*     */   static int jjmatchedPos;
/*     */   static int jjmatchedKind;
/*     */   
/*     */   public static Token getNextToken()
/*     */   {
/* 543 */     Token specialToken = null;
/*     */     
/* 545 */     int curPos = 0;
/*     */     do
/*     */     {
/*     */       for (;;)
/*     */       {
/*     */         try
/*     */         {
/* 552 */           curChar = SimpleCharStream.BeginToken();
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/* 556 */           jjmatchedKind = 0;
/* 557 */           return jjFillToken();
/*     */         }
/*     */         try
/*     */         {
/* 561 */           SimpleCharStream.backup(0);
/* 562 */           while ((curChar <= ' ') && ((0x100002000 & 1L << curChar) != 0L))
/* 563 */             curChar = SimpleCharStream.BeginToken();
/*     */         } catch (IOException e1) {}
/*     */       }
/* 566 */       jjmatchedKind = Integer.MAX_VALUE;
/* 567 */       jjmatchedPos = 0;
/* 568 */       curPos = jjMoveStringLiteralDfa0_0();
/* 569 */       if (jjmatchedKind == Integer.MAX_VALUE)
/*     */         break;
/* 571 */       if (jjmatchedPos + 1 < curPos)
/* 572 */         SimpleCharStream.backup(curPos - jjmatchedPos - 1);
/* 573 */     } while ((jjtoToken[(jjmatchedKind >> 6)] & 1L << (jjmatchedKind & 0x3F)) == 0L);
/*     */     
/* 575 */     Token matchedToken = jjFillToken();
/* 576 */     return matchedToken;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 583 */     int error_line = SimpleCharStream.getEndLine();
/* 584 */     int error_column = SimpleCharStream.getEndColumn();
/* 585 */     String error_after = null;
/* 586 */     boolean EOFSeen = false;
/* 587 */     try { SimpleCharStream.readChar();SimpleCharStream.backup(1);
/*     */     } catch (IOException e1) {
/* 589 */       EOFSeen = true;
/* 590 */       error_after = curPos <= 1 ? "" : SimpleCharStream.GetImage();
/* 591 */       if ((curChar == '\n') || (curChar == '\r')) {
/* 592 */         error_line++;
/* 593 */         error_column = 0;
/*     */       }
/*     */       else {
/* 596 */         error_column++;
/*     */       } }
/* 598 */     if (!EOFSeen) {
/* 599 */       SimpleCharStream.backup(1);
/* 600 */       error_after = curPos <= 1 ? "" : SimpleCharStream.GetImage();
/*     */     }
/* 602 */     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, 0);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\TregexParserTokenManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */