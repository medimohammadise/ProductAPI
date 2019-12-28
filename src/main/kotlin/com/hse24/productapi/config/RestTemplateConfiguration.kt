package com.hse24.productapi.config

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class RestTemplateConfiguration {
    @Bean
    fun restTemplate() : RestTemplate {
        return RestTemplate()
    }

}
