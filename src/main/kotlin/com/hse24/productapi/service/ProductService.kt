package com.hse24.productapi.service

import com.hse24.productapi.service.dto.ProductDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface ProductService {
    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(productDTO: ProductDTO): ProductDTO

    /**
     * Get all the products.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<ProductDTO>

    /**
     * Get the "id" product.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: UUID): Optional<ProductDTO>

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
    fun search(query: String, pageable: Pageable): Page<ProductDTO>
}

