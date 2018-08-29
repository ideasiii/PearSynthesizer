/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.ling.CategoryWordTag;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
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
/*     */ public class Train
/*     */ {
/*  25 */   public static int leaveItAll = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  30 */   public static boolean cheatPCFG = false;
/*     */   
/*  32 */   public static boolean markovFactor = false;
/*  33 */   public static int markovOrder = 1;
/*  34 */   public static boolean hSelSplit = false;
/*  35 */   public static int HSEL_CUT = 10;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  40 */   public static boolean markFinalStates = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */   public static int openClassTypesThreshold = 50;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */   public static double fractionBeforeUnseenCounting = 0.5D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean outsideFactor()
/*     */   {
/*  63 */     return !markovFactor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   public static boolean PA = true;
/*     */   
/*     */ 
/*     */ 
/*  73 */   public static boolean gPA = false;
/*     */   
/*  75 */   public static boolean postPA = false;
/*  76 */   public static boolean postGPA = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   public static boolean selectiveSplit = false;
/*     */   
/*  83 */   public static double selectiveSplitCutOff = 0.0D;
/*     */   
/*  85 */   public static boolean selectivePostSplit = false;
/*     */   
/*  87 */   public static double selectivePostSplitCutOff = 0.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   public static boolean postSplitWithBaseCategory = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  98 */   public static boolean sisterAnnotate = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<String> sisterSplitters;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */   public static int markUnary = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 115 */   public static boolean markUnaryTags = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */   public static boolean splitPrePreT = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */   public static boolean tagPA = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 132 */   public static boolean tagSelectiveSplit = false;
/*     */   
/* 134 */   public static double tagSelectiveSplitCutOff = 0.0D;
/*     */   
/* 136 */   public static boolean tagSelectivePostSplit = false;
/*     */   
/* 138 */   public static double tagSelectivePostSplitCutOff = 0.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 143 */   public static boolean rightRec = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 148 */   public static boolean leftRec = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 153 */   public static boolean xOverX = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 158 */   public static boolean collinsPunc = false;
/*     */   
/*     */ 
/*     */ 
/*     */   public static Set<String> splitters;
/*     */   
/*     */ 
/*     */ 
/*     */   public static Set postSplitters;
/*     */   
/*     */ 
/*     */ 
/*     */   public static Set<String> deleteSplitters;
/*     */   
/*     */ 
/*     */ 
/* 174 */   public static int printTreeTransformations = 0;
/*     */   
/*     */   public static PrintWriter printAnnotatedPW;
/*     */   
/*     */   public static PrintWriter printBinarizedPW;
/* 179 */   public static boolean printStates = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */   public static int compactGrammar = 3;
/*     */   
/* 194 */   public static boolean leftToRight = false;
/*     */   
/*     */   public static int compactGrammar() {
/* 197 */     if (markovFactor) {
/* 198 */       return compactGrammar;
/*     */     }
/* 200 */     return 0;
/*     */   }
/*     */   
/* 203 */   public static boolean noTagSplit = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */   public static boolean smoothing = false;
/* 210 */   public static boolean smoothedBound = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */   public static double ruleDiscount = 0.0D;
/*     */   
/*     */ 
/*     */ 
/* 221 */   public static boolean printAnnotatedRuleCounts = false;
/* 222 */   public static boolean printAnnotatedStateCounts = false;
/*     */   
/*     */ 
/* 225 */   public static void display() { System.err.println("Train parameters: smooth=" + smoothing + " PA=" + PA + " GPA=" + gPA + " selSplit=" + selectiveSplit + " (" + selectiveSplitCutOff + (deleteSplitters != null ? "; deleting " + deleteSplitters : "") + ")" + " mUnary=" + markUnary + " mUnaryTags=" + markUnaryTags + " sPPT=" + splitPrePreT + " tagPA=" + tagPA + " tagSelSplit=" + tagSelectiveSplit + " (" + tagSelectiveSplitCutOff + ")" + " rightRec=" + rightRec + " leftRec=" + leftRec + " xOverX=" + xOverX + " collinsPunc=" + collinsPunc + " markov=" + markovFactor + " mOrd=" + markovOrder + " hSelSplit=" + hSelSplit + " (" + HSEL_CUT + ")" + " compactGrammar=" + compactGrammar() + " leaveItAll=" + leaveItAll + " postPA=" + postPA + " postGPA=" + postGPA + " selPSplit=" + selectivePostSplit + " (" + selectivePostSplitCutOff + ")" + " tagSelPSplit=" + tagSelectivePostSplit + " (" + tagSelectivePostSplitCutOff + ")" + " postSplitWithBase=" + postSplitWithBaseCategory + " fractionBeforeUnseenCounting=" + fractionBeforeUnseenCounting + " openClassTypesThreshold=" + openClassTypesThreshold); }
/*     */   
/*     */   public static void printTrainTree(PrintWriter pw, String message, Tree t) {
/*     */     PrintWriter myPW;
/*     */     PrintWriter myPW;
/* 230 */     if (pw == null) {
/* 231 */       myPW = new PrintWriter(System.out, true);
/*     */     } else {
/* 233 */       myPW = pw;
/*     */     }
/* 235 */     if ((message != null) && (pw == null))
/*     */     {
/* 237 */       myPW.println(message);
/*     */     }
/* 239 */     boolean previousState = CategoryWordTag.printWordTag;
/* 240 */     CategoryWordTag.printWordTag = false;
/* 241 */     t.pennPrint(myPW);
/* 242 */     CategoryWordTag.printWordTag = previousState;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Train.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */