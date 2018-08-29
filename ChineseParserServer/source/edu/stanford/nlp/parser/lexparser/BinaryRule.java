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
/*     */ 
/*     */ public class BinaryRule
/*     */   extends Rule
/*     */   implements Serializable, Comparable
/*     */ {
/*  16 */   public int leftChild = -1;
/*  17 */   public int rightChild = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryRule() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryRule(int parent, int leftChild, int rightChild)
/*     */   {
/*  29 */     this.parent = parent;
/*  30 */     this.leftChild = leftChild;
/*  31 */     this.rightChild = rightChild;
/*     */   }
/*     */   
/*     */   public BinaryRule(int parent, int leftChild, int rightChild, double score) {
/*  35 */     this.parent = parent;
/*  36 */     this.leftChild = leftChild;
/*  37 */     this.rightChild = rightChild;
/*  38 */     this.score = ((float)score);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryRule(String s, Numberer n)
/*     */   {
/*  49 */     String[] fields = StringUtils.splitOnCharWithQuoting(s, ' ', '"', '\\');
/*     */     
/*  51 */     this.parent = n.number(fields[0]);
/*  52 */     this.leftChild = n.number(fields[2]);
/*  53 */     this.rightChild = n.number(fields[3]);
/*  54 */     this.score = Float.parseFloat(fields[4]);
/*     */   }
/*     */   
/*  57 */   private int hashCode = -1;
/*     */   
/*  59 */   public int hashCode() { if (this.hashCode < 0) {
/*  60 */       this.hashCode = (this.parent << 16 ^ this.leftChild << 8 ^ this.rightChild);
/*     */     }
/*  62 */     return this.hashCode;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  66 */     if (this == o) {
/*  67 */       return true;
/*     */     }
/*  69 */     if ((o instanceof BinaryRule)) {
/*  70 */       BinaryRule br = (BinaryRule)o;
/*  71 */       if ((this.parent == br.parent) && (this.leftChild == br.leftChild) && (this.rightChild == br.rightChild)) {
/*  72 */         return true;
/*     */       }
/*     */     }
/*  75 */     return false;
/*     */   }
/*     */   
/*  78 */   private static final char[] charsToEscape = { '"' };
/*     */   
/*     */   public String toString()
/*     */   {
/*  82 */     Numberer n = Numberer.getGlobalNumberer("states");
/*  83 */     return "\"" + StringUtils.escapeString(n.object(this.parent).toString(), charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(n.object(this.leftChild).toString(), charsToEscape, '\\') + "\" \"" + StringUtils.escapeString(n.object(this.rightChild).toString(), charsToEscape, '\\') + "\" " + this.score;
/*     */   }
/*     */   
/*  86 */   private transient String cached = null;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*  89 */   public String toStringNoScore() { if (this.cached == null) {
/*  90 */       Numberer n = Numberer.getGlobalNumberer("states");
/*  91 */       this.cached = ("\"" + StringUtils.escapeString(n.object(this.parent).toString(), charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(n.object(this.leftChild).toString(), charsToEscape, '\\') + "\" \"" + StringUtils.escapeString(n.object(this.rightChild).toString(), charsToEscape, '\\'));
/*     */     }
/*  93 */     return this.cached;
/*     */   }
/*     */   
/*     */   public int compareTo(Object o) {
/*  97 */     BinaryRule ur = (BinaryRule)o;
/*  98 */     if (this.parent < ur.parent) {
/*  99 */       return -1;
/*     */     }
/* 101 */     if (this.parent > ur.parent) {
/* 102 */       return 1;
/*     */     }
/* 104 */     if (this.leftChild < ur.leftChild) {
/* 105 */       return -1;
/*     */     }
/* 107 */     if (this.leftChild > ur.leftChild) {
/* 108 */       return 1;
/*     */     }
/* 110 */     if (this.rightChild < ur.rightChild) {
/* 111 */       return -1;
/*     */     }
/* 113 */     if (this.rightChild > ur.rightChild) {
/* 114 */       return 1;
/*     */     }
/* 116 */     return 0;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\BinaryRule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */