package store

import store.domain.PromotionManager
import store.util.FileReader
import store.util.Validator
import store.view.InputView
import store.view.OutputView

fun main() {
    // TODO: 프로그램 구현

    val outputView = OutputView()
    val inputView = InputView()
    val validator = Validator()
    val promotionManager = PromotionManager()
    Controller(FileReader(), outputView, inputView, validator, promotionManager).run()
}
