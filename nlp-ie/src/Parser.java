import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.EnglishGrammaticalRelations;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TypedDependency;

public class Parser {
	
	/* options we need to pass to parser */
	
	private final String grammarFile = "/home/akabdul/workspace/nlp-ie/englishPCFG.ser.gz";//
	//private final String grammarFile = "C:/Users/Prafulla/SetUps/StanfordParser/stanford-parser-2011-09-14/" +"stanford-parser-2011-09-14/grammar/englishPCFG.ser.gz";//
	
	private LexicalizedParser lp = 
		      new LexicalizedParser(Utility.getFilePath(grammarFile));
	
	
	private final GrammaticalRelation NSUBJ = EnglishGrammaticalRelations.NOMINAL_SUBJECT;
	private final GrammaticalRelation NSUBJPASS = EnglishGrammaticalRelations.NOMINAL_PASSIVE_SUBJECT;
	private final GrammaticalRelation AGENT = EnglishGrammaticalRelations.AGENT;
	private final GrammaticalRelation DOBJ = EnglishGrammaticalRelations.DIRECT_OBJECT;
	private final GrammaticalRelation NN = EnglishGrammaticalRelations.NOUN_COMPOUND_MODIFIER;
	private final GrammaticalRelation AMOD = EnglishGrammaticalRelations.ADJECTIVAL_MODIFIER;
	private final GrammaticalRelation APPOS = EnglishGrammaticalRelations.APPOSITIONAL_MODIFIER;
	
	
	
	
	
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
		
		
		TypedDependency td;
		
		for (int i = 0; i < tdl.size(); i++) {
			td = tdl.get(i);
			
			TreeGraphNode node = td.gov();
			
			
			if(node.value().equalsIgnoreCase(verb)) {
				if(td.reln() == NSUBJ
				|| td.reln() == AGENT) {
					
					Tree parent = td.dep().parent();
				    while(! parent.isPhrasal())
				    	parent = parent.parent();
				    
				   String NP = "";
				   for(Tree tree : parent.getLeaves()){
				    	
					   NP += (tree.value().equals("a") 
							   || tree.value().equals("the")
							   ? "" : tree.value().toUpperCase() + " ");
				    }
				   //TODO: need to hook this method into the PRocessor class
					processPerpOrg(tdl, i, td.dep().value());
					return NP;
				}
			}
		}
		
		return null;
	}
	
	public String processVictim(List<TypedDependency> tdl, String verb) {
		
		
		TypedDependency td;
		
		for (int i = 0; i < tdl.size(); i++) {
			td = tdl.get(i);
			
			TreeGraphNode node = td.gov();
			
			
			if(node.value().equalsIgnoreCase(verb)) {
				if(td.reln() == NSUBJPASS
				|| td.reln() == DOBJ) {
					
					Tree parent = td.dep().parent();
				    while(! parent.isPhrasal())
				    	parent = parent.parent();
				    
				   String NP = "";
				   for(Tree tree : parent.getLeaves()){
				    	
					   NP += (tree.value().equals("a") 
							   || tree.value().equals("the")
							   ? "" : tree.value().toUpperCase() + " ");
				    }
					
					return NP;
				}
			}
		}
		
		return null;
	}
	
	
	public String processPerpOrg(List<TypedDependency> tdl, int k, String prep) {
		
		
		while(k < tdl.size()) {
			
			TypedDependency td = tdl.get(k);
			if(td.reln().toString().equals("prep_of")) {
			
				if(td.gov().value().equals(prep)) {
					
					Tree parent = td.dep().parent();
				    while(! parent.isPhrasal())
				    	parent = parent.parent();
				    
				   String NP = "";
				   for(Tree tree : parent.getLeaves()){
				    	
					   NP += (tree.value().equals("a") 
							   || tree.value().equals("the")
							   ? "" : tree.value().toUpperCase() + " ");
				    }
				   
				   System.out.println("ORG ==> " + NP.toUpperCase());
				
				}	
			}
			++k;
					
			
		}
		
		return null;
	}
	
//	public String processVictim(List<TypedDependency> tdl, String verb) {
//		//List<TypedDependency> tdl = gs. typedDependenciesCCprocessed(true);
//		
//		
//		for (Iterator iterator = tdl.iterator(); iterator.hasNext();) {
//			TypedDependency typedDependency = (TypedDependency) iterator.next();
//			
//			if(typedDependency.gov().value().equalsIgnoreCase(verb)) {
//				if(typedDependency.reln().toString().equalsIgnoreCase("nsubjpass")
//						|| typedDependency.reln().toString().equalsIgnoreCase("dobj")) {
//					
//					
//					String victim =typedDependency.dep().value(); 
//					System.out.println("Victim:"+victim	);
//					return victim;
//					
//				}
//			}
//		}
//		
//		return null;
//	}
	
	public String processTarget(List<TypedDependency> tdl, String word) {
		for (Iterator iterator = tdl.iterator(); iterator.hasNext();)
		{
			TypedDependency typedDependency = (TypedDependency) iterator.next();
			
			if(typedDependency.gov().value().equalsIgnoreCase(word))
			{
				System.out.println("\n out:"+typedDependency.reln().toString());
				if(typedDependency.reln().toString().equalsIgnoreCase("rcmod")||
						typedDependency.reln().toString().equalsIgnoreCase("partmod"))
				{
					System.out.println("\n\t  location:"+ typedDependency.dep().value());
					
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
