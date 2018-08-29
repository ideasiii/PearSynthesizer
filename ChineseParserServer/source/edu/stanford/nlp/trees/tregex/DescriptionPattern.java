/*     */ package edu.stanford.nlp.trees.tregex;
/*     */ 
/*     */ import edu.stanford.nlp.process.Function;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ class DescriptionPattern extends TregexPattern
/*     */ {
/*     */   private Relation rel;
/*     */   private boolean negDesc;
/*     */   private Pattern descPattern;
/*     */   private String stringDesc;
/*     */   private Object name;
/*     */   private boolean isLink;
/*     */   private TregexPattern child;
/*     */   private List<Pair<Integer, String>> variableGroups;
/*     */   private Function<String, String> basicCatFunction;
/*     */   
/*     */   public DescriptionPattern(Relation rel, boolean negDesc, String desc, Object name, boolean useBasicCat)
/*     */   {
/*  25 */     this(rel, negDesc, desc, name, useBasicCat, new java.util.ArrayList(0));
/*     */   }
/*     */   
/*     */   public DescriptionPattern(Relation rel, boolean negDesc, String desc, Object name, boolean useBasicCat, List varGroups) {
/*  29 */     this(rel, negDesc, desc, name, useBasicCat, false, varGroups);
/*     */   }
/*     */   
/*     */   public DescriptionPattern(Relation rel, boolean negDesc, String desc, Object name, boolean useBasicCat, boolean ignoreCase, List variableGroups) {
/*  33 */     this.rel = rel;
/*  34 */     this.negDesc = negDesc;
/*  35 */     if (desc != null) {
/*  36 */       this.stringDesc = desc;
/*  37 */       if (desc.equals("__")) {
/*  38 */         this.descPattern = Pattern.compile(".*");
/*  39 */       } else if (desc.matches("/.*/")) {
/*  40 */         this.descPattern = Pattern.compile(desc.substring(1, desc.length() - 1));
/*     */       } else {
/*  42 */         this.descPattern = Pattern.compile("^(" + desc + ")$");
/*     */       }
/*     */     } else {
/*  45 */       assert (name != null);
/*  46 */       this.stringDesc = " ";
/*  47 */       this.descPattern = null;
/*     */     }
/*  49 */     this.name = name;
/*  50 */     this.child = null;
/*  51 */     this.basicCatFunction = (useBasicCat ? currentBasicCatFunction : null);
/*     */     
/*  53 */     this.variableGroups = variableGroups;
/*     */   }
/*     */   
/*     */   public void makeLink() {
/*  57 */     this.isLink = true;
/*     */   }
/*     */   
/*     */   public String localString() {
/*  61 */     return this.rel.toString() + ' ' + (this.negDesc ? "!" : "") + (this.basicCatFunction != null ? "@" : "") + this.stringDesc + (this.name == null ? "" : new StringBuilder().append('=').append(this.name.toString()).toString());
/*     */   }
/*     */   
/*     */   public String toString() {
/*  65 */     StringBuffer sb = new StringBuffer();
/*  66 */     if (isNegated()) {
/*  67 */       sb.append('!');
/*     */     }
/*  69 */     if (isOptional()) {
/*  70 */       sb.append('?');
/*     */     }
/*  72 */     sb.append(this.rel.toString());
/*  73 */     sb.append(' ');
/*  74 */     if (this.child != null) {
/*  75 */       sb.append('(');
/*     */     }
/*  77 */     if (this.negDesc) {
/*  78 */       sb.append('!');
/*     */     }
/*  80 */     if (this.basicCatFunction != null) {
/*  81 */       sb.append("@");
/*     */     }
/*  83 */     sb.append(this.stringDesc);
/*  84 */     if (this.name != null) {
/*  85 */       sb.append("=" + this.name.toString());
/*     */     }
/*  87 */     sb.append(' ');
/*  88 */     if (this.child != null) {
/*  89 */       sb.append(this.child.toString());
/*  90 */       sb.append(')');
/*     */     }
/*  92 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void setChild(TregexPattern n) {
/*  96 */     this.child = n;
/*     */   }
/*     */   
/*     */   public List getChildren() {
/* 100 */     if (this.child == null) {
/* 101 */       return java.util.Collections.EMPTY_LIST;
/*     */     }
/* 103 */     return java.util.Collections.singletonList(this.child);
/*     */   }
/*     */   
/*     */   public TregexMatcher matcher(Tree root, Tree tree, Map<Object, Tree> namesToNodes, VariableStrings variableStrings)
/*     */   {
/* 108 */     return new DescriptionMatcher(this, root, tree, namesToNodes, variableStrings);
/*     */   }
/*     */   
/*     */   private static class DescriptionMatcher extends TregexMatcher {
/* 112 */     private java.util.Iterator treeNodeMatchCandidateIterator = null;
/*     */     private final DescriptionPattern myNode;
/*     */     private TregexMatcher childMatcher;
/*     */     private Tree nextTreeNodeMatchCandidate;
/* 116 */     private boolean finished = false;
/* 117 */     private boolean matchedOnce = false;
/* 118 */     private boolean committedVariables = false;
/*     */     
/*     */ 
/*     */ 
/*     */     public DescriptionMatcher(DescriptionPattern n, Tree root, Tree tree, Map<Object, Tree> namesToNodes, VariableStrings variableStrings)
/*     */     {
/* 124 */       super(tree, namesToNodes, variableStrings);
/* 125 */       this.myNode = n;
/* 126 */       resetChildIter();
/*     */     }
/*     */     
/*     */     void resetChildIter() {
/* 130 */       this.treeNodeMatchCandidateIterator = this.myNode.rel.searchNodeIterator(this.tree, this.root);
/* 131 */       this.finished = false;
/* 132 */       this.nextTreeNodeMatchCandidate = null;
/*     */     }
/*     */     
/*     */     private void resetChild() {
/* 136 */       if (this.childMatcher == null) {
/* 137 */         if (this.myNode.child == null) {
/* 138 */           this.matchedOnce = false;
/*     */         } else {
/* 140 */           this.childMatcher = this.myNode.child.matcher(this.root, this.nextTreeNodeMatchCandidate, this.namesToNodes, this.variableStrings);
/*     */         }
/*     */       } else {
/* 143 */         this.childMatcher.resetChildIter(this.nextTreeNodeMatchCandidate);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private void goToNextTreeNodeMatch()
/*     */     {
/* 150 */       decommitVariableGroups();
/* 151 */       this.finished = true;
/* 152 */       Matcher m = null;
/* 153 */       while (this.treeNodeMatchCandidateIterator.hasNext()) {
/* 154 */         this.nextTreeNodeMatchCandidate = ((Tree)this.treeNodeMatchCandidateIterator.next());
/* 155 */         if (this.myNode.descPattern == null)
/*     */         {
/* 157 */           if (this.myNode.isLink) {
/* 158 */             Tree otherTree = (Tree)this.namesToNodes.get(this.myNode.name);
/* 159 */             if (otherTree != null) {
/* 160 */               String otherValue = this.myNode.basicCatFunction == null ? otherTree.value() : (String)this.myNode.basicCatFunction.apply(otherTree.value());
/* 161 */               String myValue = this.myNode.basicCatFunction == null ? this.nextTreeNodeMatchCandidate.value() : (String)this.myNode.basicCatFunction.apply(this.nextTreeNodeMatchCandidate.value());
/* 162 */               if (otherValue.equals(myValue)) {
/* 163 */                 this.finished = false;
/* 164 */                 break;
/*     */               }
/*     */             }
/* 167 */           } else if (this.namesToNodes.get(this.myNode.name) == this.nextTreeNodeMatchCandidate) {
/* 168 */             this.finished = false;
/* 169 */             break;
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 177 */           String value = this.nextTreeNodeMatchCandidate.value();
/* 178 */           boolean found; boolean found; if (value == null) {
/* 179 */             found = false;
/*     */           } else {
/* 181 */             if (this.myNode.basicCatFunction != null) {
/* 182 */               value = (String)this.myNode.basicCatFunction.apply(value);
/*     */             }
/* 184 */             m = this.myNode.descPattern.matcher(value);
/* 185 */             found = m.find();
/*     */           }
/* 187 */           if (found) {
/* 188 */             for (Pair<Integer, String> varGroup : this.myNode.variableGroups) {
/* 189 */               String thisVariable = (String)varGroup.second();
/* 190 */               String thisVarString = this.variableStrings.getString(thisVariable);
/* 191 */               if ((thisVarString != null) && (!thisVarString.equals(m.group(((Integer)varGroup.first()).intValue())))) {
/* 192 */                 found = false;
/* 193 */                 break;
/*     */               }
/*     */             }
/*     */           }
/* 197 */           if (found != this.myNode.negDesc) {
/* 198 */             this.finished = false;
/* 199 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 203 */       if (!this.finished) {
/* 204 */         resetChild();
/* 205 */         if (this.myNode.name != null)
/*     */         {
/* 207 */           this.namesToNodes.put(this.myNode.name, this.nextTreeNodeMatchCandidate);
/*     */         }
/* 209 */         commitVariableGroups(m);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void commitVariableGroups(Matcher m)
/*     */     {
/* 217 */       this.committedVariables = true;
/* 218 */       for (Pair<Integer, String> varGroup : this.myNode.variableGroups) {
/* 219 */         String thisVarString = m.group(((Integer)varGroup.first()).intValue());
/* 220 */         this.variableStrings.setVar(varGroup.second(), thisVarString);
/*     */       }
/*     */     }
/*     */     
/*     */     private void decommitVariableGroups() {
/* 225 */       if (this.committedVariables) {
/* 226 */         for (Pair<Integer, String> varGroup : this.myNode.variableGroups)
/* 227 */           this.variableStrings.unsetVar(varGroup.second());
/*     */       }
/* 229 */       this.committedVariables = false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean matchChild()
/*     */     {
/* 239 */       if (this.nextTreeNodeMatchCandidate == null) {
/* 240 */         return false;
/*     */       }
/* 242 */       if (this.childMatcher == null) {
/* 243 */         if (!this.matchedOnce) {
/* 244 */           this.matchedOnce = true;
/* 245 */           return true;
/*     */         }
/* 247 */         return false;
/*     */       }
/* 249 */       return this.childMatcher.matches();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean matches()
/*     */     {
/* 255 */       if (this.finished) {
/* 256 */         return false;
/*     */       }
/* 258 */       while (!this.finished) {
/* 259 */         if (matchChild()) {
/* 260 */           if (this.myNode.isNegated())
/*     */           {
/* 262 */             this.finished = true;
/* 263 */             return false;
/*     */           }
/* 265 */           if (this.myNode.isOptional()) {
/* 266 */             this.finished = true;
/*     */           }
/* 268 */           return true;
/*     */         }
/*     */         
/* 271 */         goToNextTreeNodeMatch();
/*     */       }
/*     */       
/* 274 */       if (this.myNode.isNegated()) {
/* 275 */         return true;
/*     */       }
/* 277 */       this.nextTreeNodeMatchCandidate = null;
/* 278 */       if (this.myNode.name != null) {
/* 279 */         this.namesToNodes.remove(this.myNode.name);
/*     */       }
/*     */       
/* 282 */       return this.myNode.isOptional();
/*     */     }
/*     */     
/*     */     public Tree getMatch()
/*     */     {
/* 287 */       return this.nextTreeNodeMatchCandidate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\DescriptionPattern.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */