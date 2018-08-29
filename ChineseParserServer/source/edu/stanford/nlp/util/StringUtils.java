/*      */ package edu.stanford.nlp.util;
/*      */ 
/*      */ import edu.stanford.nlp.io.RuntimeIOException;
/*      */ import edu.stanford.nlp.math.SloppyMath;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileReader;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.Reader;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ 
/*      */ public class StringUtils
/*      */ {
/*   33 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*      */   
/*      */ 
/*      */   private static final int SLURPBUFFSIZE = 16000;
/*      */   
/*      */   private static final String PROP = "prop";
/*      */   
/*      */   private static final String PROPS = "props";
/*      */   
/*      */   private static final String PROPERTIES = "properties";
/*      */   
/*      */ 
/*      */   public static boolean find(String str, String regex)
/*      */   {
/*   47 */     return Pattern.compile(regex).matcher(str).find();
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
/*      */   public static boolean lookingAt(String str, String regex)
/*      */   {
/*   62 */     return Pattern.compile(regex).matcher(str).lookingAt();
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
/*      */   public static boolean matches(String str, String regex)
/*      */   {
/*   77 */     return Pattern.compile(regex).matcher(str).matches();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String slurpFile(File file)
/*      */     throws IOException
/*      */   {
/*   87 */     Reader r = new FileReader(file);
/*   88 */     return slurpReader(r);
/*      */   }
/*      */   
/*      */ 
/*      */   public static String slurpGZippedFile(String filename)
/*      */     throws IOException
/*      */   {
/*   95 */     Reader r = new InputStreamReader(new GZIPInputStream(new FileInputStream(filename)));
/*   96 */     return slurpReader(r);
/*      */   }
/*      */   
/*      */ 
/*      */   public static String slurpGZippedFile(File file)
/*      */     throws IOException
/*      */   {
/*  103 */     Reader r = new InputStreamReader(new GZIPInputStream(new FileInputStream(file)));
/*  104 */     return slurpReader(r);
/*      */   }
/*      */   
/*      */   public static String slurpGBFileNoExceptions(String filename) {
/*  108 */     return slurpFileNoExceptions(filename, "GB18030");
/*      */   }
/*      */   
/*      */ 
/*      */   public static String slurpFile(String filename, String encoding)
/*      */     throws IOException
/*      */   {
/*  115 */     Reader r = new InputStreamReader(new FileInputStream(filename), encoding);
/*  116 */     return slurpReader(r);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String slurpFileNoExceptions(String filename, String encoding)
/*      */   {
/*      */     try
/*      */     {
/*  126 */       return slurpFile(filename, encoding);
/*      */     } catch (Exception e) {
/*  128 */       throw new RuntimeIOException("slurpFile IO problem", e);
/*      */     }
/*      */   }
/*      */   
/*      */   public static String slurpGBFile(String filename) throws IOException
/*      */   {
/*  134 */     return slurpFile(filename, "GB18030");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String slurpReader(Reader reader)
/*      */   {
/*  143 */     BufferedReader r = new BufferedReader(reader);
/*  144 */     StringBuilder buff = new StringBuilder();
/*      */     try {
/*  146 */       char[] chars = new char['㺀'];
/*      */       for (;;) {
/*  148 */         int amountRead = r.read(chars, 0, 16000);
/*  149 */         if (amountRead < 0) {
/*      */           break;
/*      */         }
/*  152 */         buff.append(chars, 0, amountRead);
/*      */       }
/*  154 */       r.close();
/*      */     } catch (Exception e) {
/*  156 */       throw new RuntimeIOException("slurpReader IO problem", e);
/*      */     }
/*  158 */     return buff.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String slurpFile(String filename)
/*      */     throws IOException
/*      */   {
/*  167 */     return slurpReader(new FileReader(filename));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String slurpFileNoExceptions(File file)
/*      */   {
/*      */     try
/*      */     {
/*  179 */       return slurpReader(new FileReader(file));
/*      */     } catch (Exception e) {
/*  181 */       e.printStackTrace(); }
/*  182 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String slurpFileNoExceptions(String filename)
/*      */   {
/*      */     try
/*      */     {
/*  196 */       return slurpFile(filename);
/*      */     } catch (Exception e) {
/*  198 */       e.printStackTrace(); }
/*  199 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String slurpGBURL(URL u)
/*      */     throws IOException
/*      */   {
/*  208 */     return slurpURL(u, "GB18030");
/*      */   }
/*      */   
/*      */ 
/*      */   public static String slurpGBURLNoExceptions(URL u)
/*      */   {
/*      */     try
/*      */     {
/*  216 */       return slurpGBURL(u);
/*      */     } catch (Exception e) {
/*  218 */       e.printStackTrace(); }
/*  219 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String slurpURLNoExceptions(URL u, String encoding)
/*      */   {
/*      */     try
/*      */     {
/*  228 */       return slurpURL(u, encoding);
/*      */     } catch (Exception e) {
/*  230 */       e.printStackTrace(); }
/*  231 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String slurpURL(URL u, String encoding)
/*      */     throws IOException
/*      */   {
/*  240 */     String lineSeparator = System.getProperty("line.separator");
/*  241 */     URLConnection uc = u.openConnection();
/*  242 */     uc.setReadTimeout(30000);
/*      */     InputStream is;
/*      */     try {
/*  245 */       is = uc.getInputStream();
/*      */     }
/*      */     catch (SocketTimeoutException e) {
/*  248 */       System.err.println("Time out. Return empty string");
/*  249 */       return "";
/*      */     }
/*  251 */     BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
/*      */     
/*  253 */     StringBuilder buff = new StringBuilder(16000);
/*  254 */     String temp; while ((temp = br.readLine()) != null) {
/*  255 */       buff.append(temp);
/*  256 */       buff.append(lineSeparator);
/*      */     }
/*  258 */     br.close();
/*  259 */     return buff.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String slurpURL(URL u)
/*      */     throws IOException
/*      */   {
/*  267 */     String lineSeparator = System.getProperty("line.separator");
/*  268 */     URLConnection uc = u.openConnection();
/*  269 */     InputStream is = uc.getInputStream();
/*  270 */     BufferedReader br = new BufferedReader(new InputStreamReader(is));
/*      */     
/*  272 */     StringBuilder buff = new StringBuilder(16000);
/*  273 */     String temp; while ((temp = br.readLine()) != null) {
/*  274 */       buff.append(temp);
/*  275 */       buff.append(lineSeparator);
/*      */     }
/*  277 */     br.close();
/*  278 */     return buff.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String slurpURLNoExceptions(URL u)
/*      */   {
/*      */     try
/*      */     {
/*  287 */       return slurpURL(u);
/*      */     } catch (Exception e) {
/*  289 */       e.printStackTrace(); }
/*  290 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String slurpURL(String path)
/*      */     throws Exception
/*      */   {
/*  298 */     return slurpURL(new URL(path));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String slurpURLNoExceptions(String path)
/*      */   {
/*      */     try
/*      */     {
/*  307 */       return slurpURL(path);
/*      */     } catch (Exception e) {
/*  309 */       e.printStackTrace(); }
/*  310 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String join(Iterable l, String glue)
/*      */   {
/*  321 */     StringBuilder sb = new StringBuilder();
/*  322 */     boolean first = true;
/*  323 */     for (Object o : l) {
/*  324 */       if (!first) {
/*  325 */         sb.append(glue);
/*      */       }
/*  327 */       sb.append(o.toString());
/*  328 */       first = false;
/*      */     }
/*  330 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String join(List l, String glue)
/*      */   {
/*  340 */     StringBuilder sb = new StringBuilder();
/*  341 */     int i = 0; for (int sz = l.size(); i < sz; i++) {
/*  342 */       if (i > 0) {
/*  343 */         sb.append(glue);
/*      */       }
/*  345 */       sb.append(l.get(i).toString());
/*      */     }
/*  347 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String join(Object[] elements, String glue)
/*      */   {
/*  356 */     return join(Arrays.asList(elements), glue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String join(List l)
/*      */   {
/*  363 */     return join(l, " ");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String join(Object[] elements)
/*      */   {
/*  370 */     return join(elements, " ");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static List split(String s)
/*      */   {
/*  378 */     return split(s, "\\s+");
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
/*      */   public static List split(String str, String regex)
/*      */   {
/*  393 */     return Arrays.asList(str.split(regex));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String pad(String str, int totalChars)
/*      */   {
/*  404 */     if (str == null) {
/*  405 */       str = "null";
/*      */     }
/*  407 */     int slen = str.length();
/*  408 */     StringBuilder sb = new StringBuilder(str);
/*  409 */     for (int i = 0; i < totalChars - slen; i++) {
/*  410 */       sb.append(" ");
/*      */     }
/*  412 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String pad(Object obj, int totalChars)
/*      */   {
/*  419 */     return pad(obj.toString(), totalChars);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String padOrTrim(String str, int num)
/*      */   {
/*  430 */     if (str == null) {
/*  431 */       str = "null";
/*      */     }
/*  433 */     int leng = str.length();
/*  434 */     if (leng < num) {
/*  435 */       StringBuilder sb = new StringBuilder(str);
/*  436 */       for (int i = 0; i < num - leng; i++) {
/*  437 */         sb.append(" ");
/*      */       }
/*  439 */       return sb.toString(); }
/*  440 */     if (leng > num) {
/*  441 */       return str.substring(0, num);
/*      */     }
/*  443 */     return str;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String padLeftOrTrim(String str, int num)
/*      */   {
/*  454 */     if (str == null) {
/*  455 */       str = "null";
/*      */     }
/*  457 */     int leng = str.length();
/*  458 */     if (leng < num) {
/*  459 */       StringBuilder sb = new StringBuilder();
/*  460 */       for (int i = 0; i < num - leng; i++) {
/*  461 */         sb.append(" ");
/*      */       }
/*  463 */       sb.append(str);
/*  464 */       return sb.toString(); }
/*  465 */     if (leng > num) {
/*  466 */       return str.substring(str.length() - num);
/*      */     }
/*  468 */     return str;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String padOrTrim(Object obj, int totalChars)
/*      */   {
/*  476 */     return padOrTrim(obj.toString(), totalChars);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String padLeft(String str, int totalChars, char ch)
/*      */   {
/*  485 */     if (str == null) {
/*  486 */       str = "null";
/*      */     }
/*  488 */     StringBuilder sb = new StringBuilder();
/*  489 */     int i = 0; for (int num = totalChars - str.length(); i < num; i++) {
/*  490 */       sb.append(ch);
/*      */     }
/*  492 */     sb.append(str);
/*  493 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String padLeft(String str, int totalChars)
/*      */   {
/*  502 */     return padLeft(str, totalChars, ' ');
/*      */   }
/*      */   
/*      */   public static String padLeft(Object obj, int totalChars)
/*      */   {
/*  507 */     return padLeft(obj.toString(), totalChars);
/*      */   }
/*      */   
/*      */   public static String padLeft(int i, int totalChars) {
/*  511 */     return padLeft(new Integer(i), totalChars);
/*      */   }
/*      */   
/*      */   public static String padLeft(double d, int totalChars) {
/*  515 */     return padLeft(new Double(d), totalChars);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String trim(String s, int maxWidth)
/*      */   {
/*  522 */     if (s.length() <= maxWidth) {
/*  523 */       return s;
/*      */     }
/*  525 */     return s.substring(0, maxWidth);
/*      */   }
/*      */   
/*      */   public static String trim(Object obj, int maxWidth) {
/*  529 */     return trim(obj.toString(), maxWidth);
/*      */   }
/*      */   
/*      */   public static String repeat(String s, int times) {
/*  533 */     if (times == 0) {
/*  534 */       return "";
/*      */     }
/*  536 */     StringBuilder sb = new StringBuilder();
/*  537 */     for (int i = 0; i < times; i++) {
/*  538 */       sb.append(s);
/*      */     }
/*  540 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String fileNameClean(String s)
/*      */   {
/*  548 */     char[] chars = s.toCharArray();
/*  549 */     StringBuilder sb = new StringBuilder();
/*  550 */     for (int i = 0; i < chars.length; i++) {
/*  551 */       char c = chars[i];
/*  552 */       if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')) || ((c >= '0') && (c <= '9')) || (c == '_')) {
/*  553 */         sb.append(c);
/*      */       }
/*  555 */       else if ((c == ' ') || (c == '-')) {
/*  556 */         sb.append('_');
/*      */       } else {
/*  558 */         sb.append("x").append(c).append("x");
/*      */       }
/*      */     }
/*      */     
/*  562 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int nthIndex(String s, char ch, int n)
/*      */   {
/*  570 */     int index = 0;
/*  571 */     for (int i = 0; i < n; i++)
/*      */     {
/*      */ 
/*  574 */       if (index == s.length() - 1) {
/*  575 */         return -1;
/*      */       }
/*  577 */       index = s.indexOf(ch, index + 1);
/*  578 */       if (index == -1) {
/*  579 */         return -1;
/*      */       }
/*      */     }
/*  582 */     return index;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String truncate(int n, int smallestDigit, int biggestDigit)
/*      */   {
/*  592 */     int numDigits = biggestDigit - smallestDigit + 1;
/*  593 */     char[] result = new char[numDigits];
/*  594 */     for (int j = 1; j < smallestDigit; j++) {
/*  595 */       n /= 10;
/*      */     }
/*  597 */     for (int j = numDigits - 1; j >= 0; j--) {
/*  598 */       result[j] = Character.forDigit(n % 10, 10);
/*  599 */       n /= 10;
/*      */     }
/*  601 */     return new String(result);
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
/*      */   public static Map<String, String[]> argsToMap(String[] args)
/*      */   {
/*  623 */     return argsToMap(args, new HashMap());
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
/*      */   public static Map<String, String[]> argsToMap(String[] args, Map<String, Integer> flagsToNumArgs)
/*      */   {
/*  661 */     Map<String, String[]> result = new HashMap();
/*  662 */     List<String> remainingArgs = new ArrayList();
/*      */     
/*  664 */     for (int i = 0; i < args.length; i++) {
/*  665 */       String key = args[i];
/*  666 */       if (key.charAt(0) == '-') {
/*  667 */         Integer maxFlagArgs = (Integer)flagsToNumArgs.get(key);
/*  668 */         int max = maxFlagArgs == null ? 0 : maxFlagArgs.intValue();
/*  669 */         List<String> flagArgs = new ArrayList();
/*  670 */         for (int j = 0; (j < max) && (i + 1 < args.length) && (args[(i + 1)].charAt(0) != '-'); j++) {
/*  671 */           flagArgs.add(args[(i + 1)]);i++;
/*      */         }
/*  673 */         if (result.containsKey(key)) {
/*  674 */           String[] newFlagArg = new String[((String[])result.get(key)).length + ((Integer)flagsToNumArgs.get(key)).intValue()];
/*  675 */           int oldNumArgs = ((String[])result.get(key)).length;
/*  676 */           System.arraycopy(result.get(key), 0, newFlagArg, 0, oldNumArgs);
/*  677 */           for (int j = 0; j < flagArgs.size(); j++) {
/*  678 */             newFlagArg[(j + oldNumArgs)] = ((String)flagArgs.get(j));
/*      */           }
/*  680 */           result.put(key, newFlagArg);
/*      */         } else {
/*  682 */           result.put(key, flagArgs.toArray(EMPTY_STRING_ARRAY));
/*      */         }
/*      */       } else {
/*  685 */         remainingArgs.add(args[i]);
/*      */       }
/*      */     }
/*  688 */     result.put(null, remainingArgs.toArray(EMPTY_STRING_ARRAY));
/*  689 */     return result;
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
/*      */   public static Properties argsToProperties(String[] args)
/*      */   {
/*  705 */     return argsToProperties(args, java.util.Collections.emptyMap());
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
/*      */   public static Properties argsToProperties(String[] args, Map<String, Integer> flagsToNumArgs)
/*      */   {
/*  727 */     Properties result = new Properties();
/*  728 */     List<String> remainingArgs = new ArrayList();
/*      */     
/*  730 */     for (int i = 0; i < args.length; i++) {
/*  731 */       String key = args[i];
/*  732 */       if (key.charAt(0) == '-') {
/*  733 */         key = key.substring(1);
/*      */         
/*  735 */         Integer maxFlagArgs = (Integer)flagsToNumArgs.get(key);
/*  736 */         int max = maxFlagArgs == null ? 1 : maxFlagArgs.intValue();
/*  737 */         int min = maxFlagArgs == null ? 0 : maxFlagArgs.intValue();
/*  738 */         List<String> flagArgs = new ArrayList();
/*  739 */         for (int j = 0; (j < max) && (i + 1 < args.length) && ((j < min) || (args[(i + 1)].charAt(0) != '-')); j++) {
/*  740 */           flagArgs.add(args[(i + 1)]);i++;
/*      */         }
/*  742 */         if (flagArgs.isEmpty()) {
/*  743 */           result.setProperty(key, "true");
/*      */         } else {
/*  745 */           result.setProperty(key, join(flagArgs, " "));
/*  746 */           if ((key.equalsIgnoreCase("prop")) || (key.equalsIgnoreCase("props")) || (key.equalsIgnoreCase("properties"))) {
/*      */             try
/*      */             {
/*  749 */               InputStream is = new BufferedInputStream(new FileInputStream(result.getProperty(key)));
/*  750 */               result.load(is);
/*  751 */               is.close();
/*      */             } catch (IOException e) {
/*  753 */               e.printStackTrace();
/*      */             }
/*      */           }
/*      */         }
/*      */       } else {
/*  758 */         remainingArgs.add(args[i]);
/*      */       }
/*      */     }
/*  761 */     if (!remainingArgs.isEmpty()) {
/*  762 */       result.setProperty("", join(remainingArgs, " "));
/*      */     }
/*  764 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Properties stringToProperties(String str)
/*      */   {
/*  776 */     Properties result = new Properties();
/*  777 */     return stringToProperties(str, result);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Properties stringToProperties(String str, Properties props)
/*      */   {
/*  788 */     String[] propsStr = str.trim().split(",\\s*");
/*  789 */     for (int i = 0; i < propsStr.length; i++) {
/*  790 */       String term = propsStr[i];
/*  791 */       int divLoc = term.indexOf("=");
/*      */       String value;
/*      */       String key;
/*  794 */       String value; if (divLoc >= 0) {
/*  795 */         String key = term.substring(0, divLoc).trim();
/*  796 */         value = term.substring(divLoc + 1).trim();
/*      */       } else {
/*  798 */         key = term.trim();
/*  799 */         value = "true";
/*      */       }
/*  801 */       props.setProperty(key, value);
/*      */     }
/*  803 */     return props;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void printToFileLn(File file, String message, boolean append)
/*      */   {
/*  812 */     PrintWriter pw = null;
/*      */     try {
/*  814 */       FileWriter fw = new FileWriter(file, append);
/*  815 */       pw = new PrintWriter(fw);
/*  816 */       pw.println(message);
/*      */     } catch (Exception e) {
/*  818 */       System.err.println("Exception: in printToFile " + file.getAbsolutePath() + " " + message);
/*  819 */       e.printStackTrace();
/*      */     } finally {
/*  821 */       if (pw != null) {
/*  822 */         pw.flush();
/*  823 */         pw.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void printToFile(File file, String message, boolean append)
/*      */   {
/*  833 */     PrintWriter pw = null;
/*      */     try {
/*  835 */       FileWriter fw = new FileWriter(file, append);
/*  836 */       pw = new PrintWriter(fw);
/*  837 */       pw.print(message);
/*      */     } catch (Exception e) {
/*  839 */       System.err.println("Exception: in printToFile " + file.getAbsolutePath());
/*  840 */       e.printStackTrace();
/*      */     } finally {
/*  842 */       if (pw != null) {
/*  843 */         pw.flush();
/*  844 */         pw.close();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void printToFile(File file, String message)
/*      */   {
/*  855 */     printToFile(file, message, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void printToFile(String filename, String message, boolean append)
/*      */   {
/*  863 */     printToFile(new File(filename), message, append);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void printToFileLn(String filename, String message, boolean append)
/*      */   {
/*  871 */     printToFileLn(new File(filename), message, append);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void printToFile(String filename, String message)
/*      */   {
/*  880 */     printToFile(new File(filename), message, false);
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
/*      */   public static Map<String, Object> parseCommandLineArguments(String[] args)
/*      */   {
/*  897 */     return parseCommandLineArguments(args, false);
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
/*      */   public static Map<String, Object> parseCommandLineArguments(String[] args, boolean parseNumbers)
/*      */   {
/*  916 */     Map<String, Object> result = new HashMap();
/*      */     
/*  918 */     for (int i = 0; i < args.length; i++) {
/*  919 */       String key = args[i];
/*  920 */       if (key.charAt(0) == '-') {
/*  921 */         if (i + 1 < args.length) {
/*  922 */           String value = args[(i + 1)];
/*  923 */           if (value.charAt(0) != '-') {
/*  924 */             if (parseNumbers) {
/*  925 */               Object numericValue = value;
/*      */               try {
/*  927 */                 numericValue = Double.valueOf(Double.parseDouble(value));
/*      */               }
/*      */               catch (NumberFormatException e2) {}
/*  930 */               result.put(key, numericValue);
/*      */             } else {
/*  932 */               result.put(key, value);
/*      */             }
/*  934 */             i++;
/*      */           } else {
/*  936 */             result.put(key, null);
/*      */           }
/*      */         } else {
/*  939 */           result.put(key, null);
/*      */         }
/*      */       }
/*      */     }
/*  943 */     return result;
/*      */   }
/*      */   
/*      */   public static String stripNonAlphaNumerics(String orig) {
/*  947 */     StringBuilder sb = new StringBuilder();
/*      */     
/*  949 */     for (int i = 0; i < orig.length(); i++) {
/*  950 */       char c = orig.charAt(i);
/*  951 */       if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9'))) {
/*  952 */         sb.append(c);
/*      */       }
/*      */     }
/*  955 */     return sb.toString();
/*      */   }
/*      */   
/*      */   public static String stripSGML(String orig) {
/*  959 */     Pattern sgmlPattern = Pattern.compile("<.*?>", 32);
/*  960 */     Matcher sgmlMatcher = sgmlPattern.matcher(orig);
/*  961 */     return sgmlMatcher.replaceAll("");
/*      */   }
/*      */   
/*      */   public static void printStringOneCharPerLine(String s) {
/*  965 */     for (int i = 0; i < s.length(); i++) {
/*  966 */       int c = s.charAt(i);
/*  967 */       System.out.println(c + " '" + (char)c + "' ");
/*      */     }
/*      */   }
/*      */   
/*      */   public static String escapeString(String s, char[] charsToEscape, char escapeChar) {
/*  972 */     StringBuilder result = new StringBuilder();
/*  973 */     for (int i = 0; i < s.length(); i++) {
/*  974 */       char c = s.charAt(i);
/*  975 */       if (c == escapeChar) {
/*  976 */         result.append(escapeChar);
/*      */       } else {
/*  978 */         for (int j = 0; j < charsToEscape.length; j++) {
/*  979 */           if (c == charsToEscape[j]) {
/*  980 */             result.append(escapeChar);
/*  981 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  985 */       result.append(c);
/*      */     }
/*  987 */     return result.toString();
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
/*      */   public static String[] splitOnCharWithQuoting(String s, char splitChar, char quoteChar, char escapeChar)
/*      */   {
/* 1004 */     List<String> result = new ArrayList();
/* 1005 */     int i = 0;
/* 1006 */     int length = s.length();
/* 1007 */     StringBuilder b = new StringBuilder();
/* 1008 */     while (i < length) {
/* 1009 */       char curr = s.charAt(i);
/* 1010 */       if (curr == splitChar)
/*      */       {
/* 1012 */         if (b.length() > 0) {
/* 1013 */           result.add(b.toString());
/* 1014 */           b = new StringBuilder();
/*      */         }
/* 1016 */         i++;
/* 1017 */       } else { if (curr == quoteChar)
/*      */         {
/* 1019 */           i++;
/* 1020 */           while (i < length) {
/* 1021 */             curr = s.charAt(i);
/* 1022 */             if (curr == escapeChar) {
/* 1023 */               b.append(s.charAt(i + 1));
/* 1024 */               i += 2;
/* 1025 */             } else { if (curr == quoteChar) {
/* 1026 */                 i++;
/* 1027 */                 break;
/*      */               }
/* 1029 */               b.append(s.charAt(i));
/* 1030 */               i++;
/*      */             }
/*      */           }
/*      */         }
/* 1034 */         b.append(curr);
/* 1035 */         i++;
/*      */       }
/*      */     }
/* 1038 */     if (b.length() > 0) {
/* 1039 */       result.add(b.toString());
/*      */     }
/* 1041 */     return (String[])result.toArray(EMPTY_STRING_ARRAY);
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
/*      */   public static int longestCommonSubstring(String s, String t)
/*      */   {
/* 1062 */     int n = s.length();
/* 1063 */     int m = t.length();
/* 1064 */     if (n == 0) {
/* 1065 */       return 0;
/*      */     }
/* 1067 */     if (m == 0) {
/* 1068 */       return 0;
/*      */     }
/* 1070 */     int[][] d = new int[n + 1][m + 1];
/*      */     
/* 1072 */     for (int i = 0; i <= n; i++) {
/* 1073 */       d[i][0] = 0;
/*      */     }
/* 1075 */     for (int j = 0; j <= m; j++) {
/* 1076 */       d[0][j] = 0;
/*      */     }
/*      */     
/* 1079 */     for (i = 1; i <= n; i++) {
/* 1080 */       char s_i = s.charAt(i - 1);
/*      */       
/* 1082 */       for (j = 1; j <= m; j++) {
/* 1083 */         char t_j = t.charAt(j - 1);
/*      */         
/*      */ 
/*      */ 
/* 1087 */         if (s_i == t_j) {
/* 1088 */           d[i][j] = SloppyMath.max(d[(i - 1)][j], d[i][(j - 1)], d[(i - 1)][(j - 1)] + 1);
/*      */         } else {
/* 1090 */           d[i][j] = Math.max(d[(i - 1)][j], d[i][(j - 1)]);
/*      */         }
/*      */       }
/*      */     }
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
/* 1113 */     return d[n][m];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int longestCommonContiguousSubstring(String s, String t)
/*      */   {
/* 1123 */     if ((s.length() == 0) || (t.length() == 0)) {
/* 1124 */       return 0;
/*      */     }
/* 1126 */     int M = s.length();
/* 1127 */     int N = t.length();
/* 1128 */     int[][] d = new int[M + 1][N + 1];
/* 1129 */     for (int j = 0; j <= N; j++) {
/* 1130 */       d[0][j] = 0;
/*      */     }
/* 1132 */     for (int i = 0; i <= M; i++) {
/* 1133 */       d[i][0] = 0;
/*      */     }
/*      */     
/* 1136 */     int max = 0;
/* 1137 */     for (int i = 1; i <= M; i++) {
/* 1138 */       for (int j = 1; j <= N; j++) {
/* 1139 */         if (s.charAt(i - 1) == t.charAt(j - 1)) {
/* 1140 */           d[i][j] = (d[(i - 1)][(j - 1)] + 1);
/*      */         } else {
/* 1142 */           d[i][j] = 0;
/*      */         }
/*      */         
/* 1145 */         if (d[i][j] > max) {
/* 1146 */           max = d[i][j];
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1151 */     return max;
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
/*      */   public static int editDistance(String s, String t)
/*      */   {
/* 1167 */     int n = s.length();
/* 1168 */     int m = t.length();
/* 1169 */     if (n == 0) {
/* 1170 */       return m;
/*      */     }
/* 1172 */     if (m == 0) {
/* 1173 */       return n;
/*      */     }
/* 1175 */     int[][] d = new int[n + 1][m + 1];
/*      */     
/* 1177 */     for (int i = 0; i <= n; i++) {
/* 1178 */       d[i][0] = i;
/*      */     }
/* 1180 */     for (int j = 0; j <= m; j++) {
/* 1181 */       d[0][j] = j;
/*      */     }
/*      */     
/* 1184 */     for (i = 1; i <= n; i++) {
/* 1185 */       char s_i = s.charAt(i - 1);
/*      */       
/* 1187 */       for (j = 1; j <= m; j++) {
/* 1188 */         char t_j = t.charAt(j - 1);
/*      */         int cost;
/* 1190 */         int cost; if (s_i == t_j) {
/* 1191 */           cost = 0;
/*      */         } else {
/* 1193 */           cost = 1;
/*      */         }
/*      */         
/* 1196 */         d[i][j] = SloppyMath.min(d[(i - 1)][j] + 1, d[i][(j - 1)] + 1, d[(i - 1)][(j - 1)] + cost);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1201 */     return d[n][m];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String pennPOSToWordnetPOS(String s)
/*      */   {
/* 1211 */     if (s.matches("NN|NNP|NNS|NNPS")) {
/* 1212 */       return "noun";
/*      */     }
/* 1214 */     if (s.matches("VB|VBD|VBG|VBN|VBZ|VBP|MD")) {
/* 1215 */       return "verb";
/*      */     }
/* 1217 */     if (s.matches("JJ|JJR|JJS|CD")) {
/* 1218 */       return "adjective";
/*      */     }
/* 1220 */     if (s.matches("RB|RBR|RBS|RP|WRB")) {
/* 1221 */       return "adverb";
/*      */     }
/* 1223 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getShortClassName(Object o)
/*      */   {
/* 1234 */     String name = o.getClass().getName();
/* 1235 */     int index = name.lastIndexOf(".");
/* 1236 */     if (index >= 0) {
/* 1237 */       name = name.substring(index + 1);
/*      */     }
/* 1239 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String capitalize(String s)
/*      */   {
/* 1250 */     if (Character.isLowerCase(s.charAt(0))) {
/* 1251 */       return Character.toUpperCase(s.charAt(0)) + s.substring(1);
/*      */     }
/* 1253 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isCapitalized(String s)
/*      */   {
/* 1265 */     return Character.isUpperCase(s.charAt(0));
/*      */   }
/*      */   
/*      */   public static String searchAndReplace(String text, String from, String to) {
/* 1269 */     from = escapeString(from, new char[] { '.', '[', ']', '\\' }, '\\');
/* 1270 */     Pattern p = Pattern.compile(from);
/* 1271 */     Matcher m = p.matcher(text);
/* 1272 */     return m.replaceAll(to);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String makeHTMLTable(String[][] table, String[] rowLabels, String[] colLabels)
/*      */   {
/* 1281 */     StringBuilder buff = new StringBuilder();
/* 1282 */     buff.append("<table class=\"auto\" border=\"1\" cellspacing=\"0\">\n");
/*      */     
/* 1284 */     buff.append("<tr>\n");
/* 1285 */     buff.append("<td></td>\n");
/* 1286 */     for (int j = 0; j < table[0].length; j++) {
/* 1287 */       buff.append("<td class=\"label\">").append(colLabels[j]).append("</td>\n");
/*      */     }
/* 1289 */     buff.append("</tr>\n");
/*      */     
/* 1291 */     for (int i = 0; i < table.length; i++)
/*      */     {
/* 1293 */       buff.append("<tr>\n");
/* 1294 */       buff.append("<td class=\"label\">").append(rowLabels[i]).append("</td>\n");
/* 1295 */       for (int j = 0; j < table[i].length; j++) {
/* 1296 */         buff.append("<td class=\"data\">");
/* 1297 */         buff.append(table[i][j] != null ? table[i][j] : "");
/* 1298 */         buff.append("</td>\n");
/*      */       }
/* 1300 */       buff.append("</tr>\n");
/*      */     }
/* 1302 */     buff.append("</table>");
/* 1303 */     return buff.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1311 */     String[] s = { "there once was a man", "this one is a manic", "hey there", "there once was a mane", "once in a manger.", "where is one match?" };
/* 1312 */     for (int i = 0; i < 6; i++) {
/* 1313 */       for (int j = 0; j < 6; j++) {
/* 1314 */         System.out.println("s1: " + s[i]);
/* 1315 */         System.out.println("s2: " + s[j]);
/* 1316 */         System.out.println("edit distance: " + editDistance(s[i], s[j]));
/* 1317 */         System.out.println("LCS:           " + longestCommonSubstring(s[i], s[j]));
/* 1318 */         System.out.println("LCCS:          " + longestCommonContiguousSubstring(s[i], s[j]));
/* 1319 */         System.out.println();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static String toAscii(String s) {
/* 1325 */     StringBuilder b = new StringBuilder();
/* 1326 */     for (int i = 0; i < s.length(); i++) {
/* 1327 */       char c = s.charAt(i);
/* 1328 */       if (c > '') {
/* 1329 */         String result = "?";
/* 1330 */         if ((c >= 'À') && (c <= 'Å')) {
/* 1331 */           result = "A";
/* 1332 */         } else if (c == 'Æ') {
/* 1333 */           result = "AE";
/* 1334 */         } else if (c == 'Ç') {
/* 1335 */           result = "C";
/* 1336 */         } else if ((c >= 'È') && (c <= 'Ë')) {
/* 1337 */           result = "E";
/* 1338 */         } else if ((c >= 'Ì') && (c <= 'Ï')) {
/* 1339 */           result = "F";
/* 1340 */         } else if (c == 'Ð') {
/* 1341 */           result = "D";
/* 1342 */         } else if (c == 'Ñ') {
/* 1343 */           result = "N";
/* 1344 */         } else if ((c >= 'Ò') && (c <= 'Ö')) {
/* 1345 */           result = "O";
/* 1346 */         } else if (c == '×') {
/* 1347 */           result = "x";
/* 1348 */         } else if (c == 'Ø') {
/* 1349 */           result = "O";
/* 1350 */         } else if ((c >= 'Ù') && (c <= 'Ü')) {
/* 1351 */           result = "U";
/* 1352 */         } else if (c == 'Ý') {
/* 1353 */           result = "Y";
/* 1354 */         } else if ((c >= 'à') && (c <= 'å')) {
/* 1355 */           result = "a";
/* 1356 */         } else if (c == 'æ') {
/* 1357 */           result = "ae";
/* 1358 */         } else if (c == 'ç') {
/* 1359 */           result = "c";
/* 1360 */         } else if ((c >= 'è') && (c <= 'ë')) {
/* 1361 */           result = "e";
/* 1362 */         } else if ((c >= 'ì') && (c <= 'ï')) {
/* 1363 */           result = "i";
/* 1364 */         } else if (c == 'ñ') {
/* 1365 */           result = "n";
/* 1366 */         } else if ((c >= 'ò') && (c <= 'ø')) {
/* 1367 */           result = "o";
/* 1368 */         } else if ((c >= 'ù') && (c <= 'ü')) {
/* 1369 */           result = "u";
/* 1370 */         } else if ((c >= 'ý') && (c <= 'ÿ')) {
/* 1371 */           result = "y";
/* 1372 */         } else if ((c >= '‘') && (c <= '’')) {
/* 1373 */           result = "'";
/* 1374 */         } else if ((c >= '“') && (c <= '„')) {
/* 1375 */           result = "\"";
/* 1376 */         } else if ((c >= 'ȓ') && (c <= '—')) {
/* 1377 */           result = "-";
/* 1378 */         } else if ((c >= '¢') && (c <= '¥')) {
/* 1379 */           result = "$";
/* 1380 */         } else if (c == '…') {
/* 1381 */           result = ".";
/*      */         }
/* 1383 */         b.append(result);
/*      */       } else {
/* 1385 */         b.append(c);
/*      */       }
/*      */     }
/* 1388 */     return b.toString();
/*      */   }
/*      */   
/*      */   public static String toCSVString(String[] fields)
/*      */   {
/* 1393 */     StringBuilder b = new StringBuilder();
/* 1394 */     for (int i = 0; i < fields.length; i++) {
/* 1395 */       if (b.length() > 0) {
/* 1396 */         b.append(",");
/*      */       }
/* 1398 */       String field = escapeString(fields[i], new char[] { '"' }, '"');
/* 1399 */       b.append("\"").append(field).append("\"");
/*      */     }
/* 1401 */     return b.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String tr(String input, String from, String to)
/*      */   {
/* 1410 */     StringBuilder sb = new StringBuilder(input);
/* 1411 */     int len = sb.length();
/* 1412 */     for (int i = 0; i < len; i++) {
/* 1413 */       int ind = from.indexOf(input.charAt(i));
/* 1414 */       if (ind >= 0) {
/* 1415 */         sb.setCharAt(i, to.charAt(ind));
/*      */       }
/*      */     }
/* 1418 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String chomp(String s)
/*      */   {
/* 1425 */     int l_1 = s.length() - 1;
/* 1426 */     if (s.charAt(l_1) == '\n') {
/* 1427 */       return s.substring(0, l_1);
/*      */     }
/* 1429 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String chomp(Object o)
/*      */   {
/* 1437 */     return chomp(o.toString());
/*      */   }
/*      */   
/*      */   public static String toInvocationString(String cls, String[] args) {
/* 1441 */     StringBuilder sb = new StringBuilder();
/* 1442 */     sb.append(cls).append(" invoked on ").append(new Date());
/* 1443 */     sb.append(" with arguments: \n ");
/* 1444 */     for (int i = 0; i < args.length; i++) {
/* 1445 */       sb.append(" ").append(args[i]);
/*      */     }
/* 1447 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getBaseName(String fileName)
/*      */   {
/* 1456 */     return getBaseName(fileName, "");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getBaseName(String fileName, String suffix)
/*      */   {
/* 1467 */     String[] elts = fileName.split("/");
/* 1468 */     String lastElt = elts[(elts.length - 1)];
/* 1469 */     if (lastElt.endsWith(suffix)) {
/* 1470 */       lastElt = lastElt.substring(0, lastElt.length() - suffix.length());
/*      */     }
/* 1472 */     return lastElt;
/*      */   }
/*      */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\StringUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */