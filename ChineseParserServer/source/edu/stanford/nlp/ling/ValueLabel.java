/*     */ package edu.stanford.nlp.ling;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public abstract class ValueLabel
/*     */   implements Label, Comparable<ValueLabel>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1413303679077285530L;
/*     */   
/*     */   public String value()
/*     */   {
/*  34 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(String value) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  55 */     String val = value();
/*  56 */     return val == null ? "" : val;
/*     */   }
/*     */   
/*     */   public void setFromString(String labelStr)
/*     */   {
/*  61 */     throw new UnsupportedOperationException();
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
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  78 */     String val = value();
/*  79 */     return ((obj instanceof ValueLabel)) && (val == null ? ((ValueLabel)obj).value() == null : val.equals(((ValueLabel)obj).value()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  89 */     String val = value();
/*  90 */     return val == null ? 3 : val.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(ValueLabel valueLabel)
/*     */   {
/* 101 */     return value().compareTo(valueLabel.value());
/*     */   }
/*     */   
/*     */   public abstract LabelFactory labelFactory();
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\ValueLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */