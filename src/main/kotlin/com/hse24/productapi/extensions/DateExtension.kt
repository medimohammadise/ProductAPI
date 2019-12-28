package com.hse24.productapi.extensions

import java.util.Date
    import java.time.temporal.ChronoUnit
    import java.time.Instant

fun Date.getTheDayBefore(days:Long):Date{
    val now = Instant.now()
    return  Date.from(now.minus(days, ChronoUnit.DAYS))
}