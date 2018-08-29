/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.MapLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypedDependency
/*     */   implements Comparable<TypedDependency>
/*     */ {
/*     */   private GrammaticalRelation reln;
/*     */   private TreeGraphNode gov;
/*     */   private TreeGraphNode dep;
/*     */   
/*     */   public TypedDependency(GrammaticalRelation reln, TreeGraphNode gov, TreeGraphNode dep)
/*     */   {
/*  19 */     this.reln = reln;
/*  20 */     this.gov = gov;
/*  21 */     this.dep = dep;
/*     */   }
/*     */   
/*     */   public TypedDependency(Object reln, TreeGraphNode gov, TreeGraphNode dep) {
/*  25 */     this.reln = ((GrammaticalRelation)reln);
/*  26 */     this.gov = gov;
/*  27 */     this.dep = dep;
/*     */   }
/*     */   
/*     */   public GrammaticalRelation reln() {
/*  31 */     return this.reln;
/*     */   }
/*     */   
/*     */   public TreeGraphNode gov() {
/*  35 */     return this.gov;
/*     */   }
/*     */   
/*     */   public TreeGraphNode dep() {
/*  39 */     return this.dep;
/*     */   }
/*     */   
/*     */   public void setReln(GrammaticalRelation reln) {
/*  43 */     this.reln = reln;
/*     */   }
/*     */   
/*     */   public void setGov(TreeGraphNode gov) {
/*  47 */     this.gov = gov;
/*     */   }
/*     */   
/*     */   public void setDep(TreeGraphNode dep) {
/*  51 */     this.dep = dep;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  55 */     if (this == o) {
/*  56 */       return true;
/*     */     }
/*  58 */     if (!(o instanceof TypedDependency)) {
/*  59 */       return false;
/*     */     }
/*  61 */     TypedDependency typedDep = (TypedDependency)o;
/*     */     
/*  63 */     if (this.reln != null ? !this.reln.equals(typedDep.reln) : typedDep.reln != null) {
/*  64 */       return false;
/*     */     }
/*  66 */     if (this.gov != null ? !this.gov.equals(typedDep.gov) : typedDep.gov != null) {
/*  67 */       return false;
/*     */     }
/*  69 */     if (this.dep != null ? !this.dep.equals(typedDep.dep) : typedDep.dep != null) {
/*  70 */       return false;
/*     */     }
/*     */     
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  77 */     int result = this.reln != null ? this.reln.hashCode() : 17;
/*  78 */     result = 29 * result + (this.gov != null ? this.gov.hashCode() : 0);
/*  79 */     result = 29 * result + (this.dep != null ? this.dep.hashCode() : 0);
/*  80 */     return result;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  84 */     if (this.dep.label.get("copy").equals("true")) {
/*  85 */       return this.reln + "(" + this.gov + ", " + this.dep + "')";
/*     */     }
/*  87 */     if (this.gov.label.get("copy").equals("true")) {
/*  88 */       return this.reln + "(" + this.gov + "', " + this.dep + ")";
/*     */     }
/*  90 */     return this.reln + "(" + this.gov + ", " + this.dep + ")";
/*     */   }
/*     */   
/*     */   public int compareTo(TypedDependency tdArg)
/*     */   {
/*  95 */     TreeGraphNode depArg = tdArg.dep();
/*  96 */     TreeGraphNode depThis = dep();
/*  97 */     int indexArg = depArg.index();
/*  98 */     int indexThis = depThis.index();
/*     */     
/* 100 */     if (indexThis > indexArg)
/* 101 */       return 1;
/* 102 */     if (indexThis < indexArg) {
/* 103 */       return -1;
/*     */     }
/* 105 */     return 0;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TypedDependency.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */