package com.hse24.productapi.domain

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * A ProductCategory.
 */
@Entity
@Table(name = "product_category")
class ProductCategory(

        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        var id: UUID? = null,

        /**
         * The firstname attribute.
         */
        @get: NotNull
        @Column(name = "code", nullable = false)
        var code: String? = null,

        @get: NotNull
        @Column(name = "name", nullable = false)
        var name: String? = null,

        @get: NotNull
        @Column(name = "created_at", nullable = false)
        var createdAt: Instant? = null,

        @OneToMany(mappedBy = "productCategory")
        var products: MutableSet<Product> = mutableSetOf(),

        @ManyToOne
        var productCategory: ProductCategory? = null,

        /**
         * A relationship
         */
        @OneToMany(mappedBy = "productCategory")
        var productCategoryIds: MutableSet<ProductCategory> = mutableSetOf()

) : Serializable {


    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "ProductCategory{" +
                "id=$id" +
                ", code='$code'" +
                ", name='$name'" +
                ", createdAt='$createdAt'" +
                "}"
    }


}
