package springbook.user.service;

import java.util.List;

import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;


public interface UserService {
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	void add(User user);
	List<User> getAll();
	void deleteAll();
	void update(User user);
	
	void upgradeLevels();
	void setUserDao(UserDao userDao);
	void setMailSender(MailSender mailSender);
}
