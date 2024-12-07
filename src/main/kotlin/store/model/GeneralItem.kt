package store.model

import store.util.toWonFormat

data class GeneralItem(
    private val name: String,
    private val price: Int,
    private var quantity: Int,
): Item{
    private fun judgeTheQuantity(): String {
        if(quantity == 0) return "재고 없음"
        return quantity.toString() + "개"
    }

    override fun itemMessage() : String {
        return "- ${name} ${price.toWonFormat()}원 ${judgeTheQuantity()}"
    }

    override fun name(): String = name
    override fun quantity(): Int = quantity
    override fun price(): Int = price

    override fun addQuantity(q: Int) {
        TODO("Not yet implemented")
    }

    override fun takeOutQuantity(q: Int) {
        quantity -= q
    }

    override fun setQuantity(q: Int) {
        quantity = q
    }

}