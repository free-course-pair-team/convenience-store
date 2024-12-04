package store.domain.entity

data class Product(
    val name: String,
    val price: Int,
    private var quantity: Int,
    val promotion: String?
) {
    fun updateQuantity(count: Int): Int {
        val buyCount = count.coerceAtMost(quantity)
        quantity -= buyCount
        return buyCount
    }

    fun getQuantity() = quantity
}
