package store.view

import store.model.ItemManager

class OutputView {

    fun introduceStore(){
        println("안녕하세요. W편의점입니다.\n" +
                "현재 보유하고 있는 상품입니다.")
    }
    fun introduceProducts(itemManager: ItemManager) {
        println(itemManager.getItemsMessage())
        println()
    }
}