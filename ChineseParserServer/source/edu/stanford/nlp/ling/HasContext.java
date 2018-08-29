package edu.stanford.nlp.ling;

public abstract interface HasContext
{
  public abstract String before();
  
  public abstract void setBefore(String paramString);
  
  public abstract void prependBefore(String paramString);
  
  public abstract String current();
  
  public abstract void setCurrent(String paramString);
  
  public abstract String after();
  
  public abstract void setAfter(String paramString);
  
  public abstract void appendAfter(String paramString);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\HasContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */