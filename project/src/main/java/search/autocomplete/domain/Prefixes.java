package search.autocomplete.domain;

import java.util.List;

public interface Prefixes {
	void setPrefixes(List<String> prefixes);
	void setHashIndex(String hashIndex);
	List<String> getPrefixes();
	String getHashIndex(); 
}
