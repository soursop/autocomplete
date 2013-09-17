package springbook.user.service;

import java.util.List;

import springbook.exception.TestUserServiceException;
import springbook.user.domain.User;

public class TestUserService extends UserServiceImpl {
	
	private String id;

	public TestUserService(){
		this.id = "madnite1";
	}
	public TestUserService(String id){
		this.id = id;
	}
	
	public void upgradeLevel(User user){
		if(user.getId().equals(this.id)){
			throw new TestUserServiceException();
		}
		super.upgradeLevel(user);
	}

//	public List<User> getAll(){
//		for(User user : super.getAll()){
//			super.update(user);
//		}
//		return null;
//	}
	
}
