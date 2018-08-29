package edu.stanford.nlp.process;

import java.util.List;

public abstract interface ListProcessor<IN, OUT>
{
  public abstract List<OUT> process(List<IN> paramList);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\ListProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */