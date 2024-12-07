package store.domain

import store.model.PromotionItem
import store.util.retryInput

class PromotionManager {

    // 프로모션 상품을 추가로 받을 수 있는 경우, 추가 프로모션 상품 제공받을 지 여부
    private fun isOfferPromotionProduct(
        inputProduct: PromotionItem,
        inputQuantity: Int,
        promotionStock: Int,
    ): Boolean {
        // TODO: 프로모션 기간 체크
        val total = inputProduct.promotion.buy + inputProduct.promotion.get
        if (inputQuantity + inputProduct.promotion.get >= promotionStock) return false
        return inputQuantity % total == inputProduct.promotion.buy
    }

    // 프로모션 적용 불가능한 상품의 수량이 존재하는지 확인
    fun isExistApplyPromotionProductQuantity(item: PromotionItem): Boolean =
        item.quantity() % (item.promotion.buy + item.promotion.get) != 0


    fun getCanAddOfferPromotionProduct(promotionItems: List<PromotionItem>, itemManager: ItemManager): List<Pair<PromotionItem, Int>> {
        return promotionItems.filter {
            isOfferPromotionProduct(
                it,
                it.quantity(),
                itemManager.findPromotionItem(it.name()).quantity()
            )
        }.map { it to it.promotion.get }
    }

}