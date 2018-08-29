/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CollectionFactory<T>
/*    */   implements Serializable
/*    */ {
/* 16 */   public static final CollectionFactory ARRAY_LIST_FACTORY = new ArrayListFactory();
/* 17 */   public static final CollectionFactory LINKED_LIST_FACTORY = new LinkedListFactory();
/* 18 */   public static final CollectionFactory HASH_SET_FACTORY = new HashSetFactory();
/*    */   
/*    */   public static <E> CollectionFactory<E> hashSetFactory()
/*    */   {
/* 22 */     return HASH_SET_FACTORY;
/*    */   }
/*    */   
/*    */   public static <E> CollectionFactory<E> arrayListFactory() {
/* 26 */     return ARRAY_LIST_FACTORY;
/*    */   }
/*    */   
/*    */   public static class ArrayListFactory<T> extends CollectionFactory<T> {
/*    */     public Collection<T> newCollection() {
/* 31 */       return new ArrayList();
/*    */     }
/*    */     
/*    */     public Collection<T> newEmptyCollection() {
/* 35 */       return Collections.emptyList();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/* 40 */   public static <E> CollectionFactory<E> linkedListFactory() { return LINKED_LIST_FACTORY; }
/*    */   
/*    */   public abstract Collection<T> newCollection();
/*    */   
/*    */   public static class LinkedListFactory<T> extends CollectionFactory<T> {
/* 45 */     public Collection<T> newCollection() { return new LinkedList(); }
/*    */     
/*    */     public Collection<T> newEmptyCollection()
/*    */     {
/* 49 */       return Collections.emptyList();
/*    */     }
/*    */   }
/*    */   
/*    */   public abstract Collection<T> newEmptyCollection();
/*    */   
/*    */   public static class HashSetFactory<T> extends CollectionFactory<T> {
/* 56 */     public Collection<T> newCollection() { return new HashSet(); }
/*    */     
/*    */     public Collection<T> newEmptyCollection()
/*    */     {
/* 60 */       return Collections.emptySet();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\CollectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */