package store.domain.entity


class Checkout {
    fun membershipDiscount(promotionResult: List<PurchaseCompleteProduct>): Int {
        val membershipAmount = promotionResult.sumOf { membershipCount ->
            membershipCount.price * (membershipCount.generalProduct + membershipCount.promotionProduct.noPromotionCount)
        }
        return minOf((membershipAmount * 0.3).toInt(), 8000)
    }

}
