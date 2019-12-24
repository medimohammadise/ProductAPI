package com.hse24.productapi.repository

import com.hse24.productapi.domain.ProductCategory
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductCategoryRepository:CrudRepository<ProductCategory, UUID>