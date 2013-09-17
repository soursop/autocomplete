package search.autocomplete.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.Jedis;
import search.autocomplete.domain.Prefixes;

public class PrefixDaoImpl implements PrefixDao {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	Jedis jedis;
	String password;
	

	public void setPassword(String password) {
		this.password = password;
	}

	@PostConstruct
	public void init(){
		jedis.auth(password);
	}
	
	private void increaseIndex(){					
		jedis.incr(DATA_LAST_INDEX_NAME);
	}
	
	private String getInterstoredCashName(List<String> searches){
		String name = DATA_CASH_NAME + ":" + StringUtils.join(searches, "|");
		logger.debug(name);
		return name;
	}
	
	@Override
	public String getSentenceIndex() {
		return jedis.get(DATA_LAST_INDEX_NAME);
	}

	@Override
	public String setSentence(String sentence) {
		increaseIndex();
		String nowIndex = getSentenceIndex();
		logger.debug(this.getClass().getPackage() + "." + this.getClass() + ": HSET " + DATA_NAME + " " + nowIndex + " " + sentence);
		jedis.hset(DATA_NAME, nowIndex, sentence);
		return nowIndex;
	}

	@Override
	public void setPrefixes(Prefixes prefixes) {
		List<String> prefixesList = prefixes.getPrefixes();
		String hashIndex = prefixes.getHashIndex();
		for(String prefix : prefixesList){
			jedis.zadd(DATA_INDEX_NAME + ":" + prefix, 1, hashIndex);
		}
	}

	@Override
	public void setInterstoredSenctence(List<String> searches) {		
		String[] indexedSearches = appendDataSearchIndex(searches);
			
		long outputCount = jedis.zinterstore(getInterstoredCashName(searches), indexedSearches);
		logger.debug("[" + getInterstoredCashName(searches) + "] Interstored Key Count : " + outputCount);
	}

	private String[] appendDataSearchIndex(List<String> searches) {
		int length = searches.size();
		String[] indexedSearches = new String[length];
		
		for(int i=0; i<length; i++)
			indexedSearches[i] = DATA_INDEX_NAME + ":" + searches.get(i);
		
		return indexedSearches;
	}

	@Override
	public void setCacheExpire(List<String> searches) {
		jedis.expire(getInterstoredCashName(searches), 60*10);
	}

	@Override
	public List<String> getInterstoredSenctenceIndexes(List<String> searches) {
		List<String> list = new ArrayList<String>();
		list.addAll(jedis.zrange(getInterstoredCashName(searches), 0, -1));
		return list;
	}

	@Override
	public List<String> getInterstoredSenctences(List<String> indexes) {
		if(indexes.isEmpty())
			return new ArrayList<String>();
		return jedis.hmget(DATA_NAME, indexes.toArray(new String[indexes.size()]));
	}


}
