/*     */ package edu.stanford.nlp.objectbank;
/*     */ 
/*     */ import edu.stanford.nlp.process.Function;
/*     */ import edu.stanford.nlp.util.AbstractIterator;
/*     */ import edu.stanford.nlp.util.XMLUtils;
/*     */ import edu.stanford.nlp.util.XMLUtils.XMLTag;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Iterator;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLBeginEndIterator
/*     */   extends AbstractIterator
/*     */ {
/*     */   private Pattern tagNamePattern;
/*     */   private BufferedReader in;
/*     */   private Object nextToken;
/*     */   private Function op;
/*     */   private boolean keepInternalTags;
/*     */   private boolean keepDelimitingTags;
/*     */   
/*     */   public XMLBeginEndIterator(Reader in, String tagNameRegexp)
/*     */   {
/*  32 */     this(in, tagNameRegexp, new IdentityFunction(), false);
/*     */   }
/*     */   
/*     */   public XMLBeginEndIterator(Reader in, String tagNameRegexp, boolean keepInternalTags) {
/*  36 */     this(in, tagNameRegexp, new IdentityFunction(), keepInternalTags);
/*     */   }
/*     */   
/*     */   public XMLBeginEndIterator(Reader in, String tagNameRegexp, Function op, boolean keepInternalTags) {
/*  40 */     this(in, tagNameRegexp, op, keepInternalTags, false);
/*     */   }
/*     */   
/*     */   public XMLBeginEndIterator(Reader in, String tagNameRegexp, boolean keepInternalTags, boolean keepDelimitingTags) {
/*  44 */     this(in, tagNameRegexp, new IdentityFunction(), keepInternalTags, keepDelimitingTags);
/*     */   }
/*     */   
/*     */   public XMLBeginEndIterator(Reader in, String tagNameRegexp, Function op, boolean keepInternalTags, boolean keepDelimitingTags) {
/*  48 */     this.tagNamePattern = Pattern.compile(tagNameRegexp);
/*  49 */     this.op = op;
/*  50 */     this.keepInternalTags = keepInternalTags;
/*  51 */     this.keepDelimitingTags = keepDelimitingTags;
/*  52 */     this.in = new BufferedReader(in);
/*  53 */     setNext();
/*     */   }
/*     */   
/*     */   private void setNext() {
/*  57 */     String s = getNext();
/*  58 */     this.nextToken = parseString(s);
/*     */   }
/*     */   
/*     */   private String getNext()
/*     */   {
/*  63 */     StringBuilder result = new StringBuilder();
/*     */     try {
/*     */       XMLUtils.XMLTag tag;
/*     */       do {
/*  67 */         String text = XMLUtils.readUntilTag(this.in);
/*     */         
/*     */ 
/*  70 */         tag = XMLUtils.readAndParseTag(this.in);
/*     */         
/*  72 */         if (tag == null) {
/*  73 */           return null;
/*     */         }
/*  75 */       } while ((!this.tagNamePattern.matcher(tag.name).matches()) || (tag.isEndTag));
/*  76 */       if (this.keepDelimitingTags) {
/*  77 */         result.append(tag.toString());
/*     */       }
/*     */       for (;;) {
/*  80 */         String text = XMLUtils.readUntilTag(this.in);
/*  81 */         if (text != null)
/*     */         {
/*     */ 
/*  84 */           result.append(text);
/*     */         }
/*  86 */         String tagString = XMLUtils.readTag(this.in);
/*  87 */         tag = XMLUtils.parseTag(tagString);
/*  88 */         if (tag == null) {
/*  89 */           return null;
/*     */         }
/*  91 */         if ((this.tagNamePattern.matcher(tag.name).matches()) && (tag.isEndTag)) {
/*  92 */           if (!this.keepDelimitingTags) break;
/*  93 */           result.append(tagString); break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  99 */         if (this.keepInternalTags) {
/* 100 */           result.append(tagString);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 105 */       e.printStackTrace();
/*     */     }
/* 107 */     return result.toString();
/*     */   }
/*     */   
/*     */   protected Object parseString(String s) {
/* 111 */     return this.op.apply(s);
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/* 115 */     return this.nextToken != null;
/*     */   }
/*     */   
/*     */   public Object next() {
/* 119 */     Object token = this.nextToken;
/* 120 */     setNext();
/* 121 */     return token;
/*     */   }
/*     */   
/*     */   public Object peek() {
/* 125 */     return this.nextToken;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IteratorFromReaderFactory getFactory(String tag)
/*     */   {
/* 136 */     return new XMLBeginEndIteratorFactory(tag, new IdentityFunction(), false, false);
/*     */   }
/*     */   
/*     */   public static IteratorFromReaderFactory getFactory(String tag, boolean keepInternalTags, boolean keepDelimitingTags) {
/* 140 */     return new XMLBeginEndIteratorFactory(tag, new IdentityFunction(), keepInternalTags, keepDelimitingTags);
/*     */   }
/*     */   
/*     */   public static IteratorFromReaderFactory getFactory(String tag, Function op) {
/* 144 */     return new XMLBeginEndIteratorFactory(tag, op, false, false);
/*     */   }
/*     */   
/*     */   public static IteratorFromReaderFactory getFactory(String tag, Function op, boolean keepInternalTags, boolean keepDelimitingTags) {
/* 148 */     return new XMLBeginEndIteratorFactory(tag, op, keepInternalTags, keepDelimitingTags);
/*     */   }
/*     */   
/*     */   static class XMLBeginEndIteratorFactory implements IteratorFromReaderFactory
/*     */   {
/*     */     private String tag;
/*     */     private Function op;
/*     */     private boolean keepInternalTags;
/*     */     private boolean keepDelimitingTags;
/*     */     
/*     */     public XMLBeginEndIteratorFactory(String tag, Function op, boolean keepInternalTags, boolean keepDelimitingTags) {
/* 159 */       this.tag = tag;
/* 160 */       this.op = op;
/* 161 */       this.keepInternalTags = keepInternalTags;
/* 162 */       this.keepDelimitingTags = keepDelimitingTags;
/*     */     }
/*     */     
/*     */     public Iterator getIterator(Reader r) {
/* 166 */       return new XMLBeginEndIterator(r, this.tag, this.op, this.keepInternalTags, this.keepDelimitingTags);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 171 */     Reader in = new FileReader(args[0]);
/* 172 */     Iterator iter = new XMLBeginEndIterator(in, args[1], args[2].equalsIgnoreCase("true"));
/* 173 */     while (iter.hasNext()) {
/* 174 */       String s = (String)iter.next();
/* 175 */       System.out.println("*************************************************");
/* 176 */       System.out.println(s);
/*     */     }
/* 178 */     in.close();
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\objectbank\XMLBeginEndIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */