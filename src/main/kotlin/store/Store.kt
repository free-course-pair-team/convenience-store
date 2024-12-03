package store

class Store(
    private val products: List<Product>,
) {
    fun getProducts() = products.toList()
}