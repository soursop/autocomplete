package hellogradle.config;


import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class HelloGradleWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext context) throws ServletException {
    	// root
    	XmlWebApplicationContext dispatcherContext = new XmlWebApplicationContext();
        dispatcherContext.setConfigLocation("classpath:dispatcherContext.xml");
        
        // Manages the life cycle of the root application context
        context.addListener(new ContextLoaderListener(dispatcherContext));
        
		// spring security
		FilterRegistration fr = context.addFilter("springSecurityFilterChain", new DelegatingFilterProxy("springSecurityFilterChain"));
		fr.addMappingForUrlPatterns(null, false, "/*");
		
		// encoding
		FilterRegistration charEncodingfilterReg = context.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
		charEncodingfilterReg.setInitParameter("encoding", "UTF-8");
		charEncodingfilterReg.setInitParameter("forceEncoding", "true");
		charEncodingfilterReg.addMappingForUrlPatterns(null, false, "/*");
		
        ServletRegistration.Dynamic dispatcher = context.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

}
