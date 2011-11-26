package src;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to extract key templates suitable for project from official
 * key distributed by MUC34 website and merge them into single file 
 * 
 * @author akabdul
 *
 */


public class ExtractDevAnswerKey {
	
	public static void main(String[] args) throws IOException {
		
		String dir = Utility.getCurrentDir() + "/key-dev-muc34/";
		
		ArrayList<String> files = Utility.getAllFiles("key-dev-muc34");
		
		System.out.println(files);
		
		BufferedWriter out = new BufferedWriter(new FileWriter(dir+"allAnswersKey"));
		
		for (String filename : files) {
			
			BufferedReader in = new BufferedReader(new FileReader(filename));
			
			String line = null;//in.readLine();
					
			
			while((line = in.readLine()) != null) {
				
				if(! line.startsWith("0"))
					continue; // skip empty lines

				
				String id = "-", incident = "-", weapon = "-",
						indiv = "-", org = "-", target = "-",
						victim = "-";
				String[] toks;
				
				System.out.println(line);
				
//				while(! line.startsWith("0") )
//					line = in.readLine();
				toks = line.split("\\s+");
				id = toks[3];
				
				
				while(! (line = in.readLine()).startsWith("4") );
				toks = line.split("\\s+");
				incident = (toks[3].equals("*") ? "ATTACK" : toks[3]);
				
				
				while(! (line = in.readLine()).startsWith("6") );
				toks = line.split("\\s+");
				weapon = removeQuotes(toks[4]);
				for (int i = 5; i < toks.length; i++) {
					weapon += " " + removeQuotes(toks[i]);
				}
				while(! (line = in.readLine()).startsWith("7") ) {
					weapon += "\n\t\t\t" + removeQuotes(line.trim());
				}
				
				
				while(! (line = in.readLine()).startsWith("9") );
				toks = line.split("\\s+");
				indiv = removeQuotes(toks[4]);
				for (int i = 5; i < toks.length; i++) {
					indiv += " " + removeQuotes(toks[i]);
				}
				while(! (line = in.readLine()).startsWith("10") ) {
					indiv += "\n\t\t\t" + removeQuotes(line.trim());
				}
				
				
				toks = line.split("\\s+");
				org = removeQuotes(toks[4]);
				for (int i = 5; i < toks.length; i++) {
					org += " " + removeQuotes(toks[i]);
				}
				while(! (line = in.readLine()).startsWith("11") ) {
					org += "\n\t\t\t" + removeQuotes(line.trim());
				}
				
				
				
				while(! (line = in.readLine()).startsWith("12") );
				toks = line.split("\\s+");
				target = removeQuotes(toks[4]);
				for (int i = 5; i < toks.length; i++) {
					target += " " + removeQuotes(toks[i]);
				}
				while(! (line = in.readLine()).startsWith("13") ) {
					target += "\n\t\t\t" + removeQuotes(line.trim());
				}
				
				
				
				while(! (line = in.readLine()).startsWith("18") );
				toks = line.split("\\s+");
				victim = removeQuotes(toks[4]);
				for (int i = 5; i < toks.length; i++) {
					victim += " " + removeQuotes(toks[i]);
				}
				while(! (line = in.readLine()).startsWith("19") ) {
					victim += "\n\t\t\t" + removeQuotes(line.trim());
				}
				
				if(victim.equals("-")) {
					toks = line.split("\\s+");
					victim = removeQuotes(toks[4]);
					for (int i = 5; i < toks.length; i++) {
						victim += " " + removeQuotes(toks[i]);
					}
					while(! (line = in.readLine()).startsWith("20") ) {
						victim += "\n\t\t\t" + removeQuotes(line.trim());
					}
				}
				
				while(! (line = in.readLine()).startsWith("24") );
				
				//write out a template to file
				out.write("\nID:\t\t\t" + id);
				out.write("\nINCIDENT:\t"+incident);
				out.write("\nWEAPON:\t\t" + weapon);
				out.write("\nPERP INDIV:\t" + indiv);
				out.write("\nPERP ORG:\t" + org);
				out.write("\nTARGET:\t\t" + target);
				out.write("\nVICTIM:\t\t" + victim);
				out.write("\n");
				
				
				
				
				
				
		
//				int lstLine = -1;
//				
//				do {
//					
//					if(line.startsWith("0")) {
//						
//						//System.out.println(toks[3]);
//						
//						lstLine = 0;
//					}
//					
//					else if (line.startsWith("4")) {
//						String[] toks = line.split("\\s+");
//						incident = toks[3];
//						lstLine = 4;
//					}
//					
//					else if(line.startsWith("6")) {
//						String[] toks = line.split("\\s+");
//						
//						weapon = removeQuotes(toks[4]);
//						lstLine = 6;
//					}
//					
//					else if(line.startsWith("9")) {
//						String[] toks = line.split("\\s+");
//						indiv = removeQuotes(toks[4]);
//						if(toks.length > 6) {
//							indiv += toks[5];
//							//System.out.println(filename + ": " + toks[4] + " " + toks.length);
//							indiv += removeQuotes(toks[6]);
//						}
//						lstLine = 9;
//					}
//					
//					else if(line.startsWith("10")) {
//						String[] toks = line.split("\\s+");
//						org = removeQuotes(toks[4]);
//						if(toks.length > 6) {
//							org += toks[5];
//							org += removeQuotes(toks[6]);
//						}
//						lstLine = 10;
//					} 
//					
//					else if(line.startsWith("12")) {
//						String[] toks = line.split("\\s+");
//						target = removeQuotes(toks[4]);
//						if(toks.length > 6) {
//							target += toks[5];
//							target += removeQuotes(toks[6]);
//						}
//						lstLine = 12;
//					} 
//					
//					else if(line.startsWith("18")) {
//						String[] toks = line.split("\\s+");
//						victim = removeQuotes(toks[4]);
//						if(toks.length > 6) {
//							victim += toks[5];
//							victim += removeQuotes(toks[6]);
//						}
//						lstLine = 18;
//					} 
//					
//					else if(line.startsWith("19") && victim.equals("-")) {
//						String[] toks = line.split("\\s+");
//						victim = removeQuotes(toks[4]);
//						if(toks.length > 6) {
//							victim += toks[5];
//							victim += removeQuotes(toks[6]);
//						}
//						lstLine = 19;
//					} 
//					
//					else {
//					
//						
//					}
//					
					
//					
//				} while( (line = in.readLine()) != null 
//						 && !line.trim().startsWith("24"));
//				
				
				
				 
				
			}
			
			in.close();
			
		}
		
		out.close();
		
		
		
	}
	
	
	public static String removeQuotes(String word) {
		if(word.equals("*")) 
			return "-";
		
		else
			return word.replace("\"", "").replace("\\", "");
	}

}
