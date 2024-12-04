package store.view

import camp.nextstep.edu.missionutils.Console

class PromotionOptionInputView {
    fun getIsPurchaseNoPromotionOrNot(productName: String, totalNoPromotionCount: Int): Boolean {
        println("현재 ${productName} ${totalNoPromotionCount}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)")

        while (true) {
            try {
                val answer = Console.readLine().replace(" ", "").uppercase()
                when (answer) {
                    "Y" -> return true
                    "N" -> return false
                    else -> throw IllegalArgumentException("[ERROR] Y/N 을 입력해주세요")
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun getAdditionalFreeProductOrnot(productName: String): Boolean {
        println("현재 ${productName}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)")
        while (true) {
            try {
                val answer = Console.readLine().replace(" ", "").uppercase()
                when (answer) {
                    "Y" -> return true
                    "N" -> return false
                    else -> throw IllegalArgumentException("[ERROR] Y/N 을 입력해주세요")
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}
