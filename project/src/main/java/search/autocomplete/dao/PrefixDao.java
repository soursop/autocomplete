package search.autocomplete.dao;

import java.util.List;
import java.util.Set;

import search.autocomplete.domain.Prefixes;

public interface PrefixDao {
	static final String DATA_LAST_INDEX_NAME = "search-data-last-index";
	static final String DATA_NAME = "search-data";
	static final String DATA_INDEX_NAME = "search-index";
	static final String DATA_CASH_NAME = "search-cache";
	
	String getSentenceIndex();
	String setSentence(String sentence);		// return sentence sentence index that stored in hash
	void setPrefixes(Prefixes prefixes);
	void setInterstoredSenctence(List<String> searches);
	List<String> getInterstoredSenctenceIndexes(List<String> searches);
	List<String> getInterstoredSenctences(List<String> indexes);
	void setCacheExpire(List<String> searches);
}
