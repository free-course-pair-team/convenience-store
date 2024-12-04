package store.view

import store.domain.entity.Product
import store.domain.entity.PurchaseCompleteProduct
import java.text.DecimalFormat

class OutputView {
    fun printStart() {
        println("안녕하세요. W편의점입니다.")
    }

    fun printProducts(products: List<Product>) {
        println("현재 보유하고 있는 상품입니다.")
        products.forEach { product ->
            println(
                "- ${product.name} ${product.price.wonFormat()}원 ${
                    product.getQuantity().zeroToSoldOut()
                } ${product.promotion ?: ""}"
            )
        }
    }

    fun printReceiptProductInfo(purchaseProductInfos: List<PurchaseCompleteProduct>) {
        val promotionTotalCount =
            purchaseProductInfos.map { it.promotionProduct.promotionCount + it.promotionProduct.noPromotionCount + it.promotionProduct.freeCount }
        println("===========W 편의점=============")
        println("상품명\t\t수량\t금액")
        purchaseProductInfos.forEachIndexed() { index, purchaseProductInfo ->
            val productCount = purchaseProductInfo.generalProduct + promotionTotalCount[index]
            println("${purchaseProductInfo.name} ${productCount} ${(productCount * purchaseProductInfo.price).wonFormat()}")
        }
    }

    fun printReceiptPromotionInfo(purchaseProductInfos: List<PurchaseCompleteProduct>) {
        val promotionProduct = purchaseProductInfos.filter { it.promotionProduct.freeCount > 0 }
        if (promotionProduct.isEmpty()) return
        println("===========증\t정=============")
        promotionProduct.forEach { promotionInfo ->
            println("${promotionInfo.name} ${promotionInfo.promotionProduct.freeCount}")
        }
    }

    fun printReceiptTotalInfo(purchaseProductInfos: List<PurchaseCompleteProduct>, membershipResult: Int) {
        val promotionTotalCount =
            purchaseProductInfos.map { it.promotionProduct.promotionCount + it.promotionProduct.noPromotionCount + it.promotionProduct.freeCount }
        val totalCount = purchaseProductInfos.sumOf { it.generalProduct } + promotionTotalCount.sum()
        val totalPrice = purchaseProductInfos.mapIndexed { index, purchaseProductInfo ->
            purchaseProductInfo.price * (purchaseProductInfo.generalProduct + promotionTotalCount[index])
        }.sum()
        val promotionDiscount = purchaseProductInfos.sumOf { it.promotionProduct.freeCount * it.price }
        val payment = totalPrice - promotionDiscount - membershipResult

        println("==============================")
        println("총구매액 $totalCount ${totalPrice.wonFormat()}")
        println("행사할인 -${promotionDiscount.wonFormat()}")
        println("멤버십할인 -${membershipResult.wonFormat()}")
        println("내실돈 ${payment.wonFormat()}")
    }

    private fun Int.wonFormat(): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(this)
    }

    private fun Int.zeroToSoldOut(): String {
        if (this == 0) return "재고 없음"
        return this.toString() + "개"
    }


}
