package com.hse24.productapi.service

import com.hse24.productapi.helper.FixtureCreator
import com.hse24.productapi.repository.ProductCategoryRepository
import com.hse24.productapi.repository.ProductRepository
import com.hse24.productapi.service.enumeration.Currency
import com.hse24.productapi.service.mapper.ProductCategoryMapper
import com.hse24.productapi.service.mapper.ProductMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import java.math.BigDecimal


@ExtendWith
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var productService: ProductService
    @Autowired
    private lateinit var productCategoryRepository: ProductCategoryRepository

    @Autowired
    private lateinit var productCategoryMapper: ProductCategoryMapper

    @Autowired
    private lateinit var productMapper: ProductMapper


    val fixtureCreator: FixtureCreator = FixtureCreator()

    @Nested
    inner class ProductCRUDOperations {
        @Test
        fun `test create new product`() {

            var productDTO = fixtureCreator.createProduct()
            var productCategoryDTO = fixtureCreator.createProductCategory()
            val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
            productDTO.productCategoryId = productCategory.id
            productDTO = productService.save(productDTO)
            productRepository.findById(productDTO.id!!)
            assertThat(productDTO.code).isNotEmpty()
        }

        @Test
        fun `test getAll products`() {
            var productCategoryDTO = fixtureCreator.createProductCategory(code = "CAT-00001", name = "Category1")
            val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))


            val productList = mutableListOf(
                    fixtureCreator.createProduct(
                            code = "HSE24-23565",
                            price = BigDecimal(20.24),
                            currency = Currency.EUR,
                            productCategoryId = productCategory.id),
                    fixtureCreator.createProduct(
                            code = "HSE24-23566",
                            price = BigDecimal(20.24),
                            currency = Currency.EUR,
                            productCategoryId = productCategory.id),
                    fixtureCreator.createProduct(
                            code = "HSE24-23567",
                            price = BigDecimal(20.24),
                            currency = Currency.EUR,
                            productCategoryId = productCategory.id)
            )

            productRepository.saveAll(productMapper.toEntity(productList))
            productRepository.flush()
            val fetchedProductList = productService.findAll(PageRequest.of(0, 10))

            assertThat(fetchedProductList.content.size).isNotZero()
            assertThat(fetchedProductList.content).containsAll(productList)
        }
    }
}
