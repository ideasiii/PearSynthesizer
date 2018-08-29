/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ import java.util.prefs.AbstractPreferences;
/*    */ import java.util.prefs.BackingStoreException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DisabledPreferences
/*    */   extends AbstractPreferences
/*    */ {
/*    */   public DisabledPreferences()
/*    */   {
/* 17 */     super(null, "");
/*    */   }
/*    */   
/*    */ 
/*    */   protected void putSpi(String key, String value) {}
/*    */   
/*    */   protected String getSpi(String key)
/*    */   {
/* 25 */     return null;
/*    */   }
/*    */   
/*    */   protected void removeSpi(String key) {}
/*    */   
/*    */   protected void removeNodeSpi()
/*    */     throws BackingStoreException
/*    */   {}
/*    */   
/*    */   protected String[] keysSpi()
/*    */     throws BackingStoreException
/*    */   {
/* 37 */     return new String[0];
/*    */   }
/*    */   
/*    */   protected String[] childrenNamesSpi() throws BackingStoreException {
/* 41 */     return new String[0];
/*    */   }
/*    */   
/*    */   protected AbstractPreferences childSpi(String name) {
/* 45 */     return null;
/*    */   }
/*    */   
/*    */   protected void syncSpi()
/*    */     throws BackingStoreException
/*    */   {}
/*    */   
/*    */   protected void flushSpi()
/*    */     throws BackingStoreException
/*    */   {}
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\DisabledPreferences.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */