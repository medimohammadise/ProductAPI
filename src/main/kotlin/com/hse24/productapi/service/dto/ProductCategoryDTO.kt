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
        var createdAt: Instant? = null,

        var productCategoryId: UUID? = null

) : Serializable
