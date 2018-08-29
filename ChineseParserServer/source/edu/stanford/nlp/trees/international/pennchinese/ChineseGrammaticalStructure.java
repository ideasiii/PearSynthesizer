/*     */ package edu.stanford.nlp.trees.international.pennchinese;
/*     */ 
/*     */ import edu.stanford.nlp.parser.lexparser.ChineseTreebankParserParams;
/*     */ import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
/*     */ import edu.stanford.nlp.trees.GrammaticalRelation;
/*     */ import edu.stanford.nlp.trees.GrammaticalStructure;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeGraphNode;
/*     */ import edu.stanford.nlp.trees.Treebank;
/*     */ import edu.stanford.nlp.trees.TypedDependency;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ChineseGrammaticalStructure extends GrammaticalStructure
/*     */ {
/*  22 */   private static edu.stanford.nlp.trees.HeadFinder shf = new ChineseSemanticHeadFinder();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChineseGrammaticalStructure(Tree t)
/*     */   {
/*  37 */     this(t, new ChineseTreebankLanguagePack().punctuationWordRejectFilter());
/*     */   }
/*     */   
/*     */   public ChineseGrammaticalStructure(Tree t, edu.stanford.nlp.util.Filter<String> puncFilter) {
/*  41 */     super(t, ChineseGrammaticalRelations.values(), shf, puncFilter);
/*     */   }
/*     */   
/*     */   protected void collapseDependencies(Collection<TypedDependency> list)
/*     */   {
/*  46 */     collapsePrepAndPoss(list);
/*     */   }
/*     */   
/*     */   private void collapsePrepAndPoss(Collection<TypedDependency> list)
/*     */   {
/*  51 */     Collection<TypedDependency> newTypedDeps = new java.util.ArrayList();
/*     */     
/*     */ 
/*     */ 
/*  55 */     Map<TreeGraphNode, Set<TypedDependency>> map = new java.util.HashMap();
/*  56 */     for (TypedDependency typedDep : list) {
/*  57 */       if (!map.containsKey(typedDep.gov())) {
/*  58 */         map.put(typedDep.gov(), new java.util.HashSet());
/*     */       }
/*  60 */       ((Set)map.get(typedDep.gov())).add(typedDep);
/*     */     }
/*     */     
/*     */ 
/*  64 */     for (Iterator i$ = list.iterator(); i$.hasNext();) { td1 = (TypedDependency)i$.next();
/*  65 */       if (td1.reln() != GrammaticalRelation.KILL) {
/*  66 */         TreeGraphNode td1Dep = td1.dep();
/*  67 */         String td1DepPOS = td1Dep.parent().value();
/*     */         
/*  69 */         Set<TypedDependency> possibles = (Set)map.get(td1Dep);
/*  70 */         if (possibles != null)
/*     */         {
/*  72 */           for (TypedDependency td2 : possibles) {
/*  73 */             TreeGraphNode td2Dep = td2.dep();
/*  74 */             String td2DepPOS = td2Dep.parent().value();
/*  75 */             if ((td2 != null) && (td1.reln() == GrammaticalRelation.DEPENDENT) && (td2.reln() == GrammaticalRelation.DEPENDENT) && (td1DepPOS.equals("P"))) {
/*  76 */               TypedDependency td3 = new TypedDependency(td1Dep.value(), td1.gov(), td2.dep());
/*     */               
/*  78 */               newTypedDeps.add(td3);
/*  79 */               td1.setReln(GrammaticalRelation.KILL);
/*  80 */               td2.setReln(GrammaticalRelation.KILL);
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */           if (td1.reln().equals(GrammaticalRelation.KILL)) {
/*  93 */             for (TypedDependency td2 : possibles) {
/*  94 */               if (!td2.reln().equals(GrammaticalRelation.KILL))
/*     */               {
/*  96 */                 td2.setGov(td1.gov());
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     TypedDependency td1;
/* 105 */     for (TypedDependency td : list) {
/* 106 */       if (!td.reln().equals(GrammaticalRelation.KILL)) {
/* 107 */         newTypedDeps.add(td);
/*     */       }
/*     */     }
/*     */     
/* 111 */     list.clear();
/* 112 */     list.addAll(newTypedDeps);
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
/*     */   public static void main(String[] args)
/*     */   {
/* 129 */     Treebank tb = new edu.stanford.nlp.trees.MemoryTreebank();
/* 130 */     Properties props = StringUtils.argsToProperties(args);
/* 131 */     String treeFileName = props.getProperty("treeFile");
/* 132 */     String sentFileName = props.getProperty("sentFile");
/* 133 */     String hf = props.getProperty("hf");
/*     */     try
/*     */     {
/* 136 */       if (hf != null) {
/* 137 */         shf = (edu.stanford.nlp.trees.HeadFinder)Class.forName(hf).newInstance();
/* 138 */         System.err.println("Using " + hf);
/*     */       }
/*     */     } catch (Exception e) {
/* 141 */       throw new RuntimeException("Fail to use HeadFinder: " + hf);
/*     */     }
/*     */     
/* 144 */     ChineseTreebankParserParams ctpp = new ChineseTreebankParserParams();
/*     */     
/* 146 */     if (args.length == 0) {
/* 147 */       System.err.println("Please provide treeFile or sentFile");
/*     */     }
/* 149 */     else if (treeFileName != null) {
/*     */       try {
/* 151 */         edu.stanford.nlp.trees.TreeReaderFactory trf = ctpp.treeReaderFactory();
/* 152 */         edu.stanford.nlp.trees.TreeReader tr = trf.newTreeReader(new java.io.InputStreamReader(new java.io.FileInputStream(treeFileName), "GB18030"));
/*     */         Tree t;
/* 154 */         while ((t = tr.readTree()) != null) {
/* 155 */           tb.add(t);
/*     */         }
/*     */       } catch (java.io.IOException e) {
/* 158 */         throw new RuntimeException("File problem: " + e);
/*     */       }
/* 160 */     } else if (sentFileName != null) {
/* 161 */       LexicalizedParser lp = new LexicalizedParser("/u/nlp/data/lexparser/chineseFactored.ser.gz");
/* 162 */       BufferedReader reader = null;
/*     */       try {
/* 164 */         reader = new BufferedReader(new java.io.FileReader(sentFileName));
/*     */       } catch (java.io.FileNotFoundException e) {
/* 166 */         System.err.println("Cannot find " + sentFileName);
/* 167 */         System.exit(1);
/*     */       }
/*     */       try {
/* 170 */         System.out.println("Processing sentence file " + sentFileName);
/*     */         String line;
/* 172 */         while ((line = reader.readLine()) != null) {
/* 173 */           CHTBTokenizer chtb = new CHTBTokenizer(new java.io.StringReader(line));
/* 174 */           java.util.List words = chtb.tokenize();
/* 175 */           lp.parse(words);
/* 176 */           Tree parseTree = lp.getBestParse();
/* 177 */           tb.add(parseTree);
/*     */         }
/* 179 */         reader.close();
/*     */       } catch (Exception e) {
/* 181 */         e.printStackTrace();
/* 182 */         System.err.println("IOexception reading key file " + sentFileName);
/* 183 */         System.exit(1);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 188 */     System.out.println("Phrase structure tree, then dependencies, then collapsed dependencies");
/* 189 */     for (Tree t : tb)
/*     */     {
/* 191 */       System.out.println("==================================================");
/* 192 */       GrammaticalStructure gs = new ChineseGrammaticalStructure(t);
/*     */       
/* 194 */       t.pennPrint();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 200 */       System.out.println("----------------------------");
/* 201 */       System.out.println(gs);
/*     */       
/* 203 */       System.out.println("----------------------------");
/* 204 */       System.out.println(StringUtils.join(gs.typedDependencies(true), "\n"));
/*     */       
/* 206 */       System.out.println("----------------------------");
/* 207 */       System.out.println(StringUtils.join(gs.typedDependenciesCollapsed(true), "\n"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\ChineseGrammaticalStructure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */