package com.hse24.productapi.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.hse24.productapi.extensions.TestExtensions
import com.hse24.productapi.extensions.andReturnResult
import com.hse24.productapi.extensions.writeValueAsByte
import com.hse24.productapi.helper.FixtureCreator
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CATEGORY_CODE
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CATEGORY_NAME
import com.hse24.productapi.helper.FixtureCreator.Companion.UPDATED_PRODUCT_CATEGORY_CODE
import com.hse24.productapi.helper.FixtureCreator.Companion.UPDATED_PRODUCT_CATEGORY_NAME
import com.hse24.productapi.repository.ProductCategoryRepository
import com.hse24.productapi.service.ProductCategoryService
import com.hse24.productapi.service.dto.ProductCategoryDTO
import com.hse24.productapi.service.dto.ProductDTO
import com.hse24.productapi.service.mapper.ProductCategoryMapper
import com.hse24.productapi.web.rest.ProductCategoryController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertNotNull


/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */

@ExtendWith
@SpringBootTest
class ProductCategoryResourceIntTest {

    @Autowired
    private lateinit var productCategoryRepository: ProductCategoryRepository

    @Autowired
    private lateinit var productCategoryMapper: ProductCategoryMapper

    @Autowired
    private lateinit var productCategoryService: ProductCategoryService


    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver


    private lateinit var restProductMockMvc: MockMvc
    val fixtureCreator: FixtureCreator = FixtureCreator()

    @Autowired
    protected lateinit var objectMapper: ObjectMapper


    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        TestExtensions.OBJECT_MAPPER=objectMapper
        val productResource = ProductCategoryController(productCategoryService)
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build()
    }
    @Test
    @Transactional
    fun getAllProductCategories() {
        val productCategoryList = mutableListOf(
                fixtureCreator.createProductCategory(
                        code = DEFAULT_PRODUCT_CATEGORY_CODE,
                        name = DEFAULT_PRODUCT_CATEGORY_NAME),
                fixtureCreator.createProductCategory(
                        code = FixtureCreator.UPDATED_PRODUCT_CATEGORY_CODE,
                        name = DEFAULT_PRODUCT_CATEGORY_NAME
                )
        )

        productCategoryRepository.saveAll(productCategoryMapper.toEntity(productCategoryList))
        productCategoryRepository.flush()

        // Get all the productCategoryList
        val result=restProductMockMvc.perform(MockMvcRequestBuilders.get("/api/product-categories?page=0,size=100,sort=id,desc"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturnResult<MutableList<ProductDTO>>()
        assertThat(result.size).isNotZero()
       // assertThat(result).containsAll(fetchedProductCategoryList)
    }

    @Test
    @Transactional
    fun updateProductCategory() {
        var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
        val productCategory = this.productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))


        // Update the product
        val id = productCategory.id
        assertNotNull(id)
        val updatedProductCategory = productCategoryRepository.findById(id).get()

        updatedProductCategory.code = UPDATED_PRODUCT_CATEGORY_CODE
        updatedProductCategory.name = UPDATED_PRODUCT_CATEGORY_NAME


        val updatedProductDTO=restProductMockMvc.perform(
                MockMvcRequestBuilders.put("/api/product-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProductCategory.writeValueAsByte())
        ).andExpect(MockMvcResultMatchers.status().isOk)
                .andReturnResult<ProductCategoryDTO>()

        // Validate the Product in the database
        val newProductCategory = productCategoryRepository.findById(updatedProductDTO.id!!).get()

        assertThat(newProductCategory.code).isEqualTo(UPDATED_PRODUCT_CATEGORY_CODE)
        assertThat(newProductCategory.name).isEqualTo(UPDATED_PRODUCT_CATEGORY_NAME)

    }

    @Test
    @Transactional
    fun createProductCategory() {
        var parentProductCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
        val parentProductCategory = productCategoryRepository.save(productCategoryMapper.toEntity(parentProductCategoryDTO))
        val parentProductCategoryId=parentProductCategory.id
        var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME,parentCategoryId = parentProductCategoryId)
        // Create the Product
        val newProductCategoryDTO= restProductMockMvc.perform(
                MockMvcRequestBuilders.post("/api/product-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productCategoryDTO.writeValueAsByte()))
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andReturnResult<ProductCategoryDTO>()

        // Validate the Product in the database
        assertThat(newProductCategoryDTO.code).isEqualTo(DEFAULT_PRODUCT_CATEGORY_CODE)
        assertThat(newProductCategoryDTO.name).isEqualTo(DEFAULT_PRODUCT_CATEGORY_NAME)
        assertThat(newProductCategoryDTO.productCategoryId).isEqualTo(parentProductCategoryId!!)

    }

    @Test
    @Transactional
    fun deleteProductCategory() {
        var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
        val productCategory = this.productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))

        val databaseSizeBeforeDelete = productCategoryRepository.findAll().size

        val id =  productCategoryRepository.findById(productCategory.id!!).get().id
        assertNotNull(id)

        // Delete the product
        restProductMockMvc.perform(
                MockMvcRequestBuilders.delete("/api/product-categories/{id}", id)
        ).andExpect(MockMvcResultMatchers.status().isNoContent)

        // Validate the database is empty
        val productCategoryList = productCategoryRepository.findAll()
        assertThat(productCategoryList).hasSize(databaseSizeBeforeDelete - 1)

    }
}
