package com.hse24.productapi.service.dto

import java.io.Serializable
import java.time.Instant
import java.util.UUID
import javax.validation.constraints.NotNull

data class ProductCategoryDTO(

        var id: UUID? = null,

        @get: NotNull
        var code: String? = null,

        @get: NotNull
        var name: String? = null,

        @get: NotNull
        var createdAt: Instant? = Instant.ofEpochMilli(0L),

        var productCategoryId: UUID? = null,

        var productCategoryIds: MutableSet<ProductCategoryDTO> = mutableSetOf()

) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProductCategoryDTO) return false
        if (other.id == null || id == null) return false
        return code.equals(other.code)
    }
}
