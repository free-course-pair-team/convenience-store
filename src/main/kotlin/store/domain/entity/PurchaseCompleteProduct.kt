package store.domain.entity

data class PurchaseCompleteProduct(
    val name: String,
    val promotionProduct: PromotionProduct,
    val generalProduct: Int,
    val price: Int
) {
    fun getTotalPurchaseCount() =
        promotionProduct.noPromotionCount + promotionProduct.freeCount + promotionProduct.promotionCount + generalProduct
}

data class PromotionProduct(
    val promotionCount: Int, //프로모션 적용
    val noPromotionCount: Int, //미적용
    val freeCount: Int //증정
)
