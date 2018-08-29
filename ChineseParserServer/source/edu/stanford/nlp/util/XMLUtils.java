/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLUtils
/*     */ {
/*  32 */   public static final Set<String> breakingTags = new HashSet(Arrays.asList(new String[] { "blockquote", "br", "div", "h1", "h2", "h3", "h4", "h5", "h6", "hr", "li", "ol", "p", "pre", "ul", "tr", "td" }));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String stripTags(Reader r, List<Integer> mapBack, boolean markLineBreaks)
/*     */   {
/*  41 */     if (mapBack != null) {
/*  42 */       mapBack.clear();
/*     */     }
/*  44 */     StringBuilder result = new StringBuilder();
/*     */     
/*     */ 
/*  47 */     int position = 0;
/*     */     try {
/*     */       for (;;) {
/*  50 */         String text = readUntilTag(r);
/*  51 */         if (text.length() > 0)
/*     */         {
/*  53 */           for (int i = 0; i < text.length(); i++) {
/*  54 */             result.append(text.charAt(i));
/*  55 */             if (mapBack != null) {
/*  56 */               mapBack.add(new Integer(position + i));
/*     */             }
/*     */           }
/*  59 */           position += text.length();
/*     */         }
/*     */         
/*  62 */         String tag = readTag(r);
/*  63 */         if (tag == null) {
/*     */           break;
/*     */         }
/*  66 */         if ((markLineBreaks) && (isBreaking(parseTag(tag)))) {
/*  67 */           result.append("\n");
/*  68 */           if (mapBack != null) {
/*  69 */             mapBack.add(new Integer(-position));
/*     */           }
/*     */         }
/*  72 */         position += tag.length();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/*  76 */       System.err.println("Error reading string");
/*  77 */       e.printStackTrace();
/*     */     }
/*  79 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static boolean isBreaking(String tag) {
/*  83 */     return breakingTags.contains(tag);
/*     */   }
/*     */   
/*     */   public static boolean isBreaking(XMLTag tag) {
/*  87 */     return breakingTags.contains(tag.name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String readUntilTag(Reader r)
/*     */     throws IOException
/*     */   {
/*  96 */     if (!r.ready()) {
/*  97 */       return "";
/*     */     }
/*  99 */     StringBuilder b = new StringBuilder();
/* 100 */     int c = r.read();
/* 101 */     while ((c >= 0) && (c != 60)) {
/* 102 */       b.append((char)c);
/* 103 */       c = r.read();
/*     */     }
/* 105 */     return b.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static XMLTag readAndParseTag(Reader r)
/*     */     throws Exception
/*     */   {
/* 112 */     String s = readTag(r);
/* 113 */     if (s == null) {
/* 114 */       return null;
/*     */     }
/* 116 */     return new XMLTag(s);
/*     */   }
/*     */   
/*     */   public static String unescapeStringForXML(String s) {
/* 120 */     Pattern p = Pattern.compile("\\&.+?;");
/* 121 */     StringBuilder result = new StringBuilder();
/* 122 */     Matcher m = p.matcher(s);
/* 123 */     int end = 0;
/* 124 */     while (m.find()) {
/* 125 */       int start = m.start();
/* 126 */       result.append(s.substring(end, start));
/* 127 */       end = m.end();
/* 128 */       result.append(translate(s.substring(start, end)));
/*     */     }
/* 130 */     result.append(s.substring(end, s.length()));
/* 131 */     return result.toString();
/*     */   }
/*     */   
/*     */   private static char translate(String s) {
/* 135 */     if (s.equals("&amp;"))
/* 136 */       return '&';
/* 137 */     if ((s.equals("&lt;")) || (s.equals("&Lt;")))
/* 138 */       return '<';
/* 139 */     if ((s.equals("&gt;")) || (s.equals("&Gt;")))
/* 140 */       return '>';
/* 141 */     if (s.equals("&quot;"))
/* 142 */       return '"';
/* 143 */     if (s.equals("&apos;"))
/* 144 */       return '\'';
/* 145 */     if ((s.equals("&ast;")) || (s.equals("&sharp;")))
/* 146 */       return '-';
/* 147 */     if (s.equals("&equals;"))
/* 148 */       return '=';
/* 149 */     if (s.equals("&nbsp;"))
/* 150 */       return ' ';
/* 151 */     if (s.equals("&iexcl;"))
/* 152 */       return '¡';
/* 153 */     if ((s.equals("&cent;")) || (s.equals("&shilling;")))
/* 154 */       return '¢';
/* 155 */     if (s.equals("&pound;"))
/* 156 */       return '£';
/* 157 */     if (s.equals("&curren;"))
/* 158 */       return '¤';
/* 159 */     if (s.equals("&yen;"))
/* 160 */       return '¥';
/* 161 */     if (s.equals("&brvbar;"))
/* 162 */       return '¦';
/* 163 */     if (s.equals("&sect;"))
/* 164 */       return '§';
/* 165 */     if (s.equals("&uml;"))
/* 166 */       return '¨';
/* 167 */     if (s.equals("&copy;"))
/* 168 */       return '©';
/* 169 */     if (s.equals("&ordf;"))
/* 170 */       return 'ª';
/* 171 */     if (s.equals("&laquo; "))
/* 172 */       return '«';
/* 173 */     if (s.equals("&not;"))
/* 174 */       return '¬';
/* 175 */     if (s.equals("&shy; "))
/* 176 */       return '­';
/* 177 */     if (s.equals("&reg;"))
/* 178 */       return '®';
/* 179 */     if (s.equals("&macr;"))
/* 180 */       return '¯';
/* 181 */     if (s.equals("&deg;"))
/* 182 */       return '°';
/* 183 */     if (s.equals("&plusmn;"))
/* 184 */       return '±';
/* 185 */     if (s.equals("&sup2;"))
/* 186 */       return '²';
/* 187 */     if (s.equals("&sup3;"))
/* 188 */       return '³';
/* 189 */     if (s.equals("&acute;"))
/* 190 */       return '´';
/* 191 */     if (s.equals("&micro;"))
/* 192 */       return 'µ';
/* 193 */     if (s.equals("&middot;"))
/* 194 */       return '·';
/* 195 */     if (s.equals("&cedil;"))
/* 196 */       return '¸';
/* 197 */     if (s.equals("&sup1;"))
/* 198 */       return '¹';
/* 199 */     if (s.equals("&ordm;"))
/* 200 */       return 'º';
/* 201 */     if (s.equals("&raquo;"))
/* 202 */       return '»';
/* 203 */     if (s.equals("&frac14; "))
/* 204 */       return '¼';
/* 205 */     if (s.equals("&frac12;"))
/* 206 */       return '½';
/* 207 */     if (s.equals("&frac34; "))
/* 208 */       return '¾';
/* 209 */     if (s.equals("&iquest;"))
/* 210 */       return '¿';
/* 211 */     if (s.equals("&Agrave;"))
/* 212 */       return 'À';
/* 213 */     if (s.equals("&Aacute;"))
/* 214 */       return 'Á';
/* 215 */     if (s.equals("&Acirc;"))
/* 216 */       return 'Â';
/* 217 */     if (s.equals("&Atilde;"))
/* 218 */       return 'Ã';
/* 219 */     if (s.equals("&Auml;"))
/* 220 */       return 'Ä';
/* 221 */     if (s.equals("&Aring;"))
/* 222 */       return 'Å';
/* 223 */     if (s.equals("&AElig;"))
/* 224 */       return 'Æ';
/* 225 */     if (s.equals("&Ccedil;"))
/* 226 */       return 'Ç';
/* 227 */     if (s.equals("&Egrave;"))
/* 228 */       return 'È';
/* 229 */     if (s.equals("&Eacute;"))
/* 230 */       return 'É';
/* 231 */     if (s.equals("&Ecirc;"))
/* 232 */       return 'Ê';
/* 233 */     if (s.equals("&Euml;"))
/* 234 */       return 'Ë';
/* 235 */     if (s.equals("&Igrave;"))
/* 236 */       return 'Ì';
/* 237 */     if (s.equals("&Iacute;"))
/* 238 */       return 'Í';
/* 239 */     if (s.equals("&Icirc;"))
/* 240 */       return 'Î';
/* 241 */     if (s.equals("&Iuml;"))
/* 242 */       return 'Ï';
/* 243 */     if (s.equals("&ETH;"))
/* 244 */       return 'Ð';
/* 245 */     if (s.equals("&Ntilde;"))
/* 246 */       return 'Ñ';
/* 247 */     if (s.equals("&Ograve;"))
/* 248 */       return 'Ò';
/* 249 */     if (s.equals("&Oacute;"))
/* 250 */       return 'Ó';
/* 251 */     if (s.equals("&Ocirc;"))
/* 252 */       return 'Ô';
/* 253 */     if (s.equals("&Otilde;"))
/* 254 */       return 'Õ';
/* 255 */     if (s.equals("&Ouml;"))
/* 256 */       return 'Ö';
/* 257 */     if (s.equals("&times;"))
/* 258 */       return '×';
/* 259 */     if (s.equals("&Oslash;"))
/* 260 */       return 'Ø';
/* 261 */     if (s.equals("&Ugrave;"))
/* 262 */       return 'Ù';
/* 263 */     if (s.equals("&Uacute;"))
/* 264 */       return 'Ú';
/* 265 */     if (s.equals("&Ucirc;"))
/* 266 */       return 'Û';
/* 267 */     if (s.equals("&Uuml;"))
/* 268 */       return 'Ü';
/* 269 */     if (s.equals("&Yacute;"))
/* 270 */       return 'Ý';
/* 271 */     if (s.equals("&THORN;"))
/* 272 */       return 'Þ';
/* 273 */     if (s.equals("&szlig;"))
/* 274 */       return 'ß';
/* 275 */     if (s.equals("&agrave;"))
/* 276 */       return 'à';
/* 277 */     if (s.equals("&aacute;"))
/* 278 */       return 'á';
/* 279 */     if (s.equals("&acirc;"))
/* 280 */       return 'â';
/* 281 */     if (s.equals("&atilde;"))
/* 282 */       return 'ã';
/* 283 */     if (s.equals("&auml;"))
/* 284 */       return 'ä';
/* 285 */     if (s.equals("&aring;"))
/* 286 */       return 'å';
/* 287 */     if (s.equals("&aelig;"))
/* 288 */       return 'æ';
/* 289 */     if (s.equals("&ccedil;"))
/* 290 */       return 'ç';
/* 291 */     if (s.equals("&egrave;"))
/* 292 */       return 'è';
/* 293 */     if (s.equals("&eacute;"))
/* 294 */       return 'é';
/* 295 */     if (s.equals("&ecirc;"))
/* 296 */       return 'ê';
/* 297 */     if (s.equals("&euml; "))
/* 298 */       return 'ë';
/* 299 */     if (s.equals("&igrave;"))
/* 300 */       return 'ì';
/* 301 */     if (s.equals("&iacute;"))
/* 302 */       return 'í';
/* 303 */     if (s.equals("&icirc;"))
/* 304 */       return 'î';
/* 305 */     if (s.equals("&iuml;"))
/* 306 */       return 'ï';
/* 307 */     if (s.equals("&eth;"))
/* 308 */       return 'ð';
/* 309 */     if (s.equals("&ntilde;"))
/* 310 */       return 'ñ';
/* 311 */     if (s.equals("&ograve;"))
/* 312 */       return 'ò';
/* 313 */     if (s.equals("&oacute;"))
/* 314 */       return 'ó';
/* 315 */     if (s.equals("&ocirc;"))
/* 316 */       return 'ô';
/* 317 */     if (s.equals("&otilde;"))
/* 318 */       return 'õ';
/* 319 */     if (s.equals("&ouml;"))
/* 320 */       return 'ö';
/* 321 */     if (s.equals("&divide;"))
/* 322 */       return '÷';
/* 323 */     if (s.equals("&oslash;"))
/* 324 */       return 'ø';
/* 325 */     if (s.equals("&ugrave;"))
/* 326 */       return 'ù';
/* 327 */     if (s.equals("&uacute;"))
/* 328 */       return 'ú';
/* 329 */     if (s.equals("&ucirc;"))
/* 330 */       return 'û';
/* 331 */     if (s.equals("&uuml;"))
/* 332 */       return 'ü';
/* 333 */     if (s.equals("&yacute;"))
/* 334 */       return 'ý';
/* 335 */     if (s.equals("&thorn;"))
/* 336 */       return 'þ';
/* 337 */     if (s.equals("&yuml;"))
/* 338 */       return 'ÿ';
/* 339 */     if (s.equals("&OElig;"))
/* 340 */       return 'Œ';
/* 341 */     if (s.equals("&oelig;"))
/* 342 */       return 'œ';
/* 343 */     if (s.equals("&Scaron;"))
/* 344 */       return 'Š';
/* 345 */     if (s.equals("&scaron;"))
/* 346 */       return 'š';
/* 347 */     if (s.equals("&Yuml;"))
/* 348 */       return 'Ÿ';
/* 349 */     if (s.equals("&circ;"))
/* 350 */       return 'ˆ';
/* 351 */     if (s.equals("&tilde;"))
/* 352 */       return '˜';
/* 353 */     if (s.equals("&lrm;"))
/* 354 */       return '‎';
/* 355 */     if (s.equals("&rlm;"))
/* 356 */       return '‏';
/* 357 */     if (s.equals("&ndash;"))
/* 358 */       return '–';
/* 359 */     if (s.equals("&mdash;"))
/* 360 */       return '—';
/* 361 */     if (s.equals("&lsquo;"))
/* 362 */       return '‘';
/* 363 */     if (s.equals("&rsquo;"))
/* 364 */       return '’';
/* 365 */     if (s.equals("&sbquo;"))
/* 366 */       return '‚';
/* 367 */     if ((s.equals("&ldquo;")) || (s.equals("&bquo;")) || (s.equals("&bq;")))
/* 368 */       return '“';
/* 369 */     if ((s.equals("&rdquo;")) || (s.equals("&equo;")))
/* 370 */       return '”';
/* 371 */     if (s.equals("&bdquo;"))
/* 372 */       return '„';
/* 373 */     if (s.equals("&sim;"))
/* 374 */       return '∼';
/* 375 */     if (s.equals("&radic;"))
/* 376 */       return '√';
/* 377 */     if (s.equals("&le;"))
/* 378 */       return '≤';
/* 379 */     if (s.equals("&ge;"))
/* 380 */       return '≥';
/* 381 */     if (s.equals("&larr;"))
/* 382 */       return '←';
/* 383 */     if (s.equals("&darr;"))
/* 384 */       return '↓';
/* 385 */     if (s.equals("&rarr;"))
/* 386 */       return '→';
/* 387 */     if (s.equals("&hellip;"))
/* 388 */       return '…';
/* 389 */     if (s.equals("&prime;"))
/* 390 */       return '′';
/* 391 */     if ((s.equals("&Prime;")) || (s.equals("&ins;")))
/* 392 */       return '″';
/* 393 */     if (s.equals("&trade;"))
/* 394 */       return '™';
/* 395 */     if ((s.equals("&Alpha;")) || (s.equals("&Agr;")))
/* 396 */       return 'Α';
/* 397 */     if ((s.equals("&Beta;")) || (s.equals("&Bgr;")))
/* 398 */       return 'Β';
/* 399 */     if ((s.equals("&Gamma;")) || (s.equals("&Ggr;")))
/* 400 */       return 'Γ';
/* 401 */     if ((s.equals("&Delta;")) || (s.equals("&Dgr;")))
/* 402 */       return 'Δ';
/* 403 */     if ((s.equals("&Epsilon;")) || (s.equals("&Egr;")))
/* 404 */       return 'Ε';
/* 405 */     if ((s.equals("&Zeta;")) || (s.equals("&Zgr;")))
/* 406 */       return 'Ζ';
/* 407 */     if (s.equals("&Eta;"))
/* 408 */       return 'Η';
/* 409 */     if ((s.equals("&Theta;")) || (s.equals("&THgr;")))
/* 410 */       return 'Θ';
/* 411 */     if ((s.equals("&Iota;")) || (s.equals("&Igr;")))
/* 412 */       return 'Ι';
/* 413 */     if ((s.equals("&Kappa;")) || (s.equals("&Kgr;")))
/* 414 */       return 'Κ';
/* 415 */     if ((s.equals("&Lambda;")) || (s.equals("&Lgr;")))
/* 416 */       return 'Λ';
/* 417 */     if ((s.equals("&Mu;")) || (s.equals("&Mgr;")))
/* 418 */       return 'Μ';
/* 419 */     if ((s.equals("&Nu;")) || (s.equals("&Ngr;")))
/* 420 */       return 'Ν';
/* 421 */     if ((s.equals("&Xi;")) || (s.equals("&Xgr;")))
/* 422 */       return 'Ξ';
/* 423 */     if ((s.equals("&Omicron;")) || (s.equals("&Ogr;")))
/* 424 */       return 'Ο';
/* 425 */     if ((s.equals("&Pi;")) || (s.equals("&Pgr;")))
/* 426 */       return 'Π';
/* 427 */     if ((s.equals("&Rho;")) || (s.equals("&Rgr;")))
/* 428 */       return 'Ρ';
/* 429 */     if ((s.equals("&Sigma;")) || (s.equals("&Sgr;")))
/* 430 */       return 'Σ';
/* 431 */     if ((s.equals("&Tau;")) || (s.equals("&Tgr;")))
/* 432 */       return 'Τ';
/* 433 */     if ((s.equals("&Upsilon;")) || (s.equals("&Ugr;")))
/* 434 */       return 'Υ';
/* 435 */     if ((s.equals("&Phi;")) || (s.equals("&PHgr;")))
/* 436 */       return 'Φ';
/* 437 */     if ((s.equals("&Chi;")) || (s.equals("&KHgr;")))
/* 438 */       return 'Χ';
/* 439 */     if ((s.equals("&Psi;")) || (s.equals("&PSgr;")))
/* 440 */       return 'Ψ';
/* 441 */     if ((s.equals("&Omega;")) || (s.equals("&OHgr;")))
/* 442 */       return 'Ω';
/* 443 */     if ((s.equals("&alpha;")) || (s.equals("&agr;")))
/* 444 */       return 'α';
/* 445 */     if ((s.equals("&beta;")) || (s.equals("&bgr;")))
/* 446 */       return 'β';
/* 447 */     if ((s.equals("&gamma;")) || (s.equals("&ggr;")))
/* 448 */       return 'γ';
/* 449 */     if ((s.equals("&delta;")) || (s.equals("&dgr;")))
/* 450 */       return 'δ';
/* 451 */     if ((s.equals("&epsilon;")) || (s.equals("&egr;")))
/* 452 */       return 'ε';
/* 453 */     if ((s.equals("&zeta;")) || (s.equals("&zgr;")))
/* 454 */       return 'ζ';
/* 455 */     if ((s.equals("&eta;")) || (s.equals("&eegr;")))
/* 456 */       return 'η';
/* 457 */     if ((s.equals("&theta;")) || (s.equals("&thgr;")))
/* 458 */       return 'θ';
/* 459 */     if ((s.equals("&iota;")) || (s.equals("&igr;")))
/* 460 */       return 'ι';
/* 461 */     if ((s.equals("&kappa;")) || (s.equals("&kgr;")))
/* 462 */       return 'κ';
/* 463 */     if ((s.equals("&lambda;")) || (s.equals("&lgr;")))
/* 464 */       return 'λ';
/* 465 */     if ((s.equals("&mu;")) || (s.equals("&mgr;")))
/* 466 */       return 'μ';
/* 467 */     if ((s.equals("&nu;")) || (s.equals("&ngr;")))
/* 468 */       return 'ν';
/* 469 */     if ((s.equals("&xi;")) || (s.equals("&xgr;")))
/* 470 */       return 'ξ';
/* 471 */     if ((s.equals("&omicron;")) || (s.equals("&ogr;")))
/* 472 */       return 'ο';
/* 473 */     if ((s.equals("&pi;")) || (s.equals("&pgr;")))
/* 474 */       return 'π';
/* 475 */     if ((s.equals("&rho;")) || (s.equals("&rgr;")))
/* 476 */       return 'ρ';
/* 477 */     if ((s.equals("&sigma;")) || (s.equals("&sgr;")))
/* 478 */       return 'σ';
/* 479 */     if ((s.equals("&tau;")) || (s.equals("&tgr;")))
/* 480 */       return 'τ';
/* 481 */     if ((s.equals("&upsilon;")) || (s.equals("&ugr;")))
/* 482 */       return 'υ';
/* 483 */     if ((s.equals("&phi;")) || (s.equals("&phgr;")))
/* 484 */       return 'φ';
/* 485 */     if ((s.equals("&chi;")) || (s.equals("&khgr;")))
/* 486 */       return 'χ';
/* 487 */     if ((s.equals("&psi;")) || (s.equals("&psgr;")))
/* 488 */       return 'ψ';
/* 489 */     if ((s.equals("&omega;")) || (s.equals("&ohgr;")))
/* 490 */       return 'ω';
/* 491 */     if (s.equals("&bull;"))
/* 492 */       return '•';
/* 493 */     if (s.equals("&percnt;"))
/* 494 */       return '%';
/* 495 */     if (s.equals("&plus;"))
/* 496 */       return '+';
/* 497 */     if (s.equals("&dash;"))
/* 498 */       return '-';
/* 499 */     if ((s.equals("&abreve;")) || (s.equals("&amacr;")) || (s.equals("&ape;")) || (s.equals("&aogon;")) || (s.equals("&aring;")))
/* 500 */       return 'a';
/* 501 */     if (s.equals("&Amacr;"))
/* 502 */       return 'A';
/* 503 */     if ((s.equals("&cacute;")) || (s.equals("&ccaron;")) || (s.equals("&ccirc;")))
/* 504 */       return 'c';
/* 505 */     if (s.equals("&Ccaron;"))
/* 506 */       return 'C';
/* 507 */     if (s.equals("&dcaron;"))
/* 508 */       return 'd';
/* 509 */     if ((s.equals("&ecaron;")) || (s.equals("&emacr;")) || (s.equals("&eogon;")))
/* 510 */       return 'e';
/* 511 */     if ((s.equals("&Emacr;")) || (s.equals("&Ecaron;")))
/* 512 */       return 'E';
/* 513 */     if (s.equals("&lacute;"))
/* 514 */       return 'l';
/* 515 */     if (s.equals("&Lacute;"))
/* 516 */       return 'L';
/* 517 */     if ((s.equals("&nacute;")) || (s.equals("&ncaron;")) || (s.equals("&ncedil;")))
/* 518 */       return 'n';
/* 519 */     if ((s.equals("&rcaron;")) || (s.equals("&racute;")))
/* 520 */       return 'r';
/* 521 */     if (s.equals("&Rcaron;"))
/* 522 */       return 'R';
/* 523 */     if (s.equals("&omacr;"))
/* 524 */       return 'o';
/* 525 */     if (s.equals("&imacr;"))
/* 526 */       return 'i';
/* 527 */     if ((s.equals("&sacute;")) || (s.equals("&scedil;")) || (s.equals("&scirc;")))
/* 528 */       return 's';
/* 529 */     if ((s.equals("&Sacute")) || (s.equals("&Scedil;")))
/* 530 */       return 'S';
/* 531 */     if ((s.equals("&tcaron;")) || (s.equals("&tcedil;")))
/* 532 */       return 't';
/* 533 */     if ((s.equals("&umacr;")) || (s.equals("&uring;")))
/* 534 */       return 'u';
/* 535 */     if (s.equals("&wcirc;"))
/* 536 */       return 'w';
/* 537 */     if (s.equals("&Ycirc;"))
/* 538 */       return 'Y';
/* 539 */     if (s.equals("&ycirc;"))
/* 540 */       return 'y';
/* 541 */     if ((s.equals("&zcaron;")) || (s.equals("&zacute;")))
/* 542 */       return 'z';
/* 543 */     if (s.equals("&Zcaron;"))
/* 544 */       return 'Z';
/* 545 */     if (s.equals("&hearts;"))
/* 546 */       return '♥';
/* 547 */     if (s.equals("&infin;"))
/* 548 */       return '∞';
/* 549 */     if (s.equals("&dollar;"))
/* 550 */       return '$';
/* 551 */     if ((s.equals("&sub;")) || (s.equals("&lcub;")))
/* 552 */       return '⊂';
/* 553 */     if ((s.equals("&sup;")) || (s.equals("&rcub;")))
/* 554 */       return '⊃';
/* 555 */     if (s.equals("&lsqb;"))
/* 556 */       return '[';
/* 557 */     if (s.equals("&rsqb;")) {
/* 558 */       return ']';
/*     */     }
/* 560 */     return ' ';
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
/*     */   public static String escapeStringForXML(String s)
/*     */   {
/* 575 */     StringBuilder result = new StringBuilder();
/* 576 */     for (int i = 0; i < s.length(); i++) {
/* 577 */       int c = s.charAt(i);
/* 578 */       if ((c < 32) || (c >= 128))
/*     */       {
/* 580 */         result.append(" ");
/*     */       } else {
/* 582 */         switch (c) {
/*     */         case 38: 
/* 584 */           result.append("&amp;");
/* 585 */           break;
/*     */         case 60: 
/* 587 */           result.append("&lt;");
/* 588 */           break;
/*     */         case 62: 
/* 590 */           result.append("&gt;");
/* 591 */           break;
/*     */         case 34: 
/* 593 */           result.append("&quot;");
/* 594 */           break;
/*     */         case 39: 
/* 596 */           result.append("&apos;");
/* 597 */           break;
/*     */         default: 
/* 599 */           result.append((char)c);
/*     */         }
/*     */       }
/*     */     }
/* 603 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String escapeXML(String in)
/*     */   {
/* 615 */     int leng = in.length();
/* 616 */     StringBuilder sb = new StringBuilder(leng);
/* 617 */     for (int i = 0; i < leng; i++) {
/* 618 */       char c = in.charAt(i);
/* 619 */       if (c == '&') {
/* 620 */         sb.append("&amp;");
/* 621 */       } else if (c == '<') {
/* 622 */         sb.append("&lt;");
/* 623 */       } else if (c == '>') {
/* 624 */         sb.append("&gt;");
/* 625 */       } else if (c == '"') {
/* 626 */         sb.append("&quot;");
/* 627 */       } else if (c == '\'') {
/* 628 */         sb.append("&apos;");
/*     */       } else {
/* 630 */         sb.append(c);
/*     */       }
/*     */     }
/* 633 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static String escapeTextAroundXMLTags(String s)
/*     */   {
/* 639 */     StringBuilder result = new StringBuilder();
/*     */     
/*     */ 
/* 642 */     Reader r = new StringReader(s);
/*     */     try {
/*     */       for (;;) {
/* 645 */         String text = readUntilTag(r);
/*     */         
/* 647 */         result.append(escapeStringForXML(text));
/* 648 */         XMLTag tag = readAndParseTag(r);
/*     */         
/* 650 */         if (tag == null) {
/*     */           break;
/*     */         }
/* 653 */         result.append(tag.toString());
/*     */       }
/*     */     } catch (Exception e) {
/* 656 */       System.err.println("Error reading string");
/* 657 */       e.printStackTrace();
/*     */     }
/* 659 */     return result.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static class XMLTag
/*     */   {
/*     */     public String text;
/*     */     
/*     */     public String name;
/*     */     public Map<String, String> attributes;
/*     */     public boolean isEndTag;
/*     */     public boolean isSingleTag;
/*     */     
/*     */     public XMLTag(String tag)
/*     */       throws Exception
/*     */     {
/* 675 */       if ((tag == null) || (tag.length() == 0)) {
/* 676 */         throw new Exception("Can't parse tag");
/*     */       }
/* 678 */       this.text = tag;
/* 679 */       int begin = 1;
/* 680 */       if (tag.charAt(1) == '/') {
/* 681 */         begin = 2;
/* 682 */         this.isEndTag = true;
/*     */       } else {
/* 684 */         this.isEndTag = false;
/*     */       }
/* 686 */       int end = tag.length() - 1;
/* 687 */       if (tag.charAt(tag.length() - 2) == '/') {
/* 688 */         end = tag.length() - 2;
/* 689 */         this.isSingleTag = true;
/*     */       } else {
/* 691 */         this.isSingleTag = false;
/*     */       }
/* 693 */       tag = tag.substring(begin, end);
/* 694 */       this.attributes = new HashMap();
/* 695 */       begin = 0;
/* 696 */       end = tag.indexOf(' ');
/* 697 */       if (end < 0) {
/* 698 */         this.name = tag;
/*     */       } else {
/* 700 */         this.name = tag.substring(begin, end);
/*     */         do {
/* 702 */           begin = end + 1;
/* 703 */           while (tag.charAt(begin) < '!') {
/* 704 */             begin++;
/*     */           }
/* 706 */           end = tag.indexOf('=', begin);
/* 707 */           if (end < 0) {
/*     */             break;
/*     */           }
/* 710 */           String att = tag.substring(begin, end);
/* 711 */           begin = end + 1;
/* 712 */           String value = null;
/* 713 */           if (tag.length() > begin) {
/* 714 */             if (tag.charAt(begin) == '"')
/*     */             {
/* 716 */               begin++;
/* 717 */               end = tag.indexOf('"', begin);
/* 718 */               if (end < 0) {
/*     */                 break;
/*     */               }
/* 721 */               value = tag.substring(begin, end);
/* 722 */               end++;
/*     */             }
/*     */             else {
/* 725 */               end = tag.indexOf(' ', begin);
/* 726 */               if (end < 0) {
/* 727 */                 end = tag.length();
/*     */               }
/* 729 */               value = tag.substring(begin, end);
/*     */             }
/*     */           }
/* 732 */           this.attributes.put(att, value);
/* 733 */         } while (end < tag.length() - 3);
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString() {
/* 738 */       return this.text;
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
/*     */   public static String readTag(Reader r)
/*     */     throws IOException
/*     */   {
/* 752 */     if (!r.ready()) {
/* 753 */       return null;
/*     */     }
/* 755 */     StringBuilder b = new StringBuilder("<");
/* 756 */     int c = r.read();
/* 757 */     while (c >= 0) {
/* 758 */       b.append((char)c);
/* 759 */       if (c == 62) {
/*     */         break;
/*     */       }
/* 762 */       c = r.read();
/*     */     }
/* 764 */     if (b.length() == 1) {
/* 765 */       return null;
/*     */     }
/* 767 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static XMLTag parseTag(String tagString) throws Exception {
/* 771 */     if ((tagString == null) || (tagString.length() == 0)) {
/* 772 */       return null;
/*     */     }
/* 774 */     return new XMLTag(tagString);
/*     */   }
/*     */   
/*     */   public static Document readDocumentFromFile(String filename) throws Exception {
/* 778 */     InputSource in = new InputSource(new FileReader(filename));
/* 779 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 780 */     factory.setNamespaceAware(false);
/* 781 */     DocumentBuilder db = factory.newDocumentBuilder();
/* 782 */     db.setErrorHandler(new SAXErrorHandler(null));
/* 783 */     return db.parse(in);
/*     */   }
/*     */   
/*     */   private static class SAXErrorHandler implements ErrorHandler
/*     */   {
/*     */     public static String makeBetterErrorString(String msg, SAXParseException ex) {
/* 789 */       StringBuilder sb = new StringBuilder(msg);
/* 790 */       sb.append(": ");
/* 791 */       String str = ex.getMessage();
/* 792 */       if (str.lastIndexOf(".") == str.length() - 1) {
/* 793 */         str = str.substring(0, str.length() - 1);
/*     */       }
/* 795 */       sb.append(str);
/* 796 */       sb.append(" at document line ").append(ex.getLineNumber());
/* 797 */       sb.append(", column ").append(ex.getColumnNumber());
/* 798 */       if (ex.getSystemId() != null) {
/* 799 */         sb.append(" in entity from systemID ").append(ex.getSystemId());
/* 800 */       } else if (ex.getPublicId() != null) {
/* 801 */         sb.append(" in entity from publicID ").append(ex.getPublicId());
/*     */       }
/* 803 */       sb.append(".");
/* 804 */       return sb.toString();
/*     */     }
/*     */     
/*     */     public void warning(SAXParseException exception) {
/* 808 */       System.err.println(makeBetterErrorString("Warning", exception));
/*     */     }
/*     */     
/*     */     public void error(SAXParseException exception) {
/* 812 */       System.err.println(makeBetterErrorString("Error", exception));
/*     */     }
/*     */     
/*     */     public void fatalError(SAXParseException ex) throws SAXParseException {
/* 816 */       throw new SAXParseException(makeBetterErrorString("Fatal Error", ex), ex.getPublicId(), ex.getSystemId(), ex.getLineNumber(), ex.getColumnNumber());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Document readDocumentFromString(String s)
/*     */     throws Exception
/*     */   {
/* 823 */     InputSource in = new InputSource(new StringReader(s));
/* 824 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 825 */     factory.setNamespaceAware(false);
/* 826 */     return factory.newDocumentBuilder().parse(in);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws Exception
/*     */   {
/* 835 */     if (args[0].equals("-readDoc")) {
/* 836 */       Document doc = readDocumentFromFile(args[1]);
/* 837 */       System.out.println(doc);
/*     */     } else {
/* 839 */       String s = StringUtils.slurpFile(args[0]);
/* 840 */       Reader r = new StringReader(s);
/* 841 */       String text = readUntilTag(r);
/* 842 */       String tag = readTag(r);
/* 843 */       while (tag.length() > 0) {
/* 844 */         text = readUntilTag(r);
/* 845 */         tag = readTag(r);
/* 846 */         if (tag.length() == 0) {
/*     */           break;
/*     */         }
/* 849 */         System.out.println("got tag=" + new XMLTag(tag));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\XMLUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */