/*     */ package edu.stanford.nlp.trees.tregex;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.StringLabelFactory;
/*     */ import edu.stanford.nlp.process.AbstractTokenizer;
/*     */ import edu.stanford.nlp.process.Tokenizer;
/*     */ import edu.stanford.nlp.trees.CollinsHeadFinder;
/*     */ import edu.stanford.nlp.trees.HeadFinder;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.PennTreeReader;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ public class TreePattern
/*     */ {
/*  85 */   static HeadFinder defaultHeadFinder = new CollinsHeadFinder();
/*     */   
/*  87 */   static final TreePattern[] zeroChildren = new TreePattern[0];
/*     */   
/*     */ 
/*     */   Object name;
/*     */   
/*     */ 
/*     */   String description;
/*     */   
/*  95 */   boolean negatedDescription = false;
/*     */   
/*     */ 
/*     */   Relation relation;
/*     */   
/*     */ 
/* 101 */   boolean negatedRelation = false;
/*     */   
/*     */   TreePattern parent;
/*     */   
/*     */   TreePattern[] children;
/*     */   
/*     */   Map namesToNodes;
/*     */   Pattern descriptionPattern;
/*     */   
/*     */   private TreePattern(TreePattern[] children)
/*     */   {
/* 112 */     this.children = children;
/* 113 */     this.namesToNodes = new HashMap();
/* 114 */     for (int i = 0; i < children.length; i++) {
/* 115 */       children[i].parent = this;
/*     */     }
/* 117 */     unifyNamesToNodesMaps();
/*     */   }
/*     */   
/*     */   TreePattern(String description, Relation relation, TreePattern[] children) {
/* 121 */     this(description, nameNode(), relation, children);
/*     */   }
/*     */   
/*     */   TreePattern(String description, Object name, Relation relation, TreePattern[] children) {
/* 125 */     this(description, name, relation, children, true);
/*     */   }
/*     */   
/*     */   TreePattern(String description, Relation relation, TreePattern[] children, boolean negatedDescription) {
/* 129 */     this(description, nameNode(), relation, children, negatedDescription);
/*     */   }
/*     */   
/*     */   TreePattern(String description, Object name, Relation relation, TreePattern[] children, boolean negatedDescription) {
/* 133 */     this(children);
/* 134 */     this.description = description;
/* 135 */     this.relation = relation;
/* 136 */     this.name = name;
/* 137 */     this.descriptionPattern = Pattern.compile(this.description);
/*     */   }
/*     */   
/*     */   private void unifyNamesToNodesMaps()
/*     */   {
/* 142 */     for (int i = 0; i < this.children.length; i++) {
/* 143 */       this.children[i].namesToNodes = this.namesToNodes;
/* 144 */       this.children[i].unifyNamesToNodesMaps();
/*     */     }
/*     */   }
/*     */   
/*     */   Tree node()
/*     */   {
/* 150 */     return (Tree)this.namesToNodes.get(this.name);
/*     */   }
/*     */   
/*     */   void setNode(Tree t) {
/* 154 */     this.namesToNodes.put(this.name, t);
/*     */   }
/*     */   
/*     */   private ListIterator nodesInOrder() {
/* 158 */     List l = new ArrayList();
/* 159 */     nodesInOrder(l);
/* 160 */     return l.listIterator();
/*     */   }
/*     */   
/*     */   private void nodesInOrder(List l) {
/* 164 */     l.add(this);
/* 165 */     int i = 0; for (int n = this.children.length; i < n; i++) {
/* 166 */       this.children[i].nodesInOrder(l);
/*     */     }
/*     */   }
/*     */   
/*     */   TreePatternIterator iterator() {
/* 171 */     return new MyIterator(nodesInOrder());
/*     */   }
/*     */   
/*     */   class MyIterator implements TreePatternIterator {
/*     */     ListIterator i;
/*     */     
/* 177 */     public MyIterator(ListIterator i) { this.i = i; }
/*     */     
/*     */ 
/*     */ 
/*     */     public TreePattern next()
/*     */     {
/* 183 */       return (TreePattern)this.i.next();
/*     */     }
/*     */     
/*     */     public TreePattern previous() {
/* 187 */       return (TreePattern)this.i.previous();
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 191 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 195 */       return this.i.hasPrevious();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreeMatcher matcher(Tree t)
/*     */   {
/* 205 */     return new TreeMatcher(t, this);
/*     */   }
/*     */   
/* 208 */   private static Pattern separateNamesPattern = Pattern.compile("^(\\S+)(=[^=/]+)$");
/* 209 */   private static Pattern separateLeftParensPattern = Pattern.compile("^\\((.+)$");
/* 210 */   private static Pattern separateRightParensPattern = Pattern.compile("^(.+)\\)$");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TreePattern compile(String str)
/*     */   {
/* 221 */     List tokens = Arrays.asList(str.split("\\s+"));
/* 222 */     tokens = separateParens(tokens);
/* 223 */     tokens = separateNames(tokens);
/*     */     
/* 225 */     if (TreeMatcher.Verbose.verbose) {
/* 226 */       System.out.println("Compiling " + str);
/* 227 */       System.out.println("Here are the tokens:\n" + tokens);
/*     */     }
/* 229 */     Tokenizer tokenizer = new ListTokenizer(tokens);
/*     */     
/* 231 */     TreePattern pattern = compile(tokenizer);
/* 232 */     if (tokenizer.hasNext()) {
/* 233 */       throw new RuntimeException("Error -- extra tokens at end of input TreePattern string!");
/*     */     }
/*     */     
/* 236 */     return pattern;
/*     */   }
/*     */   
/*     */   private static List separateNames(List tokens) {
/* 240 */     List newTokens = new ArrayList(tokens.size());
/* 241 */     for (Iterator i = tokens.iterator(); i.hasNext();) {
/* 242 */       String token = (String)i.next();
/* 243 */       Matcher m = separateNamesPattern.matcher(token);
/* 244 */       if (m.matches()) {
/* 245 */         newTokens.add(m.group(1));
/* 246 */         newTokens.add(m.group(2));
/*     */       }
/*     */       else {
/* 249 */         newTokens.add(token);
/*     */       }
/*     */     }
/* 252 */     return newTokens;
/*     */   }
/*     */   
/*     */   private static List separateParens(List tokens) {
/* 256 */     List newTokens = new ArrayList(tokens.size());
/* 257 */     for (Iterator i = tokens.iterator(); i.hasNext();) {
/* 258 */       String token = (String)i.next();
/* 259 */       Matcher m = separateLeftParensPattern.matcher(token);
/* 260 */       while (m.matches()) {
/* 261 */         newTokens.add("(");
/* 262 */         token = m.group(1);
/* 263 */         m = separateLeftParensPattern.matcher(token);
/*     */       }
/* 265 */       List rightParensList = new ArrayList();
/* 266 */       Matcher m1 = separateRightParensPattern.matcher(token);
/* 267 */       while (m1.matches()) {
/* 268 */         rightParensList.add(")");
/* 269 */         token = m1.group(1);
/* 270 */         m1 = separateRightParensPattern.matcher(token);
/*     */       }
/* 272 */       rightParensList.add(0, token);
/* 273 */       newTokens.addAll(rightParensList);
/*     */     }
/* 275 */     return newTokens;
/*     */   }
/*     */   
/*     */ 
/*     */   private static TreePattern compile(Tokenizer tokenizer)
/*     */   {
/* 281 */     return parseNonTerminalTreePattern(tokenizer, Relation.ROOT);
/*     */   }
/*     */   
/*     */   private static TreePattern parseTerminalTreePattern(Tokenizer tokenizer, Relation r) {
/* 285 */     String str = (String)tokenizer.peek();
/*     */     
/* 287 */     if ((namePattern.matcher(str).matches()) || (relationPattern.matcher(str).matches()) || (leftParPattern.matcher(str).matches()) || (rightParPattern.matcher(str).matches())) {
/* 288 */       throw new RuntimeException("Error -- terminal tree pattern parse method called for non-terminal tree pattern");
/*     */     }
/* 290 */     tokenizer.next();
/* 291 */     String next = (String)tokenizer.peek();
/* 292 */     Object name; Object name; if (next == null) {
/* 293 */       name = new Object();
/*     */     } else {
/* 295 */       Matcher m = namePattern.matcher(next);
/* 296 */       Object name; if (!m.matches()) {
/* 297 */         name = new Object();
/*     */       } else {
/* 299 */         tokenizer.next();
/* 300 */         name = m.group(1);
/*     */       }
/*     */     }
/* 303 */     NegationDescriptionPair p = formatDescriptionString(str);
/* 304 */     return new TreePattern(p.description, name, r, zeroChildren, p.negation);
/*     */   }
/*     */   
/*     */   private static TreePattern parseNonTerminalTreePattern(Tokenizer tokenizer, Relation r) {
/* 308 */     Object name = null;
/* 309 */     String str = (String)tokenizer.peek();
/* 310 */     if ((rightParPattern.matcher(str).matches()) || (namePattern.matcher(str).matches())) {
/* 311 */       throw new RuntimeException("Error -- non-terminal tree pattern parse method called for name or ) token");
/*     */     }
/* 313 */     if (leftParPattern.matcher(str).matches()) {
/* 314 */       tokenizer.next();
/* 315 */       TreePattern pattern = parseNonTerminalTreePattern(tokenizer, r);
/* 316 */       String next = (String)tokenizer.peek();
/* 317 */       if ((next == null) || (!rightParPattern.matcher(next).matches())) {
/* 318 */         throw new RuntimeException("Error -- unbalanced parenthesis!");
/*     */       }
/* 320 */       tokenizer.next();
/* 321 */       return pattern;
/*     */     }
/*     */     
/* 324 */     tokenizer.next();
/* 325 */     String next = (String)tokenizer.peek();
/* 326 */     if (next != null) {
/* 327 */       Matcher m = namePattern.matcher(next);
/* 328 */       if (m.matches()) {
/* 329 */         tokenizer.next();
/* 330 */         if (TreeMatcher.Verbose.verbose) {
/* 331 */           System.out.println("###Matched as name: " + next);
/*     */         }
/* 333 */         name = m.group(1);
/*     */       }
/*     */     }
/* 336 */     TreePattern[] children = parseTreePatternChildren(tokenizer);
/* 337 */     NegationDescriptionPair p = formatDescriptionString(str);
/* 338 */     if (name != null) {
/* 339 */       return new TreePattern(p.description, name, r, children, p.negation);
/*     */     }
/* 341 */     return new TreePattern(p.description, r, children, p.negation);
/*     */   }
/*     */   
/*     */ 
/*     */   private static TreePattern[] parseTreePatternChildren(Tokenizer tokenizer)
/*     */   {
/* 347 */     List kids = new ArrayList(0);
/* 348 */     return (TreePattern[])parseTreePatternChildren(tokenizer, kids).toArray(zeroChildren);
/*     */   }
/*     */   
/*     */   private static List parseTreePatternChildren(Tokenizer tokenizer, List kids) {
/* 352 */     if (!tokenizer.hasNext()) {
/* 353 */       return kids;
/*     */     }
/* 355 */     String relnStr = (String)tokenizer.peek();
/* 356 */     if (rightParPattern.matcher(relnStr).matches()) {
/* 357 */       if (TreeMatcher.Verbose.verbose) {
/* 358 */         System.out.println("###Finished composing children list.");
/*     */       }
/* 360 */       return kids;
/*     */     }
/* 362 */     tokenizer.next();
/* 363 */     if (!relationPattern.matcher(relnStr).matches()) {
/* 364 */       throw new RuntimeException("Error -- invalid relation string: " + relnStr);
/*     */     }
/*     */     
/*     */ 
/* 368 */     String arg = null;
/* 369 */     Matcher m1 = numericArgRelationPattern.matcher(relnStr);
/* 370 */     if (m1.matches())
/*     */     {
/* 372 */       relnStr = m1.group(1);
/* 373 */       arg = m1.group(2);
/*     */     }
/* 375 */     m1 = stringArgRelationPattern.matcher(relnStr);
/* 376 */     if (m1.matches())
/*     */     {
/* 378 */       relnStr = m1.group(1);
/* 379 */       arg = m1.group(2);
/*     */     }
/*     */     
/*     */     Relation r;
/*     */     
/*     */     try
/*     */     {
/* 386 */       r = Relation.getRelation(relnStr, arg);
/*     */     } catch (ParseException e) {
/* 388 */       throw new RuntimeException(e);
/*     */     }
/*     */     
/*     */ 
/* 392 */     if (TreeMatcher.Verbose.verbose) {
/* 393 */       System.out.println("### Found relation: " + r + " from string " + relnStr);
/*     */     }
/*     */     
/* 396 */     String str = (String)tokenizer.peek();
/* 397 */     if (namePattern.matcher(str).matches()) {
/* 398 */       throw new RuntimeException("Error -- node name token in wrong place.");
/*     */     }
/* 400 */     if (leftParPattern.matcher(str).matches()) {
/* 401 */       kids.add(parseNonTerminalTreePattern(tokenizer, r));
/*     */     } else {
/* 403 */       kids.add(parseTerminalTreePattern(tokenizer, r));
/*     */     }
/* 405 */     return parseTreePatternChildren(tokenizer, kids);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 410 */   static final Pattern leftParPattern = Pattern.compile("\\(");
/* 411 */   static final Pattern rightParPattern = Pattern.compile("\\)");
/* 412 */   static final Pattern namePattern = Pattern.compile("\\A(=.*)\\z");
/* 413 */   static final Pattern relationPattern = Pattern.compile("[<>][-,`:+H]?|[<>]-?[0-9]|(?:<<|>>)[,`:-H]?|[.,]|\\.\\.|,,|\\$[.,+-]?|\\$\\.\\.|\\$,,|\\$\\+\\+|\\$--|=|<\\+.+|>\\+.+");
/*     */   
/* 415 */   static final Pattern numericArgRelationPattern = Pattern.compile("([<>])(-?[0-9]+)");
/* 416 */   static final Pattern stringArgRelationPattern = Pattern.compile("(<\\+|>\\+)(.+)");
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
/*     */   private static Object nameNode()
/*     */   {
/* 456 */     return new Object();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 463 */     String str = "";
/* 464 */     String end = "";
/*     */     
/* 466 */     if (this.relation != null) {
/* 467 */       str = str + this.relation.toString() + "( ";
/* 468 */       end = ") ";
/*     */     }
/*     */     
/* 471 */     if ((this.name instanceof String)) {
/* 472 */       str = str + this.description + this.name + " ";
/*     */     } else {
/* 474 */       str = str + this.description + " ";
/*     */     }
/*     */     
/* 477 */     for (int i = 0; i < this.children.length; i++) {
/* 478 */       str = str + this.children[i].toString();
/*     */     }
/* 480 */     str = str + end;
/* 481 */     return str;
/*     */   }
/*     */   
/* 484 */   static final Pattern quotedString = Pattern.compile("\"(.*)\"");
/* 485 */   static final Pattern regularExpressionString = Pattern.compile("/(.*)/");
/* 486 */   static final Pattern wildCardString = Pattern.compile("__|\\*");
/*     */   
/*     */ 
/*     */   static NegationDescriptionPair formatDescriptionString(String str)
/*     */   {
/* 491 */     NegationDescriptionPair p = new NegationDescriptionPair();
/*     */     
/*     */ 
/* 494 */     if (str.charAt(0) == '!') {
/* 495 */       p.negation = true;
/* 496 */       str = str.substring(1);
/*     */     }
/*     */     
/*     */ 
/* 500 */     if (wildCardString.matcher(str).matches()) {
/* 501 */       p.description = ".*";
/* 502 */       return p;
/*     */     }
/*     */     
/* 505 */     Matcher m = regularExpressionString.matcher(str);
/* 506 */     if (m.matches()) {
/* 507 */       String regex = m.group(1);
/*     */       
/*     */       String regexStart;
/* 510 */       if (regex.startsWith("^")) {
/* 511 */         String regexStart = "^";
/* 512 */         regex = regex.substring(1);
/*     */       } else {
/* 514 */         regexStart = ".*"; }
/*     */       String regexEnd;
/* 516 */       if (regex.endsWith("$")) {
/* 517 */         String regexEnd = "$";
/* 518 */         regex = regex.substring(0, regex.length() - 1);
/*     */       } else {
/* 520 */         regexEnd = ".*";
/*     */       }
/* 522 */       p.description = (regexStart + regex + regexEnd);
/* 523 */       return p;
/*     */     }
/*     */     
/*     */ 
/* 527 */     String[] alternates = str.split("\\|");
/*     */     
/* 529 */     int i = 0; for (int n = alternates.length; i < n; i++) {
/* 530 */       Matcher q = quotedString.matcher(alternates[i]);
/* 531 */       if (q.matches()) {
/* 532 */         String quoted = q.group(1);
/* 533 */         alternates[i] = deescapeQuotes(quoted);
/*     */       }
/* 535 */       else if (hasUnquotedSpecials(alternates[i])) {
/* 536 */         throw new RuntimeException("error -- unquoted specials in description subsequence " + alternates[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 542 */     String result = "";
/* 543 */     int i = 0; for (int n = alternates.length - 1; i < n; i++) {
/* 544 */       result = result + alternates[i] + "|";
/*     */     }
/* 546 */     result = result + alternates[(alternates.length - 1)];
/*     */     
/* 548 */     p.description = result;
/* 549 */     return p;
/*     */   }
/*     */   
/*     */   static class NegationDescriptionPair
/*     */   {
/* 554 */     boolean negation = false;
/*     */     
/*     */     String description;
/*     */   }
/*     */   
/* 559 */   private static Pattern escapedQuote = Pattern.compile("\\\"");
/* 560 */   private static Pattern unEscapedQuote = Pattern.compile(".*[^\\\\]\".*");
/*     */   
/*     */   private static String deescapeQuotes(String str) {
/* 563 */     if (unEscapedQuote.matcher(str).matches()) {
/* 564 */       throw new RuntimeException("error -- unescaped quotation mark \" contained in description string!");
/*     */     }
/* 566 */     Matcher m = escapedQuote.matcher(str);
/* 567 */     return m.replaceAll("\"");
/*     */   }
/*     */   
/*     */ 
/* 571 */   private static Pattern specials = Pattern.compile("[*$#&%!]");
/*     */   TreePattern lastNode;
/*     */   
/* 574 */   private static boolean hasUnquotedSpecials(String str) { return specials.matcher(str).find(); }
/*     */   
/*     */ 
/*     */ 
/*     */   int size()
/*     */   {
/* 580 */     if (this.children.length == 0) {
/* 581 */       return 1;
/*     */     }
/* 583 */     int numKids = 0;
/* 584 */     int i = 0; for (int n = this.children.length; i < n; i++) {
/* 585 */       numKids += this.children[i].size();
/*     */     }
/* 587 */     return numKids;
/*     */   }
/*     */   
/*     */ 
/*     */   Tree root;
/*     */   
/*     */   Iterator it;
/*     */   public String pattern()
/*     */   {
/* 596 */     String str = "";
/* 597 */     str = str + this.description;
/* 598 */     if ((this.name instanceof String)) {
/* 599 */       str = str + this.name;
/*     */     }
/* 601 */     str = str + " ";
/* 602 */     int i = 0; for (int n = this.children.length; i < n; i++) {
/* 603 */       str = str + this.children[i].childPattern();
/*     */     }
/* 605 */     return str;
/*     */   }
/*     */   
/*     */   private String childPattern() {
/* 609 */     if (this.relation == null) {
/* 610 */       throw new RuntimeException("Error -- null relation at node " + pattern());
/*     */     }
/* 612 */     String str = this.relation.toString();
/* 613 */     str = str + " ";
/* 614 */     if (this.children.length > 0) {
/* 615 */       str = str + "( ";
/*     */     }
/* 617 */     str = str + this.description;
/* 618 */     if ((this.name instanceof String)) {
/* 619 */       str = str + " " + this.name;
/*     */     }
/* 621 */     str = str + " ";
/* 622 */     int i = 0; for (int n = this.children.length; i < n; i++) {
/* 623 */       str = str + this.children[i].childPattern();
/*     */     }
/* 625 */     if (this.children.length > 0) {
/* 626 */       str = str + ") ";
/*     */     }
/* 628 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   TreePatternIterator childrenIterator;
/*     */   
/*     */ 
/*     */   TreePattern currentChild;
/*     */   
/*     */ 
/*     */   TreePatternReturnValue currentResult;
/*     */   
/*     */   public void reset(Tree root)
/*     */   {
/* 643 */     initializeSearchOnTree(root, root);
/* 644 */     this.root = root;
/* 645 */     this.lastNode = this;
/*     */   }
/*     */   
/*     */   public boolean findNext() {
/* 649 */     TreePatternReturnValue result = processForward(this.root);
/* 650 */     this.lastNode = result.finalNode;
/* 651 */     return result.success;
/*     */   }
/*     */   
/*     */   public Tree returnMatch() {
/* 655 */     return node();
/*     */   }
/*     */   
/*     */   TreePatternReturnValue processForward(Tree root) {
/* 659 */     boolean matchesNode = matchOnTree(root);
/* 660 */     if (matchesNode) {
/* 661 */       initializeChildrenIterator();
/* 662 */       return processChildrenForward(root);
/*     */     }
/* 664 */     if (this.parent == null) {
/* 665 */       return new TreePatternReturnValue(this);
/*     */     }
/* 667 */     return this.parent.processBackward(root);
/*     */   }
/*     */   
/*     */ 
/*     */   void initializeSearchOnTree(Tree t, Tree root)
/*     */   {
/* 673 */     this.it = this.relation.searchNodeIterator(t, root);
/*     */   }
/*     */   
/*     */   boolean matchOnTree(Tree root) {
/* 677 */     while (this.it.hasNext()) {
/* 678 */       Tree t = (Tree)this.it.next();
/* 679 */       boolean descriptionMatchesNode = this.descriptionPattern.matcher(t.label().value()).matches();
/* 680 */       if (((descriptionMatchesNode ^ this.negatedDescription)) && ((this.parent == null) || (this.relation.satisfies(this.parent.node(), t, root)))) {
/* 681 */         setNode(t);
/* 682 */         return true;
/*     */       }
/*     */     }
/* 685 */     return false;
/*     */   }
/*     */   
/*     */   public void setCurrentResult(TreePatternReturnValue currentResult) {
/* 689 */     this.currentResult = currentResult;
/*     */   }
/*     */   
/*     */   private void initializeChildrenIterator() {
/* 693 */     this.currentResult = new TreePatternReturnValue(this);
/* 694 */     this.childrenIterator = new MyIterator(Arrays.asList(this.children).listIterator());
/*     */   }
/*     */   
/*     */   TreePatternReturnValue processChildrenForward(Tree root) {
/* 698 */     if (!this.childrenIterator.hasNext()) {
/* 699 */       this.currentResult.success = true;
/* 700 */       if (this.parent == null) {
/* 701 */         return this.currentResult;
/*     */       }
/* 703 */       this.parent.setCurrentResult(this.currentResult);
/* 704 */       return this.parent.processChildrenForward(root);
/*     */     }
/*     */     
/* 707 */     this.currentChild = this.childrenIterator.next();
/* 708 */     this.currentChild.initializeSearchOnTree(node(), root);
/* 709 */     return this.currentChild.processForward(root);
/*     */   }
/*     */   
/*     */   TreePatternReturnValue processBackward(Tree root)
/*     */   {
/* 714 */     if (!this.childrenIterator.hasPrevious()) {
/* 715 */       throw new RuntimeException("error -- processBackward() should only be called by a child on a parent");
/*     */     }
/* 717 */     this.childrenIterator.previous();
/*     */     
/* 719 */     if (this.childrenIterator.hasPrevious()) {
/* 720 */       this.currentChild = this.childrenIterator.previous();
/* 721 */       this.childrenIterator.next();
/* 722 */       return this.currentChild.processForward(root);
/*     */     }
/* 724 */     boolean matchNewTreeNode = matchOnTree(root);
/* 725 */     if (matchNewTreeNode) {
/* 726 */       return processChildrenForward(root);
/*     */     }
/* 728 */     if (this.parent == null) {
/* 729 */       return this.currentResult;
/*     */     }
/* 731 */     return this.parent.processBackward(root);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 742 */     TreePattern p = compile(args[0]);
/*     */     
/* 744 */     System.out.println(p.pattern());
/*     */     
/*     */ 
/* 747 */     Tree t = null;
/*     */     try {
/* 749 */       t = new PennTreeReader(new StringReader("(VP (VP (VBZ Try) (NP (NP (DT this) (NN wine)) (CC and) (NP (DT these) (NNS snails)))) (PUNCT .))"), new LabeledScoredTreeFactory(new StringLabelFactory())).readTree();
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 753 */     p.reset(t);
/* 754 */     if (p.findNext()) {
/* 755 */       p.node().pennPrint();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static class ListTokenizer
/*     */     extends AbstractTokenizer
/*     */   {
/*     */     ListIterator li;
/*     */     
/*     */     protected Object getNext()
/*     */     {
/* 767 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public ListTokenizer(List l) {
/* 771 */       this.li = l.listIterator();
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 775 */       return this.li.hasNext();
/*     */     }
/*     */     
/*     */     public Object next() {
/* 779 */       return this.li.next();
/*     */     }
/*     */     
/*     */     public Object peek() {
/* 783 */       if (!this.li.hasNext()) {
/* 784 */         return null;
/*     */       }
/* 786 */       Object result = this.li.next();
/* 787 */       this.li.previous();
/* 788 */       return result;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 792 */       this.li.remove();
/*     */     }
/*     */     
/*     */     public void setSource(Reader r) {
/* 796 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\TreePattern.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */