package com.hse24.productapi.service

import com.hse24.productapi.BaseTest
import com.hse24.productapi.helper.FixtureCreator
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_CURRENCY
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_DESCRIPTION
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRICE
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CATEGORY_CODE
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CATEGORY_NAME
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CODE
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
import kotlin.test.assertNotNull


@ExtendWith
@SpringBootTest
class ProductServiceTest : BaseTest() {

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

            var productCategoryDTO = fixtureCreator.createProductCategory(code=DEFAULT_PRODUCT_CATEGORY_CODE,name= DEFAULT_PRODUCT_CATEGORY_NAME )
            val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
            var productDTO=fixtureCreator.createProduct(
                    code = DEFAULT_PRODUCT_CODE,
                    description = DEFAULT_DESCRIPTION,
                    price = DEFAULT_PRICE,
                    currency = DEFAULT_CURRENCY,
                    productCategoryId = productCategory.id)
            productDTO.productCategoryId = productCategory.id
            productDTO = productService.save(productDTO)
            productRepository.findById(productDTO.id!!)
            assertThat(productDTO.code).isEqualTo(DEFAULT_PRODUCT_CODE)
            assertThat(productDTO.description).isEqualTo(DEFAULT_DESCRIPTION)
            assertThat(productDTO.price).isEqualTo(DEFAULT_PRICE)
            assertThat(productDTO.currency).isEqualTo(DEFAULT_CURRENCY)
        }

        @Test
        fun `test update the product`() {
            var productCategoryDTO = fixtureCreator.createProductCategory(code=DEFAULT_PRODUCT_CATEGORY_CODE,name= DEFAULT_PRODUCT_CATEGORY_NAME )
            val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
            var productDTO=fixtureCreator.createProduct(
                    code = DEFAULT_PRODUCT_CODE,
                    description = DEFAULT_DESCRIPTION,
                    price = DEFAULT_PRICE,
                    currency = DEFAULT_CURRENCY,
                    productCategoryId = productCategory.id)
            productDTO.productCategoryId = productCategory.id
            productDTO = productService.save(productDTO)

            val id = productDTO.id
            assertNotNull(id)
            val updatedProduct = productRepository.findById(id).get()

            updatedProduct.code = FixtureCreator.UPDATED_PRODUCT_CODE
            updatedProduct.description = FixtureCreator.UPDATED_DESCRIPTION
            updatedProduct.price = FixtureCreator.UPDATED_PRICE
            updatedProduct.currency = FixtureCreator.UPDATED_CURRENCY
            updatedProduct.createdAt = FixtureCreator.UPDATED_CREATED_AT

            productRepository.save(updatedProduct)
            val testProduct = productRepository.findById(updatedProduct.id!!).get()

            assertThat(testProduct.code).isEqualTo(FixtureCreator.UPDATED_PRODUCT_CODE)
            assertThat(testProduct.description).isEqualTo(FixtureCreator.UPDATED_DESCRIPTION)
            assertThat(testProduct.currency).isEqualTo(FixtureCreator.UPDATED_CURRENCY)
            assertThat(testProduct.createdAt).isEqualTo(FixtureCreator.UPDATED_CREATED_AT)

        }
        @Test
        fun `test delete the product`() {
            var productCategoryDTO = fixtureCreator.createProductCategory(code=DEFAULT_PRODUCT_CATEGORY_CODE,name= DEFAULT_PRODUCT_CATEGORY_NAME )
            val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
            var productDTO=fixtureCreator.createProduct(
                    code = DEFAULT_PRODUCT_CODE,
                    description = DEFAULT_DESCRIPTION,
                    price = DEFAULT_PRICE,
                    currency = DEFAULT_CURRENCY,
                    productCategoryId = productCategory.id)
            productDTO.productCategoryId = productCategory.id
            productDTO = productService.save(productDTO)

            val databaseSizeBeforeDelete = productRepository.findAll().size
            val id = productDTO.id
            assertNotNull(id)
            productRepository.deleteById(id)

            val databaseSizeAfterDelete = productRepository.findAll().size

            assertThat(databaseSizeBeforeDelete).isEqualTo(databaseSizeAfterDelete+1)


        }
        @Test
        fun `test getAll products`() {
            var productCategoryDTO = fixtureCreator.createProductCategory(code = "CAT-00001", name = "Category1")
            val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))

            val productList=productRepository.saveAll(productMapper.toEntity(mutableListOf(
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
            )))
            productRepository.flush()
            val fetchedProductList = productService.findAll(PageRequest.of(0, 100))

            assertThat(fetchedProductList.content.size).isNotZero()
            assertThat(fetchedProductList.content).containsAll(productMapper.toDto(productList))
        }


    }
}
