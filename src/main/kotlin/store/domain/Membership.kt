package store.domain

import store.model.GeneralItem
import store.model.ShoppingCart

class Membership() {

    fun getMemberShipDiscountAmount(shoppingCart: ShoppingCart): Int =
        shoppingCart.getGeneralItems()
            .sumOf { item -> calculate(item, shoppingCart) }.toInt().coerceAtMost(8_000)

    private fun calculate(item: GeneralItem, shoppingCart: ShoppingCart) =
        item.price() * (shoppingCart.getGeneralItemQuantity(item.name()) + shoppingCart.getNotApplyPromotionItemQuantity(
            item.name()
        )) * 0.3
}