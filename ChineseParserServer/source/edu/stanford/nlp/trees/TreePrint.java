/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.MapLabel;
/*     */ import edu.stanford.nlp.ling.Sentence;
/*     */ import edu.stanford.nlp.ling.TaggedWord;
/*     */ import edu.stanford.nlp.parser.lexparser.TreebankLangParserParams;
/*     */ import edu.stanford.nlp.process.Function;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import edu.stanford.nlp.util.ScoredObject;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import edu.stanford.nlp.util.XMLUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TreePrint
/*     */ {
/*     */   public static final String rootLabelOnlyFormat = "rootSymbolOnly";
/*  28 */   public static final String[] outputTreeFormats = { "penn", "oneline", "rootSymbolOnly", "words", "wordsAndTags", "dependencies", "typedDependencies", "typedDependenciesCollapsed", "latexTree", "collocations", "semanticGraph" };
/*     */   
/*     */ 
/*     */   public static final String headMark = "=H";
/*     */   
/*     */ 
/*     */   private Properties formats;
/*     */   
/*     */ 
/*     */   private Properties options;
/*     */   
/*     */ 
/*     */   private boolean markHeadNodes;
/*     */   
/*     */ 
/*     */   private boolean lexicalize;
/*     */   
/*     */ 
/*     */   private boolean stem;
/*     */   
/*     */ 
/*     */   private boolean transChinese;
/*     */   
/*     */ 
/*     */   private HeadFinder hf;
/*     */   
/*     */ 
/*     */   private TreebankLanguagePack tlp;
/*     */   
/*     */ 
/*     */   private WordStemmer stemmer;
/*     */   
/*     */ 
/*     */   private Filter<Dependency> dependencyFilter;
/*     */   
/*     */ 
/*     */   private GrammaticalStructureFactory gsf;
/*     */   
/*     */ 
/*     */   private static WordNetConnection wnc;
/*     */   
/*     */ 
/*  70 */   private PrintWriter pw = new PrintWriter(System.out, true);
/*     */   
/*     */ 
/*     */ 
/*     */   public TreePrint(String formats)
/*     */   {
/*  76 */     this(formats, "", new PennTreebankLanguagePack());
/*     */   }
/*     */   
/*     */   public TreePrint(String formats, TreebankLanguagePack tlp) {
/*  80 */     this(formats, "", tlp);
/*     */   }
/*     */   
/*     */   public TreePrint(String formats, String options, TreebankLanguagePack tlp) {
/*  84 */     this(formats, options, tlp, new CollinsHeadFinder());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreePrint(String formatString, String optionsString, TreebankLanguagePack tlp, HeadFinder hf)
/*     */   {
/* 109 */     this.formats = StringUtils.stringToProperties(formatString);
/* 110 */     this.options = StringUtils.stringToProperties(optionsString);
/* 111 */     List<String> okOutputs = java.util.Arrays.asList(outputTreeFormats);
/* 112 */     for (Object formObj : this.formats.keySet()) {
/* 113 */       String format = (String)formObj;
/* 114 */       if (!okOutputs.contains(format)) {
/* 115 */         throw new RuntimeException("Error: output tree format " + format + " not supported");
/*     */       }
/*     */     }
/*     */     
/* 119 */     setHeadFinder(hf);
/* 120 */     this.tlp = tlp;
/*     */     
/* 122 */     boolean includePunctuationDependencies = propertyToBoolean(this.options, "includePunctuationDependencies");
/*     */     Filter<String> puncWordFilter;
/*     */     Filter<String> puncWordFilter;
/* 125 */     if (includePunctuationDependencies) {
/* 126 */       this.dependencyFilter = edu.stanford.nlp.util.Filters.acceptFilter();
/* 127 */       puncWordFilter = edu.stanford.nlp.util.Filters.acceptFilter();
/*     */     } else {
/* 129 */       this.dependencyFilter = new Dependencies.DependentPuncTagRejectFilter(tlp.punctuationTagRejectFilter());
/* 130 */       puncWordFilter = tlp.punctuationWordRejectFilter();
/*     */     }
/* 132 */     this.stem = propertyToBoolean(this.options, "stem");
/* 133 */     if (this.stem) {
/* 134 */       this.stemmer = new WordStemmer();
/*     */     }
/* 136 */     if ((this.formats.containsKey("typedDependenciesCollapsed")) || (this.formats.containsKey("typedDependencies")))
/*     */     {
/* 138 */       this.gsf = tlp.grammaticalStructureFactory(puncWordFilter);
/*     */     }
/*     */     
/* 141 */     this.lexicalize = propertyToBoolean(this.options, "lexicalize");
/* 142 */     this.markHeadNodes = propertyToBoolean(this.options, "markHeadNodes");
/* 143 */     this.transChinese = propertyToBoolean(this.options, "transChinese");
/*     */   }
/*     */   
/*     */   private static boolean propertyToBoolean(Properties prop, String key)
/*     */   {
/* 148 */     return Boolean.parseBoolean(prop.getProperty(key));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printTree(Tree t)
/*     */   {
/* 156 */     printTree(t, this.pw);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printTree(Tree t, PrintWriter pw)
/*     */   {
/* 165 */     printTree(t, "", pw);
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
/*     */   public void printTree(Tree t, String id, PrintWriter pw)
/*     */   {
/* 182 */     boolean inXml = propertyToBoolean(this.options, "xml");
/* 183 */     if (t == null)
/*     */     {
/* 185 */       if (inXml) {
/* 186 */         pw.print("<s");
/* 187 */         if ((id != null) && (!"".equals(id))) {
/* 188 */           pw.print(" id=\"" + XMLUtils.escapeXML(id) + "\"");
/*     */         }
/* 190 */         pw.println(" skipped=\"true\"/>");
/* 191 */         pw.println();
/*     */       } else {
/* 193 */         pw.println("SENTENCE_SKIPPED_OR_UNPARSABLE");
/*     */       }
/*     */     } else {
/* 196 */       if (inXml) {
/* 197 */         pw.print("<s");
/* 198 */         if ((id != null) && (!"".equals(id))) {
/* 199 */           pw.print(" id=\"" + XMLUtils.escapeXML(id) + "\"");
/*     */         }
/* 201 */         pw.println(">");
/*     */       }
/* 203 */       printTreeInternal(t, pw, inXml);
/* 204 */       if (inXml) {
/* 205 */         pw.println("</s>");
/* 206 */         pw.println();
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printTrees(List<ScoredObject<Tree>> trees, String id, PrintWriter pw)
/*     */   {
/* 225 */     boolean inXml = propertyToBoolean(this.options, "xml");
/* 226 */     int ii = 0;
/* 227 */     for (ScoredObject<Tree> tp : trees) {
/* 228 */       ii++;
/* 229 */       Tree t = (Tree)tp.object();
/* 230 */       double score = tp.score();
/*     */       
/* 232 */       if (t == null)
/*     */       {
/* 234 */         if (inXml) {
/* 235 */           pw.print("<s");
/* 236 */           if ((id != null) && (!"".equals(id))) {
/* 237 */             pw.print(" id=\"" + XMLUtils.escapeXML(id) + "\"");
/*     */           }
/* 239 */           pw.print(" n=\"" + ii + "\"");
/* 240 */           pw.print(" score=\"" + score + "\"");
/* 241 */           pw.println(" skipped=\"true\"/>");
/* 242 */           pw.println();
/*     */         } else {
/* 244 */           pw.println("SENTENCE_SKIPPED_OR_UNPARSABLE Parse #" + ii + " with score " + score);
/*     */         }
/*     */       } else {
/* 247 */         if (inXml) {
/* 248 */           pw.print("<s");
/* 249 */           if ((id != null) && (!"".equals(id))) {
/* 250 */             pw.print(" id=\"" + XMLUtils.escapeXML(id) + "\"");
/*     */           }
/* 252 */           pw.print(" n=\"" + ii + "\"");
/* 253 */           pw.print(" score=\"" + score + "\"");
/* 254 */           pw.println(">");
/*     */         } else {
/* 256 */           pw.println("# Parse " + ii + " with score " + score);
/*     */         }
/* 258 */         printTreeInternal(t, pw, inXml);
/* 259 */         if (inXml) {
/* 260 */           pw.println("</s>");
/* 261 */           pw.println();
/*     */         }
/*     */       }
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
/*     */ 
/*     */   private void printTreeInternal(Tree t, PrintWriter pw, boolean inXml)
/*     */   {
/* 277 */     Tree outputTree = t;
/* 278 */     if (this.formats.containsKey("words")) {
/* 279 */       if (inXml) {
/* 280 */         Sentence<HasWord> sentUnstemmed = outputTree.yield();
/* 281 */         pw.println("  <words>");
/* 282 */         int i = 1;
/* 283 */         for (HasWord w : sentUnstemmed) {
/* 284 */           pw.println("    <word ind=\"" + i + "\">" + XMLUtils.escapeXML(w.word()) + "</word>");
/* 285 */           i++;
/*     */         }
/* 287 */         pw.println("  </words>");
/*     */       } else {
/* 289 */         pw.println(outputTree.yield().toString(false));
/* 290 */         pw.println();
/*     */       }
/*     */     }
/*     */     
/* 294 */     if (propertyToBoolean(this.options, "removeTopBracket")) {
/* 295 */       String s = outputTree.label().value();
/* 296 */       if (this.tlp.isStartSymbol(s)) {
/* 297 */         if (outputTree.isUnaryRewrite()) {
/* 298 */           outputTree = outputTree.firstChild();
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 303 */           System.err.println("TreePrint: can't remove top bracket: not unary");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 311 */     if (this.stem) {
/* 312 */       this.stemmer.visitTree(outputTree);
/*     */     }
/* 314 */     if (this.lexicalize) {
/* 315 */       Function<Tree, Tree> a = TreeFunctions.getLabeledTreeToCategoryWordTagTreeFunction();
/*     */       
/* 317 */       outputTree = (Tree)a.apply(outputTree);
/* 318 */       outputTree.percolateHeads(this.hf);
/*     */     }
/*     */     
/* 321 */     if (this.formats.containsKey("collocations"))
/*     */     {
/*     */ 
/*     */ 
/* 325 */       if (wnc == null) {
/*     */         try {
/* 327 */           Class cl = Class.forName("edu.stanford.nlp.trees.WordNetInstance");
/* 328 */           wnc = (WordNetConnection)cl.newInstance();
/*     */         } catch (Exception e) {
/* 330 */           e.printStackTrace();
/* 331 */           System.err.println("Couldn't open WordNet Connection.  Aborting collocation detection.");
/* 332 */           wnc = null;
/*     */         }
/*     */       }
/* 335 */       if (wnc != null) {
/* 336 */         CollocationFinder cf = new CollocationFinder(outputTree, wnc, this.hf);
/* 337 */         outputTree = cf.getMangledTree();
/*     */       } else {
/* 339 */         System.err.println("ERROR: WordNetConnection unavailable for collocations.");
/*     */       }
/*     */     }
/*     */     
/* 343 */     if (!this.lexicalize) {
/* 344 */       Function<Tree, Tree> a = TreeFunctions.getLabeledTreeToStringLabeledTreeFunction();
/*     */       
/* 346 */       outputTree = (Tree)a.apply(outputTree);
/*     */     }
/*     */     
/* 349 */     Tree outputPSTree = outputTree;
/*     */     
/* 351 */     if (this.markHeadNodes) {
/* 352 */       outputPSTree = markHeadNodes(outputPSTree);
/*     */     }
/*     */     
/* 355 */     if (this.transChinese) {
/* 356 */       TreeTransformer tt = new TreeTransformer() {
/*     */         public Tree transformTree(Tree t) {
/* 358 */           t = t.deepCopy();
/* 359 */           for (Tree subtree : t) {
/* 360 */             if (subtree.isLeaf()) {
/* 361 */               Label oldLabel = subtree.label();
/* 362 */               String translation = edu.stanford.nlp.trees.international.pennchinese.ChineseEnglishWordMap.getInstance().getFirstTranslation(oldLabel.value());
/* 363 */               if (translation == null) translation = "[UNK]";
/* 364 */               Label newLabel = new edu.stanford.nlp.ling.StringLabel(oldLabel.value() + ":" + translation);
/* 365 */               subtree.setLabel(newLabel);
/*     */             }
/*     */           }
/* 368 */           return t;
/*     */         }
/* 370 */       };
/* 371 */       outputPSTree = tt.transformTree(outputPSTree);
/*     */     }
/*     */     
/* 374 */     if (propertyToBoolean(this.options, "xml")) {
/* 375 */       if (this.formats.containsKey("wordsAndTags")) {
/* 376 */         Sentence<TaggedWord> sent = outputTree.taggedYield();
/* 377 */         pw.println("  <words pos=\"true\">");
/* 378 */         int i = 1;
/* 379 */         for (TaggedWord tw : sent) {
/* 380 */           pw.println("    <word ind=\"" + i + "\" pos=\"" + XMLUtils.escapeXML(tw.tag()) + "\">" + XMLUtils.escapeXML(tw.word()) + "</word>");
/* 381 */           i++;
/*     */         }
/* 383 */         pw.println("  </words>");
/*     */       }
/* 385 */       if (this.formats.containsKey("penn")) {
/* 386 */         pw.println("  <tree style=\"penn\">");
/* 387 */         StringWriter sw = new StringWriter();
/* 388 */         PrintWriter psw = new PrintWriter(sw);
/* 389 */         outputPSTree.pennPrint(psw);
/* 390 */         pw.print(XMLUtils.escapeXML(sw.toString()));
/* 391 */         pw.println("  </tree>");
/*     */       }
/* 393 */       if (this.formats.containsKey("latexTree")) {
/* 394 */         pw.println("    <tree style=\"latexTrees\">");
/* 395 */         pw.println(".[");
/* 396 */         StringWriter sw = new StringWriter();
/* 397 */         PrintWriter psw = new PrintWriter(sw);
/* 398 */         outputTree.indentedListPrint(psw, false);
/* 399 */         pw.print(XMLUtils.escapeXML(sw.toString()));
/* 400 */         pw.println(".]");
/* 401 */         pw.println("  </tree>");
/*     */       }
/* 403 */       if (this.formats.containsKey("dependencies")) {
/* 404 */         Tree indexedTree = outputTree.deeperCopy(outputTree.treeFactory(), MapLabel.factory());
/*     */         
/* 406 */         indexedTree.indexLeaves();
/* 407 */         Set<Dependency> depsSet = indexedTree.mapDependencies(this.dependencyFilter, this.hf);
/* 408 */         List<Dependency> sortedDeps = new ArrayList(depsSet);
/* 409 */         java.util.Collections.sort(sortedDeps, Dependencies.dependencyIndexComparator());
/* 410 */         pw.println("<dependencies style=\"untyped\">");
/* 411 */         for (Dependency d : sortedDeps) {
/* 412 */           pw.println(d.toString("xml"));
/*     */         }
/* 414 */         pw.println("</dependencies>");
/*     */       }
/* 416 */       if (this.formats.containsKey("typedDependencies")) {
/* 417 */         GrammaticalStructure gs = this.gsf.newGrammaticalStructure(outputTree);
/* 418 */         print(gs.typedDependencies(), "xml", pw);
/*     */       }
/* 420 */       if (this.formats.containsKey("typedDependenciesCollapsed")) {
/* 421 */         GrammaticalStructure gs = this.gsf.newGrammaticalStructure(outputTree);
/* 422 */         print(gs.typedDependenciesCollapsed(), "xml", pw);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 431 */       if (this.formats.containsKey("wordsAndTags")) {
/* 432 */         pw.println(outputTree.taggedYield().toString(false));
/* 433 */         pw.println();
/*     */       }
/* 435 */       if (this.formats.containsKey("oneline")) {
/* 436 */         pw.println(outputTree.toString());
/*     */       }
/* 438 */       if (this.formats.containsKey("penn")) {
/* 439 */         outputPSTree.pennPrint(pw);
/* 440 */         pw.println();
/*     */       }
/* 442 */       if (this.formats.containsKey("rootSymbolOnly")) {
/* 443 */         pw.println(outputTree.label());
/*     */       }
/* 445 */       if (this.formats.containsKey("latexTree")) {
/* 446 */         pw.println(".[");
/* 447 */         outputTree.indentedListPrint(pw, false);
/* 448 */         pw.println(".]");
/*     */       }
/* 450 */       if (this.formats.containsKey("dependencies")) {
/* 451 */         Tree indexedTree = outputTree.deeperCopy(outputTree.treeFactory(), MapLabel.factory());
/*     */         
/* 453 */         indexedTree.indexLeaves();
/* 454 */         Set<Dependency> depsSet = indexedTree.mapDependencies(this.dependencyFilter, this.hf);
/* 455 */         List<Dependency> sortedDeps = new ArrayList(depsSet);
/* 456 */         java.util.Collections.sort(sortedDeps, Dependencies.dependencyIndexComparator());
/* 457 */         for (Dependency d : sortedDeps) {
/* 458 */           pw.println(d.toString("predicate"));
/*     */         }
/* 460 */         pw.println();
/*     */       }
/* 462 */       if (this.formats.containsKey("typedDependencies"))
/*     */       {
/* 464 */         GrammaticalStructure gs = this.gsf.newGrammaticalStructure(outputTree);
/*     */         
/* 466 */         print(gs.typedDependencies(), pw);
/* 467 */         pw.println();
/*     */       }
/* 469 */       if (this.formats.containsKey("typedDependenciesCollapsed")) {
/* 470 */         GrammaticalStructure gs = this.gsf.newGrammaticalStructure(outputTree);
/*     */         
/* 472 */         print(gs.typedDependenciesCollapsed(), pw);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printHeader(PrintWriter pw, String charset)
/*     */   {
/* 484 */     if (propertyToBoolean(this.options, "xml")) {
/* 485 */       pw.println("<?xml version=\"1.0\" encoding=\"" + charset + "\"?>");
/* 486 */       pw.println("<corpus>");
/*     */     }
/*     */   }
/*     */   
/*     */   public void printFooter(PrintWriter pw)
/*     */   {
/* 492 */     if (propertyToBoolean(this.options, "xml")) {
/* 493 */       pw.println("</corpus>");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStem(boolean stem)
/*     */   {
/* 504 */     this.stem = stem;
/* 505 */     if (stem) {
/* 506 */       this.stemmer = new WordStemmer();
/*     */     } else {
/* 508 */       this.stemmer = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHeadFinder(HeadFinder hf)
/*     */   {
/* 519 */     this.hf = hf;
/*     */   }
/*     */   
/*     */   public HeadFinder getHeadFinder() {
/* 523 */     return this.hf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPrintWriter(PrintWriter pw)
/*     */   {
/* 533 */     this.pw = pw;
/*     */   }
/*     */   
/*     */   public PrintWriter getPrintWriter() {
/* 537 */     return this.pw;
/*     */   }
/*     */   
/*     */   public Tree markHeadNodes(Tree t) {
/* 541 */     return markHeadNodes(t, null);
/*     */   }
/*     */   
/*     */   private Tree markHeadNodes(Tree t, Tree head) {
/* 545 */     if (t.isLeaf())
/* 546 */       return t;
/*     */     Label newLabel;
/*     */     Label newLabel;
/* 549 */     if (t == head) {
/* 550 */       newLabel = headMark(t.label());
/*     */     } else {
/* 552 */       newLabel = t.label();
/*     */     }
/* 554 */     Tree newHead = this.hf.determineHead(t);
/* 555 */     return t.treeFactory().newTreeNode(newLabel, java.util.Arrays.asList(headMarkChildren(t, newHead)));
/*     */   }
/*     */   
/*     */   private static Label headMark(Label l) {
/* 559 */     Label l1 = l.labelFactory().newLabel(l);
/* 560 */     l1.setValue(l1.value() + "=H");
/* 561 */     return l1;
/*     */   }
/*     */   
/*     */   private Tree[] headMarkChildren(Tree t, Tree head) {
/* 565 */     Tree[] kids = t.children();
/* 566 */     Tree[] newKids = new Tree[kids.length];
/* 567 */     int i = 0; for (int n = kids.length; i < n; i++) {
/* 568 */       newKids[i] = markHeadNodes(kids[i], head);
/*     */     }
/* 570 */     return newKids;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 590 */     String format = "penn";
/* 591 */     String options = "";
/* 592 */     String tlppName = "edu.stanford.nlp.parser.lexparser.EnglishTreebankParserParams";
/* 593 */     String hfName = null;
/* 594 */     Map<String, Integer> flagMap = new java.util.HashMap();
/* 595 */     flagMap.put("-format", Integer.valueOf(1));
/* 596 */     flagMap.put("-options", Integer.valueOf(1));
/* 597 */     flagMap.put("-tLPP", Integer.valueOf(1));
/* 598 */     flagMap.put("-hf", Integer.valueOf(1));
/* 599 */     Map<String, String[]> argsMap = StringUtils.argsToMap(args, flagMap);
/* 600 */     args = (String[])argsMap.get(null);
/* 601 */     if (argsMap.keySet().contains("-format")) {
/* 602 */       format = ((String[])argsMap.get("-format"))[0];
/*     */     }
/* 604 */     if (argsMap.keySet().contains("-options")) {
/* 605 */       options = ((String[])argsMap.get("-options"))[0];
/*     */     }
/* 607 */     if (argsMap.keySet().contains("-tLPP")) {
/* 608 */       tlppName = ((String[])argsMap.get("-tLPP"))[0];
/*     */     }
/* 610 */     if (argsMap.keySet().contains("-hf")) {
/* 611 */       hfName = ((String[])argsMap.get("-hf"))[0];
/*     */     }
/*     */     TreebankLangParserParams tlpp;
/*     */     try {
/* 615 */       tlpp = (TreebankLangParserParams)Class.forName(tlppName).newInstance();
/*     */     } catch (Exception e) {
/* 617 */       e.printStackTrace(); return;
/*     */     }
/*     */     
/*     */     HeadFinder hf;
/* 621 */     if (hfName != null) {
/*     */       try {
/* 623 */         hf = (HeadFinder)Class.forName(hfName).newInstance();
/*     */       } catch (Exception e) {
/* 625 */         e.printStackTrace();
/* 626 */         return;
/*     */       }
/*     */     } else {
/* 629 */       hf = tlpp.headFinder();
/*     */     }
/* 631 */     TreePrint print = new TreePrint(format, options, tlpp.treebankLanguagePack(), hf == null ? tlpp.headFinder() : hf);
/*     */     Iterator<Tree> i;
/* 633 */     Iterator<Tree> i; if (args.length > 0) { Treebank trees;
/*     */       Treebank trees;
/* 635 */       if (argsMap.keySet().contains("-useTLPPTreeReader")) {
/* 636 */         trees = tlpp.diskTreebank();
/*     */       } else {
/* 638 */         trees = new DiskTreebank(new TreeReaderFactory() {
/*     */           public TreeReader newTreeReader(java.io.Reader in) {
/* 640 */             return new PennTreeReader(in, new LabeledScoredTreeFactory(new edu.stanford.nlp.ling.StringLabelFactory()), new TreeNormalizer());
/*     */           }
/*     */         });
/*     */       }
/* 644 */       trees.loadPath(args[0]);
/* 645 */       i = trees.iterator();
/*     */     } else {
/* 647 */       i = tlpp.treeTokenizerFactory().getIterator(new java.io.BufferedReader(new java.io.InputStreamReader(System.in)));
/*     */     }
/* 649 */     while (i.hasNext()) {
/* 650 */       print.printTree((Tree)i.next());
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
/*     */   private static String toString(Collection<TypedDependency> dependencies, String format)
/*     */   {
/* 700 */     if ((format != null) && (format.equals("xml")))
/* 701 */       return toXMLString(dependencies);
/* 702 */     if ((format != null) && (format.equals("readable"))) {
/* 703 */       return toReadableString(dependencies);
/*     */     }
/* 705 */     return toString(dependencies);
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
/*     */ 
/*     */   private static String toString(Collection<TypedDependency> dependencies)
/*     */   {
/* 723 */     StringBuilder buf = new StringBuilder();
/* 724 */     for (TypedDependency td : dependencies) {
/* 725 */       buf.append(td.toString()).append("\n");
/*     */     }
/* 727 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static String toReadableString(Collection<TypedDependency> dependencies)
/*     */   {
/* 732 */     StringBuilder buf = new StringBuilder();
/* 733 */     buf.append(String.format("%-20s%-20s%-20s%n", new Object[] { "dep", "reln", "gov" }));
/* 734 */     buf.append(String.format("%-20s%-20s%-20s%n", new Object[] { "---", "----", "---" }));
/* 735 */     for (TypedDependency td : dependencies) {
/* 736 */       buf.append(String.format("%-20s%-20s%-20s%n", new Object[] { td.dep(), td.reln(), td.gov() }));
/*     */     }
/* 738 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static String toXMLString(Collection<TypedDependency> dependencies)
/*     */   {
/* 743 */     StringBuilder buf = new StringBuilder("<dependencies style=\"typed\">\n");
/* 744 */     for (TypedDependency td : dependencies) {
/* 745 */       String reln = td.reln().toString();
/* 746 */       String gov = td.gov().value();
/* 747 */       int govIdx = td.gov().index();
/* 748 */       String dep = td.dep().value();
/* 749 */       int depIdx = td.dep().index();
/*     */       
/*     */ 
/* 752 */       String govCopy = "";
/* 753 */       if (td.gov().label.get("copy").equals("true")) {
/* 754 */         govCopy = " copy=\"yes\"";
/*     */       }
/* 756 */       String depCopy = "";
/* 757 */       if (td.dep().label.get("copy").equals("true")) {
/* 758 */         depCopy = " copy=\"yes\"";
/*     */       }
/* 760 */       buf.append("  <dep type=\"").append(XMLUtils.escapeXML(reln)).append("\">\n");
/* 761 */       buf.append("    <governor idx=\"").append(govIdx).append("\"").append(govCopy).append(">").append(XMLUtils.escapeXML(gov)).append("</governor>\n");
/* 762 */       buf.append("    <dependent idx=\"").append(depIdx).append("\"").append(depCopy).append(">").append(XMLUtils.escapeXML(dep)).append("</dependent>\n");
/* 763 */       buf.append("  </dep>\n");
/*     */     }
/* 765 */     buf.append("</dependencies>");
/* 766 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void print(Collection<TypedDependency> dependencies, PrintWriter pw)
/*     */   {
/* 777 */     pw.println(toString(dependencies));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void print(Collection<TypedDependency> dependencies, String format, PrintWriter pw)
/*     */   {
/* 789 */     pw.println(toString(dependencies, format));
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreePrint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */