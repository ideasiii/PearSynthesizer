/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.AbstractMapLabel;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import edu.stanford.nlp.util.XMLUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnnamedDependency
/*     */   implements Dependency
/*     */ {
/*     */   private Label regent;
/*     */   private Label dependent;
/*     */   private static final long serialVersionUID = 5L;
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  24 */     return this.regent.hashCode() ^ this.dependent.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  28 */     if (this == o) {
/*  29 */       return true;
/*     */     }
/*  31 */     if ((o instanceof UnnamedDependency)) {
/*  32 */       UnnamedDependency d = (UnnamedDependency)o;
/*  33 */       return (governor().equals(d.governor())) && (dependent().equals(d.dependent()));
/*     */     }
/*  35 */     return false;
/*     */   }
/*     */   
/*     */   public boolean equalsIgnoreName(Object o)
/*     */   {
/*  40 */     if (this == o) {
/*  41 */       return true;
/*     */     }
/*  43 */     if ((o instanceof Dependency)) {
/*  44 */       Dependency d = (Dependency)o;
/*  45 */       return (governor().equals(d.governor())) && (dependent().equals(d.dependent()));
/*     */     }
/*  47 */     return false;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  51 */     return this.regent + " --> " + this.dependent;
/*     */   }
/*     */   
/*     */   private static String getIndexStrOrEmpty(Label lab) {
/*  55 */     String ans = "";
/*  56 */     if ((lab instanceof AbstractMapLabel)) {
/*  57 */       AbstractMapLabel aml = (AbstractMapLabel)lab;
/*  58 */       int idx = aml.index();
/*  59 */       if (idx >= 0) {
/*  60 */         ans = " idx=\"" + idx + "\"";
/*     */       }
/*     */     }
/*  63 */     return ans;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString(String format)
/*     */   {
/*  72 */     if ("xml".equals(format)) {
/*  73 */       String govIdxStr = getIndexStrOrEmpty(governor());
/*  74 */       String depIdxStr = getIndexStrOrEmpty(dependent());
/*  75 */       return "  <dep>\n    <governor" + govIdxStr + ">" + XMLUtils.escapeXML(governor().value()) + "</governor>\n    <dependent" + depIdxStr + ">" + XMLUtils.escapeXML(dependent().value()) + "</dependent>\n  </dep>"; }
/*  76 */     if ("predicate".equals(format)) {
/*  77 */       return "dep(" + governor() + "," + dependent() + ")";
/*     */     }
/*  79 */     return toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public UnnamedDependency(String regent, String dependent)
/*     */   {
/*  85 */     this(new StringLabel(regent), new StringLabel(dependent));
/*     */   }
/*     */   
/*     */   public UnnamedDependency(String regent, int regentIndex, String dependent, int dependentIndex) {
/*  89 */     this(regent, regentIndex, regentIndex + 1, dependent, dependentIndex, dependentIndex + 1);
/*     */   }
/*     */   
/*     */   public UnnamedDependency(String regent, int regentStartIndex, int regentEndIndex, String dependent, int depStartIndex, int depEndIndex) {
/*  93 */     this(new LabeledConstituent(regentStartIndex, regentEndIndex, regent), new LabeledConstituent(depStartIndex, depEndIndex, dependent));
/*     */   }
/*     */   
/*     */   public UnnamedDependency(Label regent, Label dependent) {
/*  97 */     if ((regent == null) || (dependent == null)) {
/*  98 */       throw new IllegalArgumentException("governor or dependent cannot be null");
/*     */     }
/* 100 */     this.regent = regent;
/* 101 */     this.dependent = dependent;
/*     */   }
/*     */   
/*     */   public Label governor() {
/* 105 */     return this.regent;
/*     */   }
/*     */   
/*     */   public Label dependent() {
/* 109 */     return this.dependent;
/*     */   }
/*     */   
/*     */   public Object name() {
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   public DependencyFactory dependencyFactory() {
/* 117 */     return DependencyFactoryHolder.df;
/*     */   }
/*     */   
/*     */   public static DependencyFactory factory() {
/* 121 */     return DependencyFactoryHolder.df;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class DependencyFactoryHolder
/*     */   {
/* 127 */     private static final DependencyFactory df = new UnnamedDependency.UnnamedDependencyFactory();
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
/*     */   private static class UnnamedDependencyFactory
/*     */     implements DependencyFactory
/*     */   {
/*     */     public Dependency newDependency(Label regent, Label dependent)
/*     */     {
/* 146 */       return newDependency(regent, dependent, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Dependency newDependency(Label regent, Label dependent, Object name)
/*     */     {
/* 153 */       return new UnnamedDependency(regent, dependent);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\UnnamedDependency.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */