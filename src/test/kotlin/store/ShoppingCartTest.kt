package store

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import store.domain.ShoppingCartFactory
import store.model.Stock
import store.util.FileReader
import store.util.Validator

class ShoppingCartTest {
    var stock: Stock? = null
    val validator = Validator()

    @BeforeEach
    fun init() {
        val fileReader = FileReader()
        val (products, promotions) = fileReader.readProducts() to fileReader.readPromotions()
        stock = Stock.from(products, promotions)
    }

    @ParameterizedTest()
    @ValueSource(
        strings = ["[콜라-6]"]
    )
    @DisplayName("구입할 수량 장바구니에 제대로 담겼는지 확인")
    fun isExistInputProductInShoppingCart(input: String) {
        val validatedProductAndQuantity = input.split(",").map {
            validator.validateInputProductAndQuantity(it, Stock.getInstance().getItems())
        }
        val shoppingCart = ShoppingCartFactory().generate(validatedProductAndQuantity, stock!!)
        val item = shoppingCart.getItems()
        println("shoppingCart: $item")
        assertEquals(item.find { it.name() == "콜라" }?.quantity(), 2)
    }
}