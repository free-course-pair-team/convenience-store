package store.model

import store.domain.ItemManager
import store.domain.PromotionManager

data class Stock(private val items: MutableList<Item>) {

    fun getItems() = items.toList()
    override fun toString(): String {
        var last: Item = items.first()
        val printedItems = mutableListOf<Item>()
        printedItems.add(last)
        for (current in items.slice(1..items.lastIndex)) {
            if (last.name() != current.name() && last is PromotionItem) {
                val index = printedItems.indexOf(last)
                printedItems.add(index+1, GeneralItem(last.name(), last.price(), 0))
            }
            printedItems.add(current)
            last = current
        }
        return printedItems.joinToString("\n") { it.itemMessage() }
    }

    companion object {
        private var stock: Stock? = null
        fun getInstance(): Stock {
            if (stock == null) {
                stock = Stock(mutableListOf())
                return stock!!
            }
            return stock!!
        }

        fun from(products: List<String>, promotionInfoList: List<String>): Stock {
            val promotionManager = PromotionManager()
            val t = products.map { item ->
                val itemData = item.split(",")
                val promotionDataList = promotionInfoList.map { it.split(",") }
                ItemManager.getGeneralOrPromotionItem(itemData, promotionDataList)
            }.filter { promotionManager.isWithInPromotionPeriod(it) }
            stock = Stock(t.toMutableList())
            return stock!!
        }
    }
}