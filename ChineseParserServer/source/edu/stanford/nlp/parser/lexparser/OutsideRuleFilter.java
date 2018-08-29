/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class OutsideRuleFilter
/*     */ {
/*  10 */   private Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
/*  11 */   private Numberer stateNumberer = Numberer.getGlobalNumberer("states");
/*     */   private int numTags;
/*     */   private int numFAs;
/*     */   protected FA[] leftFA;
/*     */   protected FA[] rightFA;
/*     */   
/*     */   protected static <A> List<A> reverse(List<A> list)
/*     */   {
/*  19 */     int sz = list.size();
/*  20 */     List<A> reverse = new ArrayList(sz);
/*  21 */     for (int i = sz - 1; i >= 0; i--) {
/*  22 */       reverse.add(list.get(i));
/*     */     }
/*  24 */     return reverse;
/*     */   }
/*     */   
/*     */   protected FA buildFA(List tags) {
/*  28 */     FA fa = new FA(tags.size() + 1, this.numTags);
/*  29 */     fa.setLoopState(0, true);
/*  30 */     for (int state = 1; state <= tags.size(); state++) {
/*  31 */       Object tagO = tags.get(state - 1);
/*  32 */       if (tagO == null) {
/*  33 */         fa.setLoopState(state, true);
/*  34 */         for (int symbol = 0; symbol < this.numTags; symbol++) {
/*  35 */           fa.setTransition(state - 1, symbol, state);
/*     */         }
/*     */       } else {
/*  38 */         int tag = this.tagNumberer.number(tagO);
/*  39 */         fa.setTransition(state - 1, tag, state);
/*     */       }
/*     */     }
/*  42 */     return fa;
/*     */   }
/*     */   
/*     */   protected void registerRule(List leftTags, List rightTags, int state) {
/*  46 */     this.leftFA[state] = buildFA(leftTags);
/*  47 */     this.rightFA[state] = buildFA(reverse(rightTags));
/*     */   }
/*     */   
/*     */   public void init() {
/*  51 */     for (int rule = 0; rule < this.numFAs; rule++) {
/*  52 */       this.leftFA[rule].init();
/*  53 */       this.rightFA[rule].init();
/*     */     }
/*     */   }
/*     */   
/*     */   public void advanceRight(boolean[] tags) {
/*  58 */     for (int tag = 0; tag < this.numTags; tag++) {
/*  59 */       if (tags[tag] != 0)
/*     */       {
/*     */ 
/*  62 */         for (int rule = 0; rule < this.numFAs; rule++)
/*  63 */           this.leftFA[rule].input(tag);
/*     */       }
/*     */     }
/*  66 */     for (int rule = 0; rule < this.numFAs; rule++) {
/*  67 */       this.leftFA[rule].advance();
/*     */     }
/*     */   }
/*     */   
/*     */   public void leftAccepting(boolean[] result) {
/*  72 */     for (int rule = 0; rule < this.numFAs; rule++) {
/*  73 */       result[rule] = this.leftFA[rule].isAccepting();
/*     */     }
/*     */   }
/*     */   
/*     */   public void advanceLeft(boolean[] tags) {
/*  78 */     for (int tag = 0; tag < this.numTags; tag++) {
/*  79 */       if (tags[tag] != 0)
/*     */       {
/*     */ 
/*  82 */         for (int rule = 0; rule < this.numFAs; rule++)
/*  83 */           this.rightFA[rule].input(tag);
/*     */       }
/*     */     }
/*  86 */     for (int rule = 0; rule < this.numFAs; rule++) {
/*  87 */       this.rightFA[rule].advance();
/*     */     }
/*     */   }
/*     */   
/*     */   public void rightAccepting(boolean[] result) {
/*  92 */     for (int rule = 0; rule < this.numFAs; rule++) {
/*  93 */       result[rule] = this.rightFA[rule].isAccepting();
/*     */     }
/*     */   }
/*     */   
/*     */   private void allocate(int numFAs) {
/*  98 */     this.numFAs = numFAs;
/*  99 */     this.leftFA = new FA[numFAs];
/* 100 */     this.rightFA = new FA[numFAs];
/*     */   }
/*     */   
/*     */   public OutsideRuleFilter(BinaryGrammar bg) {
/* 104 */     int numStates = this.stateNumberer.total();
/* 105 */     this.numTags = this.tagNumberer.total();
/* 106 */     allocate(numStates);
/* 107 */     for (int state = 0; state < numStates; state++) {
/* 108 */       String stateStr = (String)this.stateNumberer.object(state);
/* 109 */       List left = new ArrayList();
/* 110 */       List right = new ArrayList();
/* 111 */       if (!bg.isSynthetic(state)) {
/* 112 */         registerRule(left, right, state);
/*     */       }
/*     */       else {
/* 115 */         boolean foundSemi = false;
/* 116 */         boolean foundDots = false;
/* 117 */         List array = left;
/* 118 */         StringBuilder sb = new StringBuilder();
/* 119 */         for (int c = 0; c < stateStr.length(); c++)
/* 120 */           if (stateStr.charAt(c) == ':') {
/* 121 */             foundSemi = true;
/*     */ 
/*     */           }
/* 124 */           else if (foundSemi)
/*     */           {
/*     */ 
/* 127 */             if (stateStr.charAt(c) == ' ') {
/* 128 */               if (sb.length() > 0) {
/* 129 */                 String str = sb.toString();
/* 130 */                 if (!this.tagNumberer.hasSeen(str)) {
/* 131 */                   str = null;
/*     */                 }
/* 133 */                 array.add(str);
/* 134 */                 sb = new StringBuilder();
/*     */               }
/*     */               
/*     */             }
/* 138 */             else if ((!foundDots) && (stateStr.charAt(c) == '.')) {
/* 139 */               c += 3;
/* 140 */               foundDots = true;
/* 141 */               array = right;
/*     */             }
/*     */             else {
/* 144 */               sb.append(stateStr.charAt(c));
/*     */             } }
/* 146 */         registerRule(left, right, state);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\OutsideRuleFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */