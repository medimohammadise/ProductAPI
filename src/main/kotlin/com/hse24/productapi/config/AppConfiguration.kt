package com.hse24.productapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration

@Configuration
@ConfigurationProperties("app")
class AppConfiguration{
     lateinit var  applicationName:String
     var cors:CorsConfiguration=CorsConfiguration()
     var fixerApi=FixerApi()
     lateinit var currencyActivemqTopic:String
}

class FixerApi {
     lateinit var apiKey: String
     lateinit var endpoint: String
     lateinit var latestEndpoint: String
}