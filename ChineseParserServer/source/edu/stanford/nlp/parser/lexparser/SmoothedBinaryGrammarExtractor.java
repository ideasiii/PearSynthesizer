/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SmoothedBinaryGrammarExtractor
/*     */   extends AbstractTreeExtractor
/*     */ {
/*     */   protected int HISTORY_DEPTH()
/*     */   {
/* 115 */     return Train.PA ? 2 : Train.gPA ? 3 : 1;
/*     */   }
/*     */   
/* 118 */   protected Numberer stateNumberer = Numberer.getGlobalNumberer("states");
/* 119 */   protected Numberer tagNumberer = Numberer.getGlobalNumberer("tags");
/*     */   
/* 121 */   protected Map ruleToLabel = new HashMap();
/* 122 */   protected Counter rulePairs = new Counter();
/* 123 */   protected Counter labelPairs = new Counter();
/*     */   
/*     */   public void tallyTree(Tree t) {
/* 126 */     LinkedList<String> ll = new LinkedList();
/*     */     
/*     */ 
/* 129 */     tallyTree(t, ll);
/*     */   }
/*     */   
/*     */   protected void tallyTree(Tree t, LinkedList<String> parents)
/*     */   {
/* 134 */     String str = t.label().value();
/* 135 */     boolean strIsPassive = str.indexOf('@') == -1;
/* 136 */     if (strIsPassive) {
/* 137 */       parents.addFirst(str);
/*     */     }
/* 139 */     if (!t.isLeaf()) {
/* 140 */       if (!t.children()[0].isLeaf()) {
/* 141 */         tallyInternalNode(t, parents);
/* 142 */         for (int c = 0; c < t.children().length; c++) {
/* 143 */           Tree child = t.children()[c];
/* 144 */           tallyTree(child, parents);
/*     */         }
/*     */       } else {
/* 147 */         this.tagNumberer.number(t.label().value());
/*     */       }
/*     */     }
/* 150 */     if (strIsPassive) {
/* 151 */       parents.removeFirst();
/*     */     }
/*     */   }
/*     */   
/*     */   protected Rule ltToRule(Tree lt) {
/* 156 */     if (lt.children().length == 1) {
/* 157 */       UnaryRule ur = new UnaryRule();
/* 158 */       ur.parent = this.stateNumberer.number(lt.label().value());
/* 159 */       ur.child = this.stateNumberer.number(lt.children()[0].label().value());
/* 160 */       return ur;
/*     */     }
/* 162 */     BinaryRule br = new BinaryRule();
/* 163 */     br.parent = this.stateNumberer.number(lt.label().value());
/* 164 */     br.leftChild = this.stateNumberer.number(lt.children()[0].label().value());
/* 165 */     br.rightChild = this.stateNumberer.number(lt.children()[1].label().value());
/* 166 */     return br;
/*     */   }
/*     */   
/*     */   protected boolean isSynthetic(int state)
/*     */   {
/* 171 */     return ((String)this.stateNumberer.object(state)).indexOf('@') > -1;
/*     */   }
/*     */   
/*     */   protected boolean isTag(int state) {
/* 175 */     return this.tagNumberer.hasSeen(this.stateNumberer.object(state));
/*     */   }
/*     */   
/*     */   protected Rule specifyRule(Rule rule, List history, int childDepth)
/*     */   {
/* 180 */     String topHistoryStr = historyToString(history.subList(1, history.size()));
/* 181 */     String bottomHistoryStr = historyToString(history.subList(0, childDepth));
/* 182 */     Rule r; Rule r; if ((rule instanceof UnaryRule)) {
/* 183 */       UnaryRule ur = new UnaryRule();
/* 184 */       UnaryRule urule = (UnaryRule)rule;
/* 185 */       ur.parent = this.stateNumberer.number(this.stateNumberer.object(urule.parent) + topHistoryStr);
/* 186 */       if (isSynthetic(urule.child)) {
/* 187 */         ur.child = this.stateNumberer.number(this.stateNumberer.object(urule.child) + topHistoryStr);
/* 188 */       } else if (isTag(urule.child)) {
/* 189 */         ur.child = urule.child;
/*     */       } else {
/* 191 */         ur.child = this.stateNumberer.number(this.stateNumberer.object(urule.child) + bottomHistoryStr);
/*     */       }
/* 193 */       r = ur;
/*     */     } else {
/* 195 */       BinaryRule br = new BinaryRule();
/* 196 */       BinaryRule brule = (BinaryRule)rule;
/* 197 */       br.parent = this.stateNumberer.number(this.stateNumberer.object(brule.parent) + topHistoryStr);
/* 198 */       if (isSynthetic(brule.leftChild)) {
/* 199 */         br.leftChild = this.stateNumberer.number(this.stateNumberer.object(brule.leftChild) + topHistoryStr);
/* 200 */       } else if (isTag(brule.leftChild)) {
/* 201 */         br.leftChild = brule.leftChild;
/*     */       } else {
/* 203 */         br.leftChild = this.stateNumberer.number(this.stateNumberer.object(brule.leftChild) + bottomHistoryStr);
/*     */       }
/* 205 */       if (isSynthetic(brule.rightChild)) {
/* 206 */         br.rightChild = this.stateNumberer.number(this.stateNumberer.object(brule.rightChild) + topHistoryStr);
/* 207 */       } else if (isTag(brule.rightChild)) {
/* 208 */         br.rightChild = brule.rightChild;
/*     */       } else {
/* 210 */         br.rightChild = this.stateNumberer.number(this.stateNumberer.object(brule.rightChild) + bottomHistoryStr);
/*     */       }
/* 212 */       r = br;
/*     */     }
/* 214 */     return r;
/*     */   }
/*     */   
/*     */   protected void tallyInternalNode(Tree lt, List parents)
/*     */   {
/* 219 */     String label = lt.label().value();
/* 220 */     Rule baseR = ltToRule(lt);
/* 221 */     this.ruleToLabel.put(baseR, label);
/*     */     
/* 223 */     int depth = 0; for (int maxDepth = Math.min(HISTORY_DEPTH(), parents.size()); depth <= maxDepth; depth++) {
/* 224 */       List history = new ArrayList(parents.subList(0, depth));
/*     */       
/* 226 */       this.rulePairs.incrementCount(new Pair(baseR, history), 1.0D);
/* 227 */       this.labelPairs.incrementCount(new Pair(label, history), 1.0D);
/*     */     }
/*     */   }
/*     */   
/* 231 */   protected Map historyToString = new HashMap();
/*     */   
/*     */   protected String historyToString(List history) {
/* 234 */     String str = (String)this.historyToString.get(history);
/* 235 */     if (str == null) {
/* 236 */       StringBuilder sb = new StringBuilder();
/* 237 */       for (int i = 0; i < history.size(); i++) {
/* 238 */         sb.append('^');
/* 239 */         sb.append(history.get(i));
/*     */       }
/* 241 */       str = sb.toString();
/* 242 */       this.historyToString.put(history, str);
/*     */     }
/* 244 */     return str;
/*     */   }
/*     */   
/*     */   public Object formResult() {
/* 248 */     Set brs = new HashSet();
/* 249 */     Set urs = new HashSet();
/*     */     
/* 251 */     int ruleCount = 0;
/* 252 */     for (Iterator pairI = this.rulePairs.keySet().iterator(); pairI.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */         goto 131)
/*     */     {
/* 253 */       if (ruleCount % 100 == 0) {
/* 254 */         System.err.println("Rules multiplied: " + ruleCount);
/*     */       }
/* 256 */       ruleCount++;
/* 257 */       Pair rulePair = (Pair)pairI.next();
/* 258 */       Rule baseRule = (Rule)rulePair.first;
/* 259 */       String baseLabel = (String)this.ruleToLabel.get(baseRule);
/* 260 */       List history = (List)rulePair.second;
/* 261 */       double totalProb = 0.0D;
/* 262 */       int depth = 1; if ((depth <= HISTORY_DEPTH()) && (depth <= history.size())) {
/* 263 */         List subHistory = history.subList(0, depth);
/* 264 */         double c_label = this.labelPairs.getCount(new Pair(baseLabel, subHistory));
/* 265 */         double c_rule = this.rulePairs.getCount(new Pair(baseRule, subHistory));
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 270 */         double prob = 1.0D / HISTORY_DEPTH() * c_rule / c_label;
/* 271 */         totalProb += prob;
/* 272 */         for (int childDepth = 0; childDepth <= Math.min(HISTORY_DEPTH() - 1, depth); childDepth++) {
/* 273 */           Rule rule = specifyRule(baseRule, subHistory, childDepth);
/* 274 */           rule.score = ((float)Math.log(totalProb));
/*     */           
/* 276 */           if ((rule instanceof UnaryRule)) {
/* 277 */             urs.add(rule);
/*     */           } else {
/* 279 */             brs.add(rule);
/*     */           }
/*     */         }
/* 262 */         depth++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 284 */     System.out.println("Total states: " + this.stateNumberer.total());
/* 285 */     BinaryGrammar bg = new BinaryGrammar(this.stateNumberer.total());
/* 286 */     UnaryGrammar ug = new UnaryGrammar(this.stateNumberer.total());
/* 287 */     for (Iterator brI = brs.iterator(); brI.hasNext();) {
/* 288 */       BinaryRule br = (BinaryRule)brI.next();
/* 289 */       bg.addRule(br);
/*     */     }
/* 291 */     for (Iterator urI = urs.iterator(); urI.hasNext();) {
/* 292 */       UnaryRule ur = (UnaryRule)urI.next();
/* 293 */       ug.addRule(ur);
/*     */     }
/* 295 */     return new Pair(ug, bg);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\SmoothedBinaryGrammarExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */