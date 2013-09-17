package search.autocomplete.aop;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import search.autocomplete.domain.Prefixes;

public class CharacterLowerAdvice {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Around("setPrefixes(prefixes)")
	public Object toLowerCase(ProceedingJoinPoint method, Prefixes prefixes) throws Throwable {

		List<String> list = prefixes.getPrefixes();
		int length = list.size();
		for(int i=0; i<length; i++){
			list.set(i, list.get(i).toLowerCase());
		}
		prefixes.setPrefixes(list);
		logger.debug(list.toString());
		  
		Object returnValue = method.proceed(new Object[] {prefixes});
		
		return returnValue;
		
	}

}
