package store.model

data class PromotionItem(
    val name: String,
    val price: Int,
    var quantity: Int,
    val promotion: Promotion
): Item{
    override fun itemMessage() : String {
        return "${name} ${price}원 ${quantity}개 ${promotion.name}"
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
}