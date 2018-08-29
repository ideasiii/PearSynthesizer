package edu.stanford.nlp.ling;

public abstract interface HasIndex
{
  public abstract String docID();
  
  public abstract void setDocID(String paramString);
  
  public abstract int sentIndex();
  
  public abstract void setSentIndex(int paramInt);
  
  public abstract int index();
  
  public abstract void setIndex(int paramInt);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\ling\HasIndex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */