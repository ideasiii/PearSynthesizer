/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.trees.tregex.ParseException;
/*     */ import edu.stanford.nlp.trees.tregex.TregexMatcher;
/*     */ import edu.stanford.nlp.trees.tregex.TregexPattern;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GrammaticalRelation
/*     */   implements Comparable<GrammaticalRelation>
/*     */ {
/*  86 */   public static final GrammaticalRelation GOVERNOR = new GrammaticalRelation("gov", "governor", null, null, StringUtils.EMPTY_STRING_ARRAY);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   public static final GrammaticalRelation DEPENDENT = new GrammaticalRelation("dep", "dependent", null, null, StringUtils.EMPTY_STRING_ARRAY);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   public static final GrammaticalRelation KILL = new GrammaticalRelation("KILL", "dummy relation kill", null, null, StringUtils.EMPTY_STRING_ARRAY);
/*     */   
/*     */   private final String shortName;
/*     */   private final String longName;
/*     */   private GrammaticalRelation parent;
/* 104 */   private List<GrammaticalRelation> children = new ArrayList();
/*     */   
/* 106 */   private Pattern sourcePattern = null;
/* 107 */   private List<TregexPattern> targetPatterns = new ArrayList();
/* 108 */   private String specific = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GrammaticalRelation(String shortName, String longName, GrammaticalRelation parent, String sourcePattern, String[] targetPatterns)
/*     */   {
/* 115 */     this.shortName = shortName;
/* 116 */     this.longName = longName;
/* 117 */     this.parent = parent;
/* 118 */     if (parent != null) {
/* 119 */       parent.addChild(this);
/*     */     }
/* 121 */     if (sourcePattern != null) {
/*     */       try {
/* 123 */         this.sourcePattern = Pattern.compile(sourcePattern);
/*     */       } catch (PatternSyntaxException e) {
/* 125 */         throw new RuntimeException("Bad pattern: " + sourcePattern);
/*     */       }
/*     */     }
/* 128 */     for (String pattern : targetPatterns) {
/* 129 */       TregexPattern p = null;
/*     */       try {
/* 131 */         p = TregexPattern.compile(pattern);
/*     */       } catch (ParseException pe) {
/* 133 */         throw new RuntimeException("Bad pattern: " + pattern);
/*     */       }
/* 135 */       this.targetPatterns.add(p);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addChild(GrammaticalRelation child) {
/* 140 */     this.children.add(child);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public GrammaticalRelation(String shortName, String longName, GrammaticalRelation parent, String sourcePattern, String[] targetPatterns, String specificString)
/*     */   {
/* 147 */     this(shortName, longName, parent, sourcePattern, targetPatterns);
/* 148 */     this.specific = specificString;
/*     */   }
/*     */   
/*     */   public String getSpecific() {
/* 152 */     return this.specific;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Tree> getRelatedNodes(Tree t, Tree root)
/*     */   {
/* 160 */     Set<Tree> nodeList = new LinkedHashSet();
/* 161 */     for (TregexPattern p : this.targetPatterns) {
/* 162 */       if (root.value() == null) {
/* 163 */         root.setValue("ROOT");
/*     */       }
/* 165 */       TregexMatcher m = p.matcher(root);
/* 166 */       while (m.find()) {
/* 167 */         if (m.getMatch() == t) {
/* 168 */           nodeList.add(m.getNode("target"));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 173 */     return nodeList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isApplicable(Tree t)
/*     */   {
/* 183 */     if (t.value() != null)
/* 184 */       return (this.sourcePattern != null) && (this.sourcePattern.matcher(t.value()).matches());
/* 185 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAncestor(GrammaticalRelation gr) {
/* 189 */     while (gr != null) {
/* 190 */       if (this == gr) return true;
/* 191 */       gr = gr.parent;
/*     */     }
/* 193 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 201 */     if (this.specific == null) {
/* 202 */       return this.shortName;
/*     */     }
/* 204 */     return this.shortName + "_" + this.specific;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GrammaticalRelation parent()
/*     */   {
/* 212 */     return this.parent;
/*     */   }
/*     */   
/*     */   public boolean equals(GrammaticalRelation gr) {
/* 216 */     if ((this.shortName.equals(gr.shortName)) && (this.specific == gr.specific)) {
/* 217 */       return true;
/*     */     }
/* 219 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toPrettyString()
/*     */   {
/* 231 */     StringBuilder buf = new StringBuilder("\n");
/* 232 */     toPrettyString(0, buf);
/* 233 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GrammaticalRelation valueOf(String s, List<GrammaticalRelation> values)
/*     */   {
/* 245 */     for (GrammaticalRelation reln : values) {
/* 246 */       if (reln.toString().equals(s)) { return reln;
/*     */       }
/*     */     }
/* 249 */     return null;
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
/*     */   private void toPrettyString(int indentLevel, StringBuilder buf)
/*     */   {
/* 262 */     for (int i = 0; i < indentLevel; i++) {
/* 263 */       buf.append("  ");
/*     */     }
/* 265 */     buf.append(this.shortName).append(": ").append(this.targetPatterns);
/* 266 */     for (GrammaticalRelation child : this.children) {
/* 267 */       buf.append("\n");
/* 268 */       child.toPrettyString(indentLevel + 1, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public int compareTo(GrammaticalRelation o) {
/* 273 */     StringBuilder thisName = new StringBuilder(this.shortName);
/* 274 */     StringBuilder oName = new StringBuilder(o.shortName);
/* 275 */     thisName.append(this.specific);
/* 276 */     oName.append(o.specific);
/* 277 */     String thisN = thisName.toString();
/* 278 */     String oN = oName.toString();
/* 279 */     return thisN.compareTo(oN);
/*     */   }
/*     */   
/*     */   public String getLongName() {
/* 283 */     return this.longName;
/*     */   }
/*     */   
/*     */   public String getShortName() {
/* 287 */     return this.shortName;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\GrammaticalRelation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */