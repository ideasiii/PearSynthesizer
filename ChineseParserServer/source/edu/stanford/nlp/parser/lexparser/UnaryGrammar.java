/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class UnaryGrammar implements java.io.Serializable, Iterable<UnaryRule>
/*     */ {
/*  17 */   private int numStates = -1;
/*     */   
/*     */   private String stateSpace;
/*     */   
/*  21 */   private transient List<UnaryRule>[] rulesWithParent = null;
/*  22 */   private transient List<UnaryRule>[] rulesWithChild = null;
/*     */   
/*  24 */   private transient List<UnaryRule>[] closedRulesWithParent = null;
/*  25 */   private transient List<UnaryRule>[] closedRulesWithChild = null;
/*     */   
/*  27 */   private transient UnaryRule[][] closedRulesWithP = (UnaryRule[][])null;
/*  28 */   private transient UnaryRule[][] closedRulesWithC = (UnaryRule[][])null;
/*     */   
/*     */ 
/*  31 */   private Map<UnaryRule, UnaryRule> coreRules = null;
/*     */   
/*  33 */   private transient Map<UnaryRule, UnaryRule> bestRulesUnderMax = null;
/*     */   
/*     */   public int numClosedRules()
/*     */   {
/*  37 */     return this.bestRulesUnderMax.keySet().size();
/*     */   }
/*     */   
/*     */   public UnaryRule getRule(UnaryRule ur) {
/*  41 */     return (UnaryRule)this.coreRules.get(ur);
/*     */   }
/*     */   
/*     */   public Iterator<UnaryRule> closedRuleIterator() {
/*  45 */     return this.bestRulesUnderMax.keySet().iterator();
/*     */   }
/*     */   
/*     */   public int numRules() {
/*  49 */     return this.coreRules.keySet().size();
/*     */   }
/*     */   
/*     */   public Iterator<UnaryRule> iterator() {
/*  53 */     return ruleIterator();
/*     */   }
/*     */   
/*     */   public Iterator<UnaryRule> ruleIterator() {
/*  57 */     return this.coreRules.keySet().iterator();
/*     */   }
/*     */   
/*     */   public List<UnaryRule> rules() {
/*  61 */     return new ArrayList(this.coreRules.keySet());
/*     */   }
/*     */   
/*     */   public void purgeRules()
/*     */   {
/*  66 */     Map<UnaryRule, UnaryRule> bR = new HashMap();
/*  67 */     for (UnaryRule ur : this.bestRulesUnderMax.keySet()) {
/*  68 */       if (ur.parent != ur.child) {
/*  69 */         bR.put(ur, ur);
/*     */       } else {
/*  71 */         this.closedRulesWithParent[ur.parent].remove(ur);
/*  72 */         this.closedRulesWithChild[ur.child].remove(ur);
/*     */       }
/*     */     }
/*  75 */     this.bestRulesUnderMax = bR;
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
/*     */   private void closeRulesUnderMax(UnaryRule ur)
/*     */   {
/* 108 */     int i = 0; for (int isz = this.closedRulesWithChild[ur.parent].size(); i < isz; i++) {
/* 109 */       UnaryRule pr = (UnaryRule)this.closedRulesWithChild[ur.parent].get(i);
/* 110 */       int j = 0; for (int jsz = this.closedRulesWithParent[ur.child].size(); j < jsz; j++) {
/* 111 */         UnaryRule cr = (UnaryRule)this.closedRulesWithParent[ur.child].get(j);
/* 112 */         UnaryRule resultR = new UnaryRule(pr.parent, cr.child, pr.score + cr.score + ur.score);
/*     */         
/* 114 */         relaxRule(resultR);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean relaxRule(UnaryRule ur)
/*     */   {
/* 135 */     UnaryRule bestR = (UnaryRule)this.bestRulesUnderMax.get(ur);
/* 136 */     if (bestR == null) {
/* 137 */       this.bestRulesUnderMax.put(ur, ur);
/* 138 */       this.closedRulesWithParent[ur.parent].add(ur);
/* 139 */       this.closedRulesWithChild[ur.child].add(ur);
/* 140 */       return true;
/*     */     }
/* 142 */     if (bestR.score < ur.score) {
/* 143 */       bestR.score = ur.score;
/* 144 */       return true;
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   public double scoreRule(UnaryRule ur)
/*     */   {
/* 151 */     UnaryRule bestR = (UnaryRule)this.bestRulesUnderMax.get(ur);
/* 152 */     return bestR != null ? bestR.score : Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public void addRule(UnaryRule ur)
/*     */   {
/* 157 */     closeRulesUnderMax(ur);
/* 158 */     this.coreRules.put(ur, ur);
/* 159 */     this.rulesWithParent[ur.parent].add(ur);
/* 160 */     this.rulesWithChild[ur.child].add(ur);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 167 */   private static final UnaryRule[] EMPTY_UNARY_RULE_ARRAY = new UnaryRule[0];
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/* 170 */   void makeCRArrays() { this.closedRulesWithP = new UnaryRule[this.numStates][];
/* 171 */     this.closedRulesWithC = new UnaryRule[this.numStates][];
/* 172 */     for (int i = 0; i < this.numStates; i++) {
/* 173 */       this.closedRulesWithP[i] = ((UnaryRule[])this.closedRulesWithParent[i].toArray(EMPTY_UNARY_RULE_ARRAY));
/* 174 */       this.closedRulesWithC[i] = ((UnaryRule[])this.closedRulesWithChild[i].toArray(EMPTY_UNARY_RULE_ARRAY));
/*     */     }
/*     */   }
/*     */   
/*     */   public UnaryRule[] closedRulesByParent(int state) {
/* 179 */     if (this.closedRulesWithP == null) {
/* 180 */       makeCRArrays();
/*     */     }
/* 182 */     if (state >= this.closedRulesWithP.length) {
/* 183 */       return EMPTY_UNARY_RULE_ARRAY;
/*     */     }
/* 185 */     return this.closedRulesWithP[state];
/*     */   }
/*     */   
/*     */   public UnaryRule[] closedRulesByChild(int state) {
/* 189 */     if (this.closedRulesWithC == null) {
/* 190 */       makeCRArrays();
/*     */     }
/* 192 */     if (state >= this.closedRulesWithC.length) {
/* 193 */       return EMPTY_UNARY_RULE_ARRAY;
/*     */     }
/* 195 */     return this.closedRulesWithC[state];
/*     */   }
/*     */   
/*     */   public Iterator<UnaryRule> closedRuleIteratorByParent(int state) {
/* 199 */     if (state >= this.closedRulesWithParent.length) {
/* 200 */       List<UnaryRule> lur = Collections.emptyList();
/* 201 */       return lur.iterator();
/*     */     }
/* 203 */     return this.closedRulesWithParent[state].iterator();
/*     */   }
/*     */   
/*     */   public Iterator<UnaryRule> closedRuleIteratorByChild(int state) {
/* 207 */     if (state >= this.closedRulesWithChild.length) {
/* 208 */       List<UnaryRule> lur = Collections.emptyList();
/* 209 */       return lur.iterator();
/*     */     }
/* 211 */     return this.closedRulesWithChild[state].iterator();
/*     */   }
/*     */   
/*     */   public Iterator<UnaryRule> ruleIteratorByParent(int state) {
/* 215 */     if (state >= this.rulesWithParent.length) {
/* 216 */       List<UnaryRule> lur = Collections.emptyList();
/* 217 */       return lur.iterator();
/*     */     }
/* 219 */     return this.rulesWithParent[state].iterator();
/*     */   }
/*     */   
/*     */   public Iterator<UnaryRule> ruleIteratorByChild(int state) {
/* 223 */     if (state >= this.rulesWithChild.length) {
/* 224 */       List<UnaryRule> lur = Collections.emptyList();
/* 225 */       return lur.iterator();
/*     */     }
/* 227 */     return this.rulesWithChild[state].iterator();
/*     */   }
/*     */   
/*     */   public List<UnaryRule> rulesByParent(int state) {
/* 231 */     if (state >= this.rulesWithParent.length) {
/* 232 */       return Collections.emptyList();
/*     */     }
/* 234 */     return this.rulesWithParent[state];
/*     */   }
/*     */   
/*     */   public List<UnaryRule> rulesByChild(int state) {
/* 238 */     if (state >= this.rulesWithChild.length) {
/* 239 */       return Collections.emptyList();
/*     */     }
/* 241 */     return this.rulesWithChild[state];
/*     */   }
/*     */   
/*     */   public List[] rulesWithParent() {
/* 245 */     return this.rulesWithParent;
/*     */   }
/*     */   
/*     */   private void readObject(java.io.ObjectInputStream stream) throws java.io.IOException, ClassNotFoundException {
/* 249 */     stream.defaultReadObject();
/* 250 */     Set<UnaryRule> allRules = new java.util.HashSet(this.coreRules.keySet());
/* 251 */     init();
/* 252 */     for (UnaryRule ur : allRules) {
/* 253 */       addRule(ur);
/*     */     }
/* 255 */     purgeRules();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void init()
/*     */   {
/* 263 */     this.coreRules = new HashMap();
/* 264 */     this.rulesWithParent = new List[this.numStates];
/* 265 */     this.rulesWithChild = new List[this.numStates];
/* 266 */     this.closedRulesWithParent = new List[this.numStates];
/* 267 */     this.closedRulesWithChild = new List[this.numStates];
/* 268 */     this.bestRulesUnderMax = new HashMap();
/*     */     
/* 270 */     for (int s = 0; s < this.numStates; s++) {
/* 271 */       this.rulesWithParent[s] = new ArrayList();
/* 272 */       this.rulesWithChild[s] = new ArrayList();
/* 273 */       this.closedRulesWithParent[s] = new ArrayList();
/* 274 */       this.closedRulesWithChild[s] = new ArrayList();
/* 275 */       UnaryRule selfR = new UnaryRule(s, s, 0.0D);
/* 276 */       relaxRule(selfR);
/*     */     }
/*     */   }
/*     */   
/*     */   public UnaryGrammar(int numStates) {
/* 281 */     this(numStates, "states");
/*     */   }
/*     */   
/*     */   public UnaryGrammar(int numStates, String stateSpace) {
/* 285 */     this.numStates = numStates;
/* 286 */     this.stateSpace = stateSpace;
/* 287 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readData(BufferedReader in)
/*     */     throws java.io.IOException
/*     */   {
/* 298 */     int lineNum = 1;
/* 299 */     Numberer n = Numberer.getGlobalNumberer(this.stateSpace);
/*     */     
/* 301 */     String line = in.readLine();
/* 302 */     while ((line != null) && (line.length() > 0)) {
/*     */       try {
/* 304 */         addRule(new UnaryRule(line, n));
/*     */       } catch (Exception e) {
/* 306 */         throw new java.io.IOException("Error on line " + lineNum);
/*     */       }
/* 308 */       lineNum++;
/* 309 */       line = in.readLine();
/*     */     }
/* 311 */     purgeRules();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeData(Writer w)
/*     */   {
/* 319 */     PrintWriter out = new PrintWriter(w);
/*     */     
/* 321 */     for (UnaryRule ur : this) {
/* 322 */       out.println(ur);
/*     */     }
/* 324 */     out.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeAllData(Writer w)
/*     */   {
/* 332 */     PrintWriter out = new PrintWriter(w);
/*     */     
/* 334 */     out.println("Unary ruleIterator");
/* 335 */     for (Iterator rI = ruleIterator(); rI.hasNext();) {
/* 336 */       out.println(rI.next().toString());
/*     */     }
/* 338 */     out.println("Unary closedRuleIterator");
/* 339 */     for (Iterator rI = closedRuleIterator(); rI.hasNext();) {
/* 340 */       out.println(rI.next().toString());
/*     */     }
/* 342 */     Numberer n = Numberer.getGlobalNumberer(this.stateSpace);
/* 343 */     out.println("Unary rulesWithParentIterator");
/* 344 */     Iterator rI; for (int i = 0; i < this.numStates; i++) {
/* 345 */       out.println(n.object(i));
/* 346 */       for (rI = ruleIteratorByParent(i); rI.hasNext();) {
/* 347 */         out.print("  ");
/* 348 */         out.println(rI.next().toString());
/*     */       }
/*     */     }
/* 351 */     out.println("Unary closedRulesWithParentIterator");
/* 352 */     Iterator rI; for (int i = 0; i < this.numStates; i++) {
/* 353 */       out.println(n.object(i));
/* 354 */       for (rI = closedRuleIteratorByParent(i); rI.hasNext();) {
/* 355 */         out.print("  ");
/* 356 */         out.println(rI.next().toString());
/*     */       }
/*     */     }
/* 359 */     out.flush();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 363 */     Writer w = new java.io.StringWriter();
/* 364 */     writeData(w);
/* 365 */     return w.toString();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\UnaryGrammar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */