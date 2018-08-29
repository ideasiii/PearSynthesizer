package edu.stanford.nlp.stats;

import edu.stanford.nlp.util.MapFactory;
import edu.stanford.nlp.util.MutableDouble;
import java.util.Comparator;
import java.util.Set;

public abstract interface GenericCounter<E>
{
  public abstract boolean containsKey(E paramE);
  
  public abstract double getCount(E paramE);
  
  public abstract String getCountAsString(E paramE);
  
  public abstract void setCount(E paramE, String paramString);
  
  public abstract double totalDoubleCount();
  
  public abstract Set<E> keySet();
  
  public abstract int size();
  
  public abstract double doubleMax();
  
  public abstract MapFactory<E, MutableDouble> getMapFactory();
  
  public abstract Comparator<E> comparator();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\stats\GenericCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */