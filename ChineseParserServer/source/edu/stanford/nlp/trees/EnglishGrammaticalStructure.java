/*      */ package edu.stanford.nlp.trees;
/*      */ 
/*      */ import edu.stanford.nlp.ling.MapLabel;
/*      */ import edu.stanford.nlp.ling.Word;
/*      */ import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
/*      */ import edu.stanford.nlp.process.PTBTokenizer;
/*      */ import edu.stanford.nlp.util.Filter;
/*      */ import edu.stanford.nlp.util.Pair;
/*      */ import edu.stanford.nlp.util.StringUtils;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileReader;
/*      */ import java.io.PrintStream;
/*      */ import java.io.StringReader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class EnglishGrammaticalStructure
/*      */   extends GrammaticalStructure
/*      */ {
/*      */   public static final String CONJ_MARKER = "conj_";
/*      */   public static final String DEFAULT_PARSER_FILE = "/u/nlp/data/lexparser/englishPCFG.ser.gz";
/*      */   private static final boolean DEBUG = false;
/*      */   
/*      */   public EnglishGrammaticalStructure(Tree t)
/*      */   {
/*   50 */     this(t, new PennTreebankLanguagePack().punctuationWordRejectFilter());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public EnglishGrammaticalStructure(Tree t, Filter<String> puncFilter)
/*      */   {
/*   67 */     super(new CoordinationTransformer().transformTree(t), EnglishGrammaticalRelations.values(), new SemanticHeadFinder(true), puncFilter);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TreeGraphNode getSubject(TreeGraphNode t)
/*      */   {
/*   82 */     TreeGraphNode subj = getNodeInRelation(t, EnglishGrammaticalRelations.NOMINAL_SUBJECT);
/*   83 */     if (subj == null) {
/*   84 */       return getNodeInRelation(t, EnglishGrammaticalRelations.CLAUSAL_SUBJECT);
/*      */     }
/*   86 */     return subj;
/*      */   }
/*      */   
/*      */ 
/*      */   protected void correctDependencies(Collection<TypedDependency> list)
/*      */   {
/*   92 */     correctSubjPassAndPoss(list);
/*      */   }
/*      */   
/*      */   private static void printListSorted(String title, Collection<TypedDependency> list)
/*      */   {
/*   97 */     List<TypedDependency> lis = new ArrayList(list);
/*   98 */     Collections.sort(lis);
/*   99 */     if (title != null) System.err.println(title);
/*  100 */     System.err.println(lis);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void collapseDependencies(Collection<TypedDependency> list, boolean CCprocess)
/*      */   {
/*  141 */     correctDependencies(list);
/*      */     
/*      */ 
/*  144 */     eraseMultiConj(list);
/*      */     
/*      */ 
/*  147 */     collapseMWP(list);
/*      */     
/*      */ 
/*  150 */     collapseFlatMWP(list);
/*      */     
/*      */ 
/*  153 */     collapsePrepAndPoss(list);
/*      */     
/*      */ 
/*  156 */     collapseConj(list);
/*      */     
/*      */ 
/*  159 */     if (CCprocess) {
/*  160 */       treatCC(list);
/*      */     }
/*      */     
/*      */ 
/*  164 */     collapseReferent(list);
/*      */     
/*      */ 
/*  167 */     Collections.sort((List)list);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected GrammaticalRelation conjValue(Object conj)
/*      */   {
/*  174 */     String newConj = conj.toString().toLowerCase();
/*  175 */     if ((conj.toString().equals("not")) || (conj.toString().equals("instead")) || (conj.toString().equals("rather"))) {
/*  176 */       newConj = "negcc";
/*  177 */     } else if ((conj.toString().equals("to")) || (conj.toString().equals("also")) || (conj.toString().contains("well"))) {
/*  178 */       newConj = "and";
/*      */     }
/*  180 */     return EnglishGrammaticalRelations.getConj(newConj);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void treatCC(Collection<TypedDependency> list)
/*      */   {
/*  187 */     Map<TreeGraphNode, Set<TypedDependency>> map = new HashMap();
/*      */     
/*      */ 
/*  190 */     Map<TreeGraphNode, TypedDependency> subjectMap = new HashMap();
/*      */     
/*  192 */     for (TypedDependency typedDep : list) {
/*  193 */       if (!map.containsKey(typedDep.dep())) {
/*  194 */         map.put(typedDep.dep(), new TreeSet());
/*      */       }
/*  196 */       ((Set)map.get(typedDep.dep())).add(typedDep);
/*      */       
/*  198 */       if (((typedDep.reln().parent() == EnglishGrammaticalRelations.NOMINAL_SUBJECT) || (typedDep.reln().parent() == EnglishGrammaticalRelations.SUBJECT) || (typedDep.reln().parent() == EnglishGrammaticalRelations.CLAUSAL_SUBJECT)) && 
/*  199 */         (!subjectMap.containsKey(typedDep.gov()))) {
/*  200 */         subjectMap.put(typedDep.gov(), typedDep);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  209 */     Collection<TypedDependency> newTypedDeps = new ArrayList(list);
/*      */     
/*      */ 
/*  212 */     for (TypedDependency td : list) {
/*  213 */       if (EnglishGrammaticalRelations.getConjs().contains(td.reln())) {
/*  214 */         TreeGraphNode gov = td.gov();
/*  215 */         TreeGraphNode dep = td.dep();
/*      */         
/*  217 */         Set<TypedDependency> gov_relations = (Set)map.get(gov);
/*  218 */         if (gov_relations != null) {
/*  219 */           for (TypedDependency td1 : gov_relations)
/*      */           {
/*  221 */             TreeGraphNode newGov = td1.gov();
/*  222 */             GrammaticalRelation newRel = td1.reln();
/*  223 */             newTypedDeps.add(new TypedDependency(newRel, newGov, dep));
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  233 */         if ((subjectMap.containsKey(gov)) && (dep.parent().value().startsWith("VB")) && (!subjectMap.containsKey(dep))) {
/*  234 */           TypedDependency tdsubj = (TypedDependency)subjectMap.get(gov);
/*  235 */           newTypedDeps.add(new TypedDependency(tdsubj.reln(), dep, tdsubj.dep()));
/*      */         }
/*      */       }
/*      */     }
/*  239 */     list.clear();
/*  240 */     list.addAll(newTypedDeps);
/*      */   }
/*      */   
/*      */   private void collapseConj(Collection<TypedDependency> list)
/*      */   {
/*  245 */     for (TypedDependency td : list) {
/*  246 */       if (td.reln() == EnglishGrammaticalRelations.COORDINATION) {
/*  247 */         gov = td.gov();
/*  248 */         conj = conjValue(td.dep().value());
/*      */         
/*      */ 
/*  251 */         for (TypedDependency td1 : list) {
/*  252 */           if ((td1.reln() == EnglishGrammaticalRelations.CONJUNCT) && (td1.gov() == gov))
/*      */           {
/*  254 */             td1.setReln(conj);
/*  255 */           } else if (td1.reln() == EnglishGrammaticalRelations.COORDINATION)
/*  256 */             conj = conjValue(td1.dep().value());
/*      */         }
/*      */       }
/*      */     }
/*      */     TreeGraphNode gov;
/*      */     GrammaticalRelation conj;
/*  262 */     Collection<TypedDependency> newTypedDeps = new ArrayList();
/*  263 */     for (TypedDependency td : list) {
/*  264 */       if (td.reln() != EnglishGrammaticalRelations.COORDINATION) {
/*  265 */         newTypedDeps.add(td);
/*      */       }
/*      */     }
/*  268 */     list.clear();
/*  269 */     list.addAll(newTypedDeps);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void collapseReferent(Collection<TypedDependency> list)
/*      */   {
/*  281 */     List<Pair<TreeGraphNode, TreeGraphNode>> refs = new ArrayList();
/*  282 */     for (TypedDependency td : list) {
/*  283 */       if (td.reln() == EnglishGrammaticalRelations.REFERENT) {
/*  284 */         Pair<TreeGraphNode, TreeGraphNode> ref = new Pair(td.dep(), td.gov());
/*  285 */         refs.add(ref);
/*      */       }
/*      */     }
/*      */     
/*  289 */     for (Pair<TreeGraphNode, TreeGraphNode> ref : refs) {
/*  290 */       dep = (TreeGraphNode)ref.first();
/*  291 */       ant = (TreeGraphNode)ref.second();
/*  292 */       for (TypedDependency td : list) {
/*  293 */         if ((td.dep() == dep) && (td.reln() != EnglishGrammaticalRelations.RELATIVE)) {
/*  294 */           td.setDep(ant);
/*      */         }
/*      */       }
/*      */     }
/*      */     TreeGraphNode dep;
/*      */     TreeGraphNode ant;
/*  300 */     Collection<TypedDependency> newTypedDeps = new ArrayList();
/*  301 */     for (TypedDependency td : list) {
/*  302 */       if (td.reln() != EnglishGrammaticalRelations.REFERENT) {
/*  303 */         newTypedDeps.add(td);
/*      */       }
/*      */     }
/*  306 */     list.clear();
/*  307 */     list.addAll(newTypedDeps);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void correctSubjPassAndPoss(Collection<TypedDependency> list)
/*      */   {
/*  319 */     List<TreeGraphNode> list_auxpass = new ArrayList();
/*  320 */     for (TypedDependency td : list) {
/*  321 */       if (td.reln() == EnglishGrammaticalRelations.AUX_PASSIVE_MODIFIER) {
/*  322 */         list_auxpass.add(td.gov());
/*      */       }
/*      */     }
/*      */     
/*  326 */     for (TypedDependency td : list) {
/*  327 */       if ((td.reln() == EnglishGrammaticalRelations.NOMINAL_SUBJECT) && (list_auxpass.contains(td.gov()))) {
/*  328 */         td.setReln(EnglishGrammaticalRelations.NOMINAL_PASSIVE_SUBJECT);
/*      */       }
/*  330 */       if ((td.reln() == EnglishGrammaticalRelations.CLAUSAL_SUBJECT) && (list_auxpass.contains(td.gov()))) {
/*  331 */         td.setReln(EnglishGrammaticalRelations.CLAUSAL_PASSIVE_SUBJECT);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  336 */       String tag = td.dep().parent().value();
/*  337 */       if ((td.reln() == GrammaticalRelation.DEPENDENT) && ((tag.equals("PRP$")) || (tag.equals("WP$")))) {
/*  338 */         td.setReln(EnglishGrammaticalRelations.POSSESSION_MODIFIER);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void collapsePrepAndPoss(Collection<TypedDependency> list)
/*      */   {
/*  347 */     Collection<TypedDependency> newTypedDeps = new ArrayList();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  352 */     Map<TreeGraphNode, SortedSet<TypedDependency>> map = new HashMap();
/*  353 */     for (TypedDependency typedDep : list) {
/*  354 */       if (!map.containsKey(typedDep.gov())) {
/*  355 */         map.put(typedDep.gov(), new TreeSet());
/*      */       }
/*  357 */       ((SortedSet)map.get(typedDep.gov())).add(typedDep);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  364 */     for (Iterator i$ = list.iterator(); i$.hasNext();) { td1 = (TypedDependency)i$.next();
/*  365 */       boolean pobj = true;
/*      */       
/*  367 */       if (td1.reln() != GrammaticalRelation.KILL) {
/*  368 */         TreeGraphNode td1Dep = td1.dep();
/*  369 */         String td1DepPOS = td1Dep.parent().value();
/*  370 */         SortedSet<TypedDependency> possibles = (SortedSet)map.get(td1Dep);
/*  371 */         if (possibles != null)
/*      */         {
/*  373 */           TypedDependency prepDep = null;
/*  374 */           TypedDependency ccDep = null;
/*  375 */           TypedDependency conjDep = null;
/*  376 */           TypedDependency prep2Dep = null;
/*  377 */           TypedDependency prepOtherDep = null;
/*      */           
/*  379 */           Set<TypedDependency> otherDtrs = new TreeSet();
/*      */           
/*      */ 
/*  382 */           for (TypedDependency td2 : possibles) {
/*  383 */             if (td2.reln() == EnglishGrammaticalRelations.CONJUNCT) {
/*  384 */               TreeGraphNode td2Dep = td2.dep();
/*  385 */               String td2DepPOS = td2Dep.parent().value();
/*  386 */               if (((td2DepPOS.equals("IN")) || (td2DepPOS.equals("TO"))) && (td2Dep.value().equals(td1Dep.value())))
/*      */               {
/*      */ 
/*  389 */                 conjDep = td2;
/*  390 */                 Set<TypedDependency> possibles2 = (Set)map.get(td2Dep);
/*  391 */                 if (possibles2 != null) {
/*  392 */                   for (TypedDependency td3 : possibles2) {
/*  393 */                     TreeGraphNode td3Dep = td3.dep();
/*  394 */                     String td3DepPOS = td3Dep.parent().value();
/*      */                     
/*      */ 
/*      */ 
/*      */ 
/*  399 */                     if (((td3.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT) || (td3.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT)) && (prep2Dep == null) && (!td3DepPOS.equals("RB")) && (!td3DepPOS.equals("IN")) && (!td3DepPOS.equals("TO")))
/*      */                     {
/*      */ 
/*  402 */                       prep2Dep = td3;
/*  403 */                       if (td3.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT) pobj = false;
/*      */                     } else {
/*  405 */                       otherDtrs.add(td3);
/*      */                     }
/*      */                   }
/*      */                 }
/*  409 */               } else if ((td2DepPOS.equals("IN")) || (td2DepPOS.equals("TO")))
/*      */               {
/*  411 */                 conjDep = td2;
/*  412 */                 Set<TypedDependency> possibles2 = (Set)map.get(td2Dep);
/*  413 */                 if (possibles2 != null)
/*  414 */                   for (TypedDependency td3 : possibles2) {
/*  415 */                     TreeGraphNode td3Dep = td3.dep();
/*  416 */                     String td3DepPOS = td3Dep.parent().value();
/*  417 */                     if (((td3.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT) || (td3.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT)) && (prepOtherDep == null) && (!td3DepPOS.equals("RB")) && (!td3DepPOS.equals("IN")) && (!td3DepPOS.equals("TO")))
/*      */                     {
/*      */ 
/*  420 */                       prepOtherDep = td3;
/*  421 */                       if (td3.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT) pobj = false;
/*      */                     } else {
/*  423 */                       otherDtrs.add(td3);
/*      */                     }
/*      */                   }
/*      */               }
/*      */             }
/*      */           }
/*      */           int index;
/*  430 */           if (conjDep != null) {
/*  431 */             index = conjDep.dep().index();
/*  432 */             for (TypedDependency td2 : possibles)
/*      */             {
/*      */ 
/*  435 */               if ((td2.reln() == EnglishGrammaticalRelations.COORDINATION) && (td2.dep().index() < index)) {
/*  436 */                 ccDep = td2;
/*      */               } else {
/*  438 */                 TreeGraphNode td2Dep = td2.dep();
/*  439 */                 String td2DepPOS = td2Dep.parent().value();
/*  440 */                 if (((td1.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER) || (td1.reln() == EnglishGrammaticalRelations.RELATIVE)) && ((td2.reln() == GrammaticalRelation.DEPENDENT) || (td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT) || (td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT)) && ((td1DepPOS.equals("IN")) || (td1DepPOS.equals("TO")) || (td1DepPOS.equals("VBG"))) && (prepDep == null) && (!td2DepPOS.equals("RB")) && (!td2DepPOS.equals("IN")) && (!td2DepPOS.equals("TO")) && (td2.dep().index() < index))
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  446 */                   prepDep = td2;
/*  447 */                   if (td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT) {
/*  448 */                     pobj = false;
/*      */                   }
/*  450 */                 } else if (td2 != conjDep) {
/*  451 */                   otherDtrs.add(td2);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  471 */           if ((prepDep != null) && (ccDep != null) && (conjDep != null) && (prep2Dep != null))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*  476 */             boolean agent = false;
/*  477 */             if (td1Dep.value().equals("by"))
/*      */             {
/*  479 */               Set<TypedDependency> aux_pass_poss = (Set)map.get(td1.gov());
/*  480 */               if (aux_pass_poss != null) {
/*  481 */                 for (TypedDependency td_pass : aux_pass_poss) {
/*  482 */                   if (td_pass.reln() == EnglishGrammaticalRelations.AUX_PASSIVE_MODIFIER) {
/*  483 */                     agent = true;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */             TypedDependency tdNew;
/*  490 */             if (agent) {
/*  491 */               TypedDependency tdNew = new TypedDependency(EnglishGrammaticalRelations.AGENT, td1.gov(), prepDep.dep());
/*  492 */               agent = false;
/*      */             } else {
/*      */               GrammaticalRelation reln;
/*      */               GrammaticalRelation reln;
/*  496 */               if (td1.reln() == EnglishGrammaticalRelations.RELATIVE) {
/*  497 */                 reln = EnglishGrammaticalRelations.RELATIVE;
/*      */               } else {
/*      */                 GrammaticalRelation reln;
/*  500 */                 if (pobj) {
/*  501 */                   reln = EnglishGrammaticalRelations.getPrep(td1Dep.value().toLowerCase());
/*      */                 }
/*      */                 else
/*  504 */                   reln = EnglishGrammaticalRelations.getPrepC(td1Dep.value().toLowerCase());
/*      */               }
/*  506 */               tdNew = new TypedDependency(reln, td1.gov(), prepDep.dep());
/*      */             }
/*  508 */             newTypedDeps.add(tdNew);
/*  509 */             TypedDependency tdNew2 = new TypedDependency(conjValue(ccDep.dep().value()), prepDep.dep(), prep2Dep.dep());
/*      */             
/*  511 */             newTypedDeps.add(tdNew2);
/*      */             
/*  513 */             td1.setReln(GrammaticalRelation.KILL);
/*  514 */             prepDep.setReln(GrammaticalRelation.KILL);
/*  515 */             ccDep.setReln(GrammaticalRelation.KILL);
/*  516 */             conjDep.setReln(GrammaticalRelation.KILL);
/*  517 */             prep2Dep.setReln(GrammaticalRelation.KILL);
/*      */             
/*      */ 
/*  520 */             for (TypedDependency otd : otherDtrs)
/*      */             {
/*      */ 
/*  523 */               if (otd.dep().parent().value().equals("IN")) {
/*  524 */                 otd.setReln(EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER);
/*      */               }
/*  526 */               otd.setGov(td1.gov());
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  537 */           if ((prepDep != null) && (ccDep != null) && (conjDep != null) && (prepOtherDep != null))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  544 */             TreeGraphNode copy = new TreeGraphNode(td1.gov(), td1.gov().parent);
/*  545 */             MapLabel label = new MapLabel(td1.gov().label());
/*  546 */             label.put("copy", "true");
/*  547 */             copy.setLabel(label);
/*      */             
/*      */ 
/*      */ 
/*  551 */             boolean agent = false;
/*  552 */             if (td1Dep.value().equals("by"))
/*      */             {
/*  554 */               Set<TypedDependency> aux_pass_poss = (Set)map.get(td1.gov());
/*  555 */               if (aux_pass_poss != null) {
/*  556 */                 for (TypedDependency td_pass : aux_pass_poss) {
/*  557 */                   if (td_pass.reln() == EnglishGrammaticalRelations.AUX_PASSIVE_MODIFIER) {
/*  558 */                     agent = true;
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*      */             TypedDependency tdNew;
/*  565 */             if (agent) {
/*  566 */               TypedDependency tdNew = new TypedDependency(EnglishGrammaticalRelations.AGENT, td1.gov(), prepDep.dep());
/*  567 */               agent = false;
/*      */             } else {
/*      */               GrammaticalRelation reln;
/*      */               GrammaticalRelation reln;
/*  571 */               if (td1.reln() == EnglishGrammaticalRelations.RELATIVE) {
/*  572 */                 reln = EnglishGrammaticalRelations.RELATIVE;
/*      */               } else {
/*      */                 GrammaticalRelation reln;
/*  575 */                 if (pobj) {
/*  576 */                   reln = EnglishGrammaticalRelations.getPrep(td1Dep.value().toLowerCase());
/*      */                 }
/*      */                 else
/*  579 */                   reln = EnglishGrammaticalRelations.getPrepC(td1Dep.value().toLowerCase());
/*      */               }
/*  581 */               tdNew = new TypedDependency(reln, td1.gov(), prepDep.dep());
/*      */             }
/*  583 */             newTypedDeps.add(tdNew);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  588 */             TypedDependency tdNew2 = new TypedDependency(conjValue(ccDep.dep().value()), td1.gov(), copy);
/*      */             
/*  590 */             newTypedDeps.add(tdNew2);
/*      */             
/*      */ 
/*      */             GrammaticalRelation reln;
/*      */             
/*      */             GrammaticalRelation reln;
/*      */             
/*  597 */             if (td1.reln() == EnglishGrammaticalRelations.RELATIVE) {
/*  598 */               reln = EnglishGrammaticalRelations.RELATIVE;
/*      */             } else {
/*      */               GrammaticalRelation reln;
/*  601 */               if (pobj) {
/*  602 */                 reln = EnglishGrammaticalRelations.getPrep(prepOtherDep.gov().value().toLowerCase());
/*      */               }
/*      */               else
/*      */               {
/*  606 */                 reln = EnglishGrammaticalRelations.getPrepC(prepOtherDep.gov().value().toLowerCase());
/*      */               }
/*      */             }
/*      */             
/*  610 */             TypedDependency tdNew3 = new TypedDependency(reln, copy, prepOtherDep.dep());
/*  611 */             newTypedDeps.add(tdNew3);
/*      */             
/*      */ 
/*  614 */             td1.setReln(GrammaticalRelation.KILL);
/*  615 */             prepDep.setReln(GrammaticalRelation.KILL);
/*  616 */             ccDep.setReln(GrammaticalRelation.KILL);
/*  617 */             conjDep.setReln(GrammaticalRelation.KILL);
/*  618 */             prepOtherDep.setReln(GrammaticalRelation.KILL);
/*      */             
/*      */ 
/*  621 */             for (TypedDependency otd : otherDtrs)
/*      */             {
/*      */ 
/*  624 */               if (otd.dep().parent().value().equals("IN")) {
/*  625 */                 otd.setReln(EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER);
/*      */               }
/*  627 */               otd.setGov(td1.gov());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     TypedDependency td1;
/*  634 */     for (Iterator i$ = list.iterator(); i$.hasNext();) { td1 = (TypedDependency)i$.next();
/*  635 */       boolean agent = false;
/*  636 */       boolean pobj = true;
/*      */       
/*  638 */       if (td1.reln() != GrammaticalRelation.KILL) {
/*  639 */         TreeGraphNode td1Dep = td1.dep();
/*  640 */         String td1DepPOS = td1Dep.parent().value();
/*      */         
/*  642 */         Set<TypedDependency> possibles = (Set)map.get(td1Dep);
/*      */         
/*  644 */         if (possibles != null)
/*      */         {
/*      */ 
/*  647 */           for (TypedDependency td2 : possibles) {
/*  648 */             if ((td2.reln() != EnglishGrammaticalRelations.COORDINATION) && (td2.reln() != EnglishGrammaticalRelations.CONJUNCT))
/*      */             {
/*  650 */               TreeGraphNode td2Dep = td2.dep();
/*  651 */               String td2DepPOS = td2Dep.parent().value();
/*      */               
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  657 */               if (((td1.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER) || (td1.reln() == EnglishGrammaticalRelations.RELATIVE)) && ((td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT) || (td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT)) && ((td1DepPOS.equals("IN")) || (td1DepPOS.equals("TO")) || (td1DepPOS.equals("VBG"))) && (!td2DepPOS.equals("RB")) && (!td2DepPOS.equals("IN")) && (!td2DepPOS.equals("TO")) && (!isConjWithNoPrep(td2.gov(), possibles)))
/*      */               {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  664 */                 if (td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT) {
/*  665 */                   pobj = false;
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*  670 */                 if (td1Dep.value().equals("by"))
/*      */                 {
/*  672 */                   Set<TypedDependency> aux_pass_poss = (Set)map.get(td1.gov());
/*  673 */                   if (aux_pass_poss != null) {
/*  674 */                     for (TypedDependency td_pass : aux_pass_poss) {
/*  675 */                       if (td_pass.reln() == EnglishGrammaticalRelations.AUX_PASSIVE_MODIFIER) {
/*  676 */                         agent = true;
/*      */                       }
/*      */                     }
/*      */                   }
/*      */                 }
/*      */                 
/*      */                 TypedDependency td3;
/*  683 */                 if (agent) {
/*  684 */                   TypedDependency td3 = new TypedDependency(EnglishGrammaticalRelations.AGENT, td1.gov(), td2.dep());
/*  685 */                   agent = false;
/*      */                 } else {
/*      */                   GrammaticalRelation reln;
/*      */                   GrammaticalRelation reln;
/*  689 */                   if (td1.reln() == EnglishGrammaticalRelations.RELATIVE) {
/*  690 */                     reln = EnglishGrammaticalRelations.RELATIVE;
/*      */                   } else { GrammaticalRelation reln;
/*  692 */                     if (pobj) {
/*  693 */                       reln = EnglishGrammaticalRelations.getPrep(td1Dep.value().toLowerCase());
/*      */                     } else
/*  695 */                       reln = EnglishGrammaticalRelations.getPrepC(td1Dep.value().toLowerCase());
/*      */                   }
/*  697 */                   td3 = new TypedDependency(reln, td1.gov(), td2.dep());
/*      */                 }
/*      */                 
/*  700 */                 newTypedDeps.add(td3);
/*  701 */                 td1.setReln(GrammaticalRelation.KILL);
/*  702 */                 td2.setReln(GrammaticalRelation.KILL);
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  715 */           if (td1.reln() == GrammaticalRelation.KILL) {
/*  716 */             for (TypedDependency td2 : possibles) {
/*  717 */               if ((td2.reln() != GrammaticalRelation.KILL) && (td2.reln() != EnglishGrammaticalRelations.COORDINATION) && (td2.reln() != EnglishGrammaticalRelations.CONJUNCT))
/*      */               {
/*  719 */                 td2.setGov(td1.gov());
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     TypedDependency td1;
/*      */     
/*      */ 
/*  731 */     for (TypedDependency td : list) {
/*  732 */       boolean keep = true;
/*  733 */       TreeGraphNode dep; if (td.reln() == GrammaticalRelation.KILL) {
/*  734 */         keep = false;
/*  735 */       } else if (td.reln() == EnglishGrammaticalRelations.POSSESSIVE_MODIFIER) {
/*  736 */         keep = false;
/*  737 */         dep = td.dep();
/*      */         
/*  739 */         for (TypedDependency typedD : list) {
/*  740 */           if (typedD.gov().equals(dep)) {
/*  741 */             keep = true;
/*  742 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  746 */       if (keep) {
/*  747 */         newTypedDeps.add(td);
/*      */       }
/*      */     }
/*      */     
/*  751 */     list.clear();
/*  752 */     list.addAll(newTypedDeps);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  757 */   private static String[][] MULTIWORD_PREPS = { { "according", "to" }, { "across", "from" }, { "ahead", "of" }, { "along", "with" }, { "due", "to" }, { "alongside", "of" }, { "apart", "from" }, { "as", "of" }, { "as", "to" }, { "away", "from" }, { "based", "on" }, { "because", "of" }, { "close", "by" }, { "close", "to" }, { "due", "to" }, { "compared", "to" }, { "compared", "with" }, { "depending", "on" }, { "followed", "by" }, { "inside", "of" }, { "instead", "of" }, { "next", "to" }, { "near", "to" }, { "out", "of" }, { "outside", "of" }, { "prior", "to" }, { "together", "with" } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isConjWithNoPrep(TreeGraphNode node, Collection<TypedDependency> list)
/*      */   {
/*  769 */     for (Iterator i$ = list.iterator(); i$.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  774 */         return true)
/*      */     {
/*  769 */       TypedDependency td = (TypedDependency)i$.next();
/*  770 */       if ((td.gov() == node) && (td.reln() == EnglishGrammaticalRelations.CONJUNCT))
/*      */       {
/*      */ 
/*  773 */         String tdDepPOS = td.dep().parent().value();
/*  774 */         if ((tdDepPOS.equals("IN")) || (tdDepPOS.equals("TO"))) {}
/*      */       }
/*      */     }
/*  777 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void collapseMultiwordPreps(Collection<TypedDependency> list)
/*      */   {
/*  786 */     Collection<TypedDependency> newTypedDeps = new ArrayList();
/*      */     String[] mwp;
/*  788 */     Iterator i$; TypedDependency td1; for (mwp : MULTIWORD_PREPS)
/*      */     {
/*  790 */       for (i$ = list.iterator(); i$.hasNext();) { td1 = (TypedDependency)i$.next();
/*  791 */         if (td1.dep().value().equalsIgnoreCase(mwp[0]))
/*      */         {
/*  793 */           for (TypedDependency td2 : list) {
/*  794 */             if (((td2.gov() == td1.dep()) || (td2.gov() == td1.gov())) && (td2.reln().getSpecific() != null) && (td2.reln().getSpecific().equals(mwp[1]))) {
/*  795 */               if (td2.reln().getShortName().startsWith("prep_")) {
/*  796 */                 GrammaticalRelation gr = EnglishGrammaticalRelations.getPrep(mwp[0] + "_" + mwp[1]);
/*  797 */                 newTypedDeps.add(new TypedDependency(gr, td1.gov(), td2.dep()));
/*      */               }
/*  799 */               else if (td2.reln().getShortName().startsWith("prepc_")) {
/*  800 */                 GrammaticalRelation gr = EnglishGrammaticalRelations.getPrepC(mwp[0] + "_" + mwp[1]);
/*  801 */                 newTypedDeps.add(new TypedDependency(gr, td1.gov(), td2.dep()));
/*      */               }
/*  803 */               td1.setReln(GrammaticalRelation.KILL);
/*  804 */               td2.setReln(GrammaticalRelation.KILL);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  812 */     for (TypedDependency td : list) {
/*  813 */       if (td.reln() != GrammaticalRelation.KILL) {
/*  814 */         newTypedDeps.add(td);
/*      */       }
/*      */     }
/*  817 */     list.clear();
/*  818 */     list.addAll(newTypedDeps);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void collapseMWP(Collection<TypedDependency> list)
/*      */   {
/*  837 */     Collection<TypedDependency> newTypedDeps = new ArrayList();
/*      */     
/*  839 */     for (String[] mwp : MULTIWORD_PREPS)
/*      */     {
/*  841 */       TreeGraphNode mwp0 = null;
/*  842 */       TreeGraphNode mwp1 = null;
/*  843 */       TreeGraphNode governor = null;
/*      */       
/*  845 */       TypedDependency prep = null;
/*  846 */       TypedDependency dep = null;
/*  847 */       TypedDependency pobj = null;
/*      */       
/*      */ 
/*      */ 
/*  851 */       for (TypedDependency td : list) {
/*  852 */         if ((td.gov().value().equalsIgnoreCase(mwp[0])) && (td.dep().value().equalsIgnoreCase(mwp[1])) && (Math.abs(td.gov().index() - td.dep().index()) == 1))
/*      */         {
/*  854 */           mwp0 = td.gov();
/*  855 */           mwp1 = td.dep();
/*  856 */           dep = td;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  861 */       for (TypedDependency td1 : list) {
/*  862 */         if ((td1.dep() == mwp0) && (td1.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER)) {
/*  863 */           prep = td1;
/*  864 */           governor = prep.gov();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  869 */       for (TypedDependency td2 : list) {
/*  870 */         if ((td2.gov() == mwp1) && (td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT)) {
/*  871 */           pobj = td2;
/*      */           
/*  873 */           GrammaticalRelation gr = EnglishGrammaticalRelations.getPrep(mwp[0] + "_" + mwp[1]);
/*  874 */           newTypedDeps.add(new TypedDependency(gr, governor, pobj.dep()));
/*      */         }
/*  876 */         if ((td2.gov() == mwp1) && (td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT)) {
/*  877 */           pobj = td2;
/*      */           
/*  879 */           GrammaticalRelation gr = EnglishGrammaticalRelations.getPrepC(mwp[0] + "_" + mwp[1]);
/*  880 */           newTypedDeps.add(new TypedDependency(gr, governor, pobj.dep()));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  885 */       if ((prep != null) && (dep != null) && (pobj != null)) {
/*  886 */         prep.setReln(GrammaticalRelation.KILL);
/*  887 */         dep.setReln(GrammaticalRelation.KILL);
/*  888 */         pobj.setReln(GrammaticalRelation.KILL);
/*      */         
/*      */ 
/*  891 */         for (TypedDependency td1 : list) {
/*  892 */           if (td1.reln() != GrammaticalRelation.KILL) {
/*  893 */             newTypedDeps.add(td1);
/*      */           }
/*      */         }
/*  896 */         list.clear();
/*  897 */         list.addAll(newTypedDeps);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void collapseFlatMWP(Collection<TypedDependency> list)
/*      */   {
/*  916 */     Collection<TypedDependency> newTypedDeps = new ArrayList();
/*      */     
/*      */ 
/*  919 */     for (String[] mwp : MULTIWORD_PREPS)
/*      */     {
/*  921 */       TreeGraphNode mwp1 = null;
/*  922 */       TreeGraphNode governor = null;
/*      */       
/*  924 */       TypedDependency prep = null;
/*  925 */       TypedDependency dep = null;
/*  926 */       TypedDependency pobj = null;
/*      */       
/*      */ 
/*  929 */       for (TypedDependency td : list) {
/*  930 */         if ((td.gov().value().equalsIgnoreCase(mwp[1])) && (td.dep().value().equalsIgnoreCase(mwp[0])) && (Math.abs(td.gov().index() - td.dep().index()) == 1))
/*      */         {
/*  932 */           mwp1 = td.gov();
/*  933 */           dep = td;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  938 */       for (TypedDependency td1 : list) {
/*  939 */         if ((td1.dep() == mwp1) && (td1.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER)) {
/*  940 */           prep = td1;
/*  941 */           governor = prep.gov();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  946 */       for (TypedDependency td2 : list) {
/*  947 */         if ((td2.gov() == mwp1) && (td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT)) {
/*  948 */           pobj = td2;
/*      */           
/*  950 */           GrammaticalRelation gr = EnglishGrammaticalRelations.getPrep(mwp[0] + "_" + mwp[1]);
/*  951 */           newTypedDeps.add(new TypedDependency(gr, governor, pobj.dep()));
/*      */         }
/*  953 */         if ((td2.gov() == mwp1) && (td2.reln() == EnglishGrammaticalRelations.PREPOSITIONAL_COMPLEMENT)) {
/*  954 */           pobj = td2;
/*      */           
/*  956 */           GrammaticalRelation gr = EnglishGrammaticalRelations.getPrepC(mwp[0] + "_" + mwp[1]);
/*  957 */           newTypedDeps.add(new TypedDependency(gr, governor, pobj.dep()));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  962 */       if ((prep != null) && (dep != null) && (pobj != null)) {
/*  963 */         prep.setReln(GrammaticalRelation.KILL);
/*  964 */         dep.setReln(GrammaticalRelation.KILL);
/*  965 */         pobj.setReln(GrammaticalRelation.KILL);
/*      */         
/*      */ 
/*      */ 
/*  969 */         for (TypedDependency td1 : list) {
/*  970 */           if (td1.reln() != GrammaticalRelation.KILL) {
/*  971 */             if (td1.gov() == mwp1) {
/*  972 */               td1.setGov(governor);
/*      */             }
/*  974 */             newTypedDeps.add(td1);
/*      */           }
/*      */         }
/*  977 */         list.clear();
/*  978 */         list.addAll(newTypedDeps);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void eraseMultiConj(Collection<TypedDependency> list)
/*      */   {
/*  994 */     Collection<TypedDependency> newTypedDeps = new ArrayList();
/*      */     
/*      */ 
/*  997 */     for (TypedDependency td1 : list) {
/*  998 */       if (td1.reln() == EnglishGrammaticalRelations.COORDINATION) {
/*  999 */         x = td1.dep();
/*      */         
/* 1001 */         for (TypedDependency td2 : list) {
/* 1002 */           if ((td2.gov().equals(x)) && (td2.reln() == GrammaticalRelation.DEPENDENT)) {
/* 1003 */             td2.setReln(GrammaticalRelation.KILL);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     TreeGraphNode x;
/* 1010 */     for (TypedDependency td : list) {
/* 1011 */       if (td.reln() != GrammaticalRelation.KILL) {
/* 1012 */         newTypedDeps.add(td);
/*      */       }
/*      */     }
/* 1015 */     list.clear();
/* 1016 */     list.addAll(newTypedDeps);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1047 */     Treebank tb = new MemoryTreebank();
/* 1048 */     Properties props = StringUtils.argsToProperties(args);
/* 1049 */     String treeFileName = props.getProperty("treeFile");
/* 1050 */     String sentFileName = props.getProperty("sentFile");
/* 1051 */     String fileName = null;
/*      */     
/* 1053 */     if ((sentFileName == null) && (treeFileName == null)) {
/*      */       try {
/* 1055 */         System.err.println("Usage: java EnglishGrammaticalStructure [options]* [-sentFile file|-treeFile file] [-testGraph]");
/* 1056 */         System.err.println("  options: -basic, -collapsed [the default], -CCprocessed, -parseTree, -test; -parserFile file");
/* 1057 */         TreeReader tr = new PennTreeReader(new StringReader("((S (NP (NNP Sam)) (VP (VBD died) (NP-TMP (NN today)))))"), new LabeledScoredTreeFactory());
/* 1058 */         tb.add(tr.readTree());
/*      */       } catch (Exception e) {
/* 1060 */         System.err.println("Horrible error: " + e);
/* 1061 */         e.printStackTrace();
/*      */       }
/* 1063 */     } else if (treeFileName != null) {
/* 1064 */       fileName = treeFileName;
/* 1065 */       tb.loadPath(treeFileName);
/*      */     } else {
/* 1067 */       fileName = sentFileName;
/* 1068 */       String[] opts = { "-retainNPTmpSubcategories" };
/* 1069 */       String parserFile = props.getProperty("parserFile");
/* 1070 */       if ((parserFile == null) || ("".equals(parserFile))) {
/* 1071 */         parserFile = "/u/nlp/data/lexparser/englishPCFG.ser.gz";
/*      */       }
/* 1073 */       LexicalizedParser lp = new LexicalizedParser(parserFile);
/* 1074 */       lp.setOptionFlags(opts);
/* 1075 */       BufferedReader reader = null;
/*      */       try {
/* 1077 */         reader = new BufferedReader(new FileReader(sentFileName));
/*      */       } catch (FileNotFoundException e) {
/* 1079 */         System.err.println("Cannot find " + sentFileName);
/* 1080 */         System.exit(1);
/*      */       }
/*      */       try {
/* 1083 */         System.err.println("Processing sentence file " + sentFileName);
/* 1084 */         String line; while ((line = reader.readLine()) != null)
/*      */         {
/* 1086 */           PTBTokenizer<Word> ptb = PTBTokenizer.newPTBTokenizer(new StringReader(line));
/* 1087 */           List<Word> words = ptb.tokenize();
/* 1088 */           lp.parse(words);
/* 1089 */           Tree parseTree = lp.getBestParse();
/* 1090 */           tb.add(parseTree);
/*      */         }
/* 1092 */         reader.close();
/*      */       } catch (Exception e) {
/* 1094 */         e.printStackTrace();
/* 1095 */         System.err.println("IOexception reading key file " + sentFileName);
/* 1096 */         System.exit(1);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1102 */     boolean basic = props.getProperty("basic") != null;
/* 1103 */     boolean collapsed = props.getProperty("collapsed") != null;
/* 1104 */     boolean CCprocessed = props.getProperty("CCprocessed") != null;
/* 1105 */     boolean parseTree = props.getProperty("parseTree") != null;
/* 1106 */     boolean test = props.getProperty("test") != null;
/*      */     
/* 1108 */     System.err.println("Printing trees and the typed dependencies for file " + fileName);
/* 1109 */     for (Tree t : tb)
/*      */     {
/* 1111 */       GrammaticalStructure gs = new EnglishGrammaticalStructure(t);
/*      */       
/* 1113 */       if (test)
/*      */       {
/* 1115 */         System.out.println("============= parse tree =======================");
/* 1116 */         t.pennPrint();
/* 1117 */         System.out.println();
/*      */         
/* 1119 */         System.out.println("------------- GrammaticalStructure -------------");
/* 1120 */         System.out.println(gs);
/*      */         
/* 1122 */         System.out.println("------------- basic dependencies ---------------");
/* 1123 */         System.out.println(StringUtils.join(gs.typedDependencies(true), "\n"));
/*      */         
/* 1125 */         System.out.println("------------- collapsed dependencies -----------");
/* 1126 */         System.out.println(StringUtils.join(gs.typedDependenciesCollapsed(true), "\n"));
/*      */         
/* 1128 */         System.out.println("------------- CCprocessed dependencies --------");
/* 1129 */         System.out.println(StringUtils.join(gs.typedDependenciesCCprocessed(true), "\n"));
/*      */         
/* 1131 */         System.out.println("-----------------------------------------------");
/*      */         
/* 1133 */         boolean connected = gs.isConnected(gs.typedDependenciesCollapsed(true));
/* 1134 */         System.out.println("collapsed dependencies form a connected graph: " + connected);
/* 1135 */         if (!connected) {
/* 1136 */           System.out.println("possible offending nodes: " + gs.getRoots(gs.typedDependenciesCollapsed(true)));
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/* 1144 */         if (parseTree) {
/* 1145 */           System.out.println("============= parse tree =======================");
/* 1146 */           t.pennPrint();
/* 1147 */           System.out.println();
/*      */         }
/*      */         
/* 1150 */         if (basic) {
/* 1151 */           if ((collapsed) || (CCprocessed)) System.out.println("------------- basic dependencies ---------------");
/* 1152 */           System.out.println(StringUtils.join(gs.typedDependencies(true), "\n"));
/* 1153 */           System.out.println();
/*      */         }
/* 1155 */         if (collapsed) {
/* 1156 */           if ((basic) || (CCprocessed)) System.out.println("----------- collapsed dependencies -----------");
/* 1157 */           System.out.println(StringUtils.join(gs.typedDependenciesCollapsed(true), "\n"));
/* 1158 */           System.out.println();
/*      */         }
/*      */         
/* 1161 */         if (CCprocessed) {
/* 1162 */           if ((basic) || (collapsed)) System.out.println("---------- CCprocessed dependencies ----------");
/* 1163 */           System.out.println(StringUtils.join(gs.typedDependenciesCCprocessed(true), "\n"));
/* 1164 */           System.out.println();
/*      */         }
/*      */         
/* 1167 */         if (((!basic ? 1 : 0) & (!collapsed ? 1 : 0) & (!CCprocessed ? 1 : 0)) != 0)
/*      */         {
/* 1169 */           System.out.println(StringUtils.join(gs.typedDependenciesCollapsed(true), "\n"));
/* 1170 */           System.out.println();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\EnglishGrammaticalStructure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */