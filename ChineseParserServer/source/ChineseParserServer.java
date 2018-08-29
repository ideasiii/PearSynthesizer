package chineseparser;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

public class ChineseParserServer
{
  LexicalizedParser myParser = null;
  TreePrint myTreePrint = new TreePrint("oneline");
  String myInputFile = null;
  String myOutputFile = null;
  String myInputStatusFile = null;
  String myOutputStatusFile = null;
  public static final String QUIT_SERVER_COMMAND = "COMMAND=QUIT_SERVER";
  public static final String POS_COMMAND = "COMMAND=POS";
  public static final String PARSE_COMMAND = "COMMAND=PARSE";
  
  public ChineseParserServer(String parserFile, String inputFile, String outputFile, String inputStatusFile, String outputStatusFile, String maxLength)
  {
    System.out.println("Loading Chinese parser from " + parserFile + "...");
    this.myParser = new LexicalizedParser(parserFile);
    
    this.myInputFile = inputFile;
    this.myOutputFile = outputFile;
    this.myInputStatusFile = inputStatusFile;
    this.myOutputStatusFile = outputStatusFile;
    if (!maxLength.isEmpty()) {
      this.myParser.setOptionFlags(new String[] { "-maxLength", "40" });
    }
    System.out.println("Chinese Parser Server waiting for input from " + inputFile + "...");
    
    begin();
  }
  
  private void begin()
  {
    File inputStatus = new File(this.myInputStatusFile);
    
    String command = "COMMAND=POS";
    while (!inputStatus.exists()) {}
    while (!inputStatus.canRead()) {}
    try
    {
      BufferedReader statusIn = new BufferedReader(new InputStreamReader(new FileInputStream(inputStatus)));
      
      String line = statusIn.readLine();
      while (line != null)
      {
        if (line.contains("COMMAND=QUIT_SERVER"))
        {
          System.out.println("Terminating Chinese parser server");
          System.exit(0);
        }
        else if (line.contains("COMMAND=PARSE"))
        {
          command = "COMMAND=PARSE";
        }
        line = statusIn.readLine();
      }
      statusIn.close();
    }
    catch (FileNotFoundException e)
    {
      System.err.println("Input status file " + inputStatus.getAbsolutePath() + " does not exist!");
      begin();
    }
    catch (IOException e)
    {
      System.err.println("An IO exception occurred: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    File inputFile = new File(this.myInputFile);
    while (!inputFile.exists()) {}
    while (!inputFile.canRead()) {}
    try
    {
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.myOutputFile)));
      
      String line = in.readLine();
      while ((line != null) && (!line.isEmpty()))
      {
        String parse;
        String parse;
        if (command.equals("COMMAND=POS")) {
          parse = posFor(line);
        } else {
          parse = parse(line);
        }
        out.write(parse);
        out.newLine();
        out.flush();
        
        line = in.readLine();
      }
      out.flush();
      out.close();
      
      in.close();
      
      inputFile.delete();
      inputStatus.delete();
      
      File outputStatusFile = new File(this.myOutputStatusFile);
      if (outputStatusFile.exists()) {
        System.err.println("Warning: output status file " + this.myOutputStatusFile + " already exists!");
      }
      outputStatusFile.createNewFile();
    }
    catch (FileNotFoundException e)
    {
      System.err.println("Input file " + inputFile.getAbsolutePath() + " does not exist!");
    }
    catch (IOException e)
    {
      System.err.println("An IO exception occurred: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    begin();
  }
  
  public static void killServer()
  {
    System.out.println("Terminating Chinese parser server");
    System.exit(0);
  }
  
  public String posFor(String sentence)
  {
    return posFor(parseTreeFor(sentence));
  }
  
  public static String posFor(Tree tree)
  {
    StringBuffer sb = new StringBuffer();
    
    Iterator<Tree> it = tree.iterator();
    while (it.hasNext())
    {
      Tree node = (Tree)it.next();
      if (node.isPreTerminal())
      {
        Tree[] words = node.children();
        for (Tree word : words) {
          sb.append(word.label().value());
        }
        sb.append("/");
        
        sb.append(node.label().value());
        if (it.hasNext()) {
          sb.append(" ");
        }
      }
    }
    return sb.toString();
  }
  
  public String parse(String sentence)
  {
    return treeToString(parseTreeFor(sentence));
  }
  
  public Tree parseTreeFor(String sentence)
  {
    return this.myParser.apply(sentence);
  }
  
  public String treeToString(Tree tree)
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    this.myTreePrint.printTree(tree, pw);
    pw.flush();
    
    return sw.toString().trim();
  }
  
  public static void displayUsage()
  {
    System.out.println("Usage: java -jar ChineseParserServer.jar -p parserFile -i inputFile -o outputFile -is inputStatusFile -os outputStatusFile [-l maxSentenceLength]");
    System.out.println("To load the parser, you may need to specify more heap space memory for the JVM with the -Xmx tag");
    System.out.println("E.g. java -Xmx400m -jar ChineseParserServer.jar -p chineseFactored.ser.gz -i input.txt -o output.txt -is input.status -os output.status -l 40");
    System.out.println("To get pos data, write flag 'COMMAND=POS' to input status file (default).");
    System.out.println("To get parse data, write flag 'COMMAND=PARSE' to input status file.");
    System.out.println("To stop server, write flag 'COMMAND=QUIT_SERVER' to input status file.");
  }
  
  public static void main(String[] args)
  {
    String parserFile = "";
    String inputFile = "";
    String outputFile = "";
    String inputStatusFile = "";
    String outputStatusFile = "";
    String maxSentenceLength = "";
    for (int a = 0; a < args.length; a++)
    {
      String arg = args[a];
      if (arg.equals("-p"))
      {
        a++;
        if (a >= args.length)
        {
          System.out.println("Missing value for -p");
          System.exit(1);
        }
        parserFile = args[a];
      }
      else if (arg.equals("-i"))
      {
        a++;
        if (a >= args.length)
        {
          System.out.println("Missing value for -i");
          System.exit(1);
        }
        inputFile = args[a];
      }
      else if (arg.equals("-o"))
      {
        a++;
        if (a >= args.length)
        {
          System.out.println("Missing value for -o");
          System.exit(1);
        }
        outputFile = args[a];
      }
      else if (arg.equals("-is"))
      {
        a++;
        if (a >= args.length)
        {
          System.out.println("Missing value for -is");
          System.exit(1);
        }
        inputStatusFile = args[a];
      }
      else if (arg.equals("-os"))
      {
        a++;
        if (a >= args.length)
        {
          System.out.println("Missing value for -os");
          System.exit(1);
        }
        outputStatusFile = args[a];
      }
      else if (arg.equals("-l"))
      {
        a++;
        if (a >= args.length)
        {
          System.out.println("Missing value for -l");
          System.exit(1);
        }
        maxSentenceLength = args[a];
      }
      else if ((arg.equals("-h")) || (arg.equals("-?")) || (arg.equals("-help")))
      {
        displayUsage();
        System.exit(0);
      }
      else if (arg.equals("-k"))
      {
        killServer();
      }
      else
      {
        System.out.println("Unknown argument '" + arg + "'");
        System.exit(1);
      }
    }
    if (parserFile.isEmpty())
    {
      System.out.println("Must specify parser file with -p argument");
      System.exit(1);
    }
    if (inputFile.isEmpty())
    {
      System.out.println("Must specify input file with -i argument");
      System.exit(1);
    }
    if (outputFile.isEmpty())
    {
      System.out.println("Must specify output file with -o argument");
      System.exit(1);
    }
    new ChineseParserServer(parserFile, inputFile, outputFile, inputStatusFile, outputStatusFile, maxSentenceLength);
  }
}
