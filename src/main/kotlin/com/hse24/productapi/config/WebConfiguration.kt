package com.hse24.productapi.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class WebConfiguration(
        private val appConfiguration:AppConfiguration
){
    private val log = LoggerFactory.getLogger(WebConfiguration::class.java)
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = appConfiguration.cors
        if (config.allowedOrigins != null && !config.allowedOrigins!!.isEmpty()) {
            log.debug("Registering CORS filter")
            source.registerCorsConfiguration("/api/**", config)
            source.registerCorsConfiguration("/management/**", config)
            source.registerCorsConfiguration("/v2/api-docs", config)
        }
        return CorsFilter(source)
    }
}