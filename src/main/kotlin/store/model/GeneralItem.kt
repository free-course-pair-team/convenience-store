package store.model

data class GeneralItem(
    val name: String,
    val price: Int,
    var quantity: Int,
): Item{
    fun judgeTheQuantity(): String {
        if(quantity == 0) return "재고 없음"
        return quantity.toString() + "개"
    }

    override fun itemMessage() : String {
        return "${name} ${price}원 ${judgeTheQuantity()}"
    }

    override fun name(): String = name
    override fun quantity(): Int = quantity
    override fun addQuantity(q: Int) {
        TODO("Not yet implemented")
    }

    override fun takeOutQuantity(q: Int) {
        quantity -= q
    }

}