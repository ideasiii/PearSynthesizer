/*    */ package edu.stanford.nlp.trees;
/*    */ 
/*    */ import edu.stanford.nlp.trees.international.pennchinese.CharacterLevelTagExtender;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TreeToBracketProcessor
/*    */ {
/* 14 */   public List evalTypes = new ArrayList();
/* 15 */   public static CharacterLevelTagExtender ext = new CharacterLevelTagExtender();
/*    */   
/*    */   public TreeToBracketProcessor(List evalTypes) {
/* 18 */     this.evalTypes = evalTypes;
/*    */   }
/*    */   
/*    */   public Collection allBrackets(Tree root) {
/* 22 */     boolean words = this.evalTypes.contains("word");
/* 23 */     boolean tags = this.evalTypes.contains("tag");
/* 24 */     boolean cats = this.evalTypes.contains("cat");
/* 25 */     List brackets = new ArrayList();
/* 26 */     Iterator iterator; if ((words) || (cats) || (tags)) {
/* 27 */       root = ext.transformTree(root);
/* 28 */       for (iterator = root.iterator(); iterator.hasNext();) {
/* 29 */         Tree tree = (Tree)iterator.next();
/* 30 */         if ((tree.isPrePreTerminal()) && (!tree.value().equals("ROOT"))) {
/* 31 */           if (words) {
/* 32 */             brackets.add(new WordCatConstituent(tree, root, "word"));
/*    */           }
/* 34 */           if (tags) {
/* 35 */             brackets.add(new WordCatConstituent(tree, root, "tag"));
/*    */           }
/* 37 */         } else if ((cats) && (tree.isPhrasal()) && (!tree.value().equals("ROOT"))) {
/* 38 */           brackets.add(new WordCatConstituent(tree, root, "cat"));
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 43 */     return brackets;
/*    */   }
/*    */   
/*    */   public Collection commonWordTagTypeBrackets(Tree root1, Tree root2) {
/* 47 */     root1 = ext.transformTree(root1);
/* 48 */     root2 = ext.transformTree(root2);
/*    */     
/* 50 */     List firstPreTerms = new ArrayList();
/* 51 */     for (Iterator iterator = root1.iterator(); iterator.hasNext();) {
/* 52 */       Tree tree = (Tree)iterator.next();
/* 53 */       if (tree.isPrePreTerminal()) {
/* 54 */         firstPreTerms.add(tree);
/*    */       }
/*    */     }
/*    */     
/* 58 */     List brackets = new ArrayList();
/* 59 */     for (Iterator pretermIter = firstPreTerms.iterator(); pretermIter.hasNext();) {
/* 60 */       preTerm = (Tree)pretermIter.next();
/* 61 */       for (iter = root2.iterator(); iter.hasNext();) {
/* 62 */         Tree tree = (Tree)iter.next();
/* 63 */         if (tree.isPrePreTerminal())
/*    */         {
/*    */ 
/* 66 */           if ((Trees.leftEdge(tree, root2) == Trees.leftEdge(preTerm, root1)) && (Trees.rightEdge(tree, root2) == Trees.rightEdge(preTerm, root1))) {
/* 67 */             brackets.add(new WordCatConstituent(preTerm, root1, "goodWordTag"));
/* 68 */             break;
/*    */           } }
/*    */       } }
/*    */     Tree preTerm;
/*    */     Iterator iter;
/* 73 */     return brackets;
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreeToBracketProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */