package com.sensonation.config;

import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@Import({
        PropertyPlaceholderAutoConfiguration.class,
        ServerPropertiesAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.EmbeddedTomcat.class,
        DispatcherServletAutoConfiguration.class,
        WebMvcAutoConfiguration.class
})
public class WebConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter(){
        return new CorsFilter();
    }


}

