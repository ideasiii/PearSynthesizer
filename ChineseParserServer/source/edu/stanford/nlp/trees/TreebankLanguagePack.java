package edu.stanford.nlp.trees;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.Function;
import edu.stanford.nlp.util.Filter;
import java.io.Serializable;

public abstract interface TreebankLanguagePack
  extends Serializable
{
  public static final String DEFAULT_ENCODING = "UTF-8";
  
  public abstract boolean isPunctuationTag(String paramString);
  
  public abstract boolean isPunctuationWord(String paramString);
  
  public abstract boolean isSentenceFinalPunctuationTag(String paramString);
  
  public abstract boolean isEvalBIgnoredPunctuationTag(String paramString);
  
  public abstract Filter<String> punctuationTagAcceptFilter();
  
  public abstract Filter<String> punctuationTagRejectFilter();
  
  public abstract Filter<String> punctuationWordAcceptFilter();
  
  public abstract Filter<String> punctuationWordRejectFilter();
  
  public abstract Filter<String> sentenceFinalPunctuationTagAcceptFilter();
  
  public abstract Filter<String> evalBIgnoredPunctuationTagAcceptFilter();
  
  public abstract Filter<String> evalBIgnoredPunctuationTagRejectFilter();
  
  public abstract String[] punctuationTags();
  
  public abstract String[] punctuationWords();
  
  public abstract String[] sentenceFinalPunctuationTags();
  
  public abstract String[] sentenceFinalPunctuationWords();
  
  public abstract String[] evalBIgnoredPunctuationTags();
  
  public abstract GrammaticalStructureFactory grammaticalStructureFactory();
  
  public abstract GrammaticalStructureFactory grammaticalStructureFactory(Filter<String> paramFilter);
  
  public abstract String getEncoding();
  
  public abstract TokenizerFactory<? extends HasWord> getTokenizerFactory();
  
  public abstract char[] labelAnnotationIntroducingCharacters();
  
  public abstract boolean isLabelAnnotationIntroducingCharacter(char paramChar);
  
  public abstract String basicCategory(String paramString);
  
  public abstract Function<String, String> getBasicCategoryFunction();
  
  public abstract String categoryAndFunction(String paramString);
  
  public abstract Function<String, String> getCategoryAndFunctionFunction();
  
  public abstract boolean isStartSymbol(String paramString);
  
  public abstract Filter<String> startSymbolAcceptFilter();
  
  public abstract String[] startSymbols();
  
  public abstract String startSymbol();
  
  public abstract String treebankFileExtension();
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\trees\TreebankLanguagePack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */