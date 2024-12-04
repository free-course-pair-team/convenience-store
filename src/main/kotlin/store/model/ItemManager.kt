package store.model

class ItemManager(private val items: MutableList<Item>) {
//    private val items = mutableListOf<Item>()

    fun getItems() =
        items.toList()

    fun getItemsMessage(): List<String> {
        return items.map { it.itemMessage() }
    }

    companion object {
        fun from(products: List<String>, promotionInfoList: List<String>): ItemManager {
            val t = products.map { item ->
                val itemData = item.split(",")
                val promotionDataList = promotionInfoList.map { it.split(",") }
                getGeneralOrPromotionItem(itemData, promotionDataList)
            }
            return ItemManager(t.toMutableList())
        }

        private fun getGeneralOrPromotionItem(item: List<String>, promotionInfoList: List<List<String>>): Item {
            val isPromotion = item[3]
            if (isPromotion == "null") return GeneralItem(item[0], item[1].toInt(), item[2].toInt())
            return PromotionItem(item[0], item[1].toInt(), item[2].toInt(), Promotion.from(item[3], promotionInfoList))
        }
    }
}