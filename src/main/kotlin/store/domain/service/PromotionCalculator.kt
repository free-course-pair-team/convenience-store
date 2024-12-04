package store.domain.service

import camp.nextstep.edu.missionutils.DateTimes
import store.domain.entity.*
import store.view.PromotionOptionInputView

class PromotionCalculator(private val store: Store, private val promotionOptionInputView: PromotionOptionInputView) {
    companion object {
        const val ERROR_PROMOTION_MISSED = "[ERROR] 할인대상이지만 할인이 되지 않았습니다"
    }

    fun runPromotion(purchaseProduct: List<PurchaseProduct>): List<PurchaseCompleteProduct> {
        val result = mutableListOf<PurchaseCompleteProduct>()
        purchaseProduct.forEach {
            val products = findPurchaseProduct(it.name)
            if (isPromotionable(products)) { // 프로모션 적용 구매
                val completeProduct = applyPromotionWithNoPromotionOrNot(it.count, products)
                if (completeProduct == null) {
                    println(ERROR_PROMOTION_MISSED)
                } else {
                    result.add(completeProduct)
                }
            } else { //only 일반 구매
                result.add(
                    PurchaseCompleteProduct(
                        it.name,
                        PromotionProduct(promotionCount = 0, noPromotionCount = 0, freeCount = 0),
                        generalProduct = it.count,
                        price = products.get(0).price
                    )
                )
            }
        }
        return result
    }

    fun findPurchaseProduct(name: String): List<Product> {
        return store.getProducts().filter { it.name == name }
    }

    //프로모션, 일반 재고 옵션이 있는 경우
    fun isPromotionable(product: List<Product>): Boolean {
        val promotionName = product.get(0).promotion
        if (product.any { it.promotion != null }) {
            if (promotionName != null && isAvabilablePromotion(promotionName))
                return true
        }
        return false
    }

    //기간을 확인
    fun isAvabilablePromotion(promotionName: String): Boolean {
        val promotion = store.getPromotion().get(promotionName)
        val isAvailableEnd = promotion?.endDate?.isAfter(DateTimes.now().toLocalDate())
        val isAbailableStart = promotion?.startDate?.isBefore(DateTimes.now().toLocalDate())
        if (isAbailableStart == true && isAvailableEnd == true) return true
        return false
    }

    //프로모션 미적용 개수 발생하는 경우(발생안할 수도 있음), 그 외의 경우
    //프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
    fun applyPromotionWithNoPromotionOrNot(
        count: Int,
        product: List<Product>
    ): PurchaseCompleteProduct? { //null을 반환한다는 건 할인되지 않았다는 뜻
        val promotionProduct = product.get(0)
        val buy = store.getPromotion().get(promotionProduct.promotion)?.buy ?: 0
        val get = store.getPromotion().get(promotionProduct.promotion)?.get ?: 0
        if (count >= (buy.plus(get))) {
            if (count > promotionProduct.getQuantity()) {
                val promotionCount = (promotionProduct.getQuantity() / (buy + get)) * buy //2 * 2 = 4
                val freeCount = (promotionProduct.getQuantity() / (buy + get)) * get //2*1 = 2
                val noPromotionCount = promotionProduct.getQuantity() - (promotionCount + freeCount) // 7 - 6 = 1
                val generalCount = count - (promotionCount + freeCount + noPromotionCount) // 10 - (4+2+1) = 3
                var purchaseCompleteProduct = PurchaseCompleteProduct(
                    promotionProduct.name,
                    PromotionProduct(
                        promotionCount = promotionCount,
                        freeCount = freeCount,
                        noPromotionCount = noPromotionCount
                    ),
                    generalCount,
                    promotionProduct.price
                )
                //프로모션 할인 미적용 구매여부 판단 및 묻기
                purchaseCompleteProduct =
                    provideOptionalNoPromotionPurchase(noPromotionCount + generalCount, purchaseCompleteProduct)
                return purchaseCompleteProduct
            } else {
                //프로모션 할인이 안되는 개수(PromotionProduct.미적용 + 일반)알려주기 + 구매의사 묻기
                val promotionCount = (count / (buy + get)) * buy //2 * 2 = 4
                val freeCount = (count / (buy + get)) * get //2*1 = 2
                val noPromotionCount = count - (promotionCount + freeCount) // 6 - 6 = 0
                val generalCount = count - (promotionCount + freeCount + noPromotionCount) // 6 - (4+2+0) = 0
                var purchaseCompleteProduct = PurchaseCompleteProduct(
                    promotionProduct.name,
                    PromotionProduct(
                        promotionCount = promotionCount,
                        freeCount = freeCount,
                        noPromotionCount = noPromotionCount
                    ),
                    generalCount,
                    promotionProduct.price
                )
                //프로모션 할인 미적용 구매여부 판단 및 묻기
                purchaseCompleteProduct =
                    provideOptionalNoPromotionPurchase(noPromotionCount + generalCount, purchaseCompleteProduct)
                return purchaseCompleteProduct
            }
        } else { //구매하려는 수량이 극히 적어서, count <= buy+get인 경우
            //아닌 경우 promotionWithOptionalFreeProduct함수조건과 일치하게 됨
            if (count >= promotionProduct.getQuantity()) { //구매하려는 물량이 프로모션 재고보다 크거나 같아야 프로모션 미적용 물품이 생김
                val promotionCount = 0
                val freeCount = 0
                val noPromotionCount = promotionProduct.getQuantity() //케이스 c. 2
                val generalCount = count - (promotionCount + freeCount + noPromotionCount) //케이스 c. 2 - (0+0+2)
                var purchaseCompleteProduct = PurchaseCompleteProduct(
                    promotionProduct.name,
                    PromotionProduct(
                        promotionCount = promotionCount,
                        freeCount = freeCount,
                        noPromotionCount = noPromotionCount
                    ),
                    generalCount,
                    promotionProduct.price
                )

                //프로모션 할인 미적용 구매여부 판단 및 묻기
                purchaseCompleteProduct =
                    provideOptionalNoPromotionPurchase(noPromotionCount + generalCount, purchaseCompleteProduct)
                return purchaseCompleteProduct
            } else { //프로덕션 미적용 상품이 발생하지 않는 케이스
                return applyPromotionWithOptionalFreeProduct(count, product)
            }
        }
    }

    //2.프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다
    fun applyPromotionWithOptionalFreeProduct(
        count: Int,
        product: List<Product>
    ): PurchaseCompleteProduct? {
        val promotionProduct = product.get(0)
        val buy = store.getPromotion().get(promotionProduct.promotion)?.buy ?: 0
        val get = store.getPromotion().get(promotionProduct.promotion)?.get ?: 0

        if (count < (buy + get)) {
            if (count < promotionProduct.getQuantity()) {
                val promotionCount = buy
                val noPromotionCount = 0
                val generalCount = 0
                var purchaseCompleteProduct = PurchaseCompleteProduct(
                    promotionProduct.name,
                    PromotionProduct(
                        promotionCount = promotionCount,
                        freeCount = 0,
                        noPromotionCount = noPromotionCount
                    ),
                    generalCount,
                    promotionProduct.price
                )
                //증정상품 가져오기 여부 결정
                purchaseCompleteProduct =
                    provideAdditionalFreeProduct(purchaseCompleteProduct = purchaseCompleteProduct)
                return purchaseCompleteProduct
            }
        }
        return applyPromotionOnlySubtractProduct(count, product)
    }

    //3.증정여부, 프로모션 미적용 상품 구매여부 안 물어도 되는 경우
    //극단적으로 구매수량이 적어서, buy보다 작아서 -> 증정이 없어서 증정 안 물어봄 (buy > count)
    fun applyPromotionOnlySubtractProduct(
        count: Int,
        product: List<Product>
    ): PurchaseCompleteProduct? {
        val promotionProduct = product.get(0)
        val buy = store.getPromotion().get(promotionProduct.promotion)?.buy ?: 0

        if (count < buy) {
            val purchaseCompleteProduct = PurchaseCompleteProduct(
                promotionProduct.name,
                PromotionProduct(
                    promotionCount = count,
                    freeCount = 0,
                    noPromotionCount = 0
                ),
                0,
                promotionProduct.price
            )
            return purchaseCompleteProduct
        }
        return null
    }

    //프로모션 할인 미적용 상품 구매여부 묻기
    fun provideOptionalNoPromotionPurchase(
        totalNoPromotionCount: Int,
        purchaseCompleteProduct: PurchaseCompleteProduct
    ): PurchaseCompleteProduct {
        if (totalNoPromotionCount > 0) {
            //살건가?
            val isPurchase = promotionOptionInputView.getIsPurchaseNoPromotionOrNot(
                productName = purchaseCompleteProduct.name,
                totalNoPromotionCount
            )
            if (!isPurchase) { //안산다, 프로모션 미적용, 일반 상품을 빼버린다
                return purchaseCompleteProduct.copy(
                    promotionProduct = purchaseCompleteProduct.promotionProduct.copy(noPromotionCount = 0),
                    generalProduct = 0
                )
            }
        }
        return purchaseCompleteProduct
    }

    //고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다
    fun provideAdditionalFreeProduct(purchaseCompleteProduct: PurchaseCompleteProduct): PurchaseCompleteProduct {
        //증정받을건가?
        val isAddFreeProduct =
            promotionOptionInputView.getAdditionalFreeProductOrnot(productName = purchaseCompleteProduct.name)
        if (isAddFreeProduct) { //산다
            return purchaseCompleteProduct.copy(
                promotionProduct = purchaseCompleteProduct.promotionProduct.copy(freeCount = 1),
            )
        }
        return purchaseCompleteProduct
    }
}
