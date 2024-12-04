package store.controller

import store.domain.entity.*
import store.domain.service.PromotionCalculator
import store.util.retryInput
import store.view.InputView
import store.view.OutputView
import store.view.PromotionOptionInputView
import java.io.FileReader
import java.time.LocalDate

class StoreController {
    private val checkout = Checkout()
    private val inputView = InputView()
    private val outputView = OutputView()

    fun run() {
        val store = Store(getProducts(), getPromotions())
        while (true) {
            outputView.printStart()
            outputView.printProducts(store.getProducts())
            val purchaseProducts = retryInput { inputView.readPurchaseProducts(store.getProducts()) }
            val promotionCalculator =
                PromotionCalculator(store = store, promotionOptionInputView = PromotionOptionInputView())
            val promotionResult = promotionCalculator.runPromotion(purchaseProducts)
            store.updateProducts(promotionResult)
            val membershipResult = if (isMembershipDiscount()) checkout.membershipDiscount(promotionResult) else 0
            printReceipt(promotionResult, membershipResult)
            if (isRetryPurchaseStop()) return
        }
    }

    private fun isMembershipDiscount(): Boolean = retryInput {
        val choice = inputView.readMembershipDiscount()
        when (choice) {
            "Y" -> return@retryInput true
            "N" -> return@retryInput false
            else -> throw IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.")
        }
    }

    private fun isRetryPurchaseStop() = retryInput {
        val choice = inputView.readRetryPurchase()
        when (choice) {
            "Y" -> return@retryInput false
            "N" -> return@retryInput true
            else -> throw IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.")
        }
    }

    private fun printReceipt(
        promotionResult: List<PurchaseCompleteProduct>,
        membershipResult: Int
    ) {
        outputView.printReceiptProductInfo(promotionResult)
        outputView.printReceiptPromotionInfo(promotionResult)
        outputView.printReceiptTotalInfo(promotionResult, membershipResult)
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
