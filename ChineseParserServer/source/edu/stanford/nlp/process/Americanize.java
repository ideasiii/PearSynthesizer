/*     */ package edu.stanford.nlp.process;
/*     */ 
/*     */ import edu.stanford.nlp.ling.HasWord;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Americanize
/*     */   implements Function
/*     */ {
/*  27 */   private static boolean staticCapitalizeTimex = true;
/*     */   
/*  29 */   private boolean capitalizeTimex = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final int DONT_CAPITALIZE_TIMEX = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Americanize(int flags)
/*     */   {
/*  43 */     if ((flags & 0x1) != 0) {
/*  44 */       this.capitalizeTimex = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object apply(Object in)
/*     */   {
/*  56 */     if ((in instanceof HasWord)) {
/*  57 */       HasWord w = (HasWord)in;
/*  58 */       String str = w.word();
/*  59 */       String outStr = americanize(str, this.capitalizeTimex);
/*  60 */       if (!outStr.equals(str)) {
/*  61 */         w.setWord(outStr);
/*     */       }
/*  63 */       return w;
/*     */     }
/*     */     
/*  66 */     String str = (String)in;
/*  67 */     return americanize(str, this.capitalizeTimex);
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
/*     */   public static String americanize(String str)
/*     */   {
/*  82 */     return americanize(str, staticCapitalizeTimex);
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
/*     */   public static String americanize(String str, boolean capitalizeTimex)
/*     */   {
/*  96 */     if ((capitalizeTimex) && (timexMapping.containsKey(str)))
/*  97 */       return (String)timexMapping.get(str);
/*  98 */     if (mapping.containsKey(str)) {
/*  99 */       return (String)mapping.get(str);
/*     */     }
/* 101 */     for (int i = 0; i < pats.length; i++) {
/* 102 */       Pattern ex = excepts[i];
/* 103 */       if (ex != null) {
/* 104 */         Matcher me = ex.matcher(str);
/* 105 */         if (me.find()) {}
/*     */       }
/*     */       else
/*     */       {
/* 109 */         Matcher m = pats[i].matcher(str);
/* 110 */         if (m.find())
/*     */         {
/*     */ 
/* 113 */           return m.replaceAll(reps[i]); }
/*     */       }
/*     */     }
/* 116 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 121 */   private static Pattern[] pats = { Pattern.compile("haem(at)?o"), Pattern.compile("aemia$"), Pattern.compile("([lL]euk)aem"), Pattern.compile("programme(s?)$"), Pattern.compile("^([a-z]{3,})our(s?)$") };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */   private static Pattern[] excepts = { null, null, null, null, Pattern.compile("glamour|de[tv]our") };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */   private static String[] reps = { "hem$1o", "emia", "$1em", "program$1", "$1or$2" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 144 */   private static final String[] converters = { "anaesthetic", "analogue", "analogues", "analyse", "analysed", "analysing", "armoured", "cancelled", "cancelling", "candour", "capitalise", "capitalised", "capitalisation", "centre", "chimaeric", "clamour", "coloured", "colouring", "defence", "detour", "discolour", "discolours", "discoloured", "discolouring", "encyclopaedia", "endeavour", "endeavours", "endeavoured", "endeavouring", "fervour", "favour", "favours", "favoured", "favouring", "favourite", "favourites", "fibre", "fibres", "finalise", "finalised", "finalising", "flavour", "flavours", "flavoured", "flavouring", "glamour", "grey", "harbour", "harbours", "homologue", "homologues", "honour", "honours", "honoured", "honouring", "honourable", "humour", "humours", "humoured", "humouring", "kerb", "labelled", "labelling", "labour", "labours", "laboured", "labouring", "leant", "learnt", "localise", "localised", "manoeuvre", "manoeuvres", "maximise", "maximised", "maximising", "meagre", "minimise", "minimised", "minimising", "modernise", "modernised", "modernising", "misdemeanour", "misdemeanours", "neighbour", "neighbours", "neighbourhood", "neighbourhoods", "oestrogen", "oestrogens", "organisation", "organisations", "penalise", "penalised", "popularise", "popularised", "popularises", "popularising", "practise", "practised", "pressurise", "pressurised", "pressurises", "pressurising", "realise", "realised", "realising", "realises", "recognise", "recognised", "recognising", "recognises", "rumoured", "rumouring", "savour", "savours", "savoured", "savouring", "splendour", "splendours", "theatre", "theatres", "titre", "titres", "travelled", "travelling" };
/*     */   
/*     */ 
/*     */ 
/* 148 */   private static final String[] converted = { "anesthetic", "analog", "analogs", "analyze", "analyzed", "analyzing", "armored", "canceled", "canceling", "candor", "capitalize", "capitalized", "capitalization", "center", "chimeric", "clamor", "colored", "coloring", "defense", "detour", "discolor", "discolors", "discolored", "discoloring", "encyclopedia", "endeavor", "endeavors", "endeavored", "endeavoring", "fervor", "favor", "favors", "favored", "favoring", "favorite", "favorites", "fiber", "fibers", "finalize", "finalized", "finalizing", "flavor", "flavors", "flavored", "flavoring", "glamour", "gray", "harbor", "harbors", "homolog", "homologs", "honor", "honors", "honored", "honoring", "honorable", "humor", "humors", "humored", "humoring", "curb", "labeled", "labeling", "labor", "labors", "labored", "laboring", "leaned", "learned", "localize", "localized", "maneuver", "maneuvers", "maximize", "maximized", "maximizing", "meager", "minimize", "minimized", "minimizing", "modernize", "modernized", "modernizing", "misdemeanor", "misdemeanors", "neighbor", "neighbors", "neighborhood", "neighborhoods", "estrogen", "estrogens", "organization", "organizations", "penalize", "penalized", "popularize", "popularized", "popularizes", "popularizing", "practice", "practiced", "pressurize", "pressurized", "pressurizes", "pressurizing", "realize", "realized", "realizing", "realizes", "recognize", "recognized", "recognizing", "recognizes", "rumored", "rumoring", "savor", "savors", "savored", "savoring", "splendor", "splendors", "theater", "theaters", "titer", "titers", "traveled", "traveling" };
/*     */   
/* 150 */   private static final String[] timexConverters = { "january", "february", "april", "june", "july", "august", "september", "october", "november", "december", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday" };
/*     */   
/*     */ 
/* 153 */   private static final String[] timexConverted = { "January", "February", "April", "June", "July", "August", "September", "October", "November", "December", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
/*     */   
/*     */ 
/* 156 */   private static final HashMap<String, String> mapping = new HashMap();
/*     */   
/* 158 */   private static final HashMap<String, String> timexMapping = new HashMap();
/*     */   
/*     */ 
/*     */   static
/*     */   {
/* 163 */     if ((converters.length != converted.length) || (timexConverters.length != timexConverted.length) || (pats.length != reps.length) || (pats.length != excepts.length)) {
/* 164 */       throw new RuntimeException("Americanize: Bad initialization data");
/*     */     }
/* 166 */     for (int i = 0; i < converters.length; i++) {
/* 167 */       mapping.put(converters[i], converted[i]);
/*     */     }
/* 169 */     for (int i = 0; i < timexConverters.length; i++) {
/* 170 */       timexMapping.put(timexConverters[i], timexConverted[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setStaticCapitalizeTimex(boolean capitalizeTimex)
/*     */   {
/* 176 */     staticCapitalizeTimex = capitalizeTimex;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 181 */     return "Americanize[capitalizeTimex is " + staticCapitalizeTimex + "; " + "mapping has " + mapping.size() + " mappings; " + "timexMapping has " + timexMapping.size() + " mappings]";
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
/* 192 */     System.err.println(new Americanize());
/* 193 */     System.err.println();
/* 194 */     for (String arg : args) {
/* 195 */       System.out.println(arg + " --> " + americanize(arg));
/*     */     }
/*     */   }
/*     */   
/*     */   public Americanize() {}
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\Americanize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */