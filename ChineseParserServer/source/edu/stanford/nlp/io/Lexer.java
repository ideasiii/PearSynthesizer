package edu.stanford.nlp.io;

import java.io.IOException;
import java.io.Reader;

public abstract interface Lexer
{
  public static final int ACCEPT = 1;
  public static final int IGNORE = 0;
  
  public abstract int yylex()
    throws IOException;
  
  public abstract String yytext();
  
  public abstract void pushBack(int paramInt);
  
  public abstract int getYYEOF();
  
  public abstract void yyreset(Reader paramReader)
    throws IOException;
}


/* Location:              D:\Project\TTS\PearSynthesizer(HMM)\ChineseParserServer\lib\Stanford.jar!\edu\stanford\nlp\io\Lexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */