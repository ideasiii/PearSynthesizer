/*     */ package edu.stanford.nlp.trees;
/*     */ 
/*     */ import edu.stanford.nlp.util.StringUtils;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public class EnglishGrammaticalRelations
/*     */ {
/*     */   public static List<GrammaticalRelation> values()
/*     */   {
/*  66 */     return Collections.unmodifiableList(Arrays.asList(values));
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
/*  78 */   public static final GrammaticalRelation PREDICATE = new GrammaticalRelation("pred", "predicate", GrammaticalRelation.DEPENDENT, "S|SINV", new String[] { "S < VP=target" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   public static final GrammaticalRelation AUX_MODIFIER = new GrammaticalRelation("aux", "auxiliary", GrammaticalRelation.DEPENDENT, "VP|SQ|SINV", new String[] { "VP < VP < /^(?:TO|MD|VB.*)$/=target", "SQ|SINV < (/^VB|MD/=target $++ /^(?:VP|ADJP)/)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   public static final GrammaticalRelation AUX_PASSIVE_MODIFIER = new GrammaticalRelation("auxpass", "passive auxiliary", AUX_MODIFIER, "VP|SQ", new String[] { "VP < (/^(?:VB|AUXG?)/=target < /be|was|'s|is|are|were|been|being|am|Been|Being|WAS|IS|get|got|getting|gets|Get|gotten|becomes|become|became|felt|feels|feel|seems|seem|seemed|remains|remained|remain/) < (VP|ADJP < VBN|VBD)", "VP < (/^(?:VB|AUXG?)/=target < /be|was|'s|is|are|were|been|being|am|Been|Being|WAS|IS|get|got|getting|gets|Get|gotten|becomes|become|became|felt|feels|feel|seems|seem|seemed|remains|remained|remain/) < (VP|ADJP < (VP|ADJP < VBN|VBD) < CC)", "SQ < (/^(?:VB|AUX)/=target < /^(?:was|is|are|were|am|Was|Is|Are|Were|Am|WAS|IS|ARE|WERE|AM)$/ $++ (VP < /^VB[DN]$/))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */   public static final GrammaticalRelation COPULA = new GrammaticalRelation("cop", "copula", AUX_MODIFIER, "VP|SQ", new String[] { "VP < (/^VB/=target < /(?i)^(?:am|'m|are|'re|is|'s|be|being|was|were|seem|seems|seemed|appear|appears|appeared|stay|stays|stayed|remain|remains|remained|resemble|resembles|resembled|become|becomes|became)$/ [ $++ (ADJP|NP !< VBN) | $++ (S <: (ADJP < JJ)) ] )", "SQ <, (/^VB/=target < /(?i)^(am|are|is|was|were)$/) !$ /^WH*/" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */   public static final GrammaticalRelation CONJUNCT = new GrammaticalRelation("conj", "conjunct", GrammaticalRelation.DEPENDENT, "VP|NP|ADJP|PP|QP|ADVP|UCP|S|NX|SBAR", new String[] { "VP|ADJP|PP|QP|NP|ADVP|UCP|S|NX|SBAR < (CC|CONJP $+ !PRN=target) !<, CC", "VP|ADJP|PP|NP|ADVP|UCP|S|NX|SBAR < (CC|CONJP $+ (ADVP $+ !PRN=target))", "VP|ADJP|PP|NP|ADVP|UCP|S|NX|SBAR < CC|CONJP < (/^,$/ $+ /^(A|N|V|PP|PRP|J|W|R|S)/=target)", "VP|ADJP|PP|NP|ADVP|UCP|S|NX|SBAR < CC|CONJP < (PRN $+ /^(A|N|V|PP|PRP|J|W|R|S)/=target)", "NX < CC|CONJP < (/^,$/ $- /^(A|N|V|PP|PRP|J|W|R|S)/=target)" });
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
/* 138 */   public static final GrammaticalRelation COORDINATION = new GrammaticalRelation("cc", "coordination", GrammaticalRelation.DEPENDENT, "S|VP|NP|ADJP|PP|QP|ADVP|UCP|NX|SBAR", new String[] { "S|VP|NP|QP|ADJP|PP|ADVP|UCP|NX|SBAR < (CC|CONJP=target !< /either|neither|both|Either|Neither|Both/)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */   public static final GrammaticalRelation PUNCTUATION = new GrammaticalRelation("punct", "punctuation", GrammaticalRelation.DEPENDENT, "S|NP|VP|SQ|PRN|SINV|SBAR|UCP", new String[] { "__ < /^(?:\\.|:|,|''|``|-LRB-|-RRB-)$/=target" });
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
/* 159 */   public static final GrammaticalRelation ARGUMENT = new GrammaticalRelation("arg", "argument", GrammaticalRelation.DEPENDENT, null, new String[0]);
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
/* 171 */   public static final GrammaticalRelation SUBJECT = new GrammaticalRelation("subj", "subject", ARGUMENT, null, new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */   public static final GrammaticalRelation NOMINAL_SUBJECT = new GrammaticalRelation("nsubj", "nominal subject", SUBJECT, "S|SQ|SBARQ|SINV|SBAR", new String[] { "S < ((NP|WHNP=target !< EX !< (/^NN/ < (/^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/))) $++ VP)", "S < ( NP=target < (/^NN/ < /^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/) !$++ NP $++VP)", "SQ < ((NP=target !< EX) $++ VP)", "SQ < ((NP=target !< EX) $- /^VB/ !$++ VP)", "SQ < ((NP=target !< EX) $- (RB $- /^VB/) ![$++ VP])", "SBARQ < WHNP=target < (SQ < (VP ![$-- NP]))", "SBARQ < (SQ=target < /^VB/ !< VP)", "SINV < (VP|VBZ|VBD $+ /^NP|WHNP$/=target)", "S < (NP=target $+ NP|ADJP) > VP", "SBAR <, WHNP=target < (S < (VP !$-- NP) !< SBAR)", "SBAR !< WHNP < (S !< (NP $++ VP)) > (VP > (S $- WHNP=target))", "SQ < ((NP < EX) $++ NP=target)", "S < (NP < EX) < (VP < NP=target)" });
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
/* 198 */   public static final GrammaticalRelation NOMINAL_PASSIVE_SUBJECT = new GrammaticalRelation("nsubjpass", "nominal passive subject", NOMINAL_SUBJECT, "S|VP", new String[] { "S < /^NP|WHNP$/=target < (VP|SQ < (VP < VBN|VBD) < (/^(VB|AUX)/ < /be|was|is|are|were|been|being|'s|'re|'m|am|Been|Being|WAS|IS|get|got|getting|gets|Get|gotten|becomes|become|became|felt|feels|feel|seems|seem|seemed|remains|remained|remain/))", "S < /^(NP|WHNP)$/=target < (VP|SQ <+(VP) (VP < VBN|VBD > (VP < (/^(VB|AUX)/ < /be|was|is|are|were|been|being|'s|'re|'m|am|Been|Being|WAS|IS|get|got|getting|gets|Get|gotten|becomes|become|became|felt|feels|feel|seems|seem|seemed|remains|remained|remain/))))" });
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
/* 209 */   public static final GrammaticalRelation CLAUSAL_SUBJECT = new GrammaticalRelation("csubj", "clausal subject", SUBJECT, "S", new String[] { "S < (SBAR|S=target !$+ /^,$/ $++ (VP !$-- NP))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 219 */   public static final GrammaticalRelation CLAUSAL_PASSIVE_SUBJECT = new GrammaticalRelation("csubjpass", "clausal subject", CLAUSAL_SUBJECT, "S", new String[] { "S < (SBAR|S=target !$+ /^,$/ $++ (VP < (VP < VBN|VBD) < (/^(VB|AUX)/ < /be|was|is|are|were|been|being|'s|'re|'m|am|Been|Being|WAS|IS|get|got|getting|gets|Get|gotten|becomes|become|became|felt|feels|feel|seems|seem|seemed|remains|remained|remain/) !$-- NP))", "S < (SBAR|S=target !$+ /^,$/ $++ (VP <+(VP) (VP < VBN|VBD > (VP < (/^(VB|AUX)/ < /be|was|is|are|were|been|being|'s|'re|'m|am|Been|Being|WAS|IS|get|got|getting|gets|Get|gotten|becomes|become|became|felt|feels|feel|seems|seem|seemed|remains|remained|remain/))) !$-- NP))" });
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
/* 237 */   public static final GrammaticalRelation COMPLEMENT = new GrammaticalRelation("comp", "complement", ARGUMENT, null, new String[0]);
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
/* 250 */   public static final GrammaticalRelation OBJECT = new GrammaticalRelation("obj", "object", COMPLEMENT, null, new String[0]);
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
/* 262 */   public static final GrammaticalRelation DIRECT_OBJECT = new GrammaticalRelation("dobj", "direct object", OBJECT, "SBARQ|VP|SBAR", new String[] { "VP < (NP $+ (/^NP|WHNP$/=target !< (/^NN/ < /^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/))) !<(/^VB/ < /^(am|is|are|being|Being|be|'s|'re|'m|was|were|seem|seems|seemed|appear|appears|appeared|stay|stays|stayed|remain|remains|remained|resemble|resembles|resembled|become|becomes|became)$/) ", "VP < (NP < (NP $+ (/^NP|WHNP$/=target !< (/^NN/ < /^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/))))!< (/^VB/ < /^(am|is|are|be|being|Being|'s|'re|'m|was|were|seem|seems|seemed|appear|appears|appeared|stay|stays|stayed|remain|remains|remained|resemble|resembles|resembled|become|becomes|became)$/)", "VP !<(/^VB/ < /^(am|is|are|be|being|Being|'s|'re|'m|was|were|seem|seems|seemed|appear|appears|appeared|stay|stays|stayed|remain|remains|remained|resemble|resembles|resembled|become|becomes|became)$/) < (/^(NP|WHNP)$/=target !< (/^NN/ < /^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/) !$+ NP)", "VP !<(/^VB/ < /^(am|is|are|be|being|Being|'s|'re|'m|was|were|seem|seems|seemed|appear|appears|appeared|stay|stays|stayed|remain|remains|remained|resemble|resembles|resembled|become|becomes|became)$/) < (/^(NP|WHNP)$/=target $+ NP-TMP)", "VP !<(/^VB/ < /^(am|is|are|be|being|Being|'s|'re|'m|was|were|seem|seems|seemed|appear|appears|appeared|stay|stays|stayed|remain|remains|remained|resemble|resembles|resembled|become|becomes|became)$/) < (/^(NP|WHNP)$/=target $+ (NP < (/^NN/ < /^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/)))", "SBARQ <, (WHNP=target !< WRB) << (VP !< (S < (VP < TO)) $-- NP)", "SBAR <, (WHNP=target !< WRB) < (S < NP < (VP !< SBAR !< (S < (VP < TO))))", "SBAR !< WHNP < (S < (NP $++ (VP !$++ NP))) > (VP > (S < NP $- WHNP=target))" });
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
/* 284 */   public static final GrammaticalRelation INDIRECT_OBJECT = new GrammaticalRelation("iobj", "indirect object", OBJECT, "VP", new String[] { "VP <(NP=target !< (/^NN/ < /^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/) $+ (NP !< (/^NN/ < /^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/)))", "VP < (NP=target < (NP $++ NP !$ CC !$ CONJP !$ /^,$/ !$++ /^:$/))" });
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
/* 297 */   public static final GrammaticalRelation PREPOSITIONAL_OBJECT = new GrammaticalRelation("pobj", "prepositional object", OBJECT, "^PP(?:-TMP)?$", new String[] { "/^PP(?:-TMP)?$/ < /^IN|VBG|TO/ < /^NP(?:-TMP)?$/=target", "/^PP(?:-TMP)?$/ < (/^IN|VBG|TO/ $+ (ADVP=target < (ADVP < /^NP(?:-TMP)?$/)))" });
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
/* 310 */   public static final GrammaticalRelation PREPOSITIONAL_COMPLEMENT = new GrammaticalRelation("pcomp", "prepositional complement", OBJECT, "^PP(?:-TMP)?$", new String[] { "/^PP(?:-TMP)?$/ < (IN $+ SBAR|S=target)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 317 */   public static final GrammaticalRelation ATTRIBUTIVE = new GrammaticalRelation("attr", "attributive", COMPLEMENT, "VP|SBARQ|SQ", new String[] { "VP !$ (NP < EX) < NP=target <(/^VB/ < /^(am|is|are|be|being|'s|'re|'m|was|were|seem|seems|seemed|appear|appears|appeared|stay|stays|stayed|remain|remains|remained|resemble|resembles|resembled|become|becomes|became)$/)", "SBARQ < (WHNP=target $+ (SQ < (/^VB/ < /^(am|is|are|'s|'re|'m|was|were|seem|seems|seemed|appear|appears|appeared|stay|stays|stayed|remain|remains|remained|resemble|resembles|resembled|become|becomes|became)$/ !$++ (VP < VBG))))", "SQ <, (/^VB/ < /^(Am|am|Is|is|Are|are|be|being|'s|'re|'m|Was|was|Were|were)$/) < (NP=target $-- (NP !< EX))" });
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
/* 336 */   public static final GrammaticalRelation CLAUSAL_COMPLEMENT = new GrammaticalRelation("ccomp", "clausal complement", COMPLEMENT, "VP|SINV|S|ADJP", new String[] { "VP < (S=target < (VP !<, TO|VBG) !$-- NP)", "VP < (SBAR=target < (S <+(S) VP) <, (IN|DT < /^(that|whether)$/))", "VP < (SBAR=target < (S < VP) !$-- NP !<, IN)", "S|SINV < (S|SBARQ=target $+ /^(,|.|'')$/ !$- /^:|CC$/ !< (VP < TO|VBG))", "ADJP < (SBAR=target < (S < VP))", "S <, (SBAR=target <, (IN < /^([Tt]hat|[Ww]hether)$/) !$+ VP)" });
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
/* 354 */   public static final GrammaticalRelation XCLAUSAL_COMPLEMENT = new GrammaticalRelation("xcomp", "xclausal complement", COMPLEMENT, "VP|ADJP", new String[] { "VP !> (VP < (VB < be)) < (S=target !$- (NN < /^order$/) < (VP < TO))", "ADJP < (S=target <, (VP <, TO))", "VP < (S=target !$- (NN < /^order$/) < (NP $+ NP|ADJP))", "VP < (/^VB/ $+ (VP=target < VB < NP))", "VP !> (VP < (VB < be)) < (SBAR=target < (S !$- (NN < /^order$/) < (VP < TO)))", "VP > VP < (S=target !$- (NN < /^order$/) <: NP)" });
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
/* 367 */   public static final GrammaticalRelation COMPLEMENTIZER = new GrammaticalRelation("complm", "complementizer", COMPLEMENT, "SBAR", new String[] { "SBAR <, (IN|DT=target < /^(that|whether)$/) $-- /^VB/", "SBAR <, (IN|DT=target < /^(that|whether)$/) $- NP", "SBAR <, (IN|DT=target < /^(that|whether)$/) > ADJP|PP", "SBAR <, (IN|DT=target < /^(That|Whether)$/)" });
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
/* 384 */   public static final GrammaticalRelation MARKER = new GrammaticalRelation("mark", "marker", COMPLEMENT, "^SBAR(?:-TMP)?$", new String[] { "/^SBAR(?:-TMP)?$/ <, (IN=target !< /^([Tt]hat|[Ww]hether)$/) < S" });
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
/* 399 */   public static final GrammaticalRelation RELATIVE = new GrammaticalRelation("rel", "relative", COMPLEMENT, "SBAR", new String[] { "SBAR <, /^WH/=target > NP" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 409 */   public static final GrammaticalRelation REFERENT = new GrammaticalRelation("ref", "referent", GrammaticalRelation.DEPENDENT, "NP", new String[] { "NP $+ (SBAR < (WHNP=target !< /^WP\\$/)) > NP", "NP $+ (SBAR < (WHPP < (WHNP=target !< /^WP\\$/))) > NP", "NP $+ (/^(,|PP|PRN)$/ $+ (SBAR < (WHNP=target !< /^WP\\$/)))", "NP $+ (/^(,|PP|PRN)$/ $+ (SBAR < (WHPP < (WHNP=target !< /^WP\\$/)))) > NP" });
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
/* 424 */   public static final GrammaticalRelation EXPLETIVE = new GrammaticalRelation("expl", "expletive", GrammaticalRelation.DEPENDENT, "S|SQ", new String[] { "S|SQ < (NP=target < EX)" });
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
/* 438 */   public static final GrammaticalRelation ADJECTIVAL_COMPLEMENT = new GrammaticalRelation("acomp", "adjectival complement", COMPLEMENT, "VP", new String[] { "VP < (ADJP=target !$-- NP)" });
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
/* 452 */   public static final GrammaticalRelation MODIFIER = new GrammaticalRelation("mod", "modifier", GrammaticalRelation.DEPENDENT, null, new String[0]);
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
/* 465 */   public static final GrammaticalRelation ADV_CLAUSE_MODIFIER = new GrammaticalRelation("advcl", "adverbial clause modifier", MODIFIER, "VP|S|SQ", new String[] { "VP < (/^SBAR(?:-TMP)?$/=target <, (IN !< /^([Tt]hat|[Ww]hether)$/ !$+ (NN < /^order$/)))", "S|SQ <, (/^SBAR(?:-TMP)?$/=target <, (IN !< /^([Tt]hat|[Ww]hether)$/ !$+ (NN < /^order$/)) !$+ VP)", "S|SQ <, (/^SBAR(?:-TMP)?$/=target <2 (IN !< /^([Tt]hat|[Ww]hether)$/ !$+ (NN < /^order$/)))", "S|SQ <, (SBAR=target <, (WHADVP|WHNP < WRB))", "S|SQ <, (PP=target <, RB)" });
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
/* 482 */   public static final GrammaticalRelation PURPOSE_CLAUSE_MODIFIER = new GrammaticalRelation("purpcl", "purpose clause modifier", MODIFIER, "VP", new String[] { "VP < (/^SBAR/=target < (IN < in) < (NN < order) < (S < (VP < TO)))", "VP > (VP < (VB < be)) < (S=target < (VP < TO|VBG) !$-- NP)" });
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
/* 495 */   public static final GrammaticalRelation TEMPORAL_MODIFIER = new GrammaticalRelation("tmod", "temporal modifier", MODIFIER, "VP|S|ADJP", new String[] { "VP|ADJP < /^NP-TMP$/=target", "VP < (NP=target < (/^NN/ < /^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/))", "S < (/^NP-TMP$/=target $++ (NP $++ VP))", "S < (NP=target < (/^NN/ < /^Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday|years?|months?|weeks?|days?|mornings?|evenings?|January|February|March|April|May|June|July|August|September|October|November|December|[Tt]oday|[Yy]esterday|[Tt]omorrow|[Ss]pring|[Ss]ummer|[Ff]all|[Aa]utumn|[Ww]inter$/) $++ (NP $++ VP))" });
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
/* 513 */   public static final GrammaticalRelation RELATIVE_CLAUSE_MODIFIER = new GrammaticalRelation("rcmod", "relative clause modifier", MODIFIER, "NP", new String[] { "NP $++ (SBAR=target < WHPP|WHNP) > NP", "NP $++ (SBAR=target <: S) > NP", "NP $++ (SBAR=target < (WHADVP < (WRB </^(where|why)/))) > NP", "NP $++ RRC=target" });
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
/* 526 */   public static final GrammaticalRelation ADJECTIVAL_MODIFIER = new GrammaticalRelation("amod", "adjectival modifier", MODIFIER, "^NP(?:-TMP|-ADV)?|NX|WHNP$", new String[] { "/^NP(?:-TMP|-ADV)?|NX|WHNP$/ < (ADJP|WHADJP|JJ|JJR|JJS|VBN|VBG|VBD=target !$- CC)" });
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
/* 537 */   public static final GrammaticalRelation NUMERIC_MODIFIER = new GrammaticalRelation("num", "numeric modifier", MODIFIER, "NP(?:-TMP|-ADV)?", new String[] { "/^NP(?:-TMP|-ADV)?$/ < (CD|QP=target !$- CC)", "/^NP(?:-TMP|-ADV)?$/ < (ADJP=target <: QP)" });
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
/* 552 */   public static final GrammaticalRelation NUMBER_MODIFIER = new GrammaticalRelation("number", "compound number modifier", MODIFIER, "QP", new String[] { "QP < (CD=target !$- CC)" });
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
/* 566 */   public static final GrammaticalRelation QUANTIFIER_MODIFIER = new GrammaticalRelation("quantmod", "quantifier modifier", MODIFIER, "QP", new String[] { "QP < /^IN|RB|DT|JJ|XS$/=target" });
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
/* 580 */   public static final GrammaticalRelation NOUN_COMPOUND_MODIFIER = new GrammaticalRelation("nn", "nn modifier", MODIFIER, "^NP(?:-TMP|-ADV)?$", new String[] { "/^NP(?:-TMP|-ADV)?$/ < (NP|NN|NNS|NNP|NNPS|FW=target $++ NN|NNS|NNP|NNPS|FW|CD !<- POS !$- /^,$/ )", "/^NP(?:-TMP|-ADV)?$/ < (NP|NN|NNS|NNP|NNPS|FW=target !<- POS $+ JJ|JJR|JJS) <# NN|NNS|NNP|NNPS !<- POS" });
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
/* 592 */   public static final GrammaticalRelation APPOSITIONAL_MODIFIER = new GrammaticalRelation("appos", "appositional modifier", MODIFIER, "^NP(?:-TMP|-ADV)?$", new String[] { "/^NP(?:-TMP|-ADV)?$/ < (NP=target $- /^,$/ $-- NP !$ CC|CONJP)", "/^NP(?:-TMP|-ADV)?$/ < (PRN=target < (NP < /^NNS?|CD$/ $-- /^-LRB-$/ $+ /^-RRB-$/))", "/^NP(?:-TMP|-ADV)?$/ < (NNP $+ (/^,$/ $+ NNP=target)) !< CC|CONJP" });
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
/* 610 */   public static final GrammaticalRelation ABBREVIATION_MODIFIER = new GrammaticalRelation("abbrev", "abbreviation modifier", APPOSITIONAL_MODIFIER, "^NP(?:-TMP|-ADV)?$", new String[] { "/^NP(?:-TMP|-ADV)?$/ < (PRN=target < (NP < NNP $- /^-LRB-$/ $+ /^-RRB-$/))", "/^NP(?:-TMP|-ADV)?$/ < (PRN=target < (NNP $- /^-LRB-$/ $+ /^-RRB-$/))" });
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
/* 623 */   public static final GrammaticalRelation PARTICIPIAL_MODIFIER = new GrammaticalRelation("partmod", "participial modifier", MODIFIER, "^NP(?:-TMP|-ADV)?|VP|S$", new String[] { "/^NP(?:-TMP|-ADV)?$/ < (VP=target < VBG|VBN $-- NP)", "VP < (S=target !< NP < (VP < VBG))", "/^NP(?:-TMP|-ADV)?$/ < (/^,$/ $+ (VP=target <, VBG|VBN))", "S <, (NP $+ (/^,$/ $+ (S=target < (VP <, VBG|VBN))))" });
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
/* 638 */   public static final GrammaticalRelation INFINITIVAL_MODIFIER = new GrammaticalRelation("infmod", "infinitival modifier", MODIFIER, "^NP(?:-TMP|-ADV)?$", new String[] { "/^NP(?:-[A-Z]+)?$/ < (S=target < (VP < TO) $-- /^NP|NNP?S?$/)" });
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
/* 651 */   public static final GrammaticalRelation ADVERBIAL_MODIFIER = new GrammaticalRelation("advmod", "adverbial modifier", MODIFIER, "VP|ADJP|WHADJP|ADVP|WHADVP|S|SBAR|SINV|SQ|SBARQ|XS|NP(?:-TMP|-ADV)?|RRC", new String[] { "/^VP|ADJP|WHADJP|S|SBAR|SINV|SQ|XS|NP(?:-TMP|-ADV)?|RRC$/ < RB|RBR|RBS|WRB|ADVP|WHADVP=target", "ADVP|WHADVP < RB|RBR|RBS|WRB|ADVP|WHADVP|JJ=target !< CC !< CONJP", "SBAR < (WHNP=target < WRB)", "SBARQ <, WHADVP=target", "XS < /^JJ$/=target" });
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
/* 672 */   public static final GrammaticalRelation NEGATION_MODIFIER = new GrammaticalRelation("neg", "negation modifier", ADVERBIAL_MODIFIER, "VP|ADJP|S|SBAR|SINV|SQ", new String[] { "VP|ADJP|SQ|S < (RB=target < /not|n't|never/)", "VP|ADJP|S|SBAR|SINV < (ADVP=target < (RB < /not|n't|never/))", "VP > SQ  $-- (RB=target < /not|n't|never/)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 682 */   public static final GrammaticalRelation MEASURE_PHRASE = new GrammaticalRelation("measure", "measure-phrase", MODIFIER, "ADJP|ADVP", new String[] { "ADJP <- JJ <, (NP=target !< NNP)", "ADVP|ADJP <# (/^JJ|IN$/ $- NP=target)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 692 */   public static final GrammaticalRelation DETERMINER = new GrammaticalRelation("det", "determiner", MODIFIER, "^NP(?:-TMP|-ADV)?|WHNP", new String[] { "/^NP(?:-TMP|-ADV)?$/ < (DT=target !</both|either|neither/ !$- DT !$++ CC $++ /^N[NX]/)", "/^NP(?:-TMP|-ADV)?$/ < (DT=target < /both|either|neither/ !$- DT !$++ CC $++ /^N[NX]/ !$++ (NP < CC))", "/^NP(?:-TMP|-ADV)?$/ < (DT=target !< /both|neither|either/ $++ CC $++ /^N[NX]/)", "/^NP(?:-TMP|-ADV)?$/ < (DT=target $++ (/^JJ/ !$+ /^NN/) !$++CC)", "/^NP(?:-TMP|-ADV)?$/ < (RB=target $++ (/P?DT/ $+ /^NN/))", "WHNP < (NP $-- (WHNP=target < WDT))", "WHNP < (/^NN/ $-- WDT|WP=target)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 701 */   public static final GrammaticalRelation PREDETERMINER = new GrammaticalRelation("predet", "predeterminer", MODIFIER, "^NP(?:-TMP|-ADV)?", new String[] { "/^NP(?:-TMP|-ADV)?$/ < (PDT|DT=target $+ /DT|PRP\\$/ $++ /^N[NX]/ !$++ CC)", "/^NP(?:-TMP|-ADV)?$/ < (PDT|DT=target $+ DT $++ (/^JJ/ !$+ /^NN/)) !$++ CC" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 710 */   public static final GrammaticalRelation PRECONJUNCT = new GrammaticalRelation("preconj", "preconjunct", MODIFIER, "S|VP|ADJP|PP|ADVP|UCP|NX|SBAR|^NP(?:-TMP|-ADV)?", new String[] { "/^NP(?:-TMP|-ADV)?|NX$/ < (PDT|CC=target < /both|neither|either/ $++ /^N[NX]/) $++ CC", "/^NP(?:-TMP|-ADV)?|NX$/ < (PDT|CC=target < /both|either|neither/ $++ (/^JJ/ !$+ /^NN/)) $++ CC", "/^NP(?:-TMP|-ADV)?|NX$/ < (PDT|CC|DT=target < /both|either|neither/ $++ CC)", "/^NP(?:-TMP|-ADV)?|NX$/ < (PDT|CC|DT=target </both|either|neither/) < (NP < CC)", "S|VP|ADJP|PP|ADVP|UCP|NX|SBAR < (PDT|DT|CC=target < /both|either|neither/ $++ CC)" });
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
/* 721 */   public static final GrammaticalRelation POSSESSION_MODIFIER = new GrammaticalRelation("poss", "possession modifier", MODIFIER, "^NP(?:-TMP|-ADV)?$", new String[] { "/^NP(?:-TMP|-ADV)?$/ < (/^PRP\\$/=target $++ /^NN/)", "/^NP(?:-TMP|-ADV)?$/ < (NP=target < POS)", "/^NP(?:-TMP|-ADV)?$/ < (NNS=target $+ (POS < /'/))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 731 */   public static final GrammaticalRelation POSSESSIVE_MODIFIER = new GrammaticalRelation("possessive", "possessive modifier", MODIFIER, "^NP(?:-TMP|-ADV)?$", new String[] { "/^NP(?:-TMP|-ADV)?$/ <- POS=target" });
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
/* 746 */   public static final GrammaticalRelation PREPOSITIONAL_MODIFIER = new GrammaticalRelation("prep", "prepositional modifier", MODIFIER, "NP(?:-TMP|-ADV)?|VP|S|SINV|ADJP", new String[] { "/^NP(?:-TMP|-ADV)?|VP|ADJP$/ < /^PP(?:-TMP)?$/=target", "S|SINV < (/^PP(?:-TMP)?$/=target !< SBAR) < VP" });
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
/* 761 */   public static final GrammaticalRelation PHRASAL_VERB_PARTICLE = new GrammaticalRelation("prt", "phrasal verb particle", MODIFIER, "VP", new String[] { "VP < PRT=target" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 768 */   public static final GrammaticalRelation SEMANTIC_DEPENDENT = new GrammaticalRelation("sdep", "semantic dependent", GrammaticalRelation.DEPENDENT, null, new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 777 */   public static final GrammaticalRelation CONTROLLING_SUBJECT = new GrammaticalRelation("xsubj", "controlling subject", SEMANTIC_DEPENDENT, "VP", new String[] { "VP < TO > (S !$- NP !< NP !>> (VP < (VB < be)) >+(VP) (VP $-- NP=target))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 787 */   public static final GrammaticalRelation AGENT = new GrammaticalRelation("agent", "agent", GrammaticalRelation.DEPENDENT, null, new String[0]);
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
/* 800 */   private static final GrammaticalRelation[] values = { GrammaticalRelation.GOVERNOR, GrammaticalRelation.DEPENDENT, PREDICATE, ATTRIBUTIVE, AUX_MODIFIER, AUX_PASSIVE_MODIFIER, COPULA, CONJUNCT, COORDINATION, PUNCTUATION, ARGUMENT, SUBJECT, NOMINAL_SUBJECT, NOMINAL_PASSIVE_SUBJECT, CLAUSAL_SUBJECT, CLAUSAL_PASSIVE_SUBJECT, COMPLEMENT, OBJECT, DIRECT_OBJECT, INDIRECT_OBJECT, PREPOSITIONAL_OBJECT, PREPOSITIONAL_COMPLEMENT, CLAUSAL_COMPLEMENT, XCLAUSAL_COMPLEMENT, COMPLEMENTIZER, MARKER, RELATIVE, REFERENT, EXPLETIVE, ADJECTIVAL_COMPLEMENT, MODIFIER, ADV_CLAUSE_MODIFIER, TEMPORAL_MODIFIER, RELATIVE_CLAUSE_MODIFIER, NUMERIC_MODIFIER, ADJECTIVAL_MODIFIER, NOUN_COMPOUND_MODIFIER, APPOSITIONAL_MODIFIER, ABBREVIATION_MODIFIER, PARTICIPIAL_MODIFIER, INFINITIVAL_MODIFIER, ADVERBIAL_MODIFIER, NEGATION_MODIFIER, DETERMINER, PREDETERMINER, PRECONJUNCT, POSSESSION_MODIFIER, POSSESSIVE_MODIFIER, PREPOSITIONAL_MODIFIER, PHRASAL_VERB_PARTICLE, SEMANTIC_DEPENDENT, CONTROLLING_SUBJECT, AGENT, NUMBER_MODIFIER, PURPOSE_CLAUSE_MODIFIER, QUANTIFIER_MODIFIER, MEASURE_PHRASE };
/*     */   
/*     */ 
/*     */ 
/* 804 */   private static final Map<String, GrammaticalRelation> conjs = new HashMap();
/*     */   
/* 806 */   public static Collection<GrammaticalRelation> getConjs() { return conjs.values(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GrammaticalRelation getConj(String conjunctionString)
/*     */   {
/* 816 */     GrammaticalRelation result = (GrammaticalRelation)conjs.get(conjunctionString);
/* 817 */     if (result == null) {
/* 818 */       result = new GrammaticalRelation("conj", "conj_collapsed", GrammaticalRelation.DEPENDENT, null, StringUtils.EMPTY_STRING_ARRAY, conjunctionString);
/*     */       
/* 820 */       conjs.put(conjunctionString, result);
/*     */     }
/* 822 */     return result;
/*     */   }
/*     */   
/*     */ 
/* 826 */   private static final Map<String, GrammaticalRelation> preps = new HashMap();
/* 827 */   private static final Map<String, GrammaticalRelation> prepsC = new HashMap();
/*     */   
/*     */   public static Collection<GrammaticalRelation> getPreps() {
/* 830 */     return preps.values();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GrammaticalRelation getPrep(String prepositionString)
/*     */   {
/* 840 */     GrammaticalRelation result = (GrammaticalRelation)preps.get(prepositionString);
/* 841 */     if (result == null) {
/* 842 */       result = new GrammaticalRelation("prep", "prep_collapsed", GrammaticalRelation.DEPENDENT, null, StringUtils.EMPTY_STRING_ARRAY, prepositionString);
/*     */       
/* 844 */       preps.put(prepositionString, result);
/*     */     }
/* 846 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GrammaticalRelation getPrepC(String prepositionString)
/*     */   {
/* 858 */     GrammaticalRelation result = (GrammaticalRelation)prepsC.get(prepositionString);
/* 859 */     if (result == null) {
/* 860 */       result = new GrammaticalRelation("prepc", "prepc_collapsed", GrammaticalRelation.DEPENDENT, null, StringUtils.EMPTY_STRING_ARRAY, prepositionString);
/*     */       
/* 862 */       prepsC.put(prepositionString, result);
/*     */     }
/* 864 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GrammaticalRelation valueOf(String s)
/*     */   {
/* 876 */     for (GrammaticalRelation reln : values) {
/* 877 */       if (reln.toString().equals(s)) { return reln;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 885 */     String[] tuples = s.trim().split("_", 2);
/* 886 */     if (tuples.length == 2) {
/* 887 */       String reln = tuples[0];
/* 888 */       String specific = tuples[1];
/* 889 */       if (reln.equals(PREPOSITIONAL_MODIFIER.getShortName()))
/* 890 */         return getPrep(specific);
/* 891 */       if (reln.equals(CONJUNCT.getShortName())) {
/* 892 */         return getConj(specific);
/*     */       }
/*     */     }
/*     */     
/* 896 */     return null;
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
/*     */   public static GrammaticalRelation valueOf(Object o)
/*     */   {
/* 910 */     if ((o instanceof GrammaticalRelation))
/* 911 */       return (GrammaticalRelation)o;
/* 912 */     if ((o instanceof String)) {
/* 913 */       return valueOf((String)o);
/*     */     }
/* 915 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 926 */     System.out.println(GrammaticalRelation.DEPENDENT.toPrettyString());
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\EnglishGrammaticalRelations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */