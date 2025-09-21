package com.log430.brockerx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebConfig {

    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/"); // dossier où tu mets tes JSP
        resolver.setSuffix(".jsp");          // extension des fichiers
        return resolver;
    }
}
