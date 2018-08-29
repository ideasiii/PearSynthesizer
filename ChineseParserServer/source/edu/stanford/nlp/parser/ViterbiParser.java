package edu.stanford.nlp.parser;

import edu.stanford.nlp.trees.Tree;

public abstract interface ViterbiParser
  extends Parser
{
  public abstract Tree getBestParse();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\ViterbiParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */