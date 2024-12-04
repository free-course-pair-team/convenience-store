package store

import store.model.ItemManager
import store.util.FileReader
import store.view.OutputView

class Controller(private val fileReader: FileReader,private val outputView : OutputView) {


    fun run() {
        val (products, promotions) = readProductsAndPromotionsFile()
        val itemManager = ItemManager.from(products, promotions)
        println(itemManager.getItems())

        outputView.introduceStore()
        outputView.introduceProducts(itemManager)

    }


    private fun readProductsAndPromotionsFile():Pair<List<String>, List<String>> =
        fileReader.readProducts() to fileReader.readPromotions()


}