


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Infoextract {

	/**
	 * @param args
	 */

	Parser parser;
	Reader reader;
	Processor p;
	ArrayList<Template> t;
	public Infoextract() {

		this.reader = new Reader();
		this.p =  new Processor();
		this.t = new ArrayList<Template>();
		this.parser = new Parser();
	}

	/*private ArrayList <String> getTextFromFile(String file)
	{
		return reader.readFile(Utility.getFilePath(file));
		//return reader.readFile(Utility.getFilePath("texts","DEV-MUC3-0006"));

	}*/

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
	private String processPerpetrator(Parser parser, Template t)
	{
		return p.processPerpIndiv(parser, t);
	}
	
	

	public void processDocument(String file,Parser p)
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
			
			//TODO rm print
			System.out.println("\n" + t1.getId() + ":");
			
			//t1.setPerpetratorPerson(processPerpetrator(p));
			processPerpetrator(p, t1);
			//processVictim(p, t1);
			//processTarget();
			getT().add(t1);
		}
	}



	

	public static void main(String[] args)
	{
		Infoextract ie = new Infoextract();
		ie.processDocument(args[0], ie.parser);
		ie.printOutput(args[0]);
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
