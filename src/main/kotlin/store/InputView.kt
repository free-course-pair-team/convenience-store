package store

import camp.nextstep.edu.missionutils.Console

class InputView {

    fun readPurchaseProducts(products: List<Product>): List<PurchaseProduct> {
        val purchaseProductsInput = Console.readLine()
        val purchaseProducts = purchaseProductsInput.split(",").map { productInput ->
            require(productInput.first() == '[' && productInput.last() == ']') { "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요." }
            require(productInput.split("-").size == 2) { "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요." }
            val (name, countInput) = productInput.removeSurrounding("[", "]").split("-")
            val count = requireNotNull(countInput.toIntOrNull()) { "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요." }
            requireNotNull(products.find { it.name == name }) { "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요." }
            require(products.filter { it.name == name }
                .sumOf { it.quantity } >= count) { "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요." }

            PurchaseProduct(name, count)
        }
        return purchaseProducts
    }
}