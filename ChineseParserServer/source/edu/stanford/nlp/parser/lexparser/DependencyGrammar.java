package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.trees.Tree;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;

public abstract interface DependencyGrammar
  extends Serializable
{
  public abstract int numTagBins();
  
  public abstract int tagBin(int paramInt);
  
  public abstract int numDistBins();
  
  public abstract short distanceBin(int paramInt);
  
  public abstract void tune(Collection<Tree> paramCollection);
  
  public abstract double score(IntDependency paramIntDependency);
  
  public abstract double scoreTB(IntDependency paramIntDependency);
  
  public abstract double score(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5);
  
  public abstract double scoreTB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5);
  
  public abstract void readData(BufferedReader paramBufferedReader)
    throws IOException;
  
  public abstract void writeData(PrintWriter paramPrintWriter)
    throws IOException;
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\DependencyGrammar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */