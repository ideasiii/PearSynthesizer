/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.util.Filter;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GrammaticalStructureFactory
/*    */ {
/*    */   private final Constructor con;
/*    */   private final Filter<String> puncFilter;
/*    */   
/*    */   public GrammaticalStructureFactory(String name)
/*    */   {
/* 27 */     this(name, null);
/*    */   }
/*    */   
/*    */ 
/*    */   public GrammaticalStructureFactory(String name, Filter<String> puncFilter)
/*    */   {
/*    */     Class c;
/*    */     Class t;
/*    */     Class f;
/*    */     try
/*    */     {
/* 38 */       c = Class.forName(name);
/* 39 */       if (!Class.forName("edu.stanford.nlp.trees.GrammaticalStructure").isAssignableFrom(c)) {
/* 40 */         throw new ClassNotFoundException();
/*    */       }
/* 42 */       t = Class.forName("edu.stanford.nlp.trees.Tree");
/* 43 */       f = Class.forName("edu.stanford.nlp.util.Filter");
/*    */     } catch (ClassNotFoundException e) {
/* 45 */       throw new RuntimeException("Class " + name + " does not exist or does not extend edu.stanford.nlp.trees.GrammaticalStructure.");
/*    */     }
/*    */     try
/*    */     {
/* 49 */       if (puncFilter == null) {
/* 50 */         this.con = c.getConstructor(new Class[] { t });
/*    */       } else {
/* 52 */         this.con = c.getConstructor(new Class[] { t, f });
/*    */       }
/* 54 */       this.puncFilter = puncFilter;
/*    */     }
/*    */     catch (NoSuchMethodException e) {
/* 57 */       throw new RuntimeException();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public GrammaticalStructure newGrammaticalStructure(Tree t)
/*    */   {
/*    */     try
/*    */     {
/* 69 */       if (this.puncFilter == null) {
/* 70 */         return (GrammaticalStructure)this.con.newInstance(new Object[] { t });
/*    */       }
/* 72 */       return (GrammaticalStructure)this.con.newInstance(new Object[] { t, this.puncFilter });
/*    */     }
/*    */     catch (InstantiationException e) {
/* 75 */       throw new RuntimeException("Cannot instantiate " + this.con.getDeclaringClass().getName());
/*    */     } catch (IllegalAccessException e) {
/* 77 */       throw new RuntimeException(this.con.getDeclaringClass().getName() + "(Tree t) does not have public access");
/*    */     } catch (InvocationTargetException e) {
/* 79 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\GrammaticalStructureFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */