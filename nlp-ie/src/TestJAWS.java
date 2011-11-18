import edu.smu.tspell.wordnet.*;

/**
 * Displays word forms and definitions for synsets containing the word form
 * specified on the command line. To use this application, specify the word
 * form that you wish to view synsets for, as in the following example which
 * displays all synsets containing the word form "airplane":
 * <br>
 * java TestJAWS airplane
 */
public class TestJAWS
{

	/**
	 * Main entry point. The command-line arguments are concatenated together
	 * (separated by spaces) and used as the word form to look up.
	 */
	public static void main(String[] args)
	{
		NounSynset nounSynset;
		NounSynset[] hyponyms;

		System.setProperty("wordnet.database.dir", "C:/Program Files (x86)/WordNet/2.1/dict"); 
		WordNetDatabase database = WordNetDatabase.getFileInstance();
		Synset[] synsets = database.getSynsets("Abdul", SynsetType.NOUN);
		for (int i = 0; i < synsets.length; i++) {
			nounSynset = (NounSynset)(synsets[i]);
			hyponyms = nounSynset.getHyponyms();
			//System.err.println(nounSynset.getWordForms()[0] +
				//	": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");
		} 
	}
}