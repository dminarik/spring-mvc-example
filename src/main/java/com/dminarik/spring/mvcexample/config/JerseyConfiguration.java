package com.dminarik.spring.mvcexample.config;

import com.dminarik.spring.mvcexample.resource.UserResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class JerseyConfiguration extends ResourceConfig {

    @PostConstruct
    public void configure() {
        register(UserResource.class);
    }

}
