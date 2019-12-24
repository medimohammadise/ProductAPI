package com.hse24.productapi.service.mapper



import com.hse24.productapi.domain.ProductCategory
import com.hse24.productapi.service.dto.ProductCategoryDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.util.UUID

/**
 * Mapper for the entity [ProductCategory] and its DTO [ProductCategoryDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface ProductCategoryMapper : EntityMapper<ProductCategoryDTO, ProductCategory> {
    @Mappings(
        Mapping(source = "productCategory.id", target = "productCategoryId")
    )
    override fun toDto(entity: ProductCategory): ProductCategoryDTO
    @Mappings(
        Mapping(target = "products", ignore = true),
        Mapping(source = "productCategoryId", target = "id"),
        Mapping(target = "productCategoryIds", ignore = true)
    )
    override fun toEntity(dto: ProductCategoryDTO): ProductCategory
    fun fromId(id: UUID?): ProductCategory? {
        if (id == null) {
            return null
        }
        val productCategory = ProductCategory()
        productCategory.id = id
        return productCategory
    }
}
