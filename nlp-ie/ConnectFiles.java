import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;


public class ConnectFiles {
	String text = "";
	public static void main(String... args){

		ConnectFiles c = new ConnectFiles();
		//c.readFiles("C:/Users/Prafulla/Desktop/Prafulla/NLP_IE/testset");
		//c.writeFile("C:/Users/Prafulla/Desktop/Prafulla/NLP_IE/testfile.txt");
		//c.readFiles("C:/Users/Prafulla/Desktop/Prafulla/NLP_IE/answerkeys");
		//c.writeFile("C:/Users/Prafulla/Desktop/Prafulla/NLP_IE/answerkeys/keys.txt");
		
		c.readFiles("C:/Users/Prafulla/Desktop/Prafulla/NLP_IE/developset/texts");
		c.writeFile("C:/Users/Prafulla/Desktop/texts.txt");
	}

	public void readFiles(String path)
	{

		ArrayList<String> files = getAllFiles(path);
		System.out.println(files.size());

		int size= files.size();
		for(int i=0;i<size;i++)
		{
			Reader r = new Reader();
			ArrayList<String> lines = r.readFile1(files.get(i));

			int s1 = lines.size();

			for(int j=0;j<s1;j++)
			{
				text = text + lines.get(j) + "\n";
			}			
		}
	}


	public void writeFile(String path)
	{

		try{
			FileWriter fstream = new FileWriter(path);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(text);
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	public static ArrayList <String> getAllFiles(String dir)
	{
		ArrayList <String> files = new ArrayList <String>();
		String completePath =  dir;
		File dirFile = new File(completePath);
		System.out.println("path:"+completePath);
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

}
