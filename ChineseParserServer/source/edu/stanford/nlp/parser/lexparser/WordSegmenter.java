package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;
import java.io.Serializable;
import java.util.Collection;

public abstract interface WordSegmenter
  extends Serializable
{
  public abstract void train(Collection<Tree> paramCollection);
  
  public abstract void loadSegmenter(String paramString);
  
  public abstract Sentence segmentWords(String paramString);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\WordSegmenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */