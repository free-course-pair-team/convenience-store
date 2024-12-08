package store.view

import camp.nextstep.edu.missionutils.Console

class InputView {

    fun inputProductAndQuantity(): String {
        println(INPUT_PRODUCT_AND_QUANTITY_MSG)
        return Console.readLine()
    }

    fun inputAskAddPromotionItem(name: String, quantity: Int): String {
        println(INPUT_ASK_ADD_PROMOTION_ITEM_MSG.format(name, quantity))
        return Console.readLine()
    }

    fun inputAskAddNotApplyPromotionItem(name: String, quantity: Int) : String{
        println(INPUT_ASK_ADD_NOT_APPLY_PROMOTION_ITEM_MSG.format(name, quantity))
        return Console.readLine()
    }

    fun inputAskTakeMembership() : String{
        println(INPUT_ASK_TAKE_MEMBERSHIP_MSG)
        return Console.readLine()
    }

    fun inputAskRepurchaseItem() : String{
        println(INPUT_ASK_REPURCHASE_ITEM_MSG)
        return Console.readLine()
    }

    companion object {
        const val INPUT_PRODUCT_AND_QUANTITY_MSG = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"
        const val INPUT_ASK_ADD_PROMOTION_ITEM_MSG = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
        const val INPUT_ASK_ADD_NOT_APPLY_PROMOTION_ITEM_MSG ="현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)"
        const val INPUT_ASK_TAKE_MEMBERSHIP_MSG ="멤버십 할인을 받으시겠습니까? (Y/N)"
        const val INPUT_ASK_REPURCHASE_ITEM_MSG = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)"
    }
}