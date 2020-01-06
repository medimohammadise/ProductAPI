package com.hse24.productapi.service.impl

import com.hse24.productapi.integration.restconsumer.CurrencyExchangeAPIClient
import com.hse24.productapi.integration.restconsumer.dto.ExchangeRateDTO
import com.hse24.productapi.service.CurrencyExchangeService
import com.hse24.productapi.service.dto.ExchangeRequestDTO
import com.hse24.productapi.service.enumeration.Currency
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.Date


@Service
class CurrencyConverterServiceImpl : CurrencyExchangeService {

    @Autowired
    private lateinit var currencyExchangeAPIClient: CurrencyExchangeAPIClient<ExchangeRateDTO>


    override fun getAllCurrencyRates(): ExchangeRateDTO? {
        return currencyExchangeAPIClient.getAllExchangeRates()
    }

    override fun getHistoricalConvertedValue(fromCurrency: Currency, toCurrency: Currency, valueToConvert: BigDecimal, date: Date): ExchangeRequestDTO {
        val exchangeRate = currencyExchangeAPIClient.getHistoricalValue(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency,
                date = date
        )

        return calculate(exchangeRate!!, fromCurrency, toCurrency, valueToConvert)

    }

    override suspend fun  getConvertedValue(fromCurrency: Currency, toCurrency: Currency,
                                   valueToConvert: BigDecimal): ExchangeRequestDTO {

        val exchangeRate = currencyExchangeAPIClient.getExchangeRate(fromCurrency, toCurrency)
        return if (exchangeRate?.success!!)
            calculate(exchangeRate, fromCurrency, toCurrency, valueToConvert)
        else
            ExchangeRequestDTO(
                    success = false,
                    valueToConvert = valueToConvert,
                    fromCurrency = fromCurrency,
                    toCurrency = toCurrency,
                    error = exchangeRate.error
            )
    }


    private fun calculate(exchangeRate: ExchangeRateDTO, fromCurrency: Currency,
                          toCurrency: Currency, valueToConvert: BigDecimal): ExchangeRequestDTO {
        return ExchangeRequestDTO(
                success = exchangeRate.success ?: false,
                historical = exchangeRate.historical ?: false,
                rateDate = exchangeRate.date!!,
                valueToConvert = valueToConvert,
                convertedValue = exchangeRate.rates!!.get(toCurrency.name)!!.times(valueToConvert),
                exchangeRate = exchangeRate.rates.get(toCurrency.name),
                fromCurrency = fromCurrency,
                toCurrency = toCurrency


        )
    }

}