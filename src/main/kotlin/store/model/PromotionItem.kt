package store.model

import store.util.toWonFormat

data class PromotionItem(
    private val name: String,
    private val price: Int,
    private var quantity: Int,
    val promotion: Promotion
): Item{
    private fun judgeTheQuantity(): String {
        if(quantity == 0) return "재고 없음"
        return quantity.toString() + "개"
    }
    override fun itemMessage() : String {
        return "- ${name} ${price.toWonFormat()}원 ${judgeTheQuantity()} ${promotion.name}"
    }

    override fun name(): String = name
    override fun quantity(): Int = quantity
    override fun price(): Int = price
    override fun addQuantity(q: Int) {
        quantity += q
    }

    override fun takeOutQuantity(q: Int) {
        quantity -= q
    }
    override fun setQuantity(q: Int) = apply {
        quantity = q
    }
}