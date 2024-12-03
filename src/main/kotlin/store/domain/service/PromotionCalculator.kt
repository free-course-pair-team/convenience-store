package store.domain.service

import camp.nextstep.edu.missionutils.DateTimes
import store.domain.entity.*
import java.util.Date

class PromotionCalculator(private val store:Store) {
    fun runPromotion(purchaseProduct: List<PurchaseProduct>, store:Store):List<PurchaseCompleteProduct>{
        return purchaseProduct.map {
            val products = findPurchaseProduct(it.name)
            var result = PurchaseCompleteProduct(name = products.get(0).name, PromotionProduct(0,0,0),0,products.get(0).price )
            if(isPromotionable(products)){ // 프로모션 적용 구매
            //1.프로모션 적용 상품재고, 일반 상품재고, 프로모션 buy,get <- it.count비교
                //프로모션 미적용 개수 발생하는 경우
                //프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
                //케이스 1. 프로모션 7, 일반 10, buy2, get1
                // it.count == 10
                //프로모션(프로모션 적용 4개, 프로모션 미적용 1개 ,증정 2개), 일반 3개
                //프로모션 2+1, 2세트 가능
                //프로모션 할인이 안되는 개수(PromotionProduct.미적용 + 일반)알려주기 + 구매의사 묻기

                //케이스 4. 프로모션 2 일반 3, buy 2 get1
                //it.count = 2
                //프로모션(적용0, 미적용2, 증정0), 일반 0
                //프로모션 할인이 안되는 개수(PromotionProduct.미적용 + 일반)알려주기 + 구매의사 묻기

            //2.프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다
                // 주문물량 < (buy+get)
                //케이스 2. 프로모션 9, 일반 0, buy1, get1
                // it.count == 1
                //증정 받을건지 질의
                //프로모션 (적용 1개, 미적용 0개, 증정 1개), 일반 0개

                //케이스 3. 프로모션 3 일반 3, buy 2 get1
                //it.count = 2
                //증정여부 질의
                //프로모션(적용2, 미적용0,증정1), 일반 0


            //3.증정여부 안 물어도 되는 경우
                //주문물량 <= 프로모션 재고 and 프로모션 재고 < buy
                //케이스 5. 프로모션 1 일반 3, buy 2 get1
                //it.count = 1
                //프로모션 (적용1, 미적용0, 증정0), 일반 0
            }
            else{ //only 일반 구매
                result.copy(generalProduct = it.count)
            }
            result
        }
    }

    fun findPurchaseProduct(name:String):List<Product>{
        return store.getProducts().filter { it.name == name }
    }

    //프로모션, 일반 재고 옵션이 있는 경우
    fun isPromotionable(product: List<Product>):Boolean{
        val promotionName = product.get(0).promotion
        if (product.any{ it.promotion != null}){
            if(promotionName != null && isAvabilablePromotion(promotionName))
                return true
        }
        return false
    }
    //기간을 확인
    fun isAvabilablePromotion(promotionName:String):Boolean{
        val promotion = store.getPromotion().get(promotionName)
        val isAvailableEnd = promotion?.endDate?.isAfter(DateTimes.now().toLocalDate())
        val isAbailableStart = promotion?.startDate?.isBefore(DateTimes.now().toLocalDate())
        if(isAbailableStart == true && isAvailableEnd == true) return true
        return false
    }
}
