package com.hse24.productapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration

@Configuration
@ConfigurationProperties("app")
class AppConfiguration{
     var cors:CorsConfiguration=CorsConfiguration()
}