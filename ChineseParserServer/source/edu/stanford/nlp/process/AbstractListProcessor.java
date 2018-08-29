/*    */ package edu.stanford.nlp.process;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Document;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractListProcessor<IN, OUT>
/*    */   implements ListProcessor<IN, OUT>, Processor<IN, OUT>
/*    */ {
/*    */   public Document<OUT> processDocument(Document<IN> in)
/*    */   {
/* 19 */     Document<OUT> doc = in.blankDocument();
/* 20 */     doc.addAll(process(in));
/* 21 */     return doc;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<List<OUT>> processLists(List<List<IN>> lists)
/*    */   {
/* 31 */     List<List<OUT>> result = new ArrayList(lists.size());
/* 32 */     for (List<IN> list : lists) {
/* 33 */       List<OUT> outList = process(list);
/* 34 */       result.add(outList);
/*    */     }
/* 36 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\AbstractListProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */