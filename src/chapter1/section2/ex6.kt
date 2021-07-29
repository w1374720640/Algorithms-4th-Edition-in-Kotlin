package chapter1.section2

/**
 * 如果字符串s中的字符循环移动任意位置之后能够得到另一个字符串t，那么s就被称为t的回环变位。
 * 例如，ACTGACG就是TGACGAC的一个回环变位，反之亦然。
 * 判定这个条件在基因组序列的研究中是很重要的。
 * 编写一个程序检查两个给定的字符串s和t是否互为回环变位。
 * 提示：答案只需要一行用到indexOf()、length()和字符串连接的代码。
 */
fun ex6(a: String, b: String): Boolean {
    return a.length == b.length && (a + a).indexOf(b) != -1
}

fun main() {
    val a = "ACTGACG"
    val b = "TGACGAC"
    println(ex6(a, b))
}