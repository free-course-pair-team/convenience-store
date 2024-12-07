package store.model

import store.util.toWonFormat

class Receipt {

    fun showReceipt(membershipDiscountAmount: Int, shoppingCart: ShoppingCart): String {
        val s = StringBuilder()
        s.appendLine("===========W 편의점=============")
        s.appendLine("상품명\t\t수량\t금액")
        val groupedShoppingCartItems = groupByShoppingCartItems(shoppingCart.getItems())
        groupedShoppingCartItems.forEach { (name, quantity, price) ->
            s.appendLine("${name}\t\t${quantity}\t${price.toWonFormat()}")
        }
        s.appendLine("===========증 정=============")
        val promotionItems = shoppingCart.getPromotionItems().map { Triple(it.name(), shoppingCart.getPromotionItemQuantity(it), it.price()) }
        promotionItems.forEach { (name, quantity, _) ->
            s.appendLine("${name}\t\t${quantity}")
        }
        s.appendLine("==============================")
        val totalAmountBeforeDiscount = groupedShoppingCartItems.sumOf { it.third }
        val eventDiscountAmount = promotionItems.sumOf { it.third*it.second }
        s.appendLine("총구매액\t\t${groupedShoppingCartItems.sumOf { it.second }}\t${totalAmountBeforeDiscount.toWonFormat()}")

        s.appendLine("행사할인\t\t\t${eventDiscountAmount.toWonFormat()}")
        s.appendLine("멤버십할인\t\t\t${membershipDiscountAmount.toWonFormat()}")
        val spendAmount = totalAmountBeforeDiscount - eventDiscountAmount - membershipDiscountAmount
        s.appendLine("내실돈\t\t\t${spendAmount.toWonFormat()}")

        return s.toString()
    }

    private fun groupByShoppingCartItems(shoppingCartItems: List<Item>) =
        shoppingCartItems.groupBy { it.name() }.map { (key, value) ->
            Triple(
                key,
                value.sumOf { it.quantity() },
//                value.fold(0) { acc, item -> acc + item.price() * item.quantity() }
                value.sumOf { it.price()*it.quantity() }
            )
        }

}