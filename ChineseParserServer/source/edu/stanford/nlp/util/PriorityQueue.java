package edu.stanford.nlp.util;

import java.util.List;
import java.util.Set;

public abstract interface PriorityQueue<E>
  extends Set<E>
{
  public abstract E removeFirst();
  
  public abstract E getFirst();
  
  public abstract double getPriority();
  
  public abstract double getPriority(E paramE);
  
  public abstract boolean add(E paramE, double paramDouble);
  
  public abstract boolean changePriority(E paramE, double paramDouble);
  
  public abstract boolean relaxPriority(E paramE, double paramDouble);
  
  public abstract List<E> toSortedList();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\util\PriorityQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */