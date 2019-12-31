package com.hse24.productapi.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.hse24.productapi.extensions.TestExtensions
import com.hse24.productapi.extensions.andReturnResult
import com.hse24.productapi.extensions.writeValueAsByte
import com.hse24.productapi.helper.FixtureCreator
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
import java.time.Instant
import java.time.temporal.ChronoUnit


/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */

@ExtendWith
@SpringBootTest


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
    fun createProduct() {
        val databaseSizeBeforeCreate = productRepository.findAll().size

        var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
        val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
        var productDTO=fixtureCreator.createProduct(
                        code = DEFAULT_PRODUCT_CODE,
                        description = DEFAULT_DESCRIPTION,
                        price = DEFAULT_PRICE,
                        currency = DEFAULT_CURRENY,
                        productCategoryId = productCategory.id)

        // Create the Product
        restProductMockMvc.perform(
                MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                 .content(productDTO.writeValueAsByte()))
        .andExpect(MockMvcResultMatchers.status().isCreated)

        // Validate the Product in the database
        val productList = productRepository.findAll()
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1)
        val testProduct = productList[productList.size - 1]
        assertThat(testProduct.code).isEqualTo(DEFAULT_PRODUCT_CODE)
        assertThat(testProduct.description).isEqualTo(DEFAULT_DESCRIPTION)
        assertThat(testProduct.price).isEqualTo(DEFAULT_PRICE)
        assertThat(testProduct.currency).isEqualTo(DEFAULT_CURRENY)
        assertThat(testProduct.createdAt).isEqualTo(DEFAULT_CREATED_AT)

        // Validate the Product in Elasticsearch
    }
    companion object {

        private const val DEFAULT_PRODUCT_CODE: String = "AAAAAAAAAA"
        private const val UPDATED_PRODUCT_CODE = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION: String = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        private val DEFAULT_PRICE: BigDecimal = BigDecimal(1)
        private val UPDATED_PRICE: BigDecimal = BigDecimal(2)

        private val DEFAULT_CURRENY: Currency = Currency.EUR
        private val UPDATED_CURRENY: Currency = Currency.USD

        private val DEFAULT_CREATED_AT: Instant = Instant.ofEpochMilli(0L)
        private val UPDATED_CREATED_AT: Instant = Instant.now().truncatedTo(ChronoUnit.MILLIS)

        private const val DEFAULT_PRODUCT_CATEGORY_CODE: String = "CAT-00001"
        private const val DEFAULT_PRODUCT_CATEGORY_NAME: String = "CATEGORY-1"
    }

}
