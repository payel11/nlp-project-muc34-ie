



import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.text.SimpleAttributeSet;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.SimpleTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class Parser {
	
	/* options we need to pass to parser */
	private String options;
	
	private final String grammarFile = "/home/akabdul/workspace/nlp-ie/englishPCFG.ser.gz";
	
	private LexicalizedParser lp = 
		      new LexicalizedParser(Utility.getFilePath(grammarFile));
	
	
	public List<List<HasWord>> match(ArrayList<List<HasWord>> sentences, String word) {
		
		List<List<HasWord>> result = new ArrayList<List<HasWord>>();
		
		for (List<HasWord> list : sentences) {
			for (HasWord hasWord : list) {
				if(hasWord.word().equalsIgnoreCase(word)) {
					result.add(list);
					
					break;
				}
			}
		}
		
		return result;
		
	}
	
	public Tree parseSentence(List<HasWord> sentence) {
		
		return lp.apply(sentence);
	}
	
	public String processPerpetrator(List<TypedDependency> tdl, String verb) {
		//List<TypedDependency> tdl = gs. typedDependenciesCCprocessed(true);
		
				
		for (Iterator iterator = tdl.iterator(); iterator.hasNext();) {
			TypedDependency typedDependency = (TypedDependency) iterator.next();
			
			if(typedDependency.gov().value().equalsIgnoreCase(verb)) {
				if(typedDependency.reln().toString().equalsIgnoreCase("nsubj")
						|| typedDependency.reln().toString().equalsIgnoreCase("agent")) {
					return typedDependency.dep().value();
				}
			}
		}
		
		return null;
	}
	
	
	public String processVictim(List<TypedDependency> tdl, String verb) {
		//List<TypedDependency> tdl = gs. typedDependenciesCCprocessed(true);
		
		
		for (Iterator iterator = tdl.iterator(); iterator.hasNext();) {
			TypedDependency typedDependency = (TypedDependency) iterator.next();
			
			if(typedDependency.gov().value().equalsIgnoreCase(verb)) {
				if(typedDependency.reln().toString().equalsIgnoreCase("nsubjpass")
						|| typedDependency.reln().toString().equalsIgnoreCase("dobj")) {
					return typedDependency.dep().value();
				}
			}
		}
		
		return null;
	}
	
	
	public Tree findVPTree(Tree root, String verb) {
		
//		while(! tmp.isLeaf()) {
//			if(tmp.value().equals(verb))
//				return tmp;
//			
//			tmp = root.
//		}
		
//		for (Iterator iterator = root.iterator(); iterator.hasNext();) {
//			Tree tree = (Tree) iterator.next();
//			
//			tree.pennPrint();
//			if(tree.value().equals("VP")) {
//				
//				tmp = tree;
//				break;
//			}
//		}
		
		Tree parent = root.skipRoot();
		List<Tree> children = parent.getLeaves();
		
		//TestParser.printTreeLeaves(parent);
		
		for (int i = 0; i < children.size(); i++) {
			if(children.get(i).isPhrasal() 
					&& children.get(i).value().equals("VP")) {
				parent = children.get(i);
				List<Tree> leaves = parent.getLeaves();
				for (Tree tree : leaves) {
					if(tree.value().equalsIgnoreCase(verb))
						return parent;
				}
				children.addAll(parent.getChildrenAsList());
			
			} /*else
			//children.addAll(children.get(i).getChildrenAsList());
			if(children.get(i).isLeaf()
					&& children.get(i).value().equalsIgnoreCase(verb)) {
				//found the verb
				return children.get(i).parent();
						 
			}*/
			
		}
			
		
		
//		Tree VB = null;
//		for (int i = 0; i < children.length; i++) {
////			for (Tree tree = children[i];)
////			if(children[i].isPhrasal() && children[i].value().equals("VP")) {
////				VB
////			}
//			
//			if (children[i].value().equalsIgnoreCase("VP")){
//				return children[i];
//			}
//			
//			
//		}
//		
		
		return parent;
	}
	
	public Tree findNP (Tree root, Tree VP) {
		
		//sentence.pennPrint();
		
//		
//		Tree VP = root.skipRoot();
//		Tree[] children = VP.children();
//		
//		
//		for (int i = 0; i < children.length; i++) {
//			if (children[i].value().equalsIgnoreCase("VP")){
//				VP = children[i];
//			}
//			
//			
//		}
		
		
		/* get siblings of VP and find the NP */
		List<Tree> tmp = VP.siblings(root);
		
		for (Tree tree : tmp) {
			
			if(tree.value().equals("NP"))
				return tree;
		}
		
		return null;
		
	}
	
	
	

}
