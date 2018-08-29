/*     */ package edu.stanford.nlp.trees.international.tuebadz;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.trees.AbstractCollinsHeadFinder;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.util.HashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class TueBaDZHeadFinder
/*     */   extends AbstractCollinsHeadFinder
/*     */ {
/*     */   String left;
/*     */   String right;
/*  16 */   private boolean coordSwitch = false;
/*     */   private final Pattern headMarkedPattern;
/*     */   
/*  19 */   public TueBaDZHeadFinder() { super(new TueBaDZLanguagePack());
/*  20 */     String excluded = String.valueOf(this.tlp.labelAnnotationIntroducingCharacters());
/*  21 */     if (excluded.indexOf("-") >= 0) {
/*  22 */       excluded = "-" + excluded.replaceAll("-", "");
/*     */     }
/*  24 */     this.headMarkedPattern = Pattern.compile("^[^" + excluded + "]*:HD");
/*     */     
/*  26 */     this.nonTerminalInfo = new HashMap();
/*     */     
/*  28 */     this.left = (this.coordSwitch ? "right" : "left");
/*  29 */     this.right = (this.coordSwitch ? "left" : "right");
/*     */     
/*  31 */     this.nonTerminalInfo.put("ROOT", new String[][] { { this.left, "SIMPX" }, { this.left, "NX" }, { this.left, "P" }, { this.left, "PX", "ADVX" }, { this.left, "EN", "EN_ADD" }, { this.left } });
/*  32 */     this.nonTerminalInfo.put("PX", new String[][] { { this.left, "APPR", "APPRART", "PX" } });
/*  33 */     this.nonTerminalInfo.put("NX", new String[][] { { this.right, "NX" }, { this.right, "NE", "NN" }, { this.right, "EN", "EN_ADD", "FX" }, { this.right, "ADJX", "PIS", "ADVX" }, { this.right, "CARD", "TRUNC" }, { this.right } });
/*  34 */     this.nonTerminalInfo.put("FX", new String[][] { { this.right, "FM", "FX" } });
/*  35 */     this.nonTerminalInfo.put("ADJX", new String[][] { { this.right, "ADJX", "ADJA", "ADJD" }, { this.right } });
/*  36 */     this.nonTerminalInfo.put("ADVX", new String[][] { { this.right, "ADVX" } });
/*  37 */     this.nonTerminalInfo.put("DP", new String[][] { { this.left } });
/*  38 */     this.nonTerminalInfo.put("VXFIN", new String[][] { { this.left, "VXFIN" }, { this.right, "VVFIN" } });
/*  39 */     this.nonTerminalInfo.put("VXINF", new String[][] { { this.right, "VXINF" }, { this.right, "VVPP", "VVINF" } });
/*  40 */     this.nonTerminalInfo.put("LV", new String[][] { { this.right } });
/*  41 */     this.nonTerminalInfo.put("C", new String[][] { { this.right, "KOUS" } });
/*  42 */     this.nonTerminalInfo.put("FKOORD", new String[][] { { this.left, "LK", "C" }, { this.right, "FKONJ", "MF", "VC" } });
/*  43 */     this.nonTerminalInfo.put("KOORD", new String[][] { { this.left } });
/*  44 */     this.nonTerminalInfo.put("LK", new String[][] { { this.left } });
/*     */     
/*  46 */     this.nonTerminalInfo.put("MF", new String[][] { { this.left } });
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
/*  61 */     this.nonTerminalInfo.put("MFE", new String[][] { { this.left } });
/*     */     
/*  63 */     this.nonTerminalInfo.put("NF", new String[][] { { this.left } });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */     this.nonTerminalInfo.put("PARORD", new String[][] { { this.left } });
/*     */     
/*  72 */     this.nonTerminalInfo.put("VC", new String[][] { { this.left, "VXINF" } });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */     this.nonTerminalInfo.put("VF", new String[][] { { this.left, "NX", "ADJX", "PX", "ADVX", "EN", "SIMPX" } });
/*     */     
/*  83 */     this.nonTerminalInfo.put("FKONJ", new String[][] { { this.left, "LK" }, { this.right, "VC" }, { this.left, "MF", "NF", "VF" } });
/*     */     
/*  85 */     this.nonTerminalInfo.put("DM", new String[][] { { this.left, "PTKANT" }, { this.left, "ITJ" }, { this.left, "KON", "FM" }, { this.left } });
/*     */     
/*     */ 
/*  88 */     this.nonTerminalInfo.put("P", new String[][] { { this.left, "SIMPX" }, { this.left } });
/*     */     
/*  90 */     this.nonTerminalInfo.put("R", new String[][] { { this.left, "C" }, { this.left, "R" }, { this.right, "VC" } });
/*     */     
/*  92 */     this.nonTerminalInfo.put("SIMPX", new String[][] { { this.left, "LK" }, { this.right, "VC" }, { this.left, "SIMPX" }, { this.left, "C" }, { this.right, "FKOORD" }, { this.right, "MF" }, { this.right } });
/*  93 */     this.nonTerminalInfo.put("EN", new String[][] { { this.left, "NX" } });
/*  94 */     this.nonTerminalInfo.put("EN_ADD", new String[][] { { this.left, "NX" } });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Tree findMarkedHead(Tree t)
/*     */   {
/* 104 */     Tree[] kids = t.children();
/* 105 */     int i = 0; for (int n = kids.length; i < n; i++) {
/* 106 */       if (this.headMarkedPattern.matcher(kids[i].label().value()).find())
/*     */       {
/* 108 */         return kids[i];
/*     */       }
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\tuebadz\TueBaDZHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */