package edu.stanford.nlp.trees;

import edu.stanford.nlp.ling.Label;

public abstract interface ConstituentFactory
{
  public abstract Constituent newConstituent(int paramInt1, int paramInt2);
  
  public abstract Constituent newConstituent(int paramInt1, int paramInt2, Label paramLabel, double paramDouble);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\ConstituentFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */