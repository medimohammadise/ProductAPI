package com.hse24.productapi.service.dto

import com.hse24.productapi.service.enumeration.Currency
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

//make every thing nullable because of mapstruct
data class ProductDTO(
        var id: UUID? = null,

        var code: String? = null,

        var description: String? = null,

        var price: BigDecimal? = null,

        var currency: Currency? = null,

        var createdAt: Instant? = Instant.ofEpochMilli(0L),

        var productCategoryId: UUID? = null

) : Serializable {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProductDTO) return false
        if (other.id == null || id == null) return false
        return code.equals(other.code)
    }
}
