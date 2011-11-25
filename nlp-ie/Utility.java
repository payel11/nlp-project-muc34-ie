


import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.rmi.CORBA.Util;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;

/**
 * 
 */

/**
 * @author Prafulla
 *contains generic methods required by other classes
 */
public class Utility {


	public static ArrayList <String> nonConfirmationWords;
	public static ArrayList <String> negationWords;
	public static ArrayList <String> militaryWords;
	public static ArrayList <String> weapons;
	public static ArrayList <String> determiners;
	public static ArrayList <String> locations;

	static String triggerWord = "-";
	static
	{


		nonConfirmationWords = new ArrayList<String>();
		negationWords = new ArrayList<String>();
		militaryWords =  new ArrayList<String>();
		weapons = new ArrayList <String>();
		determiners = new ArrayList <String>();
		locations = new ArrayList<String> ();
		readPlaces();

		nonConfirmationWords.add("DOUBT");
		nonConfirmationWords.add("could not");
		nonConfirmationWords.add("MIGHT");
		nonConfirmationWords.add("escaped");
		nonConfirmationWords.add("ago");//saved 1 error on training data

		negationWords.add("not");
		negationWords.add("no");
		negationWords.add("never");
		negationWords.add("nothing");
		negationWords.add("nobody");
		negationWords.add("none");

		militaryWords.add("military");
		militaryWords.add("troops");
		militaryWords.add("army");
		militaryWords.add("infantry");
		militaryWords.add("batalian");
		militaryWords.add("brigade");
		militaryWords.add("force");

		weapons.add("bomb");weapons.add("dynamite");
		weapons.add("machinegun");weapons.add("grenade");weapons.add("gun");
		weapons.add("rifle");weapons.add("machine-gun");weapons.add("bullet");weapons.add("explosive");
		weapons.add("devices");//handle later

		determiners.add("a");
		determiners.add("the");

	}

	public static String getCurrentDir()
	{
		return System.getProperty("user.dir");
	}

	public static boolean isMilitary(String sentence)
	{
		String []words = sentence.split(" ");
		for(int i=0;i<words.length;i++)
		{
			if(words[i].equalsIgnoreCase("reported"))
			{
				return false;
			}
			for(int j=0;j<militaryWords.size();j++ )
			{
				if(words[i].equalsIgnoreCase(militaryWords.get(j)))
				{
					return true;
				}

			}
		}
		return false;
	}

	public static boolean isDoubtFul(String sentence)
	{
		String []words = sentence.split(" ");
		for(int i=0;i<words.length;i++)
		{ 
			for(int j=0;j<nonConfirmationWords.size();j++ )
			{
				if(words[i].equalsIgnoreCase(nonConfirmationWords.get(j)))
				{
					return true;
				}

			}
		}
		return false;
	}

	public static String getFilePath(String...args)
	{
		String curDir = getCurrentDir();
		String completePath = null;
		if(args.length ==2)
		{
			completePath = curDir+ "/"+ args[0] + "/"+ args[1];
		}
		else if(args.length ==1)
		{
			completePath = args[0];
		}
		else
		{
			System.out.println("\n\t getFilePath needs either 1 or 2 args..");
			System.exit(1);

		}
		return completePath;
	}


	/**
	 * Currently reads a file and returns all the words in a Word Class's format (Handled nouns and verbs only)
	 * @param dictionaryName
	 * @return
	 */
	public static ArrayList<Word> readDictionary(String dictionaryName)
	{
		String dictPath = getFilePath("dictionary", dictionaryName);
		Reader r = new Reader();
		ArrayList <String> lines = r.readFile1(dictPath);
		ArrayList <Word> words = new ArrayList<Word>();
		int size= lines.size();

		for(int i=0;i<size;i++)
		{
			String line = lines.get(i);
			String [] tokens = line.split(",");
			if(tokens[0].equalsIgnoreCase("n"))
			{
				Word  w = new Word();
				w.setPOS(tokens[0]);
				w.setWordName(tokens[1]);
				w.setSpForm("-");
				w.setPpForm("-");
				w.setIngForm("-");
				w.setPluralForm(tokens[2]);
				w.setCategory(tokens[3]);

				words.add(w);
			}
			else if(tokens[0].equalsIgnoreCase("v"))
			{

				Word  w = new Word();
				w.setPOS(tokens[0]);
				w.setWordName(tokens[1]);
				w.setSpForm(tokens[2]);
				w.setPpForm(tokens[3]);
				w.setIngForm(tokens[4]);
				w.setCategory(tokens[5]);
				w.setPluralForm("-");
				words.add(w);
			}

		}

		return words;
	}

	public static void readPlaces()
	{
		String curDir = System.getProperty("user.dir");
		Reader r = new Reader();
		ArrayList <String> lines = r.readFile1(curDir +"/dictionary/places");

		int size = lines.size();

		for(int i=0;i<size;i++)
		{
			locations.add(lines.get(i).toUpperCase());
			locations.add(lines.get(i).toUpperCase()+"S");

			System.out.println("\n\t added location:"+locations.get(i));
		}
		size = locations.size();

		System.out.println("\n\t total "+size+" locations found.");

	}

	public static boolean isQuestion(String sentence)
	{
		if(sentence.trim().endsWith("?"))
		{
			return true;
		}
		return false;
	}

	public static boolean containsTriggerWord(String sentence, ArrayList <Word>lexicon)
	{
		int size = lexicon.size();
		for (int i=0;i<size;i++)
		{
			Word w = lexicon.get(i);
			String [] tokens = sentence.split(" ");
			List l = Arrays.asList(tokens);
			int lSize = l.size();
			for(int j=0;j<lSize;j++)
			{
				if(w.contains((String)l.get(j)))
				{
					if(Utility.isValidWord((String)l.get(j)))
					{
						triggerWord = (String)l.get(j);
						return true;
					}

				}
			}
		}
		return false;
	}

	public static String getTriggerWord()
	{
		return triggerWord;
	}
	//change it to odd number of times
	public static boolean containsNegation(String Sentence)
	{
		String[] tokens = Sentence.split(" ");
		for(int i=0;i<tokens.length;i++)
		{
			String token = tokens[i];
			if(negationWords.contains(token))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isTriggerWord(String token,ArrayList <Word>lexicon)
	{
		int size = lexicon.size();
		for (int i=0;i<size;i++)
		{
			Word w = lexicon.get(i);
			if(w.contains(token))
			{
				return true;
			}

		}
		return false;
	}

	public static boolean containsBombing(String sentence)
	{
		String [] words =sentence.split(" ");
		for(int i=0;i<words.length;i++)
		{ 
			if(words[i].equalsIgnoreCase("bombs")|| words[i].equalsIgnoreCase("bomb")|| words[i].equalsIgnoreCase("bombed")
					|| words[i].equalsIgnoreCase("bombing") ||words[i].equalsIgnoreCase("bombardment") ||words[i].equalsIgnoreCase("bombarding")
					|| words[i].equalsIgnoreCase("dynamite")  || words[i].equalsIgnoreCase("dynamites")
					|| words[i].equalsIgnoreCase("explosion")
			)
			{
				return true;
			}

		}
		return false;
	}

	public static String getBombingWord(String sentence)
	{
		String [] words =sentence.split(" ");
		for(int i=0;i<words.length;i++)
		{ 
			if(words[i].equalsIgnoreCase("bombs")|| words[i].equalsIgnoreCase("bomb")|| words[i].equalsIgnoreCase("bombed")
					|| words[i].equalsIgnoreCase("bombing") ||words[i].equalsIgnoreCase("bombardment") ||words[i].equalsIgnoreCase("bombarding")
					|| words[i].equalsIgnoreCase("dynamite")  || words[i].equalsIgnoreCase("dynamites")
					|| words[i].equalsIgnoreCase("explosion")
			)
			{
				return words[i];
			}

		}
		return "-";
	}

	public static int containsLocation(String []sentences)
	{
		for(int k=0;k<sentences.length;k++)
		{
			String sentence = sentences[k];
			String [] words =sentence.split(" ");
			for(int i=0;i<words.length;i++)
			{ 
				int size = locations.size();
				for(int j=0;j<size;j++)
				{
					if(words[i].equalsIgnoreCase(locations.get(j)))
					{
						return k;
					}
				}
			}
		}
		return -1;

	}

	public static String getMatchedLocation(String sentence)
	{
		String [] words =sentence.split(" ");
		for(int i=0;i<words.length;i++)
		{ 
			int size = locations.size();
			for(int j=0;j<size;j++)
			{
				if(words[i].equalsIgnoreCase(locations.get(j)))
				{
					return words[i];
				}
			}
		}
		return "-";

	}
	public static String getWeapon(String sentence)
	{
		String [] words = sentence.split(" ");
		int size = words.length;
		for (int i=0;i<size;i++)
		{
			//String weapon =  weapons.get(i);//
			for(int j=0;j<weapons.size();j++)
			{

				if(words[i].equalsIgnoreCase(weapons.get(j)) || words[i].equalsIgnoreCase(weapons.get(j)+"s"))
				{
					return words[i];
				}
			}
		}
		return "-";
	}

	public static String getTriggerType(String token,ArrayList <Word>lexicon)
	{
		int size = lexicon.size();
		for (int i=0;i<size;i++)
		{
			Word w = lexicon.get(i);
			if(w.contains(token))
			{
				return w.getCategory();
			}

		}
		return "-";
	}


	public static String getTriggerCategory(String sentence, ArrayList <Word>lexicon)
	{
		String [] tokens = sentence.split(" ");
		for(int i=0;i<tokens.length;i++)
		{
			String token = tokens[i];
			if(isValidWord(token) && isTriggerWord(token, lexicon))
			{
				//System.out.println(token);
				return getTriggerType(token, lexicon);
			}
		}

		return "-";
	}

	public static ArrayList <String> getAllFiles(String dir)
	{
		ArrayList <String> files = new ArrayList <String>();
		String curDir = getCurrentDir();
		String completePath = curDir+ "/"+ dir;
		File dirFile = new File(completePath);
		//System.out.println("path:"+completePath);
		if(dirFile.isDirectory())
		{
			String[] filenames =dirFile.list();
			Arrays.sort(filenames); // for linux 

			for(int i=0;i<filenames.length;i++)
			{
				files.add(completePath + "/"+filenames[i]);
			}

		}

		//System.out.println(files);
		return files;
	}
	public static String removeSymbols(String sentence)
	{
		sentence = sentence.replaceAll("," , "");
		sentence = sentence.replaceAll("\"", "");
		return sentence;

	}

	public static boolean isValidWord(String word)
	{
		if(word.matches("[a-zA-Z]*"))
		{
			return true;
		}
		return false;
	}


	public static boolean containsDeterminer(String phrase) {
		return determiners.contains(phrase);

	}

	public static int indexOfDeterminer(String phrase) {
		String[] wordsInPhrase = phrase.split("\\s+");

		for (int i = 0; i < wordsInPhrase.length; i++) {
			if(determiners.contains(wordsInPhrase[i]))
				return i;

		}

		return -1;
	}

	public static String getPerp(LexicalizedParser tregexParser, String text) throws Exception
	{
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new MyCoreLabelTokenFactory(), "invertible=true");
		DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(text));
		dp.setTokenizerFactory(tokenizerFactory);
		// compile pattern
		TregexPattern pattern = TregexPattern.compile("(NP > (PP << by > (VP << kidnapped|attacked|killed|bombed|shot|murdered)))");
		String answer = "";

		boolean isFound = false;
		for (List<HasWord> list : dp)
		{
			// parse sentence 
			if(isFound)
			{
				break;
			}
			Tree ptree = tregexParser.apply(list);
			// print tree
			ptree.pennPrint();
			// create matcher for tree
			TregexMatcher tm = pattern.matcher(ptree);
			// print matches -- if any
			while(tm.find())
			{
				List l = tm.getMatch().getLeaves();
				int size = l.size();

				for(int i=0;i<size;i++)
				{
					answer = answer + " " +l.get(i);
				}
				isFound = true;
				break;

			}
		}
		System.out.println("And the perpetrator is:"+answer);
		return answer;
	}

	public static ArrayList<String> getVictim(LexicalizedParser lp,TregexPattern patterns [], String text) throws Exception
	{
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new MyCoreLabelTokenFactory(), "invertible=true");
		DocumentPreprocessor dp = new DocumentPreprocessor(new StringReader(text));
		dp.setTokenizerFactory(tokenizerFactory);
		ArrayList<String> answers = new ArrayList<String>();
		// compile pattern
		String answer = "";
		int size = patterns.length;

		for(int i=0;i<size;i++)
		{
			Tree ptree = lp.apply(text);
			//ptree.pennPrint();
			TregexMatcher tm = patterns[i].matcher(ptree);
			while(tm.find())
			{
				List l = tm.getMatch().getLeaves();
				System.out.println(l);
				int s1 = l.size();
				for(int j=0;j<s1;j++)
				{
					answer = answer + " " +l.get(j);
				}
				break;

			}//while ends
			if(!answer.equalsIgnoreCase(""))
			{
				break;
			}
		}


		answer = answer.trim();
		String temp = answer;
		System.out.println("And the Victim is:"+answer);

		NER ner = new NER();
		ArrayList<String> names = new ArrayList<String>();

		names = ner.getPersons(text);

		if(names == null)
		{
			return null;
		}

		System.out.println("\n\tsize:"+names.size());



		int nameSize= names.size();
		for(int index =0;index<nameSize;index++)
		{
			String name = names.get(index);
			System.out.println(temp);
			System.out.println("\n\t name found as a noun:"+name);
			/*if(answer.contains(name))
			{
				System.out.println("\n\t Name added to the answerList:"+name);
				answers.add(name.trim().toUpperCase());
			}*/
			answers.add(name);
		}
		System.out.println("size of answers:"+answers.size());
		return answers;
	}


	public static void main(String... args)
	{
		Utility u = new Utility();
		String t = "sferer";
		u.readPlaces();
	}



}
