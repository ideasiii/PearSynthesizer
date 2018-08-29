/*     */ package edu.stanford.nlp.io.ui;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ public class OpenPageDialog extends javax.swing.JDialog
/*     */ {
/*     */   public static final int CANCEL_OPTION = 0;
/*     */   public static final int APPROVE_OPTION = 1;
/*     */   private javax.swing.JFileChooser jfc;
/*     */   private int status;
/*     */   private javax.swing.JTextField urlTextField;
/*     */   private JButton openButton;
/*     */   private javax.swing.JLabel jLabel1;
/*     */   private JPanel jPanel3;
/*     */   private javax.swing.JLabel jLabel2;
/*     */   private JPanel jPanel2;
/*     */   private JButton cancelButton;
/*     */   private JButton browseButton;
/*     */   private JPanel jPanel1;
/*     */   
/*     */   public OpenPageDialog(java.awt.Frame parent, boolean modal)
/*     */   {
/*  25 */     super(parent, modal);
/*  26 */     initComponents();
/*  27 */     this.jfc = new javax.swing.JFileChooser();
/*     */     
/*  29 */     addWindowListener(new java.awt.event.WindowAdapter() {
/*     */       public void windowClosing(java.awt.event.WindowEvent we) {
/*  31 */         OpenPageDialog.this.status = 0;
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFileChooser(javax.swing.JFileChooser jfc)
/*     */   {
/*  40 */     this.jfc = jfc;
/*     */   }
/*     */   
/*     */   public String getPage()
/*     */   {
/*  45 */     return this.urlTextField.getText();
/*     */   }
/*     */   
/*     */   public int getStatus()
/*     */   {
/*  50 */     return this.status;
/*     */   }
/*     */   
/*     */   private void browseFiles()
/*     */   {
/*  55 */     this.jfc.setDialogTitle("Open file");
/*  56 */     int status = this.jfc.showOpenDialog(this);
/*  57 */     if (status == 0) {
/*  58 */       this.urlTextField.setText(this.jfc.getSelectedFile().getPath());
/*  59 */       this.openButton.setEnabled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   private void approve() {
/*  64 */     this.status = 1;
/*  65 */     closeDialog(null);
/*     */   }
/*     */   
/*     */   private void enableOpenButton()
/*     */   {
/*  70 */     this.openButton.setEnabled(this.urlTextField.getText().length() > 0);
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
/*  81 */     this.jPanel1 = new JPanel();
/*  82 */     this.jLabel2 = new javax.swing.JLabel();
/*  83 */     this.jPanel3 = new JPanel();
/*  84 */     this.jLabel1 = new javax.swing.JLabel();
/*  85 */     this.urlTextField = new javax.swing.JTextField();
/*  86 */     this.jPanel2 = new JPanel();
/*  87 */     this.openButton = new JButton();
/*  88 */     this.cancelButton = new JButton();
/*  89 */     this.browseButton = new JButton();
/*     */     
/*  91 */     addWindowListener(new java.awt.event.WindowAdapter() {
/*     */       public void windowClosing(java.awt.event.WindowEvent evt) {
/*  93 */         OpenPageDialog.this.closeDialog(evt);
/*     */       }
/*     */       
/*  96 */     });
/*  97 */     this.jPanel1.setLayout(new javax.swing.BoxLayout(this.jPanel1, 1));
/*     */     
/*  99 */     this.jLabel2.setText("Type in the internet address of a document or web page.");
/* 100 */     this.jPanel1.add(this.jLabel2);
/*     */     
/* 102 */     this.jLabel1.setText("Open");
/* 103 */     this.jPanel3.add(this.jLabel1);
/*     */     
/* 105 */     this.urlTextField.setMinimumSize(new java.awt.Dimension(100, 20));
/* 106 */     this.urlTextField.setPreferredSize(new java.awt.Dimension(300, 20));
/* 107 */     this.urlTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
/*     */       public void changedUpdate(javax.swing.event.DocumentEvent e) {
/* 109 */         OpenPageDialog.this.enableOpenButton();
/*     */       }
/*     */       
/*     */       public void insertUpdate(javax.swing.event.DocumentEvent e) {
/* 113 */         OpenPageDialog.this.enableOpenButton();
/*     */       }
/*     */       
/*     */       public void removeUpdate(javax.swing.event.DocumentEvent e) {
/* 117 */         OpenPageDialog.this.enableOpenButton();
/*     */       }
/* 119 */     });
/* 120 */     this.urlTextField.addActionListener(new java.awt.event.ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 122 */         OpenPageDialog.this.urlTextFieldActionPerformed(evt);
/*     */       }
/*     */       
/* 125 */     });
/* 126 */     this.jPanel3.add(this.urlTextField);
/*     */     
/* 128 */     this.jPanel1.add(this.jPanel3);
/*     */     
/* 130 */     getContentPane().add(this.jPanel1, "North");
/*     */     
/* 132 */     this.openButton.setText("Open");
/* 133 */     this.openButton.setEnabled(false);
/* 134 */     this.openButton.addActionListener(new java.awt.event.ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 136 */         OpenPageDialog.this.openButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 139 */     });
/* 140 */     this.jPanel2.add(this.openButton);
/*     */     
/* 142 */     this.cancelButton.setText("Cancel");
/* 143 */     this.cancelButton.addActionListener(new java.awt.event.ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 145 */         OpenPageDialog.this.cancelButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 148 */     });
/* 149 */     this.jPanel2.add(this.cancelButton);
/*     */     
/* 151 */     this.browseButton.setText("Browse");
/* 152 */     this.browseButton.addActionListener(new java.awt.event.ActionListener() {
/*     */       public void actionPerformed(ActionEvent evt) {
/* 154 */         OpenPageDialog.this.browseButtonActionPerformed(evt);
/*     */       }
/*     */       
/* 157 */     });
/* 158 */     this.jPanel2.add(this.browseButton);
/*     */     
/* 160 */     getContentPane().add(this.jPanel2, "South");
/*     */     
/* 162 */     pack();
/*     */   }
/*     */   
/*     */   private void urlTextFieldActionPerformed(ActionEvent evt)
/*     */   {
/* 167 */     if (this.urlTextField.getText().length() > 0) {
/* 168 */       approve();
/*     */     }
/*     */   }
/*     */   
/*     */   private void browseButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 174 */     browseFiles();
/*     */   }
/*     */   
/*     */   private void cancelButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 179 */     this.status = 0;
/* 180 */     closeDialog(null);
/*     */   }
/*     */   
/*     */   private void openButtonActionPerformed(ActionEvent evt)
/*     */   {
/* 185 */     approve();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void closeDialog(java.awt.event.WindowEvent evt)
/*     */   {
/* 192 */     setVisible(false);
/* 193 */     dispose();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 200 */     new OpenPageDialog(new javax.swing.JFrame(), true).setVisible(true);
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\io\ui\OpenPageDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */