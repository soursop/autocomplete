package search.autocomplete.service;

import java.util.List;

public interface AutoCompleteService {
	void writeSentence(String sentence);
	List<String> getAutocompleteSentence(String sentence);
}
