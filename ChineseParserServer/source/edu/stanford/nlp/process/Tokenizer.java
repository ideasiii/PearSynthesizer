package edu.stanford.nlp.process;

import java.util.Iterator;
import java.util.List;

public abstract interface Tokenizer<T>
  extends Iterator<T>
{
  public abstract T next();
  
  public abstract boolean hasNext();
  
  public abstract void remove();
  
  public abstract T peek();
  
  public abstract List<T> tokenize();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\Tokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */