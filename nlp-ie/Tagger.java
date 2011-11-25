
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

class Tagger {

	public static void main(String[] args) throws Exception {
		/* if (args.length != 2) 
    {
      System.err.println("usage: java TaggerDemo modelFile fileToTag");
      return;
    }*/

		String model = "C:/Tools/POSTagger/stanford-postagger-2011-09-14/models/left3words-wsj-0-18.tagger";// set other path
		MaxentTagger tagger = new MaxentTagger(model);

		String se  = "MEANWHILE MEDELLIN METROPOLITAN POLICE ANNOUNCED THAT EARLY THIS MORNING UNIDENTIFIED PERSONS PLACED 11 BOMBS IN VARIOUS GOVERNMENT BANK BRANCHES.";
		Tagger t = new Tagger();
		System.out.println("\n final output is:"+t.getMatchedNoun(t.getNoun(tagger, se),"bank"));

	}

	public ArrayList<String> getNoun(MaxentTagger tagger, String se) throws Exception
	{

		List<List<HasWord>> sentences = tagger.tokenizeText(new StringReader(se));
		String soln = "-";
		ArrayList<String> nounList = new ArrayList<String>();

		for (List<HasWord> sentence : sentences)
		{
			Tagger d = new Tagger();
			ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
			soln = Sentence.listToString(tSentence, false);

			System.out.println(soln);
		}
		String words [] = soln.split(" ");
		for(int i=0;i<words.length;i++)
		{
		
			String word = words[i];
			String w[] = word.split("/");
			String noun =new String("-");
			if(w[1].equalsIgnoreCase("JJ")||w[1].equalsIgnoreCase("NNP") ||w[1].equalsIgnoreCase("NNS")||w[1].equalsIgnoreCase("NNPS")|| w[1].contains("NN"))
			{
				noun = w[0];
				if(noun.endsWith("ed"))
				continue;
				
				int extensions =0;
				for(int j=i+1;j<words.length;j++)
				{
					String value = words[j].substring(words[j].lastIndexOf("/")+1, words[j].length());
					String value1 = words[j].substring(0,words[j].lastIndexOf("/"));
					System.out.println("Word:"+words[j]+"Value:"+value);
					if((value.contains("NNP")|| value.contains("NNPS")||value.contains("NNS")||value.contains("NN")||value1.equalsIgnoreCase("OF")))					
					{
						if(value1.endsWith("ING") && !value1.equalsIgnoreCase("BUILDING"))
						{
							i = i+ extensions;
							break;
						}
						else
						{
							String p = words[j].substring(0,words[j].lastIndexOf("/"));
							noun = noun + " "+ p;
							extensions ++;
						}
					}
					else
					{
						i = i+ extensions;
						break;
					}
				}
				if(!noun.equalsIgnoreCase("-"))
				{

					nounList.add(noun);
					noun = "-";
				}
			}//end if 
		}
		int size= nounList.size();
		for(int i=0;i<size;i++)
		{
			System.out.println(nounList.get(i));
		}
		return nounList;
	}

	public String getMatchedNoun(ArrayList<String> nounList, String location)
	{
		//kill of first word

		String matchedNoun ="-";
		int size = nounList.size();
		for(int i=0;i<size;i++)
		{
			String temp = nounList.get(i);
			System.out.println("temp:"+temp);

			if(temp.contains(location)||temp.contains(location.toUpperCase()))
			{
				return temp;
			}
		}
		return matchedNoun;
	}
	/*public void getNoun(String se, String token) throws ClassNotFoundException, IOException
	{
		String model = "C:/Tools/POSTagger/stanford-postagger-2011-09-14/models/left3words-wsj-0-18.tagger";// set other path
		MaxentTagger tagger = new MaxentTagger(model);
		List<List<HasWord>> sentences = tagger.tokenizeText(new StringReader(se));
		for (List<HasWord> sentence : sentences)
		{
			ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
			String soln = Sentence.listToString(tSentence, false);
			//System.out.println(soln);

			boolean gotPronoun = false;
			String [] words = soln.split(" ");
			int size = words.length;
			for(int i=size-1;i>=0;i--)
			{
				String word = words[i];
				//System.out.println(word);

				String w1 = word.substring(word.lastIndexOf("/")+1);
				String w0 = word.substring(0, word.lastIndexOf("/"));
				//System.out.println(w1);

				if(w1.contains("PRP")&& w0.equalsIgnoreCase(token))
				{

					System.out.println("Found a Pronun:"+w0);
					gotPronoun = true;
				}

				if(gotPronoun)
				{
					if(w1.contains("NNP"))
					{
						System.out.println("found it's noun:"+w0);
					}
				}
			}//for ends
		}//sentences for ends
	}

	public void getNounUsingFile(File path, int index, String token) throws ClassNotFoundException, IOException
	{
		String model = "C:/Tools/POSTagger/stanford-postagger-2011-09-14/models/left3words-wsj-0-18.tagger";// set other path
		MaxentTagger tagger = new MaxentTagger(model);
		List<List<HasWord>> sentences = tagger.tokenizeText(new BufferedReader(new FileReader(path)));
		for (List<HasWord> sentence : sentences)
		{
			ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
			String soln = Sentence.listToString(tSentence, false);
			//System.out.println(soln);

			boolean gotPronoun = false;
			String [] words = soln.split(" ");
			int size = words.length;
			for(int i=size-1;i>=0;i--)
			{
				String word = words[i];
				//System.out.println(word);

				String w1 = word.substring(word.lastIndexOf("/")+1);
				String w0 = word.substring(0, word.lastIndexOf("/"));
				//System.out.println(w1);

				if(w1.contains("PRP")&& w0.equalsIgnoreCase(token))
				{

					System.out.println("Found a Pronun:"+w0);
					gotPronoun = true;
				}

				if(gotPronoun)
				{
					if(w1.contains("NNP"))
					{
						System.out.println("found it's noun:"+w0);
					}
				}
			}//for ends
		}//sentences for ends
	}*/

}
