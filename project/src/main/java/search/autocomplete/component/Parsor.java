package search.autocomplete.component;

import java.util.List;

public interface Parsor {
	List<String> getPrefixs(String sentence);
	List<String> getSlicedSentences(String sentence);
}
