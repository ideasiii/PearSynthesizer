/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.ling.TaggedWord;
/*     */ import edu.stanford.nlp.ling.WordTag;
/*     */ import edu.stanford.nlp.process.Morphology;
/*     */ import edu.stanford.nlp.util.ArrayUtils;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CollocationFinder
/*     */ {
/*  28 */   private static boolean DEBUG = true;
/*     */   
/*     */ 
/*     */   private Tree qTree;
/*     */   
/*     */   private HeadFinder hf;
/*     */   
/*     */   private ArrayList<Collocation> collocationCollector;
/*     */   
/*     */   private WordNetConnection wnConnect;
/*     */   
/*     */ 
/*     */   public CollocationFinder(Tree t, WordNetConnection w)
/*     */   {
/*  42 */     this(t, w, new CollinsHeadFinder());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CollocationFinder(Tree t, WordNetConnection w, HeadFinder hf)
/*     */   {
/*  51 */     CoordinationTransformer transformer = new CoordinationTransformer();
/*  52 */     this.wnConnect = w;
/*  53 */     this.qTree = transformer.transformTree(t);
/*  54 */     this.collocationCollector = new ArrayList();
/*  55 */     this.hf = hf;
/*  56 */     getCollocationsList();
/*  57 */     if (DEBUG) {
/*  58 */       System.err.println("Collected collocations: " + this.collocationCollector);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree getMangledTree()
/*     */   {
/*  68 */     return getMangledTree(this.qTree);
/*     */   }
/*     */   
/*     */   private Tree getMangledTree(Tree t) {
/*  72 */     Collocation matchingColl = null;
/*  73 */     for (Tree child : t.children()) {
/*  74 */       child = getMangledTree(child);
/*     */     }
/*  76 */     boolean additionalCollocationsExist = false;
/*  77 */     for (Collocation c : this.collocationCollector)
/*     */     {
/*     */ 
/*  80 */       if ((t.equals(c.parentNode)) && (
/*  81 */         (matchingColl == null) || ((((Integer)c.span.first()).intValue() <= ((Integer)matchingColl.span.first()).intValue()) && (((Integer)c.span.second()).intValue() >= ((Integer)matchingColl.span.second()).intValue()))))
/*     */       {
/*     */ 
/*  84 */         matchingColl = c;
/*  85 */         if (DEBUG) {
/*  86 */           System.err.println("Found matching collocation for tree:");
/*  87 */           t.pennPrint();
/*  88 */           System.err.print("  head label: " + c.headLabel);
/*  89 */           System.err.println("; collocation string: " + c.collocationString);
/*  90 */           System.err.println("  Constituents: " + c.indicesOfConstituentChildren);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  96 */     if (matchingColl == null) {
/*  97 */       return t;
/*     */     }
/*  99 */     if (DEBUG) {
/* 100 */       System.err.println("Collapsing " + matchingColl);
/*     */     }
/* 102 */     Tree[] allChildren = t.children();
/*     */     
/*     */ 
/* 105 */     StringBuilder mutatedString = new StringBuilder(160);
/* 106 */     for (Iterator i$ = matchingColl.indicesOfConstituentChildren.iterator(); i$.hasNext();) { int i = ((Integer)i$.next()).intValue();
/* 107 */       String strToAppend = mergeLeavesIntoCollocatedString(allChildren[i]);
/* 108 */       mutatedString.append(strToAppend);
/* 109 */       mutatedString.append("_");
/*     */     }
/* 111 */     mutatedString = mutatedString.deleteCharAt(mutatedString.length() - 1);
/*     */     
/*     */ 
/* 114 */     if (DEBUG) System.err.println("allChildren is: " + Arrays.toString(allChildren));
/* 115 */     for (int index = matchingColl.indicesOfConstituentChildren.size() - 1; index > 0; index--) {
/* 116 */       int thisConstituent = ((Integer)matchingColl.indicesOfConstituentChildren.get(index)).intValue();
/* 117 */       allChildren = (Tree[])ArrayUtils.removeAt(allChildren, thisConstituent);
/* 118 */       if (DEBUG) { System.err.println(" deleted " + thisConstituent + "; allChildren is: " + Arrays.toString(allChildren));
/*     */       }
/*     */     }
/* 121 */     String newNodeString = mutatedString.toString();
/*     */     
/* 123 */     int firstChildIndex = ((Integer)matchingColl.indicesOfConstituentChildren.get(0)).intValue();
/*     */     
/* 125 */     Tree newCollocationChild = allChildren[firstChildIndex];
/* 126 */     if (DEBUG) System.err.println("Manipulating: " + newCollocationChild);
/* 127 */     newCollocationChild.setValue(matchingColl.headLabel.value());
/* 128 */     Tree newCollocationLeaf = newCollocationChild.treeFactory().newLeaf(newNodeString);
/* 129 */     newCollocationChild.setChildren(Collections.singletonList(newCollocationLeaf));
/* 130 */     if (DEBUG) { System.err.println("  changed to: " + newCollocationChild);
/*     */     }
/* 132 */     allChildren[firstChildIndex] = newCollocationChild;
/* 133 */     t.setChildren(allChildren);
/*     */     
/* 135 */     if (DEBUG) {
/* 136 */       System.err.println("Restructured tree is:");
/* 137 */       t.pennPrint();
/* 138 */       System.err.println();
/*     */     }
/* 140 */     return t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ArrayList<Pair<Integer, Integer>> getCollocationsList()
/*     */   {
/* 150 */     getCollocationsList(this.qTree);
/* 151 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void PrintCollocationStrings(PrintWriter pw)
/*     */   {
/* 161 */     ArrayList<String> strs = new ArrayList();
/* 162 */     for (Collocation c : this.collocationCollector) {
/* 163 */       String cs = c.collocationString;
/* 164 */       pw.println(cs + " (" + (((Integer)c.span.first()).intValue() + 1) + "," + (((Integer)c.span.second()).intValue() + 1) + ")");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void getCollocationsList(Tree t)
/*     */   {
/* 175 */     int leftMostLeaf = Trees.leftEdge(t, this.qTree);
/* 176 */     if (t.isPreTerminal()) return;
/* 177 */     List<Tree> children = t.getChildrenAsList();
/* 178 */     if (children.isEmpty()) { return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 183 */     Label headLabel = this.hf.determineHead(t).label();
/* 184 */     StringBuffer testString = null;
/* 185 */     Integer leftSistersBuffer = Integer.valueOf(0);
/* 186 */     for (int i = 0; i < children.size(); i++) {
/* 187 */       ArrayList<Integer> childConstituents = new ArrayList();
/* 188 */       childConstituents.add(Integer.valueOf(i));
/* 189 */       Tree subtree = (Tree)children.get(i);
/* 190 */       Integer currWindowLength = Integer.valueOf(0);
/* 191 */       getCollocationsList(subtree);
/* 192 */       testString = new StringBuffer(160);
/* 193 */       testString.append(treeAsStemmedCollocation(subtree));
/* 194 */       testString.append("_");
/* 195 */       Integer thisSubtreeLength = Integer.valueOf(subtree.yield().size());
/* 196 */       currWindowLength = Integer.valueOf(currWindowLength.intValue() + thisSubtreeLength.intValue());
/* 197 */       StringBuffer testStringNonStemmed = new StringBuffer(160);
/* 198 */       testStringNonStemmed.append(treeAsNonStemmedCollocation(subtree));
/* 199 */       testStringNonStemmed.append("_");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */       for (int j = i + 1; j < children.size(); j++) {
/* 207 */         Tree sisterNode = (Tree)children.get(j);
/* 208 */         childConstituents.add(Integer.valueOf(j));
/* 209 */         testString.append(treeAsStemmedCollocation(sisterNode));
/* 210 */         testStringNonStemmed.append(treeAsNonStemmedCollocation(sisterNode));
/* 211 */         currWindowLength = Integer.valueOf(currWindowLength.intValue() + sisterNode.yield().size());
/* 212 */         if ((!DEBUG) || 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 217 */           (!StringUtils.lookingAt(testString.toString(), "(?:[Tt]he|THE|[Aa][Nn]?)[ _]")))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 222 */           if (wordNetContains(testString.toString())) {
/* 223 */             Pair<Integer, Integer> c = new Pair(Integer.valueOf(leftMostLeaf + leftSistersBuffer.intValue()), Integer.valueOf(leftMostLeaf + leftSistersBuffer.intValue() + currWindowLength.intValue() - 1));
/*     */             
/* 225 */             Collocation col = new Collocation(c, t, (ArrayList)childConstituents.clone(), testString.toString(), headLabel, null);
/* 226 */             this.collocationCollector.add(col);
/* 227 */             if (DEBUG) {
/* 228 */               System.err.println("Found collocation in wordnet: " + testString.toString());
/* 229 */               System.err.println("  Span of collocation is: " + c + "; childConstituents is: " + c);
/*     */             }
/*     */           }
/*     */         }
/* 233 */         testString.append("_");
/* 234 */         if (!StringUtils.lookingAt(testStringNonStemmed.toString(), "(?:[Tt]he|THE|[Aa][Nn]?)[ _]"))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 239 */           if (wordNetContains(testStringNonStemmed.toString())) {
/* 240 */             Pair<Integer, Integer> c = new Pair(Integer.valueOf(leftMostLeaf + leftSistersBuffer.intValue()), Integer.valueOf(leftMostLeaf + leftSistersBuffer.intValue() + currWindowLength.intValue() - 1));
/*     */             
/* 242 */             Collocation col = new Collocation(c, t, (ArrayList)childConstituents.clone(), testStringNonStemmed.toString(), headLabel, null);
/* 243 */             this.collocationCollector.add(col);
/* 244 */             if (DEBUG) {
/* 245 */               System.err.println("Found collocation in wordnet: " + testStringNonStemmed.toString());
/* 246 */               System.err.println("  Span of collocation is: " + c + "; childConstituents is: " + c);
/*     */             }
/*     */           }
/*     */         }
/* 250 */         testStringNonStemmed.append("_");
/*     */       }
/* 252 */       leftSistersBuffer = Integer.valueOf(leftSistersBuffer.intValue() + thisSubtreeLength.intValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static String treeAsStemmedCollocation(Tree t)
/*     */   {
/* 259 */     ArrayList<WordTag> list = getStemmedWordTagsFromTree(t);
/*     */     
/* 261 */     StringBuilder s = new StringBuilder(160);
/* 262 */     WordTag firstWord = (WordTag)list.remove(0);
/* 263 */     s.append(firstWord.word());
/*     */     
/* 265 */     for (WordTag wt : list) {
/* 266 */       s.append("_");
/* 267 */       s.append(wt.word());
/*     */     }
/*     */     
/*     */ 
/* 271 */     return s.toString();
/*     */   }
/*     */   
/*     */   private static String treeAsNonStemmedCollocation(Tree t) {
/* 275 */     ArrayList<WordTag> list = getNonStemmedWordTagsFromTree(t);
/* 276 */     StringBuilder s = new StringBuilder(160);
/* 277 */     WordTag firstWord = (WordTag)list.remove(0);
/* 278 */     s.append(firstWord.word());
/*     */     
/* 280 */     for (WordTag wt : list) {
/* 281 */       s.append("_");
/* 282 */       s.append(wt.word());
/*     */     }
/*     */     
/* 285 */     return s.toString();
/*     */   }
/*     */   
/*     */   private static String mergeLeavesIntoCollocatedString(Tree t) {
/* 289 */     StringBuilder sb = new StringBuilder(160);
/* 290 */     Sentence sent = t.taggedYield();
/* 291 */     for (int i = 0; i < sent.size(); i++) {
/* 292 */       sb.append(sent.getHasWord(i).word() + "_");
/*     */     }
/* 294 */     return sb.substring(0, sb.length() - 1);
/*     */   }
/*     */   
/*     */   private static String mergeLeavesIntoCollocatedString(Tree[] trees) {
/* 298 */     StringBuilder sb = new StringBuilder(160);
/* 299 */     for (Tree t : trees) {
/* 300 */       Sentence sent = t.taggedYield();
/* 301 */       for (int i = 0; i < sent.size(); i++) {
/* 302 */         sb.append(sent.getHasWord(i).word() + "_");
/*     */       }
/*     */     }
/* 305 */     return sb.substring(0, sb.length() - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ArrayList<WordTag> getStemmedWordTagsFromTree(Tree t)
/*     */   {
/* 315 */     ArrayList<WordTag> stemmedWordTags = new ArrayList();
/* 316 */     Sentence s = t.taggedYield();
/* 317 */     for (int i = 0; i < s.size(); i++) {
/* 318 */       TaggedWord w = (TaggedWord)s.getHasWord(i);
/* 319 */       WordTag wt = Morphology.stemStatic(w.word(), w.tag());
/* 320 */       stemmedWordTags.add(wt);
/*     */     }
/* 322 */     return stemmedWordTags;
/*     */   }
/*     */   
/*     */   private static ArrayList<WordTag> getNonStemmedWordTagsFromTree(Tree t) {
/* 326 */     ArrayList<WordTag> wordTags = new ArrayList();
/* 327 */     Sentence s = t.taggedYield();
/* 328 */     for (int i = 0; i < s.size(); i++) {
/* 329 */       TaggedWord w = (TaggedWord)s.getHasWord(i);
/* 330 */       WordTag wt = new WordTag(w.word(), w.tag());
/* 331 */       wordTags.add(wt);
/*     */     }
/* 333 */     return wordTags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean wordNetContains(String s)
/*     */   {
/* 343 */     return this.wnConnect.wordNetContains(s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class Collocation
/*     */   {
/*     */     Pair<Integer, Integer> span;
/*     */     
/*     */     Tree parentNode;
/*     */     
/*     */     Label headLabel;
/*     */     
/*     */     ArrayList<Integer> indicesOfConstituentChildren;
/*     */     
/*     */     String collocationString;
/*     */     
/*     */ 
/*     */     private Collocation(Tree span, ArrayList<Integer> parentNode, String indicesOfConstituentChildren, Label collocationString)
/*     */     {
/* 363 */       this.span = span;
/* 364 */       this.parentNode = parentNode;
/* 365 */       this.collocationString = collocationString;
/* 366 */       this.indicesOfConstituentChildren = indicesOfConstituentChildren;
/* 367 */       this.headLabel = headLabel;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 371 */       return this.collocationString + this.indicesOfConstituentChildren + "/" + this.headLabel;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\CollocationFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */