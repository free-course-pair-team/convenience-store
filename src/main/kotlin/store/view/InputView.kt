package store.view

import camp.nextstep.edu.missionutils.Console

class InputView {

    fun inputProductAndQuantity(): String {
        println(INPUT_PRODUCT_AND_QUANTITY_MSG)
        return Console.readLine()
    }

    companion object {
        const val INPUT_PRODUCT_AND_QUANTITY_MSG = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"
    }
}