/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.util.Numberer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Hook
/*    */   extends Item
/*    */ {
/*    */   public int subState;
/*    */   
/*    */   public boolean isPreHook()
/*    */   {
/* 16 */     return this.head < this.start;
/*    */   }
/*    */   
/*    */   public boolean isPostHook() {
/* 20 */     return this.head >= this.end;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 24 */     return (isPreHook() ? "Pre" : "Post") + "Hook(" + Numberer.getGlobalNumberer("states").object(this.state) + "|" + Numberer.getGlobalNumberer("states").object(this.subState) + ":" + this.start + "-" + this.end + "," + this.head + "/" + Numberer.getGlobalNumberer("tags").object(this.tag) + ")";
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 28 */     return 1 + (this.state << 14) ^ this.subState << 16 ^ this.head << 22 ^ this.tag << 27 ^ this.start << 1 ^ this.end << 7;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 36 */     if (this == o) {
/* 37 */       return true;
/*    */     }
/* 39 */     if ((o instanceof Hook)) {
/* 40 */       Hook e = (Hook)o;
/* 41 */       if ((this.state == e.state) && (this.subState == e.subState) && (this.head == e.head) && (this.tag == e.tag) && (this.start == e.start) && (this.end == e.end))
/*    */       {
/* 43 */         return true;
/*    */       }
/*    */     }
/* 46 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Hook.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */