package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class DaoFactory {
	
	@Bean
	public UserDao userDao(){
		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setDataSource(dataSource());
		return userDao;
	}
	
/*	public MessageDao messageDao(){
		ConnectionMaker connectionMaker = new DConnectionMaker();
		MessageDao messageDao = new MessageDao(connectionMaker);
		return messageDao;
	}	

	public AccountDao accountDao(){
		ConnectionMaker connectionMaker = new DConnectionMaker();
		AccountDao accountDao = new AccountDao(connectionMaker);
		return accountDao;
	}*/
	
	@Bean
	public DataSource dataSource(){
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUsername("spring");
		dataSource.setPassword("book");
		
		return dataSource;
	}
}
