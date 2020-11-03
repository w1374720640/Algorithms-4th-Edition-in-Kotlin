package chapter3.section4

/**
 * 对于字符串类型的键，考虑R=256和M=255的除留余数法的散列函数
 * 请证明这是一个糟糕的选择，因为任意排列的字母所得字符串得散列值均相同
 *
 * 解：hash = (256 * hash + char) % 255  ->  hash = hash % 255 + char % 255
 * 无论字母怎么排列，散列值为所有字符的和与255取余，和顺序无关
 */
fun ex23(string: String): Int {
    var hash = 0
    string.forEach {
        hash = (256 * hash + it.toInt()) % 255
    }
    return hash
}

fun main() {
    val array = arrayOf("abc", "acb", "bac", "bca", "cab", "cba")
    array.forEach {
        val hash = ex23(it)
        println("key=$it hash=$hash")
    }
    println(('a'.toInt() + 'b'.toInt() + 'c'.toInt()) % 255)
}