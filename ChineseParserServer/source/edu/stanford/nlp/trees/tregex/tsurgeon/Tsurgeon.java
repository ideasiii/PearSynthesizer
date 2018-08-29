/*     */ package edu.stanford.nlp.trees.tregex.tsurgeon;
/*     */ 
/*     */ import edu.stanford.nlp.trees.DiskTreebank;
/*     */ import edu.stanford.nlp.trees.PennTreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreePrint;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.tregex.TreeMatcher.TRegexTreeReaderFactory;
/*     */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*     */ import edu.stanford.nlp.trees.tregex.TregexPattern;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class Tsurgeon
/*     */ {
/*  53 */   static boolean verbose = false;
/*     */   
/*  55 */   static Pattern emptyLinePattern = Pattern.compile("^\\s*$");
/*  56 */   static String commentIntroducingCharacter = "%";
/*  57 */   static Pattern commentPattern = Pattern.compile(commentIntroducingCharacter + ".*$");
/*  58 */   static Pattern escapedCommentCharacterPattern = Pattern.compile("\\" + commentIntroducingCharacter);
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
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/* 116 */     String encoding = "UTF-8";
/* 117 */     String encodingOption = "-encoding";
/* 118 */     if (args.length == 0) {
/* 119 */       System.err.println("Usage: java edu.stanford.nlp.trees.tregex.tsurgeon.Tsurgeon [-s] -treeFile <file-with-trees> [-po <matching-pattern> <operation>] <operation-file-1> <operation-file-1> ... <operation-file-n>");
/* 120 */       System.exit(0);
/*     */     }
/* 122 */     String treePrintFormats = "";
/* 123 */     String singleLineOption = "-s";
/* 124 */     String verboseOption = "-v";
/* 125 */     String matchedOption = "-m";
/* 126 */     String patternOperationOption = "-po";
/* 127 */     String treeFileOption = "-treeFile";
/* 128 */     Map<String, Integer> flagMap = new HashMap();
/* 129 */     flagMap.put(patternOperationOption, Integer.valueOf(2));
/* 130 */     flagMap.put(treeFileOption, Integer.valueOf(1));
/* 131 */     flagMap.put(singleLineOption, Integer.valueOf(0));
/* 132 */     flagMap.put(encodingOption, Integer.valueOf(1));
/* 133 */     Map<String, String[]> argsMap = StringUtils.argsToMap(args, flagMap);
/* 134 */     args = (String[])argsMap.get(null);
/*     */     
/* 136 */     if (argsMap.containsKey(verboseOption)) verbose = true;
/* 137 */     if (argsMap.containsKey(singleLineOption)) treePrintFormats = treePrintFormats + "oneline,"; else treePrintFormats = treePrintFormats + "penn,";
/* 138 */     if (argsMap.containsKey(encodingOption)) { encoding = ((String[])argsMap.get(encodingOption))[0];
/*     */     }
/* 140 */     TreePrint tp = new TreePrint(treePrintFormats, new PennTreebankLanguagePack());
/* 141 */     PrintWriter pwOut = new PrintWriter(new OutputStreamWriter(System.out, encoding), true);
/* 142 */     tp.setPrintWriter(pwOut);
/*     */     
/* 144 */     Treebank trees = new DiskTreebank(new TreeMatcher.TRegexTreeReaderFactory(), encoding);
/* 145 */     if (argsMap.containsKey(treeFileOption)) {
/* 146 */       trees.loadPath(((String[])argsMap.get(treeFileOption))[0]);
/*     */     }
/* 148 */     List<Pair<TregexPattern, TsurgeonPattern>> ops = new ArrayList();
/*     */     
/*     */ 
/* 151 */     if (argsMap.containsKey(patternOperationOption)) {
/* 152 */       TregexPattern matchPattern = TregexPattern.compile(((String[])argsMap.get(patternOperationOption))[0]);
/* 153 */       TsurgeonPattern p = parseOperation(((String[])argsMap.get(patternOperationOption))[1]);
/* 154 */       ops.add(new Pair(matchPattern, p));
/*     */     }
/*     */     else {
/* 157 */       for (String arg : args) {
/* 158 */         Pair<TregexPattern, TsurgeonPattern> pair = getOperationFromFile(arg);
/* 159 */         if (verbose)
/* 160 */           System.err.println(pair.second());
/* 161 */         ops.add(pair);
/*     */       }
/*     */     }
/* 164 */     for (Tree t : trees) {
/* 165 */       Tree original = t.deeperCopy();
/* 166 */       Tree result = processPatternsOnTree(ops, t);
/* 167 */       if ((argsMap.containsKey(matchedOption)) && (matchedOnTree)) {
/* 168 */         pwOut.println("Operated on: ");
/* 169 */         disposeOfTree(original, tp, pwOut);
/* 170 */         pwOut.println("Result: ");
/*     */       }
/* 172 */       disposeOfTree(result, tp, pwOut);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void disposeOfTree(Tree t, TreePrint tp, PrintWriter pw) {
/* 177 */     if (t == null) {
/* 178 */       System.out.println("null");
/*     */     } else
/* 180 */       tp.printTree(t, pw);
/*     */   }
/*     */   
/*     */   public static Pair<TregexPattern, TsurgeonPattern> getOperationFromFile(String arg) throws IOException {
/* 184 */     BufferedReader r = new BufferedReader(new FileReader(arg));
/* 185 */     StringBuilder matchString = new StringBuilder();
/* 186 */     String thisLine; while (((thisLine = r.readLine()) != null) && 
/* 187 */       (!emptyLinePattern.matcher(thisLine).matches()))
/*     */     {
/*     */ 
/* 190 */       matchString.append(thisLine);
/*     */     }
/*     */     TregexPattern matchPattern;
/*     */     try
/*     */     {
/* 195 */       matchPattern = TregexPattern.compile(matchString.toString());
/*     */     }
/*     */     catch (edu.stanford.nlp.trees.tregex.ParseException e) {
/* 198 */       System.err.println("Error parsing your tregex pattern:\n" + matchString);
/* 199 */       throw new RuntimeException(e);
/*     */     }
/* 201 */     List<TsurgeonPattern> ps = new ArrayList();
/* 202 */     String thisLine; while ((thisLine = r.readLine()) != null) {
/* 203 */       Matcher m = commentPattern.matcher(thisLine);
/* 204 */       thisLine = m.replaceFirst("");
/* 205 */       Matcher m1 = escapedCommentCharacterPattern.matcher(thisLine);
/* 206 */       thisLine = m1.replaceAll(commentIntroducingCharacter);
/* 207 */       if (!emptyLinePattern.matcher(thisLine).matches())
/*     */       {
/*     */         try
/*     */         {
/* 211 */           ps.add(TsurgeonParser.parse(thisLine));
/*     */         }
/*     */         catch (ParseException e) {
/* 214 */           System.err.println("Error parsing your tsurgeon operation:\n" + thisLine);
/* 215 */           throw new RuntimeException(e.toString());
/*     */         } }
/*     */     }
/* 218 */     TsurgeonPattern collectedPattern = collectOperations(ps);
/* 219 */     return new Pair(matchPattern, collectedPattern);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static List<Tree> processPatternOnTrees(TregexPattern matchPattern, TsurgeonPattern p, Collection<Tree> inputTrees)
/*     */   {
/* 230 */     List<Tree> result = new ArrayList();
/* 231 */     for (Tree tree : inputTrees)
/* 232 */       result.add(processPattern(matchPattern, p, tree));
/* 233 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Tree processPattern(TregexPattern matchPattern, TsurgeonPattern p, Tree t)
/*     */   {
/* 244 */     TregexMatcher m = matchPattern.matcher(t);
/* 245 */     while (m.find()) {
/* 246 */       t = p.evaluate(t, m);
/* 247 */       if (t == null)
/*     */         break;
/* 249 */       m = matchPattern.matcher(t);
/*     */     }
/* 251 */     return t;
/*     */   }
/*     */   
/* 254 */   private static boolean matchedOnTree = false;
/*     */   
/*     */   public static Tree processPatternsOnTree(List<Pair<TregexPattern, TsurgeonPattern>> ops, Tree t) {
/* 257 */     matchedOnTree = false;
/* 258 */     for (Pair<TregexPattern, TsurgeonPattern> op : ops) {
/*     */       try {
/* 260 */         TregexMatcher m = ((TregexPattern)op.first()).matcher(t);
/* 261 */         while (m.find()) {
/* 262 */           matchedOnTree = true;
/* 263 */           t = ((TsurgeonPattern)op.second()).evaluate(t, m);
/* 264 */           if (t == null) {
/* 265 */             return null;
/*     */           }
/* 267 */           m = ((TregexPattern)op.first()).matcher(t);
/*     */         }
/*     */       } catch (NullPointerException npe) {
/* 270 */         throw new RuntimeException("Tsurgeon.processPatternsOnTree failed to match label for pattern: " + op.first() + ", " + op.second(), npe);
/*     */       }
/*     */     }
/* 273 */     return t;
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
/*     */ 
/*     */ 
/*     */   public static TsurgeonPattern parseOperation(String operationString)
/*     */   {
/*     */     try
/*     */     {
/* 292 */       return new TsurgeonPatternRoot(new TsurgeonPattern[] { TsurgeonParser.parse(operationString) });
/*     */     }
/*     */     catch (ParseException e) {
/* 295 */       throw new IllegalArgumentException("Ill-formed operation string: " + operationString, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TsurgeonPattern collectOperations(List<TsurgeonPattern> patterns)
/*     */   {
/* 308 */     return new TsurgeonPatternRoot((TsurgeonPattern[])patterns.toArray(TsurgeonPattern.EMPTY_TSURGEON_ARRAY));
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\Tsurgeon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */