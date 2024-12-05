package store.util

import store.model.Item

class Validator {

    fun validateInputProductAndQuantity(input: String, items: List<Item>) {
        require(input[0] == '[' && input[input.length - 1] == ']') {
            "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."
        }
        require(input.split("-").size == 2) {
            "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."
        }
        val productAndQuantity = input.replace("[", "").replace("]", "").split("-")

        require(items.any { it.name() == productAndQuantity[0]}) {
            "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."
        }
        val quantity = requireNotNull(productAndQuantity[1].toIntOrNull()) {
            "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."
        }

        require(quantity >= 1) {
            "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."
        }
        require(quantity<= items.filter { it.name() == productAndQuantity[0] }.sumOf { it.quantity() }) {
            "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."
        }

    }
}