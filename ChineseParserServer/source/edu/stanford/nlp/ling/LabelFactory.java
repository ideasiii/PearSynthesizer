package edu.stanford.nlp.ling;

public abstract interface LabelFactory
{
  public abstract Label newLabel(String paramString);
  
  public abstract Label newLabel(String paramString, int paramInt);
  
  public abstract Label newLabelFromString(String paramString);
  
  public abstract Label newLabel(Label paramLabel);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\LabelFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */