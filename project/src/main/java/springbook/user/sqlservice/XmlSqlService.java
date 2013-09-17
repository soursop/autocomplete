package springbook.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import springbook.exception.SqlNotFoundException;
import springbook.exception.SqlRetrievalFailureException;
import springbook.sqlservice.jaxb.SqlType;
import springbook.sqlservice.jaxb.Sqlmap;
import springbook.user.dao.UserDao;

public class XmlSqlService implements SqlService, SqlReader {
	private SqlReader sqlReader;
	private SqlRegistry sqlRegistry;
	private String sqlmapFile;
	
	public void setSqlmapFile(String sqlmapFile) {
		this.sqlmapFile = sqlmapFile;
	}

	public void setSqlReader(SqlReader sqlReader) {
		this.sqlReader = sqlReader;
	}

	public void setSqlRegistry(SqlRegistry sqlRegistry) {
		this.sqlRegistry = sqlRegistry;
	}

	@Override
	public String getSql(String key) throws SqlRetrievalFailureException{
		try{
			return this.sqlRegistry.findSql(key);
		}catch(SqlNotFoundException e){
			throw new SqlRetrievalFailureException(e);
		}
	}

	@Override
	public void read(SqlRegistry sqlRegistry) {
		String contextPath = Sqlmap.class.getPackage().getName();
		try{
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
			Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
			for(SqlType sql : sqlmap.getSql()){
				sqlRegistry.registerSql(sql.getKey(), sql.getValue());
			}
		}catch(JAXBException e){
			throw new RuntimeException(e);
		}
	}
	
	@PostConstruct
	public void loadSql(){
		this.sqlReader.read(this.sqlRegistry);
	}
}
