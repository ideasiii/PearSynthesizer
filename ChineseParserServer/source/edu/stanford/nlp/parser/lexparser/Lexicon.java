package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.trees.Tree;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

public abstract interface Lexicon
  extends Serializable
{
  public static final String UNKNOWN_WORD = "UNK";
  public static final String BOUNDARY = ".$.";
  public static final String BOUNDARY_TAG = ".$$.";
  
  public abstract boolean isKnown(int paramInt);
  
  public abstract boolean isKnown(String paramString);
  
  public abstract Iterator<IntTaggedWord> ruleIteratorByWord(int paramInt1, int paramInt2);
  
  public abstract int numRules();
  
  public abstract void train(Collection<Tree> paramCollection);
  
  public abstract float score(IntTaggedWord paramIntTaggedWord, int paramInt);
  
  public abstract void writeData(Writer paramWriter)
    throws IOException;
  
  public abstract void readData(BufferedReader paramBufferedReader)
    throws IOException;
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Lexicon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */