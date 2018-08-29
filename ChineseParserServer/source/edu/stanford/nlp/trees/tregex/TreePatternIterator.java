package edu.stanford.nlp.trees.tregex;

abstract interface TreePatternIterator
{
  public abstract TreePattern next();
  
  public abstract TreePattern previous();
  
  public abstract boolean hasNext();
  
  public abstract boolean hasPrevious();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\TreePatternIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */