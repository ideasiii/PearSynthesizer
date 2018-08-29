package edu.stanford.nlp.trees;

import edu.stanford.nlp.ling.Label;
import java.io.Serializable;

public abstract interface Dependency
  extends Serializable
{
  public abstract Label governor();
  
  public abstract Label dependent();
  
  public abstract Object name();
  
  public abstract boolean equalsIgnoreName(Object paramObject);
  
  public abstract String toString(String paramString);
  
  public abstract DependencyFactory dependencyFactory();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\Dependency.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */