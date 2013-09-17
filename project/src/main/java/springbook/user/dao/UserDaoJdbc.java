package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao {
	private JdbcTemplate jdbcTemplate;
	private Map<String, String> sqlMap;

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private RowMapper<User> userMapper = new RowMapper<User>(){
		public User mapRow(ResultSet rs, int rowNum) throws SQLException{
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEmail(rs.getString("email"));
			return user;
		}
	};
	
	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#add(springbook.user.domain.User)
	 */
	@Override
	public void add(User user){
		this.jdbcTemplate.update(this.sqlMap.get("add"), user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
	}
	
	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#get(java.lang.String)
	 */
	@Override
	public User get(String id){
		return this.jdbcTemplate.queryForObject(this.sqlMap.get("get"), new Object[]{id}, this.userMapper);
	}
	
	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#getAll()
	 */
	@Override
	public List<User> getAll(){
		return this.jdbcTemplate.query(this.sqlMap.get("getAll"), this.userMapper);
	}
	
	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#deleteAll()
	 */
	@Override
	public void deleteAll(){
		this.jdbcTemplate.update(this.sqlMap.get("deleteAll"));
	}
	
	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#getCount()
	 */
	@Override
	public int getCount(){
		return this.jdbcTemplate.queryForInt(this.sqlMap.get("getCount"));
	}

	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#update(springbook.user.domain.User)
	 */
	@Override
	public void update(User user) {
		this.jdbcTemplate.update(this.sqlMap.get("update"), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
	}

}
