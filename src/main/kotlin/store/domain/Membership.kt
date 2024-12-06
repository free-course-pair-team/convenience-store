package store.domain

import store.model.GeneralItem
import store.model.ShoppingCart

class Membership() {

    fun getMemberShipDiscountAmount(): Int =
        ShoppingCart.getGeneralItems()
            .sumOf { item -> calculateMembership(item) }.toInt().coerceAtMost(8_000)

    private fun calculateMembership(item: GeneralItem) =
        item.price * (ShoppingCart.getGeneralItemQuantity(item.name) + ShoppingCart.getNotApplyPromotionItemQuantity2(
            item.name
        )) * 0.3
}