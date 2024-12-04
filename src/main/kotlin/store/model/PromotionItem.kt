package store.model

data class PromotionItem(
    val name: String,
    val price: Int,
    val quantity: Int,
    val promotion: Promotion
): Item{
    override fun toString(): String {
        return "${name} ${price}원 ${quantity}개 ${promotion}"
    }

    override fun itemMessage() : String {
        return "${name} ${price}원 ${quantity}개 ${promotion}"
    }
}