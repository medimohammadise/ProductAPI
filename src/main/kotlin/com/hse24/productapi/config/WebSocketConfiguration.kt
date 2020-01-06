package com.hse24.productapi.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import java.util.Optional

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    @Autowired
    lateinit var appConfiguration: AppConfiguration

    override fun registerStompEndpoints(registry: StompEndpointRegistry?) {
        val allowedOrigins =
                Optional.ofNullable(appConfiguration.cors.allowedOrigins)
                        .map { origins -> origins.toTypedArray() }
                        .orElse(arrayOfNulls(0))

        registry!!.addEndpoint("/websocket")

                .setAllowedOrigins(*allowedOrigins)


    }

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic")
        config.setApplicationDestinationPrefixes("/api")
    }


}