package com.hse24.productapi.service

import com.hse24.productapi.BaseTest
import com.hse24.productapi.helper.FixtureCreator
import com.hse24.productapi.helper.FixtureCreator.Companion.CHILD_PRODUCT_CATEGORY_CODE
import com.hse24.productapi.helper.FixtureCreator.Companion.CHILD_PRODUCT_CATEGORY_NAME
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CATEGORY_CODE
import com.hse24.productapi.helper.FixtureCreator.Companion.DEFAULT_PRODUCT_CATEGORY_NAME
import com.hse24.productapi.helper.FixtureCreator.Companion.UPDATED_PRODUCT_CATEGORY_CODE
import com.hse24.productapi.helper.FixtureCreator.Companion.UPDATED_PRODUCT_CATEGORY_NAME
import com.hse24.productapi.repository.ProductCategoryRepository
import com.hse24.productapi.service.mapper.ProductCategoryMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import kotlin.test.assertNotNull


@ExtendWith
@SpringBootTest
class ProductCategoryServiceTest: BaseTest() {

    @Autowired
    private lateinit var productCategoryRepository: ProductCategoryRepository

    @Autowired
    private lateinit var productCategoryService: ProductCategoryService


    @Autowired
    private lateinit var productCategoryMapper: ProductCategoryMapper


    val fixtureCreator: FixtureCreator = FixtureCreator()

    @Nested
    inner class ProductCategoryCRUDOperations {
        @Test
        fun `test create new productCategory`() {

            var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
            val parentProductCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))
            val parentProductCategoryId=parentProductCategory.id
            var childProductCategoryDTO = fixtureCreator.createProductCategory(code = CHILD_PRODUCT_CATEGORY_CODE, name = CHILD_PRODUCT_CATEGORY_NAME,parentCategoryId = parentProductCategoryId)
            val childProductCategory = productCategoryRepository.save(productCategoryMapper.toEntity(childProductCategoryDTO))

            productCategoryRepository.findById(childProductCategory.id!!)
            assertThat(childProductCategory.code).isEqualTo(CHILD_PRODUCT_CATEGORY_CODE)
            assertThat(childProductCategory.name).isEqualTo(CHILD_PRODUCT_CATEGORY_NAME)
            assertThat(childProductCategory.productCategory?.id).isEqualTo(parentProductCategory.id)
        }

        @Test
        fun `test update the productCategory`() {
            var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
            val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))

            val id = productCategory.id
            assertNotNull(id)
            val updatedProductCategory = productCategoryRepository.findById(id).get()

            updatedProductCategory.code = UPDATED_PRODUCT_CATEGORY_CODE
            updatedProductCategory.name = UPDATED_PRODUCT_CATEGORY_NAME

            productCategoryRepository.save(updatedProductCategory)
            val testProduct = productCategoryRepository.findById(id).get()

            assertThat(testProduct.code).isEqualTo(FixtureCreator.UPDATED_PRODUCT_CATEGORY_CODE)
            assertThat(testProduct.name).isEqualTo(FixtureCreator.UPDATED_PRODUCT_CATEGORY_NAME)

        }

        @Test
        fun `test delete the productCategory`() {
            var productCategoryDTO = fixtureCreator.createProductCategory(code = DEFAULT_PRODUCT_CATEGORY_CODE, name = DEFAULT_PRODUCT_CATEGORY_NAME)
            val productCategory = productCategoryRepository.save(productCategoryMapper.toEntity(productCategoryDTO))

            val databaseSizeBeforeDelete = productCategoryRepository.findAll().size
            val id = productCategory.id
            assertNotNull(id)
            productCategoryRepository.deleteById(id)

            val databaseSizeAfterDelete = productCategoryRepository.findAll().size

            assertThat(databaseSizeBeforeDelete).isEqualTo(databaseSizeAfterDelete + 1)

        }

        @Test
        fun `test getAll products`() {
           val productCategoryList=productCategoryRepository.saveAll(productCategoryMapper.toEntity(mutableListOf(
                    fixtureCreator.createProductCategory(
                            code = DEFAULT_PRODUCT_CATEGORY_CODE,
                            name = DEFAULT_PRODUCT_CATEGORY_NAME),
                    fixtureCreator.createProductCategory(
                            code = UPDATED_PRODUCT_CATEGORY_CODE,
                            name = DEFAULT_PRODUCT_CATEGORY_NAME
                    )
            )))
            productCategoryRepository.flush()
            val fetchedProductCategoryList = productCategoryService.findAll(PageRequest.of(0, 100))

            assertThat(fetchedProductCategoryList.content.size).isNotZero()
            assertThat(fetchedProductCategoryList.content).containsAll(productCategoryMapper.toDto(productCategoryList))
        }
    }
}
