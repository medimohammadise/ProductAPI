package com.hse24.productapi.repository

import com.hse24.productapi.domain.ProductCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductCategoryRepository: JpaRepository<ProductCategory, UUID>