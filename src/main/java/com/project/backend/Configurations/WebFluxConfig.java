package com.project.backend.Configurations;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;





@Configuration
@EnableWebFlux // cors쓰기위해서 
public class WebFluxConfig implements WebFluxConfigurer{
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모두 땡겨와라 
                .allowedMethods("*");
    }
}