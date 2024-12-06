package store.model

import store.domain.ItemManager

object ShoppingCart {
    var shoppingCartItems = mutableListOf<Item>()

    // 초기화
    fun init(inputProductAndQuantity: List<Pair<String, Int>>) {
        inputProductAndQuantity.forEach { (name, quantity) ->
            val promotionItem = ItemManager.getInstance().findPromotionItem(name)
            val generalItem = ItemManager.getInstance().findItem(name)
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

    fun setPromotionItemQuantity(canAddPromotionItem: List<Pair<PromotionItem, Int>>) {
        canAddPromotionItem.forEach { (promotionItem, quantity) ->
            shoppingCartItems.find { it.name() == promotionItem.name && it is PromotionItem }
                ?.addQuantity(quantity)
        }
    }




}