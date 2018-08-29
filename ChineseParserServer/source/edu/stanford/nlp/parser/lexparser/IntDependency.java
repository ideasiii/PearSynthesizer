/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.util.StringUtils;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntDependency
/*    */   implements Serializable
/*    */ {
/*    */   public static final String LEFT = "left";
/*    */   public static final String RIGHT = "right";
/*    */   public static final int ANY_DISTANCE_INT = -1;
/*    */   public IntTaggedWord head;
/*    */   public IntTaggedWord arg;
/*    */   public boolean leftHeaded;
/*    */   public short distance;
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 24 */     return this.head.hashCode() ^ this.arg.hashCode() << 8 ^ (this.leftHeaded ? 1 : 0) << 15 ^ this.distance << 16;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 28 */     if (this == o) {
/* 29 */       return true;
/*    */     }
/* 31 */     if ((o instanceof IntDependency)) {
/* 32 */       IntDependency d = (IntDependency)o;
/* 33 */       return (this.head.equals(d.head)) && (this.arg.equals(d.arg)) && (this.distance == d.distance) && (this.leftHeaded == d.leftHeaded);
/*    */     }
/* 35 */     return false;
/*    */   }
/*    */   
/*    */ 
/* 39 */   private static final char[] charsToEscape = { '"' };
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/* 42 */   public String toString() { return "\"" + StringUtils.escapeString(this.head.toString(), charsToEscape, '\\') + "\" -> \"" + StringUtils.escapeString(this.arg.toString(), charsToEscape, '\\') + "\" " + (this.leftHeaded ? "left" : "right") + " " + this.distance; }
/*    */   
/*    */ 
/*    */   public IntDependency() {}
/*    */   
/*    */   public IntDependency(IntTaggedWord head, IntTaggedWord arg, boolean leftHeaded, int distance)
/*    */   {
/* 49 */     this.head = head;
/* 50 */     this.arg = arg;
/* 51 */     this.distance = ((short)distance);
/* 52 */     this.leftHeaded = leftHeaded;
/*    */   }
/*    */   
/*    */   public IntDependency(int headWord, int headTag, int argWord, int argTag, boolean leftHeaded, int distance) {
/* 56 */     this.head = new IntTaggedWord(headWord, headTag);
/* 57 */     this.arg = new IntTaggedWord(argWord, argTag);
/* 58 */     this.distance = ((short)distance);
/* 59 */     this.leftHeaded = leftHeaded;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\IntDependency.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */