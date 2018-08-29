package edu.stanford.nlp.trees;

import edu.stanford.nlp.ling.Label;
import java.util.Collection;

public abstract interface Labeled
{
  public abstract Label label();
  
  public abstract void setLabel(Label paramLabel);
  
  public abstract Collection labels();
  
  public abstract void setLabels(Collection paramCollection);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\Labeled.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */