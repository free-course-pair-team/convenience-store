package store

import java.io.FileReader

class StoreController {
    private val inputView = InputView()
    private val outputView = OutputView()

    fun run() {
        outputView.printStart()
        val product = getProducts()
        outputView.printProducts(product)
    }

    private fun getProducts(): List<Product> {
        val products = FileReader("src/main/resources/products.md").readLines().drop(1).map {
            val (name, price, quantity, promotion) = it.split(",")
            Product(name, price.toInt(), quantity.toInt(), promotion.mapToNull())
        }.toMutableList()
        val onlyPromotionProduct =
            products.filter { product -> product.promotion != null && products.count { it.name == product.name } == 1 }
        onlyPromotionProduct.forEach { products.add(it.copy(quantity = 0)) }

        return products.groupBy { it.name }.flatMap { (_, products) -> products.sortedByDescending { it.promotion } }
    }

    private fun String.mapToNull(): String? {
        if (this == "null") return null
        return this
    }
}