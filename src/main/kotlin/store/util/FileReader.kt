package store.util

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class FileReader {

    fun readProducts(): List<String> {
        val file = File(PATH_PRODUCTS)
        val reader = BufferedReader(FileReader(file, Charsets.UTF_8))
        return reader.readLines().drop(1)
    }

    fun readPromotions(): List<String> {
        val file = File(PATH_PROMOTIONS)
        val reader = BufferedReader(FileReader(file, Charsets.UTF_8))
        return reader.readLines().drop(1)
    }


    companion object {
        const val PATH_PRODUCTS = "src/main/resources/products.md"
        const val PATH_PROMOTIONS = "src/main/resources/promotions.md"
    }
}