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
import java.util.Locale
import java.util.Random
import java.util.UUID

class FixtureCreator {
    companion object {
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


