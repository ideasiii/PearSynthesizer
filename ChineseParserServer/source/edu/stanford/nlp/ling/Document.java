package edu.stanford.nlp.ling;

import java.util.List;

public abstract interface Document<T>
  extends Datum, List<T>
{
  public abstract String title();
  
  public abstract Document blankDocument();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\Document.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */