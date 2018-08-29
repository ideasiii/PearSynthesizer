/*     */ package edu.stanford.nlp.trees.international.negra;
/*     */ 
/*     */ import edu.stanford.nlp.trees.AbstractCollinsHeadFinder;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import java.util.HashMap;
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
/*     */ public class NegraHeadFinder
/*     */   extends AbstractCollinsHeadFinder
/*     */ {
/*     */   public static HeadFinder negraSemanticHeadFinder()
/*     */   {
/*  24 */     NegraHeadFinder result = new NegraHeadFinder();
/*  25 */     result.nonTerminalInfo.put("S", new String[][] { { result.right, "VVFIN", "VVIMP" }, { "right", "VP", "CVP" }, { "right", "VMFIN", "VAFIN", "VAIMP" }, { "right", "S", "CS" } });
/*  26 */     result.nonTerminalInfo.put("VP", new String[][] { { "right", "VVINF", "VVIZU", "VVPP" }, { result.right, "VZ", "VAINF", "VMINF", "VMPP", "VAPP", "PP" } });
/*  27 */     result.nonTerminalInfo.put("VZ", new String[][] { { result.right, "VVINF", "VAINF", "VMINF", "VVFIN", "VVIZU" } });
/*  28 */     return result;
/*     */   }
/*     */   
/*  31 */   private boolean coordSwitch = false;
/*     */   String left;
/*     */   
/*  34 */   public NegraHeadFinder() { this(new NegraPennLanguagePack()); }
/*     */   
/*     */ 
/*     */   String right;
/*     */   
/*     */   public NegraHeadFinder(TreebankLanguagePack tlp)
/*     */   {
/*  41 */     super(tlp);
/*     */     
/*  43 */     this.nonTerminalInfo = new HashMap();
/*     */     
/*  45 */     this.left = (this.coordSwitch ? "right" : "left");
/*  46 */     this.right = (this.coordSwitch ? "left" : "right");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */     this.nonTerminalInfo.put("S", new String[][] { { this.left, "PRELS" } });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */     this.nonTerminalInfo.put("NUR", new String[][] { { this.left, "S" } });
/*     */     
/*     */ 
/*  63 */     this.nonTerminalInfo.put("ROOT", new String[][] { { this.left, "S", "CS", "VP", "CVP", "NP", "XY", "CNP", "AVP", "CAVP" } });
/*     */     
/*     */ 
/*  66 */     this.nonTerminalInfo.put("NP", new String[][] { { this.right, "NN", "NE", "MPN", "NP", "CNP", "PN", "CAR" } });
/*  67 */     this.nonTerminalInfo.put("AP", new String[][] { { this.right, "ADJD", "ADJA", "CAP", "AA", "ADV" } });
/*  68 */     this.nonTerminalInfo.put("PP", new String[][] { { this.left, "KOKOM", "APPR", "PROAV" } });
/*     */     
/*  70 */     this.nonTerminalInfo.put("S", new String[][] { { this.right, "VMFIN", "VVFIN", "VAFIN", "VVIMP", "VAIMP" }, { "right", "VP", "CVP" }, { "right", "S", "CS" } });
/*  71 */     this.nonTerminalInfo.put("VP", new String[][] { { this.right, "VZ", "VAINF", "VMINF", "VVINF", "VVIZU", "VVPP", "VMPP", "VAPP", "PP" } });
/*  72 */     this.nonTerminalInfo.put("VZ", new String[][] { { this.left, "PRTZU", "APPR", "PTKZU" } });
/*  73 */     this.nonTerminalInfo.put("CO", new String[][] { { this.left } });
/*  74 */     this.nonTerminalInfo.put("AVP", new String[][] { { this.right, "ADV", "AVP", "ADJD", "PROAV", "PP" } });
/*  75 */     this.nonTerminalInfo.put("AA", new String[][] { { this.right, "ADJD", "ADJA" } });
/*  76 */     this.nonTerminalInfo.put("CNP", new String[][] { { this.right, "NN", "NE", "MPN", "NP", "CNP", "PN", "CAR" } });
/*  77 */     this.nonTerminalInfo.put("CAP", new String[][] { { this.right, "ADJD", "ADJA", "CAP", "AA", "ADV" } });
/*  78 */     this.nonTerminalInfo.put("CPP", new String[][] { { this.right, "APPR", "PROAV", "PP", "CPP" } });
/*  79 */     this.nonTerminalInfo.put("CS", new String[][] { { this.right, "S", "CS" } });
/*  80 */     this.nonTerminalInfo.put("CVP", new String[][] { { this.right, "VP", "CVP" } });
/*  81 */     this.nonTerminalInfo.put("CVZ", new String[][] { { this.right, "VZ" } });
/*  82 */     this.nonTerminalInfo.put("CAVP", new String[][] { { this.right, "ADV", "AVP", "ADJD", "PWAV", "APPR", "PTKVZ" } });
/*  83 */     this.nonTerminalInfo.put("MPN", new String[][] { { this.right, "NE", "FM", "CARD" } });
/*  84 */     this.nonTerminalInfo.put("NM", new String[][] { { this.right, "CARD", "NN" } });
/*  85 */     this.nonTerminalInfo.put("CAC", new String[][] { { this.right, "APPR", "AVP" } });
/*  86 */     this.nonTerminalInfo.put("CH", new String[][] { { this.right } });
/*  87 */     this.nonTerminalInfo.put("MTA", new String[][] { { this.right, "ADJA", "ADJD", "NN" } });
/*  88 */     this.nonTerminalInfo.put("CCP", new String[][] { { this.right, "AVP" } });
/*  89 */     this.nonTerminalInfo.put("DL", new String[][] { { this.left } });
/*  90 */     this.nonTerminalInfo.put("ISU", new String[][] { { this.right } });
/*  91 */     this.nonTerminalInfo.put("QL", new String[][] { { this.right } });
/*     */     
/*  93 */     this.nonTerminalInfo.put("--", new String[][] { { this.right, "PP" } });
/*     */     
/*     */ 
/*  96 */     this.nonTerminalInfo.put("CD", new String[][] { { this.right, "CD" } });
/*  97 */     this.nonTerminalInfo.put("NN", new String[][] { { this.right, "NN" } });
/*  98 */     this.nonTerminalInfo.put("NR", new String[][] { { this.right, "NR" } });
/*     */   }
/*     */   
/*     */ 
/*     */   protected Tree findMarkedHead(Tree[] kids)
/*     */   {
/* 104 */     int i = 0; for (int n = kids.length; i < n; i++) {
/* 105 */       if (((kids[i].label() instanceof NegraLabel)) && (((NegraLabel)kids[i].label()).getEdge() != null) && (((NegraLabel)kids[i].label()).getEdge().equals("HD")))
/*     */       {
/* 107 */         return kids[i];
/*     */       }
/*     */     }
/* 110 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\negra\NegraHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */