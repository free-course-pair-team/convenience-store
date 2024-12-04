package store

import store.model.ItemManager
import store.util.FileReader

class Controller(private val fileReader: FileReader) {


    fun run() {
        val (products, promotions) = readProductsAndPromotionsFile()
        val itemManager = ItemManager.from(products, promotions)
    }


    private fun readProductsAndPromotionsFile():Pair<List<String>, List<String>> =
        fileReader.readProducts() to fileReader.readPromotions()


}