/*     */ package edu.stanford.nlp.parser.ui;
/*     */ 
/*     */ import edu.stanford.nlp.io.ui.OpenPageDialog;
/*     */ import edu.stanford.nlp.ling.BasicDocument;
/*     */ import edu.stanford.nlp.ling.Document;
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import edu.stanford.nlp.objectbank.TokenizerFactory;
/*     */ import edu.stanford.nlp.parser.lexparser.ChineseLexiconAndWordSegmenter;
/*     */ import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
/*     */ import edu.stanford.nlp.process.Processor;
/*     */ import edu.stanford.nlp.process.StripTagsProcessor;
/*     */ import edu.stanford.nlp.process.Tokenizer;
/*     */ import edu.stanford.nlp.process.WordSegmentingTokenizer;
/*     */ import edu.stanford.nlp.swing.FontDetector;
/*     */ import edu.stanford.nlp.trees.PennTreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.Tree;
/*     */ import edu.stanford.nlp.trees.TreebankLanguagePack;
/*     */ import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Point;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseMotionAdapter;
/*     */ import java.io.CharArrayReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.border.BevelBorder;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ import javax.swing.text.StyleConstants.ColorConstants;
/*     */ import javax.swing.text.StyledDocument;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParserPanel
/*     */   extends JPanel
/*     */ {
/*     */   public static final int UNTOKENIZED_ENGLISH = 0;
/*     */   public static final int TOKENIZED_CHINESE = 1;
/*     */   public static final int UNTOKENIZED_CHINESE = 2;
/*     */   private static TreebankLanguagePack tlp;
/*  76 */   private String encoding = "UTF-8";
/*  77 */   private boolean segmentWords = false;
/*     */   
/*     */   private static final int ONE_SECOND = 1000;
/*     */   
/*     */   private static final int PARSER_LOAD_TIME = 60;
/*     */   
/*     */   private static final int PARSE_TIME = 30;
/*     */   
/*     */   private static final int SEEK_FORWARD = 1;
/*     */   
/*     */   private static final int SEEK_BACK = -1;
/*     */   
/*     */   private final JFileChooser jfc;
/*     */   
/*     */   private OpenPageDialog pageDialog;
/*     */   
/*     */   private SimpleAttributeSet normalStyle;
/*     */   
/*     */   private SimpleAttributeSet highlightStyle;
/*     */   
/*     */   private int startIndex;
/*     */   
/*     */   private int endIndex;
/*     */   
/*     */   private TreeJPanel treePanel;
/*     */   
/*     */   private LexicalizedParser parser;
/*     */   
/*     */   private LoadParserThread lpThread;
/*     */   
/*     */   private ParseThread parseThread;
/*     */   
/*     */   private Timer timer;
/*     */   
/*     */   private int count;
/*     */   private Component glassPane;
/*     */   private boolean scrollWhenDone;
/*     */   private JLabel dataFileLabel;
/*     */   
/*     */   public ParserPanel()
/*     */   {
/* 118 */     initComponents();
/*     */     
/*     */ 
/* 121 */     this.jfc = new JFileChooser();
/* 122 */     this.pageDialog = new OpenPageDialog(new Frame(), true);
/* 123 */     this.pageDialog.setFileChooser(this.jfc);
/*     */     
/* 125 */     setLanguage(0);
/*     */     
/*     */ 
/* 128 */     this.timer = new Timer(1000, new TimerListener(null));
/*     */     
/*     */ 
/* 131 */     this.highlightStyle = new SimpleAttributeSet();
/* 132 */     this.normalStyle = new SimpleAttributeSet();
/* 133 */     StyleConstants.ColorConstants.setBackground(this.highlightStyle, Color.yellow);
/* 134 */     StyleConstants.ColorConstants.setBackground(this.normalStyle, this.textPane.getBackground());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void scrollBack()
/*     */   {
/* 141 */     highlightSentence(this.startIndex - 1);
/*     */     
/* 143 */     this.textPane.setCaretPosition(this.startIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void scrollForward()
/*     */   {
/* 150 */     highlightSentence(this.endIndex + 1);
/*     */     
/* 152 */     this.textPane.setCaretPosition(this.startIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void highlightText(int start, int end, SimpleAttributeSet style)
/*     */   {
/* 159 */     if (start < end) {
/* 160 */       this.textPane.getStyledDocument().setCharacterAttributes(start, end - start + 1, style, false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void highlightSentence(int start)
/*     */   {
/* 169 */     highlightSentence(start, -1);
/*     */   }
/*     */   
/*     */   private JPanel treeContainer;
/*     */   private JPanel topPanel;
/*     */   private JScrollPane textScrollPane;
/*     */   private JButton backButton;
/*     */   private JLabel statusLabel;
/*     */   
/*     */   private void highlightSentence(int start, int end)
/*     */   {
/* 180 */     highlightText(0, this.textPane.getText().length(), this.normalStyle);
/*     */     
/*     */ 
/* 183 */     this.startIndex = (start < 1 ? 0 : nearestDelimiter(this.textPane.getText(), start - 1, -1) + 1);
/*     */     
/*     */ 
/*     */ 
/* 187 */     this.endIndex = nearestDelimiter(this.textPane.getText(), end < this.startIndex ? this.startIndex : end, 1);
/* 188 */     if (this.endIndex == -1) {
/* 189 */       this.endIndex = (this.textPane.getText().length() - 1);
/*     */     }
/*     */     
/* 192 */     highlightText(this.startIndex, this.endIndex, this.highlightStyle);
/*     */     
/*     */ 
/* 195 */     this.backButton.setEnabled(this.startIndex != 0);
/* 196 */     this.forwardButton.setEnabled(this.endIndex != this.textPane.getText().length() - 1);
/* 197 */     this.parseNextButton.setEnabled((this.forwardButton.isEnabled()) && (this.parser != null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int nearestDelimiter(String text, int start, int seekDir)
/*     */   {
/* 206 */     int curIndex = start;
/* 207 */     int textLeng = text.length();
/* 208 */     String[] puncWords = tlp.sentenceFinalPunctuationWords();
/* 209 */     while ((curIndex >= 0) && (curIndex < textLeng)) {
/* 210 */       for (int i = 0; i < puncWords.length; i++) {
/* 211 */         if (puncWords[i].equals(Character.toString(text.charAt(curIndex)))) {
/* 212 */           return curIndex;
/*     */         }
/*     */       }
/* 215 */       curIndex += seekDir;
/*     */     }
/* 217 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void highlightSelectedSentence()
/*     */   {
/* 225 */     highlightSentence(this.textPane.getSelectionStart(), this.textPane.getSelectionEnd());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void highlightEditedSentence()
/*     */   {
/* 232 */     highlightSentence(this.textPane.getCaretPosition());
/*     */   }
/*     */   
/*     */   private JButton loadFileButton;
/*     */   private JPanel loadButtonPanel;
/*     */   private JPanel buttonsAndFilePanel;
/*     */   private JButton parseButton;
/*     */   private JButton parseNextButton;
/* 240 */   public void setStatus(String status) { this.statusLabel.setText(status); }
/*     */   
/*     */ 
/*     */   private JButton forwardButton;
/*     */   private JLabel parserFileLabel;
/*     */   private JButton clearButton;
/*     */   private JSplitPane splitPane;
/*     */   private JPanel statusPanel;
/*     */   
/*     */   public void setLanguage(int language)
/*     */   {
/* 251 */     switch (language) {
/*     */     case 0: 
/* 253 */       tlp = new PennTreebankLanguagePack();
/* 254 */       this.encoding = tlp.getEncoding();
/* 255 */       this.textPane.setFont(new Font("Sans Serif", 0, 14));
/* 256 */       this.treePanel.setFont(new Font("Sans Serif", 0, 14));
/* 257 */       break;
/*     */     case 2: 
/* 259 */       this.segmentWords = true;
/* 260 */       tlp = new ChineseTreebankLanguagePack();
/* 261 */       this.encoding = "UTF-8";
/* 262 */       setChineseFont();
/* 263 */       break;
/*     */     case 1: 
/* 265 */       this.segmentWords = false;
/* 266 */       tlp = new ChineseTreebankLanguagePack();
/* 267 */       this.encoding = "UTF-8";
/* 268 */       setChineseFont();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setChineseFont()
/*     */   {
/* 274 */     List fonts = FontDetector.supportedFonts(0);
/* 275 */     if (fonts.size() > 0) {
/* 276 */       Font font = new Font(((Font)fonts.get(0)).getName(), 0, 14);
/* 277 */       this.textPane.setFont(font);
/* 278 */       this.treePanel.setFont(font);
/* 279 */       System.err.println("Selected font " + font);
/* 280 */     } else if (FontDetector.hasFont("Watanabe Mincho")) {
/* 281 */       this.textPane.setFont(new Font("Watanabe Mincho", 0, 14));
/* 282 */       this.treePanel.setFont(new Font("Watanabe Mincho", 0, 14));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void parse()
/*     */   {
/* 293 */     if (this.textPane.getText().length() == 0) {
/* 294 */       return;
/*     */     }
/*     */     
/*     */ 
/* 298 */     String text = this.textPane.getText().substring(this.startIndex, this.endIndex + 1).trim();
/*     */     
/* 300 */     if ((this.parser != null) && (text.length() > 0)) {
/* 301 */       if (this.segmentWords) {
/* 302 */         ChineseLexiconAndWordSegmenter lex = (ChineseLexiconAndWordSegmenter)this.parser.getLexicon();
/* 303 */         ChineseTreebankLanguagePack.setTokenizerFactory(WordSegmentingTokenizer.factory(lex));
/*     */       }
/* 305 */       Tokenizer<? extends HasWord> toke = tlp.getTokenizerFactory().getTokenizer(new CharArrayReader(text.toCharArray()));
/* 306 */       List<? extends HasWord> wordList = toke.tokenize();
/* 307 */       this.parseThread = new ParseThread(wordList);
/* 308 */       this.parseThread.start();
/* 309 */       startProgressMonitor("Parsing", 30);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadFile()
/*     */   {
/* 318 */     this.pageDialog.setLocation(getLocationOnScreen().x + (getWidth() - this.pageDialog.getWidth()) / 2, getLocationOnScreen().y + (getHeight() - this.pageDialog.getHeight()) / 2);
/* 319 */     this.pageDialog.setVisible(true);
/*     */     
/* 321 */     if (this.pageDialog.getStatus() == 1) {
/* 322 */       loadFile(this.pageDialog.getPage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadFile(String filename)
/*     */   {
/* 332 */     if (filename == null) {
/* 333 */       return;
/*     */     }
/*     */     
/* 336 */     File file = new File(filename);
/*     */     
/* 338 */     String urlOrFile = filename;
/*     */     
/* 340 */     if ((!file.exists()) && (filename.indexOf("://") == -1)) {
/* 341 */       urlOrFile = "http://" + filename;
/*     */ 
/*     */     }
/* 344 */     else if (filename.indexOf("://") == -1) {
/* 345 */       urlOrFile = "file://" + filename;
/*     */     }
/*     */     Document doc;
/*     */     try
/*     */     {
/*     */       Document doc;
/* 351 */       if ((urlOrFile.startsWith("http://")) || (urlOrFile.endsWith(".htm")) || (urlOrFile.endsWith(".html")))
/*     */       {
/* 353 */         Document docPre = new BasicDocument().init(new URL(urlOrFile));
/* 354 */         Processor noTags = new StripTagsProcessor();
/* 355 */         doc = noTags.processDocument(docPre);
/*     */       } else {
/* 357 */         doc = new BasicDocument(tlp.getTokenizerFactory()).init(new InputStreamReader(new FileInputStream(filename), this.encoding));
/*     */       }
/*     */     } catch (Exception e) {
/* 360 */       JOptionPane.showMessageDialog(this, "Could not load file " + filename + "\n" + e, null, 0);
/* 361 */       e.printStackTrace();
/* 362 */       setStatus("Error loading document");
/* 363 */       return;
/*     */     }
/*     */     
/*     */ 
/* 367 */     StringBuilder docStr = new StringBuilder();
/* 368 */     for (Iterator it = doc.iterator(); it.hasNext();) {
/* 369 */       if (docStr.length() > 0) {
/* 370 */         docStr.append(' ');
/*     */       }
/* 372 */       docStr.append(it.next().toString());
/*     */     }
/* 374 */     this.textPane.setText(docStr.toString());
/* 375 */     this.dataFileLabel.setText(urlOrFile);
/*     */     
/* 377 */     highlightSentence(0);
/* 378 */     this.forwardButton.setEnabled(this.endIndex != this.textPane.getText().length() - 1);
/*     */     
/* 380 */     this.textPane.setCaretPosition(0);
/*     */     
/* 382 */     setStatus("Done");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void loadParser()
/*     */   {
/* 389 */     this.jfc.setDialogTitle("Load parser");
/* 390 */     int status = this.jfc.showOpenDialog(this);
/* 391 */     if (status == 0) {
/* 392 */       loadParser(this.jfc.getSelectedFile().getPath());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void loadParser(String filename)
/*     */   {
/* 400 */     if (filename == null) {
/* 401 */       return;
/*     */     }
/*     */     
/*     */ 
/* 405 */     File file = new File(filename);
/* 406 */     if (file.exists()) {
/* 407 */       this.lpThread = new LoadParserThread(filename);
/* 408 */       this.lpThread.start();
/* 409 */       startProgressMonitor("Loading Parser", 60);
/*     */     } else {
/* 411 */       JOptionPane.showMessageDialog(this, "Could not find file " + filename, null, 0);
/* 412 */       setStatus("Error loading parser");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void startProgressMonitor(String text, int maxCount)
/*     */   {
/* 421 */     if ((this.glassPane == null) && 
/* 422 */       (getRootPane() != null)) {
/* 423 */       this.glassPane = getRootPane().getGlassPane();
/* 424 */       this.glassPane.addMouseListener(new MouseAdapter() {
/*     */         public void mouseClicked(MouseEvent evt) {
/* 426 */           Toolkit.getDefaultToolkit().beep();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/* 431 */     if (this.glassPane != null) {
/* 432 */       this.glassPane.setVisible(true);
/*     */     }
/*     */     
/* 435 */     this.statusLabel.setText(text);
/* 436 */     this.progressBar.setMaximum(maxCount);
/* 437 */     this.progressBar.setValue(0);
/* 438 */     this.count = 0;
/* 439 */     this.timer.start();
/* 440 */     this.progressBar.setVisible(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void stopProgressMonitor()
/*     */   {
/* 447 */     this.timer.stop();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 452 */     this.progressBar.setVisible(false);
/* 453 */     if (this.glassPane != null) {
/* 454 */       this.glassPane.setVisible(false);
/*     */     }
/* 456 */     this.lpThread = null;
/* 457 */     this.parseThread = null;
/*     */   }
/*     */   
/*     */   private class LoadParserThread
/*     */     extends Thread
/*     */   {
/*     */     String filename;
/*     */     
/*     */     LoadParserThread(String filename)
/*     */     {
/* 467 */       this.filename = filename;
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       try {
/* 472 */         ParserPanel.this.parser = new LexicalizedParser(this.filename);
/*     */       } catch (Exception ex) {
/* 474 */         JOptionPane.showMessageDialog(ParserPanel.this, "Error loading parser: " + this.filename, null, 0);
/* 475 */         ParserPanel.this.setStatus("Error loading parser");
/* 476 */         ParserPanel.this.parser = null;
/*     */       } catch (OutOfMemoryError e) {
/* 478 */         JOptionPane.showMessageDialog(ParserPanel.this, "Could not load parser. Out of memory.", null, 0);
/* 479 */         ParserPanel.this.setStatus("Error loading parser");
/* 480 */         ParserPanel.this.parser = null;
/*     */       }
/*     */       
/* 483 */       ParserPanel.this.stopProgressMonitor();
/* 484 */       if (ParserPanel.this.parser != null) {
/* 485 */         ParserPanel.this.setStatus("Loaded parser.");
/* 486 */         ParserPanel.this.parserFileLabel.setText("Parser: " + this.filename);
/* 487 */         ParserPanel.this.parseButton.setEnabled(true);
/* 488 */         ParserPanel.this.parseNextButton.setEnabled(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class ParseThread
/*     */     extends Thread
/*     */   {
/*     */     List<? extends HasWord> sentence;
/*     */     
/*     */     public ParseThread()
/*     */     {
/* 501 */       this.sentence = sentence;
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       boolean successful;
/*     */       try {
/* 507 */         successful = ParserPanel.this.parser.parse(this.sentence);
/*     */       } catch (Exception e) {
/* 509 */         ParserPanel.this.stopProgressMonitor();
/* 510 */         JOptionPane.showMessageDialog(ParserPanel.this, "Could not parse selected sentence\n(sentence probably too long)", null, 0);
/* 511 */         ParserPanel.this.setStatus("Error parsing");
/* 512 */         return;
/*     */       }
/*     */       
/* 515 */       ParserPanel.this.stopProgressMonitor();
/* 516 */       ParserPanel.this.setStatus("Done");
/* 517 */       if (successful)
/*     */       {
/* 519 */         Tree tree = ParserPanel.this.parser.getBestParse();
/*     */         
/* 521 */         ParserPanel.this.treePanel.setTree(tree);
/* 522 */         ParserPanel.this.clearButton.setEnabled(true);
/*     */       } else {
/* 524 */         JOptionPane.showMessageDialog(ParserPanel.this, "Could not parse selected sentence", null, 0);
/* 525 */         ParserPanel.this.setStatus("Error parsing");
/* 526 */         ParserPanel.this.treePanel.setTree(null);
/* 527 */         ParserPanel.this.clearButton.setEnabled(false);
/*     */       }
/* 529 */       if (ParserPanel.this.scrollWhenDone) {
/* 530 */         ParserPanel.this.scrollForward();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class TimerListener implements ActionListener
/*     */   {
/*     */     private TimerListener() {}
/*     */     
/*     */     public void actionPerformed(ActionEvent e)
/*     */     {
/* 541 */       ParserPanel.this.progressBar.setValue(Math.min(ParserPanel.access$908(ParserPanel.this), ParserPanel.this.progressBar.getMaximum() - 1));
/*     */     }
/*     */   }
/*     */   
/*     */   private JPanel dataFilePanel;
/*     */   private JPanel buttonPanel;
/*     */   private JTextPane textPane;
/*     */   private JProgressBar progressBar;
/*     */   private JPanel parserFilePanel;
/*     */   private JButton loadParserButton;
/*     */   private void initComponents()
/*     */   {
/* 553 */     this.splitPane = new JSplitPane();
/* 554 */     this.topPanel = new JPanel();
/* 555 */     this.buttonsAndFilePanel = new JPanel();
/* 556 */     this.loadButtonPanel = new JPanel();
/* 557 */     this.loadFileButton = new JButton();
/* 558 */     this.loadParserButton = new JButton();
/* 559 */     this.buttonPanel = new JPanel();
/* 560 */     this.backButton = new JButton();
/* 561 */     if (getClass().getResource("/edu/stanford/nlp/parser/ui/leftarrow.gif") != null) {
/* 562 */       this.backButton.setIcon(new ImageIcon(getClass().getResource("/edu/stanford/nlp/parser/ui/leftarrow.gif")));
/*     */     } else {
/* 564 */       this.backButton.setText("< Prev");
/*     */     }
/* 566 */     this.forwardButton = new JButton();
/* 567 */     if (getClass().getResource("/edu/stanford/nlp/parser/ui/rightarrow.gif") != null) {
/* 568 */       this.forwardButton.setIcon(new ImageIcon(getClass().getResource("/edu/stanford/nlp/parser/ui/rightarrow.gif")));
/*     */     } else {
/* 570 */       this.forwardButton.setText("Next >");
/*     */     }
/* 572 */     this.parseButton = new JButton();
/* 573 */     this.parseNextButton = new JButton();
/* 574 */     this.clearButton = new JButton();
/* 575 */     this.dataFilePanel = new JPanel();
/* 576 */     this.dataFileLabel = new JLabel();
/* 577 */     this.textScrollPane = new JScrollPane();
/* 578 */     this.textPane = new JTextPane();
/* 579 */     this.treeContainer = new JPanel();
/* 580 */     this.parserFilePanel = new JPanel();
/* 581 */     this.parserFileLabel = new JLabel();
/* 582 */     this.statusPanel = new JPanel();
/* 583 */     this.statusLabel = new JLabel();
/* 584 */     this.progressBar = new JProgressBar();
/* 585 */     this.progressBar.setVisible(false);
/*     */     
/* 587 */     setLayout(new BorderLayout());
/*     */     
/* 589 */     this.splitPane.setOrientation(0);
/* 590 */     this.topPanel.setLayout(new BorderLayout());
/*     */     
/* 592 */     this.buttonsAndFilePanel.setLayout(new BoxLayout(this.buttonsAndFilePanel, 1));
/*     */     
/* 594 */     this.loadButtonPanel.setLayout(new FlowLayout(0));
/*     */     
/* 596 */     this.loadFileButton.setText("Load File");
/* 597 */     this.loadFileButton.setToolTipText("Load a data file.");
/* 598 */     this.loadFileButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 600 */         ParserPanel.this.loadFileButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 603 */     });
/* 604 */     this.loadButtonPanel.add(this.loadFileButton);
/*     */     
/* 606 */     this.loadParserButton.setText("Load Parser");
/* 607 */     this.loadParserButton.setToolTipText("Load a serialized parser.");
/* 608 */     this.loadParserButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 610 */         ParserPanel.this.loadParserButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 613 */     });
/* 614 */     this.loadButtonPanel.add(this.loadParserButton);
/*     */     
/* 616 */     this.buttonsAndFilePanel.add(this.loadButtonPanel);
/*     */     
/* 618 */     this.buttonPanel.setLayout(new FlowLayout(0));
/*     */     
/* 620 */     this.backButton.setToolTipText("Scroll backward one sentence.");
/* 621 */     this.backButton.setEnabled(false);
/* 622 */     this.backButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 624 */         ParserPanel.this.backButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 627 */     });
/* 628 */     this.buttonPanel.add(this.backButton);
/*     */     
/* 630 */     this.forwardButton.setToolTipText("Scroll forward one sentence.");
/* 631 */     this.forwardButton.setEnabled(false);
/* 632 */     this.forwardButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 634 */         ParserPanel.this.forwardButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 637 */     });
/* 638 */     this.buttonPanel.add(this.forwardButton);
/*     */     
/* 640 */     this.parseButton.setText("Parse");
/* 641 */     this.parseButton.setToolTipText("Parse selected sentence.");
/* 642 */     this.parseButton.setEnabled(false);
/* 643 */     this.parseButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 645 */         ParserPanel.this.parseButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 648 */     });
/* 649 */     this.buttonPanel.add(this.parseButton);
/*     */     
/* 651 */     this.parseNextButton.setText("Parse >");
/* 652 */     this.parseNextButton.setToolTipText("Parse selected sentence and then scrolls forward one sentence.");
/* 653 */     this.parseNextButton.setEnabled(false);
/* 654 */     this.parseNextButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 656 */         ParserPanel.this.parseNextButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 659 */     });
/* 660 */     this.buttonPanel.add(this.parseNextButton);
/*     */     
/* 662 */     this.clearButton.setText("Clear");
/* 663 */     this.clearButton.setToolTipText("Clears parse tree.");
/* 664 */     this.clearButton.setEnabled(false);
/* 665 */     this.clearButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 667 */         ParserPanel.this.clearButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 670 */     });
/* 671 */     this.buttonPanel.add(this.clearButton);
/*     */     
/* 673 */     this.buttonsAndFilePanel.add(this.buttonPanel);
/*     */     
/* 675 */     this.dataFilePanel.setLayout(new FlowLayout(0));
/*     */     
/* 677 */     this.dataFilePanel.add(this.dataFileLabel);
/*     */     
/* 679 */     this.buttonsAndFilePanel.add(this.dataFilePanel);
/*     */     
/* 681 */     this.topPanel.add(this.buttonsAndFilePanel, "North");
/*     */     
/* 683 */     this.textPane.setPreferredSize(new Dimension(250, 250));
/* 684 */     this.textPane.addFocusListener(new FocusAdapter() {
/*     */       public void focusLost(FocusEvent evt) {
/* 686 */         ParserPanel.this.textPaneFocusLost(evt);
/*     */       }
/*     */       
/* 689 */     });
/* 690 */     this.textPane.addMouseListener(new MouseAdapter() {
/*     */       public void mouseClicked(MouseEvent evt) {
/* 692 */         ParserPanel.this.textPaneMouseClicked(evt);
/*     */       }
/*     */       
/* 695 */     });
/* 696 */     this.textPane.addMouseMotionListener(new MouseMotionAdapter() {
/*     */       public void mouseDragged(MouseEvent evt) {
/* 698 */         ParserPanel.this.textPaneMouseDragged(evt);
/*     */       }
/*     */       
/* 701 */     });
/* 702 */     this.textScrollPane.setViewportView(this.textPane);
/*     */     
/* 704 */     this.topPanel.add(this.textScrollPane, "Center");
/*     */     
/* 706 */     this.splitPane.setLeftComponent(this.topPanel);
/*     */     
/* 708 */     this.treeContainer.setLayout(new BorderLayout());
/*     */     
/* 710 */     this.treeContainer.setBackground(new Color(255, 255, 255));
/* 711 */     this.treeContainer.setBorder(new BevelBorder(0));
/* 712 */     this.treeContainer.setForeground(new Color(0, 0, 0));
/* 713 */     this.treeContainer.setPreferredSize(new Dimension(200, 200));
/* 714 */     this.treePanel = new TreeJPanel();
/* 715 */     this.treeContainer.add("Center", this.treePanel);
/* 716 */     this.treePanel.setBackground(Color.white);
/* 717 */     this.parserFilePanel.setLayout(new FlowLayout(0));
/*     */     
/* 719 */     this.parserFilePanel.setBackground(new Color(255, 255, 255));
/* 720 */     this.parserFileLabel.setText("Parser: None");
/* 721 */     this.parserFilePanel.add(this.parserFileLabel);
/*     */     
/* 723 */     this.treeContainer.add(this.parserFilePanel, "North");
/*     */     
/* 725 */     this.splitPane.setRightComponent(this.treeContainer);
/*     */     
/* 727 */     add(this.splitPane, "Center");
/*     */     
/* 729 */     this.statusPanel.setLayout(new FlowLayout(0));
/*     */     
/* 731 */     this.statusLabel.setText("Ready");
/* 732 */     this.statusPanel.add(this.statusLabel);
/*     */     
/* 734 */     this.progressBar.setName("");
/* 735 */     this.statusPanel.add(this.progressBar);
/*     */     
/* 737 */     add(this.statusPanel, "South");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void textPaneFocusLost(FocusEvent evt)
/*     */   {
/* 747 */     highlightEditedSentence();
/*     */   }
/*     */   
/*     */   private void parseNextButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 752 */     parse();
/* 753 */     this.scrollWhenDone = true;
/*     */   }
/*     */   
/*     */   private void clearButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 758 */     this.treePanel.setTree(null);
/* 759 */     this.clearButton.setEnabled(false);
/*     */   }
/*     */   
/*     */   private void textPaneMouseDragged(MouseEvent evt)
/*     */   {
/* 764 */     highlightSelectedSentence();
/*     */   }
/*     */   
/*     */   private void textPaneMouseClicked(MouseEvent evt)
/*     */   {
/* 769 */     highlightSelectedSentence();
/*     */   }
/*     */   
/*     */   private void parseButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 774 */     parse();
/* 775 */     this.scrollWhenDone = false;
/*     */   }
/*     */   
/*     */   private void loadParserButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 780 */     loadParser();
/*     */   }
/*     */   
/*     */   private void loadFileButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 785 */     loadFile();
/*     */   }
/*     */   
/*     */   private void backButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 790 */     scrollBack();
/*     */   }
/*     */   
/*     */   private void forwardButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 795 */     scrollForward();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\ui\ParserPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */