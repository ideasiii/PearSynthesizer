package edu.stanford.nlp.trees;

import edu.stanford.nlp.ling.Label;
import java.util.List;

public abstract interface TreeFactory
{
  public abstract Tree newLeaf(String paramString);
  
  public abstract Tree newTreeNode(String paramString, List paramList);
  
  public abstract Tree newLeaf(Label paramLabel);
  
  public abstract Tree newTreeNode(Label paramLabel, List paramList);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */