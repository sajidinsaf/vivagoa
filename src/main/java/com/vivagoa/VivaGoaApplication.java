package com.vivagoa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = "file:/ifaru02/vivagoa/vivagoa.properties", ignoreResourceNotFound = true)
public class VivaGoaApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VivaGoaApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(VivaGoaApplication.class, args);
    }
}
