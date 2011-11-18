


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Reader {
	ArrayList<ArrayList<String>> files = new ArrayList <ArrayList<String>>();


	public ArrayList<ArrayList<String>> readFile(String name)
	{

		try{
			FileInputStream fileStream = new FileInputStream(name);
			DataInputStream inputStream = new DataInputStream(fileStream);			
			BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			ArrayList<String> sentences = null;
			int fileIndex =0;
			while ((line = bufferdReader.readLine()) != null)   {

				if(line.startsWith("DEV-MUC3-")||line.startsWith("TST1-MUC3-")||
						line.startsWith("TST2-MUC4-"))// new story
				{
					if(fileIndex >0)
					{
						ArrayList <String> newStory = new ArrayList<String>();
						int size = sentences.size();
						for(int i=0;i<size;i++)
						{
							newStory.add(sentences.get(i));
						}
						files.add(newStory);
						sentences.clear();
					}
					else
					{
						sentences = new ArrayList<String>();
					}

					fileIndex++;
				}
				sentences.add(line);
			}//while
			files.add(sentences);

			/*System.out.println("\n total files:"+files.size());
			for(int i=0;i<files.size();i++)
			{	
				int size = files.get(i).size();
				for(int j=0;j<size;j++)
				{
					System.out.println(files.get(i).get(j));
				}
			}*/
			inputStream.close();
		}catch (Exception e){
			System.out.print("Could not read file specified:" +name);
			System.exit(1);
		}

		return files;

	}


	public ArrayList<String> readFile1(String name)
	{
		ArrayList<String> sentences = new ArrayList<String>();
		try{
			FileInputStream fileStream = new FileInputStream(name);
			DataInputStream inputStream = new DataInputStream(fileStream);			
			BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			sentences.clear();

			while ((line = bufferdReader.readLine()) != null)   {

				sentences.add(line);

			}

			inputStream.close();
		}catch (Exception e){
			System.out.print("Could not read file specified:" +name);
			///System.out.print(""+e.);
			System.exit(1);
		}

		return sentences;

	}
}
