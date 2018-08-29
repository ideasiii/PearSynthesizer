package edu.stanford.nlp.trees;

import java.io.Serializable;

public abstract interface HeadFinder
  extends Serializable
{
  public abstract Tree determineHead(Tree paramTree);
  
  public abstract Tree determineHead(Tree paramTree1, Tree paramTree2);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\HeadFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */