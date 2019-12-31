package com.hse24.productapi.helper


import com.github.javafaker.Faker
import com.hse24.productapi.extensions.priceDecimal
import com.hse24.productapi.extensions.productCode
import com.hse24.productapi.extensions.productDescription
import com.hse24.productapi.service.dto.ProductCategoryDTO
import com.hse24.productapi.service.dto.ProductDTO
import com.hse24.productapi.service.enumeration.Currency
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.Random
import java.util.UUID

class FixtureCreator {
    companion object {
        const val DEFAULT_PRODUCT_CODE: String = "AAAAAAAAAA"
        const val UPDATED_PRODUCT_CODE = "BBBBBBBBBB"

        const val DEFAULT_DESCRIPTION: String = "AAAAAAAAAA"
        const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        val DEFAULT_PRICE: BigDecimal = BigDecimal(1.00)
        val UPDATED_PRICE: BigDecimal = BigDecimal(2.00)

        val DEFAULT_CURRENCY: Currency = Currency.EUR
        val UPDATED_CURRENCY: Currency = Currency.USD

        val DEFAULT_CREATED_AT: Instant = Instant.ofEpochMilli(0L)
        val UPDATED_CREATED_AT: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        const val DEFAULT_PRODUCT_CATEGORY_CODE: String = "CAT-00001"
        const val DEFAULT_PRODUCT_CATEGORY_NAME: String = "CATEGORY-1"
        private val SEED = Random(123456789)
        val faker = Faker(Locale.ENGLISH, SEED)
    }

    fun createProduct(
            id: UUID? = null,
            description: String? = faker.commerce()?.productDescription(),
            code: String? = faker.commerce().productCode(),
            price: BigDecimal? = faker.commerce().priceDecimal(),
            currency: Currency? = Currency.EUR,
            createdAt: Instant? = Instant.ofEpochMilli(0L),
            productCategoryId: UUID? = null
    ): ProductDTO {
        return ProductDTO(
                id = id,
                description = description ?: "",
                code = code ?: "",
                price = price ?: BigDecimal.ZERO,
                currency = currency,
                createdAt = createdAt,
                productCategoryId = productCategoryId)
    }

    fun createProductCategory(
            id: UUID? = null,
            code: String? = faker.commerce().productCode(),
            name: String? = faker.commerce()?.productDescription(),
            createdAt: Instant? = java.time.Instant.ofEpochMilli(0L)
    ): ProductCategoryDTO {
        return ProductCategoryDTO(
                id = id,
                name = name ?: "",
                code = code ?: "",
                createdAt = createdAt)
    }

}


