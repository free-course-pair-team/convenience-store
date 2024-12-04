package store.domain.entity

class Store(
    private val products: List<Product>,
    private val promotion: Map<String, Promotion>
) {
    fun getProducts() = products.toList()
    fun getPromotion() = promotion

    fun updateProducts(purchaseCompleteProducts: List<PurchaseCompleteProduct>) {
        purchaseCompleteProducts.forEach { purchaseCompleteProduct ->
            val purchaseProducts = products.filter { purchaseCompleteProduct.name == it.name }
            var currentTotalPurchaseCount = purchaseCompleteProduct.getTotalPurchaseCount()
            purchaseProducts.forEach { product ->
                val buyCount = product.updateQuantity(currentTotalPurchaseCount)
                currentTotalPurchaseCount -= buyCount
            }
        }
    }
}
