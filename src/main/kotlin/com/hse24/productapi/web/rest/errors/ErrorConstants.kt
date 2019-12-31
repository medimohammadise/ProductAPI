package com.hse24.productapi.web.rest.errors

import java.net.URI

object ErrorConstants {
    const val ERR_CONCURRENCY_FAILURE: String = "error.concurrencyFailure"
    const val ERR_VALIDATION: String = "error.validation"
    const val PROBLEM_BASE_URL: String = "https://www.hse24.com/productapi"
    @JvmField
    val DEFAULT_TYPE: URI = URI.create("$PROBLEM_BASE_URL/problem-with-message")
    @JvmField
    val CONSTRAINT_VIOLATION_TYPE: URI = URI.create("$PROBLEM_BASE_URL/constraint-violation")
    @JvmField
    val PARAMETERIZED_TYPE: URI = URI.create("$PROBLEM_BASE_URL/parameterized")
    @JvmField
    val ENTITY_NOT_FOUND_TYPE: URI = URI.create("$PROBLEM_BASE_URL/entity-not-found")
    @JvmField
    val INVALID_PASSWORD_TYPE: URI = URI.create("$PROBLEM_BASE_URL/invalid-password")
}
