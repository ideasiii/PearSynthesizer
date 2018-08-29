package edu.stanford.nlp.util;

import java.io.Serializable;

public abstract interface Filter<T>
  extends Serializable
{
  public abstract boolean accept(T paramT);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Filter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */