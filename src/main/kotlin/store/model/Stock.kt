package store.model

import store.domain.ItemManager

data class Stock(val items: MutableList<Item>) {
    override fun toString(): String {
        var lastPrint = listOf("", "0", "0", "null")
        var string = ""

        items.map { it.itemMessage() }.forEach {

            var value = it.split(" ") + "null"

            if (lastPrint[0] != value[0].toString() && lastPrint[3] != "null") {
                string += "${lastPrint[0]} ${lastPrint[1]} 재고 없음\n"
                lastPrint = value
                string += it + "\n"
            } else {
                lastPrint = value
                string += it + "\n"
            }
        }
        return string
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
            val t = products.map { item ->
                val itemData = item.split(",")
                val promotionDataList = promotionInfoList.map { it.split(",") }
                ItemManager.getGeneralOrPromotionItem(itemData, promotionDataList)
            }
            stock = Stock(t.toMutableList())
            return stock!!
        }
    }
}