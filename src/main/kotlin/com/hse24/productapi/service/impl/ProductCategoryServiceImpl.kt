package com.hse24.productapi.service.impl


import com.hse24.productapi.repository.ProductCategoryRepository
import com.hse24.productapi.service.ProductCategoryService
import com.hse24.productapi.service.dto.ProductCategoryDTO
import com.hse24.productapi.service.mapper.ProductCategoryMapper
import org.slf4j.LoggerFactory

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.Optional
import java.util.UUID


/**
 * Service Implementation for managing [ProductCategory].
 */
@Service
@Transactional
class ProductCategoryServiceImpl(
        val productCategoryRepository: ProductCategoryRepository,
        val productCategoryMapper: ProductCategoryMapper
) : ProductCategoryService {
    override fun search(query: String, pageable: Pageable): Page<ProductCategoryDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val log = LoggerFactory.getLogger(ProductCategoryServiceImpl::class.java)

    /**
     * Save a productCategory.
     *
     * @param productCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(productCategoryDTO: ProductCategoryDTO): ProductCategoryDTO {
        log.debug("Request to save ProductCategory : {}", productCategoryDTO)

        var productCategory = productCategoryMapper.toEntity(productCategoryDTO)
        productCategory = productCategoryRepository.save(productCategory)
        return  productCategoryMapper.toDto(productCategory)
    }

    /**
     * Get all the productCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<ProductCategoryDTO> {
        log.debug("Request to get all ProductCategories")
        return productCategoryRepository.findAll(pageable)
            .map(productCategoryMapper::toDto)
    }

    /**
     * Get one productCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    override fun findOne(id: UUID): Optional<ProductCategoryDTO> {
        log.debug("Request to get ProductCategory : {}", id)
        return productCategoryRepository.findById(id)
            .map(productCategoryMapper::toDto)
    }

    /**
     * Delete the productCategory by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: UUID) {
        log.debug("Request to delete ProductCategory : {}", id)

        productCategoryRepository.deleteById(id)
    }
}
