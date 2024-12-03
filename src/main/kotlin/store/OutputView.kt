package store

import java.text.DecimalFormat

class OutputView {
    fun printStart() {
        println("안녕하세요. W편의점입니다.")
    }

    fun printProducts(products: List<Product>) {
        println("현재 보유하고 있는 상품입니다.")
        products.forEach { product ->
            println("- ${product.name} ${product.price.wonFormat()}원 ${product.quantity}개 ${product.promotion ?: ""}")
        }
    }

    private fun Int.wonFormat(): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(this)
    }
}