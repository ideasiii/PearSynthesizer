/*     */ package edu.stanford.nlp.trees.international.negra;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.LabelFactory;
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NegraLabel
/*     */   extends StringLabel
/*     */ {
/*     */   public static final String FEATURE_SEP = "#";
/*     */   private String edge;
/*     */   private Map<String, String> features;
/*     */   
/*     */   public LabelFactory labelFactory()
/*     */   {
/*  25 */     return new NegraLabelFactory(null);
/*     */   }
/*     */   
/*     */   private static class NegraLabelFactory implements LabelFactory
/*     */   {
/*     */     public Label newLabel(String labelStr) {
/*  31 */       return new NegraLabel(labelStr);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Label newLabel(String labelStr, int options)
/*     */     {
/*  41 */       return newLabel(labelStr);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Label newLabelFromString(String encodedLabelStr)
/*     */     {
/*  50 */       return newLabel(encodedLabelStr);
/*     */     }
/*     */     
/*     */ 
/*     */     public Label newLabel(Label oldLabel)
/*     */     {
/*     */       NegraLabel result;
/*     */       
/*     */       NegraLabel result;
/*     */       
/*  60 */       if ((oldLabel instanceof NegraLabel)) {
/*  61 */         NegraLabel l = (NegraLabel)oldLabel;
/*  62 */         result = new NegraLabel(l.value(), l.getEdge(), new HashMap());
/*  63 */         for (Map.Entry<String, String> e : l.features.entrySet()) {
/*  64 */           result.features.put(e.getKey(), e.getValue());
/*     */         }
/*     */       } else {
/*  67 */         result = new NegraLabel(oldLabel.value());
/*     */       }
/*  69 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setEdge(String edge)
/*     */   {
/*  76 */     this.edge = edge;
/*     */   }
/*     */   
/*     */   public String getEdge() {
/*  80 */     return this.edge;
/*     */   }
/*     */   
/*     */   private NegraLabel() {}
/*     */   
/*     */   public NegraLabel(String str)
/*     */   {
/*  87 */     this(str, new HashMap());
/*     */   }
/*     */   
/*     */   public NegraLabel(String str, Map<String, String> features) {
/*  91 */     this(str, null, features);
/*     */   }
/*     */   
/*     */   public NegraLabel(String str, String edge, Map<String, String> features) {
/*  95 */     super(str);
/*  96 */     this.edge = edge;
/*  97 */     this.features = features;
/*     */   }
/*     */   
/*     */   public void setFeatureValue(String feature, String value) {
/* 101 */     this.features.put(feature, value);
/*     */   }
/*     */   
/*     */   public String featureValue(String feature) {
/* 105 */     return (String)this.features.get(feature);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 110 */     String str = value();
/* 111 */     if (this.edge != null) {
/* 112 */       str = str + "->" + getEdge();
/*     */     }
/* 114 */     if (!this.features.isEmpty()) {
/* 115 */       str = str + "." + this.features.toString();
/*     */     }
/* 117 */     return str;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\negra\NegraLabel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */