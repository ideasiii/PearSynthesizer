package edu.stanford.nlp.parser;

import edu.stanford.nlp.ling.HasWord;
import java.util.List;

public abstract interface Parser
{
  public abstract boolean parse(List<? extends HasWord> paramList);
  
  public abstract boolean parse(List<? extends HasWord> paramList, String paramString);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */