package com.volavis.dbanonym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })      // Without exclude: Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
public class Application extends SpringBootServletInitializer {

    /**
     * Main method.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }
}