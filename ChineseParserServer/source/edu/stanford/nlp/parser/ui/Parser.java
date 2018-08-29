/*     */ package edu.stanford.nlp.parser.ui;
/*     */ 
/*     */ import edu.stanford.nlp.util.DisabledPreferencesFactory;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.JCheckBoxMenuItem;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.KeyStroke;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Parser
/*     */   extends JFrame
/*     */ {
/*     */   private ParserPanel parserPanel;
/*     */   private JCheckBoxMenuItem untokEngItem;
/*     */   private JCheckBoxMenuItem tokChineseItem;
/*     */   private JCheckBoxMenuItem untokChineseItem;
/*     */   private JMenu jMenu2;
/*     */   private JMenuItem loadParserItem;
/*     */   private JMenuItem loadDataItem;
/*     */   private JSeparator jSeparator1;
/*     */   private JMenu jMenu1;
/*     */   private JMenuItem exitItem;
/*     */   private JMenuBar jMenuBar1;
/*     */   
/*     */   public Parser()
/*     */   {
/*  46 */     this(null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Parser(String parserFilename, String dataFilename)
/*     */   {
/*  57 */     initComponents();
/*     */     
/*  59 */     this.parserPanel = new ParserPanel();
/*  60 */     getContentPane().add("Center", this.parserPanel);
/*  61 */     if (parserFilename != null) {
/*  62 */       this.parserPanel.loadParser(parserFilename);
/*     */     }
/*  64 */     if (dataFilename != null) {
/*  65 */       this.parserPanel.loadFile(dataFilename);
/*     */     }
/*  67 */     pack();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initComponents()
/*     */   {
/*  78 */     this.jMenuBar1 = new JMenuBar();
/*  79 */     this.jMenu1 = new JMenu();
/*  80 */     this.loadDataItem = new JMenuItem();
/*  81 */     this.loadParserItem = new JMenuItem();
/*  82 */     this.jSeparator1 = new JSeparator();
/*  83 */     this.exitItem = new JMenuItem();
/*  84 */     this.jMenu2 = new JMenu();
/*  85 */     this.untokEngItem = new JCheckBoxMenuItem();
/*  86 */     this.tokChineseItem = new JCheckBoxMenuItem();
/*  87 */     this.untokChineseItem = new JCheckBoxMenuItem();
/*     */     
/*  89 */     setTitle("Parser");
/*  90 */     addWindowListener(new WindowAdapter() {
/*     */       public void windowClosing(WindowEvent evt) {
/*  92 */         Parser.this.exitForm(evt);
/*     */       }
/*     */       
/*  95 */     });
/*  96 */     this.jMenu1.setText("File");
/*  97 */     this.loadDataItem.setAccelerator(KeyStroke.getKeyStroke(79, 8));
/*  98 */     this.loadDataItem.setMnemonic('o');
/*  99 */     this.loadDataItem.setText("Load File");
/* 100 */     this.loadDataItem.setToolTipText("Load a data file.");
/* 101 */     this.loadDataItem.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 103 */         Parser.this.loadDataItemActionPerformed(evt);
/*     */       }
/*     */       
/* 106 */     });
/* 107 */     this.jMenu1.add(this.loadDataItem);
/* 108 */     this.loadParserItem.setText("Load Parser");
/* 109 */     this.loadParserItem.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 111 */         Parser.this.loadParserItemActionPerformed(evt);
/*     */       }
/*     */       
/* 114 */     });
/* 115 */     this.jMenu1.add(this.loadParserItem);
/* 116 */     this.jMenu1.add(this.jSeparator1);
/* 117 */     this.exitItem.setAccelerator(KeyStroke.getKeyStroke(88, 8));
/* 118 */     this.exitItem.setMnemonic('x');
/* 119 */     this.exitItem.setText("Exit");
/* 120 */     this.exitItem.setToolTipText("Exits the program.");
/* 121 */     this.exitItem.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 123 */         Parser.this.exitItemActionPerformed(evt);
/*     */       }
/*     */       
/* 126 */     });
/* 127 */     this.jMenu1.add(this.exitItem);
/* 128 */     this.jMenuBar1.add(this.jMenu1);
/* 129 */     this.jMenu2.setText("Language");
/* 130 */     this.untokEngItem.setSelected(true);
/* 131 */     this.untokEngItem.setText("Untokenized English");
/* 132 */     this.untokEngItem.setToolTipText("Parses untokenized English text.");
/* 133 */     this.untokEngItem.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 135 */         Parser.this.untokEngItemActionPerformed(evt);
/*     */       }
/*     */       
/* 138 */     });
/* 139 */     this.jMenu2.add(this.untokEngItem);
/* 140 */     this.tokChineseItem.setText("Tokenized Simplified Chinese (UTF-8)");
/* 141 */     this.tokChineseItem.setToolTipText("Parses pre-tokenized text.");
/* 142 */     this.tokChineseItem.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 144 */         Parser.this.tokChineseItemActionPerformed(evt);
/*     */       }
/*     */       
/* 147 */     });
/* 148 */     this.jMenu2.add(this.tokChineseItem);
/* 149 */     this.untokChineseItem.setText("Untokenized Simplified Chinese (UTF-8)");
/* 150 */     this.untokChineseItem.setToolTipText("Segments and parses untokenized text.");
/* 151 */     this.untokChineseItem.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 153 */         Parser.this.untokChineseItemActionPerformed(evt);
/*     */       }
/*     */       
/* 156 */     });
/* 157 */     this.jMenu2.add(this.untokChineseItem);
/* 158 */     this.jMenuBar1.add(this.jMenu2);
/* 159 */     setJMenuBar(this.jMenuBar1);
/*     */     
/* 161 */     pack();
/*     */   }
/*     */   
/*     */   private void tokChineseItemActionPerformed(ActionEvent evt)
/*     */   {
/* 166 */     this.untokEngItem.setSelected(false);
/* 167 */     this.untokChineseItem.setSelected(false);
/* 168 */     this.tokChineseItem.setSelected(true);
/* 169 */     this.parserPanel.setLanguage(1);
/*     */   }
/*     */   
/*     */   private void untokChineseItemActionPerformed(ActionEvent evt)
/*     */   {
/* 174 */     this.untokEngItem.setSelected(false);
/* 175 */     this.tokChineseItem.setSelected(false);
/* 176 */     this.untokChineseItem.setSelected(true);
/* 177 */     this.parserPanel.setLanguage(2);
/*     */   }
/*     */   
/*     */   private void untokEngItemActionPerformed(ActionEvent evt)
/*     */   {
/* 182 */     this.untokEngItem.setSelected(true);
/* 183 */     this.tokChineseItem.setSelected(false);
/* 184 */     this.untokChineseItem.setSelected(false);
/* 185 */     this.parserPanel.setLanguage(0);
/*     */   }
/*     */   
/*     */   private void exitItemActionPerformed(ActionEvent evt)
/*     */   {
/* 190 */     exitForm(null);
/*     */   }
/*     */   
/*     */   private void loadParserItemActionPerformed(ActionEvent evt)
/*     */   {
/* 195 */     this.parserPanel.loadParser();
/*     */   }
/*     */   
/*     */   private void loadDataItemActionPerformed(ActionEvent evt)
/*     */   {
/* 200 */     this.parserPanel.loadFile();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void exitForm(WindowEvent evt)
/*     */   {
/* 207 */     System.exit(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 214 */     DisabledPreferencesFactory.install();
/* 215 */     String dataFilename = null;
/* 216 */     String parserFilename = null;
/* 217 */     ParserPanel parserPanel = new ParserPanel();
/* 218 */     if (args.length > 0) {
/* 219 */       if (args[0].equals("-h")) {
/* 220 */         System.out.println("Usage: java edu.stanford.nlp.parser.ui.ParserPanel [parserfilename] [textFilename]");
/*     */       } else {
/* 222 */         parserFilename = args[0];
/* 223 */         if (args.length > 1) {
/* 224 */           dataFilename = args[1];
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 229 */     Parser parser = new Parser(parserFilename, dataFilename);
/* 230 */     parser.setVisible(true);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\ui\Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */