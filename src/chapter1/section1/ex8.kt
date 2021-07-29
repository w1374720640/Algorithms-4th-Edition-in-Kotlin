package chapter1.section1

/**
 * 下列语句会打印出什么结果？给出解释
 */
fun main() {
    println('b') // 打印字符b
    println('b' + 'c'.toInt()) // 将字符b和字符c都转换成int值，想加后再转换成字符
    println(('a' + 4).toChar()) // 打印字符a之后的第四个字符
}