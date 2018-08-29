package edu.stanford.nlp.trees;

public abstract interface DependencyTyper<T>
{
  public abstract T makeDependency(Tree paramTree1, Tree paramTree2, Tree paramTree3);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\DependencyTyper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */