package com.hse24.productapi.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.ResultActions

class TestExtensions {
    companion object {
        lateinit var OBJECT_MAPPER: ObjectMapper
    }
}

inline fun <reified T> ResultActions.andReturnResult(): T {
    val response = this.andReturn().response
    val json = response.contentAsString
    return TestExtensions.OBJECT_MAPPER.readValue(json, T::class.java)
}