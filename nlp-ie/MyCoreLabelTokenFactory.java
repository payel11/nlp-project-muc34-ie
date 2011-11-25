



import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;

public class MyCoreLabelTokenFactory extends CoreLabelTokenFactory {
	
	public CoreLabel makeToken(String str, int begin, int length) {
		CoreLabel c;  
		if(str.equals("-LRB-") 
				|| str.equals("-RRB-"))
			c = super.makeToken(str, begin, length);
		else
			c = super.makeToken(str.toLowerCase(), begin, length);
		
		
		
		return c;
	}
	
	
}
