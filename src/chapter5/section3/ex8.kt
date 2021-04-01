package chapter5.section3

/**
 * 为KMP类添加一个count()方法来统计模式字符串的在文本中的出现次数，
 * 再添加一个searchAll()方法来打印出所有出现的位置。
 */
fun KMP.searchAll(txt: String): IntArray {
    val list = ArrayList<Int>()
    val N = txt.length
    val M = pat.length
    var i = 0
    var j = 0
    while (i < N) {
        j = dfa[alphabet.toIndex(txt[i++])][j]
        if (j == M) {
            list.add(i - M)
            i = i - M + 1
            j = 0
        }
    }
    return list.toIntArray()
}

fun KMP.count(txt: String): Int {
    return searchAll(txt).size
}

fun main() {
    testSearchAll<KMP>(KMP::searchAll) { KMP(it) }
}