package store.domain

import store.model.GeneralItem
import store.model.Item
import store.model.Promotion
import store.model.PromotionItem
import store.model.Stock

class ItemManager(private val stock: Stock) {

    fun findPromotionItem(name: String): PromotionItem =
        requireNotNull(stock.getItems().filterIsInstance<PromotionItem>().find { it.name() == name })


    fun findGeneralItem(name: String): GeneralItem? {
        return stock.getItems().filter { it.name() == name }.find { it is GeneralItem } as GeneralItem?
    }

    fun takeOutExistingStock(shoppingCartItems: List<Item>) {
        shoppingCartItems.forEach { item ->
            var remainingQuantity = item.quantity()
            for (stockItem in stock.getItems().filter { it.name() == item.name() }) {
                if (remainingQuantity > stockItem.quantity()) {
                    stockItem.setQuantity(0)
                    remainingQuantity -= stockItem.quantity()
                    continue
                }
                if (remainingQuantity > 0) {
                    stockItem.apply { takeOutQuantity(remainingQuantity) }
                    remainingQuantity = 0
                }
            }
        }


//        stock.items.forEach{
//            if(it.name() == item.name() && item is GeneralItem) {
//                it.takeOutQuantity(item.quantity())
//                println("item: $item")
//            }
//            if(it.name() == item.name() && item is PromotionItem) {
//                it.takeOutQuantity(item.quantity())
//                println("item: $item")
//            }
//        }
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