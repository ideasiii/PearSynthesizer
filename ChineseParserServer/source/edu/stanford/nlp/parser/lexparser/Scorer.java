package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.ling.HasWord;
import java.util.List;

public abstract interface Scorer
{
  public abstract double oScore(Edge paramEdge);
  
  public abstract double iScore(Edge paramEdge);
  
  public abstract boolean oPossible(Hook paramHook);
  
  public abstract boolean iPossible(Hook paramHook);
  
  public abstract boolean parse(List<? extends HasWord> paramList);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Scorer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */