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

    companion object {
        const val INPUT_PRODUCT_AND_QUANTITY_MSG = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"
        const val INPUT_ASK_ADD_PROMOTION_ITEM_MSG = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)"
    }
}