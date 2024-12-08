package store.domain

import store.model.GeneralItem
import store.model.Item
import store.model.Promotion
import store.model.PromotionItem
import store.model.Stock

class ItemManager(private val stock: Stock) {

    fun findPromotionItem(name: String): PromotionItem =
        requireNotNull(stock.items.filterIsInstance<PromotionItem>().find { it.name() == name })


    fun findGeneralItem(name: String): GeneralItem? {
        return stock.items.filter { it.name() == name }.find { it is GeneralItem } as GeneralItem?
    }

    fun takeOutExistingStock(item: Item) {

        stock.items.forEach{
            if(it.name() == item.name() && it is GeneralItem) it.takeOutQuantity(item.quantity())
            if(it.name() == item.name() && it is PromotionItem) it.takeOutQuantity(item.quantity())
        }
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