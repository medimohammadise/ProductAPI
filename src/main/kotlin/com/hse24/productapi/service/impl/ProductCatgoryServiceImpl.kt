package com.hse24.productapi.service.impl


import com.hse24.productapi.repository.ProductCategoryRepository
import com.hse24.productapi.service.ProductCategoryService
import com.hse24.productapi.service.dto.ProductCategoryDTO
import com.hse24.productapi.service.mapper.ProductCategoryMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID
import javax.transaction.Transactional

@Service
@Transactional
class ProductCatgoryServiceImpl
(val productCategoryRepository: ProductCategoryRepository,
 val productCategoryMapper: ProductCategoryMapper
) : ProductCategoryService {
    private val log = LoggerFactory.getLogger(ProductCatgoryServiceImpl::class.java)
    override fun findAll(pageable: Pageable): Page<ProductCategoryDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findOne(id: UUID): Optional<ProductCategoryDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: UUID) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun search(query: String, pageable: Pageable): Page<ProductCategoryDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(productCategoryDTO: ProductCategoryDTO): ProductCategoryDTO {
        log.debug("Request to save ProductCategory : {}", productCategoryDTO)

        var productCategory = productCategoryMapper.toEntity(productCategoryDTO)
        productCategory = productCategoryRepository.save(productCategory)
        val result = productCategoryMapper.toDto(productCategory)
        productCategoryRepository.save(productCategory)
        return result
    }
}