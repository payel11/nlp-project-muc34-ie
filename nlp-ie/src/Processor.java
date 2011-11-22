



import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;


public class Processor {

	ArrayList <String> originalText = new ArrayList<String>();
	ArrayList <String> textWithoutHeading = new ArrayList<String>();
	ArrayList <Word> terroristDictionary = new ArrayList<Word>();
	String textsOnly ="";

	ArrayList<List<HasWord>> sentences = new ArrayList<List<HasWord>>();
	String matchedWord = "-";

	private TokenizerFactory<CoreLabel> tokenizerFactory = 
		PTBTokenizer.factory(new MyCoreLabelTokenFactory(), "invertible=true");
	
	NER ner = new NER();


	public String getTextsOnly() {
		return textsOnly;
	}


	public void setTextsOnly(ArrayList<String> textsOnly) {
		StringBuilder text = new StringBuilder();
		for (String line : textsOnly) {
			text.append(line +"\n");
		}

		this.textsOnly = text.toString();

		sentences.clear();
		DocumentPreprocessor dp = 
			new DocumentPreprocessor(new StringReader(getTextsOnly()));
		dp.setTokenizerFactory(tokenizerFactory);

		for (List<HasWord> list : dp) {
			sentences.add(list);
		}

	}




	int sentenceIndexForTriggerWord = -1;


	public Processor() {
	}

	public ArrayList<String> getOriginalText() {
		return originalText;
	}

	public void setOriginalText(ArrayList<String> originalText) {
		this.originalText = originalText;
	}

	public ArrayList<String> getTextWithoutHeading() {
		return textWithoutHeading;
	}

	public void setTextWithoutHeading(ArrayList<String> textWithoutHeading) {
		this.textWithoutHeading = textWithoutHeading;
	}

	public ArrayList<Word> getTerroristDictionary() {
		return terroristDictionary;
	}

	public void setTerroristDictionary(ArrayList<Word> terroristDictionary) {
		this.terroristDictionary = terroristDictionary;
	}

	public void setLists(ArrayList<String> sents)
	{
		setTerroristDictionary(Utility.readDictionary("terrorism"));
		setOriginalText(sents);

		//setting textwithoutHeading
		int size = sents.size();
		ArrayList<String> temp =new ArrayList<String>();

		for(int i=0;i<size;i++)
		{
			temp.add(sents.get(i));
		}

		temp.remove(0);
		if(temp.get(0)== null || temp.get(0).trim().length() == 0)
		{

			temp.remove(0);
		}
		if(temp.get(0).contains("[TEXT]"))
		{
			String tmp = temp.get(0).substring(temp.get(0).indexOf("[TEXT]")+6);
			temp.remove(0);
			temp.add(0, tmp);

		}


		setTextsOnly(temp);

		temp = convertLinesIntoSentences(temp);

		setTextWithoutHeading(temp);
		//System.out.print("\n\t Size of origninal text message:"+getTextWithoutHeading().size());
		//printArrayList(temp);
	}

	public ArrayList<String> convertLinesIntoSentences ( ArrayList<String> list)
	{
		ArrayList<String> sents = new ArrayList<String>();


		String text ="";
		//String forTextOnly = "";
		int size = list.size();
		for(int i=0;i<size;i++)
		{

			text = text + list.get(i) + " ";

		}
		//

		int i=0;
		size = text.length();
		String temp ="";
		while(i<size)
		{
			temp = temp + text.charAt(i);
			if((text.charAt(i)=='.')|| (text.charAt(i)=='!')||(text.charAt(i)=='?'))
			{  
				String temp1 = new String(temp);
				//forTextOnly = forTextOnly+ temp;
				sents.add(temp1);
				temp ="";
			}
			i++;
		}
		//setTextsOnly(forTextOnly);
		//System.out.println(forTextOnly);
		return sents;
	}

	public void printArrayList(ArrayList<String> list)
	{
		int size = list.size();

		for(int i=0;i<size;i++)
		{
			System.out.print("\n"+list.get(i));
		}
	}

	public String processId()
	{
		String id = getOriginalText().get(0).trim();

		if(id.contains("("))
		{
			id = id.substring(0, id.lastIndexOf("("));
		}
		return id;
	}

	public String processIncident()
	{
		ArrayList <String> text= getTextWithoutHeading();
		int size = text.size();
		int i =0;
		String incidentType = "-";
		while(i < size)
		{
			String sentence = text.get(i);
			sentence = Utility.removeSymbols(sentence);
			if((!Utility.isQuestion(sentence)))
			{	
				if(Utility.containsBombing(sentence) && !Utility.isDoubtFul(sentence) && !Utility.isMilitary(sentence))
				{ 
					incidentType = "BOMBING";
					sentenceIndexForTriggerWord = i;
					matchedWord = Utility.getBombingWord(sentence);
					return incidentType;
				}
				else if((Utility.containsTriggerWord(sentence, getTerroristDictionary()) && !Utility.containsNegation(sentence)))
				{
					//System.out.println(sentence);
					incidentType = Utility.getTriggerCategory(sentence, getTerroristDictionary());
					//System.out.println(incidentType);
					if(incidentType.equalsIgnoreCase("ARSON") ||incidentType.equalsIgnoreCase("KIDNAPPING") ||incidentType.equalsIgnoreCase("ROBBERY"))
					{
						sentenceIndexForTriggerWord = i;
						matchedWord = Utility.getTriggerWord();
						return incidentType;
					}
				}

			}
			i++;
		}
		if(incidentType.equalsIgnoreCase("-"))
		{
			incidentType = "ATTACK";
		}
		return incidentType;
	}


	public String processWeapon(String incidentType)
	{
		String weapon= "-";
		if(sentenceIndexForTriggerWord == -1)//no strong evidence of Attack
		{
			return "-";
		}
		else
		{
			int size = getTextWithoutHeading().size();
			for(int i= sentenceIndexForTriggerWord-1;i<sentenceIndexForTriggerWord+2;i++)
			{
				if(i>=0 && i<size)
				{

					String sentence = getTextWithoutHeading().get(i);
					weapon = Utility.getWeapon(sentence);
					if(!weapon.equalsIgnoreCase("-"))
					{
						return weapon;
					}
				}

			}

			if(incidentType.equals("BOMBING"))
			{
				return "BOMB";

			}
			else
			{
				return "-";
			}
		}
	}


	//	public void processMatches(Parser lp, String verb) 
	//	{
	//		DocumentPreprocessor dp = 
	//				new DocumentPreprocessor(new StringReader(getTextsOnly()));
	//		dp.setTokenizerFactory(tokenizerFactory);
	//			
	//		ArrayList<List<HasWord>> sentences = new ArrayList<List<HasWord>>();
	//		for (List<HasWord> list : dp) {
	//			sentences.add(list);
	//		}
	//		
	//		for(Word word: getTerroristDictionary()) {
	//			
	//			if(! word.getPOS().equalsIgnoreCase("V"))
	//				continue;
	//			
	//			//String verb = word.getSpForm();
	//			
	//			for (List<HasWord> list : sentences) {
	//				for (HasWord hasWord : list) {
	//					if(hasWord.word().equalsIgnoreCase(verb)) {
	//						matches.add(list);
	//						
	//						break;
	//					}
	//				}
	//			}
	//			
	//			
	//		}
	//		
	//	}  

	public String processEvents(Parser lp, Template t)
	{
		ArrayList<String> answers_PERP =  new ArrayList<String>();
		ArrayList<String> answers_VICTIM = new ArrayList<String>();


		for(Word word: getTerroristDictionary()) {

			if(! word.getPOS().equalsIgnoreCase("V"))
				continue;

			String verb = word.getSpForm();
			System.out.println(verb + ": ");
			

			// get matching sentences for verb
			List<List<HasWord>> matched_sents =  lp.match(sentences, verb);
			
			
			for (List<HasWord> list : matched_sents) {

				String sent = "";
				for (HasWord hasWord : list) {
				
					sent+= hasWord.word() + " ";
				}
				System.out.println(sent+"\n");
				
				//ArrayList<String> PERPIND = ner.getPersons(sent);
				//ArrayList<String> PERPORG = ner.getOrgs(sent);
				
				//System.out.println("PERSONS = " + PERPIND);
				//System.out.println("ORGS    = " + PERPORG);
				
				if(!list.isEmpty()) {
					Tree parseTree = lp.parseSentence(list);
					parseTree.pennPrint();
					System.out.println();

					TreebankLanguagePack tlp = new PennTreebankLanguagePack();
					GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
					GrammaticalStructure gs = gsf.newGrammaticalStructure(parseTree);
					List<TypedDependency> tdl = gs. typedDependenciesCCprocessed(true);

					System.out.println(tdl);
					System.out.println();

					String PERP = lp.processPerpetrator(tdl, verb); 

//					if(PERP != null) 
//					{
//						
//						for (String person: PERPIND) {
//							if(person.contains(PERP)) {
//								if(t.getPerpetratorPerson().equals("-"))
//									t.setPerpetratorPerson(person.toUpperCase());
//							}
//								
//						}
//						
//						for (String org: PERPORG) {
//							if(org.contains(PERP)) {
//								if(t.getPerpetratorOrg().equals("-"))
//									t.setPerpetratorOrg(org.toUpperCase());
//							}
//								
//						}
//			
//						answers_PERP.add(PERP.toUpperCase());
//					}

					String VICTIM = lp.processVictim(tdl, verb); 
					
					if(PERP != null) {
						if(t.getPerpetratorPerson().equals("-"))
							t.setPerpetratorPerson(PERP.toUpperCase());
						
						System.out.println("PERP: " + PERP.toUpperCase());
						answers_PERP.add(PERP.toUpperCase());
					}

					if(VICTIM != null) 
					{
						if(t.getVictim().equals("-"))
							t.setVictim(VICTIM.toUpperCase());

						System.out.println("VICTIM: " + VICTIM.toUpperCase());
						answers_VICTIM .add(VICTIM.toUpperCase());
					}
				}

			}

		}
//		String PERP = "", VICTIM= "";
//		for (String perp : answers_PERP) {
//			PERP += perp + " /";
//		}
//
//		for (String victim : answers_VICTIM) {
//			VICTIM += victim + " /";
//		}
//
//		t.setPerpetratorPerson(PERP);
//		t.setVictim(VICTIM);

		//TODO: rm print stmt
		System.out.println("PERP ===>> " +  answers_PERP);
		System.out.println("VICTIM ==>> " + answers_VICTIM);

		return "-";
	}


	
	
}//class ends







