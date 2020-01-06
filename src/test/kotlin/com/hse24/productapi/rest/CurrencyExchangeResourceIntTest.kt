package com.hse24.productapi.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.hse24.productapi.config.AppConfiguration
import com.hse24.productapi.extensions.TestExtensions
import com.hse24.productapi.helper.FixtureCreator
import com.hse24.productapi.repository.ProductCategoryRepository
import com.hse24.productapi.service.CurrencyExchangeService
import com.hse24.productapi.service.enumeration.Currency
import com.hse24.productapi.web.rest.CurrencyExchangeController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.jms.core.JmsTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import javax.websocket.server.ServerApplicationConfig


/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */

@ExtendWith
@SpringBootTest


class CurrencyExchangeResourceIntTest {

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    private lateinit var restProductMockMvc: MockMvc
    val fixtureCreator: FixtureCreator = FixtureCreator()

    @Autowired
    private lateinit var currencyExchangeService: CurrencyExchangeService

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var appConfiguration: AppConfiguration

    @Autowired
    private lateinit var jmsTemplate: JmsTemplate


    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        TestExtensions.OBJECT_MAPPER=objectMapper
        val currencyExchangeController = CurrencyExchangeController(currencyExchangeService,jmsTemplate,appConfiguration)
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(currencyExchangeController)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build()
    }
    @Test
    @Transactional
    fun requestExchangeRate() {
        restProductMockMvc.perform(MockMvcRequestBuilders.get("/api/currency-exchange/{fromCurrency}/{toCurrency}/{value}",Currency.EUR,Currency.USD,10))
                .andExpect(MockMvcResultMatchers.status().isAccepted)
    }
}
