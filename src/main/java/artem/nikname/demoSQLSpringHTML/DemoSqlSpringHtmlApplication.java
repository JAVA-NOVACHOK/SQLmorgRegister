package artem.nikname.demoSQLSpringHTML;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@SpringBootApplication
public class DemoSqlSpringHtmlApplication {
    
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login-form");
    }
    
    public static void main(String[] args) {
        SpringApplication.run(DemoSqlSpringHtmlApplication.class, args);
    }
    
}
