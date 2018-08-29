/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.Interner;
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
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
/*     */ public abstract class AbstractDependencyGrammar
/*     */   implements DependencyGrammar
/*     */ {
/*     */   protected TagProjection tagProjection;
/*     */   private static Numberer tagNumberer;
/*     */   private static Numberer wordNumberer;
/*     */   protected int numTagBins;
/*     */   protected int[] tagBin;
/*     */   protected TreebankLanguagePack tlp;
/*     */   protected boolean directional;
/*     */   protected boolean useDistance;
/*     */   protected boolean useCoarseDistance;
/*  42 */   protected static final IntTaggedWord stopTW = new IntTaggedWord(-2, -2);
/*  43 */   protected static final IntTaggedWord wildTW = new IntTaggedWord(-1, -1);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   private transient IntDependency tempDependency = new IntDependency(-2, -2, -2, -2, false, 0);
/*     */   
/*  50 */   private transient IntDependency internTempDependency = null;
/*  51 */   protected transient Map<IntDependency, IntDependency> expandDependencyMap = new HashMap();
/*     */   
/*     */   private static final boolean DEBUG = false;
/*     */   private static final long serialVersionUID = 2L;
/*     */   
/*     */   public AbstractDependencyGrammar(TreebankLanguagePack tlp, TagProjection tagProjection, boolean directional, boolean useDistance, boolean useCoarseDistance)
/*     */   {
/*  58 */     this.tlp = tlp;
/*  59 */     this.tagProjection = tagProjection;
/*  60 */     this.directional = directional;
/*  61 */     this.useDistance = useDistance;
/*  62 */     this.useCoarseDistance = useCoarseDistance;
/*  63 */     initTagBins();
/*     */   }
/*     */   
/*     */   protected static Numberer tagNumberer() {
/*  67 */     if (tagNumberer == null) {
/*  68 */       tagNumberer = Numberer.getGlobalNumberer("tags");
/*     */     }
/*  70 */     return tagNumberer;
/*     */   }
/*     */   
/*     */   protected static Numberer wordNumberer() {
/*  74 */     if (wordNumberer == null) {
/*  75 */       wordNumberer = Numberer.getGlobalNumberer("words");
/*     */     }
/*  77 */     return wordNumberer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void tune(Collection<Tree> trees) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public int numTagBins()
/*     */   {
/*  88 */     return this.numTagBins;
/*     */   }
/*     */   
/*     */   public int tagBin(int tag) {
/*  92 */     if (tag < 0) {
/*  93 */       return tag;
/*     */     }
/*  95 */     return this.tagBin[tag];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean rootTW(IntTaggedWord rTW)
/*     */   {
/* 104 */     return rTW.tag == tagNumberer().number(".$$.");
/*     */   }
/*     */   
/*     */   protected short valenceBin(int distance) {
/* 108 */     if (!this.useDistance) {
/* 109 */       return 0;
/*     */     }
/* 111 */     if (distance < 0) {
/* 112 */       return -1;
/*     */     }
/* 114 */     if (distance == 0) {
/* 115 */       return 0;
/*     */     }
/* 117 */     return 1;
/*     */   }
/*     */   
/*     */   public int numDistBins() {
/* 121 */     return this.useCoarseDistance ? 4 : 5;
/*     */   }
/*     */   
/*     */   public short distanceBin(int distance) {
/* 125 */     if (!this.useDistance)
/* 126 */       return 0;
/* 127 */     if (this.useCoarseDistance) {
/* 128 */       return coarseDistanceBin(distance);
/*     */     }
/* 130 */     return regDistanceBin(distance);
/*     */   }
/*     */   
/*     */   public static short regDistanceBin(int distance)
/*     */   {
/* 135 */     if (distance <= 0)
/* 136 */       return 0;
/* 137 */     if (distance <= 1)
/* 138 */       return 1;
/* 139 */     if (distance <= 5)
/* 140 */       return 2;
/* 141 */     if (distance <= 10) {
/* 142 */       return 3;
/*     */     }
/* 144 */     return 4;
/*     */   }
/*     */   
/*     */   public static short coarseDistanceBin(int distance)
/*     */   {
/* 149 */     if (distance <= 0)
/* 150 */       return 0;
/* 151 */     if (distance <= 2)
/* 152 */       return 1;
/* 153 */     if (distance <= 5) {
/* 154 */       return 2;
/*     */     }
/* 156 */     return 3;
/*     */   }
/*     */   
/*     */   protected void initTagBins()
/*     */   {
/* 161 */     Numberer tagBinNumberer = new Numberer();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 166 */     this.tagBin = new int[tagNumberer().total()];
/* 167 */     for (int t = 0; t < this.tagBin.length; t++) {
/* 168 */       String tagStr = (String)tagNumberer().object(t);
/*     */       String binStr;
/* 170 */       String binStr; if (this.tagProjection == null) {
/* 171 */         binStr = tagStr;
/*     */       } else {
/* 173 */         binStr = this.tagProjection.project(tagStr);
/*     */       }
/* 175 */       this.tagBin[t] = tagBinNumberer.number(binStr);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 181 */     this.numTagBins = tagBinNumberer.total();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double score(IntDependency dependency)
/*     */   {
/* 190 */     short hTBackup = dependency.head.tag;
/* 191 */     short aTBackup = dependency.arg.tag;
/*     */     
/* 193 */     dependency.head.tag = ((short)tagBin(dependency.head.tag));
/* 194 */     dependency.arg.tag = ((short)tagBin(dependency.arg.tag));
/*     */     
/* 196 */     double s = scoreTB(dependency);
/*     */     
/* 198 */     dependency.head.tag = hTBackup;
/* 199 */     dependency.arg.tag = aTBackup;
/*     */     
/* 201 */     return s;
/*     */   }
/*     */   
/*     */   public double score(int headWord, int headTag, int argWord, int argTag, boolean leftHeaded, int dist) {
/* 205 */     this.tempDependency.head.word = headWord;
/* 206 */     this.tempDependency.head.tag = ((short)headTag);
/* 207 */     this.tempDependency.arg.word = argWord;
/* 208 */     this.tempDependency.arg.tag = ((short)argTag);
/* 209 */     this.tempDependency.leftHeaded = leftHeaded;
/* 210 */     this.tempDependency.distance = ((short)dist);
/* 211 */     return score(this.tempDependency);
/*     */   }
/*     */   
/*     */   public double scoreTB(int headWord, int headTag, int argWord, int argTag, boolean leftHeaded, int dist) {
/* 215 */     this.tempDependency.head.word = headWord;
/* 216 */     this.tempDependency.head.tag = ((short)headTag);
/* 217 */     this.tempDependency.arg.word = argWord;
/* 218 */     this.tempDependency.arg.tag = ((short)argTag);
/* 219 */     this.tempDependency.leftHeaded = leftHeaded;
/* 220 */     this.tempDependency.distance = ((short)dist);
/* 221 */     return scoreTB(this.tempDependency);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
/*     */   {
/* 226 */     ois.defaultReadObject();
/*     */     
/* 228 */     this.tempDependency = new IntDependency(-2, -2, -2, -2, false, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readData(BufferedReader in)
/*     */     throws IOException
/*     */   {
/* 237 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeData(PrintWriter out)
/*     */     throws IOException
/*     */   {
/* 246 */     throw new UnsupportedOperationException();
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
/*     */   protected IntDependency intern(IntTaggedWord headTW, IntTaggedWord argTW, boolean leftHeaded, short dist)
/*     */   {
/* 260 */     Map<IntDependency, IntDependency> map = this.expandDependencyMap;
/* 261 */     if (this.internTempDependency == null) {
/* 262 */       this.internTempDependency = new IntDependency();
/*     */     }
/* 264 */     this.internTempDependency.head = ((IntTaggedWord)Interner.globalIntern(headTW));
/* 265 */     this.internTempDependency.arg = ((IntTaggedWord)Interner.globalIntern(argTW));
/* 266 */     this.internTempDependency.leftHeaded = leftHeaded;
/* 267 */     this.internTempDependency.distance = dist;
/* 268 */     IntDependency returnDependency = this.internTempDependency;
/* 269 */     if (map != null) {
/* 270 */       returnDependency = (IntDependency)map.get(this.internTempDependency);
/* 271 */       if (returnDependency == null) {
/* 272 */         map.put(this.internTempDependency, this.internTempDependency);
/* 273 */         returnDependency = this.internTempDependency;
/*     */       }
/*     */     }
/* 276 */     if (returnDependency == this.internTempDependency) {
/* 277 */       this.internTempDependency = null;
/*     */     }
/* 279 */     return returnDependency;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\AbstractDependencyGrammar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */