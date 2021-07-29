package chapter1.section1

/**
 * 给出ex16(6)的返回值
 * 返回的是String而不是Int
 */
fun ex16(n: Int): String {
    if (n <= 0) return ""
    return ex16(n - 3) + n + ex16(n - 2) + n
}

fun main() {
    println(ex16(6))
}