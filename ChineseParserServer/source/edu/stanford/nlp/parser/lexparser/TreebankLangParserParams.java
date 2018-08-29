package edu.stanford.nlp.parser.lexparser;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.trees.DiskTreebank;
import edu.stanford.nlp.trees.HeadFinder;
import edu.stanford.nlp.trees.MemoryTreebank;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeReaderFactory;
import edu.stanford.nlp.trees.TreeTransformer;
import edu.stanford.nlp.trees.TreebankFactory;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

public abstract interface TreebankLangParserParams
  extends TreebankFactory, Serializable
{
  public abstract HeadFinder headFinder();
  
  public abstract void setInputEncoding(String paramString);
  
  public abstract void setOutputEncoding(String paramString);
  
  public abstract String getOutputEncoding();
  
  public abstract String getInputEncoding();
  
  public abstract TreeReaderFactory treeReaderFactory();
  
  public abstract Lexicon lex(Options.LexOptions paramLexOptions);
  
  public abstract TreeTransformer collinizer();
  
  public abstract TreeTransformer collinizerEvalb();
  
  public abstract MemoryTreebank memoryTreebank();
  
  public abstract DiskTreebank diskTreebank();
  
  public abstract MemoryTreebank testMemoryTreebank();
  
  public abstract TreebankLanguagePack treebankLanguagePack();
  
  public abstract PrintWriter pw();
  
  public abstract PrintWriter pw(OutputStream paramOutputStream);
  
  public abstract String[] sisterSplitters();
  
  public abstract TreeTransformer subcategoryStripper();
  
  public abstract Tree transformTree(Tree paramTree1, Tree paramTree2);
  
  public abstract void display();
  
  public abstract int setOptionFlag(String[] paramArrayOfString, int paramInt);
  
  public abstract List<? extends HasWord> defaultTestSentence();
  
  public abstract TokenizerFactory<Tree> treeTokenizerFactory();
  
  public abstract Extractor dependencyGrammarExtractor(Options paramOptions);
  
  public abstract double[] MLEDependencyGrammarSmoothingParams();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\parser\lexparser\TreebankLangParserParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */