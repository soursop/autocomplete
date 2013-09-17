package springbook.user.service;

import java.lang.reflect.Proxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class TxProxyFactoryBean implements FactoryBean<Object> {
	Object target;
	PlatformTransactionManager transactionManager;
	String pattern;
	Class<?> serviceInterface;
	
	public void setTarget(Object target){
		this.target = target;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager){
		this.transactionManager = transactionManager;
	}
	
	public void setPattern(String pattern){
		this.pattern = pattern;
	}
	
	public void setServiceInterface(Class<?> serviceInterface){
		this.serviceInterface = serviceInterface;
	}

	@Override
	public Object getObject() {
		TransactionHandler txHandler = new TransactionHandler();
		txHandler.setTarget(target);
		txHandler.setTransactionManager(transactionManager);
		txHandler.setPattern(pattern);
		// getClass().getClassLoader() 클래스의 리소스를 반환해서 프록시에서 받아서 호출 가능하도록 해줌~!
		return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{ serviceInterface }, txHandler);
	}

	@Override
	public Class<?> getObjectType() {
		return serviceInterface;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
