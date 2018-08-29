/*     */ package edu.stanford.nlp.parser.ui;
/*     */ 
/*     */ import edu.stanford.nlp.ling.Label;
/*     */ import edu.stanford.nlp.ling.StringLabelFactory;
/*     */ import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
/*     */ import edu.stanford.nlp.trees.PennTreeReader;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.geom.Line2D.Double;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Double;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ public class TreeJPanel extends JPanel
/*     */ {
/*  26 */   protected int VERTICAL_ALIGN = 0;
/*  27 */   protected int HORIZONTAL_ALIGN = 0;
/*     */   
/*  29 */   int maxFontSize = 128;
/*  30 */   int minFontSize = 2;
/*     */   
/*  32 */   int preferredX = 400;
/*  33 */   int preferredY = 300;
/*     */   
/*  35 */   double sisterSkip = 2.5D;
/*  36 */   double parentSkip = 0.8D;
/*  37 */   double belowLineSkip = 0.2D;
/*  38 */   double aboveLineSkip = 0.2D;
/*     */   protected Tree tree;
/*     */   
/*     */   public Tree getTree()
/*     */   {
/*  43 */     return this.tree;
/*     */   }
/*     */   
/*     */   public void setTree(Tree tree) {
/*  47 */     this.tree = tree;
/*  48 */     repaint();
/*     */   }
/*     */   
/*     */   protected String nodeToString(Tree t) {
/*  52 */     if (t == null) {
/*  53 */       return " ";
/*     */     }
/*  55 */     Label l = t.label();
/*  56 */     if (l == null) {
/*  57 */       return " ";
/*     */     }
/*  59 */     String str = l.value();
/*  60 */     if (str == null) {
/*  61 */       return " ";
/*     */     }
/*  63 */     return str;
/*     */   }
/*     */   
/*     */   public static class WidthResult {
/*  67 */     public double width = 0.0D;
/*  68 */     public double nodeTab = 0.0D;
/*  69 */     public double nodeCenter = 0.0D;
/*  70 */     public double childTab = 0.0D;
/*     */   }
/*     */   
/*     */   protected double width(Tree tree, FontMetrics fM) {
/*  74 */     return widthResult(tree, fM).width;
/*     */   }
/*     */   
/*  77 */   WidthResult wr = new WidthResult();
/*     */   
/*     */   protected WidthResult widthResult(Tree tree, FontMetrics fM) {
/*  80 */     if (tree == null) {
/*  81 */       this.wr.width = 0.0D;
/*  82 */       this.wr.nodeTab = 0.0D;
/*  83 */       this.wr.nodeCenter = 0.0D;
/*  84 */       this.wr.childTab = 0.0D;
/*  85 */       return this.wr;
/*     */     }
/*  87 */     double local = fM.stringWidth(nodeToString(tree));
/*  88 */     if (tree.isLeaf()) {
/*  89 */       this.wr.width = local;
/*  90 */       this.wr.nodeTab = 0.0D;
/*  91 */       this.wr.nodeCenter = (local / 2.0D);
/*  92 */       this.wr.childTab = 0.0D;
/*  93 */       return this.wr;
/*     */     }
/*  95 */     double sub = 0.0D;
/*  96 */     double nodeCenter = 0.0D;
/*     */     
/*  98 */     for (int i = 0; i < tree.children().length; i++) {
/*  99 */       WidthResult subWR = widthResult(tree.children()[i], fM);
/* 100 */       if (i == 0) {
/* 101 */         nodeCenter += (sub + subWR.nodeCenter) / 2.0D;
/*     */       }
/* 103 */       if (i == tree.children().length - 1) {
/* 104 */         nodeCenter += (sub + subWR.nodeCenter) / 2.0D;
/*     */       }
/* 106 */       sub += subWR.width;
/* 107 */       if (i < tree.children().length - 1) {
/* 108 */         sub += this.sisterSkip * fM.stringWidth(" ");
/*     */       }
/*     */     }
/* 111 */     double localLeft = local / 2.0D;
/* 112 */     double subLeft = nodeCenter;
/* 113 */     double totalLeft = Math.max(localLeft, subLeft);
/* 114 */     double localRight = local / 2.0D;
/* 115 */     double subRight = sub - nodeCenter;
/* 116 */     double totalRight = Math.max(localRight, subRight);
/* 117 */     this.wr.width = (totalLeft + totalRight);
/* 118 */     this.wr.childTab = (totalLeft - subLeft);
/* 119 */     this.wr.nodeTab = (totalLeft - localLeft);
/* 120 */     this.wr.nodeCenter = (nodeCenter + this.wr.childTab);
/* 121 */     return this.wr;
/*     */   }
/*     */   
/*     */   protected double height(Tree tree, FontMetrics fM) {
/* 125 */     if (tree == null) {
/* 126 */       return 0.0D;
/*     */     }
/* 128 */     double depth = tree.depth();
/* 129 */     return fM.getHeight() * (1.0D + depth * (1.0D + this.parentSkip + this.aboveLineSkip + this.belowLineSkip));
/*     */   }
/*     */   
/*     */   protected void superPaint(Graphics g) {
/* 133 */     super.paintComponent(g);
/*     */   }
/*     */   
/*     */   protected FontMetrics pickFont(Graphics2D g2, Tree tree, Dimension space) {
/* 137 */     Font font = g2.getFont();
/* 138 */     String name = font.getName();
/* 139 */     int style = font.getStyle();
/*     */     
/* 141 */     for (int size = this.maxFontSize; size > this.minFontSize; size--) {
/* 142 */       font = new Font(name, style, size);
/* 143 */       g2.setFont(font);
/* 144 */       FontMetrics fontMetrics = g2.getFontMetrics();
/* 145 */       if (height(tree, fontMetrics) <= space.getHeight())
/*     */       {
/*     */ 
/* 148 */         if (width(tree, fontMetrics) <= space.getWidth())
/*     */         {
/*     */ 
/*     */ 
/* 152 */           return fontMetrics; } }
/*     */     }
/* 154 */     font = new Font(name, style, this.minFontSize);
/* 155 */     g2.setFont(font);
/* 156 */     return g2.getFontMetrics();
/*     */   }
/*     */   
/*     */   protected double paintTree(Tree t, Point2D start, Graphics2D g2, FontMetrics fM) {
/* 160 */     if (t == null) {
/* 161 */       return 0.0D;
/*     */     }
/* 163 */     String nodeStr = nodeToString(t);
/* 164 */     double nodeWidth = fM.stringWidth(nodeStr);
/* 165 */     double nodeHeight = fM.getHeight();
/* 166 */     double nodeAscent = fM.getAscent();
/* 167 */     WidthResult wr = widthResult(t, fM);
/* 168 */     double treeWidth = wr.width;
/* 169 */     double nodeTab = wr.nodeTab;
/* 170 */     double childTab = wr.childTab;
/* 171 */     double nodeCenter = wr.nodeCenter;
/*     */     
/*     */ 
/* 174 */     g2.drawString(nodeStr, (float)(nodeTab + start.getX()), (float)(start.getY() + nodeAscent));
/* 175 */     if (t.isLeaf()) {
/* 176 */       return nodeWidth;
/*     */     }
/* 178 */     double layerMultiplier = 1.0D + this.belowLineSkip + this.aboveLineSkip + this.parentSkip;
/* 179 */     double layerHeight = nodeHeight * layerMultiplier;
/* 180 */     double childStartX = start.getX() + childTab;
/* 181 */     double childStartY = start.getY() + layerHeight;
/* 182 */     double lineStartX = start.getX() + nodeCenter;
/* 183 */     double lineStartY = start.getY() + nodeHeight * (1.0D + this.belowLineSkip);
/* 184 */     double lineEndY = lineStartY + nodeHeight * this.parentSkip;
/*     */     
/* 186 */     for (int i = 0; i < t.children().length; i++) {
/* 187 */       Tree child = t.children()[i];
/* 188 */       double cWidth = paintTree(child, new Point2D.Double(childStartX, childStartY), g2, fM);
/*     */       
/* 190 */       wr = widthResult(child, fM);
/* 191 */       double lineEndX = childStartX + wr.nodeCenter;
/* 192 */       g2.draw(new Line2D.Double(lineStartX, lineStartY, lineEndX, lineEndY));
/* 193 */       childStartX += cWidth;
/* 194 */       if (i < t.children().length - 1) {
/* 195 */         childStartX += this.sisterSkip * fM.stringWidth(" ");
/*     */       }
/*     */     }
/* 198 */     return treeWidth;
/*     */   }
/*     */   
/*     */   public void paintComponent(Graphics g) {
/* 202 */     super.paintComponent(g);
/* 203 */     Graphics2D g2 = (Graphics2D)g;
/* 204 */     Dimension space = getSize();
/* 205 */     FontMetrics fM = pickFont(g2, this.tree, space);
/* 206 */     double width = width(this.tree, fM);
/* 207 */     double height = height(this.tree, fM);
/* 208 */     double startX = 0.0D;
/* 209 */     double startY = 0.0D;
/* 210 */     if (this.HORIZONTAL_ALIGN == 0) {
/* 211 */       startX = (space.getWidth() - width) / 2.0D;
/*     */     }
/* 213 */     if (this.HORIZONTAL_ALIGN == 4) {
/* 214 */       startX = space.getWidth() - width;
/*     */     }
/* 216 */     if (this.VERTICAL_ALIGN == 0) {
/* 217 */       startY = (space.getHeight() - height) / 2.0D;
/*     */     }
/* 219 */     if (this.VERTICAL_ALIGN == 3) {
/* 220 */       startY = space.getHeight() - height;
/*     */     }
/* 222 */     paintTree(this.tree, new Point2D.Double(startX, startY), g2, fM);
/*     */   }
/*     */   
/*     */   public TreeJPanel() {
/* 226 */     this(0, 0);
/*     */   }
/*     */   
/*     */   public TreeJPanel(int hAlign, int vAlign) {
/* 230 */     this.HORIZONTAL_ALIGN = hAlign;
/* 231 */     this.VERTICAL_ALIGN = vAlign;
/* 232 */     setPreferredSize(new Dimension(400, 300));
/*     */   }
/*     */   
/*     */   public void setMinFontSize(int size) {
/* 236 */     this.minFontSize = size;
/*     */   }
/*     */   
/*     */   public void setMaxFontSize(int size) {
/* 240 */     this.maxFontSize = size;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/* 244 */     TreeJPanel tjp = new TreeJPanel();
/* 245 */     String ptbTreeString = "(ROOT (S (NP (DT This)) (VP (VBZ is) (NP (DT a) (NN test))) (. .)))";
/* 246 */     if (args.length > 0) {
/* 247 */       ptbTreeString = args[0];
/*     */     }
/* 249 */     Tree tree = new PennTreeReader(new StringReader(ptbTreeString), new LabeledScoredTreeFactory(new StringLabelFactory())).readTree();
/* 250 */     tjp.setTree(tree);
/* 251 */     JFrame frame = new JFrame();
/* 252 */     frame.getContentPane().add(tjp, "Center");
/* 253 */     frame.addWindowListener(new WindowAdapter() {
/*     */       public void windowClosing(WindowEvent e) {
/* 255 */         System.exit(0);
/*     */       }
/* 257 */     });
/* 258 */     frame.pack();
/* 259 */     frame.setVisible(true);
/* 260 */     frame.setVisible(true);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\ui\TreeJPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */