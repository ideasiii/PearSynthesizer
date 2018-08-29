/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IterativeCKYPCFGParser
/*     */   extends ExhaustivePCFGParser
/*     */ {
/*     */   private static final float STEP_SIZE = -11.0F;
/*     */   
/*     */   public IterativeCKYPCFGParser(BinaryGrammar bg, UnaryGrammar ug, Lexicon lex, Options op)
/*     */   {
/*  18 */     super(bg, ug, lex, op);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void doInsideScores()
/*     */   {
/*  25 */     float threshold = -11.0F;
/*  26 */     while (!doInsideScoresHelper(threshold)) {
/*  27 */       threshold += -11.0F;
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
/*     */   private boolean doInsideScoresHelper(float threshold)
/*     */   {
/*  41 */     boolean prunedSomething = false;
/*  42 */     for (int diff = 2; diff <= this.length; diff++)
/*     */     {
/*     */ 
/*  45 */       for (int start = 0; start < (diff == this.length ? 1 : this.length - diff); start++)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  50 */         int end = start + diff;
/*     */         
/*  52 */         if (Test.constraints != null) {
/*  53 */           boolean skip = false;
/*  54 */           for (Test.Constraint c : Test.constraints) {
/*  55 */             if (((start > c.start) && (start < c.end) && (end > c.end)) || ((end > c.start) && (end < c.end) && (start < c.start))) {
/*  56 */               skip = true;
/*  57 */               break;
/*     */             }
/*     */           }
/*  60 */           if (skip) {}
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*  65 */           for (int leftState = 0; leftState < this.numStates; leftState++)
/*     */           {
/*  67 */             int narrowR = this.narrowRExtent[start][leftState];
/*  68 */             boolean iPossibleL = narrowR < end;
/*  69 */             if (iPossibleL)
/*     */             {
/*     */ 
/*  72 */               BinaryRule[] leftRules = this.bg.splitRulesWithLC(leftState);
/*     */               
/*  74 */               for (int i = 0; i < leftRules.length; i++)
/*     */               {
/*  76 */                 BinaryRule r = leftRules[i];
/*     */                 
/*  78 */                 int narrowL = this.narrowLExtent[end][r.rightChild];
/*  79 */                 boolean iPossibleR = narrowL >= narrowR;
/*  80 */                 if (iPossibleR)
/*     */                 {
/*     */ 
/*  83 */                   int min1 = narrowR;
/*  84 */                   int min2 = this.wideLExtent[end][r.rightChild];
/*  85 */                   int min = min1 > min2 ? min1 : min2;
/*  86 */                   if (min <= narrowL)
/*     */                   {
/*     */ 
/*  89 */                     int max1 = this.wideRExtent[start][leftState];
/*  90 */                     int max2 = narrowL;
/*  91 */                     int max = max1 < max2 ? max1 : max2;
/*  92 */                     if (min <= max)
/*     */                     {
/*     */ 
/*  95 */                       float pS = r.score;
/*  96 */                       int parentState = r.parent;
/*  97 */                       float oldIScore = this.iScore[start][end][parentState];
/*  98 */                       float bestIScore = oldIScore;
/*     */                       
/*     */                       boolean foundBetter;
/*     */                       boolean foundBetter;
/* 102 */                       if (!Test.lengthNormalization)
/*     */                       {
/* 104 */                         for (int split = min; split <= max; split++)
/*     */                         {
/* 106 */                           if (Test.constraints != null) {
/* 107 */                             boolean skip = false;
/* 108 */                             for (Test.Constraint c : Test.constraints) {
/* 109 */                               if (((start < c.start) && (end >= c.end)) || ((start <= c.start) && (end > c.end) && (split > c.start) && (split < c.end))) {
/* 110 */                                 skip = true;
/* 111 */                                 break;
/*     */                               }
/* 113 */                               if ((start == c.start) && (split == c.end)) {
/* 114 */                                 String tag = (String)this.stateNumberer.object(leftState);
/* 115 */                                 Matcher m = c.state.matcher(tag);
/* 116 */                                 if (!m.matches()) {
/* 117 */                                   skip = true;
/* 118 */                                   break;
/*     */                                 }
/*     */                               }
/* 121 */                               if ((split == c.start) && (end == c.end)) {
/* 122 */                                 String tag = (String)this.stateNumberer.object(r.rightChild);
/* 123 */                                 Matcher m = c.state.matcher(tag);
/* 124 */                                 if (!m.matches()) {
/* 125 */                                   skip = true;
/* 126 */                                   break;
/*     */                                 }
/*     */                               }
/*     */                             }
/* 130 */                             if (skip) {}
/*     */ 
/*     */                           }
/*     */                           else
/*     */                           {
/* 135 */                             float lS = this.iScore[start][split][leftState];
/* 136 */                             if (lS != Float.NEGATIVE_INFINITY)
/*     */                             {
/*     */ 
/* 139 */                               float rS = this.iScore[split][end][r.rightChild];
/* 140 */                               if (rS != Float.NEGATIVE_INFINITY)
/*     */                               {
/*     */ 
/* 143 */                                 float tot = pS + lS + rS;
/* 144 */                                 if (tot > bestIScore)
/* 145 */                                   bestIScore = tot;
/*     */                               }
/*     */                             } } }
/* 148 */                         foundBetter = bestIScore > oldIScore;
/*     */                       }
/*     */                       else {
/* 151 */                         int bestWordsInSpan = this.wordsInSpan[start][end][parentState];
/* 152 */                         float oldNormIScore = oldIScore / bestWordsInSpan;
/* 153 */                         float bestNormIScore = oldNormIScore;
/*     */                         
/* 155 */                         for (int split = min; split <= max; split++) {
/* 156 */                           float lS = this.iScore[start][split][leftState];
/* 157 */                           if (lS != Float.NEGATIVE_INFINITY)
/*     */                           {
/*     */ 
/*     */ 
/* 161 */                             float rS = this.iScore[split][end][r.rightChild];
/* 162 */                             if (rS != Float.NEGATIVE_INFINITY)
/*     */                             {
/*     */ 
/* 165 */                               float tot = pS + lS + rS;
/* 166 */                               int newWordsInSpan = this.wordsInSpan[start][split][leftState] + this.wordsInSpan[split][end][r.rightChild];
/* 167 */                               float normTot = tot / newWordsInSpan;
/* 168 */                               if (normTot > bestNormIScore) {
/* 169 */                                 bestIScore = tot;
/* 170 */                                 bestNormIScore = normTot;
/* 171 */                                 bestWordsInSpan = newWordsInSpan;
/*     */                               }
/*     */                             } } }
/* 174 */                         foundBetter = bestNormIScore > oldNormIScore;
/* 175 */                         if ((foundBetter) && (bestIScore > threshold)) {
/* 176 */                           this.wordsInSpan[start][end][parentState] = bestWordsInSpan;
/*     */                         }
/*     */                       }
/* 179 */                       if (foundBetter)
/* 180 */                         if (bestIScore > threshold)
/*     */                         {
/*     */ 
/* 183 */                           this.iScore[start][end][parentState] = bestIScore;
/*     */                           
/*     */ 
/* 186 */                           if (oldIScore == Float.NEGATIVE_INFINITY) {
/* 187 */                             if (start > this.narrowLExtent[end][parentState]) {
/* 188 */                               this.narrowLExtent[end][parentState] = start;
/* 189 */                               this.wideLExtent[end][parentState] = start;
/*     */                             }
/* 191 */                             else if (start < this.wideLExtent[end][parentState]) {
/* 192 */                               this.wideLExtent[end][parentState] = start;
/*     */                             }
/*     */                             
/* 195 */                             if (end < this.narrowRExtent[start][parentState]) {
/* 196 */                               this.narrowRExtent[start][parentState] = end;
/* 197 */                               this.wideRExtent[start][parentState] = end;
/*     */                             }
/* 199 */                             else if (end > this.wideRExtent[start][parentState]) {
/* 200 */                               this.wideRExtent[start][parentState] = end;
/*     */                             }
/*     */                           }
/*     */                         }
/*     */                         else {
/* 205 */                           prunedSomething = true;
/*     */                         }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               } } }
/* 211 */           for (int rightState = 0; rightState < this.numStates; rightState++) {
/* 212 */             int narrowL = this.narrowLExtent[end][rightState];
/* 213 */             boolean iPossibleR = narrowL > start;
/* 214 */             if (iPossibleR)
/*     */             {
/*     */ 
/* 217 */               BinaryRule[] rightRules = this.bg.splitRulesWithRC(rightState);
/*     */               
/* 219 */               for (int i = 0; i < rightRules.length; i++)
/*     */               {
/* 221 */                 BinaryRule r = rightRules[i];
/*     */                 
/* 223 */                 int narrowR = this.narrowRExtent[start][r.leftChild];
/* 224 */                 boolean iPossibleL = narrowR <= narrowL;
/* 225 */                 if (iPossibleL)
/*     */                 {
/*     */ 
/* 228 */                   int min1 = narrowR;
/* 229 */                   int min2 = this.wideLExtent[end][rightState];
/* 230 */                   int min = min1 > min2 ? min1 : min2;
/* 231 */                   if (min <= narrowL)
/*     */                   {
/*     */ 
/* 234 */                     int max1 = this.wideRExtent[start][r.leftChild];
/* 235 */                     int max2 = narrowL;
/* 236 */                     int max = max1 < max2 ? max1 : max2;
/* 237 */                     if (min <= max)
/*     */                     {
/*     */ 
/* 240 */                       float pS = r.score;
/* 241 */                       int parentState = r.parent;
/* 242 */                       float oldIScore = this.iScore[start][end][parentState];
/* 243 */                       float bestIScore = oldIScore;
/*     */                       boolean foundBetter;
/*     */                       boolean foundBetter;
/* 246 */                       if (!Test.lengthNormalization)
/*     */                       {
/* 248 */                         for (int split = min; split <= max; split++)
/*     */                         {
/* 250 */                           if (Test.constraints != null) {
/* 251 */                             boolean skip = false;
/* 252 */                             for (Test.Constraint c : Test.constraints) {
/* 253 */                               if (((start < c.start) && (end >= c.end)) || ((start <= c.start) && (end > c.end) && (split > c.start) && (split < c.end))) {
/* 254 */                                 skip = true;
/* 255 */                                 break;
/*     */                               }
/* 257 */                               if ((start == c.start) && (split == c.end)) {
/* 258 */                                 String tag = (String)this.stateNumberer.object(r.leftChild);
/* 259 */                                 Matcher m = c.state.matcher(tag);
/* 260 */                                 if (!m.matches())
/*     */                                 {
/* 262 */                                   skip = true;
/* 263 */                                   break;
/*     */                                 }
/*     */                               }
/* 266 */                               if ((split == c.start) && (end == c.end)) {
/* 267 */                                 String tag = (String)this.stateNumberer.object(rightState);
/* 268 */                                 Matcher m = c.state.matcher(tag);
/* 269 */                                 if (!m.matches())
/*     */                                 {
/* 271 */                                   skip = true;
/* 272 */                                   break;
/*     */                                 }
/*     */                               }
/*     */                             }
/* 276 */                             if (skip) {}
/*     */ 
/*     */                           }
/*     */                           else
/*     */                           {
/* 281 */                             float lS = this.iScore[start][split][r.leftChild];
/* 282 */                             if (lS != Float.NEGATIVE_INFINITY)
/*     */                             {
/*     */ 
/* 285 */                               float rS = this.iScore[split][end][rightState];
/* 286 */                               if (rS != Float.NEGATIVE_INFINITY)
/*     */                               {
/*     */ 
/* 289 */                                 float tot = pS + lS + rS;
/* 290 */                                 if (tot > bestIScore)
/* 291 */                                   bestIScore = tot;
/*     */                               }
/*     */                             } } }
/* 294 */                         foundBetter = bestIScore > oldIScore;
/*     */                       }
/*     */                       else {
/* 297 */                         int bestWordsInSpan = this.wordsInSpan[start][end][parentState];
/* 298 */                         float oldNormIScore = oldIScore / bestWordsInSpan;
/* 299 */                         float bestNormIScore = oldNormIScore;
/* 300 */                         for (int split = min; split <= max; split++) {
/* 301 */                           float lS = this.iScore[start][split][r.leftChild];
/* 302 */                           if (lS != Float.NEGATIVE_INFINITY)
/*     */                           {
/*     */ 
/* 305 */                             float rS = this.iScore[split][end][rightState];
/* 306 */                             if (rS != Float.NEGATIVE_INFINITY)
/*     */                             {
/*     */ 
/* 309 */                               float tot = pS + lS + rS;
/* 310 */                               int newWordsInSpan = this.wordsInSpan[start][split][r.leftChild] + this.wordsInSpan[split][end][rightState];
/* 311 */                               float normTot = tot / newWordsInSpan;
/* 312 */                               if (normTot > bestNormIScore) {
/* 313 */                                 bestIScore = tot;
/* 314 */                                 bestNormIScore = normTot;
/* 315 */                                 bestWordsInSpan = newWordsInSpan;
/*     */                               }
/*     */                             } } }
/* 318 */                         foundBetter = bestNormIScore > oldNormIScore;
/* 319 */                         if (foundBetter) {
/* 320 */                           this.wordsInSpan[start][end][parentState] = bestWordsInSpan;
/*     */                         }
/*     */                       }
/* 323 */                       if (foundBetter) {
/* 324 */                         if (bestIScore > threshold) {
/* 325 */                           this.iScore[start][end][parentState] = bestIScore;
/*     */                           
/* 327 */                           if (oldIScore == Float.NEGATIVE_INFINITY) {
/* 328 */                             if (start > this.narrowLExtent[end][parentState]) {
/* 329 */                               this.narrowLExtent[end][parentState] = start;
/* 330 */                               this.wideLExtent[end][parentState] = start;
/*     */                             }
/* 332 */                             else if (start < this.wideLExtent[end][parentState]) {
/* 333 */                               this.wideLExtent[end][parentState] = start;
/*     */                             }
/*     */                             
/* 336 */                             if (end < this.narrowRExtent[start][parentState]) {
/* 337 */                               this.narrowRExtent[start][parentState] = end;
/* 338 */                               this.wideRExtent[start][parentState] = end;
/*     */                             }
/* 340 */                             else if (end > this.wideRExtent[start][parentState]) {
/* 341 */                               this.wideRExtent[start][parentState] = end;
/*     */                             }
/*     */                           }
/*     */                         }
/*     */                         else {
/* 346 */                           prunedSomething = true;
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 355 */           for (int state = 0; state < this.numStates; state++) {
/* 356 */             float iS = this.iScore[start][end][state];
/* 357 */             if (iS != Float.NEGATIVE_INFINITY)
/*     */             {
/*     */ 
/* 360 */               UnaryRule[] unaries = this.ug.closedRulesByChild(state);
/* 361 */               for (int r = 0; r < unaries.length; r++)
/*     */               {
/* 363 */                 UnaryRule ur = unaries[r];
/*     */                 
/* 365 */                 if (Test.constraints != null) {
/* 366 */                   boolean skip = false;
/* 367 */                   for (Test.Constraint c : Test.constraints) {
/* 368 */                     if ((start == c.start) && (end == c.end)) {
/* 369 */                       String tag = (String)this.stateNumberer.object(ur.parent);
/* 370 */                       Matcher m = c.state.matcher(tag);
/* 371 */                       if (!m.matches())
/*     */                       {
/* 373 */                         skip = true;
/* 374 */                         break;
/*     */                       }
/*     */                     }
/*     */                   }
/* 378 */                   if (skip) {}
/*     */ 
/*     */                 }
/*     */                 else
/*     */                 {
/* 383 */                   int parentState = ur.parent;
/* 384 */                   float pS = ur.score;
/* 385 */                   float tot = iS + pS;
/* 386 */                   float cur = this.iScore[start][end][parentState];
/*     */                   boolean foundBetter;
/* 388 */                   if (Test.lengthNormalization) {
/* 389 */                     int totWordsInSpan = this.wordsInSpan[start][end][state];
/* 390 */                     float normTot = tot / totWordsInSpan;
/* 391 */                     int curWordsInSpan = this.wordsInSpan[start][end][parentState];
/* 392 */                     float normCur = cur / curWordsInSpan;
/* 393 */                     boolean foundBetter = normTot > normCur;
/* 394 */                     if ((foundBetter) && (tot > threshold)) {
/* 395 */                       this.wordsInSpan[start][end][parentState] = this.wordsInSpan[start][end][state];
/*     */                     }
/*     */                   } else {
/* 398 */                     foundBetter = tot > cur;
/*     */                   }
/* 400 */                   if (foundBetter)
/*     */                   {
/* 402 */                     if (tot > threshold) {
/* 403 */                       this.iScore[start][end][parentState] = tot;
/* 404 */                       if (cur == Float.NEGATIVE_INFINITY) {
/* 405 */                         if (start > this.narrowLExtent[end][parentState]) {
/* 406 */                           this.narrowLExtent[end][parentState] = start;
/* 407 */                           this.wideLExtent[end][parentState] = start;
/*     */                         }
/* 409 */                         else if (start < this.wideLExtent[end][parentState]) {
/* 410 */                           this.wideLExtent[end][parentState] = start;
/*     */                         }
/*     */                         
/* 413 */                         if (end < this.narrowRExtent[start][parentState]) {
/* 414 */                           this.narrowRExtent[start][parentState] = end;
/* 415 */                           this.wideRExtent[start][parentState] = end;
/*     */                         }
/* 417 */                         else if (end > this.wideRExtent[start][parentState]) {
/* 418 */                           this.wideRExtent[start][parentState] = end;
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                     else {
/* 423 */                       prunedSomething = true;
/*     */                     } }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         } } }
/* 430 */     int goal = this.stateNumberer.number(this.goalStr);
/*     */     
/* 432 */     return (this.iScore[0][this.length][goal] > Float.NEGATIVE_INFINITY) || (!prunedSomething);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\IterativeCKYPCFGParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */