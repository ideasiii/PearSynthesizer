/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FA
/*     */ {
/*     */   protected boolean[] inStatePrev;
/*     */   protected boolean[] inStateNext;
/*     */   protected boolean[] loopState;
/*     */   protected int acceptingState;
/*     */   protected int initialState;
/*     */   protected int numStates;
/*     */   protected int numSymbols;
/*     */   protected int[][] transition;
/*     */   
/*     */   public void init()
/*     */   {
/*  62 */     Arrays.fill(this.inStatePrev, false);
/*  63 */     Arrays.fill(this.inStateNext, false);
/*  64 */     this.inStatePrev[this.initialState] = true;
/*     */   }
/*     */   
/*     */   public void input(int symbol) {
/*  68 */     for (int prevState = 0; prevState < this.numStates; prevState++) {
/*  69 */       if (this.inStatePrev[prevState] != 0) {
/*  70 */         this.inStateNext[this.transition[prevState][symbol]] = true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void advance() {
/*  76 */     boolean[] temp = this.inStatePrev;
/*  77 */     this.inStatePrev = this.inStateNext;
/*  78 */     this.inStateNext = temp;
/*  79 */     Arrays.fill(this.inStateNext, false);
/*  80 */     for (int state = 0; state < this.numStates; state++) {
/*  81 */       if ((this.inStatePrev[state] != 0) && (this.loopState[state] != 0)) {
/*  82 */         this.inStateNext[state] = true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAccepting() {
/*  88 */     return this.inStatePrev[this.acceptingState];
/*     */   }
/*     */   
/*     */   public void setTransition(int state, int symbol, int result) {
/*  92 */     this.transition[state][symbol] = result;
/*     */   }
/*     */   
/*     */   public void setLoopState(int state, boolean loops) {
/*  96 */     this.loopState[state] = loops;
/*     */   }
/*     */   
/*     */   public FA(int numStates, int numSymbols) {
/* 100 */     this.numStates = numStates;
/* 101 */     this.numSymbols = numSymbols;
/* 102 */     this.acceptingState = (numStates - 1);
/* 103 */     this.inStatePrev = new boolean[numStates];
/* 104 */     this.inStateNext = new boolean[numStates];
/* 105 */     this.loopState = new boolean[numStates];
/* 106 */     this.transition = new int[numStates][numSymbols];
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\FA.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */