package capter1.exercise1_2

//检查两个字符串是否互为回环变位
//回环变位是指：a中的字符 循环移动 任意位置之后能够得到另一个字符串b，则a被称为b的回环变位
fun ex6(a: String, b: String): Boolean {
    return a.length == b.length && (a + a).indexOf(b) != -1
}

fun main() {
    val a = "ACTGACG"
    val b = "TGACGAC"
    println(ex6(a, b))
}