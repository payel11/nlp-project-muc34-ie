


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.rmi.CORBA.Util;

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

	static
	{
		nonConfirmationWords = new ArrayList<String>();
		negationWords = new ArrayList<String>();
		militaryWords =  new ArrayList<String>();
		weapons = new ArrayList <String>();
		
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
		weapons.add("rifle");weapons.add("machine-gun");weapons.add("bullet");
		weapons.add("devices");//handle later
		
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
						return true;
					}
					
				}
			}
		}
		return false;
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
	public static String getWeapon(String sentence)
	{
		String [] words = sentence.split(" ");
		int size = words.length;
		for (int i=0;i<size;i++)
		{
			//String weapon =  weapons.get(i);//
			for(int j=0;j<weapons.size();j++)
			{
				
				if(words[i].equalsIgnoreCase(weapons.get(j)) ||words[i].equalsIgnoreCase(weapons.get(j)+"s"))
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

	
	
	
	
	public static void main(String... args)
	{
		Utility u = new Utility();
		System.out.print(Utility.isValidWord("-"));
	}
	
	
	
}
