package store.domain.entity

class Store(
    private val products: List<Product>,
    private val promotion: Map<String, Promotion>
) {
    fun getProducts() = products.toList()
    fun getPromotion() = promotion
}
