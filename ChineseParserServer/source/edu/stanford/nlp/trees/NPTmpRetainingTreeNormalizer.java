/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.LabelFactory;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import java.util.Collections;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class NPTmpRetainingTreeNormalizer
/*     */   extends BobChrisTreeNormalizer
/*     */ {
/*     */   public static final int TEMPORAL_NONE = 0;
/*     */   public static final int TEMPORAL_ACL03PCFG = 1;
/*     */   public static final int TEMPORAL_ANY_TMP_PERCOLATED = 2;
/*     */   public static final int TEMPORAL_ALL_TERMINALS = 3;
/*     */   public static final int TEMPORAL_ALL_NP = 4;
/*     */   public static final int TEMPORAL_ALL_NP_AND_PP = 5;
/*     */   public static final int TEMPORAL_NP_AND_PP_WITH_NP_HEAD = 6;
/*     */   public static final int TEMPORAL_ALL_NP_EVEN_UNDER_PP = 7;
/*     */   public static final int TEMPORAL_ALL_NP_PP_ADVP = 8;
/*     */   public static final int TEMPORAL_9 = 9;
/*  46 */   private static final Pattern NPTmpPattern = Pattern.compile("NP.*-TMP.*");
/*  47 */   private static final Pattern PPTmpPattern = Pattern.compile("PP.*-TMP.*");
/*  48 */   private static final Pattern ADVPTmpPattern = Pattern.compile("ADVP.*-TMP.*");
/*  49 */   private static final Pattern TmpPattern = Pattern.compile(".*-TMP.*");
/*  50 */   private static final Pattern NPSbjPattern = Pattern.compile("NP.*-SBJ.*");
/*  51 */   private static final Pattern NPAdvPattern = Pattern.compile("NP.*-ADV.*");
/*     */   
/*     */   private final int temporalAnnotation;
/*     */   private final boolean doSGappedStuff;
/*     */   private final int leaveItAll;
/*     */   private final boolean doAdverbialNP;
/*     */   private final HeadFinder headFinder;
/*     */   
/*     */   public NPTmpRetainingTreeNormalizer()
/*     */   {
/*  61 */     this(1, false);
/*     */   }
/*     */   
/*     */   public NPTmpRetainingTreeNormalizer(int temporalAnnotation, boolean doSGappedStuff) {
/*  65 */     this(temporalAnnotation, doSGappedStuff, 0, false);
/*     */   }
/*     */   
/*     */   public NPTmpRetainingTreeNormalizer(int temporalAnnotation, boolean doSGappedStuff, int leaveItAll, boolean doAdverbialNP) {
/*  69 */     this(temporalAnnotation, doSGappedStuff, leaveItAll, doAdverbialNP, new ModCollinsHeadFinder());
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
/*     */ 
/*     */   public NPTmpRetainingTreeNormalizer(int temporalAnnotation, boolean doSGappedStuff, int leaveItAll, boolean doAdverbialNP, HeadFinder headFinder)
/*     */   {
/* 118 */     this.temporalAnnotation = temporalAnnotation;
/* 119 */     this.doSGappedStuff = doSGappedStuff;
/* 120 */     this.leaveItAll = leaveItAll;
/* 121 */     this.doAdverbialNP = doAdverbialNP;
/* 122 */     this.headFinder = headFinder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String cleanUpLabel(String label)
/*     */   {
/* 131 */     if (label == null) {
/* 132 */       return "ROOT";
/*     */     }
/* 134 */     if (this.leaveItAll == 1)
/* 135 */       return this.tlp.categoryAndFunction(label);
/* 136 */     if (this.leaveItAll == 2) {
/* 137 */       return label;
/*     */     }
/* 139 */     boolean nptemp = NPTmpPattern.matcher(label).matches();
/* 140 */     boolean pptemp = PPTmpPattern.matcher(label).matches();
/* 141 */     boolean advptemp = ADVPTmpPattern.matcher(label).matches();
/* 142 */     boolean anytemp = TmpPattern.matcher(label).matches();
/* 143 */     boolean subj = NPSbjPattern.matcher(label).matches();
/* 144 */     boolean npadv = NPAdvPattern.matcher(label).matches();
/* 145 */     label = this.tlp.basicCategory(label);
/* 146 */     if ((anytemp) && (this.temporalAnnotation == 2)) {
/* 147 */       label = label + "-TMP";
/* 148 */     } else if ((pptemp) && ((this.temporalAnnotation == 5) || (this.temporalAnnotation == 6) || (this.temporalAnnotation == 7) || (this.temporalAnnotation == 8) || (this.temporalAnnotation == 9))) {
/* 149 */       label = label + "-TMP";
/* 150 */     } else if ((advptemp) && ((this.temporalAnnotation == 8) || (this.temporalAnnotation == 9))) {
/* 151 */       label = label + "-TMP";
/* 152 */     } else if ((this.temporalAnnotation > 0) && (nptemp)) {
/* 153 */       label = label + "-TMP";
/*     */     }
/* 155 */     if ((this.doAdverbialNP) && (npadv)) {
/* 156 */       label = label + "-ADV";
/*     */     }
/* 158 */     if ((this.doSGappedStuff) && (subj)) {
/* 159 */       label = label + "-SBJ";
/*     */     }
/* 161 */     return label;
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean includesEmptyNPSubj(Tree t)
/*     */   {
/* 167 */     if (t == null) {
/* 168 */       return false;
/*     */     }
/* 170 */     Tree[] kids = t.children();
/* 171 */     if (kids == null) {
/* 172 */       return false;
/*     */     }
/* 174 */     boolean foundNullSubj = false;
/* 175 */     for (int i = 0; i < kids.length; i++) {
/* 176 */       Tree[] kidkids = kids[i].children();
/* 177 */       if (NPSbjPattern.matcher(kids[i].value()).matches()) {
/* 178 */         kids[i].setValue("NP");
/* 179 */         if ((kidkids != null) && (kidkids.length == 1) && (kidkids[0].value().equals("-NONE-")))
/*     */         {
/* 181 */           foundNullSubj = true;
/*     */         }
/*     */       }
/*     */     }
/* 185 */     return foundNullSubj;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree normalizeWholeTree(Tree tree, TreeFactory tf)
/*     */   {
/* 195 */     TreeTransformer transformer1 = new TreeTransformer() {
/*     */       public Tree transformTree(Tree t) {
/* 197 */         if (NPTmpRetainingTreeNormalizer.this.doSGappedStuff) {
/* 198 */           String lab = t.label().value();
/* 199 */           if ((lab.equals("S")) && (NPTmpRetainingTreeNormalizer.includesEmptyNPSubj(t))) {
/* 200 */             LabelFactory lf = t.label().labelFactory();
/*     */             
/*     */ 
/* 203 */             t.setLabel(lf.newLabel(t.label().value() + "-G"));
/*     */           }
/*     */         }
/* 206 */         return t;
/*     */       }
/* 208 */     };
/* 209 */     Filter subtreeFilter = new Filter() {
/*     */       public boolean accept(Object obj) {
/* 211 */         Tree t = (Tree)obj;
/* 212 */         Tree[] kids = t.children();
/* 213 */         Label l = t.label();
/*     */         
/* 215 */         if (("RS".equals(t.label().value())) || ("RM".equals(t.label().value())) || ("IP".equals(t.label().value())) || ("CODE".equals(t.label().value()))) {
/* 216 */           return false;
/*     */         }
/* 218 */         if ((l != null) && (l.value() != null) && (l.value().equals("-NONE-")) && (!t.isLeaf()) && (kids.length == 1) && (kids[0].isLeaf()))
/*     */         {
/* 220 */           return false;
/*     */         }
/* 222 */         return true;
/*     */       }
/* 224 */     };
/* 225 */     Filter nodeFilter = new Filter() {
/*     */       public boolean accept(Tree t) {
/* 227 */         if ((t.isLeaf()) || (t.isPreTerminal())) {
/* 228 */           return true;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 234 */         if (t.numChildren() != 1) {
/* 235 */           return true;
/*     */         }
/* 237 */         if ((t.label() != null) && (t.label().value() != null) && (t.label().value().equals(t.children()[0].label().value()))) {
/* 238 */           return false;
/*     */         }
/* 240 */         return true;
/*     */       }
/* 242 */     };
/* 243 */     TreeTransformer transformer2 = new TreeTransformer() {
/*     */       public Tree transformTree(Tree t) {
/* 245 */         if (NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 2) {
/* 246 */           String lab = t.label().value();
/* 247 */           if (NPTmpRetainingTreeNormalizer.TmpPattern.matcher(lab).matches()) {
/* 248 */             Tree oldT = t;
/*     */             Tree ht;
/*     */             do {
/* 251 */               ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(oldT);
/*     */               
/* 253 */               if (ht.label().value().equals("POS")) {
/* 254 */                 int j = oldT.indexOf(ht);
/* 255 */                 if (j > 0) {
/* 256 */                   ht = oldT.getChild(j - 1);
/*     */                 }
/*     */               }
/* 259 */               LabelFactory lf = ht.label().labelFactory();
/*     */               
/*     */ 
/* 262 */               ht.setLabel(lf.newLabel(ht.label().value() + "-TMP"));
/* 263 */               oldT = ht;
/* 264 */             } while (!ht.isPreTerminal());
/* 265 */             if (lab.startsWith("PP")) {
/* 266 */               ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(t);
/*     */               
/* 268 */               int j = t.indexOf(ht);
/* 269 */               int sz = t.children().length;
/* 270 */               if (j + 1 < sz) {
/* 271 */                 ht = t.getChild(j + 1);
/*     */               }
/* 273 */               if (ht.label().value().startsWith("NP")) {
/* 274 */                 while (!ht.isLeaf()) {
/* 275 */                   LabelFactory lf = ht.label().labelFactory();
/*     */                   
/*     */ 
/* 278 */                   ht.setLabel(lf.newLabel(ht.label().value() + "-TMP"));
/* 279 */                   ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(ht);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 284 */         } else if (NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 3) {
/* 285 */           String lab = t.label().value();
/* 286 */           if (NPTmpRetainingTreeNormalizer.NPTmpPattern.matcher(lab).matches())
/*     */           {
/* 288 */             Tree ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(t);
/* 289 */             if (ht.isPreTerminal())
/*     */             {
/* 291 */               LabelFactory lf = ht.label().labelFactory();
/* 292 */               Tree[] kids = t.children();
/* 293 */               for (int i = 0; i < kids.length; i++) {
/* 294 */                 if (kids[i].isPreTerminal())
/*     */                 {
/*     */ 
/* 297 */                   kids[i].setLabel(lf.newLabel(kids[i].value() + "-TMP"));
/*     */                 }
/*     */               }
/*     */             } else {
/* 301 */               Tree oldT = t;
/*     */               do {
/* 303 */                 ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(oldT);
/* 304 */                 oldT = ht;
/* 305 */               } while (!ht.isPreTerminal());
/* 306 */               LabelFactory lf = ht.label().labelFactory();
/*     */               
/*     */ 
/* 309 */               ht.setLabel(lf.newLabel(ht.label().value() + "-TMP"));
/*     */             }
/*     */           }
/* 312 */         } else if (NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 4) {
/* 313 */           String lab = t.label().value();
/* 314 */           if (NPTmpRetainingTreeNormalizer.NPTmpPattern.matcher(lab).matches()) {
/* 315 */             Tree oldT = t;
/*     */             Tree ht;
/*     */             do {
/* 318 */               ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(oldT);
/*     */               
/* 320 */               if (ht.label().value().equals("POS")) {
/* 321 */                 int j = oldT.indexOf(ht);
/* 322 */                 if (j > 0) {
/* 323 */                   ht = oldT.getChild(j - 1);
/*     */                 }
/*     */               }
/* 326 */               if ((ht.isPreTerminal()) || (ht.value().startsWith("NP"))) {
/* 327 */                 LabelFactory lf = ht.labelFactory();
/*     */                 
/*     */ 
/* 330 */                 ht.setLabel(lf.newLabel(ht.label().value() + "-TMP"));
/* 331 */                 oldT = ht;
/*     */               }
/* 333 */             } while (ht.value().startsWith("NP"));
/*     */           }
/* 335 */         } else if ((NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 5) || (NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 6) || (NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 7))
/*     */         {
/* 337 */           String lab = t.value();
/* 338 */           if ((NPTmpRetainingTreeNormalizer.NPTmpPattern.matcher(lab).matches()) || (NPTmpRetainingTreeNormalizer.PPTmpPattern.matcher(lab).matches())) {
/* 339 */             Tree oldT = t;
/*     */             do
/*     */             {
/* 342 */               Tree ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(oldT);
/*     */               
/* 344 */               if (ht.value().equals("POS")) {
/* 345 */                 int j = oldT.indexOf(ht);
/* 346 */                 if (j > 0) {
/* 347 */                   ht = oldT.getChild(j - 1);
/*     */                 }
/* 349 */               } else if (((NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 6) || (NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 7)) && ((ht.value().equals("IN")) || (ht.value().equals("TO"))))
/*     */               {
/* 351 */                 Tree[] kidlets = oldT.children();
/* 352 */                 for (int k = kidlets.length - 1; k > 0; k--) {
/* 353 */                   if (kidlets[k].value().startsWith("NP")) {
/* 354 */                     ht = kidlets[k];
/*     */                   }
/*     */                 }
/*     */               }
/* 358 */               LabelFactory lf = ht.labelFactory();
/*     */               
/*     */ 
/* 361 */               if ((ht.isPreTerminal()) || (ht.value().startsWith("NP"))) {
/* 362 */                 ht.setLabel(lf.newLabel(ht.value() + "-TMP"));
/*     */               }
/* 364 */               if ((NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 7) && (oldT.value().startsWith("PP"))) {
/* 365 */                 oldT.setLabel(lf.newLabel(NPTmpRetainingTreeNormalizer.this.tlp.basicCategory(oldT.value())));
/*     */               }
/* 367 */               oldT = ht;
/* 368 */             } while ((oldT.value().startsWith("NP")) || (oldT.value().startsWith("PP")));
/*     */           }
/* 370 */         } else if (NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 8)
/*     */         {
/* 372 */           String lab = t.value();
/* 373 */           if ((NPTmpRetainingTreeNormalizer.NPTmpPattern.matcher(lab).matches()) || (NPTmpRetainingTreeNormalizer.PPTmpPattern.matcher(lab).matches()) || (NPTmpRetainingTreeNormalizer.ADVPTmpPattern.matcher(lab).matches())) {
/* 374 */             Tree oldT = t;
/*     */             do
/*     */             {
/* 377 */               Tree ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(oldT);
/*     */               
/* 379 */               if (ht.value().equals("POS")) {
/* 380 */                 int j = oldT.indexOf(ht);
/* 381 */                 if (j > 0) {
/* 382 */                   ht = oldT.getChild(j - 1);
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/* 387 */               if ((ht.isPreTerminal()) || (ht.value().startsWith("NP"))) {
/* 388 */                 LabelFactory lf = ht.labelFactory();
/* 389 */                 ht.setLabel(lf.newLabel(ht.value() + "-TMP"));
/*     */               }
/* 391 */               oldT = ht;
/* 392 */             } while (oldT.value().startsWith("NP"));
/*     */           }
/* 394 */         } else if (NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 9)
/*     */         {
/* 396 */           String lab = t.value();
/* 397 */           if ((NPTmpRetainingTreeNormalizer.NPTmpPattern.matcher(lab).matches()) || (NPTmpRetainingTreeNormalizer.PPTmpPattern.matcher(lab).matches()) || (NPTmpRetainingTreeNormalizer.ADVPTmpPattern.matcher(lab).matches()))
/*     */           {
/* 399 */             NPTmpRetainingTreeNormalizer.this.addTMP9(t);
/*     */           }
/* 401 */         } else if (NPTmpRetainingTreeNormalizer.this.temporalAnnotation == 1) {
/* 402 */           String lab = t.label().value();
/* 403 */           if ((lab != null) && (NPTmpRetainingTreeNormalizer.NPTmpPattern.matcher(lab).matches())) {
/* 404 */             Tree oldT = t;
/*     */             Tree ht;
/*     */             do {
/* 407 */               ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(oldT);
/*     */               
/* 409 */               if (ht.label().value().equals("POS")) {
/* 410 */                 int j = oldT.indexOf(ht);
/* 411 */                 if (j > 0) {
/* 412 */                   ht = oldT.getChild(j - 1);
/*     */                 }
/*     */               }
/* 415 */               oldT = ht;
/* 416 */             } while (!ht.isPreTerminal());
/* 417 */             LabelFactory lf = ht.label().labelFactory();
/*     */             
/*     */ 
/* 420 */             ht.setLabel(lf.newLabel(ht.label().value() + "-TMP"));
/*     */           }
/*     */         }
/* 423 */         if (NPTmpRetainingTreeNormalizer.this.doAdverbialNP) {
/* 424 */           String lab = t.value();
/* 425 */           if (NPTmpRetainingTreeNormalizer.NPAdvPattern.matcher(lab).matches()) {
/* 426 */             Tree oldT = t;
/*     */             Tree ht;
/*     */             do {
/* 429 */               ht = NPTmpRetainingTreeNormalizer.this.headFinder.determineHead(oldT);
/*     */               
/* 431 */               if (ht.label().value().equals("POS")) {
/* 432 */                 int j = oldT.indexOf(ht);
/* 433 */                 if (j > 0) {
/* 434 */                   ht = oldT.getChild(j - 1);
/*     */                 }
/*     */               }
/* 437 */               if ((ht.isPreTerminal()) || (ht.value().startsWith("NP"))) {
/* 438 */                 LabelFactory lf = ht.labelFactory();
/*     */                 
/*     */ 
/* 441 */                 ht.setLabel(lf.newLabel(ht.label().value() + "-ADV"));
/* 442 */                 oldT = ht;
/*     */               }
/* 444 */             } while (ht.value().startsWith("NP"));
/*     */           }
/*     */         }
/* 447 */         return t;
/*     */       }
/*     */     };
/*     */     
/* 451 */     if (tree.label().value().equals("S")) {
/* 452 */       tree = tf.newTreeNode("ROOT", Collections.singletonList(tree));
/*     */     }
/*     */     
/* 455 */     for (Tree subtree : tree) {
/* 456 */       if ((subtree.isPhrasal()) && ("VB".equals(subtree.label().value()))) {
/* 457 */         subtree.setValue("VP");
/*     */       }
/*     */     }
/* 460 */     tree = tree.transform(transformer1);
/* 461 */     if (tree == null) return null;
/* 462 */     tree = tree.prune(subtreeFilter, tf);
/* 463 */     if (tree == null) return null;
/* 464 */     tree = tree.spliceOut(nodeFilter, tf);
/* 465 */     if (tree == null) return null;
/* 466 */     return tree.transform(transformer2, tf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addTMP9(Tree tree)
/*     */   {
/* 475 */     Tree ht = this.headFinder.determineHead(tree);
/*     */     
/* 477 */     if (ht.value().equals("POS")) {
/* 478 */       int j = tree.indexOf(ht);
/* 479 */       if (j > 0) {
/* 480 */         ht = tree.getChild(j - 1);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 485 */     if ((ht.isPreTerminal()) || (ht.value().startsWith("NP")) || (ht.value().startsWith("PP")) || (ht.value().startsWith("ADVP")))
/*     */     {
/* 487 */       if (!TmpPattern.matcher(ht.value()).matches()) {
/* 488 */         LabelFactory lf = ht.labelFactory();
/*     */         
/*     */ 
/* 491 */         ht.setLabel(lf.newLabel(ht.value() + "-TMP"));
/*     */       }
/* 493 */       if ((ht.value().startsWith("NP")) || (ht.value().startsWith("PP")) || (ht.value().startsWith("ADVP")))
/*     */       {
/* 495 */         addTMP9(ht);
/*     */       }
/*     */     }
/*     */     
/* 499 */     Tree[] kidlets = tree.children();
/* 500 */     for (int k = 0; k < kidlets.length; k++) {
/* 501 */       ht = kidlets[k];
/*     */       
/* 503 */       if ((tree.isPrePreTerminal()) && (!TmpPattern.matcher(ht.value()).matches()))
/*     */       {
/*     */ 
/* 506 */         LabelFactory lf = ht.labelFactory();
/*     */         
/*     */ 
/* 509 */         ht.setLabel(lf.newLabel(ht.value() + "-TMP"));
/* 510 */       } else if (ht.value().startsWith("NP"))
/*     */       {
/* 512 */         if (!TmpPattern.matcher(ht.value()).matches()) {
/* 513 */           LabelFactory lf = ht.labelFactory();
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 518 */           ht.setLabel(lf.newLabel(ht.value() + "-TMP"));
/*     */         }
/* 520 */         addTMP9(ht);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\NPTmpRetainingTreeNormalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */