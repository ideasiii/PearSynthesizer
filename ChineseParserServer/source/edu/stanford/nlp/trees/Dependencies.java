/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.ling.AbstractMapLabel;
/*    */ import edu.stanford.nlp.ling.HasTag;
/*    */ import edu.stanford.nlp.ling.HasWord;
/*    */ import edu.stanford.nlp.util.Filter;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Dependencies
/*    */ {
/*    */   public static class DependentPuncTagRejectFilter
/*    */     implements Filter<Dependency>
/*    */   {
/*    */     private Filter tagRejectFilter;
/*    */     
/*    */     public DependentPuncTagRejectFilter(Filter trf)
/*    */     {
/* 24 */       this.tagRejectFilter = trf;
/*    */     }
/*    */     
/*    */     public boolean accept(Dependency d) {
/* 28 */       if (d == null) {
/* 29 */         return false;
/*    */       }
/* 31 */       if (!(d.dependent() instanceof HasTag)) {
/* 32 */         return false;
/*    */       }
/* 34 */       String tag = ((HasTag)d.dependent()).tag();
/* 35 */       return this.tagRejectFilter.accept(tag);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static class DependentPuncWordRejectFilter
/*    */     implements Filter<Dependency>
/*    */   {
/*    */     private final Filter wordRejectFilter;
/*    */     
/*    */ 
/*    */     public DependentPuncWordRejectFilter(Filter wrf)
/*    */     {
/* 49 */       this.wordRejectFilter = wrf;
/*    */     }
/*    */     
/*    */     public boolean accept(Dependency d) {
/* 53 */       if (d == null) {
/* 54 */         return false;
/*    */       }
/* 56 */       if (!(d.dependent() instanceof HasWord)) {
/* 57 */         return false;
/*    */       }
/* 59 */       String word = ((HasWord)d.dependent()).word();
/*    */       
/* 61 */       return this.wordRejectFilter.accept(word);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   private static class ComparatorHolder
/*    */   {
/*    */     private static class DependencyIdxComparator
/*    */       implements Comparator<Dependency>
/*    */     {
/*    */       public int compare(Dependency dep1, Dependency dep2)
/*    */       {
/* 73 */         AbstractMapLabel dep1lab = (AbstractMapLabel)dep1.dependent();
/* 74 */         AbstractMapLabel dep2lab = (AbstractMapLabel)dep2.dependent();
/* 75 */         int dep1idx = dep1lab.index();
/* 76 */         int dep2idx = dep2lab.index();
/* 77 */         return dep1idx - dep2idx;
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 82 */     private static final Comparator<Dependency> dc = new DependencyIdxComparator(null);
/*    */   }
/*    */   
/*    */   public static Comparator<Dependency> dependencyIndexComparator()
/*    */   {
/* 87 */     return ComparatorHolder.dc;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\Dependencies.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */