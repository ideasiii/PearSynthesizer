/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.StringLabel;
/*     */ import edu.stanford.nlp.trees.tregex.TregexPattern;
/*     */ import edu.stanford.nlp.trees.tregex.tsurgeon.Tsurgeon;
/*     */ import edu.stanford.nlp.trees.tregex.tsurgeon.TsurgeonPattern;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CoordinationTransformer
/*     */   implements TreeTransformer
/*     */ {
/*  24 */   boolean VERBOSE = false;
/*  25 */   boolean notDone = true;
/*  26 */   TreeTransformer tn = new DependencyTreeTransformer();
/*  27 */   TreeTransformer qp = new QPTreeTransformer();
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
/*     */   public Tree transformTree(Tree t)
/*     */   {
/*  41 */     this.tn.transformTree(t);
/*  42 */     Tree tt = UCPtransform(t);
/*  43 */     Tree ttt = CCtransform(tt);
/*  44 */     return this.qp.transformTree(ttt);
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
/*     */ 
/*     */   public Tree UCPtransform(Tree t)
/*     */   {
/*  59 */     Tree firstChild = t.firstChild();
/*  60 */     if (firstChild != null)
/*     */     {
/*  62 */       List<Pair<TregexPattern, TsurgeonPattern>> ops = new ArrayList();
/*     */       
/*     */ 
/*  65 */       String patternMatch = "UCP=ucp <, /^JJ|ADJP/";
/*     */       
/*  67 */       String patternMatch2 = "UCP=ucp <, (DT $+ /^JJ|ADJP/)";
/*  68 */       String operation = "relabel ucp ADJP";
/*  69 */       TregexPattern matchPattern = null;
/*  70 */       TregexPattern matchPattern2 = null;
/*     */       try {
/*  72 */         matchPattern = TregexPattern.compile(patternMatch);
/*     */       } catch (Exception e) {
/*  74 */         System.err.println("Error compiling " + patternMatch);
/*     */       }
/*     */       try {
/*  77 */         matchPattern2 = TregexPattern.compile(patternMatch2);
/*     */       } catch (Exception e) {
/*  79 */         System.err.println("Error compiling " + patternMatch2);
/*     */       }
/*  81 */       TsurgeonPattern p = Tsurgeon.parseOperation(operation);
/*  82 */       ops.add(new Pair(matchPattern, p));
/*  83 */       ops.add(new Pair(matchPattern2, p));
/*     */       
/*     */ 
/*     */ 
/*  87 */       patternMatch = "UCP=ucp <, /^N/";
/*  88 */       patternMatch2 = "UCP=ucp <, (DT $+ /^N/)";
/*  89 */       operation = "relabel ucp NP";
/*  90 */       matchPattern = null;
/*     */       try {
/*  92 */         matchPattern = TregexPattern.compile(patternMatch);
/*     */       } catch (Exception e) {
/*  94 */         System.err.println("Error compiling match pattern");
/*     */       }
/*     */       try {
/*  97 */         matchPattern2 = TregexPattern.compile(patternMatch2);
/*     */       } catch (Exception e) {
/*  99 */         System.err.println("Error compiling " + patternMatch2);
/*     */       }
/* 101 */       p = Tsurgeon.parseOperation(operation);
/* 102 */       ops.add(new Pair(matchPattern, p));
/* 103 */       ops.add(new Pair(matchPattern2, p));
/*     */       
/* 105 */       Tree result = Tsurgeon.processPatternsOnTree(ops, t);
/* 106 */       return result;
/*     */     }
/* 108 */     return t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree CCtransform(Tree t)
/*     */   {
/* 120 */     this.notDone = true;
/* 121 */     Tree toReturn = new LabeledScoredTreeNode();
/* 122 */     while (this.notDone) {
/* 123 */       Tree cc = findCCparent(t, t);
/* 124 */       if (cc != null) {
/* 125 */         toReturn = cc;
/* 126 */         t = cc;
/*     */       } else {
/* 128 */         this.notDone = false;
/* 129 */         toReturn = t;
/*     */       }
/*     */     }
/* 132 */     return toReturn;
/*     */   }
/*     */   
/*     */   private String getHeadTag(Tree t) {
/* 136 */     if (t.value().startsWith("NN"))
/* 137 */       return new String("NP");
/* 138 */     if (t.value().startsWith("JJ")) {
/* 139 */       return new String("ADJP");
/*     */     }
/* 141 */     return new String("NP");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Tree transformCC(Tree t, int ccIndex)
/*     */   {
/* 149 */     Tree[] ccSiblings = t.children();
/*     */     
/*     */ 
/* 152 */     List<Integer> list = new ArrayList();
/* 153 */     for (int i = ccIndex + 1; i < ccSiblings.length; i++) {
/* 154 */       if (ccSiblings[i].value().startsWith("CC")) {
/* 155 */         list.add(new Integer(i));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 160 */     if ((ccIndex == 1) && (!ccSiblings[(ccIndex - 1)].value().equals("NP")) && (!ccSiblings[(ccIndex - 1)].value().equals("ADJP")) && (!ccSiblings[(ccIndex - 1)].value().equals("NNS"))) {
/* 161 */       String leftHead = getHeadTag(ccSiblings[(ccIndex - 1)]);
/*     */       
/* 163 */       Tree left = new LabeledScoredTreeNode(new StringLabel(leftHead), null);
/* 164 */       for (int i = 0; i < ccIndex + 2; i++) {
/* 165 */         left.addChild(ccSiblings[i]);
/*     */       }
/*     */       
/* 168 */       if (this.VERBOSE) {
/* 169 */         System.out.println("print left tree");
/* 170 */         left.pennPrint();
/* 171 */         System.out.println();
/*     */       }
/*     */       
/*     */ 
/* 175 */       for (int i = 0; i < ccIndex + 2; i++) {
/* 176 */         t.removeChild(0);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 181 */       if (!list.isEmpty()) {
/* 182 */         boolean comma = false;
/* 183 */         int index = ((Integer)list.get(0)).intValue();
/* 184 */         if (this.VERBOSE) System.err.println("more CC index " + index);
/* 185 */         if (ccSiblings[(index - 1)].value().equals(",")) {
/* 186 */           index -= 1;
/* 187 */           comma = true;
/*     */         }
/* 189 */         if (this.VERBOSE) System.err.println("more CC index " + index);
/* 190 */         String head = getHeadTag(ccSiblings[(index - 1)]);
/* 191 */         Tree tree = new LabeledScoredTreeNode(new StringLabel(head), null);
/* 192 */         tree.addChild(0, left);
/*     */         
/* 194 */         int k = 1;
/* 195 */         for (int j = ccIndex + 2; j < index; j++) {
/* 196 */           if (this.VERBOSE) ccSiblings[j].pennPrint();
/* 197 */           t.removeChild(0);
/* 198 */           tree.addChild(k, ccSiblings[j]);
/* 199 */           k++;
/*     */         }
/*     */         
/* 202 */         if (this.VERBOSE) {
/* 203 */           System.out.println("print t");
/* 204 */           t.pennPrint();
/*     */           
/* 206 */           System.out.println("print tree");
/* 207 */           tree.pennPrint();
/* 208 */           System.out.println();
/*     */         }
/* 210 */         t.addChild(0, tree);
/*     */       } else {
/* 212 */         t.addChild(0, left);
/*     */       }
/*     */       
/*     */     }
/* 216 */     else if ((ccIndex == 2) && (ccSiblings[0].value().startsWith("DT")) && (!ccSiblings[(ccIndex - 1)].value().equals("NNS")) && ((ccSiblings.length == 5) || ((!list.isEmpty()) && (((Integer)list.get(0)).intValue() == 5)))) {
/* 217 */       String head = getHeadTag(ccSiblings[(ccIndex - 1)]);
/*     */       
/* 219 */       Tree child = new LabeledScoredTreeNode(new StringLabel(head), null);
/* 220 */       for (int i = 1; i < ccIndex + 2; i++) {
/* 221 */         child.addChild(ccSiblings[i]);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 227 */       for (int i = 1; i < ccIndex + 2; i++) {
/* 228 */         t.removeChild(1);
/*     */       }
/*     */       
/* 231 */       t.addChild(1, child);
/*     */ 
/*     */ 
/*     */     }
/* 235 */     else if ((ccIndex > 2) && (ccSiblings[(ccIndex - 2)].value().equals(",")) && (!ccSiblings[(ccIndex - 1)].value().equals("NNS"))) {
/* 236 */       String head = getHeadTag(ccSiblings[(ccIndex - 1)]);
/* 237 */       Tree child = new LabeledScoredTreeNode(new StringLabel(head), null);
/* 238 */       for (int i = ccIndex - 3; i < ccIndex + 2; i++) {
/* 239 */         child.addChild(ccSiblings[i]);
/*     */       }
/* 241 */       int i = ccIndex - 4;
/* 242 */       while ((i > 0) && (ccSiblings[i].value().equals(","))) {
/* 243 */         child.addChild(0, ccSiblings[i]);
/* 244 */         child.addChild(0, ccSiblings[(i - 1)]);
/* 245 */         i -= 2;
/*     */       }
/*     */       
/* 248 */       if (i < 0) {
/* 249 */         i = -1;
/*     */       }
/*     */       
/*     */ 
/* 253 */       for (int j = i + 1; j < ccIndex + 2; j++) {
/* 254 */         t.removeChild(i + 1);
/*     */       }
/*     */       
/* 257 */       t.addChild(i + 1, child);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 263 */       boolean commaLeft = false;
/* 264 */       boolean commaRight = false;
/* 265 */       boolean preconj = false;
/* 266 */       int indexBegin = 0;
/* 267 */       Tree conjT = new LabeledScoredTreeNode(new StringLabel("CC"), null);
/*     */       
/* 269 */       String leftHead = getHeadTag(ccSiblings[(ccIndex - 1)]);
/* 270 */       Tree left = new LabeledScoredTreeNode(new StringLabel(leftHead), null);
/*     */       
/*     */ 
/* 273 */       Tree first = ccSiblings[0];
/* 274 */       String leaf = first.children()[0].value().toLowerCase();
/* 275 */       if ((leaf.equals("either")) || (leaf.equals("neither")) || (leaf.equals("both"))) {
/* 276 */         preconj = true;
/* 277 */         indexBegin = 1;
/* 278 */         conjT.addChild(first.children()[0]);
/*     */       }
/*     */       
/* 281 */       for (int i = indexBegin; i < ccIndex - 1; i++) {
/* 282 */         left.addChild(ccSiblings[i]);
/*     */       }
/*     */       
/* 285 */       if (ccSiblings[(ccIndex - 1)].value().equals(",")) {
/* 286 */         commaLeft = true;
/*     */       } else {
/* 288 */         left.addChild(ccSiblings[(ccIndex - 1)]);
/*     */       }
/*     */       
/*     */ 
/* 292 */       Tree cc = ccSiblings[ccIndex];
/*     */       
/*     */       int nextCC;
/*     */       int nextCC;
/* 296 */       if (list.isEmpty()) {
/* 297 */         nextCC = ccSiblings.length;
/*     */       } else {
/* 299 */         nextCC = ((Integer)list.get(0)).intValue();
/*     */       }
/* 301 */       String rightHead = getHeadTag(ccSiblings[(nextCC - 1)]);
/* 302 */       Tree right = new LabeledScoredTreeNode(new StringLabel(rightHead), null);
/* 303 */       for (int i = ccIndex + 1; i < nextCC - 1; i++) {
/* 304 */         right.addChild(ccSiblings[i]);
/*     */       }
/*     */       
/* 307 */       if (ccSiblings[(nextCC - 1)].value().equals(",")) {
/* 308 */         commaRight = true;
/*     */       } else {
/* 310 */         right.addChild(ccSiblings[(nextCC - 1)]);
/*     */       }
/*     */       
/*     */ 
/* 314 */       for (int i = 0; i < nextCC; i++) {
/* 315 */         t.removeChild(0);
/*     */       }
/* 317 */       if (!list.isEmpty()) {
/* 318 */         Tree tree = new LabeledScoredTreeNode(new StringLabel("NP"), null);
/* 319 */         if (preconj) {
/* 320 */           tree.addChild(conjT);
/*     */         }
/* 322 */         tree.addChild(left);
/* 323 */         if (commaLeft) {
/* 324 */           tree.addChild(ccSiblings[(ccIndex - 1)]);
/*     */         }
/* 326 */         tree.addChild(cc);
/* 327 */         tree.addChild(right);
/* 328 */         if (commaRight) {
/* 329 */           t.addChild(0, ccSiblings[(nextCC - 1)]);
/*     */         }
/* 331 */         t.addChild(0, tree);
/*     */       } else {
/* 333 */         if (preconj) {
/* 334 */           t.addChild(conjT);
/*     */         }
/* 336 */         t.addChild(left);
/* 337 */         if (commaLeft) {
/* 338 */           t.addChild(ccSiblings[(ccIndex - 1)]);
/*     */         }
/* 340 */         t.addChild(cc);
/* 341 */         t.addChild(right);
/* 342 */         if (commaRight) {
/* 343 */           t.addChild(ccSiblings[(nextCC - 1)]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 348 */     return t;
/*     */   }
/*     */   
/*     */   private boolean notNP(List<Tree> children, int ccIndex) {
/* 352 */     boolean toReturn = true;
/* 353 */     for (int i = ccIndex; i < children.size(); i++) {
/* 354 */       if (((Tree)children.get(i)).value().startsWith("NP")) {
/* 355 */         toReturn = false;
/* 356 */         break;
/*     */       }
/*     */     }
/* 359 */     return toReturn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Tree findCCparent(Tree t, Tree root)
/*     */   {
/* 369 */     if (t.isPreTerminal()) {
/* 370 */       if (t.value().startsWith("CC")) {
/* 371 */         Tree parent = t.parent(root);
/* 372 */         if (parent.value().startsWith("NP")) {
/* 373 */           List<Tree> children = parent.getChildrenAsList();
/*     */           
/* 375 */           int ccIndex = children.indexOf(t);
/* 376 */           if ((children.size() > ccIndex + 2) && (notNP(children, ccIndex)) && (ccIndex != 0)) {
/* 377 */             Tree newChild = transformCC(parent, ccIndex);
/* 378 */             parent = newChild;
/* 379 */             return root;
/*     */           }
/*     */         }
/*     */       }
/*     */     } else {
/* 384 */       for (Tree child : t.getChildrenAsList()) {
/* 385 */         Tree cur = findCCparent(child, root);
/* 386 */         if (cur != null) {
/* 387 */           return cur;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 392 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 398 */     CoordinationTransformer transformer = new CoordinationTransformer();
/* 399 */     Treebank tb = new MemoryTreebank();
/* 400 */     Properties props = StringUtils.argsToProperties(args);
/* 401 */     String treeFileName = props.getProperty("treeFile");
/*     */     
/* 403 */     if (treeFileName != null) {
/*     */       try {
/* 405 */         TreeReader tr = new PennTreeReader(new BufferedReader(new InputStreamReader(new FileInputStream(treeFileName))), new LabeledScoredTreeFactory());
/*     */         Tree t;
/* 407 */         while ((t = tr.readTree()) != null) {
/* 408 */           tb.add(t);
/*     */         }
/*     */       } catch (IOException e) {
/* 411 */         throw new RuntimeException("File problem: " + e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 416 */     for (Tree t : tb) {
/* 417 */       System.out.println("Original tree");
/* 418 */       t.pennPrint();
/* 419 */       System.out.println();
/* 420 */       System.out.println("Tree transformed");
/* 421 */       Tree tree = transformer.transformTree(t);
/* 422 */       tree.pennPrint();
/* 423 */       System.out.println();
/* 424 */       System.out.println("----------------------------");
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\CoordinationTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */