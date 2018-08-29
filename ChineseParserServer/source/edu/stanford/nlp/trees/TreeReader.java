package edu.stanford.nlp.trees;

import java.io.IOException;

public abstract interface TreeReader
{
  public abstract Tree readTree()
    throws IOException;
  
  public abstract void close()
    throws IOException;
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreeReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */