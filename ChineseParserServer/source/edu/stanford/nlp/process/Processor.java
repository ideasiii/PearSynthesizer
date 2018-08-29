package edu.stanford.nlp.process;

import edu.stanford.nlp.ling.Document;

public abstract interface Processor<IN, OUT>
{
  public abstract Document<OUT> processDocument(Document<IN> paramDocument);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\process\Processor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */