/*     */ package edu.stanford.nlp.trees.international.negra;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.LabelFactory;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreeFactory;
/*     */ import edu.stanford.nlp.trees.TreeNormalizer;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.util.Filter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class NegraPennTreeNormalizer
/*     */   extends TreeNormalizer
/*     */ {
/*     */   private static final String root = "ROOT";
/*     */   private static final String nonUnaryRoot = "NUR";
/*     */   protected final TreebankLanguagePack tlp;
/*     */   
/*     */   public String rootSymbol()
/*     */   {
/*  24 */     return "ROOT";
/*     */   }
/*     */   
/*     */   public String nonUnaryRootSymbol() {
/*  28 */     return "NUR";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  34 */   private boolean leaveGF = false;
/*  35 */   private boolean insertNPinPP = false;
/*     */   private static final String junkCPP = "---CJ";
/*     */   
/*  38 */   public void setLeaveGF(boolean b) { this.leaveGF = b; }
/*     */   
/*     */   public boolean getLeaveGF()
/*     */   {
/*  42 */     return this.leaveGF;
/*     */   }
/*     */   
/*     */   public void setInsertNPinPP(boolean b) {
/*  46 */     this.insertNPinPP = b;
/*     */   }
/*     */   
/*     */   public boolean getInsertNPinPP() {
/*  50 */     return this.insertNPinPP;
/*     */   }
/*     */   
/*     */   public NegraPennTreeNormalizer()
/*     */   {
/*  55 */     this(new NegraPennLanguagePack());
/*     */   }
/*     */   
/*     */   public NegraPennTreeNormalizer(TreebankLanguagePack tlp) {
/*  59 */     this.tlp = tlp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String normalizeTerminal(String leaf)
/*     */   {
/*  69 */     return leaf.intern();
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
/*     */   public String normalizeNonterminal(String category)
/*     */   {
/*  82 */     if ("---CJ".equals(category))
/*     */     {
/*  84 */       category = "CPP";
/*     */     }
/*  86 */     return cleanUpLabel(category).intern();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String cpp = "CPP";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tree normalizeWholeTree(Tree tree, TreeFactory tf)
/*     */   {
/* 105 */     if ((tree.label().value().equals("ROOT")) && (tree.children().length > 1))
/*     */     {
/*     */ 
/* 108 */       Tree underRoot = tree.treeFactory().newTreeNode("NUR", tree.getChildrenAsList());
/* 109 */       tree.setChildren(new Tree[1]);
/* 110 */       tree.children()[0] = underRoot;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 116 */     if (this.insertNPinPP) {
/* 117 */       insertNPinPPall(tree);
/*     */     }
/*     */     
/* 120 */     if (!this.leaveGF)
/*     */     {
/* 122 */       for (Tree t : tree)
/*     */       {
/* 124 */         if ((!t.isLeaf()) && 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 129 */           (!t.label().value().matches("--.*")))
/*     */         {
/*     */ 
/*     */ 
/* 133 */           String[] catEdge = t.label().value().split("-");
/*     */           
/* 135 */           String label = catEdge.length > 0 ? catEdge[0] : "-";
/* 136 */           NegraLabel l = new NegraLabel(label);
/*     */           
/* 138 */           if (catEdge.length > 1)
/*     */           {
/* 140 */             l.setEdge(catEdge[1]);
/*     */           }
/* 142 */           t.setLabel(l);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 147 */     Tree fixed = tree.prune(new Filter() {
/*     */       public boolean accept(Object obj) {
/* 149 */         Tree t = (Tree)obj;
/* 150 */         Tree[] kids = t.children();
/* 151 */         Label l = t.label();
/* 152 */         if ((l != null) && (l.value() != null) && (l.value().matches("^\\*T.*$")) && (!t.isLeaf()) && (kids.length == 1) && (kids[0].isLeaf()))
/*     */         {
/* 154 */           return false;
/*     */         }
/* 156 */         return true; } }, tf).spliceOut(new Filter()
/*     */     {
/*     */       public boolean accept(Object obj)
/*     */       {
/* 160 */         Tree t = (Tree)obj;
/* 161 */         if ((t.isLeaf()) || (t.isPreTerminal()) || (t.children().length != 1)) {
/* 162 */           return true;
/*     */         }
/* 164 */         if ((t.label() != null) && (t.label().equals(t.children()[0].label()))) {
/* 165 */           return false;
/*     */         }
/* 167 */         return true; } }, tf);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 172 */     return fixed;
/*     */   }
/*     */   
/*     */ 
/* 176 */   private List prepositionTags = Arrays.asList(new String[] { "APPR", "APPRART" });
/* 177 */   private List postpositionTags = Arrays.asList(new String[] { "APPO", "APZR" });
/*     */   
/*     */   private void insertNPinPPall(Tree t)
/*     */   {
/* 181 */     Tree[] kids = t.children();
/* 182 */     int i = 0; for (int n = kids.length; i < n; i++) {
/* 183 */       insertNPinPPall(kids[i]);
/*     */     }
/* 185 */     insertNPinPP(t);
/*     */   }
/*     */   
/*     */   private void insertNPinPP(Tree t)
/*     */   {
/* 190 */     if (this.tlp.basicCategory(t.label().value()).equals("PP")) {
/* 191 */       Tree[] kids = t.children();
/* 192 */       int i = 0;
/* 193 */       int j = kids.length - 1;
/* 194 */       while ((i < j) && (this.prepositionTags.contains(this.tlp.basicCategory(kids[i].label().value())))) {
/* 195 */         i++;
/*     */       }
/* 197 */       while ((i < j) && (this.postpositionTags.contains(this.tlp.basicCategory(kids[j].label().value())))) {
/* 198 */         j--;
/*     */       }
/*     */       
/* 201 */       if (i > j) {
/* 202 */         System.err.println("##### Warning -- no NP material here!");
/* 203 */         return;
/*     */       }
/*     */       
/* 206 */       int npKidsLength = j - i + 1;
/* 207 */       Tree[] npKids = new Tree[npKidsLength];
/* 208 */       System.arraycopy(kids, i, npKids, 0, npKidsLength);
/* 209 */       Tree np = t.treeFactory().newTreeNode(t.label().labelFactory().newLabel("NP"), Arrays.asList(npKids));
/* 210 */       Tree[] newPPkids = new Tree[kids.length - npKidsLength + 1];
/* 211 */       System.arraycopy(kids, 0, newPPkids, 0, i + 1);
/* 212 */       newPPkids[i] = np;
/* 213 */       System.arraycopy(kids, j + 1, newPPkids, i + 1, kids.length - j - 1);
/* 214 */       t.setChildren(newPPkids);
/* 215 */       System.out.println("#### inserted NP in PP");
/* 216 */       t.pennPrint();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String cleanUpLabel(String label)
/*     */   {
/* 226 */     if (label == null) {
/* 227 */       label = "ROOT";
/*     */     }
/*     */     
/* 230 */     return label;
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\negra\NegraPennTreeNormalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */