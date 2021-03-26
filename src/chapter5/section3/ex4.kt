package chapter5.section3

/**
 * 编写一个方法，接受一个字符串txt和一个整数M作为参数，返回字符串中M个连续的空格第一次出现的位置，
 * 如果不存在则返回txt.length。
 * 估计你的方法在一般的文本中和最坏情况下所需的字符比较次数。
 *
 * 解：添加一个计数器，遍历字符串，当遇见第一个空格时，每遇见一个空格计数器自增一，
 * 当计数器的值等于M时，函数返回，当遇见非空格时，清空计数器，继续寻找空格，
 * 需要比较的次数为N，N为从第一个字符到连续空格最后一个字符或字符串结尾的长度。
 */
fun ex4(txt: String, M: Int): Int {
    require(txt.isNotEmpty() && M > 0)
    var count = 0
    var i = 0
    while (i < txt.length) {
        if (txt[i] == ' ') {
            count++
            if (count == M) return i - M + 1
        } else {
            if (count != 0) {
                count = 0
            }
        }
        i++
    }
    return txt.length
}

fun main() {
    val txt = " A  A   AAA AA    A  "
    check(ex4(txt, 1) == 0)
    check(ex4(txt, 2) == 2)
    check(ex4(txt, 3) == 5)
    check(ex4(txt, 4) == 14)
    check(ex4(txt, 5) == txt.length)
    println("check succeed.")
}