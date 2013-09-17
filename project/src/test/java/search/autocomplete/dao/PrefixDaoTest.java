package search.autocomplete.dao;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import search.autocomplete.component.Parsor;
import search.autocomplete.domain.Prefixes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/dispatcherContext.xml")
public class PrefixDaoTest {
	@Autowired
	PrefixDao prefixDao;
	@Autowired
	Parsor parsor;
	@Autowired
	Prefixes prefixes;

	Jedis redis;
	List<String> sentences;
	String test1 = "Take out the trash";
	String test2 = "Talk to the school bus driver";
	
	@Before
	public void setup(){

		redis = new Jedis("localhost", 7379);
		redis.auth("master");
		
		sentences = new ArrayList();
//		redis.flushDB();
		
	}
	
	private void setPrefixes(){
		
		
		prefixes.setPrefixes(parsor.getPrefixs(test1));
		prefixes.setHashIndex("1");
		prefixDao.setPrefixes(prefixes);
		
		
		prefixes.setPrefixes(parsor.getPrefixs(test2));
		prefixes.setHashIndex("2");
		prefixDao.setPrefixes(prefixes);
		
	}
	
	@Test
	public void testSetSentence(){
		
		setSentence();
		
		assertThat(redis.hget(PrefixDao.DATA_NAME, "1"), is(test1));
		assertThat(redis.hget(PrefixDao.DATA_NAME, "2"), is(test2));
		
	}
	
	private void setSentence() {
		
		prefixDao.setSentence(test1);
		prefixDao.setSentence(test2);
		
	}

	@Test
	public void testSetPrefixes(){
		
		setPrefixes();
		
		Set<String> set = redis.keys(PrefixDao.DATA_INDEX_NAME + ":*");
		
		assertThat(set.size(), is(31));
		
	}
	
	@Test
	public void testSetInterstoredSenctence(){
		
		sentences.add("ta");
		sentences.add("bus");
		
		prefixDao.setInterstoredSenctence(sentences);
		Set<String> set = redis.keys(PrefixDao.DATA_CASH_NAME + ":*");
		
	}
	
	@Test
	public void testGetInterstoredSenctenceIndexes(){
		sentences.add("ta");
		sentences.add("bus");
		
		prefixDao.setInterstoredSenctence(sentences);
		List<String> list  = prefixDao.getInterstoredSenctenceIndexes(sentences);
		System.out.println(list);
		
	}
	
	@Test
	public void testGetInterstoredSenctences(){
		
		sentences.add("ta");
		sentences.add("bus");

		prefixDao.setInterstoredSenctence(sentences);
		List<String> indexes = prefixDao.getInterstoredSenctenceIndexes(sentences);
		List<String> list = prefixDao.getInterstoredSenctences(indexes);
		System.out.println(list);
		
	}

}
