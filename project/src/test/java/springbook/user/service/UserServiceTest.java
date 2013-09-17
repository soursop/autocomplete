package springbook.user.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import static org.mockito.Mockito.*;
import springbook.exception.TestUserServiceException;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECCOMEND_FOR_GOLD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/dispatcherContext.xml")
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@DirtiesContext
public class UserServiceTest {
	@Autowired
	private ApplicationContext context;
	@Autowired
	private UserDao userDao;
	@Autowired
	DataSource dataSource;
	@Autowired
	UserService userService;
	@Resource(name="testUserService")
	UserService testUserService;
	@Autowired
	MailSender mailSender;
	
	public static List<User> users;
	private User user1;
	private User user2;
	private User user3;
	private User user4;
	private User user5;
	
	@Autowired
	PlatformTransactionManager transactionManager;
	
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
		user1.setEmail("soursop0@ncsoft.com");
		
		user2 = new User();
		user2.setId("joytouch");
		user2.setName("강명성");
		user2.setPassword("springno2");
		user2.setLevel(Level.BASIC);
		user2.setLogin(MIN_LOGCOUNT_FOR_SILVER);
		user2.setRecommend(0);
		user2.setEmail("soursop1@ncsoft.com");
		
		user3 = new User();
		user3.setId("erwins");
		user3.setName("신승호");
		user3.setPassword("springno3");
		user3.setLevel(Level.SILVER);
		user3.setLogin(60);
		user3.setRecommend(MIN_RECCOMEND_FOR_GOLD - 1);
		user3.setEmail("soursop2@ncsoft.com");

		user4 = new User();
		user4.setId("madnite1");
		user4.setName("박범진");
		user4.setPassword("springno4");
		user4.setLevel(Level.SILVER);
		user4.setLogin(160);
		user4.setRecommend(MIN_RECCOMEND_FOR_GOLD);
		user4.setEmail("soursop3@ncsoft.com");
		
		user5 = new User();
		user5.setId("green");
		user5.setName("오민규");
		user5.setPassword("springno5");
		user5.setLevel(Level.GOLD);
		user5.setLogin(100);
		user5.setRecommend(Integer.MAX_VALUE);
		user5.setEmail("soursop4@ncsoft.com");
		
		users = new ArrayList();
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		users.add(user5);
	}
	
	@Test
	public void add(){
		
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);

		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
		
	}
	
	@Test
	public void upgradeLevels(){
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
		
		List<String> request = mockMailSender.getRequests();		
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
		
	}

	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
	}

	private void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = userDao.get(user.getId());
		if(upgraded){
			 assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}else{
		 assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	@Test
	public void upgradeAllOrNothing(){
		
		userDao.deleteAll();
		for(User user : users)
			userDao.add(user);
		
		try{
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}catch(TestUserServiceException e){
		}
		
		checkLevelUpgraded(users.get(1), false);
	}
	
//	@Test
//	@DirtiesContext
//	public void upgradeAllOrNothing(){
//		TestUserService testUserService = new TestUserService(users.get(3).getId());	// 예외 던질 id 입력
//		testUserService.setUserDao(this.userDao);
//		testUserService.setMailSender(mailSender);
//		
//		ProxyFactoryBean txProxyFactoryBean = context.getBean("&userServiceSFactory", ProxyFactoryBean.class);
//		txProxyFactoryBean.setTarget(testUserService);
//		UserService txUserService = (UserService) txProxyFactoryBean.getObject();
//		
//		userDao.deleteAll();
//		for(User user : users)
//			userDao.add(user);
//		
//		try{
//			txUserService.upgradeLevels();
//			fail("TestUserServiceException expected");
//		}catch(TestUserServiceException e){
//		}
//		
//		checkLevelUpgraded(users.get(1), false);
//	}

	@Test
	public void mockUpgradeLevels() throws Exception{
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		UserDao mockUserDao = mock(UserDao.class);
		when(mockUserDao.getAll()).thenReturn(this.users);
		userServiceImpl.setUserDao(mockUserDao);
		
		MailSender mockMailSender = mock(MailSender.class);
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao, times(2)).update(any(User.class));
		verify(mockUserDao).update(users.get(1));
		assertThat(users.get(1).getLevel(), is(Level.SILVER));
		verify(mockUserDao).update(users.get(3));
		assertThat(users.get(3).getLevel(), is(Level.GOLD));
		
		ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mockMailSender, times(2)).send(mailMessageArg.capture());
		List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
		assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
		assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
		
	}
	
	static class TestUserServiceImpl extends UserServiceImpl {
		private String id = "madnite1";
		
		protected void upgradeLevel(User user){
			if(user.getId().equals(this.id)){
				throw new TestUserServiceException();
			}
			super.upgradeLevel(user);
		}
	}
	
	@Test
	public void advisorAutoPrxyCreator(){
		//assertThat(testUserService, is(java.lang.reflect.Proxy.class));
	}
	
	@Test
	public void readOnlyAttribue(){
		testUserService.getAll();
	}
	
}
