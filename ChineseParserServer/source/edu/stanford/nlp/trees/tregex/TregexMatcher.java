/*     */ package edu.stanford.nlp.trees.tregex;
/*     */ 
/*     */ import edu.stanford.nlp.io.NumberRangesFileFilter;
/*     */ import edu.stanford.nlp.parser.lexparser.EnglishTreebankParserParams;
/*     */ import edu.stanford.nlp.parser.lexparser.TreebankLangParserParams;
/*     */ import edu.stanford.nlp.trees.DiskTreebank;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TregexMatcher
/*     */ {
/*     */   final Tree root;
/*     */   Tree tree;
/*     */   Map<Object, Tree> namesToNodes;
/*     */   VariableStrings variableStrings;
/*     */   Iterator findIterator;
/*     */   Tree findCurrent;
/*     */   
/*     */   TregexMatcher(Tree root, Tree tree, Map<Object, Tree> namesToNodes, VariableStrings variableStrings)
/*     */   {
/*  32 */     this.root = root;
/*  33 */     this.tree = tree;
/*  34 */     this.namesToNodes = namesToNodes;
/*  35 */     this.variableStrings = variableStrings;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/*  42 */     this.findIterator = null;
/*  43 */     this.namesToNodes.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void resetChildIter(Tree tree)
/*     */   {
/*  50 */     this.tree = tree;
/*  51 */     resetChildIter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   void resetChildIter() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean matches();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matchesAt(Tree node)
/*     */   {
/*  76 */     resetChildIter(node);
/*  77 */     return matches();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Tree getMatch();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean find()
/*     */   {
/*  95 */     if (this.findIterator == null) {
/*  96 */       this.findIterator = this.root.iterator();
/*     */     }
/*  98 */     if ((this.findCurrent != null) && (matches())) {
/*  99 */       return true;
/*     */     }
/* 101 */     while (this.findIterator.hasNext()) {
/* 102 */       this.findCurrent = ((Tree)this.findIterator.next());
/* 103 */       resetChildIter(this.findCurrent);
/* 104 */       if (matches()) {
/* 105 */         return true;
/*     */       }
/*     */     }
/* 108 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean findNextMatchingNode()
/*     */   {
/* 117 */     Tree lastMatchingNode = getMatch();
/* 118 */     while (find()) {
/* 119 */       if (getMatch() != lastMatchingNode)
/* 120 */         return true;
/*     */     }
/* 122 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree getNode(Object name)
/*     */   {
/* 132 */     return (Tree)this.namesToNodes.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/* 143 */     Map<String, Integer> flagMap = new HashMap();
/* 144 */     flagMap.put("-p", new Integer(1));
/* 145 */     Map<String, String[]> argsMap = StringUtils.argsToMap(args, flagMap);
/* 146 */     args = (String[])argsMap.get(null);
/* 147 */     String s = "VP < VBZ";
/* 148 */     if (argsMap.keySet().contains("-p")) {
/* 149 */       s = ((String[])argsMap.get("-p"))[0];
/*     */     }
/* 151 */     TregexPattern tregexPattern = TregexPattern.compile(s);
/*     */     
/* 153 */     TreebankLangParserParams tlpp = new EnglishTreebankParserParams();
/* 154 */     FileFilter testFilt = new NumberRangesFileFilter(args[1], true);
/* 155 */     DiskTreebank testTreebank = tlpp.diskTreebank();
/* 156 */     testTreebank.loadPath(new File(args[0]), testFilt);
/*     */     
/* 158 */     TreePattern treePattern = null;
/*     */     
/* 160 */     boolean print = argsMap.keySet().contains("-print");
/*     */     
/* 162 */     for (Iterator<Tree> iterator = testTreebank.iterator(); iterator.hasNext();) {
/* 163 */       Tree root = (Tree)iterator.next();
/* 164 */       TregexMatcher tregexMatcher = tregexPattern.matcher(root);
/* 165 */       TreeMatcher treeMatcher = treePattern.matcher(root);
/* 166 */       Tree lastTreeMatch = null;
/*     */       
/* 168 */       List<Tree> tregexMatchedTrees = new ArrayList();
/* 169 */       List<Tree> treeMatchedTrees = new ArrayList();
/* 170 */       while (treeMatcher.find()) {
/* 171 */         Tree treeMatchedTree = treeMatcher.getMatch();
/* 172 */         if (treeMatchedTree != lastTreeMatch)
/*     */         {
/*     */ 
/* 175 */           treeMatchedTrees.add(treeMatchedTree);
/* 176 */           lastTreeMatch = treeMatchedTree;
/*     */         } }
/* 178 */       while (tregexMatcher.find()) {
/* 179 */         Tree tregexMatchedTree = tregexMatcher.getMatch();
/* 180 */         tregexMatchedTrees.add(tregexMatchedTree);
/*     */       }
/* 182 */       if (!tregexMatchedTrees.equals(treeMatchedTrees)) {
/* 183 */         System.out.println("Disagreement");
/* 184 */         if (print) {
/* 185 */           System.out.println("TreeMatcher found " + treeMatchedTrees.size() + " matches:");
/* 186 */           for (Iterator<Tree> iter = treeMatchedTrees.iterator(); iter.hasNext();) {
/* 187 */             Tree tree = (Tree)iter.next();
/* 188 */             tree.pennPrint();
/*     */           }
/* 190 */           System.out.println("TregexMatcher found " + tregexMatchedTrees.size() + " matches:");
/* 191 */           for (iter = tregexMatchedTrees.iterator(); iter.hasNext();) {
/* 192 */             Tree tree = (Tree)iter.next();
/* 193 */             tree.pennPrint();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Iterator<Tree> iter;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\TregexMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */