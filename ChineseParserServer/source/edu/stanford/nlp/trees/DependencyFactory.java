package edu.stanford.nlp.trees;

import edu.stanford.nlp.ling.Label;

public abstract interface DependencyFactory
{
  public abstract Dependency newDependency(Label paramLabel1, Label paramLabel2);
  
  public abstract Dependency newDependency(Label paramLabel1, Label paramLabel2, Object paramObject);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\DependencyFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */