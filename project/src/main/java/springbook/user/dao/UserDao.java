package springbook.user.dao;

import java.util.List;

import javax.sql.DataSource;

import springbook.user.domain.User;

public interface UserDao {

	public abstract void add(User user);

	public abstract User get(String id);

	public abstract List<User> getAll();

	public abstract void deleteAll();

	public abstract int getCount();

	public abstract void update(User user);

	public abstract void setDataSource(DataSource dataSource);

}