package store.util

fun <T> retryInput(run: () -> T): T {
    while (true) {
        try {
            return run()
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }
}
