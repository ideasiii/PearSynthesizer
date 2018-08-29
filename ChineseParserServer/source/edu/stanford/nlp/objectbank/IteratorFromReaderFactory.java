package edu.stanford.nlp.objectbank;

import java.io.Reader;
import java.util.Iterator;

public abstract interface IteratorFromReaderFactory<T>
{
  public abstract Iterator<T> getIterator(Reader paramReader);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\objectbank\IteratorFromReaderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */