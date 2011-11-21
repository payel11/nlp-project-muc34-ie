import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import java.util.ArrayList;
import java.io.IOException;

public class NER {

	static AbstractSequenceClassifier classifier = null;
	static
	{
		String curDir = System.getProperty("user.dir");
		String serializedClassifier = curDir+ "/classifiers/all.3class.distsim.crf.ser";
		classifier= CRFClassifier.getClassifierNoExceptions(serializedClassifier);
	}
	ArrayList <String> orgs = new ArrayList<String>();

	public static void main(String[] args) throws IOException 
	{
		String s1 = "Brack Obama and Bill Clinton the minister [CM] go to school at Stanford University which is located in California.";
		NER n = new NER();
		n.getPersons(s1);
		System.out.println("\n");
		n.getOrgs(s1);
		System.out.println("\n");
		n.getLocations(s1);
	}



	public void getPersons(String sent)
	{
		sent = removeSymbols(sent);
		String soln = classifier.classifyToString(sent);
		
		
		ArrayList<String> persons = new ArrayList <String>();
		String [] words = soln.split(" ");

		for(int i=0;i<words.length;i++)
		{
			String word = words[i];
			String w[] = word.split("/");
			String person =new String("-");
			if(w[1].equalsIgnoreCase("person"))
			{
				person = w[0];
				int extensions =0;
				for(int j=i+1;j<words.length;j++)
				{
					if(words[j].substring(words[j].lastIndexOf("/"), words[j].length()).contains("PERSON"))
					{
						String p = words[j].substring(0,words[j].lastIndexOf("/"));
						person = person + " "+ p;
						extensions ++;
					}
					else
					{
						i = i+ extensions;
						break;
					}
				}
				if(!person.equalsIgnoreCase("-"))
				{
					persons.add(person);
					person = "-";
				}
			}//end if 
		}
		
		int size= persons.size();
		for(int i=0;i<size;i++)
		{
			System.out.println(persons.get(i));
		}
	}//end method


	public void getOrgs(String sent)
	{
		sent = removeSymbols(sent);
		String soln = classifier.classifyToString(sent);
		ArrayList<String> orgs = new ArrayList <String>();
		String [] words = soln.split(" ");

		for(int i=0;i<words.length;i++)
		{
			String word = words[i];
			String w[] = word.split("/");
			String org =new String("-");
			if(w[1].equalsIgnoreCase("ORGANIZATION"))
			{
				org = w[0];
				int extensions =0;
				for(int j=i+1;j<words.length;j++)
				{
					if(words[j].substring(words[j].lastIndexOf("/"), words[j].length()).contains("ORGANIZATION"))
					{
						String p = words[j].substring(0,words[j].lastIndexOf("/"));
						org = org + " "+ p;
						extensions ++;
					}
					else
					{
						i = i+ extensions;
						break;
					}
				}
				if(!org.equalsIgnoreCase("-"))
				{
					orgs.add(org);
					org = "-";
				}
			}//end if 
		}
		
		int size= orgs.size();
		for(int i=0;i<size;i++)
		{
			System.out.println(orgs.get(i));
		}
	}//end method


	public void getLocations(String sent)
	{
		sent = removeSymbols(sent);
		String soln = classifier.classifyToString(sent);
		
		ArrayList<String> locs = new ArrayList <String>();
		String [] words = soln.split(" ");
		
		for(int i=0;i<words.length;i++)
		{
			String word = words[i];
			String w[] = word.split("/");
			String loc =new String("-");
			if(w[1].equalsIgnoreCase("LOCATION"))
			{
				loc = w[0];
				int extensions =0;
				for(int j=i+1;j<words.length;j++)
				{
					if(words[j].substring(words[j].lastIndexOf("/"), words[j].length()).contains("LOCATION"))
					{
						String p = words[j].substring(0,words[j].lastIndexOf("/"));
						loc = loc + " "+ p;
						extensions ++;
					}
					else
					{
						i = i+ extensions;
						break;
					}
				}
				if(!loc.equalsIgnoreCase("-"))
				{
					locs.add(loc);
					loc = "-";
				}
			}//end if 
		}
		
		int size= locs.size();
		for(int i=0;i<size;i++)
		{
			System.out.println(locs.get(i));
		}
	}//end method
	
	
	public static String removeSymbols(String sentence)
	{
		sentence = sentence.replaceAll("," , "");
		sentence = sentence.replaceAll("\"", "");
		sentence = sentence.replaceAll("\\.", "");
		sentence = sentence.replaceAll(";", "");
		sentence = sentence.replaceAll("!", "");
		sentence = sentence.replaceAll(":", "");
		
		return sentence;
		
	}
}