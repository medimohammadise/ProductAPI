package com.hse24.productapi.web.rest

import com.hse24.productapi.service.dto.ProductCategoryDTO
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
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ProductCategoryController {
    @PostMapping("/product-categories")
    fun createProductCategory(@Valid @RequestBody productCategoryDTO: ProductCategoryDTO): ResponseEntity<ProductCategoryDTO> {
        TODO("not implemented")
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
        TODO("not implemented")
    }

    /**
     * `GET  /product-categories` : get all the productCategories.
     *
     * @param pageable the pagination information.
     * @return the [ResponseEntity] with status `200 (OK)` and the list of productCategories in body.
     */
    @GetMapping("/product-categories")
    fun getAllProductCategories(pageable: Pageable, @RequestParam queryParams: MultiValueMap<String, String>, uriBuilder: UriComponentsBuilder): ResponseEntity<MutableList<ProductCategoryDTO>> {
        TODO("not implemented")
    }

    /**
     * `GET  /product-categories/:id` : get the "id" productCategory.
     *
     * @param id the id of the productCategoryDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the productCategoryDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/product-categories/{id}")
    fun getProductCategory(@PathVariable id: Long): ResponseEntity<ProductCategoryDTO> {
        TODO("not implemented")
    }

    /**
     * `DELETE  /product-categories/:id` : delete the "id" productCategory.
     *
     * @param id the id of the productCategoryDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/product-categories/{id}")
    fun deleteProductCategory(@PathVariable id: Long): ResponseEntity<Void> {
        TODO("not implemented")
    }

}