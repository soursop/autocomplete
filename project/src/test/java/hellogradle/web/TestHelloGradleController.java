package hellogradle.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class TestHelloGradleController {
	static final int REMOVE_SCORE = 1;
	Map<String, String> map = new HashMap<String, String>();
	List<String> list = new ArrayList<String>();
	
	@Before
	public void init(){
		map.put("foo", "bar");
		map.put("amy", "harry");
		for(int i=0; i<1 ; i++){
			list.add("apache");
			list.add("b");
			list.add("banana");
			list.add("charry");
			list.add("black");
			list.add("a");
			list.add("arr");
			list.add("ab");
			list.add("abb");
			list.add("abbc");
			list.add("abbd");
			list.add("affb");
			list.add("abbdsf");
			list.add("chr");
		}
	}

	@Test
	public void testJadis(){
		Jedis jedis = new Jedis("localhost");
		for (Object key : map.keySet()) {
			System.out.println("Key : " + key.toString() + " Value : "
				+ map.get(key));
			
			jedis.set(key.toString(), map.get(key));
			String get = jedis.get(key.toString());
			assertThat(map.get(key), is(get));
			
		}
	}

	@Test
	public void testJadisMasterSlave(){
		Jedis master = new Jedis("localhost", 7379);
		master.auth("master");
		Jedis slave = new Jedis("localhost", 8379);
		slave.auth("slave");
		for (Object key : map.keySet()) {
			System.out.println("Key : " + key.toString() + " Value : "
				+ map.get(key));
			
			master.set(key.toString(), map.get(key));
			String get = slave.get(key.toString());
			assertThat(map.get(key), is(get));
			
		}
		
	}
	
	@Test
	public void testOrder(){
		Jedis master = new Jedis("localhost", 7379);
		master.auth("master");
		master.flushDB();
		for(String val : list){
			//System.out.println(" Value : " + val);
			master.zincrby("myset", 1, val);
		}
		for(int i=0; i<5; i++){
			master.zincrby("myset", 1, "affb");
		}
		for(int i=0; i<3; i++){
			master.zincrby("myset", 1, "abb");
		}
		
		Set<String> set = master.zrange("myset", 0, -1);
		for(String val : set)
			System.out.println(" Value : " + val);
		
		// remove low score word
		//System.out.println(master.zremrangeByScore("myset", 0, 1));
		
		long idx = master.zrank("myset", "ab");
		System.out.println(idx);
		
		Set<Tuple> set1 = master.zrangeWithScores("myset", idx, 5);
		for(Tuple val : set1)
			System.out.println(" element : " + val.getElement() + " score : " + val.getScore() );
		
	}

}
