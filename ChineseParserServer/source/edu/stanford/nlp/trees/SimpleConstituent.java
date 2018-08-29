/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.LabelFactory;
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
/*     */ public class SimpleConstituent
/*     */   extends Constituent
/*     */ {
/*     */   private int start;
/*     */   private int end;
/*     */   
/*     */   public SimpleConstituent() {}
/*     */   
/*     */   public SimpleConstituent(int start, int end)
/*     */   {
/*  45 */     this.start = start;
/*  46 */     this.end = end;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int start()
/*     */   {
/*  54 */     return this.start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStart(int start)
/*     */   {
/*  62 */     this.start = start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int end()
/*     */   {
/*  70 */     return this.end;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnd(int end)
/*     */   {
/*  78 */     this.end = end;
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
/*     */   private static class SimpleConstituentLabelFactory
/*     */     implements LabelFactory
/*     */   {
/*     */     public Label newLabel(String labelStr)
/*     */     {
/*  95 */       return new SimpleConstituent(0, 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Label newLabel(String labelStr, int options)
/*     */     {
/* 107 */       return newLabel(labelStr);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Label newLabelFromString(String labelStr)
/*     */     {
/* 118 */       return newLabel(labelStr);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Label newLabel(Label oldLabel)
/*     */     {
/* 129 */       return new SimpleConstituent(0, 0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class LabelFactoryHolder
/*     */   {
/* 137 */     static final LabelFactory lf = new SimpleConstituent.SimpleConstituentLabelFactory(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LabelFactory labelFactory()
/*     */   {
/* 148 */     return LabelFactoryHolder.lf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ConstituentFactoryHolder
/*     */   {
/*     */     private static class SimpleConstituentFactory
/*     */       implements ConstituentFactory
/*     */     {
/*     */       public Constituent newConstituent(int start, int end)
/*     */       {
/* 162 */         return new SimpleConstituent(start, end);
/*     */       }
/*     */       
/*     */       public Constituent newConstituent(int start, int end, Label label, double score) {
/* 166 */         return new SimpleConstituent(start, end);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 171 */     static final ConstituentFactory cf = new SimpleConstituentFactory(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstituentFactory constituentFactory()
/*     */   {
/* 182 */     return ConstituentFactoryHolder.cf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConstituentFactory factory()
/*     */   {
/* 193 */     return ConstituentFactoryHolder.cf;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\SimpleConstituent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */