package com.hse24.productapi.extensions

import com.github.javafaker.Commerce
import com.github.javafaker.Faker
import java.math.BigDecimal

/** Generates 8 digit product code **/
fun Commerce.productCode()= Faker.instance().bothify("HSE24-########")
fun Commerce.productDescription()= Faker.instance().letterify("????? ??????? ?????? ??")
fun Commerce.priceDecimal()= BigDecimal.valueOf(Faker.instance().number().randomDouble(2,1,1000000000))