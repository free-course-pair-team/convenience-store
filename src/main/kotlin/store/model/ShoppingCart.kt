package store.model

import store.domain.ItemManager

object ShoppingCart {
    var shoppingCartItems = mutableListOf<Item>()

    // 초기화
    fun init(inputProductAndQuantity: List<Pair<String, Int>>, itemManager: ItemManager) {
        inputProductAndQuantity.forEach { (name, quantity) ->
            val promotionItem = itemManager.findPromotionItem2(name)
            val generalItem = itemManager.findItem(name)
            if (promotionItem == null && generalItem != null) {
                shoppingCartItems.add(GeneralItem(name, generalItem.price, quantity))
            } else if (promotionItem != null && generalItem == null){
                shoppingCartItems.add(PromotionItem(name, promotionItem.price, quantity, promotionItem.promotion))
            } else if (promotionItem != null && generalItem != null){
                if (quantity <= promotionItem.quantity) {
                    shoppingCartItems.add(PromotionItem(name, promotionItem.price, quantity, promotionItem.promotion))
                } else {
                    shoppingCartItems.add(PromotionItem(name, promotionItem.price, promotionItem.quantity, promotionItem.promotion))
                    shoppingCartItems.add(GeneralItem(name, generalItem.price, quantity-promotionItem.quantity))
                }
            }
        }

    }

    fun getPromotionItems() =
        shoppingCartItems.filterIsInstance<PromotionItem>()

    fun getGeneralItems() =
        shoppingCartItems.filterIsInstance<GeneralItem>()

    fun getNotApplyPromotionItemQuantity(promotionItem: PromotionItem) =
        promotionItem.quantity % (promotionItem.promotion.buy + promotionItem.promotion.get)

    fun getNotApplyPromotionItemQuantity(name: String) =
        shoppingCartItems.filterIsInstance<PromotionItem>().find { it.name == name }?.let {
            getNotApplyPromotionItemQuantity(it)
        } ?: 0

    fun getPromotionItemQuantity(promotionItem: PromotionItem) =
        promotionItem.quantity / (promotionItem.promotion.buy + promotionItem.promotion.get)

    fun getGeneralItemQuantity(name : String) =
        shoppingCartItems.filterIsInstance<GeneralItem>().find { it.name == name }?.quantity()  ?:0

    fun addPromotionItemQuantity(canAddPromotionItem: List<Pair<PromotionItem, Int>>) {
        canAddPromotionItem.forEach { (promotionItem, quantity) ->
            shoppingCartItems.find { it.name() == promotionItem.name && it is PromotionItem }
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