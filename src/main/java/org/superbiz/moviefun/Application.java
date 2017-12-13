package org.superbiz.moviefun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

<<<<<<< HEAD
/**
 * Created by aw169 on 12/12/17.
 */
@SpringBootApplication
public class Application {

    public static void main(String [] args){
=======
@SpringBootApplication
public class Application {

    public static void main(String... args) {
>>>>>>> temp
        SpringApplication.run(Application.class, args);
    }

    @Bean
<<<<<<< HEAD
    public ServletRegistrationBean servletRegistrationBean(ActionServlet actionServlet){
=======
    public ServletRegistrationBean actionServletRegistration(ActionServlet actionServlet) {
>>>>>>> temp
        return new ServletRegistrationBean(actionServlet, "/moviefun/*");
    }
}
