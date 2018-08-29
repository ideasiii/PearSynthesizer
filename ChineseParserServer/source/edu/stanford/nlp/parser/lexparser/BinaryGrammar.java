/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BinaryGrammar implements java.io.Serializable, Iterable<BinaryRule>
/*     */ {
/*     */   private String stateSpace;
/*     */   private int numStates;
/*     */   private List<BinaryRule> allRules;
/*     */   private transient List<BinaryRule>[] rulesWithParent;
/*     */   private transient List<BinaryRule>[] rulesWithLC;
/*     */   private transient List<BinaryRule>[] rulesWithRC;
/*     */   private transient Set<BinaryRule>[] ruleSetWithLC;
/*     */   private transient Set<BinaryRule>[] ruleSetWithRC;
/*     */   private transient BinaryRule[][] splitRulesWithLC;
/*     */   private transient BinaryRule[][] splitRulesWithRC;
/*     */   private transient Map<BinaryRule, BinaryRule> ruleMap;
/*     */   private transient boolean[] synthetic;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public int numRules()
/*     */   {
/*  35 */     return this.allRules.size();
/*     */   }
/*     */   
/*     */   public List<BinaryRule> rules() {
/*  39 */     return this.allRules;
/*     */   }
/*     */   
/*     */   public String stateSpace() {
/*  43 */     return this.stateSpace;
/*     */   }
/*     */   
/*     */   public boolean isSynthetic(int state) {
/*  47 */     return this.synthetic[state];
/*     */   }
/*     */   
/*     */ 
/*     */   private static BinaryRule[] toBRArray(List<BinaryRule> list)
/*     */   {
/*  53 */     BinaryRule[] array = new BinaryRule[list.size()];
/*  54 */     for (int i = 0; i < array.length; i++) {
/*  55 */       array[i] = ((BinaryRule)list.get(i));
/*     */     }
/*  57 */     return array;
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
/*     */   public void splitRules()
/*     */   {
/*  71 */     Numberer stateNumberer = Numberer.getGlobalNumberer(this.stateSpace);
/*  72 */     this.synthetic = new boolean[this.numStates];
/*  73 */     for (int s = 0; s < this.numStates; s++) {
/*     */       try
/*     */       {
/*  76 */         if ((stateNumberer.object(s) instanceof String)) {
/*  77 */           this.synthetic[s] = (((String)stateNumberer.object(s)).charAt(0) == '@' ? 1 : false);
/*     */         } else {
/*  79 */           this.synthetic[s] = (((Label)stateNumberer.object(s)).value().charAt(0) == '@' ? 1 : false);
/*     */         }
/*     */       } catch (NullPointerException e) {
/*  82 */         this.synthetic[s] = true;
/*     */       }
/*     */     }
/*     */     
/*  86 */     this.splitRulesWithLC = new BinaryRule[this.numStates][];
/*  87 */     this.splitRulesWithRC = new BinaryRule[this.numStates][];
/*     */     
/*     */ 
/*  90 */     for (int state = 0; state < this.numStates; state++)
/*     */     {
/*     */ 
/*  93 */       if (isSynthetic(state)) {
/*  94 */         this.splitRulesWithLC[state] = toBRArray(this.rulesWithLC[state]);
/*  95 */         this.splitRulesWithRC[state] = toBRArray(this.rulesWithRC[state]);
/*     */       }
/*     */       else
/*     */       {
/*  99 */         List<BinaryRule> ruleList = new ArrayList();
/* 100 */         for (BinaryRule br : this.rulesWithLC[state]) {
/* 101 */           if (!isSynthetic(br.rightChild)) {
/* 102 */             ruleList.add(br);
/*     */           }
/*     */         }
/* 105 */         this.splitRulesWithLC[state] = toBRArray(ruleList);
/*     */         
/* 107 */         ruleList.clear();
/* 108 */         for (BinaryRule br : this.rulesWithRC[state]) {
/* 109 */           if (!isSynthetic(br.leftChild)) {
/* 110 */             ruleList.add(br);
/*     */           }
/*     */         }
/* 113 */         this.splitRulesWithRC[state] = toBRArray(ruleList);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public BinaryRule[] splitRulesWithLC(int state)
/*     */   {
/* 121 */     if (state >= this.splitRulesWithLC.length) {
/* 122 */       return new BinaryRule[0];
/*     */     }
/* 124 */     return this.splitRulesWithLC[state];
/*     */   }
/*     */   
/*     */   public BinaryRule[] splitRulesWithRC(int state) {
/* 128 */     if (state >= this.splitRulesWithRC.length) {
/* 129 */       return new BinaryRule[0];
/*     */     }
/* 131 */     return this.splitRulesWithRC[state];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double scoreRule(BinaryRule br)
/*     */   {
/* 141 */     BinaryRule rule = (BinaryRule)this.ruleMap.get(br);
/* 142 */     return rule != null ? rule.score : Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public void addRule(BinaryRule br)
/*     */   {
/* 147 */     this.rulesWithParent[br.parent].add(br);
/* 148 */     this.rulesWithLC[br.leftChild].add(br);
/* 149 */     this.rulesWithRC[br.rightChild].add(br);
/* 150 */     this.ruleSetWithLC[br.leftChild].add(br);
/* 151 */     this.ruleSetWithRC[br.rightChild].add(br);
/* 152 */     this.allRules.add(br);
/* 153 */     this.ruleMap.put(br, br);
/*     */   }
/*     */   
/*     */   public Iterator<BinaryRule> iterator()
/*     */   {
/* 158 */     return this.allRules.iterator();
/*     */   }
/*     */   
/*     */   public Iterator<BinaryRule> ruleIteratorByParent(int state) {
/* 162 */     if (state >= this.rulesWithParent.length) {
/* 163 */       return Collections.emptyList().iterator();
/*     */     }
/* 165 */     return this.rulesWithParent[state].iterator();
/*     */   }
/*     */   
/*     */   public Iterator<BinaryRule> ruleIteratorByRightChild(int state) {
/* 169 */     if (state >= this.rulesWithRC.length) {
/* 170 */       return Collections.emptyList().iterator();
/*     */     }
/* 172 */     return this.rulesWithRC[state].iterator();
/*     */   }
/*     */   
/*     */   public Iterator<BinaryRule> ruleIteratorByLeftChild(int state) {
/* 176 */     if (state >= this.rulesWithLC.length) {
/* 177 */       return Collections.emptyList().iterator();
/*     */     }
/* 179 */     return this.rulesWithLC[state].iterator();
/*     */   }
/*     */   
/*     */   public List<BinaryRule> ruleListByParent(int state) {
/* 183 */     if (state >= this.rulesWithParent.length) {
/* 184 */       return Collections.emptyList();
/*     */     }
/* 186 */     return this.rulesWithParent[state];
/*     */   }
/*     */   
/*     */   public List<BinaryRule> ruleListByRightChild(int state) {
/* 190 */     if (state >= this.rulesWithRC.length) {
/* 191 */       return Collections.emptyList();
/*     */     }
/* 193 */     return this.rulesWithRC[state];
/*     */   }
/*     */   
/*     */   public List<BinaryRule> ruleListByLeftChild(int state) {
/* 197 */     if (state >= this.rulesWithRC.length) {
/* 198 */       return Collections.emptyList();
/*     */     }
/* 200 */     return this.rulesWithLC[state];
/*     */   }
/*     */   
/*     */   public Set<BinaryRule> ruleSetByRightChild(int state) {
/* 204 */     if (state >= this.ruleSetWithRC.length) {
/* 205 */       return Collections.emptySet();
/*     */     }
/* 207 */     return this.ruleSetWithRC[state];
/*     */   }
/*     */   
/*     */   public Set<BinaryRule> ruleSetByLeftChild(int state) {
/* 211 */     if (state >= this.ruleSetWithRC.length) {
/* 212 */       return Collections.emptySet();
/*     */     }
/* 214 */     return this.ruleSetWithLC[state];
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
/*     */   {
/* 219 */     stream.defaultReadObject();
/* 220 */     init();
/* 221 */     for (BinaryRule br : this.allRules) {
/* 222 */       this.rulesWithParent[br.parent].add(br);
/* 223 */       this.rulesWithLC[br.leftChild].add(br);
/* 224 */       this.rulesWithRC[br.rightChild].add(br);
/* 225 */       this.ruleMap.put(br, br);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void init()
/*     */   {
/* 232 */     this.ruleMap = new java.util.HashMap();
/* 233 */     this.rulesWithParent = new List[this.numStates];
/* 234 */     this.rulesWithLC = new List[this.numStates];
/* 235 */     this.rulesWithRC = new List[this.numStates];
/* 236 */     this.ruleSetWithLC = new Set[this.numStates];
/* 237 */     this.ruleSetWithRC = new Set[this.numStates];
/* 238 */     for (int s = 0; s < this.numStates; s++) {
/* 239 */       this.rulesWithParent[s] = new ArrayList();
/* 240 */       this.rulesWithLC[s] = new ArrayList();
/* 241 */       this.rulesWithRC[s] = new ArrayList();
/* 242 */       this.ruleSetWithLC[s] = new java.util.HashSet();
/* 243 */       this.ruleSetWithRC[s] = new java.util.HashSet();
/*     */     }
/*     */   }
/*     */   
/*     */   public BinaryGrammar(int numStates) {
/* 248 */     this(numStates, "states");
/*     */   }
/*     */   
/*     */   public BinaryGrammar(int numStates, String stateSpace) {
/* 252 */     this.stateSpace = stateSpace;
/* 253 */     this.numStates = numStates;
/* 254 */     this.allRules = new ArrayList();
/* 255 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readData(BufferedReader in)
/*     */     throws IOException
/*     */   {
/* 268 */     int lineNum = 1;
/* 269 */     Numberer n = Numberer.getGlobalNumberer("states");
/* 270 */     String line = in.readLine();
/* 271 */     while ((line != null) && (line.length() > 0)) {
/*     */       try {
/* 273 */         addRule(new BinaryRule(line, n));
/*     */       } catch (Exception e) {
/* 275 */         throw new IOException("Error on line " + lineNum);
/*     */       }
/* 277 */       lineNum++;
/* 278 */       line = in.readLine();
/*     */     }
/* 280 */     splitRules();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeData(Writer w)
/*     */     throws IOException
/*     */   {
/* 290 */     PrintWriter out = new PrintWriter(w);
/* 291 */     for (BinaryRule br : this) {
/* 292 */       out.println(br);
/*     */     }
/* 294 */     out.flush();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\BinaryGrammar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */