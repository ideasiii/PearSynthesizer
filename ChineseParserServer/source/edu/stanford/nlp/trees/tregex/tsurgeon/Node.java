package edu.stanford.nlp.trees.tregex.tsurgeon;

abstract interface Node
{
  public abstract void jjtOpen();
  
  public abstract void jjtClose();
  
  public abstract void jjtSetParent(Node paramNode);
  
  public abstract Node jjtGetParent();
  
  public abstract void jjtAddChild(Node paramNode, int paramInt);
  
  public abstract Node jjtGetChild(int paramInt);
  
  public abstract int jjtGetNumChildren();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\tregex\tsurgeon\Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */