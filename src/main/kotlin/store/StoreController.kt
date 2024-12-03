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
            Product(
                name, price.toInt(), quantity.toInt(), promotion.mapToNull()
            )
        }
        return products
    }

    private fun String.mapToNull(): String? {
        if (this == "null") return null
        return this
    }
}