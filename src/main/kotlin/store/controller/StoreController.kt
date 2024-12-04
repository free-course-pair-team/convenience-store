package store.controller

import store.domain.entity.Product
import store.domain.entity.Promotion
import store.domain.entity.Store
import store.domain.service.PromotionCalculator
import store.view.InputView
import store.view.OutputView
import store.view.PromotionOptionInputView
import java.io.FileReader
import java.time.LocalDate

class StoreController {
    private lateinit var store: Store
    private val inputView = InputView()
    private val outputView = OutputView()

    fun run() {
        outputView.printStart()
        store = Store(getProducts(), getPromotions())
        outputView.printProducts(store.getProducts())
        val purchaseProducts = inputView.readPurchaseProducts(store.getProducts())
        val promotionCalculator =
            PromotionCalculator(store = store, promotionOptionInputView = PromotionOptionInputView())
        val promotionResult = promotionCalculator.runPromotion(purchaseProducts)
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

    private fun getPromotions(): Map<String, Promotion> {
        return FileReader("src/main/resources/promotions.md").readLines().drop(1).map {
            val (name, buy, get, startDate, endDate) = it.split(",")
            Pair(name, Promotion(buy.toInt(), get.toInt(), LocalDate.parse(startDate), LocalDate.parse(endDate)))
        }.toMap()
    }

    private fun String.mapToNull(): String? {
        if (this == "null") return null
        return this
    }
}
