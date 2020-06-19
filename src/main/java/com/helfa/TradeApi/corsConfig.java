package com.helfa.TradeApi;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class corsConfig implements WebMvcConfigurer {
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins( "https://tradehome.herokuapp.com").allowedOrigins("http://localhost:4200")
        .allowedHeaders("*").exposedHeaders("Authorization,RefreshToken").allowedMethods("*");
    }
}
