package com.hse24.productapi.integration.restconsumer.activemq

import com.google.gson.GsonBuilder
import com.hse24.productapi.config.AppConfiguration
import com.hse24.productapi.service.dto.ExchangeRequestDTO
import com.hse24.productapi.utils.GsonDateDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.annotation.JmsListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import java.util.Date
import javax.jms.Message
import javax.jms.TextMessage


@Component
class CurrencyMessageListener() {
    @Autowired
    lateinit var simpMessagingTemplate: SimpMessagingTemplate

    @Autowired
    lateinit var appConfiguration: AppConfiguration
    @JmsListener(destination = "#{appConfiguration.currencyActivemqTopic}")
    fun receiveMessageFromTopic(jsonMessage: Message) {
        var gsonBuilder=GsonBuilder().registerTypeAdapter(Date::class.java, GsonDateDeserializer())

        var messageData: String?
        println("Received message $jsonMessage")
        if (jsonMessage is TextMessage) {
            messageData = jsonMessage.text
            simpMessagingTemplate.convertAndSend("/topic/exchange",  gsonBuilder.create().fromJson(messageData, ExchangeRequestDTO::class.java))
        }

    }


}