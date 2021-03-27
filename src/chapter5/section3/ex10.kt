package chapter5.section3

/**
 * 为RabinKarp类添加一个count()方法来统计模式字符串的在文本中的出现次数，
 * 再添加一个searchAll()方法来打印出所有出现的位置。
 */
fun RabinKarp.searchAll(txt: String): IntArray {
    val list = ArrayList<Int>()
    val M = pat.length
    val N = txt.length
    if (M > N) return intArrayOf()
    val patHash = hash(pat, M)
    var txtHash = hash(txt, M)
    if (patHash == txtHash && check(0)) {
        list.add(0)
    }
    for (i in 0 until N - M) {
        // 先计算旧hash值排除第一个元素后的hash值
        txtHash = (txtHash + Q - RM * alphabet.toIndex(txt[i]) % Q) % Q
        // 再添加新元素重新计算hash值
        txtHash = (txtHash * alphabet.R() + alphabet.toIndex(txt[i + M])) % Q
        if (txtHash == patHash && check(i + 1)) {
            list.add(i + 1)
        }
    }
    return list.toIntArray()
}

fun RabinKarp.count(txt: String): Int {
    return searchAll(txt).size
}

fun main() {
    val pat = "AAA"
    val txt = "AABAAAAABABAAAA"
    val rabinKarp = RabinKarp(pat)
    check(rabinKarp.count(txt) == 5)
    check(rabinKarp.searchAll(txt).contentEquals(intArrayOf(3, 4, 5, 11, 12)))
    println("check succeed.")
}