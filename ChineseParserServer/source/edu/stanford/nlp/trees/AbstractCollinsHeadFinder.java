/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCollinsHeadFinder
/*     */   implements HeadFinder, Serializable
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*     */   protected final TreebankLanguagePack tlp;
/*     */   protected HashMap<String, String[][]> nonTerminalInfo;
/*  54 */   protected String[] defaultRule = null;
/*     */   private static final long serialVersionUID = -6540278059442931087L;
/*     */   
/*  57 */   protected AbstractCollinsHeadFinder(TreebankLanguagePack tlp) { this.tlp = tlp; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Tree findMarkedHead(Tree t)
/*     */   {
/*  70 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree determineHead(Tree t)
/*     */   {
/*  84 */     return determineHead(t, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree determineHead(Tree t, Tree parent)
/*     */   {
/*  98 */     if (this.nonTerminalInfo == null) {
/*  99 */       throw new RuntimeException("Classes derived from AbstractCollinsHeadFinder must create and fill HashMap nonTerminalInfo.");
/*     */     }
/*     */     
/* 102 */     if (t.isLeaf()) {
/* 103 */       return null;
/*     */     }
/* 105 */     Tree[] kids = t.children();
/*     */     
/*     */     Tree theHead;
/*     */     
/* 109 */     if ((theHead = findMarkedHead(t)) != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 114 */       return theHead;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 120 */     if (kids.length == 1)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 125 */       return kids[0];
/*     */     }
/*     */     
/* 128 */     return determineNonTrivialHead(t, parent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Tree determineNonTrivialHead(Tree t, Tree parent)
/*     */   {
/* 135 */     Tree theHead = null;
/* 136 */     String motherCat = this.tlp.basicCategory(t.label().value());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */     String[][] how = (String[][])this.nonTerminalInfo.get(motherCat);
/* 154 */     if (how == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 160 */       if (this.defaultRule != null)
/*     */       {
/*     */ 
/*     */ 
/* 164 */         return traverseLocate(t.children(), this.defaultRule, true);
/*     */       }
/* 166 */       return null;
/*     */     }
/*     */     
/* 169 */     for (int i = 0; i < how.length; i++) {
/* 170 */       boolean deflt = i == how.length - 1;
/* 171 */       theHead = traverseLocate(t.children(), how[i], deflt);
/* 172 */       if (theHead != null) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 179 */     return theHead;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Tree traverseLocate(Tree[] daughterTrees, String[] how, boolean deflt)
/*     */   {
/* 191 */     int headIdx = 0;
/*     */     
/* 193 */     boolean found = false;
/*     */     
/* 195 */     if (how[0].equals("left"))
/*     */     {
/* 197 */       for (int i = 1; i < how.length; i++)
/* 198 */         for (headIdx = 0; headIdx < daughterTrees.length; headIdx++) {
/* 199 */           String childCat = this.tlp.basicCategory(daughterTrees[headIdx].label().value());
/* 200 */           if (how[i].equals(childCat)) {
/* 201 */             found = true;
/*     */             break label90;
/*     */           }
/*     */         }
/*     */       label90:
/* 206 */       if (!found)
/*     */       {
/* 208 */         if (deflt) {
/* 209 */           headIdx = 0;
/*     */         } else {
/* 211 */           return null;
/*     */         }
/*     */       }
/* 214 */     } else if (how[0].equals("leftdis"))
/*     */     {
/* 216 */       for (headIdx = 0; headIdx < daughterTrees.length; headIdx++) {
/* 217 */         String childCat = this.tlp.basicCategory(daughterTrees[headIdx].label().value());
/* 218 */         for (int i = 1; i < how.length; i++)
/* 219 */           if (how[i].equals(childCat)) {
/* 220 */             found = true;
/*     */             break label191;
/*     */           }
/*     */       }
/*     */       label191:
/* 225 */       if (!found)
/*     */       {
/* 227 */         if (deflt) {
/* 228 */           headIdx = 0;
/*     */         } else {
/* 230 */           return null;
/*     */         }
/*     */       }
/* 233 */     } else if (how[0].equals("right"))
/*     */     {
/*     */ 
/* 236 */       for (int i = 1; i < how.length; i++)
/* 237 */         for (headIdx = daughterTrees.length - 1; headIdx >= 0; headIdx--) {
/* 238 */           String childCat = this.tlp.basicCategory(daughterTrees[headIdx].label().value());
/* 239 */           if (how[i].equals(childCat)) {
/* 240 */             found = true;
/*     */             break label293;
/*     */           }
/*     */         }
/*     */       label293:
/* 245 */       if (!found)
/*     */       {
/* 247 */         if (deflt) {
/* 248 */           headIdx = daughterTrees.length - 1;
/*     */         } else {
/* 250 */           return null;
/*     */         }
/*     */       }
/* 253 */     } else if (how[0].equals("rightdis"))
/*     */     {
/*     */ 
/* 256 */       for (headIdx = daughterTrees.length - 1; headIdx >= 0; headIdx--) {
/* 257 */         String childCat = this.tlp.basicCategory(daughterTrees[headIdx].label().value());
/* 258 */         for (int i = 1; i < how.length; i++)
/*     */         {
/*     */ 
/*     */ 
/* 262 */           if (how[i].equals(childCat)) {
/* 263 */             found = true;
/*     */             break label398;
/*     */           } }
/*     */       }
/*     */       label398:
/* 268 */       if (!found)
/*     */       {
/* 270 */         if (deflt) {
/* 271 */           headIdx = daughterTrees.length - 1;
/*     */         } else {
/* 273 */           return null;
/*     */         }
/*     */       }
/*     */     } else {
/* 277 */       throw new RuntimeException("ERROR: invalid direction type to nonTerminalInfo map in AbstractCollinsHeadFinder.");
/*     */     }
/* 279 */     headIdx = postOperationFix(headIdx, daughterTrees);
/*     */     
/* 281 */     return daughterTrees[headIdx];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int postOperationFix(int headIdx, Tree[] daughterTrees)
/*     */   {
/* 293 */     return headIdx;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\AbstractCollinsHeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */