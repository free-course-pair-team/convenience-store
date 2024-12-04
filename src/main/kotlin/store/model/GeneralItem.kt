package store.model

data class GeneralItem(
    val name: String,
    val price: Int,
    val quantity: Int,
): Item{
    override fun toString(): String {
        return "${name} ${price}원 ${quantity}개 ${notHaveProduct()}"
    }
    fun notHaveProduct(): String {
        if(quantity == 0) return "재고 없음"
        return quantity.toString() + "개"
    }

    override fun itemMessage() : String {
        return "${name} ${price}원 ${quantity}개 ${notHaveProduct()}"
    }
}