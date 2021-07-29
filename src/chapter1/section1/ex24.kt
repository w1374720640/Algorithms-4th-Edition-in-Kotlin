package chapter1.section1

/**
 * 给出使用欧几里得算法计算105和24的最大公约数的过程中得到的一系列p和q的值。
 * 扩展该算法中的代码得到一个程序Euclid，从命令行接受两个参数，计算它们的最大公约数并打印出每次调用递归方法时的两个参数。
 * 使用你的程序计算1111111和1234567的最大公约数
 *
 * 解：两个整数的最大公约数等于其中较小的那个数和两数相除余数的最大公约数
 */
fun ex24(a: Int, b: Int): Int {
    println("a=$a b=$b")
    require(a > 0 && b > 0)
    val large = if (a > b) a else b
    val small = if (a > b) b else a
    //商数
    val quotient = large / small
    //余数
    val remainder = large % small
    if (remainder == 0) return small
    return ex24(small, remainder)
}

fun main() {
    val a = 1111111
    val b = 1234567
    println("The greatest common divisor of ${a} and ${b} is ${ex24(a, b)}")
}