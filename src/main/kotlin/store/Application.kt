package store

import store.model.ItemManager
import store.util.FileReader
import store.view.OutputView

fun main() {
    // TODO: 프로그램 구현

    val outputView = OutputView()
    Controller(FileReader(),outputView).run()

}
