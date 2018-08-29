/*    */ package edu.stanford.nlp.parser.lexparser;
/*    */ 
/*    */ import edu.stanford.nlp.ling.Label;
/*    */ import edu.stanford.nlp.stats.Counter;
/*    */ import edu.stanford.nlp.trees.Tree;
/*    */ import edu.stanford.nlp.util.Numberer;
/*    */ import edu.stanford.nlp.util.Pair;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class BinaryGrammarExtractor extends AbstractTreeExtractor
/*    */ {
/* 14 */   protected Numberer stateNumberer = Numberer.getGlobalNumberer("states");
/* 15 */   private Counter<Rule> ruleCounter = new Counter();
/* 16 */   private Counter symbolCounter = new Counter();
/* 17 */   private Set<Rule> binaryRules = new HashSet();
/* 18 */   private Set<Rule> unaryRules = new HashSet();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void tallyRule(Rule r, double score)
/*    */   {
/* 27 */     this.symbolCounter.incrementCount(this.stateNumberer.object(r.parent), score);
/* 28 */     this.ruleCounter.incrementCount(r, score);
/* 29 */     if (r.isUnary()) {
/* 30 */       this.unaryRules.add(r);
/*    */     } else
/* 32 */       this.binaryRules.add(r);
/*    */   }
/*    */   
/*    */   protected void tallyInternalNode(Tree lt) {
/*    */     Rule r;
/*    */     Rule r;
/* 38 */     if (lt.children().length == 1) {
/* 39 */       r = new UnaryRule(this.stateNumberer.number(lt.label().value()), this.stateNumberer.number(lt.children()[0].label().value()));
/*    */     }
/*    */     else {
/* 42 */       r = new BinaryRule(this.stateNumberer.number(lt.label().value()), this.stateNumberer.number(lt.children()[0].label().value()), this.stateNumberer.number(lt.children()[1].label().value()));
/*    */     }
/*    */     
/*    */ 
/* 46 */     tallyRule(r, this.weight);
/*    */   }
/*    */   
/*    */   public Object formResult() {
/* 50 */     this.stateNumberer.number(".$$.");
/* 51 */     BinaryGrammar bg = new BinaryGrammar(this.stateNumberer.total());
/* 52 */     UnaryGrammar ug = new UnaryGrammar(this.stateNumberer.total());
/*    */     
/* 54 */     for (Iterator uI = this.unaryRules.iterator(); uI.hasNext();) {
/* 55 */       UnaryRule ur = (UnaryRule)uI.next();
/* 56 */       ur.score = ((float)Math.log(this.ruleCounter.getCount(ur) / this.symbolCounter.getCount(this.stateNumberer.object(ur.parent))));
/* 57 */       if (Train.compactGrammar() >= 4) {
/* 58 */         ur.score = ((float)this.ruleCounter.getCount(ur));
/*    */       }
/* 60 */       ug.addRule(ur);
/*    */     }
/*    */     
/* 63 */     for (Iterator bI = this.binaryRules.iterator(); bI.hasNext();) {
/* 64 */       BinaryRule br = (BinaryRule)bI.next();
/* 65 */       br.score = ((float)Math.log((this.ruleCounter.getCount(br) - Train.ruleDiscount) / this.symbolCounter.getCount(this.stateNumberer.object(br.parent))));
/* 66 */       if (Train.compactGrammar() >= 4) {
/* 67 */         br.score = ((float)this.ruleCounter.getCount(br));
/*    */       }
/* 69 */       bg.addRule(br);
/*    */     }
/* 71 */     return new Pair(ug, bg);
/*    */   }
/*    */ }


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\BinaryGrammarExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */