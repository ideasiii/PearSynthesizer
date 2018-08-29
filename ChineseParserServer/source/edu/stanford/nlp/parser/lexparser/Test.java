/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.trees.TreePrint;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
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
/*     */ public class Test
/*     */ {
/*  24 */   public static boolean noRecoveryTagging = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  29 */   public static boolean doRecovery = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  34 */   public static boolean useN5 = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */   public static boolean useFastFactored = false;
/*     */   
/*     */ 
/*     */ 
/*  47 */   public static boolean iterativeCKY = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   public static int maxLength = 559038737;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   public static int MAX_ITEMS = 200000;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   public static double unseenSmooth = -1.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  74 */   public static boolean increasingLength = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  79 */   public static boolean preTag = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  84 */   public static boolean forceTags = preTag;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */   public static boolean evalb = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  95 */   public static boolean verbose = false;
/*     */   
/*     */ 
/*     */   public static final boolean exhaustiveTest = false;
/*     */   
/*     */   public static final boolean pcfgThreshold = false;
/*     */   
/*     */   public static final double pcfgThresholdValue = -2.0D;
/*     */   
/* 104 */   public static boolean printAllBestParses = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */   public static double depWeight = 1.0D;
/* 111 */   public static boolean prunePunc = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean addMissingFinalPunctuation;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */   public static String outputFormat = "penn";
/* 125 */   public static String outputFormatOptions = "";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean writeOutputFiles;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String outputFilesDirectory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String outputFilesExtension;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */   public static int maxSpanForTags = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */   public static boolean lengthNormalization = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 166 */   public static List<Constraint> constraints = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */   public static boolean sample = false;
/*     */   
/*     */ 
/* 175 */   public static int printPCFGkBest = 0;
/*     */   
/*     */ 
/* 178 */   public static int printFactoredKGood = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 203 */   public static Properties evals = new Properties();
/* 204 */   static { evals.setProperty("pcfgLB", "true");
/* 205 */     evals.setProperty("depDA", "true");
/* 206 */     evals.setProperty("factLB", "true");
/* 207 */     evals.setProperty("factTA", "true");
/* 208 */     evals.setProperty("summary", "true");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 214 */   public static int fastFactoredCandidateMultiplier = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 219 */   public static int fastFactoredCandidateAddend = 50;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TreePrint treePrint(TreebankLangParserParams tlpParams)
/*     */   {
/* 234 */     TreebankLanguagePack tlp = tlpParams.treebankLanguagePack();
/* 235 */     return new TreePrint(outputFormat, outputFormatOptions, tlp, tlpParams.headFinder());
/*     */   }
/*     */   
/*     */   public static void display()
/*     */   {
/* 240 */     String str = "Test parameters maxLength=" + maxLength + " preTag=" + preTag + " outputFormat=" + outputFormat + " outputFormatOptions=" + outputFormatOptions + " printAllBestParses=" + printAllBestParses;
/* 241 */     System.err.println(str);
/*     */   }
/*     */   
/*     */   public static class Constraint
/*     */   {
/*     */     public int start;
/*     */     public int end;
/*     */     public Pattern state;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Test.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */