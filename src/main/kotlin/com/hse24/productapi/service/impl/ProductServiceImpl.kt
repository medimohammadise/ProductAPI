package com.hse24.productapi.service.impl

import com.hse24.productapi.repository.ProductRepository
import com.hse24.productapi.service.ProductService
import com.hse24.productapi.service.dto.ProductDTO
import com.hse24.productapi.service.mapper.ProductMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
@Transactional
class ProductServiceImpl
(val productRepository: ProductRepository,
 val productMapper: ProductMapper
) : ProductService {
    private val log = LoggerFactory.getLogger(ProductServiceImpl::class.java)
    override fun findAll(pageable: Pageable): Page<ProductDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findOne(id: Long): Optional<ProductDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun search(query: String, pageable: Pageable): Page<ProductDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(productDTO: ProductDTO): ProductDTO {
        log.debug("Request to save Product : {}", productDTO)
        var product = productMapper.toEntity(productDTO)
        product = productRepository.save(product)
        val result = productMapper.toDto(product)
        return result

    }

}