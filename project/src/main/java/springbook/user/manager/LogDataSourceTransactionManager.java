package springbook.user.manager;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

@SuppressWarnings("serial")
public class LogDataSourceTransactionManager extends
		DataSourceTransactionManager {
	
	public void doBegin(Object transaction, TransactionDefinition definition){
		super.doBegin(transaction, definition);
		System.out.println("doBegin : definition - name:" + definition.getName() + " isReadOnly:" +definition.isReadOnly());
	}
	
	public void doCommit(DefaultTransactionStatus status){
		super.doCommit(status);
		System.out.println("do commit!!!!!!!!!!");
	}
	
	public void doRollback(DefaultTransactionStatus status){
		super.doRollback(status);
		System.out.println("do rollback!!!!!!!!!!");
	}
	
}
