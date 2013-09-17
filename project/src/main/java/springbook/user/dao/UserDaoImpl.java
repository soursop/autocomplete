package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoImpl implements UserDao {
	private JdbcTemplate jdbcTemplate;
	
	public UserDaoImpl(){		
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
		this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend, email) values(?, ?, ?, ?, ?, ?, ?)", user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
	}
	
	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#get(java.lang.String)
	 */
	@Override
	public User get(String id){
		return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, this.userMapper);
	}
	
	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#getAll()
	 */
	@Override
	public List<User> getAll(){
		return this.jdbcTemplate.query("select * from users order by id", this.userMapper);
	}
	
	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#deleteAll()
	 */
	@Override
	public void deleteAll(){
		this.jdbcTemplate.update("delete from users");
	}
	
	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#getCount()
	 */
	@Override
	public int getCount(){
		return this.jdbcTemplate.queryForInt("select count(*) from users");
	}

	/* (non-Javadoc)
	 * @see spirngbook.user.dao.UserDaoI#update(springbook.user.domain.User)
	 */
	@Override
	public void update(User user) {
		this.jdbcTemplate.update("update users set name=?, password=?, level=?, login=?, recommend=?, email=? where id=?", user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId());
	}
	
}
