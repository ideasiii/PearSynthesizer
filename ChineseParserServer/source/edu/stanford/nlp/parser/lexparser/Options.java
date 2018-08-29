/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import edu.stanford.nlp.util.Numberer;
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class Options implements Serializable
/*     */ {
/*     */   public Options()
/*     */   {
/*  21 */     this(new EnglishTreebankParserParams());
/*     */   }
/*     */   
/*     */   public Options(TreebankLangParserParams tlpParams) {
/*  25 */     this.tlpParams = tlpParams;
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
/*     */   public void setOptions(String... flags)
/*     */   {
/*  41 */     setOptions(flags, 0, flags.length);
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
/*     */ 
/*     */   public void setOptions(String[] flags, int startIndex, int endIndexPlusOne)
/*     */   {
/*  59 */     for (int i = startIndex; i < endIndexPlusOne;) {
/*  60 */       i = setOption(flags, i);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOptionsOrWarn(String... flags)
/*     */   {
/*  77 */     setOptionsOrWarn(flags, 0, flags.length);
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
/*     */ 
/*     */   public void setOptionsOrWarn(String[] flags, int startIndex, int endIndexPlusOne)
/*     */   {
/*  95 */     for (int i = startIndex; i < endIndexPlusOne;) {
/*  96 */       i = setOptionOrWarn(flags, i);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public int setOptionOrWarn(String[] flags, int i)
/*     */   {
/* 125 */     int j = setOptionFlag(flags, i);
/* 126 */     if (j == i) {
/* 127 */       j = this.tlpParams.setOptionFlag(flags, i);
/*     */     }
/* 129 */     if (j == i) {
/* 130 */       System.err.println("WARNING! lexparser.Options: Unknown option ignored: " + flags[i]);
/* 131 */       j++;
/*     */     }
/* 133 */     return j;
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
/*     */   public int setOption(String[] flags, int i)
/*     */   {
/* 162 */     int j = setOptionFlag(flags, i);
/* 163 */     if (j == i) {
/* 164 */       j = this.tlpParams.setOptionFlag(flags, i);
/*     */     }
/* 166 */     if (j == i) {
/* 167 */       throw new IllegalArgumentException("Unknown option: " + flags[i]);
/*     */     }
/* 169 */     return j;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int setOptionFlag(String[] args, int i)
/*     */   {
/* 191 */     if (args[i].equalsIgnoreCase("-PCFG")) {
/* 192 */       this.doDep = false;
/* 193 */       this.doPCFG = true;
/* 194 */       i++;
/* 195 */     } else if (args[i].equalsIgnoreCase("-dep")) {
/* 196 */       this.doDep = true;
/* 197 */       this.doPCFG = false;
/* 198 */       i++;
/* 199 */     } else if (args[i].equalsIgnoreCase("-factored")) {
/* 200 */       this.doDep = true;
/* 201 */       this.doPCFG = true;
/* 202 */       Test.useFastFactored = false;
/* 203 */       i++;
/* 204 */     } else if (args[i].equalsIgnoreCase("-fastFactored")) {
/* 205 */       this.doDep = true;
/* 206 */       this.doPCFG = true;
/* 207 */       Test.useFastFactored = true;
/* 208 */       i++;
/* 209 */     } else if (args[i].equalsIgnoreCase("-noRecoveryTagging")) {
/* 210 */       Test.noRecoveryTagging = true;
/* 211 */       i++;
/* 212 */     } else if ((args[i].equalsIgnoreCase("-maxLength")) && (i + 1 < args.length)) {
/* 213 */       Test.maxLength = Integer.parseInt(args[(i + 1)]);
/* 214 */       i += 2;
/* 215 */     } else if ((args[i].equalsIgnoreCase("-MAX_ITEMS")) && (i + 1 < args.length)) {
/* 216 */       Test.MAX_ITEMS = Integer.parseInt(args[(i + 1)]);
/* 217 */       i += 2;
/* 218 */     } else if (args[i].equalsIgnoreCase("-iterativeCKY")) {
/* 219 */       Test.iterativeCKY = true;
/* 220 */       i++;
/* 221 */     } else if ((args[i].equalsIgnoreCase("-vMarkov")) && (i + 1 < args.length)) {
/* 222 */       int order = Integer.parseInt(args[(i + 1)]);
/* 223 */       if (order <= 1) {
/* 224 */         Train.PA = false;
/* 225 */         Train.gPA = false;
/* 226 */       } else if (order == 2) {
/* 227 */         Train.PA = true;
/* 228 */         Train.gPA = false;
/* 229 */       } else if (order >= 3) {
/* 230 */         Train.PA = true;
/* 231 */         Train.gPA = true;
/*     */       }
/* 233 */       i += 2;
/* 234 */     } else if ((args[i].equalsIgnoreCase("-vSelSplitCutOff")) && (i + 1 < args.length)) {
/* 235 */       Train.selectiveSplitCutOff = Double.parseDouble(args[(i + 1)]);
/* 236 */       Train.selectiveSplit = Train.selectiveSplitCutOff > 0.0D;
/* 237 */       i += 2;
/* 238 */     } else if ((args[i].equalsIgnoreCase("-vSelPostSplitCutOff")) && (i + 1 < args.length)) {
/* 239 */       Train.selectivePostSplitCutOff = Double.parseDouble(args[(i + 1)]);
/* 240 */       Train.selectivePostSplit = Train.selectivePostSplitCutOff > 0.0D;
/* 241 */       i += 2;
/* 242 */     } else if ((args[i].equalsIgnoreCase("-deleteSplitters")) && (i + 1 < args.length)) {
/* 243 */       String[] toDel = args[(i + 1)].split(" *, *");
/* 244 */       Train.deleteSplitters = new HashSet();
/* 245 */       for (String str : toDel) {
/* 246 */         Train.deleteSplitters.add(str);
/*     */       }
/* 248 */       i += 2;
/* 249 */     } else if (args[i].equalsIgnoreCase("-postSplitWithBaseCategory")) {
/* 250 */       Train.postSplitWithBaseCategory = true;
/* 251 */       i++;
/* 252 */     } else if ((args[i].equalsIgnoreCase("-vPostMarkov")) && (i + 1 < args.length)) {
/* 253 */       int order = Integer.parseInt(args[(i + 1)]);
/* 254 */       if (order <= 1) {
/* 255 */         Train.postPA = false;
/* 256 */         Train.postGPA = false;
/* 257 */       } else if (order == 2) {
/* 258 */         Train.postPA = true;
/* 259 */         Train.postGPA = false;
/* 260 */       } else if (order >= 3) {
/* 261 */         Train.postPA = true;
/* 262 */         Train.postGPA = true;
/*     */       }
/* 264 */       i += 2;
/* 265 */     } else if ((args[i].equalsIgnoreCase("-hMarkov")) && (i + 1 < args.length)) {
/* 266 */       int order = Integer.parseInt(args[(i + 1)]);
/* 267 */       if (order >= 0) {
/* 268 */         Train.markovOrder = order;
/* 269 */         Train.markovFactor = true;
/*     */       } else {
/* 271 */         Train.markovFactor = false;
/*     */       }
/* 273 */       i += 2;
/* 274 */     } else if ((args[i].equalsIgnoreCase("-distanceBins")) && (i + 1 < args.length)) {
/* 275 */       int numBins = Integer.parseInt(args[(i + 1)]);
/* 276 */       if (numBins <= 1) {
/* 277 */         this.distance = false;
/* 278 */       } else if (numBins == 4) {
/* 279 */         this.distance = true;
/* 280 */         this.coarseDistance = true;
/* 281 */       } else if (numBins == 5) {
/* 282 */         this.distance = true;
/* 283 */         this.coarseDistance = false;
/*     */       } else {
/* 285 */         throw new IllegalArgumentException("Invalid value for -distanceBin: " + args[(i + 1)]);
/*     */       }
/* 287 */       i += 2;
/* 288 */     } else if (args[i].equalsIgnoreCase("-noStop")) {
/* 289 */       this.genStop = false;
/* 290 */       i++;
/* 291 */     } else if (args[i].equalsIgnoreCase("-nonDirectional")) {
/* 292 */       this.directional = false;
/* 293 */       i++;
/* 294 */     } else if ((args[i].equalsIgnoreCase("-depWeight")) && (i + 1 < args.length)) {
/* 295 */       Test.depWeight = Double.parseDouble(args[(i + 1)]);
/* 296 */       i += 2;
/* 297 */     } else if ((args[i].equalsIgnoreCase("-printPCFGkBest")) && (i + 1 < args.length)) {
/* 298 */       Test.printPCFGkBest = Integer.parseInt(args[(i + 1)]);
/* 299 */       i += 2;
/* 300 */     } else if ((args[i].equalsIgnoreCase("-printFactoredKGood")) && (i + 1 < args.length)) {
/* 301 */       Test.printFactoredKGood = Integer.parseInt(args[(i + 1)]);
/* 302 */       i += 2;
/* 303 */     } else if ((args[i].equalsIgnoreCase("-smoothTagsThresh")) && (i + 1 < args.length)) {
/* 304 */       this.lexOptions.smoothInUnknownsThreshold = Integer.parseInt(args[(i + 1)]);
/* 305 */       i += 2;
/* 306 */     } else if ((args[i].equalsIgnoreCase("-unseenSmooth")) && (i + 1 < args.length)) {
/* 307 */       Test.unseenSmooth = Double.parseDouble(args[(i + 1)]);
/* 308 */       i += 2;
/* 309 */     } else if ((args[i].equalsIgnoreCase("-fractionBeforeUnseenCounting")) && (i + 1 < args.length)) {
/* 310 */       Train.fractionBeforeUnseenCounting = Double.parseDouble(args[(i + 1)]);
/* 311 */       i += 2;
/* 312 */     } else if ((args[i].equalsIgnoreCase("-hSelSplitThresh")) && (i + 1 < args.length)) {
/* 313 */       Train.HSEL_CUT = Integer.parseInt(args[(i + 1)]);
/* 314 */       Train.hSelSplit = Train.HSEL_CUT > 0;
/* 315 */       i += 2;
/* 316 */     } else if (args[i].equalsIgnoreCase("-tagPA")) {
/* 317 */       Train.tagPA = true;
/* 318 */       i++;
/* 319 */     } else if ((args[i].equalsIgnoreCase("-tagSelSplitCutOff")) && (i + 1 < args.length)) {
/* 320 */       Train.tagSelectiveSplitCutOff = Double.parseDouble(args[(i + 1)]);
/* 321 */       Train.tagSelectiveSplit = Train.tagSelectiveSplitCutOff > 0.0D;
/* 322 */       i += 2;
/* 323 */     } else if ((args[i].equalsIgnoreCase("-tagSelPostSplitCutOff")) && (i + 1 < args.length)) {
/* 324 */       Train.tagSelectivePostSplitCutOff = Double.parseDouble(args[(i + 1)]);
/* 325 */       Train.tagSelectivePostSplit = Train.tagSelectivePostSplitCutOff > 0.0D;
/* 326 */       i += 2;
/* 327 */     } else if (args[i].equalsIgnoreCase("-noTagSplit")) {
/* 328 */       Train.noTagSplit = true;
/* 329 */       i++;
/* 330 */     } else if ((args[i].equalsIgnoreCase("-uwm")) && (i + 1 < args.length)) {
/* 331 */       this.lexOptions.useUnknownWordSignatures = Integer.parseInt(args[(i + 1)]);
/* 332 */       i += 2;
/* 333 */     } else if ((args[i].equalsIgnoreCase("-unknownSuffixSize")) && (i + 1 < args.length)) {
/* 334 */       this.lexOptions.unknownSuffixSize = Integer.parseInt(args[(i + 1)]);
/* 335 */       i += 2;
/* 336 */     } else if ((args[i].equalsIgnoreCase("-unknownPrefixSize")) && (i + 1 < args.length)) {
/* 337 */       this.lexOptions.unknownPrefixSize = Integer.parseInt(args[(i + 1)]);
/* 338 */       i += 2;
/* 339 */     } else if ((args[i].equalsIgnoreCase("-openClassThreshold")) && (i + 1 < args.length)) {
/* 340 */       Train.openClassTypesThreshold = Integer.parseInt(args[(i + 1)]);
/* 341 */       i += 2;
/* 342 */     } else if ((args[i].equalsIgnoreCase("-leaveItAll")) && (i + 1 < args.length)) {
/* 343 */       Train.leaveItAll = Integer.parseInt(args[(i + 1)]);
/* 344 */       i += 2;
/* 345 */     } else if ((args[i].equalsIgnoreCase("-unary")) && (i + 1 < args.length)) {
/* 346 */       Train.markUnary = Integer.parseInt(args[(i + 1)]);
/* 347 */       i += 2;
/* 348 */     } else if (args[i].equalsIgnoreCase("-unaryTags")) {
/* 349 */       Train.markUnaryTags = true;
/* 350 */       i++;
/* 351 */     } else if (args[i].equalsIgnoreCase("-mutate")) {
/* 352 */       this.lexOptions.smartMutation = true;
/* 353 */       i++;
/* 354 */     } else if (args[i].equalsIgnoreCase("-useUnicodeType")) {
/* 355 */       this.lexOptions.useUnicodeType = true;
/* 356 */       i++;
/* 357 */     } else if (args[i].equalsIgnoreCase("-rightRec")) {
/* 358 */       Train.rightRec = true;
/* 359 */       i++;
/* 360 */     } else if (args[i].equalsIgnoreCase("-noRightRec")) {
/* 361 */       Train.rightRec = false;
/* 362 */       i++;
/* 363 */     } else if (args[i].equalsIgnoreCase("-preTag")) {
/* 364 */       Test.preTag = true;
/* 365 */       i++;
/* 366 */     } else if (args[i].equalsIgnoreCase("-forceTags")) {
/* 367 */       Test.forceTags = true;
/* 368 */       i++;
/* 369 */     } else if (args[i].equalsIgnoreCase("-scTags")) {
/* 370 */       this.dcTags = false;
/* 371 */       i++;
/* 372 */     } else if (args[i].equalsIgnoreCase("-dcTags")) {
/* 373 */       this.dcTags = true;
/* 374 */       i++;
/* 375 */     } else if (args[i].equalsIgnoreCase("-evalb")) {
/* 376 */       Test.evalb = true;
/* 377 */       i++;
/* 378 */     } else if ((args[i].equalsIgnoreCase("-v")) || (args[i].equalsIgnoreCase("-verbose"))) {
/* 379 */       Test.verbose = true;
/* 380 */       i++;
/* 381 */     } else if ((args[i].equalsIgnoreCase("-outputFilesDirectory")) && (i + 1 < args.length)) {
/* 382 */       Test.outputFilesDirectory = args[(i + 1)];
/* 383 */       i += 2;
/* 384 */     } else if ((args[i].equalsIgnoreCase("-outputFilesExtension")) && (i + 1 < args.length)) {
/* 385 */       Test.outputFilesExtension = args[(i + 1)];
/* 386 */       i += 2;
/* 387 */     } else if (args[i].equalsIgnoreCase("-writeOutputFiles")) {
/* 388 */       Test.writeOutputFiles = true;
/* 389 */       i++;
/* 390 */     } else if (args[i].equalsIgnoreCase("-printAllBestParses")) {
/* 391 */       Test.printAllBestParses = true;
/* 392 */       i++;
/* 393 */     } else if ((args[i].equalsIgnoreCase("-outputTreeFormat")) || (args[i].equalsIgnoreCase("-outputFormat"))) {
/* 394 */       Test.outputFormat = args[(i + 1)];
/* 395 */       i += 2;
/* 396 */     } else if ((args[i].equalsIgnoreCase("-outputTreeFormatOptions")) || (args[i].equalsIgnoreCase("-outputFormatOptions"))) {
/* 397 */       Test.outputFormatOptions = args[(i + 1)];
/* 398 */       i += 2;
/* 399 */     } else if (args[i].equalsIgnoreCase("-addMissingFinalPunctuation")) {
/* 400 */       Test.addMissingFinalPunctuation = true;
/* 401 */       i++;
/* 402 */     } else if (args[i].equalsIgnoreCase("-flexiTag")) {
/* 403 */       this.lexOptions.flexiTag = true;
/* 404 */       i++;
/* 405 */     } else if (args[i].equalsIgnoreCase("-lexiTag")) {
/* 406 */       this.lexOptions.flexiTag = false;
/* 407 */       i++;
/* 408 */     } else if (args[i].equalsIgnoreCase("-compactGrammar")) {
/* 409 */       Train.compactGrammar = Integer.parseInt(args[(i + 1)]);
/* 410 */       i += 2;
/* 411 */     } else if (args[i].equalsIgnoreCase("-markFinalStates")) {
/* 412 */       Train.markFinalStates = args[(i + 1)].equalsIgnoreCase("true");
/* 413 */       i += 2;
/* 414 */     } else if (args[i].equalsIgnoreCase("-leftToRight")) {
/* 415 */       Train.leftToRight = args[(i + 1)].equals("true");
/* 416 */       i += 2;
/* 417 */     } else if (args[i].equalsIgnoreCase("-cnf")) {
/* 418 */       this.forceCNF = true;
/* 419 */       i++;
/* 420 */     } else if ((args[i].equalsIgnoreCase("-nodePrune")) && (i + 1 < args.length)) {
/* 421 */       this.nodePrune = args[(i + 1)].equalsIgnoreCase("true");
/* 422 */       i += 2;
/* 423 */     } else if (args[i].equalsIgnoreCase("-acl03pcfg")) {
/* 424 */       this.doDep = false;
/* 425 */       this.doPCFG = true;
/*     */       
/* 427 */       Train.markUnary = 1;
/* 428 */       Train.PA = true;
/* 429 */       Train.gPA = false;
/* 430 */       Train.tagPA = true;
/* 431 */       Train.tagSelectiveSplit = false;
/* 432 */       Train.rightRec = true;
/* 433 */       Train.selectiveSplit = true;
/* 434 */       Train.selectiveSplitCutOff = 400.0D;
/* 435 */       Train.markovFactor = true;
/* 436 */       Train.markovOrder = 2;
/* 437 */       Train.hSelSplit = true;
/* 438 */       this.lexOptions.useUnknownWordSignatures = 2;
/* 439 */       this.lexOptions.flexiTag = true;
/*     */       
/* 441 */       this.dcTags = false;
/*     */     }
/* 443 */     else if (args[i].equalsIgnoreCase("-jenny")) {
/* 444 */       this.doDep = false;
/* 445 */       this.doPCFG = true;
/*     */       
/* 447 */       Train.markUnary = 1;
/* 448 */       Train.PA = false;
/* 449 */       Train.gPA = false;
/* 450 */       Train.tagPA = false;
/* 451 */       Train.tagSelectiveSplit = false;
/* 452 */       Train.rightRec = true;
/* 453 */       Train.selectiveSplit = false;
/*     */       
/* 455 */       Train.markovFactor = false;
/*     */       
/* 457 */       Train.hSelSplit = false;
/* 458 */       this.lexOptions.useUnknownWordSignatures = 2;
/* 459 */       this.lexOptions.flexiTag = true;
/*     */       
/* 461 */       this.dcTags = false;
/*     */     }
/* 463 */     else if (args[i].equalsIgnoreCase("-goodPCFG")) {
/* 464 */       this.doDep = false;
/* 465 */       this.doPCFG = true;
/*     */       
/* 467 */       Train.markUnary = 1;
/* 468 */       Train.PA = true;
/* 469 */       Train.gPA = false;
/* 470 */       Train.tagPA = true;
/* 471 */       Train.tagSelectiveSplit = false;
/* 472 */       Train.rightRec = true;
/* 473 */       Train.selectiveSplit = true;
/* 474 */       Train.selectiveSplitCutOff = 400.0D;
/* 475 */       Train.markovFactor = true;
/* 476 */       Train.markovOrder = 2;
/* 477 */       Train.hSelSplit = true;
/* 478 */       this.lexOptions.useUnknownWordSignatures = 2;
/* 479 */       this.lexOptions.flexiTag = true;
/*     */       
/* 481 */       this.dcTags = false;
/* 482 */       String[] delSplit = { "-deleteSplitters", "VP^NP,VP^VP,VP^SINV,VP^SQ" };
/*     */       
/* 484 */       if (setOptionFlag(delSplit, 0) != 2) {
/* 485 */         System.err.println("Error processing deleteSplitters");
/*     */       }
/*     */     }
/* 488 */     else if (args[i].equalsIgnoreCase("-linguisticPCFG")) {
/* 489 */       this.doDep = false;
/* 490 */       this.doPCFG = true;
/*     */       
/* 492 */       Train.markUnary = 1;
/* 493 */       Train.PA = true;
/* 494 */       Train.gPA = false;
/* 495 */       Train.tagPA = true;
/* 496 */       Train.tagSelectiveSplit = false;
/* 497 */       Train.rightRec = false;
/* 498 */       Train.selectiveSplit = true;
/* 499 */       Train.selectiveSplitCutOff = 400.0D;
/* 500 */       Train.markovFactor = true;
/* 501 */       Train.markovOrder = 2;
/* 502 */       Train.hSelSplit = true;
/* 503 */       this.lexOptions.useUnknownWordSignatures = 5;
/* 504 */       this.lexOptions.flexiTag = false;
/*     */       
/* 506 */       this.dcTags = false;
/*     */     }
/* 508 */     else if (args[i].equalsIgnoreCase("-ijcai03")) {
/* 509 */       this.doDep = true;
/* 510 */       this.doPCFG = true;
/* 511 */       Train.markUnary = 0;
/* 512 */       Train.PA = true;
/* 513 */       Train.gPA = false;
/* 514 */       Train.tagPA = false;
/* 515 */       Train.tagSelectiveSplit = false;
/* 516 */       Train.rightRec = false;
/* 517 */       Train.selectiveSplit = true;
/* 518 */       Train.selectiveSplitCutOff = 300.0D;
/* 519 */       Train.markovFactor = true;
/* 520 */       Train.markovOrder = 2;
/* 521 */       Train.hSelSplit = true;
/* 522 */       Train.compactGrammar = 0;
/* 523 */       this.lexOptions.useUnknownWordSignatures = 2;
/* 524 */       this.lexOptions.flexiTag = false;
/* 525 */       this.dcTags = true;
/*     */ 
/*     */     }
/* 528 */     else if (args[i].equalsIgnoreCase("-goodFactored")) {
/* 529 */       this.doDep = true;
/* 530 */       this.doPCFG = true;
/* 531 */       Train.markUnary = 0;
/* 532 */       Train.PA = true;
/* 533 */       Train.gPA = false;
/* 534 */       Train.tagPA = false;
/* 535 */       Train.tagSelectiveSplit = false;
/* 536 */       Train.rightRec = false;
/* 537 */       Train.selectiveSplit = true;
/* 538 */       Train.selectiveSplitCutOff = 300.0D;
/* 539 */       Train.markovFactor = true;
/* 540 */       Train.markovOrder = 2;
/* 541 */       Train.hSelSplit = true;
/* 542 */       Train.compactGrammar = 0;
/* 543 */       this.lexOptions.useUnknownWordSignatures = 5;
/* 544 */       this.lexOptions.flexiTag = false;
/* 545 */       this.dcTags = true;
/*     */ 
/*     */     }
/* 548 */     else if (args[i].equalsIgnoreCase("-chineseFactored"))
/*     */     {
/*     */ 
/* 551 */       this.dcTags = false;
/* 552 */       this.lexOptions.useUnicodeType = true;
/* 553 */       Train.markovOrder = 2;
/* 554 */       Train.hSelSplit = true;
/* 555 */       Train.markovFactor = true;
/* 556 */       Train.HSEL_CUT = 50;
/*     */ 
/*     */ 
/*     */     }
/* 560 */     else if (args[i].equalsIgnoreCase("-arabicFactored")) {
/* 561 */       this.doDep = true;
/* 562 */       this.doPCFG = true;
/* 563 */       this.dcTags = false;
/* 564 */       Train.markovFactor = true;
/* 565 */       Train.markovOrder = 2;
/* 566 */       Train.hSelSplit = true;
/* 567 */       Train.HSEL_CUT = 75;
/* 568 */       Train.PA = true;
/* 569 */       Train.gPA = false;
/* 570 */       Train.selectiveSplit = true;
/* 571 */       Train.selectiveSplitCutOff = 300.0D;
/* 572 */       Train.markUnary = 1;
/*     */       
/* 574 */       this.lexOptions.useUnknownWordSignatures = 9;
/* 575 */       this.lexOptions.unknownPrefixSize = 1;
/* 576 */       this.lexOptions.unknownSuffixSize = 1;
/*     */     }
/* 578 */     else if (args[i].equalsIgnoreCase("-chinesePCFG")) {
/* 579 */       this.doDep = false;
/* 580 */       this.doPCFG = true;
/*     */       
/*     */ 
/* 583 */       this.dcTags = false;
/* 584 */     } else if ((args[i].equalsIgnoreCase("-printTT")) && (i + 1 < args.length)) {
/* 585 */       Train.printTreeTransformations = Integer.parseInt(args[(i + 1)]);
/* 586 */       i += 2;
/* 587 */     } else if (args[i].equalsIgnoreCase("-printAnnotatedRuleCounts")) {
/* 588 */       Train.printAnnotatedRuleCounts = true;
/* 589 */       i++;
/* 590 */     } else if (args[i].equalsIgnoreCase("-printAnnotatedStateCounts")) {
/* 591 */       Train.printAnnotatedStateCounts = true;
/* 592 */       i++;
/* 593 */     } else if ((args[i].equalsIgnoreCase("-printAnnotated")) && (i + 1 < args.length)) {
/*     */       try {
/* 595 */         Train.printAnnotatedPW = this.tlpParams.pw(new FileOutputStream(args[(i + 1)]));
/*     */       } catch (IOException ioe) {
/* 597 */         Train.printAnnotatedPW = null;
/*     */       }
/* 599 */       i += 2;
/* 600 */     } else if ((args[i].equalsIgnoreCase("-printBinarized")) && (i + 1 < args.length)) {
/*     */       try {
/* 602 */         Train.printBinarizedPW = this.tlpParams.pw(new FileOutputStream(args[(i + 1)]));
/*     */       } catch (IOException ioe) {
/* 604 */         Train.printBinarizedPW = null;
/*     */       }
/* 606 */       i += 2;
/* 607 */     } else if (args[i].equalsIgnoreCase("-printStates")) {
/* 608 */       Train.printStates = true;
/* 609 */       i++;
/* 610 */     } else if (args[i].equalsIgnoreCase("-evals")) {
/* 611 */       Test.evals = StringUtils.stringToProperties(args[(i + 1)], Test.evals);
/* 612 */       i += 2;
/* 613 */     } else if (args[i].equalsIgnoreCase("-fastFactoredCandidateMultiplier")) {
/* 614 */       Test.fastFactoredCandidateMultiplier = Integer.parseInt(args[(i + 1)]);
/* 615 */       i += 2;
/* 616 */     } else if (args[i].equalsIgnoreCase("-fastFactoredCandidateAddend")) {
/* 617 */       Test.fastFactoredCandidateAddend = Integer.parseInt(args[(i + 1)]);
/* 618 */       i += 2;
/*     */     }
/* 620 */     return i;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class LexOptions
/*     */     implements Serializable
/*     */   {
/* 642 */     public int useUnknownWordSignatures = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 648 */     public int smoothInUnknownsThreshold = 100;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 653 */     public boolean smartMutation = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 658 */     public boolean useUnicodeType = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 664 */     public int unknownSuffixSize = 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 670 */     public int unknownPrefixSize = 1;
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
/* 683 */     public boolean flexiTag = false;
/*     */     
/*     */     private static final long serialVersionUID = 2805351374506855632L;
/*     */     
/* 687 */     private static final String[] params = { "useUnknownWordSignatures", "smoothInUnknownsThreshold", "smartMutation", "useUnicodeType", "unknownSuffixSize", "unknownPrefixSize", "flexiTag" };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 696 */       return params[0] + " " + this.useUnknownWordSignatures + "\n" + params[1] + " " + this.smoothInUnknownsThreshold + "\n" + params[2] + " " + this.smartMutation + "\n" + params[3] + " " + this.useUnicodeType + "\n" + params[4] + " " + this.unknownSuffixSize + "\n" + params[5] + " " + this.unknownPrefixSize + "\n" + params[6] + " " + this.flexiTag + "\n";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void readData(BufferedReader in)
/*     */       throws IOException
/*     */     {
/* 706 */       for (int i = 0; i < params.length; i++) {
/* 707 */         String line = in.readLine();
/* 708 */         int idx = line.indexOf(' ');
/* 709 */         String key = line.substring(0, idx);
/* 710 */         String value = line.substring(idx + 1);
/* 711 */         if (!key.equalsIgnoreCase(params[i])) {
/* 712 */           System.err.println("Yikes!!! Expected " + params[i] + " got " + key);
/*     */         }
/* 714 */         switch (i) {
/*     */         case 0: 
/* 716 */           this.useUnknownWordSignatures = Integer.parseInt(value);
/* 717 */           break;
/*     */         case 1: 
/* 719 */           this.smoothInUnknownsThreshold = Integer.parseInt(value);
/* 720 */           break;
/*     */         case 2: 
/* 722 */           this.smartMutation = Boolean.parseBoolean(value);
/* 723 */           break;
/*     */         case 3: 
/* 725 */           this.useUnicodeType = Boolean.parseBoolean(value);
/* 726 */           break;
/*     */         case 4: 
/* 728 */           this.unknownSuffixSize = Integer.parseInt(value);
/* 729 */           break;
/*     */         case 5: 
/* 731 */           this.unknownPrefixSize = Integer.parseInt(value);
/* 732 */           break;
/*     */         case 6: 
/* 734 */           this.flexiTag = Boolean.parseBoolean(value);
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 742 */   public int numStates = -1;
/*     */   
/* 744 */   public LexOptions lexOptions = new LexOptions();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TreebankLangParserParams tlpParams;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public edu.stanford.nlp.trees.TreebankLanguagePack langpack()
/*     */   {
/* 756 */     return this.tlpParams.treebankLanguagePack();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 764 */   public boolean forceCNF = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 770 */   public boolean doPCFG = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 775 */   public boolean doDep = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 780 */   public boolean freeDependencies = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 785 */   public boolean directional = true;
/* 786 */   public boolean genStop = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 791 */   public boolean distance = true;
/*     */   
/*     */ 
/*     */ 
/* 795 */   public boolean coarseDistance = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 803 */   public boolean dcTags = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 810 */   public boolean nodePrune = false;
/*     */   private static final long serialVersionUID = 4L;
/*     */   
/*     */   public void display() {
/*     */     try {
/* 815 */       System.err.println("Options parameters:");
/* 816 */       writeData(new PrintWriter(System.err));
/* 817 */       if (Train.printStates) {
/* 818 */         dumpNumberer(Numberer.getGlobalNumberer("states"), "states", this.tlpParams.pw(System.err));
/*     */       }
/*     */     }
/*     */     catch (IOException e) {
/* 822 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeData(Writer w) throws IOException {
/* 827 */     this.numStates = Numberer.getGlobalNumberer("states").total();
/* 828 */     PrintWriter out = new PrintWriter(w);
/* 829 */     StringBuilder sb = new StringBuilder();
/* 830 */     sb.append("numStates ").append(this.numStates).append("\n");
/* 831 */     sb.append(this.lexOptions.toString());
/* 832 */     sb.append("parserParams ").append(this.tlpParams.getClass().getName()).append("\n");
/* 833 */     sb.append("forceCNF ").append(this.forceCNF).append("\n");
/* 834 */     sb.append("doPCFG ").append(this.doPCFG).append("\n");
/* 835 */     sb.append("doDep ").append(this.doDep).append("\n");
/* 836 */     sb.append("freeDependencies ").append(this.freeDependencies).append("\n");
/* 837 */     sb.append("directional ").append(this.directional).append("\n");
/* 838 */     sb.append("genStop ").append(this.genStop).append("\n");
/* 839 */     sb.append("distance ").append(this.distance).append("\n");
/* 840 */     sb.append("coarseDistance ").append(this.coarseDistance).append("\n");
/* 841 */     sb.append("dcTags ").append(this.dcTags).append("\n");
/* 842 */     sb.append("nPrune ").append(this.nodePrune).append("\n");
/* 843 */     out.print(sb.toString());
/* 844 */     out.flush();
/*     */   }
/*     */   
/*     */   private void dumpNumberer(Numberer num, String name, PrintWriter pw)
/*     */   {
/* 849 */     pw.println("### Sorted contents of " + name);
/* 850 */     java.util.List lis = new ArrayList(num.objects());
/* 851 */     Collections.sort(lis);
/* 852 */     for (Object obj : lis) {
/* 853 */       pw.println(obj);
/*     */     }
/* 855 */     pw.println("### End sorted contents of " + name);
/* 856 */     pw.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readData(BufferedReader in)
/*     */     throws IOException
/*     */   {
/*     */     do
/*     */     {
/* 869 */       line = in.readLine();
/* 870 */     } while ((line != null) && (!line.matches("^numStates.*")));
/* 871 */     if (line == null) {
/* 872 */       throw new IOException("Bad Options format: no numStates");
/*     */     }
/* 874 */     String value = line.substring(line.indexOf(' ') + 1);
/* 875 */     this.numStates = Integer.parseInt(value);
/* 876 */     this.lexOptions.readData(in);
/* 877 */     String line = in.readLine();
/* 878 */     value = line.substring(line.indexOf(' ') + 1);
/*     */     try {
/* 880 */       this.tlpParams = ((TreebankLangParserParams)Class.forName(value).newInstance());
/*     */     } catch (Exception e) {
/* 882 */       e.printStackTrace();
/* 883 */       throw new IOException("Problem instantiating parserParams: " + line);
/*     */     }
/* 885 */     line = in.readLine();
/*     */     
/* 887 */     if (line.matches("^forceCNF.*")) {
/* 888 */       value = line.substring(line.indexOf(' ') + 1);
/* 889 */       this.forceCNF = Boolean.parseBoolean(value);
/* 890 */       line = in.readLine();
/*     */     }
/* 892 */     value = line.substring(line.indexOf(' ') + 1);
/* 893 */     this.doPCFG = Boolean.parseBoolean(value);
/* 894 */     line = in.readLine();
/* 895 */     value = line.substring(line.indexOf(' ') + 1);
/* 896 */     this.doDep = Boolean.parseBoolean(value);
/* 897 */     line = in.readLine();
/* 898 */     value = line.substring(line.indexOf(' ') + 1);
/* 899 */     this.freeDependencies = Boolean.parseBoolean(value);
/* 900 */     line = in.readLine();
/* 901 */     value = line.substring(line.indexOf(' ') + 1);
/* 902 */     this.directional = Boolean.parseBoolean(value);
/* 903 */     line = in.readLine();
/* 904 */     value = line.substring(line.indexOf(' ') + 1);
/* 905 */     this.genStop = Boolean.parseBoolean(value);
/* 906 */     line = in.readLine();
/* 907 */     value = line.substring(line.indexOf(' ') + 1);
/* 908 */     this.distance = Boolean.parseBoolean(value);
/* 909 */     line = in.readLine();
/* 910 */     value = line.substring(line.indexOf(' ') + 1);
/* 911 */     this.coarseDistance = Boolean.parseBoolean(value);
/* 912 */     line = in.readLine();
/* 913 */     value = line.substring(line.indexOf(' ') + 1);
/* 914 */     this.dcTags = Boolean.parseBoolean(value);
/* 915 */     line = in.readLine();
/* 916 */     if (!line.matches("^nPrune.*")) {
/* 917 */       throw new RuntimeException("Expected nPrune, found: " + line);
/*     */     }
/* 919 */     value = line.substring(line.indexOf(' ') + 1);
/* 920 */     this.nodePrune = Boolean.parseBoolean(value);
/* 921 */     line = in.readLine();
/* 922 */     if (line.length() != 0) {
/* 923 */       throw new RuntimeException("Expected blank line, found: " + line);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Options.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */