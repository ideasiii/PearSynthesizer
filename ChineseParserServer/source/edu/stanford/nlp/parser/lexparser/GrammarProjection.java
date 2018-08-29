package edu.stanford.nlp.parser.lexparser;

abstract interface GrammarProjection
{
  public abstract int project(int paramInt);
  
  public abstract UnaryGrammar sourceUG();
  
  public abstract BinaryGrammar sourceBG();
  
  public abstract UnaryGrammar targetUG();
  
  public abstract BinaryGrammar targetBG();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\GrammarProjection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */