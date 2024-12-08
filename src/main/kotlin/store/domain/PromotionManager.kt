package store.domain

import camp.nextstep.edu.missionutils.DateTimes
import store.model.GeneralItem
import store.model.Item
import store.model.PromotionItem
import store.util.retryInput
import java.time.LocalDate
import java.time.LocalDateTime

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
        (item.quantity() % (item.promotion.buy + item.promotion.get) != 0)


    fun getCanAddOfferPromotionProduct(promotionItems: List<PromotionItem>, itemManager: ItemManager): List<Pair<PromotionItem, Int>> {
        return promotionItems.filter {
            isOfferPromotionProduct(
                it,
                it.quantity(),
                itemManager.findPromotionItem(it.name()).quantity()
            )
        }.map { it to it.promotion.get }
    }

    fun isWithInPromotionPeriod(item: Item): Boolean {
        if (item is GeneralItem) return true
        val promotionItem = item as PromotionItem
        val current = DateTimes.now()
        val startDate = promotionItem.promotion.startDate.split("-").map { it.toInt() }
        val endDate = promotionItem.promotion.endDate.split("-").map { it.toInt() }
        if (current.year !in startDate[0]..endDate[0]) return false
        if (current.monthValue !in startDate[1]..endDate[1]) return false
        if (current.dayOfMonth !in startDate[2]..endDate[2]) return false
        return true
    }

}