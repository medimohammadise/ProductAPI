package com.hse24.productapi.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.hse24.productapi.extensions.TestExtensions
import com.hse24.productapi.extensions.andReturnResult
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

}
