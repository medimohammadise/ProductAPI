package com.hse24.productapi.web.rest

import com.hse24.productapi.config.AppConfiguration
import com.hse24.productapi.service.CurrencyExchangeService
import com.hse24.productapi.service.enumeration.Currency
import kotlinx.coroutines.runBlocking
import org.apache.activemq.command.ActiveMQTopic
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api")
class CurrencyExchangeController(
        private val currencyExchangeService: CurrencyExchangeService,
        private val jmsTemplate: JmsTemplate,
        private val appConfiguration: AppConfiguration
){
    private val log = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/currency-exchange/{fromCurrency}/{toCurrency}/{value}")
    fun requestExchangeRate(@PathVariable fromCurrency: Currency,@PathVariable toCurrency: Currency,@PathVariable value:BigDecimal): ResponseEntity<String> {
        log.debug("REST request to get Exchange rate : {}", fromCurrency,toCurrency,value)

        runBlocking{
            val exchangeRequestDTO= currencyExchangeService.getConvertedValue(fromCurrency =fromCurrency,toCurrency = toCurrency,valueToConvert = value )
            jmsTemplate.convertAndSend(ActiveMQTopic(appConfiguration.currencyActivemqTopic),exchangeRequestDTO)

        }
        return ResponseEntity.accepted().build()

    }
}