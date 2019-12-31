package com.hse24.productapi.web.rest

import com.hse24.productapi.service.ProductService
import com.hse24.productapi.service.dto.ProductDTO
import com.hse24.productapi.utils.HeaderUtil
import com.hse24.productapi.utils.PaginationUtil
import com.hse24.productapi.web.rest.errors.BadRequestAlertException
import org.hibernate.id.IdentifierGenerator.ENTITY_NAME
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ProductController(
        val productService: ProductService
) {
    private val log = LoggerFactory.getLogger(this.javaClass)



    @PostMapping("/products")
    fun createProduct(@Valid @RequestBody productDTO: ProductDTO): ResponseEntity<ProductDTO> {
        log.debug("REST request to save Product : {}", productDTO)
        if (productDTO.id != null) {
            throw BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists")
        }
        val result = productService.save(productDTO)
        return ResponseEntity.created(URI("/api/products/" + result.id))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, true, ENTITY_NAME, result.id.toString()))
                .body(result)
    }

    /**
     * `PUT  /products` : Updates an existing product.
     *
     * @param productDTO the productDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated productDTO,
     * or with status `400 (Bad Request)` if the productDTO is not valid,
     * or with status `500 (Internal Server Error)` if the productDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products")
    fun updateProduct(@Valid @RequestBody productDTO: ProductDTO): ResponseEntity<ProductDTO> {
        log.debug("REST request to update Product : {}", productDTO)
        if (productDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = productService.save(productDTO)
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, true, ENTITY_NAME, productDTO.id.toString()))
                .body(result)
    }

    /**
     * `GET  /products` : get all the products.
     *
     * @param pageable the pagination information.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of products in body.
     */
    @GetMapping("/products")
    fun getAllProducts(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<ProductDTO>> {
        log.debug("REST request to get a page of Products")
        val page = productService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /products/:id` : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the productDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/products/{id}")
    fun getProduct(@PathVariable id: Long): ResponseEntity<ProductDTO> {
        TODO("not implemented")
    }

    /**
     * `DELETE  /products/:id` : delete the "id" product.
     *
     * @param id the id of the productDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        TODO("not implemented")
    }
}