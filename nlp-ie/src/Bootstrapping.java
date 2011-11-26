package src;


/**
 * The aim of this class is to develop a a program which 
 * knows how to bootstrap from a set of seed patterns.
 * 
 * After some iterations it should produce the modt promising
 * patterns for our IE task.
 * 
 * @author Abdul
 * 
 *
 */
public class Bootstrapping {
	
	public static void main(String[] args) {
		
		/* read seed patterns from file */
		
		/* parse patterns into suitable format */
		String[] patterns = parsePatterns();
		
		/* 1. search through collections for pattern
		 * matches */
		
		/* 2. figure out the context i.e. verbs, nouns */
		
		/* 3. from previous context try to extract keywords
		 * which would be useful to extract more patterns 
		 * in subsequent phases */
		
		/* 4. now use RLogF to score each pattern */
		
		/* 5. pick up the top 5 excluding what we have already
		 * in the seed set */
		
		/* 6. repeat previous steps using extrcated patterns */
		
		
		
	}
	
	private static String[] parsePatterns() {
		String[] p = {"ATTACKED", ""}; 
		return p;
	}

	public static String get_head_noun(String token) {
		String[] new_tokens = token.split("\\s");
		return new_tokens[new_tokens.length-1].trim();
	}

}
