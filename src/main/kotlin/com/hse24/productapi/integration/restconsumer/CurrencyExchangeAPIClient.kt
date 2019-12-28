package com.hse24.productapi.integration.restconsumer

import com.hse24.productapi.service.enumeration.Currency
import java.util.Date

interface CurrencyExchangeAPIClient<T> {
    fun getAllExchangeRates(): T?
    fun getExchangeRate(fromCurrency: Currency, toCurrency: Currency): T?
    fun getHistoricalValue(fromCurrency: Currency, toCurrency: Currency, date: Date): T?
}