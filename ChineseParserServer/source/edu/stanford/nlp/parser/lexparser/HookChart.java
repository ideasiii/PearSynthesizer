/*     */ package edu.stanford.nlp.parser.lexparser;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ class HookChart
/*     */ {
/*  14 */   private Map<ChartIndex, List<Hook>> registeredPreHooks = new HashMap();
/*  15 */   private Map<ChartIndex, List<Hook>> registeredPostHooks = new HashMap();
/*  16 */   private Map<ChartIndex, List<Edge>> registeredEdgesByLeftIndex = new HashMap();
/*  17 */   private Map<ChartIndex, List<Edge>> registeredEdgesByRightIndex = new HashMap();
/*     */   
/*  19 */   private Map<WeakChartIndex, List<Edge>> realEdgesByL = new HashMap();
/*  20 */   private Map<WeakChartIndex, List<Edge>> realEdgesByR = new HashMap();
/*  21 */   private Set<ChartIndex> builtLIndexes = new HashSet();
/*  22 */   private Set<ChartIndex> builtRIndexes = new HashSet();
/*     */   
/*  24 */   private Interner interner = new Interner();
/*     */   
/*     */   private static class ChartIndex {
/*     */     public int state;
/*     */     public int head;
/*     */     public int tag;
/*     */     public int loc;
/*     */     
/*     */     public int hashCode() {
/*  33 */       return this.state ^ this.head << 8 ^ this.tag << 16 ^ this.loc << 24;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/*  37 */       if (this == o) {
/*  38 */         return true;
/*     */       }
/*  40 */       if ((o instanceof ChartIndex)) {
/*  41 */         ChartIndex ci = (ChartIndex)o;
/*  42 */         return (this.state == ci.state) && (this.head == ci.head) && (this.tag == ci.tag) && (this.loc == ci.loc);
/*     */       }
/*  44 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class WeakChartIndex
/*     */   {
/*     */     public int state;
/*     */     public int loc;
/*     */     
/*     */     public int hashCode() {
/*  54 */       return this.state ^ this.loc << 16;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/*  58 */       if (this == o) {
/*  59 */         return true;
/*     */       }
/*  61 */       if ((o instanceof WeakChartIndex)) {
/*  62 */         WeakChartIndex ci = (WeakChartIndex)o;
/*  63 */         return (this.state == ci.state) && (this.loc == ci.loc);
/*     */       }
/*  65 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  70 */   private static final Collection<Edge> empty = ;
/*  71 */   private static final Collection<Hook> emptyHooks = Collections.emptyList();
/*     */   
/*  73 */   private ChartIndex tempIndex = new ChartIndex(null);
/*  74 */   private WeakChartIndex tempWeakIndex = new WeakChartIndex(null);
/*     */   
/*     */   public void registerEdgeIndexes(Edge edge)
/*     */   {
/*  78 */     this.tempIndex.state = edge.state;
/*  79 */     this.tempIndex.head = edge.head;
/*  80 */     this.tempIndex.tag = edge.tag;
/*  81 */     this.tempIndex.loc = edge.start;
/*  82 */     ChartIndex index = (ChartIndex)this.interner.intern(this.tempIndex);
/*  83 */     this.builtLIndexes.add(index);
/*  84 */     if (index == this.tempIndex) {
/*  85 */       this.tempIndex = new ChartIndex(null);
/*  86 */       this.tempIndex.state = edge.state;
/*  87 */       this.tempIndex.head = edge.head;
/*  88 */       this.tempIndex.tag = edge.tag;
/*     */     }
/*     */     
/*  91 */     this.tempIndex.loc = edge.end;
/*  92 */     index = (ChartIndex)this.interner.intern(this.tempIndex);
/*  93 */     if (index == this.tempIndex) {
/*  94 */       this.tempIndex = new ChartIndex(null);
/*     */     }
/*  96 */     this.builtRIndexes.add(index);
/*     */   }
/*     */   
/*     */   public void registerRealEdge(Edge edge) {
/* 100 */     this.tempWeakIndex.state = edge.state;
/* 101 */     this.tempWeakIndex.loc = edge.start;
/* 102 */     WeakChartIndex index = (WeakChartIndex)this.interner.intern(this.tempWeakIndex);
/* 103 */     insert(this.realEdgesByL, index, edge);
/* 104 */     if (index == this.tempWeakIndex) {
/* 105 */       this.tempWeakIndex = new WeakChartIndex(null);
/* 106 */       this.tempWeakIndex.state = edge.state;
/*     */     }
/* 108 */     this.tempWeakIndex.loc = edge.end;
/* 109 */     index = (WeakChartIndex)this.interner.intern(this.tempWeakIndex);
/* 110 */     insert(this.realEdgesByR, index, edge);
/* 111 */     if (index == this.tempWeakIndex) {
/* 112 */       this.tempWeakIndex = new WeakChartIndex(null);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isBuiltL(int state, int start, int head, int tag) {
/* 117 */     this.tempIndex.state = state;
/* 118 */     this.tempIndex.head = head;
/* 119 */     this.tempIndex.tag = tag;
/* 120 */     this.tempIndex.loc = start;
/* 121 */     return this.builtLIndexes.contains(this.tempIndex);
/*     */   }
/*     */   
/*     */   public boolean isBuiltR(int state, int end, int head, int tag) {
/* 125 */     this.tempIndex.state = state;
/* 126 */     this.tempIndex.head = head;
/* 127 */     this.tempIndex.tag = tag;
/* 128 */     this.tempIndex.loc = end;
/* 129 */     return this.builtRIndexes.contains(this.tempIndex);
/*     */   }
/*     */   
/*     */   public Collection<Edge> getRealEdgesWithL(int state, int start) {
/* 133 */     this.tempWeakIndex.state = state;
/* 134 */     this.tempWeakIndex.loc = start;
/* 135 */     Collection<Edge> edges = (Collection)this.realEdgesByL.get(this.tempWeakIndex);
/* 136 */     if (edges == null) {
/* 137 */       return empty;
/*     */     }
/* 139 */     return edges;
/*     */   }
/*     */   
/*     */   public Collection<Edge> getRealEdgesWithR(int state, int end) {
/* 143 */     this.tempWeakIndex.state = state;
/* 144 */     this.tempWeakIndex.loc = end;
/* 145 */     Collection<Edge> edges = (Collection)this.realEdgesByR.get(this.tempWeakIndex);
/* 146 */     if (edges == null) {
/* 147 */       return empty;
/*     */     }
/* 149 */     return edges;
/*     */   }
/*     */   
/*     */   public Collection<Hook> getPreHooks(Edge edge) {
/* 153 */     this.tempIndex.state = edge.state;
/* 154 */     this.tempIndex.head = edge.head;
/* 155 */     this.tempIndex.tag = edge.tag;
/* 156 */     this.tempIndex.loc = edge.end;
/* 157 */     Collection<Hook> result = (Collection)this.registeredPreHooks.get(this.tempIndex);
/* 158 */     if (result == null) {
/* 159 */       result = emptyHooks;
/*     */     }
/*     */     
/* 162 */     return result;
/*     */   }
/*     */   
/*     */   public Collection<Hook> getPostHooks(Edge edge) {
/* 166 */     this.tempIndex.state = edge.state;
/* 167 */     this.tempIndex.head = edge.head;
/* 168 */     this.tempIndex.tag = edge.tag;
/* 169 */     this.tempIndex.loc = edge.start;
/* 170 */     Collection<Hook> result = (Collection)this.registeredPostHooks.get(this.tempIndex);
/* 171 */     if (result == null) {
/* 172 */       result = emptyHooks;
/*     */     }
/*     */     
/* 175 */     return result;
/*     */   }
/*     */   
/*     */   public Collection<Edge> getEdges(Hook hook) {
/* 179 */     this.tempIndex.state = hook.subState;
/* 180 */     this.tempIndex.head = hook.head;
/* 181 */     this.tempIndex.tag = hook.tag;
/*     */     Collection<Edge> result;
/* 183 */     Collection<Edge> result; if (hook.isPreHook()) {
/* 184 */       this.tempIndex.loc = hook.start;
/* 185 */       result = (Collection)this.registeredEdgesByRightIndex.get(this.tempIndex);
/*     */     } else {
/* 187 */       this.tempIndex.loc = hook.end;
/* 188 */       result = (Collection)this.registeredEdgesByLeftIndex.get(this.tempIndex);
/*     */     }
/* 190 */     if (result == null) {
/* 191 */       result = empty;
/*     */     }
/*     */     
/* 194 */     return result;
/*     */   }
/*     */   
/*     */   private static <K, V> void insert(Map<K, List<V>> map, K index, V item)
/*     */   {
/* 199 */     List<V> list = (List)map.get(index);
/* 200 */     if (list == null)
/*     */     {
/* 202 */       list = new ArrayList(3);
/* 203 */       map.put(index, list);
/*     */     }
/* 205 */     list.add(item);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addEdge(Edge edge)
/*     */   {
/* 211 */     this.tempIndex.state = edge.state;
/* 212 */     this.tempIndex.head = edge.head;
/* 213 */     this.tempIndex.tag = edge.tag;
/*     */     
/* 215 */     this.tempIndex.loc = edge.start;
/* 216 */     ChartIndex index = (ChartIndex)this.interner.intern(this.tempIndex);
/* 217 */     insert(this.registeredEdgesByLeftIndex, index, edge);
/* 218 */     if (index == this.tempIndex) {
/* 219 */       this.tempIndex = new ChartIndex(null);
/* 220 */       this.tempIndex.state = edge.state;
/* 221 */       this.tempIndex.head = edge.head;
/* 222 */       this.tempIndex.tag = edge.tag;
/*     */     }
/* 224 */     this.tempIndex.loc = edge.end;
/* 225 */     index = (ChartIndex)this.interner.intern(this.tempIndex);
/* 226 */     insert(this.registeredEdgesByRightIndex, index, edge);
/* 227 */     if (index == this.tempIndex) {
/* 228 */       this.tempIndex = new ChartIndex(null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addHook(Hook hook)
/*     */   {
/* 234 */     this.tempIndex.state = hook.subState;
/* 235 */     this.tempIndex.head = hook.head;
/* 236 */     this.tempIndex.tag = hook.tag;
/* 237 */     Map<ChartIndex, List<Hook>> map; Map<ChartIndex, List<Hook>> map; if (hook.isPreHook()) {
/* 238 */       this.tempIndex.loc = hook.start;
/* 239 */       map = this.registeredPreHooks;
/*     */     } else {
/* 241 */       this.tempIndex.loc = hook.end;
/* 242 */       map = this.registeredPostHooks;
/*     */     }
/* 244 */     ChartIndex index = (ChartIndex)this.interner.intern(this.tempIndex);
/* 245 */     insert(map, index, hook);
/* 246 */     if (index == this.tempIndex) {
/* 247 */       this.tempIndex = new ChartIndex(null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\HookChart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */