package store.domain

import store.model.PromotionItem
import store.util.retryInput

class PromotionManager {

    // 일치 하는 프로모션 상품을 가지고 추가 프로모션 상품 제공받을 지 여부
    private fun isOfferPromotionProduct(
        inputProduct: PromotionItem,
        inputQuantity: Int,
        promotionStock: Int,
    ): Boolean {
        // TODO: 프로모션 기간 체크
        val total = inputProduct.promotion.buy + inputProduct.promotion.get
//        if (inputQuantity % total != inputProduct.promotion.buy) return false
        if (inputQuantity + inputProduct.promotion.get >= promotionStock) return false
        return inputQuantity % total == inputProduct.promotion.buy
    }

    fun getCanAddOfferPromotionProduct(promotionItems: List<PromotionItem>): List<Pair<PromotionItem, Int>> {
        return promotionItems.filter {
            isOfferPromotionProduct(it, it.quantity, ItemManager.getInstance().findPromotionItem(it.name).quantity)
        }.map { it to it.promotion.get }
    }

}