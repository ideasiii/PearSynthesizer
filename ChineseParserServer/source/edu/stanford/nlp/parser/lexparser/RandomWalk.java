/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.stats.Counter;
/*    */ import edu.stanford.nlp.util.Pair;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ class RandomWalk
/*    */ {
/* 14 */   private Map model = new HashMap();
/*    */   
/* 16 */   private Map hiddenToSeen = new HashMap();
/* 17 */   private Map seenToHidden = new HashMap();
/*    */   
/*    */ 
/*    */   private static final double LAMBDA = 0.01D;
/*    */   
/*    */ 
/*    */   public double score(Object hidden, Object seen)
/*    */   {
/* 25 */     return ((Counter)this.model.get(hidden)).getNormalizedCount(seen);
/*    */   }
/*    */   
/*    */   public double score(Object hidden, Object seen, int steps)
/*    */   {
/* 30 */     double total = 0.0D;
/* 31 */     for (int i = 0; i <= steps; i++) {
/* 32 */       total += Math.pow(0.01D, steps) * step(hidden, seen, steps);
/*    */     }
/* 34 */     return total;
/*    */   }
/*    */   
/*    */ 
/*    */   public double step(Object hidden, Object seen, int steps)
/*    */   {
/* 40 */     if (steps < 1) {
/* 41 */       return ((Counter)this.hiddenToSeen.get(hidden)).getNormalizedCount(seen);
/*    */     }
/* 43 */     double total = 0.0D;
/* 44 */     for (Iterator i = this.seenToHidden.keySet().iterator(); i.hasNext();) {
/* 45 */       seen1 = i.next();
/* 46 */       for (j = this.hiddenToSeen.keySet().iterator(); j.hasNext();) {
/* 47 */         Object hidden1 = j.next();
/* 48 */         double subtotal = ((Counter)this.hiddenToSeen.get(hidden)).getNormalizedCount(seen1) * ((Counter)this.seenToHidden.get(seen1)).getNormalizedCount(hidden1);
/* 49 */         subtotal += score(hidden1, seen, steps - 1);
/* 50 */         total += subtotal; } }
/*    */     Object seen1;
/*    */     Iterator j;
/* 53 */     return total;
/*    */   }
/*    */   
/*    */ 
/*    */   public void train(Collection data)
/*    */   {
/* 59 */     for (Iterator i = data.iterator(); i.hasNext();) {
/* 60 */       Pair p = (Pair)i.next();
/* 61 */       Object seen = p.first();
/* 62 */       Object hidden = p.second();
/* 63 */       if (!this.hiddenToSeen.keySet().contains(hidden)) {
/* 64 */         this.hiddenToSeen.put(hidden, new Counter());
/*    */       }
/* 66 */       ((Counter)this.hiddenToSeen.get(hidden)).incrementCount(seen);
/*    */       
/* 68 */       if (!this.seenToHidden.keySet().contains(seen)) {
/* 69 */         this.seenToHidden.put(seen, new Counter());
/*    */       }
/* 71 */       ((Counter)this.seenToHidden.get(seen)).incrementCount(hidden);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RandomWalk(Collection data, int steps)
/*    */   {
/* 81 */     Map m = new HashMap();
/* 82 */     train(data);
/* 83 */     for (Iterator i = this.seenToHidden.keySet().iterator(); i.hasNext();) {
/* 84 */       seen = i.next();
/* 85 */       if (!this.model.containsKey(seen)) {
/* 86 */         this.model.put(seen, new Counter());
/*    */       }
/* 88 */       for (j = this.hiddenToSeen.keySet().iterator(); j.hasNext();) {
/* 89 */         Object hidden = j.next();
/* 90 */         ((Counter)this.model.get(seen)).setCount(hidden, score(seen, hidden, steps));
/*    */       }
/*    */     }
/*    */     Object seen;
/*    */     Iterator j;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\RandomWalk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */