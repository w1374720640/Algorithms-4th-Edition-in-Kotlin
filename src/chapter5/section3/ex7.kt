package chapter5.section3

/**
 * 为暴力子字符串查找算法的实现添加一个count()方法，统计模式字符串在文本中出现次数，
 * 再添加一个searchAll()方法来打印出所有出现的位置。
 */
fun BruteForceSearch.searchAll(txt: String): IntArray {
    val list = ArrayList<Int>()
    val N = txt.length
    val M = pat.length
    var i = 0
    var j = 0
    while (i < N) {
        if (txt[i] == pat[j]) {
            i++
            j++
        } else {
            i = i - j + 1
            j = 0
        }
        if (j == M) {
            list.add(i - M)
            i = i - M + 1
            j = 0
        }
    }
    return list.toIntArray()
}

fun BruteForceSearch.count(txt: String): Int {
    return searchAll(txt).size
}

fun main() {
    val pat = "AAA"
    val txt = "AABAAAAABABAAAA"
    val bruteForceSearch = BruteForceSearch(pat)
    check(bruteForceSearch.count(txt) == 5)
    check(bruteForceSearch.searchAll(txt).contentEquals(intArrayOf(3, 4, 5, 11, 12)))
    println("check succeed.")
}