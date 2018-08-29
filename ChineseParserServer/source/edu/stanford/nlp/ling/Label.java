package edu.stanford.nlp.ling;

public abstract interface Label
{
  public abstract String value();
  
  public abstract void setValue(String paramString);
  
  public abstract String toString();
  
  public abstract void setFromString(String paramString);
  
  public abstract LabelFactory labelFactory();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\Label.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */