/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.util.Scored;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ public abstract class Constituent
/*     */   implements Labeled, Scored, Label
/*     */ {
/*     */   public abstract int start();
/*     */   
/*     */   public abstract void setStart(int paramInt);
/*     */   
/*     */   public abstract int end();
/*     */   
/*     */   public abstract void setEnd(int paramInt);
/*     */   
/*     */   public Label label()
/*     */   {
/*  56 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLabel(Label label) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection labels()
/*     */   {
/*  73 */     return Collections.singletonList(label());
/*     */   }
/*     */   
/*     */   public void setLabels(Collection labels)
/*     */   {
/*  78 */     throw new UnsupportedOperationException("Constituent can't be multilabeled");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double score()
/*     */   {
/*  86 */     return NaN.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScore(double score) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 105 */     Label lab = label();
/* 106 */     StringBuffer sb; StringBuffer sb; if (lab != null) {
/* 107 */       sb = new StringBuffer(lab.toString());
/*     */     } else {
/* 109 */       sb = new StringBuffer();
/*     */     }
/* 111 */     sb.append("(").append(start()).append(",").append(end()).append(")");
/* 112 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 120 */     return end() - start();
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
/* 148 */     if ((obj instanceof Constituent)) {
/* 149 */       Constituent c = (Constituent)obj;
/*     */       
/*     */ 
/*     */ 
/* 153 */       if ((start() == c.start()) && (end() == c.end())) {
/* 154 */         Label lab1 = label();
/* 155 */         Label lab2 = c.label();
/* 156 */         if (lab1 == null) {
/* 157 */           return lab2 == null;
/*     */         }
/* 159 */         return lab1.equals(lab2);
/*     */       }
/*     */     }
/*     */     
/* 163 */     return false;
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
/*     */   public int hashCode()
/*     */   {
/* 176 */     int hash = start() << 16 | end();
/* 177 */     Label lab = label();
/* 178 */     return lab == null ? hash : hash ^ lab.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean crosses(Constituent c)
/*     */   {
/* 190 */     return ((start() < c.start()) && (c.start() < end()) && (end() < c.end())) || ((c.start() < start()) && (start() < c.end()) && (c.end() < end()));
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
/*     */   public boolean crosses(Collection<Constituent> constColl)
/*     */   {
/* 205 */     for (Constituent c : constColl) {
/* 206 */       if (crosses(c)) {
/* 207 */         return true;
/*     */       }
/*     */     }
/* 210 */     return false;
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
/*     */   public boolean contains(Constituent c)
/*     */   {
/* 223 */     return (start() <= c.start()) && (end() >= c.end());
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
/*     */   public String value()
/*     */   {
/* 236 */     Label lab = label();
/* 237 */     if (lab == null) {
/* 238 */       return null;
/*     */     }
/* 240 */     return lab.value();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setValue(String value)
/*     */   {
/* 251 */     Label lab = label();
/* 252 */     if (lab != null) {
/* 253 */       lab.setValue(value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFromString(String labelStr)
/*     */   {
/* 266 */     Label lab = label();
/* 267 */     if (lab != null) {
/* 268 */       lab.setFromString(labelStr);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toSentenceString(Sentence s)
/*     */   {
/* 280 */     StringBuilder sb = new StringBuilder();
/* 281 */     int wordNum = start(); for (int end = end(); wordNum <= end; wordNum++) {
/* 282 */       sb.append(s.get(wordNum));
/* 283 */       if (wordNum != end) {
/* 284 */         sb.append(" ");
/*     */       }
/*     */     }
/* 287 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\Constituent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */