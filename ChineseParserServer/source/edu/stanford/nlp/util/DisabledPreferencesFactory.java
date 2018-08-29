/*    */ package edu.stanford.nlp.util;
/*    */ 
/*    */ import java.util.prefs.Preferences;
/*    */ import java.util.prefs.PreferencesFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DisabledPreferencesFactory
/*    */   implements PreferencesFactory
/*    */ {
/*    */   public Preferences systemRoot()
/*    */   {
/* 19 */     return new DisabledPreferences();
/*    */   }
/*    */   
/*    */   public Preferences userRoot() {
/* 23 */     return new DisabledPreferences();
/*    */   }
/*    */   
/*    */   public static void install() {
/*    */     try {
/* 28 */       System.setProperty("java.util.prefs.PreferencesFactory", "edu.stanford.nlp.util.DisabledPreferencesFactory");
/*    */     }
/*    */     catch (SecurityException e) {}
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\DisabledPreferencesFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */