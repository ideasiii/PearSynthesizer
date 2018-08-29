/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnaryRule
/*     */   extends Rule
/*     */   implements Serializable, Comparable
/*     */ {
/*  15 */   public int child = -1;
/*     */   
/*     */ 
/*     */   public UnaryRule() {}
/*     */   
/*     */ 
/*     */   public UnaryRule(int parent, int child)
/*     */   {
/*  23 */     this.parent = parent;
/*  24 */     this.child = child;
/*     */   }
/*     */   
/*     */   public UnaryRule(int parent, int child, double score) {
/*  28 */     this.parent = parent;
/*  29 */     this.child = child;
/*  30 */     this.score = ((float)score);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public UnaryRule(String s, Numberer n)
/*     */   {
/*  37 */     String[] fields = StringUtils.splitOnCharWithQuoting(s, ' ', '"', '\\');
/*     */     
/*  39 */     this.parent = n.number(fields[0]);
/*  40 */     this.child = n.number(fields[2]);
/*  41 */     this.score = Float.parseFloat(fields[3]);
/*     */   }
/*     */   
/*     */   public boolean isUnary() {
/*  45 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  49 */     return this.parent << 16 ^ this.child;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  59 */     if (this == o) {
/*  60 */       return true;
/*     */     }
/*  62 */     if ((o instanceof UnaryRule)) {
/*  63 */       UnaryRule ur = (UnaryRule)o;
/*  64 */       if ((this.parent == ur.parent) && (this.child == ur.child)) {
/*  65 */         return true;
/*     */       }
/*     */     }
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   public int compareTo(Object o) {
/*  72 */     UnaryRule ur = (UnaryRule)o;
/*  73 */     if (this.parent < ur.parent) {
/*  74 */       return -1;
/*     */     }
/*  76 */     if (this.parent > ur.parent) {
/*  77 */       return 1;
/*     */     }
/*  79 */     if (this.child < ur.child) {
/*  80 */       return -1;
/*     */     }
/*  82 */     if (this.child > ur.child) {
/*  83 */       return 1;
/*     */     }
/*  85 */     return 0;
/*     */   }
/*     */   
/*  88 */   private static final char[] charsToEscape = { '"' };
/*     */   
/*     */   public String toString() {
/*  91 */     Numberer n = Numberer.getGlobalNumberer("states");
/*  92 */     return "\"" + StringUtils.escapeString(n.object(this.parent).toString(), charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(n.object(this.child).toString(), charsToEscape, '\\') + "\" " + this.score;
/*     */   }
/*     */   
/*  95 */   private transient String cached = null;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*  98 */   public String toStringNoScore() { if (this.cached == null) {
/*  99 */       Numberer n = Numberer.getGlobalNumberer("states");
/* 100 */       this.cached = ("\"" + StringUtils.escapeString(n.object(this.parent).toString(), charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(n.object(this.child).toString(), charsToEscape, '\\'));
/*     */     }
/* 102 */     return this.cached;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\UnaryRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */