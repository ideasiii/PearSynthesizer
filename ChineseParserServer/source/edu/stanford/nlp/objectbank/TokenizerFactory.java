package edu.stanford.nlp.objectbank;

import edu.stanford.nlp.process.Tokenizer;
import java.io.Reader;

public abstract interface TokenizerFactory<T>
  extends IteratorFromReaderFactory<T>
{
  public abstract Tokenizer<T> getTokenizer(Reader paramReader);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\objectbank\TokenizerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */