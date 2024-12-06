package store.domain

import store.model.GeneralItem
import store.model.Item
import store.model.Promotion
import store.model.PromotionItem

class ItemManager(private val items: MutableList<Item>) {

    fun getItems() =
        items.toList()

    fun findPromotionItem(name: String): PromotionItem? {
        val item = requireNotNull(items.find { it.name() == name })
        if (item is PromotionItem) {
            return item
        }
        return null
    }

    fun findItem(name: String): GeneralItem? {
        return items.filter { it.name() == name }.find { it is GeneralItem } as GeneralItem?


    }

    fun getItemsMessage(): String {
        var lastPrint = listOf<String>("", "0", "0", "null")
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
        private var itemManager: ItemManager? = null
        fun getInstance():ItemManager {
            if (itemManager == null) {
                itemManager = ItemManager(mutableListOf())
                return itemManager!!
            }
            return itemManager!!
        }
        fun from(products: List<String>, promotionInfoList: List<String>): ItemManager {
            val t = products.map { item ->
                val itemData = item.split(",")
                val promotionDataList = promotionInfoList.map { it.split(",") }
                getGeneralOrPromotionItem(itemData, promotionDataList)
            }
            itemManager = ItemManager(t.toMutableList())
            return itemManager!!
        }

        private fun getGeneralOrPromotionItem(
            item: List<String>,
            promotionInfoList: List<List<String>>
        ): Item {
            val isPromotion = item[3]

            if (isPromotion == "null") {
                return GeneralItem(item[0], item[1].toInt(), item[2].toInt())
            }

            return PromotionItem(
                item[0],
                item[1].toInt(),
                item[2].toInt(),
                Promotion.from(item[3], promotionInfoList)
            )
        }
    }
}