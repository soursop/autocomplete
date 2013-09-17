package search.autocomplete.service;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/dispatcherContext.xml")
public class AutoCompleteServiceTest {
	@Autowired
	AutoCompleteService service;
	
	@Test
	public void testGetAutocompleteSentence(){
		List<String> list = service.getAutocompleteSentence("ta");
		for(String val : list)
			System.out.println(val);
	}

}
