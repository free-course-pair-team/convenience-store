package store.util

import camp.nextstep.edu.missionutils.Console

fun <T>retryInput(input: () -> T): T {
    while (true) {
        try {
            return input()
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)
            Console.readLine() // 입력 버퍼에 남아 있는 개행문자 제거
        }
    }
}