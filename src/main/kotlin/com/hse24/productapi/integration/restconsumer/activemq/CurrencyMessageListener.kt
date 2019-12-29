package com.hse24.productapi.integration.restconsumer.activemq

import com.google.gson.GsonBuilder
import com.hse24.productapi.config.AppConfiguration
import com.hse24.productapi.integration.restconsumer.dto.ExchangeRateDTO
import com.hse24.productapi.utils.GsonDateDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import java.util.Date
import javax.jms.Message
import javax.jms.TextMessage


@Component
class CurrencyMessageListener() {

    @Autowired
    lateinit var appConfiguration: AppConfiguration

    @JmsListener(destination = "#{appConfiguration.currencyActivemqTopic}")
    fun receiveMessageFromTopic(jsonMessage: Message): String? {
        var gsonBuilder=GsonBuilder().registerTypeAdapter(Date::class.java, GsonDateDeserializer())

        var messageData: String? = null
        println("Received message $jsonMessage")
        if (jsonMessage is TextMessage) {
            messageData = jsonMessage.text
            val exchangeRateDTO = gsonBuilder.create().fromJson(messageData, ExchangeRateDTO::class.java)
            println(exchangeRateDTO)
        }
        return messageData
    }


}