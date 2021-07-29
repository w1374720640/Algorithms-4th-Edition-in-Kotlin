package chapter1.section1

/**
 * 以下代码段会打印出什么结果
 */
fun main() {
    val a = IntArray(10)
    for (i in 0 until 10)
        a[i] = 9 - i
    for (i in 0 until 10)
        a[i] = a[a[i]]
    for (i in 0 until 10)
        println(a[i]) // 原文中是打印i，估计是印刷错误
}