package store.domain

import store.model.GeneralItem
import store.model.Item
import store.model.PromotionItem
import store.model.ShoppingCart
import store.model.Stock

class ShoppingCartFactory() {
    private val items = mutableListOf<Item>()

    fun generate(inputProductAndQuantity: List<Pair<String, Int>>, stock: Stock): ShoppingCart {
        inputProductAndQuantity.forEach { (name, quantity) ->
            add(stock, name, quantity)
        }
        return ShoppingCart(items)
    }

    private fun add(stock: Stock, name: String, quantity: Int) {
        val filteringItems = stock.getItems().filter { it.name() == name }
        var remainingQuantity = quantity
        for (item in filteringItems) {
            if (remainingQuantity > item.quantity()) {
                items.add(item)
                remainingQuantity -= item.quantity()
                continue
            }
            if (remainingQuantity > 0) {
                val t = when(item) {
                    is GeneralItem -> {
                        item.copy(quantity = remainingQuantity)
                    }
                    is PromotionItem -> {
                        item.copy(quantity = remainingQuantity)
                    }
                    else -> {item}
                }
                items.add(t)
                remainingQuantity = 0
            }
        }
    }
}