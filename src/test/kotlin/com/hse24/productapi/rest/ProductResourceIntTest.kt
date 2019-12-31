package com.hse24.productapi.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.hse24.productapi.extensions.TestExtensions
import com.hse24.productapi.extensions.andReturnResult
import com.hse24.productapi.extensions.writeValueAsByte
import com.hse24.productapi.helper.FixtureCreator
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_CREATED_AT
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_CURRENCY
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_DESCRIPTION
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRICE
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CATEGORY_CODE
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CATEGORY_NAME
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CODE
import com.hse24.productapi.helper.FixtureCreator.Companion.UPDATED_CREATED_AT
import com.hse24.productapi.helper.FixtureCreator.Companion.UPDATED_CURRENCY
import com.hse24.productapi.helper.FixtureCreator.Companion.UPDATED_DESCRIPTION
import com.hse24.productapi.helper.FixtureCreator.Companion.UPDATED_PRICE
import com.hse24.productapi.helper.FixtureCreator.Companion.UPDATED_PRODUCT_CODE
import com.hse24.productapi.repository.ProductCategoryRepository
import com.hse24.productapi.repository.ProductRepository
import com.hse24.productapi.service.ProductService
import com.hse24.productapi.service.dto.ProductDTO
import com.hse24.productapi.service.enumeration.Currency
import com.hse24.productapi.service.mapper.ProductCategoryMapper
import com.hse24.productapi.service.mapper.ProductMapper
import com.hse24.productapi.web.rest.ProductController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import kotlin.test.assertNotNull


/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */

@ExtendWith
@SpringBootTest(properties=["jhipster.clientApp.name=dummyValue"])


class ProductResourceIntTest {

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var productMapper: ProductMapper

    @Autowired
    private lateinit var productService: ProductService


    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var productCategoryRepository: ProductCategoryRepository

    private lateinit var restProductMockMvc: MockMvc
    val fixtureCreator: FixtureCreator = FixtureCreator()

    @Autowired
    private lateinit var productCategoryMapper: ProductCategoryMapper

    @Autowired
    protected lateinit var objectMapper: ObjectMapper


    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        TestExtensions.OBJECT_MAPPER=objectMapper
        val productResource = ProductController(productService)
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build()

        System.setProperty("jhipster.clientApp.name", "test");
    }
    @Test
    @Transactional
    fun getAllProducts() {
        var productCategoryDTO = fixtureCreator.createProductCategory(code = "CAT-00001", name = "Category1")
        val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
        var productList = mutableListOf(
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

        productList=productService.findAll(PageRequest.of(1,10)).content
        // Get all the productList
        val result=restProductMockMvc.perform(MockMvcRequestBuilders.get("/api/products?page=0,size=100,sort=id,desc"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturnResult<MutableList<ProductDTO>>()
        assertThat(result.size).isNotZero()
        assertThat(result).containsAll(productList)
    }

    @Test
    @Transactional
    fun updateProduct() {
        var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
        val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
        var productDTO=fixtureCreator.createProduct(
                code = DEFAULT_PRODUCT_CODE,
                description = DEFAULT_DESCRIPTION,
                price = DEFAULT_PRICE,
                currency = DEFAULT_CURRENCY,
                productCategoryId = productCategory.id)

        // Initialize the database
        val product=productRepository.saveAndFlush(productMapper.toEntity(productDTO))

        val databaseSizeBeforeUpdate = productRepository.findAll().size

        // Update the product
        val id = product.id
        assertNotNull(id)
        val updatedProduct = productRepository.findById(id).get()

        updatedProduct.code = UPDATED_PRODUCT_CODE
        updatedProduct.description = UPDATED_DESCRIPTION
        updatedProduct.price = UPDATED_PRICE
        updatedProduct.currency = UPDATED_CURRENCY
        updatedProduct.createdAt = UPDATED_CREATED_AT
        productDTO = productMapper.toDto(updatedProduct)

        val updatedProductDTO=restProductMockMvc.perform(
                MockMvcRequestBuilders.put("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDTO.writeValueAsByte())
        ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturnResult<ProductDTO>()

        // Validate the Product in the database
        val newProduct = productRepository.findById(updatedProductDTO.id).get()

        assertThat(newProduct.code).isEqualTo(UPDATED_PRODUCT_CODE)
        assertThat(newProduct.description).isEqualTo(UPDATED_DESCRIPTION)
        assertThat(newProduct.price).isEqualTo(UPDATED_PRICE)
        assertThat(newProduct.currency).isEqualTo(UPDATED_CURRENCY)
        assertThat(newProduct.createdAt).isEqualTo(UPDATED_CREATED_AT)

    }

    @Test
    @Transactional
    fun createProduct() {
        val databaseSizeBeforeCreate = productRepository.findAll().size

        var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
        val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
        var productDTO=fixtureCreator.createProduct(
                code = DEFAULT_PRODUCT_CODE,
                description = DEFAULT_DESCRIPTION,
                price = DEFAULT_PRICE,
                currency = DEFAULT_CURRENCY,
                createdAt = DEFAULT_CREATED_AT,
                productCategoryId = productCategory.id)

        // Create the Product
       val newProductDTO= restProductMockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productDTO.writeValueAsByte()))
                .andExpect(MockMvcResultMatchers.status().isCreated)
               .andReturnResult<ProductDTO>()

        // Validate the Product in the database
        val newProduct = productRepository.findById(newProductDTO.id).get()
        assertThat(newProduct.code).isEqualTo(DEFAULT_PRODUCT_CODE)
        assertThat(newProduct.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(newProduct.price).isEqualTo(DEFAULT_PRICE)
        assertThat(newProduct.currency).isEqualTo(DEFAULT_CURRENCY)
        assertThat(newProduct.createdAt).isEqualTo(DEFAULT_CREATED_AT)
    }

    @Test
    @Transactional
    fun deleteProduct() {
        val databaseSizeBeforeCreate = productRepository.findAll().size

        var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
        val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
        var productDTO=fixtureCreator.createProduct(
                code = DEFAULT_PRODUCT_CODE,
                description = DEFAULT_DESCRIPTION,
                price = DEFAULT_PRICE,
                currency = DEFAULT_CURRENCY,
                createdAt = DEFAULT_CREATED_AT,
                productCategoryId = productCategory.id)
        val product=productRepository.saveAndFlush(productMapper.toEntity(productDTO))


        val databaseSizeBeforeDelete = productRepository.findAll().size

        val id =  productRepository.findById(product.id).get().id
        assertNotNull(id)

        // Delete the product
        restProductMockMvc.perform(
                MockMvcRequestBuilders.delete("/api/products/{id}", id)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)

        // Validate the database is empty
        val productList = productRepository.findAll()
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1)

    }
}
