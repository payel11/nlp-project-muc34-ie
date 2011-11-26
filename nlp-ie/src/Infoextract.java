package src;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.tregex.ParseException;


public class Infoextract {

	/**
	 * @param args
	 */

	Parser parser;
	Reader reader;
	Processor p;
	ArrayList<Template> t;
	String curDir = System.getProperty("user.dir");
	LexicalizedParser tregexParser;
	String model = "C:/Tools/POSTagger/stanford-postagger-2011-09-14/models/left3words-wsj-0-18.tagger";// set other path
	MaxentTagger tagger;

	public Infoextract() throws Exception  {

		this.reader = new Reader();
		this.p =  new Processor();
		this.t = new ArrayList<Template>();
		this.parser = new Parser();
		this.tregexParser = new LexicalizedParser(curDir+"/englishPCFG.ser.gz");
		tagger = new MaxentTagger(model);
	}



	public ArrayList<Template> getT() {
		return t;
	}

	/*public void setT(Template t) {
		this.t = t;
	}*/

	private void initProcessing(ArrayList<String> Sentences)
	{
		p.setLists(Sentences);
	}

	private String processId()
	{
		return p.processId();
	}

	private String processIncident()
	{
		return  p.processIncident();
	}

	private String processWeapon(String type)
	{
		return p.processWeapon(type);
	}
	private String processEvents(Parser parser, Template t)
	{
		return p.processEvents(parser, t);
	}

	/*private String processTarget(MaxentTagger tagger) throws Exception
	{
		return p.processTarget(tagger);
	}*/


	public void processDocument(String file,Parser parser) throws Exception
	{
		ArrayList<ArrayList<String>> stories = reader.readFile(file);

		int storiesSize = stories.size();
		System.out.println("Number of stories:"+storiesSize);

		for(int i=0;i<storiesSize;i++)
		{
			Template t1= new Template();
			ArrayList <String> localText = new ArrayList<String>();

			int size = stories.get(i).size();
			for(int j=0;j<size;j++)
			{
				localText.add(stories.get(i).get(j));
				//System.out.println(localText.get(j));
			}

			initProcessing(localText);//set the texts for the processor object
			t1.setId(processId());
			t1.setIncident(processIncident());
			t1.setWeapon(processWeapon(t1.getIncident()));
			t1.setTarget(p.processTarget(tagger,t1.getIncident()));
			
			String v= p.processVictim(tagger,t1.getIncident());
			System.out.println("\n\t Victim is:"+v);
			t1.setVictim(v);
			getT().add(t1);
		}
	}

	public static void main(String[] args) throws Exception
	{
		Infoextract ie = new Infoextract();
		ie.processDocument(args[0], ie.parser);

		if(args.length != 1)
		{
			System.err.print("\n\t InfoExtract accepts only one argument.");
		}
		else
		{
			ie.printOutput(args[0]);
		}

	}
	public void printOutput(String path)
	{
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path+".templates"));
			String text = new String("");
			int size = getT().size();
			for(int i=0;i<size;i++)
			{
				Template t = getT().get(i);
				text = text + "\n\nID: "+t.getId()+"\nINCIDENT: "+t.getIncident();
				text = text + "\nWEAPON: "+t.getWeapon()+"\nPERP INDIV: "+t.getPerpetratorPerson();
				text = text + "\nPERP ORG: "+t.getPerpetratorOrg()+"\nTARGET: "+t.getTarget();
				text = text + "\nVICTIM: "+t.getVictim();


			}

			out.write(text);
			out.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		//System.out.println(text);
		System.out.println("\n File written successfully.");

	}

}
