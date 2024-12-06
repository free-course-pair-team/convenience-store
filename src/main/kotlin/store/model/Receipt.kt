package store.model

import store.domain.Membership
import store.util.minusFormat
import store.util.toWonFormat

class Receipt {

//    ===========W 편의점=============
//    상품명		수량	금액
//    콜라		3 	3,000
//    에너지바 		5 	10,000
//    ===========증	정=============
//    콜라		1
//    ==============================
//    총구매액		8	13,000
//    행사할인			-1,000
//    멤버십할인			-3,000
//    내실돈			 9,000

    fun showReceipt(membershipDiscountAmount: Int): String {
        val s = StringBuilder()
        s.appendLine("===========W 편의점=============")
        s.appendLine("상품명\t\t수량\t금액")
        val groupedShoppingCartItems = groupByShoppingCartItems(ShoppingCart.shoppingCartItems)
        groupedShoppingCartItems.forEach { (name, quantity, price) ->
            s.appendLine("${name}\t\t${quantity}\t${price.toWonFormat()}")
        }
        s.appendLine("===========증 정=============")
        val promotionItems = ShoppingCart.getPromotionItems().map { Triple(it.name, ShoppingCart.getPromotionItemQuantity(it), it.price) }
        promotionItems.forEach { (name, quantity, _) ->
            s.appendLine("${name}\t\t${quantity}")
        }
        s.appendLine("==============================")
        val totalAmountBeforeDiscount = groupedShoppingCartItems.sumOf { it.third }
        val eventDiscountAmount = promotionItems.sumOf { it.third }
        s.appendLine("총구매액\t\t${groupedShoppingCartItems.sumOf { it.second }}\t${totalAmountBeforeDiscount.toWonFormat()}")

        s.appendLine("행사할인\t\t\t${eventDiscountAmount.minusFormat()}")
        s.appendLine("멤버십할인\t\t\t${membershipDiscountAmount.minusFormat()}")
        val spendAmount = totalAmountBeforeDiscount - eventDiscountAmount - membershipDiscountAmount
        s.appendLine("내실돈\t\t\t${spendAmount.toWonFormat()}")

        return s.toString()
    }

    private fun groupByShoppingCartItems(shoppingCartItems: List<Item>) =
        shoppingCartItems.groupBy { it.name() }.map { (key, value) ->
            Triple(
                key,
                value.sumOf { it.quantity() },
                value.fold(0) { acc, item -> acc + item.price() * item.quantity() }
            )
        }

}