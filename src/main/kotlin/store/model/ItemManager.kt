package store.model

class ItemManager() {
    private val items = mutableListOf<Item>()



    companion object {
        fun from(items: List<String>, promotionInfoList: List<String>) {
            items.forEach { item ->
                val itemData = item.split(",")
                val promotionDataList = promotionInfoList.map { it.split(",") }
                getGeneralOrPromotionItem(itemData, promotionDataList)
            }
            println(items)
        }

        private fun getGeneralOrPromotionItem(item: List<String>, promotionInfoList: List<List<String>>): Item {
            val isPromotion = item[3]
            if (isPromotion == "null") return GeneralItem(item[0], item[1].toInt(), item[2].toInt())
            return PromotionItem(item[0], item[1].toInt(), item[2].toInt(), Promotion.from(item[3], promotionInfoList))
        }
    }
}