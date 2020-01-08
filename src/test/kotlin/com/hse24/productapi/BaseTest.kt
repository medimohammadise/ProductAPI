package com.hse24.productapi

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

abstract class  BaseTest(){
    @Autowired
    private lateinit var flyway: Flyway

    @BeforeEach
    fun initEach(){
        flyway.clean()
        flyway.migrate()
    }

    @AfterAll
    fun cleanDB(){
        flyway.clean()
        flyway.migrate()
    }
}