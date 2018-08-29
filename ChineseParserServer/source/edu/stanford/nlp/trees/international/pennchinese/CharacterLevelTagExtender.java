/*     */ package edu.stanford.nlp.trees.international.pennchinese;
/*     */ 
/*     */ import edu.stanford.nlp.io.IOUtils;
/*     */ import edu.stanford.nlp.io.NumberRangesFileFilter;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.parser.lexparser.ChineseTreebankParserParams;
/*     */ import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
/*     */ import edu.stanford.nlp.parser.lexparser.Options;
/*     */ import edu.stanford.nlp.stats.EquivalenceClassEval;
/*     */ import edu.stanford.nlp.trees.BobChrisTreeNormalizer;
/*     */ import edu.stanford.nlp.trees.MemoryTreebank;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreeTransformer;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.WordCatEqualityChecker;
/*     */ import edu.stanford.nlp.trees.WordCatEquivalenceClasser;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class CharacterLevelTagExtender extends BobChrisTreeNormalizer implements TreeTransformer
/*     */ {
/*     */   private static final boolean useTwoCharTags = false;
/*     */   
/*     */   public CharacterLevelTagExtender()
/*     */   {
/*  38 */     super(new ChineseTreebankLanguagePack());
/*     */   }
/*     */   
/*     */   public CharacterLevelTagExtender(TreebankLanguagePack tlp) {
/*  42 */     super(tlp);
/*     */   }
/*     */   
/*     */   public Tree normalizeWholeTree(Tree tree, TreeFactory tf) {
/*  46 */     return transformTree(super.normalizeWholeTree(tree, tf));
/*     */   }
/*     */   
/*     */ 
/*     */   public Tree transformTree(Tree tree)
/*     */   {
/*  52 */     TreeFactory tf = tree.treeFactory();
/*  53 */     String tag = tree.label().value();
/*  54 */     if (tree.isPreTerminal()) {
/*  55 */       String word = tree.firstChild().label().value();
/*     */       
/*  57 */       List newPreterms = new ArrayList();
/*  58 */       int i = 0; for (int size = word.length(); i < size; i++) {
/*  59 */         String singleCharLabel = new String(new char[] { word.charAt(i) });
/*  60 */         Tree newLeaf = tf.newLeaf(singleCharLabel);
/*     */         
/*     */ 
/*     */         String suffix;
/*     */         
/*     */ 
/*     */         String suffix;
/*     */         
/*     */ 
/*  69 */         if (word.length() == 1) {
/*  70 */           suffix = "_S"; } else { String suffix;
/*  71 */           if (i == 0) {
/*  72 */             suffix = "_B"; } else { String suffix;
/*  73 */             if (i == word.length() - 1) {
/*  74 */               suffix = "_E";
/*     */             } else
/*  76 */               suffix = "_M";
/*     */           }
/*     */         }
/*  79 */         newPreterms.add(tf.newTreeNode(tag + suffix, Collections.singletonList(newLeaf)));
/*     */       }
/*  81 */       return tf.newTreeNode(tag, newPreterms);
/*     */     }
/*  83 */     List newChildren = new ArrayList();
/*  84 */     for (int i = 0; i < tree.children().length; i++) {
/*  85 */       Tree child = tree.children()[i];
/*  86 */       newChildren.add(transformTree(child));
/*     */     }
/*  88 */     return tf.newTreeNode(tag, newChildren);
/*     */   }
/*     */   
/*     */   public Tree untransformTree(Tree tree)
/*     */   {
/*  93 */     TreeFactory tf = tree.treeFactory();
/*  94 */     if (tree.isPrePreTerminal()) {
/*  95 */       if (tree.firstChild().label().value().matches(".*_.")) {
/*  96 */         StringBuffer word = new StringBuffer();
/*  97 */         for (int i = 0; i < tree.children().length; i++) {
/*  98 */           Tree child = tree.children()[i];
/*  99 */           word.append(child.firstChild().label().value());
/*     */         }
/* 101 */         Tree newChild = tf.newLeaf(word.toString());
/* 102 */         tree.setChildren(Collections.singletonList(newChild));
/*     */       }
/*     */     } else {
/* 105 */       for (int i = 0; i < tree.children().length; i++) {
/* 106 */         Tree child = tree.children()[i];
/* 107 */         untransformTree(child);
/*     */       }
/*     */     }
/* 110 */     return tree;
/*     */   }
/*     */   
/*     */   private static void testTransAndUntrans(CharacterLevelTagExtender e, Treebank tb, PrintWriter pw) {
/* 114 */     for (Iterator iter = tb.iterator(); iter.hasNext();) {
/* 115 */       Tree tree = (Tree)iter.next();
/* 116 */       Tree oldTree = tree.deepCopy();
/* 117 */       e.transformTree(tree);
/* 118 */       e.untransformTree(tree);
/* 119 */       if (!tree.equals(oldTree)) {
/* 120 */         pw.println("NOT EQUAL AFTER UNTRANSFORMATION!!!");
/* 121 */         pw.println();
/* 122 */         oldTree.pennPrint(pw);
/* 123 */         pw.println();
/* 124 */         tree.pennPrint(pw);
/* 125 */         pw.println("------------------");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws IOException
/*     */   {
/* 137 */     if (args.length != 3) {
/* 138 */       throw new RuntimeException("args: treebankPath trainNums testNums");
/*     */     }
/*     */     
/* 141 */     ChineseTreebankParserParams ctpp = new ChineseTreebankParserParams();
/* 142 */     ctpp.charTags = true;
/* 143 */     Options op = new Options(ctpp);
/* 144 */     op.doDep = false;
/*     */     LexicalizedParser lp;
/*     */     try
/*     */     {
/* 148 */       FileFilter trainFilt = new NumberRangesFileFilter(args[1], false);
/*     */       
/* 150 */       lp = new LexicalizedParser(args[0], trainFilt, op);
/*     */       try {
/* 152 */         String filename = "chineseCharTagPCFG.ser.gz";
/* 153 */         System.err.println("Writing parser in serialized format to file " + filename + " ");
/* 154 */         System.err.flush();
/* 155 */         ObjectOutputStream out = IOUtils.writeStreamFromString(filename);
/*     */         
/* 157 */         out.writeObject(lp.parserData());
/* 158 */         out.close();
/* 159 */         System.err.println("done.");
/*     */       } catch (IOException ioe) {
/* 161 */         ioe.printStackTrace();
/*     */       }
/*     */     } catch (IllegalArgumentException e) {
/* 164 */       lp = new LexicalizedParser(args[1], op);
/*     */     }
/*     */     
/* 167 */     edu.stanford.nlp.parser.lexparser.Test.maxLength = 90;
/*     */     
/* 169 */     FileFilter testFilt = new NumberRangesFileFilter(args[2], false);
/* 170 */     MemoryTreebank testTreebank = ctpp.memoryTreebank();
/* 171 */     testTreebank.loadPath(new File(args[0]), testFilt);
/* 172 */     PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("out.chi"), "GB18030"), true);
/* 173 */     WordCatEquivalenceClasser eqclass = new WordCatEquivalenceClasser();
/* 174 */     WordCatEqualityChecker eqcheck = new WordCatEqualityChecker();
/* 175 */     EquivalenceClassEval eval = new EquivalenceClassEval(eqclass, eqcheck);
/*     */     
/* 177 */     System.out.println("Testing...");
/* 178 */     for (Iterator iterator = testTreebank.iterator(); iterator.hasNext();) {
/* 179 */       Tree gold = (Tree)iterator.next();
/*     */       try {
/* 181 */         lp.parse(gold.yield());
/*     */       } catch (Exception e) {
/* 183 */         e.printStackTrace(); }
/* 184 */       continue;
/*     */       
/* 186 */       gold = gold.firstChild();
/* 187 */       pw.println(gold.preTerminalYield());
/* 188 */       pw.println(gold.yield());
/* 189 */       gold.pennPrint(pw);
/*     */       
/* 191 */       Tree tree = lp.getBestParse();
/* 192 */       pw.println(tree.preTerminalYield());
/* 193 */       pw.println(tree.yield());
/* 194 */       tree.pennPrint(pw);
/*     */       
/*     */ 
/*     */ 
/* 198 */       eval.displayLast();
/*     */     }
/* 200 */     System.out.println();
/* 201 */     System.out.println();
/* 202 */     eval.display();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\CharacterLevelTagExtender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */