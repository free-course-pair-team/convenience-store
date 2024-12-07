package store.model

import store.domain.ItemManager

class ShoppingCart(private var shoppingCartItems: MutableList<Item>) {

    init {
        println("shoppingCartItems: $shoppingCartItems")
    }

    fun getItems() = shoppingCartItems.toList()
    fun getPromotionItems() =
        shoppingCartItems.filterIsInstance<PromotionItem>()

    fun getGeneralItems() =
        shoppingCartItems.filterIsInstance<GeneralItem>()

    fun getNotApplyPromotionItemQuantity(promotionItem: PromotionItem) =
        promotionItem.quantity() % (promotionItem.promotion.buy + promotionItem.promotion.get)

    fun getNotApplyPromotionItemQuantity(name: String) =
        shoppingCartItems.filterIsInstance<PromotionItem>().find { it.name() == name }?.let {
            getNotApplyPromotionItemQuantity(it)
        } ?: 0

    fun getPromotionItemQuantity(promotionItem: PromotionItem) =
        promotionItem.quantity() / (promotionItem.promotion.buy + promotionItem.promotion.get)

    fun getGeneralItemQuantity(name : String) =
        shoppingCartItems.filterIsInstance<GeneralItem>().find { it.name() == name }?.quantity()  ?:0

    fun addPromotionItemQuantity(canAddPromotionItem: List<Pair<PromotionItem, Int>>) {
        canAddPromotionItem.forEach { (promotionItem, quantity) ->
            shoppingCartItems.find { it.name() == promotionItem.name() && it is PromotionItem }
                ?.addQuantity(quantity)
        }
    }
    fun takeOutNotApplyPromotionItemQuantity(name: String, takeOutQuantities: List<Int>){
        shoppingCartItems.filter { it.name() == name }.forEachIndexed { index, item ->
            item.takeOutQuantity(takeOutQuantities[index])
        }
        organizeShoppingCartItems()
    }

    private fun organizeShoppingCartItems() {
        shoppingCartItems.removeAll { it.quantity() == 0 }
    }
}