package com.hse24.productapi.service.mapper

import com.hse24.productapi.domain.Product
import com.hse24.productapi.service.dto.ProductDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.util.UUID

/**
 * Mapper for the entity [Product] and its DTO [ProductDTO].
 */
@Mapper(componentModel = "spring", uses = [ProductCategoryMapper::class])
interface ProductMapper : EntityMapper<ProductDTO, Product> {
    @Mappings(
            Mapping(source = "productCategory.id", target = "productCategoryId")
    )
    override fun toDto(entity: Product): ProductDTO

    @Mappings(
            Mapping(source = "productCategoryId", target = "productCategory.id")
    )
    override fun toEntity(dto: ProductDTO): Product

    fun fromId(id: UUID?): Product? {
        if (id == null) {
            return null
        }
        val product = Product()
        product.id = id
        return product
    }

}
