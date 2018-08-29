/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.stats.Counter;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import edu.stanford.nlp.util.Triple;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class ChineseSimWordAvgDepGrammar extends MLEDependencyGrammar
/*     */ {
/*  24 */   private double simSmooth = 10.0D;
/*     */   
/*     */   private static final String argHeadFile = "simWords/ArgHead.5";
/*     */   
/*     */   private static final String headArgFile = "simWords/HeadArg.5";
/*     */   
/*     */   private Map<Pair<Integer, String>, List<Triple<Integer, String, Double>>> simArgMap;
/*     */   private Map<Pair<Integer, String>, List<Triple<Integer, String, Double>>> simHeadMap;
/*     */   private Lexicon lex;
/*  33 */   private boolean debug = true;
/*     */   
/*  35 */   private boolean verbose = false;
/*     */   
/*     */   public ChineseSimWordAvgDepGrammar(TreebankLangParserParams tlpParams, boolean directional, boolean distance, boolean coarseDistance)
/*     */   {
/*  39 */     super(tlpParams, directional, distance, coarseDistance);
/*     */     
/*  41 */     this.simHeadMap = getMap("simWords/HeadArg.5");
/*  42 */     this.simArgMap = getMap("simWords/ArgHead.5");
/*     */   }
/*     */   
/*     */   public static Map<Pair<Integer, String>, List<Triple<Integer, String, Double>>> getMap(String filename) {
/*  46 */     Map<Pair<Integer, String>, List<Triple<Integer, String, Double>>> hashMap = new HashMap();
/*     */     try {
/*  48 */       BufferedReader wordMapBReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
/*     */       
/*     */ 
/*  51 */       Pattern linePattern = Pattern.compile("sim\\((.+)/(.+):(.+)/(.+)\\)=(.+)");
/*  52 */       String wordMapLine; while ((wordMapLine = wordMapBReader.readLine()) != null) {
/*  53 */         Matcher m = linePattern.matcher(wordMapLine);
/*  54 */         if (!m.matches()) {
/*  55 */           System.err.println("Ill-formed line in similar word map file: " + wordMapLine);
/*     */         }
/*     */         else
/*     */         {
/*  59 */           Pair<Integer, String> iTW = new Pair(Integer.valueOf(wordNumberer().number(m.group(1))), m.group(2));
/*  60 */           double score = Double.parseDouble(m.group(5));
/*     */           
/*  62 */           List<Triple<Integer, String, Double>> tripleList = (List)hashMap.get(iTW);
/*  63 */           if (tripleList == null) {
/*  64 */             tripleList = new ArrayList();
/*  65 */             hashMap.put(iTW, tripleList);
/*     */           }
/*     */           
/*  68 */           tripleList.add(new Triple(Integer.valueOf(wordNumberer().number(m.group(3))), m.group(4), Double.valueOf(score)));
/*     */         }
/*     */       }
/*  71 */     } catch (IOException e) { throw new RuntimeException("Problem reading similar words file!");
/*     */     }
/*     */     
/*  74 */     return hashMap;
/*     */   }
/*     */   
/*     */   public double scoreTB(IntDependency dependency)
/*     */   {
/*  79 */     return Test.depWeight * Math.log(probTBwithSimWords(dependency));
/*     */   }
/*     */   
/*     */   public void setLex(Lexicon lex) {
/*  83 */     this.lex = lex;
/*     */   }
/*     */   
/*  86 */   private Counter<String> statsCounter = new Counter();
/*     */   
/*     */   static {
/*  89 */     System.runFinalizersOnExit(true);
/*     */   }
/*     */   
/*     */   protected void finalize() throws Throwable {
/*  93 */     super.finalize();
/*  94 */     System.err.println("SimWordAvg stats:");
/*  95 */     System.err.println(this.statsCounter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private double probTBwithSimWords(IntDependency dependency)
/*     */   {
/* 107 */     if (!this.directional) {
/* 108 */       dependency.leftHeaded = false;
/*     */     }
/* 110 */     if (this.verbose) {
/* 111 */       System.out.println("Generating " + dependency);
/*     */     }
/*     */     
/* 114 */     short distance = dependency.distance;
/* 115 */     boolean leftHeaded = dependency.leftHeaded;
/* 116 */     int hW = dependency.head.word;
/* 117 */     int aW = dependency.arg.word;
/* 118 */     IntTaggedWord aTW = dependency.arg;
/* 119 */     IntTaggedWord hTW = dependency.head;
/*     */     
/* 121 */     double pb_stop_hTWds = getStopProb(dependency);
/*     */     
/* 123 */     boolean isRoot = rootTW(dependency.head);
/* 124 */     if (dependency.arg.word == -2)
/*     */     {
/* 126 */       if (isRoot) {
/* 127 */         return 0.0D;
/*     */       }
/* 129 */       return pb_stop_hTWds;
/*     */     }
/*     */     
/* 132 */     double pb_go_hTWds = 1.0D - pb_stop_hTWds;
/*     */     
/* 134 */     if (isRoot) {
/* 135 */       pb_go_hTWds = 1.0D;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 140 */     dependency.distance = valenceBin(distance);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */     double c_aTW_hTWd = this.argCounter.getCount(dependency);
/* 154 */     dependency.arg.word = -1;
/* 155 */     double c_aT_hTWd = this.argCounter.getCount(dependency);
/* 156 */     dependency.arg.word = aW;
/* 157 */     dependency.arg = wildTW;
/* 158 */     double c_hTWd = this.argCounter.getCount(dependency);
/* 159 */     dependency.arg = aTW;
/*     */     
/* 161 */     dependency.head.word = -1;
/* 162 */     double c_aTW_hTd = this.argCounter.getCount(dependency);
/* 163 */     dependency.arg.word = -1;
/* 164 */     double c_aT_hTd = this.argCounter.getCount(dependency);
/* 165 */     dependency.arg.word = aW;
/* 166 */     dependency.arg = wildTW;
/* 167 */     double c_hTd = this.argCounter.getCount(dependency);
/* 168 */     dependency.arg = aTW;
/* 169 */     dependency.head.word = hW;
/*     */     
/* 171 */     dependency.head = wildTW;
/* 172 */     dependency.leftHeaded = false;
/* 173 */     dependency.distance = -1;
/* 174 */     double c_aTW = this.argCounter.getCount(dependency);
/* 175 */     dependency.arg.word = -1;
/* 176 */     double c_aT = this.argCounter.getCount(dependency);
/* 177 */     dependency.arg.word = aW;
/* 178 */     dependency.leftHeaded = leftHeaded;
/* 179 */     dependency.head = hTW;
/*     */     
/* 181 */     dependency.distance = distance;
/*     */     
/*     */ 
/* 184 */     double p_aTW_hTd = c_hTd > 0.0D ? c_aTW_hTd / c_hTd : 0.0D;
/* 185 */     double p_aT_hTd = c_hTd > 0.0D ? c_aT_hTd / c_hTd : 0.0D;
/* 186 */     double p_aTW_aT = c_aTW > 0.0D ? c_aTW / c_aT : 1.0D;
/*     */     
/* 188 */     double pb_aTW_hTWd = (c_aTW_hTWd + this.smooth_aTW_hTWd * p_aTW_hTd) / (c_hTWd + this.smooth_aTW_hTWd);
/* 189 */     double pb_aT_hTWd = (c_aT_hTWd + this.smooth_aT_hTWd * p_aT_hTd) / (c_hTWd + this.smooth_aT_hTWd);
/*     */     
/* 191 */     double score = (this.interp * pb_aTW_hTWd + (1.0D - this.interp) * p_aTW_aT * pb_aT_hTWd) * pb_go_hTWds;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 197 */     List<Triple<Integer, String, Double>> sim2head = null;
/* 198 */     List<Triple<Integer, String, Double>> sim2arg = null;
/*     */     
/* 200 */     sim2arg = (List)this.simArgMap.get(new Pair(Integer.valueOf(dependency.arg.word), stringBasicCategory(dependency.arg.tag)));
/* 201 */     sim2head = (List)this.simHeadMap.get(new Pair(Integer.valueOf(dependency.head.word), stringBasicCategory(dependency.head.tag)));
/*     */     
/* 203 */     List<Integer> simArg = new ArrayList();
/* 204 */     List<Integer> simHead = new ArrayList();
/*     */     
/* 206 */     if (sim2arg != null) {
/* 207 */       for (Triple<Integer, String, Double> t : sim2arg) {
/* 208 */         simArg.add(t.first);
/*     */       }
/*     */     }
/*     */     
/* 212 */     if (sim2head != null) {
/* 213 */       for (Triple<Integer, String, Double> t : sim2head) {
/* 214 */         simHead.add(t.first);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 220 */     double cSim_aTW_hTd = 0.0D;
/* 221 */     double cSim_hTd = 0.0D;
/* 222 */     for (Iterator i$ = simHead.iterator(); i$.hasNext();) { int h = ((Integer)i$.next()).intValue();
/* 223 */       dependency.arg = aTW;
/* 224 */       dependency.head = hTW;
/* 225 */       dependency.head.word = h;
/* 226 */       cSim_aTW_hTd += this.argCounter.getCount(dependency);
/*     */       
/* 228 */       dependency.arg = wildTW;
/* 229 */       cSim_hTd += this.argCounter.getCount(dependency);
/*     */     }
/* 231 */     dependency.arg = aTW;
/* 232 */     dependency.head = hTW;
/* 233 */     double pSim_aTW_hTd = cSim_hTd > 0.0D ? cSim_aTW_hTd / cSim_hTd : 0.0D;
/*     */     
/* 235 */     if (this.debug)
/*     */     {
/* 237 */       if (pSim_aTW_hTd > 0.0D)
/*     */       {
/* 239 */         System.out.println(dependency + "\t" + pSim_aTW_hTd);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 248 */     double smoothSim_aTW_hTWd = 17.7D;
/* 249 */     double smooth_aTW_hTWd = 35.4D;
/*     */     
/*     */ 
/* 252 */     pb_aTW_hTWd = (c_aTW_hTWd + smoothSim_aTW_hTWd * pSim_aTW_hTd + smooth_aTW_hTWd * p_aTW_hTd) / (c_hTWd + smoothSim_aTW_hTWd + smooth_aTW_hTWd);
/* 253 */     System.out.println(dependency);
/* 254 */     System.out.println(c_aTW_hTWd + " + " + smoothSim_aTW_hTWd + " * " + pSim_aTW_hTd + " + " + smooth_aTW_hTWd + " * " + p_aTW_hTd);
/* 255 */     System.out.println("--------------------------------  = " + pb_aTW_hTWd);
/* 256 */     System.out.println(c_hTWd + " + " + smoothSim_aTW_hTWd + " + " + smooth_aTW_hTWd);
/* 257 */     System.out.println();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 262 */     score = (this.interp * pb_aTW_hTWd + (1.0D - this.interp) * p_aTW_aT * pb_aT_hTWd) * pb_go_hTWds;
/*     */     
/*     */ 
/*     */ 
/* 266 */     if (this.verbose) {
/* 267 */       NumberFormat nf = NumberFormat.getNumberInstance();
/* 268 */       nf.setMaximumFractionDigits(2);
/* 269 */       System.out.println("  c_aTW_hTWd: " + c_aTW_hTWd + "; c_aT_hTWd: " + c_aT_hTWd + "; c_hTWd: " + c_hTWd);
/* 270 */       System.out.println("  c_aTW_hTd: " + c_aTW_hTd + "; c_aT_hTd: " + c_aT_hTd + "; c_hTd: " + c_hTd);
/* 271 */       System.out.println("  Generated with pb_go_hTWds: " + nf.format(pb_go_hTWds) + " pb_aTW_hTWd: " + nf.format(pb_aTW_hTWd) + " p_aTW_aT: " + nf.format(p_aTW_aT) + " pb_aT_hTWd: " + nf.format(pb_aT_hTWd));
/* 272 */       System.out.println("  NoDist score: " + score);
/*     */     }
/*     */     
/* 275 */     if ((Test.prunePunc) && (pruneTW(aTW))) {
/* 276 */       return 1.0D;
/*     */     }
/*     */     
/* 279 */     if (Double.isNaN(score)) {
/* 280 */       score = 0.0D;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 286 */     if (score < 1.0E-40D) {
/* 287 */       score = 0.0D;
/*     */     }
/*     */     
/* 290 */     return score;
/*     */   }
/*     */   
/*     */ 
/*     */   private double probSimilarWordAvg(IntDependency dep)
/*     */   {
/* 296 */     double regProb = probTB(dep);
/* 297 */     this.statsCounter.incrementCount("total");
/*     */     
/* 299 */     List<Triple<Integer, String, Double>> sim2head = null;
/* 300 */     List<Triple<Integer, String, Double>> sim2arg = null;
/*     */     
/* 302 */     sim2arg = (List)this.simArgMap.get(new Pair(Integer.valueOf(dep.arg.word), stringBasicCategory(dep.arg.tag)));
/* 303 */     sim2head = (List)this.simHeadMap.get(new Pair(Integer.valueOf(dep.head.word), stringBasicCategory(dep.head.tag)));
/*     */     
/* 305 */     if ((sim2head == null) && (sim2arg == null)) {
/* 306 */       return regProb;
/*     */     }
/*     */     
/* 309 */     double sumScores = 0.0D;double sumWeights = 0.0D;
/*     */     
/* 311 */     if (sim2head == null) {
/* 312 */       IntTaggedWord aTW = dep.arg;
/* 313 */       this.statsCounter.incrementCount("aSim");
/* 314 */       for (Triple<Integer, String, Double> simArg : sim2arg)
/*     */       {
/* 316 */         double weight = Math.exp(-50.0D * ((Double)simArg.third).doubleValue());
/* 317 */         int tag = 0; for (int numT = tagNumberer().total(); tag < numT; tag++)
/* 318 */           if (stringBasicCategory(tag).equals(simArg.second))
/*     */           {
/*     */ 
/* 321 */             dep.arg = new IntTaggedWord(((Integer)simArg.first).intValue(), tag);
/* 322 */             double probArg = Math.exp(this.lex.score(dep.arg, 0));
/* 323 */             if (probArg != 0.0D)
/*     */             {
/*     */ 
/* 326 */               sumScores += probTB(dep) * weight / probArg;
/* 327 */               sumWeights += weight;
/*     */             }
/*     */           } }
/* 330 */       dep.arg = aTW;
/* 331 */     } else if (sim2arg == null) {
/* 332 */       IntTaggedWord hTW = dep.head;
/* 333 */       this.statsCounter.incrementCount("hSim");
/* 334 */       for (Triple<Integer, String, Double> simHead : sim2head)
/*     */       {
/* 336 */         double weight = Math.exp(-50.0D * ((Double)simHead.third).doubleValue());
/* 337 */         int tag = 0; for (int numT = tagNumberer().total(); tag < numT; tag++)
/* 338 */           if (stringBasicCategory(tag).equals(simHead.second))
/*     */           {
/*     */ 
/* 341 */             dep.head = new IntTaggedWord(((Integer)simHead.first).intValue(), tag);
/* 342 */             sumScores += probTB(dep) * weight;
/* 343 */             sumWeights += weight;
/*     */           }
/*     */       }
/* 346 */       dep.head = hTW;
/*     */     } else {
/* 348 */       IntTaggedWord hTW = dep.head;
/* 349 */       IntTaggedWord aTW = dep.arg;
/* 350 */       this.statsCounter.incrementCount("hSim");
/* 351 */       this.statsCounter.incrementCount("aSim");
/* 352 */       this.statsCounter.incrementCount("aSim&hSim");
/* 353 */       for (Triple<Integer, String, Double> simArg : sim2arg) {
/* 354 */         int aTag = 0; double probArg; for (int numT = tagNumberer().total(); aTag < numT; aTag++) {
/* 355 */           if (stringBasicCategory(aTag).equals(simArg.second))
/*     */           {
/*     */ 
/* 358 */             dep.arg = new IntTaggedWord(((Integer)simArg.first).intValue(), aTag);
/* 359 */             probArg = Math.exp(this.lex.score(dep.arg, 0));
/* 360 */             if (probArg != 0.0D)
/*     */             {
/*     */ 
/* 363 */               for (Triple<Integer, String, Double> simHead : sim2head)
/* 364 */                 for (int hTag = 0; hTag < numT; hTag++)
/* 365 */                   if (stringBasicCategory(hTag).equals(simHead.second))
/*     */                   {
/*     */ 
/* 368 */                     dep.head = new IntTaggedWord(((Integer)simHead.first).intValue(), aTag);
/*     */                     
/* 370 */                     double weight = Math.exp(-50.0D * ((Double)simHead.third).doubleValue()) * Math.exp(-50.0D * ((Double)simArg.third).doubleValue());
/* 371 */                     sumScores += probTB(dep) * weight / probArg;
/* 372 */                     sumWeights += weight;
/*     */                   } }
/*     */           }
/*     */         }
/*     */       }
/* 377 */       dep.head = hTW;
/* 378 */       dep.arg = aTW;
/*     */     }
/*     */     
/* 381 */     IntTaggedWord aTW = dep.arg;
/* 382 */     dep.arg = wildTW;
/* 383 */     double countHead = this.argCounter.getCount(dep);
/* 384 */     dep.arg = aTW;
/*     */     double simProb;
/*     */     double simProb;
/* 387 */     if (sim2arg == null) {
/* 388 */       simProb = sumScores / sumWeights;
/*     */     } else {
/* 390 */       double probArg = Math.exp(this.lex.score(dep.arg, 0));
/* 391 */       simProb = probArg * sumScores / sumWeights;
/*     */     }
/*     */     
/* 394 */     if (simProb == 0.0D) {
/* 395 */       this.statsCounter.incrementCount("simProbZero");
/*     */     }
/* 397 */     if (regProb == 0.0D)
/*     */     {
/* 399 */       this.statsCounter.incrementCount("regProbZero");
/*     */     }
/* 401 */     double smoothProb = (countHead * regProb + this.simSmooth * simProb) / (countHead + this.simSmooth);
/* 402 */     if (smoothProb == 0.0D)
/*     */     {
/* 404 */       this.statsCounter.incrementCount("smoothProbZero");
/*     */     }
/*     */     
/* 407 */     return smoothProb;
/*     */   }
/*     */   
/*     */   private String stringBasicCategory(int tag) {
/* 411 */     return this.tlp.basicCategory((String)tagNumberer().object(tag));
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\ChineseSimWordAvgDepGrammar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */