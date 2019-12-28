package com.hse24.productapi.integration.restconsumer

import com.hse24.productapi.config.AppConfiguration
import com.hse24.productapi.integration.restconsumer.dato.ExchangeRateDTO
import com.hse24.productapi.service.enumeration.Currency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.text.SimpleDateFormat
import java.util.Date


@Component
class FixerCurrencyExchangeAPIClientImpl : CurrencyExchangeAPIClient<ExchangeRateDTO> {

    @Autowired
    lateinit var appConfiguration: AppConfiguration

    @Autowired
    lateinit var restTemplate:RestTemplate

    override fun getAllExchangeRates(): ExchangeRateDTO? {
        return restTemplate.getForObject(appConfiguration.fixerApi.latestEndpoint, ExchangeRateDTO::class.java)
    }

    override fun getExchangeRate(fromCurrency: Currency, toCurrency: Currency): ExchangeRateDTO?{
        return restTemplate.getForObject(
                "${appConfiguration.fixerApi.latestEndpoint}&base=$fromCurrency&symbols=$fromCurrency,$toCurrency",
                ExchangeRateDTO::class.java)
    }
    override fun getHistoricalValue(fromCurrency: Currency, toCurrency: Currency, date: Date): ExchangeRateDTO? {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val historicalDate = sdf.format(date)

        val url = ("${appConfiguration.fixerApi.endpoint}/$historicalDate?access_key=${appConfiguration.fixerApi.apiKey}&base=$fromCurrency&symbols=$fromCurrency, $toCurrency")

        return restTemplate.getForObject(url, ExchangeRateDTO::class.java)
    }

}



