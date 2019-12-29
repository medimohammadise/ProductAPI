package com.hse24.productapi.service

import com.hse24.productapi.integration.restconsumer.dto.ExchangeRateDTO
import com.hse24.productapi.service.dto.ExchangeRequestDTO
import com.hse24.productapi.service.enumeration.Currency
import java.math.BigDecimal
import java.util.Date

interface CurrencyExchangeService {

    fun getAllCurrencyRates(): ExchangeRateDTO?

    fun getConvertedValue(fromCurrency: Currency, toCurrency: Currency,
                          valueToConvert: BigDecimal): ExchangeRequestDTO
    fun getHistoricalConvertedValue(fromCurrency: Currency, toCurrency: Currency, valueToConvert: BigDecimal, date: Date): ExchangeRequestDTO
}