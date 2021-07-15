package chapter1.section1

/**
 * 给出以下表达式的类型和值
 */
fun ex2(lambda: () -> Any) {
    val result = lambda()
    println("type=${result.javaClass} value=${result}")
}

fun main() {
    ex2 { (1 + 2.236) / 2 }
    ex2 { 1 + 2 + 3 + 4.0 }
    ex2 { 4.1 >= 4 }
    // kotlin中不支持整数加字符串，只支持字符串加整数
    ex2 { "3" + 2 + 1 }
}