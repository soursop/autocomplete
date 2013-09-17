package search.autocomplete.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EngParsor implements Parsor {
	
	List<String> slicedSentences = new ArrayList<String>();
	List<String> slicedPrefixes = new ArrayList<String>();

	private void sliceSentence(String sentence) {
		slicedSentences = Arrays.asList(sentence.split(" "));
	}

	private void sliceWords() {
		for(String sentence : slicedSentences){
			int length = sentence.length();
			for(int i=1; i<=length; i++){
				slicedPrefixes.add(sentence.substring(0, i));
			}
		}
	}

	@Override
	public List<String> getPrefixs(String sentence) {
		sliceSentence(sentence);
		sliceWords();
		return slicedPrefixes;
	}

	@Override
	public List<String> getSlicedSentences(String sentence){
		sliceSentence(sentence);
		return slicedSentences;
	}

}
