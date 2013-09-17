package search.autocomplete.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import search.autocomplete.component.Parsor;
import search.autocomplete.dao.PrefixDao;
import search.autocomplete.domain.Prefixes;

public class AutoCompleteServiceImpl implements AutoCompleteService {
	
	@Autowired
	PrefixDao prefixDao;
	@Autowired
	Prefixes prefixes;
	@Autowired
	Parsor parsor;
	

	@Override
	public void writeSentence(String sentence) {
		List<String> words = parsor.getSlicedSentences(sentence);
		
		String sentenceIndex = prefixDao.setSentence(sentence);
		
		prefixes.setHashIndex(sentenceIndex);
		prefixes.setPrefixes(words);		
		prefixDao.setPrefixes(prefixes);
		
		prefixDao.setInterstoredSenctence(words);
	}

	@Override
	public List<String> getAutocompleteSentence(String sentence) {
		List<String> words = parsor.getSlicedSentences(sentence);
		List<String> indexes = prefixDao.getInterstoredSenctenceIndexes(words);
		
		if(indexes.isEmpty()){
			prefixDao.setInterstoredSenctence(words);
			prefixDao.setCacheExpire(words);
			indexes = prefixDao.getInterstoredSenctenceIndexes(words);
		}
		
		return prefixDao.getInterstoredSenctences(indexes);
	}

}
