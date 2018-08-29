/*     */ package edu.stanford.nlp.trees.international.pennchinese;
/*     */ 
/*     */ import edu.stanford.nlp.trees.GrammaticalRelation;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChineseGrammaticalRelations
/*     */ {
/*     */   public static List<GrammaticalRelation> values()
/*     */   {
/*  25 */     return Collections.unmodifiableList(Arrays.asList(values));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   public static final GrammaticalRelation PREDICATE = new GrammaticalRelation("pred", "predicate", GrammaticalRelation.DEPENDENT, "IP", new String[] { " IP=target !> IP" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   public static final GrammaticalRelation ARGUMENT = new GrammaticalRelation("arg", "argument", GrammaticalRelation.DEPENDENT, null, new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   public static final GrammaticalRelation CONJUNCT = new GrammaticalRelation("conj", "conjunct", GrammaticalRelation.DEPENDENT, "VP|NP|ADJP|PP|ADVP|UCP", new String[] { "VP|NP|ADJP|PP|ADVP|UCP < (!PU=target $+ CC)", "VP|NP|ADJP|PP|ADVP|UCP < ( __=target $+ PU $+ CC)", "VP|NP|ADJP|PP|ADVP|UCP < ( __=target $+ (PU < /\\u3001/) )", "PP < (PP $+ PP=target )", "NP <( NP=target $+  PU $+  NP )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */   public static final GrammaticalRelation AUX_MODIFIER = new GrammaticalRelation("cop", "copula", GrammaticalRelation.DEPENDENT, "VP", new String[] { " VP < VC=target" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   public static final GrammaticalRelation COORDINATION = new GrammaticalRelation("cc", "coordination", GrammaticalRelation.DEPENDENT, "VP|NP|ADJP|PP|ADVP|UCP|IP|QP", new String[] { "VP|NP|ADJP|PP|ADVP|UCP|IP|QP < (CC=target)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   public static final GrammaticalRelation PUNCTUATION = new GrammaticalRelation("punct", "punctuation", GrammaticalRelation.DEPENDENT, "VP|NP|PP|IP|CP", new String[] { "__ < PU=target" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   public static final GrammaticalRelation SUBJECT = new GrammaticalRelation("subj", "subject", ARGUMENT, null, new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 112 */   public static final GrammaticalRelation NOMINAL_SUBJECT = new GrammaticalRelation("nsubj", "nominal subject", SUBJECT, "IP|VP", new String[] { " IP <( ( NP|QP=target!< NT ) $++ ( /^VP|VCD|IP/  !< VE !<VC !<SB !<LB  ))", " NP !$+ VP < ( (  NP|DP|QP=target !< NT ) $+ ( /^VP|VCD/ !<VE !< VC !<SB !<LB))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 125 */   public static final GrammaticalRelation EXT_SUBJECT = new GrammaticalRelation("top", "topic", SUBJECT, "IP|VP", new String[] { "IP|VP < ( NP|DP=target $+ ( VP < VC|VE ) )", "IP < (IP=target $+ ( VP < VC|VE))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 132 */   public static final GrammaticalRelation TOP_SUBJECT = new GrammaticalRelation("topic", "topic", SUBJECT, "IP", new String[] { " VP !> IP < ( NP=target $++ NP $++  VP  )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 138 */   public static final GrammaticalRelation NOMINAL_PASSIVE_SUBJECT = new GrammaticalRelation("npsubj", "nominal passive subject", NOMINAL_SUBJECT, "IP", new String[] { "IP < (NP=target $+ (VP|IP < SB|LB))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */   public static final GrammaticalRelation CLAUSAL_SUBJECT = new GrammaticalRelation("csubj", "clausal subject", SUBJECT, "IP", new String[] { "IP < (IP=target $+ ( VP !< VC))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */   public static final GrammaticalRelation COMPLEMENT = new GrammaticalRelation("comp", "complement", ARGUMENT, null, new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */   public static final GrammaticalRelation OBJECT = new GrammaticalRelation("obj", "object", COMPLEMENT, null, new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */   public static final GrammaticalRelation DIRECT_OBJECT = new GrammaticalRelation("dobj", "direct object", OBJECT, "CP|VP", new String[] { "VP < ( /^V*/ $+  NP $+ NP|DP=target ) !< VC ", " VP < ( /^V*/ $+ NP|DP=target ! $+ NP|DP) !< VC ", "CP < (IP $++ NP=target ) !<< VC" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 178 */   public static final GrammaticalRelation INDIRECT_OBJECT = new GrammaticalRelation("iobj", "indirect object", OBJECT, "VP", new String[] { " CP !> VP < ( VV $+ ( NP|DP|QP|CLP=target . NP|DP ) )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */   public static final GrammaticalRelation RANGE = new GrammaticalRelation("range", "range", OBJECT, "VP", new String[] { " VP < ( NP|DP|QP $+ NP|DP|QP=target)", "VP < ( VV $+ QP=target )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */   public static final GrammaticalRelation PREPOSITIONAL_OBJECT = new GrammaticalRelation("pobj", "prepositional object", OBJECT, "^PP", new String[] { "/^PP/ < /^P/ < /^NP|^DP|QP/=target" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */   public static final GrammaticalRelation TIME_POSTPOSITION = new GrammaticalRelation("lobj", "localizer object", OBJECT, "LCP", new String[] { "LCP < ( NP|QP|DP=target $+ LC)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 230 */   public static final GrammaticalRelation ATTRIBUTIVE = new GrammaticalRelation("attr", "attributive", COMPLEMENT, "VP", new String[] { "VP < /^VC$/ < NP|QP=target" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 247 */   public static final GrammaticalRelation CLAUSAL_COMPLEMENT = new GrammaticalRelation("ccomp", "clausal complement", COMPLEMENT, "VP|ADJP|IP", new String[] { "VP|ADJP|IP < IP|VP|VRD|VCD=target" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 254 */   public static final GrammaticalRelation XCLAUSAL_COMPLEMENT = new GrammaticalRelation("xcomp", "xclausal complement", COMPLEMENT, "VP|ADJP", new String[] { "VP !> (/^VP/ < /^VC$/ ) < (IP=target < (VP < P))", "ADJP < (IP=target <, (VP <, P))", "VP < (IP=target < (NP $+ NP|ADJP))", "VP < (/^VC/ $+ (VP=target < VC < NP))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 274 */   public static final GrammaticalRelation COMPLEMENTIZER = new GrammaticalRelation("cpm", "complementizer", COMPLEMENT, "^CP", new String[] { "/^CP/ < (__  $++ DEC=target)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 281 */   public static final GrammaticalRelation ADJECTIVAL_COMPLEMENT = new GrammaticalRelation("acomp", "adjectival complement", COMPLEMENT, "VP", new String[] { "VP < (ADJP=target !$-- NP)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 300 */   public static final GrammaticalRelation TIMEM = new GrammaticalRelation("tcomp", "temporal complement", COMPLEMENT, "VP|IP", new String[] { "VP|IP < (NP=target < NT !.. /^VC$/ $++  VP)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 315 */   public static final GrammaticalRelation LC_COMPLEMENT = new GrammaticalRelation("lccomp", "localizer complement", COMPLEMENT, "VP|IP", new String[] { "VP|IP < LCP=target " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 321 */   public static final GrammaticalRelation RES_VERB = new GrammaticalRelation("rcomp", "result verb", COMPLEMENT, "VRD", new String[] { "VRD < ( /V*/ $+ /V*/=target )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 331 */   public static final GrammaticalRelation MODIFIER = new GrammaticalRelation("mod", "modifier", GrammaticalRelation.DEPENDENT, null, new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 340 */   public static final GrammaticalRelation VERB_COMPOUND = new GrammaticalRelation("comod", "coordinated verb compound", MODIFIER, "VCD", new String[] { "VCD < ( VV|VA $+  VV|VA=target)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 352 */   public static final GrammaticalRelation MODAL_VERB = new GrammaticalRelation("mmod", "modal verb", MODIFIER, "VP", new String[] { "VP < ( VV=target $+ VP|VRD )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 360 */   public static final GrammaticalRelation AUX_PASSIVE_MODIFIER = new GrammaticalRelation("pass", "passive", MODIFIER, "VP", new String[] { "VP < SB|LB=target" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 366 */   public static final GrammaticalRelation BA = new GrammaticalRelation("ba", "ba", GrammaticalRelation.DEPENDENT, "VP|IP", new String[] { "VP|IP < BA=target " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 374 */   public static final GrammaticalRelation TEMPORAL_MODIFIER = new GrammaticalRelation("tmod", "temporal modifier", MODIFIER, "VP|IP|ADJP", new String[] { " VC|VE ! >> VP|ADJP < NP=target < NT", "VC|VE !>>IP <( NP=target < NT $++ VP !< VC|VE )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 391 */   public static final GrammaticalRelation TIME = new GrammaticalRelation("tclaus", "temporal clause", MODIFIER, "LCP", new String[] { "/LCP/ < ( IP=target $+ LC )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 415 */   public static final GrammaticalRelation RELATIVE_CLAUSE_MODIFIER = new GrammaticalRelation("rcmod", "relative clause modifier", MODIFIER, "NP", new String[] { "NP  $++ (CP=target ) > NP ", "NP  $++ (CP=target <: IP) > NP  ", "NP  $++ (CP=target)", " NP  << ( CP=target $++ NP  )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 428 */   public static final GrammaticalRelation NUMERIC_MODIFIER = new GrammaticalRelation("numod", "numeric modifier", MODIFIER, "QP|NP", new String[] { "QP < CD=target", "NP < (QP =target !< CLP )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 434 */   public static final GrammaticalRelation ODNUMERIC_MODIFIER = new GrammaticalRelation("ordmod", "numeric modifier", MODIFIER, "NP|QP", new String[] { "NP < QP=target < ( OD !$+ CLP )", "QP < (OD=target $+ CLP)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 446 */   public static final GrammaticalRelation NUMBER_MODIFIER = new GrammaticalRelation("clf", "classifier modifier", MODIFIER, "^NP|DP|QP", new String[] { "NP|QP < ( QP  =target << M $++ NP|QP)", "DP < ( DT $+ CLP=target )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 462 */   public static final GrammaticalRelation NOUN_COMPOUND_MODIFIER = new GrammaticalRelation("nmod", "nn modifier", MODIFIER, "^NP", new String[] { "NP < (NN|NR|NT=target $+ NN|NR|NT)", "NP < (NN|NR|NT $+ FW=target)", " NP <  (NP=target !$+ PU|CC $++ NP|PRN )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 477 */   public static final GrammaticalRelation ADJECTIVEL_MODIFIER = new GrammaticalRelation("amod", "adjectivel modifier", MODIFIER, "NP|CLP|QP", new String[] { "NP|CLP|QP < (ADJP=target $++ NP|CLP|QP ) " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 489 */   public static final GrammaticalRelation ADVERBIAL_MODIFIER = new GrammaticalRelation("advmod", "adverbial modifier", MODIFIER, "VP|ADJP|IP|CP|PP|NP|QP", new String[] { "VP|ADJP|IP|CP|PP|NP < ADVP=target", "VP|ADJP < AD|CS=target", "QP < (ADVP=target $+ QP)", "QP < ( QP $+ ADVP=target)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 497 */   public static final GrammaticalRelation IP_MODIFIER = new GrammaticalRelation("vmod", "participle modifier", MODIFIER, "NP", new String[] { "NP < IP=target " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 504 */   public static final GrammaticalRelation PRN_MODIFIER = new GrammaticalRelation("prnmod", "prn odifier", MODIFIER, "NP", new String[] { "NP < PRN=target " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 517 */   public static final GrammaticalRelation NEGATION_MODIFIER = new GrammaticalRelation("neg", "negation modifier", ADVERBIAL_MODIFIER, "VP|ADJP|IP", new String[] { "VP|ADJP|IP < (AD=target < /\\u4e0d/)", "VP|ADJP|IP < (ADVP=target < (AD < /\\u4e0d/))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 528 */   public static final GrammaticalRelation DETERMINER = new GrammaticalRelation("det", "determiner", MODIFIER, "^NP|DP", new String[] { "/^NP/ < (DP=target $++ NP )", "DP < DT < QP=target" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 534 */   public static final GrammaticalRelation POSSESSION_MODIFIER = new GrammaticalRelation("poss", "possession modifier", MODIFIER, "NP", new String[] { "NP < ( PN=target $+ DEC $+  NP )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 541 */   public static final GrammaticalRelation POSSESSIVE_MODIFIER = new GrammaticalRelation("possm", "possessive marker", MODIFIER, "NP", new String[] { "NP < ( PN $+ DEC=target ) " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 552 */   public static final GrammaticalRelation DVP_MODIFIER = new GrammaticalRelation("dvpm", "dvp marker", MODIFIER, "DVP", new String[] { " DVP < (__ $+ DEV=target ) " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 566 */   public static final GrammaticalRelation DVPM_MODIFIER = new GrammaticalRelation("dvpmod", "dvp modifier", MODIFIER, "VP", new String[] { " VP < ( DVP=target $+ VP) " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 577 */   public static final GrammaticalRelation ASSOCIATIVE_MODIFIER = new GrammaticalRelation("assm", "associative marker", MODIFIER, "DNP", new String[] { " DNP < ( __ $+ DEG=target ) " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 590 */   public static final GrammaticalRelation ASSOCIATIVEM_MODIFIER = new GrammaticalRelation("assmod", "associative modifier", MODIFIER, "NP|QP", new String[] { "NP|QP < ( DNP =target $++ NP|QP ) " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 612 */   public static final GrammaticalRelation PREPOSITIONAL_MODIFIER = new GrammaticalRelation("prep", "prepositional modifier", MODIFIER, "^NP|VP|IP", new String[] { "/^NP/ < /^PP/=target", "VP < /^PP/=target", "IP < /^PP/=target " });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 629 */   public static final GrammaticalRelation CL_MODIFIER = new GrammaticalRelation("clmpd", "clause modifier", MODIFIER, "^PP|IP", new String[] { "PP < (P $+ IP|VP =target)", "IP < (CP=target $++ VP)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 644 */   public static final GrammaticalRelation PREPOSTPOSITIONAL_MODIFIER = new GrammaticalRelation("plmod", "prepositional localizer modifier", MODIFIER, "PP", new String[] { "PP < ( P $++ LCP=target )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 654 */   public static final GrammaticalRelation PREDICATE_ASPECT = new GrammaticalRelation("asp", "aspect", MODIFIER, "VP", new String[] { "VP < ( /^V*/ $+ AS=target)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 660 */   public static final GrammaticalRelation PART_VERB = new GrammaticalRelation("partmod", "particle verb", MODIFIER, "VP|IP", new String[] { "VP|IP < ( MSP=target )" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 671 */   public static final GrammaticalRelation ETC = new GrammaticalRelation("etc", "ETC", MODIFIER, "^NP", new String[] { "/^NP/ < (NN|NR . ETC=target)" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 677 */   public static final GrammaticalRelation SEMANTIC_DEPENDENT = new GrammaticalRelation("sdep", "semantic dependent", GrammaticalRelation.DEPENDENT, null, new String[0]);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 704 */   public static final GrammaticalRelation CONTROLLED_SUBJECT = new GrammaticalRelation("xsubj", "controlled subject", SEMANTIC_DEPENDENT, "VP", new String[] { "VP !< NP < VP > (IP !$- NP !< NP !>> (VP < VC ) >+(VP) (VP $-- NP=target))" });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 709 */   private static final GrammaticalRelation[] values = { GrammaticalRelation.GOVERNOR, GrammaticalRelation.DEPENDENT, PREDICATE, AUX_MODIFIER, AUX_PASSIVE_MODIFIER, COORDINATION, PUNCTUATION, ARGUMENT, SUBJECT, NOMINAL_SUBJECT, CLAUSAL_SUBJECT, COMPLEMENT, OBJECT, DIRECT_OBJECT, INDIRECT_OBJECT, PREPOSITIONAL_OBJECT, ATTRIBUTIVE, CLAUSAL_COMPLEMENT, XCLAUSAL_COMPLEMENT, COMPLEMENTIZER, ADJECTIVAL_COMPLEMENT, MODIFIER, TEMPORAL_MODIFIER, RELATIVE_CLAUSE_MODIFIER, NUMERIC_MODIFIER, NUMBER_MODIFIER, NOUN_COMPOUND_MODIFIER, ADJECTIVEL_MODIFIER, ADVERBIAL_MODIFIER, NEGATION_MODIFIER, DETERMINER, POSSESSION_MODIFIER, POSSESSIVE_MODIFIER, PREPOSITIONAL_MODIFIER, PREDICATE_ASPECT, TIME_POSTPOSITION, VERB_COMPOUND, RES_VERB, MODAL_VERB, ETC, SEMANTIC_DEPENDENT, CONTROLLED_SUBJECT, TIME, BA, ASSOCIATIVE_MODIFIER, ASSOCIATIVEM_MODIFIER, CONJUNCT, PREPOSTPOSITIONAL_MODIFIER, DVP_MODIFIER, DVPM_MODIFIER, RANGE, TIMEM, CL_MODIFIER, EXT_SUBJECT, ODNUMERIC_MODIFIER, LC_COMPLEMENT, IP_MODIFIER, PRN_MODIFIER, PART_VERB, TOP_SUBJECT };
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\international\pennchinese\ChineseGrammaticalRelations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */