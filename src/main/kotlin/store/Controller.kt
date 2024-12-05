package store

import store.model.Item
import store.domain.ItemManager
import store.domain.PromotionManager
import store.model.PromotionItem
import store.util.FileReader
import store.util.Validator
import store.util.retryInput
import store.view.InputView
import store.view.OutputView

class Controller(
    private val fileReader: FileReader,
    private val outputView: OutputView,
    private val inputView: InputView,
    private val validator: Validator,
    private val promotionManager: PromotionManager,
) {


    fun run() {
        val (products, promotions) = readProductsAndPromotionsFile()
        val itemManager = ItemManager.from(products, promotions)

        outputView.introduceStore()
        outputView.introduceProducts(itemManager)

        val validatedProductAndQuantity = inputProductAndQuantity {
            inputView.inputProductAndQuantity()
        }
        askOfferPromotionProduct(validatedProductAndQuantity)
    }


    private fun readProductsAndPromotionsFile(): Pair<List<String>, List<String>> =
        fileReader.readProducts() to fileReader.readPromotions()

    private fun inputProductAndQuantity(input: () -> String) = retryInput {
        val inputProductAndQuantityList = input().split(",")
        inputProductAndQuantityList.map {
            validator.validateInputProductAndQuantity(it, ItemManager.getInstance().getItems())
        }
    }

    private fun askOfferPromotionProduct(
        productAndQuantity: List<Pair<String, Int>>,
    ) {
        val canOfferPromotionProduct = productAndQuantity.filter { ItemManager.getInstance().findPromotionItem(it.first) != null }.filter {
            promotionManager.isOfferPromotionProduct(
                it.first,
                it.second,
                ItemManager.getInstance().findPromotionItem(it.first)!!
            )
        }
        canOfferPromotionProduct.map { it.first }.forEach {
            println(it)
        }
    }

}