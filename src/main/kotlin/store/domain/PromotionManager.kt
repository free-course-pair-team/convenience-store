package store.domain

import store.model.PromotionItem
import store.util.retryInput

class PromotionManager {

    // 일치 하는 프로모션 상품을 가지고 추가 프로모션 상품 제공받을 지 여부
    fun isOfferPromotionProduct(
        inputProduct: PromotionItem,
        inputQuantity: Int,
    ): Boolean {
        // TODO: 프로모션 기간 체크
        val total = inputProduct.promotion.buy + inputProduct.promotion.get
        return inputQuantity % total == inputProduct.promotion.buy
    }
}