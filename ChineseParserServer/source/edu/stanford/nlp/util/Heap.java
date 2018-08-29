package edu.stanford.nlp.util;

import java.util.Iterator;

public abstract interface Heap<E>
{
  public abstract E extractMin();
  
  public abstract E min();
  
  public abstract boolean add(E paramE);
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract int decreaseKey(E paramE);
  
  public abstract Iterator<E> iterator();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\Heap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */