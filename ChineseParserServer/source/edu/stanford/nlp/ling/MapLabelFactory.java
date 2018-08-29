/*    */ package edu.stanford.nlp.ling;
/*    */ 
/*    */ import java.io.PrintStream;
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
/*    */ public class MapLabelFactory
/*    */   implements LabelFactory
/*    */ {
/*    */   public Label newLabel(String str)
/*    */   {
/* 19 */     return new MapLabel(str);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Label newLabel(String str, int options)
/*    */   {
/* 31 */     return newLabel(str);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Label newLabelFromString(String str)
/*    */   {
/* 41 */     return newLabel(str);
/*    */   }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Label newLabel(Label oldLabel)
/*    */   {
/* 63 */     return new MapLabel(oldLabel);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/* 70 */     MapLabelFactory lf = new MapLabelFactory();
/* 71 */     System.out.println("new label from String: " + ((MapLabel)lf.newLabel("foo")).toString("value{map}"));
/* 72 */     System.out.println("new label from StringLabel: " + ((MapLabel)lf.newLabel(new StringLabel("foo"))).toString("value{map}"));
/* 73 */     CategoryWordTag cwt = new CategoryWordTag("cat", "word", "tag");
/* 74 */     MapLabel label = (MapLabel)lf.newLabel(cwt);
/* 75 */     System.out.println("new label from CategoryWordTag: " + label.toString("value{map}"));
/* 76 */     label.put("temp", "hot");
/* 77 */     System.out.println("new label from MapLabel: " + ((MapLabel)lf.newLabel(label)).toString("value{map}"));
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\MapLabelFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */