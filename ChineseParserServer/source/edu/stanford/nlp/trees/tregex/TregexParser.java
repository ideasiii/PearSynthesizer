/*     */ package edu.stanford.nlp.trees.tregex;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class TregexParser implements TregexParserConstants
/*     */ {
/*  13 */   private static boolean underNegation = false;
/*     */   
/*     */ 
/*     */   static TregexPattern parse(String s)
/*     */     throws ParseException
/*     */   {
/*  19 */     if (jj_initialized_once) {
/*  20 */       ReInit(new StringReader(s + "\n"));
/*     */     } else {
/*  22 */       new TregexParser(new StringReader(s + "\n"));
/*     */     }
/*  24 */     return Root();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final TregexPattern Root()
/*     */     throws ParseException
/*     */   {
/*  32 */     TregexPattern node = SubNode(Relation.ROOT);
/*  33 */     jj_consume_token(11);
/*  34 */     return node;
/*     */   }
/*     */   
/*     */ 
/*     */   public static final TregexPattern Node(Relation r)
/*     */     throws ParseException
/*     */   {
/*     */     TregexPattern node;
/*  42 */     switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */     case 12: 
/*  44 */       jj_consume_token(12);
/*  45 */       node = SubNode(r);
/*  46 */       jj_consume_token(13);
/*  47 */       break;
/*     */     case 7: 
/*     */     case 8: 
/*     */     case 9: 
/*     */     case 14: 
/*     */     case 15: 
/*     */     case 18: 
/*     */     case 19: 
/*  55 */       node = ModDescription(r);
/*  56 */       break;
/*     */     case 10: case 11: case 13: case 16: case 17: default: 
/*  58 */       jj_la1[0] = jj_gen;
/*  59 */       jj_consume_token(-1);
/*  60 */       throw new ParseException();
/*     */     }
/*  62 */     return node;
/*     */   }
/*     */   
/*     */   public static final DescriptionPattern SubNode(Relation r) throws ParseException
/*     */   {
/*  67 */     DescriptionPattern result = null;
/*  68 */     TregexPattern child = null;
/*  69 */     switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */     case 12: 
/*  71 */       jj_consume_token(12);
/*  72 */       result = SubNode(r);
/*  73 */       jj_consume_token(13);
/*  74 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 4: 
/*     */       case 5: 
/*     */       case 14: 
/*     */       case 22: 
/*     */       case 23: 
/*  80 */         child = ChildrenDisj();
/*  81 */         break;
/*     */       default: 
/*  83 */         jj_la1[1] = jj_gen;
/*     */       }
/*     */       
/*  86 */       if (child != null) {
/*  87 */         List newChildren = new ArrayList();
/*  88 */         newChildren.addAll(result.getChildren());
/*  89 */         newChildren.add(child);
/*  90 */         result.setChild(new CoordinationPattern(newChildren, true));
/*     */       }
/*  92 */       return result;
/*     */     
/*     */     case 7: 
/*     */     case 8: 
/*     */     case 9: 
/*     */     case 14: 
/*     */     case 15: 
/*     */     case 18: 
/*     */     case 19: 
/* 101 */       result = ModDescription(r);
/* 102 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 4: 
/*     */       case 5: 
/*     */       case 14: 
/*     */       case 22: 
/*     */       case 23: 
/* 108 */         child = ChildrenDisj();
/* 109 */         break;
/*     */       default: 
/* 111 */         jj_la1[2] = jj_gen;
/*     */       }
/*     */       
/* 114 */       if (child != null) result.setChild(child);
/* 115 */       return result;
/*     */     }
/*     */     
/* 118 */     jj_la1[3] = jj_gen;
/* 119 */     jj_consume_token(-1);
/* 120 */     throw new ParseException();
/*     */   }
/*     */   
/*     */ 
/*     */   public static final DescriptionPattern ModDescription(Relation r)
/*     */     throws ParseException
/*     */   {
/* 127 */     boolean neg = false;boolean cat = false;
/* 128 */     switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */     case 14: 
/* 130 */       jj_consume_token(14);
/* 131 */       neg = true;
/* 132 */       break;
/*     */     default: 
/* 134 */       jj_la1[4] = jj_gen;
/*     */     }
/*     */     
/* 137 */     switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */     case 15: 
/* 139 */       jj_consume_token(15);
/* 140 */       cat = true;
/* 141 */       break;
/*     */     default: 
/* 143 */       jj_la1[5] = jj_gen;
/*     */     }
/*     */     
/* 146 */     DescriptionPattern node = Description(r, neg, cat);
/* 147 */     return node;
/*     */   }
/*     */   
/*     */   public static final DescriptionPattern Description(Relation r, boolean negateDesc, boolean cat) throws ParseException
/*     */   {
/* 152 */     Token desc = null;
/* 153 */     Token name = null;
/* 154 */     boolean link = false;
/*     */     
/*     */ 
/* 157 */     List varGroups = new ArrayList();
/* 158 */     switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */     case 7: 
/*     */     case 8: 
/*     */     case 9: 
/* 162 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 7: 
/* 164 */         desc = jj_consume_token(7);
/* 165 */         break;
/*     */       case 9: 
/* 167 */         desc = jj_consume_token(9);
/* 168 */         break;
/*     */       case 8: 
/* 170 */         desc = jj_consume_token(8);
/* 171 */         break;
/*     */       default: 
/* 173 */         jj_la1[6] = jj_gen;
/* 174 */         jj_consume_token(-1);
/* 175 */         throw new ParseException();
/*     */       }
/*     */       for (;;)
/*     */       {
/* 179 */         switch (jj_ntk == -1 ? jj_ntk() : jj_ntk)
/*     */         {
/*     */         case 16: 
/*     */           break;
/*     */         default: 
/* 184 */           jj_la1[7] = jj_gen;
/* 185 */           break;
/*     */         }
/* 187 */         jj_consume_token(16);
/* 188 */         Token groupNum = jj_consume_token(6);
/* 189 */         jj_consume_token(17);
/* 190 */         Token groupVar = jj_consume_token(7);
/* 191 */         varGroups.add(new edu.stanford.nlp.util.Pair(Integer.valueOf(Integer.parseInt(groupNum.image)), groupVar.image));
/*     */       }
/* 193 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 18: 
/* 195 */         jj_consume_token(18);
/* 196 */         name = jj_consume_token(7);
/* 197 */         if (underNegation)
/* 198 */           throw new ParseException("No named tregex nodes allowed in the scope of negation.");
/*     */         break;
/*     */       default: 
/* 201 */         jj_la1[8] = jj_gen;
/*     */       }
/*     */       
/* 204 */       break;
/*     */     case 18: 
/*     */     case 19: 
/* 207 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 18: 
/* 209 */         jj_consume_token(18);
/* 210 */         break;
/*     */       case 19: 
/* 212 */         jj_consume_token(19);
/* 213 */         link = true;
/* 214 */         break;
/*     */       default: 
/* 216 */         jj_la1[9] = jj_gen;
/* 217 */         jj_consume_token(-1);
/* 218 */         throw new ParseException();
/*     */       }
/* 220 */       name = jj_consume_token(7);
/* 221 */       break;
/*     */     case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 17: default: 
/* 223 */       jj_la1[10] = jj_gen;
/* 224 */       jj_consume_token(-1);
/* 225 */       throw new ParseException();
/*     */     }
/* 227 */     DescriptionPattern ret = new DescriptionPattern(r, negateDesc, desc != null ? desc.image : null, name != null ? name.image : null, cat, varGroups);
/* 228 */     if (link) ret.makeLink();
/* 229 */     return ret;
/*     */   }
/*     */   
/*     */   public static final TregexPattern ChildrenDisj()
/*     */     throws ParseException
/*     */   {
/* 235 */     List children = new ArrayList();
/* 236 */     TregexPattern child = ChildrenConj();
/* 237 */     children.add(child);
/*     */     for (;;)
/*     */     {
/* 240 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk)
/*     */       {
/*     */       case 20: 
/*     */         break;
/*     */       default: 
/* 245 */         jj_la1[11] = jj_gen;
/* 246 */         break;
/*     */       }
/* 248 */       jj_consume_token(20);
/* 249 */       child = ChildrenConj();
/* 250 */       children.add(child);
/*     */     }
/* 252 */     if (children.size() == 1) {
/* 253 */       return child;
/*     */     }
/* 255 */     return new CoordinationPattern(children, false);
/*     */   }
/*     */   
/*     */   public static final TregexPattern ChildrenConj()
/*     */     throws ParseException
/*     */   {
/* 261 */     List children = new ArrayList();
/* 262 */     TregexPattern child = ModChild();
/* 263 */     children.add(child);
/*     */     for (;;)
/*     */     {
/* 266 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 4: case 5: case 14: case 21: case 22: case 23: 
/*     */         break;
/*     */       case 6: case 7: case 8: 
/*     */       case 9: case 10: 
/*     */       case 11: case 12: 
/*     */       case 13: case 15: 
/*     */       case 16: case 17: 
/*     */       case 18: case 19: 
/*     */       case 20: default: 
/* 276 */         jj_la1[12] = jj_gen;
/* 277 */         break;
/*     */       }
/* 279 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 21: 
/* 281 */         jj_consume_token(21);
/* 282 */         break;
/*     */       default: 
/* 284 */         jj_la1[13] = jj_gen;
/*     */       }
/*     */       
/* 287 */       child = ModChild();
/* 288 */       children.add(child);
/*     */     }
/* 290 */     if (children.size() == 1) {
/* 291 */       return child;
/*     */     }
/* 293 */     return new CoordinationPattern(children, true);
/*     */   }
/*     */   
/*     */   public static final TregexPattern ModChild()
/*     */     throws ParseException
/*     */   {
/*     */     TregexPattern child;
/* 300 */     switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */     case 4: 
/*     */     case 5: 
/*     */     case 23: 
/* 304 */       child = Child();
/* 305 */       break;
/*     */     case 14: 
/* 307 */       jj_consume_token(14);
/* 308 */       boolean startUnderNeg = underNegation;
/* 309 */       underNegation = true;
/* 310 */       child = Child();
/* 311 */       underNegation = startUnderNeg;
/* 312 */       child.negate();
/* 313 */       break;
/*     */     case 22: 
/* 315 */       jj_consume_token(22);
/* 316 */       child = Child();
/* 317 */       child.makeOptional();
/* 318 */       break;
/*     */     default: 
/* 320 */       jj_la1[14] = jj_gen;
/* 321 */       jj_consume_token(-1);
/* 322 */       throw new ParseException();
/*     */     }
/* 324 */     return child;
/*     */   }
/*     */   
/*     */   public static final TregexPattern Child() throws ParseException
/*     */   {
/*     */     TregexPattern child;
/* 330 */     switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */     case 23: 
/* 332 */       jj_consume_token(23);
/* 333 */       child = ChildrenDisj();
/* 334 */       jj_consume_token(24);
/* 335 */       break;
/*     */     case 4: 
/*     */     case 5: 
/* 338 */       child = Relation();
/* 339 */       break;
/*     */     default: 
/* 341 */       jj_la1[15] = jj_gen;
/* 342 */       jj_consume_token(-1);
/* 343 */       throw new ParseException();
/*     */     }
/* 345 */     return child;
/*     */   }
/*     */   
/*     */   public static final TregexPattern Relation() throws ParseException
/*     */   {
/* 350 */     Token strArg = null;Token numArg = null;Token negation = null;
/*     */     
/*     */ 
/*     */     Token t;
/*     */     
/* 355 */     switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */     case 4: 
/* 357 */       t = jj_consume_token(4);
/* 358 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 6: 
/* 360 */         numArg = jj_consume_token(6);
/* 361 */         break;
/*     */       default: 
/* 363 */         jj_la1[16] = jj_gen;
/*     */       }
/*     */       
/* 366 */       break;
/*     */     case 5: 
/* 368 */       t = jj_consume_token(5);
/* 369 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 12: 
/* 371 */         jj_consume_token(12);
/* 372 */         switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */         case 14: 
/* 374 */           negation = jj_consume_token(14);
/* 375 */           break;
/*     */         default: 
/* 377 */           jj_la1[17] = jj_gen;
/*     */         }
/*     */         
/* 380 */         switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */         case 9: 
/* 382 */           strArg = jj_consume_token(9);
/* 383 */           break;
/*     */         case 7: 
/* 385 */           strArg = jj_consume_token(7);
/* 386 */           break;
/*     */         default: 
/* 388 */           jj_la1[18] = jj_gen;
/* 389 */           jj_consume_token(-1);
/* 390 */           throw new ParseException();
/*     */         }
/* 392 */         jj_consume_token(13);
/* 393 */         break;
/*     */       case 9: 
/*     */       case 14: 
/* 396 */         switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */         case 14: 
/* 398 */           negation = jj_consume_token(14);
/* 399 */           break;
/*     */         default: 
/* 401 */           jj_la1[19] = jj_gen;
/*     */         }
/*     */         
/* 404 */         strArg = jj_consume_token(9);
/* 405 */         break;
/*     */       default: 
/* 407 */         jj_la1[20] = jj_gen;
/* 408 */         jj_consume_token(-1);
/* 409 */         throw new ParseException();
/*     */       }
/*     */       break;
/*     */     default: 
/* 413 */       jj_la1[21] = jj_gen;
/* 414 */       jj_consume_token(-1);
/* 415 */       throw new ParseException(); }
/*     */     Relation r;
/* 417 */     Relation r; if (strArg != null) {
/* 418 */       r = Relation.getRelation(t.image, "!" + strArg.image); } else { Relation r;
/* 419 */       if (numArg != null) {
/* 420 */         if (t.image.endsWith("-")) {
/* 421 */           t.image = t.image.substring(0, t.image.length() - 1);
/* 422 */           numArg.image = ("-" + numArg.image);
/*     */         }
/* 424 */         r = Relation.getRelation(t.image, numArg.image);
/*     */       } else {
/* 426 */         r = Relation.getRelation(t.image);
/*     */       } }
/* 428 */     TregexPattern child = Node(r);
/* 429 */     return child;
/*     */   }
/*     */   
/*     */ 
/* 433 */   private static boolean jj_initialized_once = false;
/*     */   public static TregexParserTokenManager token_source;
/*     */   static SimpleCharStream jj_input_stream;
/*     */   public static Token token;
/*     */   public static Token jj_nt;
/*     */   private static int jj_ntk;
/* 439 */   private static int jj_gen; private static final int[] jj_la1 = new int[22];
/*     */   private static int[] jj_la1_0;
/*     */   
/* 442 */   static { jj_la1_0(); }
/*     */   
/*     */   private static void jj_la1_0() {
/* 445 */     jj_la1_0 = new int[] { 840576, 12599344, 12599344, 840576, 16384, 32768, 896, 65536, 262144, 786432, 787328, 1048576, 14696496, 2097152, 12599344, 8388656, 64, 16384, 640, 16384, 20992, 48 };
/*     */   }
/*     */   
/*     */   public TregexParser(InputStream stream) {
/* 449 */     if (jj_initialized_once) {
/* 450 */       System.out.println("ERROR: Second call to constructor of static parser.  You must");
/* 451 */       System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
/* 452 */       System.out.println("       during parser generation.");
/* 453 */       throw new Error();
/*     */     }
/* 455 */     jj_initialized_once = true;
/* 456 */     jj_input_stream = new SimpleCharStream(stream, 1, 1);
/* 457 */     token_source = new TregexParserTokenManager(jj_input_stream);
/* 458 */     token = new Token();
/* 459 */     jj_ntk = -1;
/* 460 */     jj_gen = 0;
/* 461 */     for (int i = 0; i < 22; i++) jj_la1[i] = -1;
/*     */   }
/*     */   
/*     */   public static void ReInit(InputStream stream) {
/* 465 */     jj_input_stream.ReInit(stream, 1, 1);
/* 466 */     TregexParserTokenManager.ReInit(jj_input_stream);
/* 467 */     token = new Token();
/* 468 */     jj_ntk = -1;
/* 469 */     jj_gen = 0;
/* 470 */     for (int i = 0; i < 22; i++) jj_la1[i] = -1;
/*     */   }
/*     */   
/*     */   public TregexParser(Reader stream) {
/* 474 */     if (jj_initialized_once) {
/* 475 */       System.out.println("ERROR: Second call to constructor of static parser.  You must");
/* 476 */       System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
/* 477 */       System.out.println("       during parser generation.");
/* 478 */       throw new Error();
/*     */     }
/* 480 */     jj_initialized_once = true;
/* 481 */     jj_input_stream = new SimpleCharStream(stream, 1, 1);
/* 482 */     token_source = new TregexParserTokenManager(jj_input_stream);
/* 483 */     token = new Token();
/* 484 */     jj_ntk = -1;
/* 485 */     jj_gen = 0;
/* 486 */     for (int i = 0; i < 22; i++) jj_la1[i] = -1;
/*     */   }
/*     */   
/*     */   public static void ReInit(Reader stream) {
/* 490 */     jj_input_stream.ReInit(stream, 1, 1);
/* 491 */     TregexParserTokenManager.ReInit(jj_input_stream);
/* 492 */     token = new Token();
/* 493 */     jj_ntk = -1;
/* 494 */     jj_gen = 0;
/* 495 */     for (int i = 0; i < 22; i++) jj_la1[i] = -1;
/*     */   }
/*     */   
/*     */   public TregexParser(TregexParserTokenManager tm) {
/* 499 */     if (jj_initialized_once) {
/* 500 */       System.out.println("ERROR: Second call to constructor of static parser.  You must");
/* 501 */       System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
/* 502 */       System.out.println("       during parser generation.");
/* 503 */       throw new Error();
/*     */     }
/* 505 */     jj_initialized_once = true;
/* 506 */     token_source = tm;
/* 507 */     token = new Token();
/* 508 */     jj_ntk = -1;
/* 509 */     jj_gen = 0;
/* 510 */     for (int i = 0; i < 22; i++) jj_la1[i] = -1;
/*     */   }
/*     */   
/*     */   public void ReInit(TregexParserTokenManager tm) {
/* 514 */     token_source = tm;
/* 515 */     token = new Token();
/* 516 */     jj_ntk = -1;
/* 517 */     jj_gen = 0;
/* 518 */     for (int i = 0; i < 22; i++) jj_la1[i] = -1;
/*     */   }
/*     */   
/*     */   private static final Token jj_consume_token(int kind) throws ParseException {
/*     */     Token oldToken;
/* 523 */     if ((oldToken = token).next != null) token = token.next; else
/* 524 */       token = token.next = TregexParserTokenManager.getNextToken();
/* 525 */     jj_ntk = -1;
/* 526 */     if (token.kind == kind) {
/* 527 */       jj_gen += 1;
/* 528 */       return token;
/*     */     }
/* 530 */     token = oldToken;
/* 531 */     jj_kind = kind;
/* 532 */     throw generateParseException();
/*     */   }
/*     */   
/*     */   public static final Token getNextToken() {
/* 536 */     if (token.next != null) token = token.next; else
/* 537 */       token = token.next = TregexParserTokenManager.getNextToken();
/* 538 */     jj_ntk = -1;
/* 539 */     jj_gen += 1;
/* 540 */     return token;
/*     */   }
/*     */   
/*     */   public static final Token getToken(int index) {
/* 544 */     Token t = token;
/* 545 */     for (int i = 0; i < index; i++) {
/* 546 */       if (t.next != null) t = t.next; else
/* 547 */         t = t.next = TregexParserTokenManager.getNextToken();
/*     */     }
/* 549 */     return t;
/*     */   }
/*     */   
/*     */   private static final int jj_ntk() {
/* 553 */     if ((jj_nt = token.next) == null) {
/* 554 */       return jj_ntk = (token.next = TregexParserTokenManager.getNextToken()).kind;
/*     */     }
/* 556 */     return jj_ntk = jj_nt.kind;
/*     */   }
/*     */   
/* 559 */   private static Vector jj_expentries = new Vector();
/*     */   private static int[] jj_expentry;
/* 561 */   private static int jj_kind = -1;
/*     */   
/*     */   public static ParseException generateParseException() {
/* 564 */     jj_expentries.removeAllElements();
/* 565 */     boolean[] la1tokens = new boolean[25];
/* 566 */     for (int i = 0; i < 25; i++) {
/* 567 */       la1tokens[i] = false;
/*     */     }
/* 569 */     if (jj_kind >= 0) {
/* 570 */       la1tokens[jj_kind] = true;
/* 571 */       jj_kind = -1;
/*     */     }
/* 573 */     for (int i = 0; i < 22; i++) {
/* 574 */       if (jj_la1[i] == jj_gen) {
/* 575 */         for (int j = 0; j < 32; j++) {
/* 576 */           if ((jj_la1_0[i] & 1 << j) != 0) {
/* 577 */             la1tokens[j] = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 582 */     for (int i = 0; i < 25; i++) {
/* 583 */       if (la1tokens[i] != 0) {
/* 584 */         jj_expentry = new int[1];
/* 585 */         jj_expentry[0] = i;
/* 586 */         jj_expentries.addElement(jj_expentry);
/*     */       }
/*     */     }
/* 589 */     int[][] exptokseq = new int[jj_expentries.size()][];
/* 590 */     for (int i = 0; i < jj_expentries.size(); i++) {
/* 591 */       exptokseq[i] = ((int[])(int[])jj_expentries.elementAt(i));
/*     */     }
/* 593 */     return new ParseException(token, exptokseq, tokenImage);
/*     */   }
/*     */   
/*     */   public static final void enable_tracing() {}
/*     */   
/*     */   public static final void disable_tracing() {}
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\TregexParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */