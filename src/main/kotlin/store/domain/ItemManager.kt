package store.domain

import store.model.GeneralItem
import store.model.Item
import store.model.Promotion
import store.model.PromotionItem
import store.model.Stock

class ItemManager(private val stock: Stock) {

    fun findPromotionItem2(name: String): PromotionItem? {
        val item = requireNotNull(stock.items.find { it.name() == name })
        if (item is PromotionItem) {
            return item
        }
        return null
    }

    fun findPromotionItem(name: String): PromotionItem =
        requireNotNull(stock.items.filterIsInstance<PromotionItem>().find { it.name() == name })


    fun findItem(name: String): GeneralItem? {
        return stock.items.filter { it.name() == name }.find { it is GeneralItem } as GeneralItem?


    }

    companion object {
        fun getGeneralOrPromotionItem(
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