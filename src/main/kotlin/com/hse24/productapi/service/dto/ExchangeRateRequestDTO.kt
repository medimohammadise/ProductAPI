package com.hse24.productapi.service.dto

import com.hse24.productapi.integration.restconsumer.dato.ExchangeRateErrorDTO
import com.hse24.productapi.service.enumeration.Currency
import java.math.BigDecimal
import java.util.Date

data class ExchangeRequestDTO(
        val success: Boolean=false,
        val historical:Boolean=false,
        val fromCurrency: Currency,

        val toCurrency: Currency,

        var exchangeRate: BigDecimal?=null,

        val valueToConvert: BigDecimal,

        val convertedValue: BigDecimal?=null,

        val rateDate: Date?=null,
        var error: ExchangeRateErrorDTO? = null
)

