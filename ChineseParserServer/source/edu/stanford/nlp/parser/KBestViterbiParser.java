package edu.stanford.nlp.parser;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.ScoredObject;
import java.util.List;

public abstract interface KBestViterbiParser
  extends ViterbiParser
{
  public abstract List<ScoredObject<Tree>> getKBestParses(int paramInt);
  
  public abstract List<ScoredObject<Tree>> getBestParses();
  
  public abstract List<ScoredObject<Tree>> getKGoodParses(int paramInt);
  
  public abstract List<ScoredObject<Tree>> getKSampledParses(int paramInt);
  
  public abstract boolean hasParse();
  
  public abstract double getBestScore();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\KBestViterbiParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */