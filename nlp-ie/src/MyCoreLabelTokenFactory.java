



import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;

public class MyCoreLabelTokenFactory extends CoreLabelTokenFactory {
	
	@Override
	public CoreLabel makeToken() {
		// TODO Auto-generated method stub
		return super.makeToken();
	}
	
	@Override
	public CoreLabel makeToken(CoreLabel labelToBeCopied) {
		// TODO Auto-generated method stub
		return super.makeToken(labelToBeCopied);
	}

	
	@Override
	public CoreLabel makeToken(String str, int begin, int length) {
		// TODO Auto-generated method stub
		return super.makeToken(str.toLowerCase(), begin, length);
	}
	
	@Override
	public CoreLabel makeToken(String str, String original, int begin,
			int length) {
		// TODO Auto-generated method stub
		return super.makeToken(str.toLowerCase(), original, begin, length);
	}
	
	@Override
	public CoreLabel makeToken(String[] keys, String[] values) {
		// TODO Auto-generated method stub
		return super.makeToken(keys, values);
	}
	
	
}
