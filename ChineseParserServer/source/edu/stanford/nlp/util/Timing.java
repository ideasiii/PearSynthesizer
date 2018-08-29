/*     */ package edu.stanford.nlp.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Timing
/*     */ {
/*     */   private long start;
/*  26 */   private static long startTime = ;
/*     */   
/*     */ 
/*  29 */   private static NumberFormat nf = new DecimalFormat("0.0");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Timing()
/*     */   {
/*  36 */     start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*  45 */     this.start = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long report()
/*     */   {
/*  56 */     return System.currentTimeMillis() - this.start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long report(String str, PrintStream stream)
/*     */   {
/*  67 */     long elapsed = report();
/*  68 */     stream.println(str + " Time elapsed: " + elapsed + " ms");
/*  69 */     return elapsed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long report(String str)
/*     */   {
/*  79 */     return report(str, System.err);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long report(String str, PrintWriter writer)
/*     */   {
/*  90 */     long elapsed = report();
/*  91 */     writer.println(str + " Time elapsed: " + elapsed + " ms");
/*  92 */     return elapsed;
/*     */   }
/*     */   
/*     */   public String toSecondsString() {
/*  96 */     return toSecondsString(report());
/*     */   }
/*     */   
/*     */   public static String toSecondsString(long elapsed) {
/* 100 */     return nf.format(elapsed / 1000.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long restart()
/*     */   {
/* 112 */     long elapsed = report();
/* 113 */     start();
/* 114 */     return elapsed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long restart(String str, PrintStream stream)
/*     */   {
/* 125 */     long elapsed = report(str, stream);
/* 126 */     start();
/* 127 */     return elapsed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long restart(String str)
/*     */   {
/* 137 */     return restart(str, System.err);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long restart(String str, PrintWriter writer)
/*     */   {
/* 148 */     long elapsed = report(str, writer);
/* 149 */     start();
/* 150 */     return elapsed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long stop()
/*     */   {
/* 161 */     long elapsed = report();
/* 162 */     this.start = 0L;
/* 163 */     return elapsed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long stop(String str, PrintStream stream)
/*     */   {
/* 174 */     report(str, stream);
/* 175 */     return stop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long stop(String str)
/*     */   {
/* 185 */     return stop(str, System.err);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long stop(String str, PrintWriter writer)
/*     */   {
/* 196 */     report(str, writer);
/* 197 */     return stop();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void startTime()
/*     */   {
/* 206 */     startTime = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long endTime()
/*     */   {
/* 217 */     return System.currentTimeMillis() - startTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long endTime(String str, PrintStream stream)
/*     */   {
/* 228 */     long elapsed = endTime();
/* 229 */     stream.println(str + " Time elapsed: " + elapsed + " ms");
/* 230 */     return elapsed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long endTime(String str)
/*     */   {
/* 241 */     return endTime(str, System.err);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doing(String str)
/*     */   {
/* 249 */     System.err.print(str);
/* 250 */     System.err.print(" ... ");
/* 251 */     System.err.flush();
/* 252 */     start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void done()
/*     */   {
/* 259 */     System.err.println("done [" + toSecondsString() + " sec].");
/*     */   }
/*     */   
/*     */ 
/*     */   public void done(String msg)
/*     */   {
/* 265 */     System.err.println(msg + " done [" + toSecondsString() + " sec].");
/*     */   }
/*     */   
/*     */   public static void startDoing(String str)
/*     */   {
/* 270 */     System.err.print(str);
/* 271 */     System.err.print(" ... ");
/* 272 */     System.err.flush();
/* 273 */     startTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void endDoing()
/*     */   {
/* 280 */     long elapsed = System.currentTimeMillis() - startTime;
/* 281 */     System.err.println("done [" + nf.format(elapsed / 1000.0D) + " sec].");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void endDoing(String msg)
/*     */   {
/* 289 */     long elapsed = System.currentTimeMillis() - startTime;
/* 290 */     System.err.println(msg + " done [" + nf.format(elapsed / 1000.0D) + " sec].");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long tick()
/*     */   {
/* 302 */     long elapsed = System.currentTimeMillis() - startTime;
/* 303 */     startTime();
/* 304 */     return elapsed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long tick(String str, PrintStream stream)
/*     */   {
/* 315 */     long elapsed = tick();
/* 316 */     stream.println(str + " Time elapsed: " + elapsed + " ms");
/* 317 */     return elapsed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long tick(String str)
/*     */   {
/* 327 */     return tick(str, System.err);
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
/*     */   public String toString()
/*     */   {
/* 342 */     return "Timing[start=" + startTime + "]";
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Timing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */