package store.domain

import store.model.Item
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
        val filteringItems = stock.items.filter { it.name() == name }
        var remainingQuantity = quantity
        for (item in filteringItems) {
            if (remainingQuantity > item.quantity()) {
                items.add(item)
                remainingQuantity -= item.quantity()
                continue
            }
            item.apply { setQuantity(remainingQuantity) }
            items.add(item)
        }
    }
}