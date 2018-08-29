package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.process.Function;
import edu.stanford.nlp.trees.Tree;
import java.util.Collection;
import java.util.Iterator;

public abstract interface Extractor
{
  public abstract Object extract(Collection<Tree> paramCollection);
  
  public abstract Object extract(Iterator<Tree> paramIterator, Function<Tree, Tree> paramFunction);
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\Extractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */