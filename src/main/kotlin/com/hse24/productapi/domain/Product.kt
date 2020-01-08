package com.hse24.productapi.domain

import com.hse24.productapi.service.enumeration.Currency
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * A Product.
 */
@Entity
@Table(name = "product")
class Product(
        @Id
        @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid2")
        var id: UUID? = null,

        @get: NotNull
        @Column(name = "code", nullable = false, unique = true)
        var code: String? = null,

        @get: NotNull
        @Column(name = "description", nullable = false)
        var description: String? = null,

        @get: NotNull
        @Column(name = "price", precision = 21, scale = 2, nullable = false)
        var price: BigDecimal? = null,

        @get: NotNull
        @Enumerated(EnumType.STRING)
        @Column(name = "currency", nullable = false)
        var currency: Currency? = null,

        @get: NotNull
        @Column(name = "created_at", nullable = false)
        var createdAt: Instant? = null,

        @ManyToOne
        var productCategory: ProductCategory? = null


) : Serializable {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Product{" +
                "id=$id" +
                ", code='$code'" +
                ", description='$description'" +
                ", price=$price" +
                ", currency='$currency'" +
                ", createdAt='$createdAt'" +
                "}"
    }
}