package org.edunext;

import com.jetwinner.spring.SpringBootAppContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author xulixin
 */
@EnableCaching
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class CourseWorkApplication extends SpringBootServletInitializer {

    static Logger log = LoggerFactory.getLogger(CourseWorkApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(CourseWorkApplication.class);
            ConfigurableApplicationContext ctx = app.run(args);
            SpringBootAppContextHandler.me().setSpringBootAppPrimaryClass(CourseWorkApplication.class)
                    .setArgs(args).setApplicationContext(ctx);
        } catch (Exception e) {
            log.error("CourseWorkApplication startup error: ", e);
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        this.setRegisterErrorPageFilter(false);
        return application.sources(CourseWorkApplication.class);
    }

}