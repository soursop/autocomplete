package search.autocomplete.domain;

import java.util.List;

public class PrefixesImpl implements Prefixes {
	List<String> prefixes;
	String hashIndex;

	@Override
	public void setPrefixes(List<String> prefixes) {
		this.prefixes = prefixes;
	}

	@Override
	public void setHashIndex(String hashIndex) {
		this.hashIndex = hashIndex;
	}

	@Override
	public List<String> getPrefixes() {
		return this.prefixes;
	}

	@Override
	public String getHashIndex() {
		return this.hashIndex;
	}

}
