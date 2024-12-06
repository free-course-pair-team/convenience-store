package store.util

import java.text.DecimalFormat

fun Int.toWonFormat(): String =
    DecimalFormat("#,###").format(this)

fun Int.minusFormat(): String {
    return "-"+DecimalFormat("#,###").format(this)
}