package store

import store.model.Item
import store.model.ItemManager
import store.util.FileReader
import store.util.Validator
import store.util.retryInput
import store.view.InputView
import store.view.OutputView

class Controller(
    private val fileReader: FileReader,
    private val outputView: OutputView,
    private val inputView: InputView,
    private val validator: Validator
) {


    fun run() {
        val (products, promotions) = readProductsAndPromotionsFile()
        val itemManager = ItemManager.from(products, promotions)

        outputView.introduceStore()
        outputView.introduceProducts(itemManager)

        inputProductAndQuantity(itemManager.getItems()) {
            inputView.inputProductAndQuantity()
        }
    }


    private fun readProductsAndPromotionsFile(): Pair<List<String>, List<String>> =
        fileReader.readProducts() to fileReader.readPromotions()

    private fun inputProductAndQuantity(items: List<Item>, input: () -> String) = retryInput {
        val inputProductAndQuantityList = input().split(",")
        inputProductAndQuantityList.forEach {
            validator.validateInputProductAndQuantity(it, items)
        }
    }

}