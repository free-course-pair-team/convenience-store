package store

import java.time.LocalDate

data class Promotion(
    val buy: Int,
    val get: Int,
    val startDate: LocalDate,
    val endDate: LocalDate
)