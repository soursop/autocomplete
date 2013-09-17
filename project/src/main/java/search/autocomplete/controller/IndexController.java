package search.autocomplete.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import search.autocomplete.service.AutoCompleteService;

@Controller
public class IndexController {
	@Autowired
	AutoCompleteService autoComService;
	
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String index() {
		
    	//List<String> str = autoComService.getAutocompleteSentence("ta sch");
		
        return "index";
    }
    
    @RequestMapping(value="/submit", method=RequestMethod.POST)
    public String getAutoComplete(Model model, @RequestParam("search") String search) {
    	autoComService.writeSentence(search);
    	List<String> list = new ArrayList<String>();
    	list.add(search);
    	model.addAttribute("list", list);
    	return "index";
    }
    
    @RequestMapping(value="/autocomplete", method=RequestMethod.POST)
    @ResponseBody
    public List<String> getSubmit(Model model, @RequestParam("search") String search) {
    	return autoComService.getAutocompleteSentence(search);
    }
    
//    @RequestMapping(value="/autocomplete", method=RequestMethod.GET)
//    @ResponseBody
//    public String getAutoComplete(Model model, @RequestParam("search") String search) {
//    	
//    	List<String> completeSentence = autoComService.getAutocompleteSentence(search);
//        return completeSentence.toString();
//    }


}
