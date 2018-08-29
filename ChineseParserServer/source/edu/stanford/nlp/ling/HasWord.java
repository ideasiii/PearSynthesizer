package edu.stanford.nlp.ling;

import java.io.Serializable;

public abstract interface HasWord
  extends Serializable
{
  public abstract String word();
  
  public abstract void setWord(String paramString);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\HasWord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */