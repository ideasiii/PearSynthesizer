/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.util.Numberer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Edge
/*    */   extends Item
/*    */ {
/*    */   public Hook backHook;
/*    */   
/*    */   public boolean isEdge()
/*    */   {
/* 14 */     return true;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 18 */     return "Edge(" + Numberer.getGlobalNumberer("states").object(this.state) + ":" + this.start + "-" + this.end + "," + this.head + "/" + Numberer.getGlobalNumberer("tags").object(this.tag) + ")";
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 22 */     return this.state << 1 ^ this.head << 8 ^ this.tag << 16 ^ this.start << 4 ^ this.end << 24;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 26 */     if (this == o)
/* 27 */       return true;
/* 28 */     if ((o instanceof Edge)) {
/* 29 */       Edge e = (Edge)o;
/* 30 */       if ((this.state == e.state) && (this.head == e.head) && (this.tag == e.tag) && (this.start == e.start) && (this.end == e.end)) {
/* 31 */         return true;
/*    */       }
/*    */     }
/* 34 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Edge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */