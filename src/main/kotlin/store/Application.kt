package store

import store.domain.Membership
import store.domain.PromotionManager
import store.util.FileReader
import store.util.Validator
import store.view.InputView
import store.view.OutputView

fun main() {
    // TODO: 1. 프로모션 기간 체크
    // TODO:

    val outputView = OutputView()
    val inputView = InputView()
    val validator = Validator()
    val promotionManager = PromotionManager()
    val membership = Membership()
    Controller(FileReader(), outputView, inputView, validator, promotionManager, membership).run()
}
