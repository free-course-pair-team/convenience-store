package store.util

import java.text.DecimalFormat

fun Int.toWonFormat(): String {
    if (this < 0) return "-"+DecimalFormat("#,###").format(this)
    return DecimalFormat("#,###").format(this)
}