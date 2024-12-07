package store.view

import store.domain.ItemManager
import store.model.Stock

class OutputView {

    fun introduceStore(){
        println("안녕하세요. W편의점입니다.\n" +
                "현재 보유하고 있는 상품입니다.")
    }
    fun introduceProducts(stock: Stock) {
        println(stock.toString())
        println()
    }
}