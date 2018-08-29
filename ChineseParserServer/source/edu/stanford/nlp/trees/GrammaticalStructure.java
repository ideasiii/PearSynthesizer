/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.MapLabel;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GrammaticalStructure
/*     */   extends TreeGraph
/*     */   implements Serializable
/*     */ {
/*  39 */   protected Set<Dependency> dependencies = null;
/*  40 */   protected Collection<TypedDependency> typedDependencies = null;
/*  41 */   protected Collection<TypedDependency> allTypedDependencies = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GrammaticalStructure(Tree t, Collection<GrammaticalRelation> relations, HeadFinder hf, Filter<String> puncFilter)
/*     */   {
/*  54 */     super(t);
/*     */     
/*  56 */     this.root.percolateHeads(hf);
/*     */     
/*  58 */     NoPunctFilter puncDepFilter = new NoPunctFilter(puncFilter);
/*  59 */     NoPunctTypedDependencyFilter puncTypedDepFilter = new NoPunctTypedDependencyFilter(puncFilter);
/*  60 */     this.dependencies = this.root.dependencies(puncDepFilter);
/*  61 */     for (Dependency p : this.dependencies)
/*     */     {
/*  63 */       TreeGraphNode gov = (TreeGraphNode)p.governor();
/*  64 */       TreeGraphNode dep = (TreeGraphNode)p.dependent();
/*  65 */       dep.addArc(GrammaticalRelation.GOVERNOR, gov);
/*     */     }
/*     */     
/*  68 */     analyzeNode(this.root, this.root, relations);
/*     */     
/*  70 */     this.typedDependencies = getDeps(false, puncTypedDepFilter);
/*  71 */     this.allTypedDependencies = getDeps(true, puncTypedDepFilter);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  75 */     StringBuilder sb = new StringBuilder(super.toString());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void analyzeNode(TreeGraphNode t, TreeGraphNode root, Collection<GrammaticalRelation> relations) {
/*  86 */     if (t.numChildren() > 0) {
/*  87 */       TreeGraphNode tHigh = t.highestNodeWithSameHead();
/*  88 */       for (Iterator i$ = relations.iterator(); i$.hasNext();) { egr = (GrammaticalRelation)i$.next();
/*  89 */         if (egr.isApplicable(t)) {
/*  90 */           for (Tree u : egr.getRelatedNodes(t, root)) {
/*  91 */             tHigh.addArc(egr, (TreeGraphNode)u);
/*     */           }
/*     */         }
/*     */       }
/*     */       GrammaticalRelation egr;
/*  96 */       for (Tree kid : t.children()) {
/*  97 */         analyzeNode((TreeGraphNode)kid, root, relations);
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
/*     */   public Collection<TypedDependency> getDeps(boolean getExtra, Filter<TypedDependency> f)
/*     */   {
/* 110 */     List<TypedDependency> basicDep = new ArrayList();
/*     */     
/* 112 */     for (Dependency d : dependencies()) {
/* 113 */       TreeGraphNode gov = (TreeGraphNode)d.governor();
/* 114 */       TreeGraphNode dep = (TreeGraphNode)d.dependent();
/*     */       
/*     */ 
/* 117 */       GrammaticalRelation reln = getGrammaticalRelation(gov, dep);
/*     */       
/* 119 */       basicDep.add(new TypedDependency(reln, gov, dep));
/*     */     }
/* 121 */     if (getExtra) {
/* 122 */       TreeGraphNode root = root();
/* 123 */       getDep(root, root, basicDep, f);
/*     */     }
/* 125 */     Collections.sort(basicDep);
/* 126 */     return basicDep;
/*     */   }
/*     */   
/*     */   private void getDep(TreeGraphNode t, TreeGraphNode root, List<TypedDependency> basicDep, Filter<TypedDependency> f)
/*     */   {
/* 131 */     if (t.numChildren() > 0) {
/* 132 */       Map depMap = getAllDependents(t);
/* 133 */       for (Object depName : depMap.keySet()) {
/* 134 */         for (Object depNode : (HashSet)depMap.get(depName)) {
/* 135 */           gov = t.headWordNode();
/* 136 */           dep = ((TreeGraphNode)depNode).headWordNode();
/* 137 */           if (gov != dep) {
/* 138 */             List<GrammaticalRelation> rels = getListGrammaticalRelation(t, (TreeGraphNode)depNode);
/* 139 */             if (!rels.isEmpty()) {
/* 140 */               for (GrammaticalRelation rel : rels) {
/* 141 */                 TypedDependency newDep = new TypedDependency(rel, gov, dep);
/* 142 */                 if ((!basicDep.contains(newDep)) && (f.accept(newDep)))
/* 143 */                   basicDep.add(newDep);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       TreeGraphNode gov;
/*     */       TreeGraphNode dep;
/* 151 */       for (Tree kid : t.children()) {
/* 152 */         getDep((TreeGraphNode)kid, root, basicDep, f);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class NoPunctFilter implements Filter<Dependency>
/*     */   {
/*     */     private Filter<String> npf;
/*     */     
/*     */     NoPunctFilter(Filter<String> f) {
/* 162 */       this.npf = f;
/*     */     }
/*     */     
/*     */     public boolean accept(Dependency d) {
/* 166 */       if (d == null) {
/* 167 */         return false;
/*     */       }
/* 169 */       Label lab = d.dependent();
/* 170 */       if (lab == null) {
/* 171 */         return false;
/*     */       }
/* 173 */       return this.npf.accept(lab.value());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class NoPunctTypedDependencyFilter
/*     */     implements Filter<TypedDependency>
/*     */   {
/*     */     private Filter<String> npf;
/*     */     
/*     */     NoPunctTypedDependencyFilter(Filter<String> f)
/*     */     {
/* 184 */       this.npf = f;
/*     */     }
/*     */     
/*     */     public boolean accept(TypedDependency d) {
/* 188 */       if (d == null) {
/* 189 */         return false;
/*     */       }
/* 191 */       Object s = d.dep();
/* 192 */       if ((s == null) || (!(s instanceof TreeGraphNode))) {
/* 193 */         return false;
/*     */       }
/* 195 */       Object l = ((TreeGraphNode)s).label();
/* 196 */       if ((l == null) || (!(l instanceof Label))) {
/* 197 */         return false;
/*     */       }
/* 199 */       return this.npf.accept(((Label)l).value());
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
/*     */   public Set<Dependency> dependencies()
/*     */   {
/* 212 */     return this.dependencies;
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
/*     */   public Set<Tree> getDependents(TreeGraphNode t)
/*     */   {
/* 226 */     Set<Tree> deps = new TreeSet();
/* 227 */     Set<Tree> nodes = this.root.subTrees();
/* 228 */     for (Iterator it = nodes.iterator(); it.hasNext();) {
/* 229 */       TreeGraphNode node = (TreeGraphNode)it.next();
/* 230 */       TreeGraphNode gov = getNodeInRelation(node, GrammaticalRelation.GOVERNOR);
/* 231 */       if ((gov != null) && (gov == t)) {
/* 232 */         deps.add(node);
/*     */       }
/*     */     }
/* 235 */     return deps;
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
/*     */   public TreeGraphNode getGovernor(TreeGraphNode t)
/*     */   {
/* 249 */     return t.followArcToNode(GrammaticalRelation.GOVERNOR);
/*     */   }
/*     */   
/*     */   public TreeGraphNode getNodeInRelation(TreeGraphNode t, GrammaticalRelation r) {
/* 253 */     return t.followArcToNode(r);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GrammaticalRelation getGrammaticalRelation(int govIndex, int depIndex)
/*     */   {
/* 261 */     TreeGraphNode gov = getNodeByIndex(govIndex);
/* 262 */     TreeGraphNode dep = getNodeByIndex(depIndex);
/* 263 */     return getGrammaticalRelation(gov, dep);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GrammaticalRelation getGrammaticalRelation(TreeGraphNode gov, TreeGraphNode dep)
/*     */   {
/* 271 */     GrammaticalRelation reln = GrammaticalRelation.DEPENDENT;
/* 272 */     TreeGraphNode govH = gov.highestNodeWithSameHead();
/* 273 */     TreeGraphNode depH = dep.highestNodeWithSameHead();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 279 */     SortedSet arcLabels = new TreeSet(govH.arcLabelsToNode(depH));
/*     */     
/*     */ 
/*     */ 
/* 283 */     for (Object arcLabel : arcLabels)
/*     */     {
/* 285 */       if ((arcLabel != null) && ((arcLabel instanceof GrammaticalRelation))) {
/* 286 */         GrammaticalRelation reln2 = (GrammaticalRelation)arcLabel;
/* 287 */         if (reln.isAncestor(reln2)) {
/* 288 */           reln = reln2;
/*     */         }
/*     */       }
/*     */     }
/* 292 */     return reln;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<GrammaticalRelation> getListGrammaticalRelation(TreeGraphNode gov, TreeGraphNode dep)
/*     */   {
/* 300 */     List<GrammaticalRelation> list = new ArrayList();
/* 301 */     TreeGraphNode govH = gov.highestNodeWithSameHead();
/* 302 */     TreeGraphNode depH = dep.highestNodeWithSameHead();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 309 */     Set arcLabels = govH.arcLabelsToNode(depH);
/*     */     
/* 311 */     if (dep != depH) {
/* 312 */       Set arcLabels2 = govH.arcLabelsToNode(dep);
/*     */       
/* 314 */       arcLabels.addAll(arcLabels2);
/*     */     }
/*     */     
/*     */ 
/* 318 */     for (Object arcLabel : arcLabels) {
/* 319 */       if ((arcLabel != null) && ((arcLabel instanceof GrammaticalRelation))) {
/* 320 */         GrammaticalRelation reln2 = (GrammaticalRelation)arcLabel;
/* 321 */         if (!list.isEmpty()) {
/* 322 */           for (int i = 0; i < list.size(); i++) {
/* 323 */             GrammaticalRelation gr = (GrammaticalRelation)list.get(i);
/*     */             
/* 325 */             if (gr.isAncestor(reln2)) {
/* 326 */               int index = list.indexOf(gr);
/* 327 */               list.set(index, reln2);
/*     */ 
/*     */             }
/* 330 */             else if (!reln2.isAncestor(gr)) {
/* 331 */               list.add(reln2);
/*     */             }
/*     */             
/*     */           }
/*     */         } else {
/* 336 */           list.add(reln2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 341 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<TypedDependency> typedDependencies()
/*     */   {
/* 348 */     return typedDependencies(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<TypedDependency> allTypedDependencies()
/*     */   {
/* 356 */     return typedDependencies(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<TypedDependency> typedDependencies(boolean includeExtras)
/*     */   {
/* 367 */     if (includeExtras) {
/* 368 */       correctDependencies(this.allTypedDependencies);
/* 369 */       return this.allTypedDependencies;
/*     */     }
/* 371 */     correctDependencies(this.typedDependencies);
/* 372 */     return this.typedDependencies;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<TypedDependency> typedDependenciesCollapsed()
/*     */   {
/* 382 */     return typedDependenciesCollapsed(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<TypedDependency> typedDependenciesCollapsed(boolean includeExtras)
/*     */   {
/* 394 */     Collection<TypedDependency> tdl = typedDependencies(includeExtras);
/* 395 */     collapseDependencies(tdl, false);
/* 396 */     return tdl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<TypedDependency> typedDependenciesCCprocessed(boolean includeExtras)
/*     */   {
/* 408 */     Collection<TypedDependency> tdl = typedDependencies(includeExtras);
/* 409 */     collapseDependencies(tdl, true);
/* 410 */     return tdl;
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
/*     */   protected void collapseDependencies(Collection<TypedDependency> list, boolean CCprocess) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void correctDependencies(Collection<TypedDependency> list) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getDependencyPath(int nodeIndex, int rootIndex)
/*     */   {
/* 447 */     TreeGraphNode node = getNodeByIndex(nodeIndex);
/* 448 */     TreeGraphNode root = getNodeByIndex(rootIndex);
/* 449 */     return getDependencyPath(node, root);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getDependencyPath(TreeGraphNode node, TreeGraphNode root)
/*     */   {
/* 461 */     List<String> path = new ArrayList();
/*     */     for (;;) {
/* 463 */       TreeGraphNode gov = node.followArcToNode(GrammaticalRelation.GOVERNOR);
/* 464 */       Set relations = node.arcLabelsToNode(gov);
/* 465 */       StringBuilder sb = new StringBuilder();
/* 466 */       for (Object arcLabel : relations)
/*     */       {
/* 468 */         sb.append(sb.length() == 0 ? "" : "+").append(arcLabel.toString());
/*     */       }
/* 470 */       path.add(sb.toString());
/* 471 */       if (gov.equals(root)) {
/*     */         break;
/*     */       }
/* 474 */       node = gov;
/*     */     }
/* 476 */     return path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map getAllDependents(TreeGraphNode node)
/*     */   {
/* 486 */     Map newMap = new HashMap();
/* 487 */     Map m = node.label.map();
/* 488 */     for (Object o : m.keySet()) {
/* 489 */       if ((o instanceof GrammaticalRelation)) {
/* 490 */         newMap.put(o, m.get(o));
/*     */       }
/*     */     }
/* 493 */     return newMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isConnected(Collection<TypedDependency> list)
/*     */   {
/* 503 */     return getRoots(list).size() <= 1;
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
/*     */   public Collection<TypedDependency> getRoots(Collection<TypedDependency> list)
/*     */   {
/* 516 */     Collection<TypedDependency> roots = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/* 520 */     Collection<TreeGraphNode> deps = new HashSet();
/* 521 */     for (TypedDependency typedDep : list) {
/* 522 */       deps.add(typedDep.dep());
/*     */     }
/*     */     
/*     */ 
/* 526 */     Collection<TreeGraphNode> govs = new HashSet();
/* 527 */     for (TypedDependency typedDep : list) {
/* 528 */       TreeGraphNode gov = typedDep.gov();
/* 529 */       if ((!deps.contains(gov)) && (!govs.contains(gov))) {
/* 530 */         roots.add(typedDep);
/*     */       }
/* 532 */       govs.add(gov);
/*     */     }
/* 534 */     return roots;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\GrammaticalStructure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */