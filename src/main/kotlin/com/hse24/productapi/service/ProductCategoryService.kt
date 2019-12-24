package com.hse24.productapi.service

import com.hse24.productapi.service.dto.ProductCategoryDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface ProductCategoryService {
    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(productCategoryDTO: ProductCategoryDTO): ProductCategoryDTO

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<ProductCategoryDTO>

    /**
     * Get the "id" product.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: UUID): Optional<ProductCategoryDTO>

    /**
     * Delete the "id" product.
     *
     * @param id the id of the entity.
     */
    fun delete(id: UUID)

    /**
     * Search for the product corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun search(query: String, pageable: Pageable): Page<ProductCategoryDTO>
}

