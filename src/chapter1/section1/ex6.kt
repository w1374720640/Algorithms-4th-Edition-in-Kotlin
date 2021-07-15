package chapter1.section1

/**
 * 以下这段程序会打印出什么？
 */
fun main() {
    var f = 0
    var g = 1
    for (i in 0..15) {
        println(f)
        f = f + g
        g = f - g
    }
}