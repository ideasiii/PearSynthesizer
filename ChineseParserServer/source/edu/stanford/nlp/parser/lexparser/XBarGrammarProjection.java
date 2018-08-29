/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ class XBarGrammarProjection
/*     */   implements GrammarProjection
/*     */ {
/*     */   UnaryGrammar sourceUG;
/*     */   BinaryGrammar sourceBG;
/*     */   Numberer sourceNumberer;
/*     */   UnaryGrammar targetUG;
/*     */   BinaryGrammar targetBG;
/*     */   Numberer targetNumberer;
/*     */   int[] projection;
/*     */   
/*     */   public int project(int state)
/*     */   {
/* 313 */     return this.projection[state];
/*     */   }
/*     */   
/*     */   public UnaryGrammar sourceUG() {
/* 317 */     return this.sourceUG;
/*     */   }
/*     */   
/*     */   public BinaryGrammar sourceBG() {
/* 321 */     return this.sourceBG;
/*     */   }
/*     */   
/*     */   public UnaryGrammar targetUG() {
/* 325 */     return this.targetUG;
/*     */   }
/*     */   
/*     */   public BinaryGrammar targetBG() {
/* 329 */     return this.targetBG;
/*     */   }
/*     */   
/*     */   protected String projectString(String str)
/*     */   {
/* 334 */     if (str.indexOf('@') == -1) {
/* 335 */       if (str.indexOf('^') == -1) {
/* 336 */         return str;
/*     */       }
/* 338 */       return str.substring(0, str.indexOf('^'));
/*     */     }
/* 340 */     StringBuilder sb = new StringBuilder();
/* 341 */     sb.append(str.substring(0, str.indexOf(' ')));
/* 342 */     if (str.indexOf('^') > -1) {}
/*     */     
/*     */ 
/* 345 */     int num = -2;
/* 346 */     for (int i = 0; i < str.length(); i++) {
/* 347 */       if (str.charAt(i) == ' ') {
/* 348 */         num++;
/*     */       }
/*     */     }
/* 351 */     sb.append(" w " + num);
/* 352 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected void scanStates(Numberer source, Numberer target) {
/* 356 */     for (int i = 0; i < source.total(); i++) {
/* 357 */       String stateStr = (String)source.object(i);
/* 358 */       String projStr = projectString(stateStr);
/* 359 */       this.projection[i] = target.number(projStr);
/*     */     }
/*     */   }
/*     */   
/*     */   protected BinaryRule projectBinaryRule(BinaryRule br) {
/* 364 */     BinaryRule br2 = new BinaryRule();
/* 365 */     br2.parent = this.projection[br.parent];
/* 366 */     br2.leftChild = this.projection[br.leftChild];
/* 367 */     br2.rightChild = this.projection[br.rightChild];
/* 368 */     br2.score = br.score;
/* 369 */     return br2;
/*     */   }
/*     */   
/*     */   protected UnaryRule projectUnaryRule(UnaryRule ur) {
/* 373 */     UnaryRule ur2 = new UnaryRule();
/* 374 */     ur2.parent = this.projection[ur.parent];
/* 375 */     ur2.child = this.projection[ur.child];
/* 376 */     ur2.score = ur.score;
/* 377 */     return ur2;
/*     */   }
/*     */   
/*     */   public XBarGrammarProjection(BinaryGrammar bg, UnaryGrammar ug) {
/* 381 */     Map<BinaryRule, BinaryRule> binaryRules = new HashMap();
/* 382 */     Map<UnaryRule, UnaryRule> unaryRules = new HashMap();
/* 383 */     this.sourceUG = ug;
/* 384 */     this.sourceBG = bg;
/* 385 */     this.sourceNumberer = Numberer.getGlobalNumberer(bg.stateSpace());
/* 386 */     this.targetNumberer = Numberer.getGlobalNumberer(bg.stateSpace() + "-xbar");
/* 387 */     this.projection = new int[this.sourceNumberer.total()];
/* 388 */     scanStates(this.sourceNumberer, this.targetNumberer);
/* 389 */     this.targetBG = new BinaryGrammar(this.targetNumberer.total(), bg.stateSpace() + "-xbar");
/* 390 */     this.targetUG = new UnaryGrammar(this.targetNumberer.total());
/* 391 */     for (Iterator<BinaryRule> brI = bg.iterator(); brI.hasNext();) {
/* 392 */       BinaryRule rule = projectBinaryRule((BinaryRule)brI.next());
/* 393 */       Rule old = (Rule)binaryRules.get(rule);
/* 394 */       if ((old == null) || (rule.score > old.score)) {
/* 395 */         binaryRules.put(rule, rule);
/*     */       }
/*     */     }
/* 398 */     for (BinaryRule br : binaryRules.keySet()) {
/* 399 */       this.targetBG.addRule(br);
/*     */     }
/*     */     
/* 402 */     this.targetBG.splitRules();
/* 403 */     Iterator<UnaryRule> urI; for (int parent = 0; parent < this.sourceNumberer.total(); parent++) {
/* 404 */       for (urI = ug.ruleIteratorByParent(parent); urI.hasNext();) {
/* 405 */         UnaryRule sourceRule = (UnaryRule)urI.next();
/* 406 */         UnaryRule rule = projectUnaryRule(sourceRule);
/* 407 */         Rule old = (Rule)unaryRules.get(rule);
/* 408 */         if ((old == null) || (rule.score > old.score)) {
/* 409 */           unaryRules.put(rule, rule);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 420 */     for (UnaryRule ur : unaryRules.keySet()) {
/* 421 */       this.targetUG.addRule(ur);
/*     */     }
/*     */     
/* 424 */     this.targetUG.purgeRules();
/* 425 */     System.out.println("Projected " + this.sourceNumberer.total() + " states to " + this.targetNumberer.total() + " states.");
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\XBarGrammarProjection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */