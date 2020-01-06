package com.hse24.productapi.service

import com.hse24.productapi.config.AppConfiguration
import com.hse24.productapi.extensions.getTheDayBefore
import com.hse24.productapi.integration.restconsumer.dto.ExchangeRateDTO
import com.hse24.productapi.service.dto.ExchangeRequestDTO
import com.hse24.productapi.service.enumeration.Currency
import kotlinx.coroutines.runBlocking
import org.apache.activemq.command.ActiveMQTopic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jms.core.JmsTemplate
import java.math.BigDecimal
import java.time.Instant
import java.util.Date
import javax.transaction.Transactional


@ExtendWith
@SpringBootTest
@Transactional
class CurrencyConverterServiceTest {


    @Autowired
    private lateinit var currencyExchangeService: CurrencyExchangeService

    @Autowired
    private lateinit var jmsTemplate: JmsTemplate

    @Autowired
    lateinit var appConfiguration: AppConfiguration

    @Nested
    inner class SuccessfulFixerAPIClass {
        @Test
        fun `test get all rates`() {
            val exchangeRatesDTO = currencyExchangeService.getAllCurrencyRates()
            assertThat(exchangeRatesDTO!!.success).isTrue()
            assertThat(exchangeRatesDTO.rates).isNotEmpty
            assertThat(exchangeRatesDTO.rates!!.keys).contains("EUR", "USD", "AUD", "CAD")
        }

        @Test
        fun `test convert rate from one symbol to another`() {
            lateinit var exchangeRateRequestDTO:ExchangeRequestDTO
            runBlocking {
                 exchangeRateRequestDTO = currencyExchangeService.getConvertedValue(Currency.EUR, Currency.USD, BigDecimal(1))
            }

            assertThat(exchangeRateRequestDTO.success).isTrue()
            assertThat(exchangeRateRequestDTO.convertedValue).isNotNull()
        }

        @Test
        fun `test rate history for currency sambols`() {
            val exchangeRateRequestDTO = currencyExchangeService.getHistoricalConvertedValue(Currency.EUR, Currency.USD, BigDecimal(1),
                    Date().getTheDayBefore(2)
            )
            assertThat(exchangeRateRequestDTO.success).isTrue()
            assertThat(exchangeRateRequestDTO.historical).isTrue()
            assertThat(exchangeRateRequestDTO.convertedValue).isNotNull()
        }

    }

    @Nested
    inner class UnsuccessfulFixerAPIClass {
        @Test
        fun `test service returns proper error when invalid symbol is passed`() {
            lateinit var exchangeRateRequestDTO:ExchangeRequestDTO
            runBlocking {
                exchangeRateRequestDTO = currencyExchangeService.getConvertedValue(Currency.INVALID_CURRENCY, Currency.INVALID_CURRENCY, BigDecimal(1))
            }
            assertThat(exchangeRateRequestDTO.success).isFalse()
            assertThat(exchangeRateRequestDTO.error).isNotNull()
        }

    }


    @Nested
    inner class SuccessfulSubscriptionCurrencyTopic {
        @Test
        fun `test subscribing to receive currency from fixer api`() {
            val exchangeRateDTO = ExchangeRateDTO(success = true,
                    historical = false,
                    timestamp = Date.from(Instant.now()))
            jmsTemplate.convertAndSend(ActiveMQTopic(appConfiguration.currencyActivemqTopic), exchangeRateDTO)
        }

    }
}
