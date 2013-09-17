package springbook.user.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import springbook.exception.TestUserServiceException;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/dispatcherContext.xml")
@DirtiesContext
public class UserDaoTest {
	
	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserDao userDao;
	@Autowired
	DataSource dataSource;
	@Autowired
	UserService userService;
	
	List<User> users;
	private User user1;
	private User user2;
	private User user3;
	private User user4;
	private User user5;
	
	@Before
	public void setUp() {		
		dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/springbook", "root", "mysql", true);
		this.userDao.setDataSource(dataSource);
		
		user1 = new User();
		user1.setId("bumjin");
		user1.setName("박범진");
		user1.setPassword("springno1");
		user1.setLevel(Level.BASIC);
		user1.setLogin(MIN_LOGCOUNT_FOR_SILVER - 1);
		user1.setRecommend(0);
		user1.setEmail("soursop@ncsoft.com");
		
		user2 = new User();
		user2.setId("joytouch");
		user2.setName("강명성");
		user2.setPassword("springno2");
		user2.setLevel(Level.BASIC);
		user2.setLogin(MIN_LOGCOUNT_FOR_SILVER);
		user2.setRecommend(0);
		user2.setEmail("soursop@ncsoft.com");
		
		user3 = new User();
		user3.setId("erwins");
		user3.setName("신승호");
		user3.setPassword("springno3");
		user3.setLevel(Level.SILVER);
		user3.setLogin(60);
		user3.setRecommend(MIN_RECCOMEND_FOR_GOLD - 1);
		user3.setEmail("soursop@ncsoft.com");

		user4 = new User();
		user4.setId("madnite1");
		user4.setName("박범진");
		user4.setPassword("springno4");
		user4.setLevel(Level.SILVER);
		user4.setLogin(160);
		user4.setRecommend(MIN_RECCOMEND_FOR_GOLD);
		user4.setEmail("soursop@ncsoft.com");
		
		user5 = new User();
		user5.setId("green");
		user5.setName("오민규");
		user5.setPassword("springno5");
		user5.setLevel(Level.GOLD);
		user5.setLogin(100);
		user5.setRecommend(Integer.MAX_VALUE);
		user5.setEmail("soursop@ncsoft.com");
		
		users = new ArrayList();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		users.add(user5);
	}
	
	@Test
	public void bean(){
		assertThat(this.userService, is(notNullValue()));
	}
	
	@Test
	public void addAndGet() throws SQLException{
		
		userDao.deleteAll();
		
		User user = user1;
		
		userDao.add(user);
		
		User user2 = userDao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));

	}
	
	@Test
	public void dupliciateKey(){
		userDao.deleteAll();
		
		try{
			userDao.add(user1);
			userDao.add(user1);
		}catch(DuplicateKeyException ex){
			SQLException sqlEx = (SQLException) ex.getRootCause();
			SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
			
			assertThat(set.translate(null, null, sqlEx), is(DataAccessException.class));
		}
	}
	
	@Test
	public void update(){
		userDao.deleteAll();
		
		userDao.add(user1);
		userDao.add(user2);
		
		user1.setName("오민규");
		user1.setPassword("springno6");
		user1.setLevel(Level.GOLD);
		user1.setLogin(100);
		user1.setRecommend(999);
		
		userDao.update(user1);
		
		User user1update = userDao.get(user1.getId());
		checkSameUser(user1, user1update);
		User user2same = userDao.get(user2.getId());
		checkSameUser(user2, user2same);
		
	}

	private void checkSameUser(User user1, User user2) {
		
		assertThat(user1.getId(), is(user2.getId()));
		assertThat(user1.getName(), is(user2.getName()));
		assertThat(user1.getPassword(), is(user2.getPassword()));
		assertThat(user1.getLogin(), is(user2.getLogin()));
		assertThat(user1.getRecommend(), is(user2.getRecommend()));
		assertThat(user1.getLevel(), is(user2.getLevel()));

	}
 
}
