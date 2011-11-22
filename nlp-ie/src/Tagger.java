
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
		
		String se  = "";
		Tagger d = new Tagger();
		d.getNoun(se, "his");
		
	}

	public void getNoun(String se, String token) throws ClassNotFoundException, IOException
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
	}

}
