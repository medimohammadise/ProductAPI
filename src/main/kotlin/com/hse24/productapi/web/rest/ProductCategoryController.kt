package com.hse24.productapi.web.rest


import com.hse24.productapi.service.ProductCategoryService
import com.hse24.productapi.service.dto.ProductCategoryDTO
import com.hse24.productapi.utils.HeaderUtil
import com.hse24.productapi.utils.PaginationUtil
import com.hse24.productapi.web.rest.errors.BadRequestAlertException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.URISyntaxException
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ProductCategoryController(
    val productCategoryService: ProductCategoryService
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * `POST  /product-categories` : Create a new productCategory.
     *
     * @param productCategoryDTO the productCategoryDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new productCategoryDTO, or with status `400 (Bad Request)` if the productCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-categories")
    fun createProductCategory(@Valid @RequestBody productCategoryDTO: ProductCategoryDTO): ResponseEntity<ProductCategoryDTO> {
        log.debug("REST request to save ProductCategory : {}", productCategoryDTO)
        if (productCategoryDTO.id != null) {
            throw BadRequestAlertException("A new productCategory cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = productCategoryService.save(productCategoryDTO)
        return ResponseEntity.created(URI("/api/product-categories/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /product-categories` : Updates an existing productCategory.
     *
     * @param productCategoryDTO the productCategoryDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated productCategoryDTO,
     * or with status `400 (Bad Request)` if the productCategoryDTO is not valid,
     * or with status `500 (Internal Server Error)` if the productCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-categories")
    fun updateProductCategory(@Valid @RequestBody productCategoryDTO: ProductCategoryDTO): ResponseEntity<ProductCategoryDTO> {
        log.debug("REST request to update ProductCategory : {}", productCategoryDTO)
        if (productCategoryDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = productCategoryService.save(productCategoryDTO)
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, true, ENTITY_NAME, productCategoryDTO.id.toString()))
            .body(result)
    }

    /**
     * `GET  /product-categories` : get all the productCategories.
     *
     * @param pageable the pagination information.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of productCategories in body.
     */
    @GetMapping("/product-categories")    
    fun getAllProductCategories(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<ProductCategoryDTO>> {
        log.debug("REST request to get a page of ProductCategories")
        val page = productCategoryService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }


    /**
     * `DELETE  /product-categories/:id` : delete the "id" productCategory.
     *
     * @param id the id of the productCategoryDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/product-categories/{id}")
    fun deleteProductCategory(@PathVariable id: UUID): ResponseEntity<Void> {
        log.debug("REST request to delete ProductCategory : {}", id)
        productCategoryService.delete(id)
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, true, ENTITY_NAME, id.toString())).build()
    }

    /**
     * `SEARCH  /_search/product-categories?query=:query` : search for the productCategory corresponding
     * to the query.
     *
     * @param query the query of the productCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/product-categories")
    fun searchProductCategories(@RequestParam query: String, pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<ProductCategoryDTO>> {
        log.debug("REST request to search for a page of ProductCategories for query {}", query)
        val page = productCategoryService.search(query, pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }


    companion object {
        private const val ENTITY_NAME = "productCategory"
    }
}
