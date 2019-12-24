package com.hse24.productapi.service

import com.hse24.productapi.helper.FixtureCreator
import com.hse24.productapi.repository.ProductCategoryRepository
import com.hse24.productapi.repository.ProductRepository
import com.hse24.productapi.service.dto.ProductDTO
import com.hse24.productapi.service.mapper.ProductCategoryMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional


@ExtendWith
@SpringBootTest
@Transactional
class TestProduct {

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var productService: ProductService
    @Autowired
    private lateinit var productCategoryRepository: ProductCategoryRepository

    @Autowired
    private lateinit var productCategoryMapper: ProductCategoryMapper

    val fixtureCreator: FixtureCreator = FixtureCreator()

    @Nested
    inner class ProductCRUDOperations {
        @Test
        fun `test create new product`() {

            var productDTO = fixtureCreator.createProduct()
            var productCategoryDTO = fixtureCreator.createProductCategory()
            val productCatgory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
            productDTO.productCategoryId = productCatgory.id
            productDTO = productService.save(productDTO)
            productRepository.findById(productDTO.id!!)
            assertThat(productDTO.code).isNotEmpty()
        }

        @Test
        fun `test mock product`() {
            var product = mockk<ProductDTO>(relaxed = true)

            every { product.hashCode() } returns product.id.hashCode()
            verify { product.id.hashCode() }

        }
    }
}
