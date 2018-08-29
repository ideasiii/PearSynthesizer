package edu.stanford.nlp.process;

import java.io.Serializable;

public abstract interface Function<T1, T2>
  extends Serializable
{
  public abstract T2 apply(T1 paramT1);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\Function.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */