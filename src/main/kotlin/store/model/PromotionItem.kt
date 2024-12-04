package store.model

data class PromotionItem(
    val name: String,
    val price: Int,
    val quantity: Int,
    val promotion: Promotion
): Item