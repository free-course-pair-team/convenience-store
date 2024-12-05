package store.util

fun <T>retryInput(input: () -> T): T {
    while (true) {
        try {
            return input()
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}