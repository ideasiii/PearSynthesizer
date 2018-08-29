/*     */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class TsurgeonParserTokenManager implements TsurgeonParserConstants {
/*   6 */   public static java.io.PrintStream debugStream = System.out;
/*   7 */   public static void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
/*     */   
/*     */   private static final int jjStopStringLiteralDfa_0(int pos, long active0) {
/*  10 */     switch (pos)
/*     */     {
/*     */     case 0: 
/*  13 */       if ((active0 & 0x7FF0) != 0L)
/*     */       {
/*  15 */         jjmatchedKind = 16;
/*  16 */         return 42;
/*     */       }
/*  18 */       return -1;
/*     */     case 1: 
/*  20 */       if ((active0 & 0x7FF0) != 0L)
/*     */       {
/*  22 */         jjmatchedKind = 16;
/*  23 */         jjmatchedPos = 1;
/*  24 */         return 42;
/*     */       }
/*  26 */       return -1;
/*     */     case 2: 
/*  28 */       if ((active0 & 0x7FF0) != 0L)
/*     */       {
/*  30 */         jjmatchedKind = 16;
/*  31 */         jjmatchedPos = 2;
/*  32 */         return 42;
/*     */       }
/*  34 */       return -1;
/*     */     case 3: 
/*  36 */       if ((active0 & 0x7DF0) != 0L)
/*     */       {
/*  38 */         jjmatchedKind = 16;
/*  39 */         jjmatchedPos = 3;
/*  40 */         return 42;
/*     */       }
/*  42 */       if ((active0 & 0x200) != 0L)
/*  43 */         return 42;
/*  44 */       return -1;
/*     */     case 4: 
/*  46 */       if ((active0 & 0x7DD0) != 0L)
/*     */       {
/*  48 */         jjmatchedKind = 16;
/*  49 */         jjmatchedPos = 4;
/*  50 */         return 42;
/*     */       }
/*  52 */       if ((active0 & 0x20) != 0L)
/*  53 */         return 42;
/*  54 */       return -1;
/*     */     case 5: 
/*  56 */       if ((active0 & 0x4440) != 0L)
/*     */       {
/*  58 */         if (jjmatchedPos != 5)
/*     */         {
/*  60 */           jjmatchedKind = 16;
/*  61 */           jjmatchedPos = 5;
/*     */         }
/*  63 */         return 42;
/*     */       }
/*  65 */       if ((active0 & 0x3990) != 0L)
/*  66 */         return 42;
/*  67 */       return -1;
/*     */     }
/*  69 */     return -1;
/*     */   }
/*     */   
/*     */   private static final int jjStartNfa_0(int pos, long active0)
/*     */   {
/*  74 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
/*     */   }
/*     */   
/*     */   private static final int jjStopAtPos(int pos, int kind) {
/*  78 */     jjmatchedKind = kind;
/*  79 */     jjmatchedPos = pos;
/*  80 */     return pos + 1;
/*     */   }
/*     */   
/*     */   private static final int jjStartNfaWithStates_0(int pos, int kind, int state) {
/*  84 */     jjmatchedKind = kind;
/*  85 */     jjmatchedPos = pos;
/*  86 */     try { curChar = SimpleCharStream.readChar();
/*  87 */     } catch (IOException e) { return pos + 1; }
/*  88 */     return jjMoveNfa_0(state, pos + 1);
/*     */   }
/*     */   
/*     */   private static final int jjMoveStringLiteralDfa0_0() {
/*  92 */     switch (curChar)
/*     */     {
/*     */     case '\n': 
/*  95 */       return jjStopAtPos(0, 24);
/*     */     case ')': 
/*  97 */       return jjStopAtPos(0, 25);
/*     */     case 'a': 
/*  99 */       return jjMoveStringLiteralDfa1_0(14336L);
/*     */     case 'c': 
/* 101 */       return jjMoveStringLiteralDfa1_0(16384L);
/*     */     case 'd': 
/* 103 */       return jjMoveStringLiteralDfa1_0(16L);
/*     */     case 'e': 
/* 105 */       return jjMoveStringLiteralDfa1_0(128L);
/*     */     case 'i': 
/* 107 */       return jjMoveStringLiteralDfa1_0(256L);
/*     */     case 'm': 
/* 109 */       return jjMoveStringLiteralDfa1_0(512L);
/*     */     case 'p': 
/* 111 */       return jjMoveStringLiteralDfa1_0(32L);
/*     */     case 'r': 
/* 113 */       return jjMoveStringLiteralDfa1_0(1088L);
/*     */     }
/* 115 */     return jjMoveNfa_0(0, 0);
/*     */   }
/*     */   
/*     */   private static final int jjMoveStringLiteralDfa1_0(long active0) {
/*     */     try {
/* 120 */       curChar = SimpleCharStream.readChar();
/*     */     } catch (IOException e) {
/* 122 */       jjStopStringLiteralDfa_0(0, active0);
/* 123 */       return 1;
/*     */     }
/* 125 */     switch (curChar)
/*     */     {
/*     */     case 'd': 
/* 128 */       return jjMoveStringLiteralDfa2_0(active0, 14336L);
/*     */     case 'e': 
/* 130 */       return jjMoveStringLiteralDfa2_0(active0, 1104L);
/*     */     case 'n': 
/* 132 */       return jjMoveStringLiteralDfa2_0(active0, 256L);
/*     */     case 'o': 
/* 134 */       return jjMoveStringLiteralDfa2_0(active0, 16896L);
/*     */     case 'r': 
/* 136 */       return jjMoveStringLiteralDfa2_0(active0, 32L);
/*     */     case 'x': 
/* 138 */       return jjMoveStringLiteralDfa2_0(active0, 128L);
/*     */     }
/*     */     
/*     */     
/* 142 */     return jjStartNfa_0(0, active0);
/*     */   }
/*     */   
/*     */   private static final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
/* 146 */     if ((active0 &= old0) == 0L)
/* 147 */       return jjStartNfa_0(0, old0);
/* 148 */     try { curChar = SimpleCharStream.readChar();
/*     */     } catch (IOException e) {
/* 150 */       jjStopStringLiteralDfa_0(1, active0);
/* 151 */       return 2;
/*     */     }
/* 153 */     switch (curChar)
/*     */     {
/*     */     case 'c': 
/* 156 */       return jjMoveStringLiteralDfa3_0(active0, 128L);
/*     */     case 'i': 
/* 158 */       return jjMoveStringLiteralDfa3_0(active0, 16384L);
/*     */     case 'j': 
/* 160 */       return jjMoveStringLiteralDfa3_0(active0, 14336L);
/*     */     case 'l': 
/* 162 */       return jjMoveStringLiteralDfa3_0(active0, 80L);
/*     */     case 'p': 
/* 164 */       return jjMoveStringLiteralDfa3_0(active0, 1024L);
/*     */     case 's': 
/* 166 */       return jjMoveStringLiteralDfa3_0(active0, 256L);
/*     */     case 'u': 
/* 168 */       return jjMoveStringLiteralDfa3_0(active0, 32L);
/*     */     case 'v': 
/* 170 */       return jjMoveStringLiteralDfa3_0(active0, 512L);
/*     */     }
/*     */     
/*     */     
/* 174 */     return jjStartNfa_0(1, active0);
/*     */   }
/*     */   
/*     */   private static final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
/* 178 */     if ((active0 &= old0) == 0L)
/* 179 */       return jjStartNfa_0(1, old0);
/* 180 */     try { curChar = SimpleCharStream.readChar();
/*     */     } catch (IOException e) {
/* 182 */       jjStopStringLiteralDfa_0(2, active0);
/* 183 */       return 3;
/*     */     }
/* 185 */     switch (curChar)
/*     */     {
/*     */     case 'a': 
/* 188 */       return jjMoveStringLiteralDfa4_0(active0, 64L);
/*     */     case 'e': 
/* 190 */       if ((active0 & 0x200) != 0L)
/* 191 */         return jjStartNfaWithStates_0(3, 9, 42);
/* 192 */       return jjMoveStringLiteralDfa4_0(active0, 272L);
/*     */     case 'i': 
/* 194 */       return jjMoveStringLiteralDfa4_0(active0, 128L);
/*     */     case 'l': 
/* 196 */       return jjMoveStringLiteralDfa4_0(active0, 1024L);
/*     */     case 'n': 
/* 198 */       return jjMoveStringLiteralDfa4_0(active0, 16416L);
/*     */     case 'o': 
/* 200 */       return jjMoveStringLiteralDfa4_0(active0, 14336L);
/*     */     }
/*     */     
/*     */     
/* 204 */     return jjStartNfa_0(2, active0);
/*     */   }
/*     */   
/*     */   private static final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
/* 208 */     if ((active0 &= old0) == 0L)
/* 209 */       return jjStartNfa_0(2, old0);
/* 210 */     try { curChar = SimpleCharStream.readChar();
/*     */     } catch (IOException e) {
/* 212 */       jjStopStringLiteralDfa_0(3, active0);
/* 213 */       return 4;
/*     */     }
/* 215 */     switch (curChar)
/*     */     {
/*     */     case 'a': 
/* 218 */       return jjMoveStringLiteralDfa5_0(active0, 1024L);
/*     */     case 'b': 
/* 220 */       return jjMoveStringLiteralDfa5_0(active0, 64L);
/*     */     case 'd': 
/* 222 */       return jjMoveStringLiteralDfa5_0(active0, 16384L);
/*     */     case 'e': 
/* 224 */       if ((active0 & 0x20) != 0L)
/* 225 */         return jjStartNfaWithStates_0(4, 5, 42);
/*     */       break;
/*     */     case 'i': 
/* 228 */       return jjMoveStringLiteralDfa5_0(active0, 14336L);
/*     */     case 'r': 
/* 230 */       return jjMoveStringLiteralDfa5_0(active0, 256L);
/*     */     case 's': 
/* 232 */       return jjMoveStringLiteralDfa5_0(active0, 128L);
/*     */     case 't': 
/* 234 */       return jjMoveStringLiteralDfa5_0(active0, 16L);
/*     */     }
/*     */     
/*     */     
/* 238 */     return jjStartNfa_0(3, active0);
/*     */   }
/*     */   
/*     */   private static final int jjMoveStringLiteralDfa5_0(long old0, long active0) {
/* 242 */     if ((active0 &= old0) == 0L)
/* 243 */       return jjStartNfa_0(3, old0);
/* 244 */     try { curChar = SimpleCharStream.readChar();
/*     */     } catch (IOException e) {
/* 246 */       jjStopStringLiteralDfa_0(4, active0);
/* 247 */       return 5;
/*     */     }
/* 249 */     switch (curChar)
/*     */     {
/*     */     case 'c': 
/* 252 */       return jjMoveStringLiteralDfa6_0(active0, 1024L);
/*     */     case 'e': 
/* 254 */       if ((active0 & 0x10) != 0L)
/* 255 */         return jjStartNfaWithStates_0(5, 4, 42);
/* 256 */       if ((active0 & 0x80) != 0L)
/* 257 */         return jjStartNfaWithStates_0(5, 7, 42);
/* 258 */       return jjMoveStringLiteralDfa6_0(active0, 16448L);
/*     */     case 'n': 
/* 260 */       if ((active0 & 0x800) != 0L)
/*     */       {
/* 262 */         jjmatchedKind = 11;
/* 263 */         jjmatchedPos = 5;
/*     */       }
/* 265 */       return jjMoveStringLiteralDfa6_0(active0, 12288L);
/*     */     case 't': 
/* 267 */       if ((active0 & 0x100) != 0L) {
/* 268 */         return jjStartNfaWithStates_0(5, 8, 42);
/*     */       }
/*     */       break;
/*     */     }
/*     */     
/* 273 */     return jjStartNfa_0(4, active0);
/*     */   }
/*     */   
/*     */   private static final int jjMoveStringLiteralDfa6_0(long old0, long active0) {
/* 277 */     if ((active0 &= old0) == 0L)
/* 278 */       return jjStartNfa_0(4, old0);
/* 279 */     try { curChar = SimpleCharStream.readChar();
/*     */     } catch (IOException e) {
/* 281 */       jjStopStringLiteralDfa_0(5, active0);
/* 282 */       return 6;
/*     */     }
/* 284 */     switch (curChar)
/*     */     {
/*     */     case 'F': 
/* 287 */       if ((active0 & 0x2000) != 0L)
/* 288 */         return jjStartNfaWithStates_0(6, 13, 42);
/*     */       break;
/*     */     case 'H': 
/* 291 */       if ((active0 & 0x1000) != 0L)
/* 292 */         return jjStartNfaWithStates_0(6, 12, 42);
/*     */       break;
/*     */     case 'e': 
/* 295 */       if ((active0 & 0x400) != 0L)
/* 296 */         return jjStartNfaWithStates_0(6, 10, 42);
/*     */       break;
/*     */     case 'l': 
/* 299 */       if ((active0 & 0x40) != 0L)
/* 300 */         return jjStartNfaWithStates_0(6, 6, 42);
/*     */       break;
/*     */     case 'x': 
/* 303 */       if ((active0 & 0x4000) != 0L) {
/* 304 */         return jjStartNfaWithStates_0(6, 14, 42);
/*     */       }
/*     */       break;
/*     */     }
/*     */     
/* 309 */     return jjStartNfa_0(5, active0);
/*     */   }
/*     */   
/*     */   private static final void jjCheckNAdd(int state) {
/* 313 */     if (jjrounds[state] != jjround)
/*     */     {
/* 315 */       jjstateSet[(jjnewStateCnt++)] = state;
/* 316 */       jjrounds[state] = jjround;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final void jjAddStates(int start, int end) {
/*     */     do {
/* 322 */       jjstateSet[(jjnewStateCnt++)] = jjnextStates[start];
/* 323 */     } while (start++ != end);
/*     */   }
/*     */   
/*     */   private static final void jjCheckNAddTwoStates(int state1, int state2) {
/* 327 */     jjCheckNAdd(state1);
/* 328 */     jjCheckNAdd(state2);
/*     */   }
/*     */   
/*     */   private static final void jjCheckNAddStates(int start, int end) {
/*     */     do {
/* 333 */       jjCheckNAdd(jjnextStates[start]);
/* 334 */     } while (start++ != end);
/*     */   }
/*     */   
/*     */   private static final void jjCheckNAddStates(int start) {
/* 338 */     jjCheckNAdd(jjnextStates[start]);
/* 339 */     jjCheckNAdd(jjnextStates[(start + 1)]); }
/*     */   
/* 341 */   static final long[] jjbitVec0 = { 0L, 0L, -1L, -1L };
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int jjMoveNfa_0(int startState, int curPos)
/*     */   {
/* 347 */     int startsAt = 0;
/* 348 */     jjnewStateCnt = 42;
/* 349 */     int i = 1;
/* 350 */     jjstateSet[0] = startState;
/* 351 */     int kind = Integer.MAX_VALUE;
/*     */     for (;;)
/*     */     {
/* 354 */       if (++jjround == Integer.MAX_VALUE)
/* 355 */         ReInitRounds();
/* 356 */       if (curChar < '@')
/*     */       {
/* 358 */         long l = 1L << curChar;
/*     */         do
/*     */         {
/* 361 */           switch (jjstateSet[(--i)])
/*     */           {
/*     */           case 42: 
/* 364 */             if ((0xF3FFFCFA00000000 & l) != 0L)
/*     */             {
/* 366 */               if (kind > 22)
/* 367 */                 kind = 22;
/* 368 */               jjCheckNAdd(19);
/*     */             }
/* 370 */             if ((0x23FF240000000000 & l) != 0L)
/*     */             {
/* 372 */               if (kind > 16)
/* 373 */                 kind = 16;
/* 374 */               jjCheckNAdd(1);
/*     */             }
/*     */             break;
/*     */           case 0: 
/* 378 */             if ((0xF3FFFCFA00000000 & l) != 0L)
/*     */             {
/* 380 */               if (kind > 22)
/* 381 */                 kind = 22;
/* 382 */               jjCheckNAdd(19);
/*     */             }
/* 384 */             else if (curChar == '(')
/*     */             {
/* 386 */               if (kind > 23)
/* 387 */                 kind = 23;
/* 388 */               jjCheckNAdd(21);
/*     */             }
/* 390 */             if ((0x240000000000 & l) != 0L)
/*     */             {
/* 392 */               if (kind > 16)
/* 393 */                 kind = 16;
/* 394 */               jjCheckNAdd(1);
/*     */             }
/* 396 */             else if (curChar == '#') {
/* 397 */               jjAddStates(0, 1);
/* 398 */             } else if (curChar == '$') {
/* 399 */               jjAddStates(2, 3);
/* 400 */             } else if (curChar == '/') {
/* 401 */               jjCheckNAddStates(4, 6);
/* 402 */             } else if (curChar == '>') {
/* 403 */               jjCheckNAddTwoStates(5, 6); }
/* 404 */             if (curChar == '#')
/* 405 */               jjCheckNAdd(18);
/*     */             break;
/*     */           case 1: 
/* 408 */             if ((0x23FF240000000000 & l) != 0L)
/*     */             {
/* 410 */               if (kind > 16)
/* 411 */                 kind = 16;
/* 412 */               jjCheckNAdd(1); }
/* 413 */             break;
/*     */           case 3: 
/* 415 */             if ((0x3FF200000000000 & l) != 0L)
/*     */             {
/* 417 */               if (kind > 17)
/* 418 */                 kind = 17;
/* 419 */               jjstateSet[(jjnewStateCnt++)] = 3; }
/* 420 */             break;
/*     */           case 4: 
/* 422 */             if (curChar == '>')
/* 423 */               jjCheckNAddTwoStates(5, 6);
/*     */             break;
/*     */           case 5: 
/* 426 */             if (curChar == '-')
/* 427 */               jjCheckNAdd(6);
/*     */             break;
/*     */           case 6: 
/* 430 */             if ((0x3FF000000000000 & l) != 0L)
/*     */             {
/* 432 */               if (kind > 18)
/* 433 */                 kind = 18;
/* 434 */               jjCheckNAdd(6); }
/* 435 */             break;
/*     */           case 7: 
/*     */           case 8: 
/* 438 */             if (curChar == '/')
/* 439 */               jjCheckNAddStates(4, 6);
/*     */             break;
/*     */           case 10: 
/* 442 */             if ((0xFFFF7FFFFFFFDBFF & l) != 0L)
/* 443 */               jjCheckNAddStates(4, 6);
/*     */             break;
/*     */           case 11: 
/* 446 */             if ((curChar == '/') && (kind > 19))
/* 447 */               kind = 19;
/*     */             break;
/*     */           case 15: 
/* 450 */             if ((0xFFFFFFFFFFFFDBFF & l) != 0L)
/* 451 */               jjAddStates(7, 9);
/*     */             break;
/*     */           case 17: 
/* 454 */             if (curChar == '#')
/* 455 */               jjCheckNAdd(18);
/*     */             break;
/*     */           case 18: 
/* 458 */             if ((0x3FF000000000000 & l) != 0L)
/*     */             {
/* 460 */               if (kind > 21)
/* 461 */                 kind = 21;
/* 462 */               jjCheckNAdd(18); }
/* 463 */             break;
/*     */           case 19: 
/* 465 */             if ((0xF3FFFCFA00000000 & l) != 0L)
/*     */             {
/* 467 */               if (kind > 22)
/* 468 */                 kind = 22;
/* 469 */               jjCheckNAdd(19); }
/* 470 */             break;
/*     */           case 20: 
/* 472 */             if (curChar == '(')
/*     */             {
/* 474 */               if (kind > 23)
/* 475 */                 kind = 23;
/* 476 */               jjCheckNAdd(21); }
/* 477 */             break;
/*     */           case 21: 
/* 479 */             if ((0xF3FFFCFA00000000 & l) != 0L)
/*     */             {
/* 481 */               if (kind > 23)
/* 482 */                 kind = 23;
/* 483 */               jjCheckNAdd(21); }
/* 484 */             break;
/*     */           case 22: 
/* 486 */             if (curChar == '$')
/* 487 */               jjAddStates(2, 3);
/*     */             break;
/*     */           case 23: 
/* 490 */             if ((curChar == '-') && (kind > 18))
/* 491 */               kind = 18;
/*     */             break;
/*     */           case 24: 
/* 494 */             if ((curChar == '+') && (kind > 18))
/* 495 */               kind = 18;
/*     */             break;
/*     */           case 25: 
/* 498 */             if (curChar == '#') {
/* 499 */               jjAddStates(0, 1);
/*     */             }
/*     */             break;
/*     */           }
/* 503 */         } while (i != startsAt);
/*     */       }
/* 505 */       else if (curChar < '')
/*     */       {
/* 507 */         long l = 1L << (curChar & 0x3F);
/*     */         do
/*     */         {
/* 510 */           switch (jjstateSet[(--i)])
/*     */           {
/*     */           case 42: 
/* 513 */             if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
/*     */             {
/* 515 */               if (kind > 22)
/* 516 */                 kind = 22;
/* 517 */               jjCheckNAdd(19);
/*     */             }
/* 519 */             if ((0x17FFFFFE87FFFFFF & l) != 0L)
/*     */             {
/* 521 */               if (kind > 16)
/* 522 */                 kind = 16;
/* 523 */               jjCheckNAdd(1);
/*     */             }
/*     */             break;
/*     */           case 0: 
/* 527 */             if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
/*     */             {
/* 529 */               if (kind > 22)
/* 530 */                 kind = 22;
/* 531 */               jjCheckNAdd(19);
/*     */             }
/* 533 */             if ((0x7FFFFFE07FFFFFE & l) != 0L)
/*     */             {
/* 535 */               if (kind > 16)
/* 536 */                 kind = 16;
/* 537 */               jjCheckNAdd(1);
/*     */             }
/* 539 */             else if (curChar == '|') {
/* 540 */               jjCheckNAddStates(7, 9); }
/* 541 */             if ((0x7FFFFFE & l) != 0L)
/*     */             {
/* 543 */               if (kind > 17)
/* 544 */                 kind = 17;
/* 545 */               jjCheckNAdd(3);
/*     */             }
/*     */             break;
/*     */           case 1: 
/* 549 */             if ((0x17FFFFFE87FFFFFF & l) != 0L)
/*     */             {
/* 551 */               if (kind > 16)
/* 552 */                 kind = 16;
/* 553 */               jjCheckNAdd(1); }
/* 554 */             break;
/*     */           case 2: 
/* 556 */             if ((0x7FFFFFE & l) != 0L)
/*     */             {
/* 558 */               if (kind > 17)
/* 559 */                 kind = 17;
/* 560 */               jjCheckNAdd(3); }
/* 561 */             break;
/*     */           case 3: 
/* 563 */             if ((0x87FFFFFE & l) != 0L)
/*     */             {
/* 565 */               if (kind > 17)
/* 566 */                 kind = 17;
/* 567 */               jjCheckNAdd(3); }
/* 568 */             break;
/*     */           case 9: 
/* 570 */             if (curChar == '\\')
/* 571 */               jjstateSet[(jjnewStateCnt++)] = 8;
/*     */             break;
/*     */           case 10: 
/* 574 */             jjAddStates(4, 6);
/* 575 */             break;
/*     */           case 12: 
/*     */           case 13: 
/* 578 */             if (curChar == '|')
/* 579 */               jjCheckNAddStates(7, 9);
/*     */             break;
/*     */           case 14: 
/* 582 */             if (curChar == '\\')
/* 583 */               jjstateSet[(jjnewStateCnt++)] = 13;
/*     */             break;
/*     */           case 15: 
/* 586 */             if ((0xEFFFFFFFFFFFFFFF & l) != 0L)
/* 587 */               jjCheckNAddStates(7, 9);
/*     */             break;
/*     */           case 16: 
/* 590 */             if ((curChar == '|') && (kind > 20))
/* 591 */               kind = 20;
/*     */             break;
/*     */           case 19: 
/* 594 */             if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
/*     */             {
/* 596 */               if (kind > 22)
/* 597 */                 kind = 22;
/* 598 */               jjCheckNAdd(19); }
/* 599 */             break;
/*     */           case 21: 
/* 601 */             if ((0x7FFFFFFFFFFFFFFF & l) != 0L)
/*     */             {
/* 603 */               if (kind > 23)
/* 604 */                 kind = 23;
/* 605 */               jjstateSet[(jjnewStateCnt++)] = 21; }
/* 606 */             break;
/*     */           case 26: 
/* 608 */             if ((curChar == 't') && (kind > 15)) {
/* 609 */               kind = 15;
/*     */             }
/*     */             break;
/*     */           case 27: case 35: 
/* 613 */             if (curChar == 's')
/* 614 */               jjCheckNAdd(26);
/*     */             break;
/*     */           case 28: 
/* 617 */             if (curChar == 'o')
/* 618 */               jjstateSet[(jjnewStateCnt++)] = 27;
/*     */             break;
/*     */           case 29: 
/* 621 */             if (curChar == 'm')
/* 622 */               jjstateSet[(jjnewStateCnt++)] = 28;
/*     */             break;
/*     */           case 30: 
/* 625 */             if (curChar == 't')
/* 626 */               jjstateSet[(jjnewStateCnt++)] = 29;
/*     */             break;
/*     */           case 31: 
/* 629 */             if (curChar == 'h')
/* 630 */               jjstateSet[(jjnewStateCnt++)] = 30;
/*     */             break;
/*     */           case 32: 
/* 633 */             if (curChar == 'g')
/* 634 */               jjstateSet[(jjnewStateCnt++)] = 31;
/*     */             break;
/*     */           case 33: 
/* 637 */             if (curChar == 'i')
/* 638 */               jjstateSet[(jjnewStateCnt++)] = 32;
/*     */             break;
/*     */           case 34: 
/* 641 */             if (curChar == 'r')
/* 642 */               jjstateSet[(jjnewStateCnt++)] = 33;
/*     */             break;
/*     */           case 36: 
/* 645 */             if (curChar == 'o')
/* 646 */               jjstateSet[(jjnewStateCnt++)] = 35;
/*     */             break;
/*     */           case 37: 
/* 649 */             if (curChar == 'm')
/* 650 */               jjstateSet[(jjnewStateCnt++)] = 36;
/*     */             break;
/*     */           case 38: 
/* 653 */             if (curChar == 't')
/* 654 */               jjstateSet[(jjnewStateCnt++)] = 37;
/*     */             break;
/*     */           case 39: 
/* 657 */             if (curChar == 'f')
/* 658 */               jjstateSet[(jjnewStateCnt++)] = 38;
/*     */             break;
/*     */           case 40: 
/* 661 */             if (curChar == 'e')
/* 662 */               jjstateSet[(jjnewStateCnt++)] = 39;
/*     */             break;
/*     */           case 41: 
/* 665 */             if (curChar == 'l') {
/* 666 */               jjstateSet[(jjnewStateCnt++)] = 40;
/*     */             }
/*     */             break;
/*     */           }
/* 670 */         } while (i != startsAt);
/*     */       }
/*     */       else
/*     */       {
/* 674 */         int i2 = (curChar & 0xFF) >> '\006';
/* 675 */         long l2 = 1L << (curChar & 0x3F);
/*     */         do
/*     */         {
/* 678 */           switch (jjstateSet[(--i)])
/*     */           {
/*     */           case 10: 
/* 681 */             if ((jjbitVec0[i2] & l2) != 0L)
/* 682 */               jjAddStates(4, 6);
/*     */             break;
/*     */           case 15: 
/* 685 */             if ((jjbitVec0[i2] & l2) != 0L) {
/* 686 */               jjAddStates(7, 9);
/*     */             }
/*     */             break;
/*     */           }
/* 690 */         } while (i != startsAt);
/*     */       }
/* 692 */       if (kind != Integer.MAX_VALUE)
/*     */       {
/* 694 */         jjmatchedKind = kind;
/* 695 */         jjmatchedPos = curPos;
/* 696 */         kind = Integer.MAX_VALUE;
/*     */       }
/* 698 */       curPos++;
/* 699 */       if ((i = jjnewStateCnt) == (startsAt = 42 - (jjnewStateCnt = startsAt)))
/* 700 */         return curPos;
/* 701 */       try { curChar = SimpleCharStream.readChar(); } catch (IOException e) {} }
/* 702 */     return curPos;
/*     */   }
/*     */   
/* 705 */   static final int[] jjnextStates = { 34, 41, 23, 24, 9, 10, 11, 14, 15, 16 };
/*     */   
/*     */ 
/* 708 */   public static final String[] jjstrLiteralImages = { "", null, null, null, "delete", "prune", "relabel", "excise", "insert", "move", "replace", "adjoin", "adjoinH", "adjoinF", "coindex", null, null, null, null, null, null, null, null, null, "\n", ")" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 714 */   public static final String[] lexStateNames = { "DEFAULT" };
/*     */   
/*     */ 
/* 717 */   static final long[] jjtoToken = { 67108849L };
/*     */   
/*     */ 
/* 720 */   static final long[] jjtoSkip = { 14L };
/*     */   
/*     */   protected static SimpleCharStream input_stream;
/*     */   
/* 724 */   private static final int[] jjrounds = new int[42];
/* 725 */   private static final int[] jjstateSet = new int[84];
/*     */   protected static char curChar;
/*     */   
/*     */   public TsurgeonParserTokenManager(SimpleCharStream stream) {
/* 729 */     if (input_stream != null)
/* 730 */       throw new TokenMgrError("ERROR: Second call to constructor of static lexer. You must use ReInit() to initialize the static variables.", 1);
/* 731 */     input_stream = stream;
/*     */   }
/*     */   
/*     */   public TsurgeonParserTokenManager(SimpleCharStream stream, int lexState) {
/* 735 */     this(stream);
/* 736 */     SwitchTo(lexState);
/*     */   }
/*     */   
/*     */   public static void ReInit(SimpleCharStream stream) {
/* 740 */     jjmatchedPos = jjnewStateCnt = 0;
/* 741 */     curLexState = defaultLexState;
/* 742 */     input_stream = stream;
/* 743 */     ReInitRounds();
/*     */   }
/*     */   
/*     */   private static final void ReInitRounds()
/*     */   {
/* 748 */     jjround = -2147483647;
/* 749 */     for (int i = 42; i-- > 0;)
/* 750 */       jjrounds[i] = Integer.MIN_VALUE;
/*     */   }
/*     */   
/*     */   public static void ReInit(SimpleCharStream stream, int lexState) {
/* 754 */     ReInit(stream);
/* 755 */     SwitchTo(lexState);
/*     */   }
/*     */   
/*     */   public static void SwitchTo(int lexState) {
/* 759 */     if ((lexState >= 1) || (lexState < 0)) {
/* 760 */       throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*     */     }
/* 762 */     curLexState = lexState;
/*     */   }
/*     */   
/*     */   protected static Token jjFillToken()
/*     */   {
/* 767 */     Token t = Token.newToken(jjmatchedKind);
/* 768 */     t.kind = jjmatchedKind;
/* 769 */     String im = jjstrLiteralImages[jjmatchedKind];
/* 770 */     t.image = (im == null ? SimpleCharStream.GetImage() : im);
/* 771 */     t.beginLine = SimpleCharStream.getBeginLine();
/* 772 */     t.beginColumn = SimpleCharStream.getBeginColumn();
/* 773 */     t.endLine = SimpleCharStream.getEndLine();
/* 774 */     t.endColumn = SimpleCharStream.getEndColumn();
/* 775 */     return t;
/*     */   }
/*     */   
/* 778 */   static int curLexState = 0;
/* 779 */   static int defaultLexState = 0;
/*     */   
/*     */   static int jjnewStateCnt;
/*     */   static int jjround;
/*     */   static int jjmatchedPos;
/*     */   static int jjmatchedKind;
/*     */   
/*     */   public static Token getNextToken()
/*     */   {
/* 788 */     Token specialToken = null;
/*     */     
/* 790 */     int curPos = 0;
/*     */     do
/*     */     {
/*     */       for (;;)
/*     */       {
/*     */         try
/*     */         {
/* 797 */           curChar = SimpleCharStream.BeginToken();
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/* 801 */           jjmatchedKind = 0;
/* 802 */           return jjFillToken();
/*     */         }
/*     */         try
/*     */         {
/* 806 */           SimpleCharStream.backup(0);
/* 807 */           while ((curChar <= ' ') && ((0x100002200 & 1L << curChar) != 0L))
/* 808 */             curChar = SimpleCharStream.BeginToken();
/*     */         } catch (IOException e1) {}
/*     */       }
/* 811 */       jjmatchedKind = Integer.MAX_VALUE;
/* 812 */       jjmatchedPos = 0;
/* 813 */       curPos = jjMoveStringLiteralDfa0_0();
/* 814 */       if (jjmatchedKind == Integer.MAX_VALUE)
/*     */         break;
/* 816 */       if (jjmatchedPos + 1 < curPos)
/* 817 */         SimpleCharStream.backup(curPos - jjmatchedPos - 1);
/* 818 */     } while ((jjtoToken[(jjmatchedKind >> 6)] & 1L << (jjmatchedKind & 0x3F)) == 0L);
/*     */     
/* 820 */     Token matchedToken = jjFillToken();
/* 821 */     return matchedToken;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 828 */     int error_line = SimpleCharStream.getEndLine();
/* 829 */     int error_column = SimpleCharStream.getEndColumn();
/* 830 */     String error_after = null;
/* 831 */     boolean EOFSeen = false;
/* 832 */     try { SimpleCharStream.readChar();SimpleCharStream.backup(1);
/*     */     } catch (IOException e1) {
/* 834 */       EOFSeen = true;
/* 835 */       error_after = curPos <= 1 ? "" : SimpleCharStream.GetImage();
/* 836 */       if ((curChar == '\n') || (curChar == '\r')) {
/* 837 */         error_line++;
/* 838 */         error_column = 0;
/*     */       }
/*     */       else {
/* 841 */         error_column++;
/*     */       } }
/* 843 */     if (!EOFSeen) {
/* 844 */       SimpleCharStream.backup(1);
/* 845 */       error_after = curPos <= 1 ? "" : SimpleCharStream.GetImage();
/*     */     }
/* 847 */     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, 0);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\TsurgeonParserTokenManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */