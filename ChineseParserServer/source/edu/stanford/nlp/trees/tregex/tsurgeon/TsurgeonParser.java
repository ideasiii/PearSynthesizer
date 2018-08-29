/*     */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class TsurgeonParser implements TsurgeonParserTreeConstants, TsurgeonParserConstants
/*     */ {
/*  10 */   protected static JJTTsurgeonParserState jjtree = new JJTTsurgeonParserState();
/*  11 */   private static edu.stanford.nlp.trees.TreeFactory treeFactory = new edu.stanford.nlp.trees.LabeledScoredTreeFactory();
/*     */   
/*     */   static TsurgeonPattern parse(String s) throws ParseException {
/*  14 */     if (jj_initialized_once) {
/*  15 */       ReInit(new java.io.StringReader(s + "\n"));
/*     */     } else {
/*  17 */       new TsurgeonParser(new java.io.StringReader(s + "\n"));
/*     */     }
/*  19 */     return Root();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*  24 */     System.out.println("Reading from standard input...");
/*  25 */     TsurgeonParser t = new TsurgeonParser(System.in);
/*     */     try {
/*  27 */       TsurgeonPattern n = Root();
/*  28 */       System.out.println(n.toString());
/*  29 */       System.out.println("Thank you.");
/*     */     } catch (Exception e) {
/*  31 */       System.out.println("Oops.");
/*  32 */       System.out.println(e.getMessage());
/*  33 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final TsurgeonPattern Root() throws ParseException
/*     */   {
/*  39 */     SimpleNode jjtn000 = new SimpleNode(0);
/*  40 */     boolean jjtc000 = true;
/*  41 */     jjtree.openNodeScope(jjtn000);
/*     */     try {
/*  43 */       TsurgeonPattern result = Operation();
/*  44 */       jj_consume_token(24);
/*  45 */       jjtree.closeNodeScope(jjtn000, true);
/*  46 */       jjtc000 = false;
/*  47 */       return result;
/*     */     } catch (Throwable jjte000) {
/*  49 */       if (jjtc000) {
/*  50 */         jjtree.clearNodeScope(jjtn000);
/*  51 */         jjtc000 = false;
/*     */       } else {
/*  53 */         jjtree.popNode();
/*     */       }
/*  55 */       if ((jjte000 instanceof RuntimeException)) {
/*  56 */         throw ((RuntimeException)jjte000);
/*     */       }
/*  58 */       if ((jjte000 instanceof ParseException)) {
/*  59 */         throw ((ParseException)jjte000);
/*     */       }
/*  61 */       throw ((Error)jjte000);
/*     */     } finally {
/*  63 */       if (jjtc000) {
/*  64 */         jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final TsurgeonPattern Operation()
/*     */     throws ParseException
/*     */   {
/*  72 */     SimpleNode jjtn000 = new SimpleNode(1);
/*  73 */     boolean jjtc000 = true;
/*  74 */     jjtree.openNodeScope(jjtn000);
/*  75 */     TsurgeonPattern child2 = null;
/*  76 */     Token newLabel = null;
/*  77 */     TreeLocation loc = null;
/*     */     
/*  79 */     AuxiliaryTree tree = null;
/*  80 */     List nodeSelections = null;
/*     */     try { Token operator;
/*     */       Object localObject1;
/*     */       TsurgeonPattern child1;
/*  84 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 4: 
/*  86 */         operator = jj_consume_token(4);
/*  87 */         nodeSelections = NodeSelectionList(new ArrayList());
/*  88 */         jjtree.closeNodeScope(jjtn000, true);
/*  89 */         jjtc000 = false;
/*  90 */         return new DeleteNode(nodeSelections);
/*     */       
/*     */       case 5: 
/*  93 */         operator = jj_consume_token(5);
/*  94 */         nodeSelections = NodeSelectionList(new ArrayList());
/*  95 */         jjtree.closeNodeScope(jjtn000, true);
/*  96 */         jjtc000 = false;
/*  97 */         return new PruneNode(nodeSelections);
/*     */       
/*     */       case 7: 
/* 100 */         operator = jj_consume_token(7);
/* 101 */         child1 = NodeSelection();
/* 102 */         child2 = NodeSelection();
/* 103 */         jjtree.closeNodeScope(jjtn000, true);
/* 104 */         jjtc000 = false;
/* 105 */         return new ExciseNode(child1, child2);
/*     */       }
/*     */       
/* 108 */       jj_la1[0] = jj_gen;
/* 109 */       if (jj_2_1(3)) {
/* 110 */         operator = jj_consume_token(6);
/* 111 */         child1 = NodeSelection();
/* 112 */         newLabel = jj_consume_token(16);
/* 113 */         jjtree.closeNodeScope(jjtn000, true);
/* 114 */         jjtc000 = false;
/* 115 */         return new RelabelNode(child1, newLabel.image); }
/* 116 */       if (jj_2_2(3)) {
/* 117 */         Token operator = jj_consume_token(6);
/* 118 */         TsurgeonPattern child1 = NodeSelection();
/* 119 */         newLabel = jj_consume_token(20);
/* 120 */         jjtree.closeNodeScope(jjtn000, true);
/* 121 */         jjtc000 = false;
/* 122 */         return new RelabelNode(child1, newLabel.image); }
/*     */       Token operator;
/* 124 */       TsurgeonPattern child1; switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 6: 
/* 126 */         operator = jj_consume_token(6);
/* 127 */         child1 = NodeSelection();
/* 128 */         Token regex = jj_consume_token(19);
/* 129 */         Token hash_int = jj_consume_token(21);
/* 130 */         jjtree.closeNodeScope(jjtn000, true);
/* 131 */         jjtc000 = false;
/* 132 */         return new RelabelNode(child1, regex.image, Integer.parseInt(hash_int.image.substring(1)));
/*     */       }
/*     */       
/* 135 */       jj_la1[1] = jj_gen;
/* 136 */       if (jj_2_3(2)) {
/* 137 */         operator = jj_consume_token(10);
/* 138 */         child1 = NodeSelection();
/* 139 */         tree = TreeRoot(false);
/* 140 */         jjtree.closeNodeScope(jjtn000, true);
/* 141 */         jjtc000 = false;
/* 142 */         return new ReplaceNode(child1, tree); }
/*     */       Token operator;
/* 144 */       TsurgeonPattern child1; switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 10: 
/* 146 */         operator = jj_consume_token(10);
/* 147 */         child1 = NodeSelection();
/* 148 */         child2 = NodeSelection();
/* 149 */         jjtree.closeNodeScope(jjtn000, true);
/* 150 */         jjtc000 = false;
/* 151 */         return new ReplaceNode(child1, child2);
/*     */       
/*     */       case 9: 
/* 154 */         operator = jj_consume_token(9);
/* 155 */         child1 = NodeSelection();
/* 156 */         loc = Location();
/* 157 */         jjtree.closeNodeScope(jjtn000, true);
/* 158 */         jjtc000 = false;
/* 159 */         return new MoveNode(child1, loc);
/*     */       }
/*     */       
/* 162 */       jj_la1[2] = jj_gen;
/* 163 */       if (jj_2_4(2)) {
/* 164 */         operator = jj_consume_token(8);
/* 165 */         child1 = NodeSelection();
/* 166 */         loc = Location();
/* 167 */         jjtree.closeNodeScope(jjtn000, true);
/* 168 */         jjtc000 = false;
/* 169 */         return new InsertNode(child1, loc); }
/*     */       Token operator;
/* 171 */       TsurgeonPattern child1; switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 8: 
/* 173 */         operator = jj_consume_token(8);
/* 174 */         tree = TreeRoot(false);
/* 175 */         loc = Location();
/* 176 */         jjtree.closeNodeScope(jjtn000, true);
/* 177 */         jjtc000 = false;
/* 178 */         return new InsertNode(tree, loc);
/*     */       
/*     */       case 11: 
/* 181 */         operator = jj_consume_token(11);
/* 182 */         tree = TreeRoot(true);
/* 183 */         child1 = NodeSelection();
/* 184 */         jjtree.closeNodeScope(jjtn000, true);
/* 185 */         jjtc000 = false;
/* 186 */         return new AdjoinNode(tree, child1);
/*     */       
/*     */       case 12: 
/* 189 */         operator = jj_consume_token(12);
/* 190 */         tree = TreeRoot(true);
/* 191 */         child1 = NodeSelection();
/* 192 */         jjtree.closeNodeScope(jjtn000, true);
/* 193 */         jjtc000 = false;
/* 194 */         return new AdjoinToHeadNode(tree, child1);
/*     */       
/*     */       case 13: 
/* 197 */         operator = jj_consume_token(13);
/* 198 */         tree = TreeRoot(true);
/* 199 */         child1 = NodeSelection();
/* 200 */         jjtree.closeNodeScope(jjtn000, true);
/* 201 */         jjtc000 = false;
/* 202 */         return new AdjoinToFootNode(tree, child1);
/*     */       
/*     */       case 14: 
/* 205 */         operator = jj_consume_token(14);
/* 206 */         nodeSelections = NodeSelectionList(new ArrayList());
/* 207 */         jjtree.closeNodeScope(jjtn000, true);
/* 208 */         jjtc000 = false;
/* 209 */         return new CoindexNodes((TsurgeonPattern[])nodeSelections.toArray(new TsurgeonPattern[0]));
/*     */       }
/*     */       
/* 212 */       jj_la1[3] = jj_gen;
/* 213 */       jj_consume_token(-1);
/* 214 */       throw new ParseException();
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Throwable jjte000)
/*     */     {
/*     */ 
/*     */ 
/* 223 */       if (jjtc000) {
/* 224 */         jjtree.clearNodeScope(jjtn000);
/* 225 */         jjtc000 = false;
/*     */       } else {
/* 227 */         jjtree.popNode();
/*     */       }
/* 229 */       if ((jjte000 instanceof RuntimeException)) {
/* 230 */         throw ((RuntimeException)jjte000);
/*     */       }
/* 232 */       if ((jjte000 instanceof ParseException)) {
/* 233 */         throw ((ParseException)jjte000);
/*     */       }
/* 235 */       throw ((Error)jjte000);
/*     */     } finally {
/* 237 */       if (jjtc000) {
/* 238 */         jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final TreeLocation Location()
/*     */     throws ParseException
/*     */   {
/* 246 */     SimpleNode jjtn000 = new SimpleNode(2);
/* 247 */     boolean jjtc000 = true;
/* 248 */     jjtree.openNodeScope(jjtn000);
/*     */     try
/*     */     {
/* 251 */       Token rel = jj_consume_token(18);
/* 252 */       TsurgeonPattern child = NodeSelection();
/* 253 */       jjtree.closeNodeScope(jjtn000, true);
/* 254 */       jjtc000 = false;
/* 255 */       return new TreeLocation(rel.image, child);
/*     */     } catch (Throwable jjte000) {
/* 257 */       if (jjtc000) {
/* 258 */         jjtree.clearNodeScope(jjtn000);
/* 259 */         jjtc000 = false;
/*     */       } else {
/* 261 */         jjtree.popNode();
/*     */       }
/* 263 */       if ((jjte000 instanceof RuntimeException)) {
/* 264 */         throw ((RuntimeException)jjte000);
/*     */       }
/* 266 */       if ((jjte000 instanceof ParseException)) {
/* 267 */         throw ((ParseException)jjte000);
/*     */       }
/* 269 */       throw ((Error)jjte000);
/*     */     } finally {
/* 271 */       if (jjtc000) {
/* 272 */         jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final List NodeSelectionList(List l)
/*     */     throws ParseException
/*     */   {
/* 280 */     SimpleNode jjtn000 = new SimpleNode(3);
/* 281 */     boolean jjtc000 = true;
/* 282 */     jjtree.openNodeScope(jjtn000);
/*     */     try {
/* 284 */       TsurgeonPattern result = NodeSelection();
/* 285 */       l.add(result);
/*     */       for (;;)
/*     */       {
/* 288 */         switch (jj_ntk == -1 ? jj_ntk() : jj_ntk)
/*     */         {
/*     */         case 16: 
/*     */           break;
/*     */         default: 
/* 293 */           jj_la1[4] = jj_gen;
/* 294 */           break;
/*     */         }
/* 296 */         result = NodeSelection();
/* 297 */         l.add(result);
/*     */       }
/* 299 */       jjtree.closeNodeScope(jjtn000, true);
/* 300 */       jjtc000 = false;
/* 301 */       return l;
/*     */     } catch (Throwable jjte000) {
/* 303 */       if (jjtc000) {
/* 304 */         jjtree.clearNodeScope(jjtn000);
/* 305 */         jjtc000 = false;
/*     */       } else {
/* 307 */         jjtree.popNode();
/*     */       }
/* 309 */       if ((jjte000 instanceof RuntimeException)) {
/* 310 */         throw ((RuntimeException)jjte000);
/*     */       }
/* 312 */       if ((jjte000 instanceof ParseException)) {
/* 313 */         throw ((ParseException)jjte000);
/*     */       }
/* 315 */       throw ((Error)jjte000);
/*     */     } finally {
/* 317 */       if (jjtc000) {
/* 318 */         jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static final TsurgeonPattern NodeSelection()
/*     */     throws ParseException
/*     */   {
/* 327 */     SimpleNode jjtn000 = new SimpleNode(4);
/* 328 */     boolean jjtc000 = true;
/* 329 */     jjtree.openNodeScope(jjtn000);
/*     */     try {
/* 331 */       TsurgeonPattern result = NodeName();
/* 332 */       jjtree.closeNodeScope(jjtn000, true);
/* 333 */       jjtc000 = false;
/* 334 */       return result;
/*     */     } catch (Throwable jjte000) {
/* 336 */       if (jjtc000) {
/* 337 */         jjtree.clearNodeScope(jjtn000);
/* 338 */         jjtc000 = false;
/*     */       } else {
/* 340 */         jjtree.popNode();
/*     */       }
/* 342 */       if ((jjte000 instanceof RuntimeException)) {
/* 343 */         throw ((RuntimeException)jjte000);
/*     */       }
/* 345 */       if ((jjte000 instanceof ParseException)) {
/* 346 */         throw ((ParseException)jjte000);
/*     */       }
/* 348 */       throw ((Error)jjte000);
/*     */     } finally {
/* 350 */       if (jjtc000) {
/* 351 */         jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final TsurgeonPattern NodeName()
/*     */     throws ParseException
/*     */   {
/* 359 */     SimpleNode jjtn000 = new SimpleNode(5);
/* 360 */     boolean jjtc000 = true;
/* 361 */     jjtree.openNodeScope(jjtn000);
/*     */     try {
/* 363 */       Token t = jj_consume_token(16);
/* 364 */       jjtree.closeNodeScope(jjtn000, true);
/* 365 */       jjtc000 = false;
/* 366 */       return new FetchNode(t.image);
/*     */     } finally {
/* 368 */       if (jjtc000) {
/* 369 */         jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static final AuxiliaryTree TreeRoot(boolean requiresFoot)
/*     */     throws ParseException
/*     */   {
/* 378 */     SimpleNode jjtn000 = new SimpleNode(6);
/* 379 */     boolean jjtc000 = true;
/* 380 */     jjtree.openNodeScope(jjtn000);
/*     */     try {
/* 382 */       edu.stanford.nlp.trees.Tree t = TreeNode();
/* 383 */       jjtree.closeNodeScope(jjtn000, true);
/* 384 */       jjtc000 = false;
/* 385 */       return new AuxiliaryTree(t, requiresFoot);
/*     */     } catch (Throwable jjte000) {
/* 387 */       if (jjtc000) {
/* 388 */         jjtree.clearNodeScope(jjtn000);
/* 389 */         jjtc000 = false;
/*     */       } else {
/* 391 */         jjtree.popNode();
/*     */       }
/* 393 */       if ((jjte000 instanceof RuntimeException)) {
/* 394 */         throw ((RuntimeException)jjte000);
/*     */       }
/* 396 */       if ((jjte000 instanceof ParseException)) {
/* 397 */         throw ((ParseException)jjte000);
/*     */       }
/* 399 */       throw ((Error)jjte000);
/*     */     } finally {
/* 401 */       if (jjtc000) {
/* 402 */         jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final edu.stanford.nlp.trees.Tree TreeNode()
/*     */     throws ParseException
/*     */   {
/* 410 */     SimpleNode jjtn000 = new SimpleNode(7);
/* 411 */     boolean jjtc000 = true;
/* 412 */     jjtree.openNodeScope(jjtn000);
/* 413 */     List dtrs = null;
/*     */     try { Token label;
/* 415 */       edu.stanford.nlp.trees.Tree localTree; switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 23: 
/* 417 */         label = jj_consume_token(23);
/* 418 */         dtrs = TreeDtrs(new ArrayList());
/* 419 */         jjtree.closeNodeScope(jjtn000, true);
/* 420 */         jjtc000 = false;
/* 421 */         return treeFactory.newTreeNode(label.image.substring(1), dtrs);
/*     */       
/*     */       case 16: 
/*     */       case 22: 
/* 425 */         switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */         case 22: 
/* 427 */           label = jj_consume_token(22);
/* 428 */           break;
/*     */         case 16: 
/* 430 */           label = jj_consume_token(16);
/* 431 */           break;
/*     */         default: 
/* 433 */           jj_la1[5] = jj_gen;
/* 434 */           jj_consume_token(-1);
/* 435 */           throw new ParseException();
/*     */         }
/* 437 */         jjtree.closeNodeScope(jjtn000, true);
/* 438 */         jjtc000 = false;
/* 439 */         return treeFactory.newTreeNode(label.image, new ArrayList());
/*     */       }
/*     */       
/* 442 */       jj_la1[6] = jj_gen;
/* 443 */       jj_consume_token(-1);
/* 444 */       throw new ParseException();
/*     */     }
/*     */     catch (Throwable jjte000) {
/* 447 */       if (jjtc000) {
/* 448 */         jjtree.clearNodeScope(jjtn000);
/* 449 */         jjtc000 = false;
/*     */       } else {
/* 451 */         jjtree.popNode();
/*     */       }
/* 453 */       if ((jjte000 instanceof RuntimeException)) {
/* 454 */         throw ((RuntimeException)jjte000);
/*     */       }
/* 456 */       if ((jjte000 instanceof ParseException)) {
/* 457 */         throw ((ParseException)jjte000);
/*     */       }
/* 459 */       throw ((Error)jjte000);
/*     */     } finally {
/* 461 */       if (jjtc000) {
/* 462 */         jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static final List TreeDtrs(List dtrs)
/*     */     throws ParseException
/*     */   {
/* 470 */     SimpleNode jjtn000 = new SimpleNode(8);
/* 471 */     boolean jjtc000 = true;
/* 472 */     jjtree.openNodeScope(jjtn000);
/*     */     try { List localList;
/* 474 */       switch (jj_ntk == -1 ? jj_ntk() : jj_ntk) {
/*     */       case 16: 
/*     */       case 22: 
/*     */       case 23: 
/* 478 */         edu.stanford.nlp.trees.Tree tree = TreeNode();
/* 479 */         TreeDtrs(dtrs);
/* 480 */         jjtree.closeNodeScope(jjtn000, true);
/* 481 */         jjtc000 = false;
/* 482 */         dtrs.add(0, tree);return dtrs;
/*     */       
/*     */       case 25: 
/* 485 */         jj_consume_token(25);
/* 486 */         jjtree.closeNodeScope(jjtn000, true);
/* 487 */         jjtc000 = false;
/* 488 */         return dtrs;
/*     */       }
/*     */       
/* 491 */       jj_la1[7] = jj_gen;
/* 492 */       jj_consume_token(-1);
/* 493 */       throw new ParseException();
/*     */     }
/*     */     catch (Throwable jjte000) {
/* 496 */       if (jjtc000) {
/* 497 */         jjtree.clearNodeScope(jjtn000);
/* 498 */         jjtc000 = false;
/*     */       } else {
/* 500 */         jjtree.popNode();
/*     */       }
/* 502 */       if ((jjte000 instanceof RuntimeException)) {
/* 503 */         throw ((RuntimeException)jjte000);
/*     */       }
/* 505 */       if ((jjte000 instanceof ParseException)) {
/* 506 */         throw ((ParseException)jjte000);
/*     */       }
/* 508 */       throw ((Error)jjte000);
/*     */     } finally {
/* 510 */       if (jjtc000) {
/* 511 */         jjtree.closeNodeScope(jjtn000, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final boolean jj_2_1(int xla)
/*     */   {
/* 518 */     jj_la = xla;jj_lastpos = jj_scanpos = token;
/* 519 */     try { return !jj_3_1();
/* 520 */     } catch (LookaheadSuccess ls) { return true;
/* 521 */     } finally { jj_save(0, xla);
/*     */     }
/*     */   }
/*     */   
/* 525 */   private static final boolean jj_2_2(int xla) { jj_la = xla;jj_lastpos = jj_scanpos = token;
/* 526 */     try { return !jj_3_2();
/* 527 */     } catch (LookaheadSuccess ls) { return true;
/* 528 */     } finally { jj_save(1, xla);
/*     */     }
/*     */   }
/*     */   
/* 532 */   private static final boolean jj_2_3(int xla) { jj_la = xla;jj_lastpos = jj_scanpos = token;
/* 533 */     try { return !jj_3_3();
/* 534 */     } catch (LookaheadSuccess ls) { return true;
/* 535 */     } finally { jj_save(2, xla);
/*     */     }
/*     */   }
/*     */   
/* 539 */   private static final boolean jj_2_4(int xla) { jj_la = xla;jj_lastpos = jj_scanpos = token;
/* 540 */     try { return !jj_3_4();
/* 541 */     } catch (LookaheadSuccess ls) { return true;
/* 542 */     } finally { jj_save(3, xla);
/*     */     }
/*     */   }
/*     */   
/* 546 */   private static final boolean jj_3R_2() { if (jj_3R_3()) return true;
/* 547 */     return false;
/*     */   }
/*     */   
/*     */   private static final boolean jj_3R_3() {
/* 551 */     if (jj_scan_token(16)) return true;
/* 552 */     return false;
/*     */   }
/*     */   
/*     */   private static final boolean jj_3_1() {
/* 556 */     if (jj_scan_token(6)) return true;
/* 557 */     if (jj_3R_2()) return true;
/* 558 */     if (jj_scan_token(16)) return true;
/* 559 */     return false;
/*     */   }
/*     */   
/*     */   private static final boolean jj_3_3() {
/* 563 */     if (jj_scan_token(10)) return true;
/* 564 */     if (jj_3R_2()) return true;
/* 565 */     return false;
/*     */   }
/*     */   
/*     */   private static final boolean jj_3_4() {
/* 569 */     if (jj_scan_token(8)) return true;
/* 570 */     if (jj_3R_2()) return true;
/* 571 */     return false;
/*     */   }
/*     */   
/*     */   private static final boolean jj_3_2() {
/* 575 */     if (jj_scan_token(6)) return true;
/* 576 */     if (jj_3R_2()) return true;
/* 577 */     if (jj_scan_token(20)) return true;
/* 578 */     return false;
/*     */   }
/*     */   
/* 581 */   private static boolean jj_initialized_once = false;
/*     */   public static TsurgeonParserTokenManager token_source;
/*     */   static SimpleCharStream jj_input_stream;
/*     */   public static Token token;
/*     */   public static Token jj_nt;
/*     */   private static int jj_ntk;
/*     */   private static Token jj_scanpos;
/* 588 */   private static Token jj_lastpos; private static int jj_la; public static boolean lookingAhead = false;
/*     */   private static boolean jj_semLA;
/*     */   private static int jj_gen;
/* 591 */   private static final int[] jj_la1 = new int[8];
/*     */   private static int[] jj_la1_0;
/*     */   
/* 594 */   static { jj_la1_0(); }
/*     */   
/*     */ 
/* 597 */   private static void jj_la1_0() { jj_la1_0 = new int[] { 176, 64, 1536, 30976, 65536, 4259840, 12648448, 46202880 }; }
/*     */   
/* 599 */   private static final JJCalls[] jj_2_rtns = new JJCalls[4];
/* 600 */   private static boolean jj_rescan = false;
/* 601 */   private static int jj_gc = 0;
/*     */   
/*     */   public TsurgeonParser(java.io.InputStream stream) {
/* 604 */     if (jj_initialized_once) {
/* 605 */       System.out.println("ERROR: Second call to constructor of static parser.  You must");
/* 606 */       System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
/* 607 */       System.out.println("       during parser generation.");
/* 608 */       throw new Error();
/*     */     }
/* 610 */     jj_initialized_once = true;
/* 611 */     jj_input_stream = new SimpleCharStream(stream, 1, 1);
/* 612 */     token_source = new TsurgeonParserTokenManager(jj_input_stream);
/* 613 */     token = new Token();
/* 614 */     jj_ntk = -1;
/* 615 */     jj_gen = 0;
/* 616 */     for (int i = 0; i < 8; i++) jj_la1[i] = -1;
/* 617 */     for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
/*     */   }
/*     */   
/*     */   public static void ReInit(java.io.InputStream stream) {
/* 621 */     jj_input_stream.ReInit(stream, 1, 1);
/* 622 */     TsurgeonParserTokenManager.ReInit(jj_input_stream);
/* 623 */     token = new Token();
/* 624 */     jj_ntk = -1;
/* 625 */     jjtree.reset();
/* 626 */     jj_gen = 0;
/* 627 */     for (int i = 0; i < 8; i++) jj_la1[i] = -1;
/* 628 */     for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
/*     */   }
/*     */   
/*     */   public TsurgeonParser(java.io.Reader stream) {
/* 632 */     if (jj_initialized_once) {
/* 633 */       System.out.println("ERROR: Second call to constructor of static parser.  You must");
/* 634 */       System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
/* 635 */       System.out.println("       during parser generation.");
/* 636 */       throw new Error();
/*     */     }
/* 638 */     jj_initialized_once = true;
/* 639 */     jj_input_stream = new SimpleCharStream(stream, 1, 1);
/* 640 */     token_source = new TsurgeonParserTokenManager(jj_input_stream);
/* 641 */     token = new Token();
/* 642 */     jj_ntk = -1;
/* 643 */     jj_gen = 0;
/* 644 */     for (int i = 0; i < 8; i++) jj_la1[i] = -1;
/* 645 */     for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
/*     */   }
/*     */   
/*     */   public static void ReInit(java.io.Reader stream) {
/* 649 */     jj_input_stream.ReInit(stream, 1, 1);
/* 650 */     TsurgeonParserTokenManager.ReInit(jj_input_stream);
/* 651 */     token = new Token();
/* 652 */     jj_ntk = -1;
/* 653 */     jjtree.reset();
/* 654 */     jj_gen = 0;
/* 655 */     for (int i = 0; i < 8; i++) jj_la1[i] = -1;
/* 656 */     for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
/*     */   }
/*     */   
/*     */   public TsurgeonParser(TsurgeonParserTokenManager tm) {
/* 660 */     if (jj_initialized_once) {
/* 661 */       System.out.println("ERROR: Second call to constructor of static parser.  You must");
/* 662 */       System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
/* 663 */       System.out.println("       during parser generation.");
/* 664 */       throw new Error();
/*     */     }
/* 666 */     jj_initialized_once = true;
/* 667 */     token_source = tm;
/* 668 */     token = new Token();
/* 669 */     jj_ntk = -1;
/* 670 */     jj_gen = 0;
/* 671 */     for (int i = 0; i < 8; i++) jj_la1[i] = -1;
/* 672 */     for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
/*     */   }
/*     */   
/*     */   public void ReInit(TsurgeonParserTokenManager tm) {
/* 676 */     token_source = tm;
/* 677 */     token = new Token();
/* 678 */     jj_ntk = -1;
/* 679 */     jjtree.reset();
/* 680 */     jj_gen = 0;
/* 681 */     for (int i = 0; i < 8; i++) jj_la1[i] = -1;
/* 682 */     for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
/*     */   }
/*     */   
/*     */   private static final Token jj_consume_token(int kind) throws ParseException {
/*     */     Token oldToken;
/* 687 */     if ((oldToken = token).next != null) token = token.next; else
/* 688 */       token = token.next = TsurgeonParserTokenManager.getNextToken();
/* 689 */     jj_ntk = -1;
/* 690 */     if (token.kind == kind) {
/* 691 */       jj_gen += 1;
/* 692 */       if (++jj_gc > 100) {
/* 693 */         jj_gc = 0;
/* 694 */         for (int i = 0; i < jj_2_rtns.length; i++) {
/* 695 */           JJCalls c = jj_2_rtns[i];
/* 696 */           while (c != null) {
/* 697 */             if (c.gen < jj_gen) c.first = null;
/* 698 */             c = c.next;
/*     */           }
/*     */         }
/*     */       }
/* 702 */       return token;
/*     */     }
/* 704 */     token = oldToken;
/* 705 */     jj_kind = kind;
/* 706 */     throw generateParseException();
/*     */   }
/*     */   
/*     */ 
/* 710 */   private static final LookaheadSuccess jj_ls = new LookaheadSuccess(null);
/*     */   
/* 712 */   private static final boolean jj_scan_token(int kind) { if (jj_scanpos == jj_lastpos) {
/* 713 */       jj_la -= 1;
/* 714 */       if (jj_scanpos.next == null) {
/* 715 */         jj_lastpos = jj_scanpos = jj_scanpos.next = TsurgeonParserTokenManager.getNextToken();
/*     */       } else {
/* 717 */         jj_lastpos = jj_scanpos = jj_scanpos.next;
/*     */       }
/*     */     } else {
/* 720 */       jj_scanpos = jj_scanpos.next;
/*     */     }
/* 722 */     if (jj_rescan) {
/* 723 */       int i = 0; for (Token tok = token; 
/* 724 */           (tok != null) && (tok != jj_scanpos); tok = tok.next) i++;
/* 725 */       if (tok != null) jj_add_error_token(kind, i);
/*     */     }
/* 727 */     if (jj_scanpos.kind != kind) return true;
/* 728 */     if ((jj_la == 0) && (jj_scanpos == jj_lastpos)) throw jj_ls;
/* 729 */     return false;
/*     */   }
/*     */   
/*     */   public static final Token getNextToken() {
/* 733 */     if (token.next != null) token = token.next; else
/* 734 */       token = token.next = TsurgeonParserTokenManager.getNextToken();
/* 735 */     jj_ntk = -1;
/* 736 */     jj_gen += 1;
/* 737 */     return token;
/*     */   }
/*     */   
/*     */   public static final Token getToken(int index) {
/* 741 */     Token t = lookingAhead ? jj_scanpos : token;
/* 742 */     for (int i = 0; i < index; i++) {
/* 743 */       if (t.next != null) t = t.next; else
/* 744 */         t = t.next = TsurgeonParserTokenManager.getNextToken();
/*     */     }
/* 746 */     return t;
/*     */   }
/*     */   
/*     */   private static final int jj_ntk() {
/* 750 */     if ((jj_nt = token.next) == null) {
/* 751 */       return jj_ntk = (token.next = TsurgeonParserTokenManager.getNextToken()).kind;
/*     */     }
/* 753 */     return jj_ntk = jj_nt.kind;
/*     */   }
/*     */   
/* 756 */   private static Vector jj_expentries = new Vector();
/*     */   private static int[] jj_expentry;
/* 758 */   private static int jj_kind = -1;
/* 759 */   private static int[] jj_lasttokens = new int[100];
/*     */   private static int jj_endpos;
/*     */   
/*     */   private static void jj_add_error_token(int kind, int pos) {
/* 763 */     if (pos >= 100) return;
/* 764 */     if (pos == jj_endpos + 1) {
/* 765 */       jj_lasttokens[(jj_endpos++)] = kind;
/* 766 */     } else if (jj_endpos != 0) {
/* 767 */       jj_expentry = new int[jj_endpos];
/* 768 */       for (int i = 0; i < jj_endpos; i++) {
/* 769 */         jj_expentry[i] = jj_lasttokens[i];
/*     */       }
/* 771 */       boolean exists = false;
/* 772 */       for (java.util.Enumeration e = jj_expentries.elements(); e.hasMoreElements();) {
/* 773 */         int[] oldentry = (int[])e.nextElement();
/* 774 */         if (oldentry.length == jj_expentry.length) {
/* 775 */           exists = true;
/* 776 */           for (int i = 0; i < jj_expentry.length; i++) {
/* 777 */             if (oldentry[i] != jj_expentry[i]) {
/* 778 */               exists = false;
/* 779 */               break;
/*     */             }
/*     */           }
/* 782 */           if (exists) break;
/*     */         }
/*     */       }
/* 785 */       if (!exists) jj_expentries.addElement(jj_expentry);
/* 786 */       if (pos != 0) jj_lasttokens[((jj_endpos = pos) - 1)] = kind;
/*     */     }
/*     */   }
/*     */   
/*     */   public static ParseException generateParseException() {
/* 791 */     jj_expentries.removeAllElements();
/* 792 */     boolean[] la1tokens = new boolean[26];
/* 793 */     for (int i = 0; i < 26; i++) {
/* 794 */       la1tokens[i] = false;
/*     */     }
/* 796 */     if (jj_kind >= 0) {
/* 797 */       la1tokens[jj_kind] = true;
/* 798 */       jj_kind = -1;
/*     */     }
/* 800 */     for (int i = 0; i < 8; i++) {
/* 801 */       if (jj_la1[i] == jj_gen) {
/* 802 */         for (int j = 0; j < 32; j++) {
/* 803 */           if ((jj_la1_0[i] & 1 << j) != 0) {
/* 804 */             la1tokens[j] = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 809 */     for (int i = 0; i < 26; i++) {
/* 810 */       if (la1tokens[i] != 0) {
/* 811 */         jj_expentry = new int[1];
/* 812 */         jj_expentry[0] = i;
/* 813 */         jj_expentries.addElement(jj_expentry);
/*     */       }
/*     */     }
/* 816 */     jj_endpos = 0;
/* 817 */     jj_rescan_token();
/* 818 */     jj_add_error_token(0, 0);
/* 819 */     int[][] exptokseq = new int[jj_expentries.size()][];
/* 820 */     for (int i = 0; i < jj_expentries.size(); i++) {
/* 821 */       exptokseq[i] = ((int[])(int[])jj_expentries.elementAt(i));
/*     */     }
/* 823 */     return new ParseException(token, exptokseq, tokenImage);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final void jj_rescan_token()
/*     */   {
/* 833 */     jj_rescan = true;
/* 834 */     for (int i = 0; i < 4; i++) {
/* 835 */       JJCalls p = jj_2_rtns[i];
/*     */       do {
/* 837 */         if (p.gen > jj_gen) {
/* 838 */           jj_la = p.arg;jj_lastpos = jj_scanpos = p.first;
/* 839 */           switch (i) {
/* 840 */           case 0:  jj_3_1(); break;
/* 841 */           case 1:  jj_3_2(); break;
/* 842 */           case 2:  jj_3_3(); break;
/* 843 */           case 3:  jj_3_4();
/*     */           }
/*     */         }
/* 846 */         p = p.next;
/* 847 */       } while (p != null);
/*     */     }
/* 849 */     jj_rescan = false;
/*     */   }
/*     */   
/*     */   private static final void jj_save(int index, int xla) {
/* 853 */     JJCalls p = jj_2_rtns[index];
/* 854 */     while (p.gen > jj_gen) {
/* 855 */       if (p.next == null) { p = p.next = new JJCalls(); break; }
/* 856 */       p = p.next;
/*     */     }
/* 858 */     p.gen = (jj_gen + xla - jj_la);p.first = token;p.arg = xla;
/*     */   }
/*     */   
/*     */   public static final void enable_tracing() {}
/*     */   
/*     */   public static final void disable_tracing() {}
/*     */   
/*     */   static final class JJCalls
/*     */   {
/*     */     int gen;
/*     */     Token first;
/*     */     int arg;
/*     */     JJCalls next;
/*     */   }
/*     */   
/*     */   private static final class LookaheadSuccess
/*     */     extends Error
/*     */   {}
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\TsurgeonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */