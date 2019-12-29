package com.hse24.productapi.config

import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import javax.jms.ConnectionFactory


@Configuration
class ActiveMQConfiguration {

    @Bean
    fun connectionFactory(): ActiveMQConnectionFactory {
        return ActiveMQConnectionFactory()
    }

    @Bean
    fun topicListenerFactory(connectionFactory: ConnectionFactory,
                             configurer: DefaultJmsListenerContainerFactoryConfigurer): JmsListenerContainerFactory<*> {
        val factory = DefaultJmsListenerContainerFactory()
        configurer.configure(factory, connectionFactory)
        factory.setPubSubDomain(true)
        return factory
    }

    @Bean
    fun jmsTemplate(): JmsTemplate {
        val template = JmsTemplate()
        template.connectionFactory = connectionFactory()
        template.messageConverter = jacksonJmsMessageConverter()
        return template
    }

    @Bean // Serialize message content to json using TextMessage
    fun jacksonJmsMessageConverter(): MessageConverter {
        val converter = MappingJackson2MessageConverter()
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        return converter
    }


}